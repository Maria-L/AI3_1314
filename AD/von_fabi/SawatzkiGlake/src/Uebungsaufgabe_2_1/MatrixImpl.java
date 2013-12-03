/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import static com.google.common.base.Preconditions.*;

/**
 *
 * @author abk640
 */
abstract class MatrixImpl  implements Matrix{

    protected int zaehler;
 /**
     * Addiert zwei Matrizen zu einer zusammen, wenn ihre Ausmaße übereinstimmte.
     * Beid Matrizen genauso viele Zeilen, wie Spalten haben.
     * @param matrix = Zu addierende Matrix
     * @return Gibt die addierte Matrix zurück.
     */
    public Matrix add(Matrix matrix) {
        checkArgument(this.getSpalten() == matrix.getSpalten());
        checkArgument(this.getZeilen() == matrix.getZeilen());
        for (int i = 0; i < this.getZeilen(); i++) {
            for (int j = 0; j < this.getSpalten(); j++) {
                this.setValue(i, j, (this.getValue(i, j) + matrix.getValue(i, j)));
            }
        }
        return this;
    }

    public Matrix skalarMulti(double lambda) {
        int zeilen = this.getZeilen();
        int spalten = this.getSpalten();
        Matrix ergebnis = this.init(zeilen, spalten);
         for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                ergebnis.setValue(i, j, (this.getValue(i, j) * lambda));
            }
        }
        return ergebnis;
    }
   /**
     * Gibt die Einheitsmatrix zurück, desse Groesse ueber die natürlichen Zahlen
     * zeilen und spalten verändert werden kann.
     * @param zeilen = Eine natürliche Zahl für die Zeilenanzahl
     * @param spalten = Eine natürliche Zahl für die Spaltenanzahl
     * @return = Gibt die Einheitsmatrix zurück, die zeilen x spakten groß ist.
     */
 public Matrix einheitsmatrix(int zeilenSpalten) {
        checkArgument(zeilenSpalten >=0);
        Matrix einheit = this.init(zeilenSpalten, zeilenSpalten);
        for (int i = 0; i < zeilenSpalten; i++) {
            for (int j = 0; j < zeilenSpalten; j++) {
                if (i == j) {
                    einheit.setValue(i, j, 1.0);
                } else {
                    einheit.setValue(i, j, 0.0);
                }
            }
        }
        return einheit;
    }
     /**
     * Potenziert eine Matrix um eine natürliche Zahl. Diese Multiplikation
     * wird über die Matrix Mulitplikation matrixMulti gelöst. Handelt es sich,
     * um eine Nullpotenz, dann wird die Einheitsmatrix zurückgegeben.
     * @param potenz = Potenz, um wie viele die Matrix potenziert werden soll.
     * @return = Wenn Potenz=0 gewesen ist, dann wird die Einheitsmatrix zurückgegeben,
     * ansonsten die Ergebnismatrix von matrixMulti.
     */
    public Matrix potenz(int potenz) {
        checkArgument(potenz >= 0);
        checkArgument(this.getZeilen() == this.getSpalten());
        Matrix ergebnis = this;
        if (potenz != 0) {
            if (potenz == 1) {
                return this;
            } else {
                while (potenz > 1) {
                    ergebnis = this.matrixMulti(ergebnis);
                    potenz = potenz - 1;
                }
            }
        } else {
            return einheitsmatrix(this.getZeilen());
        }
        return ergebnis;
    }
    /**
     * Multipliziert zwei Matrizen zu einer zusammen. Faktor 1 Matrix, mit n x m
     * und Faktor 2 Matrix, mit m x l Ausmaß. Jede Zeile, der Faktor 1 Matrix wird
     * mit jeder Spalte, der Faktor 2 Matrix multipliziert. Die errechneten Werte
     * werden anschließend addiert und als neues Element in die Ergebnismatrix
     * eingetragen.
     * @param matrix = Zu multiplizierende Matrix, mit m x l Ausmaß
     * @return = Egebnismatrix, mit n x l Ausmaß
     */
    public Matrix matrixMulti(Matrix matrix) {
        checkArgument(this.getSpalten() == matrix.getZeilen());
        Matrix ergebnis = this.init(this.getZeilen(), matrix.getSpalten());
        for (int i = 0; i < this.getZeilen(); i++) {
            for (int j = 0; j < matrix.getSpalten(); j++) {
                ergebnis.setValue(i, j, 0.0);
                for (int k = 0; k < this.getSpalten(); k++) {
                    double value =(Math.round(100.0*(ergebnis.getValue(i, j) + this.getValue(i, k) * matrix.getValue(k, j)))/100.0);
                    ergebnis.setValue(i, j, value);
                }
            }
        }

        return ergebnis;
    }

     /**
      * Es wird eine zufällige n x n Matrix erzeugt, die mit Werten gefüllt wird,
      * die ebenfalls zufallsgesteuert ausgewählt werden. Anschließend wird nach dem Zufallsprinzip
      * ein Element, aus dieser Matrix ausgewählt. Die Wahrscheinlichkeit, ob
      * die Elemente ungleich Null sind, kann man über die Parameter wahrscheinlichkeit
      * beeinflussen. Ebenso kann man über die Paramter auswahlfaktor die Auswahl des Zufallselements
      * beeinflussen.
      * @param n =
      * @param wahrscheinlichkeit
      * @param auswahlfaktor
      * @return
      */
     public Matrix initAndReturn(int n, int wahrscheinlichkeit) {
        checkArgument(wahrscheinlichkeit >= 0 && wahrscheinlichkeit <= 100);
        Random random = new Random();
        List matrix = new LinkedList<List<Double>>();
        for (int i = 0; i < n; i++) {
            List zeile = new LinkedList<Double>();
            for (int j = 0; j < n; j++) {
                double rand = random.nextDouble();
                int rand2 = random.nextInt(100);
                if (rand2 < wahrscheinlichkeit) {
                        zeile.add(rand);
                } else {
                    rand = 0.0;
                    zeile.add(rand);
                }
            }
            matrix.add(zeile);
        }
        return init(n,n,matrix);
    }

    /**
     * Erstellt nach Zufallswürfen einen neue n x n Matrix, die mit verschiedenen
     * Werten, abhängig von der gewählten Wahrscheinlichkeit wahrscheinlichkeit
     * ungleich Null sein können. Anschließen wird nach einem weiteren Zufallswurf
     * in Verbindung mit einem Auswahlfaktor auswahl ein Wert aus der neu erstellten
     * Matrix gelesen und zurückgegeben.
     * @param wahrscheinlichkeit = Wahrscheinlichkeit von 0% bis 100%, dass Werte ungleich Null sind.
     * @param auswahlfaktor = Auswahlfaktor zum Beeinflussen der Auswahl eines zufälligen Wertes.
     * @return = Gibt den zufällig ausgewählten Wert zurück.
     */
    public Matrix initAndReturn(int wahrscheinlichkeit) {
        checkArgument(wahrscheinlichkeit >= 0 && wahrscheinlichkeit <= 100);
        Random random = new Random();
        int n = random.nextInt(10);
        if(n<0){
            n = n*(-1);
        }
        return this.initAndReturn(n,wahrscheinlichkeit);
    }

    public double initAndReturnElem(int wahrscheinlichkeit, int auswahlfaktor){
        Matrix matrix = this.initAndReturn(wahrscheinlichkeit);
        return matrix.getValue(matrix.getZeilen()%auswahlfaktor, matrix.getSpalten()%auswahlfaktor);
    }


      @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Matrix)) return false;
        Matrix matrix = (Matrix) obj;
        if(matrix.getSpalten()!= this.getSpalten() || matrix.getZeilen() != this.getZeilen()) return false;
        for(int i=0; i<matrix.getZeilen();i++){
            for(int j=0; j<matrix.getSpalten();j++){
                if(matrix.getValue(i, j) != this.getValue(i, j)) return false;
            }
        }
        return true;
    }

    public Matrix potenzRek(int n){
        checkArgument(n >= 0);
        if(n == 0){
            return this.einheitsmatrix(this.getZeilen());
        }
        if(n == 1) return this;
        else if(n % 2 == 0) return this.potenzRek(n / 2).matrixMulti(this);
        else return this.matrixMulti(this.potenzRek(n / 2).matrixMulti(this));
    }


}
