package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.EmergencyAbortListener;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderSwingViewTest {

	private static final int TIMEOUT = 20000;
	private OrderSwingView orderSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;

	// https://joel-costigliola.github.io/assertj/assertj-swing-running.html
	private EmergencyAbortListener listener;

	@Mock
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);
		listener = EmergencyAbortListener.registerInToolkit();

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderSwingView.setSubscriptionsController(subscriptionsController);
			return orderSwingView;
		});

		window = showInFrame(orderSwingView);
	}

	@After
	public void tearDown() throws Exception {
		listener.unregister();
		window.cleanUp();
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.textBox("orderIdTextBox").requireEditable();
		window.textBox("orderDateTextBox").requireEditable();
		window.textBox("orderTotalTextBox").requireEditable();
		window.textBox("netTotalTextBox").requireEditable();
		window.textBox("paymentMethodTextBox").requireEditable();
		window.textBox("firstNameTextBox").requireEditable();
		window.textBox("lastNameTextBox").requireEditable();
		window.textBox("addressTextBox").requireEditable();
		window.textBox("postcodeTextBox").requireEditable();
		window.textBox("stateTextBox").requireEditable();
		window.textBox("cityTextBox").requireEditable();
		window.textBox("emailTextBox").requireEditable();
		window.textBox("phoneTextBox").requireEditable();
		window.textBox("productTextBox").requireEditable();
		window.textBox("firstIssueTextBox").requireEditable();
		window.textBox("lastIssueTextBox").requireEditable();
		window.textBox("notesTextBox").requireEditable();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();

	}

	@Test
	@GUITest
	public void testIfAndOnlyIfAllOfOrderIdOrderDateAndEmailAreNotEmptyThenTheUpdateAndDeleteButtonsShouldBeEnabled() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("orderIdTextBox").deleteText();

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testOrderIdFieldShouldAllowOnlyNumbers() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("R");

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testOrderDateFieldShouldAllowOnlyFormattedDates() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("202511-05");

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testEmailFieldShouldAllowOnlyFormattedEmailAddresses() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customeremail.com");

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	public void testUpdateButtonShouldDelegateToSubscriptionsController() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Update")).click();

		Order updatedOrder = new Order.OrderBuilder(4, LocalDate.of(2025, 11, 05), "customer@email.com").build();
		verify(subscriptionsController, timeout(TIMEOUT)).updateOrder(4, updatedOrder);
		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("4");
		assertThat(window.textBox("orderDateTextBox").text()).isEqualTo("2025-11-05");
		assertThat(window.textBox("emailTextBox").text()).isEqualTo("customer@email.com");

	}

	@Test
	public void testDeleteButtonShouldDelegateToSubscriptionsController() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("425");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-06");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

		window.button(JButtonMatcher.withText("Delete")).click();

		verify(subscriptionsController, timeout(TIMEOUT)).deleteOrder(425);

	}

	@Test
	public void testOrderUpdatedShouldDelegateTheMessageViewToDisplayAnInfoMessage() {
		orderSwingView.orderUpdated(2);
		verify(subscriptionsController).sendInfoMessage("Order n. 2 has been correctly updated");
	}

	@Test
	public void testOrderDeletedShouldDelegateTheMessageViewToDisplayAnInfoMessage() {
		orderSwingView.orderDeleted(263);
		verify(subscriptionsController).sendInfoMessage("Order n. 263 has been deleted");
	}

	@Test
	public void testShowOrderDetailsShouldShowFormattedOrderFieldValuesInTheOrderViewTextFields() {
		Order order = createSampleOrder();
		FormattedOrder formattedOrder = createSampleFormattedOrder();
		when(subscriptionsController.formatOrder(order)).thenReturn(formattedOrder);

		orderSwingView.showOrderDetails(order);

		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("1");
		assertThat(window.textBox("orderDateTextBox").text()).isEqualTo("2025-10-09");
		assertThat(window.textBox("paidDateTextBox").text()).isEqualTo("2025-11-03");
		assertThat(window.textBox("orderTotalTextBox").text()).isEqualTo("€ 65.00");
		assertThat(window.textBox("netTotalTextBox").text()).isEqualTo("€ 58.00");
		assertThat(window.textBox("paymentMethodTextBox").text()).isEqualTo("PayPal");
		assertThat(window.textBox("firstNameTextBox").text()).isEqualTo("Ada");
		assertThat(window.textBox("lastNameTextBox").text()).isEqualTo("Perrotta");
		assertThat(window.textBox("addressTextBox").text()).isEqualTo("via Irnerio 51");
		assertThat(window.textBox("postcodeTextBox").text()).isEqualTo("40125");
		assertThat(window.textBox("stateTextBox").text()).isEqualTo("BO");
		assertThat(window.textBox("cityTextBox").text()).isEqualTo("Bologna");
		assertThat(window.textBox("emailTextBox").text()).isEqualTo("customer@emailaddress.com");
		assertThat(window.textBox("phoneTextBox").text()).isEqualTo("+39 321 456 7890");
		assertThat(window.textBox("productTextBox").text()).isEqualTo("Abbonamento cartaceo");
		assertThat(window.textBox("firstIssueTextBox").text()).isEqualTo("106");
		assertThat(window.textBox("lastIssueTextBox").text()).isEqualTo("111");
		assertThat(window.textBox("notesTextBox").text()).isEqualTo("Abbonamento regalato da Gianluca Boarelli");

		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

	}

	private Order createSampleOrder() {
		return new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com")
				.setPaidDate(LocalDate.of(2025, 11, 3)).setOrderTotal(65.00).setOrderNetTotal(58.00)
				.setPaymentMethod("PayPal").setBillingFirstName("Ada").setBillingLastName("Perrotta")
				.setBillingAddress1("via Irnerio 51").setBillingPostcode("40125").setBillingState("BO")
				.setBillingCity("Bologna").setBillingEmail("customer@emailaddress.com")
				.setBillingPhone("+39 321 456 7890").setShippingItems("Abbonamento cartaceo").build();
	}

	private FormattedOrder createSampleFormattedOrder() {
		return new FormattedOrder.FormattedOrderBuilder("1").setOrderDate("2025-10-09").setPaidDate("2025-11-03")
				.setOrderTotal("€ 65.00").setOrderNetTotal("€ 58.00").setPaymentMethodTitle("PayPal")
				.setShippingFirstName("Ada").setShippingLastName("Perrotta").setShippingAddress1("via Irnerio 51")
				.setShippingPostcode("40125").setShippingCity("Bologna").setShippingState("BO")
				.setCustomerEmail("customer@emailaddress.com").setBillingPhone("+39 321 456 7890")
				.setShippingItems("Abbonamento cartaceo").setFirstIssue("106").setLastIssue("111")
				.setCustomerNote("Abbonamento regalato da Gianluca Boarelli").build();
	}
}
