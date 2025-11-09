package org.blefuscu.apt.subscriptions.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;

@RunWith(GUITestRunner.class)
public class DashboardSwingViewTest extends AssertJSwingJUnitTestCase {
	
	 private FrameFixture window;
	 private DashboardSwingView dashboardSwingView;
	 
		@Override
		protected void onSetUp() {
			GuiActionRunner.execute(() -> {
				dashboardSwingView = new DashboardSwingView();
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
	}

}
