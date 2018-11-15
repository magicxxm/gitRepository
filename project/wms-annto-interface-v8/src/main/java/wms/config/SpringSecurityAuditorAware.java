package wms.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import wms.config.security.SecurityUtils;

/**
 * Implementation of AuditorAware based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {
        return SecurityUtils.getCurrentUsername();
    }
}
