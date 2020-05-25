package com.shopping.shoppingcart;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoInsertionTest {
	private MongoCollection<Document> productDocuments;
	
	@Before
    public void clearAndPopulateDB() {
        @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient();
        MongoDatabase db = mongoClient.getDatabase("testingdb");
        productDocuments = db.getCollection("ProductTest");
        productDocuments.drop();
        List<Document> testUsers = new ArrayList<>();
        testUsers.add(Document.parse("{\r\n" + 
        		"	\"productID\": 001,\r\n" + 
        		"	\"productName\":\"SAMSUNG 11 WHITE\",\r\n" + 
        		"	\"productPrice\":600,\r\n" + 
        		"	\"quantity\":3\r\n" + 
        		"	\r\n" + 
        		"}"));
        testUsers.add(Document.parse("{\r\n" + 
        		"	\"productID\": 002,\r\n" + 
        		"	\"productName\":\"SAMSUNG 10 black\",\r\n" + 
        		"	\"productPrice\":500,\r\n" + 
        		"	\"quantity\":3\r\n" + 
        		"	\r\n" + 
        		"}"));
        productDocuments.insertMany(testUsers);
    }
	
	private List<Document> intoList(MongoIterable<Document> documents) {
        List<Document> users = new ArrayList<>();
        documents.into(users);
        return users;
    }

    private int countProduct(FindIterable<Document> documents) {
        List<Document> users = intoList(documents);
        return users.size();
    }
    
    
    //Test to check  count of Insertion records
    @Test
    public void shouldBeOneProduct() {
        FindIterable<Document> documents = productDocuments.find(eq("productName", "SAMSUNG 11 WHITE"));
        int numberOfProduct = countProduct(documents);
        assertEquals("Should be 1 Product", 1, numberOfProduct);
    }
    
    @Test
    public void shouldBeZeroProduct() {
        FindIterable<Document> documents = productDocuments.find(eq("productName", "SAMSUNG 12 WHITE"));
        int numberOfProduct = countProduct(documents);
        assertEquals("Should be 0 Product", 0, numberOfProduct);
    }
	
}
