package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemDataService extends BaseService<ItemDataDTO> {

    List<ItemDataDTO> getByClientId(String clientId);
    void importFile(MultipartFile file);
}
