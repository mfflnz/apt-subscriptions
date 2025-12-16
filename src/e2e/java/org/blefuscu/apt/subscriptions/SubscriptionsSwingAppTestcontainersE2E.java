package org.blefuscu.apt.subscriptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.awaitility.Awaitility.await;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.mongodb.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@RunWith(GUITestRunner.class)
public class SubscriptionsSwingAppTestcontainersE2E extends AssertJSwingJUnitTestCase {

	private static final String EXPORTED_ORDERS_FILENAME = System.getProperty("user.dir") + "/exported-orders-e2e.csv";
	private static MongoDBContainer mongoDBContainer;
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private MongoCollection<Document> mongoCollection;
	private FrameFixture window;

	@BeforeClass
	public static void startContainer() {
		mongoDBContainer = new MongoDBContainer("mongo:5");
		mongoDBContainer.start();
	}

	@Override
	protected void onSetUp() throws Exception {

		String containerIpAddress = mongoDBContainer.getHost();
		Integer mappedPort = mongoDBContainer.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);
		mongoDatabase = mongoClient.getDatabase(SUBSCRIPTIONS_DB_NAME);
		mongoCollection = mongoDatabase.getCollection(ORDER_COLLECTION_NAME);
		mongoDatabase.drop();

		Files.deleteIfExists(Paths.get(EXPORTED_ORDERS_FILENAME));

		String json1 = new String(Files.readAllBytes(
				Paths.get(System.getProperty("user.dir") + "/src/main/resources/sample-document-1.json")));
		Document doc1 = Document.parse(json1);

		String json2 = new String(Files.readAllBytes(
				Paths.get(System.getProperty("user.dir") + "/src/main/resources/sample-document-2.json")));
		Document doc2 = Document.parse(json2);

		mongoCollection.insertOne(doc1);
		mongoCollection.insertOne(doc2);

		application("org.blefuscu.apt.subscriptions.SubscriptionsSwingApp")
				.withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString(),
						"--db-name=" + SUBSCRIPTIONS_DB_NAME, "--db-collection=" + ORDER_COLLECTION_NAME)
				.start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Dashboard".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Override
	protected void onTearDown() throws Exception {
		Files.deleteIfExists(Paths.get(EXPORTED_ORDERS_FILENAME));
	}

	@AfterClass
	public static void stopContainer() {
		mongoDBContainer.stop();
	}

	@Test
	@GUITest
	public void testOnStartAllTheFieldsAreEmpty() {
		assertThat(window.list().contents()).isEmpty();
		assertThat(window.textBox("fromTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("toTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("orderIdTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("orderTotalTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("orderDateTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("netTotalTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("paidDateTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("paymentMethodTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("firstNameTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("lastNameTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("addressTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("postcodeTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("stateTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("cityTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("emailTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("phoneTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("productTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("firstIssueTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("lastIssueTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("notesTextBox").requireVisible().text()).isEmpty();
		assertThat(window.textBox("messageTextBox").requireVisible().text()).isEmpty();

	}

	@Test
	@GUITest
	public void testButtonsAndListSelector() throws IOException {

		window.button(JButtonMatcher.withText("Search")).click();

		assertThat(window.list().contents()).anySatisfy(e -> assertThat(e).contains("12345", "client1@address.com"));
		assertThat(window.textBox("messageTextBox").text()).contains("2 orders found");
		window.list().requireNoSelection();
		window.list().requireItemCount(2);
		assertThat(window.textBox("orderIdTextBox").text()).isEmpty();
		assertFalse(window.button(JButtonMatcher.withText("Update")).isEnabled());
		assertFalse(window.button(JButtonMatcher.withText("Delete")).isEnabled());

		window.list().selectItem(0);

		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("12345");
		assertThat(window.textBox("paidDateTextBox").text()).isEqualTo("2025-08-05");
		assertThat(window.textBox("emailTextBox").text()).isEqualTo("client1@address.com");
		assertTrue(window.button(JButtonMatcher.withText("Update")).isEnabled());
		assertTrue(window.button(JButtonMatcher.withText("Delete")).isEnabled());

		window.textBox("emailTextBox").deleteText().enterText("new1@address.com");
		window.button(JButtonMatcher.withText("Update")).click();
		window.list().selectItem(1);

		assertThat(window.textBox("emailTextBox").text()).isEqualTo("client2@address.com");

		window.list().selectItem(0);

		assertThat(window.textBox("emailTextBox").text()).isEqualTo("new1@address.com");
		Bson emailFilter = Filters.eq("customer_email", "new1@address.com");
		FindIterable<Document> documentWithUpdatedEmailAddress = mongoCollection.find(emailFilter);
		assertEquals(12345, documentWithUpdatedEmailAddress.first().get("order_id"));
		assertEquals(2, mongoCollection.countDocuments());

		window.button(JButtonMatcher.withText("Delete")).click();

		window.list().requireItemCount(1);
		assertEquals(1, mongoCollection.countDocuments());

		window.button(JButtonMatcher.withText("Export")).click();

		window.fileChooser("Export orders").requireVisible();

		File exportFile = new File(EXPORTED_ORDERS_FILENAME);
		window.fileChooser("Export orders").setCurrentDirectory(new File(System.getProperty("user.dir")));
		window.fileChooser("Export orders").fileNameTextBox().enterText("exported-orders-e2e.csv");
		window.fileChooser("Export orders").approveButton().click();

		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> assertTrue(exportFile.exists()));

		Files.deleteIfExists(Paths.get(EXPORTED_ORDERS_FILENAME));

		window.button(JButtonMatcher.withText("Export")).click();

		window.fileChooser("Export orders").requireVisible();
		window.fileChooser("Export orders").cancelButton().click();

		assertFalse(exportFile.exists());

	}

	@Test
	@GUITest
	public void testNewSearchesShouldResetBothOrdersListAndOrderDetails() {
		window.button(JButtonMatcher.withText("Search")).click();
		window.list().selectItem(0);
		assertThat(window.textBox("orderIdTextBox").text()).isEqualTo("12345");
		assertThat(window.textBox("paidDateTextBox").text()).isEqualTo("2025-08-05");
		window.button(JButtonMatcher.withText("Search")).click();
		assertThat(window.list().selection()).isEmpty();
		assertThat(window.textBox("orderIdTextBox").text()).isEmpty();
		assertThat(window.textBox("orderDateTextBox").text()).isEmpty();
		assertThat(window.textBox("paidDateTextBox").text()).isEmpty();
		assertThat(window.textBox("orderTotalTextBox").text()).isEmpty();
		assertThat(window.textBox("netTotalTextBox").text()).isEmpty();
		assertThat(window.textBox("paymentMethodTextBox").text()).isEmpty();
		assertThat(window.textBox("lastNameTextBox").text()).isEmpty();
		assertThat(window.textBox("addressTextBox").text()).isEmpty();
		assertThat(window.textBox("postcodeTextBox").text()).isEmpty();
		assertThat(window.textBox("cityTextBox").text()).isEmpty();
		assertThat(window.textBox("emailTextBox").text()).isEmpty();
		assertThat(window.textBox("phoneTextBox").text()).isEmpty();
		assertThat(window.textBox("productTextBox").text()).isEmpty();
		assertThat(window.textBox("firstIssueTextBox").text()).isEmpty();
		assertThat(window.textBox("lastIssueTextBox").text()).isEmpty();
		assertThat(window.textBox("notesTextBox").text()).isEmpty();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();

	}

}
