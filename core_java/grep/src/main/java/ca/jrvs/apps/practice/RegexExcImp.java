package ca.jrvs.apps.practice;

public class RegexExcImp implements RegexExc {

	public boolean matchJpeg(String filename){

		return false;
	}

	public boolean matchIp(String ip){

		return false;
	}

	public boolean isEmptyLine(String line){

		return false;
	}

	public static void main(String args[]) {

		System.out.println("RegexExcImp Test");

		RegexExcImp test = new RegexExcImp();
		System.out.println(test.matchJpeg("filename"));
	}
}
