package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static java.util.Arrays.asList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
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
		orderView = mock(OrderView.class);
		listView = mock(ListView.class);
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
		verify(listView).showAllOrders(orders);
	}

	@Test
	public void testRequestOrdersWhenDateRangeIsProvided() {
		List<Order> orders = asList(new Order());
		LocalDate fromDate = LocalDate.of(2024, 8, 15);
		LocalDate toDate = LocalDate.of(2024, 8, 30);
		when(orderRepository.findByDateRange(fromDate, toDate)).thenReturn(orders);
		subscriptionsController.requestOrders(fromDate, toDate);
		verify(listView).showOrders(orders);
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
	public void testRequestOrdersWhenEndDateIsStrictlyEarlierThanStartDateShouldThrow() {
		LocalDate fromDate = LocalDate.now();
		LocalDate toDate = LocalDate.of(2024, 1, 1);
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Start date should be earlier or equal to end date");
	}

	@Test
	public void testNewOrderWhenOrderDoesNotAlreadyExist() {
		Order order = new Order(1, LocalDate.of(2024, 8, 28));
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.newOrder(order);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
	}

	@Test
	public void testestNewOrderWhenOrderDoesNotAlreadyExisttNewOrderWhenOrderAlreadyExists() {
		Order orderToAdd = new Order(1, LocalDate.of(2024, 8, 28));
		Order existingOrder = new Order(1, LocalDate.of(2024, 8, 29));
		when(orderRepository.findById(1)).thenReturn(existingOrder);
		subscriptionsController.newOrder(orderToAdd);
		verify(orderView).showError("Already existing order with id 1", existingOrder);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderWhenOrderExists() {
		Order orderToDelete = new Order(1, LocalDate.of(2024, 8, 29));
		when(orderRepository.findById(1)).thenReturn(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).delete(1);
		inOrder.verify(orderView).orderRemoved(orderToDelete);
	}

	@Test
	public void testDeleteOrderWhenOrderDoesNotExist() {
		Order orderToDelete = new Order(1, LocalDate.of(2024, 8, 29));
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).showError("No existing order with id 1", orderToDelete);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testOrderDetailsShouldFetchOrderDetailsFromRepository() {
		Order order = new Order(1, LocalDate.of(2024, 8, 28));
		orderRepository.save(order);
		when(orderRepository.findById(1)).thenReturn(order);
		assertThat(subscriptionsController.orderDetails(1)).isEqualTo(order);
	}

	@Test
	public void testExportOrdersShouldWriteOrdersListToDisk() {
		Order order1 = new Order(1, LocalDate.of(2024, 8, 28));
		Order order2 = new Order(2, LocalDate.of(2024, 8, 29));
		orderRepository.save(order1);
		orderRepository.save(order2);
		String filename = "export.csv";
		when(orderRepository.findAll()).thenReturn(asList(order1, order2));
		List<Order> ordersToSave = orderRepository.findAll();

		try {
			Files.deleteIfExists(Paths.get("export.csv"));
			// subscriptionsController.exportOrders(filename, ordersToSave);
			assertThat(subscriptionsController.exportOrders(filename, ordersToSave)).isEqualTo(1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertThat(new File("export.csv")).exists();
		assertThat(new File("export.csv")).hasContent("1,2024-08-28\n2,2024-08-29");

	}

	@Test
	public void testRequestOrdersShouldPassTheOrdersListToTheListView() {
		Order order1 = new Order(1, LocalDate.of(2024, 8, 28));
		Order order2 = new Order(2, LocalDate.of(2024, 8, 29));
		orderRepository.save(order1);
		orderRepository.save(order2);
		when(orderRepository.findByDateRange(LocalDate.of(2024, 8, 28), LocalDate.of(2024, 8, 29)))
				.thenReturn(asList(order1, order2));
		subscriptionsController.requestOrders(LocalDate.of(2024, 8, 28), LocalDate.of(2024, 8, 29));
		verify(listView).showOrders(asList(order1, order2));
	}
	
	@Test
	public void testRequestOrdersShouldPassAllOrdersListToTheListViewIfNoDatesAreProvided() {
		Order order1 = new Order(1, LocalDate.of(2024, 8, 28));
		Order order2 = new Order(2, LocalDate.of(2024, 8, 29));
		Order order3 = new Order(3, LocalDate.of(2024, 8, 30));
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		when(orderRepository.findAll())
				.thenReturn(asList(order1, order2, order3));
		subscriptionsController.requestOrders();
		verify(listView).showAllOrders(asList(order1, order2, order3));
	}

}
