package com.mushiny.wms.system.crud.dto;

import java.io.Serializable;
import java.util.List;

public class MenuItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private MenuDTO menu;

    private List<MenuItemDTO> childMenuItems;

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    public List<MenuItemDTO> getChildMenuItems() {
        return childMenuItems;
    }

    public void setChildMenuItems(List<MenuItemDTO> childMenuItems) {
        this.childMenuItems = childMenuItems;
    }
}
