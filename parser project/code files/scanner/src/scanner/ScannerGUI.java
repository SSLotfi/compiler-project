package scanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ScannerGUI {

	private JFrame frame;
	private JTextField input_textfield;
	private JTextField output_textfield;
	private String input_file_path , output_file_path , x_value_string , y_value_string;
	private JTextField x_dev;
	private JTextField y_dev;
	int x_value = 0 , y_value = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ScannerGUI window = new ScannerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ScannerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 572, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnScan = new JButton("Scan");
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					input_file_path = input_textfield.getText();
					output_file_path = output_textfield.getText();
					x_value_string = x_dev.getText();
					y_value_string = y_dev.getText();
					
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "Please enter a file path!");
				}
				scanner scanner = new scanner();
				parser parser = new parser();
				try{
					scanner.scan(input_file_path , output_file_path);
					JOptionPane.showMessageDialog(null, "Scanning done please check the output file in " + output_file_path);
					
					frame.dispose();
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Error , Please select a valid input or output file");
				}
				
				try {
					parser.parser(output_file_path);
					String[][] Shapes = parser.Get_Shapes();
					String[] Labels = parser.Get_Labels();
					int node_num = parser.Get_Number_Nodes();
					String[] token = parser.Get_Tokens();
					int[] parent_index = parser.Get_Parents();
					
					x_value = Integer.valueOf(x_value_string);
					y_value = Integer.valueOf(y_value_string);
					
					for(int i = 0 ; i < node_num ; i++)
					{
							System.out.println(Shapes[i][0] + "\nparent : " + Shapes[i][1] + "\r\n" + "label: " + Labels[i]);
							System.out.println("parents : " + parent_index[i]);
					}
					System.out.println(node_num);
					treeGUI treeGUI = new treeGUI(Shapes , Labels , node_num , token , parent_index , x_value , y_value);
					//controls control = new controls();
					
				}
				catch(Exception e) {
					JOptionPane.showMessageDialog(null, "Parser Error");
				}
				
				//JOptionPane.showMessageDialog(null, "Scanning done please check the output file in "+output_file_path);
				
				
				
			}
		});
		btnScan.setBounds(206, 146, 89, 23);
		frame.getContentPane().add(btnScan);
		
		input_textfield = new JTextField();
		input_textfield.setBounds(139, 27, 265, 20);
		frame.getContentPane().add(input_textfield);
		input_textfield.setColumns(10);
		
		output_textfield = new JTextField();
		output_textfield.setBounds(139, 75, 265, 20);
		frame.getContentPane().add(output_textfield);
		output_textfield.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Input file path");
		lblNewLabel.setBounds(34, 30, 76, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblOutputFilePath = new JLabel("Output file path");
		lblOutputFilePath.setBounds(34, 78, 89, 14);
		frame.getContentPane().add(lblOutputFilePath);
		
		JLabel lblFilePathExample = new JLabel("File path Example: F:\\projects\\inputfile.txt");
		lblFilePathExample.setBounds(34, 121, 370, 14);
		frame.getContentPane().add(lblFilePathExample);
		
		x_dev = new JTextField();
		x_dev.setText("0");
		x_dev.setBounds(111, 209, 86, 20);
		frame.getContentPane().add(x_dev);
		x_dev.setColumns(10);
		
		y_dev = new JTextField();
		y_dev.setText("0");
		y_dev.setBounds(318, 209, 86, 20);
		frame.getContentPane().add(y_dev);
		y_dev.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("y_deviation");
		lblNewLabel_1.setBounds(238, 212, 70, 14);
		frame.getContentPane().add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("x_deviation");
		lblNewLabel_2.setBounds(27, 212, 74, 14);
		frame.getContentPane().add(lblNewLabel_2);
		
		JLabel lblInCaseThe = new JLabel("In case the nodes overlaped please change x_deviation and y_deviation for better results");
		lblInCaseThe.setBounds(10, 170, 536, 28);
		frame.getContentPane().add(lblInCaseThe);
	}
}
