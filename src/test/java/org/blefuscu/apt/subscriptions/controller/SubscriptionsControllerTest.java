package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubscriptionsControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;

	private AutoCloseable closeable;

	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = mock(OrderRepository.class);
		subscriptionsController = new SubscriptionsController(orderView, orderRepository);

	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testFetchOrdersWhenNoOrderIsPresent() {
		when(orderRepository.findAll()).thenReturn(emptyList());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}

	@Test
	public void testFetchOrdersWhenOneOrderIsPresent() {
		when(orderRepository.findAll()).thenReturn(asList(new Order()));
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(1);
	}

	@Test
	public void testFetchOrdersWhenMoreThanOneOrderIsPresent() {
		when(orderRepository.findAll()).thenReturn(asList(new Order(), new Order()));
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(2);
	}
	
	@Test
	public void testFetchOrdersWhenCorrectDateRangeIsProvided() {
		when(orderRepository.findByDateRange("2024-06-01", "2024-07-01")).thenReturn(asList(new Order()));
		assertThat(subscriptionsController.fetchOrders("2024-06-01", "2024-07-01")).size().isEqualTo(1);
	}
	
	@Test
	public void testFetchOrdersWhenStartDateIsMissingShouldThrow() {
		when(orderRepository.findByDateRange("", "2024-07-01")).thenThrow(new IllegalArgumentException());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> subscriptionsController.fetchOrders("", "2024-07-01"));
		assertEquals("Please provide start date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}
	
	@Test
	public void testFetchOrdersWhenEndDateIsMissingShouldThrow() {
		when(orderRepository.findByDateRange("2024-06-01", "")).thenThrow(new IllegalArgumentException());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> subscriptionsController.fetchOrders("2024-06-01", ""));
		assertEquals("Please provide end date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}
	
	@Test
	public void testFetchOrdersWhenIncorrectDateRangeIsProvidedShouldThrow() {
		when(orderRepository.findByDateRange("2024-07-01", "2024-06-01")).thenThrow(new IllegalArgumentException());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> subscriptionsController.fetchOrders("2024-0X-01", "2024-06-01"));
		assertEquals("Start date should be earlier than end date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}

	@Test
	public void testFetchOrdersWhenStartDateAndEndDateAreTheSame() {
		when(orderRepository.findByDateRange("2024-06-01", "2024-06-01")).thenReturn(asList(new Order()));
		assertThat(subscriptionsController.fetchOrders("2024-06-01", "2024-06-01")).size().isEqualTo(1);
	}
	
}
