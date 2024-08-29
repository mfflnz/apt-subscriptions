package org.blefuscu.apt.subscriptions.controller;

import java.time.LocalDateTime;
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
		orderView.showAllOrders(orderRepository.findAll());
		return orderRepository.findAll();
	}

	public List<Order> fetchOrders(LocalDateTime fromDate, LocalDateTime toDate) {
		if (fromDate == null) {
			throw new NullPointerException("Please provide start date");
		} else if (toDate == null) {
			throw new NullPointerException("Please provide end date");
		} else if (fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("Start date should be earlier than end date");
		}
		return orderRepository.findByDateRange(fromDate, toDate);
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
