package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.ibbasics.crud.mapper.UnitLoadMapper;
import com.mushiny.wms.masterdata.ibbasics.repository.UnitLoadRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.StorageLocationDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.StorageLocationMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.StorageLocation;
import com.mushiny.wms.masterdata.mdbasics.exception.MasterDataException;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.StorageLocationTypeRepository;
import com.mushiny.wms.masterdata.mdbasics.service.StorageLocationService;
import com.mushiny.wms.masterdata.mdbasics.util.ReadExcelStorageLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StorageLocationServiceImpl implements StorageLocationService {

    private final StorageLocationRepository storageLocationRepository;
    private final StorageLocationTypeRepository storageLocationTypeRepository;
    private final ApplicationContext applicationContext;
    private final StorageLocationMapper storageLocationMapper;
    private final UnitLoadRepository unitLoadRepository;
    private final UnitLoadMapper unitLoadMapper;

    @Autowired
    public StorageLocationServiceImpl(ApplicationContext applicationContext,
                                      StorageLocationRepository storageLocationRepository,
                                      StorageLocationTypeRepository storageLocationTypeRepository, StorageLocationMapper storageLocationMapper, UnitLoadRepository unitLoadRepository, UnitLoadMapper unitLoadMapper) {
        this.applicationContext = applicationContext;
        this.storageLocationRepository = storageLocationRepository;
        this.storageLocationTypeRepository = storageLocationTypeRepository;
        this.storageLocationMapper = storageLocationMapper;
        this.unitLoadRepository = unitLoadRepository;
        this.unitLoadMapper = unitLoadMapper;
    }

    @Override
    public StorageLocationDTO create(StorageLocationDTO dto) {
        StorageLocation entity = storageLocationMapper.toEntity(dto);
        entity.setName(dto.getName());
        checkStorageLocationName(entity.getWarehouseId(), entity.getName());
        return storageLocationMapper.toDTO(storageLocationRepository.save(entity));
    }



    @Override
    public void delete(String id) {
        StorageLocation entity = storageLocationRepository.retrieve(id);
        storageLocationRepository.delete(entity);
    }

    @Override
    public StorageLocationDTO update(StorageLocationDTO dto) {
        return dto;
    }

    @Override
    public StorageLocationDTO retrieve(String id) {
        return storageLocationMapper.toDTO(storageLocationRepository.retrieve(id));
    }

    @Override
    public List<StorageLocationDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<StorageLocation> entities = storageLocationRepository.getBySearchTerm(searchTerm, sort);
        return storageLocationMapper.toDTOList(entities);
    }

    @Override
    public Page<StorageLocationDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);}
//        }if(pageable.getPageSize()==1000){
//            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
//            pageable = new PageRequest(pageable.getPageNumber(), 100000, sort);
//        }
        Page<StorageLocation> entities = storageLocationRepository.getBySearchTerm(searchTerm, pageable);
        return storageLocationMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<StorageLocationDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        applicationContext.isCurrentClient(clientId);
        List<StorageLocation> entities = storageLocationRepository.getList(clientId, sort);
        return storageLocationMapper.toDTOList(entities);
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelStorageLocation readExcel = new ReadExcelStorageLocation(storageLocationTypeRepository);
        //解析excel，获取上传的事件单
        List<StorageLocationDTO> storageLocationDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(storageLocationDTOList);
    }

    @Override
    public List<StorageLocationDTO> getName() {
        List<String> list=  storageLocationRepository.getName(applicationContext.getCurrentWarehouse(),0);
        List<StorageLocationDTO> dto=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            StorageLocationDTO storageLocation=new StorageLocationDTO();
            storageLocation.setName(list.get(i));
            dto.add(storageLocation);
        }
        return dto;
    }

    private void checkStorageLocationName(String warehouse, String name) {
        StorageLocation storageLocation = storageLocationRepository.getByName(warehouse, name);
        if (storageLocation != null) {
            throw new ApiException(MasterDataException.EX_MD_STORAGE_LOCATION_NAME_UNIQUE.toString(), name);
        }
    }
    public void createImport(List<StorageLocationDTO> storageLocationDTOList) {
        for (StorageLocationDTO storageLocationDTO : storageLocationDTOList) {
            StorageLocation entity = storageLocationMapper.toEntity(storageLocationDTO);
            entity.setName(storageLocationDTO.getName());
            checkStorageLocationName(entity.getWarehouseId(), entity.getName());
            storageLocationRepository.save(entity);
        }
    }
}
