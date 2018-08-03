package mq.filter;

import com.mushiny.rcs.global.AGVConfig;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import com.rabbitmq.client.Channel;
import mq.SubjectManager;
import org.apache.commons.lang.SerializationUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * 过滤-->下发的路径小车是否在充电中，若在结束充电  执行任务
 *
 * Created by Laptop-6 on 2017/9/5.
 *  mingchun.mu@mushiny.com
 */
public class PathFilter implements IPathFilter {

    private static Logger LOG = Logger.getLogger(PathFilter.class.getName());

    private IPathFilter pathFilter;

    private Channel channel;
    private String  COM_EXCHANGE;

    public PathFilter(IPathFilter pathFilter, Channel channel, String COM_EXCHANGE) {
        this.pathFilter = pathFilter;
        this.channel = channel;
        this.COM_EXCHANGE = COM_EXCHANGE;
    }

    @Override
    public void doFilter(Map<String, Object> message) {

        try {
            if(message != null){
                long robotID = Long.parseLong(String.valueOf(message.get("robotID")));
                List<Long> seriesPath = ((List<Long>)message.get("seriesPath"));
                KivaAGV curAGV = AGVManager.getInstance().getAGVByID(robotID);
                if(curAGV != null){
                if(AGVConfig.AGV_STATUS_POWERING == curAGV.getAGVStatus()){
                    if(seriesPath != null && seriesPath.size() > 0){
                        Map<String, Object> chargingPileMap = new HashMap<>();
                        chargingPileMap.put("robotID", robotID);
                        chargingPileMap.put("chargingAddressCodeID", seriesPath.get(0));
                        chargingPileMap.put("batterManufacturerNumber", AGVManager.getInstance().getAGVByID(robotID).getBatterManufacturerNumber());
                        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_CHARGING_PILE_END_CHARGE, null,
                                SerializationUtils.serialize((Serializable) chargingPileMap));

                        Thread.sleep(3000);//暂停先让充电桩结束充电

                        //当AGV在充电状态时，将AGV状态置为等待任务
                        curAGV.setAGVStatus(1); // 注释原因，当充电时，第一个旋转动作不用下发
                    }
                }
            }

            }
            if(pathFilter != null){
                doFilter(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public IPathFilter getPathFilter() {
        return pathFilter;
    }

    public void setPathFilter(IPathFilter pathFilter) {
        this.pathFilter = pathFilter;
    }
}
