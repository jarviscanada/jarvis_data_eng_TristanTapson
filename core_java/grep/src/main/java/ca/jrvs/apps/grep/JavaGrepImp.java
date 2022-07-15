package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.log4j.BasicConfigurator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.stream.Stream;

public class JavaGrepImp implements JavaGrep {

	final Logger logger = LoggerFactory.getLogger(JavaGrep.class);

	private String regex;
	public String rootPath;
	private String outFile;

	public static void main(String[] args){

		if(args.length != 3){
			throw new IllegalArgumentException("USAGE: JavaGrepImp regex rootPath outFile");
		}

		else{
			System.out.println("--- JavaGrepImp Class ---");
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

		System.out.println("root dir: " + rootPath);
		System.out.println("regex: " + regex);
		System.out.println("outfile: " + outFile);

		Stream<File> listFilesRecursively = listFiles(rootPath);
		List<String> matchedLinesList = new ArrayList<String>();

		// ticket pseudocode implemented using lambda/streams
		listFilesRecursively.forEach(file -> {
			try {
				// read all lines in a file and filter by regex pattern
				Stream<String> matchedLinesStream = readLines(file).filter(line -> containsPattern(line));
				matchedLinesStream.forEach(line -> matchedLinesList.add(line));
			} catch (IOException ex) {
				throw new RuntimeException(ex);
			}
		});

		// write matched lines to outFile
		writeToFile(matchedLinesList);

		// logger test
		logger.debug("Sample debug message");

	}

	// adds all files in a directory (stream walking) to a stream of files
	// NOTE: function signature was initially changed from template to properly do recursion
	@Override
	public Stream<File> listFiles(String rootDir) throws IOException {

		String directory = rootDir;
		List<File> validFiles = new ArrayList<File>();

		// stream walking through files in a directory
		// filter if valid file, and add the filepath to the list of valid files
		try (Stream<Path> tempStream = Files.walk(Paths.get(directory))) {
			tempStream.map(Path::normalize)
					.filter(path-> Files.isRegularFile(path))
					.forEach(path -> validFiles.add(path.toFile()));

		} catch (IOException ex){
			ex.printStackTrace();
		}

		// console output, for README...
		validFiles.forEach(file -> System.out.println("filepath: " + file));

		// stream walking through files in a directory
		// filter if valid file, and map path to file
		return Files.walk(Paths.get(directory))
				.filter(Files::isRegularFile)
				.map(Path::toFile);
	}

	// adds all lines in a given file to a stream of lines
	@Override
	public Stream<String> readLines(File inputFile) throws IOException {

		// file path
		String filepath = inputFile.getPath();

		// stream to add all lines in a file to a stream of lines
		return Files.lines(Paths.get(filepath));
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

	// write to outFile if line matched the regex
	@Override
	public void writeToFile(List<String> lines) throws IOException {

		// standard file writing
		try{
			FileWriter myWriter = new FileWriter(outFile);

			// write to file if line matches with regex
			for(int i = 0; i < lines.size(); i++){
				myWriter.write(lines.get(i) + "\n");
				System.out.println("matched line: " + lines.get(i));
			}

			myWriter.close();
			System.out.println("Successfully wrote to the file!");
		} catch (IOException ex){
			logger.error("Error: Unable to write to file", ex);
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
