package com.mushiny.zookeeper;
import com.mushiny.WcsApplication;
import com.mushiny.comm.SpringUtil;
import com.mushiny.zookeeper.ConcurrentTest.ConcurrentTask;
import org.springframework.boot.SpringApplication;;
public class ZkTest {
    public static void main2(String[] args) {
        SpringApplication.run(WcsApplication.class, args);

        Runnable task1 = new Runnable(){
            public void run() {
                DistributedLock lock = null;
                try {
                    lock = new DistributedLock(SpringUtil.getBean(ZookeeperConfig.class).getUrl(),"lock");
                    lock.lock();
                    System.out.println("进入锁成功");
                    Thread.sleep(20000);
                    System.out.println("===Thread " + Thread.currentThread().getId() + " running");
                } catch (Exception e) {
                    e.printStackTrace();
                } 
                finally {
                    if(lock != null)
                        lock.unlock();
                }
                
            }
            
        };
        new Thread(task1).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        ConcurrentTask[] tasks = new ConcurrentTask[5];
        for(int i=0;i<tasks.length;i++){
            ConcurrentTask task3 = new ConcurrentTask(){
                public void run() {
                    DistributedLock lock = null;
                    try {
                        lock = new DistributedLock(SpringUtil.getBean(ZookeeperConfig.class).getUrl(),"lock2");
                        lock.lock();
                        System.out.println("Thread " + Thread.currentThread().getId() + " running");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Thread " + Thread.currentThread().getId() + " stop!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } 
                    finally {
                        lock.unlock();
                        System.out.println("Thread " + Thread.currentThread().getId() + " release lock!");
                    }
                    
                }
            };
            tasks[i] = task3;
        }
        new ConcurrentTest(tasks);//执行task
    }

    public static void main(String[] args) {
        SpringApplication.run(WcsApplication.class, args);

        ConcurrentTask[] tasks = new ConcurrentTask[5];
        DistributedLock lock = new DistributedLock(SpringUtil.getBean(ZookeeperConfig.class).getUrl(),"lock2");
        for(int i=0;i<tasks.length;i++){
            ConcurrentTask task3 = new ConcurrentTask(){
                public void run() {
                    try {
                        lock.lock();
                        System.out.println("Thread " + Thread.currentThread().getId() + " running");
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("Thread " + Thread.currentThread().getId() + " stop!");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        lock.unlock();
                        System.out.println("Thread " + Thread.currentThread().getId() + " release lock!");
                    }

                }
            };
            tasks[i] = task3;
        }
        new ConcurrentTest(tasks);//执行task
    }
}