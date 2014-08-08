package datadraw.editor;
import datadraw.Helper;
import datadraw.MouseControl;
import processing.core.PApplet;
import processing.core.PVector;


public class GUISliderInt
{
	// references
	private EditorElement parent;
	private PApplet p_app;
	private MouseControl mcontrol;
	
	// value and constraints
	private int[] value = {0};
	private int min, max;
	
	// interaction
	private boolean mouse_over = false;
	private boolean grabbed = false;
	
	// drawing
	private String name = ""; 
	private PVector local_pos = new PVector(); // position relative to the content start pos of the parent element
	private float max_width = 200;
	private float height = 50;
	
	
	
	// CONSTRUCTOR
	
	public GUISliderInt(EditorElement parent, PApplet p_app, MouseControl mcontrol, int initial, int min, int max)
	{
		this.parent = parent;
		this.p_app = p_app;
		this.mcontrol = mcontrol;
		
		this.min = min;
		this.max = max;

		Set(initial);
		SetHeight();
	}
	
	
	// PUBLIC MODIFIERS
	
	public void Set(int value)
	{
		this.value[0] = value;
		this.value[0] = Helper.Clamp(this.value[0], this.min, this.max);
	}
	public void SetName(String name)
	{
		this.name = name;
	}
	public void OnTextSizeChange()
	{
		SetHeight();
	}
	
	
	// PUBLIC ACCESSORS
	
	public boolean HasFocus()
	{
		return grabbed;
	}
	public boolean MouseOver()
	{
		return mouse_over;
	}
	
	public int[] Get() { return value; }
	public int GetMin() { return min; }
	public int GetMax() { return max; }
	
	public float Percentage()
	{
		return (float)(value[0] - min) / (max - min);
	}
	public float GetHeight()
	{
		return height;
	}
	public float GetWidth()
	{
		// includes the radius of the slider node
		return GetPos().x + max_width + 6;
	}
	public PVector GetPos()
	{ 
		return PVector.add(parent.GetContentStartPos(), local_pos);
	}
	
	
	// PRIVATE MODIFIERS
	
	private void SetHeight()
	{
		height = EditorStyle.GetTextLineHeight();
	}
	
	
	// UPDATE AND DRAW
	
	public void UpdatePosition(PVector pos) { local_pos = new PVector(pos.x, pos.y); }
	
	public void Update()
	{	
		PVector pos = GetPos();
		
		// focus
		mouse_over = Helper.PointInRect(pos, max_width, height, mcontrol.Mouse());
		
		// interaction
		if (!grabbed && mcontrol.MouseLeftDragged() && mouse_over)
		{
			grabbed = true;
		}
		
		if (grabbed)
		{
			if (!mcontrol.MouseLeftDragged())
			{
				grabbed = false;
			}
			else
			{
				// change value
				float w = mcontrol.Mouse().x - pos.x;
				value[0] = (int)((w / max_width) * (max - min) + min);
				value[0] = Helper.Clamp(value[0], min, max);
			}
		}
		
	}
	
	public void Draw()
	{			
		PVector pos = GetPos();
		
		// bar
		p_app.fill(EditorStyle.SliderValue());
		p_app.noStroke();
		p_app.rect(pos.x, pos.y + height / 2f, max_width, 2);
		
		// node
		if (mouse_over || grabbed) p_app.fill(EditorStyle.SliderValueSelected());
		p_app.ellipse(pos.x + Percentage() * max_width, pos.y + height / 2f, 12, 12);
		
		// name
		p_app.fill(mouse_over || grabbed ? EditorStyle.TextValueSelected() : EditorStyle.TextValue());
		p_app.textSize(EditorStyle.GetTextSize());
		p_app.text(name + " " + value[0], pos.x, pos.y + height);
	}
}
