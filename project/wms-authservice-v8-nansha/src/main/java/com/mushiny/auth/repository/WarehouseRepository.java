package com.mushiny.auth.repository;

import com.mushiny.auth.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, String>, WarehouseRepositoryCustom {

}
