package wms.crud.common.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wms.common.context.ApplicationContext;
import wms.common.crud.mapper.BaseMapper;
import wms.crud.common.dto.GoodsReceiptDTO;
import wms.domain.GoodsReceipt;
import wms.repository.common.AdviceRequestRepository;
import wms.repository.common.ClientRepository;
import wms.repository.common.WarehouseRepository;

@Component
public class GoodsReceiptMapper implements BaseMapper<GoodsReceiptDTO, GoodsReceipt> {

    private final AdviceRequestRepository adviceRequestRepository;
    private final ApplicationContext applicationContext;
    private final AdviceRequestMapper adviceRequestMapper;
    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public GoodsReceiptMapper(ApplicationContext applicationContext,
                              AdviceRequestRepository adviceRequestRepository,
                              AdviceRequestMapper adviceRequestMapper,
                              ClientRepository clientRepository, WarehouseRepository warehouseRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.adviceRequestRepository = adviceRequestRepository;
        this.adviceRequestMapper = adviceRequestMapper;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public GoodsReceiptDTO toDTO(GoodsReceipt entity) {
        if (entity == null) {
            return null;
        }
        GoodsReceiptDTO dto = new GoodsReceiptDTO(entity);

        dto.setGrNo(entity.getGrNo());
        dto.setSize(entity.getSize());
        dto.setDeliveryNote(entity.getDeliveryNote());
        dto.setReceiptDate(entity.getReceiptDate());
        dto.setReceiptState(entity.getReceiptState());

        dto.setClient(clientRepository.retrieve(entity.getClientId()).getName());
        dto.setClientId(entity.getClientId());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setRelatedAdvice(adviceRequestMapper.toDTO(entity.getRelatedAdvice()));

        return dto;
    }

    @Override
    public GoodsReceipt toEntity(GoodsReceiptDTO dto) {
        if (dto == null) {
            return null;
        }
        GoodsReceipt entity = new GoodsReceipt();

        entity.setId(dto.getId());
        entity.setSize(dto.getSize());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setGrNo(dto.getCode());
        entity.setDeliveryNote(dto.getDeliveryNote());
        entity.setReceiptDate(dto.getReceiptDate());
        entity.setReceiptState(dto.getReceiptState());

//        entity.setClientId(dto.getClientId());
        entity.setClientId(clientRepository.findByClientNo(dto.getCompanyCode()).getId());
        entity.setWarehouseId(warehouseRepository.getByWarehouseNo(dto.getWarehouseCode()).getId());

        if (dto.getRelatedAdviceId() != null) {
            entity.setRelatedAdvice(adviceRequestRepository.retrieve(dto.getRelatedAdviceId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(GoodsReceiptDTO dto, GoodsReceipt entity) {
    }
}
