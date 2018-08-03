package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.masterdata.obbasics.common.util.ResourceHelper;

public class FacadeException extends Exception {

    private static final long serialVersionUID = 1L;

    public final static String LOCALE_DEFAULT = "EN";

    private String key;
    private Object[] parameters;

    public FacadeException(String msg) {
        super(msg);
    }

    public FacadeException(String msg, String resourceKey, Object[] parameters) {
        super(msg);
        this.key = resourceKey;
        this.parameters = parameters;
    }

    public FacadeException(Throwable t, String resourceKey, Object[] parameters) {
        super(t);
        this.key = resourceKey;
        this.parameters = parameters;
    }

    protected String resolve(String message, String key, Object[] parameters) {
        return resolve(message, key, parameters, LOCALE_DEFAULT);
    }

    protected String resolve(String message, String key, Object[] parameters, String locale) {
        return ResourceHelper.resolve(message, key, parameters, locale);
    }

    @Override
    public String getLocalizedMessage() {
        return resolve(getMessage(), key, parameters);
    }

    @Override
    public String getMessage() {
        String msg = super.getMessage();
        if (msg == null || msg.length() == 0) {
            msg = key + ", " + resolve("", key, parameters);
        }
        return msg;
    }

    public String getLocalizedMessage(String locale) {
        return resolve(getMessage(), key, parameters, locale);
    }

    public String getKey() {
        return key;
    }

    public Object[] getParameters() {
        return parameters;
    }
}
