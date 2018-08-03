package com.mushiny.wms.tot.ppr.query.dto;

import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.tot.ppr.domain.JobCategoryRelation;

import java.io.Serializable;

/**
 * Created by Laptop-8 on 2017/6/29.
 * 工作汇总页面DTO
 */
public class PprMainPageDTO extends BaseDTO {

    private  String  mainProcesses;
    private  String  coreProcesses;
    private  String  categoryName;
    private  String  lineItems;
    private  String reorder;
    private  String  jobType;
    private  String  unit = "-";
    private  long  amount;
    private  double  hours;//直接工作,普通间接，超级间接
    private  double  rates;
    private String warehouseId;
    private String clientId;
    private  double  planRate;
    private  double  planHours;
    private  double  increment;
    private  String  quotient;

    public PprMainPageDTO(  ) {

    }
    public PprMainPageDTO(JobCategoryRelation entity ) {
        super(entity);
    }

    public String getMainProcesses() {
        return mainProcesses;
    }

    public void setMainProcesses(String mainProcesses) {
        this.mainProcesses = mainProcesses;
    }

    public String getCoreProcesses() {
        return coreProcesses;
    }

    public void setCoreProcesses(String coreProcesses) {
        this.coreProcesses = coreProcesses;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLineItems() {
        return lineItems;
    }

    public void setLineItems(String lineItems) {
        this.lineItems = lineItems;
    }

    public String getReorder() {
        return reorder;
    }

    public void setReorder(String reorder) {
        this.reorder = reorder;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public double getHours() {
        return hours;
    }

    public void setHours(double hours) {
        this.hours = hours;
    }

    public double getRates() {
        return rates;
    }

    public void setRates(double rates) {
        this.rates = rates;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public double getPlanRate() {
        return planRate;
    }

    public void setPlanRate(double planRate) {
        this.planRate = planRate;
    }

    public double getPlanHours() {
        return planHours;
    }

    public void setPlanHours(double planHours) {
        this.planHours = planHours;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

    public String getQuotient() {
        return quotient;
    }

    public void setQuotient(String quotient) {
        this.quotient = quotient;
    }
}
