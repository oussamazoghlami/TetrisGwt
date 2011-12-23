package com.sfeir.tutorials.client.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.sfeir.tutorials.client.tetriselement.Rectangle;
import com.sfeir.tutorials.client.tetriselement.TetrisElement;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl1;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl2;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl3;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl4;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl5;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl6;
import com.sfeir.tutorials.client.tetriselement.TetrisElementImpl7;

/**
 * This is the widget containing all the Tetris Game Logic.
 * 
 * @author Oussama Zoghlami
 * @date 19/12/2011
 * 
 */
public class TetrisGameWidget extends Composite {

	private static final String CANVAS_NOT_SUPPORTED = "HTML 5 is not supported by your browser";
	private static final String GAME_OVER = "Game Over !";
	private static final int RIGHT_KEY_CODE = 39;
	private static final int LEFT_KEY_CODE = 37;
	private static final int UP_KEY_CODE = 38;
	private static final int DOWN_KEY_CODE = 40;
	private static final int ESPACE_KEY_CODE = 32;
	private static final int ACCELERATION_RATE = 50;
	private static final int PIECES_TO_ACCELERATE = 20;
	private static final int LINE_COMPLETED_TRANSLATION_RATE = 11;

	private int timerIntervall = 500;
	private TetrisView tetrisView;
	private Canvas canvas;
	private Context2d context;
	private TetrisElement currentElement;
	private TetrisElement nextElement;
	private List<Rectangle> fixElements = new ArrayList<Rectangle>();
	private Timer timer;
	private int score = 0;
	private int playedPieces = 0;
	private boolean isGameOver = false;
	private int gameOverY = 0;

	/**
	 * 
	 * @param tetrisView
	 */
	public TetrisGameWidget(TetrisView tetrisView) {
		this.tetrisView = tetrisView;
		canvas = Canvas.createIfSupported();
		if (null != canvas) {
			initializeCanvas();
			currentElement = getRandomElement();
			currentElement.refreshWidget();
			nextElement = getRandomElement();
			this.tetrisView.getNextElementWidget().displayNextElement(nextElement);
			initializeTimer();
		} else {
			Window.alert(CANVAS_NOT_SUPPORTED);
		}
	}

	/**
	 * Method allowing to preparing the display of the next Tetris Element
	 */
	public void displayNextElement() {
		if (!checkGameOver()) {
			updateFixElements();
			checkCompleteLines();
			currentElement = nextElement;
			currentElement.refreshWidget();
			nextElement = getRandomElement();
			this.tetrisView.getNextElementWidget().displayNextElement(nextElement);
			playedPieces++;
			updateAccerationRate();
		} else {
			timer.cancel();
			clearWidget();
		}
	}

	/**
	 * Method allowing to clear the canvas when the game will finish
	 */
	private void clearWidget() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				context.clearRect(0, 0, Rectangle.WIDTH * 10 + 10 * 2, Rectangle.HEIGHT * 20 + 2 * 20);
				List<Rectangle> recToDelete = new ArrayList<Rectangle>();
				for (Rectangle rectangle : fixElements) {

					if (rectangle.getX() < 0) {
						recToDelete.add(rectangle);
					} else if (rectangle.getX() < (TetrisElement.LAST_COLOMN_X / 2)) {
						rectangle.setX(rectangle.getX() - 3);
						context.setFillStyle(rectangle.getCssColor());
						context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
					} else if ((rectangle.getX() >= (TetrisElement.LAST_COLOMN_X / 2))
							&& (rectangle.getX() < TetrisElement.LAST_COLOMN_X)) {
						rectangle.setX(rectangle.getX() + 3);
						context.setFillStyle(rectangle.getCssColor());
						context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
					} else {
						recToDelete.add(rectangle);
					}
				}
				fixElements.removeAll(recToDelete);
				if (fixElements.size() == 0) {
					cancel();
					displayGameOverMsg();
				}
			}

		};
		timer.scheduleRepeating(15);
	}

	/**
	 * Display Game Over Msg
	 */
	private void displayGameOverMsg() {
		context.setFillStyle("black");
		context.setFont("italic 30px sans-serif");

		Timer timer = new Timer() {

			@Override
			public void run() {
				context.clearRect(0, 0, Rectangle.WIDTH * 10 + 10 * 2, Rectangle.HEIGHT * 20 + 2 * 20);
				context.fillText(GAME_OVER, 50, gameOverY);
				gameOverY += 3;
				if (gameOverY > 300) {
					cancel();
				}
			}
		};
		timer.scheduleRepeating(15);
	}

	/**
	 * Method allowing to initialize the canvas
	 */
	private void initializeCanvas() {
		Double canvasWidth = Rectangle.WIDTH * 10 + 2 * 10;
		Double canvasHeight = Rectangle.HEIGHT * 20 + 2 * 20;
		canvas.setWidth(canvasWidth + "px");
		canvas.setHeight(canvasHeight + "px");
		canvas.setCoordinateSpaceWidth(canvasWidth.intValue());
		canvas.setCoordinateSpaceHeight(canvasHeight.intValue());
		context = canvas.getContext2d();
		addCanvasKeyListener();
		// initCanvasLines();
		initWidget(canvas);
	}

	/**
	 * Method allowing to check if the game is over
	 * 
	 * @return True if the game is over
	 */
	private boolean checkGameOver() {
		double minY = currentElement.getMinLine();
		if (minY == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method allowing to update the Buttom Translation Acceleration Rate
	 * according to the played Tetris Elements
	 */
	private void updateAccerationRate() {
		if (playedPieces == PIECES_TO_ACCELERATE) {
			if (timerIntervall - ACCELERATION_RATE > 150) {
				timerIntervall = timerIntervall - ACCELERATION_RATE;
				timer.scheduleRepeating(timerIntervall);
			}
			playedPieces = 0;
		}

	}

	/**
	 * Method allowing to check if there is some completed lines
	 */
	private void checkCompleteLines() {
		double maxLine = currentElement.getMaxLine();
		double minLine = currentElement.getMinLine();
		List<Double> lineToDelete = new ArrayList<Double>();
		for (double d = minLine; d <= maxLine; d += Rectangle.HEIGHT + 2) {
			int compteur = 0;
			for (Rectangle rectangle : fixElements) {
				if (rectangle.getY() == d) {
					compteur++;
				}
			}
			if (compteur == 10) {
				lineToDelete.add(d);
			}
		}

		for (Double line : lineToDelete) {
			deleteLine(line);
		}
		updateScore(lineToDelete.size());

	}

	/**
	 * Method allowing to update the Score after completing a given number of
	 * line
	 * 
	 * @param completedLineNumber
	 */
	private void updateScore(int completedLineNumber) {
		switch (completedLineNumber) {
		case 1:
			score += 100;
			break;
		case 2:
			score += 300;
			break;
		case 3:
			score += 600;
			break;
		case 4:
			score += 1000;
			break;

		default:
			break;
		}
		this.tetrisView.getScore().setText(score + "");
	}

	/**
	 * Method allowing to delete a given line when completed
	 * 
	 * @param y
	 */
	private void deleteLine(double y) {
		List<Rectangle> recToDelete = new ArrayList<Rectangle>();
		for (Rectangle rectangle : fixElements) {
			if (rectangle.getY() == y) {
				recToDelete.add(rectangle);
			}
		}
		fixElements.removeAll(recToDelete);
		makeTranslationAnimation(y, recToDelete);
	}

	/**
	 * 
	 * Method allowing to make a translation animation when a line is completed
	 * 
	 * @param y
	 * @param recToDelete
	 */
	private void makeTranslationAnimation(final double y, final List<Rectangle> recToDelete) {
		Timer timer = new Timer() {

			@Override
			public void run() {
				context.clearRect(0, y, Rectangle.WIDTH * 10 + 10 * 2, Rectangle.HEIGHT + 2);
				List<Rectangle> recs = new ArrayList<Rectangle>();
				for (Rectangle rectangle : recToDelete) {
					if (rectangle.getX() < 0) {
						recs.add(rectangle);
					} else if (rectangle.getX() < (TetrisElement.LAST_COLOMN_X / 2)) {
						rectangle.setX(rectangle.getX() - LINE_COMPLETED_TRANSLATION_RATE);
						context.setFillStyle(rectangle.getCssColor());
						context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
					} else if ((rectangle.getX() >= (TetrisElement.LAST_COLOMN_X / 2))
							&& (rectangle.getX() < TetrisElement.LAST_COLOMN_X)) {
						rectangle.setX(rectangle.getX() + LINE_COMPLETED_TRANSLATION_RATE);
						context.setFillStyle(rectangle.getCssColor());
						context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
					} else {
						recs.add(rectangle);
					}
				}

				recToDelete.removeAll(recs);
				if (recToDelete.size() == 0) {
					for (Rectangle rectangle : fixElements) {
						if (rectangle.getY() < y) {
							context.clearRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
						}
					}

					for (Rectangle rectangle : fixElements) {
						if (rectangle.getY() < y) {
							rectangle.setY(rectangle.getY() + Rectangle.HEIGHT + 2);
							context.setFillStyle(rectangle.getCssColor());
							context.fillRect(rectangle.getX(), rectangle.getY(), Rectangle.WIDTH, Rectangle.HEIGHT);
						}
					}
					cancel();
				}
			}
		};
		timer.scheduleRepeating(10);
	}

	/**
	 * Method allowing to get A Random Tetris Element
	 * 
	 * @return Random Tetris Element
	 */
	private TetrisElement getRandomElement() {
		TetrisElement result = null;
		Random generator = new Random();
		int randomIndex = generator.nextInt(7);
		switch (randomIndex) {
		case 0:
			result = new TetrisElementImpl1(context);
			break;

		case 1:
			result = new TetrisElementImpl2(context);
			break;

		case 2:
			result = new TetrisElementImpl3(context);
			break;

		case 3:
			result = new TetrisElementImpl4(context);
			break;

		case 4:
			result = new TetrisElementImpl5(context);
			break;

		case 5:
			result = new TetrisElementImpl6(context);
			break;

		case 6:
			result = new TetrisElementImpl7(context);
			break;

		default:
			result = new TetrisElementImpl1(context);
			break;
		}

		result.setTetrisWidget(this);
		return result;
	}

	/**
	 * Method allowing to update the widget's fix Elements, after fixing a
	 * Tetris Element
	 */
	private void updateFixElements() {
		for (Rectangle rectangle : currentElement.getRectangles()) {
			fixElements.add(rectangle);
		}

	}

	// /**
	// *
	// */
	// private void initCanvasLines() {
	// for (double y = Rectangle.HEIGHT + 2; y <= TetrisElement.LAST_LINE_Y; y
	// += Rectangle.HEIGHT + 2) {
	// context.beginPath();
	// context.setLineWidth(0.3);
	// context.moveTo(0, y - 1);
	// context.lineTo(TetrisElement.LAST_COLOMN_X + Rectangle.WIDTH + 2, y - 1);
	// context.closePath();
	// context.stroke();
	// }
	// }

	/**
	 * Add a key linstener on the canvas
	 */
	private void addCanvasKeyListener() {
		canvas.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (!isGameOver) {
					if (event.getNativeKeyCode() == RIGHT_KEY_CODE) {
						currentElement.translateRight();
					} else if (event.getNativeKeyCode() == LEFT_KEY_CODE) {
						currentElement.translateLeft();
					} else if (event.getNativeKeyCode() == UP_KEY_CODE) {
						currentElement.rotateUp();
					} else if (event.getNativeKeyCode() == DOWN_KEY_CODE) {
						currentElement.rotateDown();
					} else if (event.getNativeKeyCode() == ESPACE_KEY_CODE) {
						currentElement.translateButtom();
					}
				}
			}
		});
	}

	/**
	 * Initializing the Timer
	 */
	private void initializeTimer() {
		timer = new Timer() {
			@Override
			public void run() {
				currentElement.translateButtom();
			}
		};
		timer.scheduleRepeating(timerIntervall);
	}

	public List<Rectangle> getFixElements() {
		return fixElements;
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
