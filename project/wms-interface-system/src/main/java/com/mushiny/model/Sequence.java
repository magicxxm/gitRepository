package com.mushiny.model;

import com.mushiny.common.entity.BaseClientAssignedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2018/2/8.
 */
@Entity
@Table(name = "MD_SEQUENCE_NO")
public class Sequence extends BaseClientAssignedEntity {

    @Column(name = "SEQUENCE_NO")
    private String sequenceNo;

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "STATE")
    private int state;

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
