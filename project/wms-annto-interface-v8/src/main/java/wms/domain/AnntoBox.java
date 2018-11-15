package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by 123 on 2017/12/6.
 */
@Entity
@Table(name = "ANNTO_BOX")
public class AnntoBox extends BaseEntity {

    @Column(name = "WAREHOUSE_CODE")
    private String warehouseCode;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    //箱型编码
    @Column(name = "CONTAINER_CODE")
    private String containerCode;

    //箱型名称
    @Column(name = "CONTAINER_NAME")
    private String containerName;

    //箱型类别
    @Column(name = "CONTAINER_TYPE")
    private String containerType;

    //备注
    @Column(name = "CONTAINER_REMARK")
    private String containerRemark;

    @Column(name = "LENGTH")
    private int length;

    @Column(name = "WIDTH")
    private int width;

    @Column(name = "HEIGHT")
    private int height;

    @Column(name = "WEIGHT")
    private int weight;

    public AnntoBox() {
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getContainerCode() {
        return containerCode;
    }

    public void setContainerCode(String containerCode) {
        this.containerCode = containerCode;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public String getContainerRemark() {
        return containerRemark;
    }

    public void setContainerRemark(String containerRemark) {
        this.containerRemark = containerRemark;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
