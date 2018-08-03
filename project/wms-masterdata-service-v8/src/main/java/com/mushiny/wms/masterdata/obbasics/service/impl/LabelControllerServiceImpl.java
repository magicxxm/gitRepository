package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.obbasics.crud.dto.LabelControllerDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.LabelControllerMapper;
import com.mushiny.wms.masterdata.obbasics.domain.DigitalLabel;
import com.mushiny.wms.masterdata.obbasics.domain.LabelController;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import com.mushiny.wms.masterdata.obbasics.repository.LabelControllerRepository;
import com.mushiny.wms.masterdata.obbasics.service.LabelControllerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LabelControllerServiceImpl implements LabelControllerService {

    private final LabelControllerRepository labelControllerRepository;
    private final ApplicationContext applicationContext;
    private final LabelControllerMapper labelControllerMapper;
    private final DigitalLabelRepository digitalLabelRepository;

    public LabelControllerServiceImpl(LabelControllerRepository labelControllerRepository,
                                      ApplicationContext applicationContext,
                                      LabelControllerMapper labelControllerMapper,
                                      DigitalLabelRepository digitalLabelRepository) {
        this.labelControllerRepository = labelControllerRepository;
        this.applicationContext = applicationContext;
        this.labelControllerMapper = labelControllerMapper;
        this.digitalLabelRepository = digitalLabelRepository;
    }

    @Override
    public LabelControllerDTO create(LabelControllerDTO dto) {
        return  null;
    }

    @Override
    public void createMore(LabelControllerDTO dto) {
        LabelController entity = labelControllerMapper.toEntity(dto);
        checkLabelControllerName(entity.getWarehouseId(), entity.getName());

        LabelController labelController = new LabelController();
        labelController.setName(dto.getName());
        labelController.setAddressIp(dto.getAddressIp());
        labelController.setPortNumber(dto.getPortNumber());
        labelController.setWarehouseId(applicationContext.getCurrentWarehouse());
        labelController = labelControllerRepository.save(labelController);
        int sizeNumber = 0;
        if(dto.getSize() > 0 && dto.getSize() <= 9) {
            sizeNumber = 1;
        }else if(dto.getSize() > 9 && dto.getSize() <= 99) {
            sizeNumber = 2;
        }else if(dto.getSize() > 99 && dto.getSize() <= 999) {
            sizeNumber = 3;
        }else {
            sizeNumber = 4;
        }

        if(dto.getSize() != 0 && dto.getSize() >= 1){
            int numOrder = 1;
            for(int x = 1; x <= dto.getSize(); x++) {
                DigitalLabel digitalLabel = new DigitalLabel();
                digitalLabel.setName(dto.getName() + "-" + String.format("%0" + sizeNumber + "d", x));
                digitalLabel.setNumOrder(numOrder);
                numOrder++;
                digitalLabel.setLabelController(labelController);
                digitalLabel.setWarehouseId(applicationContext.getCurrentWarehouse());
                digitalLabelRepository.save(digitalLabel);
            }
        }
    }

    @Override
    public void delete(String id) {
        LabelController entity = labelControllerRepository.retrieve(id);
        labelControllerRepository.delete(entity);
    }

    @Override
    public LabelControllerDTO update(LabelControllerDTO dto) {
        LabelController entity = labelControllerRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkLabelControllerName(entity.getWarehouseId(), dto.getName());
        }
        labelControllerMapper.updateEntityFromDTO(dto, entity);
        return labelControllerMapper.toDTO(labelControllerRepository.save(entity));
    }


    @Override
    public LabelControllerDTO retrieve(String id) {
        return labelControllerMapper.toDTO(labelControllerRepository.retrieve(id));
    }

    @Override
    public List<LabelControllerDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<LabelController> entities = labelControllerRepository.getNotLockList(null, sort);
        return labelControllerMapper.toDTOList(entities);
    }

    @Override
    public List<LabelControllerDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<LabelController> entities = labelControllerRepository.getBySearchTerm(searchTerm, sort);
        return labelControllerMapper.toDTOList(entities);
    }

    @Override
    public Page<LabelControllerDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if(pageable.getSort()==null){
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<LabelController> entities = labelControllerRepository.getBySearchTerm(searchTerm, pageable);
        return labelControllerMapper.toDTOPage(pageable, entities);
    }

    private void checkLabelControllerName(String warehouse, String name) {
        LabelController labelController = labelControllerRepository.getByName(warehouse, name);
        if (labelController != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_LABEL_CONTROLLER_NAME_UNIQUE.toString(), name);
        }
    }
}
