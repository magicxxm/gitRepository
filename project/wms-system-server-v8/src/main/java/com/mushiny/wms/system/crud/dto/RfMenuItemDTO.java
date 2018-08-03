package com.mushiny.wms.system.crud.dto;

import java.io.Serializable;
import java.util.List;

public class RfMenuItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private RfMenuDTO rfMenu;

    private List<RfMenuItemDTO> childMenuItems;

    public RfMenuDTO getRfMenu() {
        return rfMenu;
    }

    public void setRfMenu(RfMenuDTO rfMenu) {
        this.rfMenu = rfMenu;
    }

    public List<RfMenuItemDTO> getChildMenuItems() {
        return childMenuItems;
    }

    public void setChildMenuItems(List<RfMenuItemDTO> childMenuItems) {
        this.childMenuItems = childMenuItems;
    }
}
