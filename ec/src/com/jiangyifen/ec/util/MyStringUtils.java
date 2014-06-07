package com.jiangyifen.ec.util;

import java.util.regex.Pattern;

public class MyStringUtils {
	
	public static String getSipNameFromChannel(String channel) {
		String exten = channel.substring(channel.indexOf('/') + 1,
				channel.indexOf('-'));
		return exten;
	}
	
	
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*(\\.?)[0-9]*"); 
	    return (pattern.matcher(str).matches() && !str.equals(".")&&!str.equals(""));    
	 }

	public static String formatIntToHHmmss(int i){
		int h=0,m=0,s = 0;
		
		if(i>=3600){
			h=i/3600;
			i = i%3600;
		}
		
		if(i>=60){
			m=i/60;
			i=i%60;
		}
		
		if(i>=0){
			s=i;
		}
		String hour,minute,second;
		if(h<10){
			hour = "0"+h;
		}else{
			hour = ""+h;
		}
		if(m<10){
			minute = "0"+m;
		}else{
			minute = ""+m;
		}
		if(s<10){
			second = "0"+s;
		}else{
			second = ""+s;
		}
		return hour+":"+minute+":"+second;
	}
	
	
	public static void testIsNumeric(){
		System.out.println(isNumeric("") + "=false");
		System.out.println(isNumeric(".") + "=false");
		System.out.println(isNumeric("1324-43-23")  + "=false");
		System.out.println(isNumeric("12.1") + "=true");
		System.out.println(isNumeric("1.") + "=true");
		System.out.println(isNumeric(".12") + "=true");
	}
	
	public static void testFormatIntToHHmmss(){
		System.out.println(formatIntToHHmmss(3599));
		System.out.println(formatIntToHHmmss(3601));
		System.out.println(formatIntToHHmmss(7199));
		System.out.println(formatIntToHHmmss(7201));
		System.out.println(formatIntToHHmmss(5467-3600));
	}
	
	public static void main(String[] args){
		
		MyStringUtils.testFormatIntToHHmmss();
	}
}
