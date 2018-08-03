package com.mushiny.wms.system.service.impl;

import com.mushiny.wms.common.Constant;
import com.mushiny.wms.system.crud.dto.ClientDTO;
import com.mushiny.wms.system.crud.dto.WarehouseDTO;
import com.mushiny.wms.system.crud.mapper.ClientMapper;
import com.mushiny.wms.system.crud.mapper.WarehouseMapper;
import com.mushiny.wms.system.domain.Client;
import com.mushiny.wms.system.domain.Warehouse;
import com.mushiny.wms.system.repository.ClientRepository;
import com.mushiny.wms.system.repository.WarehouseRepository;
import com.mushiny.wms.system.service.WarehouseClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class WarehouseClientServiceImpl implements WarehouseClientService {

    private final WarehouseRepository warehouseRepository;
    private final ClientRepository clientRepository;
    private final WarehouseMapper warehouseMapper;
    private final ClientMapper clientMapper;

    @Autowired
    public WarehouseClientServiceImpl(ClientRepository clientRepository,
                                      WarehouseRepository warehouseRepository,
                                      WarehouseMapper warehouseMapper,
                                      ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseMapper = warehouseMapper;
        this.clientMapper = clientMapper;
    }


    @Override
    public void createClientWarehouses(String clientId, List<String> warehouseIds) {
        Client client = clientRepository.retrieve(clientId);
        if (warehouseIds == null || warehouseIds.isEmpty()) {
            client.setWarehouses(null);
        } else {
            Set<Warehouse> warehouses = new HashSet<>();
            for (String warehouseId : warehouseIds) {
                Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
                warehouses.add(warehouse);
            }
            client.setWarehouses(warehouses);
        }
        clientRepository.save(client);
    }

    @Override
    public void createWarehouseClients(String warehouseId, List<String> clientIds) {
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        if (clientIds != null && !clientIds.isEmpty()) {
            Set<Client> clients = new HashSet<>();
            for (String clientId : clientIds) {
                Client client = clientRepository.retrieve(clientId);
                clients.add(client);
            }
            warehouse.setClients(clients);
        } else {
            warehouse.setClients(null);
        }
        warehouseRepository.save(warehouse);
    }

    @Override
    public List<WarehouseDTO> getWarehouseList() {
        List<Warehouse> entities = warehouseRepository.findByEntityLockOrderByName(Constant.NOT_LOCKED);
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<ClientDTO> getClientList() {
        List<Client> entities = clientRepository.findByEntityLockOrderByName(Constant.NOT_LOCKED);
        return clientMapper.toDTOList(entities);
    }

    @Override
    public List<ClientDTO> getAssignedClientByWarehouseId(String warehouseId) {
        Warehouse warehouse = warehouseRepository.retrieve(warehouseId);
        List<Client> entities = new ArrayList<>();
        entities.addAll(warehouse.getClients());
        return clientMapper.toDTOList(entities);
    }

    @Override
    public List<ClientDTO> getUnassignedClientByWarehouseId(String warehouseId) {
        List<Client> entities = clientRepository.getUnassignedWarehouseClients(
                warehouseId, Constant.NOT_LOCKED);
        return clientMapper.toDTOList(entities);
    }

    @Override
    public List<WarehouseDTO> getAssignedWarehouseByClientId(String clientId) {
        Client client = clientRepository.retrieve(clientId);
        List<Warehouse> entities = new ArrayList<>();
        entities.addAll(client.getWarehouses());
        return warehouseMapper.toDTOList(entities);
    }

    @Override
    public List<WarehouseDTO> getUnassignedWarehouseByClientId(String clientId) {
        List<Warehouse> entities = warehouseRepository.getUnassignedClientWarehouses(
                clientId, Constant.NOT_LOCKED);
        return warehouseMapper.toDTOList(entities);
    }
}
