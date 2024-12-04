package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.assertj.core.api.Assertions.*;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import java.net.InetSocketAddress;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		assertThat(orderRepository.findAll()).containsExactly(new Order(1, LocalDate.of(2024, 8, 1)),
				new Order(2, LocalDate.of(2024, 8, 2)));
	}

	@Test
	public void testFindByDateRangeWhenNoOrderIsFound() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		assertThat(orderRepository.findByDateRange(LocalDate.of(2024, 8, 15), LocalDate.of(2024, 8, 16))).isEmpty();
	}

	@Test
	public void testFindByDateRangeWhenSomeOrderIsFound() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		addTestOrderToDatabase(3, LocalDate.of(2024, 8, 3));
		assertThat(orderRepository.findByDateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 2)))
				.containsExactly(new Order(1, LocalDate.of(2024, 8, 1)), new Order(2, LocalDate.of(2024, 8, 2)));
	}

	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		assertThatThrownBy(() -> orderRepository.findByDateRange(LocalDate.of(2024, 8, 2), LocalDate.of(2024, 8, 1)))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than start date");
	}

	@Test
	public void testFindByDateRangeWhenStartDateIsEqualToEndDate() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 1));
		assertThat(orderRepository.findByDateRange(LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 1)))
				.containsExactly(new Order(1, LocalDate.of(2024, 8, 1)), new Order(2, LocalDate.of(2024, 8, 1)));
	}

	@Test
	public void testFindByIdNotFound() {
		assertThat(orderRepository.findById(1)).isNull();
	}

	@Test
	public void testFindByIdFound() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		assertThat(orderRepository.findById(1)).isEqualTo(new Order(1, LocalDate.of(2024, 8, 1)));
	}

	@Test
	public void testSave() {
		Order order = new Order(1, LocalDate.of(2024, 8, 1));
		orderRepository.save(order);
		assertThat(readAllOrdersFromDatabase()).containsExactly(order);
	}

	@Test
	public void testDelete() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		orderRepository.delete(1);
		assertThat(readAllOrdersFromDatabase()).isEmpty();
	}

	@Test
	public void testEditWhenOrderToEditIsFound() {
		Order orderToUpdate = new Order(1, LocalDate.of(2024, 8, 1));
		Order orderWithNewValues = new Order(0, LocalDate.of(2024, 8, 2));
		addTestOrderToDatabase(orderToUpdate.getOrderId(), orderToUpdate.getOrderDate());
		assertThat(readAllOrdersFromDatabase()).containsExactly(orderToUpdate);
		orderRepository.edit(1, orderWithNewValues);
		assertThat(readAllOrdersFromDatabase()).containsExactly(new Order(1, LocalDate.of(2024, 8, 2)));
	}

	@Test
	public void testEditWhenOrderToEditIsNotFound() {
		Order orderToUpdate = new Order(1, LocalDate.of(2024, 8, 1));
		Order orderWithNewValues = new Order(0, LocalDate.of(2024, 8, 2));
		addTestOrderToDatabase(orderToUpdate.getOrderId(), orderToUpdate.getOrderDate());
		assertThat(readAllOrdersFromDatabase()).containsExactly(orderToUpdate);
		assertThatThrownBy(() -> orderRepository.edit(2, orderWithNewValues)).isInstanceOf(NullPointerException.class)
				.hasMessage("Error: No order found with given Id");
	}

	private List<Order> readAllOrdersFromDatabase() {
		return StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order(d.getInteger("orderId"),
						d.get("orderDate", Date.class).toInstant().atZone(ZoneId.of("UTC")).toLocalDate()))
				.collect(Collectors.toList());
	}

	private void addTestOrderToDatabase(int orderId, LocalDate orderDate) {
		orderCollection.insertOne(new Document().append("orderId", orderId).append("orderDate", orderDate));
	}

}
