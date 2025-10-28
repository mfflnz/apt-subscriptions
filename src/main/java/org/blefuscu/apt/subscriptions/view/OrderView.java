package org.blefuscu.apt.subscriptions.view;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void showOrderDetails(Order order);
	void orderUpdated(int orderId, Order orderToUpdate);
	void orderDeleted(Order orderToDelete);
}
