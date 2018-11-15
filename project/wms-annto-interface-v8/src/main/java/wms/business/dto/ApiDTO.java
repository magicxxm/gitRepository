package wms.business.dto;

import net.sf.json.JSONObject;
import net.sf.json.JSONString;

import java.io.Serializable;

/**
 * Created by 123 on 2017/9/12.
 */
public class ApiDTO implements Serializable {

    private String api;//具体的接口名

    private String v = "2.0";//版本

    private String data;//具体数据实体类的json格式
//    private JSONObject data;

//    private String sign;//签名

    private String url;//请求的接口url

    private String format = "json";

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

//    public String getSign() {
//        return sign;
//    }
//
//    public void setSign(String sign) {
//        this.sign = sign;
//    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
