package org.blefuscu.apt.subscriptions.view;

import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.junit.Assert.*;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MessageSwingViewTest {

	private MessageSwingView messageSwingView;
	private FrameFixture window;
	private AutoCloseable closeable;

	@Mock
	private SubscriptionsController subscriptionsController;

	@Before
	public void setUp() {
		closeable = MockitoAnnotations.openMocks(this);

		GuiActionRunner.execute(() -> {
			messageSwingView = new MessageSwingView();
			messageSwingView.setSubscriptionsController(subscriptionsController);
			return messageSwingView;
		});

		window = showInFrame(messageSwingView);
	}

	@After
	public void tearDown() throws Exception {
		window.cleanUp();
		closeable.close();
	}

	@Test
	@GUITest
	public void testControlsInitialStates() {
		window.textBox("messageTextBox").requireVisible();
	}

	@Test
	@GUITest
	public void testShowInfoMessageShouldDisplayAnInfoMessage() {
		messageSwingView.showInfoMessage("Sample info message");
		assertEquals("Sample info message", window.textBox("messageTextBox").text());
	}

	@Test
	@GUITest
	public void testShowErrorMessageShouldDisplayARedInfoMessage() {
		messageSwingView.showErrorMessage("Sample error message");
		assertEquals("Sample error message", window.textBox("messageTextBox").text());
	}
}
