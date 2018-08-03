package com.mushiny.beans.order;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.AddressStatus;
import com.mushiny.beans.enums.AddressType;
import com.mushiny.beans.enums.OrderErrorMessage;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.business.*;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 调度单表
 * Created by Tank.li on 2017/7/14.
 
 */
public abstract class Order extends BaseObject {
    public final static Logger logger = LoggerFactory.getLogger(Order.class);

    public static final int ERROR_POD_NONEEDFACE = 7; //没有需要的面 tripposition表
    public static final int ERROR_POD_ONPATH = 8;     //重车路径的存储位上有POD
    public static final int ERROR_NO_HOTADDRESS = 9;  //没有可用的存储位
    public static final int ERROR_NOPOD = 10;         //调度单没有POD
    private static final String RCS_TRIP = "RCS_TRIP";

    private int orderError = SUCCESS;//等于0是正常

    public static final int SUCCESS = 0;
    public static final int ERROR_EMPTY_PATH = 1;//空车路径计算失败!
    public static final int ERROR_HEAVY_PATH = 2;//重车路径计算失败
    public static final int ERROR_START_POINT = 3;//小车不在路径起始位置
    public static final int NO_END_POINT = 4;//没有终点位置
    public static final int ERROR_ENDPOINT_NOTMATCH = 11;//终点位置不是路径最后一个节点
    public static final int ERROR_NO_WCSPATH = 5;//没有下发的路径 长度是0或1
    public static final int ERROR_POD = 6;//没有扫到POD 或者POD错误 被使用
    public static final int ERROR_LOCK_ENDADDR = 20;//无法锁定目标存储位

    private boolean flag;//成功执行调度单,任务已下发,状态已修改
    private boolean finished;//是否已经完成 防止重复点击释放
    //是否可以结束 通过到达终点判断 解决工作站到工作站的环形问题 由来编程序 不见有尽头
    private boolean canFinish = false;
    //替换小车  1是  0否
    private int isExchangeRobot;

    public int getIsExchangeRobot() {
        return isExchangeRobot;
    }

    public void setIsExchangeRobot(int isExchangeRobot) {
        this.isExchangeRobot = isExchangeRobot;
    }

    public boolean isCanFinish() {
        return canFinish;
    }

    public void setCanFinish(boolean canFinish) {
        this.canFinish = canFinish;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getOrderError() {
        return orderError;
    }



    public void setOrderError(int orderError) {
        this.orderError = orderError;
    }

    public static final int STATION2BUFFER = -1;
    public static final int BUFFER2STORAGE = -2;
    public static final int POD2STATION = 0;
    //四个重量级的管理器
    private WareHouseManager wareHouseManager;
    private PodManager podManager;
    private WebApiBusiness webApiBusiness;
    private JdbcRepository jdbcRepository;
    private RobotManager robotManager;

    public RobotManager getRobotManager() {
        return robotManager;
    }

    public void setRobotManager(RobotManager robotManager) {
        this.robotManager = robotManager;
    }

    //获取系统配置值
    private SystemPropertiesManager systemPropertiesManager;

    public PodManager getPodManager() {
        return podManager;
    }

    public void setPodManager(PodManager podManager) {
        this.podManager = podManager;
    }

    public JdbcRepository getJdbcRepository() {
        return jdbcRepository;
    }

    public void setJdbcRepository(JdbcRepository jdbcRepository) {
        this.jdbcRepository = jdbcRepository;
    }

    public WebApiBusiness getWebApiBusiness() {
        return webApiBusiness;
    }

    public void setWebApiBusiness(WebApiBusiness webApiBusiness) {
        this.webApiBusiness = webApiBusiness;
    }

    public WareHouseManager getWareHouseManager() {
        return wareHouseManager;
    }

    public void setWareHouseManager(WareHouseManager wareHouseManager) {
        this.wareHouseManager = wareHouseManager;
    }

    public void setSystemPropertiesManager(SystemPropertiesManager systemPropertiesManager) {
        this.systemPropertiesManager = systemPropertiesManager;
    }

    public SystemPropertiesManager getSystemPropertiesManager() {
        return systemPropertiesManager;
    }

    //public static final String WCS_RCS_AGV_CHARGE = "WCS_RCS_AGV_CHARGE";
    //public static final String WCS_RCS_AGV_SERIESPATH = "WCS_RCS_AGV_SERIESPATH";
    //下发路径
    protected WcsPath wcsPath = new WcsPath();

    public void setWcsPath(WcsPath wcsPath) {
        this.wcsPath = wcsPath;
    }

    public WcsPath getWcsPath() {
        return wcsPath;
    }

    private String orderId; //对应trip表的ID
    private String orderStatus;//对应trip表的status；

    private String activedBy;//同组的其他Trip的ID

    private String sectionId;
    private String wareHouseId;

    private String send2RcsTime;//字符串格式的发送时间

    private String message;//json格式的路径

    public String getSend2RcsTime() {
        return send2RcsTime;
    }

    public void setSend2RcsTime(String send2RcsTime) {
        this.send2RcsTime = send2RcsTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    //终点位置，起始位置不需要，都是小车开始位置
    private Long endAddr;

    private Pod pod;



    public Pod getPod() {
        return pod;
    }

    public void setPod(Pod pod) {
        this.pod = pod;
    }

    public Long getEndAddr() {
        return endAddr;
    }

    public void setEndAddr(Long endAddr) {
        this.endAddr = endAddr;
    }

    public String getActivedBy() {
        return activedBy;
    }

    public void setActivedBy(String activedBy) {
        this.activedBy = activedBy;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    //绑定的Robot
    private Robot robot;

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    //public static final String QUERY_POD_WEIGHT = "select * from sys_systemproperty where system_key = ?";
    public Map genWcsMessage(){
        Map msg = new HashMap();
        msg.put("robotID",this.getRobot().getRobotId());
        Section section = this.wareHouseManager.getSectionById(this.getRobot().getSectionId());
        msg.put("sectionID",section.getRcs_sectionId());
        msg.put("time", System.currentTimeMillis());
        //其他信息
        msg.put("seriesPath",wcsPath.getSeriesPath());
        msg.put("rotateTheta",wcsPath.getRotateTheta());
        msg.put("isRotatePod",wcsPath.getRotatePod());
        msg.put("podUpAddress",wcsPath.getPodUpAddress());
        msg.put("podDownAddress",wcsPath.getPodDownAddress());
        if(this.getPod()!=null){
            msg.put("podCodeID",this.getPod().getRcsPodId());
            Map productMap = this.getProductWeight();
            int podAllWeight = 0;
            try {
                /*List<Map> rows = this.jdbcRepository.queryBySql(QUERY_POD_WEIGHT.toUpperCase(), "POD_SELF_WEIGHT");
                if (rows!=null && rows.size()>0){
                    podAllWeight = CommonUtils.parseInteger("SYSTEM_VALUE", rows.get(0));
                }*/
                String w = this.getSystemPropertiesManager().getProperty("POD_SELF_WEIGHT", section.getWareHouse_id());
                if(CommonUtils.isEmpty(w)){
                    w = "150000";
                }
                podAllWeight = Integer.parseInt(w);
                logger.debug("系统中POD自重："+podAllWeight+"克");
            } catch (Exception e) {
                logger.error("查询pod自重出错！e:"+e.toString());
            }
            if (podAllWeight <= 0){
                podAllWeight = 150000;
            }
            if (productMap!=null && !productMap.isEmpty()){
                podAllWeight += CommonUtils.parseInteger("productWeight", productMap);
            }
            msg.put("podWeight", podAllWeight);
        }
        return msg;
    }

    public static final String GET_PRODUCT_WEIGHT = "SELECT P.podIndex AS PODINDEX, P.podName AS PODNAME, P.podType AS PODTYPE, P.location AS LOCATION, COALESCE(A.itemTotalAmount, 0) AS ITEMTOTALMOUNT, COALESCE(A.itemTotalWeight, 0) AS ITEMTOTALWEIGHT \n" +
            " FROM \n" +
            " (SELECT DISTINCT pod.POD_INDEX  AS podIndex, pod.NAME AS podName, podt.NAME AS podType, pod.PLACEMARK AS location \n" +
            " FROM MD_POD pod LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID \n" +
            " LEFT JOIN MD_PODTYPEPOSITION potp ON podt.ID = potp.PODTYPE_ID \n" +
            " WHERE 1 = 1 and POD_INDEX=?\n" +
            " ORDER BY podIndex,podType,location) P \n" +
            " LEFT JOIN \n" +
            " (SELECT pod.NAME AS podName, podt.NAME AS podType, pod.PLACEMARK AS location, COALESCE(SUM(su.AMOUNT * imd.WEIGHT), 0) AS itemTotalWeight, COALESCE(SUM(su.AMOUNT), 0) AS itemTotalAmount \n" +
            " FROM MD_POD pod \n" +
            " LEFT JOIN MD_PODTYPE podt ON pod.PODTYPE_ID = podt.ID \n" +
            " LEFT JOIN MD_STORAGELOCATION sl ON pod.ID = sl.POD_ID \n" +
            " LEFT JOIN INV_UNITLOAD ul ON sl.ID = ul.STORAGELOCATION_ID \n" +
            " LEFT JOIN INV_STOCKUNIT su ON ul.ID = su.UNITLOAD_ID \n" +
            " LEFT JOIN MD_ITEMDATA imd ON su.ITEMDATA_ID = imd.ID \n" +
            " WHERE su.AMOUNT <> 0 AND POD_INDEX=?\n" +
            " GROUP BY podName, podType, location) A \n" +
            " ON P.podName = A.podName WHERE PODINDEX=?";
    private Map<String, Object> getProductWeight(){
        Map<String, Object> reMap = new HashMap<String, Object>();
        List<Map> list = this.jdbcRepository.queryBySql(GET_PRODUCT_WEIGHT.toUpperCase()
                , this.pod.getRcsPodId(), this.pod.getRcsPodId(), this.pod.getRcsPodId());
        if (list==null || list.isEmpty()){
            return null;
        }
        Map map = list.get(0);
        reMap.put("productWeight", map.get("ITEMTOTALWEIGHT"));
        return reMap;
    }

    //子类型需要实现的方法
    public abstract void sendMessage2Rcs();//根据调度单 生成消息 发送给RCS

    public abstract void initOrder();//初始化调度单是要做的工作，比如派生新调度单，初始化起始点、终点 是否旋转等

    public abstract void process();//执行调度单要做的工作

    public abstract void finish();//完成时要做的工作

    public void updatePodTarget() {
        if(this.getPod()!=null && this.getEndAddr()!=null){
            this.getPod().addKV("ADDRCODEID_TAR",this.getEndAddr());
            Address address = this.getWareHouseManager().getAddressByAddressCodeId(this.getEndAddr(),this.getSectionId());
            this.getPod().addKV("XPOS_TAR",address.getxPosition());
            this.getPod().addKV("YPOS_TAR",address.getyPosition());
            this.getJdbcRepository().updateBusinessObject(this.getPod());
            /*if(address.getType() == AddressType.STORAGE){
                address.setNodeState(AddressStatus.RESERVED);
                this.getJdbcRepository().updateBusinessObject(address);
            }*/
        }
    }

    public void updatePodOutterAddr() {
        if(this.getPod()!=null && this.getWcsPath().getPodUpAddress()!=0L){
            Long podUp = this.getWcsPath().getPodUpAddress();
            Address address = this.getWareHouseManager().getAddressByAddressCodeId(podUp,this.getSectionId());
            AddressGroup addressGroup = address.getAddressGroup();
            if(addressGroup == null){
                return;
            }
            Address out = address.getGroupOutterAddr();
            while (out != null){
                out.setNodeState(AddressStatus.RESERVED);
                //this.getJdbcRepository().updateBusinessObject(out);
                out = out.getGroupOutterAddr();
            }
        }
    }

    //取消该调度单
    public void cancel(){
        logger.error("取消调度单 this.getOrderId() : "+this.getOrderId()+" orderError:"+ OrderErrorMessage.getMsg(this.getOrderError()));
        if(this.getPod()!=null
                && this.getPod().getLockedBy() == this.getRobot().getRobotId()){
            this.getPod().setLockedBy(0L);
            //TODO POD和在途POD的状态恢复
        }
        /*this.jdbcRepository.updateByKey("OrderManager.cancelRobotOrders",
                TripStatus.NOT_FINISHED,this.getOrderId(),robot.getRobotId());*/
        this.getRobot().setCurOrder(null);//当前
    }

    public abstract boolean isFinish();//判断调度单结束

    public abstract String getType();//获取调度单类型

    @Override
    public Object getId() {
        return this.orderId;
    }

    public static final String TABLE = "RCS_TRIP";

    @Override
    public String getTable() {
        return TABLE;
    }

    public static final String IDNAME = "ID";

    @Override
    public String getIdName() {
        return IDNAME;
    }

    public boolean carryPod(Robot robot, Pod pod) {
        return robot.getPod() != null && Objects.equals(robot.getPod(), pod);
    }

    public void add2Path(List<Long> sources, List<Long> pathNodes) {
        if (sources.size() != 0 && pathNodes.size() != 0
                && Objects.equals(sources.get(sources.size() - 1), pathNodes.get(0))) {
            //如果最后一条等于下一个路径的第一条 减去
            pathNodes.remove(0);
        }
        for (int i = 0; i < pathNodes.size(); i++) {
            Long integer = pathNodes.get(i);
            sources.add(integer);
        }
    }
    public List<Long> getHeavyPath(String wareHouseId, String sectionId, String srcAddr, String destAddr) {
        List<Long> path = this.webApiBusiness.getHeavyPath(wareHouseId, sectionId, Integer.parseInt(srcAddr), Integer.parseInt(destAddr));
        //检查经过的存储区是否包含POD 或者有pod目的地址
        //支持三层即可，就是外面两个节点
        String spath = WebApiBusiness.logPath(path,"");
        if(path.size() < 1){
            logger.error("路径非法,长度小于1");
            this.setOrderError(Order.ERROR_HEAVY_PATH);
            return path;
        }
        /*for (int i = 1; i< path.size(); i++) {
            Long aLong = path.get(i);
            Pod pod;
            if((pod = podOnStorageAddr(aLong,sectionId))!=null){
                logger.error("路径"+spath+"的存储位节点:"+aLong+"上有POD:"+pod.getPodName());
                this.createPodRunOrder(pod);
                this.setOrderError(Order.ERROR_POD_ONPATH);
            }
        }*/
        return path;
    }

    private void createPodRunOrder(Pod pod) {
        if(podOnTrip(pod)){
            logger.debug("Pod:"+pod.getPodName()+"已经存在调度任务!");
            return;
        }
        PodRunOrder order = new PodRunOrder();
        CommonUtils.genUselessInfo(order.getKv());
        order.addKV("SECTION_ID", pod.getSectionId())
                .addKV("WAREHOUSE_ID", pod.getWareHouseId())
                .addKV("TRIP_STATE", TripStatus.NEW)
                .addKV("ID", CommonUtils.genUUID())
                .addKV("VERSION", 0)
                .addKV("TRIP_TYPE", order.getType())
                .addKV("POD_ID", pod.getPodId());
        this.jdbcRepository.insertBusinessObject(order);
        logger.debug("为Pod:"+pod.getPodName()+"生成PodRun调度任务!");
    }
    public static final String POD_ON_TRIP = "SELECT 1 FROM RCS_TRIP WHERE POD_ID=? AND TRIP_STATE IN (?,?,?) LIMIT 1";
    private boolean podOnTrip(Pod pod) {
        List<Map> rows = this.jdbcRepository.queryBySql(POD_ON_TRIP,pod.getPodId()
                ,TripStatus.NEW, TripStatus.AVAILABLE, TripStatus.PROCESS);
        return rows!=null && rows.size()>0;
    }

    private Pod podOnStorageAddr(Long addrCode, String sectionId) {
        Address address = this.wareHouseManager.getAddressByAddressCodeId(addrCode,sectionId);
        Pod pod = this.podManager.getPodByAddress(addrCode, sectionId);
        //满足是存储区的格子，上面有pod，并且状态是OCCUPIED
        if( address.getType() == AddressType.STORAGE && pod != null
                && Objects.equals(address.getNodeState(), AddressStatus.OCCUPIED)){
            return pod;
        }
        return null;
    }

    public List<Long> getEmptyPath(String wareHouseId, String sectionId, String srcAddr, String destAddr) {
        checkNull(wareHouseId, sectionId, srcAddr, destAddr);
        return this.webApiBusiness.getEmptyPath(wareHouseId, sectionId, Integer.parseInt(srcAddr), Integer.parseInt(destAddr));
    }

    private void checkNull(String... strings) {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (string == null || "".equals(string)) {
                System.out.println(i + ":" + string);
                throw new RuntimeException(i + ":" + string + " 为空");
            }
        }
    }

    public abstract void setType(String type) ;

    public boolean isCanLockEndAddress(){
        Address dest = this.getWareHouseManager()
                .getAddressByAddressCodeId(this.getEndAddr(), this.getSectionId());
        if(dest == null){
            return false;
        }
        if (dest.getLockedBy()==this.getRobot().getRobotId() || dest.getLockedBy()==0){
            return true;
        }
        return false;
    }

    public boolean unlockEndAddress() {

        Address dest = this.getWareHouseManager().getAddressByAddressCodeId(this.getEndAddr(), this.getSectionId());
        if(dest == null){
            return false;
        }
        return dest.robotLock(this.getRobot().getRobotId(),false);
    }

    public boolean updateEndAddr2Database(String orderId, String endAddr){
        //trip表设置回存储区的终点
        try {
            logger.debug("准备更新rcs_trip表的endAddress：tripID="+orderId+" , endAddress="+endAddr);
            /*String sql = "UPDATE RCS_TRIP SET END_ADDRESS=? WHERE ID=?";
            int re = this.getJdbcRepository().updateBySql(sql, Integer.parseInt(endAddr), orderId);*/
            Order order = new EmptyRunOrder();//借用
            Map<String,Object> con = new HashMap<>();
            con.put("ID",orderId);
            order.setCon(con);
            //Map<String,Object> newValue = new HashMap<>();
            order.addKV("END_ADDRESS",Long.parseLong(endAddr));
            this.getJdbcRepository().updateBusinessObject(order);
        } catch (Exception e) {
            logger.error("更新rcs_trip表的endAddress失败！tripID="+orderId+" , endAddress="+endAddr,e);
            e.printStackTrace();
        }
        return true;
    }


    @Override
    public String toString() {
        return "Order{" +
                "wcsPath=" + wcsPath +
                ", orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", activedBy='" + activedBy + '\'' +
                ", send2RcsTime='" + send2RcsTime + '\'' +
                ", message='" + message + '\'' +
                ", endAddr=" + endAddr +
                ", orderError=" + orderError +
                ", flag=" + flag +
                ", pod=" + (pod==null?"":pod.getPodName()) +
                '}';
    }

    public abstract void reInitOrder();

    public void createPodRun(Pod outPod) {
        String podId = outPod.getPodId();
        String sectionId = outPod.getSectionId();
        String uuid = CommonUtils.genUUID();
        Map record = new HashMap();
        CommonUtils.genUselessInfo(record);
        record.put("ID",uuid);
        record.put("POD_ID",podId);
        record.put("ACTIVED_BY",this.getOrderId());
        record.put("TRIP_TYPE","PodRun");
        record.put("SECTION_ID",sectionId);
        record.put("TRIP_STATE",TripStatus.NEW);
        record.put("WAREHOUSE_ID",outPod.getWareHouseId());

        this.getJdbcRepository().insertRecord("RCS_TRIP",record);
    }


    public boolean checkInnerAddr(Address podAddr) {
        if(podAddr == null){
            return false;
        }
        Address out = podAddr.getGroupOutterAddr();
        return out != null;
    }

    public boolean lockEndAddr(){
        if(this.endAddr == null || this.endAddr == 0L){
            return false;
        }
        Address address = this.getWareHouseManager().getAddressByAddressCodeId(this.endAddr,this.getSectionId());
        return address.robotLock(this.getRobot().getRobotId(),true);
    }
}
