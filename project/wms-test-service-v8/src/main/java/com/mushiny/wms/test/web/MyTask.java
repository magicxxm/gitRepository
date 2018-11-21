package com.mushiny.wms.test.web;

/**
 * Created by Laptop-9 on 2018/8/15.
 */
public class MyTask implements Runnable{
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
        System.out.println("线程名称"+Thread.currentThread().getName());
    }
}
