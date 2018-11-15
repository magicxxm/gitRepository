package wms.service;

import wms.business.dto.PackInfo;
import wms.common.crud.AccessDTO;
import wms.common.crud.ResponseDTO;
import wms.crud.dto.PackConfirmDTO;
import wms.crud.dto.PackDTO;
import wms.crud.dto.StorageDTO;

/**
 * Created by 123 on 2017/9/3.
 */
public interface Pack {

    void triggerInfo(String id);

    AccessDTO getInfo(PackDTO packDTO);

    AccessDTO confirm(PackConfirmDTO packConfirmDTO);

    AccessDTO loginStation(PackDTO packDTO);

    AccessDTO loginOut(String stationCode, String warehoseCode);

    AccessDTO checkStorage(StorageDTO storageDTO);

    AccessDTO getlightOff(StorageDTO storageDTO);

    AccessDTO getShipmentByDigital(StorageDTO storageDTO);


}
