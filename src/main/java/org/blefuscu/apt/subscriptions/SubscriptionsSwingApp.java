package org.blefuscu.apt.subscriptions;

import java.awt.EventQueue;

import org.blefuscu.apt.subscriptions.controller.ExportController;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.view.DashboardSwingView;
import org.blefuscu.apt.subscriptions.view.ListSwingView;
import org.blefuscu.apt.subscriptions.view.MessageSwingView;
import org.blefuscu.apt.subscriptions.view.OrderSwingView;
import org.blefuscu.apt.subscriptions.view.SearchSwingView;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import java.util.concurrent.Callable;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(mixinStandardHelpOptions = true)
public class SubscriptionsSwingApp implements Callable<Void> {

	@Option(names = { "--mongo-host" }, description = "MongoDB host address")
	private String mongoHost = "localhost";

	@Option(names = { "--mongo-port" }, description = "MongoDB host port")
	private int mongoPort = 27017;

	@Option(names = { "--db-name" }, description = "Database name")
	private String databaseName = "subscriptions";

	@Option(names = { "--db-collection" }, description = "Collection name")
	private String collectionName = "orders";

	public static void main(String[] args) {

		new CommandLine(new SubscriptionsSwingApp()).execute(args);

	}

	@Override
	public Void call() throws Exception {
		EventQueue.invokeLater(() -> {
			try {
				SearchSwingView searchSwingView = new SearchSwingView();
				ListSwingView listSwingView = new ListSwingView();
				OrderSwingView orderSwingView = new OrderSwingView();
				MessageSwingView messageSwingView = new MessageSwingView();
				DashboardSwingView dashboardSwingView = new DashboardSwingView(searchSwingView, listSwingView,
						orderSwingView, messageSwingView);
				OrderMongoRepository orderRepository = new OrderMongoRepository(
						new MongoClient(new ServerAddress(mongoHost, mongoPort)), databaseName, collectionName);
				ExportController exportManager = new ExportController(messageSwingView);
				SubscriptionsController subscriptionsController = new SubscriptionsController(listSwingView,
						orderSwingView, messageSwingView, orderRepository, exportManager);
				searchSwingView.setSubscriptionsController(subscriptionsController);
				listSwingView.setSubscriptionsController(subscriptionsController);
				orderSwingView.setSubscriptionsController(subscriptionsController);
				dashboardSwingView.setVisible(true);
			} catch (Exception e) {
			}
		});
		return null;

	}

}
