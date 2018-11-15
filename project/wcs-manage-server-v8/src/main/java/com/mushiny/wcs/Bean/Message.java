package com.mushiny.wcs.Bean;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/7.
 */
public class Message {
    private int result = 0;
    private String message;
    private Object data;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
