package com.mushiny.wcs.application.business.callPods;

import com.mushiny.wcs.application.business.common.CommonBusiness;
import com.mushiny.wcs.application.business.common.SystemPropertyBusiness;
import com.mushiny.wcs.application.business.enums.StationType;
import com.mushiny.wcs.application.respository.PodRepository;
import com.mushiny.wcs.application.respository.PqaEnroutePodRepository;
import com.mushiny.wcs.application.respository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
@Component
public class ObpCallPodBusiness extends IbpCallPodBusiness {
    @Autowired
    public ObpCallPodBusiness(PodRepository podRepository,
                              SystemPropertyBusiness systemPropertyBusiness,
                              CommonBusiness commonBusiness,
                              TripRepository tripRepository,
                              PqaEnroutePodRepository pqaEnroutePodRepository) {
        super(podRepository,systemPropertyBusiness,commonBusiness,tripRepository,pqaEnroutePodRepository);

    }


    @Override
    public StationType getStationType() {
        return StationType.OBP;
    }
}
