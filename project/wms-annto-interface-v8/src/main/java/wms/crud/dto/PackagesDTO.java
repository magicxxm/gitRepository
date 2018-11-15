package wms.crud.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/7/11.
 */
public class PackagesDTO {

    //"primaryWaybillCode": "运单号，string (20)，必填",
    private String primaryWaybillCode;

    //"containercode": "箱号，string (20)，必填",
    private String containercode;

    //"containertype ": "箱型，string (20) ",
    private String containertype;

    //"totalweight": "包裹重量 (kg)，double (18, 3) ",
    private BigDecimal totalweight;

    //"totalvolume": "体积 (m³) ，double (18, 6) ",
    private BigDecimal totalvolume;

    private List<PackageItemsDTO> packageItems = new ArrayList<>();

    public String getPrimaryWaybillCode() {
        return primaryWaybillCode;
    }

    public void setPrimaryWaybillCode(String primaryWaybillCode) {
        this.primaryWaybillCode = primaryWaybillCode;
    }

    public String getContainercode() {
        return containercode;
    }

    public void setContainercode(String containercode) {
        this.containercode = containercode;
    }

    public String getContainertype() {
        return containertype;
    }

    public void setContainertype(String containertype) {
        this.containertype = containertype;
    }

    public BigDecimal getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(BigDecimal totalweight) {
        this.totalweight = totalweight;
    }

    public BigDecimal getTotalvolume() {
        return totalvolume;
    }

    public void setTotalvolume(BigDecimal totalvolume) {
        this.totalvolume = totalvolume;
    }

    public List<PackageItemsDTO> getPackageItems() {
        return packageItems;
    }

    public void setPackageItems(List<PackageItemsDTO> packageItems) {
        this.packageItems = packageItems;
    }
}
