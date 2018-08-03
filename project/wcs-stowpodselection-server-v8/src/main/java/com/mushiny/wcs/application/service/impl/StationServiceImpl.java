package com.mushiny.wcs.application.service.impl;

import com.mushiny.wcs.application.business.callPods.CommonCallPodBusiness;
import com.mushiny.wcs.application.domain.ReceiveStation;
import com.mushiny.wcs.application.domain.StowStation;
import com.mushiny.wcs.application.respository.ReceiveStationRepository;
import com.mushiny.wcs.application.respository.StowStationRepository;
import com.mushiny.wcs.application.service.StationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationServiceImpl implements StationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StationServiceImpl.class);
    private final ReceiveStationRepository receiveStationRepository;
    private final StowStationRepository stowStationRepository;

    @Autowired
    public StationServiceImpl(StowStationRepository stowStationRepository,
                              ReceiveStationRepository receiveStationRepository) {
        this.stowStationRepository = stowStationRepository;
        this.receiveStationRepository = receiveStationRepository;
    }

    @Override
    public List<ReceiveStation> getAllReceiveStations() {
        return receiveStationRepository.getAll();
    }

    @Override
    public List<StowStation> getAllStowStations() {
        return stowStationRepository.getAll();
    }
}
