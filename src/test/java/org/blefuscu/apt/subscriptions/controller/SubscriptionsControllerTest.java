package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static java.util.Arrays.asList;

import java.time.LocalDate;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubscriptionsControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ListView listView;

	@InjectMocks
	private SubscriptionsController subscriptionsController;

	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testRequestOrders() {
		List<Order> orders = asList(
				new Order.OrderBuilder(1, LocalDate.of(2025, 8, 25), "customer_1@address.com").build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 8, 26), "customer_2@address.com").build());
		when(orderRepository.findAll()).thenReturn(orders);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(orders);

	}

}
