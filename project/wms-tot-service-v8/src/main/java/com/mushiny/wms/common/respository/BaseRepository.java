package com.mushiny.wms.common.respository;

import com.mushiny.wms.tot.general.domain.Client;
import com.mushiny.wms.tot.general.domain.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    T retrieve(String id);

    List<T> getList(String clientId, Sort sort);

    List<T> getNotLockList(String clientId, Sort sort);

    Long countBySearchTerm(String searchTerm);

    List<T> getBySearchTerm(String searchTerm);

    List<T> getBySearchTerm(String searchTerm, Sort sort);

    Page<T> getBySearchTerm(String searchTerm, Pageable pageable);

    List<T> getList(Warehouse warehouse, Client client);

    List<T> getList(String warehouseId, String clientId, Sort sort);

    Page<T> getList(String warehouseId, String clientId, Pageable pageable);

    List<T> getListBySearchTerm(Warehouse warehouse, Client client, String searchTerm);

    List<T> getListBySearchTerm(Warehouse warehouse, Client client, String searchTerm, Sort sort);

    Page<T> getListBySearchTerm(String warehouseId, String clientId, String searchTerm, Pageable pageable);

    boolean exists(ID id);

    long count(String warehouseId, String clientId);

    long count(String warehouseId, String clientId, String searchTerm);
}
