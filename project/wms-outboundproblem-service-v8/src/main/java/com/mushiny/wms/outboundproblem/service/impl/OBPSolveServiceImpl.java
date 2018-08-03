package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.business.*;
import com.mushiny.wms.outboundproblem.business.common.OutBoundProblemCommonBusiness;
import com.mushiny.wms.outboundproblem.business.dto.SolveShipmentPositionDTO;
import com.mushiny.wms.outboundproblem.business.enums.*;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitState;
import com.mushiny.wms.outboundproblem.crud.common.dto.StorageLocationDTO;
import com.mushiny.wms.outboundproblem.crud.common.mapper.StorageLocationMapper;
import com.mushiny.wms.outboundproblem.crud.dto.*;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPCheckStateMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPSolveMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBProblemCheckMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.OBPSolveQuery;
import com.mushiny.wms.outboundproblem.query.SolveDamageQuery;
import com.mushiny.wms.outboundproblem.query.UnitLoadShipmentQuery;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.query.hql.OBProblemQuery;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.service.OBPCellService;
import com.mushiny.wms.outboundproblem.service.OBPSolveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class OBPSolveServiceImpl implements OBPSolveService {

    private final OBPSolveRepository obpSolveRepository;
    private final OBPSolveMapper obpSolveMapper;
    private final OBPSolveBusiness obpSolveBusiness;
    private final OBProblemQuery obProblemQuery;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final OutBoundProblemCommonBusiness outBoundProblemCommonBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final OBPWallRepository obpWallRepository;
    private final OBPCellRepository obpCellRepository;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final OBPStationRepository obpStationRepository;
    private final OBPCellService obpCellService;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBProblemRepository obProblemRepository;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final WorkStationRepository workStationRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final OBProblemCheckMapper obProblemCheckMapper;
    private final OBPCheckStateMapper obpCheckStateMapper;
    private final OBPSolveQuery obpSolveQuery;
    private final UnitLoadRepository unitLoadRepository;
    private final BuildBusiness buildBusiness;
    private final ItemDataRepository itemDataRepository;
    private final UnitLoadShipmentQuery unitLoadShipmentQuery;
    private final LocationRepository locationRepository;
    private final OBPCellBusiness obpCellBusiness;
    private final static JsonParser parser= JsonParserFactory.getJsonParser();
    private final SolveDamageQuery solveDamageQuery;
    private final PickingUnitLoadRepository pickingUnitLoadRepository;
    private final UnitLoadBusiness unitLoadBusiness;

    @Autowired
    public OBPSolveServiceImpl(OBPSolveRepository obpSolveRepository,
                               OBPSolveMapper obpSolveMapper,
                               OBPSolveBusiness obpSolveBusiness,
                               OBProblemQuery obProblemQuery,
                               ApplicationContext applicationContext,
                               StorageLocationRepository storageLocationRepository,
                               StorageLocationTypeRepository storageLocationTypeRepository,
                               StorageLocationMapper storageLocationMapper,
                               OutBoundProblemCommonBusiness outBoundProblemCommonBusiness,
                               StockUnitRepository stockUnitRepository,
                               OBPWallRepository obpWallRepository,
                               OBPCellRepository obpCellRepository,
                               OBPSolvePositionRepository obpSolvePositionRepository,
                               OBPStationRepository obpStationRepository,
                               OBPCellService obpCellService,
                               OBPShipmentSerialNoRepository obpShipmentSerialNoRepository,
                               CustomerShipmentRepository customerShipmentRepository,
                               OBProblemRepository obProblemRepository,
                               MoveGoodsBusiness moveGoodsBusiness,
                               WorkStationRepository workStationRepository,
                               CustomerShipmentPositionRepository customerShipmentPositionRepository,
                               OBProblemCheckMapper obProblemCheckMapper,
                               OBPCheckStateMapper obpCheckStateMapper,
                               OBPSolveQuery obpSolveQuery,
                               UnitLoadRepository unitLoadRepository,
                               BuildBusiness buildBusiness,
                               ItemDataRepository itemDataRepository,
                               UnitLoadShipmentQuery unitLoadShipmentQuery,
                               LocationRepository locationRepository,
                               OBPCellBusiness obpCellBusiness,
                               SolveDamageQuery solveDamageQuery,
                               PickingUnitLoadRepository pickingUnitLoadRepository, UnitLoadBusiness unitLoadBusiness) {
        this.obpSolveRepository = obpSolveRepository;
        this.obpSolveMapper = obpSolveMapper;
        this.obpSolveBusiness = obpSolveBusiness;
        this.obProblemQuery = obProblemQuery;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.outBoundProblemCommonBusiness = outBoundProblemCommonBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.obpWallRepository = obpWallRepository;
        this.obpCellRepository = obpCellRepository;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.obpStationRepository = obpStationRepository;
        this.obpCellService = obpCellService;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obProblemRepository = obProblemRepository;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.workStationRepository = workStationRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.obProblemCheckMapper = obProblemCheckMapper;
        this.obpCheckStateMapper = obpCheckStateMapper;
        this.obpSolveQuery = obpSolveQuery;
        this.unitLoadRepository = unitLoadRepository;
        this.buildBusiness = buildBusiness;
        this.itemDataRepository = itemDataRepository;
        this.unitLoadShipmentQuery = unitLoadShipmentQuery;
        this.locationRepository = locationRepository;
        this.obpCellBusiness = obpCellBusiness;
        this.solveDamageQuery = solveDamageQuery;
        this.pickingUnitLoadRepository = pickingUnitLoadRepository;
        this.unitLoadBusiness = unitLoadBusiness;
    }

    @Override
    public OBPSolveDTO create(OBPSolveDTO dto) {
        OBPSolve entity = obpSolveMapper.toEntity(dto);
        return obpSolveMapper.toDTO(obpSolveRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        OBPSolve entity = obpSolveRepository.retrieve(id);
        obpSolveRepository.delete(entity);
    }

    @Override
    public OBPSolveDTO update(OBPSolveDTO dto) {
        OBPSolve entity = obpSolveRepository.retrieve(dto.getId());
        if (!(entity.getObpCell().getName().equalsIgnoreCase(dto.getCell()))) {
            checkCell(entity.getWarehouseId(), entity.getObpWall().getId(), entity.getObpCell().getName());
        }
        obpSolveMapper.updateEntityFromDTO(dto, entity);
        return obpSolveMapper.toDTO(obpSolveRepository.save(entity));
    }

    @Override
    public OBPSolveDTO retrieve(String id) {
        return obpSolveMapper.toDTO(obpSolveRepository.retrieve(id));
    }

    @Override
    public List<OBPSolveDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<OBPSolve> entities = obpSolveRepository.getBySearchTerm(searchTerm, sort);
        return obpSolveMapper.toDTOList(entities);
    }

    @Override
    public Page<OBPSolveDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        Page<OBPSolve> entities = obpSolveRepository.getBySearchTerm(searchTerm, pageable);
        return obpSolveMapper.toDTOPage(pageable, entities);
    }

    @Override
    //全局搜索 根据状态，用户，搜索内容，开始时间或结束时间
    public List<OBPCheckStateDTO> getSolveBySeek(String state, String userName, String seek,
                                                 String startDate, String endDate) {
        List<OBPCheckStateDTO> list = obpCheckStateMapper.toDTOList(
                obProblemQuery.getSolveBySeek(state, userName, seek, startDate, endDate));
        //如果 状态是"PROCESS" 处理中， 将 问题记录表中的数据，添加到 历史记录数据中一起返给前台。
        if (state.equalsIgnoreCase("PROCESS")) {
            List<OBProblemCheck> inboundProblems = obProblemQuery.
                    getBySeek(state, userName, seek, startDate, endDate);
            list.addAll(obpCheckStateMapper.toSolveList(inboundProblems));
        }
        return list;
    }


    @Override
    public List<OBPSolveDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "amount"));
        List<OBPSolve> entities = obpSolveRepository.getList(applicationContext.getCurrentClient(), sort);
        return obpSolveMapper.toDTOList(entities);
    }

    @Override
    public List<OBPSolveDTO> getProblemsByShipmentNo(String obpStationId, String obpWallId, String shipmentNo, String state) {
        shipmentNo= obpSolveBusiness.scanShipmentNo(shipmentNo);
        List<OBPSolve> entities = obpSolveBusiness.getOBPSolve(obpStationId, obpWallId, shipmentNo, state);
        List<OBPSolveDTO> solveProblemList = obpSolveMapper.toDTOList(entities);
        if (!solveProblemList.isEmpty()) {
            List<OBPSolveDTO> deletes = new ArrayList<>();
            for (OBPSolveDTO obpsolve : solveProblemList) {
                if (obpsolve.getProblemType() != null) {
                    if (obpsolve.getProblemType().equals(SolveResoult.PRINT_SKU_REPAIR.toString()))
                        obpsolve.setProblemType(ProblemType.UNABLE_SCAN_SKU.toString());
                } else {
                    if (obpsolve.getState() != null && obpsolve.getState().equals(OutBoundProblemException.EX_SCANNING_SHIPMENT_IS_NORMAL.getName())) {
                        obpsolve.setShipmentNo(shipmentNo);
                        obpsolve.setShipmentState(outBoundProblemCommonBusiness.getShipmetnState(customerShipmentRepository.getByShipmentNo(shipmentNo)));
                    }
                    //这段是后来加的,为了在搜索未完成时候过滤掉已完成订单，同时显示正常订单还没转问题订单的问题订单
                    //对于正常订单obProblems.size()==0
                    List<OBProblem> obProblems = obProblemRepository.getByShipment(applicationContext.getCurrentWarehouse(), obpsolve.getShipmentNo());
                    if (obProblems.size() > 0 && obpsolve.getState() != null
                            && !obpsolve.getState().equals(OutBoundProblemException.EX_SCANNING_SHIPMENT_IS_NORMAL.getName())
                            && !obpsolve.getState().equals(SolveResoult.DELETE_ORDER_CUSTOMER.toString())
                            && !obpsolve.getState().equals(OutBoundProblemException.EX_SCANNING_SHIPMENT_NOT_BIND_CELL.getName())) {
                        deletes.add(obpsolve);
                    }
                }
//                if (state.equals(OBPSolveState.solved.toString())) {
//                    obpsolve.setCell("");
//                }
            }
            if (!deletes.isEmpty())
                solveProblemList.removeAll(deletes);
        }
        if (!solveProblemList.isEmpty()) {
            for (int i = 0; i < solveProblemList.size(); i++) {
                String problemType = problemType(solveProblemList.get(i).getProblemType());
                for (int j = i + 1; j < solveProblemList.size(); j++) {
                    if (solveProblemList.get(i).getShipmentNo().equals(solveProblemList.get(j).getShipmentNo())) {
                        if(problemType==null) problemType="";
                        else  problemType = problemType + " " + problemType(solveProblemList.get(j).getProblemType()) + " ";
                        solveProblemList.remove(solveProblemList.get(j));
                        j--;
                    }
                }
                solveProblemList.get(i).setProblemType(problemType);
            }
        }
        return solveProblemList;
    }

    private String problemType(String problemType){
        String type="";
        if(problemType!=null) {
            switch (problemType) {
                case "DAMAGED":
                    type = "残损";
                    break;
                case "UNABLE_SCAN_SKU":
                    type = "条码无法扫描";
                    break;
                case "UNABLE_SCAN_SN":
                    type = "序列号无法扫描";
                    break;
                case "LOSE":
                    type = "少货";
                    break;
                default:
                    type = "";
                    break;
            }
        }
        return type;
    }

    @Override
    public List<SolveShipmentPositionDTO> getSolveShipmentPositionByShipmentNo(String shipmentNo) {
        return obpSolveBusiness.getSolveShipmentPositionByShipmentNo(shipmentNo);
    }

    @Override
    public SolveShipmentPositionDTO scanItemByShipmentAndItem(String toCellName, String shipmentNo, String itemNo) {
        return obpSolveBusiness.scanItemByShipmentAndItem(toCellName, shipmentNo, itemNo);
    }

    @Override
    public void scanSerialNo(String toCellName, String shipmentNo, String itemNo, String serialNo) {
        obpSolveBusiness.checkSerialNo(serialNo, shipmentNo);
        //SN扫描成功，该商品的已扫描次数加1
        OBPSolve entity = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemNo);
        List<OBPSolve> entityProblems = obpSolveRepository.getProblemGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, itemNo);
        OBPSolve entityProblem = null;
        if (!entityProblems.isEmpty()) {
            for (OBPSolve solve : entityProblems) {
                if (solve.getObproblem().getProblemType().equals(ProblemType.LOSE.toString())) {
                    continue;
                }
                if (entity.getScaned().equals(OBPSolveState.unscaned.toString())) {
                    entityProblem = solve;
                    break;
                }
            }
        }
        //此类商品所有的问题数
        BigDecimal amountProblem = BigDecimal.ZERO;
        for (OBPSolve solve : entityProblems) {
            amountProblem = amountProblem.add(solve.getAmountProblem());
        }
        entity.setAmountScaned(entity.getAmountScaned().add(BigDecimal.ONE));
        //正常商品数量
        BigDecimal num = entity.getAmountShipment().subtract(amountProblem);
        if (entityProblem != null && entity.getAmountScaned().compareTo(num) > 0) {
            entityProblem.setAmountScanedProblem(entityProblem.getAmountScanedProblem().add(BigDecimal.ONE));
        }
        if (entity.getAmountScaned().compareTo(entity.getAmountShipment()) > 0) {
            entity.setAmountScaned(entity.getAmountShipment());
        }
        //如果需要扫序列号的无法扫描商品都能扫描，或者是序列号无法扫描的商品可以扫描，将问题数改为0
        if (entityProblem!=null&&entityProblem.getAmountProblem().compareTo(entityProblem.getAmountScanedProblem()) == 0){
            OBProblem obProblem=null;
            if(entityProblem.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SKU.toString())){
                obProblem = obProblemRepository.getScanskuByItem(applicationContext.getCurrentWarehouse(), entityProblem.getCustomerShipment().getId(), entityProblem.getItemData().getId());
            }else if(entityProblem.getObproblem().getProblemType().equals(ProblemType.UNABLE_SCAN_SN.toString())){
                obProblem = obProblemRepository.getByShipmentAndItemSN(applicationContext.getCurrentWarehouse(), entityProblem.getCustomerShipment().getId(), entityProblem.getItemData().getId());
            }
            if(obProblem!=null) {
//                obProblem.setAmount(BigDecimal.ZERO);
//                obProblemRepository.save(obProblem);
                //如果序列号，条码无法扫描都能扫描，将状态改为solved
                entityProblem.setState(OBPSolveState.solved.toString());
                obpSolveRepository.save(entityProblem);
            }
        }
        obpSolveBusiness.updateScaned(entity, entityProblem);

        //记录已扫描的SN
        OBPShipmentSerialNo shipmentSerialNo = obpShipmentSerialNoRepository.getSerialNo(
                applicationContext.getCurrentWarehouse(), serialNo, shipmentNo);
        shipmentSerialNo.setScaned(true);
        shipmentSerialNo.setState(StockUnitState.INVENTORY.getName());
        obpShipmentSerialNoRepository.save(shipmentSerialNo);

        obpSolveBusiness.addRecord(shipmentNo, toCellName);
    }

    @Override
    public StorageLocationDTO scanProcessContainer(String containerName) {
        StorageLocation storageLocation = Optional
                .ofNullable(storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(), containerName, InventoryState.PENDING.toString()))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_IS_CONFORMITY.getName(), containerName));

        return storageLocationMapper.toDTO(storageLocation);
    }

    //生成hot pick扫描拣货车牌
    @Override
    public OBPCellDTO getCellByContainer(String containerName, String obpWallId) {
        StorageLocation storageLocation = outBoundProblemCommonBusiness.getContainer(containerName, InventoryState.INVENTORY.toString());
        UnitLoad unitLoad=unitLoadRepository.movingGetByStorageLocation(storageLocation);
        if (unitLoad!=null&&Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)) {
            throw new ApiException(OutBoundProblemException.EX_IT_UNITLOAD_IS_LOCKED.getName(), containerName);
        }
        //获得此UNITLOAD对应的PickingUnitLoad
       List<PickingUnitLoad> pickingUnitLoads=pickingUnitLoadRepository.getByUnitLoad(applicationContext.getCurrentWarehouse(),unitLoad);
       for(PickingUnitLoad pickingUnitLoad:pickingUnitLoads){
           if(pickingUnitLoad.getPickingOrder()!=null){
               PickingOrder pickingOrder= pickingUnitLoad.getPickingOrder();
               if(!("PP-HOT_PICK").equals(pickingOrder.getProcessPath().getName())){
                   throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), containerName);
               }
           }
       }
        //得到车牌内的所有商品
        List<StockUnit> stockUnits = stockUnitRepository.getByStorageLocation(storageLocation);
        OBPCellDTO cellDTO = obpSolveBusiness.getCellByWallId(obpWallId);
        List<OBPCellPositionDTO> cellPositionDTOS = cellDTO.getObpCellPositions();

        //循环拣货回来后车牌内的每件商品
        for (StockUnit stockUnit : stockUnits) {
            for (OBPCellPositionDTO cellPositionDTO : cellPositionDTOS) {
                //同一个问题格下同种商品所有问题
                List<OBPSolve> solves = obpSolveRepository.getByCellAndItem(applicationContext.getCurrentWarehouse(), cellPositionDTO.getName(), stockUnit.getItemData().getItemNo(), obpWallId);
                //不为空，说明所扫描商品在此格子中
                if(!solves.isEmpty()) {
                    //判断当前的库存是否是为该订单生成的HOTPICK
                    String unitLoadId = stockUnit.getUnitLoad().getId();
                    List<Object[]> objects = unitLoadShipmentQuery.selectUnitloadShipment(unitLoadId, solves.get(0).getCustomerShipment().getId());
                    //此订单是否生成了hotPick
                    if (objects.size() > 0 ||solves.get(0).getEntityLock()==1) {
                        cellDTO.setHasGoods(true);
                        BigDecimal amountProblem = BigDecimal.ZERO;
                        BigDecimal amountScanedProblem = BigDecimal.ZERO;
                        for (OBPSolve solve : solves) {
                            amountProblem = amountProblem.add(solve.getAmountProblem());
                            amountScanedProblem = amountScanedProblem.add(solve.getAmountScanedProblem());
                            solve.setEntityLock(Constant.GENERAL);
                            obpSolveRepository.save(solve); //entityLock=1为生成hotPick标志
                        }
                        //同一个问题格下所有问题商品，不同商品也存在问题
                        List<OBPSolve> solveList = obpSolveRepository.getProblemByCell(applicationContext.getCurrentWarehouse(), cellPositionDTO.getName(), obpWallId);
                        BigDecimal problemNum = BigDecimal.ZERO;
                        for (OBPSolve obp : solveList) {
                            problemNum = problemNum.add(obp.getAmountProblem());
                        }
                        cellPositionDTO.setGoodsInCell(true);
                        //同一个问题格下不同商品也存在问题，问题数累加
                        cellPositionDTO.setAmountProblem(cellPositionDTO.getAmountProblem().add(amountProblem));
                        cellPositionDTO.setAmountScanedProblem(cellPositionDTO.getAmountScanedProblem().add(amountScanedProblem));
                        //问题数>实际问题数
                        if (cellPositionDTO.getAmountProblem().compareTo(problemNum) > 0) {
                            cellPositionDTO.setAmountProblem(cellPositionDTO.getAmountProblem().subtract(amountProblem));
                            cellPositionDTO.setAmountScanedProblem(cellPositionDTO.getAmountScanedProblem().subtract(amountScanedProblem));
                        }
                        cellPositionDTO.setStorageLocation(storageLocationMapper.toDTO(stockUnit.getUnitLoad().getStorageLocation()));
                    }
                }
            }
        }
        if(!cellDTO.isHasGoods())
            throw new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_HAS_HOTICK_GOODS.getName());
        return cellDTO;
    }

    //生成hot pick扫描商品
    @SuppressWarnings("Duplicates")
    @Override
    public OBPCellDTO getCellByContainerAndItemNo(String containerName, String obpWallId, String itemNo) {
        List<ItemData> itemDatas = itemDataRepository.getByItemNoSkuNo(applicationContext.getCurrentWarehouse(),itemNo);
        //多条码商品
        if (itemDatas.size() > 1) {
            throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_MORE_THAN_ONE.getName(), itemNo);
        }else if(itemDatas.isEmpty()){
            throw  new ApiException(OutBoundProblemException.EX_IT_SKU_NOT_FOUND.getName());
        }
        //检测商品是否存在问题处理中
        List<OBPSolve> solves = obpSolveRepository.getCellByItemData(applicationContext.getCurrentWarehouse(), itemDatas.get(0).getItemNo(), obpWallId);
        if(solves.isEmpty())
            throw  new ApiException(OutBoundProblemException.EX_SKU_NOT_MATCH.getName());

        OBPCellDTO cellDTO = getCellByContainer(containerName, obpWallId);
        //判断该商品是否需要输入序列号
        if(solves.get(0).getItemData().getSerialRecordType().equals("")||solves.get(0).getItemData().getSerialRecordType().equals(SerialNoRecordType.NO_RECORD.toString())) {
            cellDTO.setSerialRecordType(SerialNoRecordType.NO_RECORD.toString());

            List<OBPCellPositionDTO> cellPositionDTOS = cellDTO.getObpCellPositions();
            //给车内商品是否全部扫描完，设定初始值
            String scaned = OBPSolveState.scaned.toString();
            for (OBPCellPositionDTO oBPCellPositionDTO : cellPositionDTOS) {
                //判断当前所扫描的商品在哪个格子中，同一个问题格，同种商品会存在不同问题
                List<OBPSolve> obpSolves = obpSolveRepository.getByCellAndItemAmount(applicationContext.getCurrentWarehouse(), oBPCellPositionDTO.getName(), itemDatas.get(0).getItemNo(), obpWallId);
                //1.不为空说明在当前cell格子中 2.此货筐拣了此问题格生成HOTPICK的商品
                if (!obpSolves.isEmpty() && oBPCellPositionDTO.isGoodsInCell()) {
                    for (OBPSolve entity : obpSolves) {
                        OBPSolve obpSolve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(), entity.getItemData().getItemNo(), entity.getCustomerShipment().getShipmentNo());
                        obpSolve.setAmountScaned(obpSolve.getAmountScaned().add(BigDecimal.ONE));
                        obpSolveRepository.save(obpSolve);
                        entity.setAmountScanedProblem(entity.getAmountScanedProblem().add(BigDecimal.ONE));
                        //更改问题为solved
                        if (entity.getAmountScanedProblem().compareTo(entity.getAmountProblem()) == 0) {
                            entity.setState(OBPSolveState.solved.toString());
                        }
                        obpSolveRepository.save(entity);
                        oBPCellPositionDTO.setAmountScanedProblem(oBPCellPositionDTO.getAmountScanedProblem().add(BigDecimal.ONE));

                        //每扫描一次，需将商品从筐中移到CELL中，同时将生成移货历史记录，更改库存记录
                        //  StorageLocation toStorageLocation = storageLocationRepository.retrieve(oBPCellPositionDTO.getStorageLocation().getId());
                        OBPCell obpCell = entity.getObpCell();
                        StorageLocation toStorageLocation = buildStorageLocation(obpCell);
                        StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), containerName);
                        UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(toStorageLocation);
                        if (unitLoad == null) {
                            unitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
                        }
                        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(fromStorageLocation);
                        if (sourceUnitLoad == null) {
                            throw new ApiException(OutBoundProblemException
                                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName());
                        }
                        moveGoodsBusiness.movingGoods(sourceUnitLoad,unitLoad,toStorageLocation,obpSolve.getItemData(),BigDecimal.ONE,null,null);

                        unitLoadShipmentQuery.deleteUnitloadShipment(entity.getCustomerShipment().getId());
                        unitLoadShipmentQuery.insertUnitloadShipment(unitLoad.getId(), entity.getCustomerShipment().getId());
                        cellDTO.setInCellName(oBPCellPositionDTO.getName().split("-")[1]);   // 扫描商品判断放在哪个cell格
                        cellDTO.setInCellyPos(oBPCellPositionDTO.getyPos());
                        break; //每次只扫描一件商品
                    }
                    break; //每次只扫描一件商品
                }
            }
            for (OBPCellPositionDTO oBPCellPositionDTO : cellPositionDTOS) {
                //为了显示已经处理完成的问题格
                if (oBPCellPositionDTO.getAmountProblem().compareTo(BigDecimal.ZERO) > 0
                        && oBPCellPositionDTO.getAmountScanedProblem().compareTo(oBPCellPositionDTO.getAmountProblem()) == 0) {
                    oBPCellPositionDTO.setGoodsInCell(true);
                    oBPCellPositionDTO.setScaned(true);
                }
                //传给前台显示返回或者结束按钮
                if (oBPCellPositionDTO.isGoodsInCell()) {
                    if (!oBPCellPositionDTO.isScaned()) {
                        scaned = OBPSolveState.unscaned.toString();
                    }
                }
            }
            cellDTO.setScaned(scaned);
        }else{
            cellDTO.setSerialRecordType(SerialNoRecordType.ALWAYS_RECORD.toString());
            cellDTO.setItemName(itemDatas.get(0).getName());
            cellDTO.setItemNo(itemDatas.get(0).getItemNo());
        }
        return cellDTO;
    }

    //生成hot pick扫描商品序列号
    @SuppressWarnings("Duplicates")
    @Override
    public OBPCellDTO scanHotPickSn(String containerName, String obpWallId, String itemNo, String serialNo) {
        //判断序列号是否存在
      StockUnit stockUnit= stockUnitRepository.getByItemNoAndSn(serialNo,containerName,itemNo);
      if(stockUnit==null)
          throw new ApiException(OutBoundProblemException.EX_SN_NOT_FOUND.getName());
        OBPCellDTO cellDTO = getCellByContainer(containerName, obpWallId);
        List<OBPCellPositionDTO> cellPositionDTOS = cellDTO.getObpCellPositions();
        //给车内商品是否全部扫描完，设定初始值
        String scaned = OBPSolveState.scaned.toString();
        for (OBPCellPositionDTO oBPCellPositionDTO : cellPositionDTOS) {
            //判断当前所扫描的商品在哪个格子中，同一个问题格，同种商品会存在不同问题
            List<OBPSolve> obpSolves = obpSolveRepository.getByCellAndItemAmount(applicationContext.getCurrentWarehouse(), oBPCellPositionDTO.getName(),stockUnit.getItemData().getItemNo(),obpWallId);
            //1.不为空说明在当前cell格子中 2.此货筐拣了此问题格生成HOTPICK的商品
            if (!obpSolves.isEmpty() && oBPCellPositionDTO.isGoodsInCell()) {
                for (OBPSolve entity : obpSolves) {
                    OBPSolve obpSolve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(), entity.getItemData().getItemNo(), entity.getCustomerShipment().getShipmentNo());
                    obpSolve.setAmountScaned(obpSolve.getAmountScaned().add(BigDecimal.ONE));
                    obpSolveRepository.save(obpSolve);
                    entity.setAmountScanedProblem(entity.getAmountScanedProblem().add(BigDecimal.ONE));
                    if (entity.getAmountScanedProblem().compareTo(entity.getAmountProblem()) == 0) {
                        entity.setState(OBPSolveState.solved.toString());
                    }
                    obpSolveRepository.save(entity);
                    oBPCellPositionDTO.setAmountScanedProblem(oBPCellPositionDTO.getAmountScanedProblem().add(BigDecimal.ONE));
                    OBPCell obpCell = entity.getObpCell();
                    StorageLocation toStorageLocation = buildStorageLocation(obpCell);
                    StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), containerName);
                   // moveGoodsBusiness.moving(fromStorageLocation, toStorageLocation, entity.getItemData(), BigDecimal.ONE,null);
                    //更新UNITLOAD_SHIPMENT
                    UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(toStorageLocation);
                    if (unitLoad == null) {
                        unitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
                    }
                    UnitLoad sourceUnitLoad = unitLoadRepository.movingGetByStorageLocation(fromStorageLocation);
                    if (sourceUnitLoad == null) {
                        throw new ApiException(OutBoundProblemException
                                .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName());
                    }
                    moveGoodsBusiness.movingGoodsToContainer(sourceUnitLoad,unitLoad,obpSolve.getItemData(),BigDecimal.ONE,serialNo,null);

                    unitLoadShipmentQuery.deleteUnitloadShipment(entity.getCustomerShipment().getId());
                    unitLoadShipmentQuery.insertUnitloadShipment(unitLoad.getId(), entity.getCustomerShipment().getId());
                    cellDTO.setInCellName(oBPCellPositionDTO.getName().split("-")[1]);   // 扫描商品判断放在哪个cell格
                    cellDTO.setInCellyPos(oBPCellPositionDTO.getyPos());
                    break; //每次只扫描一件商品
                }
                break; //每次只扫描一件商品
            }
        }
        for (OBPCellPositionDTO oBPCellPositionDTO : cellPositionDTOS) {
            //为了显示已经处理完成的问题格
            if (oBPCellPositionDTO.getAmountProblem().compareTo(BigDecimal.ZERO) > 0
                    && oBPCellPositionDTO.getAmountScanedProblem().compareTo(oBPCellPositionDTO.getAmountProblem()) == 0) {
                oBPCellPositionDTO.setGoodsInCell(true);
                oBPCellPositionDTO.setScaned(true);
            }
            //传给前台显示返回或者结束按钮
            if (oBPCellPositionDTO.isGoodsInCell()) {
                if (!oBPCellPositionDTO.isScaned()) {
                    scaned = OBPSolveState.unscaned.toString();
                }
            }
        }
        cellDTO.setScaned(scaned);
        return cellDTO;
    }

    @Override
    public List<OBPSolveDTO> getOBProblemByCellName(String obpWallId, String cellName) {
        List<OBPSolve> entities = obpSolveRepository.getByCell(applicationContext.getCurrentWarehouse(), obpWallId, cellName);
        return obpSolveMapper.toDTOList(entities);
    }

    @Override
    public void signOutOBPStation(String obpStationId) {
        OBPStation station = obpStationRepository.retrieve(obpStationId);
        station.setOperator(null);
        obpStationRepository.save(station);
        WorkStation workStation = station.getWorkStation();
        workStation.setOperator(null);
        workStation.setStationName(null);
        workStation.setCallPod(false);
        workStationRepository.save(workStation);
    }
    //分配货位放商品
    @Override
    public void assignLocation(String name, String solveId, String obpLocationId,String obpWallId) {
    }

    @Override
    public PrintShipmentPositionDTO printOrderByShipmentNo(String shipmentNo,String solveKey) {
        //拆单发货打印订单
        List<OBPSolvePosition> obpSolvePositions=new ArrayList<>();
        if(!solveKey.isEmpty()){
            obpSolvePositions= obpSolvePositionRepository.getBySolveKeyAndShipmentNo(applicationContext.getCurrentWarehouse(),shipmentNo);
        }
        //获取订单里面所有的商品信息
        List<OBPSolve> obpSolves=obpSolveRepository.getAllGoodsByShipmentNo(applicationContext.getCurrentWarehouse(),shipmentNo);
        if(obpSolves.isEmpty()){
            throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.toString());
        }
        PrintShipmentPositionDTO printShipmentPosition=new PrintShipmentPositionDTO();
        List<SolveShipmentPositionDTO> solveShipmentPositions=new ArrayList<>();
        printShipmentPosition.setShipmentNo(shipmentNo);
        for(OBPSolve obpSolve:obpSolves){
            SolveShipmentPositionDTO solveShipmentPosition=new SolveShipmentPositionDTO();
            solveShipmentPosition.setItemName(obpSolve.getItemData().getName());
            solveShipmentPosition.setItemNo(obpSolve.getItemData().getItemNo());
            solveShipmentPosition.setSkuNo(obpSolve.getItemData().getSkuNo());
            solveShipmentPosition.setAmount(obpSolve.getAmountShipment());
            //拆单发货
            if(!solveKey.isEmpty()){
               for(OBPSolvePosition obpSolvePosition:obpSolvePositions) {
                   if (obpSolvePosition.getItemDataNo().equals(obpSolve.getItemData().getItemNo())) {
                       solveShipmentPosition.setAmount(obpSolve.getAmountShipment().subtract(obpSolvePosition.getObpSolve().getAmountProblem()));
                       break;
                   }
               }
            }
            //是否有效期商品
            solveShipmentPosition.setLotMandatory(obpSolve.getItemData().isLotMandatory());
            //有效期类型
            solveShipmentPosition.setLotType(obpSolve.getItemData().getLotType());
            //是否有序列号
            solveShipmentPosition.setSerialRecordType(obpSolve.getItemData().getSerialRecordType());
            solveShipmentPosition.setDepth(obpSolve.getItemData().getDepth());
            solveShipmentPosition.setHeight(obpSolve.getItemData().getHeight());
            solveShipmentPosition.setWeight(obpSolve.getItemData().getWeight());
            solveShipmentPositions.add(solveShipmentPosition);
            if(solveShipmentPosition.getAmount().compareTo(BigDecimal.ZERO)==0){
                solveShipmentPositions.remove(solveShipmentPosition);
            }
        }
            printShipmentPosition.setSolveShipmentPositions(solveShipmentPositions);
        return printShipmentPosition;
    }

    @Override
    public void batchToPacking(String datas) {
       Map<String,Object> map= parser.parseMap(datas);
       String dtos=(String)map.get("shipmentDatas");
       String obpStationId=(String)map.get("obpStationId");
       List list= parser.parseList(dtos);
       for(Object obj:list){
             Map<String,String> m=(Map<String,String>)obj;
             String shipmentNo=""; String cell="";
             for(String k:m.keySet()){
                 if(k.equals("shipmentNo")) shipmentNo=m.get(k);
                 else cell=m.get(k);
             }
        obpCellBusiness.unbindCell(shipmentNo,obpStationId,cell,SolveResoult.RELEASE_CELL.toString());
        }
    }

    @Override
    public boolean verifySignOutOBPStation(String obpStationId) {
        boolean bl = false;
        OBPStation obpStation = obpStationRepository.findOne(obpStationId);
        List<RcsTrip> rcsTripList = obpStationRepository.getRcsTripState(obpStation.getWorkStation().getId(),applicationContext.getCurrentWarehouse());
        if (rcsTripList.size() > 0){
            bl=true;
        }
        return bl;
    }

    @Override
    public List<Map> getLocations(List<String> cellNames,String workStationId) {
        WorkStation workStation= workStationRepository.retrieve(workStationId);
        workStation.setCallPod(true);
        workStationRepository.save(workStation);
        List<Map> podPosition = new ArrayList<>();
        for(String cell:cellNames){
            List<OBPLocation> obpLocations=locationRepository.getByCellName(applicationContext.getCurrentWarehouse(),cell);
            for(OBPLocation obpLocation:obpLocations){
                obpLocation.setCallPod(false);
                locationRepository.save(obpLocation);
                podPosition.addAll(solveDamageQuery.getPodFace(obpLocation.getLocation(),applicationContext.getCurrentWarehouse()));
            }
        }
        //对List<Map>进行去重
        List<Map> podPosition2 = new ArrayList<>();
        Set<Map> setMap = new HashSet<>();
        for(Map pod: podPosition){
            if(setMap.add(pod)){
                podPosition2.add(pod);
            }
        }
        return podPosition2;
    }

    @Override
    public SolveShipmentPositionDTO scanPickingGoods(String location, String itemNo) {
        List<ItemData> itemDatas = itemDataRepository.getByItemNoSkuNo(applicationContext.getCurrentWarehouse(), itemNo);
        if (itemDatas.size() > 1) {
            throw new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_MORE_THAN_ONE.getName(), itemNo);
        } else if (itemDatas.isEmpty()) {
            throw new ApiException(OutBoundProblemException.EX_IT_SKU_NOT_FOUND.getName());
        }
        //扫描的商品不在问题处理里
        List<OBPLocation> locations = locationRepository.getByLocationAndItemNo(applicationContext.getCurrentWarehouse(), location, itemDatas.get(0).getItemNo());
        if (locations.isEmpty())
            throw new ApiException(OutBoundProblemException.EX_SKU_NOT_MATCH.getName());
        SolveShipmentPositionDTO solveDTO = new SolveShipmentPositionDTO();
        if (itemDatas.get(0).getSerialRecordType().equals("")||itemDatas.get(0).getSerialRecordType().equals(SerialNoRecordType.NO_RECORD.toString())) {
            solveDTO.setSerialRecordType(SerialNoRecordType.NO_RECORD.toString());
            OBPSolve obpSolve = null;
            OBPLocation obpLocation = null;
            for (OBPLocation loc : locations) {
                if (loc.getAmountScaned() < loc.getAmount()) {
                    obpSolve = obpSolveRepository.retrieve(loc.getSolveId());
                    obpLocation = loc;
                    break;
                }
            }
            obpSolve.setAmountScanedProblem(obpSolve.getAmountScanedProblem().add(BigDecimal.ONE));
            obpSolveRepository.save(obpSolve);
            //正常商品扫描数加1
            OBPSolve solve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(), obpSolve.getItemData().getItemNo(), obpSolve.getCustomerShipment().getShipmentNo());
            solve.setAmountScaned(solve.getAmountScaned().add(BigDecimal.ONE));
            obpSolveRepository.save(solve);
            //问题商品已扫描数加1
            obpLocation.setAmountScaned(obpLocation.getAmountScaned() + 1);
            if (obpLocation.getAmount() == obpLocation.getAmountScaned()) {
                obpLocation.setState(OBPSolveState.solved.toString());
            }
            locationRepository.save(obpLocation);
            if (obpSolve.getAmountScanedProblem().compareTo(obpSolve.getAmountProblem()) == 0) {
                obpSolve.setState(OBPSolveState.solved.toString());
                obpSolveRepository.save(obpSolve);
            }
            OBPCell cell = obpSolve.getObpCell();
            StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), location);
            StorageLocation toStorageLocation=buildStorageLocation(cell);
            //减去reserved数量
            StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), location);
            List<StockUnit> stockUnits = stockUnitRepository.getByItemDataNo(storageLocation, obpLocation.getItemNo());
            for (StockUnit stockUnit : stockUnits) {
                if (stockUnit.getReservedAmount().compareTo(BigDecimal.ZERO) > 0) {
                    stockUnit.setReservedAmount(stockUnit.getReservedAmount().subtract(BigDecimal.ONE));
                    stockUnitRepository.save(stockUnit);
                    break;
                }
            }
            UnitLoad sourceUnitLoad = unitLoadRepository.movingGetByStorageLocation(fromStorageLocation);
            if (sourceUnitLoad == null) {
               throw new ApiException(OutBoundProblemException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName());
            }
            UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(toStorageLocation);
            if (unitLoad == null) {
                unitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
            }
            moveGoodsBusiness.movingGoods(sourceUnitLoad,unitLoad,toStorageLocation,obpSolve.getItemData(),BigDecimal.ONE,null,null);
            unitLoadShipmentQuery.updateUnitloadByShipment(unitLoad.getId(), obpSolve.getCustomerShipment().getId());
        } else {
            solveDTO.setSerialRecordType(SerialNoRecordType.ALWAYS_RECORD.toString());
            solveDTO.setItemName(itemDatas.get(0).getName());
            solveDTO.setItemNo(itemDatas.get(0).getItemNo());
        }
        return solveDTO;
    }

    @Override
    public void scanGoodsSn(String serialNo,String location,String itemNo) {
         //检查此货位此商品此序列号是否存在
        StockUnit stockUnit=stockUnitRepository.getByItemNoAndSn(serialNo,location,itemNo);
        if(stockUnit==null)
            throw new ApiException(OutBoundProblemException.EX_SN_NOT_FOUND.getName());
        if(stockUnit.getReservedAmount().compareTo(BigDecimal.ONE)<0)
            throw new ApiException(OutBoundProblemException.EX_SN_NOT_ASSIGN.getName());
        List<OBPLocation> locations = locationRepository.getByLocationAndItemNo(applicationContext.getCurrentWarehouse(), location,itemNo);
        OBPSolve obpSolve = null;
        OBPLocation obpLocation = null;
        for (OBPLocation loc : locations) {
            if (loc.getAmountScaned() < loc.getAmount()) {
                obpSolve = obpSolveRepository.retrieve(loc.getSolveId());
                obpLocation = loc;
                break;
            }
        }
        obpSolve.setAmountScanedProblem(obpSolve.getAmountScanedProblem().add(BigDecimal.ONE));
        obpSolveRepository.save(obpSolve);
        //正常商品扫描数加1
        OBPSolve solve = obpSolveRepository.getByItemNoShipmentNo(applicationContext.getCurrentWarehouse(), obpSolve.getItemData().getItemNo(), obpSolve.getCustomerShipment().getShipmentNo());
        solve.setAmountScaned(solve.getAmountScaned().add(BigDecimal.ONE));
        obpSolveRepository.save(solve);
        //问题商品已扫描数加1
        obpLocation.setAmountScaned(obpLocation.getAmountScaned() + 1);
        if (obpLocation.getAmount() == obpLocation.getAmountScaned()) {
            obpLocation.setState(OBPSolveState.solved.toString());
        }
        locationRepository.save(obpLocation);
        if (obpSolve.getAmountScanedProblem().compareTo(obpSolve.getAmountProblem()) == 0) {
            obpSolve.setState(OBPSolveState.solved.toString());
            obpSolveRepository.save(obpSolve);
        }
        OBPCell cell = obpSolve.getObpCell();
        StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), location);
        StorageLocation toStorageLocation=buildStorageLocation(cell);
        stockUnit.setReservedAmount(stockUnit.getReservedAmount().subtract(BigDecimal.ONE));
        stockUnitRepository.save(stockUnit);
        UnitLoad sourceUnitLoad = unitLoadRepository.movingGetByStorageLocation(fromStorageLocation);
        if (sourceUnitLoad == null) {
            throw new ApiException(OutBoundProblemException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName());
        }
        UnitLoad unitLoad = unitLoadRepository.movingGetByStorageLocation(toStorageLocation);
        if (unitLoad == null) {
            unitLoad = buildBusiness.buildUnitLoad(toStorageLocation);
        }
        moveGoodsBusiness.movingGoodsToContainer(sourceUnitLoad,unitLoad,stockUnit.getItemData(),BigDecimal.ONE,serialNo,null);
        unitLoadShipmentQuery.updateUnitloadByShipment(unitLoad.getId(), obpSolve.getCustomerShipment().getId());
    }

    @SuppressWarnings("Duplicated")
    private StorageLocation buildStorageLocation(OBPCell cell){
        StorageLocation toStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), cell.getName());
        if (toStorageLocation == null) {
            toStorageLocation = new StorageLocation();
            toStorageLocation.setName(cell.getName());
            toStorageLocation.setxPos(cell.getxPos());
            toStorageLocation.setyPos(cell.getyPos());
            toStorageLocation.setzPos(cell.getzPos());
            toStorageLocation.setOrderIndex(storageLocationRepository.getMaxOrderIndex() + 1);
            toStorageLocation.setWarehouseId(applicationContext.getCurrentWarehouse());
            storageLocationRepository.save(toStorageLocation);
            toStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), cell.getName());
        }
        return toStorageLocation;
    }

    private void checkCell(String warehouseId, String wallId, String cellName) {
        List<OBPSolve> obpSolve = obpSolveRepository.getByCell(warehouseId, wallId, cellName);
        if (obpSolve != null) {
            throw new ApiException(OutBoundProblemException.EX_CELL_HAS_BIND.getName(), cellName);
        }
    }
}
