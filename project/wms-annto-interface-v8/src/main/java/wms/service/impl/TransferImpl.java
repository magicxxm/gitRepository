package wms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.service.Transfer;
import wms.business.AnntoBusiness;
import wms.business.TransferBusiness;
import wms.common.crud.AccessDTO;
import wms.crud.common.dto.MovementDTO;
import wms.crud.dto.TransferConfirmDTO;

@Service
@Transactional
public class TransferImpl implements Transfer{

    private final AnntoBusiness anntoBusiness;
    private final TransferBusiness transferBusiness;

    @Autowired
    public TransferImpl(AnntoBusiness anntoBusiness,
                        TransferBusiness transferBusiness){
        this.anntoBusiness = anntoBusiness;
        this.transferBusiness = transferBusiness;
    }
    @Override
    public AccessDTO update(MovementDTO dto) {
        AccessDTO accessDTO = new AccessDTO();
        /**
         * 检查移库单中商品在仓库中是否存在库存
         */
        accessDTO = transferBusiness.checkStockUnit(dto);
        if(accessDTO.getCode().equals("1")){
            return accessDTO;
        }
        /**
         * 将移库单信息存入美的表中
         */
        accessDTO = transferBusiness.createAnntoTransfer(dto);
        /*if(accessDTO.getCode().equals("1")){
            return accessDTO;
        }*/
        /**
         * 在wms库中生成调拨任务
         */
        transferBusiness.createTransferShipment(dto);
        return accessDTO;
    }

    @Override
    public void confirm(TransferConfirmDTO transferConfirmDTO) {

        anntoBusiness.confirmTransfer(transferConfirmDTO);
    }
}
