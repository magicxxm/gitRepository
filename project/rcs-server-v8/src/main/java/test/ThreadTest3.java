/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author 陈庆余 <13469592826@163.com>
 */
public class ThreadTest3 extends Thread {

    private int threadNo;

    public ThreadTest3(int threadNo) {
        this.threadNo = threadNo;
    }

    /*public static void main(String[] args) throws Exception {
        for (int i = 1; i < 10; i++) {
            new ThreadTest3(i).start();
            Thread.sleep(1);
        }
    }*/

    @Override
    public void run() {
        abc(threadNo);
    }

    public static synchronized void abc(int threadNo) {
        for (int i = 1; i < 10000; i++) {
            System.out.println("No." + threadNo + ":" + i);
        }
    }
}
