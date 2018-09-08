package com.mushiny.wms.application.domain;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mushiny.wms.common.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/14.
 */
@Entity
@Table(name = "WMS_INSTRUCT_OUT_POSITION")

public class WmsInstructOutPosition extends BaseEntity{

    @Column(name = "STORAGE_NO_L")
    private String STORAGE_NO_L="";
    @Column(name = "STOCK_QTY")
    private String STOCK_QTY;
    @Column(name = "STATUS")
    private String STATUS="";
    @Column(name = "CAR_NO")
    private String CAR_NO="";
    @Column(name = "LOC_CODE")
    private String LOC_CODE="";
    @Column(name = "DATETIME_STOCK")
    private Date DATETIME_STOCK;
    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "INSTRUCT_ID")
    private OutboundInstruct outboundInstruct;

    public OutboundInstruct getOutboundInstruct() {
        return outboundInstruct;
    }

    public void setOutboundInstruct(OutboundInstruct outboundInstruct) {
        this.outboundInstruct = outboundInstruct;
    }
    @JsonProperty(value = "LOC_CODE")
    public String getLOC_CODE() {
        return LOC_CODE;
    }

    public void setLOC_CODE(String LOC_CODE) {
        this.LOC_CODE = LOC_CODE;
    }

    @JsonProperty(value = "STORAGE_NO_L")
    public String getSTORAGE_NO_L() {
        return STORAGE_NO_L;
    }

    public void setSTORAGE_NO_L(String STORAGE_NO_L) {
        this.STORAGE_NO_L = STORAGE_NO_L;
    }
    @JsonProperty(value = "STOCK_QTY")
    public String getSTOCK_QTY() {
        return STOCK_QTY;
    }

    public void setSTOCK_QTY(String STOCK_QTY) {
        this.STOCK_QTY = STOCK_QTY;
    }
    @JsonProperty(value = "STATUS")
    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
    @JsonProperty(value = "CAR_NO")
    public String getCAR_NO() {
        return CAR_NO;
    }

    public void setCAR_NO(String CAR_NO) {
        this.CAR_NO = CAR_NO;
    }
    @JsonProperty(value = "DATETIME_STOCK")
    public Date getDATETIME_STOCK() {
        return DATETIME_STOCK;
    }

    public void setDATETIME_STOCK(Date DATETIME_STOCK) {
        this.DATETIME_STOCK = DATETIME_STOCK;
    }
}
