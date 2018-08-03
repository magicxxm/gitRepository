package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PackingStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PackingStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PackingStationBusiness {

    private final ApplicationContext applicationContext;
    private final PackingStationRepository packingStationRepository;
    private final PackingStationPositionRepository packingStationPositionRepository;
    private final PackingStationTypeRepository packingStationTypeRepository;
    private final PackingStationTypePositionRepository packingStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public PackingStationBusiness(ApplicationContext applicationContext,
                                  PackingStationRepository packingStationRepository,
                                  PackingStationPositionRepository packingStationPositionRepository,
                                  PackingStationTypeRepository packingStationTypeRepository,
                                  PackingStationTypePositionRepository packingStationTypePositionRepository,
                                  WorkStationRepository workStationRepository,
                                  WorkStationPositionRepository workStationPositionRepository,
                                  UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.packingStationRepository = packingStationRepository;
        this.packingStationPositionRepository = packingStationPositionRepository;
        this.packingStationTypeRepository = packingStationTypeRepository;
        this.packingStationTypePositionRepository = packingStationTypePositionRepository;
        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(PackingStationDTO dto) {
        PackingStationType packingStationType = packingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<PackingStationTypePosition> packingStationTypePositions = packingStationTypePositionRepository.getByStowStationType(packingStationType);

        PackingStation station = new PackingStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setPackingStationType(packingStationTypeRepository.retrieve(dto.getTypeId()));
        if (dto.getOperatorId() != null) {
            station.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperator(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = packingStationRepository.save(station);
        for(PackingStationTypePosition StationTypePosition : packingStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            PackingStationPosition packingStationPosition = new PackingStationPosition();
            packingStationPosition.setWorkStationPosition(workStationPosition);
            packingStationPosition.setPositionState(positionState);
            packingStationPosition.setPackingStation(station);
            packingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            packingStationPositionRepository.save(packingStationPosition);
        }
    }
    public PackingStation upDateMore(PackingStationDTO dto) {
        PackingStation entity=packingStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setPackingStationType(packingStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setPackingStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
        List<PackingStationPosition> packingStationPositionList=packingStationPositionRepository.getByPackingStationId(dto.getId());
        for (PackingStationPosition packingStationPosition:packingStationPositionList){
            packingStationPositionRepository.delete(packingStationPosition);
        }
        PackingStationType packingStationType = packingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<PackingStationTypePosition> packingStationTypePositions = packingStationTypePositionRepository.getByStowStationType(packingStationType);

        for(PackingStationTypePosition StationTypePosition1 : packingStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation,positionIndex);
            if(workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            PackingStationPosition packingStationPosition = new PackingStationPosition();
            packingStationPosition.setWorkStationPosition(workStationPosition);
            packingStationPosition.setPositionState(positionState);
            packingStationPosition.setPackingStation(entity);
            packingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            packingStationPositionRepository.save(packingStationPosition);
        }
        return packingStationRepository.save(entity);
    }
}
