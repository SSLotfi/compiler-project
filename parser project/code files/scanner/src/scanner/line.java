package scanner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class line {
	
	Double x1 , x2 , y1 , y2;
	
	public line(Double x1 , Double y1 , Double x2 , Double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Line2D.Double Line = new Line2D.Double(x1, y1, x2, y2);
		
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(6));
       
	}

}
