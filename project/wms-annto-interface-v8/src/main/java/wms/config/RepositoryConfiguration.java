package wms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import wms.common.respository.impl.BaseRepositoryImpl;

@Configuration
@EnableJpaRepositories(basePackages = "wms.**.repository", repositoryBaseClass = BaseRepositoryImpl.class)
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableTransactionManagement
public class RepositoryConfiguration {

}
