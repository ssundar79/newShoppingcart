package com.shopping.shoppingcart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.bson.Document;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoClient;
import com.shopping.logger.ILogger;
import com.shopping.logger.LoggerFactory;
import static org.apache.camel.component.mongodb.MongoDbConstants.OID;

@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("mock")
@SpringBootTest(properties = {
	    "spring.data.mongodb.host=localhost",
	    "spring.data.mongodb.port=27017",
	})
public class ShoppingCartConsumerApplicationTest {

    private ILogger logger = LoggerFactory.getBusinessLogger(ShoppingCartConsumerApplicationTest.class);

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
   
  
    //Test for Valid Request
    @Test
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
        assertTrue(actualOutput.contains("234"));
        assertTrue(actualOutput.contains("SAMSUNG 11 WHITE"));
    }
    
  //Test for InValid Request
    @Test
    public void testOnInvalidRequest() throws Exception {
    	String req ="{\r\n" + 
    			"	\"productID: 234,\r\n" + 
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
    
    
    //Test for Json Node value
    @Test
    public void testOnNodeValue() throws Exception {
    	String req ="{\r\n" + 
    			"	\"productID\": 236,\r\n" + 
    			"	\"productName\":\"IPHONE 11 WHITE\",\r\n" + 
    			"	\"productPrice\":600,\r\n" + 
    			"	\"quantity\":3\r\n" + 
    			"	\r\n" + 
    			"}";
    	Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(req);
        producerTemplate.send("direct:consumer", exchange);
        String body = exchange.getIn().getBody(String.class);
        logger.info("body:::"+body);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readValue(body, JsonNode.class);

        JsonNode child = jsonNode.get("productID");
        int productID = child.asInt();
        logger.info("productID:::"+productID);
        int expectedProductID=236;
        assertEquals(expectedProductID, productID);
        
    }
    
    
   
}

