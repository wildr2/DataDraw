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

package datadraw.editor;

import java.io.PrintWriter;

import datadraw.Helper;
import datadraw.MouseControl;
import processing.core.*;


/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 */
class EditorElement
{			
	// references
	protected Editor parent;
	protected MouseControl mcontrol;
	protected PApplet p_app;
	
	// name
	private String name = "Untitled";
	
	// heading interaction
	private float heading_bounds_width;
	private float heading_bounds_height;
	private boolean heading_mouse_over = false;
	
	// position, size
	// local_pos is the top left corner of the element's heading relative to the editor's position
	private PVector pos, local_pos;
	
	// collapse / expand
	protected boolean collapsed = true;
	
	
	
	// CONSTRUCTOR

	/**
	 * 
	 */
	public EditorElement(String name, Editor parent, MouseControl mcontrol, PApplet p_app)
	{
		this.name = name;
		
		this.parent = parent;
		this.mcontrol = mcontrol;
		this.p_app = p_app;
		
		UpdateHeadingBounds();
		UpdatePosition(new PVector());
	}
	
	
	// PUBLIC ACCESSORS

	public boolean HasFocus() { return false; }
	public boolean MouseOver() { return heading_mouse_over; }
	
	public String GetName() { return name; }
	public PVector GetPos()
	{ 
		return pos;
	}
	public PVector GetContentStartPos() 
	{ 			
		return PVector.add(pos, 
				new PVector(EditorStyle.IndentElement(), EditorStyle.GetTextLineHeight()
						+ EditorStyle.gap_heading_content));
	}
	public float GetHeight()
	{
		return collapsed ? EditorStyle.GetTextLineHeight() : 
			EditorStyle.GetTextLineHeight() + EditorStyle.gap_heading_content + EditorStyle.gap_after_content;
	}
	public float GetWidth()
	{
		return pos.x + heading_bounds_width;
	}
	
	
	// SAVING AND LOADING
	
	public void Save(PrintWriter pw) {}
	public void Load(String[] data) {}

	
	// UPDATE AND DRAW
	
	public void UpdatePosition(PVector local_pos)
	{
		this.local_pos = new PVector(local_pos.x, local_pos.y);
		pos = PVector.add(parent.GetPos(), local_pos);
	}
	public void OnTextSizeChange()
	{
		UpdateHeadingBounds();
	}
	public void UpdateHeadingBounds()
	{
		heading_bounds_width = p_app.textWidth(name) + EditorStyle.IndentElement();
		heading_bounds_height = EditorStyle.GetTextLineHeight() + 3;
	}
	
	protected void UpdateContent() { }
	protected void DrawContent() { }
	
	/**
	 * Returns whether the element was collapsed or expanded in this update call
	 */
	public final boolean Update()
	{			
		// heading mouse over
		heading_mouse_over = Helper.PointInRect(pos, heading_bounds_width,
				heading_bounds_height, mcontrol.Mouse());		
		
		// collapse / expand
		boolean collapsed_or_expanded = false;
		if (heading_mouse_over && mcontrol.MouseLeftClicked())
		{
			collapsed = !collapsed;
			collapsed_or_expanded = true;
		}
		
		// update content if expanded
		if (!collapsed)
		{
			UpdateContent();
		}
		
		return collapsed_or_expanded;
	}
	public final void Draw()
	{		
		// heading and collapse icon
		p_app.textSize(EditorStyle.GetTextSize());
		p_app.noStroke();
		
		if (heading_mouse_over)
			p_app.fill(EditorStyle.TextValueSelected());
		else p_app.fill(EditorStyle.TextValue());
		
		String s = collapsed ? "+" : "-"; 
		p_app.text(s, pos.x, pos.y + EditorStyle.GetTextLineHeight());
		p_app.text(name, pos.x + EditorStyle.IndentElement(), pos.y + EditorStyle.GetTextLineHeight());
		
		
		// draw content
		if (!collapsed)
		{
			DrawContent();
		}
	}

}

