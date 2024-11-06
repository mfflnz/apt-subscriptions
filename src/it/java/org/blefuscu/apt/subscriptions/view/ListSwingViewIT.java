package org.blefuscu.apt.subscriptions.view;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.time.LocalDate;

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
	private SearchSwingView searchSwingView;
	private SubscriptionsController subscriptionsController;
	private FrameFixture listWindow;
	private FrameFixture searchWindow;


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
			listSwingView.setSubscriptionsController(subscriptionsController);
			return listSwingView;
		});

		listWindow = new FrameFixture(robot(), listSwingView);
		searchWindow = new FrameFixture(robot(), searchSwingView);
		
		listWindow.show();
		searchWindow.show();
		
	}
	
	@Override
	protected void onTearDown() throws Exception {
		mongoClient.close();
	}
	

	@Test
	public void testRequestOrdersShowsOrdersList() {
		Order order1 = new Order(1, LocalDate.of(2024, 11, 01));
		Order order2 = new Order(2, LocalDate.of(2024, 11, 02));
		Order order3 = new Order(3, LocalDate.of(2024, 11, 03));
		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		GuiActionRunner.execute(() -> {
			subscriptionsController.requestOrders();
		});
		assertThat(listWindow.list().contents()).containsExactly(order1.toString(), order2.toString(),
				order3.toString());
	}
	

}
