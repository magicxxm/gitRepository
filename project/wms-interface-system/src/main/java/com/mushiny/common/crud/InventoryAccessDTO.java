package com.mushiny.common.crud;


import java.io.Serializable;

public class InventoryAccessDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code = "0";

    private String message = "Success";

    private int count;

    private String data;

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


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
