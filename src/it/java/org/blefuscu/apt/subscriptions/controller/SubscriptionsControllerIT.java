package org.blefuscu.apt.subscriptions.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static java.util.Arrays.asList;

import java.io.Closeable;
import java.time.LocalDate;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mongodb.MongoClient;

public class SubscriptionsControllerIT {

	private AutoCloseable closeable;
	private OrderRepository orderRepository;
	private SubscriptionsController subscriptionsController;
	
	@Mock
	private ListView listView;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = new OrderMongoRepository(new MongoClient("localhost"));
		subscriptionsController = new SubscriptionsController(listView, orderRepository);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testReadFromDatabase() {
		// TODO: Only a stub
		subscriptionsController.requestOrders();
		verify(listView).showOrders(null);
	}
	
	@Test
	public void testReadFromDatabaseWhenDatesAreProvided() {
		// TODO: Only a stub
		subscriptionsController.requestOrders(null, null);
		verify(listView).showOrders(null);
	}

}
