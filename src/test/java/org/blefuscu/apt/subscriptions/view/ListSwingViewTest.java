package org.blefuscu.apt.subscriptions.view;

import java.time.LocalDate;
import java.util.Arrays;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class ListSwingViewTest  {

	private ListSwingView listSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;
	
	@Mock
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() {
		
		closeable = MockitoAnnotations.openMocks(this);
		
		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
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
		 
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		window.button(JButtonMatcher.withText("Export")).requireEnabled();

	}

	@Test
	@GUITest
	public void testWhenOrdersListIsEmptyThenExportButtonShouldBeDisabled() {
		
		// intervalRemoved()
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel().removeAllElements();
		window.button(JButtonMatcher.withText("Export")).requireDisabled();

		// intervalAdded()
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(2, LocalDate.of(2025, 10, 28), "customer@email.com").build());
		listSwingView.getListOrdersModel().removeElementAt(0);
		window.button(JButtonMatcher.withText("Export")).requireEnabled();
		
		// contentsChanged()
		listSwingView.getListOrdersModel().removeAllElements();
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 27), "customer@email.com").build());
		listSwingView.getListOrdersModel().addElement(new Order.OrderBuilder(2, LocalDate.of(2025, 10, 28), "customer@email.com").build());
		listSwingView.getListOrdersModel().setElementAt(new Order.OrderBuilder(1, LocalDate.of(2025, 10, 29), "new@email.com").build(), 0);
		window.button(JButtonMatcher.withText("Export")).requireEnabled();
		
	}
	
	@Test
	public void testShowOrdersShouldDisplayGivenOrdersInTheList() {
		
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 10, 9), null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 10, 9), null).build();
		GuiActionRunner.execute(() -> listSwingView.showOrders(Arrays.asList(order1, order2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly(order1.toString(), order2.toString());

	}
	
	@Test
	public void testOrderUpdatedShouldDisplayTheUpdatedOrderInTheList() {

		// TODO
		
		// Inserisci due ordini
		// modifica il primo ordine
		
		// chiama il metodo del SUT
		listSwingView.orderUpdated(0, null);
		
		// verifica che nella lista al posto dell'ordine in questione ci sia l'ordine aggiornato
	}

}
