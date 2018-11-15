package wms.business.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 123 on 2017/12/7.
 */
public class CydhResult implements Serializable {

    private String code;

    private String msg;

    private List<CydhDTO> data;

    public CydhResult() {
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

    public List<CydhDTO> getData() {
        return data;
    }

    public void setData(List<CydhDTO> data) {
        this.data = data;
    }
}
