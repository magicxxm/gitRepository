package com.mushiny.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 123 on 2018/2/24.
 */
@Entity
@Table(name = "SUNING_ZRFC_AGV_CHANGEPOSITION")
public class ChangePosition implements Serializable{

    @Id
    @GeneratedValue(generator = "annto-uuid")
    @GenericGenerator(name = "annto-uuid", strategy = "uuid2")
    @Column(name = "UID")
    private String id;

    @Column(name = "ZITEM")
    private String zitem;

    @Column(name = "CHANGE_ORDER")
    private String changeOrder;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "MESSAGE")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZitem() {
        return zitem;
    }

    public void setZitem(String zitem) {
        this.zitem = zitem;
    }

    public String getChangeOrder() {
        return changeOrder;
    }

    public void setChangeOrder(String changeOrder) {
        this.changeOrder = changeOrder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
