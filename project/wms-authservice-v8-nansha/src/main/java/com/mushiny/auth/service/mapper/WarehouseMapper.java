package com.mushiny.auth.service.mapper;

import com.mushiny.auth.domain.Warehouse;
import com.mushiny.auth.service.dto.WarehouseDTO;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper implements BaseMapper<Warehouse, WarehouseDTO> {

    @Override
    public WarehouseDTO mapEntityIntoDTO(Warehouse warehouse) {
        if (warehouse == null) {
            return null;
        }

        WarehouseDTO warehouseDTO = new WarehouseDTO(warehouse);
//        warehouseDTO.setName(warehouse.getName());
//        warehouseDTO.setNumber(warehouse.getNumber());
//        warehouseDTO.setEmail(warehouse.getEmail());
//        warehouseDTO.setPhone(warehouse.getPhone());
//        warehouseDTO.setFax(warehouse.getFax());
//        warehouseDTO.setFromIP(warehouse.getFromIP());
//        warehouseDTO.setToIP(warehouse.getToIP());
        return warehouseDTO;
    }

    @Override
    public Warehouse mapDTOIntoEntity(WarehouseDTO warehouseDTO) {
        if (warehouseDTO == null) {
            return null;
        }

        Warehouse warehouse = new Warehouse();
        warehouseDTO.merge(warehouse);
//        warehouse.setName(warehouseDTO.getName());
//        warehouse.setNumber(warehouseDTO.getNumber());
//        warehouse.setEmail(warehouseDTO.getEmail());
//        warehouse.setPhone(warehouseDTO.getPhone());
//        warehouse.setFax(warehouseDTO.getFax());
//        warehouse.setFromIP(warehouseDTO.getFromIP());
//        warehouse.setToIP(warehouseDTO.getToIP());
        return warehouse;
    }

    @Override
    public void updateEntityFromDTO(WarehouseDTO warehouseDTO, Warehouse warehouse) {
        warehouseDTO.merge(warehouse);
    }
}
