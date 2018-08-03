package com.mushiny.wms.system.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.system.domain.Selection;

import java.util.List;

public interface SelectionRepository extends BaseRepository<Selection, String> {

    Selection findBySelectionKeyAndSelectionValue(String selectionKey, String selectionValue);

    List<Selection> findBySelectionKeyOrderByOrderIndex(String selectionKey);
}
