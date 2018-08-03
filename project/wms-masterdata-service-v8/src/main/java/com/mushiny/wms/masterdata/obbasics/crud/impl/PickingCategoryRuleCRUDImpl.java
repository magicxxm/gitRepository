package com.mushiny.wms.masterdata.obbasics.crud.impl;

import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.crud.BaseCRUDImpl;
import com.mushiny.wms.masterdata.obbasics.common.crud.mapper.BaseMapper;
import com.mushiny.wms.masterdata.obbasics.common.service.BaseService;
import com.mushiny.wms.masterdata.obbasics.crud.PickingCategoryRuleCRUD;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickingCateGoryRuleDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class PickingCategoryRuleCRUDImpl extends BaseCRUDImpl<PickingCateGoryRule, PickingCateGoryRuleDTO> implements PickingCategoryRuleCRUD {

    private final ContextService contextService;

    public PickingCategoryRuleCRUDImpl(BaseService<PickingCateGoryRule> baseService,
                                       BaseMapper<PickingCateGoryRule, PickingCateGoryRuleDTO> baseMapper,
                                       ContextService contextService) {
        super(baseService, baseMapper);
        this.contextService = contextService;
    }
}
