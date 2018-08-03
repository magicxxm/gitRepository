package com.mushiny.redis;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;
    private final DistributedLocker distributedLocker;

    public Worker(CountDownLatch startSignal, CountDownLatch doneSignal,DistributedLocker distributedLocker) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        this.distributedLocker = distributedLocker;
    }

    public void run() {
        try {
            //startSignal.await();
            distributedLocker.lock("test",new AquiredLockWorker<Object>() {

                @Override
                public Object invokeAfterLockAquire() {
                    doTask();
                    return null;
                }

            });
        }catch (Exception e){

        }
    }

    void doTask() {
        System.out.println(Thread.currentThread().getName() + " start");
        Random random = new Random();
        int _int = random.nextInt(2000);
        System.out.println(Thread.currentThread().getName() + " sleep " + _int + "millis");
        try {
            Thread.sleep(_int+20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end");
        //doneSignal.countDown();
    }
}