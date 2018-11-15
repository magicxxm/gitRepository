package wms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.common.crud.AccessDTO;
import wms.crud.dto.BoxDTO;
import wms.service.BoxService;

/**
 * Created by 123 on 2017/12/6.
 */
@RestController
@RequestMapping("/wms/robot")
public class BoxController {

    @Autowired
    private BoxService boxService;

    //箱型同步
    @RequestMapping(value = "/containerInfo/update",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AccessDTO> itemUpdate(@RequestBody BoxDTO boxDTO) {

        return ResponseEntity.ok().body(boxService.synchronous(boxDTO));
    }
}
