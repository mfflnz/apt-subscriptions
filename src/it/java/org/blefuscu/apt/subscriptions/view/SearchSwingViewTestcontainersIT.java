package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;

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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

@RunWith(GUITestRunner.class)
public class SearchSwingViewTestcontainersIT extends AssertJSwingJUnitTestCase {
	
	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");
	
	private MongoClient mongoClient;
	private OrderMongoRepository orderRepository;
	
	private OrderSwingView orderSwingView;
	private ListSwingView listSwingView;
	private SearchSwingView searchSwingView;
	private SubscriptionsController subscriptionsController;
	private FrameFixture searchWindow;
	private FrameFixture listWindow;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		orderRepository = new OrderMongoRepository(mongoClient, SUBSCRIPTIONS_DB_NAME, ORDER_COLLECTION_NAME);
		
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

		listWindow.show();
		searchWindow.show();
	}
	
	@Test
	public void testRequestOrdersShowsOrdersInListView() {
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
	public void testSearchButtonByDateRangeShowsOrdersInListView() {

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


	@Test
	@GUITest
	public void testSearchButtonByDateRangeShowsAnEmptyListInListViewIfNoOrderIsFound() {
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

	
	@After
	public void onTearDown() throws Exception {
		mongoClient.close();
	}

}
