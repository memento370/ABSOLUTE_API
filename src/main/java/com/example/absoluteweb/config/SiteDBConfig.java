package com.example.absoluteweb.config;

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

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.absoluteweb.site.repository",
        entityManagerFactoryRef = "siteEntityManagerFactory",
        transactionManagerRef = "siteTransactionManager"
)
public class SiteDBConfig {

    @Bean(name = "siteDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.site")
    public DataSource siteDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "siteEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean siteEntityManagerFactory(
            @Qualifier("siteDataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.absoluteweb.site.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean(name = "siteTransactionManager")
    public PlatformTransactionManager siteTransactionManager(
            @Qualifier("siteEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
