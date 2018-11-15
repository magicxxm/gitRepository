package wms.business;

import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wms.crud.dto.AnntoItemDTO;
import wms.crud.dto.BarcodeDTO;
import wms.domain.AnntoItem;
import wms.domain.ItemData;
import wms.domain.ItemDataGlobal;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;
import wms.repository.AnntoRepository;
import wms.repository.common.*;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by 123 on 2017/8/10.
 */
@Component
public class ItemBusiness {
    private Logger log = LoggerFactory.getLogger(ItemBusiness.class);

    private final EntityManager manager;
    private final ItemUnitRepository itemUnitRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final AnntoRepository anntoRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;
    private final AnntoBusiness anntoBusiness;
    private final WarehouseBusiness warehouseBusiness;
    private final ClientBusiness clientBusiness;

    public ItemBusiness(EntityManager manager,
                        ItemUnitRepository itemUnitRepository,
                        ItemGroupRepository itemGroupRepository,
                        ItemDataGlobalRepository itemDataGlobalRepository,
                        AnntoRepository anntoRepository,
                        WarehouseRepository warehouseRepository,
                        ClientRepository clientRepository,
                        ItemDataRepository itemDataRepository,
                        AnntoBusiness anntoBusiness,
                        ClientBusiness clientBusiness,
                        WarehouseBusiness warehouseBusiness){
        this.manager = manager;
        this.itemUnitRepository = itemUnitRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.anntoRepository = anntoRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.itemDataRepository = itemDataRepository;
        this.anntoBusiness = anntoBusiness;
        this.warehouseBusiness = warehouseBusiness;
        this.clientBusiness = clientBusiness;
    }

    public void updateItem(AnntoItemDTO dto, AnntoItem entity) {
        log.info("updata anntoItem : itemCode : " + dto.getCode());
        entity.setExpireUnit(dto.getExpireUnit());
        entity.setLotMandatory(dto.getLotMandatory());
        entity.setIsNeedCushion(dto.getIsNeedCushion());
        entity.setIsOriginPacking(dto.getIsOriginPacking());
        entity.setIsPlasticFirst(dto.getIsPlasticFirst());
        entity.setRejectDays(dto.getRejectDays());
        entity.setExpireType(dto.getExpireType());
        entity.setTrackSerialRegular(dto.getTrackSerialRegular());
        entity.setIsMeasure(dto.getIsMeasure());
        entity.setName(dto.getName());
        entity.setWarehouseCode(dto.getWarehouseCode());
        entity.setUnitWidth(dto.getUnitWidth());
        entity.setUnitWeight(dto.getUnitWeight());
        entity.setUnitVolume(dto.getUnitVolume());
        entity.setUnitLength(dto.getUnitLength());
        entity.setUnitHeight(dto.getUnitHeight());
        entity.setUnitDesc(dto.getUnitDesc());
        entity.setTrackSerialNum(dto.getTrackSerialNum());
        entity.setPlQTY(dto.getPlQTY());
        entity.setPlDesc(dto.getPlDesc());
        entity.setPlaceOfOrigin(dto.getPlaceOfOrigin());
        entity.setItemStyle(dto.getItemStyle());
        entity.setItemSize(dto.getItemSize());
        entity.setItemColor(dto.getItemColor());
        entity.setExpiringDays(dto.getExpiringDays());
        entity.setDaysToExpire(dto.getDaysToExpire());
        entity.setCsWidth(dto.getCsWidth());
        entity.setCsWeight(dto.getCsWeight());
        entity.setCsVolume(dto.getCsVolume());
        entity.setCsQTY(dto.getCsQTY());
        entity.setCsLength(dto.getCsLength());
        entity.setCsHeight(dto.getCsHeight());
        entity.setCsDesc(dto.getCsDesc());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setClass03(dto.getClass03());
        entity.setClass02(dto.getClass02());
        entity.setClass01(dto.getClass01());
        entity.setBrand(dto.getBrand());

    }

    public void createItem(AnntoItemDTO dto) {
        log.info("create anntoItem ,,itemCode : " + dto.getCode() );
        AnntoItem entity = new AnntoItem();

        entity.setBrand(dto.getBrand());
        entity.setClass01(dto.getClass01());
        entity.setClass02(dto.getClass02());
        entity.setClass03(dto.getClass03());
        entity.setCompanyCode(dto.getCompanyCode());
        entity.setCsDesc(dto.getCsDesc());
        entity.setCsHeight(dto.getCsHeight());
        entity.setCsLength(dto.getCsLength());
        entity.setCsQTY(dto.getCsQTY());
        entity.setCsVolume(dto.getCsVolume());
        entity.setCsWeight(dto.getCsWeight());
        entity.setCsWidth(dto.getCsWidth());
        entity.setDaysToExpire(dto.getDaysToExpire());
        entity.setExpiringDays(dto.getExpiringDays());
        entity.setItemColor(dto.getItemColor());
        entity.setItemSize(dto.getItemSize());
        entity.setItemStyle(dto.getItemStyle());
        entity.setPlaceOfOrigin(dto.getPlaceOfOrigin());
        entity.setPlDesc(dto.getPlDesc());
        entity.setPlQTY(dto.getPlQTY());
        entity.setTrackSerialNum(dto.getTrackSerialNum());
        entity.setUnitDesc(dto.getUnitDesc());
        entity.setUnitHeight(dto.getUnitHeight());
        entity.setUnitLength(dto.getUnitLength());
        entity.setUnitVolume(dto.getUnitVolume());
        entity.setUnitWeight(dto.getUnitWeight());
        entity.setUnitWidth(dto.getUnitWidth());
        entity.setWarehouseCode(dto.getWarehouseCode());
        entity.setName(dto.getName());
        entity.setIsMeasure(dto.getIsMeasure());
        entity.setTrackSerialRegular(dto.getTrackSerialRegular());
        entity.setExpireType(dto.getExpireType());
        entity.setIsPlasticFirst(dto.getIsPlasticFirst());
        entity.setRejectDays(dto.getRejectDays());
        entity.setIsOriginPacking(dto.getIsOriginPacking());
        entity.setIsNeedCushion(dto.getIsNeedCushion());
        entity.setLotMandatory(dto.getLotMandatory());
        entity.setExpireUnit(dto.getExpireUnit());
        entity.setCode(dto.getCode());
        if(!dto.getBarcode().isEmpty()){
            JSONArray barCodeArray = JSONArray.fromObject(dto.getBarcode());
            List<BarcodeDTO> barcodeDTOS = (List<BarcodeDTO>)JSONArray.toCollection(barCodeArray,BarcodeDTO.class);
            entity.setBarcode(barcodeDTOS.get(0).getBarcode());
            entity.setQuantityUM(barcodeDTOS.get(0).getQuantityUM());
        }

        manager.persist(entity);

    }

    public ItemDataGlobal createItemDataGlobal(AnntoItemDTO dto) {
        ItemDataGlobal itemDataGlobal = new ItemDataGlobal();
        log.info("创建ItemDataGlobal === > " + dto.getCode());
        itemDataGlobal.setItemNo(dto.getCode());//商品itemNo
        if(!dto.getBarcode().isEmpty()){
            JSONArray barCodeArray = JSONArray.fromObject(dto.getBarcode());
            List<BarcodeDTO> barcodeDTOS = (List<BarcodeDTO>)JSONArray.toCollection(barCodeArray,BarcodeDTO.class);
            itemDataGlobal.setSkuNo(barcodeDTOS.get(0).getBarcode());//商品skuNo
        }
//        itemDataGlobal.setSkuNo(dto.getBarcode().get(0).getBarcode());//商品skuNo
        itemDataGlobal.setName(dto.getName());//商品名称
        itemDataGlobal.setDepth(dto.getUnitWidth());
        itemDataGlobal.setHeight(dto.getCsHeight());
        itemDataGlobal.setWidth(dto.getUnitLength());
        //单位默认 “个”，即wms中的 “单品”
        itemDataGlobal.setItemUnit(itemUnitRepository.getByName(dto.getUnitDesc()));

        //商品分组  和安得确认用具体代码表示哪一类分组
        //默认安得传的商品都是一个分类
        itemDataGlobal.setItemGroup(itemGroupRepository.getByName("百货"));


        //是否是有效期商品
        if("Y".equalsIgnoreCase(dto.getLotMandatory())){
            itemDataGlobal.setLotMandatory(true);
        }else if("N".equalsIgnoreCase(dto.getLotMandatory())){
            itemDataGlobal.setLotMandatory(false);
        }
        //拒收天数
        itemDataGlobal.setLotThreshold(dto.getRejectDays());
        //有效期类型
        if("E".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("EXPIRATION");
        }else if("P".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("MANUFACTURE");
        }else if("N".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("PC");
        }

        //有效期单位
        if("Y".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("YEAR");
        }else if("M".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("MONTH");
        }else if("D".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("DAY");
        }
        //是否测量
        if("Y".equalsIgnoreCase(dto.getIsMeasure())) {
            itemDataGlobal.setMeasured(true);
        }else {
            itemDataGlobal.setMeasured(false);
        }
        //是否自带包装
        if("Y".equalsIgnoreCase(dto.getIsOriginPacking())){
            itemDataGlobal.setPreferOwnBox(true);
        }else{
            itemDataGlobal.setPreferOwnBox(false);
        }
        //是否提供袋子
        if("Y".equalsIgnoreCase(dto.getIsPlasticFirst())){
            itemDataGlobal.setPreferBag(true);
        }else {
            itemDataGlobal.setPreferBag(false);

        }
        //是否使用气垫膜
        if("Y".equalsIgnoreCase(dto.getIsNeedCushion())){
            itemDataGlobal.setUseBubbleFilm(true);
        }else {
            itemDataGlobal.setUseBubbleFilm(false);
        }
        itemDataGlobal.setMultiplePartAmount(0);
        itemDataGlobal.setMultiplePart(false);
        //安全库存
        itemDataGlobal.setSafetyStock(10);
        //序列号规则
        itemDataGlobal.setSerialRegular(dto.getTrackSerialRegular());
        //是否记录序列号
        if(dto.getTrackSerialNum() == 0){
            itemDataGlobal.setSerialRecordType("NO_RECORD");
        }else if(dto.getTrackSerialNum() == 1 || dto.getTrackSerialNum() == 2 || dto.getTrackSerialNum() == 3){
            itemDataGlobal.setSerialRecordType("ALWAYS_RECORD");
        }
        itemDataGlobal.setVolume(dto.getUnitVolume());
        itemDataGlobal.setWeight(dto.getUnitWeight());

        manager.persist(itemDataGlobal);

        return itemDataGlobal;
    }

    public ItemDataGlobal updateItemDataGlobal(AnntoItemDTO dto, ItemDataGlobal itemDataGlobal) {
        log.info("更新ItemDataGlobal === > " + dto.getCode());
        itemDataGlobal.setName(dto.getName());//商品名称
        itemDataGlobal.setDepth(dto.getUnitWidth());
        itemDataGlobal.setHeight(dto.getCsHeight());
        itemDataGlobal.setWidth(dto.getUnitLength());
        //单位默认 “个”，即wms中的 “单品”
        itemDataGlobal.setItemUnit(itemUnitRepository.getByName(dto.getUnitDesc()));

        //商品分组  和安得确认用具体代码表示哪一类分组
        //默认安得传的商品都是一个分类
//        itemDataGlobal.setItemGroup(itemGroupRepository.getByName("百货"));

        //是否是有效期商品
        if("Y".equalsIgnoreCase(dto.getLotMandatory())){
            itemDataGlobal.setLotMandatory(true);
        }else if("N".equalsIgnoreCase(dto.getLotMandatory())){
            itemDataGlobal.setLotMandatory(false);
        }
        //拒收天数
        itemDataGlobal.setLotThreshold(dto.getRejectDays());
        //有效期类型
        if("E".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("EXPIRATION");
        }else if("P".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("MANUFACTURE");
        }else if("N".equalsIgnoreCase(dto.getExpireType())){
            itemDataGlobal.setLotType("PC");
        }

        //有效期单位
        if("Y".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("YEAR");
        }else if("M".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("MONTH");
        }else if("D".equalsIgnoreCase(dto.getExpireUnit())){
            itemDataGlobal.setLotUnit("DAY");
        }
        //是否测量
        if("Y".equalsIgnoreCase(dto.getIsMeasure())) {
            itemDataGlobal.setMeasured(true);
        }else {
            itemDataGlobal.setMeasured(false);
        }
        //是否自带包装
        if("Y".equalsIgnoreCase(dto.getIsOriginPacking())){
            itemDataGlobal.setPreferOwnBox(true);
        }else{
            itemDataGlobal.setPreferOwnBox(false);
        }
        //是否提供袋子
        if("Y".equalsIgnoreCase(dto.getIsPlasticFirst())){
            itemDataGlobal.setPreferBag(true);
        }else {
            itemDataGlobal.setPreferBag(false);

        }
        //是否使用气垫膜
        if("Y".equalsIgnoreCase(dto.getIsNeedCushion())){
            itemDataGlobal.setUseBubbleFilm(true);
        }else {
            itemDataGlobal.setUseBubbleFilm(false);
        }
        itemDataGlobal.setMultiplePartAmount(0);
        itemDataGlobal.setMultiplePart(false);
        //安全库存
        itemDataGlobal.setSafetyStock(10);
        //序列号规则
        itemDataGlobal.setSerialRecordType(dto.getTrackSerialRegular());
        //是否记录序列号
        if(dto.getTrackSerialNum() == 0){
            itemDataGlobal.setSerialRecordType("NO_RECORD");
        }else if(dto.getTrackSerialNum() == 1){
            itemDataGlobal.setSerialRecordType("ALWAYS_RECORD");
        }
        itemDataGlobal.setVolume(dto.getUnitVolume());
        itemDataGlobal.setWeight(dto.getUnitWeight());

        return itemDataGlobal;
    }

    public ItemData createItemdata(ItemDataGlobal itemDataGlobal, Warehouse warehouse, Client client) {
        ItemData itemData = new ItemData();
        log.info("创建ItemData === > " + itemDataGlobal.getItemNo());
        itemData.setItemNo(itemDataGlobal.getItemNo());//商品itemNo
        itemData.setSkuNo(itemDataGlobal.getSkuNo());//商品skuNo
        itemData.setName(itemDataGlobal.getName());//商品名称
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

        itemData.setItemDataGlobalId(itemDataGlobal.getId());

        itemData.setClientId(client.getId());
        itemData.setWarehouseId(warehouse.getId());

        manager.persist(itemData);

        return itemData;
    }

    public ItemData updateItemData(ItemDataGlobal itemDataGlobal, ItemData itemData,Warehouse warehouse, Client client) {

        log.info("更新ItemData === > " + itemDataGlobal.getItemNo());
        itemData.setName(itemDataGlobal.getName());//商品名称
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

        itemData.setClientId(client.getId());
        itemData.setWarehouseId(warehouse.getId());

        return itemData;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ItemData synchronous(AnntoItemDTO dto) {

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(dto.getWarehouseCode());
        if(warehouse == null){
            warehouse = warehouseBusiness.saveWarehouse(dto.getWarehouseCode());
        }
        Client client = clientRepository.findByClientNo(dto.getCompanyCode());
        if(client == null){
            client = clientBusiness.saveClient(dto.getCompanyCode());
        }

        //查询商品在安得库中是否存在，存在则更新，不存在则添加
        AnntoItem item = anntoRepository.getByCode(dto.getCode());
        if(item != null){
            this.updateItem(dto,item);
        }else {
            this.createItem(dto);
        }

        //将信息同步到wms系统SKU_global表中
        ItemDataGlobal itemDataGlobal = itemDataGlobalRepository.getByItemDataNo(dto.getCode());
        if(itemDataGlobal == null){
            itemDataGlobal = this.createItemDataGlobal(dto);
        }else {
            itemDataGlobal = this.updateItemDataGlobal(dto,itemDataGlobal);
        }

        //将信息同步到wms系统itemData表中
        ItemData itemData = itemDataRepository.getOneByItemNoAndClientId(dto.getCode(),client.getId());
        if(itemData == null){
            itemData = this.createItemdata(itemDataGlobal,warehouse,client);
        }else {
            itemData = this.updateItemData(itemDataGlobal,itemData,warehouse,client);
        }

        return itemData;
    }
}
