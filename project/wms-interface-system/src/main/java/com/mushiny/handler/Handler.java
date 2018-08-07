package com.mushiny.handler;

import com.mushiny.business.ItemBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.service.*;
import com.mushiny.utils.CommonUtil;
import com.mushiny.web.dto.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2018/3/20.
 */
@Component
public class Handler {

    private final ItemService itemService;
    private final SequenceService sequenceService;
    private final CustomerService customerService;
    private final AdviceService adviceService;
    private final ChangeStockService changeStockService;
    private final ItemBusiness itemBusiness;

    @Autowired
    public Handler(ItemService itemService,
                   SequenceService sequenceService,
                   CustomerService customerService,
                   AdviceService adviceService,
                   ItemBusiness itemBusiness,
                   ChangeStockService changeStockService){
        this.itemService = itemService;
        this.sequenceService = sequenceService;
        this.customerService = customerService;
        this.adviceService = adviceService;
        this.itemBusiness = itemBusiness;
        this.changeStockService = changeStockService;
    }


    public String handleMessage(int type,String text){
        String msg = "";
        //商品同步
        if(CommonUtil.ITEMDATA_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            String itemList = object.getString("positions");
            JSONArray array = JSONArray.fromObject(itemList);
            List<ItemDataPosition> positions = (List<ItemDataPosition>) JSONArray.toCollection(array,ItemDataPosition.class);
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setPositions(positions);

            List<String> itemIds =  itemService.synchronous(itemDTO);

            itemBusiness.updateItemSize(itemIds);

        }

        //商品条码同步
        if(CommonUtil.SKU_NO_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            String skuList = object.getString("positions");
            JSONArray array = JSONArray.fromObject(skuList);
            List<SkuNoPosition> positions = (List<SkuNoPosition>)JSONArray.toCollection(array,SkuNoPosition.class);
            SkuNoDTO skuNoDTO = new SkuNoDTO();
            skuNoDTO.setPositions(positions);
            AccessDTO accessDTO = itemService.updateItem(skuNoDTO);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        //串码信息同步
        if(CommonUtil.SEQUENCE_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            String skuList = object.getString("positions");
            JSONArray array = JSONArray.fromObject(skuList);
            List<ChuanMaInfoPosition> positions = (List<ChuanMaInfoPosition>)JSONArray.toCollection(array,ChuanMaInfoPosition.class);

            ChuanMaInfo chuanMaInfo = new ChuanMaInfo();
            chuanMaInfo.setPositions(positions);
            AccessDTO accessDTO = sequenceService.createSequence(chuanMaInfo);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        //出库单信息同步
        if(CommonUtil.OUTBOUND_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            CustomerShipmentDTO shipmentDTO = (CustomerShipmentDTO)JSONObject.toBean(object,CustomerShipmentDTO.class);

            String shipmentPositions = object.getString("positions");
            JSONArray array = JSONArray.fromObject(shipmentPositions);
            List<CustomerShipmentPositionDTO> positions = (List<CustomerShipmentPositionDTO>)JSONArray.toCollection(array,CustomerShipmentPositionDTO.class);
            shipmentDTO.setPositions(positions);

            AccessDTO accessDTO = customerService.createCustomerShipment(shipmentDTO);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        //上架单信息同步
        if(CommonUtil.INBOUND_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            String advicePositions = object.getString("positions");
            JSONArray array = JSONArray.fromObject(advicePositions);
            List<AdviceReceiptPositionDTO> positions = (List<AdviceReceiptPositionDTO>)JSONArray.toCollection(array,AdviceReceiptPositionDTO.class);

            AdviceReceiptDTO adviceReceiptDTO = (AdviceReceiptDTO)JSONObject.toBean(object,AdviceReceiptDTO.class);
            adviceReceiptDTO.setPositions(positions);

            AccessDTO accessDTO = adviceService.createAdviceRequest(adviceReceiptDTO);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        //优先级改变
        if(CommonUtil.PRIORITY == type){
            JSONObject object = JSONObject.fromObject(text);
            String skuList = object.getString("positions");
            JSONArray array = JSONArray.fromObject(skuList);
            List<PriorityPosition> positions = (List<PriorityPosition>)JSONArray.toCollection(array,PriorityPosition.class);
            Priority priority = new Priority();
            priority.setPositions(positions);
            AccessDTO accessDTO = customerService.updateDeliveryTime(priority);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        //调整单信息同步
        if(CommonUtil.CHANGE_TYPE == type){
            JSONObject object = JSONObject.fromObject(text);
            String changePositions = object.getString("positions");
            JSONArray array = JSONArray.fromObject(changePositions);
            List<StockChangePosition> positions = (List<StockChangePosition>)JSONArray.toCollection(array,StockChangePosition.class);

            StockChangeInfo stockChangeInfo = (StockChangeInfo)JSONObject.toBean(object,StockChangeInfo.class);
            stockChangeInfo.setPositions(positions);

            AccessDTO accessDTO = changeStockService.changeStockUnit(stockChangeInfo);
            JSONObject result = JSONObject.fromObject(accessDTO);
            return result.toString();
        }

        return msg;
    }

}
