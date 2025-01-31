package org.blefuscu.apt.subscriptions.view;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.time.LocalDate;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class OrderSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private OrderSwingView orderSwingView;
	private AutoCloseable closeable;

	private static final int TIMEOUT = 5000;

	@Mock
	private SubscriptionsController subscriptionsController;

	@Override
	protected void onSetUp() throws Exception {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			orderSwingView.setSubscriptionsController(subscriptionsController);
			return orderSwingView;
		});

		window = new FrameFixture(robot(), orderSwingView);
		window.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id *"));
		window.textBox("idTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Order Date *"));
		window.textBox("orderDateTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Credit Date"));
		window.textBox("creditDateTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Gross *"));
		window.textBox("grossTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Net"));
		window.textBox("netTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Payment *"));
		window.textBox("paymentTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Product *"));
		window.textBox("productTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("First Issue"));
		window.textBox("firstIssueTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Last Issue"));
		window.textBox("lastIssueTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("First Name"));
		window.textBox("firstNameTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Last Name"));
		window.textBox("lastNameTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Address"));
		window.textBox("addressTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Zip Code"));
		window.textBox("zipCodeTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("City"));
		window.textBox("cityTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Province"));
		window.textBox("provinceTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Country"));
		window.textBox("countryTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Email *"));
		window.textBox("emailTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Phone"));
		window.textBox("phoneTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Notes"));
		window.textBox("notesTextArea").requireEnabled().requireNotEditable();
		window.checkBox("confirmedCheckBox");
		window.checkBox("unlockCheckBox");
		window.textBox("messagesTextBox");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeEnabledOnlyIfRequiredFieldsAreNotEmptyAndOrderIsUnlocked() {
		window.checkBox("unlockCheckBox").check();

		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");

		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		// Uncheck and check again with required fields already filled
		window.checkBox("unlockCheckBox").uncheck();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.checkBox("unlockCheckBox").check();
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("idTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("idTextBox").setText("1");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("orderDateTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("productTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("grossTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("grossTextBox").setText("€65,00");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("paymentTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("paymentTextBox").setText("Bonifico");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.textBox("emailTextBox").deleteText();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.textBox("emailTextBox").setText("user@email.com");
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

	}

	@Test
	@GUITest
	public void testButtonsShouldBeDisabledIfARequiredFieldsIsEmpty() {
		window.textBox("idTextBox").deleteText();
		window.checkBox("unlockCheckBox").check();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testButtonsShouldBeDisabledIfOrderIsLocked() {
		window.checkBox("unlockCheckBox").check();
		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");
		window.checkBox("unlockCheckBox").uncheck();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test
	@GUITest
	public void testTextFieldsShouldBeEditableIfOrderIsUnlockedAndNotEditableIfOrderIsLocked() {
		window.checkBox("unlockCheckBox").check();

		window.textBox("idTextBox").requireEditable();
		window.textBox("orderDateTextBox").requireEditable();
		window.textBox("creditDateTextBox").requireEditable();
		window.textBox("grossTextBox").requireEditable();
		window.textBox("netTextBox").requireEditable();
		window.textBox("paymentTextBox").requireEditable();
		window.textBox("productTextBox").requireEditable();
		window.textBox("firstIssueTextBox").requireEditable();
		window.textBox("lastIssueTextBox").requireEditable();
		window.textBox("firstNameTextBox").requireEditable();
		window.textBox("lastNameTextBox").requireEditable();
		window.textBox("addressTextBox").requireEditable();
		window.textBox("zipCodeTextBox").requireEditable();
		window.textBox("cityTextBox").requireEditable();
		window.textBox("provinceTextBox").requireEditable();
		window.textBox("countryTextBox").requireEditable();
		window.textBox("emailTextBox").requireEditable();
		window.textBox("phoneTextBox").requireEditable();
		window.textBox("notesTextArea").requireEditable();

		window.checkBox("unlockCheckBox").uncheck();

		window.textBox("idTextBox").requireNotEditable();
		window.textBox("orderDateTextBox").requireNotEditable();
		window.textBox("creditDateTextBox").requireNotEditable();
		window.textBox("grossTextBox").requireNotEditable();
		window.textBox("netTextBox").requireNotEditable();
		window.textBox("paymentTextBox").requireNotEditable();
		window.textBox("productTextBox").requireNotEditable();
		window.textBox("firstIssueTextBox").requireNotEditable();
		window.textBox("lastIssueTextBox").requireNotEditable();
		window.textBox("firstNameTextBox").requireNotEditable();
		window.textBox("lastNameTextBox").requireNotEditable();
		window.textBox("addressTextBox").requireNotEditable();
		window.textBox("zipCodeTextBox").requireNotEditable();
		window.textBox("cityTextBox").requireNotEditable();
		window.textBox("provinceTextBox").requireNotEditable();
		window.textBox("countryTextBox").requireNotEditable();
		window.textBox("emailTextBox").requireNotEditable();
		window.textBox("phoneTextBox").requireNotEditable();
		window.textBox("notesTextArea").requireNotEditable();

	}

	@Test
	public void testShowOrderDetailsShouldShowOrderDetailsInTheView() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 12, 28), 0, null, null, null).build();
		orderSwingView.showOrderDetails(order);
		window.textBox("idTextBox").requireText("1");
		window.textBox("orderDateTextBox").requireText("2024-12-28");
	}

	@Test
	public void testClickOnAddButtonShouldDelegateToControllerAndShowAMessageInTheMessagesBox() {
		window.checkBox("unlockCheckBox").check();

		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");

		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();

		window.button(JButtonMatcher.withText("Add")).click();

		verify(subscriptionsController, timeout(TIMEOUT)).newOrder(new Order.OrderBuilder(1, LocalDate.of(2024, 10, 12), 0, null, null, null).build());
		verifyNoMoreInteractions(subscriptionsController);
		
		window.textBox("messagesTextBox").requireText("Order added to database with id 1");


	}

	@Test
	public void testClickOnUpdateButtonShouldDelegateToControllerAndShowAMessageInTheMessagesBox() {
		window.checkBox("unlockCheckBox").check();
		
		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");
		
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();
		
		window.button(JButtonMatcher.withText("Update")).click();
		
		verify(subscriptionsController, timeout(TIMEOUT)).updateOrder(1, new Order.OrderBuilder(1, LocalDate.of(2024, 10, 12), 0, null, null, null).build());
		verifyNoMoreInteractions(subscriptionsController);
		
	}

	@Test
	public void testClickOnDeleteButtonShouldDelegateToControllerAndShowAMessageInTheMessagesBox() {
		window.checkBox("unlockCheckBox").check();
		
		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");
		
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
		window.button(JButtonMatcher.withText("Update")).requireEnabled();
		window.button(JButtonMatcher.withText("Delete")).requireEnabled();
		
		window.button(JButtonMatcher.withText("Delete")).click();
		
		verify(subscriptionsController, timeout(TIMEOUT)).deleteOrder(new Order.OrderBuilder(1, LocalDate.of(2024, 10, 12), 0, null, null, null).build());
		verifyNoMoreInteractions(subscriptionsController);
		
		window.textBox("messagesTextBox").requireText("Order with id 1 was removed");
		
	}

	@Test
	public void testOrderAddedShouldShowAMessageInTheMessagesBox() {
		orderSwingView.orderAdded(new Order.OrderBuilder(1, LocalDate.of(2024, 12, 28), 0, null, null, null).build());
		window.textBox("messagesTextBox").requireText("Order added to database with id 1");

	}

	@Test
	public void testIfAnythingIsClickedThenTheMessagesBoxShouldBeCleared() {
		window.textBox("messagesTextBox").deleteText();
		window.textBox("messagesTextBox").setText("Sample message");

		window.panel().click();
		window.textBox("messagesTextBox").requireText(" ");
	}

	@Test
	public void testOrderRemovedShouldShowAMessageInTheMessagesBox() {
		orderSwingView.orderRemoved(new Order.OrderBuilder(1, LocalDate.of(2024, 12, 28), 0, null, null, null).build());
		window.textBox("messagesTextBox").requireText("Order with id 1 was removed");

	}

	@Test
	public void testShowErrorShouldShowAMessageInTheMessagesBox() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 12, 28), 0, null, null, null).build();
		window.textBox("messagesTextBox").deleteText();
		orderSwingView.showError("Already existing order with id 1");
		window.textBox("messagesTextBox").requireText("Already existing order with id 1");
		window.textBox("messagesTextBox").deleteText();
		orderSwingView.showError("Already existing order with id", order);
		window.textBox("messagesTextBox").requireText("Already existing order with id 1");
	}

}
