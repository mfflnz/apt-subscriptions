package org.blefuscu.apt.subscriptions.controller;

import static org.mockito.Mockito.verify;

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

	private AutoCloseable closeable;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@After
	public void tearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testSaveDataShouldDelegateMessageViewToShowAnInfoMessageIfExportWasSuccessful() {
		FormattedOrder formattedOrder1 = new FormattedOrder.FormattedOrderBuilder("1").build();
		FormattedOrder formattedOrder2 = new FormattedOrder.FormattedOrderBuilder("2").build();
		FormattedOrder formattedOrder3 = new FormattedOrder.FormattedOrderBuilder("3").build();
		List<FormattedOrder> formattedOrders = asList(formattedOrder1, formattedOrder2, formattedOrder3);
		
		exportController.saveData(formattedOrders, "export.csv");
		
		verify(messageSwingView).showInfoMessage("Orders exported as export.csv");
	}
	
	@Test
	public void testDeleteDataShouldDelegateMessageViewToShowAnInfoMessageIfDeletionWasSuccessful() {
		
		exportController.deleteData("export.csv");
		
		verify(messageSwingView).showInfoMessage("File export.csv was deleted");
	}
	


}
