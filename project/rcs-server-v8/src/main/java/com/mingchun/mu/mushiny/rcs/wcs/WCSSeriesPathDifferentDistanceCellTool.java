package com.mingchun.mu.mushiny.rcs.wcs;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.KivaMap;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.kiva.bus.action.StraightLine02Divide1Action;
import com.mushiny.rcs.kiva.bus.action.StraightLineFollow03Action;
import com.mushiny.rcs.wcs.WCSSeriesPath;

import java.util.LinkedList;

/**
 *   更改上下或左右的距离
 *
 */
public class WCSSeriesPathDifferentDistanceCellTool extends WCSSeriesPathDifCellTool {
    private KivaMap kivaMap;
    public WCSSeriesPathDifferentDistanceCellTool(WCSSeriesPath wcsp) {
        super(wcsp);
    }
    @Override
    public void analysis() {
        super.analysis();
        if(kivaMap == null){
            kivaMap = MapManager.getInstance().getMap();
        }
        LinkedList<SeriesPath> resPath = wcsp.getRSSeriesPathList();
        for(SeriesPath seriesPath : resPath){
            for(int i = 0; i < seriesPath.getCellListSize() - 1; i++){
                CellNode current = seriesPath.getPathList().get(i);
                CellNode next = seriesPath.getPathList().get(i+1);
                if( current.isUpNode(next) && current.getUpDistance() != 0){
                    for(RobotAction action : current.getRobotActionList(seriesPath)){
                        if(action instanceof StraightLine02Divide1Action){
                            ((StraightLine02Divide1Action) action).setStraightDistance(current.getUpDistance());
                        }
                        if(action instanceof StraightLineFollow03Action){
                            ((StraightLineFollow03Action) action).setStraightDistance(current.getUpDistance());
                        }
                    }
                }
                if(current.isDownNode(next) && current.getDownDistance() != 0){
                    for(RobotAction action : current.getRobotActionList(seriesPath)){
                        if(action instanceof StraightLine02Divide1Action){
                            ((StraightLine02Divide1Action) action).setStraightDistance(current.getDownDistance());
                        }
                        if(action instanceof StraightLineFollow03Action){
                            ((StraightLineFollow03Action) action).setStraightDistance(current.getDownDistance());
                        }
                    }
                }
                if(current.isLeftNode(next) && current.getLeftDistance() != 0){
                    for(RobotAction action : current.getRobotActionList(seriesPath)){
                        if(action instanceof StraightLine02Divide1Action){
                            ((StraightLine02Divide1Action) action).setStraightDistance(current.getLeftDistance());
                        }
                        if(action instanceof StraightLineFollow03Action){
                            ((StraightLineFollow03Action) action).setStraightDistance(current.getLeftDistance());
                        }
                    }
                }
                if(current.isRightNode(next) && current.getRightDistance() != 0){
                    for(RobotAction action : current.getRobotActionList(seriesPath)){
                        if(action instanceof StraightLine02Divide1Action){
                            ((StraightLine02Divide1Action) action).setStraightDistance(current.getRightDistance());
                        }
                        if(action instanceof StraightLineFollow03Action){
                            ((StraightLineFollow03Action) action).setStraightDistance(current.getRightDistance());
                        }
                    }
                }
            }
        }
    }
}
