package org.blefuscu.apt.subscriptions.repository;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderRepository {
	
	public List<Order> findAll();
	public List<Order> findByDateRange(String fromDate, String toDate);

}
