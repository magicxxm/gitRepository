package com.mushiny.wms.masterdata.ibbasics.business;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.dto.ReceiveStationDTO;
import com.mushiny.wms.masterdata.ibbasics.domain.*;
import com.mushiny.wms.masterdata.ibbasics.repository.*;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationPositionRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.ibbasics.exception.InBoundException;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReceiveStationBusiness {

    private final ApplicationContext applicationContext;

    private final ReceiveStationRepository receiveStationRepository;
    private final ReceiveStationPositionRepository receiveStationPositionRepository;
    private final ReceiveStationTypeRepository receiveStationTypeRepository;
    private final ReceiveStationTypePositionRepository receiveStationTypePositionRepository;
    private final WorkStationRepository workStationRepository;
    private final WorkStationPositionRepository workStationPositionRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReceiveStationBusiness(ApplicationContext applicationContext,
                                  ReceiveStationRepository receiveStationRepository,
                                  ReceiveStationPositionRepository receiveStationPositionRepository,
                                  ReceiveStationTypeRepository receiveStationTypeRepository,
                                  ReceiveStationTypePositionRepository receiveStationTypePositionRepository,
                                  WorkStationRepository workStationRepository,
                                  WorkStationPositionRepository workStationPositionRepository,
                                  UserRepository userRepository) {
        this.applicationContext = applicationContext;
        this.receiveStationRepository = receiveStationRepository;
        this.receiveStationPositionRepository = receiveStationPositionRepository;
        this.receiveStationTypeRepository = receiveStationTypeRepository;
        this.receiveStationTypePositionRepository = receiveStationTypePositionRepository;
        this.workStationRepository = workStationRepository;
        this.workStationPositionRepository = workStationPositionRepository;
        this.userRepository = userRepository;
    }

    public void createMore(ReceiveStationDTO dto) {
        ReceiveStationType receiveStationType = receiveStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StowStationType下所有StowStationTypePosition
        List<ReceiveStationTypePosition> receiveStationTypePositions = receiveStationTypePositionRepository.getByStowStationType(receiveStationType);

        ReceiveStation station = new ReceiveStation();
        station.setName(dto.getName());
        station.setDescription(dto.getDescription());
        station.setReceivingStationType(receiveStationTypeRepository.retrieve(dto.getTypeId()));
        station.setOperator(dto.getOperatorId());
        station.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        station.setWarehouseId(applicationContext.getCurrentWarehouse());
        station = receiveStationRepository.save(station);
        for (ReceiveStationTypePosition StationTypePosition : receiveStationTypePositions) {
            String positionState = StationTypePosition.getPositionState();
            int positionIndex = StationTypePosition.getPositionIndex();

            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(InBoundException.EX_MD_IN_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            ReceiveStationPosition receiveStationPositionEntity = new ReceiveStationPosition();
            receiveStationPositionEntity.setWorkStationPosition(workStationPosition);
            receiveStationPositionEntity.setPositionState(positionState);
            receiveStationPositionEntity.setReceiveStation(station);
            receiveStationPositionEntity.setWarehouseId(applicationContext.getCurrentWarehouse());
            receiveStationPositionRepository.save(receiveStationPositionEntity);
        }

    }

    public ReceiveStation updateMore(ReceiveStationDTO dto) {
        ReceiveStation entity = receiveStationRepository.retrieve(dto.getId());
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getWorkstationId() != null) {
            entity.setWorkStation(workStationRepository.retrieve(dto.getWorkstationId()));
        }
        if (dto.getTypeId() != null) {
            entity.setReceivingStationType(receiveStationTypeRepository.retrieve(dto.getTypeId()));
        }
        ReceiveStationType receiveStationType = receiveStationTypeRepository.retrieve(dto.getTypeId());
        // 取出StowStationType下所有StowStationTypePosition
        List<ReceiveStationTypePosition> receiveStationTypePositions = receiveStationTypePositionRepository.getByStowStationType(receiveStationType);
        for (ReceiveStationTypePosition StationTypePosition1 : receiveStationTypePositions) {
            String positionState = StationTypePosition1.getPositionState();
            int positionIndex = StationTypePosition1.getPositionIndex();
            //用workStationId和PositionIndex取出从表的唯一Id
            WorkStation workStation = workStationRepository.retrieve(dto.getWorkstationId());
            WorkStationPosition workStationPosition = workStationPositionRepository.getByWorkStationIdAndPositionIndex(workStation, positionIndex);
            if (workStationPosition == null) {
                throw new ApiException(InBoundException.EX_MD_IN_WORKSTATION_NOT_FOUND.toString(), positionIndex);
            }
            List<ReceiveStationPosition> ReceiveStationPositionList=receiveStationPositionRepository.getByReceiveStationId(entity.getId());
            for (ReceiveStationPosition receiveStationPosition:ReceiveStationPositionList){
                receiveStationPositionRepository.delete(receiveStationPosition);//删除对应的position
            }
            ReceiveStationPosition receiveStationPosition = new ReceiveStationPosition();
            receiveStationPosition.setWorkStationPosition(workStationPosition);
            receiveStationPosition.setPositionState(positionState);
            receiveStationPosition.setReceiveStation(entity);
            receiveStationPosition.setWarehouseId(applicationContext.getCurrentWarehouse());
            receiveStationPositionRepository.save(receiveStationPosition);
        }
        return receiveStationRepository.save(entity);
    }

}
