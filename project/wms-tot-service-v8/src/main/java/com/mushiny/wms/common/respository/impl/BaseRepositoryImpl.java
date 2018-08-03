package com.mushiny.wms.common.respository.impl;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.mushiny.wms.common.Constant;
import com.mushiny.wms.common.context.ApplicationBeanContextAware;
import com.mushiny.wms.common.context.ApplicationContext;
import com.mushiny.wms.common.entity.BaseClientAssignedEntity;
import com.mushiny.wms.common.entity.BaseEntity;
import com.mushiny.wms.common.entity.BaseWarehouseAssignedEntity;
import com.mushiny.wms.common.exception.ApiException;
import com.mushiny.wms.common.exception.ExceptionEnum;
import com.mushiny.wms.common.respository.BaseRepository;
import com.mushiny.wms.common.respository.rsql.CustomizedArgumentParser;
import com.mushiny.wms.common.respository.rsql.CustomizedJpaPredicateVisitor;
import com.mushiny.wms.common.respository.rsql.CustomizedPredicateBuilderStrategy;
import com.mushiny.wms.tot.general.domain.Client;
import com.mushiny.wms.tot.general.domain.Warehouse;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.*;

@SuppressWarnings("unchecked")
public class BaseRepositoryImpl<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {

    private final Class<T> entityClass;

    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    @Override
    public T retrieve(String id) {
        ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
        String queryBuilder = "SELECT e FROM " +
                entityClass.getSimpleName() +
                " e " +
                " WHERE e.id = :id ";
        TypedQuery<T> query = entityManager.createQuery(queryBuilder, entityClass);
        query.setParameter("id", id);
        T entity;
        try {
            entity = query.getSingleResult();
        } catch (Throwable e) {
            throw new ApiException(ExceptionEnum.EX_OBJECT_NOT_FOUND.toString());
        }
        if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
            BaseClientAssignedEntity baseEntity = (BaseClientAssignedEntity) entity;
            applicationContext.isCurrentWarehouse(baseEntity.getWarehouseId());
            if (!applicationContext.isSystemClient(applicationContext.getCurrentClient())) {
                applicationContext.isCurrentClient(baseEntity.getClientId());
            }
        } else if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass)) {
            BaseWarehouseAssignedEntity baseEntity = (BaseWarehouseAssignedEntity) entity;
            applicationContext.isCurrentWarehouse(baseEntity.getWarehouseId());
        }
        return entity;
    }

    @Override
    public List<T> getList(String clientId, Sort sort) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("SELECT e FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");
        queryBuilder.append("WHERE 1 = 1 ");
        appendWarehouseAndClient(queryBuilder);
        appendSort(queryBuilder, sort);
        TypedQuery<T> query = entityManager.createQuery(queryBuilder.toString(), entityClass);
        setParameter(query, clientId);
        return query.getResultList();
    }

    @Override
    public List<T> getNotLockList(String clientId, Sort sort) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder
                .append("SELECT e FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");
        queryBuilder.append("WHERE 1 = 1 ");
        queryBuilder.append(" AND entityLock = :entityLock ");
        appendWarehouseAndClient(queryBuilder);
        appendSort(queryBuilder, sort);
        TypedQuery<T> query = entityManager.createQuery(queryBuilder.toString(), entityClass);
        query.setParameter("entityLock", Constant.NOT_LOCKED);
        setParameter(query, clientId);
        return query.getResultList();
    }

    @Override
    public List<T> getBySearchTerm(String searchTerm) {
        TypedQuery query = getTypedQueryBySearchTerm(searchTerm, null);
        return query.getResultList();
    }

    @Override
    public List<T> getBySearchTerm(String searchTerm, Sort sort) {
        TypedQuery query = getTypedQueryBySearchTerm(searchTerm, sort);
        return query.getResultList();
    }

    @Override
    public Page<T> getBySearchTerm(String searchTerm, Pageable pageable) {
        TypedQuery query = getTypedQueryBySearchTerm(searchTerm, pageable.getSort());
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, countBySearchTerm(searchTerm));
    }

    @Override
    public Long countBySearchTerm(String searchTerm) {
        if (searchTerm == null || searchTerm.equals("")) {
            String queryBuilder = "SELECT count (e.id) FROM " +
                    entityClass.getSimpleName() +
                    " e ";
            TypedQuery<Long> query = entityManager.createQuery(queryBuilder, Long.class);
            return query.getSingleResult();
        } else {
            // Create criteria and from
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery criteria = builder.createQuery(Long.class);
            From root = criteria.from(entityClass);
            criteria.select(builder.count(root));
            builderCriteria(root, criteria, searchTerm);
            TypedQuery<Long> query = entityManager.createQuery(criteria);
            return query.getSingleResult();
        }
    }

    private TypedQuery<T> getTypedQueryBySearchTerm(String searchTerm, Sort sort) {
        // Create criteria and from
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(entityClass);
        From root = criteria.from(entityClass);
        builderCriteria(root, criteria, searchTerm);
        if(sort == null && searchTerm != null && !searchTerm.contains("warehouseId")) {
            //首次显示页面按首列表头是 code 的自动排序，默认code 故不作判断
            if(searchTerm.contains("jobType")) sort = new Sort(new Sort.Order(Sort.Direction.ASC, "code"));
            //首次显示页面按首列表头不是 code 的自动排序，用searchTerm是否含有jobType区分
            else sort = new Sort(new Sort.Order(Sort.Direction.ASC, "operation"));
        }
        if(sort != null){
            for (Sort.Order order : sort) {
                Path orderPath = null;
                String property = order.getProperty();
                if ("categoryName".equals(property) || "categoryDTO.name".equals(property)) property = "category";
                String[] orderArray = property.split("\\.");
                List<String> orderList = new ArrayList<>();
                Collections.addAll(orderList, orderArray);
                for (int i = 0; i < orderList.size(); i++) {
                    if (i == 0) {
                        orderPath = root.get(orderList.get(i));
                    } else {
                        orderPath = orderPath.get(orderList.get(i));
                    }
                }
                if (order.getDirection().equals(Sort.Direction.DESC)) {
                    criteria.orderBy(builder.desc(orderPath));
                } else {
                    criteria.orderBy(builder.asc(orderPath));
                }
            }
        }
        return entityManager.createQuery(criteria);
    }

    private void builderCriteria(From root, CriteriaQuery criteria, String searchTerm) {
        ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
        StringBuilder defaultBuilder = new StringBuilder();
        if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
            defaultBuilder
                    .append("warehouseId==")
                    .append(applicationContext.getCurrentWarehouse());
            String clientId = applicationContext.getCurrentClient();
            if (!applicationContext.isSystemClient(clientId)) {
                defaultBuilder
                        .append(";clientId==")
                        .append(clientId);
            }
        } else if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass)) {
            defaultBuilder
                    .append("warehouseId==")
                    .append(applicationContext.getCurrentWarehouse());
        }
        if (defaultBuilder.length() > 0 && searchTerm != null && !searchTerm.equals("")) {
            defaultBuilder
                    .append(";");
        }
        if (searchTerm != null && !searchTerm.equals("")) {
            defaultBuilder.append(searchTerm);
        }
        if (defaultBuilder.length() > 0) {
            searchTerm = defaultBuilder.toString();
            // Create the JPA Predicate Visitor
//            RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<T>().defineRoot(root);
            RSQLVisitor<Predicate, EntityManager> visitor = new CustomizedJpaPredicateVisitor<T>()
                    .withRoot(root)
                    .withArgumentParser(new CustomizedArgumentParser())
                    .withPredicateBuilderStrategy(new CustomizedPredicateBuilderStrategy());
            Set<ComparisonOperator> operators = RSQLOperators.defaultOperators();
            operators.add(new ComparisonOperator("=dg=", true));
            operators.add(new ComparisonOperator("=dl=", true));
            // Parse a RSQL into a Node
            Node rootNode = new RSQLParser(operators).parse(searchTerm);
            // Visit the node to retrieve CriteriaQuery
            Predicate predicate = rootNode.accept(visitor, entityManager);
            // Use generated predicate as you like
            criteria.where(predicate);
        }
    }

    private void appendWarehouseAndClient(StringBuilder builder) {
        if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
            builder.append(" AND e.warehouseId = :warehouseId ");
            builder.append(" AND e.clientId = :clientId ");
        } else if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass)) {
            builder.append(" AND e.warehouseId = :warehouseId ");
        }
    }

    private void appendSort(StringBuilder builder, Sort sort) {
        if (sort != null) {
            boolean orderByUsed = false;
            for (Sort.Order order : sort) {
                if (!orderByUsed) {
                    builder.append("ORDER BY ");
                    orderByUsed = true;
                } else {
                    builder.append(", ");
                }
                builder.append("e.");
                builder.append(order.getProperty());
                builder.append(" ");
                builder.append(order.getDirection().name());
            }
        }
    }

    private void setParameter(TypedQuery<T> query, String clientId) {
        ApplicationContext applicationContext = ApplicationBeanContextAware.getBean(ApplicationContext.class);
        if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
            query.setParameter("warehouseId", applicationContext.getCurrentWarehouse());
            String currentClient = applicationContext.getCurrentClient();
            if (!applicationContext.isSystemClient(currentClient)) {
                clientId = currentClient;
            }
            query.setParameter("clientId", clientId);
        } else if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass)) {
            query.setParameter("warehouseId", applicationContext.getCurrentWarehouse());
        }
    }

    @Override
    public List<T> getList(Warehouse warehouse, Client client) {
        StringBuffer sb = new StringBuffer();
        sb
                .append("SELECT e FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");

        sb.append("WHERE 1 = 1 ");

        if (warehouse != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.warehouse = :warehouse ");
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

        if (client != null && !client.isSystemClient()) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.client = :client ");
            } else if (Client.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

        sb
                .append("ORDER BY e.createdDate DESC, e.id DESC");

        TypedQuery query = entityManager.createQuery(sb.toString(), entityClass);

        if (warehouse != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("warehouse", warehouse);
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", warehouse.getId());
            }
        }

        if (client != null && !client.isSystemClient()) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("client", client);
            } else if (Client.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", client.getId());
            }
        }

        return (List<T>) query.getResultList();
    }

    @Override
    public List<T> getList(String warehouseId, String clientId, Sort sort) {
        StringBuffer sb = new StringBuffer();
        sb
                .append("SELECT e FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");

        sb.append("WHERE 1 = 1 ");

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.warehouseId = :warehouseId ");
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

//        if (clientId != null && !clientId.isSystemClient()) {
        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.clientId = :clientId ");
            } else if (Client.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

        if (sort != null) {
            boolean orderByUsed = false;
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                if (!orderByUsed) {
                    sb.append("ORDER BY ");
                    orderByUsed = true;
                } else {
                    sb.append(", ");
                }
                Sort.Order order = iterator.next();
                sb.append(order.getProperty());
                sb.append(" ");
                sb.append(order.getDirection().name());
            }
        }

        TypedQuery query = entityManager.createQuery(sb.toString(), entityClass);

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("warehouseId", warehouseId);
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", warehouseId);
            }
        }

//        if (clientId != null && !clientId.isSystemClient()) {
        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("clientId", clientId);
            } else if (Client.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", clientId);
            }
        }

        return (List<T>) query.getResultList();
    }

    @Override
    public Page<T> getList(String warehouseId, String clientId, Pageable pageable) {
        StringBuffer sb = new StringBuffer();
        sb
                .append("SELECT e FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");

        sb.append("WHERE 1 = 1 ");

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.warehouseId = :warehouseId ");
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.clientId = :clientId ");
            } else if (Client.class.isAssignableFrom(entityClass)) {
                sb.append("AND e.id = :id ");
            }
        }

        Sort sort = pageable.getSort();
        if (sort != null) {
            boolean orderByUsed = false;
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                if (!orderByUsed) {
                    sb.append("ORDER BY ");
                    orderByUsed = true;
                } else {
                    sb.append(", ");
                }
                Sort.Order order = iterator.next();
                sb.append(order.getProperty());
                sb.append(" ");
                sb.append(order.getDirection().name());
            }
        }

        TypedQuery query = entityManager.createQuery(sb.toString(), entityClass);

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("warehouseId", warehouseId);
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", warehouseId);
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("clientId", clientId);
            } else if (Client.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", clientId);
            }
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>((List<T>) query.getResultList(), pageable, count(warehouseId, clientId));
    }

    @Override
    public List<T> getListBySearchTerm(Warehouse warehouse, Client client, String searchTerm) {
//        Node rootNode = new RSQLParser().parse(searchTerm);
//        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
//
//        if (warehouse != null) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("warehouse.id", SearchOperation.EQUALITY, warehouse.getId())));
//            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
////                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, warehouse.getId())));
//            }
//        }
//
//        if (client != null && !client.isSystemClient()) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("client.id", SearchOperation.EQUALITY, client.getId())));
//            } else if (Client.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, client.getId())));
//            }
//        }
//
//        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate"), new Sort.Order(Sort.Direction.DESC, "id"));
//
//        return super.findAll(spec, sort);

        // Create criteria and from
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(entityClass);
        From root = criteria.from(entityClass);

        // Create the JPA Predicate Visitor
        RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<T>().defineRoot(root);

        // Parse a RSQL into a Node
        Node rootNode = new RSQLParser().parse(searchTerm);

        // Visit the node to retrieve CriteriaQuery
        Predicate predicate = rootNode.accept(visitor, entityManager);

        // Use generated predicate as you like
        criteria.where(predicate);

        if (warehouse != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("warehouse"), warehouse));
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), client.getId()));
            }
        }

        if (client != null && !client.isSystemClient()) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("client"), client));
            } else if (Client.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), client.getId()));
            }
        }

        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "createdDate"), new Sort.Order(Sort.Direction.DESC, "id"));

        if (sort != null) {
            List<Order> orders = new ArrayList<>();
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                orders.add(new OrderImpl(root.get(order.getProperty()), order.isAscending()));
            }
            criteria.orderBy(orders);
        }

        TypedQuery query = entityManager.createQuery(criteria);

        return (List<T>) query.getResultList();
    }

    @Override
    public List<T> getListBySearchTerm(Warehouse warehouse, Client client, String searchTerm, Sort sort) {
//        Node rootNode = new RSQLParser().parse(searchTerm);
//        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
//
//        if (warehouse != null) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("warehouse.id", SearchOperation.EQUALITY, warehouse.getId())));
//            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
////                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, warehouse.getId())));
//            }
//        }
//
//        if (client != null && !client.isSystemClient()) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("client.id", SearchOperation.EQUALITY, client.getId())));
//            } else if (Client.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, client.getId())));
//            }
//        }
//
//        return super.findAll(spec, sort);

        // Create criteria and from
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(entityClass);
        From root = criteria.from(entityClass);

        // Create the JPA Predicate Visitor
        RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<T>().defineRoot(root);

        // Parse a RSQL into a Node
        Node rootNode = new RSQLParser().parse(searchTerm);

        // Visit the node to retrieve CriteriaQuery
        Predicate predicate = rootNode.accept(visitor, entityManager);

        // Use generated predicate as you like
        criteria.where(predicate);

        if (warehouse != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("warehouse"), warehouse));
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), client.getId()));
            }
        }

        if (client != null && !client.isSystemClient()) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("client"), client));
            } else if (Client.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), client.getId()));
            }
        }

        if (sort != null) {
            List<Order> orders = new ArrayList<>();
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                orders.add(new OrderImpl(root.get(order.getProperty()), order.isAscending()));
            }
            criteria.orderBy(orders);
        }

        TypedQuery query = entityManager.createQuery(criteria);

        return (List<T>) query.getResultList();
    }

    @Override
    public Page<T> getListBySearchTerm(String warehouseId, String clientId, String searchTerm, Pageable pageable) {
//        Node rootNode = new RSQLParser().parse(searchTerm);
//        Specification<T> spec = rootNode.accept(new CustomRsqlVisitor<T>());
//
//        if (warehouse != null) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("warehouse.id", SearchOperation.EQUALITY, warehouse.getId())));
//            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
////                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, warehouse.getId())));
//            }
//        }
//
//        if (client != null && !client.isSystemClient()) {
//            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("client.id", SearchOperation.EQUALITY, client.getId())));
//            } else if (Client.class.isAssignableFrom(entityClass)) {
//                spec = Specifications.where(spec).and(new SpecificationImpl(new SpecSearchCriteria("id", SearchOperation.EQUALITY, client.getId())));
//            }
//        }
//
//        return super.findAll(spec, pageable);

        // Create criteria and from
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(entityClass);
        From root = criteria.from(entityClass);

        // Create the JPA Predicate Visitor
        RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<T>().defineRoot(root);

        // Parse a RSQL into a Node
        Node rootNode = new RSQLParser().parse(searchTerm);

        // Visit the node to retrieve CriteriaQuery
        Predicate predicate = rootNode.accept(visitor, entityManager);

        // Use generated predicate as you like
        criteria.where(predicate);

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("warehouseId"), warehouseId));
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), warehouseId));
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("clientId"), clientId));
            } else if (Client.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), clientId));
            }
        }

        Sort sort = pageable.getSort();
        if (sort != null) {
            List<Order> orders = new ArrayList<>();
            Iterator<Sort.Order> iterator = sort.iterator();
            while (iterator.hasNext()) {
                Sort.Order order = iterator.next();
                orders.add(new OrderImpl(root.get(order.getProperty()), order.isAscending()));
            }
            criteria.orderBy(orders);
        }

        TypedQuery query = entityManager.createQuery(criteria);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        return new PageImpl<>((List<T>) query.getResultList(), pageable, count(warehouseId, clientId, searchTerm));
    }

    @Override
    public long count(String warehouseId, String clientId) {
        StringBuffer sbBuf = new StringBuffer();
        sbBuf
                .append("SELECT COUNT(e.id) FROM ")
                .append(entityClass.getSimpleName())
                .append(" e ");

        sbBuf.append("WHERE 1 = 1 ");

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sbBuf.append("AND e.warehouseId = :warehouseId ");
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                sbBuf.append("AND e.id = :id ");
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                sbBuf.append("AND e.clientId = :clientId ");
            } else if (Client.class.isAssignableFrom(entityClass)) {
                sbBuf.append("AND e.id = :id ");
            }
        }

        TypedQuery query = entityManager.createQuery(sbBuf.toString(), Long.class);

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("warehouseId", warehouseId);
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", warehouseId);
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                query.setParameter("clientId", clientId);
            } else if (Client.class.isAssignableFrom(entityClass)) {
                query.setParameter("id", clientId);
            }
        }

        return (long) query.getSingleResult();
    }

    @Override
    public long count(String warehouseId, String clientId, String searchTerm) {
        // Create criteria and from
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery(Long.class);
        From root = criteria.from(entityClass);
        criteria.select(builder.count(root));

        // Create the JPA Predicate Visitor
        RSQLVisitor<Predicate, EntityManager> visitor = new JpaPredicateVisitor<T>().defineRoot(root);

        // Parse a RSQL into a Node
        Node rootNode = new RSQLParser().parse(searchTerm);

        // Visit the node to retrieve CriteriaQuery
        Predicate predicate = rootNode.accept(visitor, entityManager);

        // Use generated predicate as you like
        criteria.where(predicate);

        if (warehouseId != null) {
            if (BaseWarehouseAssignedEntity.class.isAssignableFrom(entityClass) || BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("warehouseId"), warehouseId));
            } else if (Warehouse.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), warehouseId));
            }
        }

        if (clientId != null ) {
            if (BaseClientAssignedEntity.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("clientId"), clientId));
            } else if (Client.class.isAssignableFrom(entityClass)) {
                criteria.where(builder.equal(root.get("id"), clientId));
            }
        }

        TypedQuery query = entityManager.createQuery(criteria);

        return (long) query.getSingleResult();
    }

    @Override
    public boolean exists(ID id) {
//        if (entityInformation.getIdAttribute() != null) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(e.id) FROM" + entityClass.getSimpleName() + " e WHERE e.id = :id", Long.class);
        query.setParameter("id", id);
        return query.getSingleResult() == 1;
//        } else {
//            return findOne(id) != null;
//        }
    }
}
