package org.blefuscu.apt.subscriptions.view;

import java.time.LocalDate;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class SearchSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;
	
	private SearchSwingView searchSwingView;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			searchSwingView = new SearchSwingView();
			return searchSwingView;
		});
		
		window = new FrameFixture(robot(), searchSwingView);
		window.show();
	}
	
	@Test
	public void testControlsInitialStates() {
		window.label(JLabelMatcher.withText("From"));
		window.textBox("fromTextBox").requireText(LocalDate.now().toString());
		window.label(JLabelMatcher.withText("To"));
		window.textBox("toTextBox").requireText(LocalDate.now().toString());
		window.button(JButtonMatcher.withText("Search")).requireEnabled();
	}
	
	@Test
	public void testIfFromTextBoxIsEmptySearchButtonShouldBeDisabled() {
		window.textBox("fromTextBox").deleteText();
		window.button(JButtonMatcher.withText("Search")).requireDisabled();
	}
	
	@Test
	public void testIfToTextBoxIsEmptySearchButtonShouldBeDisabled() {
		window.textBox("toTextBox").deleteText();
		window.button(JButtonMatcher.withText("Search")).requireDisabled();
	}

	/*
	@Test
	public void testIfToTextBoxIsEmptySearchButtonShouldBeDisabled() {
		window.textBox("toTextBox").deleteText();
		window.button(JButtonMatcher.withText("Search")).requireDisabled();
	}
	*/

}
