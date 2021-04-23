package cz.osu;

import java.awt.*;
import java.util.ArrayList;

public class GraphicsOperations {

    public static void fillBrightness(V_RAM vram, int brightness){

        brightness = Math.min(255, Math.max(0, brightness));

        for(int y = 0; y < vram.getHeight(); y++)
            for(int x = 0; x < vram.getWidth(); x++)
                vram.getRawData()[y][x] = brightness;
    }

    public static void drawLine(V_RAM vram, Line2D line, int brightness){

        bresenhamLineDrawing(vram, line, brightness);
    }

    public static ArrayList<Point2D> nakresliCaru(V_RAM vram, Line2D line, ArrayList<Point2D> bodiky){

        Point2D A = line.pointA;
        Point2D B = line.pointB;

        if(A.X > B.X){
            A = line.pointB;
            B = line.pointA;
        }

        int xA = A.X;
        int yA = A.Y;

        int xB = B.X;

        int xd = 0;
        if(xB > 0 && xA > 0)
        {
           xd = xB - xA;
        }
        else if(xB < 0 && xA < 0) {
            xd = Math.abs(xA) - Math.abs(xB);
        }

        else if(xB < 0) {
            xd = xA + Math.abs(xB);
        }

        else if(xA < 0) {
            xd = Math.abs(xA) + xB;
        }

        else if(xA == 0 || xB == 0) {
            xd = Math.abs(xA + xB);
        }


        if(xd != 0){
            for(int i = 0; i < xd; i++ ){
                vram.setPixel((int) xA + i, (int) yA, 0);
                Point2D bod = new Point2D(xA + 1, yA, "");
                bodiky.add(bod);
            }
        }

        if(A.Y > B.Y){
            A = line.pointB;
            B = line.pointA;
        }

        xA = A.X;
        yA = A.Y;

        xB = B.X;
        double yB = B.Y;

        double yd = 0;
        if(yB > 0 && yA > 0)
        {
            yd = yB - yA;
        }
        else if(yB < 0 && yA < 0) {
            yd = Math.abs(yA) - Math.abs(yB);
        }

        else if(yB < 0) {
            yd = yA + Math.abs(yB);
        }

        else if(yA < 0) {
            yd = Math.abs(yA) + yB;
        }

        else if(yA == 0 || yB == 0) {
            yd = Math.abs(yA + yB);
        }

        if(yd != 0){
            for(int i = 0; i < yd; i++ ){
                vram.setPixel((int) xB, (int) yA + i, 128);
                Point2D bod = new Point2D(xB, yA + 1, "");
                bodiky.add(bod);
            }
        }

        return bodiky;
    }

    public static void drawTriangle(V_RAM vram, Triangle2D triangle, int brightness){

        bresenhamLineDrawing(vram, new Line2D(triangle.pointA, triangle.pointB), brightness);
        bresenhamLineDrawing(vram, new Line2D(triangle.pointB, triangle.pointC), brightness);
        bresenhamLineDrawing(vram, new Line2D(triangle.pointC, triangle.pointA), brightness);
    }

    private static void bresenhamLineDrawing(V_RAM vram, Line2D line, int brightness) {

        Point point0 = line.pointA.getRoundedPoint();
        Point point1 = line.pointB.getRoundedPoint();

        int dx =  Math.abs(point1.x - point0.x);
        int sx = point0.x < point1.x ? 1 : -1;

        int dy = -Math.abs(point1.y - point0.y);
        int sy = point0.y < point1.y ? 1 : -1;

        int err = dx + dy;
        int e2;

        while (true){

            if(point0.x >= 0 && point0.x < vram.getWidth() && point0.y >= 0 && point0.y < vram.getHeight()){

                vram.setPixel(point0.x, point0.y, brightness);
            }

            if (point0.x == point1.x && point0.y == point1.y) break;

            e2 = 2 * err;

            if (e2 >= dy) { err += dy; point0.x += sx; } /* e_xy+e_x > 0 */
            if (e2 <= dx) { err += dx; point0.y += sy; } /* e_xy+e_y < 0 */
        }
    }
}
