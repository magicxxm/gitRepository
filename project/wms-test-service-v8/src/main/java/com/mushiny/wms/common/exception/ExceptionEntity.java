package com.mushiny.wms.common.exception;

import java.io.Serializable;
import java.util.List;

public class ExceptionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String key;

    private List<Object> values;

    private String message;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
