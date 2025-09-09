package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.blefuscu.apt.subscriptions.model.Order;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class OrderMongoRepository implements OrderRepository {

	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "orders";
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private MongoCollection<Document> orderCollection;

	public OrderMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		orderCollection = mongoClient.getDatabase(databaseName).getCollection(collectionName);
	}

	@Override
	public List<Order> findAll() {
		return StreamSupport.stream(orderCollection.find().spliterator(), false).map(this::fromDocumentToOrder)
				.collect(Collectors.toList());
	}

	@Override
	public List<Order> findByDateRange(LocalDate fromDate, LocalDate toDate) {

		if (fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("Error: End date must be later than start date");
		}

		Bson filter = Filters.and(Filters.gte("order_date", fromDate.toString()),
				Filters.lte("order_date", toDate.toString()));

		return StreamSupport.stream(orderCollection.find(filter).spliterator(), false).map(this::fromDocumentToOrder)
				.collect(Collectors.toList());

	}

	@Override
	public Order findById(int id) {
		return fromDocumentToOrder(orderCollection.find(Filters.eq("order_id", id)).first());
	}

	@Override
	public void update(int id, Order updatedOrder) throws IllegalArgumentException {

		if (findById(id) == null) {
			throw new IllegalArgumentException("Error: Order not found");
		}
		
		orderCollection.deleteOne(Filters.eq("order_id", id));
		orderCollection.insertOne(fromOrderToDocument(updatedOrder));

	}

	@Override
	public void delete(int id) throws IllegalArgumentException {
		if (findById(id) == null) {
			throw new IllegalArgumentException("Error: Order not found");
		}
		orderCollection.deleteOne(Filters.eq("order_id", id));
	}

	private Order fromDocumentToOrder(Document d) {

		if (d == null)
			return null;

		return new Order.OrderBuilder(d.getInteger("order_id"), LocalDate.parse(d.getString("order_date"), FORMATTER),
				d.getString("customer_email"))
				/*
				 * TODO: set other fields .setOrderNumber(d.getInteger("order_number", 0))
				 * .setPaidDate(LocalDate.parse(d.getString("paid_date"), formatter))
				 * .setStatus(d.get("status", ""))
				 */
				.build();

	}

	private Document fromOrderToDocument(Order order) {
		return new Document().append("order_id", order.getOrderId())
				.append("order_date", order.getOrderDate().toString())
				.append("customer_email", order.getCustomerEmail());
	}

}