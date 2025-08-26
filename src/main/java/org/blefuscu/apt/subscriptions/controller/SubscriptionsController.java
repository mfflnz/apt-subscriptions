package org.blefuscu.apt.subscriptions.controller;

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

}
