package com.mushiny.wms.internaltool.service;

import com.mushiny.wms.internaltool.web.dto.LotManagerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 123 on 2017/11/10.
 */
public interface LotManagerService {

    List<LotManagerDTO> getAllStockUnits();

    Page<LotManagerDTO> getStockUnits(Pageable pageable);

    List<LotManagerDTO> getByParam(String param);
}
