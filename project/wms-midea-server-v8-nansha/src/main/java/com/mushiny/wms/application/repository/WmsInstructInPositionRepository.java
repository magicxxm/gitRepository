package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.WmsInstructInPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/14.
 */
public interface WmsInstructInPositionRepository extends JpaRepository<WmsInstructInPosition, String> {
    @Query(" select r from WmsInstructInPosition r inner JOIN r.inboundInstruct ii" +
            " where  ii.INV_ORG_ID=:INV_ORG_ID and ii.BILL_TYPE=:BILL_TYPE and  ii.BILL_NO=:BILL_NO and ii.INV_CODE=:INV_CODE and ii.MO_NAME=:MO_NAME")
    List<WmsInstructInPosition> getWmsInstructInPosition(@Param("INV_ORG_ID") String INV_ORG_ID,
                                                         @Param("BILL_TYPE") String BILL_TYPE,
                                                         @Param("BILL_NO") String BILL_NO,
                                                         @Param("INV_CODE") String INV_CODE, @Param("MO_NAME") String MO_NAME);
    @Query(" select r from WmsInstructInPosition r inner JOIN r.inboundInstruct ii" +
            " where  ii.INV_ORG_ID=:INV_ORG_ID and ii.BILL_TYPE=:BILL_TYPE and  ii.MES_ID=:MES_ID")
    List<WmsInstructInPosition> getWmsInstructInPosition(@Param("INV_ORG_ID") String INV_ORG_ID, @Param("BILL_TYPE") String BILL_TYPE, @Param("MES_ID") String MES_ID);
}
