package com.sfeir.tutorials.client.tetriselement;

import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

/**
 * Tetris Element:  + +
 *                  + +
 *                  
 * @author Oussama Zoghlami
 * @date 19/12/2011
 *
 */
public class TetrisElementImpl5 extends TetrisElement {
	
	private static final String CSS_COLOR = "rgb(47,79,47)";

	/**
	 * 
	 * @param context
	 */
	public TetrisElementImpl5(Context2d context) {
		this.context = context;
		cssColor = CssColor.make(CSS_COLOR);
		initWidgetPoints();
	}

	@Override
	protected void initWidgetPoints() {
		Rectangle rectangle1 = new Rectangle(Rectangle.WIDTH * 4 + 2 * 4, 0, cssColor);
		Rectangle rectangle2 = new Rectangle(Rectangle.WIDTH * 5 + 2 * 5, 0, cssColor);
		Rectangle rectangle3 = new Rectangle(Rectangle.WIDTH * 4 + 2 * 4, Rectangle.HEIGHT + 2, cssColor);
		Rectangle rectangle4 = new Rectangle(Rectangle.WIDTH * 5 + 2 * 5, Rectangle.HEIGHT + 2, cssColor);
		rectangle1.setGravityCenter(true);
		rectangles.add(rectangle1);
		rectangles.add(rectangle2);
		rectangles.add(rectangle3);
		rectangles.add(rectangle4);
	}

	@Override
	public void rotateUp() {
	}

	@Override
	public void rotateDown() {
	}

	@Override
	protected List<Rectangle> getRotatedRectangles() {
		return null;
	}

}
