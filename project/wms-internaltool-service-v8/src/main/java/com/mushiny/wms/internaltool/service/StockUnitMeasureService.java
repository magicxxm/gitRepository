package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.web.dto.ItemDataDTO;
import com.mushiny.wms.internaltool.web.dto.StockDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 123 on 2017/11/8.
 */
public interface StockUnitMeasureService {

    List<StockDTO> getAllStockUnit();

    ItemDataDTO getItem(String itemNo,String client);

    List<StockDTO> searchStockUnit(String param);

    Page<StockDTO> getStockUnit(Pageable pageable );

    List<StockDTO> exportStockUnit();

}
