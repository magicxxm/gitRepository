package com.mushiny;

import com.mushiny.redis.AquiredLockWorker;
import com.mushiny.redis.RedisLocker;
import com.mushiny.redis.Worker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Tank.li on 2017/7/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WcsApplication.class)
public class DistributeLockerTest {

    @Autowired
    RedisLocker distributedLocker;

    @Test
    public void testRedlock() throws Exception{
        while(true){
            run();
        }
    }

    public void run(){
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
            Thread.sleep(_int+5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " end");
        //doneSignal.countDown();
    }


}
