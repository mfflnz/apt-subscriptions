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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ListSwingView extends JPanel implements ListView {

	private static final long serialVersionUID = 1L;

	private DefaultListModel<Order> listOrdersModel;
	private SubscriptionsController subscriptionsController;
	private JFileChooser fc;

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

		JScrollPane scrollPane_1 = new JScrollPane();
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridheight = 2;
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane_1.gridx = 0;
		gbc_scrollPane_1.gridy = 0;
		add(scrollPane_1, gbc_scrollPane_1);

		JList<Order> list = new JList<Order>(listOrdersModel);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				
// TODO: getSelectedValue non trova più l'elemento della lista
				subscriptionsController.orderDetails(list.getSelectedValue().getOrderId());
			}
		});
		scrollPane_1.setViewportView(list);
		list.setName("ordersList");

		JButton btnExport = new JButton("Export");
		btnExport.setEnabled(false);
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 2;
		add(btnExport, gbc_btnExport);
		btnExport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.setDialogTitle("Export orders");

				if (fc.showSaveDialog(scrollPane_1) == JFileChooser.APPROVE_OPTION) {
					List<FormattedOrder> formattedOrders = new ArrayList<FormattedOrder>();
					for (int i = 0; i < listOrdersModel.getSize(); i++) {
						formattedOrders.add(subscriptionsController.formatOrder(listOrdersModel.elementAt(i)));
					}
					subscriptionsController.exportOrders(formattedOrders, fc.getName());
				} else {
					fc.setVisible(false);
				}
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


}
