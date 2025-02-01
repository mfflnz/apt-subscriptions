package org.blefuscu.apt.subscriptions.controller;

import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class SubscriptionsControllerIT {

	// TODO: test with real views, not mocks
	
	@Mock
	private OrderView orderView;
	
	@Mock
	private ListView listView;

	private MongoClient client;
	private OrderMongoRepository orderRepository;
	private SubscriptionsController subscriptionsController;
	private MongoCollection<Document> orderCollection;

	private AutoCloseable closeable;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));
	
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	@Before
	public void setUp() throws Exception {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getMappedPort(27017)));
		orderRepository = new OrderMongoRepository(client);
		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();
		orderCollection = database.getCollection(ORDER_COLLECTION_NAME);
		
		
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = new OrderMongoRepository(new MongoClient("localhost", mongoPort));
		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}
		subscriptionsController = new SubscriptionsController(orderView, listView, orderRepository);
	}

	@Test
	public void testRequestOrders() {
		Order orderOne = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build();
		Order orderTwo = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 2), 0, null, null, null).build();
		orderRepository.save(orderOne);
		orderRepository.save(orderTwo);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(asList(orderOne, orderTwo));
	}

	@Test
	public void testNewOrder() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build();
		subscriptionsController.newOrder(order);
		verify(orderView).orderAdded(order);
	}

	@Test
	public void testDeleteOrder() {
		Order orderToDelete = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 0, null, null, null).build();
		orderRepository.save(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).orderRemoved(orderToDelete);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

}
