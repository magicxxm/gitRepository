package test.com.mushiny.wms.application.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.com.mushiny.wms.application.test.domain.OutboundInstruct;

import java.util.List;

/**
 * Created by Administrator on 2018/7/6.
 */
public interface OutboundInstructRepository extends JpaRepository<OutboundInstruct, String> {
    /**
     * 查询某个入库单是否已经存在
     * @param INV_ORG_ID
     * @param BILL_TYPE
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
            "  AND t.BILL_TYPE = :BILL_TYPE \n" +
            "  AND t.BILL_NO = :BILL_NO \n" +
            "  AND t.LABEL_NO = :LABEL_NO \n" +
            "  AND t.INV_CODE = :INV_CODE \n", nativeQuery = true)
    Integer countOutboundInstruct(@Param("INV_ORG_ID") String INV_ORG_ID,
                                  @Param("BILL_TYPE") String BILL_TYPE,
                                  @Param("BILL_NO") String BILL_NO,
                                  @Param("LABEL_NO") String LABEL_NO,
                                  @Param("INV_CODE") String INV_CODE);

    default Integer countOutboundInstruct(OutboundInstruct inboundInstruct){
        if(inboundInstruct == null){
            return 0;
        }
        return countOutboundInstruct(inboundInstruct.getINV_ORG_ID(), inboundInstruct.getBILL_TYPE(), inboundInstruct.getBILL_NO(), inboundInstruct.getLABEL_NO(), inboundInstruct.getINV_CODE());
    }


    @Query("select t from  OutboundInstruct t where  t.STATUS = 'CREATED'")
    List<OutboundInstruct> getCreated();




}
