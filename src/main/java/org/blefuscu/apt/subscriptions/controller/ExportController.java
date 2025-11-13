package org.blefuscu.apt.subscriptions.controller;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.view.MessageView;

public class ExportController {

	private static final int EXPORT_SUCCESS = 0;
	private static final int EXPORT_FAILURE = -1;
	private MessageView messageView;

	public ExportController(MessageView messageView) {
		this.messageView = messageView;
	}

	public int saveData(List<FormattedOrder> formattedOrders, String location) throws IOException {
		Path filePath = Paths.get(location);

		try {
			Files.createFile(filePath);

			for (FormattedOrder formattedOrder : formattedOrders) {

				String formattedOrderToSaveAsString = "" + formattedOrder.getOrderId() + ","
						+ formattedOrder.getOrderDate() + "," + formattedOrder.getPaidDate() + ","
						+ formattedOrder.getOrderTotal() + "," + formattedOrder.getOrderNetTotal() + ","
						+ formattedOrder.getPaymentMethodTitle() + "," + formattedOrder.getShippingFirstName() + ","
						+ formattedOrder.getShippingLastName() + ",\"" + formattedOrder.getShippingAddress1() + "\""
						+ "," + formattedOrder.getShippingPostcode() + "," + formattedOrder.getShippingCity() + ","
						+ formattedOrder.getShippingState() + "," + formattedOrder.getCustomerEmail() + ","
						+ formattedOrder.getBillingPhone() + "," + formattedOrder.getShippingItems() + ","
						+ formattedOrder.getFirstIssue() + "," + formattedOrder.getLastIssue() + ",\""
						+ formattedOrder.getCustomerNote() + "\"" + "\n";

				Files.write(filePath, formattedOrderToSaveAsString.getBytes(), StandardOpenOption.APPEND);
			}
		} catch (FileAlreadyExistsException e) {
			messageView.showErrorMessage("File " + location + " already exists");
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception", e);
			return EXPORT_FAILURE;
		}

		messageView.showInfoMessage("Orders exported as " + location);
		return EXPORT_SUCCESS;
	}

	public void deleteData(String location) throws IOException {
		Files.deleteIfExists(Paths.get(location));
		messageView.showInfoMessage("File " + location + " was deleted");

	}

}