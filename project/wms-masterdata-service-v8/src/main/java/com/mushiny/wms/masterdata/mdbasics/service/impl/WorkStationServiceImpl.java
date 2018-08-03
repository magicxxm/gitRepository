package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.ibbasics.domain.IBPStation;
import com.mushiny.wms.masterdata.ibbasics.domain.ReceiveStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StockTakingStation;
import com.mushiny.wms.masterdata.ibbasics.domain.StowStation;
import com.mushiny.wms.masterdata.ibbasics.repository.IBPStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.ReceiveStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StockTakingStationRepository;
import com.mushiny.wms.masterdata.ibbasics.repository.StowStationRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.WorkStationPositionDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.NodeMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.WorkStationPositionMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStation;
import com.mushiny.wms.masterdata.mdbasics.domain.WorkStationPosition;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.MapRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.NodeRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.WorkStationRepository;
import com.mushiny.wms.masterdata.mdbasics.service.WorkStationService;
import com.mushiny.wms.masterdata.obbasics.domain.*;
import com.mushiny.wms.masterdata.obbasics.repository.*;
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
public class WorkStationServiceImpl implements WorkStationService {

    private final WorkStationRepository workStationRepository;
    private final WorkStationMapper workStationMapper;
    private final WorkStationPositionMapper workStationPositionMapper;
    private final LabelControllerRepository labelControllerRepository;
    private final NodeRepository nodeRepository;
    private final NodeMapper nodeMapper;
    private final ApplicationContext applicationContext;
    private final MapRepository mapRepository;
    private final PickStationRepository pickStationRepository;
    private final PackingStationRepository packingStationRepository;
    private final ReceiveStationRepository receiveStationRepository;
    private final StockTakingStationRepository stockTakingStationRepository;
    private final IBPStationRepository iBPStationRepository;
    private final OBPStationRepository oBPStationRepository;
    private final StowStationRepository StowStationRepository;
    private final SortingStationRepository SortingStationRepository;
    private final ReBinStationRepository ReBinStationRepository;
    private final RebatchStationRepository RebatchStationRepository;

    @Autowired
    public WorkStationServiceImpl(WorkStationRepository workStationRepository,
                                  WorkStationMapper workStationMapper,
                                  WorkStationPositionMapper workStationPositionMapper,
                                  LabelControllerRepository labelControllerRepository,
                                  NodeRepository nodeRepository,
                                  NodeMapper nodeMapper,
                                  ApplicationContext applicationContext,
                                  MapRepository mapRepository,
                                  PickStationRepository pickStationRepository,
                                  PackingStationRepository packingStationRepository,
                                  ReceiveStationRepository receiveStationRepository,
                                  StockTakingStationRepository stockTakingStationRepository,
                                  IBPStationRepository iBPStationRepository,
                                  OBPStationRepository oBPStationRepository,
                                  StowStationRepository stowStationRepository,
                                  SortingStationRepository sortingStationRepository,
                                  ReBinStationRepository reBinStationRepository,
                                  com.mushiny.wms.masterdata.obbasics.repository.RebatchStationRepository rebatchStationRepository) {
        this.workStationRepository = workStationRepository;
        this.workStationMapper = workStationMapper;
        this.workStationPositionMapper = workStationPositionMapper;
        this.labelControllerRepository = labelControllerRepository;
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
        this.applicationContext = applicationContext;
        this.mapRepository = mapRepository;
        this.pickStationRepository = pickStationRepository;
        this.packingStationRepository = packingStationRepository;
        this.receiveStationRepository = receiveStationRepository;
        this.stockTakingStationRepository = stockTakingStationRepository;
        this.iBPStationRepository = iBPStationRepository;
        this.oBPStationRepository = oBPStationRepository;
        StowStationRepository = stowStationRepository;
        SortingStationRepository = sortingStationRepository;
        ReBinStationRepository = reBinStationRepository;
        RebatchStationRepository = rebatchStationRepository;
    }

    @Override
    public WorkStationDTO create(WorkStationDTO dto) {
        WorkStation entity = workStationMapper.toEntity(dto);
        checkName(entity.getWarehouseId(), entity.getName());
        for (WorkStationPositionDTO positionDTO : dto.getPositions()) {
            entity.addPosition(workStationPositionMapper.toEntity(positionDTO));
        }
        return workStationMapper.toDTO(workStationRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        WorkStation entity = workStationRepository.retrieve(id);
        workStationRepository.delete(entity);
    }

    @Override
    public WorkStationDTO update(WorkStationDTO dto) {
        WorkStation entity = workStationRepository.retrieve(dto.getId());
        if (!(entity.getName().equalsIgnoreCase(dto.getName()))) {
            checkName(entity.getWarehouseId(), dto.getName());
        }
        workStationMapper.updateEntityFromDTO(dto, entity);
        //判断position是否数据发生变化
        List<WorkStationPositionDTO> positionDTO = dto.getPositions();
        List<WorkStationPosition> positionEntity = entity.getPositions();
        //先判断数量是否发生变化，如果数量没有发生变化,进行positionNo判断
        if (positionDTO.size() == positionEntity.size()) {
            String positionNo = "";
            for (WorkStationPosition p : positionEntity) {
                positionNo = positionNo + p.getPositionNo() + p.getPositionIndex() + p.getDigitalLabel();
            }
            for (WorkStationPositionDTO pD : positionDTO) {
                if (!positionNo.contains(pD.getPositionNo() + pD.getPositionIndex() + pD.getDigitalLabel())) {
                    entity.getPositions().clear();
                    List<WorkStationPosition> positions = workStationPositionMapper.toEntityList(dto.getPositions());
                    for (WorkStationPosition position : positions) {
                        entity.addPosition(position);
                    }
                    break;
                }
            }
        } else {
            entity.getPositions().clear();
            List<WorkStationPosition> positions = workStationPositionMapper.toEntityList(dto.getPositions());
            for (WorkStationPosition position : positions) {
                entity.addPosition(position);
            }
        }
        return workStationMapper.toDTO(workStationRepository.save(entity));
    }

    @Override
    public WorkStationDTO retrieve(String id) {
        WorkStation entity = workStationRepository.retrieve(id);
        WorkStationDTO dto = workStationMapper.toDTO(entity);
        dto.setPositions(workStationPositionMapper.toDTOList(entity.getPositions()));
//        if (entity.getPositions()!=null&&entity.getPositions().size()!=0) {
//            if (entity.getPositions().get(0).getDigitalLabel() != null) {
//                dto.setLabelController(labelControllerRepository.getById(entity.getPositions().get(0).getDigitalLabel().getLabelController().getId()));
//            }
//        }
        return dto;
    }

    @Override
    public List<WorkStationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<WorkStation> entities = workStationRepository.getBySearchTerm(searchTerm, sort);
        return workStationMapper.toDTOList(entities);
    }

    @Override
    public Page<WorkStationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<WorkStation> entities = workStationRepository.getBySearchTerm(searchTerm, pageable);
        return workStationMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<WorkStationDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<WorkStation> entities = workStationRepository.getList(null, sort);
        return workStationMapper.toDTOList(entities);
    }

    @Override
    public List<NodeDTO> getBySectionId(String sectionId) {
        List<Map> mapList = mapRepository.getMapBySectionId(applicationContext.getCurrentWarehouse(), sectionId, true);
        if (mapList.size() == 1) {
            Map mapEntities = mapList.get(0);
            List<Node> nodeList = nodeRepository.getByMapId(mapEntities.getWarehouseId(), mapEntities.getId());
            return nodeMapper.toDTOList(nodeList);
        } else if (mapList.size() == 0) {
            throw new ApiException("该section内没有激活的map");
        } else {//如果数据不止一条说明，该区域激活了多个地图；
            throw new ApiException("该section内激活的map不止一条，请修改激活地图");
        }
    }

    @Override
    public void exitWorkStation(String stationId) {
        WorkStation entity = workStationRepository.retrieve(stationId);
        entity.setStationName(null);
       // entity.setOperator(null);
        entity.setisCallPod(false);
        PickStation PickStation = pickStationRepository.getByWorkStationId(entity, entity.getOperator());
        PackingStation PackingStation = packingStationRepository.getByWorkStationId(entity, entity.getOperator());
        ReceiveStation ReceiveStation = receiveStationRepository.getByWorkStationId(entity, entity.getOperator());
        StockTakingStation StockTakingStation = stockTakingStationRepository.getByWorkStationId(entity, entity.getOperator());
        IBPStation IBPStation = iBPStationRepository.getByWorkStationId(entity, entity.getOperator());
        OBPStation obpStation = oBPStationRepository.getByWorkStationId(entity, entity.getOperator());
        StowStation StowStation = StowStationRepository.getByWorkStationId(entity, entity.getOperator());
        SortingStation sortStation = SortingStationRepository.getByWorkStationId(entity, entity.getOperator());
        ReBinStation rebinStation = ReBinStationRepository.getByWorkStationId(entity, entity.getOperator());
        RebatchStation RebatchStation = RebatchStationRepository.getByWorkStationId(entity, entity.getOperator());
        if (PickStation != null) {
            PickStation.setOperator(null);
        } else if (PackingStation != null) {
            PackingStation.setOperator(null);
        } else if (ReceiveStation != null) {
            ReceiveStation.setOperator(null);
        } else if (StockTakingStation != null) {
            StockTakingStation.setOperatorId(null);
        } else if (IBPStation != null) {
            IBPStation.setOperatorId(null);
        } else if (obpStation != null) {
            obpStation.setOperatorId(null);
        } else if (StowStation != null) {
            StowStation.setOperator(null);
        } else if (sortStation != null) {
            sortStation.setOperator(null);
        } else if (rebinStation != null) {
            rebinStation.setOperator(null);
        } else if (RebatchStation != null) {
            RebatchStation.setOperator(null);
        }
        entity.setOperator(null);
    }

    private void checkName(String warehouse, String bayTypeName) {
        WorkStation podType = workStationRepository.getByName(warehouse, bayTypeName);
        if (podType != null) {
            throw new ApiException(MasterDataException.EX_MD_WORK_STATION_NAME_UNIQUE.toString(), bayTypeName);
        }
    }
}
