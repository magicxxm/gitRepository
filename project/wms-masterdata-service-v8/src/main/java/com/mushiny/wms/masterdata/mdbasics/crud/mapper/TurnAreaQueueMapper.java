package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.TurnAreaQueueDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.TurnAreaQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TurnAreaQueueMapper implements BaseMapper<TurnAreaQueueDTO, TurnAreaQueue> {

    private final ApplicationContext applicationContext;

    @Autowired
    public TurnAreaQueueMapper(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public TurnAreaQueueDTO toDTO(TurnAreaQueue entity) {
        if (entity == null) {
            return null;
        }

        TurnAreaQueueDTO dto = new TurnAreaQueueDTO(entity);

        dto.setTurnAreaPositionId(entity.getTurnAreaPosition());
        dto.setRobotId(entity.getRobot());

        return dto;
    }

    @Override
    public TurnAreaQueue toEntity(TurnAreaQueueDTO dto) {
        if (dto == null) {
            return null;
        }

        TurnAreaQueue entity = new TurnAreaQueue();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setTurnAreaPosition(dto.getTurnAreaPositionId());
        entity.setRobot(dto.getRobotId());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(TurnAreaQueueDTO dto, TurnAreaQueue entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setTurnAreaPosition(dto.getTurnAreaPositionId());
        entity.setRobot(dto.getRobotId());
    }
}

