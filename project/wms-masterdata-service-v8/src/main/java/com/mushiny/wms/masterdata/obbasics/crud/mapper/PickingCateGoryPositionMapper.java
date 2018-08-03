package com.mushiny.wms.masterdata.obbasics.crud.mapper;


import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryPositionDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryPosition;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import org.springframework.stereotype.Component;

@Component
public class PickingCateGoryPositionMapper implements BaseMapper<PickingCateGoryPosition, PickingCateGoryPositionDTO> {

    private final PickingCateGoryRuleMapper pickingCateGoryRuleMapper;

    public PickingCateGoryPositionMapper(PickingCateGoryRuleMapper pickingCateGoryRuleMapper) {
        this.pickingCateGoryRuleMapper = pickingCateGoryRuleMapper;
    }

    @Override
    public PickingCateGoryPositionDTO mapEntityIntoDTO(PickingCateGoryPosition position) {
        if (position == null) {
            return null;
        }

        PickingCateGoryPositionDTO positionDTO = new PickingCateGoryPositionDTO(position);
        positionDTO.merge(position);
        positionDTO.setRule(pickingCateGoryRuleMapper.mapEntityIntoDTO(position.getPickingCateGoryRule()));
        positionDTO.setOperator(position.getOperator());
        positionDTO.setValue(position.getValue());
        return positionDTO;
    }

    @Override
    public PickingCateGoryPosition mapDTOIntoEntity(PickingCateGoryPositionDTO positionDTO) {
        if (positionDTO == null) {
            return null;
        }

        PickingCateGoryPosition position = new PickingCateGoryPosition();
//        positionDTO.merge(position);
        position.setPickingCateGoryRule(ruleFromId(positionDTO.getRuleId()));
        position.setOperator(positionDTO.getOperator());
        position.setValue(positionDTO.getValue());
        return position;
    }

    @Override
    public void updateEntityFromDTO(PickingCateGoryPositionDTO positionDTO, PickingCateGoryPosition position) {
        positionDTO.merge(position);
        position.setPickingCateGoryRule(ruleFromId(positionDTO.getRuleId()));
        position.setOperator(positionDTO.getOperator());
        position.setValue(positionDTO.getValue());
    }

    private PickingCateGoryRule ruleFromId(String id) {
        if (id == null) {
            return null;
        }
        PickingCateGoryRule rule = new PickingCateGoryRule();
        rule.setId(id);
        return rule;
    }
}
