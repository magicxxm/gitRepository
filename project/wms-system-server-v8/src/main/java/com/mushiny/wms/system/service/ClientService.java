package com.mushiny.wms.system.service;

import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.system.crud.dto.ClientDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClientService extends BaseService<ClientDTO> {

    List<ClientDTO> getByCurrentWarehouseId();
    void importFile(MultipartFile file);
}
