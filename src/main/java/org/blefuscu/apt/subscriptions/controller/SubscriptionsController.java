package org.blefuscu.apt.subscriptions.controller;

import java.util.List;
import java.util.function.IntPredicate;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.OrderView;

public class SubscriptionsController {

	private OrderView orderView;
	private OrderRepository orderRepository;

	public SubscriptionsController(OrderView orderView, OrderRepository orderRepository) {
		this.orderView = orderView;
		this.orderRepository = orderRepository;
	}

	public List<Order> fetchOrders() {
		return orderRepository.findAll();
	}

	public List<Order> fetchOrders(String fromDate, String toDate) {
		if (fromDate.compareTo("") == 0) {
			throw new IllegalArgumentException("Please provide start date");
		} else if (toDate.compareTo("") == 0) {
			throw new IllegalArgumentException("Please provide end date");
		} else if (fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("Start date should be earlier than end date");
		} 
		return orderRepository.findByDateRange(fromDate, toDate);
	}

}
