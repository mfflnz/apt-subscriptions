package org.blefuscu.apt.subscriptions.view;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void orderAdded(Order order);

	void showError(String string, Order existingOrder);

	void orderRemoved(Order orderToDelete);

	void showAllOrders(List<Order> orders);

}
