package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDate;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;
import org.bson.conversions.Bson;

public interface OrderRepository {

	public List<Order> findAll();
	public List<Order> findByDateRange(LocalDate fromDate, LocalDate toDate);
	public Order findById(int id);
	public void save(Order order);
	public void update(int id, Order updatedOrder);
	public void delete(int id);
	public long countOrders(Bson filter);
	
}
