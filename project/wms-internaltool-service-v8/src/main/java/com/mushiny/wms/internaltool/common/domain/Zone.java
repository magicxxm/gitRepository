package com.mushiny.wms.internaltool.common.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MD_ZONE", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "CLIENT_ID", "WAREHOUSE_ID"
        })
})
public class Zone extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany
    @OrderBy("name")
    @JoinTable(
            name = "MD_ZONE_ITEMGROUP",
            joinColumns = @JoinColumn(name = "ZONE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ITEMGROUP_ID"))
    private Set<ItemGroup> itemGroups = new HashSet<>();

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

    public Set<ItemGroup> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(Set<ItemGroup> itemGroups) {
        this.itemGroups = itemGroups;
    }
}
