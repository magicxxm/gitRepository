package wms.repository.common;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.StorageLocation;

import java.util.List;

public interface StorageLocationRepository extends BaseRepository<StorageLocation, String> {

    @Modifying
    @Query(" update StorageLocation set orderIndex = orderIndex + :addIndex " +
            " where orderIndex >= :binIndex and warehouseId = :warehouse ")
    void updateCreateBinOrderIndex(@Param("addIndex") int addIndex,
                                   @Param("binIndex") int binIndex,
                                   @Param("warehouse") String warehouse);

    @Modifying
    @Query(" update StorageLocation set orderIndex = orderIndex - :subIndex " +
            " where orderIndex > :binIndex and warehouseId = :warehouse ")
    void updateDeleteBinOrderIndex(@Param("subIndex") int subIndex,
                                   @Param("binIndex") int binIndex,
                                   @Param("warehouse") String warehouse);

    @Query(" select s from StorageLocation s " +
            " where s.warehouseId = :warehouse and s.name =:name")
    StorageLocation getByName(@Param("warehouse") String warehouse,
                              @Param("name") String name);

}
