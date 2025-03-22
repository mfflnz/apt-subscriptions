package org.blefuscu.apt.subscriptions.app.swing;

import java.awt.EventQueue;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.repository.mongo.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.view.ListSwingView;
import org.blefuscu.apt.subscriptions.view.OrderSwingView;
import org.blefuscu.apt.subscriptions.view.SearchSwingView;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Option;

public class SubscriptionsSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";
	
	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;
	
	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "subscriptions";
	
	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "order";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		new CommandLine(new SubscriptionsSwingApp()).execute(args);
	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				OrderMongoRepository orderRepository = new OrderMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
				OrderSwingView orderView = new OrderSwingView();
				ListSwingView listView = new ListSwingView();
				SearchSwingView searchView = new SearchSwingView();
				SubscriptionsController subscriptionsController = new SubscriptionsController(orderView, listView,
						orderRepository);
				searchView.setSubscriptionsController(subscriptionsController);
				listView.setSubscriptionsController(subscriptionsController);
				orderView.setSubscriptionsController(subscriptionsController);
				searchView.setLocation(100, 100);
				searchView.setVisible(true);
				listView.setLocation(100, 400);
				listView.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return null;
	}

}
