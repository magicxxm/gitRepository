package com.mushiny.wms.masterdata.ibbasics.crud.mapper;


import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.GoodsReceiptPositionDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceiptPosition;
import com.mushiny.wms.masterdata.ibbasics.repository.GoodsReceiptRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockUnitRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsReceiptPositionMapper implements BaseMapper<GoodsReceiptPositionDTO, GoodsReceiptPosition> {

    private final StockUnitRepository stockUnitRepository;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final StockUnitMapper stockUnitMapper;
    private final UserMapper userMapper;
    private final GoodsReceiptMapper goodsReceiptMapper;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final ClientRepository clientRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final StorageLocationRepository storageLocationRepository;
    private final UnitLoadMapper unitLoadMapper;


    @Autowired
    public GoodsReceiptPositionMapper(ApplicationContext applicationContext,
                                      ClientRepository clientRepository,
                                      StockUnitRepository stockUnitRepository,
                                      UserRepository userRepository,
                                      StockUnitMapper stockUnitMapper,
                                      UserMapper userMapper,
                                      GoodsReceiptMapper goodsReceiptMapper,
                                      GoodsReceiptRepository goodsReceiptRepository,
                                      StorageLocationMapper storageLocationMapper,
                                      StorageLocationRepository storageLocationRepository, UnitLoadMapper unitLoadMapper) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.userRepository = userRepository;
        this.stockUnitMapper = stockUnitMapper;
        this.userMapper = userMapper;
        this.goodsReceiptMapper = goodsReceiptMapper;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.storageLocationRepository = storageLocationRepository;
        this.unitLoadMapper = unitLoadMapper;
    }

    @Override
    public GoodsReceiptPositionDTO toDTO(GoodsReceiptPosition entity) {
        if (entity == null) {
            return null;
        }
        GoodsReceiptPositionDTO dto = new GoodsReceiptPositionDTO(entity);

        dto.setAmount(entity.getAmount());
        dto.setItemData(entity.getItemData());
        dto.setLot(entity.getLot());
        dto.setReceiptType(entity.getReceiptType());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());

        dto.setStockUnit(stockUnitMapper.toDTO(entity.getStockUnit()));
        dto.setOperator(userMapper.toDTO(entity.getOperator()));
        dto.setGoodsReceipt(goodsReceiptMapper.toDTO(entity.getGoodsReceipt()));
        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
        dto.setUnitLoad(unitLoadMapper.toDTO(entity.getUnitLoad()));
        return dto;
    }

    @Override
    public GoodsReceiptPosition toEntity(GoodsReceiptPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        GoodsReceiptPosition entity = new GoodsReceiptPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setAmount(dto.getAmount());
        entity.setItemData(dto.getItemData());
        entity.setLot(dto.getLot());
        entity.setReceiptType(dto.getReceiptType());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getReceiveToStockUnitId() != null) {
            entity.setStockUnit(stockUnitRepository.retrieve(dto.getReceiveToStockUnitId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getGoodsReceiptId() != null) {
            entity.setGoodsReceipt(goodsReceiptRepository.retrieve(dto.getGoodsReceiptId()));
        }
        if (dto.getReceiveToLocation() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getReceiveToLocation()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(GoodsReceiptPositionDTO dto, GoodsReceiptPosition entity) {
    }
}
