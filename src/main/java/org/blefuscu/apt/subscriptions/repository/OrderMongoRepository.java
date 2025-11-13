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

	private static final String ORDER_ID = "order_id";
	private static final String ORDER_DATE = "order_date";
	private static final String PAID_DATE = "paid_date";
	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "orders";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
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

		Bson filter = Filters.and(Filters.gte(ORDER_DATE, fromDate.toString()),
				Filters.lte(ORDER_DATE, toDate.toString()));

		return StreamSupport.stream(orderCollection.find(filter).spliterator(), false).map(this::fromDocumentToOrder)
				.collect(Collectors.toList());

	}

	@Override
	public Order findById(int id) {
		return fromDocumentToOrder(orderCollection.find(Filters.eq(ORDER_ID, id)).first());
	}

	@Override
	public void save(Order order) {
		if (findById(order.getOrderId()) != null) {
			throw new IllegalArgumentException("Error: Order with id " + order.getOrderId() + " already in database");
		}
		orderCollection.insertOne(fromOrderToDocument(order));
	}

	@Override
	public void update(int id, Order updatedOrder) throws IllegalArgumentException {

		if (findById(id) == null) {
			throw new IllegalArgumentException("Error: Order not found");
		}

		orderCollection.deleteOne(Filters.eq(ORDER_ID, id));
		orderCollection.insertOne(fromOrderToDocument(updatedOrder));

	}

	@Override
	public void delete(int id) throws IllegalArgumentException {
		if (findById(id) == null) {
			throw new IllegalArgumentException("Error: Order not found");
		}
		orderCollection.deleteOne(Filters.eq(ORDER_ID, id));
	}

	private Order fromDocumentToOrder(Document d) {

		if (d == null)
			return null;

		return new Order.OrderBuilder(d.getInteger(ORDER_ID),
				LocalDate.parse(d.getString(ORDER_DATE), DATE_TIME_FORMATTER), d.getString("customer_email"))
				.setOrderNumber(d.getInteger("order_number"))
				.setPaidDate(LocalDate.parse(d.getString(PAID_DATE), DATE_TIME_FORMATTER))
				.setStatus(d.getString("status")).setShippingTotal(d.getDouble("shipping_total"))
				.setShippingTaxTotal(d.getDouble("shipping_tax_total")).setFeeTotal(d.getDouble("fee_total"))
				.setFeeTaxTotal(d.getDouble("fee_tax_total")).setTaxTotal(d.getDouble("tax_total"))
				.setCartDiscount(d.getDouble("cart_discount")).setOrderDiscount(d.getDouble("order_discount"))
				.setDiscountTotal(d.getDouble("discount_total")).setOrderTotal(d.getDouble("order_total"))
				.setOrderSubtotal(d.getDouble("order_subtotal")).setOrderKey(d.getString("order_key"))
				.setOrderCurrency(d.getString("order_currency")).setPaymentMethod(d.getString("payment_method"))
				.setPaymentMethodTitle(d.getString("payment_method_title"))
				.setTransactionId(d.getString("transaction_id"))
				.setCustomerIpAddress(d.getString("customer_ip_address"))
				.setCustomerUserAgent(d.getString("customer_user_agent"))
				.setShippingMethod(d.getString("shipping_method")).setCustomerId(d.getInteger("customer_id"))
				.setCustomerUser(d.getString("customer_user")).setBillingFirstName(d.getString("billing_first_name"))
				.setBillingLastName(d.getString("billing_last_name")).setBillingCompany(d.getString("billing_company"))
				.setBillingEmail(d.getString("billing_email")).setBillingPhone(d.getString("billing_phone"))
				.setBillingAddress1(d.getString("billing_address_1"))
				.setBillingAddress2(d.getString("billing_address_2"))
				.setBillingPostcode(d.getString("billing_postcode")).setBillingCity(d.getString("billing_city"))
				.setBillingState(d.getString("billing_state")).setBillingCountry(d.getString("billing_country"))
				.setShippingFirstName(d.getString("shipping_first_name"))
				.setShippingLastName(d.getString("shipping_last_name"))
				.setShippingCompany(d.getString("shipping_company")).setShippingPhone(d.getString("shipping_phone"))
				.setShippingAddress1(d.getString("shipping_address_1"))
				.setShippingAddress2(d.getString("shipping_address_2"))
				.setShippingPostcode(d.getString("shipping_postcode")).setShippingCity(d.getString("shipping_city"))
				.setShippingState(d.getString("shipping_state")).setShippingCountry(d.getString("shipping_country"))
				.setCustomerNote(d.getString("customer_note")).setWtImportKey(d.getString("wt_import_key"))
				.setTaxItems(d.getString("tax_items")).setShippingItems(d.getString("shipping_items"))
				.setFeeItems(d.getString("fee_items")).setCouponItems(d.getString("coupon_items"))
				.setRefundItems(d.getString("refund_items")).setOrderNotes(d.getString("order_notes"))
				.setDownloadPermissions(d.getString("download_permissions"))
				.setMetaWcOrderAttributionDeviceType(d.getString("'meta:_wc_order_attribution_device_type'"))
				.setMetaWcOrderAttributionReferrer(d.getString("'meta:_wc_order_attribution_referrer'"))
				.setMetaWcOrderAttributionSessionCount(d.getString("'meta:_wc_order_attribution_session_count'"))
				.setMetaWcOrderAttributionSessionEntry(d.getString("'meta:_wc_order_attribution_session_entry'"))
				.setMetaWcOrderAttributionSessionPages(d.getString("'meta:_wc_order_attribution_session_pages'"))
				.setMetaWcOrderAttributionSessionStartTime(
						d.getString("'meta:_wc_order_attribution_session_start_time'"))
				.setMetaWcOrderAttributionSourceType(d.getString("'meta:_wc_order_attribution_source_type'"))
				.setMetaWcOrderAttributionUserAgent(d.getString("'meta:_wc_order_attribution_user_agent'"))
				.setMetaWcOrderAttributionUtmSource(d.getString("'meta:_wc_order_attribution_utm_source'"))
				.setMetaPpcpPaypalFees(d.getDouble("'meta:_ppcp_paypal_fees'"))
				.setMetaStripeCurrency(d.getString("'meta:_stripe_currency'"))
				.setMetaStripeFee(d.getDouble("'meta:_stripe_fee'")).setMetaStripeNet(d.getDouble("'meta:_stripe_net'"))
				.setLineItem1(d.getString("line_item_1")).setLineItem2(d.getString("line_item_2"))
				.setLineItem3(d.getString("line_item_3")).setLineItem4(d.getString("line_item_4"))
				.setLineItem5(d.getString("line_item_5")).setLastIssue(d.getInteger("last_issue"))
				.setFirstIssue(d.getInteger("first_issue")).setOrderNetTotal(d.getDouble("order_net_total"))
				.setOrderConfirmed(d.getBoolean("order_confirmed")).build();
	}

	private Document fromOrderToDocument(Order order) {

		String paidDate = "";
		if (order.getPaidDate() != null) {
			paidDate = order.getPaidDate().toString();
		}

		return new Document().append(ORDER_ID, order.getOrderId()).append(ORDER_DATE, order.getOrderDate().toString())
				.append("customer_email", order.getCustomerEmail()).append("order_number", order.getOrderNumber())
				.append("status", order.getStatus()).append("shipping_total", order.getShippingTotal())
				.append("shipping_tax_total", order.getShippingTaxTotal()).append("fee_total", order.getFeeTotal())
				.append("fee_tax_total", order.getFeeTaxTotal()).append("tax_total", order.getTaxTotal())
				.append("cart_discount", order.getCartDiscount()).append("order_discount", order.getOrderDiscount())
				.append("discount_total", order.getDiscountTotal()).append("order_total", order.getOrderTotal())
				.append("order_subtotal", order.getOrderSubtotal()).append("order_key", order.getOrderKey())
				.append("order_currency", order.getOrderCurrency()).append("payment_method", order.getPaymentMethod())
				.append("payment_method_title", order.getPaymentMethodTitle())
				.append("transaction_id", order.getTransactionId())
				.append("customer_ip_address", order.getCustomerIpAddress())
				.append("customer_user_agent", order.getCustomerUserAgent())
				.append("shipping_method", order.getShippingMethod()).append("customer_id", order.getCustomerId())
				.append("customer_user", order.getCustomerUser()).append(PAID_DATE, paidDate)
				.append("billing_first_name", order.getBillingFirstName())
				.append("billing_last_name", order.getBillingLastName())
				.append("billing_company", order.getBillingCompany()).append("billing_email", order.getBillingEmail())
				.append("billing_phone", order.getBillingPhone())
				.append("billing_address_1", order.getBillingAddress1())
				.append("billing_address_2", order.getBillingAddress2())
				.append("billing_postcode", order.getBillingPostcode()).append("billing_city", order.getBillingCity())
				.append("billing_state", order.getBillingState()).append("billing_country", order.getBillingCountry())
				.append("shipping_first_name", order.getBillingFirstName())
				.append("shipping_last_name", order.getBillingLastName())
				.append("shipping_company", order.getShippingCompany())
				.append("shipping_phone", order.getShippingPhone())
				.append("shipping_address_1", order.getShippingAddress1())
				.append("shipping_address_2", order.getShippingAddress2())
				.append("shipping_postcode", order.getShippingPostcode())
				.append("shipping_city", order.getShippingCity()).append("shipping_state", order.getShippingState())
				.append("shipping_country", order.getShippingCountry()).append("customer_note", order.getCustomerNote())
				.append("wt_import_key", order.getWtImportKey()).append("tax_items", order.getTaxItems())
				.append("shipping_items", order.getShippingItems()).append("fee_items", order.getFeeItems())
				.append("coupon_items", order.getCouponItems()).append("refund_items", order.getRefundItems())
				.append("order_notes", order.getOrderNotes())
				.append("download_permissions", order.getDownloadPermissions())
				.append("'meta:_wc_order_attribution_device_type'", order.getMetaWcOrderAttributionDeviceType())
				.append("'meta:_wc_order_attribution_referrer'", order.getMetaWcOrderAttributionReferrer())
				.append("'meta:_wc_order_attribution_session_count'", order.getMetaWcOrderAttributionSessionCount())
				.append("'meta:_wc_order_attribution_session_entry'", order.getMetaWcOrderAttributionSessionEntry())
				.append("'meta:_wc_order_attribution_session_pages'", order.getMetaWcOrderAttributionSessionPages())
				.append("'meta:_wc_order_attribution_session_start_time'",
						order.getMetaWcOrderAttributionSessionStartTime())
				.append("'meta:_wc_order_attribution_source_type'", order.getMetaWcOrderAttributionSourceType())
				.append("'meta:_wc_order_attribution_user_agent'", order.getMetaWcOrderAttributionUserAgent())
				.append("'meta:_wc_order_attribution_utm_source'", order.getMetaWcOrderAttributionUtmSource())
				.append("'meta:_ppcp_paypal_fees'", order.getMetaPpcpPaypalFees())
				.append("'meta:_stripe_currency'", order.getMetaStripeCurrency())
				.append("'meta:_stripe_fee'", order.getMetaStripeFee())
				.append("'meta:_stripe_net'", order.getMetaStripeNet()).append("line_item_1", order.getLineItem1())
				.append("line_item_2", order.getLineItem2()).append("line_item_3", order.getLineItem3())
				.append("line_item_4", order.getLineItem4()).append("line_item_5", order.getLineItem5())
				.append("order_confirmed", true).append("order_net_total", order.getOrderNetTotal())
				.append("first_issue", order.getFirstIssue()).append("last_issue", order.getLastIssue());
	}

}