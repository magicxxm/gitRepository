package com.mushiny.wms.masterdata.mdbasics.domain;


import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import javax.persistence.*;
/**
 * Created by Laptop-11 on 2018/3/24.
 */
@Entity
@Table(name = "MD_ITEMDATA_SKUNO")
public class ItemDataGlobalSku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "masterdata-uuid")
    @GenericGenerator(name = "masterdata-uuid", strategy = "uuid2")
    @Column(name = "ID")
    private String id;

    @Column(name = "ITEM_NO")
    private String itemNo;
    @Column(name = "SKU_NO")
    private String skuNo;
    @Column(name = "VERSION")
    private String version;

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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
