package wms.business;

import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.business.dto.ItemCheckDTO;
import wms.crud.dto.AnntoItemDTO;
import wms.crud.dto.ReceiptItemsDTO;
import wms.crud.dto.ReceiptUpdateDTO;
import wms.domain.*;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataRepository;
import wms.repository.common.WarehouseRepository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Created by 123 on 2017/8/8.
 */
@Component
public class ReceiptBusiness {
    private final Logger log = LoggerFactory.getLogger(ReceiptBusiness.class);
    private final EntityManager manager;
    private final WarehouseRepository warehouseRepository;
    private final ItemDataRepository itemDataRepository;
    private final ClientRepository clientRepository;
    private final AnntoBusiness anntoBusiness;
    private final ItemBusiness itemBusiness;

    @Autowired
    public ReceiptBusiness(EntityManager manager,
                       WarehouseRepository warehouseRepository,
                       ItemDataRepository itemDataRepository,
                       ClientRepository clientRepository,
                           AnntoBusiness anntoBusiness,
                           ItemBusiness itemBusiness) {
        this.warehouseRepository = warehouseRepository;
        this.itemDataRepository = itemDataRepository;
        this.manager = manager;
        this.clientRepository = clientRepository;
        this.anntoBusiness = anntoBusiness;
        this.itemBusiness = itemBusiness;
    }

    public void createAnntoReceipt(ReceiptUpdateDTO dto) {
        /**
         *   先将数据存至ANNTO信息表；
         */
        AnntoReceipt anntoReceipt = new AnntoReceipt();
        anntoReceipt.setWarehouseCode(dto.getWarehouseCode());
        anntoReceipt.setCompanyCode(dto.getCompanyCode());
        anntoReceipt.setCode(dto.getCode());
        anntoReceipt.setAnntoCode(dto.getAnntoCode());
        anntoReceipt.setReceiptType(dto.getReceiptType());
        anntoReceipt.setPln(dto.getPln());
        anntoReceipt.setReceiptNote(dto.getReceiptNote());

        JSONArray receiptArray = JSONArray.fromObject(dto.getReceiptItems());
        List<ReceiptItemsDTO> itemsDTOS = (List<ReceiptItemsDTO>)JSONArray.toCollection(receiptArray,ReceiptItemsDTO.class);
        for(int i = 0; i < itemsDTOS.size(); i++) {
            AnntoReceiptPosition anntoReceiptPosition = new AnntoReceiptPosition();

            anntoReceiptPosition.setLineNo(itemsDTOS.get(i).getLineNo());
            anntoReceiptPosition.setItemCode(itemsDTOS.get(i).getItemCode());
            anntoReceiptPosition.setItemName(itemsDTOS.get(i).getItemName());
            anntoReceiptPosition.setTotalQty(itemsDTOS.get(i).getTotalQty());
            anntoReceiptPosition.setOpenQty(itemsDTOS.get(i).getOpenQty());
            anntoReceiptPosition.setInventorySts(itemsDTOS.get(i).getInventorySts());
            anntoReceiptPosition.setUnit(itemsDTOS.get(i).getUnit());
            anntoReceiptPosition.setManufactureDate(itemsDTOS.get(i).getManufactureDate());
            anntoReceiptPosition.setExpirationDate(itemsDTOS.get(i).getExpirationDate());
            anntoReceiptPosition.setKitflag(itemsDTOS.get(i).getKitFlag());
            anntoReceiptPosition.setRemark(itemsDTOS.get(i).getRemark());

            anntoReceipt.addPosition(anntoReceiptPosition);
        }

        manager.persist(anntoReceipt);
    }

    public void saveToWmsReceipt(ReceiptUpdateDTO dto){
        //筛选数据存入原本WMS表中
        ReceiptRequest receipt = new ReceiptRequest();
        receipt.setReceiptNo(dto.getCode());
        receipt.setState("Raw");

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        Client client = clientRepository.findByClientNo(dto.getCompanyCode());
        if(warehouse == null){
            receipt.setWarehouseId("NONE");
        }else {
            receipt.setWarehouseId(warehouse.getId());
        }
        receipt.setClientId(client.getId());

        JSONArray receiptArray = JSONArray.fromObject(dto.getReceiptItems());
        List<ReceiptItemsDTO> items = (List<ReceiptItemsDTO>)JSONArray.toCollection(receiptArray,ReceiptItemsDTO.class);
        int amountTotal = 0;
        for(int i = 0; i < items.size(); i++) {
            ReceiptRequestPosition position = new ReceiptRequestPosition();

            List<ItemData> itemDatas = itemDataRepository.getByItemNoAndClientId(items.get(i).getItemCode(),client.getId());
            if(itemDatas.isEmpty()){
                log.info("wms 中没有这个商品。。。" + items.get(i).getItemName());
                return;
            }
            ItemData itemData = itemDatas.get(0);
            if(itemData == null){
                log.error("牧星系统中没有对应得商品："+items.get(i).getItemCode()+"  信息,进行商品查询");
                ItemCheckDTO itemCheckDTO = new ItemCheckDTO();
                itemCheckDTO.setCode(items.get(i).getItemCode());
                itemCheckDTO.setCompanyCode(dto.getCompanyCode());
                itemCheckDTO.setWarehouseCode(dto.getWarehouseCode());
                itemCheckDTO.setName(items.get(i).getItemName());
                AnntoItemDTO anntoItemDTO = anntoBusiness.getItem(itemCheckDTO);

                log.info("查询到的商品是：" + anntoItemDTO.getCode() + ".. name: " + anntoItemDTO.getName());
                //开始同步信息
                itemData = itemBusiness.synchronous(anntoItemDTO);
                log.info("商品:" + itemData.getItemNo() + ", itemName = "+itemData.getName() + ",,查询商品信息后同步成功");
            }
            position.setPositionNo(i+1);
            position.setItemData(itemData);
            position.setClientId(client.getId());
            position.setNotifiedAmount(new BigDecimal(items.get(i).getOpenQty()));//应收货数量
            if(warehouse == null){
                position.setWarehouseId("NONE");
            }else {
                position.setWarehouseId(warehouse.getId());
            }

            amountTotal = amountTotal + items.get(i).getOpenQty();

            receipt.addPosition(position);

        }
        receipt.setSize(amountTotal);

        manager.persist(receipt);
    }
}
