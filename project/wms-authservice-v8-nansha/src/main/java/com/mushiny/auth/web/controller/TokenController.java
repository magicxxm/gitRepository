package com.mushiny.auth.web.controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
public class TokenController {

    @Inject
    private ConsumerTokenServices tokenServices;

    @Inject
    private TokenStore tokenStore;

    @PostMapping("/tokens/revoke/{tokenId:.*}")
    public String revokeToken(@PathVariable String tokenId) {
        tokenServices.revokeToken(tokenId);
        return tokenId;
    }

    @GetMapping("/tokens")
    public List<String> getTokens() {
        List<String> tokenValues = new ArrayList<>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("sampleClientId");
        if (tokens != null) {
            for (OAuth2AccessToken token : tokens) {
                tokenValues.add(token.getValue());
            }
        }
        return tokenValues;
    }

    @PostMapping(value = "/tokens/revoke-refresh-token/{tokenId:.*}")
    public String revokeRefreshToken(@PathVariable String tokenId) {
        if (tokenStore instanceof RedisTokenStore) {
            ((RedisTokenStore) tokenStore).removeRefreshToken(tokenId);
        }
        return tokenId;
    }

    @GetMapping("/tokens/authorities")
    public String getSection(OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = ((AuthorizationServerTokenServices)tokenServices).getAccessToken(authentication).getAdditionalInformation();
        String customInfo = (String) additionalInfo.get("customInfo");
        Collection<? extends GrantedAuthority> authorities = (Collection<? extends GrantedAuthority>) additionalInfo.get("authorities");

        // Play with authorities
        return customInfo;
    }

}
