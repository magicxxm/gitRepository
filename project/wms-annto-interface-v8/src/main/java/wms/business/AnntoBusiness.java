package wms.business;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import wms.business.dto.*;
import wms.common.crud.AccessDTO;
import wms.constants.State;
import wms.crud.dto.*;
import wms.crud.dto.AdjustConfirmDTO;
import wms.crud.dto.TransferConfirmDTO;
import wms.domain.PendingAdjustRecord;
import wms.domain.common.CustomerShipment;
import wms.repository.common.PendingAdjustRecordRepository;
import wms.web.vm.AdjustItemDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/11.
 */
@Component
public class AnntoBusiness {

    private final Logger log = LoggerFactory.getLogger(AnntoBusiness.class);

    private final RestTemplateBuilder builder;
    private final PendingAdjustRecordRepository pendingAdjustRecordRepository;

    @Value("${wms.url.appkey}")
    private String appkey ;//= "annto";

    @Value("${wms.url.appSecret}")
    private String appSecret ;//= "midea";//约定的key

    //接口请求地址
    @Value("${wms.url.api_url}")
    private String url;

    //移库单确认
//    @Value("${wms.url.path_transfer_url}")
    private String path_transfer_url = "wms.robot.transfer.confirm" ;
    //出库单确认
    @Value("${wms.url.path_shipment_url}")
    private String path_shipment_url;
    //入库单确认
    @Value("${wms.url.path_receipt_url}")
    private String path_receipt_url ;
    //盘点单确认
    @Value("${wms.url.path_check_url}")
    private String path_check_url ;
    //补货单确认
//    @Value("${wms.url.path_allocatio_url}")
    private String path_allocatio_url = "wms.robot.allocatio.confirm" ;
    //商品查询
    @Value("${wms.url.path_item_get}")
    private String path_getItem_url ;
    //拣货完成，调用复核接口,复核触发
    @Value("${wms.url.path_trigger_url}")
    private String path_trigger_url ;
    //获取承运单号
    @Value("${wms.url.path_waybill_get}")
    private String path_getPrimaryWayBillCode_url ;
    //待调整审核
    @Value("${wms.url.path_adjustverify_url}")
    private String path_adjustverify_url;

    @Autowired
    public AnntoBusiness(RestTemplateBuilder builder,
                         PendingAdjustRecordRepository pendingAdjustRecordRepository) {
        this.builder = builder;
        this.pendingAdjustRecordRepository = pendingAdjustRecordRepository;
    }

    /**
     * 补货单确认
     * @param allocationConfirmDTO
     */
    public void confirmAllocation(AllocationConfirmDTO allocationConfirmDTO){
        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(allocationConfirmDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_allocatio_url,jsonObject.toString(),this.url);
        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用补货单确认接口成功，，返回结果 == 》" + res);
    }

    /**
     * 盘点单确认
     * @param checkConfirmDTO
     */
    public void confirmCheck(CheckConfirmDTO checkConfirmDTO){
        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(checkConfirmDTO);
        JSONObject resultJsonObject = createApiDTO(path_check_url,jsonObject.toString(),this.url);

        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用盘点单确认接口成功，，返回结果 == 》" + res);
    }

    /**
     * 入库单确认
     * @param receiptConfirmDTO
     */
    public void confirmReceipt(ReceiptConfirmDTO receiptConfirmDTO){
        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(receiptConfirmDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_receipt_url,jsonObject.toString(),this.url);
        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用入库单确认接口成功，，返回结果 == 》" + res);
    }

    /**
     * 调拨确认
     * @param transferConfirmDTO
     */
    public void confirmTransfer(TransferConfirmDTO transferConfirmDTO) {

        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(transferConfirmDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_transfer_url,jsonObject.toString(),this.url);
        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用调拨确认接口成功，，返回结果 == 》" + res);
    }

    /**
     * 出库单确认接口
     * @param shipmentConfirmDTO
     */
    public void confirmShipmet(ShipmentConfirmDTO shipmentConfirmDTO, CustomerShipment shipment){

        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(shipmentConfirmDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_shipment_url,jsonObject.toString(),this.url);
        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用出库单确认接口成功，，返回结果 == 》" + res);
        shipment.setState(State.CONFIRM_TO_ANNTO);
    }

    /**
     * 查询安得系统商品
     */
    public AnntoItemDTO getItem(ItemCheckDTO itemCheckDTO) {
        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(itemCheckDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_getItem_url,jsonObject.toString(),this.url);
        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);
        System.out.println("传给annto的数据是 formEntity ： " + formEntity);
        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用查询商品接口成功，，返回结果 == 》" + res);
        JSONObject jsonObject1 = JSONObject.fromObject(res);
        ItemResult itemResult = (ItemResult)JSONObject.toBean(jsonObject1,ItemResult.class);
        AnntoItemDTO anntoItemDTO = new AnntoItemDTO();
        if(itemResult != null){
            anntoItemDTO = (AnntoItemDTO)JSONObject.toBean(JSONObject.fromObject(itemResult.getData()),AnntoItemDTO.class);
        }
        return anntoItemDTO;
    }

    /**
     * 获取承运单号
     * @param shipmentToAnntoDTO
     * @return
     */
    public List<CydhDTO> acceptPrimaryWallBillCode(ShipmentToAnntoDTO shipmentToAnntoDTO){
        List<CydhDTO> cydhDTOList = new ArrayList<>();

        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(shipmentToAnntoDTO);
        JSONObject resultJsonObject = createApiDTO(this.path_getPrimaryWayBillCode_url,jsonObject.toString(),this.url);

        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用获取承运单号接口成功，，返回结果 == 》" + res);
        JSONObject jsonObject1 = JSONObject.fromObject(res);
        CydhResult cydhResult = (CydhResult)JSONObject.toBean(jsonObject1,CydhResult.class);

        if(cydhResult != null){
            List resultList = cydhResult.getData();
            for(int i = 0;i < resultList.size();i++){
                CydhDTO cydhDTO = (CydhDTO)JSONObject.toBean(JSONObject.fromObject(resultList.get(i)),CydhDTO.class);
                cydhDTOList.add(cydhDTO);
            }
        }

        return cydhDTOList;
    }

    /**
     * 复核触发
     * @param packInfo
     */
    public void triggerInfo(PackInfo packInfo) {

        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONObject jsonObject = JSONObject.fromObject(packInfo);
        JSONObject resultJsonObject = createApiDTO(this.path_trigger_url,jsonObject.toString(),this.url);

        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用复核触发接口成功，，返回结果 == 》" + res);
    }

    /**
     * 待调整审核
     * @param adjustItemDTOS
     */
    public void adjustItem(List<AdjustItemDTO> adjustItemDTOS){
        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONArray jsonObject = JSONArray.fromObject(adjustItemDTOS);
        JSONObject resultJsonObject = createApiDTO(this.path_adjustverify_url,jsonObject.toString(),this.url);

        HttpEntity<String> formEntity = new HttpEntity<String>(resultJsonObject.toString(), headers);

        String res = restTemplate.postForObject(this.url,formEntity,String.class);
        log.info("调用待调整审核接口成功，，返回结果 == 》" + res);
    }

    /**
     * 待调整确认
     * @param adjustConfirmDTOS
     */
    public AccessDTO adjustConfirm(List<AdjustConfirmDTO> adjustConfirmDTOS){
        AccessDTO accessDTO=new AccessDTO();
        if(adjustConfirmDTOS.isEmpty()){
            accessDTO.setCode("1");
            accessDTO.setMsg("对象为空");
            return accessDTO;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for(AdjustConfirmDTO adjustConfirmDTO:adjustConfirmDTOS){
            PendingAdjustRecord pendingAdjustRecord=pendingAdjustRecordRepository.findOne(adjustConfirmDTO.getPendingAdjustId());
            pendingAdjustRecord.setState(adjustConfirmDTO.getState());
            pendingAdjustRecord.setSureDate(LocalDateTime.parse(adjustConfirmDTO.getAgreeTime(),df));
            pendingAdjustRecordRepository.save(pendingAdjustRecord);
        }
        accessDTO.setCode("0");
        accessDTO.setMsg("待调整确认完成");
        return accessDTO;
    }

    private JSONObject createApiDTO(String api,String data,String url){
        //转换annto带签名格式的json
        ApiDTO apiDTO = new ApiDTO();

        apiDTO.setApi(api);
        apiDTO.setData(data);
        System.out.println("给 annto 的数据 data = " + data);
//        apiDTO.setData(jsonObject);
        apiDTO.setUrl(url);

        //生成签名
        /*String signInfo = this.appSecret+"api"+apiDTO.getApi()+"appkey"+this.appkey
                +"data"+apiDTO.getData() +"format"+apiDTO.getFormat()+"v"+apiDTO.getV()+this.appSecret;
        String sign = md5jdk(signInfo);
        log.info("###### 接口:"+ apiDTO.getUrl() +" >> MD5 加密后后的结果是 ： " + sign);
        apiDTO.setSign(sign);*/

        JSONObject jsonApiDTO = JSONObject.fromObject(apiDTO);

        return jsonApiDTO;
    }

    private String md5jdk(String message){
        String temp = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5MessageDigest = messageDigest.digest(message.getBytes());
            temp = convertByteToHexString(md5MessageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private String convertByteToHexString(byte[] bytes){
        String result = "";
        for(int i =0;i<bytes.length; i++){
            int temp = bytes[i] & 0xff;
            String tempHex = Integer.toHexString(temp);
            if(tempHex.length() < 2){
                result += "0"+tempHex;
            }else {
                result += tempHex;
            }
        }
        return result;
    }
}
