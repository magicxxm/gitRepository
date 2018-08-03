package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequest;
import com.mushiny.wms.masterdata.ibbasics.domain.AdviceRequestPosition;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptPositionRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class AdviceRequestBusiness {

    private final ApplicationContext applicationContext;
    private final AdviceRequestRepository adviceRequestRepository;
    private final AdviceRequestPositionRepository adviceRequestPositionRepository;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final GoodsReceiptPositionRepository goodsReceiptPositionRepository;

    @Autowired
    public AdviceRequestBusiness(ApplicationContext applicationContext,
                                 AdviceRequestRepository adviceRequestRepository,
                                 AdviceRequestPositionRepository adviceRequestPositionRepository,
                                 GoodsReceiptRepository goodsReceiptRepository,
                                 GoodsReceiptPositionRepository goodsReceiptPositionRepository) {
        this.applicationContext = applicationContext;
        this.adviceRequestRepository = adviceRequestRepository;
        this.adviceRequestPositionRepository = adviceRequestPositionRepository;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.goodsReceiptPositionRepository = goodsReceiptPositionRepository;
    }

    public AdviceRequest getAdviceRequest(String adviceNo) {
        AdviceRequest adviceRequest = Optional
                .ofNullable(adviceRequestRepository.getByAdviceNo(adviceNo))
                .orElseThrow(() -> new ApiException(InBoundException
                        .EX_SCANNING_OBJECT_NOT_FOUND.toString(), adviceNo));
        // 检查DN  判断用户所操作的仓库是否是选择的仓库
        applicationContext.isCurrentWarehouse(adviceRequest.getWarehouseId());
        //  判断用户所操作的客户是否属于当前仓库的客户
        applicationContext.isCurrentClient(adviceRequest.getClientId());
        // 判断收货单是否被删除   EntityLock不等于0
        if (!adviceRequest.getEntityLock().equals(Constant.NOT_LOCKED)) {
            throw new ApiException(InBoundException
                    .EX_DN_HAS_DELETED.toString(), adviceNo);
        }
        return adviceRequest;
    }

    public BigDecimal getAdviceRequestItemDataAmount(AdviceRequest adviceRequest, ItemData itemData) {
        // 获取收货单上该商品的数量
        return adviceRequestPositionRepository.sumByItemData(adviceRequest, itemData);
    }

    public BigDecimal getGoodsReceiptAmount(AdviceRequest adviceRequest) {
        // 获取收货单已经收了的商品数量
        GoodsReceipt goodsReceipt = goodsReceiptRepository.getByAdviceRequest(adviceRequest);
        return goodsReceiptPositionRepository.sumByGoodsReceipt(goodsReceipt);
    }

    public BigDecimal getGoodsReceiptItemDataAmount(AdviceRequest adviceRequest, ItemData itemData) {
        // 获取收货单已经收了的商品数量
        GoodsReceipt goodsReceipt = goodsReceiptRepository.getByAdviceRequest(adviceRequest);
        return goodsReceiptPositionRepository.sumByItemNo(goodsReceipt, itemData.getItemNo());
    }

    public void checkActivatedDN(AdviceRequest adviceRequest) {
        // 判断收货单是否已被激活
        GoodsReceipt activatedGoodsReceipt = goodsReceiptRepository.getByAdviceRequest(adviceRequest);
        if (activatedGoodsReceipt == null) {
            throw new ApiException(InBoundException
                    .EX_DN_NOT_ACTIVATED.toString(), adviceRequest.getAdviceNo());
        }
    }

    public void checkNotActivatedDN(AdviceRequest adviceRequest) {
        // 判断收货单是否已被激活
        GoodsReceipt activatedGoodsReceipt = goodsReceiptRepository.getByAdviceRequest(adviceRequest);
        if (activatedGoodsReceipt != null) {
            throw new ApiException(InBoundException
                    .EX_DN_HAS_ACTIVATED.toString(), adviceRequest.getAdviceNo());
        }
    }

    public void checkAdviceRequestItemData(AdviceRequest adviceRequest, ItemData itemData) {
        // 判断该商品SKU是否在收货单上
        List<AdviceRequestPosition> adviceRequestPositions = adviceRequestPositionRepository.
                getByAdviceRequestAndItemData(adviceRequest, itemData);
        if (adviceRequestPositions == null || adviceRequestPositions.isEmpty()) {
            throw new ApiException(InBoundException
                    .EX_DN_SKU_NOT_FOUND.toString(), adviceRequest.getAdviceNo(), itemData.getItemNo());
        }
    }
}
