package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class OrderMongoRepositoryTestcontainersIT {

	@SuppressWarnings("rawtypes")
	@ClassRule
	public static final GenericContainer mongo = new GenericContainer("mongo:4.4.3").withExposedPorts(27017);

	private MongoClient client;
	private OrderMongoRepository orderRepository;
	private MongoCollection<Document> orderCollection;

	@Before
	public void setup() {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
		orderRepository = new OrderMongoRepository(client);
		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();
		orderCollection = database.getCollection(ORDER_COLLECTION_NAME);
	}
	
	@After
	public void tearDown() {
		client.close();
	}
	
	@Test
	public void test() {
		
	}
	

}
