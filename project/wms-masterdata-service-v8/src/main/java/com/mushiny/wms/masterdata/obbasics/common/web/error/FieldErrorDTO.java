package com.mushiny.wms.masterdata.obbasics.common.web.error;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class FieldErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Name of the field. Null in case of a form level error.
    private final String field;

    // Error code. Typically the I18n message-code.
    private final String code;

    // Error message
    private final String message;

    public FieldErrorDTO(String field, String code, String message) {
        this.field = field;
        this.code = code;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "FieldError {field=" + field + ", code=" + code + ", message=" + message + "}";
    }

    /**
     * Converts a set of ConstraintViolations
     * to a list of FieldErrors
     *
     * @param constraintViolations
     */
    public static List<FieldErrorDTO> getErrors(Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(FieldErrorDTO::of).collect(Collectors.toList());
    }

    /**
     * Converts a ConstraintViolation
     * to a FieldError
     */
    private static FieldErrorDTO of(ConstraintViolation<?> constraintViolation) {
        // Get the field name by removing the first part of the propertyPath.
        // (The first part would be the service method name)
        String field = StringUtils.substringAfter(constraintViolation.getPropertyPath().toString(), ".");

        return new FieldErrorDTO(field,
                constraintViolation.getMessageTemplate(),
                constraintViolation.getMessage());
    }
}
