package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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

		BasicConfigurator.configure();

		JavaGrepImp javaGrepImp = new JavaGrepImp();
		javaGrepImp.setRegex(args[0]);
		javaGrepImp.setRootPath(args[1]);
		javaGrepImp.setOutFile(args[2]);

		try{
			// run the program
			javaGrepImp.process();
		} catch (Exception ex){
			// logger test
			javaGrepImp.logger.error("Error: Unable to process", ex);
		}
	}

	@Override
	public void process() throws IOException {

		List<File> emptyList = new ArrayList<File>();
		List<File> listFilesRecursively = listFiles(rootPath, emptyList);
		List<String> matchedLines = new ArrayList<String>();

		// iterate through all files in root dir and its subdirectories
		for (File file : listFilesRecursively) {
			// read all lines in that file
			for (String line : readLines(file)) {
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
		for(int i = 0; i < listFilesRecursively.size(); i++){
			System.out.println("file path: " + listFilesRecursively.get(i));
		}

		// prints out all lines in the files from recursive decent that match with the regex
		for(int i = 0; i < matchedLines.size(); i++){
			System.out.println("matched line: " + matchedLines.get(i));
		}

		// logger test
		logger.debug("Sample debug message");

	}

	// adds all files in a directory (recursive decent) to a list
	// NOTE: function signature changed from template to properly do recursion
	@Override
	public List<File> listFiles(String rootDir, List<File> listOfFiles) throws IOException {

		// store files in root dir in an array
		File root = new File(rootDir);
		File[] list = root.listFiles();

		// if not a directory, add it to list
		if(root.isDirectory() == false){
			listOfFiles.add(root);
			return listOfFiles;
		}

		// recursion on the list
		for(File file : list){
			listFiles(file.getPath(), listOfFiles);
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

		// line matches with regex
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
			for(int i = 0; i < lines.size(); i++){
				myWriter.write(lines.get(i) + "\n");
			}

			myWriter.close();
			System.out.println("Successfully wrote to the file!");
		} catch (IOException ex){
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
