package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;

public class SubscriptionsControllerIT {

	@Mock
	private OrderView orderView;
	
	@Mock
	private ListView listView;

	private OrderRepository orderRepository;

	private SubscriptionsController subscriptionsController;

	private AutoCloseable closeable;

	private static int mongoPort = Integer.parseInt(System.getProperty("mongo.port", "27017"));

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = new OrderMongoRepository(new MongoClient("localhost", mongoPort));
		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}
		subscriptionsController = new SubscriptionsController(orderView, listView, orderRepository);
	}

	@Test
	public void testRequestOrders() {
		Order orderOne = new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		Order orderTwo = new Order(2, LocalDateTime.of(2024, 8, 2, 0, 0, 0));
		orderRepository.save(orderOne);
		orderRepository.save(orderTwo);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(asList(orderOne, orderTwo));
	}

	@Test
	public void testNewOrder() {
		Order order = new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		subscriptionsController.newOrder(order);
		verify(orderView).orderAdded(order);
	}

	@Test
	public void testDeleteOrder() {
		Order orderToDelete = new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		orderRepository.save(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).orderRemoved(orderToDelete);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

}
