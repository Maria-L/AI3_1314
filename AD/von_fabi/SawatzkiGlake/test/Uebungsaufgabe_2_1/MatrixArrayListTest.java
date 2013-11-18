/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

import java.util.Random;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import Uebungsaufgabe_1_1.Messung1;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class MatrixArrayListTest {
    int ELEMENTS_IN_TUPEL = 2;
    int ELEMENTS_IN_TRIPEL = 3;

   
    Matrix testMatrix1 = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(1.0,2.0,3.0),Arrays.asList(4.0,5.0,6.0)));
    Matrix ematrix = testMatrix1.einheitsmatrix(3);
    double DELTA = 0.001;

     /**
     * Test zur Initialisierung der neuen Matrizen, mit der neuen Datenstruktur. Wir erlauben eine Initialisierung
      * mit einer beliebigen Collection, welche ebenfalls eine beliebige Collection enthält. Dies testen wir
      * beispielhaft mit der Arrays.asList()-Funktion. Die Werte müssen automatisch in der richtigen Liste und an der
      * richtigen Stelle landen. All dies überprüfen wir innerhalb dieses Testfalls.
     */
    @Test
    public void testInit() {
          List<Double>[]testMatrix = new List[2];
         testMatrix[0] = Arrays.asList(1.0,2.0,3.0);
         testMatrix[1] = Arrays.asList(4.0,5.0,6.0);
        
        assertEquals(testMatrix1.getValue(0, 0),testMatrix[0].get(0),DELTA);
        assertEquals(testMatrix1.getValue(0, 1),testMatrix[0].get(1),DELTA);
        assertEquals(testMatrix1.getValue(0, 2),testMatrix[0].get(2),DELTA);

        assertEquals(testMatrix1.getValue(1, 0),testMatrix[1].get(0),DELTA);
        assertEquals(testMatrix1.getValue(1, 1),testMatrix[1].get(1),DELTA);
        assertEquals(testMatrix1.getValue(1, 2),testMatrix[1].get(2),DELTA);
    }

    /**
     * Die Add-Funktion muss sowohl mit positiven als auch mit negativen Werten umgehen können und problemlos addieren, solange die Matrizen
     * identische Ausmaße besitzen.
     */
    @Test
    public void testAdd() {
        Matrix nullMatrix = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(0.0,0.0,0.0),Arrays.asList(0.0,0.0,0.0)));
        Matrix negativeMatrix = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(-5.0,-5.0,-5.0),Arrays.asList(-5.0,-4.0,-5.0)));
        Matrix resultMatrix = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(-4.0,-3.0,-2.0),Arrays.asList(-1.0,1.0,1.0)));
        assertEquals(testMatrix1,testMatrix1.add(nullMatrix));
        Matrix res = testMatrix1.add(negativeMatrix);
        assertEquals(res, resultMatrix);
    }

    /**
     * Wird eine Matrix mit einem Skalar multipliziert, so erhöht sich jeder Wert um den Faktor des angegebenen Skalares.
     */
    @Test
    public void testSkalarMulti() {
        Matrix ergebnisMatrix1 = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(2.0,4.0,6.0),Arrays.asList(8.0,10.0,12.0)));
        assertEquals(ergebnisMatrix1, testMatrix1.skalarMulti(2));
        Matrix ergebnisMatrix2 = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(-2.0,-4.0,-6.0),Arrays.asList(-8.0,-10.0,-12.0)));
        assertEquals(ergebnisMatrix2, testMatrix1.skalarMulti(-2));
    }

    /**
     * Zwei Matrizen können miteinander multipliziert werden, wenn die Spaltenzahl der ersten Matrix
     * mit der Zeilenzahl der zweiten Matrix übereinstimmt.
     */
    @Test
    public void testMatrixMulti() {
        Collection w5  = Arrays.asList(1.0,1.0,1.0);
        Collection w6  = Arrays.asList(1.0,1.0,1.0);
        Collection w8  = Arrays.asList(w5,w6);
        Matrix testM1 = new MatrixArrayList(2, 3, w8);
        Collection w1  = Arrays.asList(1.0,1.0,1.0);
        Collection w2  = Arrays.asList(1.0,1.0,1.0);
        Collection w3  = Arrays.asList(w1,w2);
        Matrix result_testM1 = new MatrixArrayList(2, 3, w3);
        assertEquals(result_testM1,testM1.matrixMulti(ematrix));

        Collection l5  = Arrays.asList(1.0,1.0);
        Collection l6  = Arrays.asList(1.0,1.0);
        Collection l7  = Arrays.asList(1.0,1.0);
        Collection l8  = Arrays.asList(l5,l6,l7);
        Matrix testMatrix2 = new MatrixArrayList(3,2,l8);
        Collection k1  = Arrays.asList(6.0,6.0);
        Collection k2  = Arrays.asList(15.0,15.0);
        Collection k  = Arrays.asList(k1,k2);
        Matrix ergebnisMatrix  = new MatrixArrayList(2, 2, k);
        assertEquals(ematrix.matrixMulti(ematrix),ematrix);
        assertEquals(ergebnisMatrix,testMatrix1.matrixMulti(testMatrix2));

        Matrix ergebnisMatrix1  = new MatrixArrayList(2, 2, k);
        ergebnisMatrix1.setValue(0, 0, 1.0);
        ergebnisMatrix1.setValue(0, 1, 1.0);
        ergebnisMatrix1.setValue(1, 0, 1.0);
        ergebnisMatrix1.setValue(1, 1, 1.0);
        Matrix result = ergebnisMatrix1.matrixMulti(ergebnisMatrix1);
        assertEquals(result,ergebnisMatrix1.add(ergebnisMatrix1));
    }

    /**
     * Ensprechen Zeilen-und Spaltenanzahl nicht der Definition der Matrizenmultiplikation,
     * ist es nicht möglich diese zu multiplizieren und es wird ein Error zurückgegeben.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionMatrixMulti(){
        Collection l5  = Arrays.asList(1.0,1.0,1.0);
        Collection l6  = Arrays.asList(1.0,1.0,1.0);
        Collection l8  = Arrays.asList(l5,l6);
        Matrix testMatrix2 = new MatrixArrayList(3,3,l8);
        testMatrix1.matrixMulti(testMatrix2);
    }

    /**
     * Die Potenz entspricht einer Matrixmultiplikation einer Matrix mit sich selbst.
     * Daher muss diese auf jeden Fall quadratisch sein. Wir lassen keine negativen Potenzen zu
     * und überprüfen zugelassene Eingabewerte anhand einiger Beispiele.
     */
    @Test
    public void testPotenz() {
        Collection k1  = Arrays.asList(0.3,0.3,0.3);
        Collection k2  = Arrays.asList(0.7,0.7,0.7);
        Collection k3  = Arrays.asList(1.1,1.1,1.1);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix matrix1  = new MatrixArrayList(3, 3, k);

        Collection l1  = Arrays.asList(0.3,0.3,0.3);
        Collection l2  = Arrays.asList(0.7,0.7,0.7);
        Collection l3  = Arrays.asList(1.1,1.1,1.1);
        Collection l  = Arrays.asList(l1,l2,l3);
        Matrix matrix2  = new MatrixArrayList(3, 3, l);

        assertEquals(matrix1, matrix1.potenz(1));
        assertEquals((matrix1.matrixMulti(matrix1)).matrixMulti(matrix1).getValue(0, 0), matrix1.potenz(3).getValue(0, 0), DELTA);

        assertEquals(matrix2.potenz(0), ematrix);
    }

    /**
     * Liefert eine Einheitsmatrix mit der gewünschten Dimension zurück. Diese muss an
     * den Stellen wo ai = aj ist den Wert 1.0 besitzen und ansonsten mit Nullen aufgefüllt sein.
     * Dies überprüfen wir beispielhaft.
     */
    @Test
    public void testEinheitsmatrix() {
        Collection k1  = Arrays.asList(1.0,0.0,0.0);
        Collection k2  = Arrays.asList(0.0,1.0,0.0);
        Collection k3  = Arrays.asList(0.0,0.0,1.0);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix einheitsMatrix  = new MatrixArrayList(3, 3, k);
        assertEquals(einheitsMatrix, testMatrix1.einheitsmatrix(3));

    }

    /**
     * Es wird überprüft, ob die Matrixmultiplikation bei allen 3 Implementationen identische Werte produziert
     * Hyothese: Da wir die Methoden der Matrizen unabhängig von der Implementation in der abstrakten Klasse
     * MatrixImpl definiert haben, ist fast ausgeschlossen, dass unterschiedliche Ergebnisse produziert werden.
     * Passiert dies dennoch, so müssten wir die getValue- und setValue-Methoden der jeweiligen Implementation
     * überprüfen. Denn auf diesen basieren unsere Implementationen der Matrix-Methoden.
     * Ergebnis: Wir bekommen tatsächlich identische Ergebnisse geliefert. Damit haben wir eindeutig die getValue- und
     * setValue-Methoden korrekt implementiert.
     */
    @Test
    public void testTask6(){
    Matrix testMatrix1a = new MatrixArrayList(2, 3, (Collection)Arrays.asList(Arrays.asList(1.0,2.0,3.0),Arrays.asList(4.0,5.0,6.0)));
    Matrix matrixA3 = testMatrix1a.initAndReturn(3, 1);
    Matrix matrixB3 = testMatrix1a.initAndReturn(3, 1);
    Matrix eMatrix = testMatrix1a.einheitsmatrix(matrixA3.getZeilen());
    Matrix matrixA = eMatrix.matrixMulti(matrixA3);
    Matrix matrixB = eMatrix.matrixMulti(matrixB3);

    Matrix e = matrixA.matrixMulti(matrixB);

    Matrix testMatrix2 = new MatrixArray(2,3,(Collection)Arrays.asList(Arrays.asList(1.0,2.0,3.0),Arrays.asList(4.0,5.0,6.0)));
    Matrix ematrix1 = testMatrix2.einheitsmatrix(matrixA.getZeilen());
    Matrix matrixB1 = ematrix1.matrixMulti(matrixB);
    Matrix matrixA1 = ematrix1.matrixMulti(matrixA);
    Matrix e1 = matrixA1.matrixMulti(matrixB1);

    Matrix testMatrix3 = new MatrixList(2,3,(Collection)Arrays.asList(Arrays.asList(1.0,2.0,3.0),Arrays.asList(4.0,5.0,6.0)));
    Matrix ematrix2 = testMatrix3.einheitsmatrix(matrixA.getZeilen());
    Matrix matrixB2 = ematrix2.matrixMulti(matrixB);
    Matrix matrixA2 = ematrix2.matrixMulti(matrixA);
    Matrix e2 = matrixA2.matrixMulti(matrixB2);


    assertTrue(e.equals(e1) && e1.equals(e2));


    }

    /**
     * Vergleich des Platzbedarfs der Implementationen II und III
     * Hypothese: Je Nachdem wie man die Aufgabenstellung auslegt, erwarten wird entweder die gleiche Anzahl an Elementen in den Listen,
     * sofern jedes Tupel bzw. Tripel als ein Element zählt. Ansonsten wird die zweite Implementation der Matrix im Vergleich zur dritten Implementation
     * einen um ein drittel geringeren Platzbedarf besitzen. Die ersten vier Ziffern unserer tatsächlichen Matrikelnmummern zu nehmen, war uns leider unmöglich,
     * weil der Rechenaufwand dadurch zu hoch war. Außerdem konnten wir das t nicht groß genug wählen, um ganze 5 Stellen stabil zu halten, sondern nur 4 Stellen.
     * Alles andere hätte den zeitlichen Rahmen und die Stromrechnung gesprengt :)
     * Ergebnis: Wie unsere Hypthese es prognostiziert hat.
     */
//    @Test
//    public void testTask7a() {
//        Collection k1  = Arrays.asList(1.0,0.0,0.0);
//        Collection k2  = Arrays.asList(0.0,1.0,0.0);
//        Collection k3  = Arrays.asList(0.0,0.0,1.0);
//        Collection k  = Arrays.asList(k1,k2,k3);
////        Matrix matrix = new MatrixList(3,3,k);
//        Matrix matrix1 = new MatrixArrayList(3,3,k);
//        Matrix matrix2 = new MatrixList(3,3,k);
//        Messung1 messung = new Messung1();
//        Messung1 messung1 = new Messung1();
//
//
//        for(int i = 0; i<200; i++){
//           Matrix e = matrix1.initAndReturn(1028, 10);
//           Matrix f = matrix2.initAndReturn(1028, 10);
//           messung.add(e.size());
//           messung.add(f.size());
//        }
//        System.out.println("Mittelwert der gespeicheten Tupel(MatrixArrayList): " + (int)messung.mittelwert());
//        System.out.println("Mittelwert der gespeicherten Werte in den Tupeln(MatrixArrayList): " + (int)messung.mittelwert() * ELEMENTS_IN_TUPEL);
//        System.out.println("Mittelwert der gespeicherten Tripel(MatrixList)" + (int)messung.mittelwert());
//        System.out.println("Mittelwert der gespeicherten Werte in den Tripeln(MatrixList)" + (int)messung.mittelwert() * ELEMENTS_IN_TRIPEL);
//    }
    /**
     * Vergleich des Zeitbedarfs der Implementationen II und III
     * Hypothese: Wir würden erwarten, dass der Zeitbedarf bei der zweiten Implementation geringer ist als bei der dritten.
     * Schließlich wird wenn ein neuer Wert hinzugefügt wird bei der dritten Implementation über die komplette Liste iteriert. um
     * zu überprüfen, ob es diesen Wert bereits gibt. Bei der zeiten Implementation immer nur in einer Liste, welche zu der jeweils
     * angegebenen Zeile passt.
     * Erneut konnten wir das t nicht groß genug wählen, um ganze 5 Stellen stabil zu halten, sondern nur 4 Stellen.
     * Alles andere hätte den zeitlichen Rahmen und die Stromrechnung gesprengt :)
     */
    @Test
    public void testTask8a(){
        Collection k1  = Arrays.asList(1.0,0.0,0.0);
        Collection k2  = Arrays.asList(0.0,1.0,0.0);
        Collection k3  = Arrays.asList(0.0,0.0,1.0);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix matrix1 = new MatrixArrayList(3,3,k);
        Matrix matrix2 = new MatrixList(3,3,k);
        Matrix matrix3 = new MatrixArray(3,3,k);
        Messung1 messung = new Messung1();
        Messung1 messung1 = new Messung1();
        Messung1 messung2 = new Messung1();

        for(int i=0;i<100;i++){
            System.out.println("FOTZE");
            Matrix matrixArrayList = matrix1.initAndReturn(30,1);
            Matrix matrixList = matrix2.initAndReturn(30,1);
            Matrix matrixArray = matrix3.initAndReturn(30,1);
            Matrix matrixArrayList2 = matrix1.initAndReturn(30,1);
            Matrix matrixList2 = matrix2.initAndReturn(30,1);
            Matrix matrixArray2 = matrix3.initAndReturn(30,1);
            matrixArrayList.matrixMulti(matrixArray2);
            matrixList.matrixMulti(matrixList2);
            matrixArray.matrixMulti(matrixArray2);
            messung.add(matrixArrayList.getZaehler());
            messung1.add(matrixList.getZaehler());
            messung2.add(matrixArray.getZaehler());
        }
        System.out.println("Mittelwert des Zeitbedarfs(MatrixArrayList): "+(int)messung.mittelwert());
        System.out.println("Varianz des Zeitbedarfs(MatrixArrayList): "+(int)messung.varianz());
        System.out.println("Mittelwert des Zeitbedarfs(MatrixList): "+(int)messung1.mittelwert());
        System.out.println("Varianz des Zeitbedarfs(MatrixList): "+(int)messung1.varianz());
        System.out.println("Mittelwert des Zeitbedarfs(MatrixArray): "+(int)messung2.mittelwert());
        System.out.println("Varianz des Zeitbedarfs(MatrixArray): "+(int)messung2.varianz());

//        Matrix matrixArray = matrix1.initAndReturn(1028,10);

//        System.out.println("Zaehler zur Erzeugung(MatrixArrayList): "+matrixArray.getZaehler());
//        System.out.println("Zaehler zur Erzeugung(MatrixList): "+matrixList.getZaehler());
}


     /**
     *Es wird überprüft, ob die rekursive Potenz zur normalen Potenz identische Ergebnisse produziert.
     */
//    @Test
//    public void testPotenzRek(){
//       Collection k1  = Arrays.asList(0.3,0.3,0.3);
//        Collection k2  = Arrays.asList(0.7,0.7,0.7);
//        Collection k3  = Arrays.asList(1.1,1.1,1.1);
//        Collection k  = Arrays.asList(k1,k2,k3);
//        Matrix matrix1  = new MatrixArrayList(3, 3, k);
//
//        Collection l1  = Arrays.asList(0.3,0.3,0.3);
//        Collection l2  = Arrays.asList(0.7,0.7,0.7);
//        Collection l3  = Arrays.asList(1.1,1.1,1.1);
//        Collection l  = Arrays.asList(l1,l2,l3);
//        Matrix matrix2  = new MatrixArrayList(3, 3, l);
//
//        assertEquals(matrix1, matrix1.potenzRek(1));
//        assertEquals((matrix1.matrixMulti(matrix1)).matrixMulti(matrix1).getValue(0, 0), matrix1.potenzRek(3).getValue(0, 0), DELTA);
//
//        assertEquals(matrix2.potenzRek(0), ematrix);
//    }

    /**
     *Wir erzeugen zufallsgesteurt quadratische Matrizen mit den Ausmaßen 100 x 100 und vergleichen die Laufzeit zwischen
     * der iterativen Potenz und der rekursiven Potenz
     * Hypothese: Die rekursive Potenz müsste deutlich schneller sein, weil Zwischenergebnisse gespeichert werden
     * und nicht jedes Mal neu berechnet werden.
     * Ergebnis: Die rekursive Potenz rechnet mit allen drei Implementationen deutlich schneller.
     */
//    @Test
//    public void testTask4Paper3(){
//        Collection k1  = Arrays.asList(0.3,0.3,0.3);
//        Collection k2  = Arrays.asList(0.7,0.7,0.7);
//        Collection k3  = Arrays.asList(1.1,1.1,1.1);
//        Collection k  = Arrays.asList(k1,k2,k3);
//        Matrix matrix1  = new MatrixArrayList(3, 3, k);
//        Random random = new Random();
//
//        Messung1 messung = new Messung1();
//        for(int j=0; j<10;j++){
//            Matrix matrix = matrix1.initAndReturn(50,1);
//        for(int i=1; i<20; i++){
//            Matrix e = matrix.potenz(i);
//            messung.add(e.getZaehler());
//        }
//        }
//         System.out.println("Mittelwert der Laufzeit zu t=10 Experimenten: "+ (int)messung.mittelwert());
//
//    }

}