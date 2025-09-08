package org.blefuscu.apt.subscriptions.controller;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;

public interface ExportController {

	public void saveData(List<FormattedOrder> formattedOrders, String location);
	public void deleteData(String location);
	
}