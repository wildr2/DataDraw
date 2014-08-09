
import java.util.ArrayList;

import processing.core.*;
import datadraw.*;
import datadraw.editor.*;
import datadraw.parsing.*;



private MouseControl mcontrol;  
private CameraControl camcontrol;
private Editor editor;

// Data
private ArrayList<String[]> csv_data;
private int count_shift_day, count_shift_evening, count_shift_midnight;

// Visual
private int[] color_day, color_evening, color_midnight;
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
  csv_data = ParsingHelper.ReadCSVData(sketchPath + "/crime_incidents_2013_CSV.txt");
  ProcessData();
}

public void ProcessData()
{
  for (int i = 0; i < csv_data.size(); ++i)
  {
    String[] line = csv_data.get(i);
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
  
  
  // Draw Crime Data
  
  noStroke();
  
  // day shift
  fill(color_day[0], color_day[1], color_day[2], color_day[3]);
  rect(500, 300, count_shift_day, bar_height[0]);
  
  // evening shift
  fill(color_evening[0], color_evening[1], color_evening[2], color_evening[3]);
  rect(500, 300 + bar_height[0], count_shift_evening, bar_height[0]);
  
  // midnight shift
  fill(color_midnight[0], color_midnight[1], color_midnight[2], color_midnight[3]);
  rect(500, 300 + bar_height[0] * 2, count_shift_midnight, bar_height[0]);
  
  
  
  popMatrix();
  
  editor.Draw();
  mcontrol.AfterDraw();
}
