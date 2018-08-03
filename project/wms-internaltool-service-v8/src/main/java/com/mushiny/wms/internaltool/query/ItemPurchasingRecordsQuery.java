package com.mushiny.wms.internaltool.query;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.internaltool.common.domain.AdviceRequest;
import com.mushiny.wms.internaltool.common.domain.Client;
import com.mushiny.wms.internaltool.common.domain.GoodsReceiptPosition;
import com.mushiny.wms.internaltool.common.repository.AdviceRequestPositionRepository;
import com.mushiny.wms.internaltool.common.repository.ClientRepository;
import com.mushiny.wms.internaltool.common.repository.GoodsReceiptPositionRepository;
import com.mushiny.wms.internaltool.web.dto.ItemPurchasingRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemPurchasingRecordsQuery {

    private final ApplicationContext applicationContext;
    private final AdviceRequestPositionRepository adviceRequestPositionRepository;
    private final GoodsReceiptPositionRepository goodsReceiptPositionRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ItemPurchasingRecordsQuery(ApplicationContext applicationContext,
                                      AdviceRequestPositionRepository adviceRequestPositionRepository,
                                      GoodsReceiptPositionRepository goodsReceiptPositionRepository,
                                      ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.adviceRequestPositionRepository = adviceRequestPositionRepository;
        this.goodsReceiptPositionRepository = goodsReceiptPositionRepository;
        this.clientRepository = clientRepository;
    }

    public List<ItemPurchasingRecordDTO> getItemPurchasingRecords(String sku) {
        List<GoodsReceiptPosition> positions = goodsReceiptPositionRepository
                .getBySku(applicationContext.getCurrentWarehouse(), sku);
        List<ItemPurchasingRecordDTO> itemPurchasingRecords = new ArrayList<>();
        for (GoodsReceiptPosition position : positions) {
            AdviceRequest adviceRequest = position.getGoodsReceipt().getRelatedAdvice();
            ItemPurchasingRecordDTO itemPurchasingRecord = itemPurchasingRecords.stream()
                    .filter(entity -> entity.getDn().equals(adviceRequest.getAdviceNo()))
                    .filter(entity -> entity.getItemNo().equals(position.getItemData().getItemNo()))
                    .findFirst()
                    .orElse(null);
            if (itemPurchasingRecord == null) {
                itemPurchasingRecord = new ItemPurchasingRecordDTO();
                itemPurchasingRecord.setExpectedDelivery(adviceRequest.getExpectedDelivery());
                itemPurchasingRecord.setItemNo(position.getItemData().getItemNo());
                itemPurchasingRecord.setSkuNo(position.getItemData().getSkuNo());
                itemPurchasingRecord.setDn(adviceRequest.getAdviceNo());
                Client client = clientRepository.retrieve(position.getClientId());
                itemPurchasingRecord.setClientName(client.getName());
                itemPurchasingRecords.add(itemPurchasingRecord);
            }
            itemPurchasingRecord.setAmount(itemPurchasingRecord.getAmount().add(position.getAmount()));
        }
        return itemPurchasingRecords;
    }
}
