package com.bsg6;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@SpringBootApplication
public class BandGatewayApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BandGatewayApiApplication.class, args);
    }

    @Bean
    public DataSource dataSource() {
        try {
            var dbBuilder = new EmbeddedDatabaseBuilder();
            return dbBuilder.setType(EmbeddedDatabaseType.H2)
                    .addScripts("classpath:schema.sql", "classpath:data.sql")
                    .build();
        } catch (Exception e) {
            //LOGGER.error("Embedded DataSource bean cannot be created!", e);
            return null;
        }
    }
}
