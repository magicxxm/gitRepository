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
public interface OutboundInstructRepository extends JpaRepository<OutboundInstruct, String> {
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
            "  WMS_OUTBOUND_INSTRUCT t \n" +
            "WHERE t.INV_ORG_ID = :INV_ORG_ID \n" +
            "  AND t.BILL_NO = :BILL_NO \n" +
            "  AND t.LABEL_NO = :LABEL_NO \n" +
            "  AND t.MO_NAME = :MO_NAME \n" +
            "  AND t.INV_CODE = :INV_CODE \n", nativeQuery = true)
    Integer countOutboundInstruct(@Param("INV_ORG_ID") String INV_ORG_ID,
                                 @Param("BILL_NO") String BILL_NO,
                                 @Param("LABEL_NO") String LABEL_NO,
                                 @Param("INV_CODE") String INV_CODE,
                                 @Param("MO_NAME") String MO_NAME
                                  );

    default Integer countOutboundInstruct(OutboundInstruct inboundInstruct){
        if(inboundInstruct == null){
            return 0;
        }
        return countOutboundInstruct(inboundInstruct.getINV_ORG_ID(), inboundInstruct.getBILL_NO(), inboundInstruct.getLABEL_NO(), inboundInstruct.getINV_CODE(), inboundInstruct.getMO_NAME());
    }

    @Query("select ii from  OutboundInstruct ii where ii.id=:instrsuctId and  ii.id in (select distinct ip.outboundInstruct.id from WmsInstructOutPosition ip where ip.STATUS   in :instrsuctStatus )")
    List<OutboundInstruct> getInstru(@Param("instrsuctStatus") List<String> instrsuctStatus ,@Param("instrsuctId") String instrsuctId);

    @Query("select ii from  OutboundInstruct ii where  ii.id not in (select distinct  rr.outboundInstruct.id from WmsInstructOutPosition rr where rr.STATUS  in :instrsuctStatus  ) and ii.id not in (select  coalesce(t.instruct,'')  " +
            " from Trip t where t.tripState<>'Finish') and (ii.LINE_CODE=:station or ii.WORKCENTER_CODE=:station) order by  ii.DATE_REQ ASC ")
    List<OutboundInstruct> getAllNotCreateTripInstru(@Param("instrsuctStatus") List<String> instrsuctStatus,@Param("station") String station);

    @Query("select ii from  OutboundInstruct ii where ii.id  in (select  t.instruct  from Trip t where t.id=:tripId) ")
    OutboundInstruct getInstruByTripId(@Param("tripId") String tripId);


    @Query("select ii from  OutboundInstruct ii where ii.id=:instructId")
    OutboundInstruct getInstruByInstructId(@Param("instructId") String instructId);

    @Query("select ii from  OutboundInstruct ii where ii.INV_ORG_ID=:INV_ORG_ID and ii.BILL_TYPE=:BILL_TYPE and  ii.BILL_NO=:BILL_NO and ii.INV_CODE=:INV_CODE and ii.MO_NAME=:MO_NAME")
    OutboundInstruct getInstruByCondition(@Param("INV_ORG_ID") String INV_ORG_ID,
                                          @Param("BILL_TYPE") String BILL_TYPE,
                                          @Param("BILL_NO") String BILL_NO,
                                          @Param("INV_CODE") String INV_CODE, @Param("MO_NAME") String MO_NAME);

    @Query("select ii from  OutboundInstruct ii where ii.MES_ID=:MES_ID")
    OutboundInstruct getInstruById(@Param("MES_ID") String MES_ID);




}
