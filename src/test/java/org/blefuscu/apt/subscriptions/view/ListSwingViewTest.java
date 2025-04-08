package org.blefuscu.apt.subscriptions.view;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@RunWith(GUITestRunner.class)
public class ListSwingViewTest extends AssertJSwingJUnitTestCase {

	private ListSwingView listSwingView;
	private FrameFixture listWindow;
	private AutoCloseable closeable;

	private OrderSwingView orderSwingView;
	
	private static final int TIMEOUT = 5000;

	@Mock
	private SubscriptionsController subscriptionsController;

	@Override
	protected void onSetUp() throws Exception {

		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
			listSwingView.setSubscriptionsController(subscriptionsController);
			return listSwingView;
		});

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			return orderSwingView;
		});
		
		Files.deleteIfExists(Paths.get("export.csv"));

		listWindow = new FrameFixture(robot(), listSwingView);
		listWindow.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}
	
	@Test
	@GUITest
	public void testControlsInitialStates() {
		listWindow.list("ordersList");
		listWindow.button(JButtonMatcher.withText("Show Details")).requireDisabled();
		listWindow.button(JButtonMatcher.withText("Export CSV")).requireEnabled();
		listWindow.button(JButtonMatcher.withText("Delete")).requireDisabled();
		assertThat(listWindow.button(JButtonMatcher.withText("Show Details")).requireDisabled());

	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner
				.execute(() -> listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 2), 0, null, null, null).build()));
		listWindow.list("ordersList").selectItem(0);
		JButtonFixture deleteButton = listWindow.button(JButtonMatcher.withText("Delete"));
		deleteButton.requireEnabled();
		listWindow.list("ordersList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	@GUITest
	public void testShowDetailsButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner
				.execute(() -> listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 2), 0, null, null, null).build()));
		listWindow.list("ordersList").selectItem(0);
		JButtonFixture showDetailsButton = listWindow.button(JButtonMatcher.withText("Show Details"));
		showDetailsButton.requireEnabled();
		listWindow.list("ordersList").clearSelection();
		showDetailsButton.requireDisabled();
	}

	@Test
	public void testShowOrdersShouldAddOrderDescriptionsToTheList() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		GuiActionRunner.execute(() -> listSwingView.showOrders(Arrays.asList(order1, order2)));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		GuiActionRunner.execute(() -> listSwingView.showError("error message", order));
		listWindow.label("errorMessageLabel").requireText("error message: " + order);
	}

	@Test
	public void testOrderAddedShouldAddTheOrderToTheListAndResetTheErrorLabel() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		GuiActionRunner.execute(() -> listSwingView.orderAdded(new Order.OrderBuilder(1, LocalDate.of(2024, 10, 9), 0, null, null, null).build()));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order.toString());
		listWindow.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testOrderRemovedShouldRemoveTheOrderFromTheListAndResetTheErrorLabel() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		GuiActionRunner.execute(() -> {
			DefaultListModel<Order> listOrdersModel = listSwingView.getListOrdersModel();
			listOrdersModel.addElement(order1);
			listOrdersModel.addElement(order2);
		});
		GuiActionRunner.execute(() -> listSwingView.orderRemoved(new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build()));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order2.toString());
		listWindow.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testIfShowDetailsButtonIsPressedAnOrderViewShouldBeShown() {

		// setup
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.now(), 0, null, null, null).build()));
		listWindow.list("ordersList").selectItem(0);

		// exercise + verify
		listWindow.button(JButtonMatcher.withText("Show Details")).click();
		orderSwingView.isShowing();

		// teardown
		listWindow.list("ordersList").clearSelection();
		listWindow.button(JButtonMatcher.withText("Show Details")).requireDisabled();
	}

	@Test
	public void testIfExportCSVButtonIsPressedASaveAsDialogShouldBeShown() {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().isShowing();
	}

	@Test
	public void testDeleteButtonShouldDelegateToSubscriptionsControllerDeleteOrder() {
		GuiActionRunner
				.execute(() -> listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 2), 0, null, null, null).build()));
		listWindow.list("ordersList").selectItem(0);
		listWindow.button(JButtonMatcher.withText("Delete")).click();
		verify(subscriptionsController, timeout(TIMEOUT)).deleteOrder(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 2), 0, null, null, null).build());
	}

	@Test
	@GUITest
	public void testShowDetailsButtonShouldDelegateToSubscriptionsControllerOrderDetails() {
		GuiActionRunner
				.execute(() -> listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2024, 8, 2), 0, null, null, null).build()));
		listWindow.list("ordersList").selectItem(0);
		listWindow.button(JButtonMatcher.withText("Show Details")).requireEnabled().click();
		verify(subscriptionsController).orderDetails(1);
		listWindow.list("ordersList").clearSelection();
		listWindow.button(JButtonMatcher.withText("Show Details")).requireDisabled();

	}

	@Test
	public void testCancelButtonInDialogShouldYieldToNoInteractionsWithSubscriptionsController() throws IOException {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().setName("export.csv");
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		List<Order> ordersList = asList(order1, order2);
		listSwingView.setOrdersList(ordersList);
		listSwingView.getFc().cancelSelection();
		//GuiActionRunner.execute(() -> subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList()));
		//when(subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList())).thenReturn(1);
		//verify(subscriptionsController).exportOrders("export.csv", Arrays.asList(order1, order2));
		verifyNoInteractions(subscriptionsController);
	}
	
	@Test
	public void testSaveButtonInDialogShouldDelegateToSubscriptionsControllerExportOrders() throws IOException {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().setName("export.csv");
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		List<Order> ordersList = asList(order1, order2);
		listSwingView.setOrdersList(ordersList);
		listSwingView.getFc().approveSelection();
		when(subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList())).thenReturn(1);
		verify(subscriptionsController, timeout(TIMEOUT)).exportOrders("export.csv", Arrays.asList(order1, order2));
		
	}
	
	// TODO: test del caso IOException
	@Test
	public void testListSwingViewIOException() throws IOException {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().setName("export.csv");
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		List<Order> ordersList = asList(order1, order2);
		listSwingView.setOrdersList(ordersList);
		
		when(subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList())).thenThrow(IOException.class);
		
	}

	@Test
	public void testCsvExportedShouldShowAnInfoMessageIfExportWasSuccessful() throws IOException {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().setName("export.csv");
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		List<Order> ordersList = asList(order1, order2);
		listSwingView.setOrdersList(ordersList);
		listSwingView.getFc().approveSelection();
		when(subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList())).thenReturn(1);
		GuiActionRunner.execute(() -> listSwingView.csvExported(ordersList, 1));
		listWindow.label("errorMessageLabel").requireText("Orders exported as export.csv");
		
	}

	@Test
	public void testCsvExportedShouldShowAnErrorMessageIfExportWasNotSuccessful() throws IOException {
		listWindow.button(JButtonMatcher.withText("Export CSV")).click();
		listSwingView.getFc().setName("export.csv");
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 8), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 9), 0, null, null, null).build();
		List<Order> ordersList = asList(order1, order2);
		listSwingView.setOrdersList(ordersList);
		listSwingView.getFc().approveSelection();
		when(subscriptionsController.exportOrders(listSwingView.getFc().getName(), listSwingView.getOrdersList())).thenThrow(IOException.class);
		GuiActionRunner.execute(() -> listSwingView.csvExported(ordersList, -1));
		listWindow.label("errorMessageLabel").requireText("Error exporting orders list");
	}
	


}