package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.LotRecord;

import java.util.List;

public interface SearchEntryLotService {

    List<LotRecord> getBySearchTerm(String searchTerm, String startDate, String endDate);
}
