package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.PickStation;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationType;
import com.mushiny.wms.masterdata.obbasics.domain.PickStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PickStationBusiness {

    private final ApplicationContext applicationContext;
    private final PickStationRepository pickStationRepository;
    private final PickStationPositionRepository pickStationPositionRepository;
    private final PickStationTypeRepository pickStationTypeRepository;
    private final PickStationTypePositionRepository pickStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PickStationBusiness(ApplicationContext applicationContext,
                               PickStationRepository pickStationRepository,
                               PickStationPositionRepository pickStationPositionRepository,
                               PickStationTypeRepository pickStationTypeRepository,
                               PickStationTypePositionRepository pickStationTypePositionRepository,
                               WorkStationRepository workStationRepository,
                               WorkStationPositionRepository workStationPositionRepository,
                               UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.pickStationRepository = pickStationRepository;
        this.pickStationPositionRepository = pickStationPositionRepository;
        this.pickStationTypeRepository = pickStationTypeRepository;
        this.pickStationTypePositionRepository = pickStationTypePositionRepository;

        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(PickStationDTO dto) {
        PickStationType pickStationType = pickStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<PickStationTypePosition> pickStationTypePositions = pickStationTypePositionRepository.getByPickStationType(pickStationType);

        PickStation station = new PickStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setPickStationType(pickStationTypeRepository.retrieve(dto.getTypeId()));
        if(dto.getOperatorId() != null) {
            station.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperator(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = pickStationRepository.save(station);
        for(PickStationTypePosition StationTypePosition : pickStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            PickStationPosition pickStationPosition = new PickStationPosition();
            pickStationPosition.setWorkStationPosition(workStationPosition);
            pickStationPosition.setPositionState(positionState);
            pickStationPosition.setPickStation(station);
            pickStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            pickStationPositionRepository.save(pickStationPosition);
        }
    }
    public PickStation updateMore(PickStationDTO dto) {
        PickStation entity=pickStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setPickStationType(pickStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setPickStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
        List<PickStationPosition>  pickStationPositionList=pickStationPositionRepository.getByPickStationId(dto.getId());
        for (PickStationPosition pickStationPosition:pickStationPositionList){
            pickStationPositionRepository.delete(pickStationPosition);
        }
        PickStationType pickStationType = pickStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<PickStationTypePosition> pickStationTypePositions = pickStationTypePositionRepository.getByPickStationType(pickStationType);
        for(PickStationTypePosition StationTypePosition1 : pickStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            PickStationPosition pickStationPosition = new PickStationPosition();
            pickStationPosition.setWorkStationPosition(workStationPosition);
            pickStationPosition.setPositionState(positionState);
            pickStationPosition.setPickStation(entity);
            pickStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            pickStationPositionRepository.save(pickStationPosition);
        }
        return pickStationRepository.save(entity);
    }
}
