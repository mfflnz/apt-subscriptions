package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static java.util.Arrays.asList;

import java.time.LocalDateTime;
import java.util.List;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SubscriptionsControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderView orderView;
	
	@Mock
	private ListView listView;
	
	private AutoCloseable closeable;

	@InjectMocks
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		orderRepository = mock(OrderRepository.class);
		subscriptionsController = new SubscriptionsController(orderView, listView, orderRepository);
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testRequestOrders() {
		List<Order> orders = asList(new Order());
		when(orderRepository.findAll()).thenReturn(orders);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(orders);
	}
	
	@Test
	public void testRequestOrdersWhenDateRangeIsProvided() {
		List<Order> orders = asList(new Order());
		LocalDateTime fromDate = LocalDateTime.of(2024, 8, 15, 0, 0, 0);
		LocalDateTime toDate = LocalDateTime.of(2024, 8, 30, 0, 0, 0);
		when(orderRepository.findByDateRange(fromDate, toDate)).thenReturn(orders);
		subscriptionsController.requestOrders(fromDate, toDate);
		verify(listView).showOrders(orders);
	}
	@Test
	public void testRequestOrdersWhenStartDateIsMissingShouldThrow() {
	}
	
	@Test
	public void testRequestOrdersWhenEndDateIsMissingShouldThrow() {
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
