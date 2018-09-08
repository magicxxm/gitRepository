package com.mushiny.wms.application.domain;

import com.fasterxml.jackson.annotation.*;
import com.mushiny.wms.common.entity.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/14.
 */
@Entity
@Table(name = "WMS_INSTRUCT_IN_POSITION")
public class WmsInstructInPosition extends BaseEntity {
    @Column(name = "DATETIME_STOCK")

    private Date DATETIME_STOCK;

    @Column(name = "CAR_NO")

    private String CAR_NO="";
    @Column(name = "LOC_CODE")

    private String LOC_CODE="";
    @Column(name = "STATUS")

    private String STATUS="";

    @ManyToOne
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "INSTRUCT_ID")
    private InboundInstruct inboundInstruct;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty(value = "DATETIME_STOCK")
    public Date getDATETIME_STOCK() {
        return DATETIME_STOCK;
    }

    public void setDATETIME_STOCK(Date DATETIME_STOCK) {
        this.DATETIME_STOCK = DATETIME_STOCK;
    }


    public InboundInstruct getInboundInstruct() {
        return inboundInstruct;
    }

    public void setInboundInstruct(InboundInstruct inboundInstruct) {
        this.inboundInstruct = inboundInstruct;
    }
    @JsonProperty(value = "CAR_NO")
    public String getCAR_NO() {
        return CAR_NO;
    }

    public void setCAR_NO(String CAR_NO) {
        this.CAR_NO = CAR_NO;
    }
    @JsonProperty(value = "LOC_CODE")
    public String getLOC_CODE() {
        return LOC_CODE;
    }

    public void setLOC_CODE(String LOC_CODE) {
        this.LOC_CODE = LOC_CODE;
    }
    @JsonProperty(value = "STATUS")
    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
