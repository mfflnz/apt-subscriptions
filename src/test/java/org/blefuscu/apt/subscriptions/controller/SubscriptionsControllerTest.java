package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.MessageView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.mongodb.client.model.Filters;

import org.bson.conversions.Bson;

public class SubscriptionsControllerTest {

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private ListView listView;

	@Mock
	private OrderView orderView;

	@Mock
	private MessageView messageView;

	@Mock
	private ExportController exportController;

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

	@Test
	public void testFormatOrderShouldFormatOrderDate() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getOrderDate()).isEqualTo("2025-09-02");
	}

	@Test
	public void testFormatOrderShouldFormatPaidDateWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setPaidDate(null).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getPaidDate()).isEmpty();
	}

	@Test
	public void testFormatOrderShouldFormatPaidDateWhenItsNotNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setPaidDate(LocalDate.of(2025, 9, 5)).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getPaidDate()).isEqualTo("2025-09-05");
	}

	@Test
	public void testFormatOrderShouldFormatOrderTotal() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setOrderTotal(65.00).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getOrderTotal()).isEqualTo("€ 65.0");
	}

	@Test
	public void testFormatOrderShouldFormatOrderNetTotal() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setOrderNetTotal(62.50).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getOrderNetTotal()).isEqualTo("€ 62.5");
	}

	@Test
	public void testFormatOrderShouldCopyShippiungFirstNameWhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingFirstName("Anna").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingFirstName()).isEqualTo("Anna");
	}

	@Test
	public void testFormatOrderShouldFormatShippingFirstNameWhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingFirstName("").setBillingFirstName("Anna").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingFirstName()).isEqualTo("Anna");
	}

	@Test
	public void testFormatOrderShouldFormatShippingFirstNameWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingFirstName(null).setBillingFirstName("Anna").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingFirstName()).isEqualTo("Anna");
	}

	@Test
	public void testFormatOrderShouldCopyShippingLastNameWhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingLastName("Bianchi").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingLastName()).isEqualTo("Bianchi");
	}

	@Test
	public void testFormatOrderShouldFormatShippingLastNameWhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingLastName("").setBillingLastName("Bianchi").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingLastName()).isEqualTo("Bianchi");
	}

	@Test
	public void testFormatOrderShouldFormatShippingLastNameWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingFirstName(null).setBillingLastName("Bianchi").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingLastName()).isEqualTo("Bianchi");
	}

	@Test
	public void testFormatOrderShouldCopyShippingAddress1WhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingAddress1("\"viale dei Giardini, 1\"").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingAddress1()).isEqualTo("\"viale dei Giardini, 1\"");
	}

	@Test
	public void testFormatOrderShouldFormatShippingAddress1WhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingAddress1("").setBillingAddress1("\"viale dei Giardini, 1\"").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingAddress1()).isEqualTo("\"viale dei Giardini, 1\"");
	}

	@Test
	public void testFormatOrderShouldFormatShippingAddress1WhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingAddress1(null).setBillingAddress1("\"viale dei Giardini, 1\"").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingAddress1()).isEqualTo("\"viale dei Giardini, 1\"");
	}

	@Test
	public void testFormatOrderShouldCopyShippingPostcodeWhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingPostcode("01234").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingPostcode()).isEqualTo("01234");
	}

	@Test
	public void testFormatOrderShouldFormatShippingPostcodeWhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingPostcode("").setBillingPostcode("01234").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingPostcode()).isEqualTo("01234");
	}

	@Test
	public void testFormatOrderShouldFormatShippingPostcodeWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingPostcode(null).setBillingPostcode("01234").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingPostcode()).isEqualTo("01234");
	}

	@Test
	public void testFormatOrderShouldCopyShippingCityWhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingCity("Monopoli").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingCity()).isEqualTo("Monopoli");
	}

	@Test
	public void testFormatOrderShouldFormatShippingCityWhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingCity("").setBillingCity("Monopoli").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingCity()).isEqualTo("Monopoli");
	}

	@Test
	public void testFormatOrderShouldFormatShippingCityWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingCity(null).setBillingCity("Monopoli").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingCity()).isEqualTo("Monopoli");
	}

	@Test
	public void testFormatOrderShouldCopyShippingStateWhenItsNotEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingState("BA").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingState()).isEqualTo("BA");
	}

	@Test
	public void testFormatOrderShouldFormatShippingStateWhenItsEmpty() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingState("").setBillingState("BA").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingState()).isEqualTo("BA");
	}

	@Test
	public void testFormatOrderShouldFormatShippingStateWhenItsNull() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingState(null).setBillingState("BA").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingState()).isEqualTo("BA");
	}

	@Test
	public void testFormatOrderShouldCopyCustomerEmail() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getCustomerEmail()).isEqualTo("customer@address.com");
	}

	@Test
	public void testFormatOrderShouldCopyBillingPhone() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setBillingPhone("+391234567898").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getBillingPhone()).isEqualTo("+391234567898");
	}

	@Test
	public void testFormatOrderShouldFormatShippingItems() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingItems(
						"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1, Gli asini – nuova serie · 120 · maggio-giugno 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
				.build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingItems()).isEqualTo(
				"Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1, Gli asini – nuova serie · 120 · maggio-giugno 2025 &times; 1");
	}

	@Test
	public void testFormatOrderShouldNotFormatShippingItemsIfItsAlreadyFormatted() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setShippingItems(
						"Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1, Gli asini – nuova serie · 120 · maggio-giugno 2025 &times; 1")
				.build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getShippingItems()).isEqualTo(
				"Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1, Gli asini – nuova serie · 120 · maggio-giugno 2025 &times; 1");
	}

	@Test
	public void testFormatOrderShouldCopyFirstIssue() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setFirstIssue(114).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getFirstIssue()).isEqualTo("114");
	}

	@Test
	public void testFormatOrderShouldCopyLastIssue() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setLastIssue(119).build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getLastIssue()).isEqualTo("119");
	}

	@Test
	public void testFormatOrderShouldCopyCustomerNote() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setCustomerNote("Abbonamento regalato da Carla Verdi").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getCustomerNote()).isEqualTo("Abbonamento regalato da Carla Verdi");
	}

	@Test
	public void testFormatOrderShouldCopyPaymentMethodTitle() {
		Order orderToFormat = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 2), "customer@address.com")
				.setPaymentMethodTitle("Carta di credito").build();
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(orderToFormat);
		assertThat(formattedOrder.getPaymentMethodTitle()).isEqualTo("Carta di credito");
	}


	@Test
	public void testExportOrdersWhenFormattedOrdersListIsNullShouldShowAnErrorMessageAndThrow() {
		List<FormattedOrder> formattedOrders = null;
		String filename = "filename.csv";
		assertThatThrownBy(() -> subscriptionsController.exportOrders(formattedOrders, filename))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Error: no orders to export");
		verify(messageView).showErrorMessage("Error: no orders to export");
	}

	@Test
	public void testExportOrdersWhenFilenameIsNullShouldShowAnErrorMessageAndThrow() {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1);
		String filename = null;
		assertThatThrownBy(() -> subscriptionsController.exportOrders(formattedOrders, filename))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide file name");
		verify(messageView).showErrorMessage("Please provide file name");

	}

	@Test
	public void testExportOrdersWhenFormattedOrdersListIsEmptyShouldShowAnErrorMessageAndThrow() {
		List<FormattedOrder> formattedOrders = Collections.<FormattedOrder>emptyList();
		String filename = "filename.csv";
		assertThatThrownBy(() -> subscriptionsController.exportOrders(formattedOrders, filename))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Error: no orders to export");
		verify(messageView).showErrorMessage("Error: no orders to export");

	}

	@Test
	public void testExportOrdersWhenFilenameIsEmptyShouldShowAnErrorMessageAndThrow() {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1);
		String filename = "";
		assertThatThrownBy(() -> subscriptionsController.exportOrders(formattedOrders, filename))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide file name");
		verify(messageView).showErrorMessage("Please provide file name");

	}

	@Test
	public void testExportOrdersWhenFormattedOrdersListIsNotEmptyAndFilenameIsNotEmpty() throws IOException {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1);
		String filename = "export_example.csv";
		subscriptionsController.exportOrders(formattedOrders, filename);
		verify(exportController).saveData(formattedOrders, filename);
	}

	@Test
	public void testExportOrdersWhenExportControllerSaveDataThrowsAnIOException() throws IOException {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1);
		String filename = "export_example.csv";
		when(exportController.saveData(formattedOrders, filename)).thenThrow(IOException.class);
		subscriptionsController.exportOrders(formattedOrders, filename);
		verify(messageView).showErrorMessage("Error exporting file");

	}

	@Test
	public void testOrderDetailsShouldShowOrderDetailsInOrderView() {
		Order orderInDB = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "customer@address.com").build();
		when(orderRepository.findById(1)).thenReturn(orderInDB);
		Order orderForDetails = subscriptionsController.orderDetails(1);
		assertEquals(orderInDB, orderForDetails);
		verify(orderView).showOrderDetails(orderInDB);
	}

	@Test
	public void testDeleteOrderWhenOrderExistsShouldUpdateViews() {
		Order orderInDB = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "customer@address.com").build();
		when(orderRepository.findById(1)).thenReturn(orderInDB);
		subscriptionsController.deleteOrder(1);
		InOrder inOrder = Mockito.inOrder(orderRepository, orderView, listView);
		inOrder.verify(orderView).orderDeleted(orderInDB.getOrderId());
		inOrder.verify(listView).orderDeleted(orderInDB.getOrderId());
		inOrder.verify(orderRepository).delete(1);
	}

	@Test
	public void testDeleteOrderWhenOrderDoesNotExistShouldNotUpdateViews() {
		assertThatThrownBy(() -> subscriptionsController.deleteOrder(1)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("The requested order is not available");
		verify(orderRepository).findById(1);
		verifyNoMoreInteractions(orderRepository);
		verifyNoInteractions(listView, orderView);
	}

	@Test
	public void testUpdateOrderWhenOrderExistsShouldUpdateViews() {
		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "updated_customer@address.com")
				.build();
		when(orderRepository.findById(1)).thenReturn(updatedOrder);
		subscriptionsController.updateOrder(1, updatedOrder);
		InOrder inOrder = Mockito.inOrder(orderRepository, orderView, listView);
		inOrder.verify(orderRepository).update(1, updatedOrder);
		inOrder.verify(orderView).orderUpdated(1);
		inOrder.verify(listView).orderUpdated(1, updatedOrder);
	}

	@Test
	public void testUpdateOrderWhenOrderDoesNotExistShouldNotUpdateViews() {
		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 1), "updated_customer@address.com")
				.build();
		when(orderRepository.findById(1)).thenReturn(null);
		assertThatThrownBy(() -> subscriptionsController.updateOrder(1, updatedOrder))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("The requested order is not available");
		verify(orderRepository).findById(1);
		verifyNoMoreInteractions(orderRepository);
		verifyNoInteractions(listView, orderView);

	}

	@Test
	public void testRequestOrdersWhenToDateIsEarlierThanFromDateShouldSendErrorMessageToTheMessageView() {
		LocalDate fromDate = LocalDate.of(2025, 9, 2);
		LocalDate toDate = LocalDate.of(2025, 9, 1);
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Start date should be earlier or equal to end date");
		verify(messageView).showErrorMessage("Start date should be earlier or equal to end date");

	}

	@Test
	public void testRequestOrdersWhenFromDateIsNullShouldSendErrorMessageToTheMessageView() {
		LocalDate fromDate = null;
		LocalDate toDate = LocalDate.of(2025, 9, 1);
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide start date");
		verify(messageView).showErrorMessage("Please provide start date");

	}

	@Test
	public void testRequestOrdersWhenToDateIsNullShouldSendErrorMessageToTheMessageView() {
		LocalDate fromDate = LocalDate.of(2025, 9, 1);
		LocalDate toDate = null;
		assertThatThrownBy(() -> subscriptionsController.requestOrders(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Please provide end date");
		verify(messageView).showErrorMessage("Please provide end date");

	}

	@Test
	public void testSendErrorMessageShouldDelegateMessageViewToShowTheErrorMessage() {
		subscriptionsController.sendErrorMessage("Error Message");
		verify(messageView).showErrorMessage("Error Message");
		verifyNoMoreInteractions(messageView);
	}

	@Test
	public void testSendInfoMessageShouldDelegateMessageViewToShowTheInfoMessage() {
		subscriptionsController.sendInfoMessage("Info Message");
		verify(messageView).showInfoMessage("Info Message");
		verifyNoMoreInteractions(messageView);
	}
	
	@Test
	public void testRequestOrdersShouldDelegateMessageViewToShowNumberOfOrders() {
		LocalDate fromDate = LocalDate.of(2025, 8, 25);
		LocalDate toDate = LocalDate.of(2025, 8, 26);
		Bson filter = Filters.and(Filters.gte("order_date", fromDate.toString()), Filters.lte("order_date", toDate.toString()));
		
		when(orderRepository.countOrders(filter)).thenReturn((long) 2);
		
		subscriptionsController.requestOrders(fromDate, toDate);
		
		verify(messageView).showInfoMessage("2 orders found");
	}
	
	@Test
	public void testRequestOrdersWhenExactlyOneOrderIsFoundShouldDelegateMessageViewToShowNumberOfOrders() {
		LocalDate fromDate = LocalDate.of(2025, 8, 25);
		LocalDate toDate = LocalDate.of(2025, 8, 26);
		Bson filter = Filters.and(Filters.gte("order_date", fromDate.toString()), Filters.lte("order_date", toDate.toString()));
		
		when(orderRepository.countOrders(filter)).thenReturn((long) 1);
		
		subscriptionsController.requestOrders(fromDate, toDate);
		
		verify(messageView).showInfoMessage("1 order found");
	}

	@Test
	public void testRequestOrdersWhenCollectionContainsExactlyOneOrderShouldDelegateMessageViewToShowNumberOfOrders() {
		
		when(orderRepository.countOrders(null)).thenReturn((long) 1);
		
		subscriptionsController.requestOrders();
		
		verify(messageView).showInfoMessage("1 order found");
	}
	
	@Test
	public void testRequestOrdersShouldDelegateTheListViewToClearTheListBeforeDisplayingResults() {
		subscriptionsController.requestOrders();
		verify(orderView).clearAll();
		verify(listView).clearList();

	}

	@Test
	public void testRequestOrdersWhenDatesAreProvidedShouldDelegateTheListViewToClearTheListBeforeDisplayingResults() {
		LocalDate fromDate = LocalDate.of(2025, 8, 25);
		LocalDate toDate = LocalDate.of(2025, 8, 26);
		subscriptionsController.requestOrders(fromDate, toDate);
		verify(orderView).clearAll();
		verify(listView).clearList();
		
	}

}