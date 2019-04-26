package scanner;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.ScrollPane;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JSlider;
import javax.swing.JLabel;

public class controls {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					controls window = new controls();
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
	public controls() {
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSlider x_slider = new JSlider();
		x_slider.setBounds(143, 32, 200, 26);
		frame.getContentPane().add(x_slider);
		
		JSlider y_slider = new JSlider();
		y_slider.setBounds(143, 109, 200, 26);
		frame.getContentPane().add(y_slider);
		
		JLabel lblControlXaxis = new JLabel("control x-axis");
		lblControlXaxis.setBounds(32, 32, 101, 26);
		frame.getContentPane().add(lblControlXaxis);
		
		JLabel lblControlYaxis = new JLabel("control y-axis");
		lblControlYaxis.setBounds(32, 98, 101, 26);
		frame.getContentPane().add(lblControlYaxis);
		
		
		
	}
}
