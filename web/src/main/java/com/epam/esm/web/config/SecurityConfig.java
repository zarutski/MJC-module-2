package com.epam.esm.web.config;

import com.epam.esm.web.security.handler.ExceptionHandlingFilter;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.epam.esm.web.util.SecurityValue.ADMIN;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_AUTHENTICATION;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_CERTIFICATES;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_ORDERS;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_TAGS;
import static com.epam.esm.web.util.SecurityValue.ENDPOINT_USERS;
import static com.epam.esm.web.util.SecurityValue.USER;

@Configuration
@EnableWebSecurity
@Profile("basic")
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;
    private final ExceptionHandlingFilter exceptionHandlingFilter;

    public SecurityConfig(JwtConfigurer jwtConfigurer, AuthenticationEntryPoint authenticationEntryPoint,
                          AccessDeniedHandler accessDeniedHandler, ExceptionHandlingFilter exceptionHandlingFilter) {
        this.jwtConfigurer = jwtConfigurer;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.exceptionHandlingFilter = exceptionHandlingFilter;
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
                .antMatchers(ENDPOINT_AUTHENTICATION).permitAll()
                .antMatchers(ENDPOINT_CERTIFICATES).permitAll()
                .antMatchers(ENDPOINT_TAGS).permitAll()
                .anyRequest().authenticated()
                .and().apply(jwtConfigurer)
                .and()
                .addFilterBefore(exceptionHandlingFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}