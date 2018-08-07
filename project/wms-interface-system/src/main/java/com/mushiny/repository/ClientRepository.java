package com.mushiny.repository;

import com.mushiny.common.repository.BaseRepository;
import com.mushiny.model.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by 123 on 2018/2/2.
 */
@Repository
public interface ClientRepository extends BaseRepository<Client,String> {

    @Query("select c from Client c where c.clientNo = :clientNo")
    Client getByClientNo(@Param("clientNo")String clientNo);
}
