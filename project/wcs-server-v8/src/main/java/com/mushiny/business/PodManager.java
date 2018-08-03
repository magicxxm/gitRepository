package com.mushiny.business;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.beans.enums.AddressType;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.beans.order.Order;
import com.mushiny.beans.order.StationPodOrder;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import com.mushiny.mq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Tank.li on 2017/6/26.
 */
@Component
@org.springframework.core.annotation.Order(value = 3)
public class PodManager implements CommandLineRunner {
    private final static Logger logger = LoggerFactory.getLogger(PodManager.class);

    @Autowired
    private MessageSender messageSender;
    @Autowired
    private JdbcRepository jdbcRepository;
    //将所有pod的状态保存在这里
    private Map<String, Map<String, Pod>> sectionPodMap = new ConcurrentHashMap<>();//所有POD集合 第一个key是sectionId 第二个是podId
    private Map<String, Map<Long, Pod>> sectionRcsPodMap = new ConcurrentHashMap<>();//所有POD集合 第一个key是sectionId 第二个是podId

    //用于排序的Pod集合
    private Map<String, List<Pod>> sectionPodList = new ConcurrentHashMap<>();//分section 将Pod排序

    @Autowired
    private WareHouseManager wareHouseManager;

    private boolean finisHotCompute;
   /* //@Autowired
    private RobotManager robotManager;*/

    public boolean isFinisHotCompute() {
        return finisHotCompute;
    }

    public void setFinisHotCompute(boolean finisHotCompute) {
        this.finisHotCompute = finisHotCompute;
    }

    public void putPod(String sectionId, String key, Pod pod) {
        //podMap.put(key,pod);
        Map<String, Pod> podMap = sectionPodMap.get(sectionId);
        if (podMap == null) {
            podMap = new HashMap<>();
            sectionPodMap.put(sectionId, podMap);
        }
        podMap.put(key, pod);

        Map<Long, Pod> rcsPodMap = sectionRcsPodMap.get(sectionId);
        if (rcsPodMap == null) {
            rcsPodMap = new HashMap<>();
            sectionRcsPodMap.put(sectionId, rcsPodMap);
        }
        rcsPodMap.put(pod.getRcsPodId(),pod);
    }

    public Pod getPod(String sectionId, String key) {
        Map<String, Pod> podMap = sectionPodMap.get(sectionId);
        if (podMap == null) {
            return null;
        }
        return podMap.get(key);
    }

    public Pod findPod(String key) {
        //TODO
        Iterator<WareHouse> wareHouseIterator = this.wareHouseManager.getWareHouseMap().values().iterator();
        while (wareHouseIterator.hasNext()) {
            WareHouse wareHouse = wareHouseIterator.next();
            Iterator<Section> sectionIterator = wareHouse.sectionMap.values().iterator();
            while (sectionIterator.hasNext()) {
                Section section = sectionIterator.next();
                Pod pod = this.getPod(section.getSection_id(), key);
                if (pod != null) {
                    return pod;
                }
                try {
                    pod = this.getPodByRcsPodId(Long.parseLong(key), section);
                    if (pod != null) {
                        return pod;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
        return null;
    }

    /**
     * 清除pod的热度，理想地址，目标地址排序列表,重新计算
     */
    public void clearPod() {
        Iterator podMapIter = sectionPodMap.values().iterator();
        while (podMapIter.hasNext()) {
            Map next = (Map) podMapIter.next();
            Iterator<Pod> podIterator = next.values().iterator();
            while (podIterator.hasNext()) {
                Pod pod = podIterator.next();
                //清空这三个值，每次热度重新计算
                pod.setzHot(0);
                pod.setIdeaTarget(null);
                pod.setFavorAddrs(null);
            }
        }
    }

    /**
     * @param wareHouseId
     * @return
     */
    public List getHotSortedPots(String wareHouseId) {
        List params = new ArrayList();
        params.add(wareHouseId);

        /*SELECT tabcountitem.*,countpodid FROM (SELECT COUNT(itemdata_id) AS countitem,itemdata_id FROM `ob_customershipmentposition` csp
            GROUP BY itemdata_id) tabcountitem,
            (SELECT COUNT(pod.id) countpodid,itemdata_id
            FROM MD_POD POD,MD_STORAGELOCATION LOCATION,INV_UNITLOAD UNITLOAD,INV_STOCKUNIT SKU
            WHERE POD.ID = LOCATION.POD_ID AND UNITLOAD.STORAGELOCATION_ID = LOCATION.ID AND SKU.UNITLOAD_ID = UNITLOAD.ID
            GROUP BY itemdata_id) tabcountpod WHERE tabcountpod.itemdata_id=tabcountitem.itemdata_id;*/

        //1、当前有多少个shipmentLine 每个shipmentline一种商品
        List rows = this.jdbcRepository.queryByKey("PODBIZ.GETHOTSORTEDPOTS_POT", params);
        for (int i = 0; i < rows.size(); i++) {
            Map rowPod = (Map) rows.get(i);
            Pod pod = new Pod();
            pod.setPodId((String) rowPod.get("ID"));
            //地址 TODO
            //pod.setAddress(new Address((Integer)rowPod.get("X"), (Integer)rowPod.get("Y")));
            pod.setPodName((String) rowPod.get("PODNAME"));
            List p = new ArrayList();
            p.add(pod.getPodId());

            List rowFaces = this.jdbcRepository.queryByKey("PODBIZ.GETHOTSORTEDPOTS_POTPOSITION", params);
            //一般是四个面
            Set faces = new HashSet();
            for (int j = 0; j < rowFaces.size(); j++) {
                Map rowFace = (Map) rowFaces.get(j);
                PodPosition podPosition = new PodPosition();
                faces.add(podPosition);
                podPosition.setPodId(pod.getPodId());
                podPosition.setPodPositionId((String) rowFace.get("PODPOSITIONID"));
                podPosition.setPodPositionName((String) rowFace.get("PODPOSITIONNAME"));
            }
        }
        return null;
    }

    public void initialPotFavarAddress(Pod pod, List<Address> addresses, Section section) {
        //如果Pod热度为0 要单独处理两种情况
        //一种有商品 按排序走 一种是空了 排最后
        //没有进入到有热度的列表 第一次运算就过滤掉了
        List<Address> addressList = section.addressList;
        Address targetAddress = pod.getIdeaTarget();
        if (checkIsEmpty(pod.getPodId())) {
            //获取空pod应该对应的理想位置 空pod的热度必然是0
            targetAddress = addressList.get(addressList.size() - 1);//库房最远的那个是他理想的位置
        }

        pod.setIdeaTarget(targetAddress);
        pod.setzHot(targetAddress.getHot());
        logger.debug("POD id=" + pod.getPodId() + "热度" + pod.getHot()
                + " POD转换后的热度:" + pod.getzHot()
                + " POD的理想位置 addressId:" + targetAddress.getId());
        for (int i = 0; i < addresses.size(); i++) {
            Address address = addresses.get(i);
            //计算空位置相对pod的得分 以最低分排序
            addrScore(address, pod);
        }
        Collections.sort(addresses, new AddrScoreComparator());//根据得分排序
        pod.setFavorAddrs(new ArrayList<>(addresses));//新构建List 保持顺序不变
        logger.debug("POD id=" + pod.getPodId() + " 最优位置是" + addresses.get(0).getxPosition() + "," + addresses.get(0).getyPosition());
    }

    /**
     * 缓存有任务POD的可用位置排序信息
     *
     * @param section
     */
    public void cacheAll(Section section) {
        List<Address> addresses = this.wareHouseManager.listUnOccupiedAddress(section);
        Map<String, Pod> podMap = this.sectionPodMap.get(section.getSection_id());
        Iterator iter = podMap.keySet().iterator();
        while (iter.hasNext()) {
            String podId = (String) iter.next();
            Pod pod = podMap.get(podId);
            initialPotFavarAddress(pod, addresses, section);
        }
    }

    private boolean checkIsEmpty(String podId) {
        //TODO POD是否是空的 可以通过消息来提前计算
        List params = new ArrayList();
        params.add(podId);
        List list = this.jdbcRepository.queryByKey("HOT.SERVICE.PODITEMS", params);
        return list == null || list.size() == 0;
    }

    @Autowired
    private Environment env;

    //某个格子对于某个pod的热度
    private void addrScore(Address address, Pod pod) {
        //可调系数
        double bias = Double.parseDouble(env.getProperty("com.mushiny.hotservice.bias"));
        //距离得分 score=distance(addr,pod)+bias*(addr.hot-pod.zhot)
        //其中addr的hot在地图生成时已产生 pod的zhot根据排序换算出来 distance(addr,pod)是该位置到pod的距离
        if (address == null || pod == null || pod.getAddress() == null) {
            logger.error("数据错误:address=" + address + " pod=" + pod);
            return;
        }
        double score = Math.abs(address.getxPosition() - pod.getAddress().getxPosition())
                + Math.abs(address.getyPosition() - pod.getAddress().getyPosition())
                + bias * Math.abs(address.getHot() - pod.getzHot());
        address.setScore(score);
    }

    /**
     * 地址是否被锁定
     *
     * @param address
     * @return
     */
    public boolean isAvaliable(Address address) {
        return address.getPod()==null
                && Objects.equals(address.getNodeState(), AddressStatus.AVALIABLE);
    }

    public Pod getPodByRcsPodId(long podId, Section section) {
        if (podId == 0) {
            return null;
        }
        //TODO
        Map<Long, Pod> podMap = this.sectionRcsPodMap.get(section.getSection_id());
        return podMap.get(podId);
    }


    @Override
    public void run(String... strings) throws Exception {
        logger.debug("加载仓库POD信息....");
        List<Map> rows = this.jdbcRepository.queryByKey("POD.MANAGER.LOADPODS");
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            Pod pod = new Pod();//POD在这里创建
            pod.setPodId((String) row.get("ID"));//(String) row.get("NAME")
            String podName = CommonUtils.parseString("NAME", row);
            pod.setPodName(podName);
            Long rcsPodId = Long.parseLong(podName.substring(1));//去掉第一个字母P
            pod.setRcsPodId(rcsPodId);

            pod.setDirect(CommonUtils.parseInteger("TOWARD", row));
            /*int x = CommonUtils.parseInteger("XPOS",row);
            int y = CommonUtils.parseInteger("YPOS",row);*/
            String sectionId = CommonUtils.parseString("SECTION_ID", row);
            pod.setSectionId(sectionId);
            Section section = this.wareHouseManager.getSectionById(sectionId);
            long addressCodeId = CommonUtils.parseLong("PLACEMARK", row);
            Address address = this.wareHouseManager.getAddressByAddressCodeId(addressCodeId, section);//this.wareHouseManager.getAddressByXY(x,y,section);
            if (address != null) {
                pod.setAddress(address);
                address.setPod(pod);
                if (address.getType() != AddressType.STORAGE) {
                    logger.error("POD:" + pod.getPodName() + "不在存储区，地址码:" + addressCodeId);
                }
                address.setNodeState(AddressStatus.OCCUPIED);//POD当前地址设置成被占用
                logger.info("POD:"+pod.getPodName()+"所在的地址格:"+addressCodeId+"状态为:"+address.getNodeState());
                //将POD目的地设置成-1
                pod.addKV("ADDRCODEID_TAR",-1);
                pod.addKV("XPOS_TAR",-1);
                pod.addKV("YPOS_TAR",-1);
                this.jdbcRepository.updateBusinessObject(pod);
                //加到Map里 用于快速检索
                this.putPod(sectionId, pod.getPodId(), pod);
                //加到List里 用于排序
                this.addPod2SortList(sectionId, pod);
            } else {
                logger.error("POD:" + pod.getPodName() + " 所在的地址码:" + addressCodeId + " 在地图中找不到!");
            }
        }

        //将所有Section的Address状态更新，如果有POD，只有POD往外的第一个Address是Available 其他都是Reserved
        Collection wareHouses = this.wareHouseManager.getWareHouseMap().values();
        Iterator<WareHouse> iterator = wareHouses.iterator();
        logger.debug("开始设置组地址码的状态.....");
        while (iterator.hasNext()) {
            WareHouse wareHouse = iterator.next();
            Collection<Section> sections = wareHouse.sectionMap.values();
            Iterator<Section> secIterator = sections.iterator();
            while (secIterator.hasNext()) {
                Section section = secIterator.next();
                Collection<AddressGroup> addressGroups = section.addressGroupMap.values();
                if(addressGroups.isEmpty()){
                    logger.info("Section: "+section.getRcs_sectionId()+" 没有组定义!");
                    continue;
                }
                Iterator<AddressGroup> groupIterator = addressGroups.iterator();
                while (groupIterator.hasNext()) {
                    AddressGroup addressGroup = groupIterator.next();
                    //从外到内计算,被Occupied的前一个是Available，其他都是Reserved
                    Address out = addressGroup.getOutterAddr();
                    update(out);
                    List<Address> addresses = addressGroup.getGroupAddrs();
                    for (int i = 0; i < addresses.size(); i++) {
                        Address address = addresses.get(i);
                        logger.debug("AddressGroup "+addressGroup.getGroupId()+" 的节点:"
                                +address.getId()+" 状态是"+address.getNodeState());
                    }
                }
            }
        }
        logger.debug("设置组地址码的状态结束!");

        logger.debug("仓库POD初始化完毕，共有:" + rows.size() + "个.");
    }

    /**
     * 如果内部节点
     * @param addr
     */
    public void update(Address addr) {
        if(addr == null || addr.getGroupInnerAddr() == null
                || Objects.equals(addr.getNodeState(), AddressStatus.OCCUPIED)){
            //如果当前节点已被占用,没必要看后面的
            return;
        }
        //如果里头的节点不是被POD占用的，外面的节点被RESERVED，不作为存储可选目的地
        if(Objects.equals(addr.getNodeState(), AddressStatus.AVALIABLE) && addr.getGroupInnerAddr()!=null
                && !Objects.equals(addr.getGroupInnerAddr().getNodeState(), AddressStatus.OCCUPIED)){
            addr.setNodeState(AddressStatus.RESERVED);
        }
        update(addr.getGroupInnerAddr());
    }

    public void addPod2SortList(String sectionId, Pod pod) {
        List<Pod> podList = this.sectionPodList.get(sectionId);
        if (podList == null) {
            podList = new ArrayList<>();
            this.sectionPodList.put(sectionId, podList);
        }
        podList.add(pod);
    }

    /**
     * 检查是否被占用
     *
     * @param podId
     * @return
     */
    public List checkOccupied(String podId) {
        logger.debug("检查POD是否被分配：包含上架与拣货等");
        List params = new ArrayList();
        params.add(podId);
        return this.jdbcRepository.queryByKey("POD.MANAGER.FINDPODBYPODID");
    }


    public void clearSectionPod(Section section) {
        List<Pod> podList = this.sectionPodList.get(section.getSection_id());
        for (int i = 0; i < podList.size(); i++) {
            Pod pod = podList.get(i);
            //清空这三个值，每次热度重新计算
            pod.setzHot(0);
            pod.setIdeaTarget(null);
            pod.setFavorAddrs(null);
        }
    }

    public List<Pod> getPodListBySection(String section_id) {
        return this.sectionPodList.get(section_id);
    }

    /**
     * POD位置发生改变 从RCS_WCS_AGV_POSITION_CHANGE发生
     *
     * @param pod
     * @param previousAddress
     * @param currentAddress
     * @param curOrder
     */
    public void onPodMove(Pod pod, long previousAddress, long currentAddress, Order curOrder) {
        logger.debug("POD:" + pod.getPodName() + " 位置发生变化:" + previousAddress + "==>" + currentAddress);
        String sectionId = pod.getSectionId();
        Section section = this.wareHouseManager.getSectionById(sectionId);
        //更换地址
        Address curAddr = this.wareHouseManager.getAddressByAddressCodeId(currentAddress, section);
        Address preAddr = this.wareHouseManager.getAddressByAddressCodeId(previousAddress, section);
        if (curAddr == null || preAddr == null) {
            logger.error("当前地址或者上个地址为空 curAddr:" + curAddr + " preAddr:" + preAddr);
            return;
        }
        //通过实时包更新地址 以免丢失
        //pod.setAddress(curAddr);
        pod.addKV("PLACEMARK", currentAddress)
                .addKV("XPOS", curAddr.getxPosition())
                .addKV("YPOS", curAddr.getyPosition())
                .addKV("TOWARD",pod.getDirect());
        //POD的位置在实时包里记录到数据库 会带上实时包里保存的朝向
        this.jdbcRepository.updateBusinessObject(pod);
        if (curOrder == null) {
            logger.error("没有调度单，POD" + pod.getPodName() + "在移动.....");
            return;
        }
        //处理StationPodOrder的逻辑
        if (curOrder instanceof StationPodOrder) {
            StationPodOrder order = (StationPodOrder) curOrder;
            WorkStation workStation = order.getWorkStation();//TODO
            Long rIn1 = Long.parseLong(workStation.rotateOutInAddrs.get(0));
            Long rIn2 = Long.parseLong(workStation.rotateOutInAddrs.get(1));
            //0、判断是否到达旋转区入口，打印日志
            if (rIn1 == currentAddress || rIn2 == currentAddress) {
                logger.debug("当前POD" + pod.getPodName() + "已经到达旋转区入口! addrCode=" + currentAddress + " POD的A面角度是:" + pod.getDirect()
                        + " 需要的面是" + order.getUseFace() + " 工作站朝向是:" + workStation.getFace());
                //TODO 进入旋转区的方法
            }
            //1、判断是否从旋转区离开，将入口排队数量减少1
            Long rIn2Out = Long.parseLong(workStation.getRotateInOutAddr());
            if (rIn2Out == currentAddress) {
                logger.debug("当前POD" + pod.getPodName() + "已经到达旋转区出口! addrCode=" + currentAddress + " POD的A面角度是:" + pod.getDirect()
                        + " 需要的面是" + order.getUseFace() + " 工作站朝向是:" + workStation.getFace());
                //判断经过了哪个入口 两个
                String inCode1 = workStation.rotateOutInAddrs.get(0);
                String inCode2 = workStation.rotateOutInAddrs.get(1);
                //是否经过该入口
                boolean flag = order.getWcsPath().getSeriesPath().indexOf(Long.parseLong(inCode1)) > 0;
                long temp = flag ? workStation.inAddr1.getAndDecrement() : workStation.inAddr2.getAndDecrement();
                logger.debug("工作站" + workStation.getWorkStationId() + " 入口1排队数量是:" + workStation.inAddr1.get()
                        + " 入口2排队数量是:" + workStation.inAddr2.get());
            }
            //2、如果离开工作站的扫描区
            if (Objects.equals(preAddr.getId(), workStation.getScanPoint())
                    || Objects.equals(curAddr.getId(), workStation.getMidPoint())) {//离开了扫描点 到达中间点推送
                logger.debug("POD:" + pod.getPodName() + "离开了工作台自动位移的位置!" + " POD的A面角度是:" + pod.getDirect());
                //Queue<Pod> podQueue = workStation.podQueue;
                //podQueue.add(pod);//当前pod

                logger.debug("是否满足往工作站推送的条件: noPodOnWorkStation(workStation):" + noPodOnWorkStation(workStation, pod));
                if (noPodOnWorkStation(workStation, pod)) {//如果前面的pod都不在 就发条信息给工作站
                    String podName = pod.getPodName();
                    int podFace = pod.getDirect();//0 90 180 270 A
                    int wsFace = workStation.getFace();//工作站朝向
                    //int face = ((podFace+wsFace)%360)/90;
                    String sf = CommonUtils.face2WorkStation(podFace, wsFace);
                    Map data = new HashMap();
                    data.put("sectionId", sectionId);
                    data.put("pod", podName + sf);
                    data.put("workstation", workStation.getWorkStationId());
                    this.sendMessage2WorkStation(JsonUtils.map2Json(data));
                    logger.debug("往工作站推送消息:" + data);
                }
            }
            //3、如果到了扫描点
           /*if(Objects.equals(curAddr.getId(), workStation.getScanPoint())){//到了扫描点与停止点的中点
                logger.debug("POD:"+pod.getPodName()+"到了扫描点, POD的A面角度是:"+pod.getDirect());
                //加到当前工作站的扫描位置
                workStation.setOnMidPod(pod);
            }*/

            //3、如果到了停止点
            if (Objects.equals(curAddr.getId(), workStation.getStopPoint())) {//到了停止点的中点
                logger.debug("POD:" + pod.getPodName() + "到了工作台停止点, POD的A面角度是:" + pod.getDirect());
                //加到当前工作站的扫描位置
                //logger.debug("POD:"+pod.getPodName()+"+到了工作台,操作员开始工作....");
                //this.onPodArriveWorkStation(pod, section.stopPoionts.get(curAddr));//当POD到达工作站
            }
            //当POD离开工作站
            //Address preAddr = this.wareHouseManager.getAddressByAddressCodeId(previousAddress,section);
            if (Objects.equals(preAddr.getId(), workStation.getStopPoint())) {//如果上一个位置是工作站
                logger.debug("当Pod离开工作站.....");
                //this.onPodLeaveWorkStation(pod, section.stopPoionts.get(preAddr));
            }
        }
    }

    private boolean noPodOnWorkStation(WorkStation workStation, Pod curPod) {
        String stop = workStation.getStopPoint();
        Address address = this.wareHouseManager.getAddressByAddressCodeId(stop, workStation.getSectionId());
        Pod podOnStop = address.getPod();
        //已经到了或者为空 赶紧推吧
        return Objects.equals(curPod, address.getPod()) || (podOnStop == null);
    }

    private void sendMessage2WorkStation(String message) {
        this.messageSender.sendWebSocketMsg(message);
    }

    private void onPodLeaveWorkStation(Pod pod, WorkStation workStation) {
        logger.info("POD:" + pod.getPodId() + " 离开工作台: " + workStation.getWorkStationId());
        //TODO
    }

    private void onPodArriveWorkStation(Pod pod, WorkStation workStation) {
        logger.info("POD:" + pod.getPodName() + " 到达工作台: " + workStation.getWorkStationId());
        //TODO
    }

    /**
     * 通过当前pod计算需要返回的目标地址ID
     *
     * @param podId
     * @param sectionId
     * @return
     */
    public String computeTargetAddress(String podId, String sectionId) {
        logger.debug("获取热度计算后的目标地址...");
        int i = 0;
        String addr = null;
        Section section = this.wareHouseManager.getSectionById(sectionId);
        Pod pod = this.getPodByRcsPodId(Long.parseLong(podId), section);
        while (i < 10) {
            addr = this.targetAddr(pod.getPodId());
            if (!CommonUtils.isEmpty(addr)) {
                return addr;
            }
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        logger.debug("热度计算结果是:" + addr);
        return addr;
    }

    private String targetAddr(String podId) {

        Pod pod = this.findPod(podId);
        if (pod == null) {
            throw new RuntimeException("没有找到这个POD信息!");
        }
        List<Address> addressList = pod.getFavorAddrs();//该pod的理想位置
        if (addressList == null || addressList.size() == 0) {
            //throw new RuntimeException("热度算法未执行出结果!");
            return null;
        }
        for (int i = 0; i < addressList.size() && i < 5; i++) {
            Address address = addressList.get(i);
            logger.debug("热度排序:POD:"+pod.getPodName()+" Address:"+address.getId()+" score:"+address.getScore()+" status:"+address.getNodeState());
        }
        String addressCodeId = null;
        for (int i = 0; i < addressList.size(); i++) {
            Address address = addressList.get(i);
            //TODO
            if (isAvaliable(address) && !Objects.equals(address,pod.getAddress())//不能找当前节点
                    && !innerEmptyOrLockedBy(address) && noAvailableTripInInner(address)
                    && (pod.getLockedBy()==address.getLockedBy() || address.getLockedBy()==0)) {
                addressCodeId = address.getId();
                return addressCodeId;
            }
        }
        return null;
    }

    //内部存储是否被其他车锁定或者是空的，这个点就是不能用
    private boolean innerEmptyOrLockedBy(Address address) {
        Address inner = address.getGroupInnerAddr();
        if(inner != null){
            if(inner.getLockedBy()!=0L
                    || inner.getNodeState().equals(AddressStatus.AVALIABLE)){
                logger.error("inner:"+inner.getId()+"为空或被锁定:inner.getLockedBy():"+inner.getLockedBy()
                        +" inner.getNodeState():"+inner.getNodeState());
                return true;
            }
            //inner = inner.getGroupInnerAddr();
        }
        return false;
    }

    private static final String NOTRIPAVAILABLE = "SELECT 1 FROM RCS_TRIP,MD_POD WHERE MD_POD.ID=RCS_TRIP.POD_ID \n" +
            "  AND (RCS_TRIP.TRIP_STATE=? OR RCS_TRIP.TRIP_STATE=?) AND  MD_POD.PLACEMARK=?";
    //内部存储格的POD没有调度单是Available的
    private boolean noAvailableTripInInner(Address address) {
        logger.debug("检查内部存储格是否有POD即将要执行:"+address);
        Address inner = address.getGroupInnerAddr();
        if(inner!=null){
            List list = this.jdbcRepository.queryBySql(NOTRIPAVAILABLE,TripStatus.AVAILABLE,
                    TripStatus.NEW,Long.parseLong(inner.getId()));
            if(list!=null&&list.size()>0){
                logger.debug("地址:"+address.getId()+" 内部地址:"+inner.getId()+"有POD要执行!");
                return false;
            }
            //inner = inner.getGroupInnerAddr();
        }
        logger.debug("地址:"+address.getId()+" 内部地址没有POD要执行!");
        return true;
    }

    public Pod findPodByPodName(Section section, String podName) {
        //TODO 先从内存取 因为pod数量不多 直接遍历
        String rcsPodId = podName.substring(1, podName.length() - 1);//P0000003C
        logger.debug("POD:" + podName + "===>" + rcsPodId);
        return this.getPodByRcsPodId(Long.parseLong(rcsPodId), section);
    }

    public Pod getPodByAddress(String id, String sectionId) {
        Address address = this.wareHouseManager.getAddressByAddressCodeId(id,sectionId);
        if(address == null){
            return null;
        }
        return address.getPod();
        /*Map<String, Pod> allPods = this.sectionPodMap.get(sectionId);
        Iterator<Pod> iterator = allPods.values().iterator();
        while (iterator.hasNext()) {
            Pod pod = iterator.next();
            if (pod.getAddress() != null && pod.getAddress().getId().equals(id)) {
                return pod;
            }
        }
        return null;*/
    }

    public void finishMainOrder(Order order) {
        /*  `  XPOS_TAR` int(11) DEFAULT NULL COMMENT '目标地址X轴',
              `YPOS_TAR` int(11) DEFAULT NULL COMMENT '目标地址Y轴',
              `ADDRCODEID_TAR` int(11) DEFAULT NULL COMMENT '目标地址码',*/
        order.getPod().setMovTargetAddrId(null);
        order.getPod().addKV("XPOS_TAR", -1L).addKV("YPOS_TAR", -1L).addKV("ADDRCODEID_TAR", -1L);
        if(noPodInAvailableTrip(order)){
            //没有调度单时 更新状态
            order.getPod().addKV("STATE", "Available");//更新POD状态
        }else{
            logger.error("该POD还有在执行的调度单:"+order.getPod().getPodName());
        }
        this.jdbcRepository.updateBusinessObject(order.getPod());
        order.getRobot().setPod(null);//将POD释放解除 TODO
        //order.getRobot().setLastOrderId(null);//20180611 不要再去找了
        order.getRobot().setOrderIndex(0);//20180611 默认为0
        //车没有了锁定目标
        order.getRobot().setLockedAddr(0L);
        //货架也恢复
        order.getPod().setLockedBy(0L);
        logger.debug("调度任务结束更新POD状态:" + order.getPod().getPodName());
        //当前地址码设置成占用
        Address curAddr = order.getPod().getAddress();
        if (curAddr.getType() == AddressType.STORAGE) {
            curAddr.setNodeState(AddressStatus.OCCUPIED);
            curAddr.setLockedBy(0L);
            curAddr.addKV("NODE_STATE", AddressStatus.OCCUPIED);//TODO
            logger.debug("调度任务结束更新POD占用格子状态:" + curAddr.getNodeState());
            //this.jdbcRepository.updateBusinessObject(curAddr);
        }
        Address outAddr = curAddr.getGroupOutterAddr();//
        //紧靠外面的设置成available
        if (outAddr != null) {
            //是否有pod在该地址
            if(outAddr.getPod()!=null){
                return;
            }
            outAddr.setNodeState(AddressStatus.AVALIABLE);
            outAddr.addKV("NODE_STATE", AddressStatus.AVALIABLE);//TODO
        }
    }
    private static String SQL = "SELECT 1 FROM RCS_TRIP WHERE (TRIP_STATE=? OR  TRIP_STATE=? OR TRIP_STATE=? OR TRIP_STATE=?)" +
            "AND POD_ID=? AND SECTION_ID=? AND DRIVE_ID<>? LIMIT 1";
    public boolean noPodInAvailableTrip(Order order) {
       List list = this.jdbcRepository.queryBySql(SQL, TripStatus.NEW, TripStatus.AVAILABLE,
               TripStatus.PROCESS,TripStatus.LEAVING,
               order.getPod().getPodId(),order.getPod().getSectionId(),order.getRobot().getRobotId());
       return list==null || list.size()==0;
    }

    public String getPodNameByRcsPodId(long podCodeID) {
        String ss = (10000000 + podCodeID) + "";
        return "P" + ss.substring(1);//把1换成P
    }

    public Pod getPodByAddress(long addressCodeID, String section) {
        return getPodByAddress(addressCodeID + "", section);
    }

    public static void main(String[] args) {
        //逆时针 从上往下看
        int podFace = 180;
        int wsFace = 0;
        String sf = CommonUtils.face2WorkStation(podFace, wsFace);
        //System.out.println(sf);
        int rotate = CommonUtils.aFaceToward(podFace, wsFace, "D");
        System.out.println(rotate);
    }

    private List<Map> unStoragePods = new ArrayList<>();
    public List<Map> getUnStoragePods4Rcs(String sectionId) {
        unStoragePods.clear();
        this.getPods4Rcs(sectionId);
        return unStoragePods;
    }
    public List<Map> getPods4Rcs(String sectionId) {
        List<Map> maps = new ArrayList<>();
        if (this.sectionPodMap.get(sectionId) == null) {
            return maps;
        }
        Iterator<Pod> pods = this.sectionPodMap.get(sectionId).values().iterator();

        while (pods.hasNext()) {
            Pod next = pods.next();
            Map map = new HashMap();
            if (next == null || next.getAddress() == null
                    || next.getAddress().getType()!=AddressType.STORAGE ) {
                logger.error("pod信息不对 或者 不在存储区:" + next);
                Map unMap = new HashMap();
                unMap.put(next.getRcsPodId(), next.getAddress().getId());
                unStoragePods.add(unMap);
                continue;
            }
            map.put(next.getRcsPodId(), next.getAddress().getId());
            maps.add(map);
        }
        return maps;
    }

    public Map getPodsDirect(String sectionId){
        Map podDirectMap = new HashMap();
        if (this.sectionPodMap.get(sectionId) == null) {
            return podDirectMap;
        }
        Iterator<Pod> pods = this.sectionPodMap.get(sectionId).values().iterator();
        while (pods.hasNext()) {
            Pod next = pods.next();
            if (next == null || next.getAddress() == null
                    || next.getAddress().getType()!=AddressType.STORAGE ) {
                logger.error("pod信息不对 或者 不在存储区:" + next);
                continue;
            }
            podDirectMap.put(next.getRcsPodId(), next.getDirect());
        }
        return podDirectMap;
    }

    public List<Pod> getPods(Section section) {
        return sectionPodList.get(section.getSection_id());
    }

    public Pod getPodById(String pod_id,String sectionId) {
        Map<String,Pod> pods = this.sectionPodMap.get(sectionId);
        return pods.get(pod_id);
    }

    public List<Map> queryItemHots() {
        logger.debug("获取全系统商品热度....");
        List<Map> itemHots = this.jdbcRepository.queryByKey("PodManager.queryItemHots");
        if(itemHots == null){
            itemHots = new ArrayList<>();
        }
        logger.debug("获取全系统商品热度....itemHots.size(): "+itemHots.size());
        return itemHots;
    }
}
