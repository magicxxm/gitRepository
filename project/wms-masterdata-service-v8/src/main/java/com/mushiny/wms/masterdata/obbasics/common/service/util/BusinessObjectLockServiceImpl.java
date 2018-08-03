package com.mushiny.wms.masterdata.obbasics.common.service.util;

import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.masterdata.general.domain.User;
import com.mushiny.wms.masterdata.obbasics.common.business.ContextService;
import com.mushiny.wms.masterdata.obbasics.common.exception.BusinessObjectSecurityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

@Service
public class BusinessObjectLockServiceImpl implements BusinessObjectLockService {

    private static final Logger log = LoggerFactory.getLogger(BusinessObjectLockServiceImpl.class);

    private final EntityManager manager;

    private final ContextService contextService;

    public BusinessObjectLockServiceImpl(EntityManager manager, ContextService contextService) {
        this.manager = manager;
        this.contextService = contextService;
    }

    @Override
    public void lock(BaseEntity entity, int lock, String lockCause) throws BusinessObjectSecurityException {
        String logStr = "lock ";
        User user = contextService.getCallersUser();

        if (!contextService.checkClient(entity)) {
            throw new BusinessObjectSecurityException(user);
        }
        entity = manager.find(entity.getClass(), entity.getId());
        int lockOld = entity.getEntityLock();
        entity.setEntityLock(lock);
        manager.merge(entity);

        log.info(logStr + "class=" + entity.getClass().getSimpleName() + ", entity=" + entity.toShortString() + ", lockOld=" + lockOld + ", lockNew=" + lock + ", user=" + contextService.getCallerUsername());
    }
}
