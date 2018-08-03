package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.utils.DateTimeUtil;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.domain.enums.GoodsReceiptState;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptRepository;
import com.mushiny.wms.masterdata.general.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GoodsReceiptBusiness {

    private final ApplicationContext applicationContext;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptPositionRepository goodsReceiptPositionRepository;

    @Autowired
    public GoodsReceiptBusiness(ApplicationContext applicationContext,
                                GoodsReceiptRepository goodsReceiptRepository,
                                GoodsReceiptPositionRepository goodsReceiptPositionRepository) {
        this.applicationContext = applicationContext;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptPositionRepository = goodsReceiptPositionRepository;
    }

    public GoodsReceipt buildAndSaveGoodsReceipt(AdviceRequest adviceRequest) {
        GoodsReceipt goodsReceipt = new GoodsReceipt();
        String grNo;
        boolean randomFlag = true;
        while (randomFlag) {
            grNo = RandomUtil.getGrNo();
            GoodsReceipt entity = goodsReceiptRepository.getByGrNo(grNo);
            if (entity == null) {
                goodsReceipt.setGrNo(grNo);
                randomFlag = false;
            }
        }
        goodsReceipt.setDeliveryNote(adviceRequest.getAdviceNo());
        goodsReceipt.setReceiptDate(LocalDateTime.now());
        goodsReceipt.setReceiptState(GoodsReceiptState.Accepted.toString());
//        goodsReceipt.seto(applicationContext.getCurrentUser());
        goodsReceipt.setRelatedAdvice(adviceRequest);
        goodsReceipt.setClientId(adviceRequest.getClientId());
        goodsReceipt.setWarehouseId(adviceRequest.getWarehouseId());
        return goodsReceiptRepository.save(goodsReceipt);
    }

    public GoodsReceiptPosition buildAndSaveGoodsReceiptPosition(StockUnit stockUnit,
                                                                 AdviceRequest adviceRequest,
//                                                                 StockUnitRecord stockUnitRecord,
                                                                 User user) {
        GoodsReceipt goodsReceipt = goodsReceiptRepository.getByAdviceRequest(adviceRequest);
//        ReceivingProcessContainer processContainer = receivingProcessContainerBusiness
//                .getStartedProcessContainer(container);
        GoodsReceiptPosition goodsReceiptPosition = new GoodsReceiptPosition();
        goodsReceiptPosition.setAmount(stockUnit.getAmount());
        goodsReceiptPosition.setItemData(stockUnit.getItemData().getItemNo());
//        if (stockUnit.getLot() != null) {
//            goodsReceiptPosition.setLot(stockUnit.getLot().getLotNo());
//        }
//        goodsReceiptPosition.setReceiptType(stockUnitRecord.getRecordType());
        goodsReceiptPosition.setStockUnit(stockUnit);
        goodsReceiptPosition.setOperator(user);
        goodsReceiptPosition.setGoodsReceipt(goodsReceipt);
        goodsReceiptPosition.setClientId(stockUnit.getClientId());
        goodsReceiptPosition.setWarehouseId(stockUnit.getWarehouseId());
        return goodsReceiptPositionRepository.save(goodsReceiptPosition);
    }
}
