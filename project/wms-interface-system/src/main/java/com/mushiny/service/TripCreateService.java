package com.mushiny.service;

import com.mushiny.web.dto.CallPodDTO;

/**
 * Created by 123 on 2018/3/27.
 */
public interface TripCreateService {

    CallPodDTO createTrip(String podName, String face, String stationName, String type);
}
