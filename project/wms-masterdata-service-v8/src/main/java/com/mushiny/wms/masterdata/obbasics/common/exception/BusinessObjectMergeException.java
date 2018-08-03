package com.mushiny.wms.masterdata.obbasics.common.exception;

import com.mushiny.wms.common.entity.BaseEntity;

public class BusinessObjectMergeException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_CANNOT_BE_MERGED";

    public BusinessObjectMergeException() {
        super("Entity cannot be merged", RESOURCE_KEY, new Object[0]);
    }

    public BusinessObjectMergeException(BaseEntity entity) {
        super("Entity cannot be merged", RESOURCE_KEY, new Object[]{entity});
    }
}
