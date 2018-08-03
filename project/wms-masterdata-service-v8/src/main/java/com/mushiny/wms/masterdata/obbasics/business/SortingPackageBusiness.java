package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.exception.ApiException;
//import com.mushiny.wms.context.PermissionsContext;
//import com.mushiny.wms.masterdata.domain.Container;
//import com.mushiny.wms.masterdata.repository.ContainerRepository;
//import com.mushiny.wms.masterdata.obbasics.domain.CustomerShipment;
//import com.mushiny.wms.masterdata.obbasics.domain.PackingRequest;
//import com.mushiny.wms.masterdata.obbasics.domain.SortingRequest;
//import com.mushiny.wms.masterdata.obbasics.domain.SortingRequestPosition;
import com.mushiny.wms.masterdata.obbasics.domain.enums.PackRequestState;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
//import com.mushiny.wms.masterdata.obbasics.repository.CustomerShipmentRepository;
//import com.mushiny.wms.masterdata.obbasics.repository.PackingRequestRepository;
//import com.mushiny.wms.masterdata.obbasics.repository.SortingRequestPositionRepository;
//import com.mushiny.wms.masterdata.obbasics.repository.SortingRequestRepository;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SortingPackageBusiness {
//
//    private final PermissionsContext permissionsContext;
//    private final ContainerRepository containerRepository;
//    private final CustomerShipmentRepository customerShipmentRepository;
//    private final PackingRequestRepository packingRequestRepository;
//    private final SortingRequestRepository sortingRequestRepository;
//    private final SortingRequestPositionRepository sortingRequestPositionRepository;
//
//
//    @Autowired
//    public SortingPackageBusiness(PermissionsContext permissionsContext,
//                                     PackingRequestRepository packingRequestRepository,
//                                     SortingRequestRepository sortingRequestRepository,
//                                     SortingRequestPositionRepository sortingRequestPositionRepository,
//                                     ContainerRepository containerRepository,
//                                     CustomerShipmentRepository customerShipmentRepository) {
//        this.permissionsContext = permissionsContext;
//        this.packingRequestRepository = packingRequestRepository;
//        this.sortingRequestRepository = sortingRequestRepository;
//        this.sortingRequestPositionRepository = sortingRequestPositionRepository;
//        this.containerRepository = containerRepository;
//        this.customerShipmentRepository = customerShipmentRepository;
//    }
//
//    public void checkSortCodeUsed(String sortCode){
//        // 检查SortCode是否被绑定
//        SortingRequest sortingRequest = sortingRequestRepository.getBySortCode(sortCode);
//        if(sortingRequest != null){
//            throw new ApiException(OutBoundException
//                    .EX_SORTING_SORT_CODE_HAS_USED.toString(),sortCode);
//        }
//    }
//
//    public Container getNotUsedContainer(String containerName){
//        Warehouse warehouse = permissionsContext.getCurrentWarehouse();
//        Container container = containerRepository.getByName(warehouse,containerName);
//        if(container == null){
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(),containerName);
//        }
//        SortingRequest sortingRequest = sortingRequestRepository.getByContainer(container);
//        if(sortingRequest != null){
//            throw new ApiException(OutBoundException
//                    .EX_SORTING_CONTAINER_HAS_USED.toString(),container.getName());
//        }
//        return container;
//    }
//
//    public SortingRequest getUsedContainer(String containerName) {
//        // 检查小车是否被使用
//        Warehouse warehouse = permissionsContext.getCurrentWarehouse();
//        Container container = containerRepository.getByName(warehouse, containerName);
//        if (container == null) {
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(), containerName);
//        }
//        SortingRequest sortingRequest = sortingRequestRepository.getByContainer(container);
//        if (sortingRequest == null) {
//            throw new ApiException(OutBoundException.EX_SORTING_CONTAINER_NOT_FOUND.toString());
//        }
//        return sortingRequest;
//    }
//
//    public CustomerShipment getAndCheckShipment(String shipmentNo){
//        // 检查订单是否存在
//        CustomerShipment shipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
//        if(shipment == null){
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(),shipmentNo);
//        }
//        // 检查订单是否完成包装
//        PackingRequest packingRequest = packingRequestRepository.getByShipment(shipment);
//        if(packingRequest == null ||
//                !(packingRequest.getState().equalsIgnoreCase(PackRequestState.PACKED.toString()))){
//            throw new ApiException(OutBoundException.EX_SORTING_SHIPMENT_NOT_PACKED.toString(),shipmentNo);
//        }
//        // 检查订单是否完成分拣
//        SortingRequestPosition position = sortingRequestPositionRepository.getByShipment(shipment);
//        if(position != null){
//            throw new ApiException(OutBoundException.EX_SORTING_SHIPMENT_HAS_SORTED.toString(),
//                    shipmentNo,position.getSortingRequest().getContainer().getName());
//        }
//        return shipment;
//    }
}
