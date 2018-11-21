package com.mushiny.auth.service.dto;

import com.mushiny.auth.domain.UserGroup;
import com.mushiny.auth.service.exception.VersionException;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserGroupDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @Size(max = 255)
    private String description;

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

    public UserGroupDTO(UserGroup userGroup) {
        super(userGroup);

//        this.name = userGroup.getName();
//        this.description = userGroup.getDescription();
    }

    public void merge(UserGroup userGroup) throws VersionException {
        super.merge(userGroup);

//        userGroup.setName(this.name);
//        userGroup.setDescription(this.description);
    }
}
