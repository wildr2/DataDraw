package datadraw.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ParsingHelper
{
	private BufferedReader br;
	private String last_file = "";
	
	public ParsingHelper()
	{
		
	}
	
	public void ProcessCSVDataFile(String filepath, ProcessLineListener listener)
	{
		// create a new reader for a new file
		if (last_file != filepath)
		{
			try
			{
				br = new BufferedReader(new FileReader(filepath));
				last_file = filepath;
			}
			catch (FileNotFoundException e)
			{
				System.out.println("The file /'" + filepath + "/' could not be found.");
				e.printStackTrace();
				return;
			}
		}
		
		String line;

		try 
		{
			while ((line = br.readLine())!=null)
			{
				String[] line_data = line.split(",");
				if (!listener.ProcessLine(line_data)) return; 
			}
			
			// end of file
			System.out.println("Finished reading file.");
		} 
		catch (IOException e)
		{
			System.out.println("Error reading line in file /'" + filepath + "/'.");
			e.printStackTrace();
		}
	}
	
}
