package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import static org.awaitility.Awaitility.*;

import java.util.concurrent.TimeUnit;

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
public class OrderSwingViewTestcontainersIT extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private MongoClient mongoClient;
	private OrderMongoRepository orderRepository;

	private OrderSwingView orderSwingView;
	private ListSwingView listSwingView;
	private SubscriptionsController subscriptionsController;
	private FrameFixture listWindow;
	private FrameFixture orderWindow;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Override
	protected void onSetUp() throws Exception {
		mongoClient = new MongoClient(new ServerAddress(mongo.getHost(), mongo.getFirstMappedPort()));
		orderRepository = new OrderMongoRepository(mongoClient);

		for (Order order : orderRepository.findAll()) {
			orderRepository.delete(order.getOrderId());
		}

		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			listSwingView = new ListSwingView();
			subscriptionsController = new SubscriptionsController(orderSwingView, listSwingView, orderRepository);
			orderSwingView.setSubscriptionsController(subscriptionsController);
			return orderSwingView;
		});

		listWindow = new FrameFixture(robot(), listSwingView);
		orderWindow = new FrameFixture(robot(), orderSwingView);

		orderWindow.show();

	}

	@After
	public void onTearDown() throws Exception {
		mongoClient.close();
	}

	@Test
	public void testAddButtonShouldAddOrderToDatabase() {

		orderWindow.checkBox("unlockCheckBox").check();

		orderWindow.textBox("idTextBox").setText("1");
		orderWindow.textBox("orderDateTextBox").setText("2024-10-12");
		orderWindow.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		orderWindow.textBox("grossTextBox").setText("€65,00");
		orderWindow.textBox("paymentTextBox").setText("Bonifico");
		orderWindow.textBox("emailTextBox").setText("user@email.com");

		orderWindow.button(JButtonMatcher.withText("Add")).click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
			assertThat(orderRepository.findById(1).getOrderId()).isEqualTo(1);
			assertThat(orderRepository.findById(1).getOrderDate()).isEqualTo("2024-10-12");
			assertThat(orderRepository.findById(1).getOrderAttributionReferrer()).isEqualTo("Abbonamento annuale cartaceo");
			assertThat(orderRepository.findById(1).getOrderTotal()).isEqualTo(65.00);
			assertThat(orderRepository.findById(1).getPaymentMethodTitle()).isEqualTo("Bonifico");
			assertThat(orderRepository.findById(1).getBillingEmail()).isEqualTo("user@email.com");
		});

	}

	@Test
	public void testUpdateButtonShouldUpdateOrderInDatabase() {

	}

	@Test
	public void testDeleteButtonShouldDeleteOrderFromDatabase() {

	}
}
