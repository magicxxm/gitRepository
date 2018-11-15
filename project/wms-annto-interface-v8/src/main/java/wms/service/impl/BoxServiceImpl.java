package wms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.BoxBusiness;
import wms.common.crud.AccessDTO;
import wms.crud.dto.BoxDTO;
import wms.domain.AnntoBox;
import wms.domain.common.BoxType;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;
import wms.repository.AnntoBoxRepository;
import wms.repository.common.BoxTypeRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.WarehouseRepository;
import wms.service.BoxService;


/**
 * Created by 123 on 2017/12/6.
 */
@Service
@Transactional
public class BoxServiceImpl implements BoxService {
    private final Logger log = LoggerFactory.getLogger(BoxServiceImpl.class);

    private final BoxBusiness boxBusiness;
    private final AnntoBoxRepository anntoBoxRepository;
    private final BoxTypeRepository boxTypeRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;


    public BoxServiceImpl(BoxBusiness boxBusiness,
                          AnntoBoxRepository anntoBoxRepository,
                          BoxTypeRepository boxTypeRepository,
                          ClientRepository clientRepository,
                          WarehouseRepository warehouseRepository){
        this.boxBusiness = boxBusiness;
        this.anntoBoxRepository = anntoBoxRepository;
        this.boxTypeRepository = boxTypeRepository;
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;

    }

    @Override
    public AccessDTO synchronous(BoxDTO boxDTO) {

        AccessDTO accessDTO = new AccessDTO();
        if(accessDTO == null){
            log.info("Container 同步信息有错，没法更新……");
            accessDTO.setCode("1");
            accessDTO.setMsg("对象为空");
            return accessDTO;
        }

        Warehouse warehouse = warehouseRepository.getByWarehouseNo(boxDTO.getWarehouseCode());
        Client client = clientRepository.findByClientNo(boxDTO.getCompanyCode());

        //同步箱型到ANNTO——Box
        AnntoBox anntoBox = anntoBoxRepository.getByContainerCodeAndWarehouseCode(boxDTO.getContainerCode(),boxDTO.getWarehouseCode(),boxDTO.getCompanyCode());
        if(anntoBox == null){
            anntoBox = boxBusiness.createAnntoBox(boxDTO);
            log.info("箱型同步保存到annto-box。。。");
        }else {
            log.info("箱型同步更新到annto-box。。。");
            anntoBox = boxBusiness.updateAnntoBox(anntoBox,boxDTO);
        }

        //同步箱型保存到wms系统
        BoxType box = boxTypeRepository.getByName(boxDTO.getContainerCode(),warehouse.getId(),client.getId());
        if(box == null){
            box = boxBusiness.createBoxType(boxDTO,warehouse,client);
            log.info("箱型同步保存到wms-boxType。。。");
        }else {
            box = boxBusiness.updateBoxType(box,boxDTO);
            log.info("箱型更新到wms-boxType。。。");
        }

        return accessDTO;
    }
}
