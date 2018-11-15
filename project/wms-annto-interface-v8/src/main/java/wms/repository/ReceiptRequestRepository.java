package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.ReceiptRequest;

/**
 * Created by 123 on 2017/8/31.
 */
public interface ReceiptRequestRepository extends BaseRepository<ReceiptRequest,String> {

    @Query("select r from ReceiptRequest r where r.receiptNo=:receiptNo")
    ReceiptRequest findByReceiptNo(@Param("receiptNo")String receiptNo);
}
