package com.mushiny.wms.outboundproblem.business.common;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.ConversionUtil;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.BuildBusiness;
import com.mushiny.wms.outboundproblem.business.MoveGoodsBusiness;
import com.mushiny.wms.outboundproblem.business.UnitLoadBusiness;
import com.mushiny.wms.outboundproblem.business.enums.SolveResoult;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitRecordState;
import com.mushiny.wms.outboundproblem.business.utils.ShipmentState;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBPSolvePosition;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.common.StorageLocation;
import com.mushiny.wms.outboundproblem.domain.enums.*;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.exception.common.BaseException;
import com.mushiny.wms.outboundproblem.query.UnitLoadShipmentQuery;
import com.mushiny.wms.outboundproblem.repository.OBPSolvePositionRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OutBoundProblemCommonBusiness {

    private final StockUnitRepository stockUnitRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final LotRepository lotRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final OBPSolvePositionRepository obpSolvePositionRepository;
    private final OBPSolveRepository obpSolveRepository;
    private final UnitLoadBusiness unitLoadBusiness;
    private final BuildBusiness buildBusiness;
    private final MoveGoodsBusiness moveGoodsBusiness;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final UnitLoadShipmentQuery unitLoadShipmentQuery;

    @Autowired
    public OutBoundProblemCommonBusiness(StockUnitRepository stockUnitRepository,
                                         ApplicationContext applicationContext,
                                         StorageLocationRepository storageLocationRepository,
                                         LotRepository lotRepository,
                                         UnitLoadRepository unitLoadRepository,
                                         StockUnitRecordRepository stockUnitRecordRepository,
                                         OBPSolvePositionRepository obpSolvePositionRepository,
                                         OBPSolveRepository obpSolveRepository,
                                         UnitLoadBusiness unitLoadBusiness,
                                         BuildBusiness buildBusiness,
                                         MoveGoodsBusiness moveGoodsBusiness,
                                         StorageLocationTypeRepository storageLocationTypeRepository,
                                         CustomerShipmentRepository customerShipmentRepository,
                                         UnitLoadShipmentQuery unitLoadShipmentQuery) {
        this.stockUnitRepository = stockUnitRepository;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.lotRepository = lotRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.obpSolvePositionRepository = obpSolvePositionRepository;
        this.obpSolveRepository = obpSolveRepository;
        this.unitLoadBusiness = unitLoadBusiness;
        this.buildBusiness = buildBusiness;
        this.moveGoodsBusiness = moveGoodsBusiness;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.unitLoadShipmentQuery = unitLoadShipmentQuery;
    }

    public StockUnit subtractStowingAmount(StockUnit stockUnit, BigDecimal amount) {
        // 减去相应的数量
        stockUnit.setAmount(stockUnit.getAmount().subtract(amount));
        return stockUnit;
    }

    public void checkItemClient(StorageLocation storageLocation, ItemData itemData) {
        // 容器中不能存放不同客户的相同商品
        List<StockUnit> stockUnits = stockUnitRepository.getByStorageLocationAndAmount(storageLocation);
        if (stockUnits != null && !stockUnits.isEmpty()) {
            for (StockUnit stockUnit : stockUnits) {
                ItemData receivingItemData = stockUnit.getItemData();
                if (!(itemData.getClientId().equalsIgnoreCase(receivingItemData.getClientId()))
                        && itemData.getItemNo().equalsIgnoreCase(receivingItemData.getItemNo())) {
                    throw new ApiException(BaseException
                            .EX_CONTAINER_SKU_DIFFERENT_CLIENT.toString(), storageLocation.getName());
                }
            }
        }
    }

    public Lot checkItemLot(StorageLocation fromContainer,StorageLocation storageLocation, ItemData itemData, LocalDate useNotAfter) {
            // 容器中不能存放同一商品的不同有效期
            List<StockUnit> stockUnit = stockUnitRepository.getByStorageLocationAndItemData(storageLocation, itemData);
            if (stockUnit.size() > 0 && stockUnit.get(0).getLot() != null) {
                if (!stockUnit.get(0).getLot().getUseNotAfter().equals(useNotAfter)) {
                    throw new ApiException(BaseException
                            .EX_CONTAINER_SKU_DIFFERENT_LOT.getName(), storageLocation.getName());
                }
            }
            //输入的有效期不能在当前时间之前
//            LocalDate today= LocalDate.now();
//            if (useNotAfter.isBefore(today)) {
//               throw new ApiException(BaseException
//                    .EX_LOT_ERROR.getName(), storageLocation.getName());
//            }
            //判断输入的有效期是否在真实有效期之前
            //这个版本先不用判断实际有效期和输入的有效期，以输入有效期为准
//            List<StockUnit> stockUnits = stockUnitRepository.getByStorageLocationAndItemData(fromContainer, itemData);
//            if (!stockUnits.isEmpty() && stockUnits.get(0).getLot() != null) {
//                //真实有效期
//                LocalDate reallyTime=stockUnits.get(0).getLot().getUseNotAfter();
//                if (useNotAfter.isAfter(reallyTime)) {
//                    throw new ApiException(BaseException
//                            .EX_LOT_ERROR.getName(), storageLocation.getName());
//                }
//            }
           Lot lot=buildBusiness.buildLot(itemData,useNotAfter);
           return lot;
    }

    public Lot checkItemLot(StorageLocation fromStorageLocation, StorageLocation toStorageLocation, ItemData itemData, LocalDate useNotAfter, BigDecimal amount, String inventoryState) {
        //判断容器是否存在同一商品的不同有效期
        //判断输入的有效期是否在实际有效期之前
        return checkItemLot(fromStorageLocation, toStorageLocation, itemData, useNotAfter);
            //删除原有商品的有效期，即从库存中去除相应数量即可
//            StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), fromContainer);

//            StorageLocation fromStorageLocation = storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(), fromContainer, InventoryState.INVENTORY.toString());
//            List<StockUnit> stockUnitList = stockUnitRepository.getByStorageLocationAndItemData(fromStorageLocation, itemData);
//            if (stockUnitList == null || stockUnitList.isEmpty())
//                throw new ApiException(OutBoundProblemException.EX_STOCK_UNIT_HAS_NOT_RECORD.toString());
//
//            StockUnit stockUnit = stockUnitList.get(0);
//            stockUnit.setAmount(stockUnit.getAmount().subtract(amount));
//            stockUnitRepository.save(stockUnit);

            //重设商品有效期,并生成库存记录
          //  receivingDamagedRecord(fromContainer, itemData, toStorageLocation, useNotAfter, amount);
    }

    public StorageLocation checkProcessContainer(String containerName,String inventoryState) {
        //检查小车是否为Container容器
        StorageLocation storageLocation = Optional
                .ofNullable(storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(),
                        containerName, inventoryState))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_IS_CONFORMITY.getName()));

       //需要判断unitload是否被锁定
       List<UnitLoad> unitLoads=unitLoadRepository.getByStorageLocation(storageLocation);
       if(!unitLoads.isEmpty()) {
           UnitLoad unitLoad=unitLoads.get(0);
           if (Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)) {
               throw new ApiException(OutBoundProblemException.EX_IT_UNITLOAD_IS_LOCKED.getName(), containerName);
           }
       }
        return storageLocation;
    }

    public StorageLocation getContainer(String containerName,String inventoryState) {
        //检查小车是否为Container容器
        StorageLocation storageLocation = Optional
                .ofNullable(storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(),
                        containerName, inventoryState))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_IS_CONFORMITY.getName()));
        return storageLocation;
    }

    public StorageLocation checkContainer(String containerName,String inventoryState) {
        //检查小车是否为Container容器
        StorageLocation storageLocation = Optional
                .ofNullable(storageLocationRepository.getByName(applicationContext.getCurrentWarehouse(),
                        containerName, inventoryState))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_IS_CONFORMITY.getName(), containerName));
        //判断容器是被锁定，判断容器里面是否存在商品
        List<UnitLoad> unitLoads=unitLoadRepository.getByStorageLocation(storageLocation);
        if(!unitLoads.isEmpty()) {
            UnitLoad unitLoad=unitLoads.get(0);
            if (Objects.equals(unitLoad.getEntityLock(), Constant.GENERAL)) {
                throw new ApiException(OutBoundProblemException.EX_IT_UNITLOAD_IS_LOCKED.getName(), containerName);
            }
            BigDecimal amount = stockUnitRepository.sumByUnitLoad(unitLoad);
            if(amount.compareTo(BigDecimal.ZERO)>0)
                throw new ApiException(OutBoundProblemException.EX_CONTAINER_IS_NOT_EMPTY.getName());
        }
        return storageLocation;
    }

    public void checkItemLot(StockUnit stockUnit) {
        if (stockUnit.getItemData().isLotMandatory()) {
            // 检查SKU的有效期是否录入
            if (stockUnit.getLot() == null) {
                throw new ApiException(BaseException
                        .EX_LOT_ITEM_DATA_NOT_FOUND_DATE.getName(), stockUnit.getItemData().getItemNo());
            }
        }
    }

    public void checkStorageLocationItemsAmount(StorageLocation storageLocation, ItemData itemData, BigDecimal amount) {
        // 商品数量超过小车收货商品的总数量
        BigDecimal receivingMaxAmount = stockUnitRepository.sumByStorageLocationAndItemData(storageLocation, itemData);
        if (amount.compareTo(receivingMaxAmount) == 1) {
            throw new ApiException(BaseException
                    .EX_AMOUNT_ERROR.getName(), storageLocation.getName());
        }
    }


    public void checkStorageLocationClient(StorageLocation storageLocation, ItemData itemData) {
        // 货位所属客户和商品所属客户比较
        if (!applicationContext.isSystemClient(storageLocation.getClientId())
                && storageLocation.getClientId().equalsIgnoreCase(itemData.getClientId())) {
            throw new ApiException(BaseException
                    .EX_STORAGE_LOCATION_SKU_CLIENT_DIFFERENT.getName(), storageLocation.getName());
        }
        // 不同客户的相同商品是不容许放入同一个货位中
        StockUnit stockUnit = stockUnitRepository.getByItemNoAndUnequalClient(
                storageLocation, itemData.getClientId(), itemData.getItemNo());
        if (stockUnit != null ) {
            throw new ApiException(BaseException
                    .EX_STORAGE_LOCATION_SKU_DIFFERENT_CLIENT.getName(), storageLocation.getName());
        }
    }


    public void receivingDamagedRecord(String cellName, ItemData itemData,
                                StorageLocation storageLocation,
                                LocalDate useNotAfter,BigDecimal amount) {
        Lot lot = null;
        if (itemData.isLotMandatory()) {
            lot = checkAndSaveLot(itemData, useNotAfter);
        }
        // 生成容器间的移货记录
        List<StockUnit> stockUnitList = stockUnitRepository.getByStorageLocationAndItemData(storageLocation, itemData);
        StockUnit stockUnit;
        if(stockUnitList == null || stockUnitList.isEmpty()){
            stockUnit = buildStockUnit(itemData.getWarehouseId(), itemData.getClientId(),
                    itemData, lot, amount);
        }else {
            stockUnit = stockUnitList.get(0);
            stockUnit.setAmount(stockUnit.getAmount().add(BigDecimal.ONE));
        }
        //设置库存状态
        stockUnit.setState(storageLocation.getStorageLocationType().getInventoryState());
        // 生成UnitLoad
        UnitLoad unitLoad = unitLoadBusiness.getByStorageLocation(storageLocation);
        if(unitLoad == null){
            unitLoad = buildBusiness.buildUnitLoad(storageLocation);
        }
        stockUnit.setUnitLoad(unitLoad);
        stockUnit = stockUnitRepository.save(stockUnit);
        // 生成移货历史记录
        StockUnitRecord stockUnitRecord = buildStockUnitRecord(stockUnit, BigDecimal.ONE);
        stockUnitRecord.setRecordCode(StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_CODE.getName());
        stockUnitRecord.setRecordTool(StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TOOL.getName());
        stockUnitRecord.setRecordType(StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TYPE.getName());
        stockUnitRecord.setFromStorageLocation(cellName);
        stockUnitRecord.setToStorageLocation(storageLocation.getName());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    public StockUnit buildStockUnit(StockUnit source,
                                    BigDecimal amount) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amount);
        stockUnit.setItemData(source.getItemData());
        stockUnit.setLot(source.getLot());
        stockUnit.setClientId(source.getClientId());
        stockUnit.setWarehouseId(source.getWarehouseId());
        return stockUnit;
    }

    public StockUnit buildStockUnit(String warehouseId,
                                    String clientId,
                                    ItemData itemData,
                                    Lot lot,
                                    BigDecimal amount) {
        StockUnit stockUnit = new StockUnit();
        stockUnit.setAmount(amount);
        stockUnit.setItemData(itemData);
        stockUnit.setLot(lot);
        stockUnit.setClientId(clientId);
        stockUnit.setWarehouseId(warehouseId);
        return stockUnit;
    }

    public StockUnitRecord buildStockUnitRecord(StockUnit stockUnit, BigDecimal amount) {
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setAmount(amount);
        if (stockUnit.getLot() != null) {
            stockUnitRecord.setLot(stockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
        stockUnitRecord.setToStockUnit(stockUnit.getId());
        if (stockUnit.getUnitLoad().getStorageLocation() != null) {
            stockUnitRecord.setToStorageLocation(stockUnit.getUnitLoad().getStorageLocation().getName());
        }
        if (stockUnit.getUnitLoad().getStorageLocation() != null) {
            stockUnitRecord.setToStorageLocation(stockUnit.getUnitLoad().getStorageLocation().getName());
        }
        if (stockUnit.getUnitLoad() != null) {
            stockUnitRecord.setToUnitLoad(stockUnit.getUnitLoad().getLabel());
        }
        stockUnitRecord.setClientId(stockUnit.getClientId());
        stockUnitRecord.setWarehouseId(stockUnit.getWarehouseId());
        return stockUnitRecord;
    }

    public Lot checkAndSaveLot(ItemData itemData, LocalDate useNotAfter) {
        // 判断具体商品是否已经存在保质期
        Lot lot = lotRepository.getByItemData(itemData, useNotAfter);
        if (lot == null) {
            lot = new Lot();
            String lotNo;
            boolean randomFlag = true;
            while (randomFlag) {
                lotNo = RandomUtil.getLotNo();
                Lot entity = lotRepository.getByLotNo(lotNo);
                if (entity == null) {
                    lot.setLotNo(lotNo);
                    randomFlag = false;
                }
            }
            lot.setUseNotAfter(useNotAfter);
            lot.setLotDate(DateTimeUtil.getNowDate());
            lot.setItemData(itemData);
            lot.setClientId(itemData.getClientId());
            lot.setWarehouseId(itemData.getWarehouseId());
            lot = lotRepository.save(lot);
        }
        return lot;
    }

    public OBPSolvePosition checkAndSaveSolvePosition(OBPSolve obpSolve, String solveKey, String container) {
        //判断问题处理详情信息是否已经存在
//        OBPSolvePosition solvePosition = obpSolvePositionRepository.getByShipmentNoAndItemNo(
//                applicationContext.getCurrentWarehouse(),
//                obpSolve.getId(), obpSolve.getCustomerShipment().getShipmentNo(), obpSolve.getItemData().getItemNo());
//        if (solveKey.equals(SolveResoult.DELETE_ORDER_FORCE.toString()))
        OBPSolvePosition  solvePosition = obpSolvePositionRepository.getForceDeleteSolvePositionByShipmentNoAndSolveKey(
                    applicationContext.getCurrentWarehouse(),obpSolve.getCustomerShipment().getShipmentNo(),solveKey);
        if (solvePosition == null) {
            solvePosition = new OBPSolvePosition();
            solvePosition.setObpSolve(obpSolveRepository.retrieve(obpSolve.getId()));
            solvePosition.setDescription(null);
            solvePosition.setSolveKey(solveKey);
            solvePosition.setLocation(null);
            solvePosition.setLocationContainer(container);
            solvePosition.setItemDataNo(obpSolve.getItemData().getItemNo());

            solvePosition.setState(OBPSolveState.unsolved.toString());
            if (obpSolve.getState() != null)
                solvePosition.setState(obpSolve.getState());

            solvePosition.setAmountScaned(BigDecimal.ZERO);
            solvePosition.setSolveBy(SecurityUtils.getCurrentUsername());
            solvePosition.setSolveDate(LocalDateTime.now());
            solvePosition.setShipmentNo(obpSolve.getCustomerShipment().getShipmentNo());
            solvePosition.setWarehouseId(applicationContext.getCurrentWarehouse());
        } else {
            if(solvePosition.getLocationContainer()==null||solvePosition.getLocationContainer().equals("")||
                    solvePosition.getLocationContainer().equalsIgnoreCase(container)){
                solvePosition.setLocationContainer(container);
            }else if(!solvePosition.getLocationContainer().equalsIgnoreCase(container)){
                if(solvePosition.getLocationContainer().contains("/")){
                      String locations[]=solvePosition.getLocationContainer().split("/");
                      String loc=null;
                      for(int i=0;i<locations.length;i++){
                          if(container.equals(locations[i])) {
                              loc=locations[i];
                              break;
                          }
                      }
                      if(loc==null) solvePosition.setLocationContainer(solvePosition.getLocationContainer() + "/" + container);
                }else
                    solvePosition.setLocationContainer(solvePosition.getLocationContainer() + "/" + container);
            }
           if (obpSolve.getItemData().getItemNo() != null)
               solvePosition.setItemDataNo(obpSolve.getItemData().getItemNo());
        }
        solvePosition = obpSolvePositionRepository.save(solvePosition);
        return solvePosition;
    }

    //送去包装时，将商品从CELL格移到Problem Solved容器，同时生成库存记录
    public void removeItemToProblemSolvedContainer(OBPSolve solve, String fromContainer) {

        //生成库存记录,将商品从当前CELL格中移到Problem Solved容器中
        ItemData itemData = solve.getItemData();
        StorageLocation toStorageLocation = storageLocationRepository.getAllStoragetypeByName(
                applicationContext.getCurrentWarehouse(),
                VirtualStorageLocation.PROBLEM_SOLVED.getName());
        if (toStorageLocation == null) {
            toStorageLocation = buildBusiness.buildVirtualStorageLocation(VirtualStorageLocation.PROBLEM_SOLVED.getName());
        }
        StorageLocation fromStorageLocation = storageLocationRepository.getAllStoragetypeByName(
                applicationContext.getCurrentWarehouse(),fromContainer);
        //moveGoodsBusiness.moving(fromStorageLocation,toStorageLocation,itemData,solve.getAmountShipment(),null);

    }

    //清除问题处理格时，将商品移到VirtualStorageLocation容器中，同时生成库存记录
    public void removeItemToVirtualContainer(String shipmentNo, String virtualStorageLocationName,String solveKey) {

        //生成库存记录,将商品移到VirtualStorageLocation容器中
        StorageLocation toStorageLocation = storageLocationRepository.getAllStoragetypeByName(
                applicationContext.getCurrentWarehouse(),virtualStorageLocationName);
        if (toStorageLocation == null){
            toStorageLocation = buildBusiness.buildVirtualStorageLocation(virtualStorageLocationName);
        }
        UnitLoad fromUnitLoad = unitLoadRepository.getByShipmentNo(shipmentNo);
        if (fromUnitLoad==null)
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName(), shipmentNo);

            StorageLocation fromStorageLocation = fromUnitLoad.getStorageLocation();
        if (fromStorageLocation == null)
            throw new ApiException(OutBoundProblemException.EX_SHIPMENT_HAS_NOT_PICK_UNITLOAD.getName(),shipmentNo);
        if(solveKey.equals(SolveResoult.RELEASE_CELL.toString())){
            moveGoodsBusiness.moveAllGoods(toStorageLocation,shipmentNo);
        }else {
            moveGoodsBusiness.moveAll(toStorageLocation, shipmentNo);
        }
    }

    public String getShipmetnState(CustomerShipment customerShipment) {
        int state = customerShipment.getState();
        String shipmentState = "";
        switch (state) {
            case ShipmentState.RAW:
                shipmentState = "RAW";
                break;
            case ShipmentState.RELEASED:
                shipmentState = "RELEASED";
                break;
            case ShipmentState.ASSIGNED:
                shipmentState = "ASSIGNED";
                break;
            case ShipmentState.PROCESSABLE:
                shipmentState = "PROCESSABLE";
                break;
            case ShipmentState.RESERVED:
                shipmentState = "RESERVED";
                break;
            case ShipmentState.STARTED:
                shipmentState = "STARTED";
                break;
            case ShipmentState.PENDING:
                shipmentState = "PENDING";
                break;
            case ShipmentState.PICKED:
                shipmentState = "PICKED";
                break;
            case ShipmentState.REBATCHED:
                shipmentState = "REBATCHED";
                break;
            case ShipmentState.REBINBUFFERED:
                shipmentState = "REBINBUFFERED";
                break;
            case ShipmentState.REBINED:
                shipmentState = "REBINED";
                break;
            case ShipmentState.PACKING:
                shipmentState = "PACKING";
                break;
            case ShipmentState.PACKED:
                shipmentState = "PACKED";
                break;
            case ShipmentState.SORTED:
                shipmentState = "SORTED";
                break;
            case ShipmentState.FINISHED:
                shipmentState = "FINISHED";
                break;
            case ShipmentState.CANCELED:
                shipmentState = "CANCELED";
                break;
            case ShipmentState.POSTPROCESSED:
                shipmentState = "POSTPROCESSED";
                break;
            case ShipmentState.DELETED:
                shipmentState = "DELETED";
                break;
            case ShipmentState.PROBLEM:
                shipmentState = "PROBLEM";
                break;

        }
        return shipmentState;
    }

}
