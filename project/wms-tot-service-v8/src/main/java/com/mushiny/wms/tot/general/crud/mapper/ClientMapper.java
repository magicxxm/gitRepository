package com.mushiny.wms.tot.general.crud.mapper;

import com.mushiny.wms.common.crud.mapper.BaseMapper;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.tot.general.crud.dto.ClientDTO;
import com.mushiny.wms.tot.general.domain.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper implements BaseMapper<ClientDTO, Client> {

    @Override
    public ClientDTO toDTO(Client entity) {
        if (entity == null) {
            return null;
        }

        ClientDTO dto = new ClientDTO(entity);

        dto.setName(entity.getName());
        dto.setClientNo(entity.getClientNo());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setFax(entity.getFax());

        return dto;
    }

    @Override
    public Client toEntity(ClientDTO dto) {
        if (dto == null) {
            return null;
        }

        Client entity = new Client();
        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setClientNo(dto.getClientNo());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setFax(dto.getFax());

        return entity;
    }

    @Override
    public void updateEntityFromDTO(ClientDTO dto, Client entity) {
        if (dto == null || entity == null) {
            throw new ApiException(ExceptionEnum.EX_SERVER_ERROR.toString());
        }

        entity.setId(dto.getId());
        entity.setAdditionalContent(dto.getAdditionalContent());
        entity.setName(dto.getName());
        entity.setClientNo(dto.getClientNo());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setFax(dto.getFax());
    }
}
