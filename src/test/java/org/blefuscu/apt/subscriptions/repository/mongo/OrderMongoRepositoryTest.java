package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.assertj.core.api.Assertions.*;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import java.net.InetSocketAddress;

import org.blefuscu.apt.subscriptions.model.Order;
import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class OrderMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private OrderMongoRepository orderRepository;
	private MongoCollection<Document> orderCollection;

	@BeforeClass
	public static void setupServer() throws Exception {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void shutdownServer() throws Exception {
		server.shutdown();
	}

	@Before
	public void setup() throws Exception {
		client = new MongoClient(new ServerAddress(serverAddress));
		orderRepository = new OrderMongoRepository(client);
		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();
		orderCollection = database.getCollection(ORDER_COLLECTION_NAME);
	}

	@After
	public void tearDown() throws Exception {
		client.close();
	}

	@Test
	public void testFindAllWhenDatabaseIsEmpty() {
		assertThat(orderRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenDatabaseIsNotEmpty() {
		addTestOrderToDatabase(1, "2024-08-01 00:00:00");
		addTestOrderToDatabase(2, "2024-08-02 00:00:00");
		assertThat(orderRepository.findAll()).containsExactly(new Order(1, "2024-08-01 00:00:00"),
				new Order(2, "2024-08-02 00:00:00"));
	}

	@Test
	public void testFindByDateRangeWhenNoOrderIsFound() {
		addTestOrderToDatabase(1, "2024-08-01 00:00:00");
		addTestOrderToDatabase(2, "2024-08-02 00:00:00");
		assertThat(orderRepository.findByDateRange("2024-08-15 00:00:00", "2024-08-16 00:00:00")).isEmpty();
	}

	@Test
	public void testFindByDateRangeWhenSomeOrderIsFound() {
		addTestOrderToDatabase(1, "2024-08-01 00:00:00");
		addTestOrderToDatabase(2, "2024-08-02 00:00:00");
		addTestOrderToDatabase(3, "2024-08-03 00:00:00");
		assertThat(orderRepository.findByDateRange("2024-08-01 00:00:00", "2024-08-02 00:00:00"))
				.containsExactly(new Order(1, "2024-08-01 00:00:00"), new Order(2, "2024-08-02 00:00:00"));
	}

	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		assertThatThrownBy(() -> orderRepository.findByDateRange("2024-08-02 00:00:00", "2024-08-01 00:00:00"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than begin date");
	}

	private void addTestOrderToDatabase(int orderId, String orderDate) {
		orderCollection.insertOne(new Document().append("orderId", orderId).append("orderDate", orderDate));

	}

}
