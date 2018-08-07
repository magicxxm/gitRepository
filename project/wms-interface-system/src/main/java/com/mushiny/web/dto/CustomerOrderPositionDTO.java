package com.mushiny.web.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 123 on 2018/2/1.
 */
public class CustomerOrderPositionDTO implements Serializable {

    private BigDecimal amount;

    private String itemNo;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

}
