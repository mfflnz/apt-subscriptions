package org.blefuscu.apt.subscriptions.view;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.timing.Pause;
import org.blefuscu.apt.subscriptions.controller.ExportController;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static java.util.Arrays.asList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class ListSwingViewTest {

	private static final int TIMEOUT = 10000;
	
	private ListSwingView listSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;
	
	@Mock
	private OrderSwingView orderView;

	@Mock
	private SubscriptionsController subscriptionsController;

	@Mock
	private ExportController exportController;

	@Before
	public void setUp() {

		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
			listSwingView.setFc(new JFileChooser());
			listSwingView.setSubscriptionsController(subscriptionsController);
			return listSwingView;
		});

		window = showInFrame(listSwingView);
	}

	@After
	public void tearDown() throws Exception {
		window.cleanUp();
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.list("ordersList");
		window.button(JButtonMatcher.withText("Export")).requireDisabled();

	}

	@Test
	@GUITest
	public void testWhenOrdersListIsNotEmptyThenExportButtonShouldBeEnabled() {

		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		
		window.button(JButtonMatcher.withText("Export")).requireEnabled();
		
		assertThat(window.button(JButtonMatcher.withText("Export")).isEnabled());

	}

	@Test
	@GUITest
	public void testWhenOrdersListIsEmptyThenExportButtonShouldBeDisabled() {

		// intervalRemoved()
		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel().removeAllElements();
		window.button(JButtonMatcher.withText("Export")).requireDisabled();

		// intervalAdded()
		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(2, LocalDate.of(2025, 10, 28), "customer@email.com").build());
		listSwingView.getListOrdersModel().removeElementAt(0);
		window.button(JButtonMatcher.withText("Export")).requireEnabled();

		// contentsChanged()
		listSwingView.getListOrdersModel().removeAllElements();
		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel()
				.addElement(new Order.OrderBuilder(2, LocalDate.of(2025, 10, 28), "customer@email.com").build());
		listSwingView.getListOrdersModel()
				.setElementAt(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 29), "new@email.com").build(), 0);
		window.button(JButtonMatcher.withText("Export")).requireEnabled();

	}

	@Test
	public void testShowOrdersShouldClearTheListAndThenDisplayGivenOrdersInTheList() {

		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 9), null).build();
		
		GuiActionRunner.execute(() -> listSwingView.showOrders(Arrays.asList(order1, order2)));
		
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());
		
		GuiActionRunner.execute(() -> listSwingView.showOrders(Arrays.asList(order1)));
		
		listContents = window.list().contents();
		assertThat(listContents).containsExactly(order1.toString());


	}

	@Test
	public void testOrderUpdatedShouldDisplayTheUpdatedOrderInTheList() {

		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();
		Order updatedOrder = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "updated@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
			listSwingView.orderUpdated(2, updatedOrder);
		});

		String[] listContents = window.list().contents();

		assertThat(listContents).containsExactly(order1.toString(), updatedOrder.toString(), order3.toString())
				.doesNotContain(order2.toString());
	}

	@Test
	public void testOrderDeletedShouldRemoveTheDeletedOrderFromTheList() {

		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
			listSwingView.orderDeleted(2);
		});

		String[] listContents = window.list().contents();

		assertThat(listContents).containsExactly(order1.toString(), order3.toString())
				.doesNotContain(order2.toString());
	}

	@Test
	public void testExportButtonShouldOpenAFileChooserWindow() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
		});
		window.button(JButtonMatcher.withText("Export")).click();
		assertThat(listSwingView.getFc().isShowing());
		Pause.pause(1000);
	}

	@Test
	public void testCancelButtonInDialogShouldYieldToNoInteractionsWithExportController() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
		});
		window.button(JButtonMatcher.withText("Export")).click();
		GuiActionRunner.execute(() -> {
			listSwingView.getFc().setName("export.csv");
			listSwingView.getFc().cancelSelection();
		});
		
		verifyNoInteractions(exportController);
	}
	
	@Test
	public void testCancelButtonInDialogShouldCloseTheFileChooserDialog() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
		});
		window.button(JButtonMatcher.withText("Export")).click();

		listSwingView.getFc().setName("export.csv");
		listSwingView.getFc().cancelSelection();
		
		assertFalse(listSwingView.getFc().isShowing());
	}

	@Test
	public void testSaveButtonInDialogShouldDelegateToSubscriptionsControllerFormatOrder() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();
		List<Order> orders = asList(order1, order2, order3);

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
		});
		window.button(JButtonMatcher.withText("Export")).click();
		listSwingView.getFc().setName("export.csv");
		listSwingView.getFc().approveSelection();

		orders.forEach(order -> verify(subscriptionsController, timeout(TIMEOUT)).formatOrder(order));
	}

	@Test
	public void testSaveButtonInDialogShouldDelegateToSubscriptionsControllerExportOrders() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "other@address.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2025, 10, 10), "third@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
			listSwingView.getListOrdersModel().addElement(order3);
		});

		window.button(JButtonMatcher.withText("Export")).click();

		listSwingView.getFc().setSelectedFile(new File("export.csv"));

		listSwingView.getFc().approveSelection();

		verify(subscriptionsController, timeout(TIMEOUT)).exportOrders(anyList(), anyString());
	}
	
	@Test
	public void testSelectingAnElementInTheListShouldShowOrderDetailsInTheOrderView() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
		});
		window.list("ordersList").selectItem(0);

		//TODO: controlla
			verify(subscriptionsController, timeout(TIMEOUT).atLeastOnce()).orderDetails(1);

	}
	
	@Test
	public void testClearListShouldRemoveAllElementsInTheList() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), "email@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 10), "second@address.com").build();

		GuiActionRunner.execute(() -> {
			listSwingView.getListOrdersModel().addElement(order1);
			listSwingView.getListOrdersModel().addElement(order2);
		});

		listSwingView.clearList();
		
		String[] listContents = window.list("ordersList").contents();
		assertThat(listContents).isEmpty();
	}
}
