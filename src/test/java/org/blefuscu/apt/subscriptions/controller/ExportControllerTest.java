package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.view.MessageSwingView;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static java.util.Arrays.asList;

public class ExportControllerTest {

	@InjectMocks
	private ExportController exportController;
	
	@Mock
	private MessageSwingView messageSwingView;

	private static final String FILENAME = "filename.csv";
	private AutoCloseable closeable;

	@Before
	public void setUp() throws IOException {
		closeable = MockitoAnnotations.openMocks(this);
		Files.deleteIfExists(Paths.get(FILENAME));

	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testSaveDataShouldDelegateMessageViewToShowAnInfoMessageIfExportWasSuccessful() throws IOException {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		FormattedOrder formattedOrder2 = new FormattedOrder.FormattedOrderBuilder("2").build();
		FormattedOrder formattedOrder3 = new FormattedOrder.FormattedOrderBuilder("3").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1, formattedOrder2, formattedOrder3);
		
		exportController.saveData(formattedOrders, "export.csv");
		
		verify(messageSwingView).showInfoMessage("Orders exported as export.csv");
		
	}
	
	@Test
	public void testDeleteDataShouldDelegateMessageViewToShowAnInfoMessageIfDeletionWasSuccessful() throws IOException {
		
		exportController.deleteData("export.csv");
		
		verify(messageSwingView).showInfoMessage("File export.csv was deleted");
	}
	
	@Test
	public void testSaveDataShouldDelegateMessageViewToShowAnErrorMessageIfExportWasNotSuccessful() throws IOException {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		FormattedOrder formattedOrder2 = new FormattedOrder.FormattedOrderBuilder("2").build();
		FormattedOrder formattedOrder3 = new FormattedOrder.FormattedOrderBuilder("3").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1, formattedOrder2, formattedOrder3);
		
		exportController.saveData(formattedOrders, FILENAME);
		exportController.saveData(formattedOrders, FILENAME);
		
		verify(messageSwingView).showErrorMessage("File " + FILENAME + " already exists");
		
	}
	


}
