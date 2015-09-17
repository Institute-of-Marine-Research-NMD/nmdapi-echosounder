package no.imr.nmdapi.nmdechosounder.config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import no.imr.common.security.access.NMDsecurityManager;
import no.imr.common.security.jwt.JwtAccessTokenConverter;
import no.imr.common.security.jwt.JwtTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableWebMvcSecurity
@EnableResourceServer
@ComponentScan("no.imr.nmdapi.web.security.access.voters")
@Order(-10)
public class WebSecurityConfig extends ResourceServerConfigurerAdapter {

    /**
     * Class logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * Get all access descision voters.
     */
    @Autowired
    private List<AccessDecisionVoter<? extends Object>> accessDecisionVoters;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("microsoft:identityserver:Oauth");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated().accessDecisionManager(new NMDsecurityManager(accessDecisionVoters));
        http.anonymous().authorities("ANONYMOUS");
    }

    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        return tokenServices;
    }

    /**
     * Added token enhancer for converting the jwt token elements.
     *
     * @return
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        TokenEnhancer enhancer = null;
        try {
            enhancer = new JwtAccessTokenConverter("keys/fs.cer");
        } catch (IOException | CertificateException | InvalidKeyException ex) {
            LOGGER.error("Could not create Access token converter", ex);
        }
        return enhancer;
    }

    /**
     * Tokenstore which does not store the token, but just handles them.
     *
     * @return
     */
    @Bean(name = "tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * For converting jwt access tokens to spring objects.
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = null;
        try {
            accessTokenConverter = new JwtAccessTokenConverter("keys/fs.cer");
            return accessTokenConverter;
        } catch (IOException | CertificateException | InvalidKeyException ex) {
            LOGGER.error("Could not create Access token converter", ex);
        }
        return accessTokenConverter;
    }
}
