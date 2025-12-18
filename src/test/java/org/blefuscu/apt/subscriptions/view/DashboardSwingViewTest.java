package org.blefuscu.apt.subscriptions.view;

import static org.junit.Assert.assertTrue;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class DashboardSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	private DashboardSwingView dashboardSwingView;
	private SearchSwingView searchSwingView;
	private ListSwingView listSwingView;
	private OrderSwingView orderSwingView;
	private MessageSwingView messageSwingView;

	@Override
	protected void onSetUp() {

		GuiActionRunner.execute(() -> {
			searchSwingView = new SearchSwingView();
			listSwingView = new ListSwingView();
			orderSwingView = new OrderSwingView();
			messageSwingView = new MessageSwingView();
			dashboardSwingView = new DashboardSwingView(searchSwingView, listSwingView, orderSwingView,
					messageSwingView);
			return dashboardSwingView;
		});
		window = new FrameFixture(robot(), dashboardSwingView);
		window.show();
	}

	@Test
	public void testOnStartupAllPanelsAreShownAndOrderFieldsAreNotEditable() {

		window.panel("searchPanel").requireVisible();
		window.panel("listPanel").requireVisible();
		window.panel("orderPanel").requireVisible();
		window.panel("messagePanel").requireVisible();

		window.textBox("orderIdTextBox").requireNotEditable();
		window.textBox("orderDateTextBox").requireNotEditable();
		window.textBox("orderTotalTextBox").requireNotEditable();
		window.textBox("netTotalTextBox").requireNotEditable();
		window.textBox("paymentMethodTextBox").requireNotEditable();
		window.textBox("firstNameTextBox").requireNotEditable();
		window.textBox("lastNameTextBox").requireNotEditable();
		window.textBox("addressTextBox").requireNotEditable();
		window.textBox("postcodeTextBox").requireNotEditable();
		window.textBox("stateTextBox").requireNotEditable();
		window.textBox("cityTextBox").requireNotEditable();
		window.textBox("emailTextBox").requireNotEditable();
		window.textBox("phoneTextBox").requireNotEditable();
		window.textBox("productTextBox").requireNotEditable();
		window.textBox("firstIssueTextBox").requireNotEditable();
		window.textBox("lastIssueTextBox").requireNotEditable();
		window.textBox("notesTextBox").requireNotEditable();

		// SonarQube
		assertTrue(window.panel("searchPanel").isEnabled());
		assertTrue(window.panel("listPanel").isEnabled());
		assertTrue(window.panel("orderPanel").isEnabled());
		assertTrue(window.panel("messagePanel").isEnabled());

	}

}
