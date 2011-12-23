package com.sfeir.tutorials.client.tetriselement;

import com.google.gwt.canvas.dom.client.CssColor;

/**
 * 
 * This Class modelize is the Rectangle elementary shape allowing to construct
 * any tetris element.
 * 
 * @author Oussama Zoghlami
 * @date 19/12/2011
 * 
 */
public class Rectangle {

	public final static double WIDTH = 25;
	public final static double HEIGHT = 25;

	private double x;
	private double y;
	private boolean isGravityCenter = false;
	private CssColor cssColor;

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Rectangle(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param cssColor
	 */
	public Rectangle(double x, double y, CssColor cssColor) {
		this.x = x;
		this.y = y;
		this.cssColor = cssColor;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isGravityCenter() {
		return isGravityCenter;
	}

	public void setGravityCenter(boolean isGravityCenter) {
		this.isGravityCenter = isGravityCenter;
	}

	public void setCssColor(CssColor cssColor) {
		this.cssColor = cssColor;
	}

	public CssColor getCssColor() {
		return cssColor;
	}

}
