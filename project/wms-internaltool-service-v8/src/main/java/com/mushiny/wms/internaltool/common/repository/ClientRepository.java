package com.mushiny.wms.internaltool.common.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.internaltool.common.domain.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends BaseRepository<Client, String> {

    @Query("select c from Client c where c.name=:clientName")
    Client getByClientName(@Param("clientName") String clientName);
}
