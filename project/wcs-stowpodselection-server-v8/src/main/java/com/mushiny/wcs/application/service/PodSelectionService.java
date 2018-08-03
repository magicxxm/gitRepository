package com.mushiny.wcs.application.service;

import com.mushiny.wcs.application.domain.ReceiveStation;
import com.mushiny.wcs.application.domain.StowStation;

public interface PodSelectionService {

    void callReceiveStationPods(ReceiveStation receiveStation);

    void callStowStationPods(StowStation stowStation);
}
