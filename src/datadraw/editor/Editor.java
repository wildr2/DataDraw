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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import datadraw.CameraControl;
import datadraw.Helper;
import datadraw.MouseControl;
import processing.core.*;


/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 */
public class Editor
{	
	// references
	private PApplet parent;
	private MouseControl mcontrol;
	private CameraControl camcontrol;
	
	// saving and loading
	private String save_file;
	private Map<String, String[]> save_data = new HashMap<String, String[]>();
	
	// elements
	private ArrayList<EditorElement> elements = new ArrayList<EditorElement>();
	
	// other
	private PVector pos = new PVector(10, 25);
	private boolean editor_enabled = true;
	private int disabled_count = 0; // number of calls to DisableEditor minus the number of calls to EnableEditor (> 0)
	
	// interaction
	private EditorElement focused_element = null;
	private boolean has_focus = false; // some part of the editor is being used (eg a slider is grabbed)
	private boolean has_focus_last = false; // last frame mouse_over
	private boolean mouse_over = false; // the mouse is over some part of the editor
	private boolean mouse_over_last = false; // last frame mouse_over
	
	// background
	private int width;
	private int height;
	

	// CONSTRUCTOR

	/**
	 * Call this in the setup() method. If using datadraw.CameraControl, pass a reference to it here; the editor will insure
	 * that camera controls are disabled while the editor has focus.
	 */
	public Editor(PApplet parent, MouseControl mcontrol, CameraControl camcontrol)
	{
		this(parent, mcontrol);
		this.camcontrol = camcontrol;
	}
	public Editor(PApplet parent, MouseControl mcontrol)
	{
		this.parent = parent;
		this.mcontrol = mcontrol;
		
		parent.registerMethod("pre", this);
		parent.registerMethod("dispose", this);
		
		EditorStyle.Initialize(parent);
		
		// load
		save_file = parent.sketchPath + "\\editor_save.txt";
		ReadSaveData(save_file);
	}
	
	
	// PUBLIC ACCESSORS

	public boolean HasFocus() { return has_focus; }
	public boolean MouseOver() { return mouse_over; }
	
	public PVector GetPos() { return pos; }
	
	
	// PUBLIC MODIFIERS
	
	public void EnableEditor()
	{ 
		if (disabled_count > 0) --disabled_count;
		if (disabled_count == 0) editor_enabled = true;
	}
	public void DisableEditor()
	{ 
		++disabled_count;
		
		if (disabled_count == 0)
		{
			editor_enabled = false;
			
			if (MouseOver())
			{
				mouse_over_last = false;
				OnLoseMouseOver();
			}
		}
	}
	public void SetColorScheme(EditorColorScheme scheme)
	{
		EditorStyle.SetColorScheme(scheme);
	}
	
	
	public float[] MakeSlider(String name, float min, float max)
	{
		EditorSliderFloat s = new EditorSliderFloat(name, this, mcontrol, parent, min, max);
		AddElement(s);
		return s.Get();
	}
	public float[] MakeSlider(String name)
	{
		return MakeSlider(name, 0, 1);
	}
	public int[] MakeSliderInt(String name, int min, int max)
	{
		EditorSliderInt s = new EditorSliderInt(name, this, mcontrol, parent, min, max);
		AddElement(s);
		return s.Get();
	}
	public int[] MakeColor(String name)
	{
		EditorColor s = new EditorColor(name, this, mcontrol, parent);
		AddElement(s);
		return s.Get();
	}
	
	public void SetTextSize(int size)
	{
		EditorStyle.SetTextSize(parent, size);
		for (int i = 0; i < elements.size(); ++i)
			elements.get(i).OnTextSizeChange();
		
		UpdateElementPositions();
		UpdateBackgroundSize();
	}
	private void OnElementCollapseExpand()
	{
		UpdateElementPositions();
		UpdateBackgroundSize();
	}
	
	
	// SAVING AND LOADING
	
	private void ReadSaveData(String save_file)
	{
		BufferedReader br = null;
		
		try 
		{
			br = new BufferedReader(new FileReader(save_file));
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("No editor save file.");
			return;
		}
		
		
		String line;

		try 
		{
			while ((line = br.readLine())!=null)
			{
				String[] line_data = line.split(",");
				
				save_data.put(line_data[0], line_data);
			}
			
			br.close();
			
			System.out.println("Editor save data loaded.");
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return;
		}
	}
	private void Save()
	{
		try 
		{
			PrintWriter pw = new PrintWriter(save_file);
			for (int i = 0; i < elements.size(); ++i)
			{
				elements.get(i).Save(pw);
			}
			
			pw.close();
			
			System.out.println("Editor saved.");
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	// OTHER PRIVATE MODIFIERS
	
	private void AddElement(EditorElement e)
	{
		elements.add(e);
		UpdateElementPositions();
		UpdateBackgroundSize();
		
		// load values from data if stored
		String[] d = save_data.get(e.GetName());
		if (d != null)
		{
			e.Load(d);
		}
	}
	
	
	// UPDATE AND DRAW
	
	public void dispose()
	{
		Save();
	}
	
	private void OnGainMouseOver()
	{
		if (camcontrol != null)
		{
			if (!HasFocus())
			{
				camcontrol.DisablePanning();
				camcontrol.DisableZooming();
			}
		}
	}
	private void OnLoseMouseOver()
	{
		if (camcontrol != null)
		{
			if (!HasFocus())
			{
				camcontrol.EnablePanning();
				camcontrol.EnableZoom();
			}
		}
	}
	private void OnGainFocus()
	{
	}
	private void OnLoseFocus()
	{
		if (camcontrol != null)
		{
			if (!MouseOver())
			{
				camcontrol.EnablePanning();
				camcontrol.EnableZoom();
			}
		}
	}
	
	private void UpdateBackgroundSize()
	{
		// width
		int greatest_width = 0;
		for (int i = 0; i < elements.size(); ++i)
			greatest_width = (int)Math.max(greatest_width, elements.get(i).GetWidth());
		
		width = greatest_width + EditorStyle.background_margin;
		
		// height
		if (elements.size() > 0)
		{
			EditorElement last_e = elements.get(elements.size() - 1);
			height = (int)(last_e.GetPos().y + last_e.GetHeight() + EditorStyle.background_margin);
		}
		else height = 0;
	}
	private void UpdateElementPositions()
	{
		// elements
		PVector element_local_pos = new PVector();
		
		for (int i = 0; i < elements.size(); ++i)
		{
			elements.get(i).UpdatePosition(element_local_pos);
			element_local_pos.y += elements.get(i).GetHeight() + EditorStyle.gap_elements;
		}
	}
	private void UpdateFocusedElement()
	{
		if (focused_element.Update()) OnElementCollapseExpand();
		
		// focus
		if (!focused_element.HasFocus())
		{
			has_focus = false;
			focused_element = null;
		}
	}
	private void UpdateAllElements()
	{
		has_focus = false;
		focused_element = null;
		
		for (int i = 0; i < elements.size(); ++i)
		{
			if (elements.get(i).Update()) OnElementCollapseExpand();
			
			// The editor has focus if any of its elements have focus
			if (elements.get(i).HasFocus())
			{
				has_focus = true;
				focused_element = elements.get(i);
			}
			
		}
	}
	private void UpdateGainLoseInteraction()
	{
		// gain and lose focus
		if (HasFocus() && has_focus_last == false)
		{
			OnGainFocus();
		}
		else if (!HasFocus() && has_focus_last == true)
		{
			OnLoseFocus();
		}
		
		// gain and lose mouse over
		if (MouseOver() && mouse_over_last == false)
		{
			OnGainMouseOver();
		}
		else if (!MouseOver() && mouse_over_last == true)
		{
			OnLoseMouseOver();
		}
		
		// remember this frame focus / mouse over
		has_focus_last = HasFocus();
		mouse_over_last = MouseOver();
	}
	
	public void pre()
	{
		if (!editor_enabled) return;
		if (camcontrol != null && camcontrol.IsPanning()) return;
		
		// if one element is focused, no other element should be updated
		if (focused_element != null)
		{
			UpdateFocusedElement();
		}
		else
		{
			UpdateAllElements();
		}

		// mouse over (in the bounds of the editor)
		mouse_over = Helper.PointInRect(new PVector(0, 0), width, height, mcontrol.Mouse());
		
		
		UpdateGainLoseInteraction();
	}
	public void Draw()
	{	
		if (!editor_enabled) return;

		// background
		parent.fill(EditorStyle.BackgroundValue());
		parent.noStroke();
		parent.rect(0, 0, width, height);
		
		for (int i = 0; i < elements.size(); ++i)
		{	
			elements.get(i).Draw();
		}
	}

}

