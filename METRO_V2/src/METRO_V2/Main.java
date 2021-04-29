package METRO_V2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {

//        new MyFrame();

        JFrame frame = new JFrame("Metro");
        Drawing drawing = new Drawing();
        drawing.setSize(700, 600);
        frame.add(drawing);
        frame.pack();
        frame.setVisible(true);
    }


}
