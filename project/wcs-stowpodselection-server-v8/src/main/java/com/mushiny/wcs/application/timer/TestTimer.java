
package com.mushiny.wcs.application.timer;

import com.mushiny.wcs.application.business.common.BuildEntityBusiness;
import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.ReceiveStation;
import com.mushiny.wcs.application.domain.StowStation;
import com.mushiny.wcs.application.domain.Trip;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.application.respository.StowStationRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Administrator on 2018/3/29.
 */

@Component
public class TestTimer implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestTimer.class);
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(6, 8, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), new ThreadPoolExecutor.CallerRunsPolicy());
    private  List<StowStation> stowStation=null;
    private  List<Pod> pods=null;
    private volatile int current=0;
    private volatile int index=0;
    private int podsLen=0;
    private int stowStationLen=0;
    private final List<String> faces= Arrays.asList(new String[]{"A","C"});
    @Autowired
    private  BuildEntityBusiness buildEntityBusiness;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private PodRepository podRepository;
    @Autowired
    private StowStationRepository stowStationRepository;

    public void start(String sectionId,String wareHouseId)
    {

        if(executor.isShutdown())
        {
            executor=new ThreadPoolExecutor(3, 6, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        }
        DataCreateRunnable  run=new DataCreateRunnable();
        run.setTask(new DriveAllocationTimer.Task(){

            @Override
            public void run() {
                runAllocation(sectionId,wareHouseId);
            }
        });
        executor.execute(run);


    }


    public void stop(){
        while(!executor.isShutdown())
        {
            executor.shutdownNow();
        }

    }


    public synchronized void runAllocation(String sectionId,String wareHouseId)
    {

        if(!CollectionUtils.isEmpty(stowStation)&&!CollectionUtils.isEmpty(pods))
        {
            if(current>=stowStationLen)
            {
                current=0;
            }
            if(index>=podsLen)
            {
                index=0;
            }
            List<Trip> trips=tripRepository.getTripByPodAndNotFinish(pods.get(index).getId());
            if(CollectionUtils.isEmpty(trips)&&stowStation.get(current).getWorkStation().isCallPod())
            {
                buildEntityBusiness.buildTrip(pods.get(index), stowStation.get(current).getWorkStation(), faces);
            }
            current++;
            index++;


        }
    }


    private void init(String sectionId,String wareHouseId){
        List<StowStation> stowStation=stowStationRepository.findAll();
        pods=podRepository.getAllByPod();
        stowStationLen=stowStation.size();
        podsLen=pods.size();
        LOGGER.debug("加载工作站{}", JSONUtil.toJSon(stowStation));
        LOGGER.debug("加载pod{}", JSONUtil.toJSon(pods));
    }
    @Override
    public void run(String... strings) throws Exception {


    }
}

