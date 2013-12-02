/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

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
public class MatrixArrayTest {

        Collection l1  = Arrays.asList(0.1,0.2);
        Collection l2  = Arrays.asList(0.3,0.4);
        Collection l3  = Arrays.asList(0.5,0.6);
        Collection l  = Arrays.asList(l1,l2,l3);
        Matrix testMatrix1 = new MatrixArray(3, 2, l);
        //Initialisierung einer Einheitsmatrix
        Matrix ematrix  = testMatrix1.einheitsmatrix(3);
        double DELTA = 0.001;
     /**
     * Test zur Initialisierung der neuen Matrizen, mit der neuen Datenstruktur. Wir erlauben eine Initialisierung
      * mit einer beliebigen Collection, welche ebenfalls eine beliebige Collection enthält. Dies testen wir
      * beispielhaft mit der Arrays.asList()-Funktion. Die Werte müssen automatisch in der richtigen Liste und an der
      * richtigen Stelle landen. All dies überprüfen wir innerhalb dieses Testfalls.
     */
    @Test
    public void testInit() {
        double testMatrix[][] = new double[3][2];
        testMatrix[0][0] = 0.1;
        testMatrix[0][1] = 0.2;
        testMatrix[1][0] = 0.3;
        testMatrix[1][1] = 0.4;
        testMatrix[2][0] = 0.5;
        testMatrix[2][1] = 0.6;

        assertEquals(testMatrix1.getValue(0, 0), testMatrix[0][0],DELTA);
        assertEquals(testMatrix1.getValue(0, 1), testMatrix[0][1],DELTA);
        assertEquals(testMatrix1.getValue(1, 0), testMatrix[1][0],DELTA);
        assertEquals(testMatrix1.getValue(1, 1), testMatrix[1][1],DELTA);
        assertEquals(testMatrix1.getValue(2, 0), testMatrix[2][0],DELTA);
        assertEquals(testMatrix1.getValue(2, 1), testMatrix[2][1],DELTA);
    }

    /**
     * Die Add-Funktion muss sowohl mit positiven als auch mit negativen Werten umgehen können und problemlos addieren, solange die Matrizen
     * identische Ausmaße besitzen.
     */
    @Test
    public void testAdd() {
        Collection<Double> l1 = Arrays.asList(1.0,1.0,1.0,1.0);
        Collection<Double> l2 = Arrays.asList(1.0,1.0,1.0,1.0);
        List<Collection<Double>> l = Arrays.asList(l1,l2);
        Matrix m1 = new MatrixArray(2, 4, l);
        Matrix m2 = new MatrixArray(2, 4, l);
        Collection<Double> l3 = Arrays.asList(2.0,2.0,2.0,2.0);
        Collection<Double> l4 = Arrays.asList(2.0,2.0,2.0,2.0);
        List<Collection<Double>> r = Arrays.asList(l3,l4);
        Matrix testMatrix = new MatrixArray(2, 4, r);
        m1.add(m2);
        //Test auf korrekten Additionsvorgang
        assertEquals(m1,testMatrix);
    }
    /**
     * Test von ArgumentException für Additionsmethode add(), mittels ungleicher
     * Matrizengroesse.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testExceptionAdd(){
         Collection<Double> l1 = Arrays.asList(1.0,1.0,1.0,1.0);
        Collection<Double> l2 = Arrays.asList(1.0,1.0,1.0,1.0);
        List<Collection<Double>> l = Arrays.asList(l1,l2);
        List<Collection<Double>> lx = Arrays.asList(l1,l2,l2);
        Matrix m1 = new MatrixArray(2, 4, l);
        Matrix m2 = new MatrixArray(3, 4, lx);
        m1.add(m2);
    }

    /**
     * Wird eine Matrix mit einem Skalar multipliziert, so erhöht sich jeder Wert um den Faktor des angegebenen Skalares.
     */
    @Test
    public void testSkalarMulti() {
        Collection l1  = Arrays.asList(0.2,0.4);
        Collection l2  = Arrays.asList(0.6,0.8);
        Collection l3  = Arrays.asList(1.0,1.2);
        Collection l  = Arrays.asList(l1,l2,l3);
        Matrix testMatrix2 = new MatrixArray(3, 2, l);
        assertEquals(testMatrix1.skalarMulti(2),testMatrix2);
        Collection k1  = Arrays.asList(-0.2,-0.4);
        Collection k2  = Arrays.asList(-0.6,-0.8);
        Collection k3  = Arrays.asList(-1.0,-1.2);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix testMatrix3 = new MatrixArray(3, 2, k);
        assertEquals(testMatrix3,testMatrix2.skalarMulti(-1));
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
        Matrix testM1 = new MatrixArray(2, 3, w8);
        Collection w1  = Arrays.asList(1.0,1.0,1.0);
        Collection w2  = Arrays.asList(1.0,1.0,1.0);
        Collection w3  = Arrays.asList(w1,w2);
        Matrix result_testM1 = new MatrixArray(2, 3, w3);
        assertEquals(result_testM1,testM1.matrixMulti(ematrix));

        Collection l5  = Arrays.asList(1.0,1.0,1.0);
        Collection l6  = Arrays.asList(1.0,1.0,1.0);
        Collection l8  = Arrays.asList(l5,l6);
        Matrix testMatrix2 = new MatrixArray(2,3,l8);
        Collection k1  = Arrays.asList(0.3,0.3,0.3);
        Collection k2  = Arrays.asList(0.7,0.7,0.7);
        Collection k3  = Arrays.asList(1.1,1.1,1.1);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix ergebnisMatrix  = new MatrixArray(3, 3, k);
        assertEquals(ematrix.matrixMulti(ematrix),ematrix);
        assertEquals(ergebnisMatrix,testMatrix1.matrixMulti(testMatrix2));

        Matrix ergebnisMatrix1  = new MatrixArray(3, 3, k);
        ergebnisMatrix1.setValue(0, 0, 1.0);
        ergebnisMatrix1.setValue(0, 1, 1.0);
        ergebnisMatrix1.setValue(0, 2, 1.0);
        ergebnisMatrix1.setValue(1, 0, 1.0);
        ergebnisMatrix1.setValue(1, 1, 1.0);
        ergebnisMatrix1.setValue(1, 2, 1.0);
        ergebnisMatrix1.setValue(2, 0, 1.0);
        ergebnisMatrix1.setValue(2, 1, 1.0);
        ergebnisMatrix1.setValue(2, 2, 1.0);
        Matrix result = ergebnisMatrix1.matrixMulti(ergebnisMatrix1);
        assertEquals(result.getValue(0, 0), 3.0,DELTA);
        assertEquals(result.matrixMulti(ergebnisMatrix1).getValue(0, 0),9.0,DELTA);
    }

   /**
     * Ensprechen Zeilen-und Spaltenanzahl nicht der Definition der Matrizenmultiplikation,
     * ist es nicht möglich diese zu multiplizieren und es wird ein Error zurückgegeben.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testExceptionMatrixMulti(){
        Collection l5  = Arrays.asList(1.0,1.0,1.0);
        Collection l6  = Arrays.asList(1.0,1.0,1.0);
        Collection l8  = Arrays.asList(l5,l6,l6);
        Matrix testMatrix2 = new MatrixArray(3,3,l8);
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
        Matrix matrix1  = new MatrixArray(3, 3, k);

        Collection l1  = Arrays.asList(0.3,0.3,0.3);
        Collection l2  = Arrays.asList(0.7,0.7,0.7);
        Collection l3  = Arrays.asList(1.1,1.1,1.1);
        Collection l  = Arrays.asList(l1,l2,l3);
        Matrix matrix2  = new MatrixArray(3, 3, l);

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
    public void testEinheitsmatrix(){
        Collection k1  = Arrays.asList(0.3,0.3,0.3);
        Collection k2  = Arrays.asList(0.7,0.7,0.7);
        Collection k3  = Arrays.asList(1.1,1.1,1.1);
        Collection k  = Arrays.asList(k1,k2,k3);
        Matrix matrix2  = new MatrixArray(3, 3, k);
        assertEquals(ematrix,matrix2.potenz(0));
    }


}