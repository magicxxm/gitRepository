package wms.service;

import org.springframework.web.bind.annotation.RequestBody;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.MovementDTO;
import wms.crud.dto.TransferConfirmDTO;
import wms.crud.dto.TransferUpdateDTO;

public interface Transfer {

    AccessDTO update(MovementDTO dto);

    void confirm(@RequestBody TransferConfirmDTO transferConfirmDTO);
}
