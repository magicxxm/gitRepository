package com.mushiny.wms.application.repository;

import com.mushiny.wms.application.domain.InboundInstruct;
import com.mushiny.wms.application.domain.OutboundInstruct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface InboundInstructRepository extends JpaRepository<InboundInstruct, String> {
    /**
     * 查询某个入库单是否已经存在
     * @param INV_ORG_ID
     * @param BILL_NO
     * @param LABEL_NO
     * @param INV_CODE
     * @return 1 存在该入库单，无需插入；0 不存在该入库单，插入该入库单
     */
    @Query(value = "SELECT \n" +
            "  COUNT(t.ID) \n" +
            "FROM\n" +
            "  WMS_INBOUND_INSTRUCT t \n" +
            "WHERE t.INV_ORG_ID = :INV_ORG_ID \n" +
            "  AND t.BILL_NO = :BILL_NO \n" +
            "  AND t.LABEL_NO = :LABEL_NO \n" +
            "  AND t.MO_NAME = :MO_NAME \n" +
            "  AND t.INV_CODE = :INV_CODE \n", nativeQuery = true)
    Integer countInboundInstruct(@Param("INV_ORG_ID") String INV_ORG_ID,
                              @Param("BILL_NO") String BILL_NO,
                              @Param("LABEL_NO") String LABEL_NO,
                              @Param("INV_CODE") String INV_CODE,
                              @Param("MO_NAME") String MO_NAME
                                 );

    default Integer countInboundInstruct(InboundInstruct inboundInstruct){
        if(inboundInstruct == null){
            return 0;
        }
        return countInboundInstruct(inboundInstruct.getINV_ORG_ID(), inboundInstruct.getBILL_NO(), inboundInstruct.getLABEL_NO(), inboundInstruct.getINV_CODE(), inboundInstruct.getMO_NAME());
    }



    @Query("select ii from  InboundInstruct ii where ii.id not in (select distinct ip.inboundInstruct.id from WmsInstructInPosition ip where ip.STATUS   in :instrsuctStatus ) and ii.id not in (select  coalesce(t.instruct,'')   from Trip t  where t.tripState<>'Finish') order by  ii.DATE_REQ ASC ")
   List<InboundInstruct> getAllNotCreateTripInstru(@Param("instrsuctStatus") List<String> instrsuctStatus);

    @Query("select ii from  InboundInstruct ii where ii.id=:instrsuctId and  ii.id in (select distinct ip.inboundInstruct.id from WmsInstructInPosition ip where ip.STATUS   in :instrsuctStatus )")
    List<InboundInstruct> getInstru(@Param("instrsuctStatus") List<String> instrsuctStatus ,@Param("instrsuctId") String instrsuctId);

    @Query("select ii from  InboundInstruct ii where ii.id  in (select  t.instruct from Trip t where t.id=:tripId) ")
    InboundInstruct getInstruByTripId(@Param("tripId") String tripId);


    @Query("select ii from  InboundInstruct ii where ii.id=:instructId")
    InboundInstruct getInstruById(@Param("instructId") String instructId);

    @Query("select ii from  InboundInstruct ii where ii.INV_ORG_ID=:INV_ORG_ID and ii.BILL_TYPE=:BILL_TYPE and  ii.BILL_NO=:BILL_NO and ii.LABEL_NO=:LABEL_NO and ii.INV_CODE=:INV_CODE and ii.MO_NAME=:MO_NAME")
    InboundInstruct getInstruByCondition(@Param("INV_ORG_ID") String INV_ORG_ID,
                                          @Param("BILL_TYPE") String BILL_TYPE,
                                          @Param("BILL_NO") String BILL_NO,
                                          @Param("LABEL_NO") String LABEL_NO,
                                          @Param("INV_CODE") String INV_CODE, @Param("MO_NAME") String MO_NAME);


    @Query("select ii from  InboundInstruct ii where ii.MES_ID=:MES_ID")
    InboundInstruct getInstruByMesId(@Param("MES_ID") String MES_ID);

    @Query("select ii from  InboundInstruct ii where ii.INV_ORG_ID=:INV_ORG_ID and ii.BILL_TYPE=:BILL_TYPE and  ii.BILL_NO=:BILL_NO and ii.INV_CODE=:INV_CODE and ii.MO_NAME=:MO_NAME")
    InboundInstruct getInstruByCondition(@Param("INV_ORG_ID") String INV_ORG_ID,
                                         @Param("BILL_TYPE") String BILL_TYPE,
                                         @Param("BILL_NO") String BILL_NO,
                                         @Param("INV_CODE") String INV_CODE, @Param("MO_NAME") String MO_NAME);



    @Query(value = "update  WMS_INBOUND_INSTRUCT oi set oi.WORKCENTER_CODE=:WORKCENTER_CODE ,oi.CAR_NO=:CAR_NO, oi.LOCATION_NO=:LOCATION_NO,oi.DATETIME_STOCK=:DATETIME_STOCK,oi.STATUS=:STATUS where oi.INV_ORG_ID=:INV_ORG_ID and oi.BILL_TYPE=:BILL_TYPE and  oi.BILL_NO=:BILL_NO and oi.LABEL_NO=:LABEL_NO and oi.INV_CODE=:INV_CODE and oi.MO_NAME=:MO_NAME", nativeQuery = true)
    Integer updateInstruByCondition(@Param("INV_ORG_ID") String INV_ORG_ID,
                                    @Param("BILL_TYPE") String BILL_TYPE,
                                    @Param("BILL_NO") String BILL_NO,
                                    @Param("LABEL_NO") String LABEL_NO,
                                    @Param("INV_CODE") String INV_CODE, @Param("MO_NAME") String MO_NAME);

}
