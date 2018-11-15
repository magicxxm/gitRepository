package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.GoodsReceiptPositionDTO;
import wms.domain.GoodsReceiptPosition;
import wms.repository.GoodsReceiptRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.StockUnitRepository;
import wms.repository.common.StorageLocationRepository;
import wms.repository.common.UserRepository;

@Component
public class GoodsReceiptPositionMapper implements BaseMapper<GoodsReceiptPositionDTO, GoodsReceiptPosition> {

    private final StockUnitRepository stockUnitRepository;
    private final UserRepository userRepository;
    private final ApplicationContext applicationContext;
    private final GoodsReceiptMapper goodsReceiptMapper;
    private final GoodsReceiptRepository goodsReceiptRepository;
    private final ClientRepository clientRepository;
    private final StorageLocationRepository storageLocationRepository;


    @Autowired
    public GoodsReceiptPositionMapper(ApplicationContext applicationContext,
                                      ClientRepository clientRepository,
                                      StockUnitRepository stockUnitRepository,
                                      UserRepository userRepository,
                                      GoodsReceiptMapper goodsReceiptMapper,
                                      GoodsReceiptRepository goodsReceiptRepository,
                                      StorageLocationRepository storageLocationRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.stockUnitRepository = stockUnitRepository;
        this.userRepository = userRepository;
        this.goodsReceiptMapper = goodsReceiptMapper;
        this.goodsReceiptRepository = goodsReceiptRepository;
        this.storageLocationRepository = storageLocationRepository;
    }

    @Override
    public GoodsReceiptPositionDTO toDTO(GoodsReceiptPosition entity) {
        if (entity == null) {
            return null;
        }
        GoodsReceiptPositionDTO dto = new GoodsReceiptPositionDTO(entity);

//        dto.setAmount(entity.getAmount());
//        dto.setItemData(entity.getItemData());
//        dto.setLot(entity.getLot());
//        dto.setReceiptType(entity.getReceiptType());
//        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
//        dto.setClientId(entity.getClientId());
//        dto.setWarehouseId(entity.getWarehouseId());
//
//        dto.setStockUnit(stockUnitMapper.toDTO(entity.getStockUnit()));
//        dto.setOperator(userMapper.toDTO(entity.getOperator()));
//        dto.setGoodsReceipt(goodsReceiptMapper.toDTO(entity.getGoodsReceipt()));
//        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
//        dto.setUnitLoad(unitLoadMapper.toDTO(entity.getUnitLoad()));
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
        entity.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getReceiveToStockUnitId() != null) {
            entity.setStockUnit(stockUnitRepository.findOne(dto.getReceiveToStockUnitId()));
        }
        if (dto.getOperatorId() != null) {
            entity.setOperator(userRepository.retrieve(dto.getOperatorId()));
        }
        if (dto.getGoodsReceiptId() != null) {
            entity.setGoodsReceipt(goodsReceiptRepository.findOne(dto.getGoodsReceiptId()));
        }
        if (dto.getReceiveToLocation() != null) {
            entity.setStorageLocation(storageLocationRepository.findOne(dto.getReceiveToLocation()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(GoodsReceiptPositionDTO dto, GoodsReceiptPosition entity) {
    }
}
