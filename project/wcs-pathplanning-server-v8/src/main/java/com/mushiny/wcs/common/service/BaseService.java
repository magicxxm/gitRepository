package com.mushiny.wcs.common.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface BaseService<D> {

    D create(D dto);

    void delete(String id);

    D update(D dto);

    D retrieve(String id);

    List<D> getBySearchTerm(String searchTerm, Sort sort);

    Page<D> getBySearchTerm(String searchTerm, Pageable pageable);

    default String getSearchTerm(String searchTerm, String defaultSearchTerm) {
        StringBuilder searchTermBuilder = new StringBuilder();
        if (searchTerm != null && !searchTerm.equals("")) {
            searchTermBuilder.append(searchTerm);
            if (defaultSearchTerm != null && !defaultSearchTerm.equals("")) {
                searchTermBuilder.append(";");
            }
        }
        if (defaultSearchTerm != null && !defaultSearchTerm.equals("")) {
            searchTermBuilder.append(defaultSearchTerm);
        }
        return searchTermBuilder.toString();
    }
}
