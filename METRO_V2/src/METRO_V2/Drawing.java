package METRO_V2;

import javax.sound.sampled.Line;
import java.awt.*;

public class Drawing extends Canvas {
    public void paint(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        MetroCalculations.CreateMetro(graphics2D);
    }

    public static void drawSinglePixel(int x, int y, Graphics2D g){
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(x, y, x, y);
    }

    public static void drawStop(Point2D point, Graphics2D g){
        drawStop(point.X, point.Y, g);
    }

    public static void drawStop(int x, int y, Graphics2D g){
        g.setColor(Color.RED);
        g.fillOval(x-10,y-10,20,20);
    }

    public static void drawTurn(Point2D point, Graphics2D g){
        drawTurn(point.X, point.Y, g);
    }

    public static void drawTurn(int x, int y, Graphics2D g){
        g.setColor(Color.YELLOW);
        g.fillOval(x-5,y-5,10,10);
    }

    public static void drawPixel(Point2D point, Graphics2D g){
        drawPixel(point.X, point.Y,g);
    }

    public static void drawPixel(int x, int y, Graphics2D g){
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x,y,2,2);
    }

    public static void drawLine(Line2D line, Graphics2D g){
        g.setColor(Color.BLACK);
        g.drawLine(line.pointA.X,line.pointA.Y,line.pointB.X,line.pointB.Y);
    }




}
