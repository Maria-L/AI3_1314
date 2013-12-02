/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_2_1;

import java.util.Collection;

/**
 * Praktikumsaufgabe 2.1.1
 * @author Sawatzki, Glake
 */
public interface Matrix{
    /**
     * Addiert zwei Matrizen zu einer zusammen, wenn ihre Ausmaße übereinstimmte.
     * Beid Matrizen genauso viele Zeilen, wie Spalten haben.
     * @param matrix = Zu addierende Matrix
     * @return Gibt die addierte Matrix zurück.
     */
    public Matrix add(Matrix matrix);
    /**
     * Multipliziert eine Matrix mit einem Skalar Lambda. Dabei wird jedes Element,
     * der Matrix mit dem Skalar multipliziert.
     * @param lambda = Skalar, welches mit allen Elementen verrechnet wird.
     * @return
     */
    public Matrix skalarMulti(double lambda);
    /**
     * Multipliziert zwei Matrizen zu einer zusammen. Faktor 1 Matrix, mit n x m
     * und Faktor 2 Matrix, mit m x l Ausmaß. Jede Zeile, der Faktor 1 Matrix wird
     * mit jeder Spalte, der Faktor 2 Matrix multipliziert. Die errechneten Werte
     * werden anschließen addiert und als neues Element in die Ergebnismatrix
     * eingetragen.
     * @param matrix = Zu multiplizierende Matrix, mit m x l Ausmaß
     * @return = Egebnismatrix, mit n x l Ausmaß
     */
    public Matrix matrixMulti(Matrix matrix);
    /**
     * Rekursive Version zum Potenzieren von Matrizen. Dazu bedient es sich diese Funktion
     * der binären Exponentation.
     * @param n = anzugebende Potenz
     * @return  = Gibt eine, um n potenzierte Matrix zurück. Wenn n = 0 wird die Einheitsmatrix zurueckgegeben.
     */
    public Matrix potenzRek(int n);
    /**
     * Potenziert eine Matrix um eine natürliche Zahl. Diese Multiplikation
     * wird über die Matrix Mulitplikation matrixMulti gelöst. Handelt es sich,
     * um eine Nullpotenz, dann wird die Einheitsmatrix zurückgegeben.
     * @param potenz = Potenz, um wie viele die Matrix potenziert werden soll.
     * @return = Wenn Potenz=0 gewesen ist, dann wird die Einheitsmatrix zurückgegeben,
     * ansonsten die Ergebnismatrix von matrixMulti.
     */
    public Matrix potenz(int potenz);
    /**
     * Gibt die Einheitsmatrix zurück, desse Groesse ueber die natürlichen Zahlen
     * zeilen und spalten verändert werden kann.
     * Begründung: Matrix mit einer Potenz von Null ergibt die Einheitsmatrix.
     * @param zeilenSpalten = Eine natürliche Zahl für die Groesse des Matrixausmaßes.
     * @return = Gibt die Einheitsmatrix zurück, die zeilen x spakten groß ist.
     */
    public Matrix einheitsmatrix(int zeilenSpalten);
    /**
     * Gibt den spezifischen Wert zurück, der an der Stelle (zeile,spalte) steht. Liegt zeile
     * oder spalte nicht innerhalb der Matrix, dann wird eine Exception geworfen.
     * @param zeile = Zeilenangabe
     * @param spalte = Spaltenangabe
     * @return = Gibt die Reelle Zahl der an der Stellt (zeile,spalte) zurück.
     */
    public double getValue(int zeile, int spalte);
    /**
     * Schreibt einen spezifischen Wert value an die Stelle (zeile,spalte).
     * @param zeile = Zeilenangabe
     * @param spalte = Spaltenangabe
     * @param value = Zu schreibender Wert
     */
    public void setValue(int zeile, int spalte, double value);
    /**
     * Gibt die maximale Anzahl an Zeilen zurück, die die Matrix hat.
     * @return = maximale Anzahl von Zeilen
     */
    public int getZeilen();
    /**
     * Gibt die maximale Anzahl an Spalten zurück, die die Matrix hat.
     * @return = maximale Anzahl von Spalten
     */
    public int getSpalten();
    /**
     * Nicht statische Initialisierung einer m x n Matrix, ohne Übergabe einer Collection.
     * @param zeilen = Angabe der Zeilenanzahl
     * @param spalten = Angabe der Spaltenanzahl
     * @return = Gibt eine neue Matrix zurueck.
     */
    public Matrix init(int zeilen, int spalten);
    /**
     * Getter- Methode zur Rueckgabe des Dereferenzierungs Zaehlers.
     * @return = Gibt den Dereferenzierungs Zaehler als Integer zurueck.
     */
    public int getZaehler();
    /**
     * Nicht statische Initialisierung einer n x n Matrix, ohne Übergabe einer Collection.
     * @param n = Angabe der n Groesse
     * @return = Gibt eine leere Matrix der Groesse n x n
     */
    public Matrix init(int n);
    /**
     * Gibt eine neue Matrix zurueck, die die Elemente aus der Collection enthält.
     * @param zeilen = Anzugebende Zeilengroesse der Matrix
     * @param spalten = Anzugebende Spaltengroesse der Matrix
     * @param coll = Collection, die die Werte enthalten, die in der Matrix  eingetragen werden sollen.
     * @return = Gibt eine den Paramtern entsprechende Matrix zurueck.
     */
    public Matrix init(int zeilen, int spalten, Collection<Collection<Double>> coll);
    /**
     * Gibt eine Matrix zurueck, die die Ausmaße n besitz, mit einer gewissen Anzahl von ELementen,
     * die ungleich Null sind.
     * @param n = Anzugebende Groesse der Matrix
     * @param wahrscheinlichkeit = Anzugebende Wahrscheinlichkeit, zwischen 0 und 100
     * @return = Gibt ein Matrix zurueck, die zufaellige Elemente besitzt, die nach der Wahrscheinlichkeit ausgesucht wurden.
     */
    public Matrix initAndReturn(int n, int wahrscheinlichkeit);
    /**
     * Eine ähnliche Funktioen, die initAndReturn(int n, int warhrscheinlichkeit) nur ohne Angabe einer Groesse.
     * @param wahrscheinlichkeit = Anzugebende Wahrscheinlichekti, zur Steuerung des Inhalts.
     * @return = Gibt eine zufaellig generierte Matrix zurueck.
     */
    public Matrix initAndReturn(int wahrscheinlichkeit);
    /**
     * Erzeugt eine zufaellige n x n Matrix, dessen Elemente nach einer Wahrscheinlichkeit aussgesucht wird
     * und wo anschließend, nach einem Auswahlfaktor ein zufaelliges Element ausgesucht wird.
     * @param wahrscheinlichkeit = Anzugebende Wahrscheinlichkeit
     * @param auswahlfaktor = Faktor, nach dem ein zufaelliges Element ausgesucht wird.
     * @return = Gibt ein zufaelliges Element, aus einer zufaellig generierten Matrix zurueck.
     */
    public double initAndReturnElem(int wahrscheinlichkeit, int auswahlfaktor);
    /**
     * Standard toString Methode, um die Matrix in einen String zu konvertieren.
     * Dazu wird ein leerer String angelegt, der mit jeder Zeile gefüllt wird.
     * Begründung: Nützlich um seine Matrix auszugeben.
     * @return = Ein String der Matrix
     */
    @Override
    public String toString();
    /**
     * Typspezifische equals Methode, um zwei Objekte zu prüfen, ob diese vom Wert gleich
     * sind. Prüfung mit Any. Gibt TRUE zurück, wenn beide Operanden gleich, vom Inhalt her gleich sind,
     * ansonsten FALSE.
     * @param obj = Ein zu prüfendes Objekt.
     * @return = boolean Wert
     */
    @Override
    public boolean equals(Object obj);
    /**
     * Methode zur Erfassung der Gesamtanzahl aller Elemente einer Matrix.
     * Begründung: Hilfreich bei der Ermittlung der Elementanazahl. Vor allem,
     * wenn die Datenstruktur das Speicher von Werten=0 verhindert.
     * @return = Gibt die Anzahl der gespeicherten Elemente einer Matrix zurück.
     */
    public int size();
}
