package com.mushiny.wcs.application.service;

import com.mushiny.wcs.application.domain.ReceiveStation;
import com.mushiny.wcs.application.domain.StowStation;

import java.util.List;

public interface StationService {

    List<ReceiveStation> getAllReceiveStations();

    List<StowStation> getAllStowStations();
}
