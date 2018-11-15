package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.service.Receipt;
import wms.common.crud.AccessDTO;
import wms.crud.dto.ReceiptUpdateDTO;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/robot/receipt")
public class ReceiptController {

    private final Receipt receipt;

    @Autowired
    public ReceiptController(Receipt receipt) {
        this.receipt = receipt;
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> receiptUpdate(@RequestBody ReceiptUpdateDTO dto) {
        return ResponseEntity.ok(receipt.update(dto));
    }

    @RequestMapping(value = "/confirm",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> receiptConfirm(@RequestParam String receiptNo) {
        receipt.confirm(receiptNo);
        return ResponseEntity.ok().build();
    }

}
