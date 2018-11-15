package wms.crud.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import wms.common.crud.dto.BaseDTO;
import wms.domain.common.ItemGroup;

import javax.validation.constraints.NotNull;

public class ItemGroupDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private int orderIndex = 0;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String parentId;

    private ItemGroupDTO parentItemGroup;

    public ItemGroupDTO() {
    }

    public ItemGroupDTO(ItemGroup entity) {
        super(entity);
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

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ItemGroupDTO getParentItemGroup() {
        return parentItemGroup;
    }

    public void setParentItemGroup(ItemGroupDTO parentItemGroup) {
        this.parentItemGroup = parentItemGroup;
    }
}
