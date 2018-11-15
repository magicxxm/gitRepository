package wms.service.impl;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.AnntoBusiness;
import wms.business.PackingRequestBusiness;
import wms.business.ShipmentRecordBusiness;
import wms.business.UnitLoadShipmentBusiness;
import wms.business.dto.PackInfo;
import wms.common.context.ApplicationContext;
import wms.common.crud.AccessDTO;
import wms.common.crud.ResponseDTO;
import wms.common.exception.ApiException;
import wms.common.utils.RandomUtil;
import wms.constants.ShipmentState;
import wms.constants.State;
import wms.crud.dto.PackConfirmDTO;
import wms.crud.dto.PackDTO;
import wms.crud.dto.StorageDTO;
import wms.domain.ItemData;
import wms.domain.common.*;
import wms.domain.enums.PackRequestState;
import wms.exception.OutBoundException;
import wms.repository.CustomerShipmentPositionRepository;
import wms.repository.CustomerShipmentRepository;
import wms.repository.common.*;
import wms.service.EntityGenerator;
import wms.service.Pack;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 123 on 2017/9/3.
 */
@Service
@Transactional
public class PackImpl implements Pack {

    private Logger log = Logger.getLogger(PackImpl.class);

    private final AnntoBusiness anntoBusiness;
    private final UnitLoadRepository unitLoadRepository;
    private final WarehouseRepository warehouseRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final PackingRequestRepository packingRequestRepository;
    private final PackingStationRepository packingStationRepository;
    private final ClientRepository clientRepository;
    private final CustomerShipmentRepository shipmentRepository;
    private final UnitLoadShipmentRepository unitLoadShipmentRepository;
    private final PackingRequestBusiness packingRequestBusiness;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final PackingRequestPositionRepository packingRequestPositionRepository;
    private final StockUnitRepository stockUnitRepository;
    private final StockUnitRecordRepository stockUnitRecordRepository;
    private final BoxTypeRepository boxTypeRepository;
    private final ShipmentRecordBusiness shipmentRecordBusiness;
    private final UnitLoadShipmentBusiness unitLoadShipmentBusiness;
    private final CustomerShipmentPositionRepository customerShipmentPositionRepository;
    private final RebinUnitLoadRepository rebinUnitLoadRepository;
    private final DigitallabelShipmentRepository digitallabelShipmentRepository;
    private final EntityManager entityManager;
    private final EntityGenerator entityGenerator;
    private final PickPackCellRepository pickPackCellRepository;
    private final PickUnitLoadRepository pickUnitLoadRepository;
    private final PickPackWallRepository pickPackWallRepository;
    private final PickingOrderPositionRepository pickingOrderPositionRepository;
    private final LotRepository lotRepository;

    public PackImpl(AnntoBusiness anntoBusiness,
                    ApplicationContext applicationContext,
                    PackingStationRepository packingStationRepository,
                    WarehouseRepository warehouseRepository,
                    PackingRequestBusiness packingRequestBusiness,
                    StorageLocationRepository storageLocationRepository,
                    PackingRequestRepository packingRequestRepository,
                    CustomerShipmentRepository shipmentRepository,
                    RebinUnitLoadRepository rebinUnitLoadRepository,
                    ClientRepository clientRepository,
                    UserRepository userRepository,
                    BoxTypeRepository boxTypeRepository,
                    StockUnitRecordRepository stockUnitRecordRepository,
                    CustomerShipmentPositionRepository customerShipmentPositionRepository,
                    StockUnitRepository stockUnitRepository,
                    ShipmentRecordBusiness shipmentRecordBusiness,
                    UnitLoadShipmentBusiness unitLoadShipmentBusiness,
                    PackingRequestPositionRepository packingRequestPositionRepository,
                    UnitLoadShipmentRepository unitLoadShipmentRepository,
                    PickPackCellRepository pickPackCellRepository,
                    DigitallabelShipmentRepository digitallabelShipmentRepository,
                    UnitLoadRepository unitLoadRepository,
                    EntityManager entityManager,
                    EntityGenerator entityGenerator,
                    PickUnitLoadRepository pickUnitLoadRepository,
                    PickPackWallRepository pickPackWallRepository,
                    PickingOrderPositionRepository pickingOrderPositionRepository,
                    LotRepository lotRepository){
        this.anntoBusiness = anntoBusiness;
        this.applicationContext = applicationContext;
        this.unitLoadRepository = unitLoadRepository;
        this.packingRequestBusiness = packingRequestBusiness;
        this.unitLoadShipmentRepository = unitLoadShipmentRepository;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.rebinUnitLoadRepository = rebinUnitLoadRepository;
        this.boxTypeRepository = boxTypeRepository;
        this.stockUnitRecordRepository = stockUnitRecordRepository;
        this.packingRequestPositionRepository = packingRequestPositionRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.packingRequestRepository = packingRequestRepository;
        this.packingStationRepository= packingStationRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.customerShipmentPositionRepository = customerShipmentPositionRepository;
        this.shipmentRecordBusiness = shipmentRecordBusiness;
        this.unitLoadShipmentBusiness = unitLoadShipmentBusiness;
        this.shipmentRepository= shipmentRepository;
        this.pickPackCellRepository = pickPackCellRepository;
        this.userRepository = userRepository;
        this.digitallabelShipmentRepository = digitallabelShipmentRepository;
        this.entityManager = entityManager;
        this.entityGenerator = entityGenerator;
        this.pickUnitLoadRepository = pickUnitLoadRepository;
        this.pickPackWallRepository = pickPackWallRepository;
        this.pickingOrderPositionRepository = pickingOrderPositionRepository;
        this.lotRepository = lotRepository;
    }

    @Override
    public void triggerInfo(String id) {

        PackInfo packInfo = new PackInfo();

        //根据灯的ID获取shipment
        DigitallabelShipment digitallabelShipment = digitallabelShipmentRepository.getByDigitalId(id);
        Warehouse warehouse = warehouseRepository.findOne(digitallabelShipment.getShipment().getWarehouseId());
        Client client = clientRepository.findOne(digitallabelShipment.getShipment().getClientId());
        packInfo.setCode(digitallabelShipment.getShipment().getCustomerNo());
        packInfo.setStationCode(digitallabelShipment.getPackingStation().getName());
        packInfo.setWarehouseCode(warehouse.getWarehouseNo());
        packInfo.setCompanyCode(client.getClientNo());

        packInfo.setShipmentCode(digitallabelShipment.getShipment().getShipmentNo());
        //承运单号
        packInfo.setPrimaryWaybillCode(digitallabelShipment.getShipment().getCarrierNo());

        //获取cellName
        PickPackCell pickPackCell = pickPackCellRepository.getcellName(id);
        packInfo.setPickPackCellCode(pickPackCell.getName());

        //触发包装接口，mushiny系统生成packrequest
       /* createPackrequest(packInfo.getWarehouseCode(),packInfo.getStationCode(),packInfo.getPickPackCellCode(),packInfo.getCode());*/

        //修改订单状态
        CustomerShipment shipment = shipmentRepository.getByShipmentNo(packInfo.getShipmentCode());
        shipment.setState(State.PACKING);

       /* packInfo.setCode("AD17062310248024");
        packInfo.setPickPackCellCode("PPW_SHOW_C03");
        packInfo.setCompanyCode("MD");
        packInfo.setWarehouseCode("W200286");
        packInfo.setStationCode("SPPV002");
        packInfo.setPrimaryWaybillCode("0956297324");
        packInfo.setShipmentCode("702908168210257");
*/
        //调annto接口
        anntoBusiness.triggerInfo(packInfo);
    }

    @Override
    public AccessDTO getInfo(PackDTO packDTO) {
        AccessDTO accessDTO = new AccessDTO();

        //触发包装接口，mushiny系统生成packrequest
        createPackrequest(packDTO.getWarehouseCode(),packDTO.getStationCode(),packDTO.getPickPackCellCode(),null);

        //通过工作站和cell格子查询对应的订单信息
        String warehouseNo = packDTO.getWarehouseCode();
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(warehouseNo);

        String cellName = packDTO.getPickPackCellCode();
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouse.getId(),cellName);
        UnitLoad unitLoad = unitLoadRepository.findUnitLoadByStorageLocationName(storageLocation,warehouse.getId());

        String stationNo = packDTO.getStationCode();
        PackingStation station = packingStationRepository.getByName(stationNo,warehouse.getId());

        PackingRequest request = packingRequestRepository.getByToUnitAndStation(unitLoad,station);
        List<PackInfo> packInfos = new ArrayList<>();
        PackInfo packInfo = new PackInfo();
        packInfo.setCode(request.getCustomerShipment().getCustomerNo());
        packInfo.setCompanyCode(clientRepository.findOne(request.getClientId()).getClientNo());
        packInfo.setPickPackCellCode(request.getFromUnitLoad().getStorageLocation().getName());
        packInfo.setStationCode(request.getPackingStation().getName());
        packInfo.setWarehouseCode(warehouseRepository.findOne(request.getWarehouseId()).getWarehouseNo());
        packInfos.add(packInfo);

        /*PackInfo packInfo = new PackInfo();
        packInfo.setCode("AD17062310248024");
        packInfo.setPickPackCellCode("PPW_SHOW_C03");
        packInfo.setCompanyCode("MD");
        packInfo.setWarehouseCode("W200286");
        packInfo.setStationCode("SPPV002");
        packInfo.setPrimaryWaybillCode("0956297324");
        packInfo.setShipmentCode("702908168210257");
        JSONObject jsonObject = JSONObject.fromObject(packInfo);

        accessDTO.setData(jsonObject.toString());
        accessDTO.setMsg("success");
        accessDTO.setCode("0");
*/
        //改变订单状态
        CustomerShipment shipment = request.getCustomerShipment();
        shipment.setState(State.PACKING);


        return accessDTO;
    }

    @Override
    public AccessDTO confirm(PackConfirmDTO packConfirmDTO) {
        AccessDTO accessDTO = new AccessDTO();
        //安得复核包装完成，，mushiny释放cell格
        String shipmentNo = packConfirmDTO.getShipmentCode();
        String carrierNo = packConfirmDTO.getPrimaryWaybillCode();
//        String boxName = packConfirmDTO.getContainer_type();
        String stationName = packConfirmDTO.getStationCode();
        String clientNo = packConfirmDTO.getCompanyCode();
        String warehouseNo= packConfirmDTO.getWarehouseCode();
        String user = packConfirmDTO.getUser();

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(warehouseNo);
        CustomerShipment shipment = shipmentRepository.getByShipmentNo(shipmentNo);

        PackingRequest packingRequest = packingRequestRepository.getByCustomerShipment(shipment);
        UnitLoad fromUnitLoad = packingRequest.getFromUnitLoad();
        StorageLocation storageLocation = fromUnitLoad.getStorageLocation();
        String cellName = storageLocation.getName();
        String boxName=packingRequest.getBoxType().getName();
        //完成包装
        User u = userRepository.findByUsername(user);
        finishedPack(shipmentNo, boxName, cellName, u.getId());

        RebinUnitLoad rebinUnitLoad = rebinUnitLoadRepository.getByShipmentNo(shipmentNo);
        if (rebinUnitLoad != null && rebinUnitLoad.getState() < State.FINISHED) {
            rebinUnitLoad.setState(State.FINISHED);
        }

        //释放cell格，可以继续放订单
        UnitLoad unitLoad = unitLoadRepository.findUnitLoadByStorageLocationName(storageLocation, warehouse.getId());
        if (unitLoad != null) {
            unitLoad.setEntityLock(2);
        }

        return accessDTO;
    }

    @Override
    public AccessDTO loginStation(PackDTO packDTO) {
        AccessDTO responseDTO = new AccessDTO();
        Map<String,String> data = new HashMap<>();
        String packStationName = packDTO.getStationCode();
        log.info("包装逻辑工作站："+packStationName);
        String warehose = packDTO.getWarehouseCode();
        log.info("当前仓库："+ warehose);
        String operator = packDTO.getUser();
        User user = userRepository.findOne(operator);
        log.info("当前操作人："+ user.getUsername());

        PackingStation ps = packingStationRepository.getByName(packStationName,warehose);
        if (ps == null) {
            responseDTO.setCode("1");
            responseDTO.setMsg("条码" + packStationName + "不是一个有效的工作站，请重新扫描");
            return responseDTO;
        }
        //逻辑工作站是否已分配
        if (ps.getOperator() != null) {
            if (!ps.getOperator().getId().equals(operator)) {
                responseDTO.setCode("1");
                responseDTO.setMsg("逻辑工作站已经分配给员工" + ps.getOperator().getUsername()+ ",请重新扫描工作站");
                return  responseDTO;
            }
        }
        //检查物理工作站是否被占用
        WorkStation workStation = ps.getWorkStation();
        log.info("包装物理工作站："+workStation.getName());
        if (workStation.getOperatorId() != null && !"".equals(workStation.getOperatorId())) {
            if (!workStation.getOperatorId().equals(operator)) {
                responseDTO.setCode("1");
                responseDTO.setMsg("物理工作站"+workStation.getName()+"已经分配给员工" + ps.getOperator().getUsername()+ ",请重新扫描工作站");
                return  responseDTO;
            }
            if (workStation.getStationName() != null
                    && !"".equals(workStation.getStationName())
                    && !workStation.getStationName().equals(ps.getName())) {
                responseDTO.setCode("1");
                responseDTO.setMsg("物理工作站"+workStation.getName()+"已分配给站台" + workStation.getStationName() + "，请重新扫描");
                return  responseDTO;
            }
        }

        //绑定关系
        ps.setOperator(user);
        workStation.setStationName(packStationName);
        workStation.setOperatorId(user.getId());

        //返回结果
        data.put("stationId",ps.getId());
        data.put("workStationId",workStation.getId());
        data.put("pickPackWallId",workStation.getPickPackWallId());
        /*data.put("stationId","e3aaa212-3d9b-4308-aa37-f405844fbfd9");
        data.put("workStationId","e8ed6e2a-8769-4c2b-b9aa-b9a87deb0d7f");
        data.put("pickPackWallId","6491b4c7-1206-4738-9a19-479e901361ff");*/
        JSONObject jsonObject = JSONObject.fromObject(data);
        responseDTO.setData(jsonObject.toString());
        return responseDTO;
    }

    @Override
    public AccessDTO loginOut(String stationCode, String warehoseCode) {
        AccessDTO responseDTO = new AccessDTO();
        log.info("退出包装逻辑工作站："+stationCode);
        log.info("当前仓库："+ warehoseCode);

        PackingStation packingStation = packingStationRepository.getByName(stationCode, warehoseCode);
        packingStation.setOperator(null);

        packingStation.getWorkStation().setOperatorId(null);
        packingStation.getWorkStation().setStationName(null);
        return responseDTO;
    }

    @Override
    public AccessDTO checkStorage(StorageDTO storageDTO) {
        AccessDTO responseDTO = new AccessDTO();
        String warehose = storageDTO.getWarehouseCode();
        log.info("当前仓库："+warehose);
        String clientId = storageDTO.getCompanyCode();
        log.info("当前客户："+clientId);
        String storageName = storageDTO.getStorageLocation();
        log.info("扫描的货框："+storageName);
        String shipmentCode = storageDTO.getShipmentCode();
        log.info("订单编码："+shipmentCode);
        String pickPackCellCode = storageDTO.getPickPackCellCode();
        log.info("对应的cell格子："+ pickPackCellCode);
        String problemType = storageDTO.getProblemType();
        log.info("问题类型是："+problemType);
        String carryNo = storageDTO.getPrimaryWaybillCode();
        log.info("订单的承运单号："+carryNo);
        String itemNo = storageDTO.getCode();
        log.info("丢失的商品的编号："+itemNo);
        int lossAmount = storageDTO.getLossAmount();
        log.info("丢失的商品的编号："+lossAmount);

        CustomerShipment c = shipmentRepository.getByShipmentNo(shipmentCode);
        c.setState(State.PROBLEM);
        //删除unitloadshipment，重新创建
        deleteUnitloadShipment(c.getId());
        //货框对应的unitload
        StorageLocation s = storageLocationRepository.getByName(warehose,storageName);
        if(s == null){
            responseDTO.setCode("1");
            responseDTO.setMsg(storageName+"不是一个有效周转箱");
            return responseDTO;
        }
        //查询周转箱对应的unitload
        UnitLoad toUnitLoad = unitLoadRepository.findUnitLoadByStorageLocationName(s, warehose);
        if (toUnitLoad == null) {
            log.info("********创建周转箱对应的unitload********");
            cerateUnitLoad(s,null);
        }
        if(toUnitLoad.getEntityLock() == 1){
            responseDTO.setCode("1");
            responseDTO.setMsg(storageName+"货筐已被锁定，请重新扫描货筐");
            return responseDTO;
        }
        //创建对应的untiloadshipment记录
        unitLoadShipmentBusiness.create(toUnitLoad.getId(),c.getId());

        //根据问题处理类型判断是否需要释放pick pack cell
        StorageLocation cellStorage = storageLocationRepository.getByName(warehose,pickPackCellCode);
        UnitLoad cellUnitload = unitLoadRepository.findUnitLoadByStorageLocationName(cellStorage, warehose);
        if(cellUnitload != null){
            List<StockUnit> stockUnitList = stockUnitRepository.getByUnitLoad(cellUnitload);
            if ("DAMAGED".equals(problemType) || "UNABLE_SCAN_SKU".equals(problemType)) {
                cellUnitload.setEntityLock(2);
                for (StockUnit su : stockUnitList) {
                    //修改库存
                    su.setUnitLoad(toUnitLoad);
                }
            }
            if ("LOSE".equals(problemType)) {
                cellUnitload.setEntityLock(1);
                for(int i = 0; i<stockUnitList.size(); i++){
                    StockUnit sk = stockUnitList.get(i);
                    if(sk.getItemData().getItemNo().equals(itemNo)){ //报丢失的商品(给丢失的商品创建一个库存和丢失的/没有丢失商品的库存记录)
                        log.info("丢失的商品创建库存记录：商品是-》"+sk.getItemData().getItemNo()+":"+sk.getItemData().getName()+",丢失的数量："+lossAmount);
                        log.info("库存原始的数量"+sk.getAmount());
                        StockUnit newstockUnit = createStockUnit(warehose,clientId,sk.getUnitLoad(),sk.getItemData(),new BigDecimal(lossAmount));

                        //修改库存
                        sk.setAmount(sk.getAmount().subtract(new BigDecimal(lossAmount)));
                        sk.setUnitLoad(toUnitLoad);
                        log.info("库存修改后的数量"+sk.getAmount());
                    }else{
                        //生成历史记录
                        sk.setUnitLoad(toUnitLoad);
                    }
                }
            }
            if ("MORE".equals(problemType)) {
                cellUnitload.setEntityLock(1);
            }
        }

        return responseDTO;
    }

    @Override
    public AccessDTO getlightOff(StorageDTO storageDTO) {
        AccessDTO responseDTO = new AccessDTO();
        Map<String,String> data = new HashMap<>();
        String warehose = storageDTO.getWarehouseCode();
        log.info("当前仓库："+warehose);
        String clientId = storageDTO.getCompanyCode();
        log.info("当前客户："+clientId);
        String digitalLabel = storageDTO.getDigitalId();
        log.info("拍的灯是："+ digitalLabel);
        String packStationName = storageDTO.getStationCode();
        log.info("包装逻辑工作站："+packStationName);
        String userName = storageDTO.getUser();
        log.info("当前操作用户："+userName);

        PickPackCell cell = pickPackCellRepository.getcellName(digitalLabel);
        if (cell == null) {
            responseDTO.setCode("1");
            responseDTO.setMsg("灯对应的cell格子不存在");
            return responseDTO;
        }
        //PackingStation packingStation = packingStationRepository.getByName(packStationName, warehose);
        StorageLocation s = storageLocationRepository.getByName(warehose, cell.getName());
        UnitLoad unitLoad = unitLoadRepository.findUnitLoadByStorageLocationName(s, warehose);

        PickingUnitLoad pickingUnitLoad = pickUnitLoadRepository.getPickingUnitLoadsByUnitLoad(unitLoad);
        CustomerShipment customerShipment = shipmentRepository.getByShipmentNo(pickingUnitLoad.getCustomerShipmentNo());
        //改变digitalShipment的状态
        User user = userRepository.findByUsername(userName);
        digitallabelShipmentRepository.deleteDigitalShipmentByshipment(customerShipment,user);
        data.put("shipmentCode",customerShipment.getShipmentNo());
        data.put("primaryWaybillCode",customerShipment.getCarrierNo());
//        data.put("shipmentCode","596910671184567");
//        data.put("primaryWaybillCode","0594395034");
        JSONObject jsonObject = JSONObject.fromObject(data);
        responseDTO.setData(jsonObject.toString());
        return responseDTO;
    }

    @Override
    public AccessDTO getShipmentByDigital(StorageDTO storageDTO) {
        AccessDTO responseDTO = new AccessDTO();
        Map<String,String> data = new HashMap<>();
        String warehose = storageDTO.getWarehouseCode();
        log.info("当前仓库："+warehose);
        String packStationName = storageDTO.getStationCode();
        log.info("包装逻辑工作站："+packStationName);
        String userName = storageDTO.getUser();
        log.info("当前操作用户："+userName);

        String pickPackWallId = storageDTO.getPickPackWallId();
        PickPackWall ppw = pickPackWallRepository.findOne(pickPackWallId);
        log.info("pickpackwallName是："+ppw.getName());

        DigitallabelShipment  d = digitallabelShipmentRepository.getDigitallabelByWall(pickPackWallId);
        if(d == null){
            responseDTO.setCode("1");
            responseDTO.setMsg("此时没有灯亮");
            return responseDTO;
        }
        if(!packStationName.equalsIgnoreCase(d.getPackingStation().getName())){
            responseDTO.setCode("1");
            responseDTO.setMsg("本工作站此时没有灯亮");
            return responseDTO;
        }

        CustomerShipment customerShipment = shipmentRepository.getByShipmentNo(d.getShipment().getShipmentNo());
        //改变digitalShipment的状态
        User user = userRepository.findByUsername(userName);
        //灯不亮时，则无法拍灯，需亮灯时state置为2，相当于亮灯时进行拍灯操作
        digitallabelShipmentRepository.deleteDigitalShipmentByshipment(customerShipment,user);

        data.put("shipmentCode",d.getShipment().getShipmentNo());
        data.put("digitalId",d.getDigitalLabel2());
//        data.put("shipmentCode","702908168210257");
//        data.put("digitalId","9af9b04e-7d3c-41b8-947a-713ba0cd2989");
        JSONObject jsonObject = JSONObject.fromObject(data);
        responseDTO.setData(jsonObject.toString());
        return responseDTO;
    }

    public StockUnit createStockUnit(String warehouseId, String clientId, UnitLoad unitLoad, ItemData itemData, BigDecimal amount) {
        unitLoad = entityManager.merge(unitLoad);
        itemData = entityManager.merge(itemData);

        StockUnit stockUnit = new StockUnit();
        stockUnit.setWarehouseId(warehouseId);
        stockUnit.setClientId(clientId);
        stockUnit.setUnitLoad(unitLoad);
        stockUnit.setItemData(itemData);
        stockUnit.setAmount(amount);
        stockUnit.setState("Inventory");

        entityManager.persist(stockUnit);
        entityManager.flush();

        return stockUnit;
    }

    public UnitLoad cerateUnitLoad(StorageLocation sl, String stationName) {
        UnitLoad u = entityGenerator.generateEntity(UnitLoad.class);
        while (true){
            String label = RandomUtil.getUnitLoadLabel();
            UnitLoad u1 = unitLoadRepository.getByLabel(label);
            if(u1 == null){
                u.setLabel(label);
                break;
            }
        }
        u.setStorageLocation(sl);
        u.setClientId(applicationContext.getCurrentClient());
        u.setWarehouseId(applicationContext.getCurrentWarehouse());
        u.setStationName(stationName);
        entityManager.persist(u);
        entityManager.flush();
        return u;
    }

    private void deleteUnitloadShipment(String shipmentId) {
        String sql = " delete from INV_UNITLOAD_SHIPMENT where  SHIPMENT_ID=:shipmentId";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("shipmentId", shipmentId);
        query.executeUpdate();
    }

    private void finishedPack(String shipmentNo, String boxName, String cellName, String userId) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        CustomerShipment shipment = shipmentRepository.getByShipmentNo(shipmentNo);
        BoxType boxType = boxTypeRepository.getByName(boxName, shipment.getWarehouseId(), shipment.getClientId());

        //检查是否完成包装
        PackingRequest packingRequest = packingRequestRepository.getByCustomerShipment(shipment);
        if (packingRequest == null) {
            throw new ApiException(OutBoundException.EX_SHIPMENT_NOT_IS_PACKED.toString(), shipmentNo + "is not packed");
        }
        for (CustomerShipmentPosition position : shipment.getPositions()) {
            //检查已经包装的数量和订单的数量是否相同
            BigDecimal packAmount = packingRequestPositionRepository.sumByShipmentPosition(position);
            if (packAmount.compareTo(position.getAmount()) != 0) {
                throw new ApiException(OutBoundException.EX_SHIPMENT_NOT_IS_PACKED.toString(), shipmentNo + "is not packed");
            }
        }
        //完成包装
        UnitLoad fromUnitLoad = packingRequest.getFromUnitLoad();
        UnitLoad toUnitLoad = packingRequest.getToUnitLoad();
        // 修改fromUnitLoad下库存的商品数量，增加tounitLoad下库存数量
        List<StockUnit> stockUnitList = fromUnitLoad.getStockUnits();
        List<StockUnitRecord> stockUnitRecords = new ArrayList<>();

        String recordType = "PACK DIRECT";
       /* String recordType = "";
        if("NO".equals(type)){
            recordType = "PACK DIRECT";
            createJobrecord(shipment,packingRequest,cellName,"PACKED",recordType,"noScan");
        }else if("YES".equals(type)){
            recordType = "PACK VERIFY";
        }else if("REBIN".equals(type)){
            recordType = "REBIN-PACK";
        }*/
        for (StockUnit s : stockUnitList) {
            //生成包装历史记录
            StockUnitRecord stockUnitRecord = new StockUnitRecord();
            stockUnitRecord.setFromStorageLocation(cellName);
            stockUnitRecord.setFromState(s.getState());
            stockUnitRecord.setFromStockUnit(s.getId());
            if (s.getItemData() != null) {
                stockUnitRecord.setItemDataItemNo(s.getItemData().getItemNo());
                stockUnitRecord.setItemDataSKU(s.getItemData().getSkuNo());
            }
            stockUnitRecord.setFromUnitLoad(fromUnitLoad.getLabel());
            if (s.getLotId() != null) {
                Lot lot = lotRepository.findOne(s.getLotId());
                stockUnitRecord.setLot(lot.getName());
            }
            stockUnitRecord.setToState(s.getState());
            stockUnitRecord.setToStockUnit(s.getId());
            stockUnitRecord.setToStorageLoaction("Packed");
            stockUnitRecord.setToUnitLoad(toUnitLoad.getLabel());
            stockUnitRecord.setAmount(s.getAmount());
            stockUnitRecord.setOperator(userRepository.findOne(applicationContext.getCurrentUser()).getUsername());
            stockUnitRecord.setRecordType(recordType);
            stockUnitRecord.setRecordCode("M");
            stockUnitRecord.setRecordTool("Pack");
            stockUnitRecord.setWarehouseId(s.getWarehouseId());
            stockUnitRecord.setClientId(s.getClientId());
            stockUnitRecords.add(stockUnitRecord);

            //获取已包装商品数量
            CustomerShipmentPosition c = customerShipmentPositionRepository.getByItemDataAndShipment(s.getItemData(), shipment);
            BigDecimal packedAmount = c.getAmount();
            //修改库存
            s.setUnitLoad(toUnitLoad);
        }
        stockUnitRecordRepository.save(stockUnitRecords);
        stockUnitRepository.save(stockUnitList);

        //修改订单
        shipment.setState(State.PACKED);
        //新增shipmentRecord
        shipmentRecordBusiness.getShipmentRecord(shipment, packingRequest.getPackingStation().getName(), userId, ShipmentState.PACKED);

        //在unitLoadShipment存入信息
        UnitLoadShipment us = unitLoadShipmentRepository.getByCustometShipmentId(shipment.getId());
        if(us != null){
            unitLoadShipmentRepository.delete(us);//删除原有记录
        }
        unitLoadShipmentBusiness.create(toUnitLoad.getId(),shipment.getId());

        packingRequest.setState(PackRequestState.PACKED.toString());
        packingRequest.setBoxType(boxType);

    }

    private void createPackrequest(String warehouseName,String stationName,String cellName,String shipmentNo){
        Warehouse warehouse = warehouseRepository.getByWarehouseNo(warehouseName);
        PackingStation packingStation = packingStationRepository.getByName(stationName,warehouse.getId());
        StorageLocation s = storageLocationRepository.getByName(warehouse.getId(), cellName);

        UnitLoad unitLoad  = unitLoadRepository.findUnitLoadByStorageLocationName(s, warehouse.getId());

        if (unitLoad == null) {
            throw new ApiException(OutBoundException.EX_PICKPACKCELL_IS_NOT_EXIST.toString(), cellName + "订单没有完成");
        }

        //如果是通过cellname查询，则要根据cellName获取shipment
        CustomerShipment shipment = null;
        if(shipmentNo == null){
            String shipmentId = unitLoadShipmentRepository.getByUnitLoadId(unitLoad.getId()).getCustometShipmentId();
            if ("".equals(shipmentId) || shipmentId == null) {
                throw new ApiException(OutBoundException.EX_SHIPMENT_ISNOT_FINISHED.toString(), cellName + "订单没有完成");
            }
            //获取shipment
            shipment = shipmentRepository.findOne(shipmentId);
        }else{
            shipment = shipmentRepository.getByShipmentNo(shipmentNo);
        }

        List<CustomerShipmentPosition> shipmentPositions = shipment.getPositions();

        //生成packrequest
        PackingRequest packingRequest = packingRequestRepository.getByCustomerShipment(shipment);
        if(packingRequest == null){
            //存储需包装的记录
            packingRequest = packingRequestBusiness.buildPackingRequest(shipmentPositions.get(0), unitLoad, packingStation);
            //存储包装记录明细
            for (CustomerShipmentPosition positions : shipmentPositions) {
                packingRequestBusiness.createPackingRequestPosition(packingRequest, positions);
            }
        }else {
            //删除原有的包装记录明细
            packingRequestBusiness.cleanPackingRequestPosition(packingRequest);
            //删除原有的包装记录
            packingRequestBusiness.cleanPackingRequest(shipment);
            //存储需包装的记录
            packingRequest = packingRequestBusiness.buildPackingRequest(shipmentPositions.get(0), unitLoad, packingStation);
            //存储包装记录明细
            for (CustomerShipmentPosition positions : shipmentPositions) {
                packingRequestBusiness.createPackingRequestPosition(packingRequest, positions);
            }
        }
    }
}
