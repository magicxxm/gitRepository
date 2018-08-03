package com.mushiny.wms.tot.general.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.tot.general.crud.dto.UserDTO;
import com.mushiny.wms.tot.general.domain.User;
import com.mushiny.wms.tot.general.domain.Warehouse;

/**
 * Created by Laptop-8 on 2017/6/10.
 */
public interface UserService  extends BaseService<UserDTO> {
    User findByUsername(String username,String currentWarehouseId);
}
