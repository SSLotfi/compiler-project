package scanner;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class circle {

    Double x, y, width, height;

    public circle(Double x, Double y , Double width , Double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public double getx() {
    	return x;
    }
    
    public double gety() {
    	return y;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Ellipse2D.Double circle = new Ellipse2D.Double(x, y, width, height);
        
        g2d.setColor(Color.BLACK);
        //g2d.fill(circle);
        g2d.setStroke(new BasicStroke(3));
    }

}
