package wms.service;

import org.springframework.web.bind.annotation.RequestBody;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.GoodsReceiptDTO;
import wms.crud.dto.ReceiptConfirmDTO;
import wms.crud.dto.ReceiptUpdateDTO;

public interface Receipt {

    AccessDTO update(ReceiptUpdateDTO dto);

    void confirm(String receiptNo);
}
