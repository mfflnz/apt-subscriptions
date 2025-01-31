package org.blefuscu.apt.subscriptions.repository.mongo;

import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		assertThat(orderRepository.findAll()).containsExactly(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build(),
				new Order.OrderBuilder(2, LocalDate.of(2024, 8, 2), 0, null, null, null).build());
	}

	@Test
	public void testFindByDateRange() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		addTestOrderToDatabase(3, LocalDate.of(2024, 8, 3));
		assertThat(orderRepository.findByDateRange(LocalDate.of(2024, 8, 1),
				LocalDate.of(2024, 8, 2)))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build(),
						new Order.OrderBuilder(2, LocalDate.of(2024, 8, 2), 0, null, null, null).build());

	}

	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		assertThatThrownBy(() -> orderRepository.findByDateRange(LocalDate.of(2024, 8, 2),
				LocalDate.of(2024, 8, 1))).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than start date");
	}

	@Test
	public void testFindByDateRangeWhenStartDateIsEqualToEndDate() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 1));
		assertThat(orderRepository.findByDateRange(LocalDate.of(2024, 8, 1),
				LocalDate.of(2024, 8, 1)))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build(),
						new Order.OrderBuilder(2, LocalDate.of(2024, 8, 1), 0, null, null, null).build());

	}

	@Test
	public void testFindById() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		addTestOrderToDatabase(2, LocalDate.of(2024, 8, 2));
		assertThat(orderRepository.findById(2)).isEqualTo(new Order.OrderBuilder(2, LocalDate.of(2024, 8, 2), 0, null, null, null).build());
	}

	@Test
	public void testSave() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build();
		orderRepository.save(order);
		assertThat(readAllOrdersFromDatabase()).containsExactly(order);
	}

	@Test
	public void testDelete() {
		addTestOrderToDatabase(1, LocalDate.of(2024, 8, 1));
		orderRepository.delete(1);
		assertThat(readAllOrdersFromDatabase()).isEmpty();
	}

	private void addTestOrderToDatabase(int orderId, LocalDate orderDate) {
		orderCollection.insertOne(new Document().append("orderId", orderId).append("orderDate", orderDate)
				.append("orderTotal", 0.0)
				.append("paymentMethodTitle", null)
				.append("orderAttributionReferrer", null)
				.append("billingEmail", null)
				);
	}

	private List<Order> readAllOrdersFromDatabase() {
		return StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order.OrderBuilder(d.getInteger("orderId"),
						d.get("orderDate", Date.class).toInstant().atZone(ZoneId.of("UTC")).toLocalDate(), 0, null, null, null).build())
				.collect(Collectors.toList());
	}

}
