package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.StocktakingDTO;

/**
 * Created by 123 on 2018/2/2.
 */
public interface StockService {

    AccessDTO createStock(StocktakingDTO stocktakingDTO);
}
