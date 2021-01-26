package com.epam.esm.dao.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.epam.esm")
public class DBConfig {

    private static final String HIKARI_PROPERTIES = "/hikari.properties";
    private static final String HIKARI_PROPERTIES_DEV = "/hikari_dev.properties";
    private static final String MESSAGE_SOURCE = "classpath:message";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int CACHE_SECONDS = 5;

    @Bean
    @Profile("prod")
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("dev")
    public DataSource dataSourceDev() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES_DEV);
        return new HikariDataSource(config);
    }

    @Bean
    @Profile("prod")
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
    }

    @Bean
    @Profile("dev")
    public JdbcTemplate jdbcTemplateDev() {
        return new JdbcTemplate(dataSourceDev());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(MESSAGE_SOURCE);
        // key message will be displayed when key is not found
        // instead of throwing a NoSuchMessageException
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        messageSource.setCacheSeconds(CACHE_SECONDS);
        return messageSource;
    }

}