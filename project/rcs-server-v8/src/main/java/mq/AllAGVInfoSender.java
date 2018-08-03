package mq;

import com.mingchun.mu.mushiny.kiva.pod.IPod;
import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mingchun.mu.util.ExceptionUtil;
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
public class AllAGVInfoSender extends MQPublisher implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(AllAGVInfoSender.class.getName());
    public static AllAGVInfoSender instance = null;
    private AllAGVInfoSender() {
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new AllAGVInfoSender();
            new Thread(instance).start();
        }
    }
    public static AllAGVInfoSender getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }


    private LinkedBlockingDeque<Long> allAGVBlockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                /*Long allAGVInfo = allAGVBlockingDeque.take();
                if(allAGVInfo == 1){
                    sendAllAGVInfo();
                }*/
                if(channel != null){
                    sendAllAGVInfo();
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("获取agv信息出错：\n"+ ExceptionUtil.getMessage(e));
            }
        }
    }

    private void sendAllAGVInfo() throws Exception{
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> resList = new ArrayList<>();
        List<KivaAGV> kivaAGVList = AGVManager.getInstance().getAgvList();
        if(kivaAGVList != null){
            Map<String, Object> agvInfo = null;
            for(KivaAGV curAGV : kivaAGVList){
                agvInfo = new HashMap<>();
                agvInfo.put("sectionID", sectionID);
                agvInfo.put("robotID", curAGV.getID());
                agvInfo.put("currentAddressCodeID", curAGV.getCurrentAddressCodeID());
                agvInfo.put("AGVStatus", curAGV.getAGVStatus());
                agvInfo.put("AGVStatusInfo", curAGV.getAGVStatusInfo());
                if(curAGV.getCurrentGlobalSeriesPath() == null
                        || curAGV.getCurrentGlobalSeriesPath().getPathList() == null
                        || curAGV.getCurrentGlobalSeriesPath().getPathList().size() == 1){
                    agvInfo.put("currentGlobalSeriesPath", getListFromSeriesPath(curAGV.getTempNextGlobalSeriesPath()));
                }else{
                    agvInfo.put("currentGlobalSeriesPath", getListFromSeriesPath(curAGV.getCurrentGlobalSeriesPath()));
                }
                agvInfo.put("currentSeriesPath", getListFromSeriesPath(curAGV.getCurrentSeriesPath()));
                agvInfo.put("lastSendedSeriesPath", getListFromSeriesPath(curAGV.getLastSendedSeriesPath()));
                agvInfo.put("previousAGVStatus", curAGV.getPreviousAGVStatus());
                agvInfo.put("podCodeID", curAGV.getPodCodeID());
                agvInfo.put("previousAddressCodeID", curAGV.getPreviousAddressCodeID());
                agvInfo.put("isPathResponse", curAGV.getPathResponse());
                agvInfo.put("isLift", curAGV.isLift());
                agvInfo.put("isRequestPath", curAGV.isRequestPath());
                agvInfo.put("isRotationFinished", curAGV.isRotationFinished());
                agvInfo.put("seriesPathLinkedList", getListFromSeriesPath(curAGV.getSeriesPathLinkedList()));
                resList.add(agvInfo);
            }
        }

        resMap.put("agvList", resList);

        List<IPod> podList = PodManager.getInstance().getPodList();
        List<Map<String, Object>> podList_ = new ArrayList<>();
        for(IPod pod : podList){
            if(pod.getCellNode() != null){
                Map<String, Object> map = new HashMap<>();
                map.put("podCodeID", pod.getPodCodeID());
                map.put("addressCodeID", pod.getCellNode().getAddressCodeID());
                podList_.add(map);
            }
        }
        resMap.put("podList", podList_);

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
        resMap.put("lockedList", lockedList);
        resMap.put("costChangedList", costChangedList);


        channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_ALL_AGV_INFO, null, SerializationUtils.serialize((Serializable) resMap));
//        LOG.info("发送所有AGV信息："+resList);

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

    public void putAllAGVInfo2AGVInfo(long allAGVInfo){
        try {
            if(false){
                allAGVBlockingDeque.put(allAGVInfo);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
