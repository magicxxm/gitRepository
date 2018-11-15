package com.mushiny.wms.schedule.service;

public interface PickingOrderPositionService {

    void createOrder();

    void splitCustomerOrder();

    void loginPickStation();

    void picking();

}
