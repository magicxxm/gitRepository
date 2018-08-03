/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;


import com.mushiny.kiva.map.MapContainer;
import java.awt.Color;

/**
 *
 * @author aricochen
 */
public class MapConfig {
    public static  int CELL_WIDTH=40;
    public static  int CELL_HEIGHT=40;
    public static  int MAP_MARGIN_TOP=50;
    public static  int MAP_MARGIN_RIGHT=50;
    public static  int MAP_MARGIN_BOTTON=50;
    public static  int MAP_MARGIN_LEFT=50;
    public static  float MAP_ZOOM_IN_RADIO=1.1f;
    public static  float MAP_ZOOM_OUT_RAIDO=0.9f;
    public static  Color MAP_BACKGROUD_COLOR=Color.GRAY;
    
    public static  Color CELL_MARGIN_COMMON_COLOR=new Color(181, 207, 255);
    public static  Color CELL_COMMON_COLOR=MAP_BACKGROUD_COLOR;
    
    public static  Color CELL_ALARM_COLOR=Color.RED;
    
    public static   Color CELL_PATH_COLOR = Color.GREEN.darker().darker();
    public static   Color CELL_MARGIN_PATH_COLOR =CELL_PATH_COLOR.brighter();
    
    public static   Color CELL_LOCKED_PATH_COLOR = CELL_PATH_COLOR.brighter();
    //-- public static final  Color CELL_LOCKED_PATH_COLOR = Color.ORANGE;
    public static   Color CELL_LOCKED_MARGIN_PATH_COLOR = CELL_LOCKED_PATH_COLOR.brighter();
    
    
    public static  Color CELL_SELECTED_COLOR = Color.BLUE.darker();
    public static  Color CELL_SELECTED_MARGIN_COLOR = CELL_SELECTED_COLOR.brighter();
       
    public static final Color CELL_ROTATE_AREA_COLOR = Color.YELLOW.darker();
    public static final Color CELL_ROTATE_AREA_MARGIN_COLOR = CELL_ROTATE_AREA_COLOR.brighter();
    
    public static final Color CELL_UNWALKED_COLOR = Color.ORANGE.darker();
    public static final Color CELL_UNWALKED_MARGIN_COLOR = CELL_UNWALKED_COLOR.brighter();
    
    public static final  Color AGV_POSITION_COLOR = Color.RED.darker();
    public static final  Color AGV_MARGIN_POSITION_COLOR = Color.RED.brighter();
    
    public static final int MAP_VIEW_MODEL=MapContainer.VIEW_MODEL;
    public static final boolean MAP_VIEW_CELL=true;
    public static final boolean MAP_VIEW_ROBOT_INFO=false;
    //机器信息显示
    public static final int ROBOT_INFO_VIEW_CIRCLE_R=5;
    public static final int ROBOT_INFO_VIEW_MIDDLE_P_W=8;
    public static final int ROBOT_INFO_VIEW_MIDDLE_P_H=10;
    public static final String ROBOT_INFO_VIEW_FONT_NAME="宋体";
    public static final int ROBOT_INFO_VIEW_FONT_SIZE=25;

    // mingchun.mu@mushiny.com  孤立点颜色显示
    public static final Color INDIVIDUAL_CELL_COLOR = Color.CYAN;
    // mingchun.mu@mushiny.com  --------------------------


}
