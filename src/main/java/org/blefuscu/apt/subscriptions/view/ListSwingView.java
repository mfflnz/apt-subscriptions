package org.blefuscu.apt.subscriptions.view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;

import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.Insets;
import java.util.List;
import javax.swing.JLabel;

public class ListSwingView extends JFrame implements ListView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JList<Order> listOrders;
	private DefaultListModel<Order> listOrdersModel;
	private JButton btnDelete;
	private JButton btnShowDetails;
	private JLabel lblErrorMessage;
	
	private SubscriptionsController subscriptionsController;


	DefaultListModel<Order> getListOrdersModel() {
		return listOrdersModel;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListSwingView frame = new ListSwingView();
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
	public ListSwingView() {

		listOrdersModel = new DefaultListModel<>();
		listOrders = new JList<>(listOrdersModel);

		setTitle("List View");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setName("lblErrorMessage");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);

		listOrders.addListSelectionListener(e -> btnDelete.setEnabled(listOrders.getSelectedIndex() != -1));
		listOrders.addListSelectionListener(e -> btnShowDetails.setEnabled(listOrders.getSelectedIndex() != -1));
		listOrders.setName("ordersList");
		scrollPane.setViewportView(listOrders);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.anchor = GridBagConstraints.WEST;
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 1;
		contentPane.add(btnDelete, gbc_btnDelete);

		btnShowDetails = new JButton("Show Details");
		btnShowDetails.setEnabled(false);
		GridBagConstraints gbc_btnViewDetails = new GridBagConstraints();
		gbc_btnViewDetails.insets = new Insets(0, 0, 5, 0);
		gbc_btnViewDetails.anchor = GridBagConstraints.EAST;
		gbc_btnViewDetails.gridx = 2;
		gbc_btnViewDetails.gridy = 1;
		contentPane.add(btnShowDetails, gbc_btnViewDetails);

		lblErrorMessage = new JLabel(" ");
		lblErrorMessage.setName("errorMessageLabel");
		GridBagConstraints gbc_lblErrorMessage = new GridBagConstraints();
		gbc_lblErrorMessage.gridwidth = 3;
		gbc_lblErrorMessage.insets = new Insets(0, 0, 0, 5);
		gbc_lblErrorMessage.gridx = 0;
		gbc_lblErrorMessage.gridy = 2;
		contentPane.add(lblErrorMessage, gbc_lblErrorMessage);
	}

	@Override
	public void showOrders(List<Order> orders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void csvExported(List<Order> orders) {
		// TODO Auto-generated method stub

	}

	public void showAllOrders(List<Order> orders) {
		orders.stream().forEach(listOrdersModel::addElement);
	}

	public void showError(String message, Order order) {
		lblErrorMessage.setText(message + ": " + order);
	}

	public void orderAdded(Order order) {
		listOrdersModel.addElement(order);
		resetErrorLabel();
	}

	public void orderRemoved(Order order) {
		listOrdersModel.removeElement(order);
		resetErrorLabel();
	}

	private void resetErrorLabel() {
		lblErrorMessage.setText(" ");
	}

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}
	
}