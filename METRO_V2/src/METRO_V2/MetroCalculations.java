package METRO_V2;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MetroCalculations {
    public static void CreateMetro(Graphics2D g) {
        ArrayList<Point2D> stopsAndTurns = new ArrayList<>();
        ArrayList<Line2D> lines = new ArrayList<>();
        ArrayList<Point2D> metroPoints = new ArrayList<>();

        InitializeMetroStopsAndLines(stopsAndTurns, lines);

        Point2D novy_bod = new Point2D(650, 75, "Novy", PointType.STOP);


        for (var line :
                lines) {
            metroPoints = getPointsToDraw(line, metroPoints);
            Drawing.drawLine(line, g);
        }

        ArrayList<Point2D> traveled = new ArrayList<>();

        boolean[] smery = {true, true, true, true};

        long startTime = System.nanoTime();
        try {
            ZjistiTrasuPomocuKruhu(stopsAndTurns, novy_bod.X, novy_bod.Y, metroPoints, g);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());;

        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;

        long seconds = TimeUnit.SECONDS.
                convert(timeElapsed,
                        TimeUnit.NANOSECONDS);

        System.out.println("Zjištění nejbližšího bodu trvalo: " + seconds);

        Drawing.drawStop(novy_bod, g);

        for (var stopOrTurn : stopsAndTurns) {
            if (stopOrTurn.Type == PointType.TURN) {
                Drawing.drawTurn(stopOrTurn, g);
            } else {
                Drawing.drawStop(stopOrTurn, g);
            }

        }

        System.out.println("Trasa metra v kostičkách: " + metroPoints.size());


        //MarianuvZpusob(stopsAndTurns, novy_bod);
    }

    private static void InitializeMetroStopsAndLines(ArrayList<Point2D> stops, ArrayList<Line2D> lines) {
        // MARIAN
        Point2D marian = new Point2D(475, 545, "Corvere", PointType.STOP);
        stops.add(marian);

        // ZATACKA
        Point2D turn = new Point2D(400, 545, "Zatacka s Marianem", PointType.TURN);
        stops.add(turn);

        // MARIAN - ZATACKA
        Line2D marian_turn = new Line2D(marian, turn);
        lines.add(marian_turn);

        // FEJK
        Point2D fejk = new Point2D(400, 490, "Fejk", PointType.STOP);
        stops.add(fejk);

        // ZATACKA - FEJK
        Line2D line = new Line2D(turn, fejk);
        lines.add(line);

        // PRVNI ZATACKA KE SPAWNU
        Point2D second_turn = new Point2D(400, 455, "Druhá zatáčka", PointType.TURN);
        stops.add(second_turn);

        // FEJK - PRVNI ZATACKA
        Line2D line_0 = new Line2D(fejk, second_turn);
        lines.add(line_0);

        // SPAWN
        Point2D spawn = new Point2D(210, 455, "Spawn", PointType.STOP);
        stops.add(spawn);

        // PRVNI ZATACKA - SPAWN
        Line2D line_1 = new Line2D(second_turn, spawn);
        lines.add(line_1);

        // ROZCESTNIK 2
        Point2D signpost_2 = new Point2D(65, 455, "Rozcestnik 2", PointType.TURN);
        stops.add(signpost_2);

        // SPAWN - ROZCESTNIK 2
        Line2D line_2 = new Line2D(spawn, signpost_2);
        lines.add(line_2);

        // PETA
        Point2D amemphisse = new Point2D(65, 355, "Amemphisse", PointType.STOP);
        stops.add(amemphisse);

        // ROZCESTNIK 2 - PETA
        Line2D line_3 = new Line2D(signpost_2, amemphisse);
        lines.add(line_3);

        // ZATACKA
        Point2D turn_3 = new Point2D(30, 355, "Třetí zatáčka", PointType.TURN);
        stops.add(turn_3);

        // PETA - ZATACKA
        Line2D line_4 = new Line2D(amemphisse, turn_3);
        lines.add(line_4);

        // SARAH
        Point2D sarah = new Point2D(30, 125, "Sarah", PointType.STOP);
        stops.add(sarah);

        // ZATACKA - SARAH
        Line2D line_5 = new Line2D(turn_3, sarah);
        lines.add(line_5);
    }

    private static void ZjistiTrasuPomociSmeru(ArrayList<Point2D> zastavky, int x, int y, ArrayList<Point2D> traveled, ArrayList<Point2D> metroPoints, Graphics2D g, boolean[] smery) {
        int[] aktualniPozice = {x, y};
        if (smery[0] && !(traveled.stream().anyMatch(q -> q.X == (aktualniPozice[0] - 1) && q.Y == aktualniPozice[1])))
            lowerX(zastavky, aktualniPozice, traveled, metroPoints, g, smery);
        if (smery[1] && !(traveled.stream().anyMatch(q -> q.X == (aktualniPozice[0] + 1) && q.Y == aktualniPozice[1])))
            higherX(zastavky, aktualniPozice, traveled, metroPoints, g, smery);
        if (smery[2] && !(traveled.stream().anyMatch(q -> q.X == aktualniPozice[0] && q.Y == (aktualniPozice[1] + 1))))
            higherY(zastavky, aktualniPozice, traveled, metroPoints, g, smery);
        if (smery[3] && !(traveled.stream().anyMatch(q -> q.X == aktualniPozice[0] && q.Y == (aktualniPozice[1] - 1))))
            lowerY(zastavky, aktualniPozice, traveled, metroPoints, g, smery);

    }

    private static void ZjistiTrasuPomocuKruhu(ArrayList<Point2D> zastavky, int x, int y, ArrayList<Point2D> metroPoints, Graphics2D g) throws InterruptedException {
        int[] in = {1};
        int[] coordinates = {x, y};

        Ellipse2D e = new Ellipse2D.Double(coordinates[0], coordinates[1], in[0], in[0]);
        Ellipse2D[] es = {e};
        do {
            g.setColor(Color.LIGHT_GRAY);
            //g.drawOval(coordinates[0], coordinates[1], in[0], in[0]);
            es[0] = new Ellipse2D.Double(coordinates[0], coordinates[1], in[0], in[0]);
            g.draw(es[0]);
            if (in[0] % 2 == 0) {
                coordinates[0]--;
                coordinates[1]--;
            }
            in[0]++;

        }while (!(zastavky.stream().anyMatch(q -> es[0].contains(q.X, q.Y)))  && !(metroPoints.stream().anyMatch(q -> es[0].contains(q.X, q.Y))));
        /*while ((!zastavky.stream().anyMatch(q -> (q.X == x + in[0]) && (q.Y == y + in[0]) || (q.X == x + in[0]) && (q.Y == y - in[0]) || (q.X == x - in[0]) && (q.Y == y - in[0]) || (q.X == x - in[0]) && (q.Y == y + in[0]))) || (!metroPoints.stream().anyMatch(q -> (q.X == x + in[0]) && (q.Y == y + in[0]) || (q.X == x + in[0]) && (q.Y == y - in[0]) || (q.X == x - in[0]) && (q.Y == y - in[0]) || (q.X == x - in[0]) && (q.Y == y + in[0]))));*/
    }

    private static void lowerY(ArrayList<Point2D> zastavky, int[] aktualniPozice, ArrayList<Point2D> traveled, ArrayList<Point2D> bodiky, Graphics2D g, boolean[] smery) {
        while (zastavky.stream().anyMatch(q -> q.Y < aktualniPozice[1]) && !(bodiky.stream().anyMatch(p -> p.X == aktualniPozice[0] && p.Y == aktualniPozice[1]))) {

            aktualniPozice[1] = aktualniPozice[1] - 1;

            long time = System.nanoTime();

            Drawing.drawSinglePixel(aktualniPozice[0], aktualniPozice[1], g);
            long endTime = System.nanoTime();

            System.out.println("LY: " + (endTime - time));

            Point2D finder = new Point2D(aktualniPozice[0], aktualniPozice[1], "Cesta", PointType.FINDER);

            traveled.add(finder);
//            smery[2] = false;
//
//            ZjistiTrasu(zastavky, aktualniPozice[0], aktualniPozice[1], traveled, bodiky, g, smery);
        }
    }

    private static void higherY(ArrayList<Point2D> zastavky, int[] aktualniPozice, ArrayList<Point2D> traveled, ArrayList<Point2D> bodiky, Graphics2D g, boolean[] smery) {
        while (zastavky.stream().anyMatch(q -> q.Y > aktualniPozice[1]) && !(bodiky.stream().anyMatch(p -> p.X == aktualniPozice[0] && p.Y == aktualniPozice[1]))) {
            aktualniPozice[1] = aktualniPozice[1] + 1;

            long time = System.nanoTime();

            Drawing.drawSinglePixel(aktualniPozice[0], aktualniPozice[1], g);

            long endTime = System.nanoTime();
            System.out.println("HY: " + (endTime - time));
            Point2D finder = new Point2D(aktualniPozice[0], aktualniPozice[1], "Cesta", PointType.FINDER);

            traveled.add(finder);
//            smery[3] = false;
//
//            ZjistiTrasu(zastavky, aktualniPozice[0], aktualniPozice[1], traveled, bodiky, g, smery);
        }
    }

    private static void higherX(ArrayList<Point2D> zastavky, int[] aktualniPozice, ArrayList<Point2D> traveled, ArrayList<Point2D> bodiky, Graphics2D g, boolean[] smery) {
        while (zastavky.stream().anyMatch(q -> q.X > aktualniPozice[0]) && !(bodiky.stream().anyMatch(p -> p.X == aktualniPozice[0] && p.Y == aktualniPozice[1]))) {
            aktualniPozice[0] = aktualniPozice[0] + 1;

            long time = System.nanoTime();

            Drawing.drawSinglePixel(aktualniPozice[0], aktualniPozice[1], g);

            long endTime = System.nanoTime();

            System.out.println("HX: " + (endTime - time));

            Point2D finder = new Point2D(aktualniPozice[0], aktualniPozice[1], "Cesta", PointType.FINDER);

            traveled.add(finder);
//            smery[0] = false;
//
//            ZjistiTrasu(zastavky, aktualniPozice[0], aktualniPozice[1], traveled, bodiky, g, smery);
        }
    }

    private static void lowerX(ArrayList<Point2D> zastavky, int[] aktualniPozice, ArrayList<Point2D> traveled, ArrayList<Point2D> bodiky, Graphics2D g, boolean[] smery) {
        while (zastavky.stream().anyMatch(q -> q.X < aktualniPozice[0]) && !(bodiky.stream().anyMatch(p -> p.X == aktualniPozice[0] && p.Y == aktualniPozice[1]))) {
            aktualniPozice[0] = aktualniPozice[0] - 1;

            long time = System.nanoTime();

            Drawing.drawSinglePixel(aktualniPozice[0], aktualniPozice[1], g);

            long endTime = System.nanoTime();

            System.out.println("LX: " + (endTime - time));
            Point2D finder = new Point2D(aktualniPozice[0], aktualniPozice[1], "Cesta", PointType.FINDER);

            traveled.add(finder);
//            smery[1] = false;
//
//            ZjistiTrasu(zastavky, aktualniPozice[0], aktualniPozice[1], traveled, bodiky, g, smery);
        }
    }

    private static void MarianuvZpusob(ArrayList<Point2D> zastavky, Point2D novy_bod) {

        double newDx = 0, newDy = 0;
        double returnementX, returnementY;

        double rozdilX = 0, rozdilY = 0;

        Point2D nejblizsiZastavka1 = null;
        Point2D nejblizsiZastavka2 = null;
        Point2D nejblizsiZastavka;

        for (var zastavka : zastavky) {
            returnementX = GetDx(novy_bod, zastavka);
            if (returnementX < newDx || newDx == 0) {
                newDx = returnementX;
                rozdilY = GetDx(novy_bod, zastavka);
                nejblizsiZastavka1 = zastavka;
            }

            returnementY = GetDy(novy_bod, zastavka);
            if (returnementY < newDy || newDy == 0) {
                newDy = returnementY;
                rozdilX = GetDx(novy_bod, zastavka);
                nejblizsiZastavka2 = zastavka;
            }
        }


        double prvniZastavkaRozdil = newDx + rozdilY;
        double druhaZastavkaRozdil = newDy + rozdilX;
        double rozdil;

        System.out.println("Nejbližší dvě zastávky od bodu: " + nejblizsiZastavka1.Name + " (" + prvniZastavkaRozdil + "), " + nejblizsiZastavka2.Name + " (" + druhaZastavkaRozdil + ")");

        if (prvniZastavkaRozdil > druhaZastavkaRozdil) {
            rozdil = druhaZastavkaRozdil;
            nejblizsiZastavka = nejblizsiZastavka2;
        } else {
            rozdil = prvniZastavkaRozdil;
            nejblizsiZastavka = nejblizsiZastavka1;
        }

        System.out.println("Bližší zastávka z těchto dvou je: '" + nejblizsiZastavka.Name + "' s menším rozdilem který je: " + rozdil);

        List<Point2D> blizkeZastavky = zastavky.stream().filter(x -> x.X == nejblizsiZastavka.X || x.Y == nejblizsiZastavka.Y).collect(Collectors.toList());

        System.out.println("----------------------------------------------------------------------");
        System.out.println("Výpis zastávek a zatáček na stejné X-ové nebo Y-silonové souřadnici: ");

        for (var zastavka :
                blizkeZastavky) {
            if (zastavka == nejblizsiZastavka)
                continue;

            System.out.println("Zastavka '" + zastavka.Name + "' -> Rozdil X a Y od nejbližší zastávky/zatáčky (" + nejblizsiZastavka.Name + "): " + GetDx(zastavka, nejblizsiZastavka) + ", " + GetDy(zastavka, nejblizsiZastavka));

            if (zastavka.X == novy_bod.X) {
                System.out.println("Tato zastávka: '" + zastavka.Name + "' má stejnou Xovou souřadnici.");
            }
            if (zastavka.Y == novy_bod.Y) {
                System.out.println("Tato zastávka: '" + zastavka.Name + "' má stejnou Yovou souřadnici.");
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

    private static double GetDx(Point2D novy_bod, Point2D zastavka) {
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

    private static double GetDy(Point2D novy_bod, Point2D zastavka) {
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

    public static ArrayList<Point2D> getPointsToDraw(Line2D line, ArrayList<Point2D> bodiky) {

        Point2D A = line.pointA;
        Point2D B = line.pointB;

        if (A.X > B.X) {
            A = line.pointB;
            B = line.pointA;
        }

        int xA = A.X;
        int yA = A.Y;

        int xB = B.X;

        int xd = 0;
        if (xB > 0 && xA > 0) {
            xd = xB - xA;
        } else if (xB < 0 && xA < 0) {
            xd = Math.abs(xA) - Math.abs(xB);
        } else if (xB < 0) {
            xd = xA + Math.abs(xB);
        } else if (xA < 0) {
            xd = Math.abs(xA) + xB;
        } else if (xA == 0 || xB == 0) {
            xd = Math.abs(xA + xB);
        }


        if (xd != 0) {
            for (int i = 0; i < xd; i++) {
                Point2D bod = new Point2D(xA + i, yA, "", PointType.METRO);
                bodiky.add(bod);
            }
        }

        if (A.Y > B.Y) {
            A = line.pointB;
            B = line.pointA;
        }

        xA = A.X;
        yA = A.Y;

        xB = B.X;
        double yB = B.Y;

        double yd = 0;
        if (yB > 0 && yA > 0) {
            yd = yB - yA;
        } else if (yB < 0 && yA < 0) {
            yd = Math.abs(yA) - Math.abs(yB);
        } else if (yB < 0) {
            yd = yA + Math.abs(yB);
        } else if (yA < 0) {
            yd = Math.abs(yA) + yB;
        } else if (yA == 0 || yB == 0) {
            yd = Math.abs(yA + yB);
        }

        if (yd != 0) {
            for (int i = 0; i < yd; i++) {
                Point2D bod = new Point2D(xB, yA + i, "", PointType.METRO);
                bodiky.add(bod);
            }
        }

        return bodiky;
    }
}
