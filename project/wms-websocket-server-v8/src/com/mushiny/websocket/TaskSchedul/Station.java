package com.mushiny.websocket.TaskSchedul;

import com.mushiny.comm.SpringUtil;
import com.mushiny.jdbc.service.JdbcService;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author:
 * @Description: Created by wangjianwei on 2017/9/1.
 */
public class Station {
    private String phyStation;
    private String logicalStation;
    private String pickPackWall;

    public String getPickPackWall() {
        return pickPackWall;
    }

    public void setPickPackWall(String pickPackWall) {
        this.pickPackWall = pickPackWall;
    }

    private AtomicBoolean hasTask = new AtomicBoolean();
    private LinkedBlockingQueue task = new LinkedBlockingQueue(1);
    private JdbcService service = SpringUtil.getBean(JdbcService.class);

    public AtomicBoolean getHasTask() {
        boolean result = false;
        List state = service.getWorkStationState(logicalStation);

        if (!CollectionUtils.isEmpty(state)) {
            int len = state.size();
            int k = 0;
            for (; k < len; ) {
                if ((Integer) state.get(k) == 3 || (Integer) state.get(k) == 4) {
                    k++;
                    continue;

                } else {
                    break;
                }
            }
            if (k != len) {
                result = true;
            }

        }
        hasTask.set(result);
        if (!result) {
            task.poll();
        }
        return hasTask;
    }

    public void setHasTask(AtomicBoolean hasTask) {
        this.hasTask = hasTask;
    }

    public LinkedBlockingQueue getTask() {
        return task;
    }

    public void setTask(LinkedBlockingQueue task) {
        this.task = task;
    }


    public String getPhyStation() {
        return phyStation;
    }

    public void setPhyStation(String phyStation) {
        this.phyStation = phyStation;
    }

    public String getLogicalStation() {
        return logicalStation;
    }

    public void setLogicalStation(String logicalStation) {
        this.logicalStation = logicalStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Station)) return false;

        Station station = (Station) o;

        if (!phyStation.equals(station.phyStation)) return false;
        if (!logicalStation.equals(station.logicalStation)) return false;
        return pickPackWall.equals(station.pickPackWall);
    }

    @Override
    public int hashCode() {
        int result = phyStation.hashCode();
        result = 31 * result + logicalStation.hashCode();
        result = 31 * result + pickPackWall.hashCode();
        return result;
    }
}
