package wms.crud.dto;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class ReceiverInfoDTO {

    //"shipName": "公司，string (200)",
    private String shipName;

    //"shipToAttentionTo": "联系人，string (50)，必填",
    private String shipToAttentionTo;

    //"shipTomobile": "手机号，string (50)，必填",
    private String shipTomobile;

    //"shipTophoneNum": "电话，string (50)",
    private String shipTophoneNum;

    //"shipToState": "省，string (50)，必填",
    private String shipToState;

    //"shipToCity": "市，string (50)，必填",
    private String shipToCity;

    //"shipToDistrict": "区，string (50)，必填",
    private String shipToDistrict;

    //"shipToAddress1": "详细地址1，string (500)，必填",
    private String shipToAddress1;

    //"shipToAddress2": "详细地址2，string (500) ",
    private String shipToAddress2;

    //"shipToPostalCode": "邮政编码，string (10)"
    private String shipToPostalCode;

    public ReceiverInfoDTO() {
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public String getShipToAttentionTo() {
        return shipToAttentionTo;
    }

    public void setShipToAttentionTo(String shipToAttentionTo) {
        this.shipToAttentionTo = shipToAttentionTo;
    }

    public String getShipTomobile() {
        return shipTomobile;
    }

    public void setShipTomobile(String shipTomobile) {
        this.shipTomobile = shipTomobile;
    }

    public String getShipTophoneNum() {
        return shipTophoneNum;
    }

    public void setShipTophoneNum(String shipTophoneNum) {
        this.shipTophoneNum = shipTophoneNum;
    }

    public String getShipToState() {
        return shipToState;
    }

    public void setShipToState(String shipToState) {
        this.shipToState = shipToState;
    }

    public String getShipToCity() {
        return shipToCity;
    }

    public void setShipToCity(String shipToCity) {
        this.shipToCity = shipToCity;
    }

    public String getShipToDistrict() {
        return shipToDistrict;
    }

    public void setShipToDistrict(String shipToDistrict) {
        this.shipToDistrict = shipToDistrict;
    }

    public String getShipToAddress1() {
        return shipToAddress1;
    }

    public void setShipToAddress1(String shipToAddress1) {
        this.shipToAddress1 = shipToAddress1;
    }

    public String getShipToAddress2() {
        return shipToAddress2;
    }

    public void setShipToAddress2(String shipToAddress2) {
        this.shipToAddress2 = shipToAddress2;
    }

    public String getShipToPostalCode() {
        return shipToPostalCode;
    }

    public void setShipToPostalCode(String shipToPostalCode) {
        this.shipToPostalCode = shipToPostalCode;
    }
}
