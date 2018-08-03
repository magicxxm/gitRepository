package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageLocationService extends BaseService<StorageLocationDTO> {

    List<StorageLocationDTO> getByClientId(String clientId);
    void importFile(MultipartFile file);
    List<StorageLocationDTO> getName();
}
