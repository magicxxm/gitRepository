package wms.facade;

import wms.business.dto.ApiDTO;
import wms.common.crud.AccessDTO;

/**
 * Created by 123 on 2017/12/29.
 */
public interface ApiFacade {

    AccessDTO syncIn(ApiDTO apiDTO);
}
