package com.mushiny.wms.tot.general.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.tot.general.crud.dto.ClientDTO;
import com.mushiny.wms.tot.general.crud.mapper.ClientMapper;
import com.mushiny.wms.tot.general.domain.Client;
import com.mushiny.wms.tot.general.domain.Warehouse;
import com.mushiny.wms.tot.general.exception.SystemException;
import com.mushiny.wms.tot.general.repository.ClientRepository;
import com.mushiny.wms.tot.general.repository.WarehouseRepository;
import com.mushiny.wms.tot.general.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final WarehouseRepository warehouseRepository;
    private final ApplicationContext applicationContext;
    private final ClientMapper clientMapper;
    @PersistenceContext
    private EntityManager entityManager;

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
    public List<ClientDTO> getByWarehouseId(String warehouseId) {
        List<Client> list = new ArrayList<Client>();
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        Set<Client> clients = warehouse.getClients();
//        Iterator<Client> it = clients.iterator();
//        while (it.hasNext()) {
//            Client client = it.next();
//            list.add(client);
//        }
        list.addAll(clients);
        List<ClientDTO> clientDTOs = clientMapper.toDTOList(list);
        return clientDTOs;
    }

    @Override
    public List<ClientDTO> getClientByCurrentWarehouse() {
        List<Client> list = new ArrayList<Client>();
        String currentWarehouseId = applicationContext.getCurrentWarehouse();
//        String sql ="SELECT NAME from SYS_WAREHOUSE WHERE ID = :currentWarehouseId";
//        Query query = entityManager.createNativeQuery(sql);
//        query.setParameter("currentWarehouseId", currentWarehouseId);
//        List<String> warehouseEntity = query.getResultList();
//        String currentWarehouseName = warehouseEntity.get(0);
//        //只有findByName才会用到表SYS_WAREHOUSE中的name
//        Warehouse warehouse = warehouseRepository.findByName(currentWarehouseName);
        Warehouse warehouse = warehouseRepository.retrieve(currentWarehouseId);
        Set<Client> clients = warehouse.getClients();
//        Iterator<Client> it = clients.iterator();
//        while (it.hasNext()) {
//            Client client = it.next();
//            list.add(client);
//        }
        list.addAll(clients);
        List<ClientDTO> clientDTOs = clientMapper.toDTOList(list);
        ClientDTO allDTO = new ClientDTO();
        allDTO.setClientNo("ALL");
        allDTO.setName("ALL");
        allDTO.setId("ALL");
        clientDTOs.add(0,allDTO);
        return clientDTOs;
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
}
