package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.time.LocalDate;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;

@RunWith(GUITestRunner.class)
public class ListSwingViewIT extends AssertJSwingJUnitTestCase {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
	}

	private MongoClient mongoClient;
	private OrderMongoRepository orderRepository;
	private OrderSwingView orderSwingView;
	private ListSwingView listSwingView;
	private SubscriptionsController subscriptionsController;
	private FrameFixture listWindow;
	private FrameFixture orderWindow;

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		orderRepository = new OrderMongoRepository(mongoClient, SUBSCRIPTIONS_DB_NAME, ORDER_COLLECTION_NAME);

		// Empty the database
		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			listSwingView = new ListSwingView();
			subscriptionsController = new SubscriptionsController(orderSwingView, listSwingView, orderRepository);
			listSwingView.setSubscriptionsController(subscriptionsController);
			return listSwingView;
		});

		listWindow = new FrameFixture(robot(), listSwingView);
		orderWindow = new FrameFixture(robot(), orderSwingView);

		listWindow.show();
		// searchWindow.show();
		// orderWindow.show();
	}

	@Override
	protected void onTearDown() throws Exception {
		mongoClient.close();
	}

	@Test
	public void testRequestOrdersShowsOrdersList() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 11, 01), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 11, 02), 0, null, null, null).build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 11, 03), 0, null, null, null).build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		GuiActionRunner.execute(() -> {
			subscriptionsController.requestOrders();
		});
		assertThat(listWindow.list().contents()).containsExactly(order1.toString(), order2.toString(),
				order3.toString());
	}

	@Test
	public void testIfAnOrderIsSelectedAndShowDetailsIsClickedShouldReadOrderDetailsFromDatabase() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 11, 01), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 11, 02), 65.00, "PayPal",
				"Abbonamento cartaceo + digitale", "test@email.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 11, 03), 65.00, "Bonifico bancario",
				"Abbonamento cartaceo + digitale", "test@email.com").build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		GuiActionRunner.execute(() -> {
			listSwingView.orderAdded(order1);
			listSwingView.orderAdded(order2);
			listSwingView.orderAdded(order3);
		});

		listWindow.list("ordersList").selectItem(2);
		listWindow.button(JButtonMatcher.withText("Show Details")).click();

		assertThat(subscriptionsController.orderDetails(3)).isEqualTo(order3);
		orderWindow.show(); // TODO : is this a hack? In case I should fix it
		assertThat(orderWindow.textBox("idTextBox")).toString().equals("3");

	}

	@Test
	public void testIfAnOrderIsSelectedAndDeleteIsClickedShouldDeleteOrderFromDatabase() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 11, 01), 30.00, "Carta di credito", "Abbonamento solo digitale", "test@address.com").build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 11, 02), 65.00, "PayPal",
				"Abbonamento cartaceo + digitale", "test@email.com").build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 11, 03), 65.00, "Bonifico bancario",
				"Abbonamento cartaceo + digitale", "test@email.com").build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		GuiActionRunner.execute(() -> {
			listSwingView.orderAdded(order1);
			listSwingView.orderAdded(order2);
			listSwingView.orderAdded(order3);
		});

		listWindow.list("ordersList").selectItem(1);
		listWindow.button(JButtonMatcher.withText("Delete")).click();
		
		assertThat(listWindow.list().contents()).containsExactly(order1.toString(), order3.toString());
		assertThat(orderRepository.findAll()).containsExactly(order1, order3);
	}
}
