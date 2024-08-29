package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
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
		when(orderRepository.findByDateRange(LocalDateTime.of(2024, 6, 1, 0, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 0, 0))).thenReturn(asList(new Order()));
		assertThat(subscriptionsController.fetchOrders(LocalDateTime.of(2024, 6, 1, 0, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 0, 0))).size().isEqualTo(1);
	}
	
	@Test
	public void testFetchOrdersWhenStartDateIsMissingShouldThrow() {
		when(orderRepository.findByDateRange(null, LocalDateTime.of(2024, 7, 1, 0, 0, 0))).thenThrow(new IllegalArgumentException());
		NullPointerException e = assertThrows(NullPointerException.class, () -> subscriptionsController.fetchOrders(null, LocalDateTime.of(2024, 7, 1, 0, 0, 0)));
		assertEquals("Please provide start date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}
	
	@Test
	public void testFetchOrdersWhenEndDateIsMissingShouldThrow() {
		when(orderRepository.findByDateRange(LocalDateTime.of(2024, 6, 1, 0, 0, 0), null)).thenThrow(new IllegalArgumentException());
		NullPointerException e = assertThrows(NullPointerException.class, () -> subscriptionsController.fetchOrders(LocalDateTime.of(2024, 6, 1, 0, 0, 0), null));
		assertEquals("Please provide end date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}
	
	@Test
	public void testFetchOrdersWhenIncorrectDateRangeIsProvidedShouldThrow() {
		when(orderRepository.findByDateRange(LocalDateTime.of(2024, 7, 1, 0, 0, 0), LocalDateTime.of(2024, 6, 1, 0, 0, 0))).thenThrow(new IllegalArgumentException());
		IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> subscriptionsController.fetchOrders(LocalDateTime.of(2024, 7, 1, 0, 0, 0), LocalDateTime.of(2024, 6, 1, 0, 0, 0)));
		assertEquals("Start date should be earlier than end date", e.getMessage());
		assertThat(subscriptionsController.fetchOrders()).size().isEqualTo(0);
	}

	@Test
	public void testFetchOrdersWhenStartDateAndEndDateAreTheSame() {
		when(orderRepository.findByDateRange(LocalDateTime.of(2024, 6, 1, 0, 0, 0), LocalDateTime.of(2024, 6, 1, 0, 0, 0))).thenReturn(asList(new Order()));
		assertThat(subscriptionsController.fetchOrders(LocalDateTime.of(2024, 6, 1, 0, 0, 0), LocalDateTime.of(2024, 6, 1, 0, 0, 0))).size().isEqualTo(1);
	}
	
	@Test
	public void testNewOrderWhenOrderDoesNotAlreadyExist() {
		Order order = new Order(1, LocalDateTime.of(2024, 8, 28, 0, 0, 0));
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.newOrder(order);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
	}

	@Test
	public void testNewOrderWhenOrderAlreadyExists() {
		Order orderToAdd = new Order(1, LocalDateTime.of(2024, 8, 28, 0, 0, 0));
		Order existingOrder = new Order(1, LocalDateTime.of(2024, 8, 29, 0, 0, 0));
		when(orderRepository.findById(1)).thenReturn(existingOrder);
		subscriptionsController.newOrder(orderToAdd);
		verify(orderView).showError("Already existing order with id 1", existingOrder);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}
	
	@Test
	public void testDeleteOrderWhenOrderExists() {
		Order orderToDelete = new Order(1, LocalDateTime.of(2024, 8, 29, 0, 0, 0));
		when(orderRepository.findById(1)).thenReturn(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).delete(1);
		inOrder.verify(orderView).orderRemoved(orderToDelete);
	}
	
	@Test
	public void testDeleteOrderWhenOrderDoesNotExist() {
		Order orderToDelete = new Order(1, LocalDateTime.of(2024, 8, 29, 0, 0, 0));
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).showError("No existing order with id 1", orderToDelete);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}
}
