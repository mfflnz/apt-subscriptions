package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDate;
import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;

import com.mongodb.MongoClient;

public class OrderMongoRepository implements OrderRepository {

	public OrderMongoRepository(MongoClient mongoClient) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findByDateRange(LocalDate fromDate, LocalDate toDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(int id, Order updatedOrder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub

	}

}
