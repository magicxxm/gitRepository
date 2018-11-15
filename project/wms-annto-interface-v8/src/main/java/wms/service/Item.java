package wms.service;

import wms.business.dto.ItemCheckDTO;
import wms.common.crud.AccessDTO;
import wms.crud.dto.AnntoItemDTO;

public interface Item {

    AccessDTO synchronous(AnntoItemDTO anntoItemDTO);

    AnntoItemDTO accept(ItemCheckDTO itemCheckDTO);
}
