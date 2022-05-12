package ca.jrvs.apps.grep;


import java.io.File;
import java.io.IOException;
import java.util.List;


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
			javaGrepImp.process();
		} catch (Exception ex){
			// TODO logger setup/test
			//javaGrepImp.logger.error("Error: Unable to process", ex);
		}
	}

	@Override
	public void process() throws IOException {

	}

	@Override
	public List<File> listFiles(String rootDir) {
		return null;
	}

	@Override
	public List<String> readLines(File inputFile) {
		return null;
	}

	@Override
	public boolean containsPattern(String line) {
		return false;
	}

	@Override
	public void writeToFile(List<String> lines) throws IOException {

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
