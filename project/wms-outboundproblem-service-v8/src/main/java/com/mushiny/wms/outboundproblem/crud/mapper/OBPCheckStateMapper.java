package com.mushiny.wms.outboundproblem.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.outboundproblem.crud.common.mapper.InboundProblemRuleMapper;
import com.mushiny.wms.outboundproblem.crud.common.mapper.StorageLocationMapper;
import com.mushiny.wms.outboundproblem.crud.dto.OBPCheckStateDTO;
import com.mushiny.wms.outboundproblem.domain.OBPCheckState;
import com.mushiny.wms.outboundproblem.domain.OBProblemCheck;
import com.mushiny.wms.outboundproblem.repository.OBProblemCheckRepository;
import com.mushiny.wms.outboundproblem.repository.common.InboundProblemRuleRepository;
import com.mushiny.wms.outboundproblem.repository.common.StorageLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OBPCheckStateMapper implements BaseMapper<OBPCheckStateDTO, OBPCheckState> {

    private final ApplicationContext applicationContext;
    private final InboundProblemRuleMapper inboundProblemRuleMapper;
    private final InboundProblemRuleRepository inboundProblemRuleRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final OBProblemCheckMapper obProblemCheckMapper;
    private final StorageLocationRepository storageLocationRepository;
    private final OBProblemCheckRepository obProblemCheckRepository;

    @Autowired
    public OBPCheckStateMapper(ApplicationContext applicationContext,
                               InboundProblemRuleMapper inboundProblemRuleMapper,
                               InboundProblemRuleRepository inboundProblemRuleRepository,
                               StorageLocationMapper storageLocationMapper,
                               OBProblemCheckMapper obProblemCheckMapper,
                               StorageLocationRepository storageLocationRepository,
                               OBProblemCheckRepository obProblemCheckRepository) {
        this.applicationContext = applicationContext;
        this.inboundProblemRuleMapper = inboundProblemRuleMapper;
        this.inboundProblemRuleRepository = inboundProblemRuleRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.obProblemCheckMapper = obProblemCheckMapper;
        this.storageLocationRepository = storageLocationRepository;
        this.obProblemCheckRepository = obProblemCheckRepository;
    }

    @Override
    public OBPCheckStateDTO toDTO(OBPCheckState entity) {
        if (entity == null) {
            return null;
        }
        OBPCheckStateDTO dto = new OBPCheckStateDTO(entity);
        dto.setObProblem(obProblemCheckMapper.toDTO(entity.getObproblem()));
        dto.setAmount(entity.getAmount());
        dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(entity.getInboundProblemRule()));
        dto.setStorageLocation(storageLocationMapper.toDTO(entity.getStorageLocation()));
        dto.setState(entity.getState());
        dto.setSolveBy(entity.getSolveBy());
        dto.setSolveDate(entity.getSolveDate());
        dto.setItemData(entity.getItemData());
        dto.setClientId(entity.getClientId());
        dto.setWarehouse(entity.getWarehouseId());
                dto.setWarehouse(entity.getWarehouseId());

        dto.setState(entity.getState());
        return dto;
    }

    @Override
    public OBPCheckState toEntity(OBPCheckStateDTO dto) {
        if (dto == null) {
            return null;
        }
        OBPCheckState entity = new OBPCheckState();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        if (dto.getProblemId() != null) {
            entity.setObproblem(obProblemCheckRepository.retrieve(dto.getProblemId()));
        }
        entity.setAmount(dto.getAmount());
        if (dto.getInboundProblemRuleId() != null) {
            entity.setInboundProblemRule(inboundProblemRuleRepository.retrieve(dto.getInboundProblemRuleId()));
        }
        if (dto.getStorageLocationId() != null) {
            entity.setStorageLocation(storageLocationRepository.retrieve(dto.getStorageLocationId()));
        }

        entity.setState(dto.getState());
        entity.setSolveBy(dto.getSolveBy());
        entity.setSolveDate(dto.getSolveDate());
        entity.setClientId(dto.getClientId());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(OBPCheckStateDTO dto, OBPCheckState entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
    }



    public List<OBPCheckStateDTO> toSolveList(List<OBProblemCheck> inboundProblems) {
        List<OBPCheckStateDTO> list = new ArrayList<>();
        for (OBProblemCheck obproblemCheck : inboundProblems) {
            OBPCheckStateDTO dto = new OBPCheckStateDTO();
            dto.setObProblem(obProblemCheckMapper.toDTO(obproblemCheck));
            dto.setState("1");
            dto.setInboundProblemRule(inboundProblemRuleMapper.toDTO(inboundProblemRuleRepository.getByName("Process")));
            dto.setAmount(obproblemCheck.getAmount().subtract(obproblemCheck.getSolveAmount()));
            dto.setSolveDate(LocalDateTime.now());
            dto.setSolveBy(obProblemCheckRepository.getByUserIds(applicationContext.getCurrentUser()));
            dto.setStorageLocation(storageLocationMapper.toDTO(obProblemCheckRepository.
                    getByProblemStorageLocationName(obproblemCheck.getProblemStoragelocation())));
            list.add(dto);
        }
        return list;
    }
}
