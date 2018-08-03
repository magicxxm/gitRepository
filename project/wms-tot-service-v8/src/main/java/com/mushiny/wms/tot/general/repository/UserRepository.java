package com.mushiny.wms.tot.general.repository;

import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.tot.general.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends BaseRepository<User, String> {

    @Query("select u from User u where u.username = :username ")
    User findByUsername( @Param("username")String username);

    @Query("select u from User u where u.username = :username and u.warehouse.id = :warehouse")
    User findByUsername( @Param("username")String username,@Param("warehouse")String warehouse);

    @Query(" select u from User  u where u.warehouse.id = :warehouseId and u.client.id = :clientId ")
    List<User> getUserByWarehouseAndClient(@Param("warehouseId")String warehouseId, @Param("clientId")String clientId);
}
