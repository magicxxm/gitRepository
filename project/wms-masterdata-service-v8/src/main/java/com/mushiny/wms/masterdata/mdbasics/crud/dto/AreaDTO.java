package com.mushiny.wms.masterdata.mdbasics.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseClientAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;

import javax.validation.constraints.NotNull;

public class AreaDTO extends BaseClientAssignedDTO {
    private static final long serialVersionUID = 1L;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private boolean useForGoodsIn;

    @NotNull
    private boolean useForGoodsOut;

    @NotNull
    private boolean useForPicking;

    @NotNull
    private boolean useForReplenish;

    @NotNull
    private boolean useForStorage;

    @NotNull
    private boolean useForTransfer;

    @NotNull
    private boolean useForReturn;

    @NotNull
    private boolean useForTransport;

    public AreaDTO() {
    }

    public AreaDTO(Area entity) {
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

    public boolean isUseForGoodsIn() {
        return useForGoodsIn;
    }

    public void setUseForGoodsIn(boolean useForGoodsIn) {
        this.useForGoodsIn = useForGoodsIn;
    }

    public boolean isUseForGoodsOut() {
        return useForGoodsOut;
    }

    public void setUseForGoodsOut(boolean useForGoodsOut) {
        this.useForGoodsOut = useForGoodsOut;
    }

    public boolean isUseForPicking() {
        return useForPicking;
    }

    public void setUseForPicking(boolean useForPicking) {
        this.useForPicking = useForPicking;
    }

    public boolean isUseForReplenish() {
        return useForReplenish;
    }

    public void setUseForReplenish(boolean useForReplenish) {
        this.useForReplenish = useForReplenish;
    }

    public boolean isUseForStorage() {
        return useForStorage;
    }

    public void setUseForStorage(boolean useForStorage) {
        this.useForStorage = useForStorage;
    }

    public boolean isUseForTransfer() {
        return useForTransfer;
    }

    public void setUseForTransfer(boolean useForTransfer) {
        this.useForTransfer = useForTransfer;
    }

    public boolean isUseForReturn() {
        return useForReturn;
    }

    public void setUseForReturn(boolean useForReturn) {
        this.useForReturn = useForReturn;
    }

    public boolean isUseForTransport() {
        return useForTransport;
    }

    public void setUseForTransport(boolean useForTransport) {
        this.useForTransport = useForTransport;
    }

}
