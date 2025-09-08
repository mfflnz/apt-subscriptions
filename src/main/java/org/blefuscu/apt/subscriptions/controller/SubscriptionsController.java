package org.blefuscu.apt.subscriptions.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.OrderView;

public class SubscriptionsController {

	private ListView listView;
	private OrderView orderView;
	private OrderRepository orderRepository;
	private ExportController exportManager;

	public SubscriptionsController(ListView listView, OrderView orderView, OrderRepository orderRepository, ExportController exportManager) {
		this.listView = listView;
		this.orderView = orderView;
		this.orderRepository = orderRepository;
		this.exportManager = exportManager;
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

	public Order orderDetails(int orderId) {
		Order order = orderRepository.findById(orderId);

		if (order == null) {
			throw new IllegalArgumentException("The requested order is not available");
		}
		orderView.showOrderDetails(orderRepository.findById(orderId));
		return orderRepository.findById(orderId);
	}
	
	public void updateOrder(int orderId, Order updatedOrder) {
		Order orderToUpdate = orderRepository.findById(orderId);
		if (orderToUpdate == null) {
			throw new IllegalArgumentException("The requested order is not available");
		}
		orderRepository.update(orderId, updatedOrder);
		orderView.orderUpdated(orderId, updatedOrder);
		listView.orderUpdated(orderId, updatedOrder);
	}
	
	public void deleteOrder(int orderId) {
		Order orderToDelete = orderRepository.findById(orderId);
		if (orderToDelete == null) {
			throw new IllegalArgumentException("The requested order is not available");
		}
		orderView.orderDeleted(orderToDelete);
		listView.orderDeleted(orderToDelete);
		orderRepository.delete(orderId);
	}

	// TODO: Spostare nell'ExportController
	public FormattedOrder formatOrder(Order orderToFormat) {

		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String orderDate = dateTimeFormatter.format(orderToFormat.getOrderDate());
		String paidDate;
		String orderTotal = "€ " + Double.toString(orderToFormat.getOrderTotal());
		String orderNetTotal = "€ " + Double.toString(orderToFormat.getOrderNetTotal());
		String paymentMethodTitle = orderToFormat.getPaymentMethodTitle();
		String shippingFirstName = orderToFormat.getShippingFirstName();
		String shippingLastName = orderToFormat.getShippingLastName();
		String shippingAddress1 = orderToFormat.getShippingAddress1();
		String shippingPostcode = orderToFormat.getShippingPostcode();
		String shippingCity = orderToFormat.getShippingCity();
		String shippingState = orderToFormat.getShippingState();
		String customerEmail = orderToFormat.getCustomerEmail();
		String billingPhone = orderToFormat.getBillingPhone();
		String shippingItems = orderToFormat.getShippingItems();
		String firstIssue = Integer.toString(orderToFormat.getFirstIssue());
		String lastIssue = Integer.toString(orderToFormat.getLastIssue());
		String customerNote = orderToFormat.getCustomerNote();

		if (orderToFormat.getPaidDate() == null) {
			paidDate = "";
		} else {
			paidDate = dateTimeFormatter.format(orderToFormat.getPaidDate());
		}

		if (("".equals(shippingFirstName)) || (shippingFirstName == null)) {
			shippingFirstName = orderToFormat.getBillingFirstName();
		}

		if (("".equals(shippingLastName)) || (shippingLastName == null)) {
			shippingLastName = orderToFormat.getBillingLastName();
		}

		if (("".equals(shippingAddress1)) || (shippingAddress1 == null)) {
			shippingAddress1 = orderToFormat.getBillingAddress1();
		}

		if (("".equals(shippingPostcode)) || (shippingPostcode == null)) {
			shippingPostcode = orderToFormat.getBillingPostcode();
		}

		if (("".equals(shippingCity)) || (shippingCity == null)) {
			shippingCity = orderToFormat.getBillingCity();
		}

		if (("".equals(shippingState)) || (shippingState == null)) {
			shippingState = orderToFormat.getBillingState();
		}

		if (shippingItems != null) {
			// Toglie il prefisso "items:" e il suffisso a partire dalla prima occorrenza di
			// "|"
			shippingItems = shippingItems.substring(6, shippingItems.indexOf("|"));
		}

		return new FormattedOrder.FormattedOrderBuilder(Integer.toString(orderToFormat.getOrderId()))
				.setOrderDate(orderDate).setPaidDate(paidDate).setOrderTotal(orderTotal).setOrderNetTotal(orderNetTotal)
				.setPaymentMethodTitle(paymentMethodTitle).setShippingFirstName(shippingFirstName)
				.setShippingLastName(shippingLastName).setShippingAddress1(shippingAddress1)
				.setShippingPostcode(shippingPostcode).setShippingCity(shippingCity).setShippingState(shippingState)
				.setCustomerEmail(customerEmail).setBillingPhone(billingPhone).setShippingItems(shippingItems)
				.setFirstIssue(firstIssue).setLastIssue(lastIssue).setCustomerNote(customerNote).build();
	}

	public void exportOrders(List<FormattedOrder> ordersToExport, String filename) {

		if (ordersToExport == null || ordersToExport.isEmpty()) {
			throw new IllegalArgumentException("Error: no orders to export");
		}

		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Please provide file name");
		}

		exportManager.saveData(ordersToExport, filename);
	}

}
