package com.mushiny.rcs.wcs;

import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.Charge30Action;
import com.mushiny.rcs.kiva.bus.action.RobotAction;
import com.mushiny.rcs.kiva.bus.action.Rotate10Action;
import com.mushiny.rcs.kiva.bus.action.StopFFAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class WCSChargeSeriesPath extends WCSSeriesPath {
    private static Logger LOG = LoggerFactory.getLogger(WCSChargeSeriesPath.class.getName());
    /*
    充电地址码
     */
    private long chargeAddressCodeId;
    /*
    充电墙的方向角
     */
    private int angle = -1;

    public WCSChargeSeriesPath(long chargeAddressCodeId, int angle, LinkedList<Long> addressCodeIDList) {
        super(0, 0, false, 0, addressCodeIDList);
        this.chargeAddressCodeId = chargeAddressCodeId;
        this.angle = angle;
    }
    public WCSChargeSeriesPath(int angle, LinkedList<Long> addressCodeIDList) {
        this(addressCodeIDList.getLast(), angle, addressCodeIDList);
    }

    @Override
    public boolean checkWCSSeriesPath_() {
        if(!super.checkWCSSeriesPath_()){
            return false;
        }
        //充电点必须为最后一个点
        CellNode lastNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(this.addressCodeIDList.getLast());
        CellNode chargeNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(this.chargeAddressCodeId);
        if(lastNode != chargeNode){
            LOG.error("充电路径最后一个点不是充电点！");
            return false;
        }
        //充电门方向角必须为0,90,180,270
        if(this.angle != 0
                && this.angle != 90
                && this.angle != 180
                && this.angle != 270){
            LOG.error("充电旋转角度不是0,90,180,270！");
            return false;
        }

        //校验充电旋转角度与所定义的充电桩充电隔板方向是否相对

        return true;
    }

    @Override
    public void analyzePath() {
        super.analyzePath();
        LinkedList<SeriesPath> rs = super.getRSSeriesPathList();
        SeriesPath lastSeriesPath = rs.getLast();
        CellNode chargeNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(this.chargeAddressCodeId);
        if(chargeNode.getRobotActionList(lastSeriesPath) != null && !chargeNode.getRobotActionList(lastSeriesPath).isEmpty()){
            chargeNode.getRobotActionList(lastSeriesPath).clear();
        }
        RobotAction rotate10Action = new Rotate10Action(angle);
        chargeNode.setRobotAction(lastSeriesPath, rotate10Action);
        chargeNode.setRobotAction(lastSeriesPath, new StopFFAction());
        RobotAction charge30Action = new Charge30Action();
        chargeNode.setRobotAction(lastSeriesPath, charge30Action);

        LOG.info("牧星充电路径串："+super.getRSSeriesPathList());

    }

    public long getChargeAddressCodeId() {
        return chargeAddressCodeId;
    }

    public void setChargeAddressCodeId(long chargeAddressCodeId) {
        this.chargeAddressCodeId = chargeAddressCodeId;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
