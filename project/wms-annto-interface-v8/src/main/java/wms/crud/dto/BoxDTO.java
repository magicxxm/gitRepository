package wms.crud.dto;

import java.io.Serializable;

/**
 * Created by 123 on 2017/12/6.
 */
public class BoxDTO implements Serializable {

    private String warehouseCode;

    private String companyCode;

    //箱型编码
    private String containerCode;

    //箱型名称
    private String containerName;

    //箱型类别
    private String containerType;

    //备注
    private String containerRemark;

    private int length;

    private int width;

    private int height;

    private int weight;

    private ExtendFieldsDTO extendFields;

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

    public ExtendFieldsDTO getExtendFields() {
        return extendFields;
    }

    public void setExtendFields(ExtendFieldsDTO extendFields) {
        this.extendFields = extendFields;
    }
}
