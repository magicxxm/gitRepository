package com.mushiny.wms.report.query.dto.pp_work;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PpNameData implements Serializable {
    private static final long serialVersionUID = 1L;

    private Set<LocalDateTime> times = new HashSet<>();

    List<PpNames> PpNameData = new ArrayList<>();

    public Set<LocalDateTime> getTimes() {
        return times;
    }

    public void setTimes(Set<LocalDateTime> times) {
        this.times = times;
    }

    public List<PpNames> getPpNameData() {
        return PpNameData;
    }

    public void setPpNameData(List<PpNames> ppNameData) {
        PpNameData = ppNameData;
    }
}
