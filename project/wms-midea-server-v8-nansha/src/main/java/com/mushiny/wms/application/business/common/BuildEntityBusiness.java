package com.mushiny.wms.application.business.common;
import com.mushiny.wms.application.domain.*;
import com.mushiny.wms.application.domain.enums.TripState;
import com.mushiny.wms.application.domain.enums.TripType;
import com.mushiny.wms.application.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;


@Component
@Transactional
public class BuildEntityBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildEntityBusiness.class);

    @Autowired
    private TripRepository tripRepository;


    /**
     * 构建调度单
     */
    public Trip buildTrip(Pod pod,
                          MdStationnodeposition workStation,Object instruct,TripType tripType){
        // 生成POD的调度单
        String instructDefault="";
        String stationId="";
        String stationNodePositionId="";
        if(!ObjectUtils.isEmpty(workStation))
        {
            stationId=workStation.getStationnode().getId();
            stationNodePositionId=workStation.getId();
        }



        if(!ObjectUtils.isEmpty(instruct)&&instruct instanceof InboundInstruct)
        {
            instructDefault=(((InboundInstruct) instruct)).getId();
        }else if(!ObjectUtils.isEmpty(instruct)&&instruct instanceof OutboundInstruct)
        {
            instructDefault=(((OutboundInstruct) instruct)).getId();
        }else{
            instructDefault="";
        }
        Trip trip = new Trip();
        trip.setTripType(tripType.getName());
        trip.setTripState(TripState.NEW.getName());
        trip.setPodId(pod.getId());
        trip.setInstruct(instructDefault);
        trip.setWorkStationId(stationId);
        trip.setMdNodePosition(stationNodePositionId);
        trip.setSectionId(pod.getSectionId());
        trip.setWarehouseId(pod.getWarehouseId());
        // 保存调度单
        trip=tripRepository.saveAndFlush(trip);
        LOGGER.debug("生成指令{} pod {} workStation {} Stationnodeposition {}trip {}",instructDefault,pod.getPodIndex(), stationId,stationNodePositionId,trip.getId());
        return trip;
    }

}
