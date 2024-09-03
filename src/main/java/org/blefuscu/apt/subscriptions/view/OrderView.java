package org.blefuscu.apt.subscriptions.view;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void showOrders(List<Order> orders);

	void orderAdded(Order order);

	void orderRemoved(Order orderToDelete);

	void showError(String string, Order existingOrder);

}
