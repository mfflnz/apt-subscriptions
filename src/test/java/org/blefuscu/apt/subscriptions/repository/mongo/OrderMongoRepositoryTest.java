package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.assertj.core.api.Assertions.*;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;

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
		addTestOrderToDatabase(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		addTestOrderToDatabase(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0));
		assertThat(orderRepository.findAll()).containsExactly(new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0)),
				new Order(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0)));
	}
	
	@Test
	public void testFindByDateRangeWhenNoOrderIsFound() {
		addTestOrderToDatabase(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		addTestOrderToDatabase(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0));      // Find all documents
		assertThat(orderRepository.findByDateRange(LocalDateTime.of(2024, 8, 15, 0, 0, 0),
				LocalDateTime.of(2024, 8, 16, 0, 0, 0))).isEmpty();
	}

	@Test
	public void testFindByDateRangeWhenSomeOrderIsFound() {
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

}
