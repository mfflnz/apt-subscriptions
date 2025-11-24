package org.blefuscu.apt.subscriptions.repository;

import java.time.LocalDate;
import java.time.ZoneId;
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

	private static final String DATE_REGEX = "\\b(\\d{4}-\\d{2}-\\d{2})\\b";
	private static final String TRIMMED_DATE_REGEX = "(?<!\\d)(\\d{4}-\\d{2}-\\d{2})(?!\\d)";
	private static final String LINE_ITEM_5 = "line_item_5";
	private static final String LINE_ITEM_4 = "line_item_4";
	private static final String LINE_ITEM_3 = "line_item_3";
	private static final String LINE_ITEM_2 = "line_item_2";
	private static final String LINE_ITEM_1 = "line_item_1";
	private static final String META_WC_ORDER_ATTRIBUTION_UTM_SOURCE = "'meta:_wc_order_attribution_utm_source'";
	private static final String META_WC_ORDER_ATTRIBUTION_USER_AGENT = "'meta:_wc_order_attribution_user_agent'";
	private static final String META_WC_ORDER_ATTRIBUTION_SOURCE_TYPE = "'meta:_wc_order_attribution_source_type'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_START_TIME = "'meta:_wc_order_attribution_session_start_time'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_PAGES = "'meta:_wc_order_attribution_session_pages'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_ENTRY = "'meta:_wc_order_attribution_session_entry'";
	private static final String META_WC_ORDER_ATTRIBUTION_SESSION_COUNT = "'meta:_wc_order_attribution_session_count'";
	private static final String META_WC_ORDER_ATTRIBUTION_REFERRER = "'meta:_wc_order_attribution_referrer'";
	private static final String META_WC_ORDER_ATTRIBUTION_DEVICE_TYPE = "'meta:_wc_order_attribution_device_type'";
	private static final String DOWNLOAD_PERMISSIONS = "download_permissions";
	private static final String ORDER_NOTES = "order_notes";
	private static final String REFUND_ITEMS = "refund_items";
	private static final String COUPON_ITEMS = "coupon_items";
	private static final String FEE_ITEMS = "fee_items";
	private static final String TAX_ITEMS = "tax_items";
	private static final String WT_IMPORT_KEY = "wt_import_key";
	private static final String SHIPPING_COUNTRY = "shipping_country";
	private static final String SHIPPING_ADDRESS_2 = "shipping_address_2";
	private static final String SHIPPING_PHONE = "shipping_phone";
	private static final String SHIPPING_COMPANY = "shipping_company";
	private static final String BILLING_COUNTRY = "billing_country";
	private static final String BILLING_STATE = "billing_state";
	private static final String BILLING_CITY = "billing_city";
	private static final String BILLING_POSTCODE = "billing_postcode";
	private static final String BILLING_ADDRESS_2 = "billing_address_2";
	private static final String BILLING_ADDRESS_1 = "billing_address_1";
	private static final String BILLING_EMAIL = "billing_email";
	private static final String BILLING_COMPANY = "billing_company";
	private static final String BILLING_LAST_NAME = "billing_last_name";
	private static final String BILLING_FIRST_NAME = "billing_first_name";
	private static final String CUSTOMER_USER = "customer_user";
	private static final String CUSTOMER_ID = "customer_id";
	private static final String SHIPPING_METHOD = "shipping_method";
	private static final String CUSTOMER_USER_AGENT = "customer_user_agent";
	private static final String CUSTOMER_IP_ADDRESS = "customer_ip_address";
	private static final String TRANSACTION_ID = "transaction_id";
	private static final String PAYMENT_METHOD_TITLE = "payment_method_title";
	private static final String ORDER_CURRENCY = "order_currency";
	private static final String ORDER_KEY = "order_key";
	private static final String ORDER_SUBTOTAL = "order_subtotal";
	private static final String DISCOUNT_TOTAL = "discount_total";
	private static final String ORDER_DISCOUNT = "order_discount";
	private static final String CART_DISCOUNT = "cart_discount";
	private static final String TAX_TOTAL = "tax_total";
	private static final String FEE_TAX_TOTAL = "fee_tax_total";
	private static final String FEE_TOTAL = "fee_total";
	private static final String SHIPPING_TAX_TOTAL = "shipping_tax_total";
	private static final String SHIPPING_TOTAL = "shipping_total";
	private static final String STATUS = "status";
	private static final String ORDER_NUMBER = "order_number";
	private static final String ORDER_CONFIRMED = "order_confirmed";
	private static final String ORDER_NET_TOTAL = "order_net_total";
	private static final String META_STRIPE_NET = "'meta:_stripe_net'";
	private static final String META_STRIPE_FEE = "'meta:_stripe_fee'";
	private static final String META_STRIPE_CURRENCY = "'meta:_stripe_currency'";
	private static final String META_PPCP_PAYPAL_FEES = "'meta:_ppcp_paypal_fees'";
	private static final String CUSTOMER_NOTE = "customer_note";
	private static final String LAST_ISSUE = "last_issue";
	private static final String FIRST_ISSUE = "first_issue";
	private static final String SHIPPING_ITEMS = "shipping_items";
	private static final String BILLING_PHONE = "billing_phone";
	private static final String CUSTOMER_EMAIL = "customer_email";
	private static final String SHIPPING_CITY = "shipping_city";
	private static final String SHIPPING_STATE = "shipping_state";
	private static final String SHIPPING_POSTCODE = "shipping_postcode";
	private static final String SHIPPING_ADDRESS_1 = "shipping_address_1";
	private static final String SHIPPING_LAST_NAME = "shipping_last_name";
	private static final String SHIPPING_FIRST_NAME = "shipping_first_name";
	private static final String PAYMENT_METHOD = "payment_method";
	private static final String NET_TOTAL = "net_total";
	private static final String ORDER_TOTAL = "order_total";
	private static final String ORDER_ID = "order_id";
	private static final String ORDER_DATE = "order_date";
	private static final String PAID_DATE = "paid_date";
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
		String orderDate = (updatedOrder.getOrderDate() == null) ? ""
				: updatedOrder.getOrderDate().format(DATE_TIME_FORMATTER);
		String paidDate = (updatedOrder.getPaidDate() == null) ? ""
				: updatedOrder.getPaidDate().format(DATE_TIME_FORMATTER);

		// https://www.mongodb.com/docs/drivers/java/sync/current/crud/update-documents/
		Bson filter = Filters.eq(ORDER_ID, id);
		Bson update = Updates.combine(
				Updates.set(ORDER_DATE, orderDate),
				Updates.set(PAID_DATE, paidDate),
				Updates.set(ORDER_TOTAL, updatedOrder.getOrderTotal()),
				Updates.set(NET_TOTAL, updatedOrder.getOrderNetTotal()),
				Updates.set(PAYMENT_METHOD, updatedOrder.getPaymentMethod()),
				Updates.set(SHIPPING_FIRST_NAME, updatedOrder.getShippingFirstName()),
				Updates.set(SHIPPING_LAST_NAME, updatedOrder.getShippingFirstName()),
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

		// TODO UPDATE mette tutti i valori null : devo fare un metodo "parse order view" che modifica solo i campi rilevanti leggendoli dalla view (quindi from order to document fors enon serve)
		// TODO coprire tutti i rami e ammazzare tutti i mutanti
	
		String downloadPermissions = "";
		if (d.get(DOWNLOAD_PERMISSIONS) == null)
			downloadPermissions = "";
		else {
			if (d.get(DOWNLOAD_PERMISSIONS).getClass() == String.class) {
				downloadPermissions = d.getString(DOWNLOAD_PERMISSIONS);
			}
			if (d.get(DOWNLOAD_PERMISSIONS).getClass() == Integer.class) {
				downloadPermissions = String.valueOf(d.get(DOWNLOAD_PERMISSIONS));
			}
		}

		String wtImportKey = "";
		if (d.get(WT_IMPORT_KEY) == null)
			wtImportKey = "";
		else {
			if (d.get(WT_IMPORT_KEY).getClass() == String.class) {
				wtImportKey = d.getString(WT_IMPORT_KEY);
			}
			if (d.get(WT_IMPORT_KEY).getClass() == Integer.class) {
				wtImportKey = String.valueOf(d.get(WT_IMPORT_KEY));
			}
		}

		String shippingPostcode = "";
		if (d.get(SHIPPING_POSTCODE) == null)
			shippingPostcode = "";
		else {
			if (d.get(SHIPPING_POSTCODE).getClass() == String.class) {
				shippingPostcode = d.getString(SHIPPING_POSTCODE);
			}
			if (d.get(SHIPPING_POSTCODE).getClass() == Integer.class) {
				shippingPostcode = String.valueOf(d.get(SHIPPING_POSTCODE));
			}
		}

		String billingPostcode = "";
		if (d.get(BILLING_POSTCODE) == null)
			billingPostcode = "";
		else {
			if (d.get(BILLING_POSTCODE).getClass() == String.class) {
				billingPostcode = d.getString(BILLING_POSTCODE);
			}
			if (d.get(BILLING_POSTCODE).getClass() == Integer.class) {
				billingPostcode = String.valueOf(d.get(BILLING_POSTCODE));
			}
		}

		String billingPhone = "";
		if (d.get(BILLING_PHONE) == null)
			billingPhone = "";
		else {
			if (d.get(BILLING_PHONE).getClass() == String.class) {
				billingPhone = d.getString(BILLING_PHONE);
			}
			if (d.get(BILLING_PHONE).getClass() == Integer.class) {
				billingPhone = String.valueOf(d.get(BILLING_PHONE));
			}
		}

		String customerUser = "";
		if (d.get(CUSTOMER_USER) == null)
			customerUser = "";
		else {
			if (d.get(CUSTOMER_USER).getClass() == String.class) {
				customerUser = d.getString(CUSTOMER_USER);
			}
			if (d.get(CUSTOMER_USER).getClass() == Integer.class) {
				customerUser = String.valueOf(d.get(CUSTOMER_USER));
			}
		}

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

		double shippingTaxTotal = 0.0;
		if (d.get(SHIPPING_TAX_TOTAL) == null)
			shippingTaxTotal = 0.0;
		else {
			if (d.get(SHIPPING_TAX_TOTAL).getClass() == Double.class) {
				shippingTaxTotal = d.getDouble(SHIPPING_TAX_TOTAL);
			}
			if (d.get(SHIPPING_TAX_TOTAL).getClass() == Integer.class) {
				shippingTaxTotal = d.getInteger(SHIPPING_TAX_TOTAL);
			}
		}

		double feeTotal = 0.0;
		if (d.get(FEE_TOTAL) == null)
			feeTotal = 0.0;
		else {
			if (d.get(FEE_TOTAL).getClass() == Double.class) {
				feeTotal = d.getDouble(FEE_TOTAL);
			}
			if (d.get(FEE_TOTAL).getClass() == Integer.class) {
				feeTotal = d.getInteger(FEE_TOTAL);
			}
		}

		double feeTaxTotal = 0.0;
		if (d.get(FEE_TAX_TOTAL) == null)
			feeTaxTotal = 0.0;
		else {
			if (d.get(FEE_TAX_TOTAL).getClass() == Double.class) {
				feeTaxTotal = d.getDouble(FEE_TAX_TOTAL);
			}
			if (d.get(FEE_TAX_TOTAL).getClass() == Integer.class) {
				feeTaxTotal = d.getInteger(FEE_TAX_TOTAL);
			}
		}

		double taxTotal = 0.0;
		if (d.get(TAX_TOTAL) == null)
			taxTotal = 0.0;
		else {
			if (d.get(TAX_TOTAL).getClass() == Double.class) {
				taxTotal = d.getDouble(TAX_TOTAL);
			}
			if (d.get(TAX_TOTAL).getClass() == Integer.class) {
				taxTotal = d.getInteger(TAX_TOTAL);
			}
		}

		double cartDiscount = 0.0;
		if (d.get(CART_DISCOUNT) == null)
			cartDiscount = 0.0;
		else {
			if (d.get(CART_DISCOUNT).getClass() == Double.class) {
				cartDiscount = d.getDouble(CART_DISCOUNT);
			}
			if (d.get(CART_DISCOUNT).getClass() == Integer.class) {
				cartDiscount = d.getInteger(CART_DISCOUNT);
			}
		}

		double orderDiscount = 0.0;
		if (d.get(ORDER_DISCOUNT) == null)
			orderDiscount = 0.0;
		else {
			if (d.get(ORDER_DISCOUNT).getClass() == Double.class) {
				orderDiscount = d.getDouble(ORDER_DISCOUNT);
			}
			if (d.get(ORDER_DISCOUNT).getClass() == Integer.class) {
				orderDiscount = d.getInteger(ORDER_DISCOUNT);
			}
		}

		double discountTotal = 0.0;
		if (d.get(DISCOUNT_TOTAL) == null)
			discountTotal = 0.0;
		else {
			if (d.get(DISCOUNT_TOTAL).getClass() == Double.class) {
				discountTotal = d.getDouble(DISCOUNT_TOTAL);
			}
			if (d.get(DISCOUNT_TOTAL).getClass() == Integer.class) {
				discountTotal = d.getInteger(DISCOUNT_TOTAL);
			}
		}

		double orderTotal = 0.0;
		if (d.get(ORDER_TOTAL) == null)
			orderTotal = 0.0;
		else {
			if (d.get(ORDER_TOTAL).getClass() == Double.class) {
				orderTotal = d.getDouble(ORDER_TOTAL);
			}
			if (d.get(ORDER_TOTAL).getClass() == Integer.class) {
				orderTotal = d.getInteger(ORDER_TOTAL);
			}
		}

		double orderSubtotal = 0.0;
		if (d.get(ORDER_SUBTOTAL) == null)
			orderSubtotal = 0.0;
		else {
			if (d.get(ORDER_SUBTOTAL).getClass() == Double.class) {
				orderSubtotal = d.getDouble(ORDER_SUBTOTAL);
			}
			if (d.get(ORDER_SUBTOTAL).getClass() == Integer.class) {
				orderSubtotal = d.getInteger(ORDER_SUBTOTAL);
			}
		}

		double metaPpcpPaypalFees = (d.get(META_PPCP_PAYPAL_FEES) == null) ? 0.0 : d.get(META_PPCP_PAYPAL_FEES, Double.class);
		String metaStripeCurrency = (d.get(META_STRIPE_CURRENCY) == null) ? "EUR" : d.get(META_STRIPE_CURRENCY, String.class); 
		double metaStripeFee = (d.get(META_STRIPE_FEE) == null) ? 0.0 : d.get(META_STRIPE_FEE, Double.class);
		double metaStripeNet = (d.get(META_STRIPE_NET) == null) ? 0.0 : d.get(META_STRIPE_NET, Double.class);
		
		int lastIssue = (d.getInteger(LAST_ISSUE) == null) ? 0 : d.getInteger(LAST_ISSUE); 
		int firstIssue = (d.getInteger(FIRST_ISSUE) == null) ? 0 : d.getInteger(FIRST_ISSUE);
		
		double orderNetTotal = (d.getDouble(ORDER_NET_TOTAL) == null) ? 0.0 : d.getDouble(ORDER_NET_TOTAL); 
		boolean orderConfirmed = (d.getBoolean(ORDER_CONFIRMED) == null) ? false : d.getBoolean(ORDER_CONFIRMED);
		
		//TODO: gestire i campi vuoti ('')
		LocalDate orderDate;
		LocalDate paidDate;
		


		
		if (!d.get(ORDER_DATE).getClass().equals(String.class)) {
			orderDate = d.getDate(ORDER_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} else {
			if (d.getString(ORDER_DATE).isEmpty()) {
				orderDate = null;
			} else {
				orderDate = LocalDate.parse(cleanupDate(d.getString(ORDER_DATE)));
			}
		}

		if (!d.get(PAID_DATE).getClass().equals(String.class)) {
			paidDate = d.getDate(PAID_DATE).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		} else {
			if (d.getString(PAID_DATE).isEmpty()) {
				paidDate = null;
			} else {
				paidDate = LocalDate.parse(cleanupDate(d.getString(PAID_DATE)));
			}
		}

		return new Order.OrderBuilder(d.getInteger(ORDER_ID),
				orderDate, d.getString(CUSTOMER_EMAIL))
				.setOrderNumber(d.getInteger(ORDER_NUMBER))
				.setPaidDate(paidDate)
				.setStatus(d.getString(STATUS))
				.setShippingTotal(shippingTotal)
				.setShippingTaxTotal(shippingTaxTotal)
				.setFeeTotal(feeTotal)
				.setFeeTaxTotal(feeTaxTotal)
				.setTaxTotal(taxTotal)
				.setCartDiscount(cartDiscount)
				.setOrderDiscount(orderDiscount)
				.setDiscountTotal(discountTotal)
				.setOrderTotal(orderTotal)
				.setOrderSubtotal(orderSubtotal)
				.setOrderKey(d.getString(ORDER_KEY))
				.setOrderCurrency(d.getString(ORDER_CURRENCY))
				.setPaymentMethod(d.getString(PAYMENT_METHOD))
				.setPaymentMethodTitle(d.getString(PAYMENT_METHOD_TITLE))
				.setTransactionId(d.getString(TRANSACTION_ID))
				.setCustomerIpAddress(d.getString(CUSTOMER_IP_ADDRESS))
				.setCustomerUserAgent(d.getString(CUSTOMER_USER_AGENT))
				.setShippingMethod(d.getString(SHIPPING_METHOD))
				.setCustomerId(d.getInteger(CUSTOMER_ID))
				.setCustomerUser(customerUser)
				.setBillingFirstName(d.getString(BILLING_FIRST_NAME))
				.setBillingLastName(d.getString(BILLING_LAST_NAME))
				.setBillingCompany(d.getString(BILLING_COMPANY))
				.setBillingEmail(d.getString(BILLING_EMAIL))
				.setBillingPhone(billingPhone)
				.setBillingAddress1(d.getString(BILLING_ADDRESS_1))
				.setBillingAddress2(d.getString(BILLING_ADDRESS_2))
				.setBillingPostcode(billingPostcode)
				.setBillingCity(d.getString(BILLING_CITY))
				.setBillingState(d.getString(BILLING_STATE))
				.setBillingCountry(d.getString(BILLING_COUNTRY))
				.setShippingFirstName(d.getString(SHIPPING_FIRST_NAME))
				.setShippingLastName(d.getString(SHIPPING_LAST_NAME))
				.setShippingCompany(d.getString(SHIPPING_COMPANY))
				.setShippingPhone(d.getString(SHIPPING_PHONE))
				.setShippingAddress1(d.getString(SHIPPING_ADDRESS_1))
				.setShippingAddress2(d.getString(SHIPPING_ADDRESS_2))
				.setShippingPostcode(shippingPostcode)
				.setShippingCity(d.getString(SHIPPING_CITY))
				.setShippingState(d.getString(SHIPPING_STATE))
				.setShippingCountry(d.getString(SHIPPING_COUNTRY))
				.setCustomerNote(d.getString(CUSTOMER_NOTE))
				.setWtImportKey(wtImportKey)
				.setTaxItems(d.getString(TAX_ITEMS))
				.setShippingItems(d.getString(SHIPPING_ITEMS))
				.setFeeItems(d.getString(FEE_ITEMS))
				.setCouponItems(d.getString(COUPON_ITEMS))
				.setRefundItems(d.getString(REFUND_ITEMS))
				.setOrderNotes(d.getString(ORDER_NOTES))
				.setDownloadPermissions(downloadPermissions)
				.setMetaWcOrderAttributionDeviceType(d.getString(META_WC_ORDER_ATTRIBUTION_DEVICE_TYPE))
				.setMetaWcOrderAttributionReferrer(d.getString(META_WC_ORDER_ATTRIBUTION_REFERRER))
				.setMetaWcOrderAttributionSessionCount(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_COUNT))
				.setMetaWcOrderAttributionSessionEntry(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_ENTRY))
				.setMetaWcOrderAttributionSessionPages(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_PAGES))
				.setMetaWcOrderAttributionSessionStartTime(d.getString(META_WC_ORDER_ATTRIBUTION_SESSION_START_TIME))
				.setMetaWcOrderAttributionSourceType(d.getString(META_WC_ORDER_ATTRIBUTION_SOURCE_TYPE))
				.setMetaWcOrderAttributionUserAgent(d.getString(META_WC_ORDER_ATTRIBUTION_USER_AGENT))
				.setMetaWcOrderAttributionUtmSource(d.getString(META_WC_ORDER_ATTRIBUTION_UTM_SOURCE))
				.setMetaPpcpPaypalFees(metaPpcpPaypalFees)
				.setMetaStripeCurrency(metaStripeCurrency)
				.setMetaStripeFee(metaStripeFee)
				.setMetaStripeNet(metaStripeNet)
				.setLineItem1(d.getString(LINE_ITEM_1))
				.setLineItem2(d.getString(LINE_ITEM_2))
				.setLineItem3(d.getString(LINE_ITEM_3))
				.setLineItem4(d.getString(LINE_ITEM_4))
				.setLineItem5(d.getString(LINE_ITEM_5))
				.setLastIssue(lastIssue)
				.setFirstIssue(firstIssue)
				.setOrderNetTotal(orderNetTotal)
				.setOrderConfirmed(orderConfirmed)
				.build();
	}

	private Document fromOrderToDocument(Order order) {

		String paidDate = "";
		if (order.getPaidDate() != null) {
			paidDate = order.getPaidDate().toString();
		}

		return new Document().append(ORDER_ID, order.getOrderId())
				.append(ORDER_DATE, order.getOrderDate().toString())
				.append(CUSTOMER_EMAIL, order.getCustomerEmail())
				.append(ORDER_NUMBER, order.getOrderNumber())
				.append(STATUS, order.getStatus())
				.append(SHIPPING_TOTAL, order.getShippingTotal())
				.append(SHIPPING_TAX_TOTAL, order.getShippingTaxTotal())
				.append(FEE_TOTAL, order.getFeeTotal())
				.append(FEE_TAX_TOTAL, order.getFeeTaxTotal())
				.append(TAX_TOTAL, order.getTaxTotal())
				.append(CART_DISCOUNT, order.getCartDiscount())
				.append(ORDER_DISCOUNT, order.getOrderDiscount())
				.append(DISCOUNT_TOTAL, order.getDiscountTotal())
				.append(ORDER_TOTAL, order.getOrderTotal())
				.append(ORDER_SUBTOTAL, order.getOrderSubtotal())
				.append(ORDER_KEY, order.getOrderKey())
				.append(ORDER_CURRENCY, order.getOrderCurrency())
				.append(PAYMENT_METHOD, order.getPaymentMethod())
				.append(PAYMENT_METHOD_TITLE, order.getPaymentMethodTitle())
				.append(TRANSACTION_ID, order.getTransactionId())
				.append(CUSTOMER_IP_ADDRESS, order.getCustomerIpAddress())
				.append(CUSTOMER_USER_AGENT, order.getCustomerUserAgent())
				.append(SHIPPING_METHOD, order.getShippingMethod())
				.append(CUSTOMER_ID, order.getCustomerId())
				.append(CUSTOMER_USER, order.getCustomerUser())
				.append(PAID_DATE, paidDate)
				.append(BILLING_FIRST_NAME, order.getBillingFirstName())
				.append(BILLING_LAST_NAME, order.getBillingLastName())
				.append(BILLING_COMPANY, order.getBillingCompany())
				.append(BILLING_EMAIL, order.getBillingEmail())
				.append(BILLING_PHONE, order.getBillingPhone())
				.append(BILLING_ADDRESS_1, order.getBillingAddress1())
				.append(BILLING_ADDRESS_2, order.getBillingAddress2())
				.append(BILLING_POSTCODE, order.getBillingPostcode())
				.append(BILLING_CITY, order.getBillingCity())
				.append(BILLING_STATE, order.getBillingState())
				.append(BILLING_COUNTRY, order.getBillingCountry())
				.append(SHIPPING_FIRST_NAME, order.getBillingFirstName())
				.append(SHIPPING_LAST_NAME, order.getBillingLastName())
				.append(SHIPPING_COMPANY, order.getShippingCompany())
				.append(SHIPPING_PHONE, order.getShippingPhone())
				.append(SHIPPING_ADDRESS_1, order.getShippingAddress1())
				.append(SHIPPING_ADDRESS_2, order.getShippingAddress2())
				.append(SHIPPING_POSTCODE, order.getShippingPostcode())
				.append(SHIPPING_CITY, order.getShippingCity())
				.append(SHIPPING_STATE, order.getShippingState())
				.append(SHIPPING_COUNTRY, order.getShippingCountry())
				.append(CUSTOMER_NOTE, order.getCustomerNote())
				.append(WT_IMPORT_KEY, order.getWtImportKey())
				.append(TAX_ITEMS, order.getTaxItems())
				.append(SHIPPING_ITEMS, order.getShippingItems())
				.append(FEE_ITEMS, order.getFeeItems())
				.append(COUPON_ITEMS, order.getCouponItems())
				.append(REFUND_ITEMS, order.getRefundItems())
				.append(ORDER_NOTES, order.getOrderNotes())
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
				.append(META_STRIPE_CURRENCY, order.getMetaStripeCurrency())
				.append(META_STRIPE_FEE, order.getMetaStripeFee())
				.append(META_STRIPE_NET, order.getMetaStripeNet())
				.append(LINE_ITEM_1, order.getLineItem1())
				.append(LINE_ITEM_2, order.getLineItem2())
				.append(LINE_ITEM_3, order.getLineItem3())
				.append(LINE_ITEM_4, order.getLineItem4())
				.append(LINE_ITEM_5, order.getLineItem5())
				.append(ORDER_CONFIRMED, true)
				.append(ORDER_NET_TOTAL, order.getOrderNetTotal())
				.append(FIRST_ISSUE, order.getFirstIssue())
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