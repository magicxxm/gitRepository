package com.mushiny.wms.masterdata.mdbasics.service;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.common.service.BaseService;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataGlobalDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.enums.Message;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ItemDataGlobalService extends BaseService<ItemDataGlobalDTO> {

    List<ItemDataGlobalDTO> getBySkuNo(String skuNo);
    List<ItemDataGlobalDTO> getByClientId(String clientId);
    List<ItemDataGlobalDTO>  getAll();
    List<ItemDataGlobalDTO> updateSize(ImportDTO dto);
    void importFile( MultipartFile file) throws IOException;
    Message uploadSkuGlobal(MultipartFile file, Map map, Message message);
    Message updateSkuGlobal(MultipartFile file, Map map, Message message);
}
