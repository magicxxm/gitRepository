package com.mushiny.service.impl;

import com.mushiny.model.*;
import com.mushiny.repository.*;
import com.mushiny.service.TripCreateService;
import com.mushiny.web.dto.CallPodDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by 123 on 2018/3/27.
 */
@Service
@Transactional
public class TripCreateServiceImpl implements TripCreateService {
    private final Logger logger = LoggerFactory.getLogger(TripCreateServiceImpl.class);

    private final static String PICK_TYPE = "PickPod";
    private final static String ICQA_TYPE = "ICQAPod";
    private final static String STOW_TYPE = "StowPod";
    private final static String POD_RESERVED = "Reserved";
    private final static String EN_ROUTE_MAX_PODS_BINCHENK = "EN_ROUTE_MAX_WORK_BINCHECK";
    private final static String EN_ROUTE_MAX_PODS = "EN_ROUTE_MAX_PODS";

    private final EntityManager manager;
    private final PickStationRepository pickStationRepository;
    private final IcqaStationRepository icqaStationRepository;
    private final PodRepository podRepository;
    private final SystemPropertyRepository systemPropertyRepository;
    private final PQAEnrouteRepositroy pqaEnrouteRepositroy;
    private final PickEnroutedRepository pickEnroutedRepository;
    private final StowStationRepository stowStationRepository;
    private final IBEnrouteRepository ibEnrouteRepository;

    public TripCreateServiceImpl(EntityManager manager,
                                 PickStationRepository pickStationRepository,
                                 IcqaStationRepository icqaStationRepository,
                                 SystemPropertyRepository systemPropertyRepository,
                                 PickEnroutedRepository pickEnroutedRepository,
                                 PQAEnrouteRepositroy pqaEnrouteRepositroy,
                                 StowStationRepository stowStationRepository,
                                 IBEnrouteRepository ibEnrouteRepository,
                                 PodRepository podRepository) {
        this.manager = manager;
        this.icqaStationRepository = icqaStationRepository;
        this.pickStationRepository = pickStationRepository;
        this.podRepository = podRepository;
        this.ibEnrouteRepository = ibEnrouteRepository;
        this.stowStationRepository = stowStationRepository;
        this.pqaEnrouteRepositroy = pqaEnrouteRepositroy;
        this.systemPropertyRepository = systemPropertyRepository;
        this.pickEnroutedRepository = pickEnroutedRepository;
    }

    @Override
    public CallPodDTO createTrip(String podName, String face, String stationName, String type) {

        CallPodDTO callPodDTO = new CallPodDTO();

        //根据工作站name查询具体的工作站
        String workStationId = "";
        String stationId = "";
        if (PICK_TYPE.equalsIgnoreCase(type)) {
            PickStation pickStation = pickStationRepository.getByName(stationName);
            if (pickStation == null) {
                logger.info("工作站: " + stationName + " 不存在");
                callPodDTO.setMessage("工作站: " + stationName + " 不存在");
                callPodDTO.setState("fail");
                return callPodDTO;
            }
            workStationId = pickStation.getWorkStationId();
            stationId = pickStation.getId();
        }

        if (ICQA_TYPE.equalsIgnoreCase(type)) {
            IcqaStation icqaStation = icqaStationRepository.getByName(stationName);
            if (icqaStation == null) {
                logger.info("工作站: " + stationName + " 不存在");
                callPodDTO.setMessage("工作站: " + stationName + " 不存在");
                callPodDTO.setState("fail");
                return callPodDTO;
            }
            workStationId = icqaStation.getWorkStationId();
            stationId = icqaStation.getId();
        }

        if(STOW_TYPE.equalsIgnoreCase(type)){
            StowStation stowStation = stowStationRepository.getByNameAndWarehouseId(stationName);
            if(stowStation == null){
                logger.info("工作站: " + stationName + " 不存在");
                callPodDTO.setMessage("工作站: " + stationName + " 不存在");
                callPodDTO.setState("fail");
                return callPodDTO;
            }
            workStationId = stowStation.getWorkStationId();
            stationId = stowStation.getId();
        }

        //获取Pod信息
        Pod pod = podRepository.getByName(podName);
        if (pod == null) {
            logger.info("Pod: " + podName + " 不存在");
            callPodDTO.setState("fail");
            callPodDTO.setMessage("Pod: " + podName + " 不存在");
            return callPodDTO;
        }
        if (POD_RESERVED.equalsIgnoreCase(pod.getState())) {
            logger.info("Pod: " + podName + " 已被锁定");
            callPodDTO.setState("fail");
            callPodDTO.setMessage("Pod: " + podName + " 已被锁定");
            return callPodDTO;
        }

        //检查pod是否存在调度任务，如果存在，则不能生成
        if(!checkTripByPod(pod)){
            logger.info("Pod: " + podName + " 已存在调度任务");
            callPodDTO.setState("fail");
            callPodDTO.setMessage("Pod: " + podName + " 已存在调度任务");
            return callPodDTO;
        }

        //检查面是否正确
        if(face == null || "".equalsIgnoreCase(face)){
            face = "A";

        }
        String[] faces = face.split(",");
        for(int i=0;i<faces.length;i++){
            if("ABCD".indexOf(faces[i])<0){
                logger.info("输入Pod 的面有误");
                callPodDTO.setState("fail");
                callPodDTO.setMessage("输入Pod的面有误，请重新输入!");
                return callPodDTO;
            }
        }

        //检查该工作站，该pod是否已经存在调度单
        createTripAndPosition(pod.getId(), workStationId, pod.getWarehouse(), pod.getSectionId(), faces, type);

        createEnroutePod(pod.getId(),workStationId,stationId,pod.getWarehouse(),type);

        callPodDTO.setMessage("工作站：" + stationName + " 呼叫pod ：" + podName + " 成功");
        logger.info("工作站：" + stationName + " 呼叫pod ：" + podName + " 成功");

        callPodDTO.setPod(pod.getName());
        callPodDTO.setStationId(stationId);
        callPodDTO.setWorkStationId(workStationId);
        callPodDTO.setSectionId(pod.getSectionId());

        return callPodDTO;

    }

    private boolean checkTripByPod(Pod pod) {
        Query query = manager.createQuery(" SELECT trip FROM "
                + Trip.class.getSimpleName()
                + " trip "
                + " WHERE trip.podId = :pod "
                + " AND trip.tripState NOT IN ('Finish') ");

        query.setParameter("pod", pod.getId());
        List<Trip> trips = query.getResultList();

        //创建调度任务
        if (trips.size() > 0) {
            return false;
        }
        return true;
    }

    private void createEnroutePod(String podId,String workStationId, String stationId, String warehouseId,String type) {
        if(ICQA_TYPE.equalsIgnoreCase(type)){
            //查询ICQA最大工作量
            int maxWorks = (int) systemPropertyRepository.getLong(warehouseId, EN_ROUTE_MAX_PODS_BINCHENK);

            //查询当前pod是否存在恩routePod
            List<PQAEnroutePod> enroutePods = pqaEnrouteRepositroy.getByPodIdAndStationIdAndWarehouesId(podId,workStationId,warehouseId);
            if(enroutePods.isEmpty()){
                PQAEnroutePod pqaEnroutePod = new PQAEnroutePod();
                pqaEnroutePod.setPodId(podId);
                pqaEnroutePod.setStationId(workStationId);
                pqaEnroutePod.setWarehouesId(warehouseId);
                pqaEnroutePod.setWorkLoad(new BigDecimal(maxWorks));
                manager.persist(pqaEnroutePod);
            }

        }

        if(PICK_TYPE.equalsIgnoreCase(type)){
            List<PickEnroutePod> pickEnroutePods = pickEnroutedRepository.getByPodIdAndStationIdAndWarehouseId(podId,stationId,warehouseId);
            if(pickEnroutePods.isEmpty()){
                PickEnroutePod pickEnroutePod = new PickEnroutePod();
                pickEnroutePod.setPodId(podId);
                pickEnroutePod.setStationId(stationId);
                pickEnroutePod.setWarehouseId(warehouseId);
                manager.persist(pickEnroutePod);
            }
        }

        if(STOW_TYPE.equalsIgnoreCase(type)){
            List<IBEnroutePod> enroutePods = ibEnrouteRepository.getByPodIdAndStowStationIdAndWarehouseId(podId,stationId,warehouseId);
            if(enroutePods.isEmpty()){
                IBEnroutePod enroutePod = new IBEnroutePod();
                enroutePod.setPodId(podId);
                enroutePod.setStowStationId(stationId);
                enroutePod.setWarehouseId(warehouseId);
                manager.persist(enroutePod);
            }
        }

    }

    private void createTripAndPosition(String podId, String workStationId, String warehouseId, String sectionId, String[] faces, String tripType){
        Trip trip = new Trip();
        trip.setWorkStationId(workStationId);
        trip.setPodId(podId);
        trip.setSectionId(sectionId);
        trip.setWarehouseId(warehouseId);
        trip.setTripType(tripType);
        manager.persist(trip);

        for (int i = 0;i < faces.length;i++) {
            TripPosition tripPosition = new TripPosition();
            tripPosition.setTripId(trip.getId());
            tripPosition.setWarehouseId(warehouseId);
            tripPosition.setUsePodFace(faces[i]);
            tripPosition.setSectionId(sectionId);
            tripPosition.setPositionNo(i);
            manager.persist(tripPosition);
        }
    }

    /*private void createTripAndPosition(String podId, String workStationId, String warehouseId, String sectionId, String[] faces, String tripType) {
        Trip trip = null;
        //查询当前工作站当前pod是否已经存分配了调度任务
        Query query = manager.createQuery(" SELECT trip FROM "
                + Trip.class.getSimpleName()
                + " trip "
                + " WHERE trip.workStationId = :workStation "
                + " AND trip.podId = :pod "
                + " AND trip.tripType = :tripType "
                + " AND trip.tripState IN ('Available', 'New', 'Process') ");

        query.setParameter("workStation", workStationId);
        query.setParameter("pod", podId);
        query.setParameter("tripType", tripType);
        List<Trip> trips = query.getResultList();

        //创建调度任务
        if (trips.size() > 0) {
            trip = trips.get(0);
        } else {
            trip = new Trip();
            trip.setWorkStationId(workStationId);
            trip.setPodId(podId);
            trip.setSectionId(sectionId);
            trip.setWarehouseId(warehouseId);
            trip.setTripType(tripType);
            manager.persist(trip);
        }

        //创建调度任务明细
        Query query2 = manager.createQuery(" SELECT COUNT(pos) FROM "
                + TripPosition.class.getSimpleName()
                + " pos "
                + " WHERE pos.tripId = :trip ");
        query2 = query2.setParameter("trip", trip.getId());
        int counter = ((Long) query2.getSingleResult()).intValue();
        for (int i = 0;i < faces.length;i++) {
            //根据面查询是否存在正在运行的这个面的调度明细，如果不存在，则新建
            Query query3 = manager.createQuery(" SELECT pos FROM "
                    + TripPosition.class.getSimpleName()
                    + " pos "
                    + " WHERE pos.tripId = :trip "
                    + " AND pos.usePodFace = :face "
                    + " AND pos.trippositionState IN ('Available', 'Process') ");
            query3.setParameter("trip", trip.getId());
            query3.setParameter("face", faces[i]);
            if (query3.getResultList().size() > 0) {
                continue;
            }
            TripPosition tripPosition = new TripPosition();
            tripPosition.setTripId(trip.getId());
            tripPosition.setWarehouseId(warehouseId);
            tripPosition.setUsePodFace(faces[i]);
            tripPosition.setSectionId(sectionId);
            tripPosition.setPositionNo(counter);
            manager.persist(tripPosition);
            counter++;
        }
    }*/

}
