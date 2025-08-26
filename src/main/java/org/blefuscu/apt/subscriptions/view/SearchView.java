package org.blefuscu.apt.subscriptions.view;

import java.time.LocalDateTime;

public interface SearchView {

	void searchLaunched(LocalDateTime fromDate, LocalDateTime toDate);
	void showMessage(String string);

}
