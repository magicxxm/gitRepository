package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.ReceiveThresholdMapper;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveThresholdRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.HardWareDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.HardWareMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.HardWare;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.repository.HardWareRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.mdbasics.service.HardWareWorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class HardWareWorkStationServiceImpl implements HardWareWorkStationService {

    private final UserRepository userRepository;
    private final ReceiveThresholdMapper receiveThresholdMapper;
    private final UserMapper userMapper;
    private final ApplicationContext applicationContext;
    private final WorkStationMapper workStationMapper;
    private final WorkStationRepository workStationRepository;
    private final HardWareMapper hardWareMapper;
    private final HardWareRepository hardWareRepository;
    private final ReceiveThresholdRepository receiveThresholdRepository;

    @Autowired
    public HardWareWorkStationServiceImpl(UserRepository userRepository,
                                          ReceiveThresholdMapper receiveThresholdMapper,
                                          UserMapper userMapper,
                                          ApplicationContext applicationContext, WorkStationMapper workStationMapper,
                                          WorkStationRepository workStationRepository,
                                          HardWareMapper hardWareMapper,
                                          HardWareRepository hardWareRepository,
                                          ReceiveThresholdRepository receiveThresholdRepository) {
        this.userRepository = userRepository;
        this.receiveThresholdMapper = receiveThresholdMapper;
        this.userMapper = userMapper;
        this.applicationContext = applicationContext;
        this.workStationMapper = workStationMapper;
        this.workStationRepository = workStationRepository;
        this.hardWareMapper = hardWareMapper;
        this.hardWareRepository = hardWareRepository;
        this.receiveThresholdRepository = receiveThresholdRepository;
    }

    @Override
    public void createWorkStationHardWares(String workStationId, List<String> hardwares) {
        WorkStation workStation = workStationRepository.retrieve(workStationId);
        if (hardwares == null || hardwares.isEmpty()) {
            workStation.setHardWares(null);
        } else {
            Set<HardWare> hardWares = new HashSet<>();
            for (String sholdId : hardwares) {
                HardWare hardWare = hardWareRepository.retrieve(sholdId);
                hardWares.add(hardWare);
            }
            workStation.setHardWares(hardWares);
        }
        workStationRepository.save(workStation);
    }

    @Override
    public List<WorkStationDTO> getWorkStationList() {
        List<WorkStation> entities = workStationRepository.findByEntityLock(Constant.NOT_LOCKED,applicationContext.getCurrentWarehouse());
        return workStationMapper.toDTOList(entities);
    }

    @Override
    public List<HardWareDTO> getAssignedByWorkStation(String userId) {
        WorkStation workStation = workStationRepository.retrieve(userId);
        List<HardWare> entities = new ArrayList<>();
        entities.addAll(workStation.getHardWares());
        return hardWareMapper.toDTOList(entities);
    }

    @Override
    public List<HardWareDTO> getUnassignedByWorkStation(String userId) {
        List<HardWare> entities = hardWareRepository.getUnassignedClientWarehouses(
                userId, Constant.NOT_LOCKED);
        return hardWareMapper.toDTOList(entities);
    }
}