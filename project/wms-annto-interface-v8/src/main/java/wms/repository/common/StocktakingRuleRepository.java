package wms.repository.common;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wms.common.respository.BaseRepository;
import wms.domain.common.StocktakingRule;

public interface StocktakingRuleRepository extends BaseRepository<StocktakingRule, String> {


}
