package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
//import com.mushiny.wms.masterdata.general.domain.Container;
//import com.mushiny.wms.masterdata..ContainerRepository;
//import com.mushiny.wms.masterdata.obbasics.domain.PickingOrder;
//import com.mushiny.wms.masterdata.obbasics.domain.PickingUnitLoad;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
//import com.mushiny.wms.masterdata.obbasics.repository.PickingUnitLoadRepository;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PickingOrderBusiness {

//    private final PermissionsContext permissionsContext;
//    private final ContainerRepository containerRepository;
//    private final PickingUnitLoadRepository pickingUnitLoadRepository;
//
//    @Autowired
//    public PickingOrderBusiness(PermissionsContext permissionsContext,
//                                ContainerRepository containerRepository,
//                                PickingUnitLoadRepository pickingUnitLoadRepository) {
//        this.permissionsContext = permissionsContext;
//        this.containerRepository = containerRepository;
//        this.pickingUnitLoadRepository = pickingUnitLoadRepository;
//    }
//
//    public PickingOrder getByContainerName(String containerName) {
//        // 获取小车批次
//        Warehouse warehouse = permissionsContext.getCurrentWarehouse();
//        Container container = Optional
//                .ofNullable(containerRepository.getByName(warehouse, containerName))
//                .orElseThrow(() -> new ApiException(OutBoundException
//                        .EX_SCANNING_OBJECT_NOT_FOUND.toString(), containerName));
//        List<PickingUnitLoad> pickingUnitLoads = pickingUnitLoadRepository.getByContainer(container);
//        if (pickingUnitLoads.isEmpty()) {
//            throw new ApiException(OutBoundException.EX_PICKING_ORDER_NOT_FOUND.toString(), containerName);
//        }
//        if(pickingUnitLoads.get(0).getPickingOrder() == null){
//            throw new ApiException(OutBoundException.EX_PICKING_ORDER_NOT_FOUND.toString(),containerName);
//        }
//        return pickingUnitLoads.get(0).getPickingOrder();
//    }
}
