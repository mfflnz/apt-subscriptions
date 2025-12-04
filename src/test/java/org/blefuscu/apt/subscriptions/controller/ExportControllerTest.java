package org.blefuscu.apt.subscriptions.controller;

import static org.junit.Assert.assertEquals;
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

	private static final String FILENAME = "export.csv";
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
		
		int result = exportController.saveData(formattedOrders, FILENAME);
		
		assertEquals(0, result);
		verify(messageSwingView).showInfoMessage("Orders exported as " + FILENAME);
		
	}
	
	@Test
	public void testDeleteDataShouldDelegateMessageViewToShowAnInfoMessageIfDeletionWasSuccessful() throws IOException {
		
		exportController.deleteData(FILENAME);
		
		verify(messageSwingView).showInfoMessage("File " + FILENAME + " was deleted");
	}
	
	@Test
	public void testSaveDataShouldDelegateMessageViewToShowAnErrorMessageIfExportWasNotSuccessful() throws IOException {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		FormattedOrder formattedOrder2 = new FormattedOrder.FormattedOrderBuilder("2").build();
		FormattedOrder formattedOrder3 = new FormattedOrder.FormattedOrderBuilder("3").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1, formattedOrder2, formattedOrder3);
		
		int result1 = exportController.saveData(formattedOrders, FILENAME);
		int result2 = exportController.saveData(formattedOrders, FILENAME);
		
		assertEquals(0, result1);
		assertEquals(-1, result2);
		verify(messageSwingView).showErrorMessage("File " + FILENAME + " already exists");

		
	}
	


}
