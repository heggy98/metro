package METRO_V2;

import java.awt.*;

public class Point2D implements Comparable<Point2D> {

    public int X;
    public int Y;
    public String Name;
    public PointType Type;

    public Point2D(int x, int y, String name, PointType type){

        this.Name = name;
        X = x;
        Y = y;
        this.Type = type;
    }

    public Point2D clone(){

        return new Point2D(X, Y, this.Name, this.Type);
    }

    public Point getRoundedPoint(){

        return new Point(Math.round(X), Math.round(Y));
    }
    public int compareToX(Point2D o)  {

        return Integer.compare(X, o.X);
    }

    public int compareToY(Point2D o)  {

        return Integer.compare(Y, o.Y);
    }

    @Override
    public int compareTo(Point2D o) {
        return 0;
    }
}
