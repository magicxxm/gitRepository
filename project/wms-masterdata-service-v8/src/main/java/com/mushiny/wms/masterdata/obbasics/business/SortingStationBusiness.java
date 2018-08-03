package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.SortingStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStation;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationType;
import com.mushiny.wms.masterdata.obbasics.domain.SortingStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.SortingStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SortingStationBusiness {

    private final ApplicationContext applicationContext;
    private final SortingStationRepository sortingStationRepository;
    private final SortingStationPositionRepository sortingStationPositionRepository;
    private final SortingStationTypeRepository sortingStationTypeRepository;
    private final SortingStationTypePositionRepository sortingStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SortingStationBusiness(ApplicationContext applicationContext,
                                  SortingStationRepository sortingStationRepository,
                                  SortingStationPositionRepository sortingStationPositionRepository,
                                  SortingStationTypeRepository sortingStationTypeRepository,
                                  SortingStationTypePositionRepository sortingStationTypePositionRepository,
                                  WorkStationRepository workStationRepository,
                                  WorkStationPositionRepository workStationPositionRepository,
                                  UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.sortingStationRepository = sortingStationRepository;
        this.sortingStationPositionRepository = sortingStationPositionRepository;
        this.sortingStationTypeRepository = sortingStationTypeRepository;
        this.sortingStationTypePositionRepository = sortingStationTypePositionRepository;

        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(SortingStationDTO dto) {
        SortingStationType sortingStationType = sortingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<SortingStationTypePosition> sortingStationTypePositions = sortingStationTypePositionRepository.getBySortingStationType(sortingStationType);

        SortingStation station = new SortingStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setSortingStationType(sortingStationTypeRepository.retrieve(dto.getTypeId()));
        if (dto.getOperatorId() != null) {
            station.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperator(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = sortingStationRepository.save(station);
        for (SortingStationTypePosition StationTypePosition : sortingStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            SortingStationPosition sortingStationPosition = new SortingStationPosition();
            sortingStationPosition.setWorkStationPosition(workStationPosition);
            sortingStationPosition.setPositionState(positionState);
            sortingStationPosition.setSortingStation(station);
            sortingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            sortingStationPositionRepository.save(sortingStationPosition);
        }
    }

    public SortingStation updateMore(SortingStationDTO dto) {
        SortingStation entity = sortingStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getTypeId() != null) {
            entity.setSortingStationType(sortingStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setSortingStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
        List<SortingStationPosition> SortingStationPositionList = sortingStationPositionRepository.getBySortingStationId(dto.getId());
        for (SortingStationPosition sortingStationPosition:SortingStationPositionList){
            sortingStationPositionRepository.delete(sortingStationPosition);
        }
        SortingStationType sortingStationType = sortingStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<SortingStationTypePosition> sortingStationTypePositions = sortingStationTypePositionRepository.getBySortingStationType(sortingStationType);

        for (SortingStationTypePosition StationTypePosition1 : sortingStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            SortingStationPosition sortingStationPosition = new SortingStationPosition();
            sortingStationPosition.setWorkStationPosition(workStationPosition);
            sortingStationPosition.setPositionState(positionState);
            sortingStationPosition.setSortingStation(entity);
            sortingStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            sortingStationPositionRepository.save(sortingStationPosition);
        }
        return sortingStationRepository.save(entity);
    }
}
