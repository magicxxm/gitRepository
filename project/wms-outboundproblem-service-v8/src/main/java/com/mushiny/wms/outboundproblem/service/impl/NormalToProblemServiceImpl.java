package com.mushiny.wms.outboundproblem.service.impl;


import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.outboundproblem.business.BuildBusiness;
import com.mushiny.wms.outboundproblem.business.OBPSolveBusiness;
import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipmentPosition;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentPositionRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentRepository;
import com.mushiny.wms.outboundproblem.service.NormalToProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class NormalToProblemServiceImpl implements NormalToProblemService {
    private final OBPSolveBusiness obpSolveBusiness;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBPSolveRepository obpSolveRepository;
    private final ApplicationContext applicationContext;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final BuildBusiness buildBusiness;

    @Autowired
    public NormalToProblemServiceImpl(OBPSolveBusiness obpSolveBusiness,
                                      CustomerShipmentRepository customerShipmentRepository,
                                      OBPSolveRepository obpSolveRepository,
                                      ApplicationContext applicationContext,
                                      CustomerShipmentPositionRepository customerShipmentPositionRepository,
                                      BuildBusiness buildBusiness) {
        this.obpSolveBusiness = obpSolveBusiness;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obpSolveRepository = obpSolveRepository;
        this.applicationContext = applicationContext;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.buildBusiness = buildBusiness;
    }

    @Override
    public List<SolveShipmentPositionDTO> normalToProblem(String obpStationId, String obpWallId, String shipmentNo) {
        //如果正常转问题的订单是之前已经处理过，并且订单状态是640-650,需要将此订单的原始数据删除
       // List<OBPSolve> solvedProblem = obpSolveRepository.getProblemGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
        List<OBPSolve> obpSolves = obpSolveRepository.getAllGoodsByShipmentNo(applicationContext.getCurrentWarehouse(), shipmentNo);
        if(obpSolves.size()>0){ //之前处理过此订单
            obpSolveBusiness.deleteObpSolve(shipmentNo,obpStationId,obpWallId);
        }else{
            obpSolveBusiness.addShipmentToOBPSolve(obpStationId, obpWallId, shipmentNo, null);
        }
        //更改订单的状态为1100
        CustomerShipment customerShipment=customerShipmentRepository.getByShipmentNo(shipmentNo);
        customerShipment.setState(ShipmentState.PROBLEM);
        customerShipmentRepository.save(customerShipment);
        //更新customerShipmentRecord
        buildBusiness.buildCustomerShipmentRecord(shipmentNo,obpStationId,"Problem",ShipmentState.PROBLEM);
        //更改订单详情表的状态
        List<CustomerShipmentPosition> customerShipmentPositions=customerShipmentPositionRepository.getByShipmentID(applicationContext.getCurrentWarehouse(),customerShipment.getShipmentNo());
        for(CustomerShipmentPosition customerShipmentPosition:customerShipmentPositions){
            customerShipmentPosition.setState(ShipmentState.PROBLEM);
            customerShipmentPositionRepository.save(customerShipmentPosition);
        }
        return obpSolveBusiness.getSolveShipmentPositionByShipmentNo(shipmentNo);
    }
}
