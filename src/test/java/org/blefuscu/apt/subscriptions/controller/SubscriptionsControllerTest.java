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
		Files.deleteIfExists(Paths.get("export.csv"));
	}

	@After
	public void releaseMocks() throws Exception {
		closeable.close();
	}

	@Test
	public void testRequestOrders() {
		List<Order> orders = asList(
				new Order.OrderBuilder(1, LocalDate.of(2024, 12, 18), 65.00, "Bonifico", "Abbonamento annuale cartaceo",
						"test@email.com").build(),
				new Order.OrderBuilder(2, LocalDate.of(2024, 12, 19), 65.00, "Bonifico", "Abbonamento annuale cartaceo",
						"other@email.com").build());
		when(orderRepository.findAll()).thenReturn(orders);
		subscriptionsController.requestOrders();
		verify(listView).showOrders(orders);
	}

	@Test
	public void testRequestOrdersWhenDateRangeIsProvided() {
		List<Order> orders = asList(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 18), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build());
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
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.newOrder(order);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).save(order);
		inOrder.verify(orderView).orderAdded(order);
	}

	@Test
	public void testNewOrderWhenOrderAlreadyExists() {
		Order orderToAdd = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order existingOrder = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(existingOrder);
		subscriptionsController.newOrder(orderToAdd);
		verify(orderView).showError("Already existing order with id 1", existingOrder);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testDeleteOrderWhenOrderExists() {
		Order orderToDelete = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(orderToDelete);
		subscriptionsController.deleteOrder(orderToDelete);
		InOrder inOrder = inOrder(orderRepository, orderView, listView);
		inOrder.verify(orderRepository).delete(1);
		inOrder.verify(orderView).orderRemoved(orderToDelete);
		inOrder.verify(listView).orderRemoved(orderToDelete);
	}

	@Test
	public void testDeleteOrderWhenOrderDoesNotExist() {
		Order orderToDelete = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.deleteOrder(orderToDelete);
		verify(orderView).showError("No existing order with id 1", orderToDelete);
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testUpdateOrderWhenOrderExists() {
		Order orderToUpdate = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order orderWithUpdatedValues = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(orderToUpdate);
		subscriptionsController.updateOrder(1, orderWithUpdatedValues);
		InOrder inOrder = inOrder(orderRepository, orderView);
		inOrder.verify(orderRepository).edit(1, orderWithUpdatedValues);
		inOrder.verify(orderView).showOrderDetails(orderWithUpdatedValues);
	}

	@Test
	public void testUpdateOrderWhenOrderDoesNotExist() {
		Order orderWithUpdatedValues = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		when(orderRepository.findById(1)).thenReturn(null);
		subscriptionsController.updateOrder(1, orderWithUpdatedValues);
		verify(orderView).showError("No existing order with id 1");
		verifyNoMoreInteractions(ignoreStubs(orderRepository));
	}

	@Test
	public void testOrderDetailsShouldFetchOrderDetailsFromRepository() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		orderRepository.save(order);
		when(orderRepository.findById(1)).thenReturn(order);
		assertThat(subscriptionsController.orderDetails(1)).isEqualTo(order);
	}

	@Test
	public void testExportOrdersShouldWriteOrdersListToDisk() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "livia@email.com")
				.setDepositDate(LocalDate.of(2024, 9, 1))
				.setNetOrderTotal(63.50)		
				.setShippingFirstName("Livia")		
				.setShippingLastName("Apa")		
				.setShippingAddress1("via dei Tribunali 32")		
				.setShippingPostcode("80138")		
				.setShippingCity("Napoli")		
				.setShippingState("NA")		
				.setShippingPhone("+393287654321")		
				.setFirstIssue(118)		
				.setLastIssue(123)		
				.setNotes("Abbonamento regalato da Fabrizia Ramondino")		
				.build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "gianluca@email.com")
				.setDepositDate(LocalDate.of(2024, 9, 6))
				.setNetOrderTotal(63.40)		
				.setShippingFirstName("Gianluca")		
				.setShippingLastName("D'Errico")		
				.setShippingAddress1("piazza Dante, 4")		
				.setShippingPostcode("80138")		
				.setShippingCity("Napoli")		
				.setShippingState("NA")		
				.setShippingPhone("+393398765432")		
				.setFirstIssue(118)		
				.setLastIssue(124)		
				.setNotes("Prolunghiamo abbonamento di un numero per rinnovo anticipato")				
				.build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		System.out.println(order1);
		System.out.println(order2);
		String filename = "export.csv";
		when(orderRepository.findAll()).thenReturn(asList(order1, order2));
		List<Order> ordersToSave = orderRepository.findAll();

		try {
			Files.deleteIfExists(Paths.get("export.csv"));
			//subscriptionsController.exportOrders(filename, ordersToSave);
			assertThat(subscriptionsController.exportOrders(filename, ordersToSave)).isEqualTo(1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertThat(new File("export.csv")).exists();
		assertThat(new File("export.csv")).hasContent(
				"1,2024-08-28,2024-09-01,65.0,63.5,Bonifico,Livia,Apa,\"via dei Tribunali 32\",80138,Napoli,NA,livia@email.com,+393287654321,Abbonamento annuale cartaceo,118,123,Abbonamento regalato da Fabrizia Ramondino\n2,2024-08-29,2024-09-06,65.0,63.4,Bonifico,Gianluca,D'Errico,\"piazza Dante, 4\",80138,Napoli,NA,gianluca@email.com,+393398765432,Abbonamento annuale cartaceo,118,124,Prolunghiamo abbonamento di un numero per rinnovo anticipato");

	}

	@Test
	public void testExportOrdersShouldReturnANegativeValueIfAnExceptionIsThrown() throws IOException {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		String filename = "export.csv";
		when(orderRepository.findAll()).thenReturn(asList(order1, order2));
		List<Order> ordersToSave = orderRepository.findAll();
		Files.createFile(Paths.get(filename));

		// This throws the exception
		assertThatThrownBy(() -> Files.createFile(Paths.get("export.csv"))).isInstanceOf(IOException.class)
				.hasStackTraceContaining("export.csv");

		// when(subscriptionsController.exportOrders(filename,
		// ordersToSave)).thenThrow(IOException.class);
		// assertThatThrownBy(() -> subscriptionsController.exportOrders(filename,
		// ordersToSave)).isInstanceOf(IOException.class);
		assertThat(subscriptionsController.exportOrders(filename, ordersToSave)).isEqualTo(-1);

	}

	@Test
	public void testRequestOrdersShouldPassTheOrdersListToTheListView() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		when(orderRepository.findByDateRange(LocalDate.of(2024, 8, 28), LocalDate.of(2024, 8, 29)))
				.thenReturn(asList(order1, order2));
		subscriptionsController.requestOrders(LocalDate.of(2024, 8, 28), LocalDate.of(2024, 8, 29));
		verify(listView).showOrders(asList(order1, order2));
	}

	@Test
	public void testRequestOrdersShouldPassAllOrdersListToTheListViewIfNoDatesAreProvided() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 8, 28), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 8, 29), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 8, 30), 65.00, "Bonifico",
				"Abbonamento annuale cartaceo", "test@email.com").build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		when(orderRepository.findAll()).thenReturn(asList(order1, order2, order3));
		subscriptionsController.requestOrders();
		verify(listView).showOrders(asList(order1, order2, order3));
	}

}
