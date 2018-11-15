package wms.crud.dto;

import javax.sql.rowset.serial.SerialArray;
import java.io.Serializable;

/**
 * Created by 123 on 2017/11/25.
 */
public class ItemResult implements Serializable {

    private String code;

    private String msg;

    private String data;

    public ItemResult() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
