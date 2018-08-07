package com.mushiny.common.repository.rsql;

import com.github.tennaito.rsql.builder.BuilderTools;
import com.github.tennaito.rsql.jpa.PredicateBuilderStrategy;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.Node;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class CustomizedPredicateBuilderStrategy implements PredicateBuilderStrategy {


    @SuppressWarnings("unchecked")
    @Override
    public <T> Predicate createPredicate(Node node, From root, Class<T> entity, EntityManager manager, BuilderTools tools) throws IllegalArgumentException {
        ComparisonNode cn = (ComparisonNode) node;
        String operator = cn.getOperator().getSymbol();
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        switch (operator) {
            case "=dg=": {
                Path path = root.get(cn.getSelector());
                if (path.getJavaType().isAssignableFrom(LocalDate.class)) {
                    return builder.and(builder.greaterThanOrEqualTo(path, LocalDate.parse(cn.getArguments().get(0))));
                } else if (path.getJavaType().isAssignableFrom(ZonedDateTime.class)) {
                    return builder.and(builder.greaterThanOrEqualTo(path, ZonedDateTime.parse(cn.getArguments().get(0))));
                } else {
                    return builder.and(builder.greaterThanOrEqualTo(path, LocalDate.parse(cn.getArguments().get(0))));
                }
            }
            case "=dl=": {
                Path path = root.get(cn.getSelector());
                if (path.getJavaType().isAssignableFrom(LocalDate.class)) {
                    return builder.and(builder.lessThanOrEqualTo(path, LocalDate.parse(cn.getArguments().get(0))));
                } else if (path.getJavaType().isAssignableFrom(ZonedDateTime.class)) {
                    return builder.and(builder.lessThanOrEqualTo(path, ZonedDateTime.parse(cn.getArguments().get(0))));
                } else {
                    return builder.and(builder.lessThanOrEqualTo(path, LocalDate.parse(cn.getArguments().get(0))));
                }
            }
            default:
                throw new IllegalArgumentException("Unknown operator: " + cn.getOperator());
        }

    }
}
