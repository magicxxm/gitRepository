package com.mushiny.wms.schedule.service.impl;

import com.mushiny.wms.schedule.business.ScanningBusiness;
import com.mushiny.wms.schedule.business.ScheduledBusiness;
import com.mushiny.wms.schedule.service.PickingOrderPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PickingOrderPositionServiceImpl implements PickingOrderPositionService{

    private final ScheduledBusiness scheduledBusiness;
    private final ScanningBusiness scanningBusiness;

    @Autowired
    public PickingOrderPositionServiceImpl(ScheduledBusiness scheduledBusiness,
                                           ScanningBusiness scanningBusiness) {
        this.scheduledBusiness = scheduledBusiness;
        this.scanningBusiness = scanningBusiness;
    }

    @Override
    public void createOrder() {
        scheduledBusiness.createOrder();
    }

    @Override
    public void splitCustomerOrder() {
        scheduledBusiness.splitCustomerOrder();
    }

    @Override
    public void loginPickStation() {
        scanningBusiness.scanningPickStation();
    }

    @Override
    public void picking() {
        scheduledBusiness.picking();
    }
}
