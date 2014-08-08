package datadraw.editor;

import java.io.PrintWriter;

import processing.core.PApplet;
import datadraw.MouseControl;



class EditorSliderFloat extends EditorElement
{
	private GUISliderFloat slider;
	
	
	// CONSTRUCTOR
	
	public EditorSliderFloat(String name, Editor parent, MouseControl mcontrol, PApplet p_app, 
			float min, float max) 
	{
		super(name, parent, mcontrol, p_app);
		
		slider = new GUISliderFloat(this, p_app, mcontrol, (max-min)/2f, min, max);
	}
	
	
	// SAVING AND LOADING
	
	public void Save(PrintWriter pw) 
	{
		pw.println(GetName() + ", " + "SliderFloat" + ", " + Get()[0] + ", " + slider.GetMin() + ", " + slider.GetMax());
	}
	public void Load(String[] data)
	{
		if (data[1].equals(("SliderFloat"))) return;
		
		float value = Float.parseFloat(data[2].trim());
		
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
	
	public float[] Get()
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
