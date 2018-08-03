package com.mushiny.business;

import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.MediaConfig;
import com.mushiny.jdbc.repositories.JdbcRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/19.
 */
//@Component
//@org.springframework.core.annotation.Order(value = 1)
public class MediaInit implements CommandLineRunner {
    private final static Logger logger = LoggerFactory.getLogger(MediaInit.class);

    private static MediaConfig mediaConfig = new MediaConfig();
    @Autowired
    private JdbcRepository jdbcRepository;

    private String initPods(){
        //MediaConfig mediaConfig = new MediaConfig();
        String podsStr = mediaConfig.getProperties().getProperty("pods");
        logger.debug("读取mediaPod配置文件：pods="+podsStr);
        if (!CommonUtils.isEmpty(podsStr)){
            String[] pod_addrArr = podsStr.split(",");
            logger.debug("开始更新POD，配置中共"+pod_addrArr.length+"个POD");
            for (int i = 0; i < pod_addrArr.length; i++) {
                String pod_addr = pod_addrArr[i];
                String[] podAddr = pod_addr.split("-");
                String podIndex = podAddr[0];
                String addr = podAddr[1];
                List parmsList = new ArrayList();
                parmsList.add(addr);            //placemark
                parmsList.add(podAddr[2]);              //x
                parmsList.add(podAddr[3]);              //y
                parmsList.add("Available");     //state
                parmsList.add(0);               //toward
                parmsList.add(podIndex);        //pod_index
                logger.debug("media演示场地pod地址初始化：podIndex="+podIndex+", paramsList="+ parmsList);
                int num = jdbcRepository.updateByKey("MEDIA.UpdatePodAddrByPodIndex", parmsList);
            }
        }
        return podsStr;
    }

    private void clearRobots(){
        Map delMap = new HashMap();
        delMap.put("1","1");
        int delNum = jdbcRepository.deleteRecords("WCS_ROBOT", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeleteRobots");
        logger.debug("media演示场地，清除所有小车共："+delNum);
    }

    private void clearTrips(){
        Map delMap = new HashMap();
        delMap.put("1", "1");
        int delNumTrips = jdbcRepository.deleteRecords("RCS_TRIP", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeleteTrips");
        logger.debug("media演示场地，清除所有调度单共："+delNumTrips);
        int delNumTripPositions = jdbcRepository.deleteRecords("RCS_TRIPPOSITION", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeleteTripPositions");
        logger.debug("media演示场地，清除所有调度单明细共："+delNumTripPositions);
    }

    private void clearEnroutepods(){
        Map delMap = new HashMap();
        delMap.put("1", "1");
        int delNumIB = jdbcRepository.deleteRecords("IB_ENROUTEPOD", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeleteIB");
        logger.debug("media演示场地，清除IB在途pod共："+delNumIB);
        int delNumOB = jdbcRepository.deleteRecords("OB_ENROUTEPOD", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeleteOB");
        logger.debug("media演示场地，清除OB在途pod共："+delNumOB);
        int delNumPQA = jdbcRepository.deleteRecords("PQA_ENROUTEPOD", delMap);
        //jdbcRepository.queryByKey("MEDIA.DeletePQA");
        logger.debug("media演示场地，清除PQA在途pod共："+delNumPQA);
    }

    /*public static void main(String[] args) {
        System.out.println(initPods());
    }*/

    @Override
    public void run(String... strings) throws Exception {
        logger.debug("开始初始化Media演示地图...");
        clearTrips();
        clearRobots();
        clearEnroutepods();
        initPods();
        logger.debug("初始化Media演示地图结束！");
    }
}
