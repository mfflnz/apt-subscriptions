package org.blefuscu.apt.subscriptions.model;

import java.time.LocalDate;
import java.util.Objects;

import com.groupcdg.pitest.annotations.DoNotMutate;

public class Order {

	private int orderId;
	private LocalDate orderDate;

	private LocalDate depositDate;
	private double orderTotal;
	private double netOrderTotal;
	private String paymentMethodTitle; // e.g. "PayPal", "Credit Card", ...
	private String billingFirstName;
	private String billingLastName;
	private String billingCompany;
	private String billingAddress1;
	private String billingAddress2;
	private String billingPostcode; // five-digit number padded with leading zeros
	private String billingCity;
	private String billingState;
	private String billingCountry;
	private String billingEmail;
	private String billingPhone;
	private String orderAttributionReferrer; // contains human-readable product name
	private int firstIssue;
	private int lastIssue;
	private String notes;

	private String shippingFirstName;
	private String shippingLastName;
	private String shippingCompany;
	private String shippingEmail;
	private String shippingPhone;
	private String shippingAddress1;
	private String shippingAddress2;
	private String shippingPostcode; // five-digit number padded with leading zeros
	private String shippingCity;
	private String shippingState;
	private String shippingCountry;

	// Public Order constructors were removed for builder's sake

	private Order(OrderBuilder builder) {
		this.orderId = builder.orderId;
		this.orderDate = builder.orderDate;
		this.depositDate = builder.depositDate;
		this.orderTotal = builder.orderTotal;
		this.netOrderTotal = builder.netOrderTotal;
		this.paymentMethodTitle = builder.paymentMethodTitle;
		this.billingFirstName = builder.billingFirstName;
		this.billingLastName = builder.billingLastName;
		this.billingCompany = builder.billingCompany;
		this.billingAddress1 = builder.billingAddress1;
		this.billingAddress2 = builder.billingAddress2;
		this.billingPostcode = builder.billingPostcode;
		this.billingCity = builder.billingCity;
		this.billingState = builder.billingState;
		this.billingCountry = builder.billingCountry;
		this.billingEmail = builder.billingEmail;
		this.billingPhone = builder.billingPhone;
		this.orderAttributionReferrer = builder.orderAttributionReferrer;
		this.firstIssue = builder.firstIssue;
		this.lastIssue = builder.lastIssue;
		this.notes = builder.notes;
		this.shippingFirstName = builder.shippingFirstName;
		this.shippingLastName = builder.shippingLastName;
		this.shippingCompany = builder.shippingCompany;
		this.shippingEmail = builder.shippingEmail;
		this.shippingPhone = builder.shippingPhone;
		this.shippingAddress1 = builder.shippingAddress1;
		this.shippingAddress2 = builder.shippingAddress2;
		this.shippingPostcode = builder.shippingPostcode;
		this.shippingCity = builder.shippingCity;
		this.shippingState = builder.shippingState;
		this.shippingCountry = builder.shippingCountry;
	}

	public static class OrderBuilder {

		// Required
		private int orderId;
		private LocalDate orderDate;
		private double orderTotal;
		private String paymentMethodTitle; // e.g. "PayPal", "Credit Card", ...
		private String orderAttributionReferrer; // contains human-readable product name
		private String billingEmail;

		// Optional
		private LocalDate depositDate;
		private double netOrderTotal;
		private String billingFirstName;
		private String billingLastName;
		private String billingCompany;
		private String billingAddress1;
		private String billingAddress2;
		private String billingPostcode; // five-digit number padded with leading zeros
		private String billingCity;
		private String billingState;
		private String billingCountry;
		private String billingPhone;
		private int firstIssue;
		private int lastIssue;
		private String notes;
		private String shippingFirstName;
		private String shippingLastName;
		private String shippingCompany;
		private String shippingEmail;
		private String shippingPhone;
		private String shippingAddress1;
		private String shippingAddress2;
		private String shippingPostcode; // five-digit number padded with leading zeros
		private String shippingCity;
		private String shippingState;
		private String shippingCountry;

		// Builder with required fields only
		public OrderBuilder(int orderId, LocalDate orderDate, double orderTotal, String paymentMethodTitle,
				String orderAttributionReferrer, String billingEmail) {
			this.orderId = orderId;
			this.orderDate = orderDate;
			this.orderTotal = orderTotal;
			this.paymentMethodTitle = paymentMethodTitle;
			this.orderAttributionReferrer = orderAttributionReferrer;
			this.billingEmail = billingEmail;

		}

		public OrderBuilder setDepositDate(LocalDate depositDate) {
			this.depositDate = depositDate;
			return this;
		}

		public OrderBuilder setNetOrderTotal(double netOrderTotal) {
			this.netOrderTotal = netOrderTotal;
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

		public OrderBuilder setBillingPhone(String billingPhone) {
			this.billingPhone = billingPhone;
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

		public OrderBuilder setNotes(String notes) {
			this.notes = notes;
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

		public OrderBuilder setShippingEmail(String shippingEmail) {
			this.shippingEmail = shippingEmail;
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

		public Order build() {
			return new Order(this);
		}

	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public double getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(double orderTotal) {
		this.orderTotal = orderTotal;
	}

	public String getPaymentMethodTitle() {
		return paymentMethodTitle;
	}

	public void setPaymentMethodTitle(String paymentMethodTitle) {
		this.paymentMethodTitle = paymentMethodTitle;
	}

	public String getBillingFirstName() {
		return billingFirstName;
	}

	public void setBillingFirstName(String billingFirstName) {
		this.billingFirstName = billingFirstName;
	}

	public String getBillingLastName() {
		return billingLastName;
	}

	public void setBillingLastName(String billingLastName) {
		this.billingLastName = billingLastName;
	}

	public String getBillingCompany() {
		return billingCompany;
	}

	public void setBillingCompany(String billingCompany) {
		this.billingCompany = billingCompany;
	}

	public String getBillingEmail() {
		return billingEmail;
	}

	public void setBillingEmail(String billingEmail) {
		this.billingEmail = billingEmail;
	}

	public String getBillingPhone() {
		return billingPhone;
	}

	public void setBillingPhone(String billingPhone) {
		this.billingPhone = billingPhone;
	}

	public String getBillingAddress1() {
		return billingAddress1;
	}

	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}

	public String getBillingAddress2() {
		return billingAddress2;
	}

	public void setBillingAddress2(String billingAddress2) {
		this.billingAddress2 = billingAddress2;
	}

	public String getBillingPostcode() {
		return billingPostcode;
	}

	public void setBillingPostcode(String billingPostcode) {
		this.billingPostcode = billingPostcode;
	}

	public String getBillingCity() {
		return billingCity;
	}

	public void setBillingCity(String billingCity) {
		this.billingCity = billingCity;
	}

	public String getBillingState() {
		return billingState;
	}

	public void setBillingState(String billingState) {
		this.billingState = billingState;
	}

	public String getBillingCountry() {
		return billingCountry;
	}

	public void setBillingCountry(String billingCountry) {
		this.billingCountry = billingCountry;
	}

	public String getShippingFirstName() {
		return shippingFirstName;
	}

	public void setShippingFirstName(String shippingFirstName) {
		this.shippingFirstName = shippingFirstName;
	}

	public String getShippingLastName() {
		return shippingLastName;
	}

	public void setShippingLastName(String shippingLastName) {
		this.shippingLastName = shippingLastName;
	}

	public String getShippingCompany() {
		return shippingCompany;
	}

	public void setShippingCompany(String shippingCompany) {
		this.shippingCompany = shippingCompany;
	}

	public String getShippingEmail() {
		return shippingEmail;
	}

	public void setShippingEmail(String shippingEmail) {
		this.shippingEmail = shippingEmail;
	}

	public String getShippingPhone() {
		return shippingPhone;
	}

	public void setShippingPhone(String shippingPhone) {
		this.shippingPhone = shippingPhone;
	}

	public String getShippingAddress1() {
		return shippingAddress1;
	}

	public void setShippingAddress1(String shippingAddress1) {
		this.shippingAddress1 = shippingAddress1;
	}

	public String getShippingAddress2() {
		return shippingAddress2;
	}

	public void setShippingAddress2(String shippingAddress2) {
		this.shippingAddress2 = shippingAddress2;
	}

	public String getShippingPostcode() {
		return shippingPostcode;
	}

	public void setShippingPostcode(String shippingPostcode) {
		this.shippingPostcode = shippingPostcode;
	}

	public String getShippingCity() {
		return shippingCity;
	}

	public void setShippingCity(String shippingCity) {
		this.shippingCity = shippingCity;
	}

	public String getShippingState() {
		return shippingState;
	}

	public void setShippingState(String shippingState) {
		this.shippingState = shippingState;
	}

	public String getShippingCountry() {
		return shippingCountry;
	}

	public void setShippingCountry(String shippingCountry) {
		this.shippingCountry = shippingCountry;
	}

	public String getOrderAttributionReferrer() {
		return orderAttributionReferrer;
	}

	public void setOrderAttributionReferrer(String orderAttributionReferrer) {
		this.orderAttributionReferrer = orderAttributionReferrer;
	}

	public LocalDate getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(LocalDate depositDate) {
		this.depositDate = depositDate;
	}

	public double getNetOrderTotal() {
		return netOrderTotal;
	}

	public void setNetOrderTotal(double netOrderTotal) {
		this.netOrderTotal = netOrderTotal;
	}

	public int getFirstIssue() {
		return firstIssue;
	}

	public void setFirstIssue(int firstIssue) {
		this.firstIssue = firstIssue;
	}

	public int getLastIssue() {
		return lastIssue;
	}

	public void setLastIssue(int lastIssue) {
		this.lastIssue = lastIssue;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(billingAddress1, billingAddress2, billingCity, billingCompany, billingCountry, billingEmail,
				billingFirstName, billingLastName, billingPhone, billingPostcode, billingState, depositDate, firstIssue,
				lastIssue, netOrderTotal, notes, orderAttributionReferrer, orderDate, orderId, orderTotal,
				paymentMethodTitle, shippingAddress1, shippingAddress2, shippingCity, shippingCompany, shippingCountry,
				shippingEmail, shippingFirstName, shippingLastName, shippingPhone, shippingPostcode, shippingState);
	}

	@Override
	@DoNotMutate
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
				&& Objects.equals(billingState, other.billingState) && Objects.equals(depositDate, other.depositDate)
				&& firstIssue == other.firstIssue && lastIssue == other.lastIssue
				&& Double.doubleToLongBits(netOrderTotal) == Double.doubleToLongBits(other.netOrderTotal)
				&& Objects.equals(notes, other.notes)
				&& Objects.equals(orderAttributionReferrer, other.orderAttributionReferrer)
				&& Objects.equals(orderDate, other.orderDate) && Objects.equals(orderId, other.orderId)
				&& Double.doubleToLongBits(orderTotal) == Double.doubleToLongBits(other.orderTotal)
				&& Objects.equals(paymentMethodTitle, other.paymentMethodTitle)
				&& Objects.equals(shippingAddress1, other.shippingAddress1)
				&& Objects.equals(shippingAddress2, other.shippingAddress2)
				&& Objects.equals(shippingCity, other.shippingCity)
				&& Objects.equals(shippingCompany, other.shippingCompany)
				&& Objects.equals(shippingCountry, other.shippingCountry)
				&& Objects.equals(shippingEmail, other.shippingEmail)
				&& Objects.equals(shippingFirstName, other.shippingFirstName)
				&& Objects.equals(shippingLastName, other.shippingLastName)
				&& Objects.equals(shippingPhone, other.shippingPhone)
				&& Objects.equals(shippingPostcode, other.shippingPostcode)
				&& Objects.equals(shippingState, other.shippingState);
	}

	@Override
	public String toString() {
		return "Order [orderId=" + orderId + ", orderDate=" + orderDate + ", depositDate=" + depositDate
				+ ", orderTotal=" + orderTotal + ", netOrderTotal=" + netOrderTotal + ", paymentMethodTitle="
				+ paymentMethodTitle + ", billingFirstName=" + billingFirstName + ", billingLastName=" + billingLastName
				+ ", billingCompany=" + billingCompany + ", billingAddress1=" + billingAddress1 + ", billingAddress2="
				+ billingAddress2 + ", billingPostcode=" + billingPostcode + ", billingCity=" + billingCity
				+ ", billingState=" + billingState + ", billingCountry=" + billingCountry + ", billingEmail="
				+ billingEmail + ", billingPhone=" + billingPhone + ", orderAttributionReferrer="
				+ orderAttributionReferrer + ", firstIssue=" + firstIssue + ", lastIssue=" + lastIssue + ", notes="
				+ notes + ", shippingFirstName=" + shippingFirstName + ", shippingLastName=" + shippingLastName
				+ ", shippingCompany=" + shippingCompany + ", shippingEmail=" + shippingEmail + ", shippingPhone="
				+ shippingPhone + ", shippingAddress1=" + shippingAddress1 + ", shippingAddress2=" + shippingAddress2
				+ ", shippingPostcode=" + shippingPostcode + ", shippingCity=" + shippingCity + ", shippingState="
				+ shippingState + ", shippingCountry=" + shippingCountry + "]";
	}

	

}
