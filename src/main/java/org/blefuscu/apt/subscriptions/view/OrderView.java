package org.blefuscu.apt.subscriptions.view;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderView {

	void orderAdded(Order order);

	void showError(String string, Order existingOrder);

	void orderRemoved(Order orderToDelete);

}
