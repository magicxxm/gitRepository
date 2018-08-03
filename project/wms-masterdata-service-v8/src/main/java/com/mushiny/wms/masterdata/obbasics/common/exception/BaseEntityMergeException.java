package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.common.entity.BaseEntity;

public class BaseEntityMergeException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public static final String RESOURCE_KEY = "ENTITY_CANNOT_BE_MERGED";

    public BaseEntityMergeException(BaseEntity from, BaseEntity to) {
        super("Entity can't be merged", RESOURCE_KEY, new String[]{from.toUniqueString(), to.toUniqueString()});
    }
}
