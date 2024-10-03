package org.blefuscu.apt.subscriptions.view;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class ListSwingViewTest extends AssertJSwingJUnitTestCase {
	
	private ListSwingView listSwingView;
	private FrameFixture window;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
			return listSwingView;
		});
		
		window = new FrameFixture(robot(), listSwingView);
		window.show();
	}

	@Test @GUITest
	public void testControlsInitialStates() {
		window.list("ordersList");
		window.button(JButtonMatcher.withText("Show Details")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();

	}


}
