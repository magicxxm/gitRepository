package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.business.dto.ReceivingGoodsDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.StockUnitRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.UnitLoadRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ReceivingGoodsBusiness {

    private final AdviceRequestBusiness adviceRequestBusiness;
    private final StockUnitBusiness stockUnitBusiness;
    private final GoodsReceiptBusiness goodsReceiptBusiness;
    private final UnitLoadBusiness unitLoadBusiness;
    private final SerialNumberBusiness serialNumberBusiness;
    private final StockUnitRepository stockUnitRepository;
    private final UnitLoadRepository unitLoadRepository;

    @Autowired
    public ReceivingGoodsBusiness(AdviceRequestBusiness adviceRequestBusiness,
                                  StockUnitBusiness stockUnitBusiness,
                                  GoodsReceiptBusiness goodsReceiptBusiness,
                                  StockUnitRepository stockUnitRepository,
                                  UnitLoadBusiness unitLoadBusiness,
                                  UnitLoadRepository unitLoadRepository,
                                  SerialNumberBusiness serialNumberBusiness) {
        this.adviceRequestBusiness = adviceRequestBusiness;
        this.stockUnitBusiness = stockUnitBusiness;
        this.goodsReceiptBusiness = goodsReceiptBusiness;
        this.stockUnitRepository = stockUnitRepository;
        this.unitLoadBusiness = unitLoadBusiness;
        this.unitLoadRepository = unitLoadRepository;
        this.serialNumberBusiness = serialNumberBusiness;
    }

    public void receivingInventoryGoods(AdviceRequest adviceRequest,
                                        ItemData itemData,
                                        ReceivingGoodsDTO receiving) {
        if (receiving.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(InBoundException.EX_AMOUNT_IS_ZERO.toString());
        }
        // 容器是否为收货小车
//        if (!container.getContainerType().getName().contains(ContainerTypeName.RECEIVE.getName())) {
//            throw new ApiException(InBoundException
//                    .EX_CONTAINER_NOT_IS_RECEIVE.toString(), container.getName());
//        }
//        // 容器是否可以收货
//        receivingProcessContainerBusiness.checkContainerToReceive(container);
        // 检查收货单是否被激活
        adviceRequestBusiness.checkActivatedDN(adviceRequest);
        // 检查商品是否在收货单上
        adviceRequestBusiness.checkAdviceRequestItemData(adviceRequest, itemData);
        // 容器中不能存在不同客户的相同商品
//        stockUnitBusiness.checkItemClient(container, itemData);
        // 检查SN
        serialNumberBusiness.checkAndSaveSerialNo(itemData,receiving.getSerialNo(),receiving.getAmount());
        // 容器中不能存放同一商品的不同有效期
//        stockUnitBusiness.checkItemLot(container, itemData, receiving.getUseNotAfter());
        // 收货
//        receivingGoods(adviceRequest, itemData, container, receiving);
    }

    public void receivingDamageGoods(AdviceRequest adviceRequest,
                                     ItemData itemData,
                                     ReceivingGoodsDTO receiving) {
        if (receiving.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ApiException(InBoundException.EX_AMOUNT_IS_ZERO.toString());
        }
        // 判断是否为残品容器
//        if (!container.getContainerType().getName().contains(ContainerTypeName.DAMAGE.getName())) {
//            throw new ApiException(InBoundException
//                    .EX_CONTAINER_NOT_IS_DAMAGE.toString(), container.getName());
//        }
        // 容器是否可以收货
//        receivingProcessContainerBusiness.checkContainerToReceive(container);
        // 检查收货单是否被激活
        adviceRequestBusiness.checkActivatedDN(adviceRequest);
        // 检查商品是否在收货单上
        adviceRequestBusiness.checkAdviceRequestItemData(adviceRequest, itemData);
        // 容器中不能存在不同客户的相同商品
//        stockUnitBusiness.checkItemClient(container, itemData);
        // 检查SN
        serialNumberBusiness.checkAndSaveSerialNo(itemData,receiving.getSerialNo(),receiving.getAmount());
        // 容器中不能存放同一商品的不同有效期
//        stockUnitBusiness.checkItemLot(container, itemData, receiving.getUseNotAfter());
        // 收货
//        receivingGoods(adviceRequest, itemData, container, receiving);
    }

    public void receivingCubiScanGoods(AdviceRequest adviceRequest,
                                       ItemData itemData,
                                       ReceivingGoodsDTO receiving) {
        if (receiving.getAmount().compareTo(BigDecimal.ONE) != 0) {
            throw new ApiException(InBoundException.EX_AMOUNT_ERROR.toString());
        }
//        if (!itemData.isMeasured()) {
//            throw new ApiException(ExceptionEnum.EX_OPERATION_ERROR.toString());
//        }
        // 判断是否为待测量容器
//        if (!container.getContainerType().getName().contains(ContainerTypeName.CUBI_SCAN.getName())) {
//            throw new ApiException(InBoundException
//                    .EX_CONTAINER_NOT_IS_CUBI_SCAN.toString(), container.getName());
//        }
        // 容器是否可以收货
//        receivingProcessContainerBusiness.checkContainerToReceive(container);
        // 检查收货单是否被激活
        adviceRequestBusiness.checkActivatedDN(adviceRequest);
        // 检查商品是否在收货单上
        adviceRequestBusiness.checkAdviceRequestItemData(adviceRequest, itemData);
        // 检查SN
        serialNumberBusiness.checkAndSaveSerialNo(itemData,receiving.getSerialNo(),receiving.getAmount());
        // 因为是测量，所以所有客户相同SKU的商品，只要测量一件就可以了
//        StockUnit stockUnit = stockUnitRepository.getByContainerAndItemNo(container, itemData.getItemNo());
//        if (stockUnit != null) {
//            throw new ApiException(InBoundException
//                    .EX_CUBI_SCAN_CONTAINER_HAS_THIS_SKU.toString(), container.getName(), itemData.getItemNo());
//        }
        // 收货
//        receivingGoods(adviceRequest, itemData, container, receiving);
    }

    private void receivingGoods(AdviceRequest adviceRequest,
                                ItemData itemData,
                                ReceivingGoodsDTO receiving) {
//        if (itemData.isLotMandatory()) {
//            lot = lotBusiness.checkAndSaveLot(itemData, receiving.getUseNotAfter());
//        }
//        // 生成收货记录
//        StockUnit stockUnit = stockUnitRepository.getByContainerAndItemData(container, itemData);
//        if(stockUnit == null){
//            stockUnit = stockUnitBusiness.buildStockUnit(adviceRequest.getWarehouse(), adviceRequest.getClient(),
//                    itemData, lot, receiving.getAmount());
//            stockUnit.setContainer(container);
//        }else {
//            stockUnit.setAmount(stockUnit.getAmount().add(receiving.getAmount()));
//        }
//        String stockUnitState;// StockUnit状态
//        String stockUnitRecordCode;// StockUnitRecord的ActivityCode
//        if (container.getContainerType().getName().contains(ContainerTypeName.DAMAGE.getName())) {
//            stockUnitState = StockUnitState.DAMAGE.getName();
//            stockUnitRecordCode = StockUnitRecordState.RECEIVE_DAMAGE.getName();
//        } else if (container.getContainerType().getName().contains(ContainerTypeName.CUBI_SCAN.getName())) {
//            stockUnitState = StockUnitState.INVENTORY.getName();
//            stockUnitRecordCode = StockUnitRecordState.RECEIVE_CUBI_SCAN.getName();
//        } else {
//            stockUnitState = StockUnitState.INVENTORY.getName();
//            stockUnitRecordCode = StockUnitRecordState.RECEIVE.getName();
//        }
//        stockUnit.setState(stockUnitState);
//        // 生成UnitLoad
//        UnitLoad unitLoad = unitLoadRepository.getByContainer(container);
//        if(unitLoad == null){
//            unitLoad = unitLoadBusiness.buildAndSave(stockUnit);
//        }
//        stockUnit.setUnitLoad(unitLoad);
//        stockUnit = stockUnitRepository.save(stockUnit);
//        // 生成收货历史记录
//        StockUnitRecord stockUnitRecord = stockUnitBusiness.buildStockUnitRecord(
//                stockUnit, receiving.getReceivingType(), receiving.getAmount());
//        stockUnitRecord.setActivityCode(stockUnitRecordCode);
//        stockUnitRecordRepository.save(stockUnitRecord);
//        goodsReceiptBusiness.buildAndSaveGoodsReceiptPosition(
//                stockUnit, container, adviceRequest, stockUnitRecord);
    }
}
