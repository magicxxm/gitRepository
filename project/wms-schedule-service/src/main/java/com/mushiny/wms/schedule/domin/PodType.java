package com.mushiny.wms.schedule.domin;

import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MD_PODTYPE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "WAREHOUSE_ID"
        })
})
public class PodType extends BaseWarehouseAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "NUMBER_OF_COLUMNS")
    private int numberOfColumns;

    @Column(name = "NUMBER_OF_ROWS")
    private int numberOfRows;

    @Column(name = "HEIGHT")
    private int height;

    @Column(name = "WIDTH")
    private int width;

    @Column(name = "DEPTH")
    private int depth;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "podType", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PodTypePosition> positions = new ArrayList<>();

    public void addPosition(PodTypePosition position) {
        getPositions().add(position);
        position.setPodType(this);
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

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public List<PodTypePosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PodTypePosition> positions) {
        this.positions = positions;
    }
}
