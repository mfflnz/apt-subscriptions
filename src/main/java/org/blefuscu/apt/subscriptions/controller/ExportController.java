package org.blefuscu.apt.subscriptions.controller;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.view.MessageSwingView;

public class ExportController {

	private MessageSwingView messageSwingView;

	public ExportController(MessageSwingView messageSwingView) {
		super();
		this.messageSwingView = messageSwingView;
	}
	
	public int saveData(List<FormattedOrder> formattedOrders, String location) {
		// TODO: scrivi file su disco -> IT
		messageSwingView.showInfoMessage("Orders exported as " + location);
		return 0;
	}
	public void deleteData(String location) {
		// TODO: cancella il file dal disco -> IT
		messageSwingView.showInfoMessage("File " + location + " was deleted");

	}
	
}