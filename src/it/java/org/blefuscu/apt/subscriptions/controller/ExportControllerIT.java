package org.blefuscu.apt.subscriptions.controller;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.view.MessageSwingView;
import org.junit.Before;
import org.junit.Test;

public class ExportControllerIT {

	private static final String FILENAME = "filename.csv";
	private ExportController exportController;
	private MessageSwingView messageView;

	@Before
	public void setUp() throws Exception {
		messageView = new MessageSwingView();
		exportController = new ExportController(messageView);
		Files.deleteIfExists(Paths.get(FILENAME));
	}


	@Test
	public void testSaveDataIfFileDoesNotExistWritesFileOnDiskAndSendsAnInfoMessageToTheMessageViewAndReturnsZero() throws IOException {

		List<FormattedOrder> formattedOrders = addSampleFormattedOrders();

		assertThat(exportController.saveData(formattedOrders, FILENAME)).isZero();
		assertTrue(new File(FILENAME).exists());
		assertEquals(messageView.getMessageTextBox().getText(), "Orders exported as " + FILENAME);
	}

	@Test
	public void testSaveDataIfFileAlreadyExistsShouldSendsAnErrorMessageToTheMessageViewAndReturnANegativeValue() throws IOException {
		
		List<FormattedOrder> formattedOrders = addSampleFormattedOrders();
		
		Path filePath = Paths.get(FILENAME);
		Files.createFile(filePath);
		assertTrue(new File(FILENAME).exists());
		
		assertThat(exportController.saveData(formattedOrders, FILENAME)).isNegative();
		assertEquals(messageView.getMessageTextBox().getText(), "File " + FILENAME + " already exists");
	}
	
	@Test
	public void testSaveDataShouldCorrectlySaveOrdersToFileAndReturnZero() throws IOException {
		
		List<FormattedOrder> formattedOrders = addSampleFormattedOrders();
		File file = new File(FILENAME);

		assertThat(exportController.saveData(formattedOrders, FILENAME)).isZero();
		assertThat(file).exists().hasContent(
				"1,2025-10-09,2025-11-03,€ 65.00,€ 58.00,PayPal,Ada,Perrotta,\"via Irnerio 51\",40125,Bologna,BO,customer@emailaddress.com,+39 321 456 7890,Abbonamento cartaceo,106,111,\"Abbonamento regalato da Gianluca Boarelli\""
				+ "\n"
				+ "2,2025-10-10,2025-11-03,€ 65.00,€ 58.00,PayPal,Livia,Varrà,\"via Irnerio 51\",40125,Bologna,BO,customer@emailaddress.com,+39 321 456 7890,Abbonamento cartaceo,106,111,\"Abbonamento regalato da Gianluca Boarelli\""
				);

		
	}

	@Test
	public void testDeleteDataShouldRemoveFileFromDiskIfExists() throws IOException {
		Path filePath = Paths.get(FILENAME);
		Files.createFile(filePath);
		
		exportController.deleteData(FILENAME);
		
		assertFalse(new File(FILENAME).exists());
		assertEquals(messageView.getMessageTextBox().getText(), "File " + FILENAME + " was deleted");

		
	}
	
	private List<FormattedOrder> addSampleFormattedOrders() {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").setOrderDate("2025-10-09")
				.setPaidDate("2025-11-03").setOrderTotal("€ 65.00").setOrderNetTotal("€ 58.00")
				.setPaymentMethodTitle("PayPal").setShippingFirstName("Ada").setShippingLastName("Perrotta")
				.setShippingAddress1("via Irnerio 51").setShippingPostcode("40125").setShippingCity("Bologna")
				.setShippingState("BO").setCustomerEmail("customer@emailaddress.com")
				.setBillingPhone("+39 321 456 7890").setShippingItems("Abbonamento cartaceo").setFirstIssue("106")
				.setLastIssue("111").setCustomerNote("Abbonamento regalato da Gianluca Boarelli").build();
		FormattedOrder formattedOrder2 = new FormattedOrder.FormattedOrderBuilder("2").setOrderDate("2025-10-10")
				.setPaidDate("2025-11-03").setOrderTotal("€ 65.00").setOrderNetTotal("€ 58.00")
				.setPaymentMethodTitle("PayPal").setShippingFirstName("Livia").setShippingLastName("Varrà")
				.setShippingAddress1("via Irnerio 51").setShippingPostcode("40125").setShippingCity("Bologna")
				.setShippingState("BO").setCustomerEmail("customer@emailaddress.com")
				.setBillingPhone("+39 321 456 7890").setShippingItems("Abbonamento cartaceo").setFirstIssue("106")
				.setLastIssue("111").setCustomerNote("Abbonamento regalato da Gianluca Boarelli").build();
	
		return asList(formattedOrder1, formattedOrder2);
	}


}
