package wms.common.respository.rsql;

import com.github.tennaito.rsql.jpa.JpaPredicateVisitor;
import com.github.tennaito.rsql.jpa.PredicateBuilderStrategy;
import com.github.tennaito.rsql.misc.ArgumentParser;

import javax.persistence.criteria.From;


public class CustomizedJpaPredicateVisitor<T> extends JpaPredicateVisitor<T> {

    public CustomizedJpaPredicateVisitor<T> withRoot(From root) {
        super.defineRoot(root);
        return this;
    }

    public CustomizedJpaPredicateVisitor<T> withArgumentParser(ArgumentParser argumentParser) {
        this.getBuilderTools().setArgumentParser(argumentParser);
        return this;
    }

    public CustomizedJpaPredicateVisitor<T> withPredicateBuilderStrategy(PredicateBuilderStrategy strategy) {
        this.getBuilderTools().setPredicateBuilder(strategy);
        return this;
    }
}
