package com.epam.esm.dao.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.epam.esm")
public class DBConfig {

    private static final String HIKARI_PROPERTIES = "/hikari.properties";
    private static final String MESSAGE_SOURCE = "classpath:message";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int CACHE_SECONDS = 5;

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig(HIKARI_PROPERTIES);
        return new HikariDataSource(config);
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dataSource());
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