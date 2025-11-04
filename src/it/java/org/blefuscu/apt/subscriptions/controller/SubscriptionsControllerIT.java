package org.blefuscu.apt.subscriptions.controller;

import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;

import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.MessageView;
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
	private ExportController exportManager;
	
	@Mock
	private ListView listView;
	
	@Mock
	private OrderView orderView;

	@Mock
	private MessageView messageView;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = new OrderMongoRepository(new MongoClient("localhost"), SUBSCRIPTIONS_DB_NAME, ORDER_COLLECTION_NAME);
		subscriptionsController = new SubscriptionsController(listView, orderView, messageView, orderRepository, exportManager);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testReadFromDatabase() {
		// TODO: Only a stub
		// subscriptionsController.requestOrders();
		// verify(listView).showOrders(anyList());
	}
	
	@Test
	public void testReadFromDatabaseWhenDatesAreProvided() {
		// TODO: Only a stub
		// subscriptionsController.requestOrders(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 1));
		// verify(listView).showOrders(anyList());
	}

}
