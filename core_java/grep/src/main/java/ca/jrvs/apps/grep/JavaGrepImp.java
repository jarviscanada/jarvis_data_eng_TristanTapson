package ca.jrvs.apps.grep;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class JavaGrepImp implements JavaGrep {

	// TODO logger setup/test
	//final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

	private String regex;
	private String rootPath;
	private String outFile;

	public static void main(String[] args){

		if(args.length != 3){
			throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
		}

		else{
			System.out.println("JavaGrepImp class!!");

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
			//javaGrepImp.logger.error("Error: Unable to process", ex);
		}
	}

	// TODO make the process recursive, and get rid of hard code tests later
	@Override
	public void process() throws IOException {

		// file listing hardcode test
		List<File> myFileListHC = listFiles(rootPath);
		System.out.println("num files in dir: " + myFileListHC.size());
		for(int i = 0; i < myFileListHC.size(); i++) {
			System.out.println(myFileListHC.get(i));
		}

		// read lines hardcode test
		File testFile1 = myFileListHC.get(3);
		List<String> myLinesListHC = readLines(testFile1);
		for(int i = 0; i < myLinesListHC.size(); i++) {
			System.out.println(myLinesListHC.get(i));
		}

		// regex pattern hard code test
		System.out.println("regex: " + regex);
		for(int i = 0; i < myLinesListHC.size(); i++) {
			System.out.println(myLinesListHC.get(i));
			System.out.println(containsPattern(myLinesListHC.get(i)));
		}

		// write to file hard code test - check success with cat in terminal
		writeToFile(myLinesListHC);



		// test for file write, follow the pseudocode in the ticket here...
		//List<String> myList = new ArrayList<String>();
		//writeToFile(myList);
	}

	// adds all files in a directory to a list
	@Override
	public List<File> listFiles(String rootDir) {

		System.out.println("root dir: " + rootDir);

		// storing file names in a list
		List<File> myFiles = new ArrayList<File>();

		// rootDir from string, as a file
		File root = new File(rootDir);
		File[] list = root.listFiles();

		for(int i = 0; i < list.length; i++){
			if (list[i].isDirectory()) {
				//System.out.println("Directory: " + list[i]);
			} else {
				//System.out.println("File: " + list[i]);
				myFiles.add(list[i]);
			}
		}

		return myFiles;
	}

	// adds all lines in a given file to a list
	@Override
	public List<String> readLines(File inputFile) {

		List<String> myLines = new ArrayList<String>();

		// standard buffered reader to read all lines in a file
		BufferedReader reader;
		try{
			reader = new BufferedReader(new FileReader(inputFile));
			String line = reader.readLine();
			while (line != null){
				//System.out.println(line);
				myLines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException ex){
			ex.printStackTrace();
		}

		return myLines;
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
			// TODO may have to put this in the proccess function depending on implementation
			for(int i = 0; i < lines.size(); i++){
				if(containsPattern(lines.get(i)) == true){
					myWriter.write(lines.get(i) + "\n");
				}
			}

			//myWriter.write("test write\n");
			myWriter.close();
			System.out.println("Successfully wrote to the file.");
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
