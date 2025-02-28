package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class SubscriptionsControllerIT {

	@Mock
	private OrderView orderView;
	
	@Mock
	private ListView listView;

	private MongoClient client;
	private OrderMongoRepository orderRepository;
	private SubscriptionsController subscriptionsController;

	private AutoCloseable closeable;

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	@Before
	public void setUp() throws Exception {
		client = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));

		orderRepository = new OrderMongoRepository(client);
		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();
		
		
		closeable = MockitoAnnotations.openMocks(this);
		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}
		subscriptionsController = new SubscriptionsController(orderView, listView, orderRepository);
	}

	@Test
	public void testRequestOrders() {
		Order orderOne = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 30.00, "Bonifico", "Abbonamento digitale", "test@email.com").build();
		Order orderTwo = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 2), 65.00, "PayPal", "Abbonamento cartaceo + digitale", "test@email.com").build();
		orderRepository.save(orderOne);
		orderRepository.save(orderTwo);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(asList(orderOne, orderTwo));
		assertThat(orderRepository.findAll()).containsExactly(orderOne, orderTwo);
	}

	@Test
	public void testNewOrder() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 30.00, "Bonifico", "Abbonamento digitale", "test@email.com").build();
		subscriptionsController.newOrder(order);
		verify(orderView).orderAdded(order);
		assertThat(orderRepository.findAll()).containsExactly(order);
	}

	@Test
	public void testDeleteOrder() {
		Order orderToDelete = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 1), 30.00, "Bonifico", "Abbonamento digitale", "test@email.com").build();
		orderRepository.save(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).orderRemoved(orderToDelete);
		assertThat(orderRepository.findAll()).isEmpty();
	}
	

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

}
