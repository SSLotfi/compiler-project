package scanner;

import java.util.Scanner;
import java.io.*;
import java.util.*;

public class ReadFile {
	
	private Scanner x ;
    public ArrayList<String> content = new ArrayList<String>(10);

    public void openfile(String path){

        try{
            x = new Scanner(new File(path));
        }
        catch(Exception e){
            System.out.println("file not found!");
        }

    }

    public void readfile(){
        while(x.hasNext()){
            content.add(x.nextLine());
            //System.out.println(content);

        }
    }

    public void closefile(){
        x.close();
    }

}
