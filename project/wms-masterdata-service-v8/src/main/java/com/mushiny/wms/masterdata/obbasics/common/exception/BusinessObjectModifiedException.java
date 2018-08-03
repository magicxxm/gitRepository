package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.common.entity.BaseEntity;

public class BusinessObjectModifiedException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_CANNOT_BE_MODIFIED";

    public BusinessObjectModifiedException(BaseEntity entity) {
        super("Entity can not be modified", RESOURCE_KEY, new Object[]{entity});
    }
}
