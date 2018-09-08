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
@Table(name = "WMS_INV_MITEM_LABEL")
public class InvMitemLabel  extends BaseEntity implements IRemoveDuplication {
    /*
    库存组织
     */
    @Column(name = "INV_ORG_ID")
    private Integer INV_ORG_ID;
    /*
    配送卡号
     */
    @Column(name = "LABEL_NO")
    private String LABEL_NO;
    /*
    完工子库
     */
    @Column(name = "INV_CODE")
    private String INV_CODE;
    /*
    自制工单
     */
    @Column(name = "MO_NAME")
    private String MO_NAME;
    /*
    总装工单
     */
    @Column(name = "ASS_MO_NAME")
    private String ASS_MO_NAME;
    /*
   总装车间
    */
    @Column(name = "WORKSHOP_CODE")
    private String WORKSHOP_CODE;
    /*
   总装产线
    */
    @Column(name = "LINE_CODE")
    private String LINE_CODE;
    /*
   总装工单上线时间
    */
    @Column(name = "DATE_REQ")
    private Date DATE_REQ;
    /*
   物料编码
    */
    @Column(name = "MITEM_CODE")
    private String MITEM_CODE;
    /*
    物料描述
     */
    @Column(name = "MITEM_DESC")
    private String MITEM_DESC;
    /*
    单位
     */
    @Column(name = "UOM")
    private String UOM;
    /*
    可用数量
     */
    @Column(name = "QTY")
    private BigDecimal QTY;
    /*
    标签状态
     */
    @Column(name = "LABEL_STATE")
    private String LABEL_STATE;
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

    public String getLABEL_NO() {
        return LABEL_NO;
    }

    public void setLABEL_NO(String LABEL_NO) {
        this.LABEL_NO = LABEL_NO;
    }

    public String getINV_CODE() {
        return INV_CODE;
    }

    public void setINV_CODE(String INV_CODE) {
        this.INV_CODE = INV_CODE;
    }

    public String getMO_NAME() {
        return MO_NAME;
    }

    public void setMO_NAME(String MO_NAME) {
        this.MO_NAME = MO_NAME;
    }

    public String getASS_MO_NAME() {
        return ASS_MO_NAME;
    }

    public void setASS_MO_NAME(String ASS_MO_NAME) {
        this.ASS_MO_NAME = ASS_MO_NAME;
    }

    public String getWORKSHOP_CODE() {
        return WORKSHOP_CODE;
    }

    public void setWORKSHOP_CODE(String WORKSHOP_CODE) {
        this.WORKSHOP_CODE = WORKSHOP_CODE;
    }

    public String getLINE_CODE() {
        return LINE_CODE;
    }

    public void setLINE_CODE(String LINE_CODE) {
        this.LINE_CODE = LINE_CODE;
    }

    public Date getDATE_REQ() {
        return DATE_REQ;
    }

    public void setDATE_REQ(Date DATE_REQ) {
        this.DATE_REQ = DATE_REQ;
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

    public BigDecimal getQTY() {
        return QTY;
    }

    public void setQTY(BigDecimal QTY) {
        this.QTY = QTY;
    }

    public String getLABEL_STATE() {
        return LABEL_STATE;
    }

    public void setLABEL_STATE(String LABEL_STATE) {
        this.LABEL_STATE = LABEL_STATE;
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
