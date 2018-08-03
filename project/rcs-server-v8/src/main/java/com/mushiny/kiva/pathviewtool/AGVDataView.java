/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.kiva.pathviewtool;

import com.aricojf.platform.common.HexBinaryUtil;
import com.aricojf.platform.mina.message.robot.Robot2RCSActionCommandResponseMessage;
import com.aricojf.platform.mina.message.robot.RobotErrorMessage;
import com.aricojf.platform.mina.message.robot.RobotHeartBeatRequestMessage;
import com.aricojf.platform.mina.message.robot.RobotLoginRequestMessage;
import com.aricojf.platform.mina.message.robot.RobotRTMessage;
import com.aricojf.platform.mina.message.robot.RobotStatusMessage;
import com.mushiny.rcs.listener.AGVDataListener;
import com.mushiny.rcs.listener.RCSListenerManager;
import com.mushiny.rcs.server.AGVMessage;
import com.mushiny.rcs.server.KivaAGV;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * 后台地图显示   接收对应小车数据
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVDataView extends PathView implements AGVDataListener {

    private Color backgroudColor = Color.BLACK;
    private int showCount = 20;
    private int heightSetup = 5;
    private float DEFAULT_POSITION_X = 10;
    private float DEFAULT_POSITION_Y = 10;
     private float bPositionX = DEFAULT_POSITION_X;
    private float bPositionY = DEFAULT_POSITION_Y;
    private List<String> dataList = new CopyOnWriteArrayList();//画板显示的实时数据包
    private String dataStr;

    public AGVDataView() {
        super();
        RCSListenerManager.getInstance().registeAGVDataListener(this);
    }

    public void paintComponent(Graphics g) {
        //设置画笔
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //g2.setColor(backgroudColor);
        //g2.fill(getBounds());
        showAGVData(g2);
    }

    public void onReceivedRTMessage(AGVMessage agv, RobotRTMessage rtMessage) {
        dataStr = "RT:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) rtMessage.getMessage());
       // dataList.add(rtMessage.toString());
       dataList.add(dataStr);
        updateView();
    }

    public void onReceivedHeartBeatMessage(AGVMessage agv, RobotHeartBeatRequestMessage heartMessage) {
        dataStr = "HB:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) heartMessage.getMessage());
        //dataList.add(heartMessage.toString());
        dataList.add(dataStr);
        updateView();

    }

    public void onReceivedStatusMessage(AGVMessage agv, RobotStatusMessage statusMessage) {
        dataStr = "Status:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) statusMessage.getMessage());
        //dataList.add(statusMessage.toString());
        dataList.add(dataStr);
        updateView();

    }

    public void onReceivedErrorMessage(AGVMessage agv, RobotErrorMessage errorMessage) {
        dataStr = "ERROR:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) errorMessage.getMessage());
       // dataList.add(errorMessage.toString());
       dataList.add(dataStr);
        updateView();

    }

    public void onReceivedLoginMessage(AGVMessage agv, RobotLoginRequestMessage loginMessage) {
        dataStr = "LOGIN:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) loginMessage.getMessage());
        //dataList.add(loginMessage.toString());
        dataList.add(dataStr);
        updateView();
        ((KivaAGV) agv).sendLoginOKMessage();
    }

    public void onReceivedActionCommandResponseMessage(AGVMessage agv, Robot2RCSActionCommandResponseMessage actionCommandReponseMessage) {
        dataStr = "ACR:agv_id=" + agv.getID() + ",m=" + HexBinaryUtil.byteArrayToHexString2((byte[]) actionCommandReponseMessage.getMessage());
        dataList.add(actionCommandReponseMessage.toString());
        updateView();

    }

    public void showAGVData(Graphics2D g2) {
        bPositionX = DEFAULT_POSITION_X;
        bPositionY = DEFAULT_POSITION_Y;
        if (dataList.size() > showCount) {
            int tmpCount = dataList.size() - showCount;
            for (int i = 0; i < tmpCount; i++) {
                dataList.remove(0);
            }
        }
        for (String content : dataList) {
            String tmp = content;
            float acent = getFm().getLineMetrics(tmp, g2).getAscent();
            bPositionY += acent;
            g2.drawString(tmp, bPositionX, bPositionY);
            float decent = getFm().getLineMetrics(tmp, g2).getDescent();
            bPositionY += decent + heightSetup;
        }
    }
}
