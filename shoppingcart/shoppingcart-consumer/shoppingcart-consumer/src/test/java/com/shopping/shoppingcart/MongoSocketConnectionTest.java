package com.shopping.shoppingcart;

import static com.mongodb.client.model.Filters.*;


import static com.mongodb.client.model.Projections.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.bson.Document;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.mongodb.MongoClient;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.shopping.logger.ILogger;
import com.shopping.logger.LoggerFactory;


@RunWith(CamelSpringBootRunner.class)
@ActiveProfiles("mock")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(properties = {
    "spring.data.mongodb.host=dummy",
    "spring.data.mongodb.port=27018",
})
public class MongoSocketConnectionTest {
	
	 private ILogger logger = LoggerFactory.getBusinessLogger(MongoSocketConnectionTest.class);
	
	  @Autowired
	    CamelContext camelContext;

	    @Autowired
	    ProducerTemplate producerTemplate;
	
	
	 @Before
	    public void setupTest() throws Exception {

	        camelContext.getRouteDefinitions().get(0).adviceWith(camelContext,
	            new AdviceWithRouteBuilder() {
	                @Override
	                public void configure() throws Exception {
	                    replaceFromWith("direct:consumer");
	                }
	            });

	        camelContext.start();
	    }
	 
	 //Mongo Socket exception
	 @Test()
	    public void testOnValidRequest() throws Exception {
	    	String req ="{\r\n" + 
	    			"	\"productID\": 234,\r\n" + 
	    			"	\"productName\":\"SAMSUNG 11 WHITE\",\r\n" + 
	    			"	\"productPrice\":600,\r\n" + 
	    			"	\"quantity\":3\r\n" + 
	    			"	\r\n" + 
	    			"}";
	    	Exchange exchange = new DefaultExchange(camelContext);
	        exchange.getIn().setBody(req);
	        producerTemplate.send("direct:consumer", exchange);
	        String actualOutput = exchange.getIn().getBody(String.class);
	        logger.info("actualOutput:::"+actualOutput);
	        assertEquals("FAIL TO INSERT TO DATABASE",actualOutput);
	        
	    }
	
}
