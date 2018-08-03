package com.mushiny.wms.masterdata.obbasics.domain;

import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.masterdata.general.domain.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "OB_PICKINGAREA", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "NAME", "CLIENT_ID", "WAREHOUSE_ID"})
})
public class PickingArea extends BaseClientAssignedEntity {
    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("positionNo")
    @OneToMany(mappedBy = "pickingArea", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<PickingAreaPosition> positions = new ArrayList<>();

    @ManyToMany
    @OrderBy("username")
    @JoinTable(
            name = "OB_PICKINGAREAELIGIBILITY",
            joinColumns = @JoinColumn(name = "PICKINGAREA_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private List<User> users = new ArrayList<>();

    public void addPosition(PickingAreaPosition position) {
        getPositions().add(position);
        position.setPickingArea(this);
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

    public List<PickingAreaPosition> getPositions() {
        return positions;
    }

    public void setPositions(List<PickingAreaPosition> positions) {
        this.positions = positions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
