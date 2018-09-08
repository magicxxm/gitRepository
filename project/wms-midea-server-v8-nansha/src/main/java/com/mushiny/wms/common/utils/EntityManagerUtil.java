package com.mushiny.wms.common.utils;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/8/20.
 */
@Component
public class EntityManagerUtil {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }


    public List executeQuery(String sql, Map<String, Object> params, Class type) {

        Query query = entityManager.createQuery(sql, type);
        setParams(query, params);
        List temp = query.getResultList();

        return temp;
    }



    public int executeUpdate(String sql, Map<String, Object> params) {
        Query query = entityManager.createQuery(sql);
        setParams(query, params);
        int result = query.executeUpdate();

        return result;
    }

    public void executeUpdate(Object bean) {
        entityManager.merge(bean);


    }

    public <T> T executeMerge(T bean) {
        T result = entityManager.merge(bean);
        return result;

    }


    public List executeNativeQuery(String sql, Map<String, Object> params) {

        Query query = entityManager.createNativeQuery(sql);

        setParams(query, params);

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List temp = query.getResultList();

        return temp;
    }

    public void persist(Object param) {
        entityManager.persist(param);


    }

    public <T> T merge(T param) {
        return entityManager.merge(param);


    }


    public List executeCondotionNativeQuery(String sql, Map<String, Object> params, Class type) {

        Query query = entityManager.createNativeQuery(sql);

        setParams(query, params);

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(type));

        List temp = query.getResultList();

        return temp;
    }

    public List<Map> executeNativeQuery2(String sql, Map<String, Object> params) {

        Query query = entityManager.createNativeQuery(sql);

        setParams(query, params);

        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map> temp = query.getResultList();

        return temp;
    }

    public int executeCondotionNativeUpdate(String sql, Map<String, Object> params) {
        int result = -1;
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        result = query.executeUpdate();

        return result;
    }

    private void setParams(Query query, Map<String, Object> params) {

        if (!CollectionUtils.isEmpty(params)) {

            Set<Map.Entry<String, Object>> paramsTemp = params.entrySet();
            for (Map.Entry<String, Object> temp : paramsTemp) {
                if (!ObjectUtils.isEmpty(temp.getValue())) {
                    query.setParameter(temp.getKey(), temp.getValue());
                }

            }
        }
    }

}
