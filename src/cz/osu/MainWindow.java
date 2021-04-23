package cz.osu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainWindow extends JPanel {

    private ImagePanel imagePanel;
    private JLabel infoLabel;

    private V_RAM vram;

    public MainWindow() {

        // X + 100 , Y + 750

        initialize();
        vram = new V_RAM(200, 200);
        GraphicsOperations.fillBrightness(vram, 255);
        ArrayList<Point2D> zastavky = new ArrayList<>();
        ArrayList<Line2D> lines = new ArrayList<>();
        ArrayList<Point2D> bodiky = new ArrayList<>();

        // MARIAN
        Point2D marian = new Point2D(99, 109, "Corvere");
        zastavky.add(marian);

        // ZATACKA
        Point2D zatacka = new Point2D(80, 109, "Zatacka s Marianem");
        zastavky.add(zatacka);

        // MARIAN - ZATACKA
        Line2D Marian_Zatacka = new Line2D(marian, zatacka);
        lines.add(Marian_Zatacka);

        // FEJK
        Point2D fejk = new Point2D(80, 98, "Fejk");
        zastavky.add(fejk);

        // ZATACKA - FEJK
        Line2D line = new Line2D(zatacka, fejk);
        lines.add(line);

        // PRVNI ZATACKA KE SPAWNU
        Point2D prvniZatacka = new Point2D(80, 91, "Prvni zatacka");
        zastavky.add(prvniZatacka);

        // FEJK - PRVNI ZATACKA
        Line2D line_0 = new Line2D(fejk, prvniZatacka);
        lines.add(line_0);

        // SPAWN
        Point2D spawn = new Point2D(42, 91, "Spawn");
        zastavky.add(spawn);

        // PRVNI ZATACKA - SPAWN
        Line2D line_1 = new Line2D(prvniZatacka, spawn);
        lines.add(line_1);

        // ROZCESTNIK 2
        Point2D rozcestnik2 = new Point2D(13, 91, "Rozcestnik 2");
        zastavky.add(rozcestnik2);

        // SPAWN - ROZCESTNIK 2
        Line2D line_2 = new Line2D(spawn, rozcestnik2);
        lines.add(line_2);

        // PETA
        Point2D peta = new Point2D(13, 71, "Peti zastavka");
        zastavky.add(peta);

        // ROZCESTNIK 2 - PETA
        Line2D line_3 = new Line2D(rozcestnik2, peta);
        lines.add(line_3);

        // ZATACKA
        Point2D zatacka_ = new Point2D(6, 71, "Random xd Zatacka");
        zastavky.add(zatacka_);

        // PETA - ZATACKA
        Line2D line_4 = new Line2D(peta, zatacka_);
        lines.add(line_4);

        // SARAH
        Point2D sarah = new Point2D(6, 25, "Sarah");
        zastavky.add(sarah);

        // ZATACKA - SARAH
        Line2D line_5 = new Line2D(zatacka_, sarah);
        lines.add(line_5);

        for (var point : zastavky) {
            vram.setPixel(point, 0, true);
        }

        for (var lajna :
                lines) {
            GraphicsOperations.drawLine(vram, lajna, 128);
        }

        Point2D novy_bod = new Point2D(193, 8, "Novy");

        Point2D novy_bod1 = new Point2D(50, 160, "Novy");
        Point2D novy_bod2 = new Point2D(75, 100, "Novy");

        ZjistiTrasu(zastavky, novy_bod);


        imagePanel.setImage(vram.getImage());
    }

    private void ZjistiTrasu(ArrayList<Point2D> zastavky, Point2D novy_bod) {
        vram.setPixel(novy_bod, 0, true);

        int[] X = {(int) novy_bod.X};

        boolean Xplus = false, XMinus = false, YPlus = false, YMinus = false;
        int XMinusCounter = 0,
                XPlusCounter = 0,
                YPlusCounter = 0,
                YMinusCounter = 0;

        for (double i = 0; zastavky.stream().anyMatch(x -> x.X > X[0]); i++) {
            vram.setPixel(X[0], (int) novy_bod.Y, 128);
            X[0] = (int) (X[0] + i);
            XPlusCounter++;
        }

        for (double i = 0; zastavky.stream().anyMatch(x -> x.X < X[0]); i--) {
            vram.setPixel(X[0], (int) novy_bod.Y, 128);
            X[0] = (int) (X[0] + i);
            XMinusCounter++;
        }


        int[] Y = {(int) novy_bod.Y};

        for (double i = 0; zastavky.stream().anyMatch(x -> x.Y > Y[0]); i++) {
            vram.setPixel((int) novy_bod.X, Y[0], 128);
            Y[0] = (int) (Y[0] + i);
            YPlusCounter++;
        }

        for (double i = 0; zastavky.stream().anyMatch(x -> x.Y < Y[0]); i--) {
            vram.setPixel((int) novy_bod.X, Y[0], 128);
            Y[0] = (int) (Y[0] + i);
            YMinusCounter++;
        }

        double newDx = 0, newDy = 0;
        double returnementX, returnementY;
        String jmenoZastavkyX = "nevim", jmenoZastavkyY = "nevim";

        double rozdilX = 0, rozdilY = 0;

        Point2D nejblizsiZastavka1 = null;
        Point2D nejblizsiZastavka2 = null;
        Point2D nejblizsiZastavka;

        for (var zastavka : zastavky) {
            returnementX = GetDx(novy_bod, zastavka);
            if(returnementX < newDx || newDx == 0){
                newDx = returnementX;
                rozdilY = GetDx(novy_bod, zastavka);
                nejblizsiZastavka1 = zastavka;
            }

            returnementY = GetDy(novy_bod, zastavka);
            if(returnementY < newDy || newDy == 0){
                newDy = returnementY;
                rozdilX = GetDx(novy_bod, zastavka);
                nejblizsiZastavka2 = zastavka;
            }
        }
        System.out.println("Nejbližší dvě zastávky od bodu: " + nejblizsiZastavka1.Name + ", " + nejblizsiZastavka2.Name);

        double prvniZastavkaRozdil = newDx + rozdilY;
        double druhaZastavkaRozdil = newDy + rozdilX;
        double rozdil;

        if(prvniZastavkaRozdil > druhaZastavkaRozdil){
            rozdil = druhaZastavkaRozdil;
            nejblizsiZastavka = nejblizsiZastavka1;
        }
        else{
            rozdil = prvniZastavkaRozdil;
            nejblizsiZastavka = nejblizsiZastavka2;
        }

        System.out.println("Bližší zastávka z těchto dvou je: " + nejblizsiZastavka.Name + " s rozdilem:  " + rozdil);

        List<Point2D> blizkeZastavky = zastavky.stream().filter(x -> x.X == nejblizsiZastavka.X || x.Y == nejblizsiZastavka.Y).collect(Collectors.toList());



        for (var zastavka :
                blizkeZastavky) {
            System.out.println("Zastavka " + zastavka.Name + ", Rozdil X: " + GetDx(zastavka, nejblizsiZastavka) + ", Rozdil Y: " + GetDy(zastavka, nejblizsiZastavka));

            if(zastavka.X == novy_bod.X){
                System.out.println("Tato zastávka: " + zastavka.Name + " má stejnou Xovou souřadnici.");
            }
            if(zastavka.Y == novy_bod.Y){
                System.out.println("Tato zastávka: " + zastavka.Name + " má stejnou Yovou souřadnici.");
            }
        }

        double biggestX = blizkeZastavky.stream().max(Point2D::compareToX).get().X;
        double lowestX = blizkeZastavky.stream().min(Point2D::compareToX).get().X;
        double biggestY = blizkeZastavky.stream().max(Point2D::compareToY).get().Y;
        double lowestY = blizkeZastavky.stream().min(Point2D::compareToY).get().Y;

        System.out.println(biggestX);
        System.out.println(biggestY);
        System.out.println(lowestX);
        System.out.println(lowestY);

    }

    private double GetDx(Point2D novy_bod, Point2D zastavka) {
        double vetsi = zastavka.X;
        double mensi = novy_bod.X;

        if (mensi > vetsi) {
            vetsi = novy_bod.X;
            mensi = zastavka.X;
        }

        if (vetsi > 0 && mensi > 0) {
            return vetsi - mensi;
        } else if (vetsi < 0 && mensi < 0) {
            return Math.abs(vetsi) - Math.abs(mensi);
        } else if (vetsi < 0) {
            return mensi + Math.abs(vetsi);
        } else if (mensi < 0) {
            return Math.abs(mensi) + vetsi;
        } else if (vetsi == 0 || mensi == 0) {
            return Math.abs(vetsi + mensi);
        }
        return 0;
    }

    private double GetDy(Point2D novy_bod, Point2D zastavka) {
        double vetsi = zastavka.Y;
        double mensi = novy_bod.Y;

        if (mensi > vetsi) {
            vetsi = novy_bod.Y;
            mensi = zastavka.Y;
        }

        if (vetsi > 0 && mensi > 0) {
            return vetsi - mensi;
        } else if (vetsi < 0 && mensi < 0) {
            return Math.abs(vetsi) - Math.abs(mensi);
        } else if (vetsi < 0) {
            return mensi + Math.abs(vetsi);
        } else if (mensi < 0) {
            return Math.abs(mensi) + vetsi;
        } else if (vetsi == 0 || mensi == 0) {
            return Math.abs(vetsi + mensi);
        }
        return 0;
    }

    private void initialize() {

        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();

        imagePanel = new ImagePanel();
        imagePanel.setBounds(10, 60, 970, 600);
        this.add(imagePanel);

        /*
        //open image
        JButton button = new JButton();
        button.setBounds(150,10,120,30);
        button.setText("Load Image");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                openImage();
            }
        });

        this.add(button);
        */

        //save image as PNG
        JButton button4 = new JButton();
        button4.setBounds(10, 10, 120, 30);
        button4.setText("Save as PNG");
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImageAsPNG();
            }
        });

        this.add(button4);

        infoLabel = new JLabel();
        infoLabel.setBounds(850, 10, 120, 30);
        infoLabel.setText("Rotation");
        infoLabel.setFont(new Font(infoLabel.getName(), Font.BOLD, 20));

        this.add(infoLabel);

        JFrame frame = new JFrame("Raster Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setSize(1002, 650);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private void openImage() {

        String userDir = System.getProperty("user.home");
        JFileChooser fc = new JFileChooser(userDir + "/Desktop");
        fc.setDialogTitle("Load Image");

        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();

            try {

                BufferedImage temp = ImageIO.read(file);

                if (temp != null) {

                    imagePanel.setImage(temp);

                } else {

                    JOptionPane.showMessageDialog(null, "Unable to load image", "Open image: ", JOptionPane.ERROR_MESSAGE);
                }

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    private void saveImageAsPNG() {

        String userDir = System.getProperty("user.home");
        JFileChooser fc = new JFileChooser(userDir + "/Desktop");
        fc.setDialogTitle("Save Image as PNG");

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();

            String fname = file.getAbsolutePath();

            if (!fname.endsWith(".png")) file = new File(fname + ".png");

            try {

                ImageIO.write(imagePanel.getImage(), "png", file);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        new MainWindow();
    }
}
