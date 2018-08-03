package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReceivingStationBusiness {

    private final ApplicationContext applicationContext;
    private final ReceiveStationRepository receivingStationRepository;

    @Autowired
    public ReceivingStationBusiness(ApplicationContext applicationContext,
                                    ReceiveStationRepository receivingStationRepository) {
        this.applicationContext = applicationContext;
        this.receivingStationRepository = receivingStationRepository;
    }

    public ReceiveStation getReceivingStation(String name) {
        String warehouse = applicationContext.getCurrentWarehouse();
        ReceiveStation receivingStation = Optional
                .ofNullable(receivingStationRepository.getByName(warehouse, name))
                .orElseThrow(() -> new ApiException(InBoundException
                        .EX_SCANNING_OBJECT_NOT_FOUND.toString(), name));
        // 判断工作站是否被删除
        if (!receivingStation.getEntityLock().equals(Constant.NOT_LOCKED)) {
            throw new ApiException(InBoundException
                    .EX_RECEIVING_STATION_HAS_DELETED.toString(), name);
        }
        return receivingStation;
    }
}
