package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.common.entity.BaseEntity;

public class BusinessObjectCreationException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_CANNOT_BE_CREATED";

    public BusinessObjectCreationException() {
        super("Entity cannot be created", RESOURCE_KEY, new Object[0]);
    }

    public BusinessObjectCreationException(BaseEntity entity) {
        super("Entity cannot be created", RESOURCE_KEY, new Object[]{entity});
    }
}
