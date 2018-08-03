package com.mingchun.mu.mushiny.rcs.wcs;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapCellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.Down21PodIDAction;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.kiva.bus.action.Up20PodIDAction;
import com.mushiny.rcs.wcs.WCSSeriesPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 *   同一个点既上升又下降，先升后降
 *
 */
public class WCSSeriesPathSameNodeUpDown extends WCSSeriesPathDifferentDistanceCellTool {

    private Logger LOG = LoggerFactory.getLogger(WCSSeriesPathSameNodeUpDown.class.getName());

    public WCSSeriesPathSameNodeUpDown(WCSSeriesPath wcsp) {
        super(wcsp);
    }
    @Override
    public void analysis() {
        super.analysis();

        LOG.info("robotID="+wcsp.getAgvID()+", 上升点：" + wcsp.getUpPodAddressCodeID() + ", 下降点：" + wcsp.getDownPodAddressCodeID());

        if(wcsp.getUpPodAddressCodeID() != 0 && wcsp.getUpPodAddressCodeID() == wcsp.getDownPodAddressCodeID()){
            LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
            SeriesPath lastSeriesPath = resPath.getLast();
            if(lastSeriesPath != null && lastSeriesPath.getPathList() != null && lastSeriesPath.getPathList().size() > 0){
                CellNode cellNode = lastSeriesPath.getPathList().getLast();
                LinkedList<RobotAction> robotActionList = cellNode.getRobotActionList(lastSeriesPath);

                // 移除最后一点的上升动作
                RobotAction up20PodIDAction = null;
                if(robotActionList != null){
                    for(RobotAction robotAction : robotActionList){
                        if(robotAction instanceof Up20PodIDAction){
                            up20PodIDAction = robotAction;
                        }
                    }
                }
                if(up20PodIDAction != null){
                    robotActionList.remove(up20PodIDAction);
                }

                // 添加下降动作
                Down21PodIDAction down21PodIDAction = new Down21PodIDAction(wcsp.getPodCodeID());
                robotActionList.addFirst(down21PodIDAction);

                LOG.info("同一个点既上升又下降，先升后降解析完成。。。");


            }

        }




    }
}
