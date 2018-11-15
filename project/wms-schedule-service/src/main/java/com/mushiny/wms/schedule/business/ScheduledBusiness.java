package com.mushiny.wms.schedule.business;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.schedule.common.Parameter;
import com.mushiny.wms.schedule.common.StockUnitState;
import com.mushiny.wms.schedule.crud.dto.FinishStowDTO;
import com.mushiny.wms.schedule.domin.*;
import com.mushiny.wms.schedule.repository.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ScheduledBusiness {

    private static final Logger log = LoggerFactory.getLogger(ScheduledBusiness.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final ItemDataRepository itemDataRepository;
    private final PodRepository podRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final CheckBusiness checkBusiness;
    private final UnitLoadRepository unitLoadRepository;
    private final StockUnitRepository stockUnitRepository;
    private final ItemDataSerialNumberRepository itemDataSerialNumberRepository;
    private final Parameter parameter;
    private final ScanningBusiness scanningBusiness;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final WorkStationRepository workStationRepository;
    private final StowStationRepository stowStationRepository;
    private final OrderStrategyRepository orderStrategyRepository;
    private final CustomerOrderRepository customerOrderRepository;
    private final DeliveryPointRepository deliveryPointRepository;
    private final CustomerShipmentRepository customerShipmentRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final DigitallabelShipmentRepository digitallabelShipmentRepository;
    private final PickPackCellRepository pickPackCellRepository;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;

    @Autowired
    public ScheduledBusiness(ItemDataRepository itemDataRepository,
                             PodRepository podRepository,
                             StorageLocationRepository storageLocationRepository,
                             CheckBusiness checkBusiness,
                             UnitLoadRepository unitLoadRepository,
                             StockUnitRepository stockUnitRepository,
                             ItemDataSerialNumberRepository itemDataSerialNumberRepository,
                             Parameter parameter,
                             ScanningBusiness scanningBusiness,
                             StockUnitRecordRepository stockUnitRecordRepository,
                             WorkStationRepository workStationRepository,
                             StowStationRepository stowStationRepository,
                             OrderStrategyRepository orderStrategyRepository,
                             CustomerOrderRepository customerOrderRepository,
                             DeliveryPointRepository deliveryPointRepository,
                             CustomerShipmentRepository customerShipmentRepository,
                             PickingOrderPositionRepository pickingOrderPositionRepository,
                             DigitallabelShipmentRepository digitallabelShipmentRepository,
                             PickPackCellRepository pickPackCellRepository,
                             CustomerShipmentPositionRepository customerShipmentPositionRepository) {
        this.itemDataRepository = itemDataRepository;
        this.podRepository = podRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.checkBusiness = checkBusiness;
        this.unitLoadRepository = unitLoadRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.itemDataSerialNumberRepository = itemDataSerialNumberRepository;
        this.parameter = parameter;
        this.scanningBusiness = scanningBusiness;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.workStationRepository = workStationRepository;
        this.stowStationRepository = stowStationRepository;
        this.orderStrategyRepository = orderStrategyRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.deliveryPointRepository = deliveryPointRepository;
        this.customerShipmentRepository = customerShipmentRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.digitallabelShipmentRepository = digitallabelShipmentRepository;
        this.pickPackCellRepository = pickPackCellRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
    }

    public void inboundStow() {
        WorkStation workStation = scanningBusiness.getStowWorkStation();
        //判断当前工作站是否有POD
        String podInfo = getCurrentPod(workStation);
        //进行上架操作
        if (podInfo != null) {
            StowStation stowStation = scanningBusiness.getStowLogicStation();
            //定义商品种类
          //  List<String> itemDataIds = checkBusiness.getItemDataId();
            List<ItemData> itemDatas = checkBusiness.getAll();
        //    List<ItemData> itemDatas = new ArrayList<>();
//            for (String id : itemDataIds) {
//                ItemData itemData = itemDataRepository.findOne(id);
//                itemDatas.add(itemData);
//            }
            log.info("商品数量........." + itemDatas.size());
            int i = 0, j = 0;
            if (itemDatas.size() > 0) {
                while (i < 20 - j) {
                    Random random = new Random();
                    ItemData itemData = itemDatas.get(random.nextInt(itemDatas.size()));
                    log.info("商品信息........" + itemData.getName());
                    boolean flag = stowScanSku(itemData, podInfo);
                    if (flag)
                        break;
                    i++;
                    j++;
                }
                //释放当前POD
                releasePod(workStation, podInfo, stowStation.getId());
                try {
                    Thread.sleep(Long.parseLong(parameter.getTime()) / 5);
                    log.info("释放POD成功-------------");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
//            try {
//                Thread.sleep(Long.parseLong(parameter.getTime())*4);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            log.info("上架工作站没有POD------------");
            //当前工作站没车，但是车还没释放回去
//            List<RcsTrip> rcsTrips = stowStationRepository.getRcsTrip(workStation.getId());
//            if (rcsTrips.size() > 0) {
//                boolean bl = false;
//                for (RcsTrip rcsTrip : rcsTrips) {
//                    if (!"Finish".equalsIgnoreCase(rcsTrip.getTripState())) {
//                        bl = true;
//                        break;
//                    }
//                }
//                if (!bl) {
//                    //解绑物理工作站和逻辑工作站
//                    scanningBusiness.loginOutStowStation();
//                    log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()) + "工作站已经解绑----------");
//                }

        }
    }

    private boolean stowScanSku(ItemData itemData, String podId) {
        List<StorageLocation> storageLocations = getIntroLocation(itemData, podId);
        boolean flag = false;
        if (storageLocations != null && !storageLocations.isEmpty()) {
            StorageLocation storageLocation = storageLocations.get(0);
            //获取登录工作站时间
            StowStation stowStation = scanningBusiness.getStowLogicStation();
//            flag = getStockUnitRecord(stowStation.getModifiedDate());
//            if (flag) {
//                return flag;
//            }
            //判断当前货位对应的unitLoad是否存在
            UnitLoad unitLoad = checkBusiness.checkUnitLoad(storageLocation);
            FinishStowDTO finishStowDto = new FinishStowDTO();
            //设置上架数量
            finishStowDto.setAmount(new BigDecimal(parameter.getEachStowAmount()));
            if (itemData.isLotMandatory()) {
                //设置商品有效期
                String[] useNotAfters = {"2018-09-10", "2018-10-10"};
                Random random = new Random();
                int index = random.nextInt(2);
                String useNotAfter = useNotAfters[index];
                finishStowDto.setUseNotAfter(useNotAfter);
            }
            if (itemData.getSerialRecordType().equalsIgnoreCase("ALWAYS_RECORD")) {
                //序列号商品只能单件上架
                finishStowDto.setAmount(BigDecimal.ONE);
                int length = itemData.getSerialRecordLength();
                String sn = RandomUtil.getRandomNumber(length);
                finishStowDto.setSn(sn);
            }
            StockUnit stockUnit = finishStow(itemData, unitLoad, finishStowDto);
            checkBusiness.buildStowStockUnitRecord(stockUnit, finishStowDto);
            //设置上架一件商品时间
            try {
                Thread.sleep(Long.parseLong(parameter.getTime()) / 2);
                log.info("成功上架" + finishStowDto.getAmount() + "件商品......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    private List<StorageLocation> getIntroLocation(ItemData itemData, String podId) {
        log.info("进入扫描商品条码");
        String podName = null;
        String podFace = null;
        try {
            podName = podId.substring(0, 8);
            podFace = podId.substring(8, 9);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException("pod信息错误");
        }
        Pod pod = podRepository.getByName(parameter.getWarehouse(), podName);
        List<StorageLocation> list = storageLocationRepository.getByPod(pod, podFace);
        if (list.isEmpty()) return null;
        String warehouseId = parameter.getWarehouse();
        List<StorageLocation> storageLocations = list.stream().filter(location -> {
            try {
                //商品属性是否不符
                checkBusiness.CheckItemWithStorageLocationIsSeem(location, itemData);
                UnitLoad unitLoad = unitLoadRepository.getByStorageLocation(warehouseId, location);
                if (unitLoad != null) {
                    //货位中商品种类是否超过系统上限
                    checkBusiness.CheckIsBeyoungMaxCatalogs(unitLoad, location, itemData);
                    //是否超重
                    checkBusiness.CheckStorageLoadWeight(itemData, unitLoad,Integer.parseInt(parameter.getEachStowAmount()));
                    List<StockUnit> stockUnitList = stockUnitRepository.getAllByUnitLoad(unitLoad);
                    if (stockUnitList != null && !stockUnitList.isEmpty()) {
                        //是否存在不同供应商
                        checkBusiness.CheckClientIsSeem(stockUnitList, itemData);
                        //是否存在相似商品
                        checkBusiness.CheckItemIsSeem(stockUnitList, itemData);
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }).collect(Collectors.toList());
        return storageLocations;
    }

    public boolean getStockUnitRecord(LocalDateTime startDate) {
        boolean flag = false;
        BigDecimal amount = stockUnitRecordRepository.getByDate(startDate);
        log.info("已经上架"+amount+"件商品-------------");
        if (amount.compareTo(new BigDecimal(parameter.getAmount())) >= 0) {
            //停止呼叫POD
            WorkStation workStation = scanningBusiness.getStowWorkStation();
            workStation.setCallPod(false);
            workStationRepository.save(workStation);
            flag = true;
        }
        return flag;
    }

    public String getCurrentPod(WorkStation workStation) {
        Date date = new Date();
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime()) + "当前物理工作站----" + workStation.getName());
        RestTemplate r = new RestTemplate();
        String refreshPod = r.getForObject(parameter.getRefreshPodUrl() + "sectionId={sectionId}&workStationId={workStationId}", String.class, workStation.getSectionId(), workStation.getId());
        JSONObject json = JSONObject.fromObject(refreshPod);
        String podId = (String) json.get("pod");
        //当前工作站有POD
        if (podId != null && !podId.isEmpty()) {
            log.info("PodId信息............" + podId);
            return podId;
        } else {
            return null;
        }
    }

    public void releasePod(WorkStation workStation, String podName, String logicStationId) {
        Date date = new Date();
        log.info(new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date.getTime()) + "释放当前POD-----");
        RestTemplate r = new RestTemplate();
        String releasePod = r.getForObject(parameter.getReleasePodUrl() + "sectionId={sectionId}&&podName={podName}&&force=false&workStationId={workStationId}&logicStationId{logicStationId}",
                String.class, workStation.getSectionId(), podName, workStation.getId(), logicStationId);
        JSONObject json = JSONObject.fromObject(releasePod);
        String podInfo = (String) json.get("pod");
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime()) +
                "工作站" + workStation.getName() + " 获取推送的POD------" + podInfo);
    }

    private StockUnit finishStow(ItemData itemData, UnitLoad unitLoad, FinishStowDTO finishStowDto) {
        Lot lot = null;
        //是否为有效期商品
        if (itemData.isLotMandatory()) {
            lot = checkBusiness.CheckLotOrSave(itemData, LocalDate.parse(finishStowDto.getUseNotAfter()));
        }
        StockUnit stockUnit = stockUnitRepository.getByItemDataAndUnitLoad(itemData, unitLoad);
        //序列号商品只能单件上架且一件商品就是一个库存
        if (stockUnit != null && !itemData.getSerialRecordType().equals("ALWAYS_RECORD")) {
            stockUnit.setAmount(finishStowDto.getAmount().add(stockUnit.getAmount()));
        } else {
            stockUnit = new StockUnit();
            stockUnit.setAmount(finishStowDto.getAmount());
            stockUnit.setLot(lot);
            stockUnit.setItemData(itemData);
            stockUnit.setWarehouseId(itemData.getWarehouseId());
            stockUnit.setClientId(parameter.getClient());
            stockUnit.setWarehouseId(parameter.getWarehouse());
            stockUnit.setUnitLoad(unitLoad);
            //上架的是正品库存
            stockUnit.setState(StockUnitState.INVENTORY.getName());
            stockUnit.setCreatedBy(parameter.getUserName());
            stockUnit.setModifiedBy(parameter.getUserName());
        }
        stockUnit = stockUnitRepository.saveAndFlush(stockUnit);
        if (itemData.getSerialRecordType().equalsIgnoreCase("ALWAYS_RECORD") && finishStowDto.getSn() != null) {
            ItemDataSerialNumber itemDataSerialNumber = itemDataSerialNumberRepository.getBySerialNo(itemData.getItemNo(), finishStowDto.getSn());
            ItemDataSerialNumber serialNumber = new ItemDataSerialNumber();
            if (itemDataSerialNumber == null) {
                serialNumber.setItemData(itemData);
                serialNumber.setSerialNo(finishStowDto.getSn());
                serialNumber.setEntityLock(0);
                serialNumber.setClientId(parameter.getClient());
                serialNumber.setWarehouseId(parameter.getWarehouse());
                serialNumber.setCreatedBy(parameter.getUserName());
                serialNumber.setModifiedBy(parameter.getUserName());
                itemDataSerialNumberRepository.save(serialNumber);
            } else {
                itemDataSerialNumber.setEntityLock(0);
                itemDataSerialNumberRepository.save(itemDataSerialNumber);
            }
            stockUnit.setSerialNo(finishStowDto.getSn());
            stockUnitRepository.saveAndFlush(stockUnit);
        }
        return stockUnit;
    }

    public void createOrder() {
        //获取所有发货点
        List<DeliveryPoint> deliveryPoints = deliveryPointRepository.getBySortCode(parameter.getWarehouse());
        int size = deliveryPoints.size();
        //获取所有订单策略
        List<OrderStrategy> strategies = orderStrategyRepository.findAll();
        Random random = new Random();
        //设置生成订单的数量
        for (int i = 0; i < 5; i++) {
            CustomerOrder order = new CustomerOrder();
            order.setId(UUID.randomUUID().toString());
            order.setCreatedDate(LocalDateTime.now());
            order.setCreatedBy(parameter.getUserName());
            order.setModifiedDate(LocalDateTime.now());
            order.setModifiedBy(parameter.getUserName());
            order.setAdditionalContent(null);
            order.setEntityLock(0);
            order.setVersion(0);
            order.setCustomerName("M0" + String.valueOf(i));
            order.setCustomerNo("M0" + String.valueOf(i));
            order.setDeliveryDate(deliveryPoints.get(random.nextInt(size)).getDeliveryTime().getDeliveryTime());
            order.setSortCode(deliveryPoints.get(random.nextInt(size)).getSortCode().getCode());
            //订单编号不唯一
            String orderNo = checkOrderNo("S" + random.nextInt(1000000));
            order.setOrderNo(orderNo);
            order.setPriority(random.nextInt(5));
            order.setState(0);
            order.setStrategy(strategies.get(0));
            order.setClientId(parameter.getClient());
            order.setWarehouseId(parameter.getWarehouse());
            checkBusiness.executeSQLToCreateOrder(order);
        }
    }

    public String checkOrderNo(String orderNo) {
        boolean flag = true;
        Random random = new Random();
        while (flag) {
            CustomerOrder customerOrder = customerOrderRepository.getByOrderNo(orderNo);
            if (customerOrder != null) {
                orderNo = "S0" + random.nextInt(10000);
            } else {
                flag = false;
            }
        }
        return orderNo;
    }

    //定时拆分订单
    public void splitCustomerOrder() {
        Date d = new Date();
        int i = 0;
        int j = 0;
        while (i < 100 - j) {
            List<CustomerOrder> customerOrders = getOrders(100, i);
            if (customerOrders.size() == 0) {
                return;
            }
            for (CustomerOrder order : customerOrders) {
                int result = checkBusiness.split(order, d);
                if (result == 1) {
                    j++;
                }
            }
            log.info("当前查询起始点 =========== >>>" + i);
            log.info("当前查询订单结果 拆分订单的数量 ------------>>>" + j);
            i = i + 10;
        }
    }

    private List<CustomerOrder> getOrders(int released, int i) {
        String sql = "SELECT O FROM CustomerOrder O where O.state < :state order by O.createdDate";
        Query query = entityManager.createQuery(sql);
        query.setParameter("state", released);
        query.setFirstResult(i);
        query.setMaxResults(10);
        List<CustomerOrder> customerOrders = query.getResultList();
        return customerOrders;
    }

    //拣货
    public void picking() {
        WorkStation workStation = scanningBusiness.getPickWorkStation();
        //判断当前工作站是否有POD
        String podInfo = getCurrentPod(workStation);
        if (podInfo != null) {
            RestTemplate r = new RestTemplate();
            JSONObject postData = new JSONObject();
            JSONObject postData2 = new JSONObject();
            //获取拣货单信息
            String s=r.getForObject(parameter.getPickOrderPosition()+"podName={podName}&sectionId={sectionId}&stationName={stationName}",String.class,podInfo,workStation.getSectionId(),workStation.getStationName());
            JSONObject j =JSONObject.fromObject(s);
            String id=(String) j.get("id");
            if(id!=null) {
                String strAmount=String.valueOf(j.get("amount")) ;
                BigDecimal amount = new BigDecimal(strAmount);
                JSONObject itemDataDTO2 =JSONObject.fromObject(j.get("itemDataDTO"));
                String itemNo=(String) itemDataDTO2.get("itemNo");
                PickingOrderPosition pickingOrderPosition = pickingOrderPositionRepository.findOne(id);
                if (pickingOrderPosition != null) {
                    //扫描商品
                    postData2.put("pickId", id);
                    postData2.put("itemNo", itemNo);
                    postData2.put("amountPicked", amount);
                    JSONObject jsonObj2 = r.postForEntity(parameter.getScanItemNo(), postData2, JSONObject.class).getBody();
                    //pickPack确认拣货
                    postData.put("pickId", pickingOrderPosition.getId());
                    postData.put("amountPicked", pickingOrderPosition.getAmount());
                    postData.put("type", "PICK TO PACK");
                    JSONObject jsonObj = r.postForEntity(parameter.getPickConfirm(), postData, JSONObject.class).getBody();
                    JSONObject json = JSONObject.fromObject(jsonObj);
                    String flag = String.valueOf(json.get("success"));
                    if (flag.equals("true")) {
                        //是否是序列号商品
                        if (pickingOrderPosition.getItemData().getSerialRecordType().equals("ALWAYS_RECORD")) {
                            StockUnit pickFromStockUnit = pickingOrderPosition.getPickFromStockUnit();
                            List<ItemDataSerialNumber> itemDataSerialNumbers = itemDataSerialNumberRepository.getByEntityLock(pickingOrderPosition.getItemData().getId());
                            pickFromStockUnit.setSerialNo(itemDataSerialNumbers.get(0).getSerialNo());
                            stockUnitRepository.save(pickFromStockUnit);
                            itemDataSerialNumbers.get(0).setEntityLock(2);
                            itemDataSerialNumberRepository.save(itemDataSerialNumbers.get(0));
                        }
                        log.info("成功拣货--------------"+ pickingOrderPosition.getAmount() + "件");
                        //释放POD
                        r.getForObject(parameter.getPickOrderPosition() + "podName={podName}&sectionId={sectionId}&stationName={stationName}", String.class, podInfo, workStation.getSectionId(), workStation.getStationName());
                    }
                }
            }
        } else {
//            try {
//                Thread.sleep(Long.parseLong(parameter.getTime()));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            log.info("拣货工作站没有POD-----");
            //当前工作站没车，但是车还没释放回去
//            List<RcsTrip> rcsTrips = stowStationRepository.getRcsTrip(workStation.getId());
//            if (rcsTrips.size() > 0) {
//                boolean bl = false;
//                for (RcsTrip rcsTrip : rcsTrips) {
//                    if (!"Finish".equalsIgnoreCase(rcsTrip.getTripState())) {
//                        bl = true;
//                        break;
//                    }
//                }
//                if (!bl) {
//                    Date date = new Date();
//                    //解绑物理工作站和逻辑工作站
//                    scanningBusiness.loginOutPickStation();
//                    log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime()) + "工作站已经解绑----------");
//                }
        }
    }

    //包装
    public void pack(WorkStation workStation){
        //获取OB_DIGITALLABE_SHIPMENT状态为0的数据
        List<DigitallabelShipment> digitallabelShipments = digitallabelShipmentRepository.getByState();
        if(workStation.getOperator()!=null) {
            if (digitallabelShipments.size()>0) {
                DigitallabelShipment d = digitallabelShipments.get(0);
                CustomerShipment customerShipment = d.getShipment();
                PickPackCell pickPackCell = pickPackCellRepository.findOne(customerShipment.getPickPackCellId());
                String storageName=pickPackCell.getName();
                RestTemplate r = new RestTemplate();
                //获取订单信息
                String str = r.getForObject(parameter.getPickPackCell() + "pickPackCellName={pickPackCell}&stationName={stationName}&scan=YES", String.class, storageName, workStation.getStationName());
                JSONObject json = JSONObject.fromObject(str);
                JSONObject shipmentDTO = JSONObject.fromObject(json.get("shipmentDTO"));
                String shipmentNo = (String) shipmentDTO.get("shipmentNo");
                JSONObject BoxTypeDTO = JSONObject.fromObject(shipmentDTO.get("boxType"));
                String boxName = (String) BoxTypeDTO.get("name");
                //称重
                r.getForObject(parameter.getShipmentWeight() + "shipmentNo={shipmentNo}&weight=10", String.class, shipmentNo);
                List<CustomerShipmentPosition> shipmentPositions = customerShipmentPositionRepository.getByShipment(customerShipment);
                for (CustomerShipmentPosition position : shipmentPositions) {
                    int amount=position.getAmount().intValue();
                    for(int i=0;i<amount;i++) {
                        //扫描商品
                        r.getForObject(parameter.getItemScanData() + "itemNo={itemNo}&storageName={storageName}&shipmentId={shipmentId}", String.class, position.getItemData().getItemNo(), storageName, customerShipment.getId());
                        //扫描序列号
                        JSONObject postData = new JSONObject();
                        postData.put("shipmentNo", customerShipment.getShipmentNo());
                        postData.put("itemDataId", position.getItemData().getId());
                        postData.put("stationName", workStation.getStationName());
                        postData.put("type", "YES");
                        postData.put("cellName", storageName);
                        //确认扫描商品
                        r.postForEntity(parameter.getItemConfirm(), postData, JSONObject.class).getBody();
                    }
                }
                //完成包装
                r.getForObject(parameter.getScanBox() + "shipmentNo={shipmentNo}&boxName={boxName}&cellName={cellName}&type=YES", String.class, shipmentNo, boxName, storageName);
                log.info("成功包装1个订单----------------------");
                try {
                    Thread.sleep(Long.parseLong(parameter.getTime()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
//            else {
//                //退出工作站
//                log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime())+"退出工作站------------------");
//                scanningBusiness.loginOutPackStation();
//            }
        }else{
//            if(digitallabelShipments.size()>0){
                //登录工作站
                log.info("已登录工作站-----------------");
                scanningBusiness.scanPackingStation();

        }
    }
}