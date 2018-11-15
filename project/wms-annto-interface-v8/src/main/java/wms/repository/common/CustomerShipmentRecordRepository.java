package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.CustomerShipmentRecord;

import java.util.List;

/**
 * Created by 123 on 2017/5/22.
 */
public interface CustomerShipmentRecordRepository extends BaseRepository<CustomerShipmentRecord,String> {

    @Query("select csr from CustomerShipmentRecord csr where csr.shipmentId=:shipmentid and csr.state=:state")
    CustomerShipmentRecord getByShipmentId(@Param("shipmentid") String shipmentid, @Param("state") int state);


    @Query("select csr from CustomerShipmentRecord csr where csr.shipmentId=:shipmentid order by csr.createdDate")
    List<CustomerShipmentRecord> getByShipmentId(@Param("shipmentid") String shipmentid);
}
