package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
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
		subscriptionsController = new SubscriptionsController(orderView, orderRepository);
	}
	
	@Test
	public void testFetchOrders() {
		Order order = new Order(1, LocalDateTime.of(2024, 8, 1, 0, 0, 0));
		orderRepository.save(order);
		List<Order> orders = subscriptionsController.fetchOrders();
		verify(orderView).showAllOrders(orders);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

}
