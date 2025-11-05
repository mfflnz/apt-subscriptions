package org.blefuscu.apt.subscriptions.view;

import javax.swing.JPanel;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;

public class OrderSwingView extends JPanel implements OrderView {

	private static final String WHITESPACES_REGEX = "^\\s*$";
	private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
	private static final String EMAIL_REGEX = "^\\S+@\\S+\\.\\S+$";
	private static final String NUMBER_REGEX = "^\\d+$";
	private static final long serialVersionUID = 1L;
	private JTextField orderIdTextBox;
	private JTextField orderDateTextBox;
	private JTextField textField_2;
	private JLabel lblOrderTotal;
	private JLabel lblNetTotal;
	private JLabel lblPaymentMethod;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
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
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField emailTextBox;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTextField textField_17;
	private JButton btnUpdate;
	private JButton btnDelete;
	private SubscriptionsController subscriptionsController;

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
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		JLabel lblOrderId = new JLabel("Order Id");
		GridBagConstraints gbc_lblOrderId = new GridBagConstraints();
		gbc_lblOrderId.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderId.anchor = GridBagConstraints.EAST;
		gbc_lblOrderId.gridx = 0;
		gbc_lblOrderId.gridy = 1;
		add(lblOrderId, gbc_lblOrderId);

		orderIdTextBox = new JTextField();
		orderIdTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validateRequiredFieldsAndManageButtons();
			}

		});
		orderIdTextBox.setName("orderIdTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		add(orderIdTextBox, gbc_textField);
		orderIdTextBox.setColumns(10);

		lblOrderTotal = new JLabel("Order Total");
		GridBagConstraints gbc_lblOrderTotal = new GridBagConstraints();
		gbc_lblOrderTotal.anchor = GridBagConstraints.EAST;
		gbc_lblOrderTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderTotal.gridx = 3;
		gbc_lblOrderTotal.gridy = 1;
		add(lblOrderTotal, gbc_lblOrderTotal);

		textField_3 = new JTextField();
		textField_3.setName("orderTotalTextBox");
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 4;
		gbc_textField_3.gridy = 1;
		add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);

		JLabel lblOrderDate = new JLabel("Order Date");
		GridBagConstraints gbc_lblOrderDate = new GridBagConstraints();
		gbc_lblOrderDate.anchor = GridBagConstraints.EAST;
		gbc_lblOrderDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderDate.gridx = 0;
		gbc_lblOrderDate.gridy = 2;
		add(lblOrderDate, gbc_lblOrderDate);

		orderDateTextBox = new JTextField();
		orderDateTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				validateRequiredFieldsAndManageButtons();
			}
		});
		orderDateTextBox.setName("orderDateTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		add(orderDateTextBox, gbc_textField_1);
		orderDateTextBox.setColumns(10);

		lblNetTotal = new JLabel("Net Total");
		GridBagConstraints gbc_lblNetTotal = new GridBagConstraints();
		gbc_lblNetTotal.anchor = GridBagConstraints.EAST;
		gbc_lblNetTotal.insets = new Insets(0, 0, 5, 5);
		gbc_lblNetTotal.gridx = 3;
		gbc_lblNetTotal.gridy = 2;
		add(lblNetTotal, gbc_lblNetTotal);

		textField_4 = new JTextField();
		textField_4.setName("netTotalTextBox");
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 5);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 4;
		gbc_textField_4.gridy = 2;
		add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);

		JLabel lblPaidDate = new JLabel("Paid Date");
		GridBagConstraints gbc_lblPaidDate = new GridBagConstraints();
		gbc_lblPaidDate.anchor = GridBagConstraints.EAST;
		gbc_lblPaidDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaidDate.gridx = 0;
		gbc_lblPaidDate.gridy = 3;
		add(lblPaidDate, gbc_lblPaidDate);

		textField_2 = new JTextField();
		textField_2.setName("paidDateTextBox");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 3;
		add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);

		lblPaymentMethod = new JLabel("Payment Method");
		GridBagConstraints gbc_lblPaymentMethod = new GridBagConstraints();
		gbc_lblPaymentMethod.anchor = GridBagConstraints.EAST;
		gbc_lblPaymentMethod.insets = new Insets(0, 0, 5, 5);
		gbc_lblPaymentMethod.gridx = 3;
		gbc_lblPaymentMethod.gridy = 3;
		add(lblPaymentMethod, gbc_lblPaymentMethod);

		textField_5 = new JTextField();
		textField_5.setName("paymentMethodTextBox");
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 5);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 4;
		gbc_textField_5.gridy = 3;
		add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);

		lblFirstName = new JLabel("First Name");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.EAST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 0;
		gbc_lblFirstName.gridy = 4;
		add(lblFirstName, gbc_lblFirstName);

		textField_6 = new JTextField();
		textField_6.setName("firstNameTextBox");
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.gridwidth = 4;
		gbc_textField_6.insets = new Insets(0, 0, 5, 5);
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 4;
		add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);

		lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 5;
		add(lblLastName, gbc_lblLastName);

		textField_7 = new JTextField();
		textField_7.setName("lastNameTextBox");
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.gridwidth = 4;
		gbc_textField_7.insets = new Insets(0, 0, 5, 5);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 1;
		gbc_textField_7.gridy = 5;
		add(textField_7, gbc_textField_7);
		textField_7.setColumns(10);

		lblAddress = new JLabel("Address");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 6;
		add(lblAddress, gbc_lblAddress);

		textField_8 = new JTextField();
		textField_8.setName("addressTextBox");
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.gridwidth = 4;
		gbc_textField_8.insets = new Insets(0, 0, 5, 5);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 1;
		gbc_textField_8.gridy = 6;
		add(textField_8, gbc_textField_8);
		textField_8.setColumns(10);

		lblPostcode = new JLabel("Postcode");
		GridBagConstraints gbc_lblPostcode = new GridBagConstraints();
		gbc_lblPostcode.anchor = GridBagConstraints.EAST;
		gbc_lblPostcode.insets = new Insets(0, 0, 5, 5);
		gbc_lblPostcode.gridx = 0;
		gbc_lblPostcode.gridy = 7;
		add(lblPostcode, gbc_lblPostcode);

		textField_9 = new JTextField();
		textField_9.setName("postcodeTextBox");
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.insets = new Insets(0, 0, 5, 5);
		gbc_textField_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_9.gridx = 1;
		gbc_textField_9.gridy = 7;
		add(textField_9, gbc_textField_9);
		textField_9.setColumns(10);

		lblState = new JLabel("State");
		GridBagConstraints gbc_lblState = new GridBagConstraints();
		gbc_lblState.anchor = GridBagConstraints.EAST;
		gbc_lblState.insets = new Insets(0, 0, 5, 5);
		gbc_lblState.gridx = 3;
		gbc_lblState.gridy = 7;
		add(lblState, gbc_lblState);

		textField_10 = new JTextField();
		textField_10.setName("stateTextBox");
		GridBagConstraints gbc_textField_10 = new GridBagConstraints();
		gbc_textField_10.insets = new Insets(0, 0, 5, 5);
		gbc_textField_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_10.gridx = 4;
		gbc_textField_10.gridy = 7;
		add(textField_10, gbc_textField_10);
		textField_10.setColumns(10);

		lblCity = new JLabel("City");
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.anchor = GridBagConstraints.EAST;
		gbc_lblCity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCity.gridx = 0;
		gbc_lblCity.gridy = 8;
		add(lblCity, gbc_lblCity);

		textField_11 = new JTextField();
		textField_11.setName("cityTextBox");
		GridBagConstraints gbc_textField_11 = new GridBagConstraints();
		gbc_textField_11.gridwidth = 4;
		gbc_textField_11.insets = new Insets(0, 0, 5, 5);
		gbc_textField_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_11.gridx = 1;
		gbc_textField_11.gridy = 8;
		add(textField_11, gbc_textField_11);
		textField_11.setColumns(10);

		lblEmail = new JLabel("Email");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 9;
		add(lblEmail, gbc_lblEmail);

		emailTextBox = new JTextField();
		emailTextBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				validateRequiredFieldsAndManageButtons();
			}
		});
		emailTextBox.setName("emailTextBox");
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.gridwidth = 4;
		gbc_textField_12.insets = new Insets(0, 0, 5, 5);
		gbc_textField_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_12.gridx = 1;
		gbc_textField_12.gridy = 9;
		add(emailTextBox, gbc_textField_12);
		emailTextBox.setColumns(10);

		lblPhone = new JLabel("Phone");
		GridBagConstraints gbc_lblPhone = new GridBagConstraints();
		gbc_lblPhone.anchor = GridBagConstraints.EAST;
		gbc_lblPhone.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhone.gridx = 0;
		gbc_lblPhone.gridy = 10;
		add(lblPhone, gbc_lblPhone);

		textField_13 = new JTextField();
		textField_13.setName("phoneTextBox");
		GridBagConstraints gbc_textField_13 = new GridBagConstraints();
		gbc_textField_13.gridwidth = 4;
		gbc_textField_13.insets = new Insets(0, 0, 5, 5);
		gbc_textField_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_13.gridx = 1;
		gbc_textField_13.gridy = 10;
		add(textField_13, gbc_textField_13);
		textField_13.setColumns(10);

		lblProducts = new JLabel("Product");
		GridBagConstraints gbc_lblProducts = new GridBagConstraints();
		gbc_lblProducts.anchor = GridBagConstraints.EAST;
		gbc_lblProducts.insets = new Insets(0, 0, 5, 5);
		gbc_lblProducts.gridx = 0;
		gbc_lblProducts.gridy = 11;
		add(lblProducts, gbc_lblProducts);

		textField_14 = new JTextField();
		textField_14.setName("productTextBox");
		GridBagConstraints gbc_textField_14 = new GridBagConstraints();
		gbc_textField_14.gridwidth = 4;
		gbc_textField_14.insets = new Insets(0, 0, 5, 5);
		gbc_textField_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_14.gridx = 1;
		gbc_textField_14.gridy = 11;
		add(textField_14, gbc_textField_14);
		textField_14.setColumns(10);

		lblFirstIssue = new JLabel("First Issue");
		GridBagConstraints gbc_lblFirstIssue = new GridBagConstraints();
		gbc_lblFirstIssue.anchor = GridBagConstraints.EAST;
		gbc_lblFirstIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstIssue.gridx = 0;
		gbc_lblFirstIssue.gridy = 12;
		add(lblFirstIssue, gbc_lblFirstIssue);

		textField_15 = new JTextField();
		textField_15.setName("firstIssueTextBox");
		GridBagConstraints gbc_textField_15 = new GridBagConstraints();
		gbc_textField_15.insets = new Insets(0, 0, 5, 5);
		gbc_textField_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_15.gridx = 1;
		gbc_textField_15.gridy = 12;
		add(textField_15, gbc_textField_15);
		textField_15.setColumns(10);

		lblLastIssue = new JLabel("Last Issue");
		GridBagConstraints gbc_lblLastIssue = new GridBagConstraints();
		gbc_lblLastIssue.anchor = GridBagConstraints.EAST;
		gbc_lblLastIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastIssue.gridx = 3;
		gbc_lblLastIssue.gridy = 12;
		add(lblLastIssue, gbc_lblLastIssue);

		textField_16 = new JTextField();
		textField_16.setName("lastIssueTextBox");
		GridBagConstraints gbc_textField_16 = new GridBagConstraints();
		gbc_textField_16.insets = new Insets(0, 0, 5, 5);
		gbc_textField_16.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_16.gridx = 4;
		gbc_textField_16.gridy = 12;
		add(textField_16, gbc_textField_16);
		textField_16.setColumns(10);

		lblNotes = new JLabel("Notes");
		GridBagConstraints gbc_lblNotes = new GridBagConstraints();
		gbc_lblNotes.anchor = GridBagConstraints.EAST;
		gbc_lblNotes.insets = new Insets(0, 0, 5, 5);
		gbc_lblNotes.gridx = 0;
		gbc_lblNotes.gridy = 13;
		add(lblNotes, gbc_lblNotes);

		textField_17 = new JTextField();
		textField_17.setName("notesTextBox");
		GridBagConstraints gbc_textField_17 = new GridBagConstraints();
		gbc_textField_17.gridwidth = 4;
		gbc_textField_17.insets = new Insets(0, 0, 5, 5);
		gbc_textField_17.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_17.gridx = 1;
		gbc_textField_17.gridy = 13;
		add(textField_17, gbc_textField_17);
		textField_17.setColumns(10);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				subscriptionsController.updateOrder(4,
						new Order.OrderBuilder(Integer.parseInt(orderIdTextBox.getText()), LocalDate.parse(orderDateTextBox.getText()), emailTextBox.getText()).build());
			}
		});
		btnUpdate.setEnabled(false);
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdate.gridx = 0;
		gbc_btnUpdate.gridy = 15;
		add(btnUpdate, gbc_btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 3;
		gbc_btnDelete.gridy = 15;
		add(btnDelete, gbc_btnDelete);

	}

	@Override
	public void showOrderDetails(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderUpdated(int orderId, Order orderToUpdate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderDeleted(Order orderToDelete) {
		// TODO Auto-generated method stub

	}

	private void validateRequiredFieldsAndManageButtons() {
		if (!orderIdTextBox.getText().matches(WHITESPACES_REGEX) && orderIdTextBox.getText().matches(NUMBER_REGEX)
				&& !orderDateTextBox.getText().matches(WHITESPACES_REGEX)
				&& orderDateTextBox.getText().matches(DATE_REGEX) && !emailTextBox.getText().matches(WHITESPACES_REGEX)
				&& emailTextBox.getText().matches(EMAIL_REGEX)) {
			btnUpdate.setEnabled(true);
			btnDelete.setEnabled(true);
		} else {
			btnUpdate.setEnabled(false);
			btnDelete.setEnabled(false);
		}
	}

}
