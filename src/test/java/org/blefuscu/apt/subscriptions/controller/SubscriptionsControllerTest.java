package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
		subscriptionsController = new SubscriptionsController(listView, orderRepository);
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

	@Test
	public void testRequestOrdersWhenDatesAreProvided() {
		List<Order> orders = asList(
				new Order.OrderBuilder(1, LocalDate.of(2025, 8, 25), "customer_1@address.com").build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 8, 26), "customer_2@address.com").build(),
				new Order.OrderBuilder(3, LocalDate.of(2025, 8, 27), "customer_3@address.com").build());
		LocalDate fromDate = LocalDate.of(2025, 8, 25);
		LocalDate toDate = LocalDate.of(2025, 8, 26);
		when(orderRepository.findByDateRange(fromDate, toDate)).thenReturn(asList(orders.get(0), orders.get(1)));
		subscriptionsController.requestOrders(fromDate, toDate);
		verify(listView).showOrders(asList(orders.get(0), orders.get(1)));
	}

	@Test
	public void testRequestOrdersWhenStartDateIsNullShouldThrow() {
		LocalDate fromDate = null;
		LocalDate toDate = LocalDate.now();
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide start date");
	}

	@Test
	public void testRequestOrdersWhenEndDateIsNullShouldThrow() {
		LocalDate fromDate = LocalDate.now();
		LocalDate toDate = null;
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide end date");
	}

	@Test
	public void testRequestOrdersWhenEndDateIsEarlierThanStartDateShouldThrow() {
		LocalDate fromDate = LocalDate.of(2025, 9, 2);
		LocalDate toDate = LocalDate.of(2025, 9, 1);
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Start date should be earlier or equal to end date");
	}

	@Test
	public void testOrderDetailsWhenOrderExistsShouldReturnGivenOrder() {
		Order orderInDB = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "customer@address.com").build();
		when(orderRepository.findById(1)).thenReturn(orderInDB);
		Order orderForDetails = subscriptionsController.orderDetails(1);
		assertEquals(orderInDB, orderForDetails);
	}

	@Test
	public void testOrderDetailsWhenOrderDoesNotExistShouldThrow() {
		when(orderRepository.findById(1)).thenReturn(null);
		assertThatThrownBy(() -> subscriptionsController.orderDetails(1)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The requested order is not available");
	}

	@Test
	public void testDeleteOrderWhenOrderExists() {
		Order orderInDB = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "customer@address.com").build();
		when(orderRepository.findById(1)).thenReturn(orderInDB);
		subscriptionsController.deleteOrder(1);
		verify(orderRepository).delete(1);
	}

	@Test
	public void testDeleteOrderWhenOrderDoesNotExist() {
		assertThatThrownBy(() -> subscriptionsController.deleteOrder(1)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The requested order is not available");
		verify(orderRepository).findById(1);
		verifyNoMoreInteractions(orderRepository);
	}

	@Test
	public void testUpdateOrderWhenOrderExists() {
		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "updated_customer@address.com")
				.build();
		when(orderRepository.findById(1)).thenReturn(updatedOrder);
		subscriptionsController.updateOrder(1, updatedOrder);
		verify(orderRepository).update(1, updatedOrder);
	}

	@Test
	public void testUpdateOrderWhenOrderDoesNotExist() {
		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "updated_customer@address.com")
				.build();
		when(orderRepository.findById(1)).thenReturn(null);
		assertThatThrownBy(() -> subscriptionsController.updateOrder(1, updatedOrder))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("The requested order is not available");
		verify(orderRepository).findById(1);
		verifyNoMoreInteractions(orderRepository);

	}

}
