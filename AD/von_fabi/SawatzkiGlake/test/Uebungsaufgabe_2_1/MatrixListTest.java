/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

import java.lang.instrument.Instrumentation;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author abk640
 */
public class MatrixListTest {

    Matrix testMatrix1 = new MatrixList(2, 3, (Collection)Arrays.asList(Arrays.asList(1.0,2.0,3.0),Arrays.asList(4.0,5.0,6.0)));
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
    public void testAdd(){
         Matrix nullMatrix = new MatrixList(2, 3, (Collection)Arrays.asList(Arrays.asList(0.0,0.0,0.0),Arrays.asList(0.0,0.0,0.0)));
        assertEquals(testMatrix1,testMatrix1.add(nullMatrix));
    }
    /**
     * Wird eine Matrix mit einem Skalar multipliziert, so erhöht sich jeder Wert um den Faktor des angegebenen Skalares.
     */
    @Test
    public void testSkalarMul(){
        Matrix ergebnisMatrix = new MatrixList(2, 3, (Collection)Arrays.asList(Arrays.asList(2.0,4.0,6.0),Arrays.asList(8.0,10.0,12.0)));
        assertEquals(ergebnisMatrix, testMatrix1.skalarMulti(2));
        Matrix ergebnisMatrix1 = new MatrixList(2, 3, (Collection)Arrays.asList(Arrays.asList(-2.0,-4.0,-6.0),Arrays.asList(-8.0,-10.0,-12.0)));
        assertEquals(ergebnisMatrix1, testMatrix1.skalarMulti(-2));
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
        Matrix testM1 = new MatrixList(2, 3, w8);
        Collection w1  = Arrays.asList(1.0,1.0,1.0);
        Collection w2  = Arrays.asList(1.0,1.0,1.0);
        Collection w3  = Arrays.asList(w1,w2);
        Matrix result_testM1 = new MatrixList(2, 3, w3);
        assertEquals(    result_testM1,   testM1.matrixMulti(ematrix)    );

        Collection l5  = Arrays.asList(1.0,1.0);
        Collection l6  = Arrays.asList(1.0,1.0);
        Collection l7  = Arrays.asList(1.0,1.0);
        Collection l8  = Arrays.asList(l5,l6,l7);
        Matrix testMatrix2 = new MatrixList(3,2,l8);
        Collection k1  = Arrays.asList(6.0,6.0);
        Collection k2  = Arrays.asList(15.0,15.0);
        Collection k  = Arrays.asList(k1,k2);
        Matrix ergebnisMatrix  = new MatrixList(2, 2, k);
        assertEquals(ematrix.matrixMulti(ematrix),ematrix);
        assertEquals(ergebnisMatrix,testMatrix1.matrixMulti(testMatrix2));

        Matrix ergebnisMatrix1  = new MatrixList(2, 2, k);
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
        Matrix testMatrix2 = new MatrixList(3,3,l8);
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
        Matrix matrix1  = new MatrixList(3, 3, k);

        Collection l1  = Arrays.asList(0.3,0.3,0.3);
        Collection l2  = Arrays.asList(0.7,0.7,0.7);
        Collection l3  = Arrays.asList(1.1,1.1,1.1);
        Collection l  = Arrays.asList(l1,l2,l3);
        Matrix matrix2  = new MatrixList(3, 3, l);

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
        Matrix einheitsmatrix = new MatrixList(3,3,k);
        assertEquals(ematrix, testMatrix1.einheitsmatrix(3));

        Collection l1  = Arrays.asList(0.0,0.0,0.0);
        Collection l2  = Arrays.asList(0.0,0.0,0.0);
        Collection l3  = Arrays.asList(0.0,0.0,0.0);
        Collection l  = Arrays.asList(k1,k2,k3);
        Matrix nullMatrix  = new MatrixList(3, 3, l);
    }

}