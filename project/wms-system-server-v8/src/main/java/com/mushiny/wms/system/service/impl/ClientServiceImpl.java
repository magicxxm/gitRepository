package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.system.crud.dto.ClientDTO;
import com.mushiny.wms.system.crud.mapper.ClientMapper;
import com.mushiny.wms.system.domain.Client;
import com.mushiny.wms.system.domain.Warehouse;
import com.mushiny.wms.system.exception.SystemException;
import com.mushiny.wms.system.importUitl.ReadExcelClient;
import com.mushiny.wms.system.repository.ClientRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import com.mushiny.wms.system.service.ClientService;
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
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final ApplicationContext applicationContext;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             ApplicationContext applicationContext,
                             ClientMapper clientMapper, WarehouseRepository warehouseRepository) {
        this.clientRepository = clientRepository;
        this.applicationContext = applicationContext;
        this.clientMapper = clientMapper;
        this.warehouseRepository = warehouseRepository;
    }
    @Override
    public ClientDTO create(ClientDTO dto) {
        String clientId = applicationContext.getCurrentClient();
        if (applicationContext.isSystemClient(clientId)) {
            Client entity = clientMapper.toEntity(dto);
            checkClientName(entity.getName());
            checkClientNo(entity.getClientNo());
            return clientMapper.toDTO(clientRepository.save(entity));
        }
        throw new ApiException(SystemException.EX_SYS_NOT_SYSTEM_CLIENT.toString());
    }

    @Override
    public void delete(String id) {
        String clientId = applicationContext.getCurrentClient();
        if (applicationContext.isSystemClient(clientId)) {
            Client entity = clientRepository.retrieve(id);
            entity.setWarehouses(null);
            entity.setEntityLock(Constant.GOING_TO_DELETE);
            clientRepository.save(entity);
        } else {
            throw new ApiException(SystemException.EX_SYS_NOT_SYSTEM_CLIENT.toString());
        }
    }

    @Override
    public ClientDTO update(ClientDTO dto) {
        String clientId = applicationContext.getCurrentClient();
        if (applicationContext.isSystemClient(clientId)) {
            Client entity = clientRepository.retrieve(dto.getId());
            if (!entity.getName().equalsIgnoreCase(dto.getName())) {
                checkClientName(dto.getName());
            }
            if (!entity.getClientNo().equals(dto.getClientNo())) {
                checkClientNo(dto.getClientNo());
            }
            clientMapper.updateEntityFromDTO(dto, entity);
            return clientMapper.toDTO(clientRepository.save(entity));
        } else {
            throw new ApiException(SystemException.EX_SYS_NOT_SYSTEM_CLIENT.toString());
        }
    }

    @Override
    public ClientDTO retrieve(String id) {
        Client entity = clientRepository.retrieve(id);
        String clientId = applicationContext.getCurrentClient();
        if (applicationContext.isSystemClient(clientId)) {
            return clientMapper.toDTO(entity);
        } else {
            if (applicationContext.getCurrentClient().equals(id)) {
                return clientMapper.toDTO(entity);
            } else {
                throw new ApiException(SystemException.EX_SYS_NOT_SYSTEM_CLIENT.toString());
            }
        }
    }

    @Override
    public List<ClientDTO> getBySearchTerm(String searchTerm, Sort sort) {
        List<Client> entities = clientRepository.getBySearchTerm(searchTerm, sort);
        return clientMapper.toDTOList(entities);
    }

    @Override
    public Page<ClientDTO> getBySearchTerm(String searchTerm, Pageable pageable) {
        if (pageable.getSort() == null) {
            Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "name"));
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, sort);
        } else {
            pageable = new PageRequest(pageable.getPageNumber(), 1000000, pageable.getSort());
        }
        Page<Client> entities = clientRepository.getBySearchTerm(searchTerm, pageable);
        return clientMapper.toDTOPage(pageable, entities);
    }

    @Override
    public List<ClientDTO> getByCurrentWarehouseId() {
        List<Client> entities = new ArrayList<>();
        String clientId = applicationContext.getCurrentClient();
        if (applicationContext.isSystemClient(clientId)) {
            Warehouse warehouse = warehouseRepository.retrieve(applicationContext.getCurrentWarehouse());
            entities.addAll(warehouse.getClients());
        } else {
            entities.add(clientRepository.findOne(clientId));
        }
        return clientMapper.toDTOList(entities);
    }

    @Override
    public void importFile(MultipartFile file) {
        //创建处理EXCEL的类
        ReadExcelClient readExcel = new ReadExcelClient();
        //解析excel，获取上传的事件单
        List<ClientDTO> clientDTOList = readExcel.getExcelInfo(file);
        //保存数据
        createImport(clientDTOList);
    }

    private void checkClientName(String clientName) {
        Client client = clientRepository.findByName(clientName);
        if (client != null) {
            throw new ApiException(SystemException.EX_SYS_CLIENT_NAME_UNIQUE.toString(), clientName);
        }
    }

    private void checkClientNo(String clientNo) {
        Client client = clientRepository.findByClientNo(clientNo);
        if (client != null) {
            throw new ApiException(SystemException.EX_SYS_CLIENT_NO_UNIQUE.toString(), clientNo);
        }
    }
    public void createImport(List<ClientDTO> clientDTOList) {
        for (ClientDTO clientDTO : clientDTOList) {
            String clientId = applicationContext.getCurrentClient();
            if (applicationContext.isSystemClient(clientId)) {
                Client entity = clientMapper.toEntity(clientDTO);
                checkClientName(entity.getName());
                checkClientNo(entity.getClientNo());
               clientRepository.save(entity);
            } else {
                throw new ApiException(SystemException.EX_SYS_NOT_SYSTEM_CLIENT.toString());
            }
        }
    }
}
