package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.WmsWarehousePosition;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2018/7/11.
 */
public interface WmsWarehousePositionRepository  extends JpaRepository<WmsWarehousePosition, String> {
}
