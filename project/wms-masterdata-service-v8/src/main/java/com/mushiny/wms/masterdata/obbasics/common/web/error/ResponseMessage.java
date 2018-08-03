package com.mushiny.wms.masterdata.obbasics.common.web.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Type {
        SUCCESS, WARN, ERROR, INFO
    }

    private Type type;
    private String code;
    private String message;

    public ResponseMessage(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public ResponseMessage(Type type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public static ResponseMessage success(String message) {
        return new ResponseMessage(Type.SUCCESS, message);
    }

    public static ResponseMessage warning(String message) {
        return new ResponseMessage(Type.WARN, message);
    }

    public static ResponseMessage danger(String message) {
        return new ResponseMessage(Type.ERROR, message);
    }

    public static ResponseMessage info(String message) {
        return new ResponseMessage(Type.INFO, message);
    }

    private List<Error> errors = new ArrayList<Error>();

    public List<Error> getErrors() {
        return errors;
    }

    public void setErrors(List<Error> errors) {
        this.errors = errors;
    }

    public void addError(String field, String code, String message) {
        this.errors.add(new Error(field, code, message));
    }

    class Error {
        private String code;
        private String message;
        private String field;

        private Error(String field, String code, String message) {
            this.field = field;
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }
    }
}
