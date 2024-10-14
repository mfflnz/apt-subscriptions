package org.blefuscu.apt.subscriptions.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
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
		window.textBox("idTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Order Date"));
		window.textBox("orderDateTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Credit Date"));
		window.textBox("creditDateTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Gross"));
		window.textBox("grossTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Net"));
		window.textBox("netTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Payment"));
		window.textBox("paymentTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Product"));
		window.textBox("productTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("First Issue"));
		window.textBox("firstIssueTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Last Issue"));
		window.textBox("lastIssueTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("First Name"));
		window.textBox("firstNameTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Last Name"));
		window.textBox("lastNameTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Address"));
		window.textBox("addressTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Zip Code"));
		window.textBox("zipCodeTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("City"));
		window.textBox("cityTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Province"));
		window.textBox("provinceTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Country"));
		window.textBox("countryTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Email"));
		window.textBox("emailTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Phone"));
		window.textBox("phoneTextBox").requireEnabled().requireNotEditable();
		window.label(JLabelMatcher.withText("Notes"));
		window.textBox("notesTextArea").requireEnabled().requireNotEditable();
		window.checkBox("confirmedCheckBox");
		window.checkBox("unlockCheckBox");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}

	@Test @GUITest
	public void testButtonsShouldBeEnabledOnlyIfMandatoryFieldsAreNotEmptyAndOrderIsUnlocked() {
		  window.textBox("idTextBox").setText("1");
		  window.textBox("orderDateTextBox").setText("2024-10-12");
		  window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		  window.textBox("grossTextBox").setText("€65,00");
		  window.textBox("paymentTextBox").setText("Bonifico");
		  window.textBox("emailTextBox").setText("user@email.com");
		  window.checkBox("unlockCheckBox").check();
		  window.button(JButtonMatcher.withText("Add")).requireEnabled();
		  window.button(JButtonMatcher.withText("Update")).requireEnabled();
		  window.button(JButtonMatcher.withText("Delete")).requireEnabled();
	}

	@Test @GUITest
	public void testButtonsShouldBeisabledIfAMandatoryFieldsIsEmpty() {
		window.textBox("idTextBox").deleteText();
		window.checkBox("unlockCheckBox").check();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}
	
	@Test
	@GUITest
	public void testButtonsShouldBeisabledIfOrderIsLocked() {
		window.textBox("idTextBox").setText("1");
		window.textBox("orderDateTextBox").setText("2024-10-12");
		window.textBox("productTextBox").setText("Abbonamento annuale cartaceo");
		window.textBox("grossTextBox").setText("€65,00");
		window.textBox("paymentTextBox").setText("Bonifico");
		window.textBox("emailTextBox").setText("user@email.com");
		window.checkBox("unlockCheckBox").uncheck();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Update")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
	}
	
	@Test @GUITest
	public void testDeleteButtonShouldBeEnabledOnlyIfOrderIsUnlocked() {
		
	}
	

}
