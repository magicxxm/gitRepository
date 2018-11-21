package com.mushiny.auth.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SYS_USERGROUP")
public class UserGroup extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NAME", unique = true, nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "userGroup", cascade = CascadeType.ALL)
    private List<User> users;

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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toUniqueString() {
        return getName();
    }
}
