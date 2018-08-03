package com.mushiny.wms.masterdata.mdbasics.crud.dto;


import com.mushiny.wms.common.crud.dto.BaseDTO;
import com.mushiny.wms.common.crud.dto.BaseWarehouseAssignedDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.SizeFilterRule;
import com.mushiny.wms.masterdata.obbasics.domain.DeliveryTime;

/**
 * Created by Laptop-11 on 2017/6/8.
 */

public class SizeFilterRuleDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;
    private String name;
    private String rule;
    private int price;
    private String number;
    private String mode;

    public SizeFilterRuleDTO() {

    }

    public SizeFilterRuleDTO(SizeFilterRule entity) {
        super(entity);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
