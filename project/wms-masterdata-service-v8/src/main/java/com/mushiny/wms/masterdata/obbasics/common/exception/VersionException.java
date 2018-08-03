package com.mushiny.wms.masterdata.obbasics.common.exception;

public class VersionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VersionException(String entityName) {
        super(entityName);
    }
}
