package ca.jrvs.apps.practice;
import java.util.regex.Pattern;

public class RegexExcImp implements RegexExc {

	// .jpeg | .jpg file
	public boolean matchJpeg(String filename){

		if(Pattern.matches("^.+(.jpg|.jpeg)$", filename.toLowerCase())){
			return true;
		}

		return false;
	}

	// IPv4 address
	public boolean matchIp(String ip){

		if(Pattern.matches("^([0-9]{1,3}).([0-9]{1,3}).([0-9]{1,3}).([0-9]{1,3})$", ip)){
			return true;
		}

		return false;
	}

	// empty line
	public boolean isEmptyLine(String line){

		if(Pattern.matches("^(\\s)*$", line)){
			return true;
		}

		return false;
	}

	// main
	public static void main(String args[]) {

		System.out.println("RegexExcImp class!");

		// instance of class created
		RegexExcImp testRE = new RegexExcImp();

		// testing
		/*
		System.out.println(testRE.matchJpeg("xyz.JpEg"));
		System.out.println(testRE.matchIp("222.34.3.009"));
		System.out.println(testRE.isEmptyLine("	"));
		*/
	}
}
