package org.blefuscu.apt.subscriptions.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.blefuscu.apt.subscriptions.model.Order;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

public class OrderSwingView extends JFrame implements OrderView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JLabel lblOrderDate;
	private JTextField textField_1;
	private JLabel lblCreditDate;
	private JTextField textField_2;
	private JLabel lblGross;
	private JLabel lblNet;
	private JTextField textField_3;
	private JTextField textField_4;
	private JLabel lblPayment;
	private JTextField textField_5;
	private JLabel lblProduct;
	private JTextField textField_6;
	private JLabel lblFirstIssue;
	private JTextField textField_7;
	private JLabel lblLastIssue;
	private JTextField textField_8;
	private JSeparator separator;
	private JLabel lblFirstName;
	private JTextField textField_9;
	private JLabel lblLastName;
	private JTextField textField_10;
	private JLabel lblAddress;
	private JTextField textField_11;
	private JLabel lblZipCode;
	private JTextField textField_12;
	private JLabel lblProvince;
	private JTextField textField_13;
	private JLabel lblCity;
	private JTextField textField_14;
	private JLabel lblCountry;
	private JTextField textField_15;
	private JCheckBox chckbxConfirmed;
	private JLabel lblEmail;
	private JTextField textField_16;
	private JLabel lblPhone;
	private JTextField textField_17;
	private JSeparator separator_1;
	private JLabel lblNotes;
	private JTextArea textArea;


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
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblId = new JLabel("id");
		GridBagConstraints gbc_lblId = new GridBagConstraints();
		gbc_lblId.insets = new Insets(0, 0, 5, 5);
		gbc_lblId.anchor = GridBagConstraints.EAST;
		gbc_lblId.gridx = 0;
		gbc_lblId.gridy = 0;
		contentPane.add(lblId, gbc_lblId);
		
		textField = new JTextField();
		textField.setName("idTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(10);
		
		lblGross = new JLabel("Gross");
		GridBagConstraints gbc_lblGross = new GridBagConstraints();
		gbc_lblGross.anchor = GridBagConstraints.EAST;
		gbc_lblGross.insets = new Insets(0, 0, 5, 5);
		gbc_lblGross.gridx = 3;
		gbc_lblGross.gridy = 0;
		contentPane.add(lblGross, gbc_lblGross);
		
		textField_3 = new JTextField();
		textField_3.setName("grossTextBox");
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 4;
		gbc_textField_3.gridy = 0;
		contentPane.add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		lblOrderDate = new JLabel("Order Date");
		GridBagConstraints gbc_lblOrderDate = new GridBagConstraints();
		gbc_lblOrderDate.anchor = GridBagConstraints.EAST;
		gbc_lblOrderDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblOrderDate.gridx = 0;
		gbc_lblOrderDate.gridy = 1;
		contentPane.add(lblOrderDate, gbc_lblOrderDate);
		
		textField_1 = new JTextField();
		textField_1.setName("orderDateTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		lblNet = new JLabel("Net");
		GridBagConstraints gbc_lblNet = new GridBagConstraints();
		gbc_lblNet.anchor = GridBagConstraints.EAST;
		gbc_lblNet.insets = new Insets(0, 0, 5, 5);
		gbc_lblNet.gridx = 3;
		gbc_lblNet.gridy = 1;
		contentPane.add(lblNet, gbc_lblNet);
		
		textField_4 = new JTextField();
		textField_4.setName("netTextBox");
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 4;
		gbc_textField_4.gridy = 1;
		contentPane.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		lblCreditDate = new JLabel("Credit Date");
		GridBagConstraints gbc_lblCreditDate = new GridBagConstraints();
		gbc_lblCreditDate.anchor = GridBagConstraints.EAST;
		gbc_lblCreditDate.insets = new Insets(0, 0, 5, 5);
		gbc_lblCreditDate.gridx = 0;
		gbc_lblCreditDate.gridy = 2;
		contentPane.add(lblCreditDate, gbc_lblCreditDate);
		
		textField_2 = new JTextField();
		textField_2.setName("creditDateTextBox");
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		contentPane.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		lblPayment = new JLabel("Payment");
		GridBagConstraints gbc_lblPayment = new GridBagConstraints();
		gbc_lblPayment.anchor = GridBagConstraints.EAST;
		gbc_lblPayment.insets = new Insets(0, 0, 5, 5);
		gbc_lblPayment.gridx = 3;
		gbc_lblPayment.gridy = 2;
		contentPane.add(lblPayment, gbc_lblPayment);
		
		textField_5 = new JTextField();
		textField_5.setName("paymentTextBox");
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 4;
		gbc_textField_5.gridy = 2;
		contentPane.add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		lblProduct = new JLabel("Product");
		GridBagConstraints gbc_lblProduct = new GridBagConstraints();
		gbc_lblProduct.anchor = GridBagConstraints.EAST;
		gbc_lblProduct.insets = new Insets(0, 0, 5, 5);
		gbc_lblProduct.gridx = 0;
		gbc_lblProduct.gridy = 3;
		contentPane.add(lblProduct, gbc_lblProduct);
		
		textField_6 = new JTextField();
		textField_6.setName("productTextBox");
		GridBagConstraints gbc_textField_6 = new GridBagConstraints();
		gbc_textField_6.insets = new Insets(0, 0, 5, 0);
		gbc_textField_6.gridwidth = 4;
		gbc_textField_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_6.gridx = 1;
		gbc_textField_6.gridy = 3;
		contentPane.add(textField_6, gbc_textField_6);
		textField_6.setColumns(10);
		
		lblFirstIssue = new JLabel("First Issue");
		GridBagConstraints gbc_lblFirstIssue = new GridBagConstraints();
		gbc_lblFirstIssue.anchor = GridBagConstraints.EAST;
		gbc_lblFirstIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblFirstIssue.gridx = 0;
		gbc_lblFirstIssue.gridy = 4;
		contentPane.add(lblFirstIssue, gbc_lblFirstIssue);
		
		textField_7 = new JTextField();
		textField_7.setName("firstIssueTextBox");
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.insets = new Insets(0, 0, 5, 5);
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.gridx = 1;
		gbc_textField_7.gridy = 4;
		contentPane.add(textField_7, gbc_textField_7);
		textField_7.setColumns(10);
		
		lblLastIssue = new JLabel("Last Issue");
		GridBagConstraints gbc_lblLastIssue = new GridBagConstraints();
		gbc_lblLastIssue.anchor = GridBagConstraints.EAST;
		gbc_lblLastIssue.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastIssue.gridx = 3;
		gbc_lblLastIssue.gridy = 4;
		contentPane.add(lblLastIssue, gbc_lblLastIssue);
		
		textField_8 = new JTextField();
		textField_8.setName("lastIssueTextBox");
		GridBagConstraints gbc_textField_8 = new GridBagConstraints();
		gbc_textField_8.insets = new Insets(0, 0, 5, 0);
		gbc_textField_8.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_8.gridx = 4;
		gbc_textField_8.gridy = 4;
		contentPane.add(textField_8, gbc_textField_8);
		textField_8.setColumns(10);
		
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
		
		textField_9 = new JTextField();
		textField_9.setName("firstNameTextBox");
		GridBagConstraints gbc_textField_9 = new GridBagConstraints();
		gbc_textField_9.gridwidth = 4;
		gbc_textField_9.insets = new Insets(0, 0, 5, 0);
		gbc_textField_9.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_9.gridx = 1;
		gbc_textField_9.gridy = 6;
		contentPane.add(textField_9, gbc_textField_9);
		textField_9.setColumns(10);
		
		lblLastName = new JLabel("Last Name");
		GridBagConstraints gbc_lblLastName = new GridBagConstraints();
		gbc_lblLastName.anchor = GridBagConstraints.EAST;
		gbc_lblLastName.insets = new Insets(0, 0, 5, 5);
		gbc_lblLastName.gridx = 0;
		gbc_lblLastName.gridy = 7;
		contentPane.add(lblLastName, gbc_lblLastName);
		
		textField_10 = new JTextField();
		textField_10.setName("lastNameTextBox");
		GridBagConstraints gbc_textField_10 = new GridBagConstraints();
		gbc_textField_10.insets = new Insets(0, 0, 5, 0);
		gbc_textField_10.gridwidth = 4;
		gbc_textField_10.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_10.gridx = 1;
		gbc_textField_10.gridy = 7;
		contentPane.add(textField_10, gbc_textField_10);
		textField_10.setColumns(10);
		
		lblAddress = new JLabel("Address");
		GridBagConstraints gbc_lblAddress = new GridBagConstraints();
		gbc_lblAddress.anchor = GridBagConstraints.EAST;
		gbc_lblAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblAddress.gridx = 0;
		gbc_lblAddress.gridy = 8;
		contentPane.add(lblAddress, gbc_lblAddress);
		
		textField_11 = new JTextField();
		textField_11.setName("addressTextBox");
		GridBagConstraints gbc_textField_11 = new GridBagConstraints();
		gbc_textField_11.insets = new Insets(0, 0, 5, 0);
		gbc_textField_11.gridwidth = 4;
		gbc_textField_11.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_11.gridx = 1;
		gbc_textField_11.gridy = 8;
		contentPane.add(textField_11, gbc_textField_11);
		textField_11.setColumns(10);
		
		lblCity = new JLabel("City");
		GridBagConstraints gbc_lblCity = new GridBagConstraints();
		gbc_lblCity.anchor = GridBagConstraints.EAST;
		gbc_lblCity.insets = new Insets(0, 0, 5, 5);
		gbc_lblCity.gridx = 0;
		gbc_lblCity.gridy = 9;
		contentPane.add(lblCity, gbc_lblCity);
		
		textField_14 = new JTextField();
		textField_14.setName("cityTextBox");
		GridBagConstraints gbc_textField_14 = new GridBagConstraints();
		gbc_textField_14.gridwidth = 4;
		gbc_textField_14.insets = new Insets(0, 0, 5, 0);
		gbc_textField_14.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_14.gridx = 1;
		gbc_textField_14.gridy = 9;
		contentPane.add(textField_14, gbc_textField_14);
		textField_14.setColumns(10);
		
		lblZipCode = new JLabel("Zip Code");
		GridBagConstraints gbc_lblZipCode = new GridBagConstraints();
		gbc_lblZipCode.anchor = GridBagConstraints.EAST;
		gbc_lblZipCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblZipCode.gridx = 0;
		gbc_lblZipCode.gridy = 10;
		contentPane.add(lblZipCode, gbc_lblZipCode);
		
		textField_12 = new JTextField();
		textField_12.setName("zipCodeTextBox");
		GridBagConstraints gbc_textField_12 = new GridBagConstraints();
		gbc_textField_12.insets = new Insets(0, 0, 5, 5);
		gbc_textField_12.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_12.gridx = 1;
		gbc_textField_12.gridy = 10;
		contentPane.add(textField_12, gbc_textField_12);
		textField_12.setColumns(10);
		
		lblProvince = new JLabel("Province");
		GridBagConstraints gbc_lblProvince = new GridBagConstraints();
		gbc_lblProvince.anchor = GridBagConstraints.EAST;
		gbc_lblProvince.insets = new Insets(0, 0, 5, 5);
		gbc_lblProvince.gridx = 3;
		gbc_lblProvince.gridy = 10;
		contentPane.add(lblProvince, gbc_lblProvince);
		
		textField_13 = new JTextField();
		textField_13.setName("provinceTextBox");
		GridBagConstraints gbc_textField_13 = new GridBagConstraints();
		gbc_textField_13.insets = new Insets(0, 0, 5, 0);
		gbc_textField_13.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_13.gridx = 4;
		gbc_textField_13.gridy = 10;
		contentPane.add(textField_13, gbc_textField_13);
		textField_13.setColumns(10);
		
		lblCountry = new JLabel("Country");
		GridBagConstraints gbc_lblCountry = new GridBagConstraints();
		gbc_lblCountry.anchor = GridBagConstraints.EAST;
		gbc_lblCountry.insets = new Insets(0, 0, 5, 5);
		gbc_lblCountry.gridx = 0;
		gbc_lblCountry.gridy = 11;
		contentPane.add(lblCountry, gbc_lblCountry);
		
		textField_15 = new JTextField();
		textField_15.setName("countryTextBox");
		GridBagConstraints gbc_textField_15 = new GridBagConstraints();
		gbc_textField_15.insets = new Insets(0, 0, 5, 5);
		gbc_textField_15.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_15.gridx = 1;
		gbc_textField_15.gridy = 11;
		contentPane.add(textField_15, gbc_textField_15);
		textField_15.setColumns(10);
		
		chckbxConfirmed = new JCheckBox("Confirmed");
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
		
		textField_16 = new JTextField();
		textField_16.setName("emailTextBox");
		GridBagConstraints gbc_textField_16 = new GridBagConstraints();
		gbc_textField_16.gridwidth = 4;
		gbc_textField_16.insets = new Insets(0, 0, 5, 0);
		gbc_textField_16.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_16.gridx = 1;
		gbc_textField_16.gridy = 12;
		contentPane.add(textField_16, gbc_textField_16);
		textField_16.setColumns(10);
		
		lblPhone = new JLabel("Phone");
		GridBagConstraints gbc_lblPhone = new GridBagConstraints();
		gbc_lblPhone.anchor = GridBagConstraints.EAST;
		gbc_lblPhone.insets = new Insets(0, 0, 5, 5);
		gbc_lblPhone.gridx = 0;
		gbc_lblPhone.gridy = 13;
		contentPane.add(lblPhone, gbc_lblPhone);
		
		textField_17 = new JTextField();
		textField_17.setName("phoneTextBox");
		GridBagConstraints gbc_textField_17 = new GridBagConstraints();
		gbc_textField_17.gridwidth = 4;
		gbc_textField_17.insets = new Insets(0, 0, 5, 0);
		gbc_textField_17.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_17.gridx = 1;
		gbc_textField_17.gridy = 13;
		contentPane.add(textField_17, gbc_textField_17);
		textField_17.setColumns(10);
		
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
		
		textArea = new JTextArea();
		textArea.setName("notesTextArea");
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 5;
		gbc_textArea.insets = new Insets(0, 0, 0, 5);
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 16;
		contentPane.add(textArea, gbc_textArea);

		
		
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
