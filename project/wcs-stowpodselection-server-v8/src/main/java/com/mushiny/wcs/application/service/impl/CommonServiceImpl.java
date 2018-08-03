package com.mushiny.wcs.application.service.impl;

import com.mushiny.wcs.application.business.callPods.CallPodBusiness;
import com.mushiny.wcs.application.business.dto.CallPod;
import com.mushiny.wcs.application.business.dto.CallStation;
import com.mushiny.wcs.application.business.enums.StationType;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.exception.CustomException;
import com.mushiny.wcs.application.respository.*;
import com.mushiny.wcs.application.service.CommonService;
import com.mushiny.wcs.common.exception.ApiException;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CommonServiceImpl implements CommonService {
    private  Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);
    private final OBPStationRepository obpStationRepository;
    private final IbpStationRepository ibpStationRepository;
    private final StocktakingStationRepository stocktakingStationRepository;
    private final ThreadPoolTaskExecutor commonExecutor;
    private final WorkStationRepository workStationRepository;
    private final PodRepository podRepository;
    private final TripRepository tripRepository;

    @Autowired
    public CommonServiceImpl(OBPStationRepository obpStationRepository,
                             IbpStationRepository ibpStationRepository,
                             StocktakingStationRepository stocktakingStationRepository,
                             WorkStationRepository workStationRepository,
                             PodRepository podRepository,
                             TripRepository tripRepository,
                             ThreadPoolTaskExecutor commonExecutor) {
        this.obpStationRepository = obpStationRepository;

        this.ibpStationRepository = ibpStationRepository;
        this.stocktakingStationRepository = stocktakingStationRepository;
        this.commonExecutor = commonExecutor;
        this.workStationRepository = workStationRepository;
        this.podRepository = podRepository;
        this.tripRepository = tripRepository;
    }

    public void execute(Map<String, Object> datas) {

        commonExecutor.execute(new Runnable() {

            @Override
            public void run() {

                initScheduler(datas);

            }
        });

    }

    private void initScheduler(Map datas) {

        if (!CollectionUtils.isEmpty(datas)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("开始处理呼叫pod请求 \n{}", JSONUtil.toJSon(datas));
            }
            String workStationId = (String) datas.get("workStationId");

            CallStation callStation = generateCallStation(workStationId);
            List<Map<String, String>> pods = (List) datas.get("pods");
            List<CallPod> callPods = createCallPod(pods,callStation);
            if(!CollectionUtils.isEmpty(callPods))
            {
                callPods.sort(Comparator.comparing(CallPod::getScore));
            }

            scheduler(callStation, callPods);

        }


    }


    private BigDecimal getScore(Pod pod ,WorkStation workStation){
        BigDecimal score=BigDecimal.ZERO;
        if(!ObjectUtils.isEmpty(pod)&&!ObjectUtils.isEmpty(workStation))
        {
            score = BigDecimal.valueOf(
                    Math.abs(workStation.getxPos() - pod.getxPos())
                            + Math.abs(workStation.getyPos() - pod.getyPos()));
        }

        return score;
    }
    private void scheduler(CallStation callStation, List<CallPod> callPods) {

        List<CallPod> callPodsTemp = new ArrayList<>(6);
        callPodsTemp.addAll(callPods);
        if(CollectionUtils.isEmpty(callPodsTemp))
        {
            LOGGER.debug("接受到icqa的pod 为空,停止生成调度单");
            return;
        }
        boolean stop = false;
        while (!CollectionUtils.isEmpty(callPodsTemp)) {
            try {
                if (stop) {
                    break;
                }
                Iterator<CallPod> iter = callPodsTemp.iterator();
                while (iter.hasNext()) {
                    if (isStopCall(callStation)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("工作站 {} 停止呼叫pod", callStation.getWorkStation().getId());
                        }
                        stop = true;
                        break;
                    }
                    CallPod callPod = iter.next();
                    Trip trip = tripRepository.getTripByPodId(callPod.getPod().getId(),callStation.getStationType().getName());
                    if (!ObjectUtils.isEmpty(trip)) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("已经存在pod{} 的调度单{}", callPod.getPod().getName(), trip.getId());
                        }
                        iter.remove();
                        continue;
                    }
                    boolean result = callPods(callStation, callPod);
                    if (result) {
                        iter.remove();

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("工作站{}生成pod {} 调度单成功\n 剩余{} 个pod调动单需要生成,分别为{}", callStation.getWorkStation().getId(), callPod.getPod().getId(), callPodsTemp.size(), JSONUtil.toJSon(callPodsTemp));
                        }

                    }
                }
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(),e);
            }
        }


    }

    public boolean callPods(CallStation CallStation, CallPod callPod) {
        StationType stationType = CallStation.getStationType();
        CallPodBusiness callPodBusiness = CallPodBussnessFactory.getCallPodBusiness(stationType);
        return callPodBusiness.callPods(CallStation, callPod);
    }

    private boolean isStopCall(CallStation callStation) {
        boolean isStopCall = false;
        if (!workStationRepository.findOne(callStation.getWorkStation().getId()).isCallPod()) {
            isStopCall = true;
        }
        return isStopCall;
    }

    private CallStation generateCallStation(String stationId) {
        CallStation callStation = new CallStation();
        WorkStation workStation = null;
        StationType stationType = null;
        Object station = obpStationRepository.findOne(stationId);
        if (!ObjectUtils.isEmpty(station)) {
            workStation = ((OBPStation) station).getWorkStation();
            stationType = StationType.OBP;

        } else if (!ObjectUtils.isEmpty(station = ibpStationRepository.findOne(stationId))) {
            workStation = ((IbpStation) station).getWorkStation();
            stationType = StationType.IBP;
        } else if (!ObjectUtils.isEmpty(station = stocktakingStationRepository.findOne(stationId))) {
            workStation = ((StocktakingStation) station).getWorkstation();
            stationType = StationType.ICQA;
        }
        if (ObjectUtils.isEmpty(workStation)) {
            throw new ApiException(CustomException.EX_SPS_STATION_NOT_FOUND.toString());
        }
        callStation.setLogicalStation(stationId);
        callStation.setWorkStation(workStation);
        callStation.setStationType(stationType);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成CallStation \n{}", JSONUtil.toJSon(callStation));
        }
        return callStation;
    }

    private List<CallPod> createCallPod(List<Map<String, String>> pods,CallStation callStation) {
        List<CallPod> callPods = new ArrayList<>(6);
        for (Map<String, String> pod : pods) {
            final String podId = pod.get("podId");
            final String face = pod.get("face");
            boolean contain = false;
            for (CallPod callPodTemp : callPods) {
                if (callPodTemp.getPod().getId().equals(podId)) {
                    if (!callPodTemp.getPodFaces().contains(face)) {
                        callPodTemp.getPodFaces().add(face);

                    }
                    contain = true;
                    break;

                }
            }
            if (!contain) {
                CallPod callPod = new CallPod();
                Pod podTemp = podRepository.findOne(podId);
                callPod.setPod(podTemp);
                callPod.setScore(getScore(podTemp,callStation.getWorkStation()));
                callPod.getPodFaces().add(face);
                callPods.add(callPod);
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("生成CallPod \n{}", JSONUtil.toJSon(callPods));
        }
        return callPods;
    }

}
