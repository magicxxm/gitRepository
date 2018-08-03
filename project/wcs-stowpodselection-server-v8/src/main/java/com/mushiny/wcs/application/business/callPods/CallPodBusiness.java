package com.mushiny.wcs.application.business.callPods;

import com.mushiny.wcs.application.business.dto.CallPod;
import com.mushiny.wcs.application.business.dto.CallStation;
import com.mushiny.wcs.application.business.enums.StationType;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/10/12.
 */
public interface CallPodBusiness {
    boolean callPods(CallStation CallStation, CallPod callPod);
    StationType getStationType();
}
