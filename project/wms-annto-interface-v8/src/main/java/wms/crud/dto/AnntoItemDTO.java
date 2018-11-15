package wms.crud.dto;

import wms.common.crud.dto.BaseDTO;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/10.
 *
 * 商品同步
 *
 */
public class AnntoItemDTO extends BaseDTO{

    //"Code": "商品编码，string (50)，必填",
    private String code;

    //"warehouseCode": "仓库编码，string (50)，必填",
    private String warehouseCode;

    //"warehouseCode": "货主编码，string (50)，必填",
    private String companyCode;

    //"Name": "商品名称，string (200)，必填",
    private String name;

    //是否是有效期商品
    private String lotMandatory;

    //"daysToExpire": "保质期天数，int，默认 0",
    private int daysToExpire = 0;

    //有效期单位
    private String expireUnit;

    //有效期类型
    private String expireType;

    //拒收天数
    private int rejectDays;

    //"expiringDays": "预警天数，int",
    private int expiringDays;

    //"class01": "大类，string (200)，必填",
    private String class01;

    //"class02": "中类，string (200)，必填",
    private String class02;

    //"class03": "小类，string (200)，必填",
    private String class03;

    //"trackSerialNum": "记录序列号，int， 0:不记录；1:记录",
    private int trackSerialNum;

    //序列号规则
    private String trackSerialRegular;

    //"brand": "品牌，string (50) ",
    private String brand;

    //"ItemSize": "尺寸，string (50) ",
    private String itemSize;

    //"ItemColor": "颜色，string (50) ",
    private String itemColor;

    //"ItemStyle": "规格，string (50) ",
    private String itemStyle;

    //"placeOfOrigin": "产地，string (50) ",
    private String placeOfOrigin;

    //"unitDesc": "最小数量单位名称，string (50),默认 个 ",
    private String unitDesc;

    //"unitLength": "最小单位长度，numeric(9,2) ",
    private BigDecimal unitLength;

    //"unitWidth": "最小单位宽度，numeric(9,2) ",
    private BigDecimal unitWidth;

    //"unitHeight": "最小单位高度，numeric(9,2) ",
    private BigDecimal unitHeight;

    //"unitWeight": "最小单位重量，numeric(9,2) ",
    private BigDecimal unitWeight;

    //"unitVolume": "最小单位体积，numeric(9,2) ",
    private BigDecimal unitVolume;

    //"csQTY": "包装箱数量，int，默认 0",
    private int csQTY;

    //"csDesc": "箱名称，string(50)，默认 0",
    private String csDesc;

    //"csLength": "箱长度，numeric(9,2)，默认 0",
    private BigDecimal csLength = BigDecimal.ZERO;

    //"csWidth": "箱宽度，numeric(9,2)，默认 0",
    private BigDecimal csWidth = BigDecimal.ZERO;

    //"csHeight": "箱高度，numeric(9,2)，默认 0",
    private BigDecimal csHeight = BigDecimal.ZERO;

    //"csWeight": "箱重量，numeric(9,2)，默认 0",
    private BigDecimal csWeight = BigDecimal.ZERO;

    //"csVolume": "箱体积，numeric(9,2)，默认 0",
    private BigDecimal csVolume = BigDecimal.ZERO;

    //"plQTY": "托盘包装数，int，默认 0",
    private int plQTY = 0;

    //"plDesc": "托盘名称，string(50)，默认  托",
    private String plDesc = "托";

    //是否测量
    private String isMeasure;

    //是否自带包装
    private String isOriginPacking;

    //是否使用袋子
    private String isPlasticFirst;

    //是否需要气垫膜
    private String isNeedCushion;

//    private BarcodeDTO barcode;
    private List<BarcodeDTO> barcode = new ArrayList<>();

    public AnntoItemDTO() {
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

    public String getTrackSerialRegular() {
        return trackSerialRegular;
    }

    public void setTrackSerialRegular(String trackSerialRegular) {
        this.trackSerialRegular = trackSerialRegular;
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

   /* public BarcodeDTO getBarcode() {
        return barcode;
    }

    public void setBarcode(BarcodeDTO barcode) {
        this.barcode = barcode;
    }*/

    public List<BarcodeDTO> getBarcode() {
        return barcode;
    }

    public void setBarcode(List<BarcodeDTO> barcode) {
        this.barcode = barcode;
    }
}
