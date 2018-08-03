package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaPositionDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.TurnAreaRepository;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.DigitalLabelMapper;
import com.mushiny.wms.masterdata.obbasics.repository.DigitalLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TurnAreaPositionMapper implements BaseMapper<TurnAreaPositionDTO, TurnAreaPosition> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;
    private final TurnAreaMapper trurnAreaMapper;
    private final TurnAreaRepository trurnAreaRepository;
    private final DigitalLabelMapper digitalLabelMapper;
    private final DigitalLabelRepository digitalLabelRepository;

    @Autowired
    public TurnAreaPositionMapper(ApplicationContext applicationContext,
                                  ClientRepository clientRepository,
                                  TurnAreaMapper trurnAreaMapper,
                                  TurnAreaRepository trurnAreaRepository,
                                  DigitalLabelMapper digitalLabelMapper,
                                  DigitalLabelRepository digitalLabelRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
        this.trurnAreaMapper = trurnAreaMapper;
        this.trurnAreaRepository = trurnAreaRepository;
        this.digitalLabelMapper = digitalLabelMapper;
        this.digitalLabelRepository = digitalLabelRepository;
    }

    @Override
    public TurnAreaPositionDTO toDTO(TurnAreaPosition entity) {
        if (entity == null) {
            return null;
        }
        TurnAreaPositionDTO dto = new TurnAreaPositionDTO(entity);
        dto.setName(entity.getName());
        dto.setTurnAreaNodeType(entity.getTrurnAreaNodeType());
        dto.setxPosition(entity.getxPosition());
        dto.setyPosition(entity.getyPosition());
        dto.setAddressCodeId(entity.getAddressCodeId());
//        dto.setTurnArea(trurnAreaMapper.toDTO(entity.getTrurnArea()));

        return dto;
    }


    @Override
    public TurnAreaPosition toEntity(TurnAreaPositionDTO dto) {
        if (dto == null) {
            return null;
        }

        TurnAreaPosition entity = new TurnAreaPosition();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setTrurnAreaNodeType(dto.getTurnAreaNodeType());
        entity.setxPosition(dto.getxPosition());
        entity.setyPosition(dto.getyPosition());
        entity.setAddressCodeId(dto.getAddressCodeId());
        if(dto.getTurnAreaId() != null) {
            entity.setTrurnArea(trurnAreaRepository.retrieve(dto.getTurnAreaId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(TurnAreaPositionDTO dto, TurnAreaPosition entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setTrurnAreaNodeType(dto.getTurnAreaNodeType());
        entity.setxPosition(dto.getxPosition());
        entity.setyPosition(dto.getyPosition());
        entity.setAddressCodeId(dto.getAddressCodeId());
        if(dto.getTurnAreaId() != null) {
            entity.setTrurnArea(trurnAreaRepository.retrieve(dto.getTurnAreaId()));
        }
    }
}

