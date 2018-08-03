package com.mushiny.wms.masterdata.obbasics.business.pack;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.obbasics.business.PickingOrderBusiness;
//import com.mushiny.wms.masterdata.obbasics.crud.dto.CustomerShipmentDTO;
//import com.mushiny.wms.masterdata.obbasics.crud.mapper.CustomerShipmentMapper;
//import com.mushiny.wms.masterdata.obbasics.crud.mapper.CustomerShipmentPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.domain.enums.PackRequestState;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
//import com.mushiny.wms.masterdata.obbasics.repository.CustomerShipmentRepository;
//import com.mushiny.wms.masterdata.obbasics.repository.PackingRequestRepository;
//import com.mushiny.wms.masterdata.obbasics.repository.ReBinRequestPositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class PackSingleBusiness {

//    private final PickingOrderBusiness pickingOrderBusiness;
//    private final ReBinRequestPositionRepository reBinRequestPositionRepository;
//    private final CustomerShipmentRepository customerShipmentRepository;
//    private final PackingRequestRepository packingRequestRepository;
//    private final CustomerShipmentMapper customerShipmentMapper;
//    private final CustomerShipmentPositionMapper customerShipmentPositionMapper;
//
//    @Autowired
//    public PackSingleBusiness(PickingOrderBusiness pickingOrderBusiness,
//                              ReBinRequestPositionRepository reBinRequestPositionRepository,
//                              CustomerShipmentRepository customerShipmentRepository,
//                              CustomerShipmentMapper customerShipmentMapper,
//                              CustomerShipmentPositionMapper customerShipmentPositionMapper,
//                              PackingRequestRepository packingRequestRepository) {
//        this.pickingOrderBusiness = pickingOrderBusiness;
//        this.reBinRequestPositionRepository = reBinRequestPositionRepository;
//        this.customerShipmentRepository = customerShipmentRepository;
//        this.customerShipmentMapper = customerShipmentMapper;
//        this.customerShipmentPositionMapper = customerShipmentPositionMapper;
//        this.packingRequestRepository = packingRequestRepository;
//    }
//
//
//    public CustomerShipmentDTO getByItemNo(String containerName, String itemNo) {
//        // 获取小车批次
//        PickingOrder pickingOrder = pickingOrderBusiness.getByContainerName(containerName);
//        // 判断小车中的该SKU是否完成ReBin,以及商品的数量是否为1
//        List<ReBinRequestPosition> positions = reBinRequestPositionRepository
//                .getByPickingOrderAndItemNo(pickingOrder,itemNo);
//        if(positions.isEmpty()){
//            throw new ApiException(OutBoundException.EX_CONTAINER_NOT_FOUND_SHIPMENT.toString(),containerName);
//        }else if(positions.size() != 1){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//        // 判断订单是否为一单一件
//        CustomerShipment shipment = null;
//        for(ReBinRequestPosition position : positions){
//            CustomerShipmentPosition shipmentPosition = position.getCustomerShipmentPosition();
//            if(shipmentPosition == null){
//                throw new ApiException(OutBoundException.EX_CONTAINER_NOT_FOUND_SHIPMENT.toString(),containerName);
//            }
//            PackingRequest pack = packingRequestRepository.getByShipment(shipmentPosition.getCustomerShipment());
//            if(pack.getState().equalsIgnoreCase(PackRequestState.PACKED.toString())){
//                continue;
//            }
//            shipment = shipmentPosition.getCustomerShipment();
//            break;
//        }
//        if(shipment == null){
//            throw new ApiException(OutBoundException.EX_CONTAINER_NOT_FOUND_SHIPMENT.toString(),containerName);
//        }
//        isSinglePosition(shipment);
//        return buildCustomerShipmentDTO(shipment);
//    }
//
//    public CustomerShipmentDTO getByShipmentNo(String shipmentNo){
//        // 判断订单是否为一单一件
//        CustomerShipment shipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
//        if(shipment == null){
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(),shipmentNo);
//        }
//        isSinglePosition(shipment);
//        // 判断订单否完成ReBin,以及商品的数量是否为1
//        List<ReBinRequestPosition> reBinPositions = reBinRequestPositionRepository.getByCustomerShipment(shipment);
//        if(reBinPositions.isEmpty()){
//            throw new ApiException(OutBoundException.EX_SHIPMENT_NOT_IS_REBINED.toString(),shipmentNo);
//        }
//        if(reBinPositions.size() != 1){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//        return buildCustomerShipmentDTO(shipment);
//    }
//
//    public List<CustomerShipmentDTO> getByContainerName(String containerName) {
//        // 获取小车批次
//        PickingOrder pickingOrder = pickingOrderBusiness.getByContainerName(containerName);
//        // 判断小车中的该SKU是否完成ReBin,以及商品的数量是否为1
//        List<ReBinRequestPosition> positions = reBinRequestPositionRepository.getByPickingOrder(pickingOrder);
//        if(positions.isEmpty()){
//            throw new ApiException(OutBoundException.EX_CONTAINER_NOT_FOUND_SHIPMENT.toString(),containerName);
//        }
//        List<CustomerShipmentDTO> dtoList = new ArrayList<>();
//        for(ReBinRequestPosition position : positions){
//            // 判断订单是否为一单一件
//            CustomerShipmentPosition shipmentPosition = position.getCustomerShipmentPosition();
//            if(shipmentPosition == null){
//                throw new ApiException(OutBoundException.EX_CONTAINER_NOT_FOUND_SHIPMENT.toString(),containerName);
//            }
//            CustomerShipment shipment = shipmentPosition.getCustomerShipment();
//            isSinglePosition(shipment);
//            dtoList.add(buildCustomerShipmentDTO(shipment));
//        }
//        return dtoList;
//    }
//
//    private void isSinglePosition(CustomerShipment shipment){
//        if(shipment.getPositions().size() != 1 ||
//                shipment.getPositions().get(0).getAmount().compareTo(BigDecimal.ONE) != 0){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//    }
//
//    private CustomerShipmentDTO buildCustomerShipmentDTO(CustomerShipment shipment){
//        CustomerShipmentDTO dto = customerShipmentMapper.toDTO(shipment);
//        dto.getPositions().addAll(customerShipmentPositionMapper.toDTOList(shipment.getPositions()));
//        return dto;
//    }
}
