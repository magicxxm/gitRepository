package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.UserGroupDTO;

import java.util.List;

public interface UserGroupService extends BaseService<UserGroupDTO> {

    List<UserGroupDTO> getAll();
}
