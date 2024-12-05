package org.blefuscu.apt.subscriptions.view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;

//import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
//import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
//import net.sourceforge.jdatepicker.impl.UtilDateModel;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;

public class SearchSwingView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField fromTextBox;
	private JTextField toTextBox;

	private ListSwingView listSwingView = new ListSwingView();
	private SubscriptionsController subscriptionsController;

	private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchSwingView frame = new SearchSwingView();
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
	public SearchSwingView() {
		setTitle("Search by date");
		setName("searchSwingView");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblFrom = new JLabel("From");
		GridBagConstraints gbc_lblFrom = new GridBagConstraints();
		gbc_lblFrom.insets = new Insets(0, 0, 5, 5);
		gbc_lblFrom.anchor = GridBagConstraints.EAST;
		gbc_lblFrom.gridx = 0;
		gbc_lblFrom.gridy = 0;
		contentPane.add(lblFrom, gbc_lblFrom);

		fromTextBox = new JTextField();
		fromTextBox.setName("fromTextBox");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		contentPane.add(fromTextBox, gbc_textField);
		fromTextBox.setColumns(10);
		fromTextBox.setText(LocalDate.now().toString());

		JLabel lblTo = new JLabel("To");
		GridBagConstraints gbc_lblTo = new GridBagConstraints();
		gbc_lblTo.anchor = GridBagConstraints.EAST;
		gbc_lblTo.insets = new Insets(0, 0, 5, 5);
		gbc_lblTo.gridx = 0;
		gbc_lblTo.gridy = 1;
		contentPane.add(lblTo, gbc_lblTo);

		toTextBox = new JTextField();
		toTextBox.setName("toTextBox");
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		contentPane.add(toTextBox, gbc_textField_1);
		toTextBox.setColumns(10);
		toTextBox.setText(LocalDate.now().toString());

		JButton btnSearch = new JButton("Search");
		btnSearch.setName("searchButton");
		GridBagConstraints gbc_btnSearch = new GridBagConstraints();
		gbc_btnSearch.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearch.gridwidth = 2;
		gbc_btnSearch.gridx = 0;
		gbc_btnSearch.gridy = 3;
		contentPane.add(btnSearch, gbc_btnSearch);

		JLabel lblErrorMessage = new JLabel("");
		lblErrorMessage.setForeground(Color.RED);
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridx = 1;
		gbc_lblErrorMessage.gridy = 5;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);

		/*
		 * TODO: usare JDatePicker invece di campo di testo UtilDateModel fromModel =
		 * new UtilDateModel(); JDatePanelImpl fromDatePanel = new
		 * JDatePanelImpl(fromModel); JDatePickerImpl fromDatePicker = new
		 * JDatePickerImpl(fromDatePanel); contentPane.add(fromDatePicker);
		 * 
		 * UtilDateModel toModel = new UtilDateModel(); JDatePanelImpl toDatePanel = new
		 * JDatePanelImpl(toModel); JDatePickerImpl toDatePicker = new
		 * JDatePickerImpl(toDatePanel); contentPane.add(toDatePicker);
		 */

		btnSearch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					LocalDate.parse(fromTextBox.getText(), dateFormat);
					LocalDate.parse(toTextBox.getText(), dateFormat);
				} catch (DateTimeParseException exception) {
					lblErrorMessage.setText("Please provide dates formatted as 'yyyy-MM-dd'");
				}

				subscriptionsController.requestOrders(LocalDate.parse(fromTextBox.getText()),
						LocalDate.parse(toTextBox.getText()));

				listSwingView.setVisible(true);

			}

		});

		Document fromDocument = fromTextBox.getDocument();
		Document toDocument = toTextBox.getDocument();

		DocumentListener textListener = new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (fromTextBox.getText().length() == 0)
					btnSearch.setEnabled(false);
				if (toTextBox.getText().length() == 0)
					btnSearch.setEnabled(false);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				btnSearch.setEnabled(true);

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
				
			}

		};

		fromDocument.addDocumentListener(textListener);
		toDocument.addDocumentListener(textListener);

	}

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

}
