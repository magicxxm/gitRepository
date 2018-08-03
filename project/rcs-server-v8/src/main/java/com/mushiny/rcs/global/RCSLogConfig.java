/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

import com.aricojf.util.ConfigParser;
import mq.XmlParser;

import java.awt.*;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class RCSLogConfig {

    private static RCSLogConfig instance;
    private String rcsConfigFile;

    private RCSLogConfig() {

    }

    private static synchronized void initInstance(){
        if(instance == null){
            instance = new RCSLogConfig();
        }
    }
    public static RCSLogConfig getInstance() {
        if (instance == null) {
            initInstance();
        }
        return instance;
    }
/*
    public static RCSLogConfig getInstance() {
        if (instance == null) {
            instance = new RCSLogConfig();
        }
        return instance;
    }
*/

//    public void setLogConfigFile(String log4j) {
//        PropertyConfigurator.configure(log4j);
//    }

    /**
     * @return the rcsConfigFile
     */
    public String getRcsConfigFile() {
        return rcsConfigFile;
    }

    /**
     * @param rcsConfigFile the rcsConfigFile to set
     */
    public void setRcsConfigFile(String rcsConfigFile) {
        this.rcsConfigFile = rcsConfigFile;
    }

    private XmlParser parser = new XmlParser();
    private String getCfgParam(String nodeStr) {
//        return ConfigParser.parseConfigParmByPath(rcsConfigFile, nodeStr);
        return parser.getText(rcsConfigFile, nodeStr);
    }

    public boolean initRCSGlobalParameter() {
        try {
            //解析AGVConfig
            String AGVConfigNode = "config/AGVConfig/";
            //锁格最大数量
            AGVConfig.AGV_SEND_PATH_MODEL_ALL = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_SEND_PATH_MODEL_ALL"));
            AGVConfig.AGV_SEND_PATH_MODEL_LOCKED = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_SEND_PATH_MODEL_LOCKED"));
            AGVConfig.AGV_SEND_PATH_MODEL = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_SEND_PATH_MODEL"));
            AGVConfig.AGV_LOCKED_MAX_COUNTS = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_LOCKED_MAX_COUNTS"));
            AGVConfig.AGV_SEND_SP_COUNTS_ON_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_SEND_SP_COUNTS_ON_TIMEOUT"));
            //AGV心跳执行时间
            AGVConfig.AGV_BEAT_LOOP_TIME = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_BEAT_LOOP_TIME"));
            //AGV状态检查间隔时间
            AGVConfig.AGV_CHECK_LOOP_TIME = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_CHECK_LOOP_TIME"));
            //AGV心跳超时时间
            AGVConfig.AGV_BEAT_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_BEAT_TIMEOUT"));
            //AGV实时数据超时时间
            AGVConfig.AGV_RT_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_RT_TIMEOUT"));
            //AGV心跳或实时数据超时时间
            AGVConfig.AGV_BEAT_OR_RT_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_BEAT_OR_RT_TIMEOUT"));
            //AGV位置不改变超时时间
            AGVConfig.AGV_NO_POSITION_CHANGE_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_NO_POSITION_CHANGE_TIMEOUT"));
            //AGV锁格超时时间
            AGVConfig.AGV_LOCK_SP_TIMEOUT = Integer.parseInt(getCfgParam(AGVConfigNode + "AGV_LOCK_SP_TIMEOUT"));

            AGVConfig.PRIVILEGE_ENTER_CELL = Integer.parseInt(getCfgParam(AGVConfigNode + "PRIVILEGE_ENTER_CELL"));
            AGVConfig.PRIVILEGE_ROTATION_CELL = Integer.parseInt(getCfgParam(AGVConfigNode + "PRIVILEGE_ROTATION_CELL"));


            //解析AGVConfig
            String MapConfigNode = "config/MapConfig/";
            //AGV锁格超时时间
            MapConfig.CELL_WIDTH = Integer.parseInt(getCfgParam(MapConfigNode + "CELL_WIDTH"));
            MapConfig.CELL_HEIGHT = Integer.parseInt(getCfgParam(MapConfigNode + "CELL_HEIGHT"));
            MapConfig.MAP_MARGIN_TOP = Integer.parseInt(getCfgParam(MapConfigNode + "MAP_MARGIN_TOP"));
            MapConfig.MAP_MARGIN_RIGHT = Integer.parseInt(getCfgParam(MapConfigNode + "MAP_MARGIN_RIGHT"));
            MapConfig.MAP_MARGIN_BOTTON = Integer.parseInt(getCfgParam(MapConfigNode + "MAP_MARGIN_BOTTON"));
            MapConfig.MAP_MARGIN_LEFT = Integer.parseInt(getCfgParam(MapConfigNode + "MAP_MARGIN_LEFT"));
            MapConfig.MAP_ZOOM_IN_RADIO = Float.parseFloat(getCfgParam(MapConfigNode + "MAP_ZOOM_IN_RADIO"));
            MapConfig.MAP_ZOOM_OUT_RAIDO = Float.parseFloat(getCfgParam(MapConfigNode + "MAP_ZOOM_OUT_RAIDO"));
            MapConfig.MAP_ZOOM_OUT_RAIDO = Float.parseFloat(getCfgParam(MapConfigNode + "MAP_ZOOM_OUT_RAIDO"));
            MapConfig.MAP_BACKGROUD_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "MAP_BACKGROUD_COLOR")));
            MapConfig.CELL_MARGIN_COMMON_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_MARGIN_COMMON_COLOR")));
            MapConfig.CELL_COMMON_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_COMMON_COLOR")));
            MapConfig.CELL_ALARM_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_ALARM_COLOR")));
            MapConfig.CELL_PATH_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_PATH_COLOR")));
            MapConfig.CELL_MARGIN_PATH_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_MARGIN_PATH_COLOR")));
            MapConfig.CELL_LOCKED_PATH_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_LOCKED_PATH_COLOR")));
            MapConfig.CELL_LOCKED_MARGIN_PATH_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_LOCKED_MARGIN_PATH_COLOR")));
            MapConfig.CELL_SELECTED_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_SELECTED_COLOR")));
            MapConfig.CELL_SELECTED_MARGIN_COLOR = new Color(Integer.parseInt(getCfgParam(MapConfigNode + "CELL_SELECTED_MARGIN_COLOR")));

            //解析AGVConfig
            String RCSConfigNode = "config/RCSConfig/";
            RCSConfig.NODE_NAME = getCfgParam(RCSConfigNode + "CELL_SELECTED_MARGIN_COLOR");
            RCSConfig.SECTION_ID = Long.parseLong(getCfgParam(RCSConfigNode + "SECTION_ID"));
            RCSConfig.CELL_SIZE = Integer.parseInt(getCfgParam(RCSConfigNode + "CELL_SIZE"));
            RCSConfig.LEFT_RIGHT_DISTANCE = Integer.parseInt(getCfgParam(RCSConfigNode + "ROW_CELL_SIZE"));
            RCSConfig.UP_DOWN_DISTANCE = Integer.parseInt(getCfgParam(RCSConfigNode + "COL_CELL_SIZE"));
            //解析AGVConfig
            String RotateAreaConfigNode = "config/RotateAreaConfig/";
            RotateAreaConfig.R1X = Integer.parseInt(getCfgParam(RotateAreaConfigNode + "R1X"));
            RotateAreaConfig.R1Y = Integer.parseInt(getCfgParam(RotateAreaConfigNode + "R1Y"));
            RotateAreaConfig.R1_ADDRESS_CODE_ID = Long.parseLong(getCfgParam(RotateAreaConfigNode + "R1_ADDRESS_CODE_ID"));
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
           //-- System.exit(0);
            return false;
        }
    }

    /*public static void main(String[] args) {
        RCSLogConfig config = new RCSLogConfig();
        config.setRcsConfigFile("D:\\rcsConfig.xml");
        System.out.println(config.initRCSGlobalParameter());
    }*/

}
