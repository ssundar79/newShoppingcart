package com.shopping.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ShoppingCartConsumerRoute extends RouteBuilder {

	private static final String camellog = "POC_BIZ";
    private static final String FAIL_MSG ="FAIL TO INSERT TO DATABASE";
    private static final String EXCEPTIONROUTE = "{{exceptionRoute}}";
    private static final String BODY = "${body}";
    
    @Value("${retryable.delayPattern}")
    private String redeliveryDelayPattern;
    
    @Value("${retryable.count}")
    private int retryCount;
    
    @Override
    public void configure() throws Exception {

    	onException(Exception.class)
        .log(LoggingLevel.ERROR, camellog, "DB Exception Occurred  ${exception.message}")
        .handled(true)
        .maximumRedeliveries(retryCount)
        .delayPattern(redeliveryDelayPattern)
        .end()
        .to(EXCEPTIONROUTE)
        .log(LoggingLevel.ERROR, camellog, "Completed the Route with exception ");
    	
    	

        from(
            "kafka:ProductTopic?brokers={{kafka.broker}}&autoOffsetReset=earliest&groupId=product&maxPollRecords=3&consumersCount=1&seekTo=").routeId("persisttransaction")
                .log(LoggingLevel.INFO, camellog, " receive from topic,  ${in.body} ")
                .to(
                    "mongodb:mongoClient?database={{spring.data.mongodb.database}}&collection={{collection}}&operation=insert")
                .log(LoggingLevel.INFO, camellog, " Succesfully Inserted in to Database: ${in.body} ");
               
        
     // Fallback Route
        from(EXCEPTIONROUTE).routeId("exceptionRoute")
            .setBody(simple(FAIL_MSG))
            .log(LoggingLevel.INFO, camellog, " Failure to Insert to Database:  ${in.body}  ")
            .end();
       
    }

}
