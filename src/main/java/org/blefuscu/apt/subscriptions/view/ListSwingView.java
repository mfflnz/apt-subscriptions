package org.blefuscu.apt.subscriptions.view;

import java.util.List;

import javax.swing.JPanel;

import org.blefuscu.apt.subscriptions.model.Order;
import java.awt.GridBagLayout;
import javax.swing.JList;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;

public class ListSwingView extends JPanel implements ListView{

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public ListSwingView() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JList list = new JList();
		list.setName("ordersList");
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.gridheight = 2;
		gbc_list.gridwidth = 3;
		gbc_list.insets = new Insets(0, 0, 5, 0);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		add(list, gbc_list);
		
		JButton btnExport = new JButton("Export");
		GridBagConstraints gbc_btnExport = new GridBagConstraints();
		gbc_btnExport.anchor = GridBagConstraints.WEST;
		gbc_btnExport.insets = new Insets(0, 0, 5, 5);
		gbc_btnExport.gridx = 0;
		gbc_btnExport.gridy = 2;
		add(btnExport, gbc_btnExport);

	}

	@Override
	public void showOrders(List<Order> orders) {
		// TODO Auto-generated method stub
		
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
