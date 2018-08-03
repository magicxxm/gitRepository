package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
//import com.mushiny.wms.masterdata.obbasics.business.dto.PackReBinCellDTO;
//import com.mushiny.wms.masterdata.obbasics.business.dto.PackShipmentPositionDTO;
//import com.mushiny.wms.masterdata.obbasics.business.pack.PackMultiBusiness;
//import com.mushiny.wms.masterdata.obbasics.business.pack.PackSingleBusiness;
//import com.mushiny.wms.masterdata.obbasics.business.pack.PackingRequestBusiness;
//import com.mushiny.wms.masterdata.obbasics.crud.dto.CustomerShipmentDTO;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.domain.enums.PackRequestState;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PackBusiness {

//    private final PackMultiBusiness packMultiBusiness;
//    private final PackSingleBusiness packSingleBusiness;
//    private final PackingRequestBusiness packingRequestBusiness;
//    private final ItemDataRepository itemDataRepository;
//    private final PackingStationRepository packingStationRepository;
//    private final CustomerShipmentRepository customerShipmentRepository;
//    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
//    private final ReBinRequestPositionRepository reBinRequestPositionRepository;
//    private final PackingRequestRepository packingRequestRepository;

//    @Autowired
//    public PackBusiness(PackMultiBusiness packMultiBusiness,
//                        PackSingleBusiness packSingleBusiness,
//                        PackingRequestBusiness packingRequestBusiness,
//                        ItemDataRepository itemDataRepository,
//                        PackingStationRepository packingStationRepository,
//                        CustomerShipmentRepository customerShipmentRepository,
//                        CustomerShipmentPositionRepository customerShipmentPositionRepository,
//                        ReBinRequestPositionRepository reBinRequestPositionRepository,
//                        PackingRequestRepository packingRequestRepository) {
//        this.packMultiBusiness = packMultiBusiness;
//        this.packSingleBusiness = packSingleBusiness;
//        this.packingRequestBusiness = packingRequestBusiness;
//        this.itemDataRepository = itemDataRepository;
//        this.packingStationRepository = packingStationRepository;
//        this.customerShipmentRepository = customerShipmentRepository;
//        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
//        this.reBinRequestPositionRepository = reBinRequestPositionRepository;
//        this.packingRequestRepository = packingRequestRepository;
//    }
//
//    public void packing(PackShipmentPositionDTO packShipmentPosition) {
//        PackingStation packingStation = packingStationRepository.retrieve(packShipmentPosition.getPackingStationId());
//        CustomerShipmentPosition shipmentPosition = customerShipmentPositionRepository
//                .retrieve(packShipmentPosition.getShipmentPositionId());
//        CustomerShipment shipment = shipmentPosition.getCustomerShipment();
//        ItemData itemData = itemDataRepository.retrieve(packShipmentPosition.getItemDataId());
//        if(!shipmentPosition.getItemData().getId().equalsIgnoreCase(itemData.getId())){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//        // 判断面单商品是否完成ReBin
//        ReBinRequestPosition reBinPosition = reBinRequestPositionRepository
//                .getByShipmentPosition(shipmentPosition,itemData);
//        if(reBinPosition == null){
//            throw new ApiException(OutBoundException
//                    .EX_SHIPMENT_NOT_IS_REBINED.toString(), shipment.getShipmentNo());
//        }
//        if(reBinPosition.getAmount().compareTo(shipmentPosition.getAmount()) != 0){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//        // 判断面单是否已经开始包装
//        PackingRequest pack = packingRequestRepository.getByShipment(shipment);
//        if(pack == null){
//            pack = packingRequestBusiness.build(shipmentPosition,reBinPosition,packingStation);
//            pack = packingRequestRepository.save(pack);
//        }else if(pack.getState().equalsIgnoreCase(PackRequestState.PACKED.toString())){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }else {
//            PackingRequestPosition packPosition = null;
//            for(PackingRequestPosition packingRequestPosition : pack.getPositions()){
//                if(packingRequestPosition.getCustomerShipmentPosition().getId()
//                        .equalsIgnoreCase(shipmentPosition.getId())){
//                    packPosition = packingRequestPosition;
//                    break;
//                }
//            }
//            if(packPosition == null){
//                packPosition = packingRequestBusiness.build(pack,shipmentPosition);
//                pack.getPositions().add(packPosition);
//            }else {
//                packPosition.setAmount(packPosition.getAmount().add(BigDecimal.ONE));
//            }
//        }
//        // 检查该面单是否完成所有商品包装
//        packingRequestBusiness.checkPacked(pack,shipment);
//        // 保存订单和包装信息
//        customerShipmentRepository.save(shipment);
//        packingRequestRepository.save(pack);
//    }
//
//    public PackReBinCellDTO getMultiShipmentByReBinWallName(String reBinWallName) {
//        return packMultiBusiness.getByReBinWallName(reBinWallName);
//    }
//
//    public PackReBinCellDTO getMultiShipmentByShipmentNo(String shipmentNo) {
//        return packMultiBusiness.getByShipmentNo(shipmentNo);
//    }
//
//    public CustomerShipmentDTO getSingleShipmentsByItemNo(String containerName, String itemNo) {
//        return packSingleBusiness.getByItemNo(containerName, itemNo);
//    }
//
//
//    public CustomerShipmentDTO getSingleShipmentsByShipmentNo(String shipmentNo){
//        return packSingleBusiness.getByShipmentNo(shipmentNo);
//    }
//
//    public List<CustomerShipmentDTO> getSingleShipmentsByContainerName(String containerName) {
//        return packSingleBusiness.getByContainerName(containerName);
//    }
}
