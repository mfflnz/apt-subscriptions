package org.blefuscu.apt.subscriptions.view;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SearchSwingViewTest {
	
	private SearchSwingView searchSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;
	
	@Mock
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() {
		
		closeable = MockitoAnnotations.openMocks(this);
		
		SearchSwingView frame = GuiActionRunner.execute(() -> {
			searchSwingView = new SearchSwingView();
			searchSwingView.setSubscriptionsController(subscriptionsController);
			return searchSwingView;
		});

		window = showInFrame(frame);
		
	}

	@After
	public void tearDown() throws Exception {
		window.cleanUp();
		closeable.close();
	}
	
	@Test
	@GUITest
	public void testControlsInitialStates() {

		window.textBox("fromTextBox").requireEditable();
		window.textBox("toTextBox").requireEditable();
		window.button(JButtonMatcher.withText("Search")).requireEnabled();

	}

	@Test
	public void testIfFromTextBoxIsNotEmptyAndIsNotCorrectlyFormattedThenTheSearchButtonShouldBeDisabled() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-01-0");
		window.button(JButtonMatcher.withText("Search")).requireDisabled();

	}
	
	@Test
	public void testIfToTextBoxIsNotEmptyAndIsNotCorrectlyFormattedThenTheSearchButtonShouldBeDisabled() {
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-01-0");
		window.button(JButtonMatcher.withText("Search")).requireDisabled();
	}
	
	@Test
	public void testIfFromTextBoxIsNotEmptyAndIsCorrectlyFormattedThenTheSearchButtonShouldBeEnabled() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-10-23");
		window.button(JButtonMatcher.withText("Search")).requireEnabled();

	}
	
	@Test
	public void testIfToTextBoxIsNotEmptyAndIsCorrectlyFormattedThenTheSearchButtonShouldBeEnabled() {
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-01-23");
		window.button(JButtonMatcher.withText("Search")).requireEnabled();
	}
	
	@Test
	public void testSearchButtonShouldDelegateToSubscriptionsController() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-02-01");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-02-28");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-28")));
	}
	
	@Test
	public void testIfFromDateCannotBeParsedThenTheControllerShouldSendAnErrorMessage() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-02-51");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-02-28");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.sendErrorMessage("Please check start date format"));
	}

	
	@Test
	public void testIfToDateCannotBeParsedThenTheControllerShouldSendAnErrorMessage() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-02-01");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-02-68");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.sendErrorMessage("Please check end date format"));
		

	}
	
	@Test
	public void testIfToDateIsEarlierThanFromDateThenTheControllerShouldSendAnErrorMessage() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-02-01");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-01-01");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.sendErrorMessage("Start date should be earlier or equal to end date"));
	}
	
	@Test
	public void testIfToDateIsEqualToFromDateThenTheControllerShouldPerformTheRequest() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-02-01");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-02-01");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("2025-02-01"), LocalDate.parse("2025-02-01")));
	}
	
	@Test
	public void testIfFromDateIsEmptyThenItShouldBeReplacedByAnArbitraryEarlyDate() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-01-01");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("1970-01-01"), LocalDate.parse("2025-01-01")));

	}

	@Test
	public void testIfFromDateContainsOnlySpacesThenItShouldBeReplacedByAnArbitraryEarlyDate() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("  ");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("2025-01-01");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("1970-01-01"), LocalDate.parse("2025-01-01")));
		
	}
	
	@Test
	public void testIfToDateIsEmptyThenItShouldBeReplacedByTodaysDate() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-01-01");
		window.textBox("toTextBox").deleteText();
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("2025-01-01"), LocalDate.now()));

	}
	
	@Test
	public void testIfToDateContainsOnlySpacesThenItShouldBeReplacedByTodaysDate() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText("2025-01-01");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText("  ");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders(LocalDate.parse("2025-01-01"), LocalDate.now()));
		
	}
	
	@Test
	public void testIfFromDateAndToDateAreBothEmptyThenEveryOrderShouldBeListed() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("toTextBox").deleteText();

		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders());
		
	}

	@Test
	public void testIfFromDateAndToDateAreBothOnlyWhitespacesThenEveryOrderShouldBeListed() {
		window.textBox("fromTextBox").deleteText();
		window.textBox("fromTextBox").enterText(" ");
		window.textBox("toTextBox").deleteText();
		window.textBox("toTextBox").enterText(" ");
		
		await().atMost(5, TimeUnit.SECONDS).until(() -> window.button(JButtonMatcher.withText("Search")).isEnabled());

		GuiActionRunner.execute(() -> {
			searchSwingView.getBtnSearch().doClick();
		});
		
		await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> verify(subscriptionsController)
				.requestOrders());
		
	}

}
