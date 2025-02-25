package org.blefuscu.apt.subscriptions.repository.mongo;

import java.time.LocalDate;
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
import static com.mongodb.client.model.Sorts.ascending;


public class OrderMongoRepository implements OrderRepository {

	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "order";
	private MongoCollection<Document> orderCollection;

	public OrderMongoRepository(MongoClient client) {
		orderCollection = client.getDatabase(SUBSCRIPTIONS_DB_NAME).getCollection(ORDER_COLLECTION_NAME);
	}

	@Override
	public List<Order> findAll() {
		return StreamSupport.stream(orderCollection.find().sort(ascending("orderId")).spliterator(), false).map(this::fromDocumentToOrder)
				.collect(Collectors.toList());
	}

	@Override
	public List<Order> findByDateRange(LocalDate fromDate, LocalDate toDate) {

		if (fromDate.compareTo(toDate) > 0) {
			throw new IllegalArgumentException("Error: End date must be later than start date");
		}

		Bson filter = Filters.and(Filters.gte("orderDate", fromDate), Filters.lte("orderDate", toDate));

		return StreamSupport.stream(orderCollection.find(filter).spliterator(), false).map(this::fromDocumentToOrder)
				.collect(Collectors.toList());
	}

	@Override
	public void save(Order order) {
		orderCollection.insertOne(
				new Document().
				append("orderId", order.getOrderId()).
				append("orderDate", order.getOrderDate()).
				append("orderTotal", order.getOrderTotal()).
				append("paymentMethodTitle", order.getPaymentMethodTitle()).
				append("orderAttributionReferrer", order.getOrderAttributionReferrer()).
				append("billingEmail", order.getBillingEmail()));
	}

	@Override
	public void delete(int id) {
		deleteOneOrder(id);
	}

	@Override
	public Order findById(int id) {
		Document d = orderCollection.find(Filters.eq("orderId", id)).first();
		if (d != null)
			return fromDocumentToOrder(d);
		return null;
	}

	@Override
	public void edit(int id, Order updatedOrder) throws NullPointerException {
		if (findById(id) == null) {
			throw new NullPointerException("Error: No order found with given Id");
		}
		deleteOneOrder(id);
		orderCollection.insertOne(
			new Document().
			append("orderId", id).
			append("orderDate", updatedOrder.getOrderDate()).
			append("orderTotal", updatedOrder.getOrderTotal()).
			append("paymentMethodTitle", updatedOrder.getPaymentMethodTitle()).
			append("orderAttributionReferrer", updatedOrder.getOrderAttributionReferrer()).
			append("billingEmail", updatedOrder.getBillingEmail())
		);
	}

	private void deleteOneOrder(int id) {
		orderCollection.deleteOne(Filters.eq("orderId", id));
	}

	private Order fromDocumentToOrder(Document d) {
		return new Order.OrderBuilder(
				d.getInteger("orderId"),
				d.get("orderDate", Date.class).toInstant().atZone(ZoneId.of("UTC")).toLocalDate(),
				d.getDouble("orderTotal"),
				d.getString("paymentMethodTitle"),
				d.getString("orderAttributionReferrer"),
				d.getString("billingEmail")).
				build();
	}
}