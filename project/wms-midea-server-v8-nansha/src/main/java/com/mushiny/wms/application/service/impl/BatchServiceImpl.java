package com.mushiny.wms.application.service.impl;

import com.mushiny.wms.application.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Administrator on 2018/7/11.
 */
@Service
public class BatchServiceImpl implements BatchService {

    @PersistenceContext

    protected EntityManager em;
   @Transactional
    @Override
    public void batchInsert(List list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if (i % 100 == 0) {

                em.flush();

                em.clear();

            }

        }
    }


    @Transactional
    @Override
    public void batchUpdate(List list) {
        for (int i = 0; i < list.size(); i++) {

            em.merge(list.get(i));

            if (i % 1000 == 0) {

                em.flush();

                em.clear();

            }

        }
    }
}
