package org.blefuscu.apt.subscriptions;

import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.launcher.ApplicationLauncher.*;

import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.FrameFinder;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.blefuscu.apt.subscriptions.controller.SubscriptionsController;
import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mongodb.MongoClient;

@RunWith(GUITestRunner.class)
public class SubscriptionsSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static final String DB_NAME = "test-db";
	private static final String COLLECTION_NAME = "test-collection";
	private MongoClient mongoClient;
	private FrameFixture window;
	private OrderMongoRepository orderRepository;

	@Override
	protected void onSetUp() throws Exception {

		application("org.blefuscu.apt.subscriptions.SubscriptionsSwingApp").start();

		window = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class) {
			@Override
			protected boolean isMatching(JFrame frame) {
				return "Dashboard".equals(frame.getTitle()) && frame.isShowing();
			}
		}).using(robot());

	}

	@Test @GUITest
	public void testOnStartAllTheFieldsAreEmpty() {
		 assertThat(window.list().contents()).isEmpty();
		 
	}
}
