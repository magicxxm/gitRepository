package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.StockChangeInfo;

/**
 * Created by 123 on 2018/2/9.
 */
public interface ChangeStockService {

    AccessDTO changeStockUnit(StockChangeInfo dto);
}
