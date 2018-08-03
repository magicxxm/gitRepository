package com.mushiny.wms.masterdata.obbasics.facade;


import com.mushiny.wms.masterdata.obbasics.common.exception.FacadeException;

public interface PickingCategoryFacade {

    void categorizeCustomerShipment() throws FacadeException;
}
