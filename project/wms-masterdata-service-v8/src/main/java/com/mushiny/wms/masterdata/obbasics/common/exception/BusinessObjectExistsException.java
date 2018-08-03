package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.common.entity.BaseEntity;

public class BusinessObjectExistsException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_ALREADY_EXISTS";

    public BusinessObjectExistsException() {
        super("Entity already exists", RESOURCE_KEY, new Object[0]);
    }

    public BusinessObjectExistsException(BaseEntity entity) {
        super("Entity already exists", RESOURCE_KEY, new Object[]{entity});
    }
}
