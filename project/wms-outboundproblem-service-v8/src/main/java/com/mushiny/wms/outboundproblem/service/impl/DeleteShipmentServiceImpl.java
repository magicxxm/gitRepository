package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.BuildBusiness;
import com.mushiny.wms.outboundproblem.business.OBPSolveBusiness;
import com.mushiny.wms.outboundproblem.business.SolveDamagedBusiness;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.ForceDeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolvePositionMapper;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipmentPosition;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.DeleteShipmentQuery;
import com.mushiny.wms.outboundproblem.repository.OBPSolvePositionRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentPositionRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentRepository;
import com.mushiny.wms.outboundproblem.service.DeleteShipmentService;
import com.mushiny.wms.outboundproblem.service.OBPCellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeleteShipmentServiceImpl implements DeleteShipmentService {
    private final DeleteShipmentQuery deleteShipmentQuery;
    private final SolveDamagedBusiness solveDamagedBusiness;
    private final OBPSolveBusiness obpSolveBusiness;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final ApplicationContext applicationContext;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final OBPSolveRepository obpSolveRepository;
    private final OBPSolvePositionMapper obpSolvePositionMapper;
    private final OBPCellService obpCellService;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final BuildBusiness buildBusiness;
    private final OBProblemRepository obProblemRepository;

    @Autowired
    public DeleteShipmentServiceImpl(DeleteShipmentQuery deleteShipmentQuery,
                                     SolveDamagedBusiness solveDamagedBusiness,
                                     OBPSolveBusiness obpSolveBusiness,
                                     CustomerShipmentRepository customerShipmentRepository,
                                     OBPSolvePositionRepository obpSolvePositionRepository,
                                     ApplicationContext applicationContext,
                                     OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                                     OBPSolveRepository obpSolveRepository,
                                     OBPSolvePositionMapper obpSolvePositionMapper,
                                     OBPCellService obpCellService,
                                     CustomerShipmentPositionRepository customerShipmentPositionRepository,
                                     BuildBusiness buildBusiness, OBProblemRepository obProblemRepository) {
        this.deleteShipmentQuery = deleteShipmentQuery;
        this.solveDamagedBusiness = solveDamagedBusiness;
        this.obpSolveBusiness = obpSolveBusiness;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.applicationContext = applicationContext;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.obpSolveRepository = obpSolveRepository;
        this.obpSolvePositionMapper = obpSolvePositionMapper;
        this.obpCellService = obpCellService;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.buildBusiness = buildBusiness;
        this.obProblemRepository = obProblemRepository;
    }

    @Override
    public List<ForceDeleteShipmentDTO> queryForceDeleteShipment(String startDate, String endDate, String shipmentNo, String state, String obpStationId, String obpWallId) {
        shipmentNo= obpSolveBusiness.scanShipmentNo(shipmentNo);
        //强制删单中查询功能
        List<Object[]> forceDeleteShipmentDTOS = deleteShipmentQuery.queryForceDeleteShipment(startDate, endDate, shipmentNo, state,obpStationId);
        List<ForceDeleteShipmentDTO> forceDeleteShipments = new ArrayList<>();
        List<OBPSolve> solves = obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(),shipmentNo);
        //如果按shipmentNo查询出来的结果为空，说明所扫描或查询的该订单需进行强删处理
        CustomerShipment customerShipment=null;
        if(!shipmentNo.isEmpty())
            customerShipment = customerShipmentRepository.getByShipmentNo(shipmentNo);

        if (!shipmentNo.isEmpty()&& forceDeleteShipmentDTOS.size()== 0) {

            if (customerShipment == null) {
                throw new ApiException(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_FOUND.getName(),shipmentNo);
            }

            if (customerShipment.getState() < ShipmentState.PICKED || customerShipment.getState() == ShipmentState.FINISHED
                    || customerShipment.getState() == ShipmentState.CANCELED
                    || customerShipment.getState() == ShipmentState.POSTPROCESSED) {
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_CAN_NOT_DELETE.getName(), shipmentNo);

            }
            //判断在删除订单之前，是否正在进行的问题处理，若存在，需先进行清除问题格操作
            if(!solves.isEmpty()&&solves.get(0).getObpCell()!=null){
                throw new ApiException(OutBoundProblemException.EX_SHIPMENT_IS_SOLVING.getName(), shipmentNo);
            }
            if(!solves.isEmpty()&&solves.get(0).getObpCell()==null&&customerShipment.getState()!=ShipmentState.DELETED){ //订单在问题处理中已经处理完成
                obpSolveBusiness.deleteSolve(customerShipment.getShipmentNo(),solves.get(0).getObpStation().getId(),solves.get(0).getObpWall().getId());
            }
            //添加订单下的所有商品到obpSolve表中
            if (forceDeleteShipmentDTOS.size()==0) {
                if(solves.size()==0) {
                    obpSolveBusiness.addShipmentToOBPSolve(obpStationId, obpWallId, shipmentNo,"DELETE_ORDER_FORCE");
                }
                    ForceDeleteShipmentDTO forceDeleteShipment = new ForceDeleteShipmentDTO();
                    forceDeleteShipment.setShipmentNo(shipmentNo);

                    ZonedDateTime exSD = customerShipment.getDeliveryDate();

                    forceDeleteShipment.setExSD(null);
                    forceDeleteShipment.setTimeCondition(null);
                    forceDeleteShipment.setDescription(null);
                    if (exSD != null) {
                        LocalDateTime exSDLocalDateTime = exSD.toLocalDateTime();
                        forceDeleteShipment.setExSD(exSDLocalDateTime.toString());
                        forceDeleteShipment.setTimeCondition(DateTimeUtil.getTimeString(exSD, ZonedDateTime.now()));

                    }
                    //得到该订单下的商品明细
                    forceDeleteShipment.setSolveShipmentPositions(obpSolveBusiness.getSolveShipmentPositionByShipmentNo(shipmentNo));

                    forceDeleteShipment.setState(OBPSolveState.unsolved.toString());
                    forceDeleteShipment.setShipmentState(customerShipment.getState());
                    forceDeleteShipments.add(forceDeleteShipment);
                }

        } else {
                for (Object[] object : forceDeleteShipmentDTOS) {
                    ForceDeleteShipmentDTO forceDeleteShipment = new ForceDeleteShipmentDTO();
                    forceDeleteShipment.setShipmentNo(String.valueOf(object[0]));

                    forceDeleteShipment.setExSD("");
                    if (object[1] != null && !String.valueOf(object[1]).isEmpty())
                        forceDeleteShipment.setExSD(String.valueOf(object[1]));
                    String exs = String.valueOf(object[1]).replace("T", " ").replace("+",":").substring(0, 19);
                    ZonedDateTime exSD = ConversionUtil.toZonedDateTime(exs);
                    forceDeleteShipment.setTimeCondition(DateTimeUtil.getTimeString(exSD, ZonedDateTime.now()));
                    forceDeleteShipment.setSolveDate("");
                    if(customerShipment!=null)
                    forceDeleteShipment.setShipmentState(customerShipment.getState());
                    if (object[2] != null && !String.valueOf(object[2]).isEmpty())
                        forceDeleteShipment.setSolveDate(String.valueOf(object[2]));

                    forceDeleteShipment.setDescription("");
                    if (object[3] != null && !String.valueOf(object[3]).isEmpty())
                        forceDeleteShipment.setDescription(String.valueOf(object[3]));

                    forceDeleteShipment.setSolveBy("");
                    if (object[4] != null && !String.valueOf(object[4]).isEmpty())
                        forceDeleteShipment.setSolveBy(String.valueOf(object[4]));

                    //得到该订单下的商品明细
                    if (!shipmentNo.isEmpty())
                        forceDeleteShipment.setSolveShipmentPositions(obpSolveBusiness.getSolveShipmentPositionByShipmentNo(shipmentNo));
                    else
                        forceDeleteShipment.setSolveShipmentPositions(obpSolveBusiness.getSolveShipmentPositionByShipmentNo(String.valueOf(object[0])));
                    forceDeleteShipment.setContainer("");
                    if(object[5] != null && !String.valueOf(object[5]).isEmpty())
                        forceDeleteShipment.setContainer(String.valueOf(object[5]));

                    forceDeleteShipment.setState(OBPSolveState.unsolved.toString());
                    if(object[6] != null &&String.valueOf(object[6]).equals(OBPSolveState.solved.toString()))
                        forceDeleteShipment.setState(OBPSolveState.solved.toString());
                    if(customerShipment!=null)
                        forceDeleteShipment.setShipmentState(customerShipment.getState());

                    forceDeleteShipments.add(forceDeleteShipment);
                }
            }

        return forceDeleteShipments;
    }

    @Override
    public void addDeleteReason(String shipmentNo, String deleteReason) {
        // 问题处理详情表中添加处理记录
        OBPSolvePositionDTO obpSolvePositionDTO = new OBPSolvePositionDTO();
        CustomerShipment customerShipment=customerShipmentRepository.getByShipmentNo(shipmentNo);
        OBPSolvePosition obpSolvePosition=obpSolvePositionRepository.getForceDeleteSolvePositionByShipmentNoAndSolveKey(applicationContext.getCurrentWarehouse(),shipmentNo,SolveResoult.DELETE_ORDER_FORCE.toString());
        if(customerShipment.getState()!=ShipmentState.DELETED && obpSolvePosition==null) {
            //EntityLock为订单小于630的标记
            if (customerShipment.getState() < ShipmentState.REBINED) {
                obpSolvePositionDTO.setEntityLock(2);
            }
            obpSolvePositionDTO.setDescription(deleteReason);
            obpSolvePositionDTO.setItemNo(null);
            //分配货位取货时，设置的货位
            obpSolvePositionDTO.setLocation(null);
            //绑定放删单商品的车牌
            obpSolvePositionDTO.setShipmentNo(shipmentNo);
            obpSolvePositionDTO.setSolveBy(SecurityUtils.getCurrentUsername());
            obpSolvePositionDTO.setSolveDate(LocalDateTime.now());
            List<OBPSolve> solves = obpSolveRepository.getByShipmentNo(applicationContext.getCurrentWarehouse(), shipmentNo);

            obpSolvePositionDTO.setSolveId(null);
            if (solves.size() > 0)
                obpSolvePositionDTO.setSolveId(solves.get(0).getId());

            obpSolvePositionDTO.setSolveKey(SolveResoult.DELETE_ORDER_FORCE.toString());
//        //更新状态为还在删除处理中，当将商品放到处理车之后，才能将状态改为完成
//        obpSolvePositionDTO.setState(OBPSolveState.solving.toString());
            obpSolvePositionDTO.setState(OBPSolveState.unsolved.toString());
            obpSolvePositionRepository.save(obpSolvePositionMapper.toEntity(obpSolvePositionDTO));

            //写删单原因后，订单就被删除，更改订单状态
            customerShipment.setState(ShipmentState.DELETED);
            customerShipmentRepository.save(customerShipment);
            //更改订单明细表的状态
            List<CustomerShipmentPosition> customerShipmentPositions = customerShipmentPositionRepository.getByShipmentID(applicationContext.getCurrentWarehouse(), customerShipment.getShipmentNo());
            for (CustomerShipmentPosition customerShipmentPosition : customerShipmentPositions) {
                customerShipmentPosition.setState(ShipmentState.DELETED);
                customerShipmentPositionRepository.save(customerShipmentPosition);
            }
            buildBusiness.buildCustomerShipmentRecord(shipmentNo, solves.get(0).getObpStation().getId(), "Deleted", ShipmentState.DELETED);
        }
     }

    @Override
    public void putForceDeleteGoodsToContainer(DeleteShipmentDTO deleteShipmentDTO) {
        obpSolveBusiness.putDeleteGoodsToContainer(deleteShipmentDTO);
    }
}
