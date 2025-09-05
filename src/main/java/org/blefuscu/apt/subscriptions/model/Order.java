package org.blefuscu.apt.subscriptions.model;

import java.time.LocalDate;
import java.util.Objects;

public class Order {

	// Required fields:
	private int orderId;
	private LocalDate orderDate;
	private String customerEmail;

	// Optional fields:	
	private int orderNumber;
	private LocalDate paidDate;
	private String status;
	private double shippingTotal;
	private double shippingTaxTotal;
	private double feeTotal;
	private double feeTaxTotal;
	private double taxTotal;
	private double cartDiscount;
	private double orderDiscount;
	private double discountTotal;
	private double orderTotal;
	private double orderSubtotal;
	private String orderKey;
	private String orderCurrency;
	private String paymentMethod;
	private String paymentMethodTitle;
	private String transactionId;
	private String customerIpAddress;
	private String customerUserAgent;
	private String shippingMethod;
	private int customerId;
	private String customerUser;
	private String billingFirstName;
	private String billingLastName;
	private String billingCompany;
	private String billingEmail;
	private String billingPhone;
	private String billingAddress1;
	private String billingAddress2;
	private String billingPostcode;
	private String billingCity;
	private String billingState;
	private String billingCountry;
	private String shippingFirstName;
	private String shippingLastName;
	private String shippingCompany;
	private String shippingPhone;
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingPostcode;
	private String shippingCity;
	private String shippingState;
	private String shippingCountry;
	private String customerNote;
	private String wtImportKey;
	private String taxItems;
	private String shippingItems;
	private String feeItems;
	private String couponItems;
	private String refundItems;
	private String orderNotes;
	private String downloadPermissions;
	private String metaWcOrderAttributionDeviceType;
	private String metaWcOrderAttributionReferrer;
	private String metaWcOrderAttributionSessionCount;
	private String metaWcOrderAttributionSessionEntry;
	private String metaWcOrderAttributionSessionPages;
	private String metaWcOrderAttributionSessionStartTime;
	private String metaWcOrderAttributionSourceType;
	private String metaWcOrderAttributionUserAgent;
	private String metaWcOrderAttributionUtmSource;
	private double metaPpcpPaypalFees;
	private String metaStripeCurrency;
	private double metaStripeFee;
	private double metaStripeNet;
	private String lineItem1;
	private String lineItem2;
	private String lineItem3;
	private String lineItem4;
	private String lineItem5;
	
	// Fields not specified in the documents:
	private boolean orderConfirmed;
	private double orderNetTotal;
	private int firstIssue;
	private int lastIssue;

	private Order(OrderBuilder builder) {
		this.orderId = builder.orderId;
		this.orderNumber = builder.orderNumber;
		this.orderDate = builder.orderDate;
		this.paidDate = builder.paidDate;
		this.status = builder.status;
		this.shippingTotal = builder.shippingTotal;
		this.shippingTaxTotal = builder.shippingTaxTotal;
		this.feeTotal = builder.feeTotal;
		this.feeTaxTotal = builder.feeTaxTotal;
		this.taxTotal = builder.taxTotal;
		this.cartDiscount = builder.cartDiscount;
		this.orderDiscount = builder.orderDiscount;
		this.discountTotal = builder.discountTotal;
		this.orderTotal = builder.orderTotal;
		this.orderSubtotal = builder.orderSubtotal;
		this.orderKey = builder.orderKey;
		this.orderCurrency = builder.orderCurrency;
		this.paymentMethod = builder.paymentMethod;
		this.paymentMethodTitle = builder.paymentMethodTitle;
		this.transactionId = builder.transactionId;
		this.customerIpAddress = builder.customerIpAddress;
		this.customerUserAgent = builder.customerUserAgent;
		this.shippingMethod = builder.shippingMethod;
		this.customerId = builder.customerId;
		this.customerUser = builder.customerUser;
		this.customerEmail = builder.customerEmail;
		this.billingFirstName = builder.billingFirstName;
		this.billingLastName = builder.billingLastName;
		this.billingCompany = builder.billingCompany;
		this.billingEmail = builder.billingEmail;
		this.billingPhone = builder.billingPhone;
		this.billingAddress1 = builder.billingAddress1;
		this.billingAddress2 = builder.billingAddress2;
		this.billingPostcode = builder.billingPostcode;
		this.billingCity = builder.billingCity;
		this.billingState = builder.billingState;
		this.billingCountry = builder.billingCountry;
		this.shippingFirstName = builder.shippingFirstName;
		this.shippingLastName = builder.shippingLastName;
		this.shippingCompany = builder.shippingCompany;
		this.shippingPhone = builder.shippingPhone;
		this.shippingAddress1 = builder.shippingAddress1;
		this.shippingAddress2 = builder.shippingAddress2;
		this.shippingPostcode = builder.shippingPostcode;
		this.shippingCity = builder.shippingCity;
		this.shippingState = builder.shippingState;
		this.shippingCountry = builder.shippingCountry;
		this.customerNote = builder.customerNote;
		this.wtImportKey = builder.wtImportKey;
		this.taxItems = builder.taxItems;
		this.shippingItems = builder.shippingItems;
		this.feeItems = builder.feeItems;
		this.couponItems = builder.couponItems;
		this.refundItems = builder.refundItems;
		this.orderNotes = builder.orderNotes;
		this.downloadPermissions = builder.downloadPermissions;
		this.metaWcOrderAttributionDeviceType = builder.metaWcOrderAttributionDeviceType;
		this.metaWcOrderAttributionReferrer = builder.metaWcOrderAttributionReferrer;
		this.metaWcOrderAttributionSessionCount = builder.metaWcOrderAttributionSessionCount;
		this.metaWcOrderAttributionSessionEntry = builder.metaWcOrderAttributionSessionEntry;
		this.metaWcOrderAttributionSessionPages = builder.metaWcOrderAttributionSessionPages;
		this.metaWcOrderAttributionSessionStartTime = builder.metaWcOrderAttributionSessionStartTime;
		this.metaWcOrderAttributionSourceType = builder.metaWcOrderAttributionSourceType;
		this.metaWcOrderAttributionUserAgent = builder.metaWcOrderAttributionUserAgent;
		this.metaWcOrderAttributionUtmSource = builder.metaWcOrderAttributionUtmSource;
		this.metaPpcpPaypalFees = builder.metaPpcpPaypalFees;
		this.metaStripeCurrency = builder.metaStripeCurrency;
		this.metaStripeFee = builder.metaStripeFee;
		this.metaStripeNet = builder.metaStripeNet;
		this.lineItem1 = builder.lineItem1;
		this.lineItem2 = builder.lineItem2;
		this.lineItem3 = builder.lineItem3;
		this.lineItem4 = builder.lineItem4;
		this.lineItem5 = builder.lineItem5;
		this.orderConfirmed = builder.orderConfirmed;
		this.orderNetTotal = builder.orderNetTotal;
		this.firstIssue = builder.firstIssue;
		this.lastIssue = builder.lastIssue;
	}

	public static class OrderBuilder {
		
		// Required fields:
		private int orderId;
		private LocalDate orderDate;
		private String customerEmail;

		// Optional fields:
		private int orderNumber;
		private LocalDate paidDate;
		private String status;
		private double shippingTotal;
		private double shippingTaxTotal;
		private double feeTotal;
		private double feeTaxTotal;
		private double taxTotal;
		private double cartDiscount;
		private double orderDiscount;
		private double discountTotal;
		private double orderTotal;
		private double orderSubtotal;
		private String orderKey;
		private String orderCurrency;
		private String paymentMethod;
		private String paymentMethodTitle;
		private String transactionId;
		private String customerIpAddress;
		private String customerUserAgent;
		private String shippingMethod;
		private int customerId;
		private String customerUser;
		private String billingFirstName;
		private String billingLastName;
		private String billingCompany;
		private String billingEmail;
		private String billingPhone;
		private String billingAddress1;
		private String billingAddress2;
		private String billingPostcode;
		private String billingCity;
		private String billingState;
		private String billingCountry;
		private String shippingFirstName;
		private String shippingLastName;
		private String shippingCompany;
		private String shippingPhone;
		private String shippingAddress1;
		private String shippingAddress2;
		private String shippingPostcode;
		private String shippingCity;
		private String shippingState;
		private String shippingCountry;
		private String customerNote;
		private String wtImportKey;
		private String taxItems;
		private String shippingItems;
		private String feeItems;
		private String couponItems;
		private String refundItems;
		private String orderNotes;
		private String downloadPermissions;
		private String metaWcOrderAttributionDeviceType;
		private String metaWcOrderAttributionReferrer;
		private String metaWcOrderAttributionSessionCount;
		private String metaWcOrderAttributionSessionEntry;
		private String metaWcOrderAttributionSessionPages;
		private String metaWcOrderAttributionSessionStartTime;
		private String metaWcOrderAttributionSourceType;
		private String metaWcOrderAttributionUserAgent;
		private String metaWcOrderAttributionUtmSource;
		private double metaPpcpPaypalFees;
		private String metaStripeCurrency;
		private double metaStripeFee;
		private double metaStripeNet;
		private String lineItem1;
		private String lineItem2;
		private String lineItem3;
		private String lineItem4;
		private String lineItem5;
		
		// Fields not specified in the documents:
		private int lastIssue;
		private int firstIssue;
		private double orderNetTotal;
		private boolean orderConfirmed;

		public OrderBuilder(int orderId, LocalDate orderDate, String customerEmail) {
			this.orderId = orderId;
			this.orderDate = orderDate;
			this.customerEmail = customerEmail;
		}

		public OrderBuilder setOrderNumber(int orderNumber) {
			this.orderNumber = orderNumber;
			return this;
		}

		public OrderBuilder setPaidDate(LocalDate paidDate) {
			this.paidDate = paidDate;
			return this;
		}

		public OrderBuilder setStatus(String status) {
			this.status = status;
			return this;
		}

		public OrderBuilder setShippingTotal(double shippingTotal) {
			this.shippingTotal = shippingTotal;
			return this;
		}

		public OrderBuilder setShippingTaxTotal(double shippingTaxTotal) {
			this.shippingTaxTotal = shippingTaxTotal;
			return this;
		}

		public OrderBuilder setFeeTotal(double feeTotal) {
			this.feeTotal = feeTotal;
			return this;
		}

		public OrderBuilder setFeeTaxTotal(double feeTaxTotal) {
			this.feeTaxTotal = feeTaxTotal;
			return this;
		}

		public OrderBuilder setTaxTotal(double taxTotal) {
			this.taxTotal = taxTotal;
			return this;
		}

		public OrderBuilder setCartDiscount(double cartDiscount) {
			this.cartDiscount = cartDiscount;
			return this;
		}

		public OrderBuilder setOrderDiscount(double orderDiscount) {
			this.orderDiscount = orderDiscount;
			return this;
		}

		public OrderBuilder setDiscountTotal(double discountTotal) {
			this.discountTotal = discountTotal;
			return this;
		}

		public OrderBuilder setOrderTotal(double orderTotal) {
			this.orderTotal = orderTotal;
			return this;
		}

		public OrderBuilder setOrderSubtotal(double orderSubtotal) {
			this.orderSubtotal = orderSubtotal;
			return this;
		}

		public OrderBuilder setOrderKey(String orderKey) {
			this.orderKey = orderKey;
			return this;
		}

		public OrderBuilder setOrderCurrency(String orderCurrency) {
			this.orderCurrency = orderCurrency;
			return this;
		}

		public OrderBuilder setPaymentMethod(String paymentMethod) {
			this.paymentMethod = paymentMethod;
			return this;
		}

		public OrderBuilder setPaymentMethodTitle(String paymentMethodTitle) {
			this.paymentMethodTitle = paymentMethodTitle;
			return this;
		}

		public OrderBuilder setTransactionId(String transactionId) {
			this.transactionId = transactionId;
			return this;
		}

		public OrderBuilder setCustomerIpAddress(String customerIpAddress) {
			this.customerIpAddress = customerIpAddress;
			return this;
		}

		public OrderBuilder setCustomerUserAgent(String customerUserAgent) {
			this.customerUserAgent = customerUserAgent;
			return this;
		}

		public OrderBuilder setShippingMethod(String shippingMethod) {
			this.shippingMethod = shippingMethod;
			return this;
		}

		public OrderBuilder setCustomerId(int customerId) {
			this.customerId = customerId;
			return this;
		}

		public OrderBuilder setCustomerUser(String customerUser) {
			this.customerUser = customerUser;
			return this;
		}

		public OrderBuilder setBillingFirstName(String billingFirstName) {
			this.billingFirstName = billingFirstName;
			return this;
		}

		public OrderBuilder setBillingLastName(String billingLastName) {
			this.billingLastName = billingLastName;
			return this;
		}

		public OrderBuilder setBillingCompany(String billingCompany) {
			this.billingCompany = billingCompany;
			return this;
		}

		public OrderBuilder setBillingEmail(String billingEmail) {
			this.billingEmail = billingEmail;
			return this;
		}

		public OrderBuilder setBillingPhone(String billingPhone) {
			this.billingPhone = billingPhone;
			return this;
		}

		public OrderBuilder setBillingAddress1(String billingAddress1) {
			this.billingAddress1 = billingAddress1;
			return this;
		}

		public OrderBuilder setBillingAddress2(String billingAddress2) {
			this.billingAddress2 = billingAddress2;
			return this;
		}

		public OrderBuilder setBillingPostcode(String billingPostcode) {
			this.billingPostcode = billingPostcode;
			return this;
		}

		public OrderBuilder setBillingCity(String billingCity) {
			this.billingCity = billingCity;
			return this;
		}

		public OrderBuilder setBillingState(String billingState) {
			this.billingState = billingState;
			return this;
		}

		public OrderBuilder setBillingCountry(String billingCountry) {
			this.billingCountry = billingCountry;
			return this;
		}

		public OrderBuilder setShippingFirstName(String shippingFirstName) {
			this.shippingFirstName = shippingFirstName;
			return this;
		}

		public OrderBuilder setShippingLastName(String shippingLastName) {
			this.shippingLastName = shippingLastName;
			return this;
		}

		public OrderBuilder setShippingCompany(String shippingCompany) {
			this.shippingCompany = shippingCompany;
			return this;
		}

		public OrderBuilder setShippingPhone(String shippingPhone) {
			this.shippingPhone = shippingPhone;
			return this;
		}

		public OrderBuilder setShippingAddress1(String shippingAddress1) {
			this.shippingAddress1 = shippingAddress1;
			return this;
		}

		public OrderBuilder setShippingAddress2(String shippingAddress2) {
			this.shippingAddress2 = shippingAddress2;
			return this;
		}

		public OrderBuilder setShippingPostcode(String shippingPostcode) {
			this.shippingPostcode = shippingPostcode;
			return this;
		}

		public OrderBuilder setShippingCity(String shippingCity) {
			this.shippingCity = shippingCity;
			return this;
		}

		public OrderBuilder setShippingState(String shippingState) {
			this.shippingState = shippingState;
			return this;
		}

		public OrderBuilder setShippingCountry(String shippingCountry) {
			this.shippingCountry = shippingCountry;
			return this;
		}

		public OrderBuilder setCustomerNote(String customerNote) {
			this.customerNote = customerNote;
			return this;
		}

		public OrderBuilder setWtImportKey(String wtImportKey) {
			this.wtImportKey = wtImportKey;
			return this;
		}

		public OrderBuilder setTaxItems(String taxItems) {
			this.taxItems = taxItems;
			return this;
		}

		public OrderBuilder setShippingItems(String shippingItems) {
			this.shippingItems = shippingItems;
			return this;
		}

		public OrderBuilder setFeeItems(String feeItems) {
			this.feeItems = feeItems;
			return this;
		}

		public OrderBuilder setCouponItems(String couponItems) {
			this.couponItems = couponItems;
			return this;
		}

		public OrderBuilder setRefundItems(String refundItems) {
			this.refundItems = refundItems;
			return this;
		}

		public OrderBuilder setOrderNotes(String orderNotes) {
			this.orderNotes = orderNotes;
			return this;
		}

		public OrderBuilder setDownloadPermissions(String downloadPermissions) {
			this.downloadPermissions = downloadPermissions;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionDeviceType(String metaWcOrderAttributionDeviceType) {
			this.metaWcOrderAttributionDeviceType = metaWcOrderAttributionDeviceType;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionReferrer(String metaWcOrderAttributionReferrer) {
			this.metaWcOrderAttributionReferrer = metaWcOrderAttributionReferrer;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionSessionCount(String metaWcOrderAttributionSessionCount) {
			this.metaWcOrderAttributionSessionCount = metaWcOrderAttributionSessionCount;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionSessionEntry(String metaWcOrderAttributionSessionEntry) {
			this.metaWcOrderAttributionSessionEntry = metaWcOrderAttributionSessionEntry;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionSessionPages(String metaWcOrderAttributionSessionPages) {
			this.metaWcOrderAttributionSessionPages = metaWcOrderAttributionSessionPages;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionSessionStartTime(String metaWcOrderAttributionSessionStartTime) {
			this.metaWcOrderAttributionSessionStartTime = metaWcOrderAttributionSessionStartTime;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionSourceType(String metaWcOrderAttributionSourceType) {
			this.metaWcOrderAttributionSourceType = metaWcOrderAttributionSourceType;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionUserAgent(String metaWcOrderAttributionUserAgent) {
			this.metaWcOrderAttributionUserAgent = metaWcOrderAttributionUserAgent;
			return this;
		}

		public OrderBuilder setMetaWcOrderAttributionUtmSource(String metaWcOrderAttributionUtmSource) {
			this.metaWcOrderAttributionUtmSource = metaWcOrderAttributionUtmSource;
			return this;
		}

		public OrderBuilder setMetaPpcpPaypalFees(double metaPpcpPaypalFees) {
			this.metaPpcpPaypalFees = metaPpcpPaypalFees;
			return this;
		}

		public OrderBuilder setMetaStripeCurrency(String metaStripeCurrency) {
			this.metaStripeCurrency = metaStripeCurrency;
			return this;
		}

		public OrderBuilder setMetaStripeFee(double metaStripeFee) {
			this.metaStripeFee = metaStripeFee;
			return this;
		}

		public OrderBuilder setMetaStripeNet(double metaStripeNet) {
			this.metaStripeNet = metaStripeNet;
			return this;
		}

		public OrderBuilder setLineItem1(String lineItem1) {
			this.lineItem1 = lineItem1;
			return this;
		}

		public OrderBuilder setLineItem2(String lineItem2) {
			this.lineItem2 = lineItem2;
			return this;
		}

		public OrderBuilder setLineItem3(String lineItem3) {
			this.lineItem3 = lineItem3;
			return this;
		}

		public OrderBuilder setLineItem4(String lineItem4) {
			this.lineItem4 = lineItem4;
			return this;
		}

		public OrderBuilder setLineItem5(String lineItem5) {
			this.lineItem5 = lineItem5;
			return this;
		}
		
		public OrderBuilder setOrderConfirmed(boolean orderConfirmed) {
			this.orderConfirmed = orderConfirmed;
			return this;
		}
		
		public OrderBuilder setOrderNetTotal(double orderNetTotal) {
			this.orderNetTotal = orderNetTotal;
			return this;
		}
		
		public OrderBuilder setFirstIssue(int firstIssue) {
			this.firstIssue = firstIssue;
			return this;
		}
		
		public OrderBuilder setLastIssue(int lastIssue) {
			this.lastIssue = lastIssue;
			return this;
		}
		
		public Order build() {
			return new Order(this);
		}

	}

	// Getters

	public int getOrderId() {
		return orderId;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public LocalDate getPaidDate() {
		return paidDate;
	}

	public String getStatus() {
		return status;
	}

	public double getShippingTotal() {
		return shippingTotal;
	}

	public double getShippingTaxTotal() {
		return shippingTaxTotal;
	}

	public double getFeeTotal() {
		return feeTotal;
	}

	public double getFeeTaxTotal() {
		return feeTaxTotal;
	}

	public double getTaxTotal() {
		return taxTotal;
	}

	public double getCartDiscount() {
		return cartDiscount;
	}

	public double getOrderDiscount() {
		return orderDiscount;
	}

	public double getDiscountTotal() {
		return discountTotal;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public double getOrderSubtotal() {
		return orderSubtotal;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public String getOrderCurrency() {
		return orderCurrency;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public String getPaymentMethodTitle() {
		return paymentMethodTitle;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getCustomerIpAddress() {
		return customerIpAddress;
	}

	public String getCustomerUserAgent() {
		return customerUserAgent;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public int getCustomerId() {
		return customerId;
	}

	public String getCustomerUser() {
		return customerUser;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public String getBillingFirstName() {
		return billingFirstName;
	}

	public String getBillingLastName() {
		return billingLastName;
	}

	public String getBillingCompany() {
		return billingCompany;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public String getBillingPhone() {
		return billingPhone;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public String getBillingPostcode() {
		return billingPostcode;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public String getShippingFirstName() {
		return shippingFirstName;
	}

	public String getShippingLastName() {
		return shippingLastName;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public String getShippingAddress1() {
		return shippingAddress1;
	}

	public String getShippingAddress2() {
		return shippingAddress2;
	}

	public String getShippingPostcode() {
		return shippingPostcode;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public String getCustomerNote() {
		return customerNote;
	}

	public String getWtImportKey() {
		return wtImportKey;
	}

	public String getTaxItems() {
		return taxItems;
	}

	public String getShippingItems() {
		return shippingItems;
	}

	public String getFeeItems() {
		return feeItems;
	}

	public String getCouponItems() {
		return couponItems;
	}

	public String getRefundItems() {
		return refundItems;
	}

	public String getOrderNotes() {
		return orderNotes;
	}

	public String getDownloadPermissions() {
		return downloadPermissions;
	}

	public String getMetaWcOrderAttributionDeviceType() {
		return metaWcOrderAttributionDeviceType;
	}

	public String getMetaWcOrderAttributionReferrer() {
		return metaWcOrderAttributionReferrer;
	}

	public String getMetaWcOrderAttributionSessionCount() {
		return metaWcOrderAttributionSessionCount;
	}

	public String getMetaWcOrderAttributionSessionEntry() {
		return metaWcOrderAttributionSessionEntry;
	}

	public String getMetaWcOrderAttributionSessionPages() {
		return metaWcOrderAttributionSessionPages;
	}

	public String getMetaWcOrderAttributionSessionStartTime() {
		return metaWcOrderAttributionSessionStartTime;
	}

	public String getMetaWcOrderAttributionSourceType() {
		return metaWcOrderAttributionSourceType;
	}

	public String getMetaWcOrderAttributionUserAgent() {
		return metaWcOrderAttributionUserAgent;
	}

	public String getMetaWcOrderAttributionUtmSource() {
		return metaWcOrderAttributionUtmSource;
	}

	public double getMetaPpcpPaypalFees() {
		return metaPpcpPaypalFees;
	}

	public String getMetaStripeCurrency() {
		return metaStripeCurrency;
	}

	public double getMetaStripeFee() {
		return metaStripeFee;
	}

	public double getMetaStripeNet() {
		return metaStripeNet;
	}

	public String getLineItem1() {
		return lineItem1;
	}

	public String getLineItem2() {
		return lineItem2;
	}

	public String getLineItem3() {
		return lineItem3;
	}

	public String getLineItem4() {
		return lineItem4;
	}

	public String getLineItem5() {
		return lineItem5;
	}
	
	public boolean isOrderConfirmed() {
		return orderConfirmed;
	}

	public double getOrderNetTotal() {
		return orderNetTotal;
	}

	public int getFirstIssue() {
		return firstIssue;
	}

	public int getLastIssue() {
		return lastIssue;
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", customerEmail=" + customerEmail
				+ ", orderNumber=" + orderNumber + ", paidDate=" + paidDate + ", status=" + status + ", shippingTotal="
				+ shippingTotal + ", shippingTaxTotal=" + shippingTaxTotal + ", feeTotal=" + feeTotal + ", feeTaxTotal="
				+ feeTaxTotal + ", taxTotal=" + taxTotal + ", cartDiscount=" + cartDiscount + ", orderDiscount="
				+ orderDiscount + ", discountTotal=" + discountTotal + ", orderTotal=" + orderTotal + ", orderSubtotal="
				+ orderSubtotal + ", orderKey=" + orderKey + ", orderCurrency=" + orderCurrency + ", paymentMethod="
				+ paymentMethod + ", paymentMethodTitle=" + paymentMethodTitle + ", transactionId=" + transactionId
				+ ", customerIpAddress=" + customerIpAddress + ", customerUserAgent=" + customerUserAgent
				+ ", shippingMethod=" + shippingMethod + ", customerId=" + customerId + ", customerUser=" + customerUser
				+ ", billingFirstName=" + billingFirstName + ", billingLastName=" + billingLastName
				+ ", billingCompany=" + billingCompany + ", billingEmail=" + billingEmail + ", billingPhone="
				+ billingPhone + ", billingAddress1=" + billingAddress1 + ", billingAddress2=" + billingAddress2
				+ ", billingPostcode=" + billingPostcode + ", billingCity=" + billingCity + ", billingState="
				+ billingState + ", billingCountry=" + billingCountry + ", shippingFirstName=" + shippingFirstName
				+ ", shippingLastName=" + shippingLastName + ", shippingCompany=" + shippingCompany + ", shippingPhone="
				+ shippingPhone + ", shippingAddress1=" + shippingAddress1 + ", shippingAddress2=" + shippingAddress2
				+ ", shippingPostcode=" + shippingPostcode + ", shippingCity=" + shippingCity + ", shippingState="
				+ shippingState + ", shippingCountry=" + shippingCountry + ", customerNote=" + customerNote
				+ ", wtImportKey=" + wtImportKey + ", taxItems=" + taxItems + ", shippingItems=" + shippingItems
				+ ", feeItems=" + feeItems + ", couponItems=" + couponItems + ", refundItems=" + refundItems
				+ ", orderNotes=" + orderNotes + ", downloadPermissions=" + downloadPermissions
				+ ", metaWcOrderAttributionDeviceType=" + metaWcOrderAttributionDeviceType
				+ ", metaWcOrderAttributionReferrer=" + metaWcOrderAttributionReferrer
				+ ", metaWcOrderAttributionSessionCount=" + metaWcOrderAttributionSessionCount
				+ ", metaWcOrderAttributionSessionEntry=" + metaWcOrderAttributionSessionEntry
				+ ", metaWcOrderAttributionSessionPages=" + metaWcOrderAttributionSessionPages
				+ ", metaWcOrderAttributionSessionStartTime=" + metaWcOrderAttributionSessionStartTime
				+ ", metaWcOrderAttributionSourceType=" + metaWcOrderAttributionSourceType
				+ ", metaWcOrderAttributionUserAgent=" + metaWcOrderAttributionUserAgent
				+ ", metaWcOrderAttributionUtmSource=" + metaWcOrderAttributionUtmSource + ", metaPpcpPaypalFees="
				+ metaPpcpPaypalFees + ", metaStripeCurrency=" + metaStripeCurrency + ", metaStripeFee=" + metaStripeFee
				+ ", metaStripeNet=" + metaStripeNet + ", lineItem1=" + lineItem1 + ", lineItem2=" + lineItem2
				+ ", lineItem3=" + lineItem3 + ", lineItem4=" + lineItem4 + ", lineItem5=" + lineItem5
				+ ", orderConfirmed=" + orderConfirmed + ", orderNetTotal=" + orderNetTotal + ", firstIssue="
				+ firstIssue + ", lastIssue=" + lastIssue + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(billingAddress1, billingAddress2, billingCity, billingCompany, billingCountry, billingEmail,
				billingFirstName, billingLastName, billingPhone, billingPostcode, billingState, cartDiscount,
				couponItems, customerEmail, customerId, customerIpAddress, customerNote, customerUser,
				customerUserAgent, discountTotal, downloadPermissions, feeItems, feeTaxTotal, feeTotal, firstIssue,
				lastIssue, lineItem1, lineItem2, lineItem3, lineItem4, lineItem5, metaPpcpPaypalFees,
				metaStripeCurrency, metaStripeFee, metaStripeNet, metaWcOrderAttributionDeviceType,
				metaWcOrderAttributionReferrer, metaWcOrderAttributionSessionCount, metaWcOrderAttributionSessionEntry,
				metaWcOrderAttributionSessionPages, metaWcOrderAttributionSessionStartTime,
				metaWcOrderAttributionSourceType, metaWcOrderAttributionUserAgent, metaWcOrderAttributionUtmSource,
				orderConfirmed, orderCurrency, orderDate, orderDiscount, orderId, orderKey, orderNetTotal, orderNotes,
				orderNumber, orderSubtotal, orderTotal, paidDate, paymentMethod, paymentMethodTitle, refundItems,
				shippingAddress1, shippingAddress2, shippingCity, shippingCompany, shippingCountry, shippingFirstName,
				shippingItems, shippingLastName, shippingMethod, shippingPhone, shippingPostcode, shippingState,
				shippingTaxTotal, shippingTotal, status, taxItems, taxTotal, transactionId, wtImportKey);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(billingAddress1, other.billingAddress1)
				&& Objects.equals(billingAddress2, other.billingAddress2)
				&& Objects.equals(billingCity, other.billingCity)
				&& Objects.equals(billingCompany, other.billingCompany)
				&& Objects.equals(billingCountry, other.billingCountry)
				&& Objects.equals(billingEmail, other.billingEmail)
				&& Objects.equals(billingFirstName, other.billingFirstName)
				&& Objects.equals(billingLastName, other.billingLastName)
				&& Objects.equals(billingPhone, other.billingPhone)
				&& Objects.equals(billingPostcode, other.billingPostcode)
				&& Objects.equals(billingState, other.billingState)
				&& Double.doubleToLongBits(cartDiscount) == Double.doubleToLongBits(other.cartDiscount)
				&& Objects.equals(couponItems, other.couponItems) && Objects.equals(customerEmail, other.customerEmail)
				&& customerId == other.customerId && Objects.equals(customerIpAddress, other.customerIpAddress)
				&& Objects.equals(customerNote, other.customerNote) && Objects.equals(customerUser, other.customerUser)
				&& Objects.equals(customerUserAgent, other.customerUserAgent)
				&& Double.doubleToLongBits(discountTotal) == Double.doubleToLongBits(other.discountTotal)
				&& Objects.equals(downloadPermissions, other.downloadPermissions)
				&& Objects.equals(feeItems, other.feeItems)
				&& Double.doubleToLongBits(feeTaxTotal) == Double.doubleToLongBits(other.feeTaxTotal)
				&& Double.doubleToLongBits(feeTotal) == Double.doubleToLongBits(other.feeTotal)
				&& firstIssue == other.firstIssue && lastIssue == other.lastIssue
				&& Objects.equals(lineItem1, other.lineItem1) && Objects.equals(lineItem2, other.lineItem2)
				&& Objects.equals(lineItem3, other.lineItem3) && Objects.equals(lineItem4, other.lineItem4)
				&& Objects.equals(lineItem5, other.lineItem5)
				&& Double.doubleToLongBits(metaPpcpPaypalFees) == Double.doubleToLongBits(other.metaPpcpPaypalFees)
				&& Objects.equals(metaStripeCurrency, other.metaStripeCurrency)
				&& Double.doubleToLongBits(metaStripeFee) == Double.doubleToLongBits(other.metaStripeFee)
				&& Double.doubleToLongBits(metaStripeNet) == Double.doubleToLongBits(other.metaStripeNet)
				&& Objects.equals(metaWcOrderAttributionDeviceType, other.metaWcOrderAttributionDeviceType)
				&& Objects.equals(metaWcOrderAttributionReferrer, other.metaWcOrderAttributionReferrer)
				&& Objects.equals(metaWcOrderAttributionSessionCount, other.metaWcOrderAttributionSessionCount)
				&& Objects.equals(metaWcOrderAttributionSessionEntry, other.metaWcOrderAttributionSessionEntry)
				&& Objects.equals(metaWcOrderAttributionSessionPages, other.metaWcOrderAttributionSessionPages)
				&& Objects.equals(metaWcOrderAttributionSessionStartTime, other.metaWcOrderAttributionSessionStartTime)
				&& Objects.equals(metaWcOrderAttributionSourceType, other.metaWcOrderAttributionSourceType)
				&& Objects.equals(metaWcOrderAttributionUserAgent, other.metaWcOrderAttributionUserAgent)
				&& Objects.equals(metaWcOrderAttributionUtmSource, other.metaWcOrderAttributionUtmSource)
				&& orderConfirmed == other.orderConfirmed && Objects.equals(orderCurrency, other.orderCurrency)
				&& Objects.equals(orderDate, other.orderDate)
				&& Double.doubleToLongBits(orderDiscount) == Double.doubleToLongBits(other.orderDiscount)
				&& orderId == other.orderId && Objects.equals(orderKey, other.orderKey)
				&& Double.doubleToLongBits(orderNetTotal) == Double.doubleToLongBits(other.orderNetTotal)
				&& Objects.equals(orderNotes, other.orderNotes) && orderNumber == other.orderNumber
				&& Double.doubleToLongBits(orderSubtotal) == Double.doubleToLongBits(other.orderSubtotal)
				&& Double.doubleToLongBits(orderTotal) == Double.doubleToLongBits(other.orderTotal)
				&& Objects.equals(paidDate, other.paidDate) && Objects.equals(paymentMethod, other.paymentMethod)
				&& Objects.equals(paymentMethodTitle, other.paymentMethodTitle)
				&& Objects.equals(refundItems, other.refundItems)
				&& Objects.equals(shippingAddress1, other.shippingAddress1)
				&& Objects.equals(shippingAddress2, other.shippingAddress2)
				&& Objects.equals(shippingCity, other.shippingCity)
				&& Objects.equals(shippingCompany, other.shippingCompany)
				&& Objects.equals(shippingCountry, other.shippingCountry)
				&& Objects.equals(shippingFirstName, other.shippingFirstName)
				&& Objects.equals(shippingItems, other.shippingItems)
				&& Objects.equals(shippingLastName, other.shippingLastName)
				&& Objects.equals(shippingMethod, other.shippingMethod)
				&& Objects.equals(shippingPhone, other.shippingPhone)
				&& Objects.equals(shippingPostcode, other.shippingPostcode)
				&& Objects.equals(shippingState, other.shippingState)
				&& Double.doubleToLongBits(shippingTaxTotal) == Double.doubleToLongBits(other.shippingTaxTotal)
				&& Double.doubleToLongBits(shippingTotal) == Double.doubleToLongBits(other.shippingTotal)
				&& Objects.equals(status, other.status) && Objects.equals(taxItems, other.taxItems)
				&& Double.doubleToLongBits(taxTotal) == Double.doubleToLongBits(other.taxTotal)
				&& Objects.equals(transactionId, other.transactionId) && Objects.equals(wtImportKey, other.wtImportKey);
	}
	
	
}
