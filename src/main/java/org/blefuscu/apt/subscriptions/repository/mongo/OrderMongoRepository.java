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

	public OrderMongoRepository(MongoClient client, String databaseName, String collectionName) {
		orderCollection = client.getDatabase(databaseName).getCollection(collectionName);
	}

	@Override
	public List<Order> findAll() {
		return StreamSupport.stream(orderCollection.find().sort(ascending("orderId")).spliterator(), false)
				.map(this::fromDocumentToOrder).collect(Collectors.toList());
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
		orderCollection.insertOne(new Document().append("orderId", order.getOrderId())
				.append("orderDate", order.getOrderDate()).append("orderTotal", order.getOrderTotal())
				.append("paymentMethodTitle", order.getPaymentMethodTitle())
				.append("orderAttributionReferrer", order.getOrderAttributionReferrer())
				.append("billingEmail", order.getBillingEmail()).append("depositDate", order.getDepositDate())
				.append("netOrderTotal", order.getNetOrderTotal())
				.append("billingFirstName", order.getBillingFirstName())
				.append("billingLastName", order.getBillingLastName())
				.append("billingCompany", order.getBillingCompany())
				.append("billingAddress1", order.getBillingAddress1())
				.append("billingAddress2", order.getBillingAddress2())
				.append("billingPostcode", order.getBillingPostcode()).append("billingCity", order.getBillingCity())
				.append("billingState", order.getBillingState()).append("billingCountry", order.getBillingCountry())
				.append("billingPhone", order.getBillingPhone()).append("firstIssue", order.getFirstIssue())
				.append("lastIssue", order.getLastIssue()).append("notes", order.getNotes())
				.append("shippingFirstName", order.getShippingFirstName())
				.append("shippingLastName", order.getShippingLastName())
				.append("shippingCompany", order.getShippingCompany()).append("shippingEmail", order.getShippingEmail())
				.append("shippingPhone", order.getShippingPhone())
				.append("shippingAddress1", order.getShippingAddress1())
				.append("shippingAddress2", order.getShippingAddress2())
				.append("shippingPostcode", order.getShippingPostcode()).append("shippingCity", order.getShippingCity())
				.append("shippingState", order.getShippingState())
				.append("shippingCountry", order.getShippingCountry()));
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
		orderCollection.insertOne(new Document().append("orderId", id).append("orderDate", updatedOrder.getOrderDate())
				.append("orderTotal", updatedOrder.getOrderTotal())
				.append("paymentMethodTitle", updatedOrder.getPaymentMethodTitle())
				.append("orderAttributionReferrer", updatedOrder.getOrderAttributionReferrer())
				.append("billingEmail", updatedOrder.getBillingEmail())
				.append("depositDate", updatedOrder.getDepositDate())
				.append("shippingFirstName", updatedOrder.getShippingFirstName())
				.append("shippingLastName", updatedOrder.getShippingLastName())
				
				);
	}

	private void deleteOneOrder(int id) {
		orderCollection.deleteOne(Filters.eq("orderId", id));
	}

	private Order fromDocumentToOrder(Document d) {
		
		Order order = new Order.OrderBuilder(d.getInteger("orderId"),
				d.get("orderDate", Date.class).toInstant().atZone(ZoneId.of("UTC")).toLocalDate(),
				d.getDouble("orderTotal"), d.getString("paymentMethodTitle"), d.getString("orderAttributionReferrer"),
				d.getString("billingEmail")).build();
	/*
		order.setDepositDate(d.get("depositDate", Date.class).toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
		order.setNetOrderTotal(d.getDouble("netOrderTotal"));
		order.setBillingFirstName(d.getString("billingFirstName"));
		order.setBillingLastName(d.getString("billingLastName"));
		order.setBillingCompany(d.getString("billingCompany"));
		order.setBillingAddress1(d.getString("billingAddress1"));
		order.setBillingAddress2(d.getString("billingAddress2"));
		order.setBillingPostcode(d.getString("billingPostcode"));
		order.setBillingCity(d.getString("billingCity"));
		order.setBillingState(d.getString("billingState"));
		order.setBillingCountry(d.getString("billingCountry"));
		order.setBillingPhone(d.getString("billingPhone"));
		order.setFirstIssue(d.getInteger("firstIssue"));
		order.setLastIssue(d.getInteger("lastIssue"));
		order.setNotes(d.getString("notes"));
		order.setShippingFirstName(d.getString("shippingFirstName"));
		order.setShippingLastName(d.getString("shippingLastName"));
		order.setShippingCompany(d.getString("shippingCompany"));
		order.setShippingEmail(d.getString("shippingEmail"));
		order.setShippingPhone(d.getString("shippingPhone"));
		order.setShippingAddress1(d.getString("shippingAddress1"));
		order.setShippingAddress2(d.getString("shippingAddress2"));
		order.setShippingPostcode(d.getString("shippingPostcode"));
		order.setShippingCity(d.getString("shippingCity"));
		order.setShippingState(d.getString("shippingState"));
		order.setShippingCountry(d.getString("shippingCountry"));
		*/
		return order;
	}
}