package org.blefuscu.apt.subscriptions.controller;

import java.time.LocalDate;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;

public class SubscriptionsController {

	private ListView listView;
	private OrderRepository orderRepository;

	public SubscriptionsController(ListView listView, OrderRepository orderRepository) {
		this.listView = listView;
		this.orderRepository = orderRepository;
	}

	public void requestOrders() {
		listView.showOrders(orderRepository.findAll());
	}

	public void requestOrders(LocalDate fromDate, LocalDate toDate) {
		if (fromDate == null) {
			throw new IllegalArgumentException("Please provide start date");
		}
		if (toDate == null) {
			throw new IllegalArgumentException("Please provide end date");
		}
		if (toDate.isBefore(fromDate)) {
			throw new IllegalArgumentException("Start date should be earlier or equal to end date");
		}
		listView.showOrders(orderRepository.findByDateRange(fromDate, toDate));
	}

	public void deleteOrder(int orderId) {
		checkOrderAvailability(orderId);
		orderRepository.delete(orderId);
	}

	public Order orderDetails(int orderId) {
		checkOrderAvailability(orderId);
		return orderRepository.findById(orderId);
	}

	public void updateOrder(int orderId, Order updatedOrder) {
		checkOrderAvailability(orderId);
		orderRepository.update(orderId, updatedOrder);		
	}
	
	private void checkOrderAvailability(int orderId) {
		if (orderRepository.findById(orderId) == null) {
			throw new IllegalArgumentException("The requested order is not available");
		}
	}


}
