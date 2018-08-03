package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.repository.MapRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.NodeRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.SectionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackWallMapper;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackWallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkStationMapper implements BaseMapper<WorkStationDTO, WorkStation> {

    private final ApplicationContext applicationContext;
    private final WorkStationTypeMapper workStationTypeMapper;
    private final WorkStationTypeRepository workStationTypeRepository;
    private final PickPackWallMapper pickPackWallMapper;
    private final PickPackWallRepository pickPackWallRepository;
    private final SectionMapper sectionMapper;
    private final SectionRepository sectionRepository;
    private final MapMapper mapMapper;
    private final MapRepository mapRepository;
    private final UserRepository userRepository;
    private final NodeRepository nodeRepository;


    @Autowired
    public WorkStationMapper(ApplicationContext applicationContext,
                             WorkStationTypeMapper workStationTypeMapper,
                             WorkStationTypeRepository workStationTypeRepository,
                             PickPackWallMapper pickPackWallMapper,
                             PickPackWallRepository pickPackWallRepository,
                             SectionMapper sectionMapper,
                             SectionRepository sectionRepository,
                             MapMapper mapMapper,
                             MapRepository mapRepository,
                             UserRepository userRepository, NodeRepository nodeRepository) {
        this.applicationContext = applicationContext;
        this.workStationTypeMapper = workStationTypeMapper;
        this.workStationTypeRepository = workStationTypeRepository;
        this.pickPackWallMapper = pickPackWallMapper;
        this.pickPackWallRepository = pickPackWallRepository;
        this.sectionMapper = sectionMapper;
        this.sectionRepository = sectionRepository;
        this.mapMapper = mapMapper;
        this.mapRepository = mapRepository;
        this.userRepository = userRepository;
        this.nodeRepository = nodeRepository;
    }

    @Override
    public WorkStationDTO toDTO(WorkStation entity) {
        if (entity == null) {
            return null;
        }

        WorkStationDTO dto = new WorkStationDTO(entity);
        dto.setName(entity.getName());
        dto.setXpos(entity.getXpos());
        dto.setYpos(entity.getYpos());
        dto.setWorkstationType(workStationTypeMapper.toDTO(entity.getType()));
        dto.setFixedScanner(entity.getFixedScanner());
        dto.setPickPackWall(pickPackWallMapper.toDTO(entity.getPickPackWall()));
        dto.setWorkingFaceOrientation(entity.getWorkingFaceOrientation());
        dto.setPlaceMark(entity.getPlaceMark());
        dto.setSection(sectionMapper.toDTO(entity.getSection()));
        dto.setStopPoint(entity.getStopPoint());
        dto.setScanPoint(entity.getScanPoint());
        dto.setBufferPoint(entity.getBufferPoint());
        dto.setCallPod(entity.isCallPod());
        dto.setPickOrPack(entity.getPickOrPack());
        if (entity.getOperator() != null) {
            dto.setUseName(userRepository.findUseName(entity.getOperator()));
        } else {
            dto.setUseName("");
        }
        dto.setStationName(entity.getStationName());
        dto.setWarehouseId(entity.getWarehouseId());
        return dto;
    }

    @Override
    public WorkStation toEntity(WorkStationDTO dto) {
        if (dto == null) {
            return null;
        }

        WorkStation entity = new WorkStation();

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setType(workStationTypeRepository.retrieve(dto.getTypeId()));
        if (dto.getPickPackWallId().length() > 0) {
            entity.setPickPackWall(pickPackWallRepository.retrieve(dto.getPickPackWallId()));
            entity.setPickOrPack(dto.getPickOrPack());
        } else {
            entity.setPickPackWall(null);
            entity.setPickOrPack(null);
        }
        entity.setFixedScanner(dto.getFixedScanner());
        entity.setWorkingFaceOrientation(dto.getWorkingFaceOrientation());
        entity.setPlaceMark(dto.getPlaceMark());
        if (dto.getSectionId() != null) {
            entity.setSection(sectionRepository.retrieve(dto.getSectionId()));
        } else {
            entity.setSection(null);
        }
        entity.setStopPoint(dto.getStopPoint());
        entity.setScanPoint(dto.getScanPoint());
        entity.setBufferPoint(dto.getBufferPoint());
        entity.setWarehouseId(applicationContext.getCurrentWarehouse());

        if (dto.getPlaceMark() != 0) {
            List<Map> mapList = mapRepository.getMapBySectionId(applicationContext.getCurrentWarehouse(), dto.getSectionId(), true);
            Node node = nodeRepository.getAddressCodeId(applicationContext.getCurrentWarehouse(), dto.getPlaceMark(), mapList.get(0).getId());
            entity.setXpos(node.getxPosition());
            entity.setYpos(node.getyPosition());
        } else {
            entity.setXpos(0);
            entity.setYpos(0);
        }
        entity.setisCallPod(dto.isCallPod());
        return entity;
    }

    @Override
    public void updateEntityFromDTO(WorkStationDTO dto, WorkStation entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setisCallPod(dto.isCallPod());
//        entity.setType(workStationTypeRepository.retrieve(dto.getTypeId()));
        //设置了pickPackWall的时候再设置是捡货还是包装
        if (dto.getPickPackWallId().length() > 0) {
            entity.setPickPackWall(pickPackWallRepository.retrieve(dto.getPickPackWallId()));
            entity.setPickOrPack(dto.getPickOrPack());
        } else {
            entity.setPickPackWall(null);
            entity.setPickOrPack(null);
        }
//        entity.setFixedScanner(dto.getFixedScanner());
        entity.setWorkingFaceOrientation(dto.getWorkingFaceOrientation());
        entity.setPlaceMark(dto.getPlaceMark());
        entity.setStopPoint(dto.getStopPoint());
        entity.setScanPoint(dto.getScanPoint());
        entity.setBufferPoint(dto.getBufferPoint());
        if (dto.getSectionId() != null) {
            entity.setSection(sectionRepository.retrieve(dto.getSectionId()));
        } else {
            entity.setSection(null);
        }
        if (dto.getPlaceMark() != 0) {
            List<Map> mapList = mapRepository.getMapBySectionId(applicationContext.getCurrentWarehouse(), dto.getSectionId(), true);
            Node node1 = nodeRepository.getAddressCodeId(applicationContext.getCurrentWarehouse(), dto.getPlaceMark(), mapList.get(0).getId());
            entity.setXpos(node1.getxPosition());
            entity.setYpos(node1.getyPosition());
        } else {
            entity.setXpos(0);
            entity.setYpos(0);
        }

        entity.setWarehouseId(applicationContext.getCurrentWarehouse());
    }
}

