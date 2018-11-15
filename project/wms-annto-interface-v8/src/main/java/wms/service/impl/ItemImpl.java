package wms.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wms.business.AnntoBusiness;
import wms.business.ItemBusiness;
import wms.business.dto.ItemCheckDTO;
import wms.common.crud.AccessDTO;
import wms.crud.dto.AnntoItemDTO;
import wms.crud.dto.BarcodeDTO;
import wms.domain.AnntoItem;
import wms.domain.ItemData;
import wms.domain.ItemDataGlobal;
import wms.domain.common.Client;
import wms.domain.common.Warehouse;
import wms.repository.AnntoRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataGlobalRepository;
import wms.repository.common.ItemDataRepository;
import wms.repository.common.WarehouseRepository;
import wms.service.Item;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemImpl implements Item{
    private Logger log = LoggerFactory.getLogger(ItemImpl.class);

    private final ItemBusiness itemBusiness;
    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final AnntoRepository anntoRepository;
    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final ItemDataRepository itemDataRepository;
    private final AnntoBusiness anntoBusiness;

    @Autowired
    public ItemImpl(AnntoBusiness anntoBusiness,
                    ItemDataGlobalRepository itemDataGlobalRepository,
                    AnntoRepository anntoRepository,
                    ItemBusiness itemBusiness,
                    WarehouseRepository warehouseRepository,
                    ClientRepository clientRepository,
                    ItemDataRepository itemDataRepository) {
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.anntoRepository = anntoRepository;
        this.itemBusiness = itemBusiness;
        this.warehouseRepository = warehouseRepository;
        this.clientRepository = clientRepository;
        this.itemDataRepository = itemDataRepository;
        this.anntoBusiness = anntoBusiness;
    }

    @Override
    public AccessDTO synchronous(AnntoItemDTO dto) {
        AccessDTO accessDTO = new AccessDTO();

        if(dto == null){
            log.error("商品同步信息有错，没法更新……");
            accessDTO.setCode("1");
            accessDTO.setMsg("对象为空");
            return accessDTO;
        }

        itemBusiness.synchronous(dto);

        return accessDTO;
    }

    @Override
    public AnntoItemDTO accept(ItemCheckDTO itemCheckDTO) {
       /* AnntoItemDTO anntoItemDTO = new AnntoItemDTO();
        List<BarcodeDTO> barcodeDTOs = new ArrayList<>();
        BarcodeDTO barcodeDTO = new BarcodeDTO();
        barcodeDTO.setBarcode("710042");
        barcodeDTO.setQuantityUM("EA");
        barcodeDTOs.add(barcodeDTO);
        anntoItemDTO.setBarcode(barcodeDTOs);
        anntoItemDTO.setBrand("IPhone");
        anntoItemDTO.setCode("0000000000");
        anntoItemDTO.setCompanyCode("SYSTEM");
        anntoItemDTO.setName("IPhone X");


        return anntoItemDTO;*/
        return anntoBusiness.getItem(itemCheckDTO);
    }
}
