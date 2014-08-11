package datadraw.parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Provides static methods for making data file parsing less of a chore. 
 * Currently only provides functionality for parsing CSV files.
 * Does not need to be instanced.
 */
public class ParsingHelper
{
	/**
	 * Read a CSV text file, and return the data in a list structure.
	 * Each String[] contains the data for a single line. Any whitespace is trimmed
	 * from each value.
	 * 
	 * @parem filepath
	 * 			the complete filepath of the csv file (.txt)
	 * 
	 * @return ArrayList<String[]>
	 */
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
