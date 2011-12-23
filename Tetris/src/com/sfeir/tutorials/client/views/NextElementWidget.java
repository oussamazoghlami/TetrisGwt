package com.sfeir.tutorials.client.views;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.sfeir.tutorials.client.tetriselement.Rectangle;
import com.sfeir.tutorials.client.tetriselement.TetrisElement;

/**
 * This is the Nexy Element Widget.
 * 
 * <p>
 * This widget contain a canvas allowing to display the Tetris's next Element
 * 
 * @author Oussama Zoghlami
 * @date 21/12/2011
 * 
 */
public class NextElementWidget extends Composite {
	
	private static final String CANVAS_NOT_SUPPORTED = "HTML 5 is not supported by your browser";

	private Canvas canvas;
	private Context2d context;

	public NextElementWidget() {
		canvas = Canvas.createIfSupported();
		if (null != canvas) {
			initializeCanvas();
			context = canvas.getContext2d();
		} else {
			Window.alert(CANVAS_NOT_SUPPORTED);
		}
	}

	/**
	 * Method allowing to display a Tetris Element on the canvas
	 * 
	 * @param tetrisElement
	 */
	public void displayNextElement(TetrisElement tetrisElement) {
		List<Rectangle> rectangles = tetrisElement.getRectangles();
		context.clearRect(0, 0, Rectangle.WIDTH * 6 + 6 * 2, Rectangle.HEIGHT * 6 + 6 * 2);
		for (Rectangle rectangle : rectangles) {
			context.setFillStyle(rectangle.getCssColor());
			context.fillRect(rectangle.getX() - 2 * Rectangle.WIDTH - 2 * 2, rectangle.getY() + 2 * Rectangle.WIDTH + 2
					* 2, Rectangle.WIDTH, Rectangle.HEIGHT);
		}

	}

	/**
	 * Initializing the widget's canvas
	 */
	private void initializeCanvas() {
		Double canvasWidth = Rectangle.WIDTH * 7 + 2 * 7;
		Double canvasHeight = Rectangle.HEIGHT * 6 + 2 * 6;
		canvas.setWidth(canvasWidth + "px");
		canvas.setHeight(canvasHeight + "px");
		canvas.setCoordinateSpaceWidth(canvasWidth.intValue());
		canvas.setCoordinateSpaceHeight(canvasHeight.intValue());
		initWidget(canvas);
	}

	public Canvas getCanvas() {
		return canvas;
	}

}
