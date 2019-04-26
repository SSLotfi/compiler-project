package scanner;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DrawShapes extends JPanel {
	
	//Rectangle2D.Double main_node = new Rectangle2D.Double(200 , 10 , 80 , 25);
	//Ellipse2D.Double main_node = new Ellipse2D.Double(250.0 , 0.0 , 40.0 , 25.0);
	Line2D.Double main_line = new Line2D.Double(2 , 2 , 1 ,1);
	Shape main_node;
	int global_parent_flag = 1 , do_once = 1;
	int global_i = 0;
	int text_pos_counter = 0;
	private List<Object> shapes = new ArrayList<>();
	private List<int[]> text_positions = new ArrayList<>();
	ArrayList<Integer> waiting_list = new ArrayList<Integer>();
	int[] parent_child_num2;
	int start_point = 1 , flag_wierd_child = 0;
	
	public DrawShapes(int children_num , String[][] shape , String[] Labels , String[] token , int[] parent_index , int[] parent_child_num , int x_value2 , int y_value2) {  //parent index in token
		parent_child_num2 = parent_child_num;
		for(int i = 0 ; i < children_num ; i++) {
			String[] temp2 = Labels[i].split(Pattern.quote(";"));   //label;parent
			Labels[i] = temp2[0];
		}
		System.out.println("in drawshapes  " + children_num);
		main_node = null;
		
		////////////first node ///////////////////
		Rectangle2D.Double temp_sqr = new Rectangle2D.Double(80.0 , 20.0 , 80.0 , 25.0);
		main_node = (Rectangle2D.Double) temp_sqr;
		JLabel inside_msg = new JLabel("");
	 	int textposX , textposY;
	 	int str_length = Labels[0].length();
		 textposX = (int) ((int)80 + 40   - 2.5*str_length - 2);
		 textposY = (int)20 + 17;
		 text_positions.add(new int[] {textposX , textposY});
		 
		 inside_msg.setText(Labels[0]);
		 
		shapes.add(main_line);
		shapes.add(inside_msg);
		shapes.add(main_node);
		
		
		///////////////////////////////////////
		//setBackground(Color.CYAN);
		setPreferredSize(new Dimension(5000, 5000));
		
		
		for(global_i = 0 ; global_i < parent_child_num.length ; global_i++) {
			
			if(global_i > 0) {
			int temp_node = Get_main_node(shape , parent_index , global_i );
			if(temp_node == 888) {
				start_point += parent_child_num[global_i];
				continue;
			}
			System.out.println("current parent " + shape[(temp_node - 2) / 3][0]);
			if(shapes.get(temp_node) instanceof Ellipse2D.Double) {
				main_node = (Ellipse2D.Double) shapes.get(temp_node);
				System.out.println("                         turned to circle");
			}
			else if(shapes.get(temp_node) instanceof Rectangle2D.Double) {
				main_node = (Rectangle2D.Double) shapes.get(temp_node);
				System.out.println("                         turned to square");
			}
			}
			
			int x_dev = x_value2 , y_dev = y_value2;
			for(int j = start_point ; j < start_point + parent_child_num[global_i] ; j++) {
				System.out.println("string[i][0] 2222 " + shape[j][0]);
			String[] current_shape_array = shape[j][0].split(Pattern.quote(";"));
			x_dev += 85;
			y_dev += 35;
		switch (current_shape_array[0]) {
		case "LeftCircleChild" : {
			AddLeftChildCircle(main_node , 10 , 0 , Labels[j]);
			System.out.println("draw leftcirclechild \n");
			break;
		}
			
		case "RightCircleChild" : {
			AddRightChildCircle(main_node , 10 , 0, Labels[j]);
			System.out.println("draw rightcirclechild \n");
			break;
		}
		case "OneCircleChild" : {
			AddOneChildCircle(main_node , Labels[j]);
			System.out.println("draw onecirclechild \n");
			break;
		}
		case "SideSquareChild" : {
			AddOneChildSquare((Rectangle2D.Double)main_node , Labels[j] , 1 , (x_dev - 10) / 2  , y_dev);
			System.out.println("draw sidesquarechild \n");
			break;
		}
		case "LeftSquareChild" : {
			AddLeftChildSquare((Rectangle2D.Double)main_node , x_dev , y_dev , Labels[j]);
			System.out.println("draw leftsquarechild \n");
			break;
		}
		case "RightSquareChild" : {
			AddRightChildSquare((Rectangle2D.Double)main_node , x_dev , y_dev , Labels[j]);
			System.out.println("draw rightsquarechild \n");
			break;
		}
		case "OneSquareChild" : {
			AddOneChildSquare((Rectangle2D.Double)main_node , Labels[j] , 0 , x_dev , y_dev);
			System.out.println("draw onesquarechild \n");
			break;
		}
		case "FirstNode" : {
			/*main_node = new Rectangle2D.Double(100.0 , 20.0 , 80.0 , 25.0);
			JLabel inside_msg = new JLabel("");
		 	int textposX , textposY;
		 	int str_length = Labels[j].length();
			 textposX = (int) ((int)100 + 40   - 2.5*str_length - 2);
			 textposY = (int)20 + 17;
			 text_positions.add(new int[] {textposX , textposY});
			 
			 inside_msg.setText(Labels[j]);
			 shapes.add(inside_msg);
			shapes.add(main_node);*/
		}
		}
		}
			start_point += parent_child_num[global_i];
			if(flag_wierd_child == 1) {
				flag_wierd_child = 0;
				start_point += parent_child_num[global_i + 1];
				global_i++;
			}
		}
	}
	
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ((Graphics2D) g).draw(main_node);
        //((Graphics2D) g).draw(main_line);
        for (Object s : shapes) {
        	if (s instanceof Rectangle2D.Double)
            {
        		System.out.println("rectangle drawn");
                ((Graphics2D) g).draw((Shape) s);
            } 
            else if (s instanceof Line2D.Double) {
            	System.out.println("line drawn");
            	//JOptionPane.showMessageDialog(null, "line", "line", 1);
                ((Graphics2D) g).draw((Shape) s);
            }
            else if(s instanceof JLabel) {
            	System.out.println("label drawn");
            	int posX = text_positions.get(text_pos_counter)[0];
            	int posY = text_positions.get(text_pos_counter)[1];
            	((Graphics2D) g).drawString(((JLabel) s).getText() , posX , posY);
            	text_pos_counter++;
            }
            else if(s instanceof Ellipse2D.Double) {
            	System.out.println("circle drawn");
            	((Graphics2D) g).draw((Shape) s);
            }
        }
    }
	
	 public int Get_main_node(String[][] Shapes ,int[] parent_index , int wanted_index) {
		 
		 System.out.println("wanted_index  " + wanted_index + "  parent  " + parent_index[wanted_index]);
		 
		 int index_of_shape = 0;    //shapes contain type and parent
		 if(!waiting_list.isEmpty() && do_once == 1) {
			 wanted_index = waiting_list.get(0);
			 waiting_list.remove(0);
			 global_i -= 2;
			 start_point = start_point - parent_child_num2[global_i + 1] - parent_child_num2[global_i];
			 index_of_shape = -parent_child_num2[global_i];
			 flag_wierd_child = 1;
		 }
		 do_once = 1;
		 int value = parent_index[wanted_index]; //parent index in token array
		 for(int i = 0 ; i < Shapes.length ; i++) {
			 System.out.println("string[i][0] tttttt " + Shapes[i][0] + " index  " + Shapes[i][1] );
			 String[] current_shape_array = Shapes[i][0].split(Pattern.quote(";"));
			 String temp4 = current_shape_array[1];
			 temp4 = temp4.substring(0 , 6);
			 String temp5 = current_shape_array[1].replaceAll("[^0-9]","");
			 System.out.println("value of string : " + temp4 + " parent id : " + Integer.valueOf(temp5));
			 if(temp4.equals("parent") && Integer.valueOf(temp5) == value) {   //Integer.valueOf(Shapes[i][1]) == value && 
				 index_of_shape += i;
				 System.out.println("index of shape = " + index_of_shape);
				 System.out.println("got shape : " + Shapes[i][1] + "   " + index_of_shape);
				 break;
			 }
		 }
		 
		 if((index_of_shape * 3) + 2 > shapes.size()) {
			 waiting_list.add(wanted_index);
			 do_once = 0;
			 return 888;
		 }
		 return (index_of_shape * 3) + 2;
	 }
	 
	 
	 
	 public void AddChildren(int child_num , Rectangle2D.Double node) {
		 
		 Double x_coord = node.getX();
		 Double y_coord = node.getY();
		 
		 switch(child_num) {
		 case 3:
		{
			 Rectangle2D.Double temp_node = node;
			 AddLeftChildSquare(temp_node , 80 , 0 , "1111111");
			 AddRightChildSquare(temp_node , 80 , 0 , "2");
			 AddOneChildSquare(temp_node , "middle" , 1 , 3 , 4);
			 break;
		 }
		 case 4:
		 {
			 Rectangle2D.Double temp_node = node;
			 AddLeftChildSquare(temp_node , 110 , 0 ,"1");
			 AddLeftChildSquare(temp_node , 10 , 0 , "2");
			 AddRightChildSquare(temp_node , 110 , 0 , "3");
			 AddRightChildSquare(temp_node , 10 , 0 , "4");
			 break;
		 }
		 case 5:
		 {
			 Rectangle2D.Double temp_node = node;
			 AddLeftChildSquare(temp_node , 170 , 0 , "1");
			 AddLeftChildSquare(temp_node , 70 , 0 , "2");
			 AddOneChildSquare(temp_node , "middle" , 0 , 3 , 4);
			 AddRightChildSquare(temp_node , 170 , 0 , "3");
			 AddRightChildSquare(temp_node , 70 , 0 , "4");
			 break;
		 }
		 case 6:
		 {
			 Rectangle2D.Double temp_node = node;
			 AddLeftChildCircle(temp_node , 250 , 0 , "1");
			 AddLeftChildCircle(temp_node , 130 , 0 , "2");
			 AddLeftChildCircle(temp_node , 20 ,0 , "3");
			 AddRightChildCircle(temp_node , 250 ,0 , "4");
			 AddRightChildSquare(temp_node , 130 , 0 , "5");
			 AddRightChildCircle(temp_node , 20 , 0 , "6");
			 break;
		 }
		 case 7:
		 {
			 Rectangle2D.Double temp_node = node;
			 AddLeftChildSquare(temp_node , 300 , 0 , "1");
			 AddLeftChildSquare(temp_node , 200 , 0 , "2");
			 AddLeftChildSquare(temp_node , 100 ,0 , "3");
			 AddOneChildSquare(temp_node , "end" , 1 , 3 , 4);
			 AddRightChildSquare(temp_node , 300 ,0 , "4");
			 AddRightChildSquare(temp_node , 200 , 0 , "5");
			 AddRightChildSquare(temp_node , 100 , 0 , "6");
			 break;
		 }
		 }
	 }
	 
	 public void AddLeftChildSquare(Rectangle2D.Double node ,double x_deviation ,double y_deviation , String data) {
		 
		 	JLabel inside_msg = new JLabel("");
		 	int textposX , textposY;
		
		 	Double x_coord = node.getX();
			Double y_coord = node.getY();
			Double width = node.getWidth();
			Double height = node.getHeight();
			inside_msg.setText(data);
			x_coord = x_coord + (width / 2);
			y_coord = y_coord + height;
			Line2D.Double Line = new Line2D.Double(x_coord - (x_deviation / 10), y_coord, x_coord - (width / 2) - x_deviation, y_coord + 30 + y_deviation);
			Rectangle2D.Double rect = new Rectangle2D.Double(x_coord - (width) - x_deviation, y_coord + 30 + y_deviation , 80 ,25);
			int str_length = data.length();
			textposX = (int) ((int)rect.getX() + (width / 2) - 2.5*str_length - 2);
			textposY = (int)rect.getY() + 15;
			text_positions.add(new int[] {textposX , textposY});
			
	        
	        shapes.add(Line);
	        shapes.add(inside_msg);
	        shapes.add(rect);
	        //main_node = rect;
	        //repaint();
	    }
	 
	 public void AddRightChildSquare(Rectangle2D.Double node , double x_deviation , double y_deviation , String data) {
		 
		 	JLabel inside_msg = new JLabel("");
		 	int textposX , textposY;
		 	
			Double x_coord = node.getX();
			Double y_coord = node.getY();
			Double width = node.getWidth();
			Double height = node.getHeight();
			x_coord = x_coord + (width / 2);
			y_coord = y_coord + height;
			Line2D.Double Line = new Line2D.Double(x_coord + (x_deviation / 10), y_coord , x_coord + (width / 2) + x_deviation, y_coord + 30 + y_deviation);
			Rectangle2D.Double rect = new Rectangle2D.Double(x_coord + x_deviation, y_coord + 30 + y_deviation, 80 ,25);
			int str_length = data.length();
			textposX = (int) ((int)rect.getX() + (width / 2) - 2.5*str_length - 2);
			textposY = (int)rect.getY() + 15;
			text_positions.add(new int[] {textposX , textposY});
			
			inside_msg.setText(data);
			
	        
	        shapes.add(Line);
	        shapes.add(inside_msg);
	        shapes.add(rect);
	       // main_node = rect;
	        //repaint();
	    }
	 
	 public void AddOneChildSquare(Rectangle2D.Double node , String data , int type , int x_deviation , int y_deviation) {
		 
		 JLabel inside_msg = new JLabel("");
		 int textposX , textposY;
		 
		 Double x_coord = node.getX();
		 Double y_coord = node.getY();
		 Double x_coord2 = 0.0;
		 Double y_coord2 = 0.0;
		 Double width = node.getWidth();
		 Double height = node.getHeight();
		 if(type == 0) {
			 x_coord = x_coord + (width / 2);
			 y_coord = y_coord + height;
			 x_coord2 = x_coord;
			 y_coord2 = y_coord + 30;
		 }
		 else if(type == 1) {            //type 1 for side child
			 x_coord = x_coord + width;
			 y_coord = y_coord + (height / 2);
			 x_coord2 = x_coord - 25;
			 y_coord2 = y_coord;
			 y_deviation = 0;
		 }
		 if(type == 0) { y_coord2 += 200; }
		 Line2D.Double Line = new Line2D.Double(x_coord , y_coord , x_coord2 + x_deviation, y_coord2 + y_deviation);
		 if(type == 1) { y_coord2 = y_coord2 - (height / 2);  x_coord2 = x_coord2 + (width / 2); }
		 Rectangle2D.Double rect = new Rectangle2D.Double(x_coord2 - (width / 2) + x_deviation , y_coord2 + y_deviation, 80, 25);
		 int str_length = data.length();
		 textposX = (int) ((int)rect.getX() + (width / 2) - 2.5*str_length - 2);
		 textposY = (int)rect.getY() + 15;
		 text_positions.add(new int[] {textposX , textposY});
		 
		 inside_msg.setText(data);
		 
		 
		 shapes.add(Line);
		 shapes.add(inside_msg);
		 shapes.add(rect);
		// main_node = rect;
		 //repaint();
	 }
	 
	 public void AddLeftChildCircle(Shape main_node /*, Rectangle2D.Double node*/ ,double x_deviation ,double y_deviation , String data) {
		 
		 	JLabel inside_msg = new JLabel("");
		 	int textposX , textposY;
		 	Double x_coord = 0.0, y_coord = 0.0 , width = 0.0;
		 	if (main_node instanceof Ellipse2D) {
		 		Ellipse2D.Double node;
		 		node = (Ellipse2D.Double) main_node;
		 	    x_coord = node.getX();
				y_coord = node.getY();
				y_coord = y_coord + 15;
				width = node.getWidth();
		 	}
		 	else if(main_node instanceof Rectangle2D) {
		 		Rectangle2D.Double node;
		 		node = (Rectangle2D.Double) main_node;
		 		x_coord = node.getX();
		 		y_coord = node.getY();
		 		width = node.getWidth();
		 	}
		 	
			inside_msg.setText(data);
			x_coord = x_coord + (width / 2);
			y_coord = y_coord + 25;
			Line2D.Double Line = new Line2D.Double(x_coord - (x_deviation / 10), y_coord, x_coord - 17 - x_deviation, y_coord + 30 + y_deviation);
			Ellipse2D.Double rect = new Ellipse2D.Double(x_coord - 40 - x_deviation, y_coord + 30 + y_deviation , 40 ,40);
			int str_length = data.length();
			textposX = (int) ((int)rect.getX() + 20 - 2.5*str_length - 2);
			textposY = (int)rect.getY() + 25;
			text_positions.add(new int[] {textposX , textposY});
			
	        
	        shapes.add(Line);
	        shapes.add(inside_msg);
	        shapes.add(rect);
	       // main_node = rect;
	        //repaint();
	    }
	 
	 public void AddRightChildCircle(Shape main_node /*, Rectangle2D.Double node*/ , double x_deviation , double y_deviation , String data) {
		 
		 	JLabel inside_msg = new JLabel("");
		 	int textposX , textposY;
		 	Double x_coord = 0.0, y_coord = 0.0 , width = 0.0;
		 	if (main_node instanceof Ellipse2D) {
		 		Ellipse2D.Double node;
		 		node = (Ellipse2D.Double) main_node;
		 	    x_coord = node.getX();
				y_coord = node.getY();
				y_coord = y_coord + 15;
				width = node.getWidth();
		 	}
		 	else if(main_node instanceof Rectangle2D) {
		 		Rectangle2D.Double node;
		 		node = (Rectangle2D.Double) main_node;
		 		x_coord = node.getX();
		 		y_coord = node.getY();
		 		width = node.getWidth();
		 	}
			x_coord = x_coord + (width / 2);
			y_coord = y_coord + 25;
			Line2D.Double Line = new Line2D.Double(x_coord + (x_deviation / 10), y_coord , x_coord + 17 + x_deviation, y_coord + 30 + y_deviation);
			Ellipse2D.Double rect = new Ellipse2D.Double(x_coord + x_deviation, y_coord + 30 + y_deviation, 40 ,40);
			int str_length = data.length();
			textposX = (int) ((int)rect.getX() + 20 - 2.5*str_length - 2);
			textposY = (int)rect.getY() + 25;
			text_positions.add(new int[] {textposX , textposY});
			
			inside_msg.setText(data);
			
	        
	        shapes.add(Line);
	        shapes.add(inside_msg);
	        shapes.add(rect);
	        //main_node = rect;
	        //repaint();
	    }
	 
	 public void AddOneChildCircle(Shape main_node , /*Rectangle2D.Double node ,*/ String data) {
		 
		 JLabel inside_msg = new JLabel("");
		 int textposX , textposY;
		 Double x_coord = 0.0, y_coord = 0.0;
		 	if (main_node instanceof Ellipse2D) {
		 		Ellipse2D.Double node;
		 		node = (Ellipse2D.Double) main_node;
		 	    x_coord = node.getX();
				y_coord = node.getY();
				y_coord += 30;
		 	}
		 	else if(main_node instanceof Rectangle2D) {
		 		Rectangle2D.Double node;
		 		node = (Rectangle2D.Double) main_node;
		 		x_coord = node.getX();
		 		y_coord = node.getY();
		 		y_coord += 15;
		 		x_coord += 20;
		 	}
		 x_coord = x_coord + 17;
		 y_coord = y_coord + 10;
		 Line2D.Double Line = new Line2D.Double(x_coord, y_coord, x_coord, y_coord + 40);
		 Ellipse2D.Double rect = new Ellipse2D.Double(x_coord - 17, y_coord + 40, 40, 40);
		 int str_length = data.length();
		 textposX = (int) ((int)rect.getX() + 20   - 2.5*str_length - 2);
		 textposY = (int)rect.getY() + 25;
		 text_positions.add(new int[] {textposX , textposY});
		 
		 inside_msg.setText(data);
		 
		 
		 shapes.add(Line);
		 shapes.add(inside_msg);
		 shapes.add(rect);
		// main_node = rect;
		 //repaint();
	 }
}

/*import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawShapes extends JFrame {
	JPanel p;
	Shape main_node = new Ellipse2D.Double(100, 100, 0, 0);

	private static final long serialVersionUID = 1L;

	public DrawShapes(){

		setSize(new Dimension(320, 320));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		p = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				//Shape line = new Line2D.Double(3, 3, 303, 303);   //(x1,y1 , x2,y2)
				//Shape rect = new Rectangle(3, 3, 303, 303);
				main_node = new Ellipse2D.Double(200, 0, 25,25);    //coordinates of upper left corner of the circle (x , y , width , height)
				//Shape roundRect = new RoundRectangle2D.Double(20, 20, 250, 250, 5, 25);
				//g2.draw(line);
				//g2.draw(rect);
				g2.draw(main_node);
				//g2.draw(roundRect);
			}
		};
		setTitle("My Shapes");
		this.getContentPane().add(p);
	}
	
	public void AddLeftChildSquare(Graphics g) {
		
		//p = new JPanel() {
			//@Override
			//public void paintComponent(Graphics g) {
				Graphics2D g3 = (Graphics2D) g;
				Double x_coord = main_node.getBounds2D().getX();
				Double y_coord = main_node.getBounds2D().getY();
				x_coord = x_coord;
				y_coord = y_coord + 25;
				Shape Line = new Line2D.Double(x_coord, y_coord, x_coord - 30, y_coord + 30);
				Shape circle = new Ellipse2D.Double(x_coord - 50, y_coord + 28 , 25 ,25);
				g3.draw(Line);
				g3.draw(circle);
				main_node = circle;
			//}
			
		//};
		
		this.getContentPane().add();
	}
	public void AddRightChildSquare() {
		p = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				Graphics2D g4 = (Graphics2D) g;
				Double x_coord = main_node.getBounds2D().getX();
				Double y_coord = main_node.getBounds2D().getY();
				x_coord = x_coord + 25;
				y_coord = y_coord + 25;
				Shape Line = new Line2D.Double(x_coord , y_coord , x_coord + 30, y_coord + 30);
				Shape circle = new Ellipse2D.Double(x_coord + 27 , y_coord + 28 , 25,25);
				g4.draw(Line);
				g4.draw(circle);
				main_node = circle;
			}
		};
		this.getContentPane().add(p);
	}
}*/