package com.shopping.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ShoppingCartProducerRoute extends RouteBuilder {

	private static final String camellog = "POC_BIZ";
    private static final String SUCCESS_MSG ="SUCCESSFULLY INSERTED TO TOPIC";
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
        .log(LoggingLevel.ERROR, camellog, " Exception Occurred  ${exception}")
        .handled(true)
        .maximumRedeliveries(retryCount)
        .delayPattern(redeliveryDelayPattern)
        .end()
        .to(EXCEPTIONROUTE)
        .log(LoggingLevel.ERROR, camellog, "Completed the Route with exception ");
    	
    	restConfiguration().component("netty-http").host("localhost").port(8092).bindingMode(RestBindingMode.json);

    	// use the rest DSL to define the rest services
    	rest("/api")
    		.post("addproduct").consumes("application/json").produces("application/json")
    		.to("direct:addProduct");
    	
        from("direct:addProduct").routeId("producerRoute")
            .log(LoggingLevel.INFO, camellog, " post to broker, ${in.body} ")
            .marshal().json(JsonLibrary.Jackson)
            .to("kafka:ProductTopic?brokers={{kafka.broker}}").id("updateStatusOutput")
            .log(LoggingLevel.INFO, camellog, " Successfully posted to kafka topic ")
        	.setBody(simple(SUCCESS_MSG))
            .end();

        
     // Fallback Route
        from(EXCEPTIONROUTE).routeId("exceptionRoute")
            .setBody(simple(FAIL_MSG))
            .log(LoggingLevel.INFO, camellog, " Failure message: ${in.body} ")
            .end();
       
    }

}
