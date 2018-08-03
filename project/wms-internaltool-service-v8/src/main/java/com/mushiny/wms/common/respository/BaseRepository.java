package com.mushiny.wms.common.respository;

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
}
