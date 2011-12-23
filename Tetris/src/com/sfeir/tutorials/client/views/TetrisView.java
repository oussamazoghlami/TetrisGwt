package com.sfeir.tutorials.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * This is the view associated to our Tetris Game:
 * 
 * <p>
 * Our Tetris View will contain the Game Canvas, another Canvas that will
 * display the next Element and a part displaying the game Score
 * 
 * @author Oussama Zoghlami
 * @date 21/12/2011
 * 
 */
public class TetrisView extends Composite {

	private static TetrisViewUiBinder uiBinder = GWT.create(TetrisViewUiBinder.class);

	interface TetrisViewUiBinder extends UiBinder<Widget, TetrisView> {
	}

	@UiField(provided = true)
	TetrisGameWidget gameWidget;

	@UiField(provided = true)
	NextElementWidget nextElementWidget;

	@UiField
	Label score;

	public TetrisView() {
		nextElementWidget = new NextElementWidget();
		gameWidget = new TetrisGameWidget(this);
		initWidget(uiBinder.createAndBindUi(this));
	}

	public TetrisView(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public NextElementWidget getNextElementWidget() {
		return nextElementWidget;
	}

	public Label getScore() {
		return score;
	}

}
