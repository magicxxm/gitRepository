package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.RobotDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Robot;
import com.mushiny.wms.masterdata.mdbasics.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RobotMapper implements BaseMapper<RobotDTO, Robot> {

    private final RobotTypeRepository robotTypeRepository;
    private final RobotTypeMapper robotTypeMapper;
    private final ApplicationContext applicationContext;

    @Autowired
    public RobotMapper(RobotTypeRepository robotTypeRepository,
                       RobotTypeMapper robotTypeMapper,
                       ApplicationContext applicationContext) {
        this.robotTypeRepository = robotTypeRepository;
        this.robotTypeMapper = robotTypeMapper;
        this.applicationContext = applicationContext;
    }

    @Override
    public RobotDTO toDTO(Robot entity) {
        if (entity == null) {
            return null;
        }

        RobotDTO dto = new RobotDTO(entity);

        dto.setRobot(entity.getRobot());
        dto.setPassword(entity.getPassword());
        dto.setRobotType(robotTypeMapper.toDTO(entity.getRobotType()));
        dto.setHardware(entity.getHardware());
        dto.setSoftware(entity.getSoftware());
        dto.setPriduction(entity.getPriduction());
        dto.setAcc(entity.getAcc());
        dto.setRecently(entity.getRecently());
        dto.setInbreak(entity.getInbreak());
        dto.setCold(entity.getCold());
        dto.setHot(entity.getHot());
        dto.setWarehouseId(entity.getWarehouseId());
        dto.setBatteryNumber(entity.getBatteryNumber());

        return dto;
    }

    @Override
    public Robot toEntity(RobotDTO dto) {
        if (dto == null) {
            return null;
        }

        Robot entity = new Robot();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setRobot(dto.getRobot());
        entity.setPassword(dto.getPassword());
        entity.setHardware(dto.getHardware());
        entity.setSoftware(dto.getSoftware());
        entity.setPriduction(dto.getPriduction());
        entity.setAcc(dto.getAcc());
        entity.setRecently(dto.getRecently());
        entity.setInbreak(dto.getInbreak());
        entity.setCold(dto.getCold());
        entity.setHot(dto.getHot());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
        entity.setBatteryNumber(dto.getBatteryNumber());
        if (dto.getRobotTypeId() != null) {
            entity.setRobotType(robotTypeRepository.retrieve(dto.getRobotTypeId()));
        }
        return entity;
    }

    @Override
    public void updateEntityFromDTO(RobotDTO dto, Robot entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setRobot(dto.getRobot());
//        entity.setPassword(dto.getPassword());
        entity.setHardware(dto.getHardware());
        entity.setSoftware(dto.getSoftware());
        entity.setPriduction(dto.getPriduction());
        entity.setAcc(dto.getAcc());
        entity.setRecently(dto.getRecently());
        entity.setInbreak(dto.getInbreak());
        entity.setCold(dto.getCold());
        entity.setHot(dto.getHot());
        if (dto.getRobotTypeId() != null) {
            entity.setRobotType(robotTypeRepository.retrieve(dto.getRobotTypeId()));
        }
    }
}

