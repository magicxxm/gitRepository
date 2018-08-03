package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.common.entity.BaseEntity;

public class BusinessObjectDeleteException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_CANNOT_BE_DELETED";

    public BusinessObjectDeleteException() {
        super("Entity cannot be deleted", RESOURCE_KEY, new Object[0]);
    }

    public BusinessObjectDeleteException(BaseEntity entity) {
        super("Entity cannot be deleted", RESOURCE_KEY, new Object[]{entity});
    }
}
