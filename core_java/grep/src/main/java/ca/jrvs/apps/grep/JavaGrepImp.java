package ca.jrvs.apps.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.IOException;




public class JavaGrepImp implements JavaGrep {

	// TODO logger setup/test
	final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

	private String regex;
	public String rootPath;
	private String outFile;

	public static void main(String[] args){

		if(args.length != 3){
			throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
		}

		else{
			System.out.println("-- JavaGrepImp Class --");
		}

		// TODO logger setup/test
		//BasicConfigurator.configure();

		JavaGrepImp javaGrepImp = new JavaGrepImp();
		javaGrepImp.setRegex(args[0]);
		javaGrepImp.setRootPath(args[1]);
		javaGrepImp.setOutFile(args[2]);

		try{
			// run the program
			javaGrepImp.process();
		} catch (Exception ex){
			// TODO logger setup/test
			javaGrepImp.logger.error("Error: Unable to process", ex);
		}
	}

	// TODO make the process recursive (ticket pseudocode), and get rid of hard code tests later
	@Override
	public void process() throws IOException {

		List<File> emptyListOfFiles = new ArrayList<File>();
		List<File> myFiles = listFiles(rootPath, emptyListOfFiles);
		List<String> matchedLines = new ArrayList<String>();

		File root = new File(rootPath);
		File [] list = root.listFiles();

		// iterate through all files in root dir and its subdirectories
		for (File f : myFiles) {
			// read all lines in that file
			for (String line : readLines(f)) {
				// add lines in that file that match with the regex to list, and write to outfile
				if (containsPattern(line) == true) {
					matchedLines.add(line);
				}
			}
		}

		writeToFile(matchedLines);

		System.out.println("root dir: " + rootPath);
		System.out.println("regex: " + regex);
		System.out.println("outfile: " + outFile);

		// TODO remove print statements
		// prints out file paths found from recursive decent
		for(int i = 0; i < myFiles.size(); i++){
			System.out.println("file path: " + myFiles.get(i));
		}

		// prints out all lines in the files from recursive decent that match with the regex
		for(int i = 0; i < matchedLines.size(); i++){
			System.out.println("matched line: " + matchedLines.get(i));
		}

		// logger test
		logger.debug("Sample debug message");

	}

	// adds all files in a directory (recursive decent) to a list
	// NOTE: function signature changed to properly do recursion
	@Override
	public List<File> listFiles(String rootDir, List<File> listOfFiles) throws IOException {

		// rootDir from string, as a file
		File root = new File(rootDir);
		File[] list = root.listFiles();

		// if not a directory, add it to list
		if(!root.isDirectory()){
			listOfFiles.add(root);
			return listOfFiles;
		}

		// recursion on the list
		for(File f : list){
			listFiles(f.getPath(), listOfFiles);
		}

		return listOfFiles;
	}

	// adds all lines in a given file to a list
	@Override
	public List<String> readLines(File inputFile) throws IOException {

		List<String> allLines = new ArrayList<String>();

		// standard buffered reader to read all lines in a file
		BufferedReader myReader;
		try{
			myReader = new BufferedReader(new FileReader(inputFile));
			String line = myReader.readLine();
			while (line != null){
				//System.out.println(line);
				allLines.add(line);
				line = myReader.readLine();
			}
			myReader.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}

		return allLines;
	}

	// regex line check
	@Override
	public boolean containsPattern(String line) {

		if(Pattern.matches(regex, line)){
			return true;
		}
		return false;
	}

	// write to out file if line matched the regex
	@Override
	public void writeToFile(List<String> lines) throws IOException {

		// standard file writing
		try{
			FileWriter myWriter = new FileWriter(outFile);

			// write to file if line matches with regex
			// TODO may have to put this in the process function depending on implementation
			for(int i = 0; i < lines.size(); i++){
				//if(containsPattern(lines.get(i)) == true){
					myWriter.write(lines.get(i) + "\n");
				//}
			}

			//myWriter.write("test write\n");
			myWriter.close();
			System.out.println("Successfully wrote to the file!");
		} catch (IOException ex){
			System.out.println("An error occurred.");
			ex.printStackTrace();
		}
	}

	@Override
	public String getRootPath() {
		return rootPath;
	}

	@Override
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	@Override
	public String getRegex() {
		return regex;
	}

	@Override
	public void setRegex(String regex) {
		this.regex = regex;
	}

	@Override
	public String getOutFile() {
		return outFile;
	}

	@Override
	public void setOutFile(String outFile) {
		this.outFile = outFile;
	}
}
