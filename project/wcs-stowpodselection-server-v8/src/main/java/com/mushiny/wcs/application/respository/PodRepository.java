package com.mushiny.wcs.application.respository;


import com.mushiny.wcs.application.domain.Pod;
import com.mushiny.wcs.application.domain.StorageLocationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PodRepository extends JpaRepository<Pod, String> {

    @Query(" select p from Pod p " +
            " where p.entityLock = 0  and p.state = 'Available' and p.placeMark<>0")
    List<Pod> getAllByPod();
    @Query(" select p from Pod p " +
            " where p.entityLock = 0 " +
            " and p.id in ( select distinct sl.podId from StorageLocation sl " +
            "   left join sl.storageLocationType st " +
            "   where st in :binTypeList)" +
            " and p.state = 'Available' and p.placeMark<>0" +
            " and p.sectionId = :sectionId")
    List<Pod> getPodByBinTypeList(@Param("binTypeList") List<StorageLocationType> binTypeList,
                                  @Param("sectionId") String sectionId);

    @Query(" select coalesce(sum (st.maxItemDataAmount),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st in :binTypeList ")
    int sumPodMaxItemsAmount(@Param("podId") String podId,
                             @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select coalesce(sum (st.maxItemDataAmount),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st=:binType ")
    int sumPodMaxItemsAmount2(@Param("podId") String podId,
                             @Param("binType") StorageLocationType binType);

    @Query(" select count (distinct sl.id)  from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st=:binType ")
    int countPodBinTypeAmount(@Param("podId") String podId,
                              @Param("binType") StorageLocationType binType);

    @Query(" select count (distinct i.id) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and s.amount > 0 " +
            " and st in :binTypeList ")
    int countPodItemsAmount(@Param("podId") String podId,
                            @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select count (distinct i.id) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and s.amount > 0 " +
            " and st =:binType ")
    int countPodItemsAmount2(@Param("podId") String podId,
                            @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (st.volume),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st in :binTypeList ")
    BigDecimal sumPodAllBinVolume(@Param("podId") String podId,
                                  @Param("binTypeList") List<StorageLocationType> binTypeList);
    @Query(" select coalesce(sum (st.liftingCapacity),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodAllBinWeight(@Param("podId") String podId,
                                  @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (st.volume),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and st.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodAllBinVolume2(@Param("podId") String podId,
                                   @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (s.amount * i.volume),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and s.amount > 0 " +
            " and i.volume > 0 " +
            " and st in :binTypeList ")
    BigDecimal sumPodItemVolume(@Param("podId") String podId,
                                @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select coalesce(sum (s.amount * i.volume),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and s.amount > 0 " +
            " and i.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodItemVolume2(@Param("podId") String podId,
                                @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (s.amount * i.weight),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and s.amount > 0 " +
            " and i.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodItemWeight(@Param("podId") String podId,
                                 @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (st.volume),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and st.volume > 0 " +
            " and st in :binTypeList ")
    BigDecimal sumPodFacePodAllBinVolume(@Param("podId") String podId,
                                         @Param("face") String face,
                                         @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select coalesce(sum (st.volume),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and st.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodFacePodAllBinVolume(@Param("podId") String podId,
                                         @Param("face") String face,
                                         @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (st.liftingCapacity),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and st.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodFacePodAllBinWeight(@Param("podId") String podId,
                                         @Param("face") String face,
                                         @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (s.amount * i.volume),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and s.amount > 0 " +
            " and i.volume > 0 " +
            " and st in :binTypeList ")
    BigDecimal sumPodFaceItemVolume(@Param("podId") String podId,
                                    @Param("face") String face,
                                    @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select coalesce(sum (s.amount * i.weight),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and s.amount > 0 " +
            " and i.weight > 0 " +
            " and st =:binType ")
    BigDecimal sumPodFaceItemWeight(@Param("podId") String podId,
                                    @Param("face") String face,
                                    @Param("binType") StorageLocationType binType);

    @Query(" select coalesce(sum (s.amount * i.volume),0) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face" +
            " and s.amount > 0 " +
            " and i.volume > 0 " +
            " and st =:binType ")
    BigDecimal sumPodFaceItemVolume(@Param("podId") String podId,
                                    @Param("face") String face,
                                    @Param("binType") StorageLocationType binType);


    @Query(" select coalesce(sum (st.maxItemDataAmount),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face " +
            " and st.volume > 0 " +
            " and st in :binTypeList ")
    int sumPodFaceMaxItemsAmount(@Param("podId") String podId,
                                 @Param("face") String face,
                                 @Param("binTypeList") List<StorageLocationType> binTypeList);


    @Query(" select coalesce(sum (st.maxItemDataAmount),0) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face " +
            " and st.volume > 0 " +
            " and st in :binType ")
    int sumPodFaceMaxItemsAmount(@Param("podId") String podId,
                                 @Param("face") String face,
                                 @Param("binType") StorageLocationType binType);


    @Query(" select count(sl.id) from StorageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face " +
            " and st.volume > 0 " +
            " and st =:binType ")
    int countPodFaceBinAmount(@Param("podId") String podId,
                                 @Param("face") String face,
                                 @Param("binType") StorageLocationType binType);

    @Query(" select count (distinct i.id) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face " +
            " and s.amount > 0 " +
            " and st in :binTypeList ")
    int countPodFaceItemsAmount(@Param("podId") String podId,
                                @Param("face") String face,
                                @Param("binTypeList") List<StorageLocationType> binTypeList);

    @Query(" select count (distinct i.id) from StockUnit s " +
            " left join s.itemData i " +
            " left join s.unitLoad u " +
            " left join u.storageLocation sl " +
            " left join sl.storageLocationType st " +
            " where sl.podId = :podId " +
            " and sl.face = :face " +
            " and s.amount > 0 " +
            " and st =:binType ")
    int countPodFaceItemsAmount(@Param("podId") String podId,
                                @Param("face") String face,
                                @Param("binType") StorageLocationType binType);


}
