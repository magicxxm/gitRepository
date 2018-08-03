package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.domain.Client;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.general.repository.WarehouseRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveCategoryRuleDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveCategoryRule;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.ReceivingCategoryRuleZoneType;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemGroupMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemGroup;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ReceiveCategoryRuleMapper implements BaseMapper<ReceiveCategoryRuleDTO, ReceiveCategoryRule> {

    private final ItemGroupRepository itemGroupRepository;
    private final ApplicationContext applicationContext;
    private final ClientMapper clientMapper;
    private final ItemGroupMapper itemGroupMapper;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public ReceiveCategoryRuleMapper(ItemGroupRepository itemGroupRepository,
                                     ApplicationContext applicationContext,
                                     ClientMapper clientMapper,
                                     ItemGroupMapper itemGroupMapper,
                                     UserRepository userRepository,
                                     WarehouseRepository warehouseRepository) {
        this.itemGroupRepository = itemGroupRepository;
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.itemGroupMapper = itemGroupMapper;
        this.userRepository = userRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public ReceiveCategoryRuleDTO toDTO(ReceiveCategoryRule entity) {
        if (entity == null) {
            return null;
        }

        ReceiveCategoryRuleDTO dto = new ReceiveCategoryRuleDTO(entity);

        dto.setName(entity.getName());

        List<String> operators = new ArrayList<>();
        Collections.addAll(operators, entity.getOperator().split(","));
        dto.setOperators(operators);

        dto.setDecisionKey(entity.getDecisionKey());
        dto.setComparisonType(entity.getComparisonType());
        dto.setCompKey(entity.getCompKey());

        dto.setSelectList(getSelectList(entity.getDecisionKey(), entity.getComparisonType()));

        return dto;
    }

    @Override
    public ReceiveCategoryRule toEntity(ReceiveCategoryRuleDTO dto) {
        if (dto == null) {
            return null;
        }
        ReceiveCategoryRule entity = new ReceiveCategoryRule();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ReceiveCategoryRuleDTO dto, ReceiveCategoryRule entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setName(dto.getName());
    }

    private List<?> getSelectList(String decisionKey, String comparisonType) {
        if (comparisonType.equalsIgnoreCase("VALUE_FROM_CONTEXT")) {
            Sort sort;
            switch (decisionKey) {
                case "FLAG":
                    sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
                    List<ItemGroup> itemGroups = itemGroupRepository.getList(null, sort);
                    return itemGroupMapper.toDTOList(itemGroups);
                case "ZONE_TYPE":
                    List<String> zoneTypes = new ArrayList<>();
                    zoneTypes.add(ReceivingCategoryRuleZoneType.BUFFER_ZONE.toString());
                    zoneTypes.add(ReceivingCategoryRuleZoneType.PICKING_ZONE.toString());
                    return zoneTypes;
                case "CLIENT":
                    List<Client> clients = new ArrayList<>();
//                    User user = applicationContext.getCurrentUser();
                    User user = userRepository.retrieve(applicationContext.getCurrentUser());
                    if (user.getClient().isSystemClient()) {
                        Warehouse warehouse = warehouseRepository.retrieve(applicationContext.getCurrentWarehouse());
//                        clients.addAll(warehouse.getClients());
                    } else {
                        clients.add(user.getClient());
                    }
                    return clientMapper.toDTOList(clients);
            }
        }
        return null;
    }
}
