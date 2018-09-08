package com.mushiny.wms.application.test;


import com.mushiny.wms.application.business.common.RfidBusiness;
import com.mushiny.wms.application.service.impl.PutBackService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingDeque;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/3/18.
 */
@Component
public class CommonTest implements InitializingBean {
    private static final Logger LOGGER= LoggerFactory.getLogger(CommonTest.class);
    @Autowired
    private PutBackService putBackService;
    @Autowired
    private RfidBusiness rfidBusiness;
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolExecutor rfidBusinessExecutor = new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
   public CommonTest(){

   }
  public void start(boolean start)
  {

      if(start)
      {
            if(executor.isShutdown())
            {
                executor=new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

            }
          executor.execute(new Runnable() {
              @Override
              public void run() {

                  try {
                      LOGGER.info("线程--"+Thread.currentThread().getName()+"模拟测试运行");
                      while(Thread.currentThread().isInterrupted()==false) {
                          putBackService.test();
                          TimeUnit.SECONDS.sleep(3);
                      }
                  }catch (Exception e) {
                      LOGGER.error(e.getMessage(),e);
                  }

              }
          });


       }


  }

   public void  autoRemove(boolean start)
   {
       if(start)
       {
           if(rfidBusinessExecutor.isShutdown())
           {
               rfidBusinessExecutor=new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

           }
           rfidBusinessExecutor.execute(new Runnable() {
               @Override
               public void run() {

                   try {
                       LOGGER.info("线程--"+Thread.currentThread().getName()+"自动移除货架运行");
                       while(Thread.currentThread().isInterrupted()==false) {
                           rfidBusiness.autoRemove();
                           TimeUnit.MINUTES.sleep(3);
                       }
                   }catch (Exception e) {
                       LOGGER.error(e.getMessage(),e);
                   }

               }
           });


       }
   }
    public void   autoRemoveStop(){
        while(!rfidBusinessExecutor.isShutdown())
        {
            rfidBusinessExecutor.shutdownNow();
        }
    }

    public void startAll(boolean start)
    {

        if(start)
        {
            if(executor.isShutdown())
            {
                executor=new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());

            }
            executor.execute(new Runnable() {
                @Override
                public void run() {

                    try {
                        LOGGER.info("线程--"+Thread.currentThread().getName()+"模拟测试All运行");
                        while(Thread.currentThread().isInterrupted()==false) {
                            putBackService.testAll();
                            TimeUnit.SECONDS.sleep(3);
                        }
                    }catch (Exception e) {
                        LOGGER.error(e.getMessage(),e);
                    }

                }
            });


        }


    }


  public void stop(){
        while(!executor.isShutdown())
        {
            executor.shutdownNow();
        }

  }


    @Override
    public void afterPropertiesSet() throws Exception {

    }



}
