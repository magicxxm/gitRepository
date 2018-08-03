package com.mushiny.wms.masterdata.obbasics.common.web.error;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public final class ValidationErrorDTO {

    private static final long serialVersionUID = 1L;

    private final String code = HttpStatus.BAD_REQUEST.name();

    private final List<FieldErrorDTO> fieldErrors = new ArrayList<>();

    public ValidationErrorDTO() {
    }

    void addFieldError(String field, String code, String message) {
        FieldErrorDTO error = new FieldErrorDTO(field, code, message);
        fieldErrors.add(error);
    }

    public String getCode() {
        return code;
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
