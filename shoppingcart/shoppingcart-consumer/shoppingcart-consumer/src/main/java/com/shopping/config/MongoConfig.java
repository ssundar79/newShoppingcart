package com.shopping.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.mongodb.MongoClient;


@Configuration
public class MongoConfig {
	
	
	@Value("#{'${spring.data.mongodb.host}'}")
    private String host;
	
	@Value("#{'${spring.data.mongodb.port}'}")
    private Integer port;

    @Bean("mongoClient")
    public com.mongodb.MongoClient getMongoClient() {
        return new MongoClient(host, port);
    }

}
