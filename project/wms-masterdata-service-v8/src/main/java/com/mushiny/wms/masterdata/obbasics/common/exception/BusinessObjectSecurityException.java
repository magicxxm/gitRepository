package com.mushiny.wms.masterdata.obbasics.common.exception;


import com.mushiny.wms.masterdata.general.domain.User;

public class BusinessObjectSecurityException extends FacadeException {

    private static final long serialVersionUID = 1L;

    public final static String RESOURCE_KEY = "CLIENT_PERMISSION_DENIED";

    public BusinessObjectSecurityException(User user) {
        super("Security Exception for user " + (user == null ? "" : user.getUsername()),
                RESOURCE_KEY, new Object[]{(user == null ? "" : user.getUsername())});
    }
}
