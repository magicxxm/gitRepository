/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aricojf.platform.mina.message.robot;

import com.mushiny.kiva.path.SeriesPath;
import com.mushiny.rcs.global.KivaBusConfig;
import com.mushiny.rcs.global.RCS2RobotMessageTypeConfig;

/**
 * 路径消息
 *
 *  @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCS2RobotPathMessage extends RobotMessage {

    private SeriesPath seriesPath;

    public RCS2RobotPathMessage() {
        this(-1);
    }

    public RCS2RobotPathMessage(long robotID) {
        super();
        this.robotID = robotID;
        this.functionWordCode = RCS2RobotMessageTypeConfig.PATH_MESSAGE;
        this.head = KivaBusConfig.RCS2ROBOT_HEAD;
    }

    public void toMessage() {
        byte[] pathBytes = getSeriesPath().toBytes();
        short pathLength = (short) getSeriesPath().getPathLength();
        this.messageBodyLength = pathBytes.length + 1;
        this.frameDataLength = 9 + messageBodyLength;
        byte[] tmpMessageBody = new byte[messageBodyLength];
        tmpMessageBody[0] = (byte) (pathLength & 0xff);
        System.arraycopy(pathBytes, 0, tmpMessageBody, 1, pathBytes.length);
        setMessageBody(tmpMessageBody);
        super.toMessage();
    }

    public void toObject() {
        byte[] objectBytes = (byte[]) getMessage();
        super.toObject();
        short pathLength = 0;
        pathLength = (short) (objectBytes[12] & 0xff);

    }

    /**
     * @return the brokenPath
     */
    public SeriesPath getSeriesPath() {
        return seriesPath;
    }

    /**
     * @param seriesPath the brokenPath to set
     */
    public void setSeriesPath(SeriesPath seriesPath) {
        this.seriesPath = seriesPath;
    }

}
