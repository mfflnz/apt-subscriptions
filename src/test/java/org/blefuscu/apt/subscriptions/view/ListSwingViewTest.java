package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

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
	private FrameFixture window;
	private AutoCloseable closeable;

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

		window = new FrameFixture(robot(), listSwingView);
		window.show();
	}
	
	@Override
	protected void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.list("ordersList");
		window.button(JButtonMatcher.withText("Show Details")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();

	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel()
				.addElement(new Order(1, LocalDateTime.of(2024, 8, 2, 0, 0, 0))));
		window.list("ordersList").selectItem(0);
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete"));
		deleteButton.requireEnabled();
		window.list("ordersList").clearSelection();
		deleteButton.requireDisabled();
	}

	@Test
	@GUITest
	public void testShowDetailsButtonShouldBeEnabledOnlyWhenAnOrderIsSelected() {
		GuiActionRunner.execute(() -> listSwingView.getListOrdersModel()
				.addElement(new Order(1, LocalDateTime.of(2024, 8, 2, 0, 0, 0))));
		window.list("ordersList").selectItem(0);
		JButtonFixture showDetailsButton = window.button(JButtonMatcher.withText("Show Details"));
		showDetailsButton.requireEnabled();
		window.list("ordersList").clearSelection();
		showDetailsButton.requireDisabled();
	}

	@Test
	public void testShowAllOrdersShouldAddOrderDescriptionsToTheList() {
		Order order1 = new Order(1, LocalDateTime.of(2024, 10, 9, 0, 0, 0));
		Order order2 = new Order(2, LocalDateTime.of(2024, 10, 9, 0, 0, 0));
		GuiActionRunner.execute(() -> listSwingView.showAllOrders(Arrays.asList(order1, order2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
	}

	@Test
	public void testShowErrorShouldShowTheMessageInTheErrorLabel() {
		Order order = new Order(1, LocalDateTime.of(2024, 10, 9, 0, 0, 0));
		GuiActionRunner.execute(() -> listSwingView.showError("error message", order));
		window.label("errorMessageLabel").requireText("error message: " + order);
	}

	@Test
	public void testOrderAddedShouldAddTheOrderToTheListAndResetTheErrorLabel() {
		Order order = new Order(1, LocalDateTime.of(2024, 10, 9, 0, 0, 0));
		GuiActionRunner.execute(() -> listSwingView.orderAdded(new Order(1, LocalDateTime.of(2024, 10, 9, 0, 0, 0))));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(order.toString());
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testOrderRemovedShouldRemoveTheOrderFromTheListAndResetTheErrorLabel() {
		Order order1 = new Order(1, LocalDateTime.of(2024, 10, 8, 0, 0, 0));
		Order order2 = new Order(2, LocalDateTime.of(2024, 10, 9, 0, 0, 0));
		GuiActionRunner.execute(() -> {
			DefaultListModel<Order> listOrdersModel = listSwingView.getListOrdersModel();
			listOrdersModel.addElement(order1);
			listOrdersModel.addElement(order2);
		});
		GuiActionRunner.execute(() -> listSwingView.orderRemoved(new Order(1, LocalDateTime.of(2024, 10, 8, 0, 0, 0))));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(order2.toString());
		window.label("errorMessageLabel").requireText(" ");
	}
	
	
	

}
