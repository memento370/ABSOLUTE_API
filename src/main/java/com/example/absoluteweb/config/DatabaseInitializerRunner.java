package com.example.absoluteweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class DatabaseInitializerRunner implements ApplicationRunner {

    @Autowired
    @Qualifier("mysqlDataSource")
    private DataSource mysqlDataSource;

    @Autowired
    @Qualifier("siteDataSource")
    private DataSource siteDataSource;

    @Autowired
    @Qualifier("forumDataSource")
    private DataSource forumDataSource;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Створення баз даних, якщо їх немає
        try (Connection conn = mysqlDataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE DATABASE IF NOT EXISTS absolute_site");
            stmt.execute("CREATE DATABASE IF NOT EXISTS absolute_forum");
        } catch (SQLException e) {
            System.err.println("Помилка створення баз даних: " + e.getMessage());
        }

        // Наповнення бази site SQL-скриптом
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/absolute_site.sql"));
        try {
            populator.execute(siteDataSource);
        } catch (Exception e) {
            System.err.println("Помилка наповнення бази site: " + e.getMessage());
        }

        // Наповнення бази forum SQL-скриптом
        populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("sql/absolute_forum.sql"));
        try {
            populator.execute(forumDataSource);
        } catch (Exception e) {
            System.err.println("Помилка наповнення бази forum: " + e.getMessage());
        }
    }
}