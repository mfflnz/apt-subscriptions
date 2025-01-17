package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.time.LocalDate;

import org.assertj.swing.annotation.GUITest;
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

@RunWith(GUITestRunner.class)
public class SearchSwingViewIT extends AssertJSwingJUnitTestCase {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;
	private MongoClient mongoClient;
	private OrderMongoRepository orderRepository;
	private OrderSwingView orderSwingView;
	private SubscriptionsController subscriptionsController;
	private ListSwingView listSwingView;
	private SearchSwingView searchSwingView;
	private FrameFixture searchWindow;
	private FrameFixture listWindow;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
	}

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(serverAddress));
		orderRepository = new OrderMongoRepository(mongoClient);

		// Empty the database
		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			listSwingView = new ListSwingView();
			searchSwingView = new SearchSwingView();
			subscriptionsController = new SubscriptionsController(orderSwingView, listSwingView, orderRepository);
			searchSwingView.setSubscriptionsController(subscriptionsController);
			return searchSwingView;
		});

		listWindow = new FrameFixture(robot(), listSwingView);
		searchWindow = new FrameFixture(robot(), searchSwingView);

		searchWindow.show();
		listWindow.show();
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
	@GUITest
	public void testSearchButtonShowsOrdersInDateRange() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 01), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 02), 0, null, null, null).build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 11, 01), 0, null, null, null).build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		searchWindow.textBox("fromTextBox").deleteText().enterText("2024-09-12");
		searchWindow.textBox("toTextBox").deleteText().enterText("2024-10-20");
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		assertThat(listWindow.list().contents()).containsExactly(order1.toString(), order2.toString());
	}

	// TODO: controlla questo test

	@Test
	@GUITest
	public void testSearchButton() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2024, 10, 01), 0, null, null, null).build();
		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2024, 10, 02), 0, null, null, null).build();
		Order order3 = new Order.OrderBuilder(3, LocalDate.of(2024, 11, 01), 0, null, null, null).build();
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		searchWindow.textBox("fromTextBox").deleteText().enterText("2024-09-12");
		searchWindow.textBox("toTextBox").deleteText().enterText("2024-09-20");
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		assertThat(listWindow.list().contents()).isEmpty();
	}

}
