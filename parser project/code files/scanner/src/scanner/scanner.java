package scanner;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class scanner {
	
	public void scan (String args,String args2){
		//array lists for saving word types to release at the end
        ArrayList<String> Identifier = new ArrayList<String>(10);
        ArrayList<String> Special_Symbols = new ArrayList<String>(10);
        ArrayList<String> Reserved_Word = new ArrayList<String>(10);
        ArrayList<String> Numbers = new ArrayList<String>(10);
        ArrayList<String> Comments = new ArrayList<String>(10);
        String input_file_path = args;
        String output_file_path = args2;
        
        //list of identifiers in tiny language to compare to
        List<String> Reserved_Word_list = Arrays.asList("if" , "then" , "else" , "end" , "repeat" , "until" ,
                "read" , "write");
        List<Character> Special_Symbols_List = Arrays.asList('*' , '<' , '>' , '+' , '-' , '/' , '=' , '(' , ')' , ';');
        
        //enter file path to pass it to the read file method
        //F:\projects\eclipse\scanner\src\scanner\inputfile.txt
        ////BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        ////System.out.print("Please enter the file path: ");
        /*try {
			input_file_path = stdin.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        //read input file
        ReadFile r = new ReadFile();
        r.openfile(input_file_path);
        r.readfile();
        r.closefile();
        
        //open file to write in
        writefile output = new writefile();
        output.openfile(output_file_path);
        
        int current_Line_index = 0;
        int current_Letter_index = 0;
        int flag_done = 0;    //flag to know when the scanning is done
        
        String current_State = "Start";
        
        String current_Line = r.content.get(current_Line_index);
        
        while(flag_done == 0) {
        	
        switch(current_State) {
        	
        //if the first char is letter go to INID state
        case "Start" :
        {
        	//System.out.println(current_Line.charAt(current_Letter_index));
        	//if the first char is letter go to INID state
        	if(isLetter(current_Line.charAt(current_Letter_index))) {
        		current_State = "INID";
        		break;
        	}
        	
        	//if the first char is a digit got to IINUM state
        	else if(isDigit(current_Line.charAt(current_Letter_index))) {
        		current_State = "INNUM";
        		break;
        	}
        	
        	//if the first char is { go to INCOMMENT state
        	else if(current_Line.charAt(current_Letter_index) == '{' ) {
        		current_State = "INCOMMENT";
        		break;
        	}
        	
        	//if the first char is : go to INASSIGN state
        	else if(current_Line.charAt(current_Letter_index) == ':' ){
        		current_State = "INASSIGN";
        		//System.out.println(current_Letter_index);
        		break;
        	}
        	
        	//if its a white space stay in start state
        	else if(current_Line.charAt(current_Letter_index) == ' ') {
        		current_State = "Start";
        		current_Letter_index++;
        		break;
        	}
        	
        	//if the first char is a special symbol save it in special symbols array and go to done
        	else if(Special_Symbols_List.contains(current_Line.charAt(current_Letter_index))) {
        		StringBuffer strbuffer = new StringBuffer();
        		strbuffer.append(current_Line.charAt(current_Letter_index));
        		Special_Symbols.add(strbuffer.toString());
        		output.writeinfile(strbuffer.toString(), strbuffer.toString());
        		current_Letter_index++;
        		current_State = "Done";
        		break;
        	}
        	
        	//other go to Done state
        	else {
        		current_Letter_index++;
        		current_State = "Done";
        		break;
        	}
        	
        }
        case "INID":
        {
        	StringBuffer strbuffer = new StringBuffer();
        	
        	while(isLetter(current_Line.charAt(current_Letter_index))) {
        		
        		strbuffer.append(current_Line.charAt(current_Letter_index));
        		
        		if(current_Letter_index + 1 < current_Line.length()) {
        			current_Letter_index++;
        		}
        		else {
        			current_Letter_index++;
        			break;
        		}
        	}
        	
        	//check if the word is reserved
        	if(Reserved_Word_list.contains(strbuffer.toString())) {
        		
        		Reserved_Word.add(strbuffer.toString());
        		output.writeinfile(strbuffer.toString(), strbuffer.toString());  //"Reserved word");
        		
        	}
        	//the word is identifier
        	else {
        		
        		Identifier.add(strbuffer.toString());
        		output.writeinfile(strbuffer.toString(), "Identifier");
        		
        	}
        	
        	current_State = "Done";
        	break;
        	
        }
        case "INNUM":
        {
        	StringBuffer strbuffer = new StringBuffer();
        	
        	while(isDigit(current_Line.charAt(current_Letter_index))) {
        		strbuffer.append(current_Line.charAt(current_Letter_index));
        		
        		if(current_Letter_index + 1 < current_Line.length()) {
        			current_Letter_index++;
        		}
        		else {
        			current_Letter_index++;
        			break;
        		}
        			
        	}
        	
        	Numbers.add(strbuffer.toString());
        	output.writeinfile(strbuffer.toString(), "Number");
        	
        	current_State = "Done";
        	break;
        }
        case "INASSIGN":
        {
        	if(current_Letter_index + 1 < current_Line.length()) {
    			current_Letter_index++;
    		
    			if(current_Line.charAt(current_Letter_index) == '=') {
    				Special_Symbols.add(":=");
    				output.writeinfile(":=", "Assign" );   //"Special symbol");
    				current_Letter_index++;
    			}
        	}
        	else
        		current_Letter_index++;
        	
        	current_State = "Done";
        	break;
        }
        case "INCOMMENT":
        {
        	StringBuffer strbuffer = new StringBuffer();
        	current_Letter_index++;
        	
        	while(current_Line.charAt(current_Letter_index) != '}') {
        		strbuffer.append(current_Line.charAt(current_Letter_index));
        		
        		if(current_Letter_index + 1 < current_Line.length()) {
        			current_Letter_index++;
        		}
        		else if(current_Letter_index + 1 >= current_Line.length()) {
                		if((current_Line_index + 1) < r.content.size()){
                			current_Line_index++;
                			current_Line = r.content.get(current_Line_index);
                			current_Letter_index = 0;
                		}
        			}
        		else {
        			current_Letter_index++;
        			break;
        		}
        	}
        	
        	Comments.add(strbuffer.toString());
        	//output.writeinfile(strbuffer.toString(), "Comment");
        	
        	current_State = "Done";
        	break;
        	
        }
        case "Done":
        {
        	if(current_Letter_index >= current_Line.length()) {
        		if((current_Line_index + 1) < r.content.size()){
        			current_Line_index++;
        			current_Line = r.content.get(current_Line_index);
        			current_Letter_index = 0;
        		}
        		else {
        			flag_done = 1;
        			//System.out.println("scanning done!");
        			break;	
        		}
        	}
        	else {
        		current_State = "Start";
        	}
        	
        	break;
        }
        
        }
	}
        ///System.out.println("identifiers" + Identifier);
    	///System.out.println("reserved" + Reserved_Word);
    	///System.out.println("numbers" + Numbers);
    	///System.out.println("special_symbols" + Special_Symbols);
    	///System.out.println("comments" + Comments);
    	
    	output.closefile();   //close output file
    	
	}
	
	private static boolean isLetter(char c) {
	    return (c >= 'a' && c <= 'z') ||
	           (c >= 'A' && c <= 'Z') ;
	}
	
	private static boolean isDigit(char c) {
	    return (c >= '0' && c <= '9');
	           
	}
}
