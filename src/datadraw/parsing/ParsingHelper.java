package datadraw.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class ParsingHelper
{
	
	public static ArrayList<String[]> ReadCSVData(String filepath)
	{
		BufferedReader br;
		ArrayList<String[]> data = new ArrayList<String[]>();
		
		// create a reader for the file
		try
		{
			br = new BufferedReader(new FileReader(filepath));
		}
		catch (FileNotFoundException e)
		{
			System.out.println("The file /'" + filepath + "/' could not be found.");
			e.printStackTrace();
			return null;
		}
		
		
		// read
		String line;

		try
		{
			while ((line = br.readLine())!=null)
			{
				data.add(line.split(","));
			}
			br.close();
			return data;
		} 
		catch (IOException e)
		{
			System.out.println("Error reading line in file /'" + filepath + "/'.");
			e.printStackTrace();
			return null;
		}
	}
	
}
