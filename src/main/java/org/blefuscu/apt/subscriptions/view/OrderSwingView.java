package org.blefuscu.apt.subscriptions.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;

import org.blefuscu.apt.subscriptions.model.Order;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;

public class OrderSwingView extends JFrame implements OrderView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField idTextBox;
	private JLabel lblOrderDate;
	private JTextField orderDateTextBox;
	private JLabel lblCreditDate;
	private JTextField creditDateTextBox;
	private JLabel lblGross;
	private JLabel lblNet;
	private JTextField grossTextBox;
	private JTextField netTextBox;
	private JLabel lblPayment;
	private JTextField paymentTextBox;
	private JLabel lblProduct;
	private JTextField productTextBox;
	private JLabel lblFirstIssue;
	private JTextField firstIssueTextBox;
	private JLabel lblLastIssue;
	private JTextField lastIssueTextBox;
	private JSeparator separator;
	private JLabel lblFirstName;
	private JTextField firstNameTextBox;
	private JLabel lblLastName;
	private JTextField lastNameTextBox;
	private JLabel lblAddress;
	private JTextField addressTextBox;
	private JLabel lblZipCode;
	private JTextField zipCodeTextBox;
	private JLabel lblProvince;
	private JTextField provinceTextBox;
	private JLabel lblCity;
	private JTextField cityTextBox;
	private JLabel lblCountry;
	private JTextField countryTextBox;
	private JCheckBox chckbxConfirmed;
	private JLabel lblEmail;
	private JTextField emailTextBox;
	private JLabel lblPhone;
	private JTextField phoneTextBox;
	private JSeparator separator_1;
	private JLabel lblNotes;
	private JTextArea notesTextArea;
	private JButton btnAdd;
	private JButton btnUpdate;
	private JButton btnDelete;
	private JCheckBox chckbxUnlock;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OrderSwingView frame = new OrderSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public OrderSwingView() {
		setTitle("Order View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);

		idTextBox = new JTextField();
		idTextBox.setEditable(false);
		idTextBox.setName("idTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(idTextBox, gbc_textField);
		idTextBox.setColumns(10);

		lblGross = new JLabel("Gross");
		GridBagConstraints gbc_lblGross = new GridBagConstraints();
		gbc_lblGross.anchor = GridBagConstraints.EAST;
		gbc_lblGross.insets = new Insets(0, 0, 5, 5);
		gbc_lblGross.gridx = 3;
		gbc_lblGross.gridy = 0;
		contentPane.add(lblGross, gbc_lblGross);

		grossTextBox = new JTextField();
		grossTextBox.setEditable(false);
		grossTextBox.setName("grossTextBox");
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 4;
		gbc_textField_3.gridy = 0;
		contentPane.add(grossTextBox, gbc_textField_3);
		grossTextBox.setColumns(10);

		lblOrderDate = new JLabel("Order Date");
		GridBagConstraints gbc_lblOrderDate = new GridBagConstraints();
		gbc_lblOrderDate.anchor = GridBagConstraints.EAST;
		gbc_lblOrderDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderDate.gridx = 0;
		gbc_lblOrderDate.gridy = 1;
		contentPane.add(lblOrderDate, gbc_lblOrderDate);

		orderDateTextBox = new JTextField();
		orderDateTextBox.setEditable(false);
		orderDateTextBox.setName("orderDateTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(orderDateTextBox, gbc_textField_1);
		orderDateTextBox.setColumns(10);

		lblNet = new JLabel("Net");
		GridBagConstraints gbc_lblNet = new GridBagConstraints();
		gbc_lblNet.anchor = GridBagConstraints.EAST;
		gbc_lblNet.insets = new Insets(0, 0, 5, 5);
		gbc_lblNet.gridx = 3;
		gbc_lblNet.gridy = 1;
		contentPane.add(lblNet, gbc_lblNet);

		netTextBox = new JTextField();
		netTextBox.setEditable(false);
		netTextBox.setName("netTextBox");
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 4;
		gbc_textField_4.gridy = 1;
		contentPane.add(netTextBox, gbc_textField_4);
		netTextBox.setColumns(10);

		lblCreditDate = new JLabel("Credit Date");
		GridBagConstraints gbc_lblCreditDate = new GridBagConstraints();
		gbc_lblCreditDate.anchor = GridBagConstraints.EAST;
		gbc_lblCreditDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblCreditDate.gridx = 0;
		gbc_lblCreditDate.gridy = 2;
		contentPane.add(lblCreditDate, gbc_lblCreditDate);

		creditDateTextBox = new JTextField();
		creditDateTextBox.setEditable(false);
		creditDateTextBox.setName("creditDateTextBox");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		contentPane.add(creditDateTextBox, gbc_textField_2);
		creditDateTextBox.setColumns(10);

		lblPayment = new JLabel("Payment");
		GridBagConstraints gbc_lblPayment = new GridBagConstraints();
		gbc_lblPayment.anchor = GridBagConstraints.EAST;
		gbc_lblPayment.insets = new Insets(0, 0, 5, 5);
		gbc_lblPayment.gridx = 3;
		gbc_lblPayment.gridy = 2;
		contentPane.add(lblPayment, gbc_lblPayment);

		paymentTextBox = new JTextField();
		paymentTextBox.setEditable(false);
		paymentTextBox.setName("paymentTextBox");
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 4;
		gbc_textField_5.gridy = 2;
		contentPane.add(paymentTextBox, gbc_textField_5);
		paymentTextBox.setColumns(10);

		lblProduct = new JLabel("Product");
		GridBagConstraints gbc_lblProduct = new GridBagConstraints();
		gbc_lblProduct.anchor = GridBagConstraints.EAST;
		gbc_lblProduct.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduct.gridx = 0;
		gbc_lblProduct.gridy = 3;
		contentPane.add(lblProduct, gbc_lblProduct);

		productTextBox = new JTextField();
		productTextBox.setEditable(false);
		productTextBox.setName("productTextBox");
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 0);
		gbc_textField_6.gridwidth = 4;
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 3;
		contentPane.add(productTextBox, gbc_textField_6);
		productTextBox.setColumns(10);

		lblFirstIssue = new JLabel("First Issue");
		GridBagConstraints gbc_lblFirstIssue = new GridBagConstraints();
		gbc_lblFirstIssue.anchor = GridBagConstraints.EAST;
		gbc_lblFirstIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstIssue.gridx = 0;
		gbc_lblFirstIssue.gridy = 4;
		contentPane.add(lblFirstIssue, gbc_lblFirstIssue);

		firstIssueTextBox = new JTextField();
		firstIssueTextBox.setEditable(false);
		firstIssueTextBox.setName("firstIssueTextBox");
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 5);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 1;
		gbc_textField_7.gridy = 4;
		contentPane.add(firstIssueTextBox, gbc_textField_7);
		firstIssueTextBox.setColumns(10);

		lblLastIssue = new JLabel("Last Issue");
		GridBagConstraints gbc_lblLastIssue = new GridBagConstraints();
		gbc_lblLastIssue.anchor = GridBagConstraints.EAST;
		gbc_lblLastIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastIssue.gridx = 3;
		gbc_lblLastIssue.gridy = 4;
		contentPane.add(lblLastIssue, gbc_lblLastIssue);

		lastIssueTextBox = new JTextField();
		lastIssueTextBox.setEditable(false);
		lastIssueTextBox.setName("lastIssueTextBox");
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 0);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 4;
		gbc_textField_8.gridy = 4;
		contentPane.add(lastIssueTextBox, gbc_textField_8);
		lastIssueTextBox.setColumns(10);

		separator = new JSeparator();
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 5;
		contentPane.add(separator, gbc_separator);

		lblFirstName = new JLabel("First Name");
		GridBagConstraints gbc_lblFirstName = new GridBagConstraints();
		gbc_lblFirstName.anchor = GridBagConstraints.EAST;
		gbc_lblFirstName.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstName.gridx = 0;
		gbc_lblFirstName.gridy = 6;
		contentPane.add(lblFirstName, gbc_lblFirstName);

		firstNameTextBox = new JTextField();
		firstNameTextBox.setEditable(false);
		firstNameTextBox.setName("firstNameTextBox");
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.gridwidth = 4;
		gbc_textField_9.insets = new Insets(0, 0, 5, 0);
		gbc_textField_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_9.gridx = 1;
		gbc_textField_9.gridy = 6;
		contentPane.add(firstNameTextBox, gbc_textField_9);
		firstNameTextBox.setColumns(10);

		lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 7;
		contentPane.add(lblLastName, gbc_lblLastName);

		lastNameTextBox = new JTextField();
		lastNameTextBox.setEditable(false);
		lastNameTextBox.setName("lastNameTextBox");
		GridBagConstraints gbc_textField_10 = new GridBagConstraints();
		gbc_textField_10.insets = new Insets(0, 0, 5, 0);
		gbc_textField_10.gridwidth = 4;
		gbc_textField_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_10.gridx = 1;
		gbc_textField_10.gridy = 7;
		contentPane.add(lastNameTextBox, gbc_textField_10);
		lastNameTextBox.setColumns(10);

		lblAddress = new JLabel("Address");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 8;
		contentPane.add(lblAddress, gbc_lblAddress);

		addressTextBox = new JTextField();
		addressTextBox.setEditable(false);
		addressTextBox.setName("addressTextBox");
		GridBagConstraints gbc_textField_11 = new GridBagConstraints();
		gbc_textField_11.insets = new Insets(0, 0, 5, 0);
		gbc_textField_11.gridwidth = 4;
		gbc_textField_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_11.gridx = 1;
		gbc_textField_11.gridy = 8;
		contentPane.add(addressTextBox, gbc_textField_11);
		addressTextBox.setColumns(10);

		lblCity = new JLabel("City");
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.anchor = GridBagConstraints.EAST;
		gbc_lblCity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCity.gridx = 0;
		gbc_lblCity.gridy = 9;
		contentPane.add(lblCity, gbc_lblCity);

		cityTextBox = new JTextField();
		cityTextBox.setEditable(false);
		cityTextBox.setName("cityTextBox");
		GridBagConstraints gbc_textField_14 = new GridBagConstraints();
		gbc_textField_14.gridwidth = 4;
		gbc_textField_14.insets = new Insets(0, 0, 5, 0);
		gbc_textField_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_14.gridx = 1;
		gbc_textField_14.gridy = 9;
		contentPane.add(cityTextBox, gbc_textField_14);
		cityTextBox.setColumns(10);

		lblZipCode = new JLabel("Zip Code");
		GridBagConstraints gbc_lblZipCode = new GridBagConstraints();
		gbc_lblZipCode.anchor = GridBagConstraints.EAST;
		gbc_lblZipCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblZipCode.gridx = 0;
		gbc_lblZipCode.gridy = 10;
		contentPane.add(lblZipCode, gbc_lblZipCode);

		zipCodeTextBox = new JTextField();
		zipCodeTextBox.setEditable(false);
		zipCodeTextBox.setName("zipCodeTextBox");
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.insets = new Insets(0, 0, 5, 5);
		gbc_textField_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_12.gridx = 1;
		gbc_textField_12.gridy = 10;
		contentPane.add(zipCodeTextBox, gbc_textField_12);
		zipCodeTextBox.setColumns(10);

		lblProvince = new JLabel("Province");
		GridBagConstraints gbc_lblProvince = new GridBagConstraints();
		gbc_lblProvince.anchor = GridBagConstraints.EAST;
		gbc_lblProvince.insets = new Insets(0, 0, 5, 5);
		gbc_lblProvince.gridx = 3;
		gbc_lblProvince.gridy = 10;
		contentPane.add(lblProvince, gbc_lblProvince);

		provinceTextBox = new JTextField();
		provinceTextBox.setEditable(false);
		provinceTextBox.setName("provinceTextBox");
		GridBagConstraints gbc_textField_13 = new GridBagConstraints();
		gbc_textField_13.insets = new Insets(0, 0, 5, 0);
		gbc_textField_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_13.gridx = 4;
		gbc_textField_13.gridy = 10;
		contentPane.add(provinceTextBox, gbc_textField_13);
		provinceTextBox.setColumns(10);

		lblCountry = new JLabel("Country");
		GridBagConstraints gbc_lblCountry = new GridBagConstraints();
		gbc_lblCountry.anchor = GridBagConstraints.EAST;
		gbc_lblCountry.insets = new Insets(0, 0, 5, 5);
		gbc_lblCountry.gridx = 0;
		gbc_lblCountry.gridy = 11;
		contentPane.add(lblCountry, gbc_lblCountry);

		countryTextBox = new JTextField();
		countryTextBox.setEditable(false);
		countryTextBox.setName("countryTextBox");
		GridBagConstraints gbc_textField_15 = new GridBagConstraints();
		gbc_textField_15.insets = new Insets(0, 0, 5, 5);
		gbc_textField_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_15.gridx = 1;
		gbc_textField_15.gridy = 11;
		contentPane.add(countryTextBox, gbc_textField_15);
		countryTextBox.setColumns(10);

		chckbxConfirmed = new JCheckBox("Confirmed");
		chckbxConfirmed.setName("confirmedCheckBox");
		chckbxConfirmed.setHorizontalTextPosition(SwingConstants.LEADING);
		GridBagConstraints gbc_chckbxConfirmed = new GridBagConstraints();
		gbc_chckbxConfirmed.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxConfirmed.anchor = GridBagConstraints.EAST;
		gbc_chckbxConfirmed.gridx = 4;
		gbc_chckbxConfirmed.gridy = 11;
		contentPane.add(chckbxConfirmed, gbc_chckbxConfirmed);

		lblEmail = new JLabel("Email");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.anchor = GridBagConstraints.EAST;
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 0;
		gbc_lblEmail.gridy = 12;
		contentPane.add(lblEmail, gbc_lblEmail);

		emailTextBox = new JTextField();
		emailTextBox.setEditable(false);
		emailTextBox.setName("emailTextBox");
		GridBagConstraints gbc_textField_16 = new GridBagConstraints();
		gbc_textField_16.gridwidth = 4;
		gbc_textField_16.insets = new Insets(0, 0, 5, 0);
		gbc_textField_16.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_16.gridx = 1;
		gbc_textField_16.gridy = 12;
		contentPane.add(emailTextBox, gbc_textField_16);
		emailTextBox.setColumns(10);

		lblPhone = new JLabel("Phone");
		GridBagConstraints gbc_lblPhone = new GridBagConstraints();
		gbc_lblPhone.anchor = GridBagConstraints.EAST;
		gbc_lblPhone.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhone.gridx = 0;
		gbc_lblPhone.gridy = 13;
		contentPane.add(lblPhone, gbc_lblPhone);

		phoneTextBox = new JTextField();
		phoneTextBox.setEditable(false);
		phoneTextBox.setName("phoneTextBox");
		GridBagConstraints gbc_textField_17 = new GridBagConstraints();
		gbc_textField_17.gridwidth = 4;
		gbc_textField_17.insets = new Insets(0, 0, 5, 0);
		gbc_textField_17.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_17.gridx = 1;
		gbc_textField_17.gridy = 13;
		contentPane.add(phoneTextBox, gbc_textField_17);
		phoneTextBox.setColumns(10);

		separator_1 = new JSeparator();
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.gridwidth = 5;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 14;
		contentPane.add(separator_1, gbc_separator_1);

		lblNotes = new JLabel("Notes");
		GridBagConstraints gbc_lblNotes = new GridBagConstraints();
		gbc_lblNotes.anchor = GridBagConstraints.WEST;
		gbc_lblNotes.insets = new Insets(0, 0, 5, 5);
		gbc_lblNotes.gridx = 0;
		gbc_lblNotes.gridy = 15;
		contentPane.add(lblNotes, gbc_lblNotes);

		ArrayList<Boolean> mandatoryValues = new ArrayList<>(); 
		
		chckbxUnlock = new JCheckBox("Unlock");
		chckbxUnlock.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
							 
				if (mandatoryValuesCheck(mandatoryValues)) {
					btnAdd.setEnabled(true);
					btnDelete.setEnabled(true);
					btnUpdate.setEnabled(true);
				} else {
					btnAdd.setEnabled(false);
					btnDelete.setEnabled(false);
					btnUpdate.setEnabled(false);
				}
			}

			private boolean mandatoryValuesCheck(ArrayList<Boolean> mandatoryValues) {
				mandatoryValues.add(!idTextBox.getText().isEmpty());
				mandatoryValues.add(!orderDateTextBox.getText().isEmpty());
				mandatoryValues.add(!productTextBox.getText().isEmpty());
				mandatoryValues.add(!grossTextBox.getText().isEmpty());
				mandatoryValues.add(!paymentTextBox.getText().isEmpty());
				mandatoryValues.add(!emailTextBox.getText().isEmpty());
				mandatoryValues.add(chckbxUnlock.isEnabled());
				
				boolean mandatoryValuesCheck = mandatoryValues.stream().allMatch(t -> t.equals(true));
				return mandatoryValuesCheck;
			}
		});
		chckbxUnlock.setName("unlockCheckBox");
		chckbxUnlock.setHorizontalTextPosition(SwingConstants.LEADING);
		GridBagConstraints gbc_chckbxUnlock = new GridBagConstraints();
		gbc_chckbxUnlock.anchor = GridBagConstraints.EAST;
		gbc_chckbxUnlock.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxUnlock.gridx = 4;
		gbc_chckbxUnlock.gridy = 15;
		contentPane.add(chckbxUnlock, gbc_chckbxUnlock);

		notesTextArea = new JTextArea();
		notesTextArea.setEditable(false);
		notesTextArea.setRows(5);
		notesTextArea.setName("notesTextArea");
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.insets = new Insets(0, 0, 5, 0);
		gbc_textArea.gridwidth = 5;
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 16;
		contentPane.add(notesTextArea, gbc_textArea);

		btnAdd = new JButton("Add");
		btnAdd.setEnabled(false);
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.anchor = GridBagConstraints.WEST;
		gbc_btnAdd.insets = new Insets(0, 0, 0, 5);
		gbc_btnAdd.gridx = 0;
		gbc_btnAdd.gridy = 17;
		contentPane.add(btnAdd, gbc_btnAdd);

		btnUpdate = new JButton("Update");
		btnUpdate.setEnabled(false);
		GridBagConstraints gbc_btnUpdate = new GridBagConstraints();
		gbc_btnUpdate.anchor = GridBagConstraints.WEST;
		gbc_btnUpdate.insets = new Insets(0, 0, 0, 5);
		gbc_btnUpdate.gridx = 1;
		gbc_btnUpdate.gridy = 17;
		contentPane.add(btnUpdate, gbc_btnUpdate);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.anchor = GridBagConstraints.EAST;
		gbc_btnDelete.gridx = 4;
		gbc_btnDelete.gridy = 17;
		contentPane.add(btnDelete, gbc_btnDelete);

		// TODO: gestione listener sul pulsante di sblocco
		// chckbxUnlock.addChangeListener(l);

	}

	@Override
	public void showOrderDetails(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderAdded(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderRemoved(Order orderToDelete) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showError(String string, Order existingOrder) {
		// TODO Auto-generated method stub

	}

}