package com.mushiny.service.impl;

import com.mushiny.beans.*;
import com.mushiny.beans.enums.PodStatus;
import com.mushiny.beans.enums.TripStatus;
import com.mushiny.beans.order.EnroutePod;
import com.mushiny.business.PodManager;
import com.mushiny.business.PodComparator;
import com.mushiny.business.WareHouseManager;
import com.mushiny.comm.CommonUtils;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * POD相关对外提供的服务
 * 1、热度算法实现
 * 2、工作站呼叫POD算法
 * 3、....
 * Created by Tank.li on 2017/6/23.
 */
@Service
public class PodServiceImpl implements com.mushiny.service.PodService {

    private final static Logger logger = LoggerFactory.getLogger(PodServiceImpl.class);

    @Autowired
    private JdbcRepository jdbcRepository;
    @Autowired
    private Environment env;
    @Autowired
    private PodManager podManager;
    @Autowired
    private WareHouseManager wareHouseManager;

    @Override
    //@Transactional
    @Scheduled(fixedDelay =20000l)
    public void restorePodStatus() {
        if(!this.podManager.isFinisHotCompute()){
            return;
        }
        logger.debug("将Pod状态复位,解决Reserved==>Available的问题");
        Map<String, Pod> curEnroutePods = this.currentEnroutePods();
        this.clearEnroutePods(curEnroutePods);
        this.clearReservedPods();
        logger.debug("将Pod状态复位结束!");
    }
    public static final String POD_RESERVED = "UPDATE MD_POD SET MD_POD.STATE=? \n" +
            "WHERE ID NOT IN (SELECT POD_ID FROM RCS_TRIP WHERE RCS_TRIP.TRIP_STATE<>? ) \n" +
            "AND MD_POD.STATE=?";
    public static final String PODS_ERROR_RESERVED = "SELECT ID FROM MD_POD "
            +"WHERE ID NOT IN (SELECT POD_ID FROM RCS_TRIP WHERE RCS_TRIP.TRIP_STATE<>? ) \n" +
            "AND MD_POD.STATE=? AND PLACEMARK <> 0";
    private void clearReservedPods() {
        //this.jdbcRepository.updateBySql(POD_RESERVED, PodStatus.AVAILABLE,TripStatus.FINISHED,PodStatus.RESERVED);
        //完成了 但还属于Reserved的POD
        List<Map> rows = this.jdbcRepository.queryBySql(PODS_ERROR_RESERVED,TripStatus.FINISHED,PodStatus.RESERVED);
        for (int i = 0; i < rows.size(); i++) {
            Map row = rows.get(i);
            Pod pod = new Pod();
            pod.setPodId(CommonUtils.parseString("ID",row));
            pod.addKV("STATE",PodStatus.AVAILABLE);
            this.jdbcRepository.updateBusinessObject(pod);
        }
    }

    public static final String POD_IN_TRIP = "SELECT 1 FROM RCS_TRIP WHERE POD_ID = ? AND TRIP_STATE <> ? LIMIT 1";
    private void clearEnroutePods(Map<String, Pod> curEnroutePods) {
        Iterator<Pod> iter = curEnroutePods.values().iterator();
        while (iter.hasNext()) {
            Pod pod = iter.next();
            if(pod==null){
                continue;
            }
            //没有在运行的POD
            List rows = this.jdbcRepository.queryBySql(POD_IN_TRIP,pod.getPodId(), TripStatus.FINISHED);
            if(rows == null || rows.size()==0) {
               unBindEnroutePod("IB_ENROUTEPOD",pod.getPodId());
               unBindEnroutePod("OB_ENROUTEPOD",pod.getPodId());
               unBindEnroutePod("PQA_ENROUTEPOD",pod.getPodId());
            }
        }
    }

    public void unBindEnroutePod(String tableName, String podId) {
        EnroutePod enroutePod = new EnroutePod();
        enroutePod.setTable(tableName);
        Map<String,Object> con = new HashMap();
        con.put("POD_ID", podId);
        enroutePod.setDelCon(con);
        this.jdbcRepository.deleteBo(enroutePod);
        logger.info(tableName+":表记录删除成功!:POD_ID=" + podId);
    }

    public static final String SQL_IB = "SELECT IB_ENROUTEPOD.POD_ID,MD_POD.SECTION_ID FROM IB_ENROUTEPOD " +
            "LEFT JOIN MD_POD ON IB_ENROUTEPOD.POD_ID=MD_POD.ID";
    public static final String SQL_OB = "SELECT OB_ENROUTEPOD.POD_ID,MD_POD.SECTION_ID FROM OB_ENROUTEPOD " +
            "LEFT JOIN MD_POD ON OB_ENROUTEPOD.POD_ID=MD_POD.ID";
    public static final String SQL_PQA = "SELECT PQA_ENROUTEPOD.POD_ID,MD_POD.SECTION_ID FROM PQA_ENROUTEPOD " +
            "LEFT JOIN MD_POD ON PQA_ENROUTEPOD.POD_ID=MD_POD.ID";
    private Map<String, Pod> currentEnroutePods() {
        List<Map> ibPod = this.jdbcRepository.queryBySql(SQL_IB);
        List<Map> obPod = this.jdbcRepository.queryBySql(SQL_OB);
        List<Map> pqaPod = this.jdbcRepository.queryBySql(SQL_PQA);

        Map<String, Pod> pods = new HashMap<>();
        for (int i = 0; i < ibPod.size(); i++) {
            Map map = ibPod.get(i);
            String pod_id = CommonUtils.parseString("POD_ID",map);
            String sectionId = CommonUtils.parseString("SECTION_ID",map);
            pods.put(pod_id, this.podManager.getPodById(pod_id,sectionId));
        }
        for (int i = 0; i < obPod.size(); i++) {
            Map map = obPod.get(i);
            String pod_id = CommonUtils.parseString("POD_ID",map);
            String sectionId = CommonUtils.parseString("SECTION_ID",map);
            pods.put(pod_id, this.podManager.getPodById(pod_id,sectionId));
        }
        for (int i = 0; i < pqaPod.size(); i++) {
            Map map = pqaPod.get(i);
            String pod_id = CommonUtils.parseString("POD_ID",map);
            String sectionId = CommonUtils.parseString("SECTION_ID",map);
            pods.put(pod_id, this.podManager.getPodById(pod_id,sectionId));
        }

        return pods;
    }

    /**
     * 工作站呼叫POD上架 TODO
     * 1、定时任务循环分配好矩阵 即工位对应上架pod的矩阵
     * 2、符合条件时（如工位pod任务完成或手动按钮），触发呼叫pod的方法
     * 3、从矩阵依次找最合适的POD，如果没被锁定就返回该POD （由调用方锁定）
     *
     * 分配POD的计算规则如下:
     * 1)根据阀值(空闲体积) 计算全仓库所有的可用POD
     * 2）如果小于阀值(某个数量)则发消息到主题，告知存储空间不足，需增加POD
     * 3）根据可用POD到工位的相对距离-K1*该POD中工位所需Bin位类型的空闲体积之和-K2*该POD中工位所需Bin位类型的空闲SKU种类之和
     * 4）相对距离是POD到工位的距离-POD到最近工位的距离
     * 5）第三条计算结果最小的最佳，可以为负值
     * @param workStationId
     * @return
     */
    @Override
    public String callPod(String workStationId){
        WorkStation workStation = this.wareHouseManager.findWorkStation(workStationId);
        //workStation.getAssignedPos
        return  null;
    }

    /**
     * 运算商品热度，当前时间段内 由定时器调用 TODO  历史订单法暂时不用
     */
    @Override
    @Transactional
    public void hotCreateHistory(){
        /*
        分开处理：
        1、从customshipmentposition取当前时间段内所有的shipmentline总数 Y
        2、从pickingorderposition里取pod和shipmentline的关系，取一条即可，比如一个shipmentline可能拆分两个pod，只算一次
        3、从podID里找pod上的商品，MD_POD===>找name===》以此name开头的md_storagelocation（多个）====>得到所有的INV_unitload
              ===>INV_STOCKUNIT(多个)找所有的记录数 X
        4、从步骤3的记录数除于步骤1的记录

        V = （1-a）V(上一步) + a*X/Y
        * */
        String time = env.getProperty("com.mushiny.hotservice.time");
        Long startTime = System.currentTimeMillis() - (Integer.parseInt(time)*60*1000);
        Timestamp timestamp = new Timestamp(startTime);

        List params = new ArrayList();
        params.add(timestamp);
        List res = this.jdbcRepository.queryByKey("HOT.SERVICE.QUERYALLSHIPMENTLINE",params);
        Integer count = 0;
        if(res!=null&&res.size()>0){
            Map data = (Map) res.get(0);
            count = (Integer) data.get("COUNTSHIPMENTS");
        }
        logger.debug("该周期ShipmentLine条数是:"+count);
        if(count==0){
            return;//不用执行
        }
        String constants = env.getProperty("com.mushiny.hotservice.const");
        double apar = Double.parseDouble(constants);
        /*
        SELECT ITEMCOUNT.*,HOT.ITEMSELLINGDEGREE
        FROM ( SELECT COUNT(ITEMDATA_ID) AS COUNTITEM,ITEMDATA_ID FROM OB_PICKINGORDERPOSITION T1
        WHERE STATE<600 GROUP BY ITEMDATA_ID ) AS ITEMCOUNT
        LEFT JOIN MD_ITEMDATA_HOT HOT ON ITEMCOUNT.ITEMDATA_ID=HOT.ID
        * */
        List pickingOrderPositions = this.jdbcRepository.queryByKey("HOT.SERVICE.QUERYALLPICKINGORDERPOSITION",params);
        if (pickingOrderPositions == null || pickingOrderPositions.size() <= 0) {
            logger.error("没有需要计算热度的商品");
            return;
        }
        //先转移热度表记录到历史表 INSERT INTO MD_ITEMDATA_HOTHISTORY SELECT  t.*, 1 FROM MD_ITEMDATA_HOT t
        StringBuilder stringBuilder = new StringBuilder()
                .append("INSERT INTO MD_ITEMDATA_HOTHISTORY SELECT  t.*,")
                .append(CommonUtils.now2String("yyyyMMddHHmmss"))//版本号
                .append(" FROM MD_ITEMDATA_HOT t");
        this.jdbcRepository.updateBySql(stringBuilder.toString());

        Map delCon = new HashMap();
        delCon.put("1",1);//全删
        this.jdbcRepository.deleteRecords("MD_ITEMDATA_HOT",delCon);
        logger.debug("热度表信息删除成功!");
        for (int i = 0; i < pickingOrderPositions.size(); i++) {
            Map map = (Map) pickingOrderPositions.get(i);
            String id = CommonUtils.genUUID();//(String) map.get("ITEMDATA_ID");
            String degree = (String) map.get("ITEMSELLINGDEGREE");
            String countItem = (String) map.get("COUNTITEM");
            String wareHouseId = (String) map.get("WAREHOUSE_ID");
            String clientId = (String) map.get("CLIENT_ID");
            Integer ci = Integer.parseInt(countItem);
            Double dg = degree==null?
                    0.0:Double.parseDouble(degree);
            //计算热度
            Double hot = (1-apar)*dg + apar*(ci/count);
            logger.debug("旧的热度:"+dg+"===>新的热度:"+hot);
            Map newRecord = new HashMap();
            newRecord.put("ID",id);
            newRecord.put("CREATED_DATE",new Timestamp(System.currentTimeMillis()));
            newRecord.put("CREATED_BY","SYSTEM");
            newRecord.put("WAREHOUSE_ID",wareHouseId);
            newRecord.put("CLIENT_ID",clientId);
            //热度值
            newRecord.put("ITEMSELLINGDEGREE",dg+"");
            this.jdbcRepository.insertRecord("MD_ITEMDATA_HOT",newRecord);
        }
        logger.debug("当前共有"+pickingOrderPositions.size()+"种商品产生了热度");
    }


    /*
    * 1、当前有多少个shipmentLine 每个shipmentline一种商品
     * 2、计算该商品有多少个除以拥有该商品的POD数(全库的pod)，这是商品的热度
     * 3、每个pod的热度是该pod上热度商品的加法
     * 4、以pod排序，跟地址热度一起排队，获取该pod针对的地址是理想地址，取该位置的得分
     * 5、空的货架放在排队的最后，这样他的idea位置是空闲的位置，取该位置的得分
     * 5、然后找所有的空的位置计算得分： 空的位置到pod所在工位的距离 + 可调参数*|位置到最近工位的距离-pod理想位置|
     * 6、如果可调参数变大，则越靠近理想位置的越合适
     * 7、如果可调参数为0，则应该去最近的那个空pod

      方法执行:
        1)系统启动时,WareHouseManager初始化加载所有地图，节点和工作站，并计算好每个节点位置的距离热度（固定值）
        2）定时任务每隔执行hotCreateWareHouse 当前订单法，将每个section的pod与目标位置(多个)计算好
        3）给WCS提供接口computeTargetPosition，传PODID，返回位置ID，在返回前，会查询该位置节点是否被占用，
            如果占用这递归找下一个合适的位置点
    * */
    @Override
    @Scheduled(fixedDelay =1000000l,initialDelay=10000L)
    public void hotCreateWareHouse(){
        if(this.wareHouseManager.getWareHouseMap() == null || this.wareHouseManager.getWareHouseMap().isEmpty()){
            logger.debug("仓库还未初始化,下次定时任务再计算热度！");
            return;
        }
        /* 生成热度的语句
            INSERT INTO MD_ITEMDATA_HOT
            SELECT TABCOUNTITEM.ITEMDATA_ID,NOW(),'SYSTEM',COUNTITEM/COUNTPODID,'SYSTEM','DEFAULT'
            FROM (SELECT COUNT(ITEMDATA_ID) AS COUNTITEM,ITEMDATA_ID FROM `OB_CUSTOMERSHIPMENTPOSITION` CSP
            GROUP BY ITEMDATA_ID) TABCOUNTITEM,
            (SELECT COUNT(POD.ID) COUNTPODID,ITEMDATA_ID
            FROM MD_POD POD,MD_STORAGELOCATION LOCATION,INV_UNITLOAD UNITLOAD,INV_STOCKUNIT SKU
            WHERE POD.ID = LOCATION.POD_ID AND UNITLOAD.STORAGELOCATION_ID = LOCATION.ID AND SKU.UNITLOAD_ID = UNITLOAD.ID
            GROUP BY ITEMDATA_ID) TABCOUNTPOD WHERE TABCOUNTPOD.ITEMDATA_ID=TABCOUNTITEM.ITEMDATA_ID;*/

        //1、先转移热度表记录到历史表 INSERT INTO MD_ITEMDATA_HOTHISTORY SELECT  t.*, 1 FROM MD_ITEMDATA_HOT t
        logger.debug("开始执行热度算法.....");
        //2018-04-18 注释开始 不生成数据库记录
        /*StringBuilder stringBuilder = new StringBuilder()
                .append("INSERT INTO MD_ITEMDATA_HOTHISTORY SELECT  t.*,")
                .append(CommonUtils.now2String("yyyyMMddHHmmss"))//版本号
                .append(" FROM MD_ITEMDATA_HOT t");
        this.jdbcRepository.updateBySql(stringBuilder.toString());
        logger.debug("全库热度表迁移到历史表成功");

        //2、删除热度表
        Map delCon = new HashMap();
        delCon.put("1",1);//全删
        //delCon.put("section_Id",section.getSection_id());//全删
        this.jdbcRepository.deleteRecords("WCS_ITEMDATA_HOT",delCon);
        logger.debug("全库热度表信息删除成功!");

        //3、生成商品热度表记录，当前订单信息,全库运算 不分section
        this.jdbcRepository.updateByKey("HOT.SERVICE.GENCURRENTITEMHOT");*/
        //2018-04-18 注释结束 不生成数据库记录
        //logger.debug("全库商品热度生成完成!");
        //因为商品热度生成了 就可以生成所有pot的热度，以热度表为驱动去查找，剩余的pod热度默认为0(不参与排序计算)
        Iterator<WareHouse> iter = this.wareHouseManager.getWareHouseMap().values().iterator();
        List<Map> itemHots = this.podManager.queryItemHots();
        Map data = new HashMap();
        for (int i = 0; i < itemHots.size(); i++) {
            Map map = itemHots.get(i);
            data.put(map.get("ITEMDATA_ID"),map.get("ITEMPERPOD"));
        }
        while (iter.hasNext()) {
            WareHouse next = iter.next();
            Iterator<Section> iterator = next.sectionMap.values().iterator();
            while (iterator.hasNext()) {
                Section section = iterator.next();
                logger.debug("执行热度算法,Section==>"+section);
                hotCreateCurrent(section,data);
                logger.debug("Section:"+section+"执行热度算法结束!");
            }
        }
        this.podManager.setFinisHotCompute(true);
        logger.error("热度算法结束: 全库POD目标返回地址生成完成!");
    }

    /*


     * 当前订单法 十秒执行一次
     */
    //@Transactional
    //@Scheduled(fixedDelay =10000000l)
    @Override
    public void hotCreateCurrent(Section section, Map itemHots){
        long start = System.currentTimeMillis();
        logger.debug("当前订单法生成Section热度.....SectionId="+section.getSection_id());
        if(section.addressList.size()==0){
            logger.debug("仓库section:"+section.getSection_id()+"还未初始化,下次定时任务再计算热度！");
            return;
        }
        //logger.debug("动态获取当前热度，Section："+section.getSection_id());

        List<Pod> podList = this.podManager.getPodListBySection(section.getSection_id());
        if(podList == null){
            logger.error("Section:"+section.getSection_id()+" 没有POD存在,热度计算完成!");
            return;
        }
        //把该section下的pod热度值清空
        this.podManager.clearSectionPod(section);
        /*
            算法重新计算到以下三个值
            pod.setzHot(?);
            pod.setIdeaTarget(?);
            pod.setFavorAddrs(?);
        * */
        //TODO 这条语句要获取到所有需要热度归位的POD 分别计算每个section的pod
        //List<Map> list = this.jdbcRepository.queryByKey("HOT.SERVICE.QUERYALLPOTSHOT",section.getSection_id());
        List<Map> list = this.jdbcRepository.queryByKey("HOT.SERVICE.QUERYALLPOTSITEMS",section.getSection_id());
        for (int i = 0; i < list.size(); i++) {
            Map row =  list.get(i);
            String podId = (String) row.get("POD_ID");
            Pod pod = this.podManager.getPod(section.getSection_id(),podId);
            if(pod==null){
                continue;
            }
            /*int x = CommonUtils.parseInteger("XPOS",row);//(int) row.get("XPOS");
            int y = CommonUtils.parseInteger("YPOS",row);//(int) row.get("YPOS");
            pod.setAddress(this.wareHouseManager.getAddressByXY(x,y,section));//忘了有什么用*/
            double hot = CommonUtils.parseDouble(row.get("ITEMDATA_ID")+"",itemHots); //Double.parseDouble((String) row.get("ITEMSELLINGDEGREE"))
            if(hot==0.0){
                continue;
            }
            double newHot = hot + pod.getHot();
            logger.debug("Pod:"+podId+"热度从 "+pod.getHot()+" 变成:" + newHot);
            pod.setHot(newHot);//todo
        }
        //allPots 是目前Section要排序的POD列表 其实是当前Section的所有Pod
        List<Pod> allPots = new ArrayList(podList);//重新排序的集合
        //所有空位的存储格子
        List<Address> addresses = this.wareHouseManager.listUnOccupiedAddress(section);
        //排序
        Collections.sort(allPots,new PodComparator());
        //WareHouseManager.getInstance().
        List<Address> addressList = section.addressList;
        //podManager.clearPod();//原先缓存的都清除掉
        for (int i = 0; i < allPots.size(); i++) {
            Pod pod = allPots.get(i);
            Address ideaTarget = null;
            if(i >= addressList.size()){
                logger.error("地址数量少于pod数量的判断，没有意义，只是为了实现代码");
                ideaTarget = addressList.get(addressList.size()-1);//最后一个
            }else{
                ideaTarget = addressList.get(i);
            }
            pod.setzHot(ideaTarget.getHot());//兑换
            pod.setIdeaTarget(ideaTarget);//获取理想位置
            //每个pod都会找到一个所有空位置排序后的列表
            this.podManager.initialPotFavarAddress(pod,addresses,section);
        }
        /* //计算POD节点的目标位置
        podManager.cacheAll(section);*/
        logger.debug("使用<当前订单法>生成SectionId="+section.getSection_id()+"的地图热度结束, 耗时MS:"+(System.currentTimeMillis()-start));
    }

    /**
     * 通过当前pod计算需要返回的目标地址ID
     * @param podId
     * @param sectionId
     * @return
     */
    @Override
    public String computeTargetAddress(String podId, String sectionId){
        return podManager.computeTargetAddress(podId,sectionId);
    }
    /**
     * 地址是否被锁定
     * @param address
     * @return
     */
    @Override
    public boolean isAvaliable(Address address) {
        //TODO 再从数据库查一遍 ？
        List params = new ArrayList();
        params.add(address.getId());
        List rows = this.jdbcRepository.queryByKey("HOT.SERVICE.ADDRESS",params);
        if (rows==null || rows.size()==0)
            return false;
        Map row = (Map) rows.get(0);
        return row.get("BLOCKED")!=null&&!(Boolean) row.get("BLOCKED");
    }


}
