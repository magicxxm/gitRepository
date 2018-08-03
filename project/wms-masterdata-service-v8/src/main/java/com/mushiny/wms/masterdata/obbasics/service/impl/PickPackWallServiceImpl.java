package com.mushiny.wms.masterdata.obbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.utils.LevelUtil;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocationType;
import com.mushiny.wms.masterdata.mdbasics.repository.AreaRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationTypeRepository;
import com.mushiny.wms.masterdata.obbasics.crud.dto.PickPackWallDTO;
import com.mushiny.wms.masterdata.obbasics.crud.mapper.PickPackWallMapper;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.exception.OutBoundException;
import com.mushiny.wms.masterdata.obbasics.repository.*;
import com.mushiny.wms.masterdata.obbasics.service.PickPackWallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PickPackWallServiceImpl implements PickPackWallService {

    private final PickPackWallRepository pickPackWallRepository;
    private final ApplicationContext applicationContext;
    private final PickPackWallMapper pickPackWallMapper;
    private final PickPackWallTypeRepository pickPackWallTypeRepository;
    private final PickPackWallTypePositionRepository pickPackWallTypePositionRepository;
    private final PickPackCellRepository pickPackCellRepository;
    private final DigitalLabelRepository digitalLabelRepository;
    private final CellIndexRepository cellIndexRepository;
    private final AreaRepository areaRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;

    @Autowired
    public PickPackWallServiceImpl(PickPackWallRepository pickPackWallRepository,
                                   ApplicationContext applicationContext,
                                   PickPackWallMapper pickPackWallMapper,
                                   PickPackWallTypeRepository pickPackWallTypeRepository,
                                   PickPackWallTypePositionRepository pickPackWallTypePositionRepository,
                                   PickPackCellRepository pickPackCellRepository,
                                   DigitalLabelRepository digitalLabelRepository, CellIndexRepository cellIndexRepository,
                                   AreaRepository areaRepository,
                                   StorageLocationTypeRepository storageLocationTypeRepository) {
        this.pickPackWallRepository = pickPackWallRepository;
        this.applicationContext = applicationContext;
        this.pickPackWallMapper = pickPackWallMapper;
        this.pickPackWallTypeRepository = pickPackWallTypeRepository;
        this.pickPackWallTypePositionRepository = pickPackWallTypePositionRepository;
        this.pickPackCellRepository = pickPackCellRepository;
        this.digitalLabelRepository = digitalLabelRepository;
        this.cellIndexRepository = cellIndexRepository;
        this.areaRepository = areaRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
    }

    @Override
    public PickPackWallDTO create(PickPackWallDTO dto) {
        PickPackWall entity = pickPackWallMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        return pickPackWallMapper.toDTO(pickPackWallRepository.save(entity));
    }

    @Override
    public void createMore(PickPackWallDTO dto) {
        PickPackWall entity = pickPackWallMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        PickPackWallType pickPackWallType = pickPackWallTypeRepository.retrieve(dto.getTypeId());
        Area area = areaRepository.findOne(dto.getAreaId());
        StorageLocationType storageLocationType = storageLocationTypeRepository.findOne(dto.getStorageLocationTypeId());

        PickPackWall pickPackWall = new PickPackWall();
        pickPackWall.setName(dto.getName());
        pickPackWall.setDescription(dto.getDescription());
        pickPackWall.setNumberOfRows(dto.getNumberOfRows());
        pickPackWall.setNumberOfColumns(dto.getNumberOfColumns());
        pickPackWall.setPickPackWallType(pickPackWallTypeRepository.retrieve(dto.getTypeId()));
        pickPackWall.setWarehouseId(applicationContext.getCurrentWarehouse());
        pickPackWall = pickPackWallRepository.save(pickPackWall);

        //取前台传来的FieldTypeId
        List<PickPackWallTypePosition> pickPackWallTypePositions =
                pickPackWallTypePositionRepository.getByFileType(dto.getPickPackFieldTypeNames(), pickPackWallType);
        if (pickPackWallTypePositions == null) {
            throw new ApiException(OutBoundException.EX_MD_OB_DATA_NOT_FOUND.toString(), "");
        }
        for(int i=0;i<pickPackWallTypePositions.size();i++){
            for(int j=i+1;j<pickPackWallTypePositions.size();j++){
                if(pickPackWallTypePositions.get(i).getOrderIndex()==pickPackWallTypePositions.get(j).getOrderIndex()){
                    pickPackWallTypePositions.remove(pickPackWallTypePositions.get(j));
                    j--;
                }
            }
        }

        //初始digitalLabel的List为0然后循环加一取下一个对象
        int listSize = 0;
        int orderIndex = 1;
        for (PickPackWallTypePosition pickPackWallTypePosition : pickPackWallTypePositions) {

            //取FieldType下行、列进行循环生成X、Y坐标
            int columns = pickPackWallTypePosition.getPickPackFieldType().getNumberOfColumns();
            int rows = pickPackWallTypePosition.getPickPackFieldType().getNumberOfRows(); //行
//            for(int y = 1; y <= columns; y++) {
            for (int y = rows; y >0; y--) {
                for (int x = 1; x <= columns; x++) {
                    PickPackCell pickPackCell = new PickPackCell();
                    PickPackWallTypePosition pickPackWallTypePosition2=pickPackWallTypePositionRepository.getByOrderIndex(x,y,orderIndex,pickPackWallType);
                    pickPackCell.setName(dto.getName() + "-" + pickPackWallTypePosition2.getOrderIndex() + "-" + LevelUtil.getLevel(y) + String.format("%02d", x));
                    pickPackCell.setPickPackCellType(pickPackWallTypePosition2.getPickPackFieldType().getPickPackCellType());
                    pickPackCell.setxPos(x);
                    pickPackCell.setyPos(y);
                    pickPackCell.setzPos(1);
                    pickPackCell.setField(pickPackWallTypePosition2.getPickPackFieldType().getName());
                    pickPackCell.setFieldIndex(pickPackWallTypePosition2.getOrderIndex());
                    //yxf add start
                    pickPackCell.setArea(area);
                    pickPackCell.setStorageLocationType(storageLocationType);
                    //end

                    if (dto.getDigitalLabel1().size() > listSize) {
                        if (dto.getDigitalLabel1().get(listSize) != null) {
                            pickPackCell.setDigitalLabel1(digitalLabelRepository.retrieve(dto.getDigitalLabel1().get(listSize)));
                        } else {
                            pickPackCell.setDigitalLabel1(null);
                        }
                    } else {
                        pickPackCell.setDigitalLabel1(null);
                    }
                    if (dto.getDigitalLabel2().size() > listSize) {
                        if (dto.getDigitalLabel2().get(listSize) != null) {
                            pickPackCell.setDigitalLabel2(digitalLabelRepository.retrieve(dto.getDigitalLabel2().get(listSize)));
                        } else {
                            pickPackCell.setDigitalLabel2(null);
                        }
                    } else {
                        pickPackCell.setDigitalLabel1(null);
                    }
//                    List<CellIndex> cellIndexList = cellIndexRepository.getById(applicationContext.getCurrentWarehouse(), dto.getTypeId());
//                    for (CellIndex cellIndex:cellIndexList){
//                        if(orderIndex==cellIndex.getNumber()){
//                            pickPackCell.setOrderIndex(cellIndex.getCellIndex());
//                        }
//                    }
                    //设置cell顺序
                    pickPackCell.setOrderIndex(pickPackWallTypePosition2.getPosition());
                    listSize++;
                    if(x==columns && y==1){
                        orderIndex++;
                    }
                    pickPackCell.setWarehouseId(applicationContext.getCurrentWarehouse());
                    pickPackCell.setPickPackWall(pickPackWall);
                    pickPackCellRepository.save(pickPackCell);
                }
            }
        }
    }

    @Override
    public void delete(String id) {
        PickPackWall entity = pickPackWallRepository.retrieve(id);
        pickPackWallRepository.delete(entity);
    }

    @Override
    public PickPackWallDTO update(PickPackWallDTO dto) {
        PickPackWall entity = pickPackWallRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        pickPackWallMapper.updateEntityFromDTO(dto, entity);
        return pickPackWallMapper.toDTO(pickPackWallRepository.save(entity));
    }

    @Override
    public PickPackWallDTO retrieve(String id) {
        return pickPackWallMapper.toDTO(pickPackWallRepository.retrieve(id));
    }

    @Override
    public List<PickPackWallDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<PickPackWall> entities = pickPackWallRepository.getList(null, sort);
        return pickPackWallMapper.toDTOList(entities);
    }

    @Override
    public List<PickPackWallDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<PickPackWall> entities = pickPackWallRepository.getBySearchTerm(searchTerm, sort);
        return pickPackWallMapper.toDTOList(entities);
    }

    @Override
    public Page<PickPackWallDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<PickPackWall> entities = pickPackWallRepository.getBySearchTerm(searchTerm, pageable);
        return pickPackWallMapper.toDTOPage(pageable, entities);
    }

    private void checkName(String warehouse, String areaName) {
        PickPackWall workStationType = pickPackWallRepository.getByName(warehouse, areaName);
        if (workStationType != null) {
            throw new ApiException(OutBoundException.EX_MD_OB_PICKPACK_WALL_NAME_UNIQUE.toString(), areaName);
        }
    }
}