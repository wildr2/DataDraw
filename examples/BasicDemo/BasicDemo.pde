
import java.util.ArrayList;

import processing.core.*;
import datadraw.*;
import datadraw.editor.*;
import datadraw.parsing.*;


// Library class instances
private MouseControl mcontrol;  
private CameraControl camcontrol;
private Editor editor;

// Data
private ArrayList<String[]> crime_data_csv;
// how many crimes were reported during different shifts
private int count_shift_day, count_shift_evening, count_shift_midnight; 

// Visual
// colors for representing the day, evening, and midnight shifts
private int[] color_day, color_evening, color_midnight;
// the height of the graph bars
private int[] bar_height;
  
  
  
public void setup()
{
  size(1600, 900);
  
  // Initialize library class instances.
  mcontrol = new MouseControl(this);
  camcontrol = new CameraControl(this, mcontrol);
  editor = new Editor(this, mcontrol, camcontrol);
  
  // Adjust some other options.
  camcontrol.SetZoomPower(0.1f);
  editor.SetColorScheme(EditorColorScheme.DARK);
  editor.SetTextSize(12);
  
  // Make some editor elements. These can be made anywhere at anytime, not simply in setup().
  bar_height = editor.MakeSliderInt("Bar Height", 10, 1000);
  color_day = editor.MakeColor("Color Day Shift");
  color_evening = editor.MakeColor("Color Evening Shift");
  color_midnight = editor.MakeColor("Color Midnight Shift");
  
  // Read some data.
  crime_data_csv = ParsingHelper.ReadCSVData(sketchPath + "/crime_incidents_2013_CSV.txt");
  ProcessData();
}

public void ProcessData()
{
  for (int i = 0; i < crime_data_csv.size(); ++i)
  {
    String[] line = crime_data_csv.get(i);
    
    // record the numbers of crimes reported during day evening and midnight shifts
    if (line[2].equals("DAY")) ++count_shift_day;
    else if (line[2].equals("EVENING")) ++count_shift_evening;
    else if (line[2].equals("MIDNIGHT")) ++count_shift_midnight;
  }
}

public void draw()
{    
  background(50);

  pushMatrix();
  camcontrol.ApplyPanTranslation();
  camcontrol.ApplyZoomScale();
  
  // Draw labels
  
  // our label text should not be affected by zoom
  float text_size = 14 / camcontrol.GetZoom();
  textSize(text_size);
  fill(255);
  
  String txt_day = "Day Shift Crimes: " + count_shift_day;
  String txt_evening = "Evening Shift Crimes: " + count_shift_evening;
  String txt_midnight = "Midnight Shift Crimes: " + count_shift_midnight;
  float max_text_width = max(textWidth(txt_day), textWidth(txt_evening), textWidth(txt_midnight));
  
  text(txt_day, 500, 300 + text_size);
  text(txt_evening, 500, 300 + text_size + bar_height[0]);
  text(txt_midnight, 500, 300 + text_size + bar_height[0]*2);
  
  
  // Draw Crime Data
  
  noStroke();
  
  // day shift
  fill(color_day[0], color_day[1], color_day[2], color_day[3]);
  rect(500 + max_text_width + 50, 300, count_shift_day, bar_height[0]);
  
  // evening shift
  fill(color_evening[0], color_evening[1], color_evening[2], color_evening[3]);
  rect(500 + max_text_width + 50, 300 + bar_height[0], count_shift_evening, bar_height[0]);
  
  // midnight shift
  fill(color_midnight[0], color_midnight[1], color_midnight[2], color_midnight[3]);
  rect(500 + max_text_width + 50, 300 + bar_height[0]*2, count_shift_midnight, bar_height[0]);
  
  
  
 
  
  
  popMatrix();
  
  editor.Draw();
  mcontrol.AfterDraw();
}
