package wms.web;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.business.dto.ApiDTO;
import wms.common.crud.AccessDTO;
import wms.facade.ApiFacade;

/**
 * Created by 123 on 2017/12/29.
 */
@RestController
public class AnntoController {

    private final ApiFacade apiFacade;

    public AnntoController (ApiFacade apiFacade){
        this.apiFacade = apiFacade;
    }

    @RequestMapping(value = "/api/wms/sync/in",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> syncIn(@RequestBody ApiDTO apiDTO) {

        AccessDTO accessDTO = apiFacade.syncIn(apiDTO);

        return ResponseEntity.ok().body(accessDTO);
    }
}
