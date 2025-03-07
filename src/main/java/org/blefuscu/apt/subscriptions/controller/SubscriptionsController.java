package org.blefuscu.apt.subscriptions.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.List;

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
		listView.orderRemoved(orderToDelete);
		
	}

	public void updateOrder(int orderToUpdateId, Order updatedOrder) {
		if (orderRepository.findById(orderToUpdateId) == null) {
			orderView.showError("No existing order with id " + orderToUpdateId);
			return;
		}
		orderRepository.edit(orderToUpdateId, updatedOrder);
		orderView.showOrderDetails(updatedOrder);
	}

	public Order orderDetails(int orderId) {
		return orderRepository.findById(orderId);
	}

	public int exportOrders(String filename, List<Order> ordersListToSave) throws IOException {
		try {
			Path filePath = Paths.get(filename);
			Files.createFile(filePath);
			
			for (Order order : ordersListToSave) {
				String orderToSaveAsString = "" + order.getOrderId() 
						+ "," + order.getOrderDate()
						+ "," + order.getDepositDate()
						+ "," + order.getOrderTotal()
						+ "," + order.getNetOrderTotal()
						+ "," + order.getPaymentMethodTitle()
						+ "," + order.getShippingFirstName()
						+ "," + order.getShippingLastName()
						+ ",\"" + order.getShippingAddress1() + "\""
						+ "," + order.getShippingPostcode()
						+ "," + order.getShippingCity()
						+ "," + order.getShippingState()
						+ "," + order.getBillingEmail()
						+ "," + order.getShippingPhone()
						+ "," + order.getOrderAttributionReferrer()
						+ "," + order.getFirstIssue()
						+ "," + order.getLastIssue()
						+ "," + order.getNotes()
						+ "\r";
				Files.write(filePath, orderToSaveAsString.getBytes(), StandardOpenOption.APPEND);	
			}
			
			return 1;
		} catch (IOException e) {
			return -1;
		}
		
	}

}
