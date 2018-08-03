package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.crud.dto.CellStorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.ReliveCellDTO;
import com.mushiny.wms.outboundproblem.crud.dto.StorageLocationPositionDTO;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipment;
import com.mushiny.wms.outboundproblem.domain.common.CustomerShipmentPosition;
import com.mushiny.wms.outboundproblem.domain.common.ItemData;
import com.mushiny.wms.outboundproblem.domain.common.StockUnit;
import com.mushiny.wms.outboundproblem.domain.enums.InventoryState;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.domain.enums.OBPWallState;
import com.mushiny.wms.outboundproblem.domain.enums.VirtualStorageLocation;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.OBPSolveQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OBPCellBusiness {
    private final OBPSolveRepository obpSolveRepository;
    private final OBPCellRepository obpCellRepository;
    private final ApplicationContext applicationContext;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final OBPSolveQuery obpSolveQuery;
    private final OBProblemRepository obProblemRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final BuildBusiness buildBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final ItemDataRepository itemDataRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public OBPCellBusiness(OBPSolveRepository obpSolveRepository,
                           OBPCellRepository obpCellRepository,
                           ApplicationContext applicationContext,
                           OBPSolvePositionRepository obpSolvePositionRepository,
                           OBPSolveQuery obpSolveQuery,
                           OBProblemRepository obProblemRepository,
                           CustomerShipmentRepository customerShipmentRepository,
                           OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                           OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                           CustomerShipmentPositionRepository customerShipmentPositionRepository,
                           BuildBusiness buildBusiness,
                           StockUnitRepository stockUnitRepository,
                           ItemDataRepository itemDataRepository,
                           LocationRepository locationRepository) {
        this.obpSolveRepository = obpSolveRepository;
        this.obpCellRepository = obpCellRepository;
        this.applicationContext = applicationContext;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.obpSolveQuery = obpSolveQuery;
        this.obProblemRepository = obProblemRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.buildBusiness = buildBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.itemDataRepository = itemDataRepository;
        this.locationRepository = locationRepository;
    }

    //清除问题处理格和释放问题处理格
    @SuppressWarnings("Duplicates")
    public void unbindCell(String shipmentNo, String obpStationId, String cellName, String solveKey) {
        List<OBPSolve> solves = obpSolveRepository.getByCellAndShipmentAndStation(applicationContext.getCurrentWarehouse(),shipmentNo,obpStationId,cellName);
        //清除该订单下已添加的序列号记录
        CustomerShipment shipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
        List<OBPShipmentSerialNo> shipmentSerialNoList = obpShipmentSerialNoRepository.getAllByShipment(applicationContext.getCurrentWarehouse(),shipment.getId());
        if (!shipmentSerialNoList.isEmpty()) {
            for (OBPShipmentSerialNo serialNo : shipmentSerialNoList) {
                obpShipmentSerialNoRepository.delete(serialNo);
            }
        }
        if (solveKey.equalsIgnoreCase(SolveResoult.CLEARANCE_CELL.toString())) {

            //将商品移到Problem Solving容器中
            outBoundProblemCommonBusiness.removeItemToVirtualContainer(shipmentNo, VirtualStorageLocation.PROBLEM_SOLVING.getName(),solveKey);

            for (OBPSolve entity : solves) {
                OBPSolvePosition solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(
                        applicationContext.getCurrentWarehouse(),shipmentNo,entity.getId());
                if (solvePosition != null) {
                    if(solvePosition.getSolveKey().equals(SolveResoult.HAS_HOT_PICK.toString())){
                        throw new ApiException("已生成拣货任务,不能清除问题处理格");
                    }else if(solvePosition.getSolveKey().equals(SolveResoult.ASSIGNED_LOCATION.toString())){
                        throw new ApiException("已分配货位,不能清除问题处理格");
                    }
                    obpSolvePositionRepository.delete(solvePosition.getId());
                }
                obpSolveRepository.delete(entity);
            }

        }else if (solveKey.equalsIgnoreCase(SolveResoult.RELEASE_CELL.toString())) {

            for (OBPSolve entity : solves) {
                entity.setState(OBPSolveState.solved.toString());
                entity.setEntityLock(Constant.NOT_LOCKED);
                entity.setObpCell(null);
                obpSolveRepository.save(entity);

                List<OBPLocation> obpLocations= locationRepository.getBySolveId(applicationContext.getCurrentWarehouse(),entity.getId());
                for(OBPLocation obpLocation:obpLocations){
                    obpLocation.setState(OBPSolveState.solved.toString());
                    obpLocation.setSolveDate(LocalDateTime.now());
                    obpLocation.setSolveBy(SecurityUtils.getCurrentUsername());
                    locationRepository.save(obpLocation);
                }

                OBPSolvePosition solvePosition = obpSolvePositionRepository.getByShipmentNoAndItemNo(
                        applicationContext.getCurrentWarehouse(), entity.getId(), entity.getCustomerShipment().getShipmentNo(), entity.getItemData().getItemNo());
                if (solvePosition != null) {
                    solvePosition.setState(OBPSolveState.solved.toString());
                    obpSolvePositionRepository.save(solvePosition);
                }

                OBProblem problem = entity.getObproblem();
                if (problem != null) {
                    problem.setSolvedBy(SecurityUtils.getCurrentUsername());
                    problem.setState(OBPSolveState.solved.toString());
                    problem.setContainer(VirtualStorageLocation.PROBLEM_SOLVED.getName());
                    obProblemRepository.save(problem);
                }
            }

            //将商品从CELL格移到Problem Solved容器，同时生成库存记录
            outBoundProblemCommonBusiness.removeItemToVirtualContainer(shipmentNo,VirtualStorageLocation.PROBLEM_SOLVED.getName(),solveKey);

            CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(shipmentNo);
            customerShipment.setState(ShipmentState.PACKING);
            customerShipmentRepository.save(customerShipment);

            //更新customerShipmentRecord
            buildBusiness.buildCustomerShipmentRecord(customerShipment.getShipmentNo(),obpStationId,"Packing",ShipmentState.PACKING);

            List<CustomerShipmentPosition> customerShipmentPositions=customerShipmentPositionRepository.getByShipmentID(applicationContext.getCurrentWarehouse(),customerShipment.getShipmentNo());
            for(CustomerShipmentPosition customerShipmentPosition:customerShipmentPositions){
                customerShipmentPosition.setState(ShipmentState.PACKING);
                customerShipmentPositionRepository.save(customerShipmentPosition);
            }
        }

        OBPCell cell = obpCellRepository.getByCellName(applicationContext.getCurrentWarehouse(), cellName);
        cell.setState(OBPWallState.unoccupied.toString());
        obpCellRepository.save(cell);
    }

    //美的清除问题处理格和释放问题处理格
    @SuppressWarnings("Duplicates")
    public void relieveCell(ReliveCellDTO reliveCellDTO) {
        //提示扫描正品货筐
        outBoundProblemCommonBusiness.checkContainer(reliveCellDTO.getLocationContainer(),
                InventoryState.INVENTORY.toString());
        //清除该订单下已添加的序列号记录
        CustomerShipment shipment = customerShipmentRepository.getByShipmentNo(reliveCellDTO.getShipmentNo());
        List<OBPShipmentSerialNo> shipmentSerialNoList = obpShipmentSerialNoRepository.getAllByShipment(applicationContext.getCurrentWarehouse(),shipment.getId());
        if (!shipmentSerialNoList.isEmpty()) {
            for (OBPShipmentSerialNo serialNo : shipmentSerialNoList) {
                obpShipmentSerialNoRepository.delete(serialNo);
            }
        }
        List<OBPSolve> obpSolves = obpSolveRepository.getByCellAndShipmentAndStation(applicationContext.getCurrentWarehouse(),reliveCellDTO.getShipmentNo(),reliveCellDTO.getObpStationId(),reliveCellDTO.getCellName());

        if (reliveCellDTO.getSolveKey().equalsIgnoreCase(SolveResoult.CLEARANCE_CELL.toString())) {
            outBoundProblemCommonBusiness.removeItemToVirtualContainer(reliveCellDTO.getShipmentNo(),reliveCellDTO.getLocationContainer(),SolveResoult.CLEARANCE_CELL.toString());
            for (OBPSolve entity : obpSolves) {
                OBPSolvePosition solvePosition = obpSolvePositionRepository.getSolvePositionByShipmentNoAndSolve(
                        applicationContext.getCurrentWarehouse(),reliveCellDTO.getShipmentNo(),entity.getId());
                if (solvePosition != null) {
                    if(solvePosition.getSolveKey().equals(SolveResoult.HAS_HOT_PICK.toString())){
                        throw new ApiException("已生成拣货任务,不能清除问题处理格");
                    }else if(solvePosition.getSolveKey().equals(SolveResoult.ASSIGNED_LOCATION.toString())){
                        throw new ApiException("已分配货位,不能清除问题处理格");
                    }
                    obpSolvePositionRepository.delete(solvePosition.getId());
                }
                obpSolveRepository.delete(entity);
            }
        }else if (reliveCellDTO.getSolveKey().equalsIgnoreCase(SolveResoult.RELEASE_CELL.toString())) {

            for (OBPSolve entity : obpSolves) {
                entity.setState(OBPSolveState.solved.toString());
                entity.setEntityLock(Constant.NOT_LOCKED);
                entity.setObpCell(null);
                obpSolveRepository.save(entity);

                List<OBPLocation> obpLocations= locationRepository.getBySolveId(applicationContext.getCurrentWarehouse(),entity.getId());
                for(OBPLocation obpLocation:obpLocations){
                    obpLocation.setState(OBPSolveState.solved.toString());
                    obpLocation.setSolveDate(LocalDateTime.now());
                    obpLocation.setSolveBy(SecurityUtils.getCurrentUsername());
                    locationRepository.save(obpLocation);
                }

                OBPSolvePosition solvePosition = obpSolvePositionRepository.getByShipmentNoAndItemNo(applicationContext.getCurrentWarehouse(), entity.getId(), entity.getCustomerShipment().getShipmentNo(), entity.getItemData().getItemNo());
                if (solvePosition != null) {
                    solvePosition.setState(OBPSolveState.solved.toString());
                    obpSolvePositionRepository.save(solvePosition);
                }
                OBProblem problem = entity.getObproblem();
                if (problem != null) {
                    problem.setSolvedBy(SecurityUtils.getCurrentUsername());
                    problem.setState(OBPSolveState.solved.toString());
                    problem.setContainer(reliveCellDTO.getLocationContainer());
                    obProblemRepository.save(problem);
                }
            }
            //将订单商品转移到正品容器
            outBoundProblemCommonBusiness.removeItemToVirtualContainer(reliveCellDTO.getShipmentNo(),reliveCellDTO.getLocationContainer(),SolveResoult.RELEASE_CELL.toString());
            CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(reliveCellDTO.getShipmentNo());
            customerShipment.setState(ShipmentState.PACKING);
            customerShipmentRepository.save(customerShipment);
            //更新customerShipmentRecord
            buildBusiness.buildCustomerShipmentRecord(customerShipment.getShipmentNo(),reliveCellDTO.getObpStationId(),"Packing",ShipmentState.PACKING);
            List<CustomerShipmentPosition> customerShipmentPositions=customerShipmentPositionRepository.getByShipmentID(applicationContext.getCurrentWarehouse(),customerShipment.getShipmentNo());
            for(CustomerShipmentPosition customerShipmentPosition:customerShipmentPositions){
                customerShipmentPosition.setState(ShipmentState.PACKING);
                customerShipmentPositionRepository.save(customerShipmentPosition);
            }
        }
        OBPCell cell = obpCellRepository.getByCellName(applicationContext.getCurrentWarehouse(),reliveCellDTO.getCellName());
        cell.setState(OBPWallState.unoccupied.toString());
        obpCellRepository.save(cell);
    }

    public List<CellStorageLocationDTO> assignLocation(String obpWallId, String podNo,String location) {
        List<OBPLocation> locations=new ArrayList<>();
        if(!location.isEmpty()){
           locations=locationRepository.getByLocation(applicationContext.getCurrentWarehouse(),location);
           if(locations.isEmpty())
               throw new ApiException(OutBoundProblemException.EX_BIN_NOT_FOUND.getName());
           if(!locations.get(0).getLocation().contains(podNo))
               throw new ApiException(OutBoundProblemException.EX_BIN_NOT_NOW_POD.getName());
        }
        List<OBPLocation> li=new ArrayList<>();
        if(!podNo.isEmpty()){
           li=locationRepository.getByPodNo(applicationContext.getCurrentWarehouse(),podNo);
           if(li.isEmpty())
                throw new ApiException("该POD不存在需要拣货的商品，请释放POD");
        }
        //查询所有未解决且已经分配货位的问题格
        List<String> objects=obpSolveQuery.getCellByWallId(applicationContext.getCurrentWarehouse(),obpWallId);
        //查询所有已经分配的问题格
        List<CellStorageLocationDTO> cellStorageLocationDTOS=new ArrayList<>();
        objects.removeAll(Collections.singleton(null));  //去除集合中null元素
        for (String cellId: objects) {
            CellStorageLocationDTO cellStorageLocationDTO=new CellStorageLocationDTO();
            List<StorageLocationPositionDTO> storageLocationPositionDTOList = new ArrayList<>();
            List<Object[]> objectList=new ArrayList<>();
            if (cellId != null && !cellId.isEmpty()) {
                OBPCell cell=obpCellRepository.retrieve(cellId);
                cellStorageLocationDTO.setCellName(cell.getName().split("-")[1]);
                cellStorageLocationDTO.setyPos(cell.getyPos());
                objectList=obpSolveQuery.getStorageLocation(applicationContext.getCurrentWarehouse(),obpWallId,cellId);
            }
            for (Object[] obj:objectList) {
                StorageLocationPositionDTO storageLocationPositionDTO = new StorageLocationPositionDTO();
                if (obj[2] != null && !String.valueOf(obj[2]).isEmpty()) {
                    storageLocationPositionDTO.setStorageLocationName(String.valueOf(obj[2]));
                    if(!podNo.isEmpty()&&String.valueOf(obj[2]).contains(podNo)){
                        //eg：呼叫A1的100号POD,同时B1也需要100号POD，但是B没有呼叫，此时B1和B1所在的100号POD颜色不变
                        //第二次呼叫B1的100号POD,B1变色,A1不变色
                        List<OBPLocation> obp=locationRepository.getByCellAndLocation(applicationContext.getCurrentWarehouse(),String.valueOf(obj[2]),obpCellRepository.retrieve(cellId).getName());
                         if(obp.size()>0){
                             storageLocationPositionDTO.setGoodsInStorageLocation(true);
                             cellStorageLocationDTO.setThisCell(true);
                         }
                    }
                }
                storageLocationPositionDTO.setAmountProblem(Integer.parseInt(String.valueOf(obj[0])));
                storageLocationPositionDTO.setAmountScanedProblem(Integer.parseInt(String.valueOf(obj[1])));
                storageLocationPositionDTOList.add(storageLocationPositionDTO);
            }
            for(OBPLocation obp: li){
                cellStorageLocationDTO.setNextPod(true);
                if(obp.getAmountScaned()<obp.getAmount()) {
                    cellStorageLocationDTO.setNextPod(false);
                    break;
                }
            }
            for(OBPLocation obpLocation:locations){
                if(obpLocation.getAmountScaned()<obpLocation.getAmount())
                    cellStorageLocationDTO.setItemName(obpLocation.getItemName());
            }
            if(objectList.size()>0)
                cellStorageLocationDTO.setCallPod(Boolean.valueOf(String.valueOf(objectList.get(0)[3])));
            cellStorageLocationDTO.setStorageLocationPositions(storageLocationPositionDTOList);
            cellStorageLocationDTOS.add(cellStorageLocationDTO);
        }
        Collections.sort(cellStorageLocationDTOS,new Comparator<CellStorageLocationDTO>(){
            public int compare( CellStorageLocationDTO arg0, CellStorageLocationDTO arg1) {
                //比较纵坐标
                int i=arg0.getyPos()-arg1.getyPos();
                if(i==0){
                    i=arg0.getCellName().compareTo(arg1.getCellName());
                }
                return i;
            }
        });
        return cellStorageLocationDTOS;
    }
}
