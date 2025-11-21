package org.blefuscu.apt.subscriptions;

import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RunWith(GUITestRunner.class)
public class SubscriptionsSwingAppE2E extends AssertJSwingJUnitTestCase {

	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> mongoCollection;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {

		mongoClient = new MongoClient("localhost");
		mongoDatabase = mongoClient.getDatabase(SUBSCRIPTIONS_DB_NAME);
		mongoCollection = mongoDatabase.getCollection(ORDER_COLLECTION_NAME);
		mongoDatabase.drop();

		String json1 = new String(
				Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/assets/sample-document-1.json")));
		Document doc1 = Document.parse(json1);

		String json2 = new String(
				Files.readAllBytes(Paths.get(System.getProperty("user.dir") + "/assets/sample-document-2.json")));
		Document doc2 = Document.parse(json2);

		mongoCollection.insertOne(doc1);
		mongoCollection.insertOne(doc2);

		application("org.blefuscu.apt.subscriptions.SubscriptionsSwingApp").start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Dashboard".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Test
	@GUITest
	public void testOnStartAllTheFieldsAreEmpty() {
		assertThat(window.list().contents()).isEmpty();
		// TODO: altri campi
	}

	@Test
	@GUITest
	public void testSearchButton() {
		window.button(JButtonMatcher.withText("Search")).click();
		assertThat(window.list().contents()).anySatisfy(e -> assertThat(e).contains("12345", "client1@address.com"));
		assertThat(window.textBox("messageTextBox").text()).contains("2 orders found");
	}

	@Test
	@GUITest
	public void testSelectOrderFromList() {
		window.button(JButtonMatcher.withText("Search")).click();
		window.list().selectItem(0);
		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("12345");
		assertThat(window.textBox("paidDateTextBox").text()).isEqualTo("2025-08-05");
	}
}
