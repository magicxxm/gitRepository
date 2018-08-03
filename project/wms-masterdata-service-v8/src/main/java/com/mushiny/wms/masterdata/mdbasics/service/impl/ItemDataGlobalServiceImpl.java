package com.mushiny.wms.masterdata.mdbasics.service.impl;

import com.mushiny.wms.common.crud.dto.ImportDTO;
import com.mushiny.wms.common.utils.RandomUtil;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.masterdata.mdbasics.business.ItemDataGlobalBusiness;
import com.mushiny.wms.masterdata.mdbasics.domain.enums.Message;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemGroupRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemUnitRepository;
import com.mushiny.wms.masterdata.mdbasics.util.ReadExcelItemDataGlobal;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.ItemDataGlobalDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.mapper.ItemDataGlobalMapper;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemData;
import com.mushiny.wms.masterdata.mdbasics.domain.ItemDataGlobal;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataGlobalRepository;
import com.mushiny.wms.masterdata.mdbasics.repository.ItemDataRepository;
import com.mushiny.wms.masterdata.mdbasics.service.ItemDataGlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ItemDataGlobalServiceImpl implements ItemDataGlobalService {

    private final ItemDataGlobalRepository itemDataGlobalRepository;
    private final ItemDataRepository itemDataRepository;
    private final ApplicationContext applicationContext;
    private final ItemDataGlobalMapper itemDataGlobalMapper;
    private final ItemUnitRepository itemUnitRepository;
    private final ItemGroupRepository itemGroupRepository;
    private final ItemDataGlobalBusiness itemDataGlobalBusiness;

    @Autowired
    public ItemDataGlobalServiceImpl(ItemDataGlobalRepository itemDataGlobalRepository,
                                     ApplicationContext applicationContext,
                                     ItemDataGlobalMapper itemDataGlobalMapper,
                                     ItemDataRepository itemDataRepository,
                                     ItemUnitRepository itemUnitRepository,
                                     ItemGroupRepository itemGroupRepository,
                                     ItemDataGlobalBusiness itemDataGlobalBusiness) {
        this.itemDataGlobalRepository = itemDataGlobalRepository;
        this.applicationContext = applicationContext;
        this.itemDataGlobalMapper = itemDataGlobalMapper;
        this.itemDataRepository = itemDataRepository;
        this.itemUnitRepository = itemUnitRepository;
        this.itemGroupRepository = itemGroupRepository;
        this.itemDataGlobalBusiness = itemDataGlobalBusiness;
    }

    @Override
    public ItemDataGlobalDTO create(ItemDataGlobalDTO dto) {
        ItemDataGlobal entity = itemDataGlobalMapper.toEntity(dto);
        boolean useFlag = true;
        //生成商品唯一编码
        while (useFlag) {
            String itemNo = RandomUtil.getItemNo();
            ItemDataGlobal global = itemDataGlobalRepository.getByItemDataNo(itemNo);
            if (global == null) {
                useFlag = false;
                entity.setItemNo(itemNo);
            }
        }
        return itemDataGlobalMapper.toDTO(itemDataGlobalRepository.save(entity));
    }

    @Override
    public List<ItemDataGlobalDTO> getAll() {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
        List<ItemDataGlobal> entities = itemDataGlobalRepository.getList(null, sort);
        return itemDataGlobalMapper.toDTOList(entities);
    }

    @Override
    public void delete(String id) {
        ItemDataGlobal entity = itemDataGlobalRepository.retrieve(id);
        itemDataGlobalRepository.delete(entity);
    }

    @Override
    public List<ItemDataGlobalDTO> updateSize(ImportDTO dto) {
        List<ItemDataGlobalDTO> itemDataGlobalDTOList = dto.getUpDateSize();
        List<ItemDataGlobalDTO> itemDataGlobalDTOListNew = new ArrayList<>();
        for (ItemDataGlobalDTO itemDataGlobalDTO : itemDataGlobalDTOList) {
            ItemDataGlobal entity = itemDataGlobalRepository.retrieve(itemDataGlobalDTO.getId());
            itemDataGlobalMapper.updateEntitySizeFromDTO(itemDataGlobalDTO, entity);
            List<ItemData> itemDataList = itemDataRepository.getByItemDataGlobal(entity.getId());
            if (itemDataList != null && !itemDataList.isEmpty()) {
                for (ItemData itemData : itemDataList) {
                    itemDataGlobalMapper.updateItemDataSizeFromItemDataGlobal(itemData, entity);
                }
                itemDataRepository.save(itemDataList);
            }
            itemDataGlobalDTOListNew.add(itemDataGlobalMapper.toDTO(itemDataGlobalRepository.save(entity)));
        }
        return itemDataGlobalDTOListNew;
    }

    @Override
    public void importFile(MultipartFile file) throws IOException {
        //创建处理EXCEL的类
        ReadExcelItemDataGlobal readExcel = new ReadExcelItemDataGlobal(itemGroupRepository, itemUnitRepository);
        //解析excel，获取上传的事件单
        List<ItemDataGlobalDTO> itemDataGlobalDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(itemDataGlobalDTOList);
    }

    @Override
    public Message uploadSkuGlobal(MultipartFile file, Map map, Message message){
        return itemDataGlobalBusiness.uploadSkuGlobal(file,map,message);
    }

    @Override
    public Message updateSkuGlobal(MultipartFile file, Map map, Message message) {
        return itemDataGlobalBusiness.updateSkuGlobal(file,map,message);
    }

    @Override
    public ItemDataGlobalDTO update(ItemDataGlobalDTO dto) {
        ItemDataGlobal entity = itemDataGlobalRepository.retrieve(dto.getId());
        itemDataGlobalMapper.updateEntityFromDTO(dto, entity);
        List<ItemData> itemDataList = itemDataRepository.getByItemDataGlobal(entity.getId());
        if (itemDataList != null && !itemDataList.isEmpty()) {
            for (ItemData itemData : itemDataList) {
                itemDataGlobalMapper.updateItemDataFromItemDataGlobal(itemData, entity);
            }
            itemDataRepository.save(itemDataList);
        }
        return itemDataGlobalMapper.toDTO(itemDataGlobalRepository.save(entity));
    }

    @Override
    public ItemDataGlobalDTO retrieve(String id) {
        return itemDataGlobalMapper.toDTO(itemDataGlobalRepository.retrieve(id));
    }

    @Override
    public List<ItemDataGlobalDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<ItemDataGlobal> entities = itemDataGlobalRepository.getBySearchTerm(searchTerm, sort);
        return itemDataGlobalMapper.toDTOList(entities);
    }

    @Override
    public Page<ItemDataGlobalDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<ItemDataGlobal> entities = itemDataGlobalRepository.getBySearchTerm(searchTerm, pageable);
        return itemDataGlobalMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ItemDataGlobalDTO> getBySkuNo(String skuNo) {
        List<ItemDataGlobal> entities = itemDataGlobalRepository.getBySkuNo(skuNo);
        return itemDataGlobalMapper.toDTOList(entities);
    }

    @Override
    public List<ItemDataGlobalDTO> getByClientId(String clientId) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "itemNo"));
        applicationContext.isCurrentClient(clientId);
        List<ItemDataGlobal> entities = itemDataGlobalRepository.getList(clientId, sort);
        return itemDataGlobalMapper.toDTOList(entities);
    }

    //批量保存数据
    public void createImport(List<ItemDataGlobalDTO> itemDataGlobalDTOList) {
        for (ItemDataGlobalDTO itemDataGlobalDTO : itemDataGlobalDTOList) {
            ItemDataGlobal entity = itemDataGlobalMapper.toEntity(itemDataGlobalDTO);
            boolean useFlag = true;
            while (useFlag) {
                String itemNo = RandomUtil.getItemNo();
                ItemDataGlobal global = itemDataGlobalRepository.getByItemDataNo(itemNo);
                if (global == null) {
                    useFlag = false;
                    entity.setItemNo(itemNo);
                }
            }
            itemDataGlobalRepository.save(entity);
        }
    }
}
