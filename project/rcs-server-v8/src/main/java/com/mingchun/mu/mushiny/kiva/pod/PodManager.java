package com.mingchun.mu.mushiny.kiva.pod;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 *
 *
 * Created by Laptop-6 on 2017/10/17.
 */
public class PodManager {
    private Logger LOG = LoggerFactory.getLogger(PodManager.class.getName());
    private static PodManager instance = null;
    private PodManager() {
    }
    private static synchronized void initInstance(){
        if(instance == null){
            instance = new PodManager();
        }
    }
    public static PodManager getInstance() {
        if(instance == null){
            initInstance();
        }
        return instance;
    }


    /*
    pod 容器
     */
    List<IPod> podList = new CopyOnWriteArrayList<>();

    /**
     * 讲pod添加到容器
     * @param pod
     * @return
     */
    public boolean addPod2Container(IPod pod){
        if(pod == null){
            return false;
        }
        if(podList == null){
            return false;
        }
        if(containsPod(pod)){
            return false;
        }
        return podList.add(pod);
    }

    /**
     * 从容器中移除pod
     * @param pod
     * @return
     */
    public boolean removePodFromContainer(IPod pod){
        if(pod == null){
            return false;
        }
        if(podList == null){
            return false;
        }
        if(!containsPod(pod)){
            return false;
        }
        return podList.remove(pod);
    }
    /**
     * 从容器中移除pod
     * @param podCodeId
     * @return
     */
    public boolean removePodFromContainer(long podCodeId){
        if(podCodeId <= 0){
            return false;
        }
        return removePodFromContainer(getPod(podCodeId));
    }

    /**
     * 容器中是否存在pod
     * @param pod
     * @return
     */
    private boolean containsPod(IPod pod){
        if(pod == null){
            return false;
        }
        if(podList == null){
            return false;
        }
        if(podList.size() == 0){
            return false;
        }
        for(IPod p: podList){
            if(p.equals(pod)){
                return true;
            }
        }
        return false;
    }

    /**
     * 通过podCodeID从容器中获取pod
     * @param podCodeID
     * @return
     */
    public IPod getPod(long podCodeID){
        if(podCodeID <= 0){
            return null;
        }
        for(IPod pod: podList){
            if(podCodeID == pod.getPodCodeID()){
                return pod;
            }
        }
        return null;
    }
    public IPod getPod(CellNode cellNode){
        if(cellNode == null){
            return null;
        }
        if(podList == null){
            return null;
        }
        if(podList.size() == 0){
            return null;
        }
        for(IPod pod: podList){
            if(pod.getCellNode() != null && cellNode.getAddressCodeID() == pod.getCellNode().getAddressCodeID()){
                return pod;
            }
        }
        return null;
    }

    /**
     * 给地址码安装pod
     * map<podCodeID, addressCodeID>
     */
    public void installCellNodePod(List<Map<Long, Long>> pods) {
        if(pods == null){
            return;
        }
        if(pods.size() == 0){
            return;
        }
        Map<Long, Long> resMap = new HashMap<>();
        for(int i = 0, len = pods.size(); i < len; i++){
            Map<Long, Long> tempMap = pods.get(i);
            Set<Map.Entry<Long, Long>> entrySet = tempMap.entrySet();
            Iterator<Map.Entry<Long, Long>> iter = entrySet.iterator();
            while (iter.hasNext()){
                Map.Entry<Long, Long> entry = iter.next();
                Long podCodeID = entry.getKey();
                CellNode cellNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(entry.getValue());
                IPod pod = new Pod(entry.getKey(), cellNode);
                addPod2Container(pod);
                LOG.info(pod.toString());
            }
        }
    }
    /**
     * 在指定的地图上安装pod
     * map<podCodeID, addressCodeID>
     */
    public void installCellNodePod(List<Map<Long, Long>> pods, KivaMap kivaMap) {
        if(pods == null){
            return;
        }
        if(pods.size() == 0){
            return;
        }
        Map<Long, Long> resMap = new HashMap<>();
        for(int i = 0, len = pods.size(); i < len; i++){
            Map<Long, Long> tempMap = pods.get(i);
            Set<Map.Entry<Long, Long>> entrySet = tempMap.entrySet();
            Iterator<Map.Entry<Long, Long>> iter = entrySet.iterator();
            while (iter.hasNext()){
                Map.Entry<Long, Long> entry = iter.next();
                Long podCodeID = Long.parseLong(String.valueOf(entry.getKey()));
                CellNode cellNode = kivaMap.getMapCellByAddressCodeID(Long.parseLong(String.valueOf(entry.getValue())));
                IPod pod = new Pod(podCodeID, cellNode);
                addPod2Container(pod);

            }
        }
    }

    public List<IPod> getPodList() {
        return podList;
    }

    /**
     * 安装非存储区的pods
     * @param pods
     */
    public void installUnStoragePods(List<Map<Long, Long>> pods) {
        if(pods == null){
            return;
        }
        if(pods.size() == 0){
            return;
        }
        Map<Long, Long> resMap = new HashMap<>();
        for(int i = 0, len = pods.size(); i < len; i++){
            Map<Long, Long> tempMap = pods.get(i);
            Set<Map.Entry<Long, Long>> entrySet = tempMap.entrySet();
            Iterator<Map.Entry<Long, Long>> iter = entrySet.iterator();
            while (iter.hasNext()){
                Map.Entry<Long, Long> entry = iter.next();
                Long podCodeID = Long.parseLong(String.valueOf(entry.getKey()));
                IPod pod = new Pod(podCodeID, null);
                addPod2Container(pod);
            }
        }
    }
}
