package com.mushiny.auth.service.exception;

public class VersionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VersionException(String entityName) {
        super(entityName);
    }
}
