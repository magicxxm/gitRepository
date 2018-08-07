package com.mushiny.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by 123 on 2018/2/28.
 */
@Entity
@Table(name = "MD_ITEMDATA_SKUNO")
public class ItemSkuNo implements Serializable {

    @Id
    @GeneratedValue(generator = "annto-uuid")
    @GenericGenerator(name = "annto-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ITEM_NO")
    private String itemNo;

    @Column(name = "SKU_NO")
    private String skuNo;

    @Column(name = "VERSION")
    @Version
    @JsonIgnore
    private Integer version = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getSkuNo() {
        return skuNo;
    }

    public void setSkuNo(String skuNo) {
        this.skuNo = skuNo;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
