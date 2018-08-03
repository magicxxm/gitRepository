package com.mushiny.comm;

/**
 * Created by Tank.li on 2017/10/25.
 */
public class TestThread extends Thread{
    private BizActor bizActor = new BizActor();
    private Agv agv;

    public Agv getAgv() {
        return agv;
    }

    public void setAgv(Agv agv) {
        this.agv = agv;
    }

    public TestThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        bizActor.action(agv);
    }
}
