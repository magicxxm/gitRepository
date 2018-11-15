package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ReceiptRequest;
import wms.domain.ReceiptRequestPosition;

import java.util.List;

/**
 * Created by 123 on 2017/8/31.
 */
public interface ReceiptRequestPositionRepository extends BaseRepository<ReceiptRequestPosition,String> {

    @Query("select r from ReceiptRequestPosition r where r.receipt=:request")
    List<ReceiptRequestPosition> getByReceipt(@Param("request") ReceiptRequest request);
}
