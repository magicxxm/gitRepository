package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by 123 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_SKU")
public class AnntoItem extends BaseEntity {

    //"Code": "商品编码，string (50)，必填",
    @Column(name = "CODE",nullable = false)
    private String code;

    //"warehouseCode": "仓库编码，string (50)，必填",
    @Column(name = "WAREHOUSE_CODE",nullable = false)
    private String warehouseCode;

    //"warehouseCode": "仓库编码，string (50)，必填",
    @Column(name = "COMPANY_CODE",nullable = false)
    private String companyCode;

    //"Name": "商品名称，string (200)，必填",
    @Column(name = "NAME",nullable = false)
    private String name;

    //是否是有效期商品
    @Column(name = "LOT_MANDATORY")
    private String lotMandatory;

    //"daysToExpire": "保质期天数，int，默认 0",
    @Column(name = "DAYS_TO_EXPIRE")
    private int daysToExpire = 0;

    //有效期单位
    @Column(name = "EXPIRE_UNIT")
    private String expireUnit;

    //有效期类型
    @Column(name = "EXPIRE_TYPE")
    private String expireType;

    //拒收天数
    @Column(name = "REJECT_DAYS")
    private int rejectDays;

    //"expiringDays": "近效期天数，int",
    @Column(name = "EXPIRING_DAYS")
    private int expiringDays = 0;

    //"class01": "大类，string (200)，必填",
    @Column(name = "CLASS01")
    private String class01;

    //"class02": "中类，string (200)，必填",
    @Column(name = "CLASS02")
    private String class02;

    //"class03": "小类，string (200)，必填",
    @Column(name = "CLASS03")
    private String class03;

    //"trackSerialNum": "记录序列号，int， 0:不记录；1:记录",
    @Column(name = "TRACK_SERIAL_NUM")
    private int trackSerialNum = 0;

    //序列号规则
    @Column(name = "TRACK_SERIAL_REGULAR")
    private String trackSerialRegular;

    //"brand": "品牌，string (50) ",
    @Column(name = "BRAND")
    private String brand;

    //"ItemSize": "尺寸，string (50) ",
    @Column(name = "ITEM_SIZE")
    private String itemSize;

    //"ItemColor": "颜色，string (50) ",
    @Column(name = "ITEM_COLOR")
    private String itemColor;

    //"ItemStyle": "规格，string (50) ",
    @Column(name = "ITEM_STYLE")
    private String itemStyle;

    //"placeOfOrigin": "产地，string (50) ",
    @Column(name = "PLACE_OF_ORIGIN")
    private String placeOfOrigin;

    //"unitDesc": "最小数量单位名称，string (50),默认 个 ",
    @Column(name = "UNIT_DESC")
    private String unitDesc = "个";

    //"unitLength": "最小单位长度，numeric(9,2) ",
    @Column(name = "UNIT_LENGTH")
    private BigDecimal unitLength;

    //"unitWidth": "最小单位宽度，numeric(9,2) ",
    @Column(name = "UNIT_WIDTH")
    private BigDecimal unitWidth;

    //"unitHeight": "最小单位高度，numeric(9,2) ",
    @Column(name = "UNIT_HEIGHT")
    private BigDecimal unitHeight;

    //"unitWeight": "最小单位重量，numeric(9,2) ",
    @Column(name = "UNIT_WEIGHT")
    private BigDecimal unitWeight;

    //"unitVolume": "最小单位体积，numeric(9,2) ",
    @Column(name = "UNIT_VOLUME")
    private BigDecimal unitVolume;

    //"csQTY": "包装箱数量，int，默认 0",
    @Column(name = "CS_QTY")
    private int csQTY = 0;

    //"csDesc": "箱名称，string(50)，默认 0",
    @Column(name = "CS_DESC")
    private String csDesc = "0";

    //"csLength": "箱长度，numeric(9,2)，默认 0",
    @Column(name = "CS_LENGTH")
    private BigDecimal csLength = BigDecimal.ZERO;

    //"csWidth": "箱宽度，numeric(9,2)，默认 0",
    @Column(name = "CS_WIDTH")
    private BigDecimal csWidth = BigDecimal.ZERO;

    //"csHeight": "箱高度，numeric(9,2)，默认 0",
    @Column(name = "CS_HEIGHT")
    private BigDecimal csHeight = BigDecimal.ZERO;

    //"csWeight": "箱重量，numeric(9,2)，默认 0",
    @Column(name = "CS_WEIGHT")
    private BigDecimal csWeight = BigDecimal.ZERO;

    //"csVolume": "箱体积，numeric(9,2)，默认 0",
    @Column(name = "CS_VOLUME")
    private BigDecimal csVolume = BigDecimal.ZERO;

    //"plQTY": "托盘包装数，int，默认 0",
    @Column(name = "PL_QTY")
    private int plQTY = 0;

    //"plDesc": "托盘名称，string(50)，默认  托",
    @Column(name = "PL_DESC")
    private String plDesc = "托";

    //是否测量
    @Column(name = "IS_MEASURE")
    private String isMeasure;

    //是否自带包装
    @Column(name = "IS_ORIGIN_PACKING")
    private String isOriginPacking;

    //是否使用袋子
    @Column(name = "IS_PLASTIC_FIRST")
    private String isPlasticFirst;

    //是否需要气垫膜
    @Column(name = "IS_NEED_CUSHION")
    private String isNeedCushion;

    //包装箱条码
    @Column(name = "BARCODE")
    private String barcode;

    //数量单位，默认 EA
    @Column(name = "QUANTITY_UM")
    private String quantityUM;

    public AnntoItem() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLotMandatory() {
        return lotMandatory;
    }

    public void setLotMandatory(String lotMandatory) {
        this.lotMandatory = lotMandatory;
    }

    public int getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(int daysToExpire) {
        this.daysToExpire = daysToExpire;
    }

    public int getExpiringDays() {
        return expiringDays;
    }

    public void setExpiringDays(int expiringDays) {
        this.expiringDays = expiringDays;
    }

    public String getClass01() {
        return class01;
    }

    public void setClass01(String class01) {
        this.class01 = class01;
    }

    public String getClass02() {
        return class02;
    }

    public void setClass02(String class02) {
        this.class02 = class02;
    }

    public String getClass03() {
        return class03;
    }

    public void setClass03(String class03) {
        this.class03 = class03;
    }

    public int getTrackSerialNum() {
        return trackSerialNum;
    }

    public void setTrackSerialNum(int trackSerialNum) {
        this.trackSerialNum = trackSerialNum;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public String getItemStyle() {
        return itemStyle;
    }

    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getUnitDesc() {
        return unitDesc;
    }

    public void setUnitDesc(String unitDesc) {
        this.unitDesc = unitDesc;
    }

    public BigDecimal getUnitLength() {
        return unitLength;
    }

    public void setUnitLength(BigDecimal unitLength) {
        this.unitLength = unitLength;
    }

    public BigDecimal getUnitWidth() {
        return unitWidth;
    }

    public void setUnitWidth(BigDecimal unitWidth) {
        this.unitWidth = unitWidth;
    }

    public BigDecimal getUnitHeight() {
        return unitHeight;
    }

    public void setUnitHeight(BigDecimal unitHeight) {
        this.unitHeight = unitHeight;
    }

    public BigDecimal getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(BigDecimal unitWeight) {
        this.unitWeight = unitWeight;
    }

    public BigDecimal getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(BigDecimal unitVolume) {
        this.unitVolume = unitVolume;
    }

    public int getCsQTY() {
        return csQTY;
    }

    public void setCsQTY(int csQTY) {
        this.csQTY = csQTY;
    }

    public String getCsDesc() {
        return csDesc;
    }

    public void setCsDesc(String csDesc) {
        this.csDesc = csDesc;
    }

    public BigDecimal getCsLength() {
        return csLength;
    }

    public void setCsLength(BigDecimal csLength) {
        this.csLength = csLength;
    }

    public BigDecimal getCsWidth() {
        return csWidth;
    }

    public void setCsWidth(BigDecimal csWidth) {
        this.csWidth = csWidth;
    }

    public BigDecimal getCsHeight() {
        return csHeight;
    }

    public void setCsHeight(BigDecimal csHeight) {
        this.csHeight = csHeight;
    }

    public BigDecimal getCsWeight() {
        return csWeight;
    }

    public void setCsWeight(BigDecimal csWeight) {
        this.csWeight = csWeight;
    }

    public BigDecimal getCsVolume() {
        return csVolume;
    }

    public void setCsVolume(BigDecimal csVolume) {
        this.csVolume = csVolume;
    }

    public int getPlQTY() {
        return plQTY;
    }

    public void setPlQTY(int plQTY) {
        this.plQTY = plQTY;
    }

    public String getPlDesc() {
        return plDesc;
    }

    public void setPlDesc(String plDesc) {
        this.plDesc = plDesc;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantityUM() {
        return quantityUM;
    }

    public void setQuantityUM(String quantityUM) {
        this.quantityUM = quantityUM;
    }

    public String getExpireUnit() {
        return expireUnit;
    }

    public void setExpireUnit(String expireUnit) {
        this.expireUnit = expireUnit;
    }

    public String getExpireType() {
        return expireType;
    }

    public void setExpireType(String expireType) {
        this.expireType = expireType;
    }

    public int getRejectDays() {
        return rejectDays;
    }

    public void setRejectDays(int rejectDays) {
        this.rejectDays = rejectDays;
    }

    public String getTrackSerialRegular() {
        return trackSerialRegular;
    }

    public void setTrackSerialRegular(String trackSerialRegular) {
        this.trackSerialRegular = trackSerialRegular;
    }

    public String getIsMeasure() {
        return isMeasure;
    }

    public void setIsMeasure(String isMeasure) {
        this.isMeasure = isMeasure;
    }

    public String getIsOriginPacking() {
        return isOriginPacking;
    }

    public void setIsOriginPacking(String isOriginPacking) {
        this.isOriginPacking = isOriginPacking;
    }

    public String getIsPlasticFirst() {
        return isPlasticFirst;
    }

    public void setIsPlasticFirst(String isPlasticFirst) {
        this.isPlasticFirst = isPlasticFirst;
    }

    public String getIsNeedCushion() {
        return isNeedCushion;
    }

    public void setIsNeedCushion(String isNeedCushion) {
        this.isNeedCushion = isNeedCushion;
    }
}
