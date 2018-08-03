package com.mushiny.wms.masterdata.mdbasics.crud.mapper;

import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.masterdata.general.repository.ClientRepository;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.AreaDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.NodeDTO;
import com.mushiny.wms.masterdata.mdbasics.crud.dto.PodDTO;
import com.mushiny.wms.masterdata.mdbasics.domain.Area;
import com.mushiny.wms.masterdata.mdbasics.domain.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeMapper implements BaseMapper<NodeDTO, Node> {

    private final ApplicationContext applicationContext;
    private final ClientRepository clientRepository;

    @Autowired
    public NodeMapper(ApplicationContext applicationContext,
                      ClientRepository clientRepository) {
        this.applicationContext = applicationContext;
        this.clientRepository = clientRepository;
    }


    @Override
    public NodeDTO toDTO(Node entity) {
        if (entity == null) {
            return null;
        }
        NodeDTO dto = new NodeDTO(entity);
        dto.setxPos(entity.getxPosition());
        dto.setyPos(entity.getyPosition());
        dto.setPlaceMark(entity.getAddressCodeId());
        return dto;
    }

    @Override
    public Node toEntity(NodeDTO dto) {
        return null;
    }

    @Override
    public void updateEntityFromDTO(NodeDTO dto, Node entity) {

    }
}

