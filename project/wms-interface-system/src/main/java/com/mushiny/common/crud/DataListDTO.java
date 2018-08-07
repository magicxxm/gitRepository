package com.mushiny.common.crud;

import java.io.Serializable;
import java.math.BigDecimal;

public class DataListDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String sourceId;

    private String destinationId;

    private String itemDataId;

    private BigDecimal amount;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
