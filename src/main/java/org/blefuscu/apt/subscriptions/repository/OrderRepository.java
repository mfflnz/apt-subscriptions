package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

public interface OrderRepository {
	
	public List<Order> findAll();
	public List<Order> findByDateRange(LocalDateTime fromDate, LocalDateTime toDate);

}
