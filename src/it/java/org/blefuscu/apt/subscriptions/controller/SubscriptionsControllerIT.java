package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;
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
	private ExportManager exportManager;
	
	@Mock
	private ListView listView;
	
	@Mock
	private OrderView orderView;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = new OrderMongoRepository(new MongoClient("localhost"));
		subscriptionsController = new SubscriptionsController(listView, orderView, orderRepository, exportManager);
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
		subscriptionsController.requestOrders(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1));
		verify(listView).showOrders(null);
	}

}
