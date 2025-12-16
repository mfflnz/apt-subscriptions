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
	public void testAllPanelsAreShown() {

		window.panel("searchPanel").requireVisible();
		window.panel("listPanel").requireVisible();
		window.panel("orderPanel").requireVisible();
		window.panel("messagePanel").requireVisible();

		// SonarQube
		assertTrue(window.panel("searchPanel").isEnabled());
		assertTrue(window.panel("listPanel").isEnabled());
		assertTrue(window.panel("orderPanel").isEnabled());
		assertTrue(window.panel("messagePanel").isEnabled());

	}

}
