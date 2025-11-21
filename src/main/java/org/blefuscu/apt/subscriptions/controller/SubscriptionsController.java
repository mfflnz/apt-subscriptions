package org.blefuscu.apt.subscriptions.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListView;
import org.blefuscu.apt.subscriptions.view.MessageView;
import org.blefuscu.apt.subscriptions.view.OrderView;
import org.bson.conversions.Bson;

import com.mongodb.client.model.Filters;

public class SubscriptionsController {

	private ListView listView;
	private OrderView orderView;
	private MessageView messageView;
	private OrderRepository orderRepository;
	private ExportController exportController;
	private static final String THE_REQUESTED_ORDER_IS_NOT_AVAILABLE = "The requested order is not available";
	private static final String START_DATE_SHOULD_BE_EARLIER_OR_EQUAL_TO_END_DATE = "Start date should be earlier or equal to end date";
	private static final String PLEASE_PROVIDE_START_DATE = "Please provide start date";
	private static final String PLEASE_PROVIDE_END_DATE = "Please provide end date";
	private static final String PLEASE_PROVIDE_FILE_NAME = "Please provide file name";
	private static final String ERROR_NO_ORDERS_TO_EXPORT = "Error: no orders to export";
	private static final String ERROR_EXPORTING_FILE = "Error exporting file";

	public SubscriptionsController(ListView listView, OrderView orderView, MessageView messageView,
			OrderRepository orderRepository, ExportController exportController) {
		this.listView = listView;
		this.orderView = orderView;
		this.messageView = messageView;
		this.orderRepository = orderRepository;
		this.exportController = exportController;
	}

	public void requestOrders() {
		Bson filter = null;
		listView.showOrders(orderRepository.findAll());
		messageView.showInfoMessage("" + orderRepository.countOrders(filter) + " orders found");
	}

	public void requestOrders(LocalDate fromDate, LocalDate toDate) {

		if (fromDate == null) {
			sendErrorMessage(PLEASE_PROVIDE_START_DATE);
			throw new IllegalArgumentException(PLEASE_PROVIDE_START_DATE);
		}
		if (toDate == null) {
			sendErrorMessage(PLEASE_PROVIDE_END_DATE);
			throw new IllegalArgumentException(PLEASE_PROVIDE_END_DATE);
		}
		if (toDate.isBefore(fromDate)) {
			sendErrorMessage(START_DATE_SHOULD_BE_EARLIER_OR_EQUAL_TO_END_DATE);
			throw new IllegalArgumentException(START_DATE_SHOULD_BE_EARLIER_OR_EQUAL_TO_END_DATE);
		}

		Bson filter = Filters.and(Filters.gte("order_date", fromDate), Filters.lte("order_date", toDate));
		
		listView.showOrders(orderRepository.findByDateRange(fromDate, toDate));
		messageView.showInfoMessage("" + orderRepository.countOrders(filter) + " orders found");
	}

	public Order orderDetails(int orderId) {
		Order order = orderRepository.findById(orderId);

		if (order == null) {
			throw new IllegalArgumentException(THE_REQUESTED_ORDER_IS_NOT_AVAILABLE);
		}
		orderView.showOrderDetails(orderRepository.findById(orderId));
		return orderRepository.findById(orderId);
	}

	public void updateOrder(int orderId, Order updatedOrder) {
		Order orderToUpdate = orderRepository.findById(orderId);
		if (orderToUpdate == null) {
			throw new IllegalArgumentException(THE_REQUESTED_ORDER_IS_NOT_AVAILABLE);
		}
		orderRepository.update(orderId, updatedOrder);
		orderView.showOrderDetails(updatedOrder);
		orderView.orderUpdated(orderId);
		listView.orderUpdated(orderId, updatedOrder);
	}

	public void deleteOrder(int orderId) {
		Order orderToDelete = orderRepository.findById(orderId);
		if (orderToDelete == null) {
			throw new IllegalArgumentException(THE_REQUESTED_ORDER_IS_NOT_AVAILABLE);
		}
		orderView.orderDeleted(orderId);
		listView.orderDeleted(orderId);
		orderRepository.delete(orderId);
	}

	public FormattedOrder formatOrder(Order orderToFormat) {

		// TODO: controlla questo pattern
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

		// Se la stringa non è già formattata (cioè se non contiene occorrenze di '|',
		// '{' o '}', toglie il prefisso "items:" e il suffisso a partire dalla prima
		// occorrenza di '|'
		if ((shippingItems != null) && (shippingItems.matches(".*[|{}].*"))) {
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
			sendErrorMessage(ERROR_NO_ORDERS_TO_EXPORT);
			throw new IllegalArgumentException(ERROR_NO_ORDERS_TO_EXPORT);
		}

		if (filename == null || filename.isEmpty()) {
			sendErrorMessage(PLEASE_PROVIDE_FILE_NAME);
			throw new IllegalArgumentException(PLEASE_PROVIDE_FILE_NAME);
		}

		try {
			exportController.saveData(ordersToExport, filename);
		} catch (IOException e) {
			sendErrorMessage(ERROR_EXPORTING_FILE);

		}
	}

	public void sendErrorMessage(String errorMessage) {
		messageView.showErrorMessage(errorMessage);
	}

	public void sendInfoMessage(String infoMessage) {
		messageView.showInfoMessage(infoMessage);
		
	}

}
