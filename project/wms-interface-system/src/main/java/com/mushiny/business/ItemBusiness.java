package com.mushiny.business;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.constants.State;
import com.mushiny.model.*;
import com.mushiny.repository.ItemGroupRepository;
import com.mushiny.repository.ItemUnitRepository;
import com.mushiny.utils.ItemUtil;
import com.mushiny.utils.StringUtil;
import com.mushiny.web.dto.ItemDataPosition;
import com.mushiny.web.dto.SkuNoPosition;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
@Component
public class ItemBusiness {
    private final Logger log = LoggerFactory.getLogger(ItemBusiness.class);

    private final ItemGroupRepository itemGroupRepository;
    private final ItemUnitRepository itemUnitRepository;
    private final EntityManager manager;

    @Autowired
    private RestTemplateBuilder builder;

    @Value("${wms.update.itemSize}")
    private String itemUrl;

    @Autowired
    public ItemBusiness(ItemGroupRepository itemGroupRepository,
                        ItemUnitRepository itemUnitRepository,
                        EntityManager manager){
        this.itemGroupRepository = itemGroupRepository;
        this.itemUnitRepository = itemUnitRepository;
        this.manager = manager;
    }

    public ItemDataGlobal synchronous(ItemDataPosition itemDTO){
        AccessDTO accessDTO = new AccessDTO();
        log.info("开始同步商品信息到ItemDataGloable。。。。");

        ItemDataGlobal itemDataGlobal = new ItemDataGlobal();
        itemDataGlobal.setDepth(new BigDecimal(StringUtil.stringToint(itemDTO.getLength())));
        itemDataGlobal.setHeight(new BigDecimal(StringUtil.stringToint(itemDTO.getHeight())));

        String group = ItemUtil.changeGroup(itemDTO.getCategory());
        ItemGroup itemGroup = itemGroupRepository.getByName(group);
        itemDataGlobal.setItemGroup(itemGroup);

        itemDataGlobal.setItemNo(itemDTO.getItemNo());
        itemDataGlobal.setName(itemDTO.getName());
        itemDataGlobal.setDescription(itemDTO.getDescription());

//        String unitName = ItemUtil.changeUnit(itemDTO.getUnit());
        ItemUnit itemUnit = itemUnitRepository.getByName(itemDTO.getUnit());
        if(itemUnit == null){
            log.info("给商品："+ itemDTO.getItemNo() + " 增加单位 ：" + itemDTO.getUnit());
            itemUnit = this.generateItemUnit(itemDTO.getUnit());
        }
        itemDataGlobal.setItemUnit(itemUnit);


        int shelflife = StringUtil.stringToint(itemDTO.getShelflife());
        if(shelflife > 0){
            itemDataGlobal.setLotMandatory(true);
            itemDataGlobal.setLotType("EXPIRATION");
            itemDataGlobal.setLotUnit("DAY");
        }
//        itemDataGlobal.setMeasured(itemDTO.isMeasured());
//        itemDataGlobal.setMultiplePart(itemDTO.isMultiplePart());
//        itemDataGlobal.setMultiplePartAmount(itemDTO.getMultiplePartAmount());
//        itemDataGlobal.setName(itemDTO.getName());
//        itemDataGlobal.setPreferBag(itemDTO.isPreferBag());
//        itemDataGlobal.setPreferOwnBox(itemDTO.isPreferOwnBox());
//        itemDataGlobal.setSafetyStock(itemDTO.getSafetyStock());
        itemDataGlobal.setSerialRecordType(ItemUtil.changeSerialNo(itemDTO.getSerialNumber()));
//        itemDataGlobal.setSerialRecordLength(itemDTO.getSerialLength());
//        itemDataGlobal.setSerialRegular(itemDTO.getSerialRegular());
        itemDataGlobal.setWeight(new BigDecimal(StringUtil.stringToint(itemDTO.getWeight())));
        itemDataGlobal.setWidth(new BigDecimal(StringUtil.stringToint(itemDTO.getWidth())));
        itemDataGlobal.setShelflife(shelflife);

//        itemDataGlobal.setUseBubbleFilm(itemDTO.isUseBubbleFilm());
        BigDecimal volume = StringUtil.stringToDouble(itemDTO.getVolume());
        itemDataGlobal.setVolume(volume);

        itemDataGlobal.setSize("中");

        manager.persist(itemDataGlobal);

        return itemDataGlobal;
    }

    private ItemUnit generateItemUnit(String unit) {
        ItemUnit itemUnit = new ItemUnit();
        itemUnit.setName(unit);
        itemUnit.setUnitType(unit);
        manager.persist(itemUnit);
        return itemUnit;
    }

    public ItemData synchronousItemData(ItemDataGlobal itemDataGlobal) {
        ItemData itemData = new ItemData();
        log.info("同步ItemData === > " + itemDataGlobal.getItemNo());
        itemData.setItemNo(itemDataGlobal.getItemNo());
        itemData.setName(itemDataGlobal.getName());//商品名称
        itemData.setDescription(itemDataGlobal.getDescription());
        itemData.setDepth(itemDataGlobal.getDepth());
        itemData.setHeight(itemDataGlobal.getHeight());
        itemData.setWidth(itemDataGlobal.getWidth());
        //单位默认 “个”，即wms中的 “单品”
        itemData.setItemUnit(itemDataGlobal.getItemUnit());

        //商品分组  和安得确认用具体代码表示哪一类分组
        itemData.setItemGroup(itemDataGlobal.getItemGroup());

        //是否是有效期商品
        itemData.setLotMandatory(itemDataGlobal.isLotMandatory());
        //拒收天数
        itemData.setLotThreshold(itemDataGlobal.getLotThreshold());
        //有效期类型
        itemData.setLotType(itemDataGlobal.getLotType());

        //有效期单位
        itemData.setLotUnit(itemDataGlobal.getLotUnit());
        //是否测量
        itemData.setMeasured(itemDataGlobal.isMeasured());
        //是否自带包装
        itemData.setPreferOwnBox(itemDataGlobal.isPreferOwnBox());
        //是否提供袋子
        itemData.setPreferBag(itemDataGlobal.isPreferBag());
        //是否使用气垫膜
        itemData.setUseBubbleFilm(itemDataGlobal.isUseBubbleFilm());
        itemData.setMultiplePartAmount(itemDataGlobal.getMultiplePartAmount());
        itemData.setMultiplePart(itemDataGlobal.isMultiplePart());
        //安全库存
        itemData.setSafetyStock(itemDataGlobal.getSafetyStock());
        //序列号规则
        itemData.setSerialRegular(itemDataGlobal.getSerialRegular());
        //是否记录序列号
        itemData.setSerialRecordType(itemDataGlobal.getSerialRecordType());
        itemData.setVolume(itemDataGlobal.getVolume());
        itemData.setWeight(itemDataGlobal.getWeight());
        itemData.setShelflife(itemDataGlobal.getShelflife());

        itemData.setState(State.RAW);
        itemData.setSize(itemDataGlobal.getSize());

        itemData.setItemDataGlobalId(itemDataGlobal.getId());

//        itemData.setClientId(client.getId());
//        itemData.setWarehouseId(warehouse.getId());

        manager.persist(itemData);

        return itemData;

    }

    public ItemDataGlobal update(ItemDataGlobal itemDataGlobal,ItemDataPosition itemDTO) {

        log.info("更新 " + itemDataGlobal.getItemNo() +" itemDataGlobal 信息。。。。");

        itemDataGlobal.setDepth(new BigDecimal(StringUtil.stringToint(itemDTO.getLength())));
        itemDataGlobal.setHeight(new BigDecimal(StringUtil.stringToint(itemDTO.getHeight())));

        String group = ItemUtil.changeGroup(itemDTO.getCategory());
        ItemGroup itemGroup = itemGroupRepository.getByName(group);
        itemDataGlobal.setItemGroup(itemGroup);

        itemDataGlobal.setItemNo(itemDTO.getItemNo());

//        String unitName = ItemUtil.changeUnit(itemDTO.getUnit());
        ItemUnit itemUnit = itemUnitRepository.getByName(itemDTO.getUnit());
        if(itemUnit == null){
            log.info("给商品："+ itemDTO.getItemNo() + " 增加单位 ：" + itemDTO.getUnit());
            itemUnit = this.generateItemUnit(itemDTO.getUnit());
        }
        itemDataGlobal.setItemUnit(itemUnit);


        int shelflife = StringUtil.stringToint(itemDTO.getShelflife());
        if(shelflife > 0){
            itemDataGlobal.setLotMandatory(true);
            itemDataGlobal.setLotType("EXPIRATION");
            itemDataGlobal.setLotUnit("DAY");
        }else {
            itemDataGlobal.setLotMandatory(false);
            itemDataGlobal.setLotType(null);
            itemDataGlobal.setLotUnit(null);
        }

//        itemDataGlobal.setMeasured(itemDTO.isMeasured());
//        itemDataGlobal.setMultiplePart(itemDTO.isMultiplePart());
//        itemDataGlobal.setMultiplePartAmount(itemDTO.getMultiplePartAmount());
        itemDataGlobal.setName(itemDTO.getName());
        itemDataGlobal.setDescription(itemDTO.getDescription());
//        itemDataGlobal.setPreferBag(itemDTO.isPreferBag());
//        itemDataGlobal.setPreferOwnBox(itemDTO.isPreferOwnBox());
//        itemDataGlobal.setSafetyStock(itemDTO.getSafetyStock());
        itemDataGlobal.setSerialRecordType(ItemUtil.changeSerialNo(itemDTO.getSerialNumber()));
//        itemDataGlobal.setSerialRecordLength(itemDTO.getSerialLength());
//        itemDataGlobal.setSerialRegular(itemDTO.getSerialRegular());
        itemDataGlobal.setWeight(new BigDecimal(StringUtil.stringToint(itemDTO.getWeight())));
        itemDataGlobal.setWidth(new BigDecimal(StringUtil.stringToint(itemDTO.getWidth())));
        itemDataGlobal.setShelflife(shelflife);

//        itemDataGlobal.setUseBubbleFilm(itemDTO.isUseBubbleFilm());
        BigDecimal volume = StringUtil.stringToDouble(itemDTO.getVolume());
        itemDataGlobal.setVolume(volume);
        itemDataGlobal.setSize("中 ");

        return itemDataGlobal;
    }

    public ItemData updataItem(ItemData itemData, ItemDataGlobal itemDataGlobal) {

        log.info("更新ItemData === > " + itemDataGlobal.getItemNo());

        itemData.setItemNo(itemDataGlobal.getItemNo());
        itemData.setName(itemDataGlobal.getName());//商品名称
        itemData.setDescription(itemDataGlobal.getDescription());
        itemData.setDepth(itemDataGlobal.getDepth());
        itemData.setHeight(itemDataGlobal.getHeight());
        itemData.setWidth(itemDataGlobal.getWidth());
        //单位默认 “个”，即wms中的 “单品”
        itemData.setItemUnit(itemDataGlobal.getItemUnit());

        //商品分组  和安得确认用具体代码表示哪一类分组
        itemData.setItemGroup(itemDataGlobal.getItemGroup());

        //是否是有效期商品
        itemData.setLotMandatory(itemDataGlobal.isLotMandatory());
        //拒收天数
        itemData.setLotThreshold(itemDataGlobal.getLotThreshold());
        //有效期类型
        itemData.setLotType(itemDataGlobal.getLotType());

        //有效期单位
        itemData.setLotUnit(itemDataGlobal.getLotUnit());
        //是否测量
        itemData.setMeasured(itemDataGlobal.isMeasured());
        //是否自带包装
        itemData.setPreferOwnBox(itemDataGlobal.isPreferOwnBox());
        //是否提供袋子
        itemData.setPreferBag(itemDataGlobal.isPreferBag());
        //是否使用气垫膜
        itemData.setUseBubbleFilm(itemDataGlobal.isUseBubbleFilm());
        itemData.setMultiplePartAmount(itemDataGlobal.getMultiplePartAmount());
        itemData.setMultiplePart(itemDataGlobal.isMultiplePart());
        //安全库存
        itemData.setSafetyStock(itemDataGlobal.getSafetyStock());
        //序列号规则
        itemData.setSerialRegular(itemDataGlobal.getSerialRegular());
        //是否记录序列号
        itemData.setSerialRecordType(itemDataGlobal.getSerialRecordType());
        itemData.setVolume(itemDataGlobal.getVolume());
        itemData.setWeight(itemDataGlobal.getWeight());
        itemData.setShelflife(itemDataGlobal.getShelflife());
        itemData.setSize(itemDataGlobal.getSize());

        itemData.setItemDataGlobalId(itemDataGlobal.getId());

//        itemData.setClientId(client.getId());
//        itemData.setWarehouseId(warehouse.getId());

        return itemData;
    }

    public ItemSkuNo createSkuNo(SkuNoPosition p) {
        ItemSkuNo itemSkuNo = new ItemSkuNo();
        itemSkuNo.setItemNo(p.getItemNo());
        itemSkuNo.setSkuNo(p.getSkuNo());

        manager.persist(itemSkuNo);

        return itemSkuNo;
    }

    public void updateItemSize(List<String> itemIds) {

        RestTemplate restTemplate = builder.build();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        JSONArray array = JSONArray.fromObject(itemIds);

        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        restTemplate.postForObject(this.itemUrl,array.toString(),String.class);
    }
}
