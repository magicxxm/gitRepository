package com.mushiny.wms.schedule.service.impl;

import com.mushiny.wms.schedule.business.ScanningBusiness;
import com.mushiny.wms.schedule.business.ScheduledBusiness;
import com.mushiny.wms.schedule.service.StowGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StowGoodsServiceImpl implements StowGoodsService{

    private final ScanningBusiness scanningBusiness;
    private final ScheduledBusiness scheduledBusiness;

    @Autowired
    public StowGoodsServiceImpl(ScanningBusiness scanningBusiness,
                                ScheduledBusiness scheduledBusiness) {
        this.scanningBusiness = scanningBusiness;
        this.scheduledBusiness = scheduledBusiness;
    }

    @Override
    public void loginStation() {
        scanningBusiness.scanningStowStation();
    }

    @Override
    public void stow() {
        scheduledBusiness.inboundStow();
    }
}
