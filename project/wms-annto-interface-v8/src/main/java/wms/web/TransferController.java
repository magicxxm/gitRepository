package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.service.Transfer;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.MovementDTO;
import wms.crud.dto.TransferConfirmDTO;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/transfer")
public class TransferController {

    private final Transfer transfer;

    @Autowired
    public TransferController(Transfer transfer) {
        this.transfer = transfer;
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> transferUpdate(@RequestBody MovementDTO dto) {
        return ResponseEntity.ok(transfer.update(dto));
    }

    @RequestMapping(value = "/confirm",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> transferConfirm(@RequestBody TransferConfirmDTO transferConfirmDTO) {
        transfer.confirm(transferConfirmDTO);
        return ResponseEntity.ok().build();
    }

}
