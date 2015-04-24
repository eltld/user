package net.ememed.user2.util;


public class CharUtil {
	public static boolean isChar(String content){
		char c = content.charAt(0);
		
		return Character.isLetter(c);
		
	}
	public static boolean isNum(String content){
		if(content.length()!=6){
			return false;
		}
		for(int i=0;i<content.length();i++){
			char c = content.charAt(i);
			if(!Character.isDigit(c)){
				return false;
			}
		}
		return true;
	}
	
}
