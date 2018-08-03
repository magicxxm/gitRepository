package com.mushiny.wms.masterdata.obbasics.crud.mapper;

import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ZoneRepository;
import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryRuleDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRulePosition;
import com.mushiny.wms.masterdata.obbasics.repository.BoxTypeRepository;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class PickingCateGoryRuleMapper implements BaseMapper<PickingCateGoryRule, PickingCateGoryRuleDTO> {

    private final BoxTypeRepository boxTypeRepository;

    private final ItemGroupRepository itemGroupRepository;

    private final ZoneRepository zoneRepository;

    public PickingCateGoryRuleMapper(BoxTypeRepository boxTypeRepository, ItemGroupRepository itemGroupRepository, ZoneRepository zoneRepository) {
        this.boxTypeRepository = boxTypeRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public PickingCateGoryRuleDTO mapEntityIntoDTO(PickingCateGoryRule rule) {
        if (rule == null) {
            return null;
        }

        PickingCateGoryRuleDTO ruleDTO = new PickingCateGoryRuleDTO(rule);
        ruleDTO.setKey(rule.getKey());
        ruleDTO.setName(rule.getName());
        ruleDTO.setComparisonType(rule.getComparisonType());
        ruleDTO.setOperators(rule.getPositions().stream().map(PickingCateGoryRulePosition::getOperator).collect(Collectors.toList()));
//        if (rule.getKey().equals(PickingRuleKey.BOX_TYPE)) {
//        }
        return ruleDTO;
    }

    @Override
    public PickingCateGoryRule mapDTOIntoEntity(PickingCateGoryRuleDTO ruleDTO) {
        if (ruleDTO == null) {
            return null;
        }

        PickingCateGoryRule rule = new PickingCateGoryRule();
        ruleDTO.merge(rule);
        rule.setKey(ruleDTO.getKey());
        rule.setName(ruleDTO.getName());
        rule.setComparisonType(ruleDTO.getComparisonType());
        return rule;
    }

    @Override
    public void updateEntityFromDTO(PickingCateGoryRuleDTO ruleDTO, PickingCateGoryRule rule) {
        ruleDTO.merge(rule);
        rule.setKey(ruleDTO.getKey());
        rule.setName(ruleDTO.getName());
        rule.setComparisonType(ruleDTO.getComparisonType());
    }
}
