package com.mushiny.business;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.beans.order.Order;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tank.li on 2017/6/26.
 */
@Component
@org.springframework.core.annotation.Order(value = 2)
public class WareHouseManager implements CommandLineRunner {

    private final static Logger logger = LoggerFactory.getLogger(WareHouseManager.class);

    private Map<String, WareHouse> wareHouseMap = new ConcurrentHashMap<>();
    private Map<String, Section> allSections = new ConcurrentHashMap<>();

    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private SystemPropertiesManager systemPropertiesManager;

    public Map<String, WareHouse> getWareHouseMap() {
        return wareHouseMap;
    }

    /**
     * 通过格子和工作站初始化仓库的section
     *
     * @param section
     */
    public void createWareHouse(Section section) {
        List<Address> cells = section.addressList;
        Collection<WorkStation> workStations = section.workStationMap.values();
        logger.debug("创建仓库 位置格子有:" + cells.size() + " 工作站有:" + workStations.size());
        //this.initWorkStations(workStations,section);
        for (int i = 0; i < cells.size(); i++) {
            Address address = cells.get(i);
            //section.addressList.add(address);
            //section.addressMap.put(address.getId(), address);
            computeHot(address, workStations, section);
        }
        this.initAutoMoveAddrs(workStations, section);
        //this.initChargerAddrs(section);
        //将热度排序
        Collections.sort(section.addressList, new AddressComparator());
        logger.debug("格子热度排序初始化完成!");
    }


    /**
     * 加载所有自动排队的点 这些位置点将进入该工作站队列
     *
     * @param workStations
     * @param section
     */
    private void initAutoMoveAddrs(Collection<WorkStation> workStations, Section section) {
        logger.debug("加载自动排队的扫描、停止、缓冲的位置点");
        Iterator<WorkStation> workStationIterator = workStations.iterator();
        while (workStationIterator.hasNext()) {
            WorkStation workStation = workStationIterator.next();
            /*List<Map> rows = this.jdbcRepository.queryByKey("WareHousemanager.initAutoMoveAddrs",
                    workStation.getWorkStationId(),section.getSection_id());
            //只有一条记录
            if(rows==null||rows.size()==0){
                logger.error("没有设置自动排队扫描的位置点! " +
                        "WorkStationId="+workStation.getWorkStationId()+" sectionId="+section.getSection_id());
                continue;
            }
            Map data = rows.get(0);*/
            //自动扫描
            String scanPoint = workStation.getScanPoint();//CommonUtils.parseString("ADDRESSCODEID",data);
            Address address = section.addressMap.get(scanPoint);
            logger.debug("WorkStationID : " + workStation.getWorkStationId() + " 的自动扫描点是 AddressCodeId:" + scanPoint);
            if (address != null) {
                section.scanPoionts.put(address, workStation);
            }
            //停止
            String stopPoint = workStation.getStopPoint();//CommonUtils.parseString("ADDRESSCODEID",data);
            Address stop = section.addressMap.get(stopPoint);
            logger.debug("WorkStationID : " + workStation.getWorkStationId() + " 的停止点是 AddressCodeId:" + stopPoint);
            if (stop != null) {
                section.stopPoionts.put(stop, workStation);
            }
            //回库房缓冲点
            String bufferPoint = workStation.getBufferPoint();//CommonUtils.parseString("ADDRESSCODEID",data);
            Address buffer = section.addressMap.get(bufferPoint);
            logger.debug("WorkStationID : " + workStation.getWorkStationId() + " 的缓冲点是 AddressCodeId:" + bufferPoint);
            if (buffer != null) {
                section.bufferPoionts.put(buffer, workStation);
            }
            if (buffer == null || stop == null || address == null) {
                logger.error("工作站初始化错误,扫描点 停止点 或 缓冲点为空!");
            }
        }
    }

    /**
     * 取距离最小的作为热度
     *
     * @param address
     * @param workStations
     * @param section
     */
    public static final String INNER_VALUE = "1";
    public static final String OUTTER_VALUE = "2";
    private void computeHot(Address address, Collection<WorkStation> workStations, Section section) {
        Iterator<WorkStation> iterator = workStations.iterator();
        int hot = Integer.MAX_VALUE;
        while (iterator.hasNext()) {
            WorkStation next = iterator.next();
            Address stop = this.getAddressByAddressCodeId(next.getStopPoint(), section);
            if (stop == null) {
                logger.error("数据错误:WorkStation: " + next.getWorkStationId() + " 的stop节点:"
                        + next.getStopPoint() + " 在Section:" + section.getSection_id() + "没有找到!");
                continue;
            }
            int temp = Math.abs(stop.getxPosition() - address.getxPosition())
                    + Math.abs(stop.getyPosition() - address.getyPosition());

            if(Objects.equals(address.getClassValue(), INNER_VALUE)){
                String innerCost = this.systemPropertiesManager.getProperty("COST_IN_INNER",address.getWareHouseId());
                if(CommonUtils.isEmpty(innerCost)){
                    innerCost = "20";
                }
                temp += Integer.parseInt(innerCost);//优先不放到深层货架
            }
            if (temp < hot) {
                hot = temp;
            }
        }
        address.setHot(hot);
        logger.debug("Address" + address + "的热度值是：" + hot);
    }

    @Override
    public void run(String... strings) throws Exception {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            logger.debug(string);
        }
        logger.debug("初始化仓库.....");
        List<Map> list = this.jdbcRepository.queryByKey("WAREHOUSEMANAGER.LOADALLWAREHOUSE");
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            String wareHouse_id = (String) map.get("WAREHOUSE_ID");
            logger.debug("加载仓库,wareHouse_id=" + wareHouse_id);
            WareHouse wareHouse = new WareHouse();
            wareHouse.setWareHouseId(wareHouse_id);

            List<Map> sections = this.jdbcRepository.queryByKey("WAREHOUSEMANAGER.LOADALLSECTIONS", wareHouse_id);
            for (int j = 0; j < sections.size(); j++) {
                Map secRow = sections.get(j);
                String section_id = CommonUtils.parseString("ID", secRow);

                Section section = new Section();
                section.setSection_id(section_id);
                section.setRcs_sectionId(CommonUtils.parseLong("RCS_SECTIONID", secRow));
                section.setWareHouse_id(wareHouse_id);
                section.setSection_name(CommonUtils.parseString("NAME", secRow));
                section.setActiveMapId(CommonUtils.parseString("MAP_ID", secRow));//当前激活的地图ID
                logger.debug("加载WareHouse:" + wareHouse_id + "的Section:" + section_id + "的地图信息:");
                loadAddresses(section);
                //加载组关系
                loadAddrGroup(section);
                //加载工作站
                loadWorkStations(section);
                //加载充电桩
                loadChargers(section);
                //加载carrypod的相关信息

                createWareHouse(section);
                wareHouse.sectionMap.put(section_id, section);
                this.allSections.put(section_id,section);
                logger.debug("加载WareHouse:" + wareHouse_id + "的Section:" + section_id + "的地图信息结束!");
            }
            this.wareHouseMap.put(wareHouse_id, wareHouse);
        }
        logger.debug("仓库初始化完成");
    }

    private void loadChargers(Section section) {
        logger.debug("加载充电桩信息...");
        List<Map> rows = this.jdbcRepository.queryByKey("WareHouseManager.loadChargers", section.getSection_id());
        for (int i = 0; i < rows.size(); i++) {
            Map map = rows.get(i);
            Charger charger = new Charger();
            charger.setChargerId(CommonUtils.parseString("ID", map));
            charger.setDirect(CommonUtils.parseInteger("TOWARD", map));
            charger.setName(CommonUtils.parseString("NAME", map));
            charger.setAddressCodeId(CommonUtils.parseString("PLACEMARK", map));
            charger.setWareHouseId(CommonUtils.parseString("WAREHOUSE_ID", map));
            charger.setSectionId(CommonUtils.parseString("SECTION_ID", map));
            charger.setRcsChargerId(CommonUtils.parseInteger("CHARGER_ID", map));
            charger.setChargerType(CommonUtils.parseInteger("CHARGER_TYPE", map));
            charger.setState(CommonUtils.parseString("STATE", map));
            //美的包含mac地址和充电桩的实际位置
            charger.setMac(CommonUtils.parseString("MAC", map));
            charger.setChargerAddr(CommonUtils.parseString("CHARGERADDR", map));
            section.chargers.put(charger.getChargerId(), charger);
        }

        logger.debug("初始化充电桩结束,充电桩有：" + section.chargers.size());

    }

    /**
     * 加载存储区的组关系
     *
     * 地址组内部的格子只可能
     *
     * 1、启动时 {@link WareHouseManager} 加载地址组信息
     * 2、POD启动时 {@link PodManager} 加载当前格子信息时，如果地址组的格子都没POD，将外部的设置成Reserved，最里面的Available
     * 3、POD从存储位离开时 如果是地址组格子，判断外面是否有格子，外面的格子设置成Reserved 里头的从Occupied=>Available
     * 4、
     *
     * @param section
     */
    private void loadAddrGroup(Section section) {
        //WD_NODE.`ADDRESSCODEID`, WD_NODE.CLASSVALUE,WD_NODE.`CLASSDIR`,WD_NODE.`CLASSGROUP` ORDER BY CLASSGROUP,CLASSVALUE
        List<Map> rows = this.jdbcRepository.queryByKey("WareHouseManager.loadAddrGroup", section.getSection_id());
        /*List<Map> rows = this.jdbcRepository.queryBySql("SELECT WD_NODE.`ADDRESSCODEID`, WD_NODE.CLASSVALUE,WD_NODE.`CLASSDIR`,WD_NODE.`CLASSGROUP` FROM WD_NODE,WD_MAP,WD_SECTION \n" +
                "                WHERE WD_SECTION.`ID` = WD_MAP.`SECTION_ID` AND WD_MAP.`ACTIVE`=1 AND CLASSGROUP IS NOT NULL \n" +
                "                ORDER BY CLASSGROUP,CLASSVALUE");*/
        //Address last = null;
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            Long addrCodeId = CommonUtils.parseLong("ADDRESSCODEID", row);
            String classValue = CommonUtils.parseString("CLASSVALUE", row);
            String classDir = CommonUtils.parseString("CLASSDIR", row);
            String classGroup = CommonUtils.parseString("CLASSGROUP", row);
            AddressGroup addressGroup = section.addressGroupMap.get(classGroup);
            if (addressGroup == null) {
                addressGroup = new AddressGroup(classGroup);
                section.addressGroupMap.put(classGroup, addressGroup);
            }
            Address currAddr = this.getAddressByAddressCodeId(addrCodeId, section);//当前节点即为最小节点
            currAddr.setClassDir(classDir);
            currAddr.setClassValue(classValue);
            currAddr.setAddressGroup(addressGroup);

            currAddr.setGroupInnerAddr(addressGroup.getOutterAddr());//当前节点加上上级节点作为内部节点引用 上级节点先存放到组的out属性里

            Address inner = addressGroup.getInnerAddr();
            if (inner == null) {
                addressGroup.setInnerAddr(currAddr);  //地址码组的第一个
            }
            Address outter = addressGroup.getOutterAddr();
            if (outter != null) {
                outter.setGroupOutterAddr(currAddr); //地址码组的最外一个 每一行执行后都变
            }
            currAddr.setGroupOutterAddr(null);//当前节点是最外层 所以它的外层是空的
            addressGroup.setOutterAddr(currAddr);
        }
    }

    /**
     * 加载section的workstation
     *
     * @param section
     */
    private void loadWorkStations(Section section) {
        List<Map> rows = this.jdbcRepository.queryByKey("WAREHOUSEMGR.LOADWORKSTATIONS", section.getSection_id());
        for (int i = 0; i < rows.size(); i++) {
            Map map = rows.get(i);
            if (CommonUtils.parseInteger("STOPPOINT", map) == 0
                    || CommonUtils.parseInteger("SCANPOINT", map) == 0
                    || CommonUtils.parseInteger("BUFFERPOINT", map) == 0) {
                continue;
            }

            WorkStation workStation = new WorkStation();//(WorkStation) CommonUtils.map2Bean(WorkStation.class,map);
            workStation.setWorkStationId(CommonUtils.parseString("ID", map));
            workStation.setWareHouseId(CommonUtils.parseString("WAREHOUSE_ID", map));
            workStation.setSectionId(CommonUtils.parseString("SECTION_ID", map));
            workStation.setWorkStationId(CommonUtils.parseString("ID", map));
            workStation.setStopPoint(CommonUtils.parseString("STOPPOINT", map));
            workStation.setScanPoint(CommonUtils.parseString("SCANPOINT", map));
            //扫描中点是(STOPPOINT+SCANPOINT)/2
            String midPoint = ((CommonUtils.parseLong("STOPPOINT", map)
                    + CommonUtils.parseLong("SCANPOINT", map)) / 2) + "";
            logger.info("工位上停止点与扫描点的中间点是:" + midPoint);
            workStation.setMidPoint(midPoint);
            workStation.setBufferPoint(CommonUtils.parseString("BUFFERPOINT", map));
            workStation.setFace(CommonUtils.parseInteger("WORKING_FACE_ORIENTATION", map));
            //加载旋转区
            List<Map> turnRows = this.jdbcRepository.queryByKey("WareHouseManager.loadTurnArea", section.getSection_id(), workStation.getWorkStationId());
            for (int j = 0; j < turnRows.size(); j++) {
                /*ADDRESSCODEID,TURNAREANODETYPE*/
                Map addrRow = turnRows.get(j);
                if (CommonUtils.parseInteger("TURNAREANODETYPE", addrRow) == 1 //入口1
                        || CommonUtils.parseInteger("TURNAREANODETYPE", addrRow) == 2) {
                    workStation.rotateOutInAddrs.add(CommonUtils.parseString("ADDRESSCODEID", addrRow));
                }
                if (CommonUtils.parseInteger("TURNAREANODETYPE", addrRow) == 2) {
                    workStation.setStation2RotateAddr(CommonUtils.parseString("ADDRESSCODEID", addrRow));
                }
                if (CommonUtils.parseInteger("TURNAREANODETYPE", addrRow) == 3) {
                    workStation.setRotateInOutAddr(CommonUtils.parseString("ADDRESSCODEID", addrRow));
                }
                /*如果旋转点不为0，将出口设置为旋转点*/
                if (CommonUtils.parseInteger("TURNAREANODETYPE", addrRow) == 0) {
                    String addrCode = CommonUtils.parseString("ADDRESSCODEID", addrRow);
                    if (!Objects.equals("0", addrCode)) {
                        workStation.setRotateInOutAddr(addrCode);
                        //工作站加上旋转点 L型旋转区类型
                        workStation.setRotatePoint(addrCode);
                    }
                }
                logger.debug("旋转区出口是:" + workStation.getRotateInOutAddr() + " 旋转点:"+workStation.getRotatePoint());
            }
            if (workStation.rotateOutInAddrs.size() != 2) {
                logger.error("WorkStation:" + workStation.getStopPoint()
                        + " 的旋转区入口不对:" + workStation.rotateOutInAddrs.size());
                //continue;
            }

            if (workStation.getStation2RotateAddr() == null) {
                logger.error("WorkStation:" + workStation.getStopPoint()
                        + " 的旋转区二次入口不对:" + workStation.getStation2RotateAddr());
                //continue;
            }

            if (workStation.getRotateInOutAddr() == null) {
                logger.error("WorkStation:" + workStation.getStopPoint()
                        + " 的旋转区出口不对:" + workStation.getRotateInOutAddr());
                //continue;
            }
            section.workStationMap.put(workStation.getWorkStationId(), workStation);
        }
    }

    private String getMidPoint(String stoppoint, String bufferpoint, Section section) {
        Address stop = this.getAddressByAddressCodeId(stoppoint,section);
        Address buffer = this.getAddressByAddressCodeId(bufferpoint,section);
        //缓冲点、中间点 在 停止点两侧 ，缓冲点在前方两格
        int bufferX = buffer.getxPosition();
        int bufferY = buffer.getyPosition();
        int stopX = stop.getxPosition();
        int stopY = stop.getyPosition();
        if(Math.abs(bufferX - stopX)==1){
            int midX = 3*stopX - 2*bufferX;//实际是停止点差两格
            int midY = stopY;
            Address mid = this.getAddressByXY(midX,midY,section);
            if(mid == null){
                throw new RuntimeException("停止点:"+stoppoint +" 或缓冲点:"+bufferpoint+" 定义有误!");
            }
            return mid.getId();
        }
        if(Math.abs(bufferY - stopY)==1){//
            int midY = 3*stopY - 2*bufferY; //实际是停止点差两格
            int midX = stopX;
            Address mid = this.getAddressByXY(midX,midY,section);
            if(mid == null){
                throw new RuntimeException("停止点:"+stoppoint +" 或缓冲点:"+bufferpoint+" 定义有误!");
            }
            return mid.getId();
        }
        logger.error("没有取到工作站:"+stoppoint+"的中间点");
        return null;
    }

    /**
     * 加载sectoin的格子
     *
     * @param section
     */
    public void loadAddresses(Section section) {
        List<Map> rows = this.jdbcRepository.queryByKey("WAREHOUSEMGR.LOADALLADDRESSES", section.getSection_id());
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            try {
                Address address = new Address();//(Address) CommonUtils.map2Bean(Address.class,row);
                address.setSectionId(section.getSection_id());
                address.setId(row.get("ADDRESSCODEID") + "");
                address.setPkId(CommonUtils.parseString("ID", row));
                address.setxPosition((Integer) row.get("XPOSITION"));
                address.setyPosition((Integer) row.get("YPOSITION"));
                address.setType(CommonUtils.parseInteger("TYPE", row));
                //开始都是可用的
                /*address.setNodeState(CommonUtils.parseString("NODE_STATE", row) == null ?
                        "Available" : CommonUtils.parseString("NODE_STATE", row));*/
                address.setNodeState(AddressStatus.AVALIABLE);
                logger.debug("地址码:"+address.getId()+"的状态为:"+address.getNodeState());
                section.addressList.add(address);
                section.addressMap.put(address.getId(), address);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 当前可用的格子 全部存储区
     *
     * @return
     */
    public List<Address> listUnOccupiedAddress(Section section) {
        logger.debug("获取Section" + section.getSection_id() + "未占用的格子位置");
        //TODO 正常情况每个格子的状态应该是更新好了 不用再从数据库取
        List<Address> addresses = new ArrayList<>();
        if (section.getStorageAddrs() != null) {
            addresses = section.getStorageAddrs();
        } else {
            List<Map> rows = this.jdbcRepository.queryByKey("WAREHOUSEMGR.LOADAVAlIABLEADDRESS", section.getSection_id());
            for (int i = 0; i < rows.size(); i++) {
                Map map = rows.get(i);
                String addressId = CommonUtils.parseString("ADDRESSCODEID", map);
                //从计算好的节点中获取
                if (section.addressMap.get(addressId) == null) {
                    logger.error("数据错误 addressId=" + addressId + "找不到");
                    continue;
                }
                Address address = section.addressMap.get(addressId);
                addresses.add(address);
            }
            section.setStorageAddrs(addresses);
        }
        //取所有的地址组，如果有组格子两个格子的，里头设置成可用
        Iterator<AddressGroup> addressGroup = section.addressGroupMap.values().iterator();
        while (addressGroup.hasNext()) {
            AddressGroup group = addressGroup.next();
            Address out = group.getOutterAddr();
            updateOuter(out);
        }

        return addresses;
    }
    //从外到内更新，过滤掉外面的节点
    private void updateOuter(Address out) {
        if(Objects.equals(out.getNodeState(), AddressStatus.AVALIABLE)
                && out.getGroupInnerAddr() != null
                && Objects.equals(out.getGroupInnerAddr().getNodeState(), AddressStatus.AVALIABLE)){
            out.setNodeState(AddressStatus.RESERVED);
            this.jdbcRepository.updateBusinessObject(out);
            updateOuter(out.getGroupInnerAddr());
        }
    }


    /**
     * 根据xy轴获取地址
     *
     * @param x
     * @param y
     * @return
     */
    public Address getAddressByXY(int x, int y, Section section) {
        //TODO 直接遍历
        Iterator<Address> adds = section.addressMap.values().iterator();
        while (adds.hasNext()) {
            Address address = adds.next();
            if (address.getxPosition() == x
                    && address.getyPosition() == y) {
                return address;
            }
        }
        return null;
    }

    /**
     * 通过地址码获取 ADDRESSCODEID
     *
     * @param addrCode
     * @return
     */
    public Address getAddressByAddressCodeId(long addrCode, Section section) {
        return section.addressMap.get("" + addrCode);
    }

    public Address getAddressByAddressCodeId(String addrCode, Section section) {
        return section.addressMap.get(addrCode);
    }

    /**
     * 通过section和地址码获取格子
     *
     * @param addressId
     * @param sectionId
     * @return
     */
    public Address getAddressByAddressCodeId(String addressId, String sectionId) {
        Section section = this.getSectionById(sectionId);
        return getAddressByAddressCodeId(addressId, section);
    }

    /**
     * 通过sectionID从仓库获取section
     *
     * @param sectionID
     * @return
     */
    public Section getSectionById(String sectionID) {
        return this.allSections.get(sectionID);
    }


    /*public Section getSectionById(Long sectionID){
        return this.getSectionById(sectionID+"");
    }*/

    public WorkStation findWorkStation(String workStationId) {
        Iterator<WareHouse> wareHouseIterator = this.wareHouseMap.values().iterator();
        while (wareHouseIterator.hasNext()) {
            WareHouse next = wareHouseIterator.next();
            Iterator<Section> sectionIterator = next.sectionMap.values().iterator();
            while (sectionIterator.hasNext()) {
                Section section = sectionIterator.next();
                WorkStation workStation = section.workStationMap.get(workStationId);
                if (workStation != null) return workStation;
            }
        }
        return null;
    }

    /**
     * 旋转区的两个入口
     *
     * @param workStationId
     * @param sectionId
     * @return
     */
    public List<String> getRotateInAddrs(String workStationId, String sectionId) {
        Section section = this.getSectionById(sectionId);
        WorkStation workStation = section.workStationMap.get(workStationId);
        return workStation.rotateOutInAddrs;
    }

    public long getRcsSectionIdBySectionId(String sectionId) {
        Section section = this.getSectionById(sectionId);
        return section.getRcs_sectionId();
    }

    public Section getSectionByRcsSectionId(Long rcs_sectionId) {
        Iterator<WareHouse> iter = this.wareHouseMap.values().iterator();
        while (iter.hasNext()) {
            WareHouse wareHouse = iter.next();
            Iterator<Section> iterSecs = wareHouse.sectionMap.values().iterator();
            while (iterSecs.hasNext()) {
                Section next = iterSecs.next();
                if (next.getRcs_sectionId() == rcs_sectionId) {
                    return next;
                }
            }
        }
        return null;
    }

    public boolean checkAddressByXY(long addressCodeID, int xPosition, int yPosition, Long rcs_sectionId) {
        Section section = this.getSectionByRcsSectionId(rcs_sectionId);
        Address address = this.getAddressByAddressCodeId(addressCodeID, section);
        if (address == null) {
            logger.error("地址不存在:addressCodeID:" + addressCodeID);
            return false;
        }
        return Objects.equals(xPosition, address.getxPosition())
                && Objects.equals(yPosition, address.getyPosition());
    }

    public Address getAddressByAddressCodeId(Long addrcode, String sectionId) {
        return getAddressByAddressCodeId(addrcode + "", sectionId);
    }
    //=======================================以下是更新格子状态的代码 包括COST值设定===========================================//

    /**
     * 当POD放下到格子上
     *
     * @param address
     */
    public void onPodDown(Pod pod, Address address) {
        address.setNodeState(AddressStatus.OCCUPIED);
        //设置当前节点的空车重车COST值
        this.updateCostOnPodDown(address);
        pod.setAddress(address);
        if (address.getGroupOutterAddr() != null) {
            address.getGroupOutterAddr().setNodeState(AddressStatus.AVALIABLE);
            this.jdbcRepository.updateBusinessObject(address.getGroupOutterAddr());
        }
        this.jdbcRepository.updateBusinessObject(address);
    }

    private void updateCostOnPodDown(Address address) {

    }

    /**
     * 当POD离开时 更新pod所在格子的状态
     *
     * @param address
     * @param order
     */
    public void onPodLeaveSrcAddr(Address address, Order order) {
        //address.setNodeState(AddressStatus.AVALIABLE);
        order.setFlag(true);
        //this.jdbcRepository.updateBusinessObject(address);
    }

    public void batchUpdateCells(String sectionId, List<String> addrs, String status) {
        logger.debug("批量更新节点与COST值：" + sectionId + " 节点:" + addrs + " status=" + status);
        if(addrs==null || addrs.size()==0){
            return;
        }
        Section section = this.getSectionById(sectionId);
        String wareHouseId = section.getWareHouse_id();
        String map_id = section.getActiveMapId();
        StringBuilder stringBuilder = new StringBuilder("UPDATE WD_NODE SET NODE_STATE=? WHERE MAP_ID=? AND ID IN (");
        List params = new ArrayList();
        params.add(status);
        params.add(map_id);
        StringBuilder inQuery = new StringBuilder();
        List inQueryParams = new ArrayList();
        //inQuery.append("(");
        for (int i = 0; i < addrs.size(); i++) {
            String s = addrs.get(i);
            Address addr = this.getAddressByAddressCodeId(s, sectionId);
            inQueryParams.add(addr.getPkId());

            //addr.setNodeState(status);
            inQuery.append("?");
            if (i < addrs.size() - 1) {//除了最后一个
                inQuery.append(",");
            }
        }
        //inQuery.append(")");
        stringBuilder.append(inQuery);
        logger.debug("拼接后的语句是:" + stringBuilder);
        params.addAll(inQueryParams);
        //节点的状态不需要设置 都是临时的
        //this.jdbcRepository.updateBySql(stringBuilder.toString(), params);
        //相同的条件 拼接更新COST的语句
        List paramsNeighbor = new ArrayList();
        Long cost = 0l;
        if (Objects.equals(status, AddressStatus.UNAVAILABLE)) {
            String unAvailable = this.systemPropertiesManager.getProperty("Cost_Unavailable", wareHouseId);
            if(!CommonUtils.isEmpty(unAvailable)){
                cost = Long.parseLong(unAvailable);
            }else {
                cost = 1000L;
            }
        }else{
            cost = null;
        }
        paramsNeighbor.add(cost);
        paramsNeighbor.add(map_id);
        StringBuilder updateCostSql = new StringBuilder("UPDATE WD_NEIGHBOR SET NEW_COST=? WHERE MAP_ID=? AND (IN_ID IN (");
        updateCostSql.append(inQuery).append(") OR OUT_ID IN (").append(inQuery).append("))");
        paramsNeighbor.addAll(inQueryParams);
        paramsNeighbor.addAll(inQueryParams);//两次
        this.jdbcRepository.updateBySql(updateCostSql.toString(), paramsNeighbor);
        logger.debug("批量更新节点与COST值：" + sectionId + " 节点:" + addrs + " status=" + status + " 结束!");
    }

    public void batchUpdateCellsCost(String sectionId, List<String> addrs, Long newCost) {
        logger.debug("批量更新节点与COST值：" + sectionId + " 节点:" + addrs + " newCost=" + newCost);
        if(addrs==null || addrs.size()==0){
            return;
        }
        Section section = this.getSectionById(sectionId);
        String map_id = section.getActiveMapId();

        StringBuilder inQuery = new StringBuilder();
        List inQueryParams = new ArrayList();
        //inQuery.append("(");
        for (int i = 0; i < addrs.size(); i++) {
            String s = addrs.get(i);
            Address addr = this.getAddressByAddressCodeId(s,section);
            inQueryParams.add(addr.getPkId());
            inQuery.append("?");
            if (i < addrs.size() - 1) {//除了最后一个
                inQuery.append(",");
            }
        }

        //相同的条件 拼接更新COST的语句
        List paramsNeighbor = new ArrayList();

        paramsNeighbor.add(newCost);
        paramsNeighbor.add(map_id);
        StringBuilder updateCostSql = new StringBuilder("UPDATE WD_NEIGHBOR SET NEW_COST=? WHERE MAP_ID=? AND (IN_ID IN (");
        updateCostSql.append(inQuery).append(") OR OUT_ID IN (").append(inQuery).append("))");
        paramsNeighbor.addAll(inQueryParams);
        paramsNeighbor.addAll(inQueryParams);//两次
        this.jdbcRepository.updateBySql(updateCostSql.toString(), paramsNeighbor);
        logger.debug("批量更新节点与COST值：" + sectionId + " 节点:" + addrs + " newCost=" + newCost + " 结束!");
    }

    /**
     * 根据节点状态改变节点的COST值
     *
     * @param curAddr
     */
    public void updateAddressCost(Address curAddr) {
        String sectionId = curAddr.getSectionId();
        Section section = this.getSectionById(sectionId);
        List<String> addrs  = new ArrayList();
        addrs.add(curAddr.getId());
        this.batchUpdateCells(section.getSection_id(),addrs,curAddr.getNodeState());
    }

    public void reloadWorkStation(String sectionId, String workstationId) {
        Section section = this.getSectionById(sectionId);
        this.loadWorkStations(section);
    }

    public void reloadCharger(String sectionId, String chargerId) {
        Section section = this.getSectionById(sectionId);
        this.loadChargers(section);
    }

    public void reloadSection(String sectionId) {
        Section section = this.getSectionById(sectionId);
        loadAddresses(section);
        //加载组关系
        loadAddrGroup(section);
        //加载工作站
        loadWorkStations(section);
        //加载充电桩
        loadChargers(section);
        createWareHouse(section);

    }
}
