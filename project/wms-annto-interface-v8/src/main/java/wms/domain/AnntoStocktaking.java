package wms.domain;

import wms.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC-4 on 2017/8/10.
 */
@Entity
@Table(name = "ANNTO_STOCKTAKING")
public class AnntoStocktaking extends BaseEntity{

    @Column(name = "ORIGINAL_COUNT_ID")
    private String originalCountId;

    @Column(name = "COUNT_TYPE")
    private int countType;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "WAREHOUSE_CODE")
    private String warehouseCode;

    @OrderBy("companycode")
    @OneToMany(mappedBy = "anntoStocktaking", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<AnntoStocktakingPosition> positions = new ArrayList<>();

    public void addPosition(AnntoStocktakingPosition position) {
        getPositions().add(position);
        position.setAnntoStocktaking(this);
    }

    public String getOriginalCountId() {
        return originalCountId;
    }

    public void setOriginalCountId(String originalCountId) {
        this.originalCountId = originalCountId;
    }

    public int getCountType() {
        return countType;
    }

    public void setCountType(int countType) {
        this.countType = countType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<AnntoStocktakingPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<AnntoStocktakingPosition> positions) {
        this.positions = positions;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }
}
