package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.AdviceRequestPositionDTO;
import wms.domain.common.AdviceRequestPosition;
import wms.repository.common.ClientRepository;
import wms.repository.common.ItemDataRepository;

@Component
public class AdviceRequestPositionMapper implements BaseMapper<AdviceRequestPositionDTO, AdviceRequestPosition> {

    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataMapper itemDataMapper;
    private final AdviceRequestMapper adviceRequestMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public AdviceRequestPositionMapper(ApplicationContext applicationContext,
                                       ItemDataMapper itemDataMapper,
                                       ItemDataRepository itemDataRepository,
                                       AdviceRequestMapper adviceRequestMapper,
                                       ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.itemDataMapper = itemDataMapper;
        this.itemDataRepository = itemDataRepository;
        this.adviceRequestMapper = adviceRequestMapper;
        this.clientRepository = clientRepository;
    }

    @Override
    public AdviceRequestPositionDTO toDTO(AdviceRequestPosition entity) {
        if (entity == null) {
            return null;
        }
        AdviceRequestPositionDTO dto = new AdviceRequestPositionDTO(entity);

        dto.setPositionNo(entity.getPositionNo());
        dto.setNotifiedAmount(entity.getNotifiedAmount());
        dto.setReceiptAmount(entity.getReceiptAmount());
        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setItemData(itemDataMapper.toDTO(entity.getItemData()));
//        dto.setLot(lotMapper.toDTO(entity.getLot()));
        dto.setAdviceRequest(adviceRequestMapper.toDTO(entity.getAdviceRequest()));

        return dto;
    }

    @Override
    public AdviceRequestPosition toEntity(AdviceRequestPositionDTO dto) {
        if (dto == null) {
            return null;
        }
        AdviceRequestPosition entity = new AdviceRequestPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setPositionNo(dto.getPositionNo());
        entity.setNotifiedAmount(dto.getNotifiedAmount());
        entity.setReceiptAmount(dto.getReceiptAmount());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        if (dto.getItemDataId() != null) {
            entity.setItemData(itemDataRepository.retrieve(dto.getItemDataId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(AdviceRequestPositionDTO dto, AdviceRequestPosition entity) {
    }
}
