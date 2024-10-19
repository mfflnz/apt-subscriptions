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

	private FrameFixture searchWindow;
	private FrameFixture listWindow;
	
	private SearchSwingView searchSwingView;
	private ListSwingView listSwingView;

	@Override
	protected void onSetUp() throws Exception {
		GuiActionRunner.execute(() -> {
			searchSwingView = new SearchSwingView();
			return searchSwingView;
		});
		
		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
			return listSwingView;
		});
		
		searchWindow = new FrameFixture(robot(), searchSwingView);
		
		searchWindow.show();
	}
	
	@Test
	public void testControlsInitialStates() {
		searchWindow.label(JLabelMatcher.withText("From"));
		searchWindow.textBox("fromTextBox").requireText(LocalDate.now().toString());
		searchWindow.label(JLabelMatcher.withText("To"));
		searchWindow.textBox("toTextBox").requireText(LocalDate.now().toString());
		searchWindow.button(JButtonMatcher.withText("Search")).requireEnabled();
	}
	
	@Test
	public void testIfFromTextBoxIsEmptySearchButtonShouldBeDisabled() {
		searchWindow.textBox("fromTextBox").deleteText();
		searchWindow.button(JButtonMatcher.withText("Search")).requireDisabled();
		searchWindow.textBox("fromTextBox").enterText(LocalDate.now().toString());
		searchWindow.button(JButtonMatcher.withText("Search")).requireEnabled();
	}
	
	@Test
	public void testIfToTextBoxIsEmptySearchButtonShouldBeDisabled() {
		searchWindow.textBox("toTextBox").deleteText();
		searchWindow.button(JButtonMatcher.withText("Search")).requireDisabled();
		searchWindow.textBox("toTextBox").enterText(LocalDate.now().toString());
		searchWindow.button(JButtonMatcher.withText("Search")).requireEnabled();
	}

	@Test
	public void testIfSearchButtonIsPressedAListViewSouldBeShown() {
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		listSwingView.isShowing();
		
	}



}
