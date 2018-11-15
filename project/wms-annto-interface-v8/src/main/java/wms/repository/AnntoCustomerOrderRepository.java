package wms.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.AnntoCustomerOrder;

public interface AnntoCustomerOrderRepository extends BaseRepository<AnntoCustomerOrder, String> {

    @Query("select a from AnntoCustomerOrder a where a.code = :code")
    AnntoCustomerOrder getByCode(@Param("code")String code);

}
