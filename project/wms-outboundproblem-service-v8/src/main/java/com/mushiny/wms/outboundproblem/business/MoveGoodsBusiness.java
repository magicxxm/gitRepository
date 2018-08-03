package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.business.enums.SerialNoRecordType;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitRecordState;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitState;
import com.mushiny.wms.outboundproblem.domain.OBPShipmentSerialNo;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;
import com.mushiny.wms.outboundproblem.domain.OBProblem;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.domain.enums.ProblemType;
import com.mushiny.wms.outboundproblem.domain.enums.VirtualStorageLocation;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.query.UnitLoadShipmentQuery;
import com.mushiny.wms.outboundproblem.repository.OBPShipmentSerialNoRepository;
import com.mushiny.wms.outboundproblem.repository.OBPSolveRepository;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import com.mushiny.wms.outboundproblem.repository.common.CustomerShipmentRepository;
import com.mushiny.wms.outboundproblem.repository.common.StockUnitRepository;
import com.mushiny.wms.outboundproblem.repository.common.StorageLocationRepository;
import com.mushiny.wms.outboundproblem.repository.common.UnitLoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MoveGoodsBusiness {

    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final CheckingBusiness checkingBusiness;
    private final GeneralBusiness generalBusiness;
    private final BuildBusiness buildBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final UnitLoadShipmentQuery unitLoadShipmentQuery;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final OBProblemRepository obProblemRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationRepository storageLocationRepository;
    private final OBPSolveRepository obpSolveRepository;
    private final OBPShipmentSerialNoRepository obpShipmentSerialNoRepository;

    @Autowired
    public MoveGoodsBusiness(UnitLoadRepository unitLoadRepository,
                             StockUnitRepository stockUnitRepository,
                             CheckingBusiness checkingBusiness,
                             GeneralBusiness generalBusiness,
                             BuildBusiness buildBusiness,
                             UnitLoadBusiness unitLoadBusiness,
                             UnitLoadShipmentQuery unitLoadShipmentQuery,
                             CustomerShipmentRepository customerShipmentRepository,
                             OBProblemRepository obProblemRepository,
                             ApplicationContext applicationContext,
                             StorageLocationRepository storageLocationRepository,
                             OBPSolveRepository obpSolveRepository,
                             OBPShipmentSerialNoRepository obpShipmentSerialNoRepository) {
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.checkingBusiness = checkingBusiness;
        this.generalBusiness = generalBusiness;
        this.buildBusiness = buildBusiness;
        this.unitLoadBusiness = unitLoadBusiness;
        this.unitLoadShipmentQuery = unitLoadShipmentQuery;
        this.customerShipmentRepository = customerShipmentRepository;
        this.obProblemRepository = obProblemRepository;
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.obpSolveRepository = obpSolveRepository;
        this.obpShipmentSerialNoRepository = obpShipmentSerialNoRepository;
    }

    @SuppressWarnings("Duplicates")
    public void moveAll(StorageLocation destination,String shipmentNo) {
        UnitLoad sourceUnitLoad = unitLoadRepository.getByShipmentNo(shipmentNo);
        //虚拟容器需要新建UNITLOAD
        UnitLoad toUnitLoad;
        if(destination.getName().equals(VirtualStorageLocation.PROBLEM_SOLVING.getName())) {
            toUnitLoad = buildBusiness.buildUnitLoad(destination);
        }else{ //正品货筐判断是否存在UNITLOAD
           List<UnitLoad> unitLoads= unitLoadRepository.getByStorageLocation(destination);
           if(unitLoads.isEmpty()){
              toUnitLoad = buildBusiness.buildUnitLoad(destination);
           }else{
              toUnitLoad=unitLoads.get(0);
           }
        }
        //更新UNITLOAD_SHIPEMNT表
        unitLoadShipmentQuery.updateUnitloadByShipment(toUnitLoad.getId(),customerShipmentRepository.getByShipmentNo(shipmentNo).getId());
        checkingBusiness.checkingUnitLoadEntityLock(sourceUnitLoad);
        List<OBPSolve> solves = obpSolveRepository.getGoodsByShipment(applicationContext.getCurrentWarehouse(), shipmentNo);
        for(OBPSolve solve:solves) {
            OBPSolve obpSolve = obpSolveRepository.getGoodsByShipmentAndItem(applicationContext.getCurrentWarehouse(), shipmentNo, solve.getItemData().getItemNo());

            List<OBProblem> problems = obProblemRepository.getByShipmentAndItemId(applicationContext.getCurrentWarehouse(), obpSolve.getCustomerShipment().getId(), obpSolve.getItemData().getId());
            BigDecimal problemAmount = BigDecimal.ZERO;
            if(!problems.isEmpty())
            for (OBProblem obProblem : problems) {
                if (obProblem.getContainer() != null) {
                    StorageLocation storageLocation = storageLocationRepository.getAllStoragetypeByName(applicationContext.getCurrentWarehouse(), obProblem.getContainer());
                    //如果商品已经放入残品车牌或者待调查车牌，则不能清除问题格
                    if (storageLocation != null && storageLocation.getStorageLocationType() != null && !storageLocation.getStorageLocationType().getInventoryState().equals("INVENTORY")) {
                       throw new ApiException(OutBoundProblemException.EX_ITEMDATA_HAS_TO_CONTAINER.getName());
                    } else {
                        obProblem.setContainer(destination.getName());
                        obProblemRepository.save(obProblem);
                    }
                }
                if (obProblem.getProblemType().equals(ProblemType.LOSE.toString())) {
                    problemAmount = problemAmount.add(obProblem.getAmount());
                }
            }
            movingGoods(sourceUnitLoad, toUnitLoad, destination, solve.getItemData(), obpSolve.getAmountShipment().subtract(problemAmount),null,null);
        }
    }

    @SuppressWarnings("Duplicates")
    public void moveAllGoods(StorageLocation destination,String shipmentNo) {
        UnitLoad sourceUnitLoad = unitLoadRepository.getByShipmentNo(shipmentNo);
        //虚拟容器需要新建UNITLOAD
        UnitLoad toUnitLoad;
        if(destination.getName().equals(VirtualStorageLocation.PROBLEM_SOLVED.getName())) {
            toUnitLoad = buildBusiness.buildUnitLoad(destination);
        }else{ //正品货筐判断是否存在UNITLOAD
            List<UnitLoad> unitLoads= unitLoadRepository.getByStorageLocation(destination);
            if(unitLoads.isEmpty()){
                toUnitLoad = buildBusiness.buildUnitLoad(destination);
            }else{
                toUnitLoad=unitLoads.get(0);
            }
        }
        //更新UNITLOAD_SHIPEMNT表
        unitLoadShipmentQuery.updateUnitloadByShipment(toUnitLoad.getId(),customerShipmentRepository.getByShipmentNo(shipmentNo).getId());
        checkingBusiness.checkingUnitLoadEntityLock(sourceUnitLoad);
        // 获取该当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoad(sourceUnitLoad);
        // 按商品分组
        Map<String, List<StockUnit>> stockUnitMap = sourceStockUnits.stream().collect(
                Collectors.groupingBy(entity -> entity.getItemData().getId()));
        // 按UnitLoad处理库存
        for (String key : stockUnitMap.keySet()) {
            List<StockUnit> stockUnits = stockUnitMap.get(key);
            if (stockUnits.isEmpty()) {
                continue;
            }
            ItemData itemData = stockUnits.get(0).getItemData();
            BigDecimal amount = stockUnitRepository.sumByUnitLoadAndItemData(sourceUnitLoad, itemData);
            movingGoods(sourceUnitLoad, toUnitLoad, destination, itemData, amount,null,null);
        }
    }

    @SuppressWarnings("Duplicates")
    public void movingGoods(UnitLoad sourceUnitLoad,
                       UnitLoad destinationUnitLoad,
                       StorageLocation destination,
                       ItemData itemData,
                       BigDecimal moveItemDataAmount,
                       String solveKey,
                       Lot lotNo) {

        if(moveItemDataAmount.compareTo(BigDecimal.ZERO)==0)  return ;

        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(moveItemDataAmount) == -1) {
            throw new ApiException(OutBoundProblemException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName());
        }
        // 获取该商品所在当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        if(itemData.getSerialRecordType().equals(SerialNoRecordType.ALWAYS_RECORD.toString())){
            sourceStockUnits=verifyRemoveType(sourceStockUnits,solveKey);
        }
        Lot lot = sourceStockUnits.get(0).getLot();
        if(lotNo!=null)  lot=lotNo;
        if (checkingBusiness.isBin(destination)) {
            // 超过货位的载重量
            checkingBusiness.checkStorageLocationWeight(destinationUnitLoad, itemData, moveItemDataAmount);
            // 商品属性与货位设置不符
            checkingBusiness.checkStorageLocationItemGroup(destination, itemData);
            // 商品种类超过系统设置数量
            checkingBusiness.checkStorageLocationItemsAmount(destinationUnitLoad, itemData);
        }
        // 移货
        // 减去原始容器的相应货物的数量
        BigDecimal moveAmount = moveItemDataAmount;
        StockUnit sourceStockUnit = sourceStockUnits.get(0);
        for (StockUnit stockUnit : sourceStockUnits) {
           // BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            BigDecimal stockAmount = stockUnit.getAmount();
            if (stockAmount.compareTo(moveAmount) >= 0) {
                stockUnit.setAmount(stockAmount.subtract(moveAmount));
                break;
            } else {
                moveAmount = moveAmount.subtract(stockAmount);
                stockUnit.setAmount(BigDecimal.ZERO);
            }
        }
        stockUnitRepository.save(sourceStockUnits);
        // 放入目的容器
        // 检查目的容器是否存在这种商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
        StockUnit destinationStockUnit;
        BigDecimal itemDataWeight=BigDecimal.ZERO;
        if(itemData.getSerialRecordType().equals(SerialNoRecordType.NO_RECORD.toString())||itemData.getSerialRecordType().isEmpty()) {
            if (destinationStockUnits.isEmpty()) {
                destinationStockUnit = buildBusiness.buildStockUnit(moveItemDataAmount, destinationUnitLoad, itemData, lot, null);
            } else {
                destinationStockUnit = destinationStockUnits.get(0);
                destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(moveItemDataAmount));
                destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
            }
            // 生成移货库存记录
            buildBusiness.buildStockUnitRecord(
                    sourceStockUnit, destinationStockUnit, moveItemDataAmount,
                    StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_CODE.getName(),
                    StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TOOL.getName(),
                    StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TYPE.getName(),
                    null);
            // 更新目的容器的重量
            itemDataWeight = itemData.getWeight().multiply(moveItemDataAmount);
            destinationUnitLoad.setWeightCalculated(
                    destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
            unitLoadRepository.save(destinationUnitLoad);
        }else {   //序列号商品移库
            int amount = moveItemDataAmount.intValue();
            for (int i = 0; i < amount; i++) {
                destinationStockUnit = buildBusiness.buildStockUnit(BigDecimal.ONE, destinationUnitLoad, itemData, lot, sourceStockUnits.get(i).getSerialNo());
                buildBusiness.buildStockUnitRecord(
                        sourceStockUnits.get(i), destinationStockUnit, BigDecimal.ONE,
                        StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_CODE.getName(),
                        StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TOOL.getName(),
                        StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TYPE.getName(),
                        sourceStockUnits.get(i).getSerialNo());
                // 更新目的容器的重量
                itemDataWeight = itemData.getWeight().multiply(moveItemDataAmount);
                destinationUnitLoad.setWeightCalculated(
                        destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
                unitLoadRepository.save(destinationUnitLoad);
            }
        }
        // 检查sourceUnitLoad是否存在货物
        BigDecimal sourceUnitLoadAmount = stockUnitRepository.sumByUnitLoad(sourceUnitLoad);
        if (sourceUnitLoadAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // 因为容器中已经不存在货物，删除UNITLOAD
            sourceUnitLoad.setEntityLock(Constant.GOING_TO_DELETE);
           // sourceUnitLoad.setStorageLocation(null);
            sourceUnitLoad.setWeightCalculated(BigDecimal.ZERO);
            unitLoadRepository.save(sourceUnitLoad);
        } else {
            // 更新原始容器重量
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
            unitLoadRepository.save(sourceUnitLoad);
        }
    }

    //序列号商品移动到残品车牌
    @SuppressWarnings("Duplicates")
    public void movingGoodsToContainer(UnitLoad sourceUnitLoad,
                            UnitLoad destinationUnitLoad,
                            ItemData itemData,
                            BigDecimal moveItemDataAmount,
                            String serialNo,
                            Lot lotNo) {

        StockUnit sourceStockUnit = stockUnitRepository.getByItemDataSn(sourceUnitLoad, itemData,serialNo);
        Lot lot = sourceStockUnit.getLot();
        if(lotNo!=null)  lot=lotNo;
        // 减去原始容器的相应货物的数量
        sourceStockUnit.setAmount(sourceStockUnit.getAmount().subtract(moveItemDataAmount));
        stockUnitRepository.save(sourceStockUnit);

        // 放入目的容器
        StockUnit destinationStockUnit = buildBusiness.buildStockUnit(moveItemDataAmount, destinationUnitLoad, itemData, lot, serialNo);
        // 生成移货库存记录
        buildBusiness.buildStockUnitRecord(
             sourceStockUnit, destinationStockUnit, moveItemDataAmount,
             StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_CODE.getName(),
             StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TOOL.getName(),
             StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TYPE.getName(),
             serialNo);
            // 更新目的容器的重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(moveItemDataAmount);
          destinationUnitLoad.setWeightCalculated(
                    destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
            unitLoadRepository.save(destinationUnitLoad);
        // 检查sourceUnitLoad是否存在货物
        BigDecimal sourceUnitLoadAmount = stockUnitRepository.sumByUnitLoad(sourceUnitLoad);
        if (sourceUnitLoadAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // 因为容器中已经不存在货物，删除UNITLOAD
            sourceUnitLoad.setEntityLock(Constant.GOING_TO_DELETE);
            // sourceUnitLoad.setStorageLocation(null);
            sourceUnitLoad.setWeightCalculated(BigDecimal.ZERO);
            unitLoadRepository.save(sourceUnitLoad);
        } else {
            // 更新原始容器重量
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
            unitLoadRepository.save(sourceUnitLoad);
        }
    }

    //OB问题核实移库操作
    @SuppressWarnings("Duplicates")
    public void movingProblem(StorageLocation source,
                       StorageLocation destination,
                       ItemData itemData,
                       BigDecimal moveItemDataAmount) {
        if(moveItemDataAmount.compareTo(BigDecimal.ZERO)==0)  return ;

//        UnitLoad sourceUnitLoad = unitLoadBusiness.getByStorageLocation(source);
        UnitLoad sourceUnitLoad = unitLoadRepository.movingGetByStorageLocation(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(OutBoundProblemException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName());
        }
        // 检查移动货物数量是否超过系统设定
        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(moveItemDataAmount) == -1) {
            throw new ApiException(OutBoundProblemException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName());
        }
        // 获取该商品所在当前容器的所有库存
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);

        Lot lot = sourceStockUnits.get(0).getLot();
        // 检查目的地容器
        UnitLoad destinationUnitLoad = unitLoadRepository.movingGetByStorageLocation(destination);
        if (destinationUnitLoad == null) {
            destinationUnitLoad = buildBusiness.buildUnitLoad(destination);
        } else {
            // 检查目的地容器是否存在不同客户的相同商品
            checkingBusiness.checkStorageLocationClient(destinationUnitLoad, itemData);
            //检查目的地容器是否存在不同有效期的相同商品
                checkingBusiness.checkStorageLocationLot(destinationUnitLoad, itemData, lot);
            if (checkingBusiness.isBin(destination)) {
                // 货位中存在相似商品
                checkingBusiness.checkStorageLocationSimilar(destinationUnitLoad, itemData);
            }
        }
//        if (!source.getStorageLocationType().getInventoryState().equals(
//                destination.getStorageLocationType().getInventoryState())) {
//            throw new ApiException(OutBoundProblemException.EX_CONTAINER_NOT_IS_CONFORMITY.toString());
//        }
        if (checkingBusiness.isBin(destination)) {
            // 超过货位的载重量
            checkingBusiness.checkStorageLocationWeight(destinationUnitLoad, itemData, moveItemDataAmount);
            // 商品属性与货位设置不符
            checkingBusiness.checkStorageLocationItemGroup(destination, itemData);
            // 商品种类超过系统设置数量
            checkingBusiness.checkStorageLocationItemsAmount(destinationUnitLoad, itemData);
        }
        // 移货
        // 减去原始容器的相应货物的数量
        BigDecimal moveAmount = moveItemDataAmount;
        StockUnit sourceStockUnit = sourceStockUnits.get(0);
        for (StockUnit stockUnit : sourceStockUnits) {
            BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            if (stockAmount.compareTo(moveAmount) >= 0) {
                stockUnit.setAmount(stockAmount.subtract(moveAmount));
                break;
            } else {
                moveAmount = moveAmount.subtract(stockAmount);
                stockUnit.setAmount(BigDecimal.ZERO);
            }
        }
        stockUnitRepository.save(sourceStockUnits);
        // 放入目的容器
        // 检查目的容器是否存在这种商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
        StockUnit destinationStockUnit;
        if (destinationStockUnits.isEmpty()) {
            //新建stockUnit
            destinationStockUnit = buildBusiness.buildStockUnit(moveItemDataAmount, destinationUnitLoad, itemData, lot,null);
        } else {
            destinationStockUnit = destinationStockUnits.get(0);
            destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(moveItemDataAmount));
            destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
        }
        // 生成移货库存记录
        buildBusiness.buildStockUnitRecordProblem(
            sourceStockUnit, destinationStockUnit, moveItemDataAmount,
            StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_CODE.getName(),
            StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TOOL.getName(),
            StockUnitRecordState.OB_PROBLEM_SOLVE_RECORD_TYPE.getName());
       // 更新目的容器的重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(moveItemDataAmount);
        destinationUnitLoad.setWeightCalculated(
             destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
        unitLoadRepository.save(destinationUnitLoad);
        // 检查sourceUnitLoad是否存在货物
        BigDecimal sourceUnitLoadAmount = stockUnitRepository.sumByUnitLoad(sourceUnitLoad);
        if (sourceUnitLoadAmount.compareTo(BigDecimal.ZERO) <= 0) {
            // 因为容器中已经不存在货物，删除UNITLOAD
            sourceUnitLoad.setEntityLock(Constant.GOING_TO_DELETE);
            //  sourceUnitLoad.setStorageLocation(null);
            sourceUnitLoad.setWeightCalculated(BigDecimal.ZERO);
            sourceUnitLoad.setStationName(null);
            sourceUnitLoad.setClientId(sourceStockUnit.getClientId());
            unitLoadRepository.save(sourceUnitLoad);
        } else {
            // 更新原始容器重量
            sourceUnitLoad.setWeightCalculated(
                    sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
            sourceUnitLoad.setClientId(sourceStockUnit.getClientId());
            unitLoadRepository.save(sourceUnitLoad);
        }
    }

    private List<StockUnit> verifyRemoveType(List<StockUnit> sourceStockUnits,String solveKey){
        //移货到问题格
        if(solveKey!=null&&solveKey.equals("moveToCell")) {
            List<StockUnit> deletes=new ArrayList<>();
            for (StockUnit stockUnit : sourceStockUnits) {
                OBPShipmentSerialNo obpShipmentSerialNo = obpShipmentSerialNoRepository.getSerialNoByName(applicationContext.getCurrentWarehouse(), stockUnit.getSerialNo());
                if(obpShipmentSerialNo!=null&&(!obpShipmentSerialNo.getState().equals(StockUnitState.INVENTORY.getName())||obpShipmentSerialNo.getState().equals(null)))
                    deletes.add(stockUnit);
            }
            sourceStockUnits.removeAll(deletes);
        }
        //移货到待调查车牌
        if(solveKey!=null&&solveKey.equals("moveToPending")) {
            List<StockUnit> deletes=new ArrayList<>();
            for (StockUnit stockUnit : sourceStockUnits) {
                OBPShipmentSerialNo obpShipmentSerialNo = obpShipmentSerialNoRepository.getSerialNoByName(applicationContext.getCurrentWarehouse(), stockUnit.getSerialNo());
                if(obpShipmentSerialNo!=null&&obpShipmentSerialNo.getState()!=null)
                    deletes.add(stockUnit);
            }
            sourceStockUnits.removeAll(deletes);
        }
        return sourceStockUnits;
    }
}
