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
	}

	

}
