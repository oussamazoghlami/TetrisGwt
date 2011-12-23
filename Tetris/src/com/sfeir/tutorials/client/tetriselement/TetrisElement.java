package com.sfeir.tutorials.client.tetriselement;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.sfeir.tutorials.client.views.TetrisGameWidget;

/**
 * 
 * This is the Tetris Element Abstract Class.
 * 
 * <p>
 * Every Tetris element will extends this class, that contain and define the
 * necessary functions allowing to describe the Tetris elements, to rotate them,
 * translate them to the left, right, buttom ...
 * 
 * @author Oussama Zoghlami
 * @date 19/12/2011s
 * 
 */
public abstract class TetrisElement {

	public static final double LAST_LINE_Y = Rectangle.HEIGHT * 19 + 2 * 19;
	public static final double LAST_COLOMN_X = Rectangle.WIDTH * 9 + 2 * 9;

	protected List<Rectangle> rectangles = new ArrayList<Rectangle>();
	protected CssColor cssColor;
	protected Context2d context;
	protected TetrisGameWidget tetrisWidget;
	protected int rotationId = 0;

	/**
	 * Abstract Method allowing to initialize the list of {@link Rectangle}
	 * presenting the Tetris Element
	 */
	protected abstract void initWidgetPoints();

	/**
	 * Abstract Method allowing to rotate the tetris element to the UP
	 */
	public abstract void rotateUp();

	/**
	 * Abstract Method allowing to rotate the tetris element to the Down
	 */
	public abstract void rotateDown();

	/**
	 * Method allowing to translate the Tetris element to the right
	 */
	public void translateRight() {
		if (checkRightTranslation()) {
			for (Rectangle rectangle : rectangles) {
				context.clearRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
				rectangle.setX(rectangle.getX() + Rectangle.WIDTH + 2);
			}
			refreshWidget();
		}
	}

	/**
	 * Method allowing to translate the Tetris element to the left
	 */
	public void translateLeft() {
		if (checkLeftTranslation()) {
			for (Rectangle rectangle : rectangles) {
				context.clearRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
				rectangle.setX(rectangle.getX() - Rectangle.WIDTH - 2);
			}
			refreshWidget();
		}
	}

	/**
	 * Method allowing to translate the Tetris element to the buttom
	 */
	public void translateButtom() {
		if (checkButtomTranslation()) {
			for (Rectangle rectangle : rectangles) {
				context.clearRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
				rectangle.setY(rectangle.getY() + Rectangle.HEIGHT + 2);
			}
			refreshWidget();
		} else {
			tetrisWidget.displayNextElement();
		}
	}
	
	/**
	 * Method allowing to refresh the Tetris element by redisplaying its list of
	 * {@link Rectangle}
	 */
	public void refreshWidget() {
		context.setFillStyle(cssColor);
		for (Rectangle rectangle : rectangles) {
			context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
		}
	}

	/**
	 * Method allowiong to Clear the Tetris Element by deleting its list of
	 * {@link Rectangle}
	 */
	public void clearWidget() {
		for (Rectangle rectangle : rectangles) {
			context.clearRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
		}
	}
	
	/**
	 * Method allowing to return the minimal Y Coordinate of the Tetris Element
	 * 
	 * @return Min Y
	 */
	public double getMinLine() {
		double minLine = rectangles.get(0).getY();
		for (Rectangle rectangle : rectangles) {
			if (rectangle.getY() < minLine) {
				minLine = rectangle.getY();
			}
		}
		return minLine;
	}

	/**
	 * Method allowing to return the maximal Y Coordinate of the Tetris Element
	 * 
	 * @return Max Y
	 */
	public double getMaxLine() {
		double maxLine = rectangles.get(0).getY();
		for (Rectangle rectangle : rectangles) {
			if (rectangle.getY() > maxLine) {
				maxLine = rectangle.getY();
			}
		}
		return maxLine;
	}
	
	/**
	 * Method allowing to calculate the Tetris's new list of {@link Rectangle}
	 * after making a rotation
	 * 
	 * @return the list of the new caluclated {@link Rectangle}
	 */
	protected abstract List<Rectangle> getRotatedRectangles();

	/**
	 * Method allowing to rotate the Tetris element according to the rotation Id
	 */
	protected void rotatePoints() {
		List<Rectangle> rotatedRectangles = getRotatedRectangles();
		if (checkRotatedPoints(rotatedRectangles)) {
			clearWidget();
			rectangles = getRotatedRectangles();
			refreshWidget();
		}
	}

	/**
	 * Method allowing to check if we could make a rotation on the Tetris
	 * element, according to the list of the new rotated {@link Rectangle}
	 * 
	 * @param rotRectangles
	 * @return True if we could rotate the element
	 */
	protected boolean checkRotatedPoints(List<Rectangle> rotRectangles) {
		for (Rectangle rectangle : rotRectangles) {

			if ((rectangle.getX() < 0) || (rectangle.getX() > LAST_COLOMN_X)) {
				return false;
			}

			if ((rectangle.getY() < 0) || (rectangle.getY() > LAST_LINE_Y)) {
				return false;
			}

			for (Rectangle fixRectangle : tetrisWidget.getFixElements()) {
				if ((rectangle.getY() == fixRectangle.getY()) && (rectangle.getX() == fixRectangle.getX())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Method allowing to check if we could translate the Tetris Element to the
	 * Buttom
	 * 
	 * @return true if we could tranlate the Element
	 */
	private boolean checkButtomTranslation() {
		return checkButtomTranslationLimit() && checkButtomTranslationCollision();
	}

	/**
	 * Method allowing to check that if we translate the Tetris Element to the
	 * Buttom, it will not interferate with another Tetris Element (fixed on the
	 * {@link TetrisGameWidget})
	 * 
	 * @return True if there is no collision
	 */
	private boolean checkButtomTranslationCollision() {
		for (Rectangle rectangle : rectangles) {
			for (Rectangle fixRectangle : tetrisWidget.getFixElements()) {
				if ((rectangle.getY() + Rectangle.HEIGHT + 2 == fixRectangle.getY())
						&& (rectangle.getX() == fixRectangle.getX())) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Method allowing that the Tetris Element will not overtake the Limit
	 * height when translating it to the buttom
	 * 
	 * @return True if the Tetris will not overtake the limit
	 */
	private boolean checkButtomTranslationLimit() {
		for (Rectangle rectangle : rectangles) {
			if (rectangle.getY() == LAST_LINE_Y) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method allowing to check if we could tranlsate the Tetris Element to the
	 * right
	 * 
	 * @return True if we could translate it to the right
	 */
	private boolean checkRightTranslation() {
		return checkRightLimit() && checkRightTranslationCollision();
	}

	/**
	 * Method allowing to check that if we translate the Tetris Element to the
	 * Right, it will not interferate with another Tetris Element (fixed on the
	 * {@link TetrisGameWidget})
	 * 
	 * @return True if there is no collision
	 */
	private boolean checkRightTranslationCollision() {
		for (Rectangle rectangle : rectangles) {
			for (Rectangle fixRectangle : tetrisWidget.getFixElements()) {
				if ((rectangle.getX() + Rectangle.WIDTH + 2 == fixRectangle.getX())
						&& (rectangle.getY() == fixRectangle.getY())) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Method allowing that the Tetris Element will not overtake the Limit when
	 * translating it to the right
	 * 
	 * @return True if the Tetris will not overtake the limit
	 */
	private boolean checkRightLimit() {
		for (Rectangle rectangle : rectangles) {
			if (rectangle.getX() == LAST_COLOMN_X) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method allowing to check if we could tranlsate the Tetris Element to the
	 * left
	 * 
	 * @return True if we could translate it to the left
	 */
	private boolean checkLeftTranslation() {
		return checkLeftLimit() && checkLeftTranslationCollision();
	}

	/**
	 * Method allowing to check that if we translate the Tetris Element to the
	 * Left, it will not interferate with another Tetris Element (fixed on the
	 * {@link TetrisGameWidget})
	 * 
	 * @return True if there is no collision
	 */
	private boolean checkLeftTranslationCollision() {
		for (Rectangle rectangle : rectangles) {
			for (Rectangle fixRectangle : tetrisWidget.getFixElements()) {
				if ((rectangle.getX() - Rectangle.WIDTH - 2 == fixRectangle.getX())
						&& (rectangle.getY() == fixRectangle.getY())) {
					return false;
				}
			}

		}
		return true;
	}

	/**
	 * Method allowing that the Tetris Element will not overtake the Limit when
	 * translating it to the left
	 * 
	 * @return True if the Tetris will not overtake the limit
	 */
	private boolean checkLeftLimit() {
		for (Rectangle rectangle : rectangles) {
			if (rectangle.getX() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Method allowing to return the Gravity Center Rectangle associated to the
	 * Tetris Element
	 * 
	 * @return Rectangle
	 */
	protected Rectangle getGravityCenter() {
		for (Rectangle rectangle : rectangles) {
			if (rectangle.isGravityCenter()) {
				return rectangle;
			}
		}
		return null;
	}

	public List<Rectangle> getRectangles() {
		return rectangles;
	}

	public void setTetrisWidget(TetrisGameWidget tetrisWidget) {
		this.tetrisWidget = tetrisWidget;
	}

}
