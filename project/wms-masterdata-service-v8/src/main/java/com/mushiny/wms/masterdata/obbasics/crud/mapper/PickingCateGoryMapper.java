package com.mushiny.wms.masterdata.obbasics.crud.mapper;


import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGory;
import com.mushiny.wms.masterdata.obbasics.domain.ProcessPath;
import com.mushiny.wms.masterdata.obbasics.repository.ProcessPathRepository;
import org.springframework.stereotype.Component;

@Component
public class PickingCateGoryMapper implements BaseMapper<PickingCateGory, PickingCateGoryDTO> {

    private final ProcessPathMapper processPathMapper;

    private final PickingCateGoryPositionMapper pickingCateGoryPositionMapper;

    private final ProcessPathRepository processPathRepository;

    public PickingCateGoryMapper(ProcessPathMapper processPathMapper,
                                 PickingCateGoryPositionMapper pickingCateGoryPositionMapper,
                                 ProcessPathRepository processPathRepository) {
        this.processPathMapper = processPathMapper;
        this.pickingCateGoryPositionMapper = pickingCateGoryPositionMapper;
        this.processPathRepository = processPathRepository;
    }

    @Override
    public PickingCateGoryDTO mapEntityIntoDTO(PickingCateGory category) {
        if (category == null) {
            return null;
        }

        PickingCateGoryDTO categoryDTO = new PickingCateGoryDTO(category);
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());
        categoryDTO.setOrderIndex(category.getIndex());
        categoryDTO.setProcessPath(processPathMapper.toDTO(category.getProcessPath()));
        categoryDTO.setPositions(pickingCateGoryPositionMapper.mapEntitiesIntoDTOs(category.getPositions()));
        categoryDTO.setClientId(category.getClientId());
        return categoryDTO;
    }

    @Override
    public PickingCateGory mapDTOIntoEntity(PickingCateGoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }

        PickingCateGory category = new PickingCateGory();
        categoryDTO.merge(category);
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIndex(categoryDTO.getOrderIndex());
        category.setProcessPath(processPathFromId(categoryDTO.getProcessPathId()));
        category.setPositions(pickingCateGoryPositionMapper.mapDTOsIntoEntities(categoryDTO.getPositions()));
        return category;
    }

    @Override
    public void updateEntityFromDTO(PickingCateGoryDTO categoryDTO, PickingCateGory category) {
        categoryDTO.merge(category);
        category.setProcessPath(processPathRepository.findOne(categoryDTO.getProcessPathId()));
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setIndex(categoryDTO.getOrderIndex());
    }

    private ProcessPath processPathFromId(String id) {
        if (id == null) {
            return null;
        }
        ProcessPath processPath = new ProcessPath();
        processPath.setId(id);
        return processPath;
    }
}
