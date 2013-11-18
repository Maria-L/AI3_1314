/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Uebungsaufgabe_5_2;

import java.lang.String;

/**
 *
 * @author abk640
 */
public class Rekurrenzgleichung {

    int[] array = new int[30];

    public Rekurrenzgleichung() {
    }

    /**
     * Implementation der gegebenen Funktion aus Aufgabenblatt_5, Uebungsaufgabe 5.2
     * @param n = Zu waehlendes n
     * @return = Gibt das Ergebnis zur Funktion als long Wert zurueck.
     */
    public long rekurrenz(int n) {
        this.array[n] += 1;
        if (n < 3) {
            return 1;
        } else {
            return rekurrenz(n - 1) + 2 * rekurrenz(n - 2) + 3 * rekurrenz(n - 3);
        }
    }

    /**
     * Implementation der gegeben Funktion aus Aufgabenblatt_5, Uebungsaufgabe 5.2, mittels
     * eines rekursiven Aufrufs innerhalb der Methode.
     * @param n = Zu waehlendes n
     * @return = Gibt das Ergebnis zur Funktion als long Wert zurueck.
     */
    public long rekurrenz2(int n) {
        if (n < 3) {
            return 1;
        } else {
            return rekurrenz2helper(n, 1, 1, 1);
        }
    }

    /**
     * Implementation der gegeben Funktion aus Aufgabenblatt_5, Uebungsaufgabe 5.2, mittels
     * eines iterativen Ansatzes.
     * @param n = Zu waehlendes n
     * @return = Gibt das Ergebnis zur Funktion als long Wert zurueck.
     */
    public long rekurrenz1(int n) {
        long result = 0;
        long f1 = 1;
        long f2 = 1;
        long f3 = 1;
        if (n < 3) {
            return 1;
        }
        while (n >= 3) {
            result = (f1) + 2 * (f2) + 3 * (f3);
            f3 = f2;
            f2 = f1;
            f1 = result;
            n = n - 1;
        }
        return result;
    }

    /**
     * Hilfsmethode, zur Berechnung der Methode rekurrenz2().
     * @param n
     * @param f1
     * @param f2
     * @param f3
     * @return
     */
    public long rekurrenz2helper(int n, int f1, int f2, int f3) {
        if (n < 3) {
            return f1;
        } else {
            return rekurrenz2helper(n - 1, (f1 + 2 * f2 + 3 * f3), f1, f2);
        }
    }

    /**
     * Standardmaeßige toString Methode, um das Array in einen String zu konvertieren.
     * @return = Gibt das gespeicherte Array als String zurueck.
     */
    public String toString() {
        String s = "[";
        for (int i = 0; i < array.length; i++) {
            s += i + "=>" + array[i] + "\n";
        }
        s += "]";
        return s;
    }
}
