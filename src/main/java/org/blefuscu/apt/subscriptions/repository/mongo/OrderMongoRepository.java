package org.blefuscu.apt.subscriptions.repository.mongo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

public class OrderMongoRepository implements OrderRepository {

	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "order";
	private MongoCollection<Document> orderCollection;

	public OrderMongoRepository(MongoClient client) {
		orderCollection = client
				.getDatabase(SUBSCRIPTIONS_DB_NAME)
				.getCollection(ORDER_COLLECTION_NAME);
	}

	@Override
	public List<Order> findAll() {
		return StreamSupport
				.stream(orderCollection.find().spliterator(), false)
				.map(this::fromDocumentToOrder)
				.collect(Collectors.toList());
	}

	@Override
	public List<Order> findByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {

		if (fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("Error: End date must be later than start date");
		}
		
		Bson filter = Filters.and(Filters.gte("orderDate", fromDate), Filters.lte("orderDate", toDate));
		
		return StreamSupport
				.stream(orderCollection.find(filter).spliterator(), false)
				.map(this::fromDocumentToOrder)
				.collect(Collectors.toList());
	}

	private Order fromDocumentToOrder(Document d) {
		return new Order(d.getInteger("orderId"), d.get("orderDate", Date.class).toInstant()
		  .atZone(ZoneId.of("UTC"))
		  .toLocalDateTime());
	}

}