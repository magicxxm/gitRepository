/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.kiva.bus.action;

import com.mushiny.rcs.global.AGVConfig;

/**
 *
 * @author 陈庆余 <西安，18292019681，13469592826@163.com>
 */
public class AGVActionContainer {
   //----------------AGV直行---------------------------------------------------
    protected final StraightLine02Divide1Action lineAction;//直行100mm
    protected final StraightLine02Divide2Action straightLine02Divide2Action;//直行500mm
    //--protected final StraightLine020Action straightLine020Action;//直行0mm
    //----------------AGV普通旋转----------------------------------------------
    protected final Rotate10Action rotate10Action_0;
    protected final Rotate10Action rotate10Action_90;
    protected final Rotate10Action rotate10Action_180;
    protected final Rotate10Action rotate10Action_270;
     //---------------AGV取POD时旋转--------------
    protected final Rotate12Action rotate12Action_0;
    protected final Rotate12Action rotate12Action_90;
    protected final Rotate12Action rotate12Action_180;
    protected final Rotate12Action rotate12Action_270;
    //--------------AGV放POD时旋转-----------------
    protected final Rotate13Action rotate13Action_0;
    protected final Rotate13Action rotate13Action_90;
    protected final Rotate13Action rotate13Action_180;
    protected final Rotate13Action rotate13Action_270;
    //---------------AGV旋转区旋转-----------------
    protected final Rotate11Action rotate11Action_0;
    protected final Rotate11Action rotate11Action_90;
    protected final Rotate11Action rotate11Action_180;
    protected final Rotate11Action rotate11Action_270;
    //----------------顶升，下降，充电--------------
    protected final Up20Action up20Action;
    protected final Down21Action down21Action;
    protected final Charge30Action power30Action;
    //----------------停止-------------------------
    protected final StopFFAction stopAction;

    protected AGVActionContainer() {
        lineAction = new StraightLine02Divide1Action();
        straightLine02Divide2Action = new StraightLine02Divide2Action();
        //--straightLine020Action = new StraightLine020Action();
        
        rotate10Action_0 = new Rotate10Action(AGVConfig.ACTION_PARAM_ROTATE_0);
        rotate10Action_90 = new Rotate10Action(AGVConfig.ACTION_PARAM_ROTATE_90);
        rotate10Action_180 = new Rotate10Action(AGVConfig.ACTION_PARAM_ROTATE_180);
        rotate10Action_270 = new Rotate10Action(AGVConfig.ACTION_PARAM_ROTATE_270);
        
        rotate11Action_0 = new Rotate11Action(AGVConfig.ACTION_PARAM_ROTATE_0);
        rotate11Action_90 = new Rotate11Action(AGVConfig.ACTION_PARAM_ROTATE_90);
        rotate11Action_180 = new Rotate11Action(AGVConfig.ACTION_PARAM_ROTATE_180);
        rotate11Action_270 = new Rotate11Action(AGVConfig.ACTION_PARAM_ROTATE_270);

        rotate12Action_0 = new Rotate12Action(AGVConfig.ACTION_PARAM_ROTATE_0);
        rotate12Action_90 = new Rotate12Action(AGVConfig.ACTION_PARAM_ROTATE_90);
        rotate12Action_180 = new Rotate12Action(AGVConfig.ACTION_PARAM_ROTATE_180);
        rotate12Action_270 = new Rotate12Action(AGVConfig.ACTION_PARAM_ROTATE_270);
       

        rotate13Action_0 = new Rotate13Action(AGVConfig.ACTION_PARAM_ROTATE_0);
        rotate13Action_90 = new Rotate13Action(AGVConfig.ACTION_PARAM_ROTATE_90);
        rotate13Action_180 = new Rotate13Action(AGVConfig.ACTION_PARAM_ROTATE_180);
        rotate13Action_270 = new Rotate13Action(AGVConfig.ACTION_PARAM_ROTATE_270);
        
        up20Action = new Up20Action();
        down21Action = new Down21Action();
        power30Action = new Charge30Action();
        
        stopAction = new StopFFAction();

    }
}
