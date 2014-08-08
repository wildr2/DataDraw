package datadraw.editor;

import java.io.PrintWriter;

import processing.core.PApplet;
import datadraw.MouseControl;



class EditorSliderInt extends EditorElement
{
	private GUISliderInt slider;
	
	
	// CONSTRUCTOR
	
	public EditorSliderInt(String name, Editor parent, MouseControl mcontrol, PApplet p_app, 
			int min, int max) 
	{
		super(name, parent, mcontrol, p_app);
		
		slider = new GUISliderInt(this, p_app, mcontrol, (max-min)/2, min, max);
	}
	
	
	// SAVING AND LOADING
	
	public void Save(PrintWriter pw) 
	{
		pw.println(GetName() + ", " + "SliderInt" + ", " + Get()[0] + ", " + slider.GetMin() + ", " + slider.GetMax());
	}
	public void Load(String[] data)
	{
		if (data[1].equals(("SliderInt"))) return;
		
		int value = Integer.parseInt(data[2].trim());
		
		slider.Set(value);
	}
	
	
	// PUBLIC ACCESSORS
	
	public boolean HasFocus()
	{ 
		return super.HasFocus() || slider.HasFocus();
	}
	public boolean MouseOver()
	{
		return super.MouseOver() || slider.MouseOver();
	}
	
	public int[] Get()
	{
		return slider.Get();
	}
	public float GetHeight()
	{
		return super.GetHeight() + 
			(collapsed ? 0 : slider.GetHeight());
	}
	public float GetWidth()
	{
		return collapsed ? super.GetWidth() : Math.max(super.GetWidth(), slider.GetWidth());
	}
	
	
	// OTHER MODIFIERS
	
	public void OnTextSizeChange()
	{
		super.OnTextSizeChange();
		slider.OnTextSizeChange();
	}
	
	
	// UPDATE AND DRAWING
	
	protected void UpdateContent()
	{
		slider.Update();
	}
	
	protected void DrawContent()
	{	
		slider.Draw();
	}
}
