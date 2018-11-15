package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseDTO;
import wms.domain.common.ItemUnit;

import javax.validation.constraints.NotNull;

public class ItemUnitDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    @NotNull
    private String unitType;

    @NotNull
    private int baseFactor;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String baseUnitId;

    private ItemUnitDTO baseUnit;

    public ItemUnitDTO() {
    }

    public ItemUnitDTO(ItemUnit entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public int getBaseFactor() {
        return baseFactor;
    }

    public void setBaseFactor(int baseFactor) {
        this.baseFactor = baseFactor;
    }

    public String getBaseUnitId() {
        return baseUnitId;
    }

    public void setBaseUnitId(String baseUnitId) {
        this.baseUnitId = baseUnitId;
    }

    public ItemUnitDTO getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(ItemUnitDTO baseUnit) {
        this.baseUnit = baseUnit;
    }
}
