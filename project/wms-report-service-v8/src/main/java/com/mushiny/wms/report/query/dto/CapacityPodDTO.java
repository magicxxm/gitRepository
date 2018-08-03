package com.mushiny.wms.report.query.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CapacityPodDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MathContext mc = new MathContext(4, RoundingMode.HALF_DOWN);

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String podType;

    private Integer location;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String face; //pod面

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String binName; //货位名字

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String binType;

    private BigDecimal itemTotalAmount = BigDecimal.ZERO;//所有商品总数量

    private BigDecimal skuNoUnit = BigDecimal.ZERO;//skuNo个数

    private BigDecimal itemNoUnit = BigDecimal.ZERO; //skuId个数


    private BigDecimal itemTotalM3 = BigDecimal.ZERO; //所有商品总体积

    private BigDecimal binTotalM3 = BigDecimal.ZERO; //所有货位总体积

    private BigDecimal binNotNullTotalM3 = BigDecimal.ZERO; //所有非空(已使用)货位总体积

    private BigDecimal binNullTotalM3 = BigDecimal.ZERO; //所有空(未使用)货位总体积

    private String useUtilization; // 所有商品总体积/所有非空(已使用)货位总体积 *100%

    private String totalUtilization; //所有商品总体体积/所有货位总体积 *100%


    private BigDecimal binTotal = BigDecimal.ZERO; // 所有货位个数

    private BigDecimal binNotNullTotal = BigDecimal.ZERO; //所有非空(已使用)货位个数

    private BigDecimal binNullTotal = BigDecimal.ZERO; //所有空(未使用)货位个数

    private BigDecimal bufferBinNullTotal = BigDecimal.ZERO; //所有空货位buffer货位个数

    private String binUtilization; // 所有非空(已使用)货位数量/所有货位数量 *100%

    public void setBufferBinUtilization(String bufferBinUtilization) {
        this.bufferBinUtilization = bufferBinUtilization;
    }

    private String bufferBinUtilization; //所有空buffer货位数量/所有非空(已使用)货位数量 *100%

    public CapacityPodDTO() {
    }

    public CapacityPodDTO(String podName,
                          String podType,
                          Integer location,
                          BigDecimal itemTotalAmount,
                          BigDecimal skuNoUnit,
                          BigDecimal itemNoUnit,
                          BigDecimal itemTotalM3,
                          BigDecimal binTotalM3,
                          BigDecimal binNotNullTotalM3,
                          BigDecimal binNullTotalM3,
                          BigDecimal binTotal,
                          BigDecimal binNotNullTotal,
                          BigDecimal binNullTotal,
                          BigDecimal bufferBinNullTotal) {
        this.podName = podName;
        this.podType = podType;
        this.location = location;
        this.itemTotalAmount = itemTotalAmount;
        this.skuNoUnit = skuNoUnit;
        this.itemNoUnit = itemNoUnit;
        this.itemTotalM3 = itemTotalM3;
        this.binTotalM3 = binTotalM3;
        this.binNotNullTotalM3 = binNotNullTotalM3;
        this.binNullTotalM3 = binNullTotalM3;
        this.binTotal = binTotal;
        this.binNotNullTotal = binNotNullTotal;
        this.binNullTotal = binNullTotal;
        this.bufferBinNullTotal = bufferBinNullTotal;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public String getPodType() {
        return podType;
    }

    public void setPodType(String podType) {
        this.podType = podType;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public Integer getLocation() {

        return location;
    }
    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBinType() {
        return binType;
    }

    public void setBinType(String binType) {
        this.binType = binType;
    }

    public BigDecimal getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(BigDecimal itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public BigDecimal getSkuNoUnit() {
        return skuNoUnit;
    }

    public void setSkuNoUnit(Object skuNoUnit) {
        if (skuNoUnit != null) {
            this.skuNoUnit = new BigDecimal(skuNoUnit.toString());
        } else {
            this.skuNoUnit = BigDecimal.ZERO;
        }
    }

    public BigDecimal getItemNoUnit() {
        return itemNoUnit;
    }

    public void setItemNoUnit(Object itemNoUnit) {
        if (itemNoUnit != null) {
            this.itemNoUnit = new BigDecimal(itemNoUnit.toString());
        } else {
            this.itemNoUnit = BigDecimal.ZERO;
        }
    }

    public BigDecimal getItemTotalM3() {
        return itemTotalM3;
    }

    public void setItemTotalM3(BigDecimal itemTotalM3) {
        this.itemTotalM3 = itemTotalM3;
    }

    public BigDecimal getBinTotalM3() {
        return binTotalM3;
    }

    public void setBinTotalM3(BigDecimal binTotalM3) {
        this.binTotalM3 = binTotalM3;
    }

    public BigDecimal getBinNotNullTotalM3() {
        return binNotNullTotalM3;
    }

    public void setBinNotNullTotalM3(BigDecimal binNotNullTotalM3) {
        this.binNotNullTotalM3 = binNotNullTotalM3;
    }

    public BigDecimal getBinNullTotalM3() {
        return binNullTotalM3;
    }

    public void setBinNullTotalM3(BigDecimal binNullTotalM3) {
        this.binNullTotalM3 = binNullTotalM3;
    }

    public String getUseUtilization() {
        return useUtilization;
    }

    // 所有商品总体积/所有非空(已使用)货位总体积 *100%
    public void setUseUtilization() {
        if (this.binNotNullTotalM3 != null && this.binNotNullTotalM3.compareTo(BigDecimal.ZERO) > 0) {
            this.useUtilization = this.itemTotalM3.
                    divide(this.binNotNullTotalM3, 4).
                    multiply(new BigDecimal("100")).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                    "%";
        }
    }

    public String getTotalUtilization() {
        return totalUtilization;
    }

    //
    public void setTotalUtilization() {
        if (this.binTotalM3 != null && this.binTotalM3.compareTo(BigDecimal.ZERO) > 0) {
            this.totalUtilization = this.itemTotalM3.
                    divide(this.binTotalM3, 4).
                    multiply(new BigDecimal("100")).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                    "%";
        }
    }

    public BigDecimal getBinTotal() {
        return binTotal;
    }

    public void setBinTotal(Object binTotal) {
        if (binTotal != null) {
            this.binTotal = new BigDecimal(binTotal.toString());
        } else {
            this.binTotal = BigDecimal.ZERO;
        }
    }

    public BigDecimal getBinNotNullTotal() {
        return binNotNullTotal;
    }

    public void setBinNotNullTotal(Object binNotNullTotal) {
        if (binNotNullTotal != null) {
            this.binNotNullTotal = new BigDecimal(binNotNullTotal.toString());
        } else {
            this.binNotNullTotal = BigDecimal.ZERO;
        }
    }

    public BigDecimal getBinNullTotal() {
        return binNullTotal;
    }

    public void setBinNullTotal(Object binNullTotal) {
        if (binNullTotal != null) {
            this.binNullTotal = new BigDecimal(binNullTotal.toString());
        } else {
            this.binNullTotal = BigDecimal.ZERO;
        }
    }

    public BigDecimal getBufferBinNullTotal() {
        return bufferBinNullTotal;
    }

    public void setBufferBinNullTotal(Object bufferBinNullTotal) {
        if (bufferBinNullTotal != null) {
            this.bufferBinNullTotal = new BigDecimal(bufferBinNullTotal.toString());
        } else {
            this.bufferBinNullTotal = BigDecimal.ZERO;
        }
    }

    public String getBinUtilization() {
        return binUtilization;
    }

    //
    public void setBinUtilization() {
        if (this.binTotal != null && this.binTotal.compareTo(BigDecimal.ZERO) > 0) {
            this.binUtilization = (this.binNotNullTotal.
                    divide(this.binTotal, 4, BigDecimal.ROUND_HALF_UP).
                    multiply(new BigDecimal("100"))).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                    "%";
        }

    }

    public String getBufferBinUtilization() {
        return bufferBinUtilization;
    }

    public void setBufferBinUtilization() {
        if (this.binNotNullTotal != null && this.binNotNullTotal.compareTo(BigDecimal.ZERO) > 0) {
            this.bufferBinUtilization = this.bufferBinNullTotal.
                    divide(this.binNotNullTotal, 4, BigDecimal.ROUND_HALF_UP).
                    multiply(new BigDecimal("100")).
                    setScale(2).toString() +
                    "%";
        }
    }

}
