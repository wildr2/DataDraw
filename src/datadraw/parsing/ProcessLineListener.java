package datadraw.parsing;


public interface ProcessLineListener 
{ 
	/**
	 * Returns whether we should continue to process the next line of the file.
	 * Line contains all the comma seperated values in order from one line of the file.
	 */
    public boolean ProcessLine(String[] line);
}
