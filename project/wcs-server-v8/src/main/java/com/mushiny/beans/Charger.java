package com.mushiny.beans;

/**
 * Created by Tank.li on 2017/8/7.
 */
public class Charger extends BaseObject{
    public static final String TABLE_NAME="MD_CHARGER";
    public static final String ID_NAME="ID";
    /*statusIndex:1, 2, 3, 4
     statusName:充电, 空闲, 离线, 故障*/
    public static final int CHARGING = 1;
    public static final int IDLE = 2;
    public static final int OFFLINE = 3;
    public static final int ERROR = 4;
    private String chargerId;
    private String addressCodeId;
    private int direct;
    private String name;
    private String state;
    private String wareHouseId;
    private String sectionId;

    //美的的charger包含Mac地址
    private String mac;
    private String chargerAddr;//充电桩实际地址

    private Integer rcsChargerId;
    private Integer chargerType;

    public String getChargerAddr() {
        return chargerAddr;
    }

    public void setChargerAddr(String chargerAddr) {
        this.chargerAddr = chargerAddr;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getRcsChargerId() {
        return rcsChargerId;
    }

    public void setRcsChargerId(Integer rcsChargerId) {
        this.rcsChargerId = rcsChargerId;
    }

    public Integer getChargerType() {
        return chargerType;
    }

    public void setChargerType(Integer chargerType) {
        this.chargerType = chargerType;
    }

    public String getWareHouseId() {
        return wareHouseId;
    }

    public void setWareHouseId(String wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public String getAddressCodeId() {
        return addressCodeId;
    }

    public void setAddressCodeId(String addressCodeId) {
        this.addressCodeId = addressCodeId;
    }

    public int getDirect() {
        return direct;
    }

    public void setDirect(int direct) {
        this.direct = direct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getId() {
        return this.getChargerId();
    }

    @Override
    public String getTable() {
        return TABLE_NAME;
    }

    @Override
    public String getIdName() {
        return ID_NAME;
    }

    @Override
    public String toString() {
        return "chargerId:"+chargerId+" ,chargerName:"+name+" ,rcsChargerId:"+rcsChargerId+" ,chargerType:"+chargerType+" ,chargerState:"+state;
    }
}
