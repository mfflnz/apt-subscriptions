package org.blefuscu.apt.subscriptions.controller;

import java.util.List;

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

}
