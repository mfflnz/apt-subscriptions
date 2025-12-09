package org.blefuscu.apt.subscriptions.view;

import javax.swing.JPanel;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.awt.Dimension;

public class OrderSwingView extends JPanel implements OrderView {

	private static final String WHITESPACES_REGEX = "^\\s*$";
	private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	private static final String EMAIL_REGEX = "^\\S+@\\S+\\.\\S+$";
	private static final String NUMBER_REGEX = "^\\d+$";
	private static final long serialVersionUID = 1L;
	private JTextField orderIdTextBox;
	private JTextField orderDateTextBox;
	private JTextField paidDateTextBox;
	private JLabel lblOrderTotal;
	private JLabel lblNetTotal;
	private JLabel lblPaymentMethod;
	private JTextField orderTotalTextBox;
	private JTextField netTotalTextBox;
	private JTextField paymentMethodTitleTextBox;
	private JLabel lblFirstName;
	private JLabel lblLastName;
	private JLabel lblAddress;
	private JLabel lblPostcode;
	private JLabel lblCity;
	private JLabel lblState;
	private JLabel lblEmail;
	private JLabel lblPhone;
	private JLabel lblProducts;
	private JLabel lblFirstIssue;
	private JLabel lblLastIssue;
	private JLabel lblNotes;
	private JTextField firstNameTextBox;
	private JTextField lastNameTextBox;
	private JTextField addressTextBox;
	private JTextField postcodeTextBox;
	private JTextField stateTextBox;
	private JTextField cityTextBox;
	private JTextField emailTextBox;
	private JTextField phoneTextBox;
	private JTextField productTextBox;
	private JTextField firstIssueTextBox;
	private JTextField lastIssueTextBox;
	private JTextField notesTextBox;
	private JButton btnUpdate;
	private JButton btnDelete;
	private transient SubscriptionsController subscriptionsController;

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

	/**
	 * Create the panel.
	 */
	public OrderSwingView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		JLabel lblOrderId = new JLabel("Order Id");
		GridBagConstraints gbcLblOrderId = new GridBagConstraints();
		gbcLblOrderId.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderId.anchor = GridBagConstraints.EAST;
		gbcLblOrderId.gridx = 0;
		gbcLblOrderId.gridy = 1;
		add(lblOrderId, gbcLblOrderId);

		orderIdTextBox = new JTextField();
		orderIdTextBox.setMinimumSize(new Dimension(200, 21));
		orderIdTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validateRequiredFieldsAndManageButtons();
			}

		});
		orderIdTextBox.setName("orderIdTextBox");
		GridBagConstraints gbcOrderIdTextField = new GridBagConstraints();
		gbcOrderIdTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcOrderIdTextField.insets = new Insets(0, 0, 5, 5);
		gbcOrderIdTextField.gridx = 1;
		gbcOrderIdTextField.gridy = 1;
		add(orderIdTextBox, gbcOrderIdTextField);
		orderIdTextBox.setColumns(10);

		lblOrderTotal = new JLabel("Order Total");
		GridBagConstraints gbcLblOrderTotal = new GridBagConstraints();
		gbcLblOrderTotal.anchor = GridBagConstraints.EAST;
		gbcLblOrderTotal.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderTotal.gridx = 3;
		gbcLblOrderTotal.gridy = 1;
		add(lblOrderTotal, gbcLblOrderTotal);

		orderTotalTextBox = new JTextField();
		orderTotalTextBox.setName("orderTotalTextBox");
		GridBagConstraints gbcOrderTotalTextField = new GridBagConstraints();
		gbcOrderTotalTextField.insets = new Insets(0, 0, 5, 5);
		gbcOrderTotalTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcOrderTotalTextField.gridx = 4;
		gbcOrderTotalTextField.gridy = 1;
		add(orderTotalTextBox, gbcOrderTotalTextField);
		orderTotalTextBox.setColumns(10);

		JLabel lblOrderDate = new JLabel("Order Date");
		GridBagConstraints gbcLblOrderDate = new GridBagConstraints();
		gbcLblOrderDate.anchor = GridBagConstraints.EAST;
		gbcLblOrderDate.insets = new Insets(0, 0, 5, 5);
		gbcLblOrderDate.gridx = 0;
		gbcLblOrderDate.gridy = 2;
		add(lblOrderDate, gbcLblOrderDate);

		orderDateTextBox = new JTextField();
		orderDateTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validateRequiredFieldsAndManageButtons();
			}
		});
		orderDateTextBox.setName("orderDateTextBox");
		GridBagConstraints gbcOrderDateTextField = new GridBagConstraints();
		gbcOrderDateTextField.insets = new Insets(0, 0, 5, 5);
		gbcOrderDateTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcOrderDateTextField.gridx = 1;
		gbcOrderDateTextField.gridy = 2;
		add(orderDateTextBox, gbcOrderDateTextField);
		orderDateTextBox.setColumns(10);

		lblNetTotal = new JLabel("Net Total");
		GridBagConstraints gbcLblNetTotal = new GridBagConstraints();
		gbcLblNetTotal.anchor = GridBagConstraints.EAST;
		gbcLblNetTotal.insets = new Insets(0, 0, 5, 5);
		gbcLblNetTotal.gridx = 3;
		gbcLblNetTotal.gridy = 2;
		add(lblNetTotal, gbcLblNetTotal);

		netTotalTextBox = new JTextField();
		netTotalTextBox.setName("netTotalTextBox");
		GridBagConstraints gbcNetTotalTextField = new GridBagConstraints();
		gbcNetTotalTextField.insets = new Insets(0, 0, 5, 5);
		gbcNetTotalTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcNetTotalTextField.gridx = 4;
		gbcNetTotalTextField.gridy = 2;
		add(netTotalTextBox, gbcNetTotalTextField);
		netTotalTextBox.setColumns(10);

		JLabel lblPaidDate = new JLabel("Paid Date");
		GridBagConstraints gbcLblPaidDate = new GridBagConstraints();
		gbcLblPaidDate.anchor = GridBagConstraints.EAST;
		gbcLblPaidDate.insets = new Insets(0, 0, 5, 5);
		gbcLblPaidDate.gridx = 0;
		gbcLblPaidDate.gridy = 3;
		add(lblPaidDate, gbcLblPaidDate);

		paidDateTextBox = new JTextField();
		paidDateTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validateOptionalFieldsAndManageButtons();
			}


		});
		paidDateTextBox.setName("paidDateTextBox");
		GridBagConstraints gbcPaidDateTextField = new GridBagConstraints();
		gbcPaidDateTextField.insets = new Insets(0, 0, 5, 5);
		gbcPaidDateTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcPaidDateTextField.gridx = 1;
		gbcPaidDateTextField.gridy = 3;
		add(paidDateTextBox, gbcPaidDateTextField);
		paidDateTextBox.setColumns(10);

		lblPaymentMethod = new JLabel("Payment Method");
		GridBagConstraints gbcLblPaymentMethod = new GridBagConstraints();
		gbcLblPaymentMethod.anchor = GridBagConstraints.EAST;
		gbcLblPaymentMethod.insets = new Insets(0, 0, 5, 5);
		gbcLblPaymentMethod.gridx = 3;
		gbcLblPaymentMethod.gridy = 3;
		add(lblPaymentMethod, gbcLblPaymentMethod);

		paymentMethodTitleTextBox = new JTextField();
		paymentMethodTitleTextBox.setName("paymentMethodTextBox");
		GridBagConstraints gbcPaymentMethodTextField = new GridBagConstraints();
		gbcPaymentMethodTextField.insets = new Insets(0, 0, 5, 5);
		gbcPaymentMethodTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcPaymentMethodTextField.gridx = 4;
		gbcPaymentMethodTextField.gridy = 3;
		add(paymentMethodTitleTextBox, gbcPaymentMethodTextField);
		paymentMethodTitleTextBox.setColumns(10);

		lblFirstName = new JLabel("First Name");
		GridBagConstraints gbcLblFirstName = new GridBagConstraints();
		gbcLblFirstName.anchor = GridBagConstraints.EAST;
		gbcLblFirstName.insets = new Insets(0, 0, 5, 5);
		gbcLblFirstName.gridx = 0;
		gbcLblFirstName.gridy = 4;
		add(lblFirstName, gbcLblFirstName);

		firstNameTextBox = new JTextField();
		firstNameTextBox.setName("firstNameTextBox");
		GridBagConstraints gbcFirstNameTextField = new GridBagConstraints();
		gbcFirstNameTextField.gridwidth = 4;
		gbcFirstNameTextField.insets = new Insets(0, 0, 5, 5);
		gbcFirstNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcFirstNameTextField.gridx = 1;
		gbcFirstNameTextField.gridy = 4;
		add(firstNameTextBox, gbcFirstNameTextField);
		firstNameTextBox.setColumns(10);

		lblLastName = new JLabel("Last Name");
		GridBagConstraints gbcLblLastName = new GridBagConstraints();
		gbcLblLastName.anchor = GridBagConstraints.EAST;
		gbcLblLastName.insets = new Insets(0, 0, 5, 5);
		gbcLblLastName.gridx = 0;
		gbcLblLastName.gridy = 5;
		add(lblLastName, gbcLblLastName);

		lastNameTextBox = new JTextField();
		lastNameTextBox.setName("lastNameTextBox");
		GridBagConstraints gbcLastNameTextField = new GridBagConstraints();
		gbcLastNameTextField.gridwidth = 4;
		gbcLastNameTextField.insets = new Insets(0, 0, 5, 5);
		gbcLastNameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcLastNameTextField.gridx = 1;
		gbcLastNameTextField.gridy = 5;
		add(lastNameTextBox, gbcLastNameTextField);
		lastNameTextBox.setColumns(10);

		lblAddress = new JLabel("Address");
		GridBagConstraints gbcLblAddress = new GridBagConstraints();
		gbcLblAddress.anchor = GridBagConstraints.EAST;
		gbcLblAddress.insets = new Insets(0, 0, 5, 5);
		gbcLblAddress.gridx = 0;
		gbcLblAddress.gridy = 6;
		add(lblAddress, gbcLblAddress);

		addressTextBox = new JTextField();
		addressTextBox.setName("addressTextBox");
		GridBagConstraints gbcAddressTextField = new GridBagConstraints();
		gbcAddressTextField.gridwidth = 4;
		gbcAddressTextField.insets = new Insets(0, 0, 5, 5);
		gbcAddressTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcAddressTextField.gridx = 1;
		gbcAddressTextField.gridy = 6;
		add(addressTextBox, gbcAddressTextField);
		addressTextBox.setColumns(10);

		lblPostcode = new JLabel("Postcode");
		GridBagConstraints gbcLblPostcode = new GridBagConstraints();
		gbcLblPostcode.anchor = GridBagConstraints.EAST;
		gbcLblPostcode.insets = new Insets(0, 0, 5, 5);
		gbcLblPostcode.gridx = 0;
		gbcLblPostcode.gridy = 7;
		add(lblPostcode, gbcLblPostcode);

		postcodeTextBox = new JTextField();
		postcodeTextBox.setName("postcodeTextBox");
		GridBagConstraints gbcPostCodeTextField = new GridBagConstraints();
		gbcPostCodeTextField.insets = new Insets(0, 0, 5, 5);
		gbcPostCodeTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcPostCodeTextField.gridx = 1;
		gbcPostCodeTextField.gridy = 7;
		add(postcodeTextBox, gbcPostCodeTextField);
		postcodeTextBox.setColumns(10);

		lblState = new JLabel("State");
		GridBagConstraints gbcLblState = new GridBagConstraints();
		gbcLblState.anchor = GridBagConstraints.EAST;
		gbcLblState.insets = new Insets(0, 0, 5, 5);
		gbcLblState.gridx = 3;
		gbcLblState.gridy = 7;
		add(lblState, gbcLblState);

		stateTextBox = new JTextField();
		stateTextBox.setName("stateTextBox");
		GridBagConstraints gbcStateTextField = new GridBagConstraints();
		gbcStateTextField.insets = new Insets(0, 0, 5, 5);
		gbcStateTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcStateTextField.gridx = 4;
		gbcStateTextField.gridy = 7;
		add(stateTextBox, gbcStateTextField);
		stateTextBox.setColumns(10);

		lblCity = new JLabel("City");
		GridBagConstraints gbcLblCity = new GridBagConstraints();
		gbcLblCity.anchor = GridBagConstraints.EAST;
		gbcLblCity.insets = new Insets(0, 0, 5, 5);
		gbcLblCity.gridx = 0;
		gbcLblCity.gridy = 8;
		add(lblCity, gbcLblCity);

		cityTextBox = new JTextField();
		cityTextBox.setName("cityTextBox");
		GridBagConstraints gbcCityTextField = new GridBagConstraints();
		gbcCityTextField.gridwidth = 4;
		gbcCityTextField.insets = new Insets(0, 0, 5, 5);
		gbcCityTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcCityTextField.gridx = 1;
		gbcCityTextField.gridy = 8;
		add(cityTextBox, gbcCityTextField);
		cityTextBox.setColumns(10);

		lblEmail = new JLabel("Email");
		GridBagConstraints gbcLblEmail = new GridBagConstraints();
		gbcLblEmail.anchor = GridBagConstraints.EAST;
		gbcLblEmail.insets = new Insets(0, 0, 5, 5);
		gbcLblEmail.gridx = 0;
		gbcLblEmail.gridy = 9;
		add(lblEmail, gbcLblEmail);

		emailTextBox = new JTextField();
		emailTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateRequiredFieldsAndManageButtons();
			}
		});
		emailTextBox.setName("emailTextBox");
		GridBagConstraints gbcEmailTextField = new GridBagConstraints();
		gbcEmailTextField.gridwidth = 4;
		gbcEmailTextField.insets = new Insets(0, 0, 5, 5);
		gbcEmailTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcEmailTextField.gridx = 1;
		gbcEmailTextField.gridy = 9;
		add(emailTextBox, gbcEmailTextField);
		emailTextBox.setColumns(10);

		lblPhone = new JLabel("Phone");
		GridBagConstraints gbcLblPhone = new GridBagConstraints();
		gbcLblPhone.anchor = GridBagConstraints.EAST;
		gbcLblPhone.insets = new Insets(0, 0, 5, 5);
		gbcLblPhone.gridx = 0;
		gbcLblPhone.gridy = 10;
		add(lblPhone, gbcLblPhone);

		phoneTextBox = new JTextField();
		phoneTextBox.setName("phoneTextBox");
		GridBagConstraints gbcPhoneTextField = new GridBagConstraints();
		gbcPhoneTextField.gridwidth = 4;
		gbcPhoneTextField.insets = new Insets(0, 0, 5, 5);
		gbcPhoneTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcPhoneTextField.gridx = 1;
		gbcPhoneTextField.gridy = 10;
		add(phoneTextBox, gbcPhoneTextField);
		phoneTextBox.setColumns(10);

		lblProducts = new JLabel("Product");
		GridBagConstraints gbcLblProducts = new GridBagConstraints();
		gbcLblProducts.anchor = GridBagConstraints.EAST;
		gbcLblProducts.insets = new Insets(0, 0, 5, 5);
		gbcLblProducts.gridx = 0;
		gbcLblProducts.gridy = 11;
		add(lblProducts, gbcLblProducts);

		productTextBox = new JTextField();
		productTextBox.setName("productTextBox");
		GridBagConstraints gbcProductTextField = new GridBagConstraints();
		gbcProductTextField.gridwidth = 4;
		gbcProductTextField.insets = new Insets(0, 0, 5, 5);
		gbcProductTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcProductTextField.gridx = 1;
		gbcProductTextField.gridy = 11;
		add(productTextBox, gbcProductTextField);
		productTextBox.setColumns(10);

		lblFirstIssue = new JLabel("First Issue");
		GridBagConstraints gbcLblFirstIssue = new GridBagConstraints();
		gbcLblFirstIssue.anchor = GridBagConstraints.EAST;
		gbcLblFirstIssue.insets = new Insets(0, 0, 5, 5);
		gbcLblFirstIssue.gridx = 0;
		gbcLblFirstIssue.gridy = 12;
		add(lblFirstIssue, gbcLblFirstIssue);

		firstIssueTextBox = new JTextField();
		firstIssueTextBox.setName("firstIssueTextBox");
		GridBagConstraints gbcFirstIssueTextField = new GridBagConstraints();
		gbcFirstIssueTextField.insets = new Insets(0, 0, 5, 5);
		gbcFirstIssueTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcFirstIssueTextField.gridx = 1;
		gbcFirstIssueTextField.gridy = 12;
		add(firstIssueTextBox, gbcFirstIssueTextField);
		firstIssueTextBox.setColumns(10);

		lblLastIssue = new JLabel("Last Issue");
		GridBagConstraints gbcLblLastIssue = new GridBagConstraints();
		gbcLblLastIssue.anchor = GridBagConstraints.EAST;
		gbcLblLastIssue.insets = new Insets(0, 0, 5, 5);
		gbcLblLastIssue.gridx = 3;
		gbcLblLastIssue.gridy = 12;
		add(lblLastIssue, gbcLblLastIssue);

		lastIssueTextBox = new JTextField();
		lastIssueTextBox.setName("lastIssueTextBox");
		GridBagConstraints gbcLastIssueTextField = new GridBagConstraints();
		gbcLastIssueTextField.insets = new Insets(0, 0, 5, 5);
		gbcLastIssueTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcLastIssueTextField.gridx = 4;
		gbcLastIssueTextField.gridy = 12;
		add(lastIssueTextBox, gbcLastIssueTextField);
		lastIssueTextBox.setColumns(10);

		lblNotes = new JLabel("Notes");
		GridBagConstraints gbcLblNotes = new GridBagConstraints();
		gbcLblNotes.anchor = GridBagConstraints.EAST;
		gbcLblNotes.insets = new Insets(0, 0, 5, 5);
		gbcLblNotes.gridx = 0;
		gbcLblNotes.gridy = 13;
		add(lblNotes, gbcLblNotes);

		notesTextBox = new JTextField();
		notesTextBox.setName("notesTextBox");
		GridBagConstraints gbcNotestTextField = new GridBagConstraints();
		gbcNotestTextField.gridwidth = 4;
		gbcNotestTextField.insets = new Insets(0, 0, 5, 5);
		gbcNotestTextField.fill = GridBagConstraints.HORIZONTAL;
		gbcNotestTextField.gridx = 1;
		gbcNotestTextField.gridy = 13;
		add(notesTextBox, gbcNotestTextField);
		notesTextBox.setColumns(10);

		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(e ->  
			subscriptionsController.deleteOrder(Integer.parseInt(orderIdTextBox.getText()))
		);
		btnDelete.setEnabled(false);
		GridBagConstraints gbcBtnDelete = new GridBagConstraints();
		gbcBtnDelete.anchor = GridBagConstraints.EAST;
		gbcBtnDelete.insets = new Insets(0, 0, 5, 5);
		gbcBtnDelete.gridx = 3;
		gbcBtnDelete.gridy = 15;
		add(btnDelete, gbcBtnDelete);
		
		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(e -> {
			
				 int firstIssue = readFirstIssue();
				 int lastIssue = readLastIssue();
				 
				 if(firstIssue > lastIssue) {
					 subscriptionsController.sendErrorMessage("First issue should be lesser than or equal to last issue");
				 } else {
					 
					 double orderTotal = readOrderTotal();
					 double orderNetTotal = readOrderNetTotal();
					 LocalDate paidDate = readPaidDate();
					 
					 subscriptionsController.updateOrder(Integer.parseInt(orderIdTextBox.getText()),
							 new Order.OrderBuilder(Integer.parseInt(orderIdTextBox.getText()),
									 LocalDate.parse(orderDateTextBox.getText()), emailTextBox.getText())
							 .setPaidDate(paidDate).setOrderTotal(orderTotal).setOrderNetTotal(orderNetTotal)
							 .setPaymentMethodTitle(paymentMethodTitleTextBox.getText())
							 .setShippingFirstName(firstNameTextBox.getText())
							 .setShippingLastName(lastNameTextBox.getText())
							 .setShippingAddress1(addressTextBox.getText())
							 .setShippingPostcode(postcodeTextBox.getText())
							 .setShippingState(stateTextBox.getText()).setShippingCity(cityTextBox.getText())
							 .setBillingPhone(phoneTextBox.getText()).setShippingItems(productTextBox.getText())
							 .setFirstIssue(firstIssue).setLastIssue(lastIssue)
							 .setCustomerNote(notesTextBox.getText()).build());
				 }

		});

		
		btnUpdate.setEnabled(false);
		GridBagConstraints gbcBtnUpdate = new GridBagConstraints();
		gbcBtnUpdate.anchor = GridBagConstraints.EAST;
		gbcBtnUpdate.insets = new Insets(0, 0, 5, 5);
		gbcBtnUpdate.gridx = 4;
		gbcBtnUpdate.gridy = 15;
		add(btnUpdate, gbcBtnUpdate);

	}

	private LocalDate readPaidDate() {
		return (paidDateTextBox.getText().isEmpty() ? null
				 : LocalDate.parse(paidDateTextBox.getText()));
	}

	private double readOrderNetTotal() {
		return (netTotalTextBox.getText().isEmpty()) ? 0.0
				 : Double.parseDouble(netTotalTextBox.getText().replace(",", ".").replaceAll("[^0-9.]", ""));
	}

	private double readOrderTotal() {
		// toglie tutti i caratteri che non sono numeri o il punto
		 return (orderTotalTextBox.getText().isEmpty()) ? 0.0
				 : Double.parseDouble(orderTotalTextBox.getText().replace(",", ".").replaceAll("[^0-9.]", ""));
	}

	private int readLastIssue() {
		return (lastIssueTextBox.getText().isEmpty()) ? 0
				 : Integer.parseInt(lastIssueTextBox.getText());
	}

	private int readFirstIssue() {
		return (firstIssueTextBox.getText().isEmpty()) ? 0
				 : Integer.parseInt(firstIssueTextBox.getText());
	}

	@Override
	public void showOrderDetails(Order order) {
		FormattedOrder formattedOrder = subscriptionsController.formatOrder(order);
		
		orderIdTextBox.setText(formattedOrder.getOrderId());
		orderDateTextBox.setText(formattedOrder.getOrderDate());
		paidDateTextBox.setText(formattedOrder.getPaidDate());
		orderTotalTextBox.setText(formattedOrder.getOrderTotal());
		netTotalTextBox.setText(formattedOrder.getOrderNetTotal());
		paymentMethodTitleTextBox.setText(formattedOrder.getPaymentMethodTitle());
		firstNameTextBox.setText(formattedOrder.getShippingFirstName());
		lastNameTextBox.setText(formattedOrder.getShippingLastName());
		addressTextBox.setText(formattedOrder.getShippingAddress1());
		postcodeTextBox.setText(formattedOrder.getShippingPostcode());
		stateTextBox.setText(formattedOrder.getShippingState());
		cityTextBox.setText(formattedOrder.getShippingCity());
		emailTextBox.setText(formattedOrder.getCustomerEmail());
		phoneTextBox.setText(formattedOrder.getBillingPhone());
		productTextBox.setText(formattedOrder.getShippingItems());
		firstIssueTextBox.setText(formattedOrder.getFirstIssue());
		lastIssueTextBox.setText(formattedOrder.getLastIssue());
		notesTextBox.setText(formattedOrder.getCustomerNote());
		
		orderIdTextBox.setEnabled(false);
		
		validateRequiredFieldsAndManageButtons();

	}

	@Override
	public void orderUpdated(int orderId) {
		subscriptionsController.sendInfoMessage("Order n. " + orderId + " has been correctly updated");
	}

	@Override
	public void orderDeleted(int orderId) {
		subscriptionsController.sendInfoMessage("Order n. " + orderId + " has been deleted");

	}

	private boolean validateRequiredFieldsAndManageButtons() {
		if (!orderIdTextBox.getText().matches(WHITESPACES_REGEX) && orderIdTextBox.getText().matches(NUMBER_REGEX)
				&& !orderDateTextBox.getText().matches(WHITESPACES_REGEX)
				&& orderDateTextBox.getText().matches(DATE_REGEX) && !emailTextBox.getText().matches(WHITESPACES_REGEX)
				&& emailTextBox.getText().matches(EMAIL_REGEX)) {
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
			return true;

		} else {
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
			return false;
		}
	}
	
	private boolean validateOptionalFieldsAndManageButtons() {
		if (validateRequiredFieldsAndManageButtons()
				&& (paidDateTextBox.getText().matches(DATE_REGEX)
				|| paidDateTextBox.getText().matches(WHITESPACES_REGEX))) {
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
			return true;
		} else {
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
			return false;
		}
	}
	
	@Override
	public void clearAll() {
		this.getOrderIdTextBox().setText("");
		this.getOrderDateTextBox().setText("");
		this.getPaidDateTextBox().setText("");
		this.getOrderTotalTextBox().setText("");
		this.getNetTotalTextBox().setText("");
		this.getPaymentMethodTextBox().setText("");
		this.getFirstNameTextBox().setText("");
		this.getLastNameTextBox().setText("");
		this.getAddressTextBox().setText("");
		this.getPostcodeTextBox().setText("");
		this.getStateTextBox().setText("");
		this.getCityTextBox().setText("");
		this.getEmailTextBox().setText("");
		this.getPhoneTextBox().setText("");
		this.getProductTextBox().setText("");
		this.getFirstIssueTextBox().setText("");
		this.getLastIssueTextBox().setText("");
		this.getNotesTextBox().setText("");
		btnUpdate.setEnabled(false);
		btnDelete.setEnabled(false);

	}

	public JTextField getOrderIdTextBox() {
		return orderIdTextBox;
	}

	public JTextField getOrderDateTextBox() {
		return orderDateTextBox;
	}

	public JTextField getPaidDateTextBox() {
		return paidDateTextBox;
	}

	public JTextField getOrderTotalTextBox() {
		return orderTotalTextBox;
	}

	public JTextField getNetTotalTextBox() {
		return netTotalTextBox;
	}

	public JTextField getPaymentMethodTextBox() {
		return paymentMethodTitleTextBox;
	}

	public JTextField getFirstNameTextBox() {
		return firstNameTextBox;
	}

	public JTextField getLastNameTextBox() {
		return lastNameTextBox;
	}

	public JTextField getAddressTextBox() {
		return addressTextBox;
	}

	public JTextField getPostcodeTextBox() {
		return postcodeTextBox;
	}

	public JTextField getStateTextBox() {
		return stateTextBox;
	}

	public JTextField getCityTextBox() {
		return cityTextBox;
	}

	public JTextField getEmailTextBox() {
		return emailTextBox;
	}

	public JTextField getPhoneTextBox() {
		return phoneTextBox;
	}

	public JTextField getProductTextBox() {
		return productTextBox;
	}

	public JTextField getFirstIssueTextBox() {
		return firstIssueTextBox;
	}

	public JTextField getLastIssueTextBox() {
		return lastIssueTextBox;
	}

	public JTextField getNotesTextBox() {
		return notesTextBox;
	}

	public JButton getBtnUpdate() {
		return btnUpdate;
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

}
