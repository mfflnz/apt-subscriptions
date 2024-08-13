package org.blefuscu.apt.subscriptions.repository.mongo;

import java.util.List;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;

public class OrderMongoRepository implements OrderRepository {
	
	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "order";
	private MongoCollection<Document> orderCollection;
	
	public OrderMongoRepository(MongoClient client) {
		orderCollection = client.getDatabase(SUBSCRIPTIONS_DB_NAME).getCollection(ORDER_COLLECTION_NAME);
	}

	@Override
	public List<Order> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Order> findByDateRange(String fromDate, String toDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
