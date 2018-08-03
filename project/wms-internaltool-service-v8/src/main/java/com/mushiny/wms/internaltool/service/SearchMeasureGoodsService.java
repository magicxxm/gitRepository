package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.common.domain.MeasureRecord;

import java.util.List;

public interface SearchMeasureGoodsService {

    List<MeasureRecord> getBySearchTerm(String searchTerm, String startDate, String endDate);
}
