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


/**
 * Simple 2D camera functionality for panning and zooming around a scene. Plays nice with datadraw.editor.Editor.
 * Ideal for quickly setting up a 2D visualization that you can 'explore'.
 * 
 */
public class CameraControl
{	
	// references
	private PApplet parent;
	private MouseControl mcontrol;
	
	// panning
	private boolean panning_enabled = true;
	private int panning_disabled_count = 0; // number of calls to DisablePanning minus the number of calls to EnablePanning (> 0)
	
	private PVector pan = new PVector(0, 0);
	private PVector initial_pan = new PVector(); // initial pan before mouse drag
	private boolean panning = false; // panning in progress
	
	// zooming
	private boolean zooming_enabled = true;
	private int zooming_disabled_count = 0; // number of calls to DisableZooming minus the number of calls to EnableZooming (> 0)
	
	private float zoom = 1; // current scale adjusted with the mouse wheel
	private float zoom_power = 0.1f; // speed of zooming (0 to 1)
	
	
	// CONSTRUCTOR

	/**
	 * Call this in 'setup'.
	 */
	public CameraControl(PApplet parent, MouseControl mcontrol)
	{
		this.parent = parent;
		this.mcontrol = mcontrol;
		
		parent.registerMethod("pre", this);
	}
	
	/**
	 * Transforms a point from screen coordinates (in pixels) to coordinates modified by
	 * the panning and zooming CameraControl enables.
	 * 
	 * @parem p
	 *  		A point in screen coordinates.
	 * 
	 * @return PVector
	 */
	public PVector ToWorldSpace(PVector p)
	{
		if (zoom <= 0)
		{
			System.out.println("Error: zoom should not be <= 0");
			return new PVector();
		}
		return new PVector((p.x - pan.x) / zoom, (p.y - pan.y) / zoom);
	}
	/**
	 * Transforms a point from world coordinates (modifed by panning and zooming)
	 * to screen coordinates (in pixels).
	 * 
	 * @parem p
	 *  		A point in world coordinates.
	 * 
	 * @return PVector
	 */
	public PVector ToScreenSpace(PVector p)
	{
		return new PVector(p.x * zoom + pan.x, p.y * zoom + pan.y);
	}
	/**
	 * Do a translation (using Processing's translate) to put panning into affect.
	 */
	public void ApplyPanTranslation()
	{
		parent.translate(pan.x, pan.y);
	}
	/**
	 * Do a scale (using Processing's scale) to put zooming into affect.
	 */
	public void ApplyZoomScale()
	{
		parent.scale(zoom);
	}
	
	// PUBLIC ACCESSORS
	
	/**
	 * Get a vector describing the current mouse pan.
	 * 
	 * @return pVector
	 */
	public PVector GetPan() { return pan; }
	/**
	 * Get a float describing the zoom scale.
	 * 
	 * @return pVector
	 */
	public float GetZoom() { return zoom; }
	
	/**
	 * Is the mouse currently panning (left mouse button being dragged).
	 * 
	 * @return boolean
	 */
	public boolean IsPanning() { return panning; }
	
	
	// PUBLIC MODIFIERES
	
	/**
	 * Allow left mouse button drag to control panning. Note that ApplyPanTranslation
	 * must be called before drawing objects you want to be affected by panning.
	 */
	public void EnablePanning()
	{ 
		if (panning_disabled_count > 0) --panning_disabled_count;
		if (panning_disabled_count == 0) panning_enabled = true;
	}
	/**
	 * Prevent left mouse button drag from controlling panning.
	 */
	public void DisablePanning()
	{ 
		++panning_disabled_count;
		if (panning_disabled_count == 1)
		{
			panning_enabled = false;
			panning = false;
		}
	}
	
	/**
	 * Allow the mouse wheel (or other scrolling input) to control zooming. Note that ApplyZoomScale
	 * must be called before drawing objects you want to be affected by zooming.
	 */
	public void EnableZoom()
	{ 
		if (zooming_disabled_count > 0) --zooming_disabled_count;
		if (zooming_disabled_count == 0) zooming_enabled = true;
		
		zooming_enabled = true;
	}
	/**
	 * Prevent the mouse wheel (or other scrolling input) from controling zooming. Note that ApplyZoomScale
	 * must be called before drawing objects you want to be affected by zooming.
	 */
	public void DisableZooming()
	{ 
		++zooming_disabled_count;
		
		if (zooming_disabled_count == 1)
		{
			zooming_enabled = false;
		}
	}
	/**
	 * Set how quickly you can zoom in or out. 
	 * 
	 * @Parem power
	 * 			speed of zooming between 0 and 1, 1 being the fastest - default is 0.1f
	 */
	public void SetZoomPower(float power) 
	{ 
		zoom_power = power;
		zoom_power = zoom_power > 1 ? 1 : zoom_power < 0 ? 0 : zoom_power;
	}
	
	
	// UPDATE
	
	private void UpdateZooming()
	{
		if (zooming_enabled && mcontrol.MouseWheel() != 0)
		{
			PVector m = ToWorldSpace(mcontrol.Mouse());
			float old_zoom = zoom;
			
			zoom *= mcontrol.MouseWheel() > 0 ? (1f - zoom_power) : (1f + zoom_power);
			
			float zoom_delta = zoom - old_zoom;
			pan.x -= zoom_delta * m.x;
			pan.y -= zoom_delta * m.y;
		}
	}
	private void UpdatePanning()
	{		
		if (panning_enabled && mcontrol.MouseLeftDragged())
		{
			PVector delta_mouse = mcontrol.MouseDragDelta();
			
			pan = new PVector();
			pan.x = initial_pan.x + delta_mouse.x;
			pan.y = initial_pan.y + delta_mouse.y;
		}
	}
	
	public void pre()
	{		
		// panning begin
		if (panning_enabled && mcontrol.MouseLeftPressedOnce())
		{
			initial_pan = new PVector(pan.x, pan.y);
			panning = true;
		}
		// panning end
		if (panning && !mcontrol.MouseDown()) panning = false;
		
		
    	UpdatePanning();
    	UpdateZooming();
	}
}

