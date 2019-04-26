package scanner;

import java.util.*;
import java.io.*;
import java.lang.*;

public class writefile {
	
	private Formatter y;
	
	public void openfile(String output_file_path) {
		try {
			y = new Formatter(output_file_path);
			
		}
		catch(Exception e) {
			System.out.println("couldn't write in file!");
		}
	}
	
	public void writeinfile(String token , String token_type) {
		y.format("%s , %s \r\n", token , token_type);
		
	}
	
	public void closefile() {
		y.close();
	}

}
