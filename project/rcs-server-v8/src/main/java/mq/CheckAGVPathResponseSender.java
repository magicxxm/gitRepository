package mq;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.RCS2RobotPathMessage;
import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mingchun.mu.util.ExceptionUtil;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import com.mushiny.rcs.wcs.WCSChargeSeriesPath;
import com.mushiny.rcs.wcs.WCSScanPodIdPath;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Laptop-6 on 2017/11/11.
 */
public class CheckAGVPathResponseSender extends MQPublisher implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(CheckAGVPathResponseSender.class.getName());
    public static CheckAGVPathResponseSender instance = null;
    private CheckAGVPathResponseSender() {
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new CheckAGVPathResponseSender();
            new Thread(instance).start();
        }
    }
    public static CheckAGVPathResponseSender getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }


    private LinkedBlockingDeque<Map<String, Object>> blockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                Map<String, Object> message = blockingDeque.take();
                if(message != null){
                    checkAGVPathResponse(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("获取agv信息出错：\n"+ ExceptionUtil.getMessage(e));
            }
        }
    }

    private void checkAGVPathResponse(Map<String, Object> message) throws Exception{
        if(message.get("seriesPath") != null){
            Map<String, Object> resMap = new HashMap<>();
            StringBuilder stringBuilder = new StringBuilder();
            List<Long> testPath = (List<Long>)message.get("seriesPath");
            LinkedList<Long> addressCodeIDList = new LinkedList<>(testPath);
            long upPodAddressCodeID = Long.parseLong(String.valueOf(message.get("podUpAddress")));
            long downPodAddressCodeID = Long.parseLong(String.valueOf(message.get("podDownAddress")));
            boolean isRotate = false;
            int rotateTheta = Integer.parseInt(String.valueOf(message.get("rotateTheta")));
            int podCodeID = Integer.parseInt(String.valueOf(message.get("podCodeID")));
            int flag = Integer.parseInt(String.valueOf(message.get("flag")));
            WCSSeriesPath wcsSeriesPath = null;
            if(flag == 1){
                wcsSeriesPath = new WCSSeriesPath(upPodAddressCodeID, downPodAddressCodeID, isRotate, rotateTheta, addressCodeIDList);
                wcsSeriesPath.setPodCodeID(podCodeID);// 设置路径中小车举升或下降的podID
            }else if(flag == 2){
                wcsSeriesPath = new WCSChargeSeriesPath(addressCodeIDList.getLast(), rotateTheta, addressCodeIDList);
                wcsSeriesPath.setPodCodeID(podCodeID);
            }else if(flag == 3){
                wcsSeriesPath = new WCSScanPodIdPath(addressCodeIDList);
                wcsSeriesPath.setPodCodeID(podCodeID);
            }
            if(wcsSeriesPath != null && wcsSeriesPath.checkWCSSeriesPath()){
                stringBuilder.append("总路径情况:");
                stringBuilder.append(wcsSeriesPath);
                stringBuilder.append("\n");
                stringBuilder.append("---------------------------------------------------------------------------");
                stringBuilder.append("\n");
                for(SeriesPath seriesPath : wcsSeriesPath.getRSSeriesPathList()){
                    stringBuilder.append("分段路径:");
                    stringBuilder.append(seriesPath);
                    RCS2RobotPathMessage pathMessage = new RCS2RobotPathMessage(1);
                    pathMessage.setSeriesPath(seriesPath);
                    pathMessage.toMessage();
                    stringBuilder.append("\n");
                    stringBuilder.append("对应编码:");
                    stringBuilder.append(HexBinaryUtil.byteArrayToHexString2((byte[]) pathMessage.getMessage()));
                    stringBuilder.append("\n");
                }
                stringBuilder.append("---------------------------------------------------------------------------");
            }else{
                stringBuilder.append("路径校验不通过！！！");
            }
            resMap.put("res", stringBuilder.toString());
            channel.basicPublish(COM_EXCHANGE, SubjectManager.WCS_RCS_CHECKING_AGV_PATH_RESPONSE, null, SerializationUtils.serialize((Serializable) resMap));
        }
    }


    public void putAllAGVInfo2AGVInfo(Map<String, Object> message){
        try {
            blockingDeque.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
