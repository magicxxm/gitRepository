package mq;

import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.server.AGVManager;
import com.mushiny.rcs.server.KivaAGV;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Laptop-6 on 2017/11/11.
 */
public class AGVInfoSender extends MQPublisher implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(AGVInfoSender.class.getName());
    public static AGVInfoSender instance = null;
    private AGVInfoSender() {
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new AGVInfoSender();
            new Thread(instance).start();
        }
    }
    public static AGVInfoSender getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }


    private String requestUuid;
    private LinkedBlockingDeque<Long> robotIdBlockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                Long robotId = robotIdBlockingDeque.take();
                KivaAGV curAgv = AGVManager.getInstance().getAGVByID(robotId);
                sendAGVInfo(curAgv);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("获取itemInfo出错：\n"+e.getStackTrace());
            }
        }
    }

    private void sendAGVInfo(KivaAGV curAGV) throws Exception{
        if(curAGV != null){
            Map<String, Object> agvInfo = new HashMap<>();
            agvInfo.put("sectionID", sectionID);
            agvInfo.put("sessionID", requestUuid);
            agvInfo.put("robotID", curAGV.getID());
            agvInfo.put("currentAddressCodeID", curAGV.getCurrentAddressCodeID());
            agvInfo.put("AGVStatus", curAGV.getAGVStatus());
            agvInfo.put("AGVStatusInfo", curAGV.getAGVStatusInfo());
            agvInfo.put("currentGlobalSeriesPath", getListFromSeriesPath(curAGV.getCurrentGlobalSeriesPath()));
            agvInfo.put("currentSeriesPath", getListFromSeriesPath(curAGV.getCurrentSeriesPath()));
            agvInfo.put("lastSendedSeriesPath", getListFromSeriesPath(curAGV.getLastSendedSeriesPath()));
            agvInfo.put("previousAGVStatus", curAGV.getPreviousAGVStatus());
            agvInfo.put("podCodeID", curAGV.getPodCodeID());
            agvInfo.put("previousAddressCodeID", curAGV.getPreviousAddressCodeID());
            agvInfo.put("podInfo", PodManager.getInstance().getPodList().toString());
            agvInfo.put("isPathResponse", curAGV.getPathResponse());
            agvInfo.put("isLift", curAGV.isLift());
            agvInfo.put("isRequestPath", curAGV.isRequestPath());
            agvInfo.put("isRotationFinished", curAGV.isRotationFinished());
            agvInfo.put("seriesPathLinkedList", getListFromSeriesPath(curAGV.getSeriesPathLinkedList()));

            List<Integer> costChangedList = new ArrayList<>();
            List<Integer> lockedList = new ArrayList<>();
            if(MapManager.getInstance().getMap() != null){
                int maxAddressCodeID = MapManager.getInstance().getMap().getMaxAddressCodeID();
                for(int addressCodeID = 1; addressCodeID <= maxAddressCodeID; addressCodeID++){
                    CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCodeID);
                    if(cellNode.isLocked()){
                        lockedList.add(addressCodeID);
                    }
                    if(cellNode.isChangingCost()){
                        costChangedList.add(addressCodeID);
                    }
                }
            }
            agvInfo.put("lockedList", lockedList);
            agvInfo.put("costChangedList", costChangedList);


            channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_ITEM_INFO, null, SerializationUtils.serialize((Serializable) agvInfo));
            LOG.info("发送AGV信息："+agvInfo);
        }
    }

    private List<Long> getListFromSeriesPath(List<SeriesPath> seriesPathList){
        if(seriesPathList == null){
            return null;
        }
        List<Long> resList = new ArrayList<>();
        for(SeriesPath seriesPath : seriesPathList){
            List<Long> tempList = getListFromSeriesPath(seriesPath);
            resList.addAll(tempList);
        }
        return resList;
    }

    private List<Long> getListFromSeriesPath(SeriesPath seriesPath){
        if(seriesPath == null){
            return null;
        }
        if(seriesPath.getPathList() == null){
            return null;
        }
        if(seriesPath.getPathList().size() == 0){
            return null;
        }
        List<Long> tempList = new ArrayList<>();
        for(CellNode cellNode : seriesPath.getPathList()){
            tempList.add(cellNode.getAddressCodeID());
        }
        return tempList;
    }

    public void putRobotId2AGVInfo(long robotId){
        try {
            robotIdBlockingDeque.put(robotId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getRequestUuid() {
        return requestUuid;
    }

    public void setRequestUuid(String requestUuid) {
        this.requestUuid = requestUuid;
    }
}
