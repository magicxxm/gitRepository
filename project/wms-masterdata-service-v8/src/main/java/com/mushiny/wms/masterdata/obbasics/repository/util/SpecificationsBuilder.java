package com.mushiny.wms.masterdata.obbasics.repository.util;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import java.util.ArrayList;
import java.util.List;

public class SpecificationsBuilder<E> {

    private final List<SpecSearchCriteria> params;

    public SpecificationsBuilder() {
        params = new ArrayList<SpecSearchCriteria>();
    }

    public final SpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                final boolean startWithAsterisk = prefix.contains("*");
                final boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(key, op, value));
        }
        return this;
    }

    public Specification<E> build() {
        if (params.size() == 0) {
            return null;
        }

        final List<Specification<E>> specs = new ArrayList<Specification<E>>();
        for (final SpecSearchCriteria param : params) {
            specs.add(new SpecificationImpl(param));
        }

        Specification<E> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specifications.where(result).and(specs.get(i));
        }
        return result;
    }
}
