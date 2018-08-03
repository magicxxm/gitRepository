package com.mushiny.wms.masterdata.obbasics.business.pack;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
//import com.mushiny.wms.context.PermissionsContext;
//import com.mushiny.wms.masterdata.obbasics.business.dto.PackReBinCellDTO;
//import com.mushiny.wms.masterdata.obbasics.crud.dto.CustomerShipmentDTO;
//import com.mushiny.wms.masterdata.obbasics.crud.mapper.CustomerShipmentMapper;
//import com.mushiny.wms.masterdata.obbasics.crud.mapper.CustomerShipmentPositionMapper;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.domain.enums.PackRequestState;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.*;
import com.mushiny.wms.masterdata.general.domain.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackMultiBusiness {

//    private final PermissionsContext permissionsContext;
//    private final ReBinWallRepository reBinWallRepository;
//    private final ReBinWallTypePositionRepository reBinWallTypePositionRepository;
//    private final ReBinRequestPositionRepository reBinRequestPositionRepository;
//    private final PackingRequestRepository packingRequestRepository;
//    private final CustomerShipmentRepository customerShipmentRepository;
//
//    private final CustomerShipmentMapper customerShipmentMapper;
//    private final CustomerShipmentPositionMapper customerShipmentPositionMapper;
//
//    @Autowired
//    public PackMultiBusiness(PermissionsContext permissionsContext,
//                             ReBinWallRepository reBinWallRepository,
//                             ReBinWallTypePositionRepository reBinWallTypePositionRepository,
//                             ReBinRequestPositionRepository reBinRequestPositionRepository,
//                             PackingRequestRepository packingRequestRepository,
//                             CustomerShipmentRepository customerShipmentRepository,
//                             CustomerShipmentMapper customerShipmentMapper,
//                             CustomerShipmentPositionMapper customerShipmentPositionMapper) {
//        this.permissionsContext = permissionsContext;
//        this.reBinWallRepository = reBinWallRepository;
//        this.reBinWallTypePositionRepository = reBinWallTypePositionRepository;
//        this.reBinRequestPositionRepository = reBinRequestPositionRepository;
//        this.packingRequestRepository = packingRequestRepository;
//        this.customerShipmentRepository = customerShipmentRepository;
//        this.customerShipmentMapper = customerShipmentMapper;
//        this.customerShipmentPositionMapper = customerShipmentPositionMapper;
//    }
//
//
//    public PackReBinCellDTO getByReBinWallName(String reBinWallName) {
//        Warehouse warehouse = permissionsContext.getCurrentWarehouse();
//        ReBinWall reBinWall = reBinWallRepository.getByName(warehouse,reBinWallName);
//        if(reBinWall == null){
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(),reBinWallName);
//        }
//        List<ReBinWallTypePosition> positions = reBinWallTypePositionRepository.getByType(reBinWall.getReBinWallType());
//        if(positions.isEmpty()){
//            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
//        }
//        // 检查面单是否完成ReBin
//        for(ReBinWallTypePosition position : positions){
//            List<ReBinRequestPosition> reBinPositions = reBinRequestPositionRepository
//                    .getByReBinWallAndCellName(reBinWall,position.getName());
//            if(reBinPositions.isEmpty()){
//                continue;
//            }
//            CustomerShipmentPosition shipmentPosition = reBinPositions.get(0).getCustomerShipmentPosition();
//            if(shipmentPosition == null){
//                continue;
//            }
//            CustomerShipment shipment = shipmentPosition.getCustomerShipment();
//            // 如果面单之前包装未完成，那么重新开始包装
//            PackingRequest packingRequest = packingRequestRepository.getByShipment(shipment);
//            if(packingRequest != null){
//                if(packingRequest.getState().equalsIgnoreCase(PackRequestState.PACKED.toString())){
//                    continue;
//                }else {
//                    packingRequestRepository.delete(packingRequest);
//                }
//            }
//            return buildPackReBinCellDTO(shipment,position.getName());
//        }
//        throw new ApiException(OutBoundException.EX_REBIN_WALL_NOT_FOUND_SHIPMENT.toString(),reBinWallName);
//    }
//
//    public PackReBinCellDTO getByShipmentNo(String shipmentNo) {
//        CustomerShipment shipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
//        if(shipment == null){
//            throw new ApiException(OutBoundException.EX_SCANNING_OBJECT_NOT_FOUND.toString(),shipmentNo);
//        }
//        List<ReBinRequestPosition> reBinPositions = reBinRequestPositionRepository.getByCustomerShipment(shipment);
//        if(reBinPositions.isEmpty()){
//            throw new ApiException(OutBoundException.EX_SHIPMENT_NOT_IS_REBINED.toString(),shipmentNo);
//        }
//        return buildPackReBinCellDTO(shipment,reBinPositions.get(0).getReBinToCellName());
//    }
//
//    private PackReBinCellDTO buildPackReBinCellDTO(CustomerShipment shipment,String reBinCellName){
//        CustomerShipmentDTO shipmentDTO = customerShipmentMapper.toDTO(shipment);
//        shipmentDTO.getPositions().addAll(customerShipmentPositionMapper.toDTOList(shipment.getPositions()));
//        PackReBinCellDTO dto = new PackReBinCellDTO();
//        dto.setCustomerShipmentDTO(shipmentDTO);
//        dto.setReBinCellName(reBinCellName);
//        return dto;
//    }
}
