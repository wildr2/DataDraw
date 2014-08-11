A collection of tools to help quickly create visualizations and adjust them on the fly.

- CameraControl -
  Simple 2D camera functionality for panning and zooming around a scene. Plays nice with datadraw.editor.Editor. Ideal for quickly      setting up a 2D visualization that you can 'explore'.

- Editor - 
  An interface that draws in the processing window and allows the adjustment of values
  while the program is running. You can use the 'MakeSlider' function, for instance, to
  let you change the value of a float in the processing window. Editor elements (such as the
  slider) are saved when the program is closed and loaded again the next time the program is run,
  providing you still call the 'Make...' function and give the element the same name. 

- ParsingHelper -
  Provides static methods for making data file parsing less of a chore. 
  Currently only provides functionality for parsing CSV files.

- MouseControl - 
  Provides high level functions for getting mouse input.
