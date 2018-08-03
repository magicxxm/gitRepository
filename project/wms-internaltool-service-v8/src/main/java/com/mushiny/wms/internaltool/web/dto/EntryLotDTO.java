package com.mushiny.wms.internaltool.web.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class EntryLotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lotId;

    private LocalDate useNotAfter;

    private String sourceId;

    private String itemDataId;

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public LocalDate getUseNotAfter() {
        return useNotAfter;
    }

    public void setUseNotAfter(LocalDate useNotAfter) {
        this.useNotAfter = useNotAfter;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getItemDataId() {
        return itemDataId;
    }

    public void setItemDataId(String itemDataId) {
        this.itemDataId = itemDataId;
    }
}
