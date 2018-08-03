package com.mushiny.wms.masterdata.obbasics.common.exception;

public class BusinessObjectNotFoundException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "ENTITY_NOT_FOUND";

    /**
     * Creates a new instance of BusinessObjectNotFoundException
     */
    public BusinessObjectNotFoundException() {
        super("Entity not found", RESOURCE_KEY, new Object[0]);
    }

    public BusinessObjectNotFoundException(String id, Class<? extends Object> boClass) {
        super("Entity not found", RESOURCE_KEY, new Object[]{id, boClass.getSimpleName()});
    }

    public BusinessObjectNotFoundException(String identity) {
        super("Entity not found", RESOURCE_KEY, new Object[]{identity});
    }

    /**
     * Creates a new instance of BusinessObjectNotFoundException
     */
    public BusinessObjectNotFoundException(String msg, String key, String identity) {
        super(msg, key, new Object[]{identity});
    }
}
