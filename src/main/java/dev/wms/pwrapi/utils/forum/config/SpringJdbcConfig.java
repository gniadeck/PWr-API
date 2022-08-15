package dev.wms.pwrapi.utils.forum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("dev.wms.pwrapi")
public class SpringJdbcConfig {

    @Bean
    public DataSource mySqlDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(System.getenv("URL"));
        dataSource.setUsername(System.getenv("login"));
        dataSource.setPassword(System.getenv("password"));
        return dataSource;
    }
}
