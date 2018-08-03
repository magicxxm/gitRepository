package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.RoleDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RoleService extends BaseService<RoleDTO> {
    void importFile(MultipartFile file);
}
