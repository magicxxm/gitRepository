package com.mushiny.wms.application.domain;

import com.mushiny.wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/6.
 */
@Entity
@Table(name = "WMS_SFC_MITEM")
public class SfcMitem  extends BaseEntity implements IRemoveDuplication {

    /*
    库存组织
     */
    @Column(name = "INV_ORG_ID")
    private Integer INV_ORG_ID;
    /*
    物料编码
     */
    @Column(name = "MITEM_CODE")
    private String MITEM_CODE;
    /*
    物料名称
     */
    @Column(name = "MITEM_DESC")
    private String MITEM_DESC;
    /*
    单位
     */
    @Column(name = "UOM")
    private String UOM;
    /*
    物料状态
     */
    @Column(name = "MITEM_STATUS")
    private String MITEM_STATUS;
    /*
    物料类型
     */
    @Column(name = "MITEM_TYPE")
    private String MITEM_TYPE;
    /*
    长
     */
    @Column(name = "LENGHT")
    private BigDecimal LENGHT;
    /*
    宽
     */
    @Column(name = "WIDTH")
    private BigDecimal WIDTH;
    /*
    高
     */
    @Column(name = "HEIGHT")
    private BigDecimal HEIGHT;
    /*
    重量
     */
    @Column(name = "WEIGHT")
    private BigDecimal WEIGHT;
    /*
    状态
     */
    @Column(name = "STATE")
    private String STATE;
    /*
    最后更新时间
     */
    @Column(name = "LAST_UPDATE_DATE")
    private Date LAST_UPDATE_DATE;

    public Integer getINV_ORG_ID() {
        return INV_ORG_ID;
    }

    public void setINV_ORG_ID(Integer INV_ORG_ID) {
        this.INV_ORG_ID = INV_ORG_ID;
    }

    public String getMITEM_CODE() {
        return MITEM_CODE;
    }

    public void setMITEM_CODE(String MITEM_CODE) {
        this.MITEM_CODE = MITEM_CODE;
    }

    public String getMITEM_DESC() {
        return MITEM_DESC;
    }

    public void setMITEM_DESC(String MITEM_DESC) {
        this.MITEM_DESC = MITEM_DESC;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public String getMITEM_STATUS() {
        return MITEM_STATUS;
    }

    public void setMITEM_STATUS(String MITEM_STATUS) {
        this.MITEM_STATUS = MITEM_STATUS;
    }

    public String getMITEM_TYPE() {
        return MITEM_TYPE;
    }

    public void setMITEM_TYPE(String MITEM_TYPE) {
        this.MITEM_TYPE = MITEM_TYPE;
    }

    public BigDecimal getLENGHT() {
        return LENGHT;
    }

    public void setLENGHT(BigDecimal LENGHT) {
        this.LENGHT = LENGHT;
    }

    public BigDecimal getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(BigDecimal WIDTH) {
        this.WIDTH = WIDTH;
    }

    public BigDecimal getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(BigDecimal HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public BigDecimal getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(BigDecimal WEIGHT) {
        this.WEIGHT = WEIGHT;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public Date getLAST_UPDATE_DATE() {
        return LAST_UPDATE_DATE;
    }

    public void setLAST_UPDATE_DATE(Date LAST_UPDATE_DATE) {
        this.LAST_UPDATE_DATE = LAST_UPDATE_DATE;
    }

    @Override
    public String getRemovingColumn() {
        return this.MITEM_CODE;
    }
}
