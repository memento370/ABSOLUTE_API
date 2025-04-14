package com.example.absoluteweb.config;

import javax.sql.DataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.absoluteweb.server.repository",
        entityManagerFactoryRef = "serverEntityManagerFactory",
        transactionManagerRef = "serverTransactionManager"
)
public class ServerDBConfig {

    @Bean(name = "serverDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.server")
    public DataSource serverDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "serverEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean serverEntityManagerFactory(
            @Qualifier("serverDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.absoluteweb.server.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "serverTransactionManager")
    public PlatformTransactionManager serverTransactionManager(
            @Qualifier("serverEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
