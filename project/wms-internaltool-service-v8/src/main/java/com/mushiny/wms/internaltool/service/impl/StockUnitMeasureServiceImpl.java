package com.mushiny.wms.internaltool.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.internaltool.common.domain.Client;
import com.mushiny.wms.internaltool.common.domain.ItemData;
import com.mushiny.wms.internaltool.common.repository.ClientRepository;
import com.mushiny.wms.internaltool.common.repository.ItemDataRepository;
import com.mushiny.wms.internaltool.query.StockUnitQuery;
import com.mushiny.wms.internaltool.service.StockUnitMeasureService;
import com.mushiny.wms.internaltool.web.dto.ItemDataDTO;
import com.mushiny.wms.internaltool.web.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/11/8.
 */
@Service
@Transactional(readOnly = true)
public class StockUnitMeasureServiceImpl implements StockUnitMeasureService{

    private final StockUnitQuery stockUnitQuery;
    private final ApplicationContext applicationContext;
    private final ItemDataRepository itemDataRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public StockUnitMeasureServiceImpl(StockUnitQuery stockUnitQuery,
                                       ApplicationContext applicationContext,
                                       ItemDataRepository itemDataRepository,
                                       ClientRepository clientRepository){
        this.stockUnitQuery = stockUnitQuery;
        this.applicationContext = applicationContext;
        this.itemDataRepository = itemDataRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<StockDTO> getAllStockUnit() {
        String warehouse = applicationContext.getCurrentWarehouse();
        return stockUnitQuery.getStockInfo(warehouse);
    }

    @Override
    public ItemDataDTO getItem(String itemNo,String clientName) {
//        List<ItemDataDTO> dtos = new ArrayList<>();
        String warehouse = applicationContext.getCurrentWarehouse();
        Client client = clientRepository.getByClientName(clientName);
        ItemData itemData = itemDataRepository.getByItemNo(itemNo,client.getId(),warehouse);
        ItemDataDTO dto = entityToDTO(itemData);
//        dtos.add(dto);
        return dto;
    }

    @Override
    public List<StockDTO> searchStockUnit(String param) {
        return stockUnitQuery.getByStockUnit(param);
    }

    @Override
    public Page<StockDTO> getStockUnit(Pageable pageable) {
        String warehouse = applicationContext.getCurrentWarehouse();
        return stockUnitQuery.getStockUnitInfo(warehouse,pageable);
    }

    @Override
    public List<StockDTO> exportStockUnit() {
        return stockUnitQuery.exportStockUnitInfo();
    }

    private ItemDataDTO entityToDTO(ItemData entity) {
        if (entity == null) {
            return null;
        }

        ItemDataDTO dto = new ItemDataDTO();

        dto.setItemNo(entity.getItemNo());
        dto.setSkuNo(entity.getSkuNo());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setSafetyStock(entity.getSafetyStock());
        dto.setLotMandatory(entity.isLotMandatory());
        dto.setSerialRecordType(entity.getSerialRecordType());
        dto.setSerialRecordLength(entity.getSerialRecordLength());
        dto.setHeight(entity.getHeight());
        dto.setWidth(entity.getWidth());
        dto.setDepth(entity.getDepth());
        dto.setVolume(entity.getVolume());
        dto.setWeight(entity.getWeight());
        dto.setMultiplePart(entity.isMultiplePart());
        dto.setMultiplePartAmount(entity.getMultiplePartAmount());
        dto.setMeasured(entity.isMeasured());
        dto.setPreferOwnBox(entity.isPreferOwnBox());
        dto.setPreferBag(entity.isPreferBag());
        dto.setUseBubbleFilm(entity.isUseBubbleFilm());
        dto.setLotType(entity.getLotType());
        dto.setLotUnit(entity.getLotUnit());

        dto.setItemSellingDegree(entity.getItemSellingDegree());
        dto.setLotThreshold(entity.getLotThreshold());

        dto.setClientName(entity.getClient().getName());
        dto.setWarehouse(entity.getWarehouseId());

        return dto;
    }
}
