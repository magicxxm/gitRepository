package com.mushiny.comm;

import com.mushiny.beans.Robot;

import java.util.Objects;

/**
 * Created by Tank.li on 2017/10/25.
 */
public class BizActor {
    private static final Object locker = new Object();
    public void action(Agv agv){
        synchronized (locker){
            if(agv.isAvailable() && agv.getStatus()==1){
                agv.setAvailable(false);
                agv.setStatus(Robot.WORKING);
                System.out.println(Thread.currentThread().getName()+"执行");
                try {
                    Thread.sleep(6000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"执行结束");
            }else{
                System.out.println(Thread.currentThread().getName()+"未执行");
            }
            agv.setStatus(Robot.IDLE);
            agv.setAvailable(true);
        }
    }
}
