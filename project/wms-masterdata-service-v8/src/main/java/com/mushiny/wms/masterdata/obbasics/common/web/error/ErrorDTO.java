package com.mushiny.wms.masterdata.obbasics.common.web.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class ErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String message;

    private List<FieldErrorDTO> fieldErrors;

    public ErrorDTO(String message) {
        this(null, message);
    }

    public ErrorDTO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void add(String field, String code, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorDTO(field, code, message));
    }
}
