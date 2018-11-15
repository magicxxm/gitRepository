package wms.facade.impl;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wms.business.dto.AdjustConfirmDTO;
import wms.business.dto.ApiDTO;
import wms.business.AdjustBusiness;
import wms.business.dto.ProblemDTO;
import wms.common.crud.AccessDTO;
import wms.crud.dto.*;
import wms.facade.ApiFacade;
import wms.service.*;

/**
 * Created by 123 on 2017/12/29.
 */
@Component
public class ApiFacadeImpl implements ApiFacade {
    private final Logger log = LoggerFactory.getLogger(ApiFacadeImpl.class);

    //商品同步
    @Value("${wms.url.path_item_sync}")
    private String path_item_sync;

    //入库单同步
    @Value("${wms.url.path_receipt_sync}")
    private String path_receipt_sync;

    //出库单同步
    @Value("${wms.url.path_shipment_sync}")
    private String path_shipment_sync;

    //订单取消
    @Value("${wms.url.path_shipment_cancel}")
    private String path_shipment_cancel;

    //盘点单同步
    @Value("${wms.url.path_check_sync}")
    private String path_check_sync;

    //库存查询
    @Value("${wms.url.path_inventory_get}")
    private String path_inventory_get;

    //复核查询
    @Value("${wms.url.path_pack_check}")
    private String path_pack_check;

    //复核确认
    @Value("${wms.url.path_pack_confirm}")
    private String path_pack_confirm;

    //箱型同步
    @Value("${wms.url.path_box_sync}")
    private String path_box_sync;

    //待调整确认
    @Value("${wms.url.path_adjustconfirm_url}")
    private String path_adjustconfirm_url;

    //复核触发接口
    @Value("${wms.url.path_trigger_url}")
    private String path_packStation_trigger;

    //包装工作站登录
    @Value(("${wms.url.path_station_login}"))
    private String path_station_login;

    //拍灯
    @Value("${wms.url.path_digital_closeDigital}")
    private String path_digital_closeDigital;

    //获取灯未亮的订单
    @Value("${wms.url.path_digital_get}")
    private String path_digital_get;

    @Value("${wms.url.path_problem_checkStorage}")
    private String path_problem_checkStorage;

    @Value("${wms.url.path_problem_handle}")
    private String path_problem_handle;

    @Value("${wms.url.path_station_loginOut}")
    private String path_station_loginOut;

    private final Item item;
    private final Receipt receipt;
    private final Shipment shipment;
    private final Check check;
    private final BoxService boxService;
    private final Pack pack;
    private final StockUnitService stockUnitService;
    private final AdjustBusiness adjustBusiness;

    public ApiFacadeImpl(Item item,Receipt receipt,Shipment shipment,Check check,BoxService boxService,Pack pack,
                         StockUnitService stockUnitService,AdjustBusiness adjustBusiness){
        this.item = item;
        this.receipt = receipt;
        this.shipment = shipment;
        this.check = check;
        this.boxService = boxService;
        this.pack = pack;
        this.adjustBusiness = adjustBusiness;
        this.stockUnitService = stockUnitService;
    }

    @Override
    public AccessDTO syncIn(ApiDTO apiDTO) {

        AccessDTO accessDTO = new AccessDTO();
        //根据api判断进入请求
        String api = apiDTO.getApi();
//        JSONObject jsonObject = apiDTO.getData();
        String data = apiDTO.getData();
        log.info("收到的数据是： " + data);
        JSONObject jsonObject = JSONObject.fromObject(data);
        //商品同步
;        if(this.path_item_sync.equalsIgnoreCase(api)){
            log.info("调用商品同步接口。。。");
            AnntoItemDTO anntoItemDTO = (AnntoItemDTO)JSONObject.toBean(jsonObject,AnntoItemDTO.class);
            accessDTO = item.synchronous(anntoItemDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("商品同步成功。。。");
            }
        }
        //入库单同步
        if(this.path_receipt_sync.equalsIgnoreCase(api)){
            log.info("调用入库单同步接口。。。");
            ReceiptUpdateDTO receiptUpdateDTO = (ReceiptUpdateDTO)JSONObject.toBean(jsonObject,ReceiptUpdateDTO.class);
            accessDTO = receipt.update(receiptUpdateDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("入库单同步成功。。。");
            }
        }
        //出库单同步
        if(this.path_shipment_sync.equalsIgnoreCase(api)){
            log.info("调用出库单同步接口。。。");
            ShipmentUpdateDTO shipmentUpdateDTO = (ShipmentUpdateDTO)JSONObject.toBean(jsonObject,ShipmentUpdateDTO.class);
            accessDTO = shipment.update(shipmentUpdateDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("出库单同步成功。。。");
            }
        }
        //订单取消
        if(this.path_shipment_cancel.equalsIgnoreCase(api)){
            log.info("调用订单取消接口。。。");
            ShipmentCancelDTO shipmentCancelDTO = (ShipmentCancelDTO)JSONObject.toBean(jsonObject,ShipmentCancelDTO.class);
            accessDTO = shipment.cancel(shipmentCancelDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("订单取消成功。。。");
            }
        }
        //盘点单同步
        if(this.path_check_sync.equalsIgnoreCase(api)){
            log.info("调用盘点单同步接口。。。");
            CheckUpdateDTO checkUpdateDTO = (CheckUpdateDTO)JSONObject.toBean(jsonObject,CheckUpdateDTO.class);
            accessDTO = check.update(checkUpdateDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("盘点单同步成功。。。");
            }
        }
        //箱型同步
        if(this.path_box_sync.equalsIgnoreCase(api)){
            log.info("调用箱型同步接口。。。");
            BoxDTO boxDTO = (BoxDTO)JSONObject.toBean(jsonObject,BoxDTO.class);
            accessDTO = boxService.synchronous(boxDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("箱型同步成功。。。");
            }
        }
        //库存查询
        if(this.path_inventory_get.equalsIgnoreCase(api)){
            log.info("调用库存查询接口。。。");
            StockUnitCheckDTO stockUnitCheckDTO = (StockUnitCheckDTO)JSONObject.toBean(jsonObject,StockUnitCheckDTO.class);
            accessDTO = stockUnitService.getStockUnit(stockUnitCheckDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("库存查询成功。。。");
            }
        }
        //复核触发
        if(this.path_packStation_trigger.equalsIgnoreCase(api)){
            log.info("调用复核触发接口。。。");
            String digitallabelId = jsonObject.getString("digitallabelId");
            pack.triggerInfo(digitallabelId);
        }

        //复核查询
        if(this.path_pack_check.equalsIgnoreCase(api)){
            log.info("调用复核查询接口。。。");
            //获取参数时可以一个一个获取，不用直接转为dto
//            PackDTO packDTO = (PackDTO)JSONObject.toBean(jsonObject,PackDTO.class);
            PackDTO packDTO = new PackDTO();
            accessDTO = pack.getInfo(packDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("复核查询调用成功。。。");
            }
        }
        // 复核确认
        if(this.path_pack_confirm.equalsIgnoreCase(api)){
            log.info("调用复核确认接口。。。");
//            PackConfirmDTO packConfirmDTO = (PackConfirmDTO)JSONObject.toBean(jsonObject,PackConfirmDTO.class);
            PackConfirmDTO packConfirmDTO = new PackConfirmDTO();
            accessDTO = pack.confirm(packConfirmDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("复核确认调用成功。。。");
            }
        }

        //待调整确认
        if(this.path_adjustconfirm_url.equalsIgnoreCase(api)){
            log.info("调用待调整确认接口。。。");
            AdjustConfirmDTO adjustConfirmDTO = (AdjustConfirmDTO)JSONObject.toBean(jsonObject,AdjustConfirmDTO.class);
            accessDTO = adjustBusiness.adjust(adjustConfirmDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("待调整确认调用成功。。。");
            }
        }

        //包装工作站登录
        if(this.path_station_login.equalsIgnoreCase(api)){
            log.info("包装工作站登录。。。");
            PackDTO packDTO = new PackDTO();
            packDTO.setUser(jsonObject.getString("user"));
            packDTO.setWarehouseCode(jsonObject.getString("warehouseCode"));
            packDTO.setStationCode(jsonObject.getString("stationCode"));
            accessDTO = pack.loginStation(packDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("包装工作站登录成功。。。");
            }
        }

        //拍灯
        if(this.path_digital_closeDigital.equalsIgnoreCase(api)){
            log.info("拍灯啦。。。。");
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setDigitalId(jsonObject.getString("digitalId"));
            storageDTO.setCompanyCode(jsonObject.getString("companyCode"));
            storageDTO.setStationCode(jsonObject.getString("stationCode"));
            storageDTO.setWarehouseCode(jsonObject.getString("warehouseCode"));
            storageDTO.setUser(jsonObject.getString("user"));
            accessDTO = pack.getlightOff(storageDTO);
//            String digitallabelId = jsonObject.getString("digitalId");
//            pack.triggerInfo(digitallabelId);
            if(accessDTO.getCode().equals("0")){
                log.info("拍灯调用成功。。。");
            }
        }

        //获取灯未亮的订单
        if(this.path_digital_get.equalsIgnoreCase(api)){
            log.info("获取灯未亮的订单 。。。");
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setPickPackWallId(jsonObject.getString("pickPackWallId"));
            storageDTO.setCompanyCode(jsonObject.getString("companyCode"));
            storageDTO.setStationCode(jsonObject.getString("stationCode"));
            storageDTO.setWarehouseCode(jsonObject.getString("warehouseCode"));
            storageDTO.setUser(jsonObject.getString("user"));
            accessDTO = pack.getShipmentByDigital(storageDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("获取灯未亮的订单调用成功。。。");
            }
        }

        //检查问题订单的货框
        if(this.path_problem_checkStorage.equalsIgnoreCase(api)){
            log.info("检查问题订单的货框。。。");
            StorageDTO storageDTO = new StorageDTO();
            storageDTO.setShipmentCode(jsonObject.getString("shipmentCode"));
            storageDTO.setPrimaryWaybillCode(jsonObject.getString("primaryWaybillCode"));
            storageDTO.setPickPackCellCode(jsonObject.getString("pickPackCellCode"));
            storageDTO.setProblemType(jsonObject.getString("problemType"));
            storageDTO.setStorageLocation(jsonObject.getString("containerCode"));
            storageDTO.setCompanyCode(jsonObject.getString("companyCode"));
            storageDTO.setWarehouseCode(jsonObject.getString("warehouseCode"));
            storageDTO.setUser(jsonObject.getString("user"));
//            storageDTO.setCode(jsonObject.getString("code"));
//            storageDTO.setLossAmount(Integer.parseInt(jsonObject.getString("lossAmount")));
            accessDTO = pack.checkStorage(storageDTO);
            if(accessDTO.getCode().equals("0")){
                log.info("检查问题订单的货框调用成功。。。");
            }
        }

        //上报问题处理
        if(this.path_problem_handle.equalsIgnoreCase(api)){
            log.info("上报问题处理。。。");
            //数据一个一个获取
//            ProblemDTO problemDTO = (ProblemDTO)JSONObject.toBean(jsonObject,ProblemDTO.class);
            accessDTO.setMsg("问题订单上报成功");
        }

        //退出工作站
        if(this.path_station_loginOut.equalsIgnoreCase(api)){
            log.info("包装站台退出。。。");
            String stationCode = jsonObject.getString("stationCode");
            String warehoseCode = jsonObject.getString("warehoseCode");
            accessDTO = pack.loginOut(stationCode,warehoseCode);
            if(accessDTO.getCode().equals("0")){
                log.info("包装站台退出成功。。。");
            }
        }


        return accessDTO;
    }
}
