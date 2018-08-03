package com.mushiny.wms.outboundproblem.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.outboundproblem.business.OBPStationBusiness;
import com.mushiny.wms.outboundproblem.crud.common.dto.ScanningOBPStationDTO;
import com.mushiny.wms.outboundproblem.crud.dto.OBPWallDTO;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPStationMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPStationTypePositionMapper;
import com.mushiny.wms.outboundproblem.crud.mapper.OBPWallMapper;
import com.mushiny.wms.outboundproblem.domain.*;
import com.mushiny.wms.outboundproblem.domain.enums.OBPWallState;
import com.mushiny.wms.outboundproblem.exception.OutBoundProblemException;
import com.mushiny.wms.outboundproblem.repository.*;
import com.mushiny.wms.outboundproblem.repository.common.UserRepository;
import com.mushiny.wms.outboundproblem.service.OBPVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OBPVerificationServiceImpl implements OBPVerificationService {

    private final OBPStationBusiness obpStationBusiness;
    private final OBPStationTypePositionRepository obpStationTypePositionRepository;
    private final OBPStationMapper obpStationMapper;
    private final OBPStationTypePositionMapper obpStationTypePositionMapper;
    private final ApplicationContext applicationContext;
    private final OBPWallMapper obpWallMapper;
    private final OBPWallRepository obpWallRepository;
    private final UserRepository userRepository;
    private final OBPStationRepository obpStationRepository;

    @Autowired
    public OBPVerificationServiceImpl(OBPStationBusiness obpStationBusiness,
                                      OBPStationTypePositionRepository obpStationTypePositionRepository,
                                      OBPStationMapper obpStationMapper,
                                      OBPStationTypePositionMapper obpStationTypePositionMapper,
                                      ApplicationContext applicationContext,
                                      OBPWallMapper obpWallMapper,
                                      OBPWallRepository obpWallRepository,
                                      UserRepository userRepository,
                                      OBPStationRepository obpStationRepository) {
        this.obpStationBusiness = obpStationBusiness;
        this.obpStationTypePositionRepository = obpStationTypePositionRepository;
        this.obpStationMapper = obpStationMapper;
        this.obpStationTypePositionMapper = obpStationTypePositionMapper;
        this.applicationContext = applicationContext;
        this.obpWallMapper = obpWallMapper;
        this.obpWallRepository = obpWallRepository;
        this.userRepository = userRepository;
        this.obpStationRepository = obpStationRepository;
    }


    @Override
    public ScanningOBPStationDTO scanOBPStation(String name) {
        OBPStation obpStation = obpStationBusiness.getOBPStation(name);
        // 设置工作站可以绑定小车的最大数量
//        long maxProcessContainer = obpStationTypeRepository.countByOBPStationType(
//                obpStation.getObpStationType());
        ScanningOBPStationDTO dto = new ScanningOBPStationDTO();
        List<OBPStationTypePosition> positions = obpStationTypePositionRepository
                .getPositions(obpStation.getObpStationType());
        dto.setObpStation(obpStationMapper.toDTO(obpStation));
        dto.setObpStationTypePositions(obpStationTypePositionMapper.toDTOList(positions));
//        dto.setMaxAmount(maxProcessContainer);
        return dto;
    }

    @Override
    public OBPWallDTO scanOBPWall(String name) {
        String warehouseId = applicationContext.getCurrentWarehouse();
        OBPWall obpWall = Optional
                .ofNullable(obpWallRepository.getByName(warehouseId, name))
                .orElseThrow(() -> new ApiException(OutBoundProblemException.EX_SCANNING_OBJECT_NOT_FOUND.getName(), name));
        // 判断问题处理车的状态是否被占用（Occupied状态）
        if (obpWall.getState().equalsIgnoreCase(OBPWallState.occupied.toString())) {
            throw new ApiException(OutBoundProblemException
                    .EX_OBPWALL_IS_NOT_OCCUPIED_STATE.getName(), obpWall.getName());
        }
        return obpWallMapper.toDTO(obpWall);
    }
}
