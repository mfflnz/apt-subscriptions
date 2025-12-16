package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.blefuscu.apt.subscriptions.model.Order;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

public class OrderMongoRepository implements OrderRepository {

	private static final String BILLING_ADDRESS_1 = "billing_address_1";
	private static final String BILLING_ADDRESS_2 = "billing_address_2";
	private static final String BILLING_CITY = "billing_city";
	private static final String BILLING_COMPANY = "billing_company";
	private static final String BILLING_COUNTRY = "billing_country";
	private static final String BILLING_EMAIL = "billing_email";
	private static final String BILLING_FIRST_NAME = "billing_first_name";
	private static final String BILLING_LAST_NAME = "billing_last_name";
	private static final String BILLING_PHONE = "billing_phone";
	private static final String BILLING_POSTCODE = "billing_postcode";
	private static final String BILLING_STATE = "billing_state";
	private static final String CART_DISCOUNT = "cart_discount";
	private static final String COUPON_ITEMS = "coupon_items";
	private static final String CUSTOMER_EMAIL = "customer_email";
	private static final String CUSTOMER_ID = "customer_id";
	private static final String CUSTOMER_IP_ADDRESS = "customer_ip_address";
	private static final String CUSTOMER_NOTE = "customer_note";
	private static final String CUSTOMER_USER = "customer_user";
	private static final String CUSTOMER_USER_AGENT = "customer_user_agent";
	private static final String DISCOUNT_TOTAL = "discount_total";
	private static final String DOWNLOAD_PERMISSIONS = "download_permissions";
	private static final String FEE_ITEMS = "fee_items";
	private static final String FEE_TAX_TOTAL = "fee_tax_total";
	private static final String FEE_TOTAL = "fee_total";
	private static final String FIRST_ISSUE = "first_issue";
	private static final String LAST_ISSUE = "last_issue";
	private static final String LINE_ITEM_1 = "line_item_1";
	private static final String LINE_ITEM_2 = "line_item_2";
	private static final String LINE_ITEM_3 = "line_item_3";
	private static final String LINE_ITEM_4 = "line_item_4";
	private static final String LINE_ITEM_5 = "line_item_5";
	private static final String META_PPCP_PAYPAL_FEES = "'meta:_ppcp_paypal_fees'";
	private static final String META_STRIPE_CURRENCY = "'meta:_stripe_currency'";
	private static final String META_STRIPE_FEE = "'meta:_stripe_fee'";
	private static final String META_STRIPE_NET = "'meta:_stripe_net'";
	private static final String META_WC_ORDER_ATTRIBUTION_DEVICE_TYPE = "'meta:_wc_order_attribution_device_type'";
	private static final String META_WC_ORDER_ATTRIBUTION_REFERRER = "'meta:_wc_order_attribution_referrer'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_COUNT = "'meta:_wc_order_attribution_session_count'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_ENTRY = "'meta:_wc_order_attribution_session_entry'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_PAGES = "'meta:_wc_order_attribution_session_pages'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_START_TIME = "'meta:_wc_order_attribution_session_start_time'";
	private static final String META_WC_ORDER_ATTRIBUTION_SOURCE_TYPE = "'meta:_wc_order_attribution_source_type'";
	private static final String META_WC_ORDER_ATTRIBUTION_USER_AGENT = "'meta:_wc_order_attribution_user_agent'";
	private static final String META_WC_ORDER_ATTRIBUTION_UTM_SOURCE = "'meta:_wc_order_attribution_utm_source'";
	private static final String ORDER_CURRENCY = "order_currency";
	private static final String ORDER_DATE = "order_date";
	private static final String ORDER_DISCOUNT = "order_discount";
	private static final String ORDER_ID = "order_id";
	private static final String ORDER_KEY = "order_key";
	private static final String ORDER_NET_TOTAL = "order_net_total";
	private static final String ORDER_NOTES = "order_notes";
	private static final String ORDER_NUMBER = "order_number";
	private static final String ORDER_SUBTOTAL = "order_subtotal";
	private static final String ORDER_TOTAL = "order_total";
	private static final String PAID_DATE = "paid_date";
	private static final String PAYMENT_METHOD = "payment_method";
	private static final String PAYMENT_METHOD_TITLE = "payment_method_title";
	private static final String REFUND_ITEMS = "refund_items";
	private static final String SHIPPING_ADDRESS_1 = "shipping_address_1";
	private static final String SHIPPING_ADDRESS_2 = "shipping_address_2";
	private static final String SHIPPING_CITY = "shipping_city";
	private static final String SHIPPING_COMPANY = "shipping_company";
	private static final String SHIPPING_COUNTRY = "shipping_country";
	private static final String SHIPPING_FIRST_NAME = "shipping_first_name";
	private static final String SHIPPING_ITEMS = "shipping_items";
	private static final String SHIPPING_LAST_NAME = "shipping_last_name";
	private static final String SHIPPING_METHOD = "shipping_method";
	private static final String SHIPPING_PHONE = "shipping_phone";
	private static final String SHIPPING_POSTCODE = "shipping_postcode";
	private static final String SHIPPING_STATE = "shipping_state";
	private static final String SHIPPING_TAX_TOTAL = "shipping_tax_total";
	private static final String SHIPPING_TOTAL = "shipping_total";
	private static final String STATUS = "status";
	private static final String TAX_ITEMS = "tax_items";
	private static final String TAX_TOTAL = "tax_total";
	private static final String TRANSACTION_ID = "transaction_id";
	private static final String WT_IMPORT_KEY = "wt_import_key";
	private static final String LONG = "Long";
	private static final String INTEGER = "Integer";
	private static final String STRING = "String";
	private static final String DOUBLE = "Double";
	private static final String DEFAULT_DATE = "1970-01-01";
	private static final String DATE_REGEX = "\\b(\\d{4}-\\d{2}-\\d{2})\\b";

	public static final String SUBSCRIPTIONS_DB_NAME = "subscriptions";
	public static final String ORDER_COLLECTION_NAME = "orders";
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private MongoCollection<Document> orderCollection;

	public MongoCollection<Document> getOrderCollection() {
		return orderCollection;
	}

	public void setOrderCollection(MongoCollection<Document> orderCollection) {
		this.orderCollection = orderCollection;
	}

	public OrderMongoRepository(MongoClient mongoClient, String databaseName, String collectionName) {
		setOrderCollection(mongoClient.getDatabase(databaseName).getCollection(collectionName));
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
		String orderDate = (updatedOrder.getOrderDate() == null) ? DEFAULT_DATE
				: updatedOrder.getOrderDate().format(DATE_TIME_FORMATTER);
		String paidDate = (updatedOrder.getPaidDate() == null) ? DEFAULT_DATE
				: updatedOrder.getPaidDate().format(DATE_TIME_FORMATTER);

		// https://www.mongodb.com/docs/drivers/java/sync/current/crud/update-documents/
		Bson filter = Filters.eq(ORDER_ID, id);
		Bson update = Updates.combine(Updates.set(ORDER_DATE, orderDate), Updates.set(PAID_DATE, paidDate),
				Updates.set(ORDER_TOTAL, updatedOrder.getOrderTotal()),
				Updates.set(ORDER_NET_TOTAL, updatedOrder.getOrderNetTotal()),
				Updates.set(PAYMENT_METHOD, updatedOrder.getPaymentMethod()),
				Updates.set(PAYMENT_METHOD_TITLE, updatedOrder.getPaymentMethodTitle()),
				Updates.set(SHIPPING_FIRST_NAME, updatedOrder.getShippingFirstName()),
				Updates.set(SHIPPING_LAST_NAME, updatedOrder.getShippingLastName()),
				Updates.set(SHIPPING_ADDRESS_1, updatedOrder.getShippingAddress1()),
				Updates.set(SHIPPING_POSTCODE, updatedOrder.getShippingPostcode()),
				Updates.set(SHIPPING_STATE, updatedOrder.getShippingState()),
				Updates.set(SHIPPING_CITY, updatedOrder.getShippingCity()),
				Updates.set(CUSTOMER_EMAIL, updatedOrder.getCustomerEmail()),
				Updates.set(BILLING_PHONE, updatedOrder.getBillingPhone()),
				Updates.set(SHIPPING_ITEMS, updatedOrder.getShippingItems()),
				Updates.set(FIRST_ISSUE, updatedOrder.getFirstIssue()),
				Updates.set(LAST_ISSUE, updatedOrder.getLastIssue()),
				Updates.set(CUSTOMER_NOTE, updatedOrder.getCustomerNote()));
		orderCollection.updateOne(filter, update);

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

		String downloadPermissions = convertDownloadPermissions(d);
		String wtImportKey = convertWtImportKey(d);
		String shippingPostcode = convertShippingPostcode(d);
		String billingPostcode = convertBillingPostcode(d);
		String billingPhone = convertBillingPhone(d);
		String customerUser = convertCustomerUser(d);
		int orderNumber = convertOrderNumber(d);
		int customerId = convertCustomerId(d);
		double shippingTotal = convertShippingTotal(d);
		double shippingTaxTotal = convertShippingTaxTotal(d);
		double feeTotal = convertFeeTotal(d);
		double feeTaxTotal = convertFeeTaxTotal(d);
		double taxTotal = convertTaxTotal(d);
		double cartDiscount = convertCartDiscount(d);
		double orderDiscount = convertOrderDiscount(d);
		double discountTotal = convertDiscountTotal(d);
		double orderTotal = convertOrderTotal(d);
		double orderSubtotal = convertOrderSubtotal(d);

		String metaStripeCurrency = (d.get(META_STRIPE_CURRENCY) == null) ? "EUR"
				: d.get(META_STRIPE_CURRENCY, String.class);

		double metaPpcpPaypalFees = (d.get(META_PPCP_PAYPAL_FEES) == null) ? 0.0
				: d.get(META_PPCP_PAYPAL_FEES, Double.class);
		double metaStripeFee = (d.get(META_STRIPE_FEE) == null) ? 0.0 : d.get(META_STRIPE_FEE, Double.class);
		double metaStripeNet = (d.get(META_STRIPE_NET) == null) ? 0.0 : d.get(META_STRIPE_NET, Double.class);

		int lastIssue = (d.getInteger(LAST_ISSUE) == null) ? 0 : d.getInteger(LAST_ISSUE);
		int firstIssue = (d.getInteger(FIRST_ISSUE) == null) ? 0 : d.getInteger(FIRST_ISSUE);

		double orderNetTotal = (d.getDouble(ORDER_NET_TOTAL) == null) ? 0.0 : d.getDouble(ORDER_NET_TOTAL);

		LocalDate orderDate = (d.getString(ORDER_DATE).isEmpty()) ? null
				: LocalDate.parse(cleanupDate(d.getString(ORDER_DATE)));
		LocalDate paidDate = (d.getString(PAID_DATE).isEmpty()) ? null
				: LocalDate.parse(cleanupDate(d.getString(PAID_DATE)));

		return new Order.OrderBuilder(d.getInteger(ORDER_ID), orderDate, d.getString(CUSTOMER_EMAIL))
				.setOrderNumber(orderNumber).setPaidDate(paidDate).setStatus(d.getString(STATUS))
				.setShippingTotal(shippingTotal).setShippingTaxTotal(shippingTaxTotal).setFeeTotal(feeTotal)
				.setFeeTaxTotal(feeTaxTotal).setTaxTotal(taxTotal).setCartDiscount(cartDiscount)
				.setOrderDiscount(orderDiscount).setDiscountTotal(discountTotal).setOrderTotal(orderTotal)
				.setOrderSubtotal(orderSubtotal).setOrderKey(d.getString(ORDER_KEY))
				.setOrderCurrency(d.getString(ORDER_CURRENCY)).setPaymentMethod(d.getString(PAYMENT_METHOD))
				.setPaymentMethodTitle(d.getString(PAYMENT_METHOD_TITLE)).setTransactionId(d.getString(TRANSACTION_ID))
				.setCustomerIpAddress(d.getString(CUSTOMER_IP_ADDRESS))
				.setCustomerUserAgent(d.getString(CUSTOMER_USER_AGENT)).setShippingMethod(d.getString(SHIPPING_METHOD))
				.setCustomerId(customerId).setCustomerUser(customerUser)
				.setBillingFirstName(d.getString(BILLING_FIRST_NAME)).setBillingLastName(d.getString(BILLING_LAST_NAME))
				.setBillingCompany(d.getString(BILLING_COMPANY)).setBillingEmail(d.getString(BILLING_EMAIL))
				.setBillingPhone(billingPhone).setBillingAddress1(d.getString(BILLING_ADDRESS_1))
				.setBillingAddress2(d.getString(BILLING_ADDRESS_2)).setBillingPostcode(billingPostcode)
				.setBillingCity(d.getString(BILLING_CITY)).setBillingState(d.getString(BILLING_STATE))
				.setBillingCountry(d.getString(BILLING_COUNTRY)).setShippingFirstName(d.getString(SHIPPING_FIRST_NAME))
				.setShippingLastName(d.getString(SHIPPING_LAST_NAME)).setShippingCompany(d.getString(SHIPPING_COMPANY))
				.setShippingPhone(d.getString(SHIPPING_PHONE)).setShippingAddress1(d.getString(SHIPPING_ADDRESS_1))
				.setShippingAddress2(d.getString(SHIPPING_ADDRESS_2)).setShippingPostcode(shippingPostcode)
				.setShippingCity(d.getString(SHIPPING_CITY)).setShippingState(d.getString(SHIPPING_STATE))
				.setShippingCountry(d.getString(SHIPPING_COUNTRY)).setCustomerNote(d.getString(CUSTOMER_NOTE))
				.setWtImportKey(wtImportKey).setTaxItems(d.getString(TAX_ITEMS))
				.setShippingItems(d.getString(SHIPPING_ITEMS)).setFeeItems(d.getString(FEE_ITEMS))
				.setCouponItems(d.getString(COUPON_ITEMS)).setRefundItems(d.getString(REFUND_ITEMS))
				.setOrderNotes(d.getString(ORDER_NOTES)).setDownloadPermissions(downloadPermissions)
				.setMetaWcOrderAttributionDeviceType(d.getString(META_WC_ORDER_ATTRIBUTION_DEVICE_TYPE))
				.setMetaWcOrderAttributionReferrer(d.getString(META_WC_ORDER_ATTRIBUTION_REFERRER))
				.setMetaWcOrderAttributionSessionCount(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_COUNT))
				.setMetaWcOrderAttributionSessionEntry(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_ENTRY))
				.setMetaWcOrderAttributionSessionPages(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_PAGES))
				.setMetaWcOrderAttributionSessionStartTime(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_START_TIME))
				.setMetaWcOrderAttributionSourceType(d.getString(META_WC_ORDER_ATTRIBUTION_SOURCE_TYPE))
				.setMetaWcOrderAttributionUserAgent(d.getString(META_WC_ORDER_ATTRIBUTION_USER_AGENT))
				.setMetaWcOrderAttributionUtmSource(d.getString(META_WC_ORDER_ATTRIBUTION_UTM_SOURCE))
				.setMetaPpcpPaypalFees(metaPpcpPaypalFees).setMetaStripeCurrency(metaStripeCurrency)
				.setMetaStripeFee(metaStripeFee).setMetaStripeNet(metaStripeNet).setLineItem1(d.getString(LINE_ITEM_1))
				.setLineItem2(d.getString(LINE_ITEM_2)).setLineItem3(d.getString(LINE_ITEM_3))
				.setLineItem4(d.getString(LINE_ITEM_4)).setLineItem5(d.getString(LINE_ITEM_5)).setLastIssue(lastIssue)
				.setFirstIssue(firstIssue).setOrderNetTotal(orderNetTotal).build();
	}

	private double convertOrderSubtotal(Document d) {
		double orderSubtotal;
		if (d.get(ORDER_SUBTOTAL) == null)
			orderSubtotal = 0.0;
		else {
			switch (d.get(ORDER_SUBTOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				orderSubtotal = d.getDouble(ORDER_SUBTOTAL);
				break;
			case INTEGER:
				orderSubtotal = d.getInteger(ORDER_SUBTOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Order Subtotal should be a number");
			}
		}
		return orderSubtotal;
	}

	private double convertOrderTotal(Document d) {
		double orderTotal;
		if (d.get(ORDER_TOTAL) == null)
			orderTotal = 0.0;
		else {
			switch (d.get(ORDER_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				orderTotal = d.getDouble(ORDER_TOTAL);
				break;
			case INTEGER:
				orderTotal = d.getInteger(ORDER_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Order Total should be a number");
			}
		}
		return orderTotal;
	}

	private double convertDiscountTotal(Document d) {
		double discountTotal;
		if (d.get(DISCOUNT_TOTAL) == null)
			discountTotal = 0.0;
		else {
			switch (d.get(DISCOUNT_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				discountTotal = d.getDouble(DISCOUNT_TOTAL);
				break;
			case INTEGER:
				discountTotal = d.getInteger(DISCOUNT_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Discount Total should be a number");
			}
		}
		return discountTotal;
	}

	private double convertOrderDiscount(Document d) {
		double orderDiscount;
		if (d.get(ORDER_DISCOUNT) == null)
			orderDiscount = 0.0;
		else {
			switch (d.get(ORDER_DISCOUNT).getClass().getSimpleName()) {
			case DOUBLE:
				orderDiscount = d.getDouble(ORDER_DISCOUNT);
				break;
			case INTEGER:
				orderDiscount = d.getInteger(ORDER_DISCOUNT);
				break;
			default:
				throw new IllegalArgumentException("Error: Order Discount should be a number");
			}
		}
		return orderDiscount;
	}

	private double convertCartDiscount(Document d) {
		double cartDiscount;
		if (d.get(CART_DISCOUNT) == null)
			cartDiscount = 0.0;
		else {
			switch (d.get(CART_DISCOUNT).getClass().getSimpleName()) {
			case DOUBLE:
				cartDiscount = d.getDouble(CART_DISCOUNT);
				break;
			case INTEGER:
				cartDiscount = d.getInteger(CART_DISCOUNT);
				break;
			default:
				throw new IllegalArgumentException("Error: Cart Discount should be a number");
			}
		}
		return cartDiscount;
	}

	private double convertTaxTotal(Document d) {
		double taxTotal;
		if (d.get(TAX_TOTAL) == null)
			taxTotal = 0.0;
		else {
			switch (d.get(TAX_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				taxTotal = d.getDouble(TAX_TOTAL);
				break;
			case INTEGER:
				taxTotal = d.getInteger(TAX_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Tax Total should be a number");
			}
		}
		return taxTotal;
	}

	private double convertFeeTaxTotal(Document d) {
		double feeTaxTotal;
		if (d.get(FEE_TAX_TOTAL) == null)
			feeTaxTotal = 0.0;
		else {
			switch (d.get(FEE_TAX_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				feeTaxTotal = d.getDouble(FEE_TAX_TOTAL);
				break;
			case INTEGER:
				feeTaxTotal = d.getInteger(FEE_TAX_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Fee Tax Total should be a number");
			}
		}
		return feeTaxTotal;
	}

	private double convertFeeTotal(Document d) {
		double feeTotal;
		if (d.get(FEE_TOTAL) == null)
			feeTotal = 0.0;
		else {
			switch (d.get(FEE_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				feeTotal = d.getDouble(FEE_TOTAL);
				break;
			case INTEGER:
				feeTotal = d.getInteger(FEE_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: Fee Total should be a number");
			}
		}
		return feeTotal;
	}

	private double convertShippingTaxTotal(Document d) {
		double shippingTaxTotal;
		if (d.get(SHIPPING_TAX_TOTAL) == null)
			shippingTaxTotal = 0.0;
		else {
			switch (d.get(SHIPPING_TAX_TOTAL).getClass().getSimpleName()) {
			case DOUBLE:
				shippingTaxTotal = d.getDouble(SHIPPING_TAX_TOTAL);
				break;
			case INTEGER:
				shippingTaxTotal = d.getInteger(SHIPPING_TAX_TOTAL);
				break;
			default:
				throw new IllegalArgumentException("Error: shipping tax total should be a number");
			}

		}
		return shippingTaxTotal;
	}

	private double convertShippingTotal(Document d) {
		double shippingTotal = 0.0;
		if (d.get(SHIPPING_TOTAL) == null)
			shippingTotal = 0.0;
		else {
			if (d.get(SHIPPING_TOTAL).getClass() == Double.class) {
				shippingTotal = d.getDouble(SHIPPING_TOTAL);
			}
			if (d.get(SHIPPING_TOTAL).getClass() == Integer.class) {
				shippingTotal = d.getInteger(SHIPPING_TOTAL);
			}
		}
		return shippingTotal;
	}

	private int convertCustomerId(Document d) {
		int customerId;
		if (d.get(CUSTOMER_ID) == null)
			customerId = 0;
		else {
			switch (d.get(CUSTOMER_ID).getClass().getSimpleName()) {
			case STRING:
				customerId = Integer.valueOf(d.getString(CUSTOMER_ID));
				break;
			case INTEGER:
				customerId = d.getInteger(CUSTOMER_ID);
				break;
			default:
				throw new IllegalArgumentException("Error: Customer Id should be a number or a string");
			}
		}
		return customerId;
	}

	private int convertOrderNumber(Document d) {
		int orderNumber;
		if (d.get(ORDER_NUMBER) == null)
			orderNumber = 0;
		else {
			switch (d.get(ORDER_NUMBER).getClass().getSimpleName()) {
			case STRING:
				orderNumber = Integer.valueOf(d.getString(ORDER_NUMBER));
				break;
			case INTEGER:
				orderNumber = d.getInteger(ORDER_NUMBER);
				break;
			default:
				throw new IllegalArgumentException("Error: Order Number should be a number or a string");
			}
		}
		return orderNumber;
	}

	private String convertCustomerUser(Document d) {
		String customerUser = "";
		if (d.get(CUSTOMER_USER) == null)
			customerUser = "";
		else {
			switch (d.get(CUSTOMER_USER).getClass().getSimpleName()) {
			case STRING:
				customerUser = d.getString(CUSTOMER_USER);
				break;
			case INTEGER:
				customerUser = String.valueOf(d.get(CUSTOMER_USER));
				break;
			default:
				throw new IllegalArgumentException("Error: Customer User should be a number or a string");
			}
		}
		return customerUser;
	}

	private String convertBillingPhone(Document d) {
		String billingPhone = "";
		if (d.get(BILLING_PHONE) == null)
			billingPhone = "";
		else {
			switch (d.get(BILLING_PHONE).getClass().getSimpleName()) {

			case STRING:
				billingPhone = d.getString(BILLING_PHONE);
				break;
			case INTEGER:
				billingPhone = String.valueOf(d.get(BILLING_PHONE));
				break;
			case LONG:
				billingPhone = String.valueOf(d.get(BILLING_PHONE));
				break;
			default:
				throw new IllegalArgumentException("Error: Billing Phone should be a number or a string");
			}
		}
		return billingPhone;
	}

	private String convertBillingPostcode(Document d) {
		String billingPostcode = "";
		if (d.get(BILLING_POSTCODE) == null)
			billingPostcode = "";
		else {
			switch (d.get(BILLING_POSTCODE).getClass().getSimpleName()) {
			case STRING:
				billingPostcode = d.getString(BILLING_POSTCODE);
				break;
			case INTEGER:
				billingPostcode = String.valueOf(d.get(BILLING_POSTCODE));
				break;
			default:
				throw new IllegalArgumentException("Error: Billing Postcode should be a number or a string");
			}
		}
		return billingPostcode;
	}

	private String convertShippingPostcode(Document d) {
		String shippingPostcode = "";
		if (d.get(SHIPPING_POSTCODE) == null)
			shippingPostcode = "";
		else {
			switch (d.get(SHIPPING_POSTCODE).getClass().getSimpleName()) {
			case STRING:
				shippingPostcode = d.getString(SHIPPING_POSTCODE);
				break;
			case INTEGER:
				shippingPostcode = String.valueOf(d.get(SHIPPING_POSTCODE));
				break;
			default:
				throw new IllegalArgumentException("Error: Shipping Postcode should be a number or a string");
			}
		}
		return shippingPostcode;
	}

	private String convertWtImportKey(Document d) {
		String wtImportKey = "";
		if (d.get(WT_IMPORT_KEY) == null)
			wtImportKey = "";
		else {
			switch (d.get(WT_IMPORT_KEY).getClass().getSimpleName()) {
			case STRING:
				wtImportKey = d.getString(WT_IMPORT_KEY);
				break;
			case INTEGER:
				wtImportKey = String.valueOf(d.get(WT_IMPORT_KEY));
				break;
			default:
				throw new IllegalArgumentException("Error: WT Import Key should be a number or a string");
			}
		}
		return wtImportKey;
	}

	private String convertDownloadPermissions(Document d) {
		String downloadPermissions = "";
		if (d.get(DOWNLOAD_PERMISSIONS) == null)
			downloadPermissions = "";
		else {
			switch (d.get(DOWNLOAD_PERMISSIONS).getClass().getSimpleName()) {
			case STRING:
				downloadPermissions = d.getString(DOWNLOAD_PERMISSIONS);
				break;
			case INTEGER:
				downloadPermissions = String.valueOf(d.get(DOWNLOAD_PERMISSIONS));
				break;
			default:
				throw new IllegalArgumentException("Error: download permissions should be expressed with a number");
			}
		}
		return downloadPermissions;
	}

	private Document fromOrderToDocument(Order order) {

		String paidDate = (order.getPaidDate() != null) ? order.getPaidDate().toString() : "";
		String metaStripeCurrency = (order.getMetaStripeCurrency() != null) ? order.getMetaStripeCurrency() : "EUR";

		return new Document().append(ORDER_ID, order.getOrderId()).append(ORDER_DATE, order.getOrderDate().toString())
				.append(CUSTOMER_EMAIL, order.getCustomerEmail()).append(ORDER_NUMBER, order.getOrderNumber())
				.append(STATUS, order.getStatus()).append(SHIPPING_TOTAL, order.getShippingTotal())
				.append(SHIPPING_TAX_TOTAL, order.getShippingTaxTotal()).append(FEE_TOTAL, order.getFeeTotal())
				.append(FEE_TAX_TOTAL, order.getFeeTaxTotal()).append(TAX_TOTAL, order.getTaxTotal())
				.append(CART_DISCOUNT, order.getCartDiscount()).append(ORDER_DISCOUNT, order.getOrderDiscount())
				.append(DISCOUNT_TOTAL, order.getDiscountTotal()).append(ORDER_TOTAL, order.getOrderTotal())
				.append(ORDER_SUBTOTAL, order.getOrderSubtotal()).append(ORDER_KEY, order.getOrderKey())
				.append(ORDER_CURRENCY, order.getOrderCurrency()).append(PAYMENT_METHOD, order.getPaymentMethod())
				.append(PAYMENT_METHOD_TITLE, order.getPaymentMethodTitle())
				.append(TRANSACTION_ID, order.getTransactionId())
				.append(CUSTOMER_IP_ADDRESS, order.getCustomerIpAddress())
				.append(CUSTOMER_USER_AGENT, order.getCustomerUserAgent())
				.append(SHIPPING_METHOD, order.getShippingMethod()).append(CUSTOMER_ID, order.getCustomerId())
				.append(CUSTOMER_USER, order.getCustomerUser()).append(PAID_DATE, paidDate)
				.append(BILLING_FIRST_NAME, order.getBillingFirstName())
				.append(BILLING_LAST_NAME, order.getBillingLastName())
				.append(BILLING_COMPANY, order.getBillingCompany()).append(BILLING_EMAIL, order.getBillingEmail())
				.append(BILLING_PHONE, order.getBillingPhone()).append(BILLING_ADDRESS_1, order.getBillingAddress1())
				.append(BILLING_ADDRESS_2, order.getBillingAddress2())
				.append(BILLING_POSTCODE, order.getBillingPostcode()).append(BILLING_CITY, order.getBillingCity())
				.append(BILLING_STATE, order.getBillingState()).append(BILLING_COUNTRY, order.getBillingCountry())
				.append(SHIPPING_FIRST_NAME, order.getBillingFirstName())
				.append(SHIPPING_LAST_NAME, order.getBillingLastName())
				.append(SHIPPING_COMPANY, order.getShippingCompany()).append(SHIPPING_PHONE, order.getShippingPhone())
				.append(SHIPPING_ADDRESS_1, order.getShippingAddress1())
				.append(SHIPPING_ADDRESS_2, order.getShippingAddress2())
				.append(SHIPPING_POSTCODE, order.getShippingPostcode()).append(SHIPPING_CITY, order.getShippingCity())
				.append(SHIPPING_STATE, order.getShippingState()).append(SHIPPING_COUNTRY, order.getShippingCountry())
				.append(CUSTOMER_NOTE, order.getCustomerNote()).append(WT_IMPORT_KEY, order.getWtImportKey())
				.append(TAX_ITEMS, order.getTaxItems()).append(SHIPPING_ITEMS, order.getShippingItems())
				.append(FEE_ITEMS, order.getFeeItems()).append(COUPON_ITEMS, order.getCouponItems())
				.append(REFUND_ITEMS, order.getRefundItems()).append(ORDER_NOTES, order.getOrderNotes())
				.append(DOWNLOAD_PERMISSIONS, order.getDownloadPermissions())
				.append(META_WC_ORDER_ATTRIBUTION_DEVICE_TYPE, order.getMetaWcOrderAttributionDeviceType())
				.append(META_WC_ORDER_ATTRIBUTION_REFERRER, order.getMetaWcOrderAttributionReferrer())
				.append(META_WC_ORDER_ATTRIBUTION_SESSION_COUNT, order.getMetaWcOrderAttributionSessionCount())
				.append(META_WC_ORDER_ATTRIBUTION_SESSION_ENTRY, order.getMetaWcOrderAttributionSessionEntry())
				.append(META_WC_ORDER_ATTRIBUTION_SESSION_PAGES, order.getMetaWcOrderAttributionSessionPages())
				.append(META_WC_ORDER_ATTRIBUTION_SESSION_START_TIME, order.getMetaWcOrderAttributionSessionStartTime())
				.append(META_WC_ORDER_ATTRIBUTION_SOURCE_TYPE, order.getMetaWcOrderAttributionSourceType())
				.append(META_WC_ORDER_ATTRIBUTION_USER_AGENT, order.getMetaWcOrderAttributionUserAgent())
				.append(META_WC_ORDER_ATTRIBUTION_UTM_SOURCE, order.getMetaWcOrderAttributionUtmSource())
				.append(META_PPCP_PAYPAL_FEES, order.getMetaPpcpPaypalFees())
				.append(META_STRIPE_CURRENCY, metaStripeCurrency).append(META_STRIPE_FEE, order.getMetaStripeFee())
				.append(META_STRIPE_NET, order.getMetaStripeNet()).append(LINE_ITEM_1, order.getLineItem1())
				.append(LINE_ITEM_2, order.getLineItem2()).append(LINE_ITEM_3, order.getLineItem3())
				.append(LINE_ITEM_4, order.getLineItem4()).append(LINE_ITEM_5, order.getLineItem5())
				.append(ORDER_NET_TOTAL, order.getOrderNetTotal()).append(FIRST_ISSUE, order.getFirstIssue())
				.append(LAST_ISSUE, order.getLastIssue());
	}

	@Override
	public long countOrders(Bson filter) {
		return orderCollection.countDocuments(filter);
	}

	private String cleanupDate(String dateToClean) {
		Pattern pattern = Pattern.compile(DATE_REGEX);
		Matcher matcher = pattern.matcher(dateToClean);
		matcher.find();
		return matcher.group(1);
	}
}