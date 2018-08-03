package com.mushiny.wcs.application.business.trip;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.dto.DrivePodScore;
import com.mushiny.wcs.application.business.dto.MixTrip;
import com.mushiny.wcs.application.business.dto.SectionMapNode;
import com.mushiny.wcs.application.business.score.DrivePodScoreBusiness;
import com.mushiny.wcs.application.config.MapNodeConfig;
import com.mushiny.wcs.application.domain.*;
import com.mushiny.wcs.application.domain.enums.PodStateEnum;
import com.mushiny.wcs.application.domain.enums.TripState;
import com.mushiny.wcs.application.domain.enums.TripType;
import com.mushiny.wcs.application.repository.PodRepository;
import com.mushiny.wcs.application.repository.TripRepository;
import com.mushiny.wcs.application.service.impl.DriveAllocationServiceImpl;
import com.mushiny.wcs.common.exception.ApiException;
import com.mushiny.wcs.common.exception.ExceptionEnum;
import com.mushiny.wcs.common.utils.JSONUtil;
import org.hibernate.exception.LockAcquisitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.Map;

/**
 * @author:
 * @Description: Created by wangjianweion 2017/10/21.
 */
@Component
public class CommonTripHandler extends TripHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonTripHandler.class);

    private final PodRepository podRepository;
    private static Map<String, MixTrip> allocateDriverTrips = new HashMap<>();

    private static final String[] commonTripType = new String[]{TripType.POD_SCAN.getName(),
            TripType.PICK_POD.getName(),
            TripType.STOW_POD.getName(),
            TripType.IBP_POD.getName(),
            TripType.OBP_POD.getName(),
            TripType.ICQA_POD.getName(),
            TripType.POD_RUN.getName()
    };

    @Autowired
    public CommonTripHandler(CommonBusiness commonBusiness, TripRepository tripRepository, PodRepository podRepository, DrivePodScoreBusiness drivePodScoreBusiness) {
        super(commonBusiness, tripRepository, drivePodScoreBusiness);
        this.podRepository = podRepository;
    }


    private MixTrip generate(Trip trip, Section section, List<Trip> srcTrip) {
        MixTrip mixTrip = null;

        try{
            Pod pod = podRepository.getOne(trip.getPodId());
            SectionMapNode sectionMapNodeTemp = MapNodeConfig.findSectionMapNode(pod.getPlaceMark(), section.getId());
            if (ObjectUtils.isEmpty(sectionMapNodeTemp)) {
                LOGGER.error("地图不存在pod" + pod.getName() + "地址" + pod.getPlaceMark());
                return mixTrip;
            }
            //如果pod是内部地址
            if (sectionMapNodeTemp.isInAddress()) {
                //外部地址码
                final int placeMark = sectionMapNodeTemp.getOutSizeMapNodes().get(0).getAddressCodeId();

                List<Pod> newPods = podRepository.getByAddressCodeId(placeMark, section.getId());
                Pod newPod=null;
                if(!CollectionUtils.isEmpty(newPods))
                {
                    if(newPods.size()>1)
                    {
                        LOGGER.error("调度单{} pod{} 地址{] 外部地址{} 存在多个pod{}",trip.getId(),pod.getName(),pod.getPlaceMark(),placeMark,JSONUtil.toJSon(newPods) );

                        return null;
                    }else{
                        newPod=newPods.get(0);
                    }
                }

                //判断调度单的目标地址是否在外部pod的位置
                List<Pod> releasedTrargetPod = commonBusiness.releasedTrargetPod(String.valueOf(placeMark), section.getId());


                if (!ObjectUtils.isEmpty(newPod) && CollectionUtils.isEmpty(releasedTrargetPod)) {
                    mixTrip = new MixTrip();
                    mixTrip.setCurrentTrip(trip);
                    List<Trip> searchTrip = commonBusiness.getTrip(newPod.getId(), section.getId());
                    if (CollectionUtils.isEmpty(searchTrip)) {
                        Trip podRunTrip = generateRodRunTrip(newPod, section.getId());
                        mixTrip.setPreTrip(podRunTrip);
                    } else {
                        mixTrip.setPreTrip(searchTrip.get(0));
                    }
                } else if (ObjectUtils.isEmpty(newPod) && ObjectUtils.isEmpty(releasedTrargetPod)) {
                    mixTrip = new MixTrip();
                    mixTrip.setCurrentTrip(trip);
                } else {
                    LOGGER.debug("释放pod{} 的目标地址在{} ", JSONUtil.toJSon(releasedTrargetPod), placeMark);
                }
                if (!ObjectUtils.isEmpty(mixTrip)) {
                    mixTrip.setOutSizeAddrcoed(String.valueOf(placeMark));
                }
            } else {
                mixTrip = new MixTrip();
                mixTrip.setCurrentTrip(trip);
            }
        }catch (Exception e)
        {
           LOGGER.error("处理调度单出错"+JSONUtil.toJSon(trip)+"\n"+e.getMessage(),e);
           throw e;
        }

        return mixTrip;


    }

    private Trip generateRodRunTrip(Pod pod, String sectionId) {
        pod.setState(PodStateEnum.RESERVED.getName());
        Pod podRun = podRepository.saveAndFlush(pod);
        Trip resultTemp = new Trip();
        resultTemp.setTripType(TripType.POD_RUN.getName());
        resultTemp.setTripState(TripState.NEW.getName());
        resultTemp.setPodId(podRun.getId());
        resultTemp.setWarehouseId(podRun.getWarehouseId());
        resultTemp.setSectionId(sectionId);
        Trip result = tripRepository.saveAndFlush(resultTemp);
        return result;
    }

    private Trip searchPod(List<Trip> srcTrip, Pod pod) {
        Trip result = null;
        if (!CollectionUtils.isEmpty(srcTrip)) {
            for (Trip trip : srcTrip) {
                if (trip.getPodId().equals(pod.getId())) {
                    result = trip;
                    break;
                }
            }
        }
        return result;

    }


    private void allocateDriver(Map<MixTrip, List<DrivePodScore>> podScores, List<WCSRobot> robots) {
        for (Map.Entry<MixTrip, List<DrivePodScore>> podScore : podScores.entrySet()) {
                Trip currentTrip = tripRepository.getOne(podScore.getKey().getCurrentTrip().getId());
                if (!ObjectUtils.isEmpty(podScore.getKey().getPreTrip())) {
                    Trip preTrip = tripRepository.getOne(podScore.getKey().getPreTrip().getId());
                    if (preTrip.getTripState().equals(TripState.NEW.getName())) {
                        saveTrip(preTrip, robots);
                    }
                    int targetAdd = Integer.parseInt(podScore.getKey().getOutSizeAddrcoed());
                    // 当被挡的pod移开后
                    List<Pod> podTemp = podRepository.getByTripId(preTrip.getId(), targetAdd);
                    //释放pod的目标地址在被挡的位置
                    List<Pod> releasedTrargetPod = commonBusiness.releasedTrargetPod(String.valueOf(targetAdd), preTrip.getSectionId());

                    if (CollectionUtils.isEmpty(podTemp) && CollectionUtils.isEmpty(releasedTrargetPod)) {
                        if (saveTrip(currentTrip, robots)) {
                            allocateDriverTrips.remove(currentTrip.getId());
                        }

                    }else{
                        LOGGER.debug("处理调度单{}外层地址{}有pod {},释放pod目标地址在外层停止分车{}",currentTrip.getId(),targetAdd,JSONUtil.toJSon(podTemp),releasedTrargetPod);
                    }
                } else {
                    if (saveTrip(currentTrip, robots)) {
                        allocateDriverTrips.remove(currentTrip.getId());
                    }

                }
        }
    }


    @Override
    public List<Trip> filterTrip(List<Trip> trips) {
        return filterHandlerTrip(trips, commonTripType);
    }

    private boolean containTrip(Trip trip) {
        boolean result = false;
        if (!CollectionUtils.isEmpty(allocateDriverTrips)) {
            Collection<MixTrip> trips = allocateDriverTrips.values();
            for (MixTrip temp : trips) {
                if (!ObjectUtils.isEmpty(temp.getPreTrip()) && temp.getPreTrip().getId().equals(trip.getId())) {
                    result = true;
                    break;
                }
                if (temp.getCurrentTrip().getId().equals(trip.getId())) {
                    result = true;
                    break;
                }
            }

        }
        return result;

    }


    private Map<MixTrip, List<DrivePodScore>> getDrivePodScores(List<MixTrip> mixTrips, List<WCSRobot> robots) {
        Map<MixTrip, List<DrivePodScore>> podScores = drivePodScoreBusiness.getDrivePodScores2(mixTrips, robots);
        return podScores;

    }

    @Override
    public void handleTrip(List<Trip> trips, Section section) {
        // 生成podRun 调度单
        if (CollectionUtils.isEmpty(trips)) {
            return;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("开始处理 section {} 调度单\n{}", section.getName(), JSONUtil.toJSon(trips));
            LOGGER.debug("内存中 allocateDriverTrips 状态 \n{}", JSONUtil.toJSon(allocateDriverTrips));
        }


        List<MixTrip> mixTrips = new ArrayList<>();
        for (Trip trip : trips) {


            MixTrip mixTrip = generate(trip, section, trips);
            if (!ObjectUtils.isEmpty(mixTrip)) {
                mixTrips.add(mixTrip);
                allocateDriverTrips.put(trip.getId(), mixTrip);
            }

        }


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("section {} 生成混合调度单\n{}", section.getName(), JSONUtil.toJSon(mixTrips));
        }
        // 获取所有可用的小车
        List<WCSRobot> robots = getAvailableDrives(section.getId());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("当前可用的车为{}", JSONUtil.toJSon(robots));
        }
        if (robots.isEmpty()) {

            return;
        }
        Map<MixTrip, List<DrivePodScore>> podScores = getDrivePodScores(mixTrips, robots);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("调度单对应小车得分为\n{}", JSONUtil.toJSon(podScores));
        }
        if (podScores.isEmpty()) {
            return;
        }

        allocateDriver(podScores, robots);


    }


}
