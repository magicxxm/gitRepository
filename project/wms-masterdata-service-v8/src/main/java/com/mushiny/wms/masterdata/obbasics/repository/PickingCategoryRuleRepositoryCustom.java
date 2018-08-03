package com.mushiny.wms.masterdata.obbasics.repository;

import com.mushiny.wms.masterdata.obbasics.domain.PickingCateGoryRule;

public interface PickingCategoryRuleRepositoryCustom {

    PickingCateGoryRule getByName(String name);

    boolean existsByName(String name);
}
