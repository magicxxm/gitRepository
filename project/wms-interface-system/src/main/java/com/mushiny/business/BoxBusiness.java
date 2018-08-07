package com.mushiny.business;

import com.mushiny.repository.BoxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Created by 123 on 2018/2/2.
 */
@Component
public class BoxBusiness {
    private final Logger log = LoggerFactory.getLogger(BoxBusiness.class);

    private final BoxRepository boxRepository;
    private final EntityManager manager;

    @Autowired
    public BoxBusiness(BoxRepository boxRepository,
                       EntityManager manager){
        this.boxRepository = boxRepository;
        this.manager = manager;
    }

   /* public BoxType createBox(BoxDTO dto, Warehouse warehouse, Client client){
        BoxType boxType = new BoxType();

        boxType.setDepth(dto.getDepth());
        boxType.setGroup(dto.getTypeGroup());
        boxType.setHeight(dto.getHeight());
        boxType.setName(dto.getName());
        boxType.setThickness(dto.getThickness());
        boxType.setWeight(dto.getWeight());
        boxType.setWidth(dto.getWidth());
        boxType.setWarehouseId(warehouse.getId());
        boxType.setClientId(client.getId());

        manager.persist(boxType);

        return boxType;
    }*/
}
