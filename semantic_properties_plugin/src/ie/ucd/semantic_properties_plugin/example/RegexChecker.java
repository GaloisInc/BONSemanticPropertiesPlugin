package ie.ucd.semantic_properties_plugin.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Pattern p=Pattern.compile("<\\w+%nat>");
		
		Matcher r= p.matcher("<example%nat>");
		if(r.matches())
			System.out.println("hell yeah");
		else
			System.out.println("uh oh");
		

	}

}