package com.mushiny.wms.masterdata.obbasics.common.exception;

public class BusinessObjectNotUniqueException extends BusinessObjectNotFoundException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_NOT_UNIQUE";

    /**
     * Creates a new instance of BusinessObjectNotUniqueException
     */
    public BusinessObjectNotUniqueException(String identity) {
        super("Entity not unique", RESOURCE_KEY, identity);
    }
}
