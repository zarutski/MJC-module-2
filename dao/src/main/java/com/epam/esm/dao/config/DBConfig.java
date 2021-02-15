package com.epam.esm.dao.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Repository
@ComponentScan(basePackages = "com.epam.esm")
public class DBConfig {

    private static final String PROPERTY_HIBERNATE_DIALECT = "hibernate.dialect";
    private static final String PROPERTY_HIBERNATE_DDL_AUTO = "hibernate.ddl-auto";
    private static final String PROPERTY_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
    private static final String PROPERTY_HIBERNATE_SESSION_CLASS = "hibernate.current_session_context_class";
    private static final String VALUE_HIBERNATE_DIALECT = "spring.jpa.properties.hibernate.dialect";
    private static final String VALUE_DDL_AUTO = "spring.jpa.hibernate.ddl-auto";
    private static final String VALUE_SHOW_SQL = "spring.jpa.show-sql";
    private static final String VALUE_CONTEXT_CLASS = "spring.jpa.properties.hibernate.current_session_context_class";
    private static final String PACKAGE_SCAN_ENTITY = "com.epam.esm.domain.entity";

    private static final String HIKARI_PROPERTIES = "/hikari.properties";
    private static final String HIKARI_PROPERTIES_DEV = "/hikari_dev.properties";
    private static final String MESSAGE_SOURCE = "classpath:message";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int CACHE_SECONDS = 5;

    @Resource
    private final Environment environment;

    public DBConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        JpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();

        Properties properties = new Properties();
        properties.setProperty(PROPERTY_HIBERNATE_DIALECT, environment.getProperty(VALUE_HIBERNATE_DIALECT));
        properties.setProperty(PROPERTY_HIBERNATE_DDL_AUTO, environment.getProperty(VALUE_DDL_AUTO));
        properties.setProperty(PROPERTY_HIBERNATE_SHOW_SQL, environment.getProperty(VALUE_SHOW_SQL));
        properties.setProperty(PROPERTY_HIBERNATE_SESSION_CLASS, environment.getProperty(VALUE_CONTEXT_CLASS));

        entityManager.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        entityManager.setDataSource(dataSource);
        entityManager.setPackagesToScan(PACKAGE_SCAN_ENTITY);
        entityManager.setJpaProperties(properties);
        return entityManager;
    }

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

    @Bean
    public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }
}