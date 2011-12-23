package com.sfeir.tutorials.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.sfeir.tutorials.client.views.TetrisView;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Tetris implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		RootPanel.get().add(new TetrisView());
	}
}
