/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package datadraw;

import processing.core.*;
import processing.event.*;


/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 */
public class MouseControl
{	
	// references
	private PApplet parent;

	// helper variables
	private PVector mouse_on_press = new PVector();
	private boolean mouse_clicked = false;
	private boolean mouse_down = false;
	private boolean mouse_pressed_once = false;
	private boolean mouse_dragged = false;
	private float mouse_wheel = 0; 
	
	
	// CONSTRUCTOR

	/**
	 * Call this in the setup() method.
	 */
	public MouseControl(PApplet parent)
	{
		this.parent = parent;
		
		parent.registerMethod("mouseEvent", this);
	}
	
	
	// PUBLIC ACCESSORS
	
	/**
	 * Return the screen coordinates (pixels) of the mouse.
	 * 
	 * @return PVector
	 */
	public PVector Mouse()
	{
		return new PVector(parent.mouseX, parent.mouseY); 
	}
	public float MouseWheel()
	{
		return mouse_wheel;
	}
	public boolean MouseClicked()
	{
		return mouse_clicked;
	}
	public boolean MouseLeftClicked()
	{
		return mouse_clicked && parent.mouseButton == PApplet.LEFT;
	}
	public boolean MouseRightClicked()
	{
		return mouse_clicked && parent.mouseButton == PApplet.RIGHT;
	}
	public boolean MouseDown()
	{
		return mouse_down;
	}
	public boolean MouseLeftDown()
	{
		return mouse_down && parent.mouseButton == PApplet.LEFT;
	}
	public boolean MouseRightDown()
	{
		return mouse_down && parent.mouseButton == PApplet.RIGHT;
	}
	public boolean MousePressedOnce()
	{
		return mouse_pressed_once;
	}
	public boolean MouseLeftPressedOnce()
	{
		return mouse_pressed_once && parent.mouseButton == PApplet.LEFT;
	}
	public boolean MouseRightPressedOnce()
	{
		return mouse_pressed_once && parent.mouseButton == PApplet.RIGHT;
	}
	public boolean MouseDragged()
	{
		return mouse_dragged;
	}
	public boolean MouseLeftDragged()
	{
		return parent.mouseButton == PApplet.LEFT && mouse_dragged;
	}
	public boolean MouseRightDragged()
	{
		return parent.mouseButton == PApplet.RIGHT && mouse_dragged;
	}
	public PVector MouseDragDelta()
	{		
		return MouseDragged() ? PVector.sub(Mouse(), mouse_on_press) : new PVector();
	}
	public float MouseDragDistance()
	{
		return MouseDragged() ? PVector.dist(Mouse(), mouse_on_press) : 0;
	}
	
	
	// PAPPLET INPUT METHODS
	
	public void mouseEvent(MouseEvent e)
	{				
		int a = e.getAction();
		
		if (a == MouseEvent.CLICK)
		{
			mouse_clicked = true;
		}
		if (a == MouseEvent.RELEASE)
		{
			mouse_dragged = false;
			mouse_down = false;
		}
		else if (a == MouseEvent.DRAG)
		{
			mouse_dragged = true;
		}
		else if (a == MouseEvent.PRESS)
		{
			mouse_down = true;
			mouse_pressed_once = true;
			mouse_on_press = new PVector(parent.mouseX, parent.mouseY);
		}
		else if (a == MouseEvent.WHEEL)
		{
			mouse_wheel = e.getCount();
		}
	}
	
	
	/**
	 * Currently MUST be called at the end of the draw loop for this library to work.
	 * 
	 */
	public void AfterDraw()
	{
		mouse_clicked = false;
		mouse_wheel = 0;
		mouse_pressed_once = false;
	}
}

