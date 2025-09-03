package org.blefuscu.apt.subscriptions.controller;

import java.util.List;

public interface ExportManager {

	public void saveData(List<Object> data, String location);
	public void deleteData(String location);
}