package com.mushiny.wms.internaltool.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.internaltool.common.domain.StorageLocation;
import com.mushiny.wms.internaltool.common.domain.UnitLoad;
import com.mushiny.wms.internaltool.common.repository.UnitLoadRepository;
import com.mushiny.wms.internaltool.exception.InternalToolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UnitLoadBusiness {

    private final UnitLoadRepository unitLoadRepository;

    @Autowired
    public UnitLoadBusiness(UnitLoadRepository unitLoadRepository) {
        this.unitLoadRepository = unitLoadRepository;
    }

    public UnitLoad getByStorageLocation(StorageLocation storageLocation) {
        UnitLoad unitLoad = unitLoadRepository.getByStorageLocation(storageLocation);
        if (unitLoad != null) {
            if (Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)) {
                throw new ApiException(InternalToolException
                        .EX_IT_UNITLOAD_IS_LOCKED.getName(), storageLocation.getName());
            } else if (Objects.equals(unitLoad.getEntityLock(), Constant.NOT_LOCKED)) {
                return unitLoad;
            }
        }
        return null;
    }
}
