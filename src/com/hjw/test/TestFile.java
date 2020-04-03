package com.hjw.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

public class TestFile {
	 
	    public static void main(String args[]) {
	        String srcFile = "D:/9.txt";
	        String toFile = "D:/b.txt";
	        try {
	            String result = read(srcFile);
	            write(result, toFile);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	 
	    private static String read(String srcFile) throws FileNotFoundException {
	 
	        Scanner in = new Scanner(new File(srcFile));
	        String result = "";
	        while (in.hasNextLine()) {
	        	String s =in.nextLine();
	        	//s.replace("\"", "\\\"");
	            result += "sb.append(\""+s+"\");" + "\r\n";
	        }
	 
	        in.close();
	 
	        return result;
	    }
	 
	    private static void write(String result, String toFile) throws Exception {
	 
	        Writer w = new FileWriter(new File(toFile));
	 
	        w.write(result);
	        w.flush();
	        w.close();
	    }
	}
