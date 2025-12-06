package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.awaitility.Awaitility.*;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.model.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class OrderSwingViewTest {

	private static final int TIMEOUT = 10000;

	private OrderSwingView orderSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;

	@Mock
	private ListSwingView listView;

	@Mock
	private SubscriptionsController subscriptionsController;

	public Order updatedOrder1 = new Order.OrderBuilder(4, LocalDate.of(2025, 11, 05), "customer@email.com")
			.setOrderTotal(16.5).build();
	public Order updatedOrder2 = new Order.OrderBuilder(4, LocalDate.of(2025, 11, 05), "customer@email.com")
			.setOrderTotal(0).setOrderNetTotal(14.5).setFirstIssue(106).setLastIssue(112)
			.setPaidDate(LocalDate.of(2025, 11, 8)).build();

	// https://joel-costigliola.github.io/assertj/assertj-swing-edt.html#testing-violations
	@BeforeClass
	public static void setUpOnce() {
		FailOnThreadViolationRepaintManager.install();
	}

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderSwingView.setSubscriptionsController(subscriptionsController);
			return orderSwingView;
		});

		window = showInFrame(orderSwingView);
	}

	@After
	public void tearDown() throws Exception {

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
	public void testIfAllOfOrderIdOrderDateAndEmailAreNotEmptyThenTheUpdateAndDeleteButtonsShouldBeEnabled() {
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
		window.textBox("orderIdTextBox").enterText("R");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");

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
		window.textBox("orderTotalTextBox").deleteText();
		window.textBox("orderTotalTextBox").enterText("€ 16.5");

		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Update")).isEnabled());

		GuiActionRunner.execute(() -> {
			orderSwingView.getBtnUpdate().doClick();
		});

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(subscriptionsController, timeout(TIMEOUT)).updateOrder(4, updatedOrder1));

		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("4");
		assertThat(window.textBox("orderDateTextBox").text()).isEqualTo("2025-11-05");
		assertThat(window.textBox("emailTextBox").text()).isEqualTo("customer@email.com");

	}

	@Test
	public void testUpdateButtonWithTextBoxesToBeCleanedShouldDelegateToSubscriptionsController() {

		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("4");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-11-05");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("customer@email.com");
		window.textBox("orderTotalTextBox").deleteText();
		window.textBox("netTotalTextBox").deleteText();
		window.textBox("netTotalTextBox").enterText("€ 14.5");
		window.textBox("firstIssueTextBox").deleteText();
		window.textBox("firstIssueTextBox").enterText("106");
		window.textBox("lastIssueTextBox").deleteText();
		window.textBox("lastIssueTextBox").enterText("112");
		window.textBox("paidDateTextBox").deleteText();
		window.textBox("paidDateTextBox").enterText("2025-11-08");

		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Update")).isEnabled());

		GuiActionRunner.execute(() -> {
			orderSwingView.getBtnUpdate().doClick();
		});

		await().atMost(5, TimeUnit.SECONDS)
				.untilAsserted(() -> verify(subscriptionsController).updateOrder(4, updatedOrder2));

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

		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Delete")).isEnabled());

		GuiActionRunner.execute(() -> {
			orderSwingView.getBtnDelete().doClick();
		});

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

		GuiActionRunner.execute(() -> {
			orderSwingView.showOrderDetails(order);
		});

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

	@Test
	public void testShowOrderDetailsShouldDisableOrderIdTextBox() {

		Order order = createSampleOrder();
		FormattedOrder formattedOrder = createSampleFormattedOrder();
		when(subscriptionsController.formatOrder(order)).thenReturn(formattedOrder);

		GuiActionRunner.execute(() -> {
			orderSwingView.showOrderDetails(order);
		});

		window.textBox("orderIdTextBox").requireDisabled();

	}

	@Test
	public void testClearAllShouldResetAllTextFields() {
		window.textBox("orderIdTextBox").deleteText();
		window.textBox("orderIdTextBox").enterText("123");
		window.textBox("orderDateTextBox").deleteText();
		window.textBox("orderDateTextBox").enterText("2025-12-01");
		window.textBox("paidDateTextBox").deleteText();
		window.textBox("paidDateTextBox").enterText("2025-12-03");
		window.textBox("orderTotalTextBox").deleteText();
		window.textBox("orderTotalTextBox").enterText("€ 16,50");
		window.textBox("netTotalTextBox").deleteText();
		window.textBox("netTotalTextBox").enterText("€ 14,50");
		window.textBox("paymentMethodTextBox").deleteText();
		window.textBox("paymentMethodTextBox").enterText("Bonifico");
		window.textBox("firstNameTextBox").deleteText();
		window.textBox("firstNameTextBox").enterText("Grazia");
		window.textBox("lastNameTextBox").deleteText();
		window.textBox("lastNameTextBox").enterText("Fresco");
		window.textBox("addressTextBox").deleteText();
		window.textBox("addressTextBox").enterText("via Mazzini 3");
		window.textBox("postcodeTextBox").deleteText();
		window.textBox("postcodeTextBox").enterText("00185");
		window.textBox("stateTextBox").deleteText();
		window.textBox("stateTextBox").enterText("RM");
		window.textBox("cityTextBox").deleteText();
		window.textBox("cityTextBox").enterText("Roma");
		window.textBox("emailTextBox").deleteText();
		window.textBox("emailTextBox").enterText("indirizzo@email.com");
		window.textBox("phoneTextBox").deleteText();
		window.textBox("phoneTextBox").enterText("+39 333 1122 333");
		window.textBox("productTextBox").deleteText();
		window.textBox("productTextBox").enterText("Abbonamento sostenitore");
		window.textBox("firstIssueTextBox").deleteText();
		window.textBox("firstIssueTextBox").enterText("110");
		window.textBox("lastIssueTextBox").deleteText();
		window.textBox("lastIssueTextBox").enterText("116");
		window.textBox("notesTextBox").deleteText();
		window.textBox("notesTextBox").enterText("Regalato da Carla Vannucci");

		GuiActionRunner.execute(() -> {
			orderSwingView.clearAll();
		});

		assertThat(window.textBox("orderIdTextBox").text()).isEmpty();
		assertThat(window.textBox("orderDateTextBox").text()).isEmpty();
		assertThat(window.textBox("paidDateTextBox").text()).isEmpty();
		assertThat(window.textBox("orderTotalTextBox").text()).isEmpty();
		assertThat(window.textBox("netTotalTextBox").text()).isEmpty();
		assertThat(window.textBox("paymentMethodTextBox").text()).isEmpty();
		assertThat(window.textBox("firstNameTextBox").text()).isEmpty();
		assertThat(window.textBox("lastNameTextBox").text()).isEmpty();
		assertThat(window.textBox("addressTextBox").text()).isEmpty();
		assertThat(window.textBox("postcodeTextBox").text()).isEmpty();
		assertThat(window.textBox("stateTextBox").text()).isEmpty();
		assertThat(window.textBox("cityTextBox").text()).isEmpty();
		assertThat(window.textBox("emailTextBox").text()).isEmpty();
		assertThat(window.textBox("phoneTextBox").text()).isEmpty();
		assertThat(window.textBox("productTextBox").text()).isEmpty();
		assertThat(window.textBox("firstIssueTextBox").text()).isEmpty();
		assertThat(window.textBox("lastIssueTextBox").text()).isEmpty();
		assertThat(window.textBox("notesTextBox").text()).isEmpty();

		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();

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
