package wms.service;

import org.springframework.web.bind.annotation.RequestBody;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.StocktakingDTO;
import wms.crud.dto.AdjustConfirmDTO;
import wms.crud.dto.CheckConfirmDTO;
import wms.crud.dto.CheckUpdateDTO;
import wms.web.vm.AdjustItemDTO;

import java.util.List;

public interface Check {

//    AccessDTO update(StocktakingDTO dto);

    AccessDTO update(CheckUpdateDTO dto);

    void confirm(String  checkNo);

    void confirm(CheckConfirmDTO checkConfirmDTO);

    void adjustItem(List<AdjustItemDTO> adjustItemDTOS);

    AccessDTO adjustConfirm(List<AdjustConfirmDTO> adjustConfirmDTOS);
}
