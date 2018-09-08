package com.mushiny.wms.application.repository;


import com.mushiny.wms.application.domain.InvMitemLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface InvMitemLabelRepository extends JpaRepository<InvMitemLabel, String> {

    @Query(value = "SELECT \n" +
            "  t.LAST_UPDATE_DATE \n" +
            "FROM\n" +
            "  WMS_INV_MITEM_LABEL t \n" +
            "ORDER BY t.LAST_UPDATE_DATE DESC \n" +
            "LIMIT 0 ", nativeQuery = true)
    Date getLastUpdateDate();
}
