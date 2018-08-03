package com.mushiny.wms.masterdata.obbasics.repository;


import com.mushiny.wms.masterdata.obbasics.common.domain.Resource;

public interface ResourceRepositoryCustom {

    Resource getByKey(String key, String locale);
}
