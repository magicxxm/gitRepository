package wms.domain.common;

import wms.common.entity.BaseClientAssignedEntity;
import wms.domain.ItemData;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "INV_LOT", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "LOT_NO", "ITEMDATA_ID"
        })
})
public class Lot extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "LOT_NO", nullable = false)
    private String lotNo;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LOT_DATE", nullable = false)
    private Date lotDate;

    @Column(name = "PRODUCT_DATE")
    private Date productDate;

    @Column(name = "USE_NOT_AFTER")
    private Date useNotAfter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ITEMDATA_ID")
    private ItemData itemData;

    public String getLotNo() {
        return lotNo;
    }

    public void setLotNo(String lotNo) {
        this.lotNo = lotNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLotDate() {
        return lotDate;
    }

    public void setLotDate(Date lotDate) {
        this.lotDate = lotDate;
    }

    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    public Date getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(Date useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public ItemData getItemData() {
        return itemData;
    }

    public void setItemData(ItemData itemData) {
        this.itemData = itemData;
    }

}
