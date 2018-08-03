package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.common.UnitLoad;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.common.UnitLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class UnitLoadBusiness {

    private final UnitLoadRepository unitLoadRepository;

    @Autowired
    public UnitLoadBusiness(UnitLoadRepository unitLoadRepository) {
        this.unitLoadRepository = unitLoadRepository;
    }

    public UnitLoad getByStorageLocation(StorageLocation storageLocation) {
        List<UnitLoad> unitLoads = unitLoadRepository.getByStorageLocation(storageLocation);
        if (!unitLoads.isEmpty()) {
            UnitLoad unitLoad=unitLoads.get(0);
            if (Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)) {
                throw new ApiException(OutBoundProblemException
                        .EX_IT_UNITLOAD_IS_LOCKED.getName(), storageLocation.getName());
            } else if (Objects.equals(unitLoad.getEntityLock(), Constant.NOT_LOCKED)) {
                return unitLoad;
            }
        }
        return null;
    }
}
