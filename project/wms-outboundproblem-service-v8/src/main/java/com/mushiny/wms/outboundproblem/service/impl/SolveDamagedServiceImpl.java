package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.BuildBusiness;
import com.mushiny.wms.outboundproblem.business.OBPSolveBusiness;
import com.mushiny.wms.outboundproblem.business.SolveDamagedBusiness;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.dto.DeleteShipmentDTO;
import com.mushiny.wms.outboundproblem.business.dto.SolveDamagedDTO;
import com.mushiny.wms.outboundproblem.business.enums.CellState;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.crud.dto.OBPSolvePositionDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolvePositionMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.OBPSolveState;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.SolveDamageQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.service.SolveDamagedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SolveDamagedServiceImpl implements SolveDamagedService {

    private final SolveDamagedBusiness solveDamagedBusiness;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final OBPSolveRepository obpSolveRepository;
    private final ApplicationContext applicationContext;
    private final OBPSolveBusiness obpSolveBusiness;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final OBPSolvePositionMapper obpSolvePositionMapper;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final BuildBusiness buildBusiness;
    private final SolveDamageQuery solveDamageQuery;
    private final OBPStationRepository obpStationRepository;
    private final WorkStationRepository workStationRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final OBPCellRepository obpCellRepository;

    @Autowired
    public SolveDamagedServiceImpl(SolveDamagedBusiness solveDamagedBusiness,
                                   OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                                   OBPSolveRepository obpSolveRepository,
                                   ApplicationContext applicationContext,
                                   OBPSolveBusiness obpSolveBusiness,
                                   OBPSolvePositionRepository obpSolvePositionRepository,
                                   OBPSolvePositionMapper obpSolvePositionMapper,
                                   CustomerShipmentRepository customerShipmentRepository,
                                   CustomerShipmentPositionRepository customerShipmentPositionRepository,
                                   BuildBusiness buildBusiness,
                                   SolveDamageQuery solveDamageQuery,
                                   OBPStationRepository obpStationRepository,
                                   WorkStationRepository workStationRepository,
                                   StockUnitRepository stockUnitRepository,
                                   StorageLocationRepository storageLocationRepository,
                                   OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                                   OBPCellRepository obpCellRepository) {
        this.solveDamagedBusiness = solveDamagedBusiness;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.obpSolveRepository = obpSolveRepository;
        this.applicationContext = applicationContext;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.obpSolveBusiness = obpSolveBusiness;
        this.obpSolvePositionMapper = obpSolvePositionMapper;
        this.customerShipmentRepository = customerShipmentRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.buildBusiness = buildBusiness;
        this.solveDamageQuery = solveDamageQuery;
        this.obpStationRepository = obpStationRepository;
        this.workStationRepository = workStationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.obpCellRepository = obpCellRepository;
    }

    @Override
    public void damagedToNormal(OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO);
    }

    @Override
    public void confirmDamaged(OBPSolvePositionDTO obpSolvePositionDTO) {
        solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO);
    }

    @Override
    public void generateHotPick(OBPSolvePositionDTO obpSolvePositionDTO) {
        buildBusiness.buildCustomerShipmentHotPick(obpSolvePositionDTO.getShipmentNo(),obpSolvePositionDTO.getItemDataId(),obpSolvePositionDTO.getAmountHotPick());
        solveDamagedBusiness.generateHotPick(obpSolvePositionDTO);
    }

    @Override
    public List<Map> searchStorageLocationByItemNo(String itemNo,String sectionId) {
        return solveDamagedBusiness.searchStorageLocationByItemNo(itemNo,sectionId);
    }

    @Override
    public OBPSolvePositionDTO allocationStorageLocation(OBPSolvePositionDTO obpSolvePositionDTO) {
        return obpSolvePositionMapper.toDTO(solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO));
    }

//    @Override
//    public void checkItemLot(String fromContainer, String toContainer, String shipmentNo, String itemNO, LocalDate useNotAfter, BigDecimal amount) {
//        ItemData itemData = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(),shipmentNo, itemNO).getItemData();
//        outBoundProblemCommonBusiness.checkItemLot(fromContainer, toContainer, itemData, useNotAfter, amount);
//    }

    @Override
    public void putDamagedToContainer(String containerName, String shipmentNo, String itemNo, String useNotAfter,String serialNo) {
        LocalDate userNotAfter = null;
        if (!useNotAfter.isEmpty())
            userNotAfter = ConversionUtil.toZonedDateTime(useNotAfter + " 00:00:00").toLocalDate();
        solveDamagedBusiness.putDamagedToContainer(containerName, shipmentNo, itemNo, userNotAfter,serialNo);
    }

    @Override
    public void deleteShipment(OBPSolvePositionDTO obpSolvePositionDTO) {
        //solveDamagedBusiness.solveOBProblem(obpSolvePositionDTO);
        //判断是否有进行问题处理
        OBPSolve solve= obpSolveRepository.retrieve(obpSolvePositionDTO.getSolveId());
        OBPSolvePosition obpSolvePosition= obpSolvePositionRepository.getByShipmentNo(
               applicationContext.getCurrentWarehouse(),obpSolvePositionDTO.getSolveId(),obpSolvePositionDTO.getShipmentNo());

        List<OBPShipmentSerialNo> shipmentSerialNoList = obpShipmentSerialNoRepository.getAllByShipment(applicationContext.getCurrentWarehouse(),solve.getCustomerShipment().getId());
        for (OBPShipmentSerialNo serialNo : shipmentSerialNoList) {
             serialNo.setScaned(false);
             obpShipmentSerialNoRepository.save(serialNo);
        }
        if(obpSolvePosition!=null) {
            obpSolvePosition.setModifiedDate(LocalDateTime.now());
            obpSolvePosition.setSolveKey(obpSolvePositionDTO.getSolveKey());
            obpSolvePositionRepository.save(obpSolvePosition);
            //更改订单的状态
            CustomerShipment customerShipment = customerShipmentRepository.getByShipmentNo(obpSolvePositionDTO.getShipmentNo());
            customerShipment.setState(ShipmentState.DELETED);
            customerShipmentRepository.save(customerShipment);
            //更改订单详情表状态
            List<CustomerShipmentPosition> customerShipmentPositions=customerShipmentPositionRepository.getByShipmentID(applicationContext.getCurrentWarehouse(),customerShipment.getShipmentNo());
            for(CustomerShipmentPosition customerShipmentPosition:customerShipmentPositions){
                customerShipmentPosition.setState(ShipmentState.DELETED);
                customerShipmentPositionRepository.save(customerShipmentPosition);
            }
            buildBusiness.buildCustomerShipmentRecord(obpSolvePositionDTO.getShipmentNo(),solve.getObpStation().getId(),"Deleted",ShipmentState.DELETED);
        }
        //订单里只有一种商品且无库存
        List<OBPSolve> obpSolves=obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(),obpSolvePositionDTO.getShipmentNo());
        OBPSolve obpSolve=obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(),solve.getItemData().getItemNo(),obpSolvePositionDTO.getShipmentNo());
        if(obpSolves.size()==1&&obpSolve.getAmountShipment().compareTo(solve.getAmountProblem())==0){
             solve.setState(OBPSolveState.solved.toString());
             solve.setObpCell(null);
             obpSolveRepository.save(solve);
             OBPCell cell= obpSolve.getObpCell();
             cell.setState(CellState.unoccupied.toString());
             obpCellRepository.save(cell);
             obpSolve.setState(OBPSolveState.solved.toString());
             obpSolve.setObpCell(null);
             obpSolveRepository.save(obpSolve);
        }
    }

    @Override
    public void deleteShipmentScanGoods(DeleteShipmentDTO deleteShipmentDTO) {
        obpSolveBusiness.putDeleteGoodsToContainer(deleteShipmentDTO);
    }

    @Override
    public void dismantleShipment(String shipmentNo, String solveKey) {
        //拆单发货之前需要确认正常的商品是否扫描完成放在问题格里
        List<OBPSolve> obpSolve=obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(),shipmentNo);
        String cellName=obpSolve.get(0).getObpCell().getName();
        //判断问题格里是否存在商品
        StorageLocation storageLocation= storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(),cellName);
        if(storageLocation==null)
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_SHOULD_SCAN.getName());
        List<StockUnit> stockUnits=stockUnitRepository.getByStorageLocationAndAmount(storageLocation);
        if(stockUnits.isEmpty())
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_SHOULD_SCAN.getName());
       //修改库存为0的订单详情表
        List<OBPSolve> obpSolves=obpSolveRepository.getByShipmentAndProblemType(applicationContext.getCurrentWarehouse(), shipmentNo);
        for(OBPSolve solve : obpSolves){
           BigDecimal amount= stockUnitRepository.sumByItemData(solve.getItemData());
           if(amount.compareTo(BigDecimal.ZERO)==0) {
               CustomerShipmentPosition customerShipmentPosition = customerShipmentPositionRepository.getByShipmentAndItemData(applicationContext.getCurrentWarehouse(), shipmentNo, solve.getItemData());
               customerShipmentPosition.setAmount(customerShipmentPosition.getAmount().subtract(solve.getAmountProblem()));
               customerShipmentPositionRepository.save(customerShipmentPosition);
           }
        }
        List<OBPSolve> solves = obpSolveRepository.getProblemGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
        for (OBPSolve entity : solves) {
            entity.setModifiedDate(LocalDateTime.now());
            entity.setSolveBy(SecurityUtils.getCurrentUsername());
            entity.setSolveDate(LocalDateTime.now());
            entity.setState(OBPSolveState.solved.toString());
            obpSolveRepository.save(entity);

            OBPSolvePosition solvePosition = obpSolvePositionRepository.getByShipmentNoAndItemNo(applicationContext.getCurrentWarehouse(),
                    entity.getId(),entity.getCustomerShipment().getShipmentNo(),entity.getItemData().getItemNo());
            solvePosition.setModifiedDate(LocalDateTime.now());
            solvePosition.setSolveBy(SecurityUtils.getCurrentUsername());
            solvePosition.setSolveDate(LocalDateTime.now());
            solvePosition.setSolveKey(solveKey);
            solvePosition.setState(OBPSolveState.solved.toString());
            obpSolvePositionRepository.save(solvePosition);
        }
    }

    @Override
    public List<Map> getPodFace(List<String> names,String obpStationId) {
        OBPStation obpStation= obpStationRepository.retrieve(obpStationId);
        WorkStation workStation=obpStation.getWorkStation();
        workStation.setCallPod(true);
        workStationRepository.saveAndFlush(workStation);
        List<Map> podPosition = new ArrayList<>();
        for(String name:names){
            podPosition.addAll(solveDamageQuery.getPodFace(name,applicationContext.getCurrentWarehouse()));
        }
        return  podPosition;
    }

    @Override
    public SolveDamagedDTO create(SolveDamagedDTO dto) {
        return null;
    }

    @Override
    public void delete(String id) {}

    @Override
    public SolveDamagedDTO update(SolveDamagedDTO dto) {
        return null;
    }

    @Override
    public SolveDamagedDTO retrieve(String id) {
        return null;
    }

    @Override
    public List<SolveDamagedDTO> getBySearchTerm(String searchTerm, Sort sort) {
        return null;
    }

    @Override
    public Page<SolveDamagedDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        return null;
    }
}
