package com.mushiny.rcs.wcs;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.Charge30Action;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.kiva.bus.action.Rotate10Action;
import com.mushiny.rcs.kiva.bus.action.ScanPod04Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class WCSScanPodIdPath extends WCSSeriesPath {
    private static Logger LOG = LoggerFactory.getLogger(WCSScanPodIdPath.class.getName());

    public WCSScanPodIdPath(LinkedList<Long> addressCodeIDList) {
        super(0, 0, false, 0, addressCodeIDList);
    }


    @Override
    public void analyzePath() {
        super.analyzePath();
        LinkedList<SeriesPath> rs = super.getRSSeriesPathList();
        for(SeriesPath seriesPath: rs){
            for(CellNode cellNode: seriesPath.getPathList()){
                cellNode.getRobotActionList(seriesPath).addFirst(new ScanPod04Action());
            }
        }
        LOG.info("扫描pod地址码拆分长路径："+super.getRSSeriesPathList());
    }

}
