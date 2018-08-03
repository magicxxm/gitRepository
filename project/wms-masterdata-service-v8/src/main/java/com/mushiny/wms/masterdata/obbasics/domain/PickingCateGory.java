package com.mushiny.wms.masterdata.obbasics.domain;


import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGCATEGORY", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NAME", "CLIENT_ID", "WAREHOUSE_ID"})
})
public class PickingCateGory extends BaseClientAssignedEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int index = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROCESSPATH_ID")
    private ProcessPath processPath;

    @OneToMany(mappedBy = "pickingCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("index ASC")
    private List<PickingCateGoryPosition> positions;

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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ProcessPath getProcessPath() {
        return processPath;
    }

    public void setProcessPath(ProcessPath processPath) {
        this.processPath = processPath;
    }

    public List<PickingCateGoryPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingCateGoryPosition> positions) {
        this.positions = positions;
    }

    @Override
    public String toUniqueString() {
        return name;
    }
}
