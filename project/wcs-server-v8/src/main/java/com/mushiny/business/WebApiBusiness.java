package com.mushiny.business;

/**
 * Created by Tank.li on 2017/7/26.
 */

import com.mushiny.beans.Pod;
import com.mushiny.beans.WorkStation;
import com.mushiny.beans.order.Order;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
@Component
public class WebApiBusiness {
    private final static Logger logger = LoggerFactory.getLogger(WebApiBusiness.class);
    private final RestTemplateBuilder builder;
    @Value("${mushiny.webapi.path-planning.emptyPath}")
    private String path_empty_url;
    @Value("${mushiny.webapi.path-planning.heavyPath}")
    private String path_heavy_url;
    @Value("${mushiny.webapi.callStowPods_url}")
    private String callStowPods_url;
    @Value("${mushiny.webapi.path-planning.updateNewCost}")
    private String update_newCost_url;
    @Value("${mushiny.webapi.path-planning.addHeavyCost}")
    private String add_heavyCost_url;

    @Autowired
    public WebApiBusiness(RestTemplateBuilder builder) {
        this.builder = builder;
    }

    @Value("${mushiny.webapi.path-planning.turning}")
    private String path_podTurning_url;

    /**
     * 获取POD旋转度数
     * */
    public int getPodTurning(String face, int sourceToward ,int targetToward){
        RestTemplate restTemplate = builder.build();
        /*String restUrl = "http://192.168.1.10:12003/path-planning/pod/turning?" +
                "face={face}&sourceToward={sourceToward}&targetToward={targetToward}";*/
        return restTemplate.getForObject(path_podTurning_url,Integer.class,face,sourceToward,targetToward);
    }

    /**
     * @deprecated
     * @param pod
     * @param workStation
     * @param needFace
     * @return
     */
    public static int getPodTurning2(Pod pod, WorkStation workStation,String needFace){
        int podFace = pod.getDirect();
        int wsFace = workStation.getFace();
        logger.debug("计算旋转角度: podFace:"+podFace+" wsFace:"+wsFace+" needFace:"+needFace);
        return CommonUtils.aFaceToward(podFace,wsFace,needFace);
    }

    public static void main(String[] args) {
        /*WorkStation workStation = new WorkStation();
        workStation.setFace(180);
        Pod pod = new Pod();
        pod.setDirect(270);
        String needFace = "D";

        System.out.println("计算A面角度:"+ getPodTurning2(pod,workStation,needFace));*/
        System.out.println(JsonUtils.json2List("",Long.class));
    }

    public List<Long> getEmptyPath(String warehouseId,String sectionId, Integer sourceVertex ,Integer targetVertex){
        RestTemplate restTemplate = builder.build();
        String res = null;
        try {
            logger.debug("emptyPath:"+warehouseId+" " +sectionId+" " +sourceVertex+" " +targetVertex);
            res = restTemplate.getForObject(this.path_empty_url,
                    String.class, warehouseId,sectionId,sourceVertex,targetVertex);
        } catch (RestClientException e) {
            logger.error("空车车路径获取失败! sourceVertex:"+sourceVertex+" targetVertex:"+targetVertex,e);
        }

        List<Long> path = JsonUtils.json2List(res,Long.class);
        logPath(path,"空车");
        return path;
    }

    public List<Long> getHeavyPath(String warehouseId,String sectionId, Integer sourceVertex ,Integer targetVertex){
        RestTemplate restTemplate = builder.build();
        String res = null;
        try {
            logger.debug("heavyPath:"+warehouseId+" " +sectionId+" " +sourceVertex+" " +targetVertex);
            res = restTemplate.getForObject(this.path_heavy_url,
                    String.class, warehouseId,sectionId,sourceVertex,targetVertex);
        } catch (Exception e) {
            logger.error("重车车路径获取失败! sourceVertex:"+sourceVertex+" targetVertex:"+targetVertex,e);
        }
        List<Long> path = JsonUtils.json2List(res,Long.class);
        logPath(path,"重车");
        return path;
        /*List<Integer> path = new ArrayList<>();
        path.add(11);
        path.add(12);
        path.add(13);
        path.add(14);
        path.add(15);
        return path;*/
    }

    public void callStowPods(String logicStationId) {
        RestTemplate restTemplate = builder.build();
        String res = null;
        try {
            logger.debug("logicStationId:"+logicStationId+" this.callStowPods_url:"+this.callStowPods_url);
            res = restTemplate.getForObject(this.callStowPods_url, String.class, logicStationId);
            logger.debug("callStowPods_url返回结果:"+res);
        } catch (Exception e) {
            logger.error(" 呼叫StowPod失败：logicStationId:"+logicStationId,e);
        }

    }

    public String updateNewCost(String warehouseId,String sectionId, String addressList, String newCost){
        RestTemplate restTemplate = builder.build();
        String res = null;
        try {
            logger.debug("updateNewCost:"+warehouseId+" " +sectionId+" " +addressList+" " +newCost);
            res = restTemplate.getForObject(this.update_newCost_url,
                    String.class, warehouseId,sectionId,addressList,newCost);
        } catch (RestClientException e) {
            logger.error("更新NEW-COST失败! addressList:"+addressList+" newCost:"+newCost,e);
        }
        if ("1".equals(res)){
            logger.debug("path-planning更新NEW-COST成功! addressList:"+addressList+" newCost:"+newCost);
        }else{
            logger.debug("path-planning更新NEW-COST失败! addressList:"+addressList+" newCost:"+newCost);
        }

        return res;
    }

    public String addHeavyCost(String warehouseId,String sectionId, String addressList, String heavyCost){
        RestTemplate restTemplate = builder.build();
        String res = null;
        try {
            logger.debug("addHeavyCost:"+warehouseId+" " +sectionId+" " +addressList+" " +heavyCost);
            res = restTemplate.getForObject(this.add_heavyCost_url,
                    String.class, warehouseId,sectionId,addressList,heavyCost);
        } catch (RestClientException e) {
            logger.error("addHeavyCost失败! addressList:"+addressList+" heavyCost:"+heavyCost,e);
        }
        if ("1".equals(res)){
            logger.debug("path-planning:addHeavyCost成功! addressList:"+addressList+" heavyCost:"+heavyCost);
        }else{
            logger.debug("path-planning:addHeavyCost失败! addressList:"+addressList+" heavyCost:"+heavyCost);
        }
        return res;
    }

    public static String logPath(List<Long> pod2Target,String type) {
        StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < pod2Target.size(); i++) {
            Long integer = pod2Target.get(i);
            if (i < pod2Target.size() - 1) {
                stringBuilder.append(integer).append(",");
            } else {
                stringBuilder.append(integer);
            }
        }
        stringBuilder.append("]");
        logger.debug(type + "路径是:" + stringBuilder.toString());
        return stringBuilder.toString();
    }
}