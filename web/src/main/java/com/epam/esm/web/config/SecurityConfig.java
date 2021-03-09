package com.epam.esm.web.config;

import com.epam.esm.web.security.JwtConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import static com.epam.esm.web.util.SecurityValue.ADMIN;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_ORDERS;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_USERS;
import static com.epam.esm.web.util.SecurityValue.USER;

@Configuration
@EnableWebSecurity
@Profile("basic")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(ENDPOINT_USERS).hasAnyRole(USER, ADMIN)
                .antMatchers(ENDPOINT_ORDERS).hasRole(ADMIN)
                .anyRequest().permitAll()
                .and().apply(jwtConfigurer);
    }
}