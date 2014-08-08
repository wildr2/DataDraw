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
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
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
	 * Call this in the setup() method.
	 */
	public CameraControl(PApplet parent, MouseControl mcontrol)
	{
		this.parent = parent;
		this.mcontrol = mcontrol;
		
		parent.registerMethod("pre", this);
	}
	
	public PVector ToWorldSpace(PVector p)
	{
		if (zoom <= 0)
		{
			System.out.println("Error: zoom should not be <= 0");
			return new PVector();
		}
		return new PVector((p.x - pan.x) / zoom, (p.y - pan.y) / zoom);
	}
	public PVector ToScreenSpace(PVector p)
	{
		return new PVector(p.x * zoom + pan.x, p.y * zoom + pan.y);
	}
	public void ApplyPanTranslation()
	{
		parent.translate(pan.x, pan.y);
	}
	public void ApplyZoomScale()
	{
		parent.scale(zoom);
	}
	
	// PUBLIC ACCESSORS
	
	public PVector GetPan() { return pan; }
	public float GetZoom() { return zoom; }
	
	public boolean IsPanning() { return panning; }
	
	
	// PUBLIC MODIFIERES
	
	public void EnablePanning()
	{ 
		if (panning_disabled_count > 0) --panning_disabled_count;
		if (panning_disabled_count == 0) panning_enabled = true;
	}
	public void DisablePanning()
	{ 
		++panning_disabled_count;
		if (panning_disabled_count == 1)
		{
			panning_enabled = false;
			panning = false;
		}
	}
	
	public void EnableZoom()
	{ 
		if (zooming_disabled_count > 0) --zooming_disabled_count;
		if (zooming_disabled_count == 0) zooming_enabled = true;
		
		zooming_enabled = true;
	}
	public void DisableZooming()
	{ 
		++zooming_disabled_count;
		
		if (zooming_disabled_count == 1)
		{
			zooming_enabled = false;
		}
	}
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

