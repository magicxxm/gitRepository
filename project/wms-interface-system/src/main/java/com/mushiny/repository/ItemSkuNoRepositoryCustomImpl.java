package com.mushiny.repository;

import com.mushiny.model.ItemSkuNo;
import org.springframework.data.jpa.repository.JpaContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Created by 123 on 2018/2/28.
 */
public class ItemSkuNoRepositoryCustomImpl implements ItemSkuNoRepositoryCustom {
    private final EntityManager entityManager;

    public ItemSkuNoRepositoryCustomImpl(JpaContext context){
        this.entityManager = context.getEntityManagerByManagedType(ItemSkuNo.class);
    }

    @Override
    public int deleteByItemNo(String itemNo) {
        Query query = entityManager.createNativeQuery("DELETE FROM MD_ITEMDATA_SKUNO WHERE ITEM_NO = '"+ itemNo +"'");

        return query.executeUpdate();
    }
}
