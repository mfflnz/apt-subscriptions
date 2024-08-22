package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import org.blefuscu.apt.subscriptions.model.Order;

import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.ServerAddress;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class OrderMongoRepositoryTestcontainersIT {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

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
	public void testFindAll() {
		addTestOrderToDatabase(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		addTestOrderToDatabase(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0));
		assertThat(orderRepository.findAll()).containsExactly(new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0)),
				new Order(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0)));
		
	}
	
	@Test
	public void testFindByDateRange() {
		addTestOrderToDatabase(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		addTestOrderToDatabase(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0));
		addTestOrderToDatabase(3, LocalDateTime.of(2024, 8, 3, 0, 0, 0));
		assertThat(orderRepository.findByDateRange(LocalDateTime.of(2024, 8, 1, 0, 0, 0),
				LocalDateTime.of(2024, 8, 2, 0, 0, 0)))
				.containsExactly(new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0)),
						new Order(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0)));
		
	}
	
	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		assertThatThrownBy(() -> orderRepository.findByDateRange(LocalDateTime.of(2024, 8, 2, 0, 0, 0),
				LocalDateTime.of(2024, 8, 1, 0, 0, 0))).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than start date");
	}
	
	@Test
	public void testFindByDateRangeWhenStartDateIsEqualToEndDate() {
		addTestOrderToDatabase(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		addTestOrderToDatabase(2, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		assertThat(orderRepository.findByDateRange(LocalDateTime.of(2024, 8, 1, 0, 0, 0),
				LocalDateTime.of(2024, 8, 1, 0, 0, 0)))
				.containsExactly(new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0)),
						new Order(2, LocalDateTime.of(2024, 8, 1, 0, 0, 0)));

	}

	private void addTestOrderToDatabase(int orderId, LocalDateTime orderDate) {
		orderCollection.insertOne(new Document().append("orderId", orderId).append("orderDate", orderDate));
	}
	
	
	// TODO: pitest fail on macos

}
