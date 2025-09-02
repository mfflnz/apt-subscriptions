package org.blefuscu.apt.subscriptions.model;

public class FormattedOrder {

	private String orderId;
	private String orderDate;
	private String paidDate;
	private String orderTotal;
	private String orderNetTotal;
	private String paymentMethodTitle;
	private String shippingFirstName;
	private String shippingLastName;
	private String shippingAddress1;
	private String shippingPostcode;
	private String shippingCity;
	private String shippingState;
	private String customerEmail;
	private String billingPhone;
	private String shippingItems;
	private String firstIssue;
	private String lastIssue;
	private String customerNote;

	private FormattedOrder(FormattedOrderBuilder builder) {
		this.orderId = builder.orderId;
		this.orderDate = builder.orderDate;
		this.paidDate = builder.paidDate;
		this.orderTotal = builder.orderTotal;
		this.orderNetTotal = builder.orderNetTotal;
		this.paymentMethodTitle = builder.paymentMethodTitle;
		this.shippingFirstName = builder.shippingFirstName;
		this.shippingLastName = builder.shippingLastName;
		this.shippingAddress1 = builder.shippingAddress1;
		this.shippingPostcode = builder.shippingPostcode;
		this.shippingCity = builder.shippingCity;
		this.shippingState = builder.shippingState;
		this.customerEmail = builder.customerEmail;
		this.billingPhone = builder.billingPhone;
		this.shippingItems = builder.shippingItems;
		this.firstIssue = builder.firstIssue;
		this.lastIssue = builder.lastIssue;
		this.customerNote = builder.customerNote;
	}

	public static class FormattedOrderBuilder {
		
		private String orderId;
		private String orderDate;
		private String paidDate;
		private String orderTotal;
		private String orderNetTotal;
		private String paymentMethodTitle;
		private String shippingFirstName;
		private String shippingLastName;
		private String shippingAddress1;
		private String shippingPostcode;
		private String shippingCity;
		private String shippingState;
		private String customerEmail;
		private String billingPhone;
		private String shippingItems;
		private String firstIssue;
		private String lastIssue;
		private String customerNote;
		
		public FormattedOrderBuilder(String orderId) {
			this.orderId = orderId;
		}

		public FormattedOrderBuilder setOrderDate(String orderDate) {
			this.orderDate = orderDate;
			return this;
		}

		public FormattedOrderBuilder setPaidDate(String paidDate) {
			this.paidDate = paidDate;
			return this;
		}

		public FormattedOrderBuilder setOrderTotal(String orderTotal) {
			this.orderTotal = orderTotal;
			return this;
		}

		public FormattedOrderBuilder setOrderNetTotal(String orderNetTotal) {
			this.orderNetTotal = orderNetTotal;
			return this;
		}

		public FormattedOrderBuilder setPaymentMethodTitle(String paymentMethodTitle) {
			this.paymentMethodTitle = paymentMethodTitle;
			return this;
		}

		public FormattedOrderBuilder setShippingFirstName(String shippingFirstName) {
			this.shippingFirstName = shippingFirstName;
			return this;
		}

		public FormattedOrderBuilder setShippingLastName(String shippingLastName) {
			this.shippingLastName = shippingLastName;
			return this;
		}

		public FormattedOrderBuilder setShippingAddress1(String shippingAddress1) {
			this.shippingAddress1 = shippingAddress1;
			return this;
		}

		public FormattedOrderBuilder setShippingPostcode(String shippingPostcode) {
			this.shippingPostcode = shippingPostcode;
			return this;
		}

		public FormattedOrderBuilder setShippingCity(String shippingCity) {
			this.shippingCity = shippingCity;
			return this;
		}

		public FormattedOrderBuilder setShippingState(String shippingState) {
			this.shippingState = shippingState;
			return this;
		}

		public FormattedOrderBuilder setCustomerEmail(String customerEmail) {
			this.customerEmail = customerEmail;
			return this;
		}

		public FormattedOrderBuilder setBillingPhone(String billingPhone) {
			this.billingPhone = billingPhone;
			return this;
		}

		public FormattedOrderBuilder setShippingItems(String shippingItems) {
			this.shippingItems = shippingItems;
			return this;
		}

		public FormattedOrderBuilder setFirstIssue(String firstIssue) {
			this.firstIssue = firstIssue;
			return this;
		}

		public FormattedOrderBuilder setLastIssue(String lastIssue) {
			this.lastIssue = lastIssue;
			return this;
		}

		public FormattedOrderBuilder setCustomerNote(String customerNote) {
			this.customerNote = customerNote;
			return this;
		}
		
		public FormattedOrder build() {
			return new FormattedOrder(this);
		}
	}

	// Getters
	
	public String getOrderId() {
		return orderId;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public String getPaidDate() {
		return paidDate;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public String getOrderNetTotal() {
		return orderNetTotal;
	}

	public String getPaymentMethodTitle() {
		return paymentMethodTitle;
	}

	public String getShippingFirstName() {
		return shippingFirstName;
	}

	public String getShippingLastName() {
		return shippingLastName;
	}

	public String getShippingAddress1() {
		return shippingAddress1;
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

	public String getCustomerEmail() {
		return customerEmail;
	}

	public String getBillingPhone() {
		return billingPhone;
	}

	public String getShippingItems() {
		return shippingItems;
	}

	public String getFirstIssue() {
		return firstIssue;
	}

	public String getLastIssue() {
		return lastIssue;
	}

	public String getCustomerNote() {
		return customerNote;
	}
	
}
