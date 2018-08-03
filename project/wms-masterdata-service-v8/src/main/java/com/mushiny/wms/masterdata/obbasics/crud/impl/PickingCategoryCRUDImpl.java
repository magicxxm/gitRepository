package com.mushiny.wms.masterdata.obbasics.crud.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.general.repository.WarehouseRepository;
import com.mushiny.wms.masterdata.obbasics.business.PickingCategoryGenerator;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.crud.BaseCRUDImpl;
import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;
import com.mushiny.wms.masterdata.obbasics.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.common.util.StringTools;
import com.mushiny.wms.masterdata.obbasics.crud.PickingCategoryCRUD;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryPositionDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickingCateGoryMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickingCateGoryPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class PickingCategoryCRUDImpl extends BaseCRUDImpl<PickingCateGory, PickingCateGoryDTO> implements PickingCategoryCRUD {

    private final ContextService contextService;

    private final WarehouseRepository warehouseRepository;

    private final ClientRepository clientRepository;

    private final PickingCategoryGenerator pickingCategoryGenerator;

    private final PickingCateGoryMapper pickingCateGoryMapper;

    private final PickingCateGoryPositionMapper pickingCateGoryPositionMapper;

    private final PickingCateGoryRepository pickingCategoryRepository;

    private final PickingCateGoryPositionRepository pickingCategoryPositionRepository;

    private final ApplicationContext applicationContext;

    public PickingCategoryCRUDImpl(BaseService<PickingCateGory> baseService,
                                   BaseMapper<PickingCateGory, PickingCateGoryDTO> baseMapper,
                                   PickingCategoryGenerator pickingCategoryGenerator,
                                   WarehouseRepository warehouseRepository,
                                   ClientRepository clientRepository,
                                   ContextService contextService,
                                   PickingCateGoryMapper pickingCateGoryMapper,
                                   PickingCateGoryPositionMapper pickingCateGoryPositionMapper,
                                   PickingCateGoryRepository pickingCategoryRepository,
                                   PickingCateGoryPositionRepository pickingCategoryPositionRepository,
                                   ApplicationContext applicationContext) {
        super(baseService, baseMapper);
        this.pickingCategoryGenerator = pickingCategoryGenerator;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.contextService = contextService;
        this.pickingCateGoryMapper = pickingCateGoryMapper;
        this.pickingCateGoryPositionMapper = pickingCateGoryPositionMapper;
        this.pickingCategoryRepository = pickingCategoryRepository;
        this.pickingCategoryPositionRepository = pickingCategoryPositionRepository;
        this.applicationContext = applicationContext;
    }

    @Override
    public PickingCateGoryDTO create(PickingCateGoryDTO categoryDTO) throws FacadeException {
//        Warehouse warehouse = contextService.getCallersWarehouse();
        String warehouse = warehouseRepository.retrieve(applicationContext.getCurrentWarehouse()).getId();
        Client client = null;
        if (StringTools.isEmpty(categoryDTO.getClientId())) {
            client = contextService.getCallersClient();
        } else {
            client = clientRepository.findOne(categoryDTO.getClientId());
        }

        PickingCateGory category = pickingCateGoryMapper.mapDTOIntoEntity(categoryDTO);
        PickingCateGory created = pickingCategoryGenerator.createPickingCategory(warehouse, categoryDTO.getClientId(), category.getName(), category.getProcessPath());
        for (PickingCateGoryPosition position : category.getPositions()) {
            pickingCategoryGenerator.addPickingCategoryPosition(created, position.getPickingCateGoryRule(), position.getOperator(), position.getValue());
        }
        return pickingCateGoryMapper.mapEntityIntoDTO(created);
    }

    @Override
    public PickingCateGoryDTO update(PickingCateGoryDTO categoryDTO) throws FacadeException {
        Warehouse warehouse = contextService.getCallersWarehouse();
        Client client = null;
        if (StringTools.isEmpty(categoryDTO.getClientId())) {
            client = contextService.getCallersClient();
        } else {
            client = clientRepository.findOne(categoryDTO.getId());
        }

        PickingCateGory updated = pickingCategoryRepository.findOne(categoryDTO.getId());
        updated.getPositions().clear();
        pickingCateGoryMapper.updateEntityFromDTO(categoryDTO, updated);
        for (PickingCateGoryPositionDTO positionDTO : categoryDTO.getPositions()) {
            PickingCateGoryPosition position = null;
            /*if (positionDTO.getId() != null) {
                position = pickingCategoryPositionRepository.findOne(positionDTO.getId());
                pickingCateGoryPositionMapper.updateEntityFromDTO(positionDTO, position);
                updated.getPositions().add(position);
            } else {*/
                position = pickingCateGoryPositionMapper.mapDTOIntoEntity(positionDTO);
                updated = pickingCategoryGenerator.addPickingCategoryPosition(updated, position.getPickingCateGoryRule(), position.getOperator(), position.getValue());
//            }
        }
        return pickingCateGoryMapper.mapEntityIntoDTO(updated);
    }
}
