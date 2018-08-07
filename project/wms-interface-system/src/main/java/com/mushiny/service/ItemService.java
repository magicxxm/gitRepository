package com.mushiny.service;

import com.mushiny.common.crud.AccessDTO;
import com.mushiny.web.dto.ItemChangeDTO;
import com.mushiny.web.dto.ItemDTO;
import com.mushiny.web.dto.SkuNoDTO;

import java.util.List;

/**
 * Created by 123 on 2018/2/1.
 */
public interface ItemService {

    List<String> synchronous(ItemDTO itemDTO);

    AccessDTO updateItem(SkuNoDTO dto);
}
