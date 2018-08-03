package mq;

import com.mingchun.mu.mushiny.kiva.pod.PodManager;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Laptop-6 on 2017/11/11.
 */
public class MapCellNodeInfoSender extends MQPublisher implements Runnable {
    private Logger LOG = LoggerFactory.getLogger(MapCellNodeInfoSender.class.getName());
    public static MapCellNodeInfoSender instance = null;
    private MapCellNodeInfoSender() {
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new MapCellNodeInfoSender();
            new Thread(instance).start();
        }
    }
    public static MapCellNodeInfoSender getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }


    private String requestUuid;
    private LinkedBlockingDeque<Long> mapCellNodeInfoBlockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                Long addressCodeId = mapCellNodeInfoBlockingDeque.take();
                MapCellNode mapCellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(addressCodeId);
                sendMapCellNodeInfo(mapCellNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendMapCellNodeInfo(MapCellNode mapCellNode) throws Exception{
        if(mapCellNode != null
                && getRequestUuid() != null){
            Map<String, Object> cellInfo = new HashMap<>();
            cellInfo.put("sectionID", sectionID);
            cellInfo.put("sessionID", getRequestUuid());
            cellInfo.put("addressCodeID", mapCellNode.getAddressCodeID());
            cellInfo.put("isLocked", mapCellNode.isLocked());
            if(mapCellNode.getNowLockedAGV() != null){
                cellInfo.put("nowLockedAGV", mapCellNode.getNowLockedAGV().getID());
            }
            if(PodManager.getInstance().getPod(mapCellNode) != null){
                cellInfo.put("podCodeID", PodManager.getInstance().getPod(mapCellNode).getPodCodeID());
            }
            channel.basicPublish(COM_EXCHANGE, SubjectManager.RCS_WCS_RESPONSE_ITEM_INFO, null, SerializationUtils.serialize((Serializable) cellInfo));
        }
    }

    public void putAddressCodeId(long addressCodeId){
        try {
            mapCellNodeInfoBlockingDeque.put(addressCodeId);
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
