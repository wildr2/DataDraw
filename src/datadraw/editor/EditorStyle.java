package datadraw.editor;

import processing.core.PApplet;
import datadraw.Helper;


class EditorStyle
{	
	// text size
	public static final int text_size_default = 14;
	
	// color (light scheme)
	public static final float text_value_light = 50;
	public static final float text_value_selected_light = 0;
	public static final float slider_value_light = 180;
	public static final float slider_value_selected_light = 130;
	public static final float background_value_light = 220;
	
	// color (dark scheme)
	public static final float text_value_dark = 150;
	public static final float text_value_selected_dark = 200;
	public static final float slider_value_dark = 80;
	public static final float slider_value_selected_dark = 130;
	public static final float background_value_dark = 30;
	
	// spacing
	public static final float gap_elements = 5; // space between elements
	public static final float gap_heading_content = 10; // space between an element heading and its content
	public static final float gap_after_content = 15;
	public static final float indent_element_extra = 10; // indent of element (not including +/- icon)
	public static final float gap_color_sliders = 10;
	public static final int background_margin = 20;
	
	
	
	// non final variables
	private static EditorColorScheme color_scheme;
	private static int text_size;
	private static float text_line_height;
	private static float indent_element;
		
	
	// INITIALIZE
	
	public static void Initialize(PApplet p_app)
	{
		SetTextSize(p_app, text_size_default);
		color_scheme = EditorColorScheme.LIGHT;
	}
	
	
	
	// PUBLIC MODIFIERS
	
	public static void SetColorScheme(EditorColorScheme scheme)
	{
		color_scheme = scheme;
	}
	public static void SetTextSize(PApplet p_app, int size)
	{
		text_size = Helper.Clamp(size, 1, 100);
		p_app.textSize(text_size);
		text_line_height = p_app.textAscent();
		
		indent_element = indent_element_extra + p_app.textWidth("+");
	}
	
	
	// PUBLIC ACCESSORS
	
	public static int GetTextSize()
	{
		return text_size;
	}
	public static float GetTextLineHeight()
	{
		return text_line_height;
	}
	public static float IndentElement()
	{
		return indent_element;
	}
	public static float TextValueSelected()
	{
		switch (color_scheme)
		{
			case LIGHT:	return text_value_selected_light;
			case DARK:	return text_value_selected_dark;
			default: 	return text_value_selected_light;
		}
	}
	public static float TextValue()
	{
		switch (color_scheme)
		{
			case LIGHT:	return text_value_light;
			case DARK:	return text_value_dark;
			default: 	return text_value_light;
		}
	}
	public static float SliderValue()
	{
		switch (color_scheme)
		{
			case LIGHT:	return slider_value_light;
			case DARK:	return slider_value_dark;
			default: 	return slider_value_light;
		}
	}
	public static float SliderValueSelected()
	{
		switch (color_scheme)
		{
			case LIGHT: return slider_value_selected_light;
			case DARK:  return slider_value_selected_dark;
			default:  	return slider_value_selected_light;
		}
	}
	public static float BackgroundValue()
	{
		switch (color_scheme)
		{
			case LIGHT:	return background_value_light;
			case DARK:	return background_value_dark;
			default: 	return background_value_light;
		}
	}
}
