package wms.service;

import org.springframework.web.bind.annotation.RequestBody;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.ReplenishmentDTO;
import wms.crud.dto.AllocationConfirmDTO;
import wms.crud.dto.AllocationUpdateDTO;

public interface Allocation {

    AccessDTO update(ReplenishmentDTO dto);

    void confirm(AllocationConfirmDTO allocationConfirmDTO);
}
