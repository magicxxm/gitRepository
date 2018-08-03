package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.business.EntityGenerator;
import com.mushiny.wms.masterdata.obbasics.common.service.BaseServiceImpl;
import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;
import com.mushiny.wms.masterdata.obbasics.repository.PickingCateGoryRuleRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickingCateGoryRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PickingCateGoryRuleServiceImpl extends BaseServiceImpl<PickingCateGoryRule> implements PickingCateGoryRuleService {

    private final Logger log = LoggerFactory.getLogger(PickingCateGoryRuleServiceImpl.class);

    private final PickingCateGoryRuleRepository pickingCategoryRuleRepository;

    private final ApplicationContext applicationContext;

    public PickingCateGoryRuleServiceImpl(ContextService context,
                                          EntityGenerator entityGenerator,
                                          BaseRepository<PickingCateGoryRule, String> baseRepository,
                                          PickingCateGoryRuleRepository pickingCategoryRuleRepository,
                                          ApplicationContext applicationContext) {
        super(context, applicationContext, entityGenerator, baseRepository);
        this.pickingCategoryRuleRepository = pickingCategoryRuleRepository;
        this.applicationContext = applicationContext;
    }

}
