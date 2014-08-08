package datadraw.editor;

import java.io.PrintWriter;

import processing.core.PApplet;
import processing.core.PVector;
import datadraw.MouseControl;



class EditorColor extends EditorElement
{
	private int[] rgba = new int[4];
	private GUISliderInt[] sliders_rgba = new GUISliderInt[4];
	
	// interaction
	private boolean has_focus = false;
	private boolean mouse_over = false;
	private Integer focused_index = null; // index of the focused slider
	
	
	// CONSTRUCTOR
	
	public EditorColor(String name, Editor parent, MouseControl mcontrol, PApplet p_app) 
	{
		super(name, parent, mcontrol, p_app);
		
		for (int i = 0; i < 4; ++i)
		{
			sliders_rgba[i] = new GUISliderInt(this, p_app, mcontrol, 255, 0, 255);
			UpdateSliderPosition(i);
		}
		
		// slider names
		sliders_rgba[0].SetName("r");
		sliders_rgba[1].SetName("g");
		sliders_rgba[2].SetName("b");
		sliders_rgba[3].SetName("a");
	}
	
	
	// SAVING AND LOADING
	
	public void Save(PrintWriter pw) 
	{
		pw.println(GetName() + ", " + "Color" + ", " + rgba[0] + ", " + rgba[1] + ", " + rgba[2] + ", " + rgba[3]);
	}
	public void Load(String[] data)
	{
		if (data[1].equals(("Color"))) return;
		
		for (int i = 0; i < 4; ++i)
		{
			int value = Integer.parseInt(data[2 + i].trim());
			sliders_rgba[i].Set(value);
			rgba[i] = sliders_rgba[i].Get()[0];
		}
	}
	
	
	// PUBLIC ACCESSORS
	
	public boolean HasFocus()
	{ 
		return has_focus || super.HasFocus(); 
	}
	public boolean MouseOver()
	{
		return mouse_over || super.MouseOver(); 
	}
	
	public int[] Get()
	{
		return rgba;
	}
	public float GetHeight()
	{
		return super.GetHeight() + 
			(collapsed ? 0 : 5 * sliders_rgba[0].GetHeight() + 4 * EditorStyle.gap_color_sliders);
	}
	public float GetWidth()
	{
		return collapsed ? super.GetWidth() : Math.max(super.GetWidth(), sliders_rgba[0].GetWidth());
	}
	
	
	// OTHER MODIFIERS
	
	public void OnTextSizeChange()
	{
		super.OnTextSizeChange();
		
		for (int i = 0; i < 4; ++i)
		{
			sliders_rgba[i].OnTextSizeChange();
			UpdateSliderPosition(i);
		}
	}
	
	
	// UPDATE AND DRAWING
	
	private void UpdateSliderPosition(int i)
	{
		sliders_rgba[i].UpdatePosition(new PVector(0, (i + 1) * (sliders_rgba[0].GetHeight() + EditorStyle.gap_color_sliders)));
	}
	
	private void UpdateFocusedSlider()
	{
		sliders_rgba[focused_index].Update();
		
		// value update
		rgba[focused_index] = sliders_rgba[focused_index].Get()[0];
		
		// focus and mouse over
		mouse_over = false;
		
		if (sliders_rgba[focused_index].MouseOver()) mouse_over = true;
		if (!sliders_rgba[focused_index].HasFocus())
		{
			has_focus = false;
			focused_index = null;
		}	
	}
	private void UpdateAllSliders()
	{
		has_focus = false;
		mouse_over = false;
		
		for (int i = 0; i < 4; ++i)
		{
			sliders_rgba[i].Update();
			
			// focus and mouse over
			if (sliders_rgba[i].HasFocus())
			{
				has_focus = true;
				focused_index = i;
			}
			if (sliders_rgba[i].MouseOver()) mouse_over = true;
			
			
			// value update
			rgba[i] = sliders_rgba[i].Get()[0];
		}
	}
	
	protected void UpdateContent()
	{
		// if one slider is focused, no other slider should be updated
		if (focused_index != null)
		{
			UpdateFocusedSlider();
		}
		else
		{
			UpdateAllSliders();
		}	
	}
	
	protected void DrawContent()
	{	
		// color preview
		PVector pos = GetContentStartPos();
		p_app.noStroke();
		p_app.fill(rgba[0], rgba[1], rgba[2], rgba[3]);
		p_app.rect(pos.x, pos.y, 200, EditorStyle.GetTextLineHeight());
		
		// sliders
		for (int i = 0; i < 4; ++i)
		{
			sliders_rgba[i].Draw();
		}
	}
}
