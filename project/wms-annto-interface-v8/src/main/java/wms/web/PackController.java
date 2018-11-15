package wms.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wms.common.crud.ResponseDTO;
import wms.crud.dto.StorageDTO;
import wms.test.sendMQ.Send;
import wms.business.dto.PackInfo;
import wms.common.crud.AccessDTO;
import wms.crud.dto.PackConfirmDTO;
import wms.crud.dto.PackDTO;
import wms.service.Pack;

/**
 * Created by 123 on 2017/9/3.
 */
@RestController
@RequestMapping("/wms/robot")
public class PackController {

    private final Pack pack;
    private final Send send;

    @Autowired
    public PackController(Pack pack,Send send){
        this.pack = pack;
        this.send = send;
    }

    //复核触发
    @GetMapping(path = "/packStation/trigger")
    public ResponseEntity<Void> trigger(@RequestParam String  digitallabelId){

        pack.triggerInfo(digitallabelId);
        return ResponseEntity.ok().build();
    }

    //复核查询
    @PostMapping(path = "/packStation/get")
    public ResponseEntity<AccessDTO> getPackInfo(@RequestBody PackDTO packDTO){

        AccessDTO accessDTO =  pack.getInfo(packDTO);
        return ResponseEntity.ok().body(accessDTO);
    }

    //复核确认
    @PostMapping(path = "/packStation/confirm")
    public ResponseEntity<AccessDTO> getPackInfo(@RequestBody PackConfirmDTO packConfirmDTO){

        AccessDTO accessDTO =  pack.confirm(packConfirmDTO);
        return ResponseEntity.ok().body(accessDTO);
    }

    //登录包装工作站
    @PostMapping(path = "/station/login")
    public ResponseEntity<AccessDTO> loginStation(@RequestBody PackDTO packDTO){
        AccessDTO accessDTO = pack.loginStation(packDTO);
        return ResponseEntity.ok().body(accessDTO);
    }

    //退出包装工作站
    @GetMapping(path = "/station/loginOut")
    public ResponseEntity<AccessDTO> loginOutStation(@RequestParam String stationCode,@RequestParam String warehoseCode){
        AccessDTO responseDTO = pack.loginOut(stationCode,warehoseCode);
        return ResponseEntity.ok().body(responseDTO);
    }

    //扫描问题货框
    @PostMapping(path = "/problem/checkStorage")
    public ResponseEntity<AccessDTO> checkStorage(@RequestBody StorageDTO storageDTO){
        AccessDTO responseDTO = pack.checkStorage(storageDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    //拍灯
    @PostMapping(path = "/digital/getlightOff")
    public ResponseEntity<AccessDTO> getlightOff(@RequestBody StorageDTO storageDTO){
        AccessDTO responseDTO = pack.getlightOff(storageDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
    //获取灯未亮
    @PostMapping(path = "/digital/getShipmentByDigital")
    public ResponseEntity<AccessDTO> getShipmentByDigital(@RequestBody StorageDTO storageDTO){
        AccessDTO responseDTO = pack.getShipmentByDigital(storageDTO);
        return ResponseEntity.ok().body(responseDTO);
    }
}
