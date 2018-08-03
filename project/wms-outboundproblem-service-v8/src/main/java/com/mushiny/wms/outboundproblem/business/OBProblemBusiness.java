package com.mushiny.wms.outboundproblem.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.config.security.SecurityUtils;
import com.mushiny.wms.outboundproblem.business.enums.StockUnitRecordState;
import com.mushiny.wms.outboundproblem.crud.dto.LossGoodsDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OverageGoodsDTO;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.domain.common.*;
import com.mushiny.wms.outboundproblem.exception.common.InternalToolException;
import com.mushiny.wms.outboundproblem.repository.OBProblemCheckRepository;
import com.mushiny.wms.outboundproblem.repository.common.*;
import com.mushiny.wms.outboundproblem.repository.OBProblemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Component
public class OBProblemBusiness implements Serializable {

    private final OBProblemRepository obProblemRepository;
    private final ReceiveProcessRepository receiveProcessRepository;
    private final UnitLoadRepository unitLoadRepository;
    private final UnitLoadBusiness unitLoadBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ItemDataRepository itemDataRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final CheckingBusiness checkingBusiness;
    private final GeneralBusiness generalBusiness;
    private final BuildBusiness buildBusiness;
    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final OBProblemCheckRepository obProblemCheckRepository;


    @Autowired
    public OBProblemBusiness(
            OBProblemRepository obProblemRepository,
            ReceiveProcessRepository receiveProcessRepository,
            UnitLoadRepository unitLoadRepository,
            UnitLoadBusiness unitLoadBusiness,
            StockUnitRepository stockUnitRepository,
            StorageLocationRepository storageLocationRepository,
            ItemDataRepository itemDataRepository,
            StockUnitRecordRepository stockUnitRecordRepository,
            CheckingBusiness checkingBusiness,
            GeneralBusiness generalBusiness,
            BuildBusiness buildBusiness,
            ApplicationContext applicationContext,
            ClientRepository clientRepository,
            OBProblemCheckRepository obProblemCheckRepository) {
        this.obProblemRepository = obProblemRepository;
        this.receiveProcessRepository = receiveProcessRepository;
        this.unitLoadRepository = unitLoadRepository;
        this.unitLoadBusiness = unitLoadBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.itemDataRepository = itemDataRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.checkingBusiness = checkingBusiness;
        this.generalBusiness = generalBusiness;
        this.buildBusiness = buildBusiness;
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.obProblemCheckRepository = obProblemCheckRepository;
    }

    public void update(List<OBProblemCheck> entities) {
        for (OBProblemCheck entity : entities) {
            update(entity);
        }
    }
    //关闭 异常问题 是，判断是否解绑容器
    public OBProblemCheck update(OBProblemCheck entity) {
        //判断是否修改为CLOSE状态，并且 非CLOSET状态下container是否等于0(不含将修改CLOSE状态的id)
        if (entity.getState().equalsIgnoreCase("CLOSE") &&
                obProblemCheckRepository.countIdByContainer(entity.getProblemStoragelocation(), entity.getId()) == 0) {

            //获取storageLocationId
            StorageLocation storageLocation = obProblemRepository.getByStorageLocationName(entity.getProblemStoragelocation());
            //获取 unitLoad
            UnitLoad unitLoad = obProblemRepository.getUnitLoadByStorageLocationId(storageLocation.getId());

            BigDecimal amount = BigDecimal.ZERO;
            if (unitLoad != null) {
                amount = obProblemRepository.sumByUnitLoadId(unitLoad.getId());
                //判断容器中是否还有商品
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    unitLoad.setEntityLock(Constant.GOING_TO_DELETE);
//                    unitLoad.setStorageLocation(null);
                    unitLoad.setWeightCalculated(BigDecimal.ZERO);
                    unitLoad.setStationName(null);
                    unitLoadRepository.saveAndFlush(unitLoad);
                }
                else {
                    unitLoad.setEntityLock(Constant.NOT_LOCKED);
                    unitLoad.setStationName(null);
                    unitLoadRepository.save(unitLoad);
                }
            }
        }
        return obProblemCheckRepository.saveAndFlush(entity);
    }

    @SuppressWarnings("Duplicates")
    //盘盈
    public void overageGoods(OverageGoodsDTO overageGoodsDTO) {
        StorageLocation destination = storageLocationRepository.retrieve(overageGoodsDTO.getDestinationId());
        ItemData itemData = itemDataRepository.getByItemNoes(
                overageGoodsDTO.getItemNo(), overageGoodsDTO.getClientId(), applicationContext.getCurrentWarehouse());
        if (itemData == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_SKU_NOT_FOUND.getName(), overageGoodsDTO.getItemNo());
        }
        // 获取有效期
        Lot lot = null;
        if (itemData.isLotMandatory()) {
            if (overageGoodsDTO.getUseNotAfter() != null || !"".equals(overageGoodsDTO.getUseNotAfter())) ;
            lot = buildBusiness.buildLot(itemData, overageGoodsDTO.getUseNotAfter());
        }
        // 检查目的地容器
        UnitLoad destinationUnitLoad = unitLoadRepository.getByStorageLocationAndEntityLock(destination);
        if (destinationUnitLoad == null) {
            destinationUnitLoad = buildBusiness.buildUnitLoad(destination);
        } else {
            checkingBusiness.checkingUnitLoadEntityLock(destinationUnitLoad);
            // 检查目的地容器是否存在不同客户的相同商品
            checkingBusiness.checkStorageLocationClient(destinationUnitLoad, itemData);
            //检查目的地容器是否存在不同有效期的相同商品
            checkingBusiness.checkStorageLocationLot(destinationUnitLoad, itemData, lot);
            if (checkingBusiness.isBin(destination)) {
                // 货位中存在相似商品
                checkingBusiness.checkStorageLocationSimilar(destinationUnitLoad, itemData);
            }
        }
        if (checkingBusiness.isBin(destination)) {
            // 超过货位的载重量
            checkingBusiness.checkStorageLocationWeight(destinationUnitLoad, itemData, overageGoodsDTO.getAmount());
            // 商品属性与货位设置不符
            checkingBusiness.checkStorageLocationItemGroup(destination, itemData);
            // 商品种类超过系统设置数量
            checkingBusiness.checkStorageLocationItemsAmount(destinationUnitLoad, itemData);
        }
        // 盘盈
        // 检查目的容器是否存在这种商品
        List<StockUnit> destinationStockUnits = stockUnitRepository
                .getByUnitLoadAndItemData(destinationUnitLoad, itemData);
        StockUnit destinationStockUnit;
        if (destinationStockUnits.isEmpty()) {
            destinationStockUnit = buildBusiness.buildStockUnit(
                    overageGoodsDTO.getAmount(), destinationUnitLoad, itemData, lot,null);
        } else {
            destinationStockUnit = destinationStockUnits.get(0);
            destinationStockUnit.setAmount(destinationStockUnit.getAmount().add(overageGoodsDTO.getAmount()));
            destinationStockUnit = stockUnitRepository.save(destinationStockUnit);
        }
        // 更新目的容器的重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(overageGoodsDTO.getAmount());
        destinationUnitLoad.setWeightCalculated(
                destinationUnitLoad.getWeightCalculated().add(itemDataWeight));
        unitLoadRepository.save(destinationUnitLoad);
        // 生成盘盈记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(StockUnitRecordState.OVERAGE_GOODS_RECORD_CODE.getName());
        stockUnitRecord.setRecordTool(StockUnitRecordState.OVERAGE_GOODS_RECORD_TOOL.getName());
        stockUnitRecord.setRecordType(StockUnitRecordState.OVERAGE_GOODS_RECORD_TYPE.getName());
        stockUnitRecord.setAmount(overageGoodsDTO.getAmount());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
        if (destination.getStorageLocationType() != null){
            stockUnitRecord.setToState(destination.getStorageLocationType().getInventoryState());
        }else {
            stockUnitRecord.setToState(overageGoodsDTO.getInventoryState());
        }
        stockUnitRecord.setToStockUnit(destinationStockUnit.getId());
        stockUnitRecord.setToUnitLoad(destinationUnitLoad.getLabel());
        stockUnitRecord.setToStorageLocation(destination.getName());
        if (lot != null) {
            stockUnitRecord.setLot(lot.getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
//        Client client = clientRepository.retrieve(destinationStockUnit.getClientId());
//        stockUnitRecord.setClient(client);
        stockUnitRecord.setClientId(destinationStockUnit.getClientId());
        stockUnitRecord.setWarehouseId(destinationStockUnit.getWarehouseId());
        stockUnitRecord.setAdjustReason(overageGoodsDTO.getAdjustReason());
        stockUnitRecord.setProblemDestination(overageGoodsDTO.getProblemDestination());
        stockUnitRecord.setThoseResponsible(overageGoodsDTO.getThoseResponsible());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    @SuppressWarnings("Duplicates")
    //盘亏
    public void lossGoods(LossGoodsDTO lossGoodsDTO) {
        StorageLocation source = storageLocationRepository.retrieve(lossGoodsDTO.getSourceId());

        ItemData itemData = itemDataRepository.getByItemNo(applicationContext.getCurrentWarehouse(),applicationContext.getCurrentClient(),lossGoodsDTO.getItemDataId());
        UnitLoad sourceUnitLoad = unitLoadRepository.getByStorageLocationAndEntityLock(source);
        if (sourceUnitLoad == null) {
            throw new ApiException(InternalToolException
                    .EX_IT_STORAGE_LOCATION_NOT_FOUND.getName(), source.getName());
        }
        checkingBusiness.checkingUnitLoadEntityLock(sourceUnitLoad);
        // 检查移动货物数量是否超过系统设定
        BigDecimal sourceAmount = generalBusiness.getStockUnitAmount(sourceUnitLoad, itemData);
        if (sourceAmount.compareTo(lossGoodsDTO.getAmount()) == -1) {
            throw new ApiException(InternalToolException.EX_IT_AMOUNT_MORE_THAN_SYSTEM_AMOUNT.getName());
        }
        // 减去盘亏的数量
        List<StockUnit> sourceStockUnits = stockUnitRepository.getByUnitLoadAndItemData(sourceUnitLoad, itemData);
        BigDecimal lossGoodsAmount = lossGoodsDTO.getAmount();
        StockUnit sourceStockUnit = sourceStockUnits.get(0);
        for (StockUnit stockUnit : sourceStockUnits) {
            BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            if (stockAmount.compareTo(lossGoodsAmount) >= 0) {
                stockUnit.setAmount(stockAmount.subtract(lossGoodsAmount));
                break;
            } else {
                lossGoodsAmount = lossGoodsAmount.subtract(stockAmount);
                stockUnit.setAmount(BigDecimal.ZERO);
            }
        }
        stockUnitRepository.save(sourceStockUnits);
        // 更新原始容器重量
        BigDecimal itemDataWeight = itemData.getWeight().multiply(lossGoodsDTO.getAmount());
        sourceUnitLoad.setWeightCalculated(
                sourceUnitLoad.getWeightCalculated().subtract(itemDataWeight));
        unitLoadRepository.save(sourceUnitLoad);
        // 生成盘亏记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        stockUnitRecord.setRecordCode(StockUnitRecordState.LOSS_GOODS_RECORD_CODE.getName());
        stockUnitRecord.setRecordTool(StockUnitRecordState.LOSS_GOODS_RECORD_TOOL.getName());
        stockUnitRecord.setRecordType(StockUnitRecordState.LOSS_GOODS_RECORD_TYPE.getName());
        stockUnitRecord.setAmount(lossGoodsDTO.getAmount());
        stockUnitRecord.setFromStockUnit(sourceStockUnit.getId());
        stockUnitRecord.setFromUnitLoad(sourceUnitLoad.getLabel());
        stockUnitRecord.setFromStorageLocation(source.getName());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
        stockUnitRecord.setFromState(sourceStockUnit.getState());
        stockUnitRecord.setToState(sourceStockUnit.getState());
        if (sourceStockUnit.getLot() != null) {
            stockUnitRecord.setLot(sourceStockUnit.getLot().getLotNo());
        }
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
//        Client client = clientRepository.retrieve(sourceStockUnit.getClientId());
//        stockUnitRecord.setClient(client);
        stockUnitRecord.setClientId(sourceStockUnit.getClientId());
        stockUnitRecord.setWarehouseId(sourceStockUnit.getWarehouseId());
        stockUnitRecord.setAdjustReason(lossGoodsDTO.getAdjustReason());
        stockUnitRecord.setProblemDestination(lossGoodsDTO.getProblemDestination());
        stockUnitRecord.setThoseResponsible(lossGoodsDTO.getThoseResponsible());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    //多货上架
    public void stowOverageGoods(StorageLocation storageLocation,
                                 ItemData itemData,
                                 BigDecimal amount,String jobType) {
        // 数量不容许为零
        if (amount.equals(BigDecimal.ZERO)) {
            throw new ApiException(InternalToolException.EX_AMOUNT_IS_ZERO.getName());
        }
        // 查看这个货位中是否存在这种商品
        UnitLoad unitLoad = unitLoadRepository.getByStorageLocationLimit(storageLocation);
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(unitLoad, itemData);
        if (unitLoad == null || stockUnits.isEmpty()) {
            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU.getName(),
                    storageLocation.getName(), itemData.getItemNo());
        }
        // 多货上架实际系统中数量不变，所以直接生成历史记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        String code = "";
        String tool = "";
        String type = "";
        if (jobType.equalsIgnoreCase("Rebin")){
            code = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_CODE_REBIN.getName();
            tool = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_TOOL_REBIN.getName();
            type = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_TYPE_REBIN.getName();
        }else if (jobType.equalsIgnoreCase("Pack")){
            code = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_CODE_PACK.getName();
            tool = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_TOOL_PACK.getName();
            type = StockUnitRecordState.STOW_OVERAGE_GOODS_RECORD_TYPE_PACK.getName();
        }
        stockUnitRecord.setRecordCode(code);
        stockUnitRecord.setRecordTool(tool);
        stockUnitRecord.setRecordType(type);
        stockUnitRecord.setAmount(amount);
        stockUnitRecord.setFromStorageLocation(storageLocation.getName());
        stockUnitRecord.setFromUnitLoad(unitLoad.getLabel());
        stockUnitRecord.setToStorageLocation(storageLocation.getName());
        stockUnitRecord.setToUnitLoad(unitLoad.getLabel());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
//        stockUnitRecord.setFromState(generalBusiness.getInventoryState(storageLocation));
//        stockUnitRecord.setToState(generalBusiness.getInventoryState(storageLocation));
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
//        Client client = clientRepository.retrieve(itemData.getClientId());
//        stockUnitRecord.setClient(client);
        stockUnitRecord.setClientId(itemData.getClientId());
        stockUnitRecord.setWarehouseId(itemData.getWarehouseId());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

    @SuppressWarnings("Duplicates")
    //少货上架
    public void stowLossGoods(StorageLocation from,StorageLocation to,
                              ItemData itemData,
                              BigDecimal amount,String jobType) {
        // 数量不容许为零
        if (amount.equals(BigDecimal.ZERO)) {
            throw new ApiException(InternalToolException.EX_AMOUNT_IS_ZERO.getName());
        }
        // 查看这个货位中是否存在这种商品

        UnitLoad fromUnitLoad = unitLoadRepository.getByStorageLocationAndEntityLock(from);
        UnitLoad toUnitLoad = unitLoadRepository.getByStorageLocationLimit(to);
        List<StockUnit> fromUnitLoads = stockUnitRepository.getByUnitLoadAndItemData(fromUnitLoad, itemData);
        List<StockUnit> stockUnits = stockUnitRepository.getByUnitLoadAndItemData(toUnitLoad, itemData);

//        if (toUnitLoad == null || stockUnits.isEmpty()) {
//            throw new ApiException(InternalToolException.EX_IT_STORAGE_LOCATION_NOT_FOUND_SKU.toString(),
//                    to.getName(), itemData.getItemNo());
//        }
        // 原始容器减去相应的数量
        for (StockUnit stockUnit : fromUnitLoads) {
            BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            if (stockAmount.compareTo(amount) >= 0) {
                stockUnit.setAmount(stockAmount.subtract(amount));
                break;
            } else {
                amount = amount.subtract(stockAmount);
                stockUnit.setAmount(BigDecimal.ZERO);
            }
        }
        // 目的容器加上相应的数量
        for (StockUnit stockUnit : stockUnits) {
            //         BigDecimal stockAmount = stockUnit.getAmount().subtract(stockUnit.getReservedAmount());
            BigDecimal stockAmount = stockUnit.getAmount();
            stockUnit.setAmount(stockAmount.add(amount));
        }
        stockUnitRepository.save(stockUnits);
        // 生成历史记录
        StockUnitRecord stockUnitRecord = new StockUnitRecord();
        String code = "";
        String tool = "";
        String type = "";
        if (jobType.equalsIgnoreCase("Rebin")){
            code = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_CODE_REBIN.getName();
            tool = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_TOOL_REBIN.getName();
            type = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_TYPE_REBIN.getName();
        }else if (jobType.equalsIgnoreCase("Pack")){
            code = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_CODE_PACK.getName();
            tool = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_TOOL_PACK.getName();
            type = StockUnitRecordState.STOW_LOSS_GOODS_RECORD_TYPE_PACK.getName();
        }
        stockUnitRecord.setRecordCode(code);
        stockUnitRecord.setRecordTool(tool);
        stockUnitRecord.setRecordType(type);
        stockUnitRecord.setAmount(amount);
        stockUnitRecord.setFromStorageLocation(from.getName());
        stockUnitRecord.setFromUnitLoad(fromUnitLoad.getLabel());
        stockUnitRecord.setToStorageLocation(to.getName());
        stockUnitRecord.setToUnitLoad(toUnitLoad.getLabel());
        stockUnitRecord.setItemNo(itemData.getItemNo());
        stockUnitRecord.setSku(itemData.getSkuNo());
//        stockUnitRecord.setFromState(generalBusiness.getInventoryState(from));
//        stockUnitRecord.setToState(generalBusiness.getInventoryState(to));
        stockUnitRecord.setOperator(SecurityUtils.getCurrentUsername());
//        Client client = clientRepository.retrieve(itemData.getClientId());
//        stockUnitRecord.setClient(client);
        stockUnitRecord.setClientId(itemData.getClientId());
        stockUnitRecord.setWarehouseId(itemData.getWarehouseId());
        stockUnitRecordRepository.save(stockUnitRecord);
    }

}
