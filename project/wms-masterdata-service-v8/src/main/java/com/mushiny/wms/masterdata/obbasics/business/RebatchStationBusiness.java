package com.mushiny.wms.masterdata.obbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.RebatchStationDTO;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStation;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationPosition;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationType;
import com.mushiny.wms.masterdata.obbasics.domain.RebatchStationTypePosition;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationPositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationRepository;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationTypePositionRepository;
import com.mushiny.wms.masterdata.obbasics.repository.RebatchStationTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RebatchStationBusiness {

    private final ApplicationContext applicationContext;
    private final RebatchStationRepository rebatchStationRepository;
    private final RebatchStationPositionRepository rebatchStationPositionRepository;
    private final RebatchStationTypeRepository rebatchStationTypeRepository;
    private final RebatchStationTypePositionRepository rebatchStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public RebatchStationBusiness(ApplicationContext applicationContext,
                                  RebatchStationRepository rebatchStationRepository,
                                  RebatchStationPositionRepository rebatchStationPositionRepository,
                                  RebatchStationTypeRepository rebatchStationTypeRepository,
                                  RebatchStationTypePositionRepository rebatchStationTypePositionRepository,
                                  WorkStationRepository workStationRepository,
                                  WorkStationPositionRepository workStationPositionRepository,
                                  UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.rebatchStationRepository = rebatchStationRepository;
        this.rebatchStationPositionRepository = rebatchStationPositionRepository;
        this.rebatchStationTypeRepository = rebatchStationTypeRepository;
        this.rebatchStationTypePositionRepository = rebatchStationTypePositionRepository;

        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(RebatchStationDTO dto) {
        RebatchStationType rebatchStationType = rebatchStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<RebatchStationTypePosition> rebatchStationTypePositions = rebatchStationTypePositionRepository.getByRebatchStationType(rebatchStationType);

        RebatchStation station = new RebatchStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setRebatchStationType(rebatchStationTypeRepository.retrieve(dto.getTypeId()));
        if (dto.getOperatorId() != null) {
            station.setOperator(userRepository.retrieve(dto.getOperatorId()));
        } else {
            station.setOperator(null);
        }
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = rebatchStationRepository.save(station);
        for (RebatchStationTypePosition StationTypePosition : rebatchStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            RebatchStationPosition rebatchStationPosition = new RebatchStationPosition();
            rebatchStationPosition.setWorkStationPosition(workStationPosition);
            rebatchStationPosition.setPositionState(positionState);
            rebatchStationPosition.setRebatchStation(station);
            rebatchStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            rebatchStationPositionRepository.save(rebatchStationPosition);
        }
    }

    public RebatchStation updateMore(RebatchStationDTO dto) {
        RebatchStation entity = rebatchStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());

        if (dto.getTypeId() != null) {
            entity.setRebatchStationType(rebatchStationTypeRepository.retrieve(dto.getTypeId()));
        } else {
            entity.setRebatchStationType(null);
        }
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        } else {
            entity.setWorkStation(null);
        }
        List<RebatchStationPosition> rebatchStationPositionList = rebatchStationPositionRepository.getByRebatchStationId(dto.getId());
       for (RebatchStationPosition rebatchStationPosition:rebatchStationPositionList){
           rebatchStationPositionRepository.delete(rebatchStationPosition);
       }
        RebatchStationType rebatchStationType = rebatchStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StationType下所有StationTypePosition
        List<RebatchStationTypePosition> rebatchStationTypePositions = rebatchStationTypePositionRepository.getByRebatchStationType(rebatchStationType);


        for (RebatchStationTypePosition StationTypePosition1 : rebatchStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(OutBoundException.EX_MD_OB_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            RebatchStationPosition rebatchStationPosition = new RebatchStationPosition();
            rebatchStationPosition.setWorkStationPosition(workStationPosition);
            rebatchStationPosition.setPositionState(positionState);
            rebatchStationPosition.setRebatchStation(entity);
            rebatchStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            rebatchStationPositionRepository.save(rebatchStationPosition);
        }
        return rebatchStationRepository.save(entity);
    }
}
