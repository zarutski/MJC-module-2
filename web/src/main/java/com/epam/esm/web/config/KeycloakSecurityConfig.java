package com.epam.esm.web.config;

import org.keycloak.adapters.KeycloakConfigResolver;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import static com.epam.esm.web.util.SecurityValue.ADMIN;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_ORDERS;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_USERS;
import static com.epam.esm.web.util.SecurityValue.KEYCLOAK_AUTH_SERVER_URL;
import static com.epam.esm.web.util.SecurityValue.KEYCLOAK_MASTER_CLIENT;
import static com.epam.esm.web.util.SecurityValue.KEYCLOAK_MASTER_PASSWORD;
import static com.epam.esm.web.util.SecurityValue.KEYCLOAK_MASTER_REALM;
import static com.epam.esm.web.util.SecurityValue.KEYCLOAK_MASTER_USERNAME;
import static com.epam.esm.web.util.SecurityValue.USER;

@KeycloakConfiguration
@Profile("dev")
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true)
public class KeycloakSecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(ENDPOINT_USERS).hasAnyRole(USER, ADMIN)
                .antMatchers(ENDPOINT_ORDERS).hasRole(ADMIN)
                .anyRequest().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        KeycloakAuthenticationProvider keycloakAuthenticationProvider = keycloakAuthenticationProvider();
        SimpleAuthorityMapper grantedAuthorityMapper = new SimpleAuthorityMapper();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(grantedAuthorityMapper);
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    @Bean
    public KeycloakConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }

    @Bean
    Keycloak initKeycloakWithAdminRole() {
        return Keycloak.getInstance(
                KEYCLOAK_AUTH_SERVER_URL,
                KEYCLOAK_MASTER_REALM,
                KEYCLOAK_MASTER_USERNAME,
                KEYCLOAK_MASTER_PASSWORD,
                KEYCLOAK_MASTER_CLIENT);
    }
}