package com.sfeir.tutorials.client.tetriselement;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;

/**
 * Tetris element:  + +
 *                    +
 *                    +
 * 
 * @author Oussama Zoghlami
 * @date 19/12/2011
 * 
 */
public class TetrisElementImpl1 extends TetrisElement {
	
	private static final String CSS_COLOR = "rgb(255,0,0)";

	/**
	 * 
	 * @param context
	 */
	public TetrisElementImpl1(Context2d context) {
		this.context = context;
		cssColor = CssColor.make(CSS_COLOR);
		initWidgetPoints();
	}

	@Override
	protected void initWidgetPoints() {
		Rectangle rectangle1 = new Rectangle(Rectangle.WIDTH * 4 + 2 * 4, 0, cssColor);
		Rectangle rectangle2 = new Rectangle(Rectangle.WIDTH * 5 + 2 * 5, 0, cssColor);
		Rectangle rectangle3 = new Rectangle(Rectangle.WIDTH * 5 + 2 * 5, Rectangle.HEIGHT + 2, cssColor);
		Rectangle rectangle4 = new Rectangle(Rectangle.WIDTH * 5 + 2 * 5, Rectangle.HEIGHT * 2 + 2 * 2, cssColor);
		rectangle3.setGravityCenter(true);
		rectangles.add(rectangle1);
		rectangles.add(rectangle2);
		rectangles.add(rectangle3);
		rectangles.add(rectangle4);
	}

	@Override
	public void rotateUp() {
		rotationId = Math.abs(rotationId + 1) % 4;
		rotatePoints();
	}

	@Override
	public void rotateDown() {
		rotationId = Math.abs(rotationId - 1) % 4;
		rotatePoints();
	}

	@Override
	protected List<Rectangle> getRotatedRectangles() {
		List<Rectangle> result = new ArrayList<Rectangle>();
		Rectangle gravityCenter = getGravityCenter();
		double centerX = gravityCenter.getX();
		double centerY = gravityCenter.getY();
		Rectangle rectangle1 = null, rectangle2 = null, rectangle3 = null;

		switch (rotationId) {
		case 0:
			rectangle1 = new Rectangle(centerX - Rectangle.WIDTH - 2, centerY - Rectangle.HEIGHT - 2);
			rectangle2 = new Rectangle(centerX, centerY - Rectangle.HEIGHT - 2);
			rectangle3 = new Rectangle(centerX, centerY + Rectangle.HEIGHT + 2);
			break;

		case 1:
			rectangle1 = new Rectangle(centerX - Rectangle.WIDTH - 2, centerY);
			rectangle2 = new Rectangle(centerX + Rectangle.WIDTH + 2, centerY);
			rectangle3 = new Rectangle(centerX + Rectangle.WIDTH + 2, centerY - Rectangle.HEIGHT - 2);
			break;

		case 2:
			rectangle1 = new Rectangle(centerX, centerY - Rectangle.HEIGHT - 2);
			rectangle2 = new Rectangle(centerX, centerY + Rectangle.HEIGHT + 2);
			rectangle3 = new Rectangle(centerX + Rectangle.WIDTH + 2, centerY + Rectangle.HEIGHT + 2);
			break;

		case 3:
			rectangle1 = new Rectangle(centerX + Rectangle.WIDTH + 2, centerY);
			rectangle2 = new Rectangle(centerX - Rectangle.WIDTH - 2, centerY);
			rectangle3 = new Rectangle(centerX - Rectangle.WIDTH - 2, centerY + Rectangle.HEIGHT + 2);
			break;

		default:
			break;
		}

		rectangle1.setCssColor(cssColor);
		rectangle2.setCssColor(cssColor);
		rectangle3.setCssColor(cssColor);
		result.add(rectangle1);
		result.add(rectangle2);
		result.add(rectangle3);
		result.add(gravityCenter);
		return result;
	}

}
