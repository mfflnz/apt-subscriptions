package org.blefuscu.apt.subscriptions.view;

import java.util.List;

import javax.swing.JPanel;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.Order;
import java.awt.GridBagLayout;
import javax.swing.JList;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class ListSwingView extends JPanel implements ListView {

	private static final long serialVersionUID = 1L;

	private JList<Order> listOrders;
	private DefaultListModel<Order> listOrdersModel;
	private SubscriptionsController subscriptionsController;

	public SubscriptionsController getSubscriptionsController() {
		return subscriptionsController;
	}

	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

	public DefaultListModel<Order> getListOrdersModel() {
		return listOrdersModel;
	}

	/**
	 * Create the panel.
	 */
	public ListSwingView() {

		listOrdersModel = new DefaultListModel<>();
		listOrders = new JList<>(listOrdersModel);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		add(scrollPane_1, gbc_scrollPane_1);

		JList<Order> list = new JList<Order>(listOrdersModel);
		scrollPane_1.setViewportView(list);
		list.setName("ordersList");

		JButton btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 2;
		add(btnExport, gbc_btnExport);

		class MyListDataListener implements ListDataListener {

			@Override
			public void contentsChanged(ListDataEvent e) {
				btnExport.setEnabled(true);

			}

			@Override
			public void intervalAdded(ListDataEvent e) {
				btnExport.setEnabled(true);
			}

			@Override
			public void intervalRemoved(ListDataEvent e) {
				btnExport.setEnabled(!listOrdersModel.isEmpty());

			}

		}

		listOrdersModel.addListDataListener(new MyListDataListener());

	}

	@Override
	public void showOrders(List<Order> orders) {
		orders.stream().forEach(listOrdersModel::addElement);
	}

	@Override
	public void orderUpdated(int orderId, Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void orderDeleted(Order order) {
		// TODO Auto-generated method stub

	}

	@Override
	public void csvExported(List<Order> orders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub

	}

}
