package com.mushiny.auth.config;

import com.mushiny.auth.security.CustomOauthException;
import com.mushiny.auth.security.CustomTokenEnhancer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.util.Arrays;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Inject
    private Environment env;

//    @Inject
//    private GatewayProperties gatewayProperties;

    @Inject
    private DataSource dataSource;

    @Inject
    private UserDetailsService userDetailsService;

    @Inject
    private RedisConnectionFactory connectionFactory;

    @Inject
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new InMemoryAuthorizationCodeServices();
    }

    @Bean
    public ApprovalStore approvalStore() {
        TokenApprovalStore approvalStore = new TokenApprovalStore();
        approvalStore.setTokenStore(tokenStore());
        return approvalStore;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));

        endpoints
                .authorizationCodeServices(authorizationCodeServices())
                .approvalStore(approvalStore())
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .exceptionTranslator(e -> {
                    if (e instanceof OAuth2Exception) {
                        OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
                        return ResponseEntity
//                                .status(oAuth2Exception.getHttpErrorCode())
                                .status(HttpServletResponse.SC_UNAUTHORIZED)
                                .body(new CustomOauthException(oAuth2Exception.getMessage()));
                    } else {
                        throw e;
                    }
                });
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients
                .inMemory()
                .withClient("web_app")
                .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code")
                .scopes("ui")
                .autoApprove(true)
            .and()
                .withClient("wms-masterdata-service-v8")
                .secret("wms-masterdata-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-internaltool-service-v8")
                .secret("wms-internaltool-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-system-service-v8")
                .secret("wms-system-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-inbound-service-v8")
                .secret("wms-inbound-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-outbound-service-v8")
                .secret("wms-outbound-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-inboundproblem-service-v8")
                .secret("wms-inboundproblem-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-outboundproblem-service-v8")
                .secret("wms-outboundproblem-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-icqa-service-v8")
                .secret("wms-icqa-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-andon-service-v8")
                .secret("wms-andon-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-report-service-v8")
                .secret("wms-report-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-hardware-service-v8")
                .secret("wms-hardware-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-warehousedesign-service-v8")
                .secret("wms-warehousedesign-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
            .and()
                .withClient("wms-icqa-service-v8")
                .secret("wms-icqa-service-v8")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .autoApprove(true)
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(2592000);
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }
}
