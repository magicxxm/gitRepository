package com.mushiny.rcs.wcs;

import com.aricojf.platform.common.HexBinaryUtil;
import com.mushiny.kiva.map.CellNode;
import com.mushiny.kiva.map.MapManager;
import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.kiva.bus.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

public class WCSMediaChargeSeriesPath extends WCSSeriesPath {
    private static Logger LOG = LoggerFactory.getLogger(WCSMediaChargeSeriesPath.class.getName());
    /*
    充电地址码
     */
    private long chargeAddressCodeId;
    /*
    充电口的方向角
     */
    private int angle = -1;

    /*
    充电桩编号
     */
    private long chargingPileNumber;
    /*
    充电桩mac
     */
    private String chargingPileMac;

    public WCSMediaChargeSeriesPath(long chargeAddressCodeId, int angle, LinkedList<Long> addressCodeIDList) {
        super(0, 0, false, 0, addressCodeIDList);
        this.chargeAddressCodeId = chargeAddressCodeId;
        this.angle = angle;
    }
    public WCSMediaChargeSeriesPath(int angle, LinkedList<Long> addressCodeIDList, long chargingPileNumber) {
        this(addressCodeIDList.getLast(), angle, addressCodeIDList);
        this.chargingPileNumber = chargingPileNumber;
    }
    public WCSMediaChargeSeriesPath(int angle, LinkedList<Long> addressCodeIDList, String chargingPileMac) {
        this(addressCodeIDList.getLast(), angle, addressCodeIDList);
        this.chargingPileMac = chargingPileMac;
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
        //充电角度必须为0,90,180,270
        if(this.angle != 0
                && this.angle != 90
                && this.angle != 180
                && this.angle != 270){
            LOG.error("充电旋转角度不是0,90,180,270！");
            return false;
        }
        CellNode lastSecondNode = MapManager.getInstance().getMap().getMapCellByAddressCodeID(this.addressCodeIDList.get(this.addressCodeIDList.size()-2));

        return true;
    }


    @Override
    public void analyzePath() {
        super.analyzePath();
        this.arrivalChargingPileAsParameter();
        LOG.info("美的充电分割出来的路径串：\n"+super.getRSSeriesPathList());
    }


    private void chargePath(){
        LinkedList<SeriesPath> seriesPaths = super.getRSSeriesPathList();
        SeriesPath lastSeriesPath = seriesPaths.getLast();
        if(seriesPaths.size() == 1 && lastSeriesPath.getPathList().size() == 2){//总路径条数只有一条
            CellNode rotateNode = lastSeriesPath.getPathList().getFirst();
            if(rotateNode.getRobotActionList(lastSeriesPath) != null && !rotateNode.getRobotActionList(lastSeriesPath).isEmpty()){
                rotateNode.getRobotActionList(lastSeriesPath).clear();
            }
            RobotAction rotate10Action = new Rotate10Action(this.angle);
            rotateNode.setRobotAction(lastSeriesPath, rotate10Action);
            RobotAction charge30Action = new Charge30Action();
            rotateNode.setRobotAction(lastSeriesPath, charge30Action);
        }else if(seriesPaths.size() >= 2 && lastSeriesPath.getPathList().size() == 2) {
            SeriesPath lastSecondSeriesPath = seriesPaths.get(seriesPaths.size() - 2);
            RobotAction rotate10Action = new Rotate10Action(this.angle);
            CellNode rotateNode = lastSecondSeriesPath.getPathList().getLast();
            if(rotateNode.getRobotActionList(lastSecondSeriesPath) != null && !rotateNode.getRobotActionList(lastSecondSeriesPath).isEmpty()){
                rotateNode.getRobotActionList(lastSecondSeriesPath).clear();
            }
            rotateNode.setRobotAction(lastSecondSeriesPath, rotate10Action);
            if(rotateNode.getRobotActionList(lastSeriesPath) != null && !rotateNode.getRobotActionList(lastSeriesPath).isEmpty()){
                rotateNode.getRobotActionList(lastSeriesPath).clear();
            }
            rotateNode.setRobotAction(lastSeriesPath, rotate10Action);
            RobotAction charge30Action = new Charge30Action();
            rotateNode.setRobotAction(lastSeriesPath, charge30Action);
        }else {
            //创建充电路径
            SeriesPath chargingSeriesPath = new SeriesPath();
            //获取最后两点充电路径
            CellNode stopCellNode = lastSeriesPath.getPathList().removeLast();
            CellNode chargingNode = lastSeriesPath.getPathList().getLast();
            chargingSeriesPath.getPathList().addFirst(stopCellNode);
            chargingSeriesPath.getPathList().addFirst(chargingNode);
            if(chargingNode.getRobotActionList(lastSeriesPath) != null && !chargingNode.getRobotActionList(lastSeriesPath).isEmpty()){
                chargingNode.getRobotActionList(lastSeriesPath).clear();
            }
            RobotAction rotate10Action = new Rotate10Action(this.angle);
            chargingNode.setRobotAction(lastSeriesPath, rotate10Action);
            chargingNode.setRobotAction(chargingSeriesPath, rotate10Action);
            RobotAction charge30Action = new Charge30Action();
            chargingNode.setRobotAction(chargingSeriesPath, charge30Action);
            seriesPaths.addLast(chargingSeriesPath);
        }

    }

    /**
     * 充电桩作为参数来处理
     */
    private void arrivalChargingPileAsParameter(){
        chargePath();
        SeriesPath chargeSeriesPath = super.getRSSeriesPathList().getLast();
        CellNode chargingPileNode = chargeSeriesPath.getPathList().getLast();

        if(chargingPileNode.getRobotActionList(chargeSeriesPath) != null && !chargingPileNode.getRobotActionList(chargeSeriesPath).isEmpty()){
            chargingPileNode.getRobotActionList(chargeSeriesPath).clear();
        }
        byte[] bytes = HexBinaryUtil.hexStringToByteArray(chargingPileMac);
        int mac = 0;
        RobotAction arrivalChargingPile31Action;
        for(int i = bytes.length - 1; i > 0; i = i-2){
            mac = bytes[i-1];
            mac = (int)((mac << 8)|(bytes[i] & 0xff));
            arrivalChargingPile31Action = new Media_Charge31Action(mac);
            chargingPileNode.setRobotAction(chargeSeriesPath, arrivalChargingPile31Action);
        }
        RobotAction stop00Action = new StopFFAction();
        chargingPileNode.setRobotAction(chargeSeriesPath, stop00Action);
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


    public long getChargingPileNumber() {
        return chargingPileNumber;
    }

    public void setChargingPileNumber(long chargingPileNumber) {
        this.chargingPileNumber = chargingPileNumber;
    }

    public String getChargingPileMac() {
        return chargingPileMac;
    }

    public void setChargingPileMac(String chargingPileMac) {
        this.chargingPileMac = chargingPileMac;
    }
}
