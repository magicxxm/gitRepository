package wms.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import wms.business.dto.PackInfo;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.ReplenishmentDTO;
import wms.crud.dto.AllocationConfirmDTO;
import wms.service.Allocation;
import wms.test.sendMQ.Send;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by PC-4 on 2017/7/13.
 */
@RestController
@RequestMapping("/wms/sendmq")
public class AllocationController {

    private final Send send;

    @Autowired
    public AllocationController(Send send) {
        this.send = send;
    }

    @RequestMapping(value = "/msg",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> allocationUpdate(@RequestBody PackInfo dto) {

        JSONObject jsonObject = JSONObject.fromObject(dto);
        System.out.println("the send message is :" + jsonObject.toString());
        send.sendMsg(jsonObject.toString());

        return ResponseEntity.ok().build();
    }

  /* public static void main(String[] args){
//       String res = "{\"code\": \"商品编码，string (50)，必填\"," +
//               "\"warehouseCode\": \"仓库编码，string (50)，必填\"," +
//               "\"companyCode\": \"货主编码，string (50)，必填\"," +
//               "\"name\": \"商品名称，string (200)，必填\"}";
//
//       JSONObject jsonObject1 = JSONObject.fromObject(res);
//       ItemCheckDTO itemCheckDTO = (ItemCheckDTO)JSONObject.toBean(jsonObject1,ItemCheckDTO.class);
//       System.out.print(itemCheckDTO.toString());


//       String itemCodeArray = ",";
//       String itemDatas[] = itemCodeArray.trim().split(",");
//
//       System.out.print(itemDatas.length);
       String date = "2017-09-09 00:00:00";
       SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       Date d = null;
       try {
           d = s.parse(date);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       System.out.print(d);
   }*/

}
