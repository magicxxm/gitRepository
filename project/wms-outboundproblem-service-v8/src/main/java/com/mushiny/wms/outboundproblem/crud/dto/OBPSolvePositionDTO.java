package com.mushiny.wms.outboundproblem.crud.dto;

import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.outboundproblem.domain.OBPSolve;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OBPSolvePositionDTO extends BaseWarehouseAssignedDTO {
    private static final long serialVersionUID = 1L;


    @NotNull
    private String shipmentNo;

    private String solveBy;

    private LocalDateTime solveDate;

    private String solveId;

    private String solveKey;

    private String description;

    private String location;

    private String locationContainer;

    private String itemNo;

    //商品需输有效期时，所传入的有效期商品件数
    private BigDecimal amountScaned = BigDecimal.ZERO;

    private String state;

    private String useNotAfter;

    private String serialNo;

    //确认问题数量
    private BigDecimal amountConfirmProblem = BigDecimal.ZERO;

    //区分所确认问题的数量是点击正常商品报过来的还是点击问题商品报过来的
    private String problemResources;

    private BigDecimal amountHotPick; //需要生成hotPick的数量

    private String itemDataId;

    //判断点击的问题行是扫描完成还是未扫描完成
    private String ifScaned;

    public BigDecimal getAmountConfirmProblem() {
        return amountConfirmProblem;
    }

    public void setAmountConfirmProblem(int amountConfirmProblem) {
        this.amountConfirmProblem = new BigDecimal(amountConfirmProblem);
    }

    public String getProblemResources() {
        return problemResources;
    }

    public void setProblemResources(String problemResources) {
        this.problemResources = problemResources;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getSolveBy() {
        return solveBy;
    }

    public void setSolveBy(String solveBy) {
        this.solveBy = solveBy;
    }

    public LocalDateTime getSolveDate() {
        return solveDate;
    }

    public void setSolveDate(LocalDateTime solveDate) {
        this.solveDate = solveDate;
    }

    public String getSolveId() {
        return solveId;
    }

    public void setSolveId(String solveId) {
        this.solveId = solveId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationContainer() {
        return locationContainer;
    }

    public void setLocationContainer(String locationContainer) {
        this.locationContainer = locationContainer;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSolveKey() {
        return solveKey;
    }

    public void setSolveKey(String solveKey) {
        this.solveKey = solveKey;
    }

    public BigDecimal getAmountScaned() {
        return amountScaned;
    }

    public void setAmountScaned(BigDecimal amountScaned) {
        this.amountScaned = amountScaned;
    }

    public String getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(String useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public void setAmountConfirmProblem(BigDecimal amountConfirmProblem) {
        this.amountConfirmProblem = amountConfirmProblem;
    }

    public BigDecimal getAmountHotPick() {
        return amountHotPick;
    }

    public void setAmountHotPick(BigDecimal amountHotPick) {
        this.amountHotPick = amountHotPick;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public String getIfScaned() {
        return ifScaned;
    }

    public void setIfScaned(String ifScaned) {
        this.ifScaned = ifScaned;
    }
}
