package org.blefuscu.apt.subscriptions.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.model.FormattedOrder;
import org.blefuscu.apt.subscriptions.model.Order;

public class ListSwingView extends JPanel implements ListView {

	private static final long serialVersionUID = 1L;

	private DefaultListModel<Order> listOrdersModel;
	private transient SubscriptionsController subscriptionsController;
	private JFileChooser fc;
	private JButton btnExport;

	private JList<Order> list;


	public void setSubscriptionsController(SubscriptionsController subscriptionsController) {
		this.subscriptionsController = subscriptionsController;
	}

	public DefaultListModel<Order> getListOrdersModel() {
		return listOrdersModel;
	}

	public JFileChooser getFc() {
		return fc;
	}
	

	
	/**
	 * Create the panel.
	 */
	public ListSwingView() {

		listOrdersModel = new DefaultListModel<>();
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JScrollPane scrollPane1 = new JScrollPane();
		GridBagConstraints gbcScrollPane1 = new GridBagConstraints();
		gbcScrollPane1.gridheight = 2;
		gbcScrollPane1.fill = GridBagConstraints.BOTH;
		gbcScrollPane1.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane1.gridx = 0;
		gbcScrollPane1.gridy = 0;
		add(scrollPane1, gbcScrollPane1);

		list = new JList<>(listOrdersModel);
		list.addListSelectionListener(arg0 -> {
			if (!list.isSelectionEmpty()) {
				subscriptionsController.orderDetails(list.getSelectedValue().getOrderId());
			}
		});

		scrollPane1.setViewportView(list);
		list.setName("ordersList");

		btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		
		GridBagConstraints gbcBtnExport = new GridBagConstraints();
		gbcBtnExport.anchor = GridBagConstraints.NORTHWEST;
		gbcBtnExport.gridx = 0;
		gbcBtnExport.gridy = 2;
		add(btnExport, gbcBtnExport);
		btnExport.addActionListener(e -> {
			fc = new JFileChooser();
			fc.setVisible(true);
			fc.setDialogTitle("Export orders");

			if (fc.showSaveDialog(scrollPane1) == JFileChooser.APPROVE_OPTION) {
				List<FormattedOrder> formattedOrders = new ArrayList<>();
				for (int i = 0; i < listOrdersModel.getSize(); i++) {
					formattedOrders.add(subscriptionsController.formatOrder(listOrdersModel.elementAt(i)));
				}
				subscriptionsController.exportOrders(formattedOrders, fc.getSelectedFile().getAbsolutePath());
			} else {
				fc.setVisible(false);
			}
		});
				


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
		listOrdersModel.clear();
		orders.stream().forEach(listOrdersModel::addElement);
	}

	@Override
	public void orderUpdated(int orderId, Order order) {

		for (int i = 0; i < listOrdersModel.getSize(); i++) {
			if (listOrdersModel.elementAt(i).getOrderId() == orderId) {
				listOrdersModel.set(i, order);
			}
		}
	}

	@Override
	public void orderDeleted(int orderId) {

		for (int i = 0; i < listOrdersModel.getSize(); i++) {
			if (listOrdersModel.elementAt(i).getOrderId() == orderId) {
				listOrdersModel.remove(i);
			}
		}

	}

	@Override
	public void clearList() {
		list.clearSelection();
		listOrdersModel.clear();
	}

	public void setFc(JFileChooser fc) {
		this.fc = fc;
	}

}
