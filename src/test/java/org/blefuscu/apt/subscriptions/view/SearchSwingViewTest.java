package org.blefuscu.apt.subscriptions.view;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.time.LocalDate;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(GUITestRunner.class)
public class SearchSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture searchWindow;

	private SearchSwingView searchSwingView;
	private ListSwingView listSwingView;

	@Mock
	private SubscriptionsController subscriptionsController;

	private AutoCloseable closeable;

	@Override
	protected void onSetUp() throws Exception {

		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			searchSwingView = new SearchSwingView();
			searchSwingView.setSubscriptionsController(subscriptionsController);
			return searchSwingView;
		});

		GuiActionRunner.execute(() -> {
			listSwingView = new ListSwingView();
			return listSwingView;
		});

		searchWindow = new FrameFixture(robot(), searchSwingView);

		searchWindow.show();

	}

	@Override
	public void onTearDown() throws Exception {
		closeable.close();
	}

	@Test
	public void testControlsInitialStates() {
		searchWindow.label(JLabelMatcher.withText("From"));
		searchWindow.textBox("fromTextBox").requireText(LocalDate.now().toString());
		searchWindow.label(JLabelMatcher.withText("To"));
		searchWindow.textBox("toTextBox").requireText(LocalDate.now().toString());
		searchWindow.button(JButtonMatcher.withText("Search")).requireEnabled();
		searchWindow.label(JLabelMatcher.withName("errorMessageLabel")).requireText("");
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

	@Test
	public void testIfSearchButtonIsPressedTheTwoDatesShouldBePassedToTheController() {
		searchWindow.textBox("fromTextBox").deleteText();
		searchWindow.textBox("fromTextBox").enterText(LocalDate.now().toString());
		searchWindow.textBox("toTextBox").deleteText();
		searchWindow.textBox("toTextBox").enterText(LocalDate.now().toString());
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		verify(subscriptionsController).requestOrders(LocalDate.now(), LocalDate.now());

	}

	@Test
	public void testIfFromDateIsNotCorrectlyFormattedShouldShowAnErrorMessage() {
		searchWindow.textBox("fromTextBox").deleteText();
		searchWindow.textBox("fromTextBox").enterText("12-10-2024");
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		searchWindow.label("errorMessageLabel").requireText("Please provide dates formatted as 'yyyy-MM-dd'");
		verifyNoInteractions(subscriptionsController);
	}

	@Test
	public void testIfToDateIsNotCorrectlyFormattedShouldShowAnErrorMessage() {
		searchWindow.textBox("toTextBox").deleteText();
		searchWindow.textBox("toTextBox").enterText("12-10-2024");
		searchWindow.button(JButtonMatcher.withText("Search")).click();
		searchWindow.label("errorMessageLabel").requireText("Please provide dates formatted as 'yyyy-MM-dd'");
		verifyNoInteractions(subscriptionsController);
	}

}
