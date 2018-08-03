package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.masterdata.mdbasics.business.PodBusiness;
import com.mushiny.wms.masterdata.mdbasics.business.dto.PodStorageLocationsDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.NodeMapper;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.PodMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.Map;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import com.mushiny.wms.masterdata.mdbasics.domain.Pod;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.*;
import com.mushiny.wms.masterdata.mdbasics.service.PodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PodServiceImpl implements PodService {

    private final PodRepository podRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final ApplicationContext applicationContext;
    private final PodBusiness podBusiness;
    private final PodMapper podMapper;
    private final NodeMapper nodeMapper;
    private final NodeRepository nodeRepository;
    private final MapRepository mapRepository;

    @Autowired
    public PodServiceImpl(ApplicationContext applicationContext,
                          PodRepository podRepository,
                          PodMapper podMapper,
                          StorageLocationRepository storageLocationRepository,
                          PodBusiness podBusiness,
                          NodeMapper nodeMapper, NodeRepository nodeRepository, MapRepository mapRepository) {
        this.applicationContext = applicationContext;
        this.podRepository = podRepository;
        this.podMapper = podMapper;
        this.storageLocationRepository = storageLocationRepository;
        this.podBusiness = podBusiness;
        this.nodeMapper = nodeMapper;
        this.nodeRepository = nodeRepository;
        this.mapRepository = mapRepository;
    }

    @Override
    public void createMore(PodStorageLocationsDTO dto) {
        podBusiness.createMore(dto);
    }

    @Override
    public PodDTO create(PodDTO dto) {
        Pod entity = podMapper.toEntity(dto);
        checkPodName(entity.getWarehouseId(), entity.getName());
        return podMapper.toDTO(podRepository.save(entity));
    }

    @Override
    public void delete(String id) {
        Pod entity = podRepository.retrieve(id);
        int maxBinIndex = storageLocationRepository.getPodMaxIndex(entity);
        int minBinIndex = storageLocationRepository.getPodMinIndex(entity);
//        podRepository.updateDeletePodIndex(entity.getPodIndex(), entity.getWarehouseId());
        int subIndex = maxBinIndex - minBinIndex + 1;
        storageLocationRepository.updateDeleteBinOrderIndex(subIndex, maxBinIndex, entity.getWarehouseId());
        // 删除
        List<StorageLocation> bins = storageLocationRepository.getByPod(entity);
        if (!bins.isEmpty()) {
            storageLocationRepository.delete(bins);
        }
        podRepository.delete(entity);
    }

    @Override
    public PodDTO update(PodDTO dto) {
        Pod entity = podRepository.retrieve(dto.getId());
        if (!entity.getName().equalsIgnoreCase(dto.getName())) {
            checkPodName(entity.getWarehouseId(), dto.getName());
        }
        podMapper.updateEntityFromDTO(dto, entity);
        return podMapper.toDTO(podRepository.save(entity));
    }

    @Override
    public PodDTO retrieve(String id) {
        return podMapper.toDTO(podRepository.retrieve(id));
    }

    @Override
    public List<PodDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Pod> entities = podRepository.getBySearchTerm(searchTerm, sort);
        return podMapper.toDTOList(entities);
    }

    @Override
    public Page<PodDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Pod> entities = podRepository.getBySearchTerm(searchTerm, pageable);
        return podMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<PodDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "podIndex"));
        applicationContext.isCurrentClient(clientId);
        List<Pod> entities = podRepository.getList(clientId, sort);
        return podMapper.toDTOList(entities);
    }

    @Override
    public List<NodeDTO> getPlaceMark(String id) {
        Pod pod = podRepository.getById(applicationContext.getCurrentWarehouse(),id);
      String sectionId;
        if(pod!=null){
            sectionId=pod.getSection().getId();//传过来的是podId
        }else {
            sectionId=id;//传过来的sectionID
        }
        List<Map> mapList = mapRepository.getMapBySectionId(applicationContext.getCurrentWarehouse(),sectionId, true);
        if(mapList.size()==1){
            Map map=  mapList.get(0);
            List<Node> nodeList=nodeRepository.getByMapId(map.getWarehouseId(),map.getId());
            return nodeMapper.toDTOList(nodeList);
        } else if(mapList.size()==0){
            throw new ApiException("该section内没有激活的map");
        }else{//如果数据不止一条说明，该区域激活了多个地图；
            throw new ApiException("该section内激活的map不止一条，请修改激活地图");
        }
    }

    private void checkPodName(String warehouse, String podName) {
        Pod pod = podRepository.getByName(warehouse, podName);
        if (pod != null) {
            throw new ApiException(MasterDataException.EX_MD_POD_NAME_UNIQUE.toString(), podName);
        }
    }
}
