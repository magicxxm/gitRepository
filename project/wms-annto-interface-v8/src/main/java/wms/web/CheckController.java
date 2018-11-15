package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.common.crud.AccessDTO;
import wms.crud.dto.AdjustConfirmDTO;
import wms.crud.dto.CheckConfirmDTO;
import wms.crud.dto.CheckUpdateDTO;
import wms.service.Check;
import wms.web.vm.AdjustItemDTO;

import java.util.List;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/robot/check")
public class CheckController {

    private final Check check;

    @Autowired
    public CheckController(Check check) {
        this.check = check;
    }

    @RequestMapping(value = "/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> checkUpdate(@RequestBody CheckUpdateDTO dto) {
        return ResponseEntity.ok(check.update(dto));
    }

    @RequestMapping(value = "/confirm",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> checkConfirm(@RequestBody CheckConfirmDTO checkConfirmDTO) {
        check.confirm(checkConfirmDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/adjust",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> adjustVerfy(@RequestBody List<AdjustItemDTO> adjustItemDTOS) {

        check.adjustItem(adjustItemDTOS);

        return ResponseEntity.ok().build();
    }

  /*  @RequestMapping(value = "/confirm",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> adjustConfirm(@RequestBody List<AdjustConfirmDTO> adjustConfirmDTOS) {

        return ResponseEntity.ok(check.adjustConfirm(adjustConfirmDTOS));
    }*/
}
