package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface JavaGrep {

	/**
	 * Top level search workflow
	 * @throws IOException
	 */
	void process() throws IOException;

	/**
	 *
	 * Traverse a given directory and return all files
	 *
	 * @param rootDir input directory
	 * @param files empty list of files
	 * @return files under the rootDir
	 * @throws IOException if a given files list is not a list of files
	 */
	Stream<File> listFiles(String rootDir) throws IOException;

	/**
	 *
	 * Read a file and return all the lines
	 *
	 * @param inputFile file to be read
	 * @return lines
	 * @throws IOException if a given inputFile is not a file
	 */
	Stream<String> readLines(File inputFile) throws IOException;

	/**
	 *
	 * check if a line contains the regex pattern (passed by user)
	 *
	 * @param line input string
	 * @return true if there is a match
	 */
	boolean containsPattern(String line);

	/**
	 *
	 * Write lines to a file
	 *
	 * @param lines matched line
	 * @throws IOException if write failed
	 */
	void writeToFile(List<String> lines) throws IOException;

	String getRootPath();

	void setRootPath(String rootPath);

	String getRegex();

	void setRegex(String regex);

	String getOutFile();

	void setOutFile(String outFile);
}
