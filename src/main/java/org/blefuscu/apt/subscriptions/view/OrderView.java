package org.blefuscu.apt.subscriptions.view;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void showOrderDetails(Order order);

	void orderAdded(Order order);

	void orderRemoved(Order orderToDelete);

	void showError(String string, Order existingOrder);

}
