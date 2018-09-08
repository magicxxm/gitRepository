package test.com.mushiny.wms.application.test.domain;

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
@Table(name = "WMS_INBOUND_INSTRUCT")
public class InboundInstruct extends BaseEntity {
    /*
   库存组织
    */
    @Column(name = "INV_ORG_ID")
    private String INV_ORG_ID;
    /*
   单据类型
    */
    @Column(name = "BILL_TYPE")
    private String BILL_TYPE;
    /*
   指令单号
    */
    @Column(name = "BILL_NO")
    private String BILL_NO;
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
    上级工单
     */
    @Column(name = "ASS_MO_NAME")
    private String ASS_MO_NAME;
    /*
    上线工单上线时间
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
    优先级
     */
    @Column(name = "PRIORITY")
    private BigDecimal PRIORITY;
    /*
    来源子库
     */
    @Column(name = "TARGET_INV_ID")
    private String TARGET_INV_ID;
    /*
    来源货位
     */
    @Column(name = "TARGET_LOC_ID")
    private String TARGET_LOC_ID;
    /*
   出入库数量
    */
    @Column(name = "STOCK_QTY")
    private BigDecimal STOCK_QTY;
    /*
   料架条码L
    */
    @Column(name = "STORAGE_NO_L")
    private String STORAGE_NO_L;
    /*
   料架条码R
    */
    @Column(name = "STORAGE_NO_R")
    private String STORAGE_NO_R;
    /*
   工站条码
    */
    @Column(name = "WORKCENTER_CODE")
    private String WORKCENTER_CODE;
    /*
   小车编码
    */
    @Column(name = "CAR_NO")
    private String CAR_NO;
    /*
   货位条码
    */
    @Column(name = "LOCATION_NO")
    private String LOCATION_NO;
    /*
   出入库时间
    */
    @Column(name = "DATETIME_STOCK")
    private Date DATETIME_STOCK;
    /*
   指令状态
    */
    @Column(name = "STATUS")
    private String STATUS;

    @Column(name = "STORAGE_NO")
    private String STORAGE_NO;

    public String getSTORAGE_NO() {
        return STORAGE_NO;
    }

    public void setSTORAGE_NO(String STORAGE_NO) {
        this.STORAGE_NO = STORAGE_NO;
    }

    public String getINV_ORG_ID() {
        return INV_ORG_ID;
    }

    public void setINV_ORG_ID(String INV_ORG_ID) {
        this.INV_ORG_ID = INV_ORG_ID;
    }

    public String getBILL_TYPE() {
        return BILL_TYPE;
    }

    public void setBILL_TYPE(String BILL_TYPE) {
        this.BILL_TYPE = BILL_TYPE;
    }

    public String getBILL_NO() {
        return BILL_NO;
    }

    public void setBILL_NO(String BILL_NO) {
        this.BILL_NO = BILL_NO;
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

    public BigDecimal getPRIORITY() {
        return PRIORITY;
    }

    public void setPRIORITY(BigDecimal PRIORITY) {
        this.PRIORITY = PRIORITY;
    }

    public String getTARGET_INV_ID() {
        return TARGET_INV_ID;
    }

    public void setTARGET_INV_ID(String TARGET_INV_ID) {
        this.TARGET_INV_ID = TARGET_INV_ID;
    }

    public String getTARGET_LOC_ID() {
        return TARGET_LOC_ID;
    }

    public void setTARGET_LOC_ID(String TARGET_LOC_ID) {
        this.TARGET_LOC_ID = TARGET_LOC_ID;
    }

    public BigDecimal getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(BigDecimal STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }

    public String getSTORAGE_NO_L() {
        return STORAGE_NO_L;
    }

    public void setSTORAGE_NO_L(String STORAGE_NO_L) {
        this.STORAGE_NO_L = STORAGE_NO_L;
    }

    public String getSTORAGE_NO_R() {
        return STORAGE_NO_R;
    }

    public void setSTORAGE_NO_R(String STORAGE_NO_R) {
        this.STORAGE_NO_R = STORAGE_NO_R;
    }

    public String getWORKCENTER_CODE() {
        return WORKCENTER_CODE;
    }

    public void setWORKCENTER_CODE(String WORKCENTER_CODE) {
        this.WORKCENTER_CODE = WORKCENTER_CODE;
    }

    public String getCAR_NO() {
        return CAR_NO;
    }

    public void setCAR_NO(String CAR_NO) {
        this.CAR_NO = CAR_NO;
    }

    public String getLOCATION_NO() {
        return LOCATION_NO;
    }

    public void setLOCATION_NO(String LOCATION_NO) {
        this.LOCATION_NO = LOCATION_NO;
    }

    public Date getDATETIME_STOCK() {
        return DATETIME_STOCK;
    }

    public void setDATETIME_STOCK(Date DATETIME_STOCK) {
        this.DATETIME_STOCK = DATETIME_STOCK;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }


    public String toString() {
        return "InboundInstruct{" +
                "INV_ORG_ID='" + INV_ORG_ID + '\'' +
                ", BILL_TYPE='" + BILL_TYPE + '\'' +
                ", BILL_NO='" + BILL_NO + '\'' +
                ", LABEL_NO='" + LABEL_NO + '\'' +
                ", INV_CODE='" + INV_CODE + '\'' +
                ", MO_NAME='" + MO_NAME + '\'' +
                ", ASS_MO_NAME='" + ASS_MO_NAME + '\'' +
                ", DATE_REQ=" + DATE_REQ +
                ", MITEM_CODE='" + MITEM_CODE + '\'' +
                ", MITEM_DESC='" + MITEM_DESC + '\'' +
                ", UOM='" + UOM + '\'' +
                ", QTY=" + QTY +
                ", PRIORITY=" + PRIORITY +
                ", TARGET_INV_ID='" + TARGET_INV_ID + '\'' +
                ", TARGET_LOC_ID='" + TARGET_LOC_ID + '\'' +
                ", STOCK_QTY=" + STOCK_QTY +
                ", STORAGE_NO_L='" + STORAGE_NO_L + '\'' +
                ", STORAGE_NO_R='" + STORAGE_NO_R + '\'' +
                ", WORKCENTER_CODE='" + WORKCENTER_CODE + '\'' +
                ", CAR_NO='" + CAR_NO + '\'' +
                ", LOCATION_NO='" + LOCATION_NO + '\'' +
                ", DATETIME_STOCK=" + DATETIME_STOCK +
                ", STATUS='" + STATUS + '\'' +
                '}';
    }
}
