package com.mushiny.auth.repository.impl;

import com.mushiny.auth.domain.*;
import com.mushiny.auth.repository.UserRepositoryCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getByUsername(String username) {
        if (username == null) {
            throw new NullPointerException("getByUsername: parameter == null");
        }

        Query query = entityManager.createQuery("SELECT u FROM "
                + User.class.getSimpleName()
                + " u "
                + "WHERE u.username = :username");

        query.setParameter("username", username);

        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User getByUsernameAndPassword(String username, String password) {
        if (username == null) {
            throw new NullPointerException("getByUsername: parameter == null");
        }

        Query query = entityManager.createQuery("SELECT u FROM "
                + User.class.getSimpleName()
                + " u "
                + "WHERE u.username = :username "
                + "AND u.password = :password");

        query.setParameter("username", username);
        query.setParameter("password", password);

        try {
            User user = (User) query.getSingleResult();
            return user;
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<Authority> getAuthoritiesByUserId(String warehouseId, String userId) {
        if (userId == null) {
            throw new NullPointerException("getAuthoritiesByUserId: parameter == null");
        }

        StringBuffer sb = new StringBuffer();

        sb.append("SELECT DISTINCT NEW ");
        sb.append(Authority.class.getName());
        sb.append("(m.name ");
        sb.append(") FROM ");
        sb.append(User.class.getSimpleName() + " u, ");
        sb.append(Role.class.getSimpleName() + " r, ");
        sb.append(Module.class.getSimpleName() + " m, ");
        sb.append(RoleModule.class.getSimpleName() + " rm, ");
        sb.append(UserWarehouseRole.class.getSimpleName() + " uwr ");
        sb.append("WHERE uwr.userId = u.id AND uwr.roleId = r.id ");
        sb.append("AND rm.roleId = r.id AND rm.moduleId = m.id ");
        sb.append("AND uwr.warehouseId = :warehouseId AND uwr.userId = :userId AND m.lock = 20000 ");

        sb.append("ORDER BY m.name ");

        Query query = entityManager.createQuery(sb.toString());
        query.setParameter("warehouseId", warehouseId);
        query.setParameter("userId", userId);

        List<Authority> ret = query.getResultList();
        return ret;
    }
}
