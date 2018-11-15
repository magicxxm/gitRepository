package com.mushiny.wms.schedule.service.impl;

import com.mushiny.wms.schedule.business.ScanningBusiness;
import com.mushiny.wms.schedule.business.ScheduledBusiness;
import com.mushiny.wms.schedule.domin.WorkStation;
import com.mushiny.wms.schedule.service.PackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PackServiceImpl implements PackService{

    private final ScanningBusiness scanningBusiness;
    private final ScheduledBusiness scheduledBusiness;

    @Autowired
    public PackServiceImpl(ScanningBusiness scanningBusiness,
                           ScheduledBusiness scheduledBusiness) {
        this.scanningBusiness = scanningBusiness;
        this.scheduledBusiness = scheduledBusiness;
    }

    @Override
    public void pack() {
        WorkStation workStation=scanningBusiness.getPackWorkStation();
        scheduledBusiness.pack(workStation);
    }
}
