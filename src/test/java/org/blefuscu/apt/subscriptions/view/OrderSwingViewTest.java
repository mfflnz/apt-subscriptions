package org.blefuscu.apt.subscriptions.view;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.model.Order.OrderBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OrderSwingViewTest {

	private static final int TIMEOUT = 5000;
	private OrderSwingView orderSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;
	
	@Mock
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() throws Exception {
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

		verify(subscriptionsController, timeout(TIMEOUT)).updateOrder(4, new Order.OrderBuilder(4, LocalDate.of(2025, 11, 05), "customer@email.com").build());

	}

}
