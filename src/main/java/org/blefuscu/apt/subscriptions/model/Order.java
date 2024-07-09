package org.blefuscu.apt.subscriptions.model;

public class Order {

	private int orderId;
	private String orderDate; // formatted as "yyyy-MM-dd HH:mm:ss"
	private double orderTotal;
	private String paymentMethodTitle; // e.g. "PayPal", "Credit Card", ...
	
	private String billingFirstName;
	private String billingLastName;
	private String billingCompany;
	private String billingEmail;
	private String billingPhone;
	private String billingAddress1;
	private String billingAddress2;
	private String billingPostcode; // five-digit number padded with leading zeros
	private String billingCity;
	private String billingState;
	private String billingCountry;
	
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
	
	private String orderAttributionReferrer; // contains human-readable product name
	
	private String depositDate;
	private double netOrderTotal;
	private int firstIssue;
	private int lastIssue;
	private String notes;
	
	public Order() {
	}
	
	// TODO: constructor with fields
	
	// getters, setters, equals, hashCode, toString...

}
