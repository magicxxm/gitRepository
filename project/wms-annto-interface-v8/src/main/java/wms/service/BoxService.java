package wms.service;

import wms.common.crud.AccessDTO;
import wms.crud.dto.BoxDTO;

/**
 * Created by 123 on 2017/12/6.
 */
public interface BoxService {

    AccessDTO synchronous(BoxDTO boxDTO);
}
