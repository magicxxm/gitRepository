package com.mushiny.wms.masterdata.obbasics.common.service.util;


import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.obbasics.common.exception.BusinessObjectSecurityException;

public interface BusinessObjectLockService {

    void lock(BaseEntity entity, int lock, String lockCause) throws BusinessObjectSecurityException;
}
