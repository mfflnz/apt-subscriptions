package org.blefuscu.apt.subscriptions.controller;

import java.time.LocalDate;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;

public class SubscriptionsController {

	private OrderView orderView;
	private ListView listView;
	private OrderRepository orderRepository;

	public SubscriptionsController(OrderView orderView, ListView listView, OrderRepository orderRepository) {
		this.orderView = orderView;
		this.listView = listView;
		this.orderRepository = orderRepository;
	}

	public void requestOrders() {
		listView.showOrders(orderRepository.findAll());
	}
	
	public void requestOrders(LocalDate fromDate, LocalDate toDate) {
		listView.showOrders(orderRepository.findByDateRange(fromDate, toDate));
	}

	public void newOrder(Order order) {
		Order existingOrder = orderRepository.findById(order.getOrderId());
		if (existingOrder != null) {
			orderView.showError("Already existing order with id " + order.getOrderId(), existingOrder);
			return;
		}
		orderRepository.save(order);
		orderView.orderAdded(order);
	}

	public void deleteOrder(Order orderToDelete) {
		if (orderRepository.findById(orderToDelete.getOrderId()) == null) {
			orderView.showError("No existing order with id " + orderToDelete.getOrderId(), orderToDelete);
			return;
		}
		orderRepository.delete(orderToDelete.getOrderId());
		orderView.orderRemoved(orderToDelete);
	}

}
