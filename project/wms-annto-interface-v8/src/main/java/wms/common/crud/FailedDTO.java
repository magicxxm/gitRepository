package wms.common.crud;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FailedDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code = "1";

    private String Msg = "格式错误";

    private List<DataListDTO> data = new ArrayList<>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public List<DataListDTO> getData() {
        return data;
    }

    public void setData(List<DataListDTO> data) {
        this.data = data;
    }
}
