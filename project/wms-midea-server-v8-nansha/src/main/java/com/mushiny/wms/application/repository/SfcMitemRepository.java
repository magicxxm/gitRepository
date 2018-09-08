package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.SfcMitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface SfcMitemRepository extends JpaRepository<SfcMitem, String> {
    @Query(value = "SELECT \n" +
            "  t.LAST_UPDATE_DATE \n" +
            "FROM\n" +
            "  WMS_SFC_MITEM t \n" +
            "ORDER BY t.LAST_UPDATE_DATE DESC \n" +
            "LIMIT 1 ", nativeQuery = true)
    Date getLastUpdateDate();
  @Query(" select n from SfcMitem n " +
            " where n.MITEM_CODE=:mitemCode"
    )
  List<SfcMitem> getSfcMitemByName(@Param("mitemCode") String mitemCode);
}
