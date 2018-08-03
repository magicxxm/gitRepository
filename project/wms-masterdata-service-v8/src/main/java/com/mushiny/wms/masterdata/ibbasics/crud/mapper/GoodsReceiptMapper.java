package com.mushiny.wms.masterdata.ibbasics.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.AdviceRequestDTO;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.GoodsReceiptDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.GoodsReceipt;
import com.mushiny.wms.masterdata.ibbasics.repository.AdviceRequestRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationMapper;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import com.mushiny.wms.masterdata.general.crud.mapper.ClientMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.crud.mapper.WarehouseMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GoodsReceiptMapper implements BaseMapper<GoodsReceiptDTO, GoodsReceipt> {

    private final StorageLocationRepository storageLocationRepository;
    private final UserRepository userRepository;
    private final AdviceRequestRepository adviceRequestRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationMapper storageLocationMapper;
    private final UserMapper userMapper;
    private final AdviceRequestMapper adviceRequestMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public GoodsReceiptMapper(ApplicationContext applicationContext,
                              StorageLocationRepository storageLocationRepository,
                              UserRepository userRepository,
                              AdviceRequestRepository adviceRequestRepository,
                              StorageLocationMapper storageLocationMapper,
                              UserMapper userMapper,
                              AdviceRequestMapper adviceRequestMapper, ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.storageLocationRepository = storageLocationRepository;
        this.userRepository = userRepository;
        this.adviceRequestRepository = adviceRequestRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.userMapper = userMapper;
        this.adviceRequestMapper = adviceRequestMapper;
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
        entity.setGrNo(dto.getGrNo());
        entity.setDeliveryNote(dto.getDeliveryNote());
        entity.setReceiptDate(dto.getReceiptDate());
        entity.setReceiptState(dto.getReceiptState());

        applicationContext.isCurrentClient(dto.getClientId());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        if (dto.getRelatedAdviceId() != null) {
            entity.setRelatedAdvice(adviceRequestRepository.retrieve(dto.getRelatedAdviceId()));
        }

        return entity;
    }

    @Override
    public void updateEntityFromDTO(GoodsReceiptDTO dto, GoodsReceipt entity) {
    }
}
