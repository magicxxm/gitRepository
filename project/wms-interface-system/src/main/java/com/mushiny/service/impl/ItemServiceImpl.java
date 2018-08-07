package com.mushiny.service.impl;

import com.mushiny.business.ItemBusiness;
import com.mushiny.common.crud.AccessDTO;
import com.mushiny.model.*;
import com.mushiny.repository.*;
import com.mushiny.service.ItemService;
import com.mushiny.web.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService {
    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    private final ItemBusiness itemBusiness;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final ItemDataGloableRepository itemDataGloableRepository;
    private final ItemDataRepository itemDataRepository;
    private final ItemSkuNoRepository itemSkuNoRepository;


    public ItemServiceImpl(ItemBusiness itemBusiness,
                           ClientRepository clientRepository,
                           WarehouseRepository warehouseRepository,
                           ItemDataGloableRepository itemDataGloableRepository,
                           ItemDataRepository itemDataRepository,
                           ItemSkuNoRepository itemSkuNoRepository){
        this.itemBusiness = itemBusiness;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.itemDataGloableRepository = itemDataGloableRepository;
        this.itemDataRepository = itemDataRepository;
        this.itemSkuNoRepository = itemSkuNoRepository;
    }

    @Override
    public List<String> synchronous(ItemDTO itemDTO) {

        List<String> itemIds = new ArrayList<>();
        List<ItemDataPosition> positions = itemDTO.getPositions();
        for (ItemDataPosition p:positions) {
            //查询该商品编号是否已经存在
            ItemDataGlobal itemDataGlobal = itemDataGloableRepository.getByItemDataNo(p.getItemNo());
            if(itemDataGlobal == null){
                itemDataGlobal = itemBusiness.synchronous(p);
            }else {
                itemDataGlobal = itemBusiness.update(itemDataGlobal,p);
            }

            ItemData itemData = itemDataRepository.getByItemNo(itemDataGlobal.getItemNo());
            if(itemData == null){
                itemData  = itemBusiness.synchronousItemData(itemDataGlobal);
            }else {
                itemData = itemBusiness.updataItem(itemData,itemDataGlobal);
            }

            itemIds.add(itemDataGlobal.getId());
        }


        return itemIds;
    }

    @Override
    public AccessDTO updateItem(SkuNoDTO dto) {
        log.info("更新商品条码信息。。。");
        AccessDTO accessDTO = new AccessDTO();

        //清除原有数据
        for (SkuNoPosition p:dto.getPositions()) {
            /*ItemDataGlobal itemDataGlobal = itemDataGloableRepository.getByItemDataNo(p.getItemNo());
            if(itemDataGlobal == null){
                log.info("ItemNo : " + p.getItemNo() +" 在库中没有对应的商品，不能更新条码。。。");
            accessDTO.setMsg("ItemNo : " + p.getItemNo() +" 在库中没有对应的商品，不能更新条码");
            return accessDTO;
            }
            itemDataGlobal.setSkuNo(p.getSkuNo());*/
            int amount =  itemSkuNoRepository.deleteByItemNo(p.getItemNo());
            log.info("删除商品编码：" + p.getItemNo() +" 的 "+amount+" 条码信息");
        }

        String skuNo = "";
        for (SkuNoPosition p:dto.getPositions()) {
            //创建商品条码
            log.info("创建商品： " +p.getItemNo() +" 的条码信息。。。");
            itemBusiness.createSkuNo(p);

        }

        return accessDTO;
    }

}
