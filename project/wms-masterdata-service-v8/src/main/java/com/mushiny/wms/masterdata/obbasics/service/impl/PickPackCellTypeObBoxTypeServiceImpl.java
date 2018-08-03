package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.masterdata.general.crud.mapper.UserMapper;
import com.mushiny.wms.masterdata.general.repository.UserRepository;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.obbasics.crud.dto.BoxTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.dto.ReBinCellTypeDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.BoxTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.ReBinCellTypeMapper;
import com.mushiny.wms.masterdata.obbasics.domain.BoxType;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCell;
import com.mushiny.wms.masterdata.obbasics.domain.PickPackCellType;
import com.mushiny.wms.masterdata.obbasics.domain.ReBinCellType;
import com.mushiny.wms.masterdata.obbasics.repository.BoxTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.PickPackCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.repository.ReBinCellTypeRepository;
import com.mushiny.wms.masterdata.obbasics.service.PickPackCellTypeObBoxTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PickPackCellTypeObBoxTypeServiceImpl implements PickPackCellTypeObBoxTypeService {

    private final UserRepository userRepository;
    private final PickPackCellTypeMapper pickPackCellTypeMapper;
    private final PickPackCellTypeRepository pickPackCellTypeRepository;
    private final BoxTypeRepository boxTypeRepository;
    private final BoxTypeMapper boxTypeMapper;
    private final UserMapper userMapper;

    @Autowired
    public PickPackCellTypeObBoxTypeServiceImpl(UserRepository userRepository,
                                                PickPackCellTypeMapper pickPackCellTypeMapper,
                                                PickPackCellTypeRepository pickPackCellTypeRepository,
                                                BoxTypeRepository boxTypeRepository,
                                                BoxTypeMapper boxTypeMapper,
                                                UserMapper userMapper) {
        this.userRepository = userRepository;
        this.pickPackCellTypeMapper = pickPackCellTypeMapper;
        this.pickPackCellTypeRepository = pickPackCellTypeRepository;
        this.boxTypeRepository = boxTypeRepository;
        this.boxTypeMapper = boxTypeMapper;
        this.userMapper = userMapper;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public void createPickPackCellTypeObBoxType(String pickPackCellTypeId, List<String> cellTypes) {
        PickPackCellType pickPackCellType = pickPackCellTypeRepository.retrieve(pickPackCellTypeId);
        List<BoxType> boxType = new ArrayList<>();
        if (cellTypes != null && !cellTypes.isEmpty()) {
            for (String cellTypeId : cellTypes) {
                BoxType types = boxTypeRepository.retrieve(cellTypeId);
                boxType.add(types);
            }
            pickPackCellType.setBoxType(boxType);
        } else {
            pickPackCellType.setBoxType(null);
        }
        boxTypeRepository.save(boxType);
    }

    @Override
    public List<PickPackCellTypeDTO> getList(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PickPackCellType> entities = pickPackCellTypeRepository.getList(clientId, sort);
        return pickPackCellTypeMapper.toDTOList(entities);
    }

    @Override
    public List<BoxTypeDTO> getAssignedUserByReBinCellTypeId(String pickPackCellTypeId,String clientId) {
        PickPackCellType pickPackCellType = pickPackCellTypeRepository.retrieve(pickPackCellTypeId);
        List<BoxType> entities = new ArrayList<>();
        List<BoxType> boxType= pickPackCellType.getBoxType();
        for(BoxType boxTypeEntities:boxType){
            if(boxTypeEntities.getClientId().equals(clientId)){
                entities.add(boxTypeEntities);
            }
        }
        return boxTypeMapper.toDTOList(entities);
    }
   //未分配箱型过滤clientId
    @Override
    public List<BoxTypeDTO> getUnassignedUserByReBinCellTypeId(String pickPackCellTypeId,String clientId) {
        PickPackCellType pickPackCellType = pickPackCellTypeRepository.retrieve(pickPackCellTypeId);
        List<BoxType> entities = boxTypeRepository.getUnassignedReBinCellTypes(pickPackCellType.getId(), Constant.NOT_LOCKED);
        List<BoxType> boxType= new ArrayList<>();
        if(entities!=null&&entities.size()!=0){
            for(BoxType boxTypeEntities:entities){
                if(boxTypeEntities.getClientId().equals(clientId)){
                    boxType.add(boxTypeEntities);
                }
            }
        }
        return boxTypeMapper.toDTOList(boxType);
    }
}