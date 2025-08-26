package org.blefuscu.apt.subscriptions.view;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void showOrderDetails(Order order);
	void orderUpdated(Order orderToUpdate);
	void orderDeleted(Order orderToDelete);
	void showMessage(String string);
	
}
