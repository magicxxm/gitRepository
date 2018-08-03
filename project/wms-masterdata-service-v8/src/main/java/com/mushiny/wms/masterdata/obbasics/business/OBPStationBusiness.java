package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.OBPStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStation;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationType;
import com.mushiny.wms.masterdata.obbasics.domain.OBPStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.OBPStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OBPStationBusiness {

    private final ApplicationContext applicationContext;
    private final OBPStationRepository obpStationRepository;
    private final OBPStationPositionRepository obpStationPositionRepository;
    private final OBPStationTypeRepository obpStationTypeRepository;
    private final OBPStationTypePositionRepository obpStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public OBPStationBusiness(ApplicationContext applicationContext,
                              OBPStationRepository obpStationRepository,
                              OBPStationPositionRepository obpStationPositionRepository,
                              OBPStationTypeRepository obpStationTypeRepository,
                              OBPStationTypePositionRepository obpStationTypePositionRepository,
                              WorkStationRepository workStationRepository,
                              WorkStationPositionRepository workStationPositionRepository,
                              UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.obpStationRepository = obpStationRepository;
        this.obpStationPositionRepository = obpStationPositionRepository;
        this.obpStationTypeRepository = obpStationTypeRepository;
        this.obpStationTypePositionRepository = obpStationTypePositionRepository;

        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(OBPStationDTO dto) {
        OBPStationType obpStationType = obpStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<OBPStationTypePosition> obpStationTypePositions = obpStationTypePositionRepository.getByOBPStationType(obpStationType);

        OBPStation station = new OBPStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        if(dto.getOperatorId() != null) {
            station.setOperatorId(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperatorId(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = obpStationRepository.save(station);
        for(OBPStationTypePosition StationTypePosition : obpStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            OBPStationPosition stowStationPosition = new OBPStationPosition();
            stowStationPosition.setWorkStationPosition(workStationPosition);
            stowStationPosition.setPositionState(positionState);
            stowStationPosition.setObpStation(station);
            stowStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            obpStationPositionRepository.save(stowStationPosition);
        }
    }
    public OBPStation updateMore(OBPStationDTO dto) {
        OBPStation entity=obpStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setState(dto.getState());
        if (dto.getTypeId() != null) {
            entity.setObpStationType(obpStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setObpStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
       List<OBPStationPosition> OBPStationPositionList=obpStationPositionRepository. getByOBPStationId(dto.getId());
        for (OBPStationPosition oBPStationPosition:OBPStationPositionList){
            obpStationPositionRepository.delete(oBPStationPosition);
        }
        OBPStationType obpStationType = obpStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<OBPStationTypePosition> obpStationTypePositions = obpStationTypePositionRepository.getByOBPStationType(obpStationType);


        for(OBPStationTypePosition StationTypePosition1 : obpStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            OBPStationPosition stowStationPosition = new OBPStationPosition();
            stowStationPosition.setWorkStationPosition(workStationPosition);
            stowStationPosition.setPositionState(positionState);
            stowStationPosition.setObpStation(entity);
            stowStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            obpStationPositionRepository.save(stowStationPosition);
        }
        return obpStationRepository.save(entity);
    }
}
