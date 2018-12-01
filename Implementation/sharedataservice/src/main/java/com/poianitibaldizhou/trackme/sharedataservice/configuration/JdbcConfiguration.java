package com.poianitibaldizhou.trackme.sharedataservice.configuration;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.spring.SpringConnectionProvider;
import com.querydsl.sql.spring.SpringExceptionTranslator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;

@Configuration
public class JdbcConfiguration {

    @Bean
    public DataSource querydslDataSource() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(3306);
        dataSource.setDatabaseName("share_data_db");
        dataSource.setUser("trackmeadmin");
        dataSource.setPassword("datecallmeeting95");
        return dataSource;
    }

    @Bean
    public com.querydsl.sql.Configuration querydslConfiguration() {
        SQLTemplates templates = MySQLTemplates.builder().build();
        com.querydsl.sql.Configuration configuration = new com.querydsl.sql.Configuration(templates);
        configuration.setExceptionTranslator(new SpringExceptionTranslator());
        return configuration;
    }

    @Bean
    public MySQLQueryFactory queryFactory() {
        Provider<Connection> provider = new SpringConnectionProvider(querydslDataSource());
        return new MySQLQueryFactory(querydslConfiguration(), provider);
    }

}
