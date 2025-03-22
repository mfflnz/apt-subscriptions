package org.blefuscu.apt.subscriptions.view;

import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

import java.time.LocalDate;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testcontainers.containers.MongoDBContainer;

import com.mongodb.MongoClient;

import org.bson.Document;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

@RunWith(GUITestRunner.class)
public class SubscriptionsSwingAppE2E extends AssertJSwingJUnitTestCase {

	@ClassRule
	public static final MongoDBContainer mongo = new MongoDBContainer("mongo:4.4.3");

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";

	private MongoClient mongoClient;

	private FrameFixture searchWindow, listWindow;

	@Override
	protected void onSetUp() throws Exception {
		String containerIpAddress = mongo.getHost();
		Integer mappedPort = mongo.getFirstMappedPort();
		mongoClient = new MongoClient(containerIpAddress, mappedPort);

		mongoClient.getDatabase(DB_NAME).drop();

		addTestOrderToDatabase(1, LocalDate.of(2025, 3, 1), 65.00, "Bonifico", "Abbonamento cartaceo + digitale",
				"test@email.com");
		addTestOrderToDatabase(2, LocalDate.of(2025, 3, 2), 65.00, "PayPal", "Abbonamento cartaceo + digitale",
				"othertest@email.com");
		addTestOrderToDatabase(3, LocalDate.of(2024, 10, 1), 65.00, "Carta di credito",
				"Abbonamento cartaceo + digitale", "oldtest@email.com");

		application("org.blefuscu.apt.subscriptions.app.swing.SubscriptionsSwingApp")
				.withArgs("--mongo-host=" + containerIpAddress, "--mongo-port=" + mappedPort.toString(),
						"--db-name=" + DB_NAME, "--db-collection=" + COLLECTION_NAME)
				.start();

		searchWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Search by date".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Override
	protected void onTearDown() throws Exception {
		mongoClient.close();
	}

	@Test
	@GUITest
	public void testOnSearchAllCorrectDatabaseElementsAreShown() {

		searchWindow.textBox("fromTextBox").deleteText().enterText("2025-01-01");
		searchWindow.textBox("toTextBox").deleteText().enterText("2025-04-01");
		searchWindow.button(JButtonMatcher.withText("Search")).click();

		listWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "List View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

		assertThat(listWindow.list().contents()).anySatisfy(e -> assertThat(e).contains("1", "2025-03-01"))
				.anySatisfy(e -> assertThat(e).contains("2", "2025-03-02"));
	}

	@Test
	@GUITest
	public void testOnDeleteSuccess() {
		
		searchWindow.textBox("fromTextBox").deleteText().enterText("2025-01-01");
		searchWindow.textBox("toTextBox").deleteText().enterText("2025-04-01");
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		
		listWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "List View".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());
		
		listWindow.list("ordersList").selectItem(1);
		listWindow.button(JButtonMatcher.withText("Delete")).click();
	}

	@Test
	@GUITest
	public void testOnDeleteError() {

	}

	private void addTestOrderToDatabase(int orderId, LocalDate orderDate, double orderTotal, String paymentMethodTitle,
			String orderAttributionReferrer, String billingEmail) {
		mongoClient.getDatabase(DB_NAME).getCollection(COLLECTION_NAME)
				.insertOne(new Document().append("orderId", orderId).append("orderDate", orderDate)
						.append("orderTotal", orderTotal).append("paymentMethodTitle", paymentMethodTitle)
						.append("orderAttributionReferrer", orderAttributionReferrer)
						.append("billingEmail", billingEmail).append("depositDate", LocalDate.of(2025, 3, 15))
						.append("netOrderTotal", 63.50).append("billingFirstName", "FirstName")
						.append("billingLastName", "LastName").append("billingCompany", "Company")
						.append("billingAddress1", "Address1").append("billingAddress2", "Address2")
						.append("billingPostcode", "Postcode").append("billingCity", "City")
						.append("billingState", "State").append("billingCountry", "Country")
						.append("billingPhone", "Phone").append("firstIssue", 118).append("lastIssue", 123)
						.append("notes", "Notes").append("shippingFirstName", "FirstName")
						.append("shippingLastName", "LastName").append("shippingCompany", "Company")
						.append("shippingEmail", "Email").append("shippingPhone", "Phone")
						.append("shippingAddress1", "Address1").append("shippingAddress2", "Address2")
						.append("shippingPostcode", "Postcode").append("shippingCity", "City")
						.append("shippingState", "State").append("shippingCountry", "Country"));
	}

}
