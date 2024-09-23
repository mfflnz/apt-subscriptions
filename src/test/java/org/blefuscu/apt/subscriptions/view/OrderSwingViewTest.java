package org.blefuscu.apt.subscriptions.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class OrderSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	
	private OrderSwingView orderSwingView;
	
	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			orderSwingView = new OrderSwingView();
			return orderSwingView;
		});
		window = new FrameFixture(robot(), orderSwingView);
		window.show();
	}
	
	@Test @GUITest
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("id"));
		window.textBox("idTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Order Date"));
		window.textBox("orderDateTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Credit Date"));
		window.textBox("creditDateTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Gross"));
		window.textBox("grossTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Net"));
		window.textBox("netTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Payment"));
		window.textBox("paymentTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Product"));
		window.textBox("productTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("First Issue"));
		window.textBox("firstIssueTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Last Issue"));
		window.textBox("lastIssueTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("First Name"));
		window.textBox("firstNameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Last Name"));
		window.textBox("lastNameTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Address"));
		window.textBox("addressTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Zip Code"));
		window.textBox("zipCodeTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("City"));
		window.textBox("cityTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Province"));
		window.textBox("provinceTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Country"));
		window.textBox("countryTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Email"));
		window.textBox("emailTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Phone"));
		window.textBox("phoneTextBox").requireEnabled();
		window.label(JLabelMatcher.withText("Notes"));
		window.textBox("notesTextArea").requireEnabled();
	}

	

}
