package com.mushiny.wms.masterdata.obbasics.common.exception;

public class BusinessObjectQueryException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_QUERY_EXCEPTION";

    public final static String BusinessObjectQueryException = "ENTITY_QUERY_EXCEPTION_CLIENT_CONFLICT";

    /**
     * Creates a new instance of BusinessObjectNotFoundException
     */
    public BusinessObjectQueryException() {
        super("Entity query exception", RESOURCE_KEY, new Object[0]);
    }

    /**
     * Creates a new instance of BusinessObjectQueryException
     */
    public BusinessObjectQueryException(String resourceKey, String param1) {
        super("Entity query exception", resourceKey, new Object[]{param1});
    }
}
