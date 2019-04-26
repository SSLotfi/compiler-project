package scanner;


import java.awt.EventQueue;
import java.awt.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class treeGUI {
	
	String[] Labels ;
	String[][] Shapes;
	String[][][] Shapes_Labels;
	int[] parent_child_num , parent_index;
	

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	/*public static void tree() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					treeGUI window = new treeGUI();
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public treeGUI(String[][] Shape , String[] Labels , int Node_Num , String[] token , int[] parent_indexs , int x_value , int y_value) {
		parent_index = parent_indexs;
		System.out.println(Node_Num);
		Adjust_Shapes(Shape , token , Node_Num);
		initialize(Shape , Labels , Node_Num , token , parent_index , x_value , y_value);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String[][] Shape , String[] Label , int Node_Num , String[] token , int[] parent_index , int x_value , int y_value) {
		/*frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/

		
		JFrame frame = new JFrame();
        frame.add(new DrawShapes(Node_Num, Shapes , Labels , token , parent_index , parent_child_num , x_value , y_value));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
		
	}
	
	public void sort_string(String[][] array , int size) {
		
		for(int i = 0 ; i < size ; i++) {
			for(int j = i ; j < size ; j++) {
				int min = Integer.valueOf(array[i][1]);
				int min_index = i;
				if(Integer.valueOf(array[j][1]) < min) {
					min = Integer.valueOf(array[j][1]);
					min_index = j;
				}
				String temp1 = array[i][1];
				String temp2 = array[i][0];
				array[i][1] = array[min_index][1];
				array[i][0] = array[min_index][0];
				array[min_index][1] = temp1;
				array[min_index][0] = temp2;
			}
		}
	}
	
	
	private void Adjust_Shapes(String[][] Shape , String[] token , int node_num ) {
		
		System.out.println("adjust : " + node_num);
		Shapes = new String[node_num][2];
		Labels = new String[node_num];
		
		Shapes = Shape;
		for(int i = 0 ; i < node_num ; i++) {
			System.out.println(Shapes[i][1] + "  ");
			System.out.println(Shapes[i][0] + "  ");
		}
		
		sort_string(Shapes , node_num);
		
		Arrays.sort(parent_index);
		Set<Integer> set = new HashSet<Integer>();
		
		for(int num : parent_index) {
			set.add(num);
		}
		
		parent_index = new int[set.size() - 1];
		
		
		ArrayList<Integer> list = new ArrayList<Integer>(set);
		Collections.sort(list);
		
		for(int i = 0 ; i < set.size() - 1; i++) {
			parent_index[i] = list.get(i);
		}
		
		parent_child_num = new int[set.size() - 1];
		Arrays.fill(parent_child_num, 0);
		
		for(int i = 0 ; i < set.size() - 1; i++) {
			for(int j = 0 ; j < node_num ; j++) {
				if(Integer.valueOf(Shapes[j][1]) == parent_index[i]) {
					parent_child_num[i]++;
				}
			}
		}
		
		for(int i = 0 ; i < node_num ; i++) {
			String[] temp;
			temp = Shapes[i][0].split(Pattern.quote(","));    //shape[0] = shape,label;parentindex 
			String[] temp2 = temp[1].split(Pattern.quote(";"));
			Labels[i] = temp2[0];
			System.out.println("temp2[1] :  " + temp2[1]);
			Shapes[i][0] = temp[0] + ";" + temp2[1];
			System.out.println("string[i][0] zzzzzz " + Shapes[i][0] + "label: " + Labels[i]);
		}
		
		
		for(int i = 0 ; i < node_num ; i++) {
			System.out.println("after sorting ::: shape: " + Shapes[i][0] + " label : " + Labels[i] + "\n" + "my parent is : " + Shapes[i][1]);
			
		}
		
		for(int i = 0 ; i < parent_index.length ; i++) {
			System.out.println("parents  " + parent_index[i] + "   child num :" + parent_child_num[i]);
		}
	
	}

}
