package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Arrays;

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

	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel()
				.addElement(new Order(1, LocalDate.of(2024, 8, 2))));
		listWindow.list("ordersList").selectItem(0);
		JButtonFixture deleteButton = listWindow.button(JButtonMatcher.withText("Delete"));
		deleteButton.requireEnabled();
		listWindow.list("ordersList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	@GUITest
	public void testShowDetailsButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel()
				.addElement(new Order(1, LocalDate.of(2024, 8, 2))));
		listWindow.list("ordersList").selectItem(0);
		JButtonFixture showDetailsButton = listWindow.button(JButtonMatcher.withText("Show Details"));
		showDetailsButton.requireEnabled();
		listWindow.list("ordersList").clearSelection();
		showDetailsButton.requireDisabled();
	}

	@Test
	public void testShowAllOrdersShouldAddOrderDescriptionsToTheList() {
		Order order1 = new Order(1, LocalDate.of(2024, 10, 9));
		Order order2 = new Order(2, LocalDate.of(2024, 10, 9));
		GuiActionRunner.execute(() -> listSwingView.showAllOrders(Arrays.asList(order1, order2)));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Order order = new Order(1, LocalDate.of(2024, 10, 9));
		GuiActionRunner.execute(() -> listSwingView.showError("error message", order));
		listWindow.label("errorMessageLabel").requireText("error message: " + order);
	}

	@Test
	public void testOrderAddedShouldAddTheOrderToTheListAndResetTheErrorLabel() {
		Order order = new Order(1, LocalDate.of(2024, 10, 9));
		GuiActionRunner.execute(() -> listSwingView.orderAdded(new Order(1, LocalDate.of(2024, 10, 9))));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order.toString());
		listWindow.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testOrderRemovedShouldRemoveTheOrderFromTheListAndResetTheErrorLabel() {
		Order order1 = new Order(1, LocalDate.of(2024, 10, 8));
		Order order2 = new Order(2, LocalDate.of(2024, 10, 9));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Order> listOrdersModel = listSwingView.getListOrdersModel();
			listOrdersModel.addElement(order1);
			listOrdersModel.addElement(order2);
		});
		GuiActionRunner.execute(() -> listSwingView.orderRemoved(new Order(1, LocalDate.of(2024, 10, 8))));
		String[] listContents = listWindow.list().contents();
		assertThat(listContents).containsExactly(order2.toString());
		listWindow.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testIfShowDetailsButtonIsPressedAnOrderViewShouldBeShown() {

		// setup
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel().addElement(new Order(1, LocalDate.now())));
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

	
}