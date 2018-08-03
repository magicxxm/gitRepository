/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.threadtest;

/**
 *
 * @author aricochen
 */
public class ThreadTest {

    private int x = 0;
    private int y = 0;
    int oldX = 0;
    int z=0;
    public void A() {
        x = x++;
         try {
                        Thread.sleep(30);
                    } catch (InterruptedException ie) {
                    }
        y = y++;
    }

    public void B() {
        if (y != x ) {
            System.out.println("数据同步错误!!!,x=" + x + ",y=" + y);
        }
       // System.out.println("z="+z);
    }

    public void dataRun() {
        new Thread() {
            public void run() {
                while (true) {
                    A();
                    B();
                     try {
                        Thread.sleep(23);
                    } catch (InterruptedException ie) {
                    }
                }
            }
        }.start();
    }

    /*public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            ThreadTest test = new ThreadTest();
            test.dataRun();
            
        }
    }*/
}
