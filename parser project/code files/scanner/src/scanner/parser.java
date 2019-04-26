package scanner;

import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
///////////////////////////poooooooooooolp
public class parser {
	
	String[] token , Labels , token_value;// , initial_data;
	String[][] Shapes;   //[0] child name , [1] parent
	int children_counter = 0 , token_counter = 0 , Label_counter = 0 , Shape_counter = 0 , first_node_flag = 0 , flag_end = 0 , flag_until = 0;
	String[] parent_node;
	int parent_counter = 0 , if_counter = 0 , repeat_counter = 0 , parent_index_counter = 0 ;     //parent_counter is number of parents
	int[] parent_index = new int[100];
	ArrayList<Integer> repeat_depth = new ArrayList<>();
	ArrayList<Integer> if_depth = new ArrayList<>();
	
	public void parser(String output_file_path) {
		
		Arrays.fill(parent_index, 7777);
		
		ReadFile r = new ReadFile();
		r.openfile(output_file_path);
		r.readfile();
		r.closefile();
		
		int size = r.content.size();
		token = new String[size];
		token_value = new String[size];
		Labels  = new String[size];
		Shapes = new String[size][2];
		parent_node = new String[size];
		
		
		for(int current_line_index = 0 ; current_line_index < r.content.size() ; current_line_index++) {
		
			String temp = r.content.get(current_line_index);
			final String[] initial_data = temp.split(Pattern.quote(" , "));
			token_value[current_line_index] = initial_data[0];
			//token_value[current_line_index].replaceAll("\b\t\r","");
			token[current_line_index] = initial_data[1];
			token[current_line_index] = token[current_line_index].substring(0 , token[current_line_index].length() - 1);
			//System.out.println(token[current_line_index] + token_counter);
		}
		
		stmt_sequence();      //program ------> stmt_sequence
		
	}
	
	public void stmt_sequence() {
		if(token_counter < token.length) {
		statement();
		
		if(token_counter < token.length) {
			System.out.println("finished statement   :" + token[token_counter] + "1234");
		while(token[token_counter].equals(";")) {
			match(token[token_counter]);
			System.out.println("entered new statement");
			statement();
			if(token_counter > token.length - 1) {
				break;
			}
		}
		}
		}
		
	}
	
	public void statement() {
		if(parent_counter > 0 && token_counter < token.length)
			System.out.println(token[token_counter] + "  " + token_counter + "current_parent : " + token[Integer.valueOf(parent_node[parent_counter])]);

		switch(token[token_counter]) {
		
		case "write":{   //write
			System.out.println("entered write statement with token : " + token[token_counter]);
			if(first_node_flag == 0) {
				first_node_flag = FirstNode(token[token_counter] + ";parent"+ String.valueOf(token_counter));
				parent_node[parent_counter] = String.valueOf(token_counter);  //token[token_counter];
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else if((repeat_counter != flag_until && token[Integer.valueOf(parent_node[parent_counter - 1])].equals("repeat")) || (if_counter != flag_end && (token[Integer.valueOf(parent_node[parent_counter - 1])].equals("if") || token[Integer.valueOf(parent_node[parent_counter])].equals("if")))) {
				System.out.println("yaaaaaa raaaaaaab : repeatcoutner = " + repeat_counter + "  flag_until = " + flag_until + "    if_counter = " + if_counter + "  flag_end = " + flag_end);
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				if(token[Integer.valueOf(parent_node[parent_counter - 1])].equals("if")) {
					AddRightSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter) , parent_node[parent_counter - 1]);
					
				}
				else if(token[Integer.valueOf(parent_node[parent_counter])].equals("if")) {
					AddRightSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter) , parent_node[parent_counter]);
					
				}
				else if(token[Integer.valueOf(parent_node[parent_counter - 1])].equals("repeat"))
					AddLeftSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter) , parent_node[parent_counter - 1]);
				
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter);
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else {
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				AddSideSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter) , parent_node[parent_counter - 1]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter);
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			match("write");
			if(token_counter + 1 < token.length) {
				if(token[token_counter + 1].equals("+") || token[token_counter + 1].equals("-") || token[token_counter + 1].equals("*") || token[token_counter + 1].equals("\\") || token[token_counter + 1].equals("("))
					exp();
				else
					MakeOpNode(exp() + ";notparent" + "9820" , parent_node[parent_counter]);
			}
			else
				MakeOpNode(exp() + ";notparent" + "9820" , parent_node[parent_counter]);
			 
			break;
		}
		case "read":{
			//parent_node[parent_counter] = token[token_counter];
			if(first_node_flag == 0) {
				first_node_flag = FirstNode(token[token_counter] + " " + token_value[token_counter + 1] + ";parent" + token_counter);
				//parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else if(repeat_counter != flag_until || if_counter != flag_end){
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				if(token[Integer.valueOf(parent_node[parent_counter])].equals("if"))
				     AddRightSquareChild(token[token_counter] + " " + token_value[token_counter + 1] + ";parent" + String.valueOf(token_counter) , parent_node[parent_counter]);
				else 
				     AddLeftSquareChild(token[token_counter] + " " + token_value[token_counter + 1] + ";parent" + String.valueOf(token_counter) , parent_node[parent_counter]);

				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else {
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				AddSideSquareChild(token[token_counter] + " " + token_value[token_counter + 1] + ";parent" + String.valueOf(token_counter) , parent_node[parent_counter]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			match("read");
			match("Identifier");
			
			break;
		}
		case "Identifier":{
			System.out.println("entered identifier case" + "   parent_counter : " + parent_counter + " parent : " + parent_node[parent_counter]);
			
			if(first_node_flag == 0) {
				first_node_flag = FirstNode((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)));
				parent_node[parent_counter] = String.valueOf(token_counter + 1); 
				parent_index[parent_index_counter] = token_counter + 1;
				parent_index_counter++;
			}
			else if(if_counter != flag_end || repeat_counter != flag_until){
				if(token[Integer.valueOf(parent_node[parent_counter])].equals("if") )
					AddRightSquareChild((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)) , parent_node[parent_counter]);
				else if(token[Integer.valueOf(parent_node[parent_counter])].equals("repeat"))
					AddLeftSquareChild((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)) , parent_node[parent_counter]);
				else
					AddSideSquareChild((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)) , parent_node[parent_counter]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter + 1); 
				parent_index[parent_index_counter] = token_counter + 1;
				parent_index_counter++;
			}
			else {
				if(parent_counter > 0)
					AddSideSquareChild((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)) , parent_node[parent_counter]);
				else
					AddSideSquareChild((token[token_counter + 1] + " " + token_value[token_counter] + ";parent"+ String.valueOf(token_counter + 1)) , parent_node[parent_counter]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter + 1); 
				parent_index[parent_index_counter] = token_counter + 1;
				parent_index_counter++;

			}
			if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
			if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
			
			match("Identifier");
			match(":=");
			if(token[token_counter + 1].equals(";") || token[token_counter + 1].equals("then")) {
				MakeOpNode(exp() + ";notparent" + "9820", parent_node[parent_counter]);  //makes a node whose parent is only one id
			}
			else if (token[token_counter - 2].equals("if") || token[token_counter - 2].equals("until")) {
				MakeOpNode(exp() + "parent" + String.valueOf(token_counter), parent_node[parent_counter]);
			}
			else exp();   //makes a node whose parent is assig
			
			/*if(if_counter == flag_end && repeat_counter == flag_until ) {
				parent_counter--;
				System.out.println("yo yo yo yo yo yo warap homie");
			}*/
			break;
			}
		
		case "repeat":{
			System.out.println("entered repeat with token : " + token[token_counter]);
			repeat_counter++;
			repeat_depth.add(0);
			if(first_node_flag == 0) {
				first_node_flag = FirstNode(token[token_counter] + ";parent"+ String.valueOf(token_counter));
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else if(repeat_counter != flag_until || if_counter != flag_end){
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				if(token[Integer.valueOf(parent_node[parent_counter])].equals("repeat"))
					AddLeftSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);
				else if(token[Integer.valueOf(parent_node[parent_counter])].equals("if"))
					AddOneSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);
				else
					AddSideSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter);
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else {
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				AddSideSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter) , parent_node[parent_counter - 1]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			
			match("repeat");
			/*if(token[token_counter].equals("read")) {
				AddOpRightChild(token[token_counter] + ";notparent" + "9820" , parent_node[parent_counter - 1]);
			}
			else if(!token[token_counter].equals("Identifier")){
				AddOpRightChild(token[token_counter] + ";parent" + String.valueOf(token_counter - 2), parent_node[parent_counter - 1]);
			}*/
			stmt_sequence();
			match("until");
			flag_until = flag_until + 1;
			
			System.out.println("parent counter in repeat : " + parent_counter + "  reapeat_depth in repeat : " + repeat_depth);
			System.out.println("parent counter decreased by repeat depth");
			//parent_counter = parent_counter - repeat_depth;
			
			parent_counter -= repeat_depth.get(repeat_counter - 1) - 2;
			repeat_depth.remove(repeat_counter - 1);
			repeat_counter--;
			flag_until--;
			
			AddOpRightChild(exp() + ";parent" + String.valueOf(token_counter - 2) , parent_node[parent_counter - 1]);
			
			System.out.println("after finishing repeat : repeat_counter = " + repeat_counter + "  flag_until = " + flag_until);
			break;
		}
		case "if":{
			System.out.println("entered if case with token : " + token[token_counter]);
			
			if(first_node_flag == 0) {
				System.out.println("parent_counter at the begining of if condition : " + parent_counter);
				first_node_flag = FirstNode(token[token_counter] + ";parent"+ String.valueOf(token_counter));
				//parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else if((repeat_counter != flag_until && token[Integer.valueOf(parent_node[parent_counter - 1])].equals("repeat")) || (if_counter != flag_end && token[Integer.valueOf(parent_node[parent_counter - 1])].equals("if"))) {
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				AddSideSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			else {
				if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
				if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
				AddSideSquareChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter] );
				parent_counter++;
				parent_node[parent_counter] = String.valueOf(token_counter); 
				parent_index[parent_index_counter] = token_counter;
				parent_index_counter++;
			}
			if_counter++;
			if_depth.add(0);
			match("if");
			
			AddOpLeftChild(exp() + ";parent" + String.valueOf(token_counter - 2), parent_node[parent_counter]);   //removed parent-counter -1
			
			System.out.println("exp in if done");
			match("then");
			
			stmt_sequence();
			
			switch(token[token_counter]) {
			case "end":{
				match("end");
				flag_end++;
				parent_counter -= if_depth.get(if_counter - 1) - 2;
				if_depth.remove(if_counter - 1);
				if_counter--;
				flag_end--;
				System.out.println("reached end of if so if_counter = " + if_counter + " and flag_end = " + flag_end);
				break;
			}
			case "else":{
				match("else");
				
				parent_counter -= if_depth.get(if_counter - 1) - 1;
				if_depth.set(if_counter - 1, 1);
				stmt_sequence();
				match("end");
				flag_end++;
				parent_counter -= if_depth.get(if_counter - 1) - 1;
				if_depth.remove(if_counter - 1);
				if_counter--;
				flag_end--;
				break;
			}
			}
			
			//if(if_counter == flag_end) { parent_counter -= if_depth;}
			break;
		}
		}
	}
	
	public String exp() {
		System.out.println("entered exp with token : " + token[token_counter]);
		String Left = simple_exp();
		if(token_counter < token.length) {
		switch(token[token_counter]) {
		case "<":{
			//if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
			//if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
			parent_counter++;
			parent_node[parent_counter] = String.valueOf(token_counter); 
			parent_index[parent_index_counter] = token_counter;
			parent_index_counter++;
			match("<");
			AddOpLeftChild(token_value[token_counter - 2] + ";notparent"+ "9820", parent_node[parent_counter] );
			
			AddOpRightChild(simple_exp() + ";notparent" + "9820" , parent_node[parent_counter]);
			parent_counter--;    //remove if this things go wrong
			
			return token[Integer.valueOf(parent_node[parent_counter + 1])];      //parent_node[parent_counter];
		}
		case "=":{
			//if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
			//if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
			parent_counter++;
			parent_node[parent_counter] = String.valueOf(token_counter); 
			parent_index[parent_index_counter] = token_counter;
			parent_index_counter++;
			match("=");
			AddOpLeftChild(token_value[token_counter - 2] + ";notparent"+ "9820" , parent_node[parent_counter] );
	
			String Right = simple_exp();
			AddOpRightChild(Right + ";notparent" + "9820", parent_node[parent_counter]);
			parent_counter--;    //remove if this things go wrong
			return token[Integer.valueOf(parent_node[parent_counter + 1])]; 
		}
		}
		}
		System.out.println("reached end of exp returned :" + Left);
		return Left;
	}
	
	public String simple_exp() {
		System.out.println("entered simple exp with token : " + token[token_counter]);
		String Left = term();
		int while_counter = 0;
		if(token_counter < token.length) {
		while(token[token_counter].equals("+") || token[token_counter].equals("-")) {
			while_counter++;
			if(while_counter == 1) {
				AddOpLeftChild(token_value[token_counter - 1] + ";notparent" + "9820" , String.valueOf(token_counter));
				if(token[token_counter + 2].equals(";") || token[token_counter + 2].equals(")")){
					MakeOpNode(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);  //this is where + is saved
				}
				else if(!token[token_counter + 2].equals(")"))
				{
					AddOpLeftChild(token[token_counter] + ";parent" + String.valueOf(token_counter) , String.valueOf(token_counter + 2));
				}
				
			}
			else 
			{
				if(token[token_counter + 2].equals(";")){
					AddOpLeftChild(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter - 1]);  //this is where + is saved
				}
				else {
					AddOpLeftChild(token[token_counter] + ";parent" + String.valueOf(token_counter) , String.valueOf(token_counter + 2));
				}
				//AddOpLeftChild(token[token_counter] + ";parent" + String.valueOf(token_counter) , parent_node[parent_counter - 1]);
			}
			//if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
			//if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
			parent_counter++;
			parent_node[parent_counter] = String.valueOf(token_counter); 
			parent_index[parent_index_counter] = token_counter;
			parent_index_counter++;
			match(token[token_counter]);
			String Right = term();
			AddOpRightChild(Right + ";notparent" + "9820" , parent_node[parent_counter]);
			if(!token[token_counter].equals("+") && !token[token_counter].equals("-")) {
				parent_counter -= while_counter;    //remove if this things go wrong
				return token[Integer.valueOf(parent_node[parent_counter + 1])]; 
			}
			
		}
		}
		System.out.println("reached end of simple exp " + Left);
		return Left;
	}
	
	public String term() {
		//if(token_counter + 1 < token.length) {
		if(token_counter + 1 < token.length) {
			System.out.println("entered term with next token : " + token[token_counter + 1] + "1233");
			if(token[token_counter + 1].equals("=") || token[token_counter + 1].equals("Assign") || token[token_counter + 1].equals("<"))
			{	parent_node[parent_counter + 1] = String.valueOf(token_counter + 1);  
			parent_index[parent_index_counter] = token_counter + 1;
			parent_index_counter++;
				System.out.println("hello zeft world");
			}
		}
		
		String Left = factor();
		//AddOpLeftChild(Left , parent_node[parent_counter + 1]);
		int while_counter = 0;
		if(token_counter < token.length) {
		while(token[token_counter].equals("*") || token[token_counter].equals("\\")) {
			while_counter++;
			if(while_counter == 1) {
				AddOpLeftChild(token_value[token_counter - 1] + ";notparent" + "9820" , String.valueOf(token_counter));
				MakeOpNode(token[token_counter] + ";parent"+ String.valueOf(token_counter), parent_node[parent_counter]);
			}
			else {
				AddOpLeftChild(token[token_counter] + ";parent" + String.valueOf(token_counter) , parent_node[parent_counter - 1]);
			}
			//if(repeat_counter != flag_until) { repeat_depth.set(repeat_counter - 1,repeat_depth.get(repeat_counter - 1) + 1); }
			//if(if_counter != flag_end) { if_depth.set(if_counter - 1, if_depth.get(if_counter - 1) + 1); }
			parent_counter++;
			parent_node[parent_counter] = String.valueOf(token_counter); 
			parent_index[parent_index_counter] = token_counter;
			parent_index_counter++;
			match(token[token_counter]);
			if(token[token_counter].equals("("))
				factor();
			else {
				String Right = factor();
				AddOpRightChild(Right + ";notparent" + "9820", parent_node[parent_counter]);
			}
			if(!token[token_counter].equals("*") && !token[token_counter].equals("\\")) {
				parent_counter -= while_counter;
				return token[Integer.valueOf(parent_node[parent_counter + 1])]; 
			}
		}
		}
		System.out.println("reached end of term returned :" + Left);
		return Left;
		
	}
	
	public String factor() {
		System.out.println("entered factor with token : " + token[token_counter]);
		switch(token[token_counter]) {
		case "(":{
			match("(");
			System.out.println("current parent in factor after brackets : " + token[Integer.valueOf(parent_node[parent_counter])]);
			exp();
			match(")");
			return token[token_counter - 3];
		}
		case "Number":{
			match("Number");
			return token_value[token_counter - 1];
		}
		case "Identifier":{
			match("Identifier");
			return token_value[token_counter - 1];
		}
		}
		return "99999";
	
		
	}

	
	public void match(String expected_token) {
		token_counter++;
		if(expected_token == "end") {
			//flag_end = 1;
		}
		else if(expected_token == "until") {
			//flag_until = 1;
		}
	}
	
	public void MakeOpNode(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "OneCircleChild";     //add Circle for op to the shape list
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public void AddOpLeftChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "LeftCircleChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
		
	}
	
	public void AddOpRightChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "RightCircleChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public void AddSideSquareChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "SideSquareChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public void AddOneSquareChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "OneSquareChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public void AddLeftSquareChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "LeftSquareChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public void AddRightSquareChild(String node_token , String parent) {
		if(parent == null)
			parent = "999";
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "RightSquareChild";
		Shapes[Shape_counter][1] = parent;
		Shape_counter++;
	}
	
	public int FirstNode(String node_token) {
		Labels[Label_counter] = node_token;
		Label_counter++;
		Shapes[Shape_counter][0] = "FirstNode";
		Shapes[Shape_counter][1] = "-1";
		Shape_counter++;
		return 1;
	}
	
	public String[][] Get_Shapes() {
		for(int i = 0 ; i < Shape_counter ; i++) {
			Shapes[i][0] += ",";
			Shapes[i][0] += Labels[i];
		}
		return Shapes;
	}
	
	public String[] Get_Labels() {
		return Labels;
	}
	
	public int Get_Number_Nodes() {
		
		return Shape_counter;
	}
	
	public String[] Get_Tokens() {
		return token;
	}
	
	public int[] Get_Parents() {
		return parent_index;
	}

}
