package org.blefuscu.apt.subscriptions.view;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

public interface ListView {

	void showOrders(List<Order> orders);
	void orderUpdated(int orderId, Order order);
	void orderDeleted(Order order);
	void csvExported(List<Order> orders);
	void showMessage(String message);
	
}
