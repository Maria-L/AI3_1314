package A2;


/**
 * @author Steffen Giersch, Birger Kamp, Maria Janna Martina L�demann
 *
 */
public interface QuadMatrix {
	
	//Getter & Setter
	/** Gibt das Element am eingegebenen Index zur�ck
	 * @param m Zeilenindex beginnend mit 1
	 * @param n Spaltenindex beginnend mit 1
	 * @return Element an diesem Index
	 */
	public double get(int m, int n);
	/** Setzt das Element am eingegebenen Index auf den Wert x (double)
	 * @param m Zeilenindex beginnend mit 1
	 * @param n Spaltenindex beginnend mit 1
	 * @param x Element an diesem Index
	 */
	public void set(int m, int n, double x);
	/** Gibt die Gr��e (int) der Matrix zur�ck
	 * @return Gr��e der Matrix (int)
	 */
	public int getSize();
	/** Gibt eine neue Instanz der QuadMatrix Implementation mit der Gr��e n (int) zur�ck
	 * @param n Gr��e der zu erzeugenden Matrix
	 * @return Neue Instanz einer QuadMatrix Implementation
	 */
	public QuadMatrix init(int n);
	
	//Operationen
	/** Gleichheitsfunktion f�r QuadMatrix
	 * @param mtx Zu vergleichendes Objekt
	 * @return Sind die Matritzen gleich (true) oder nicht (false)
	 */
	public boolean equals(Object mtx);
	/** Matrixaddition �ber zwei QuadMatrix Implementationen
	 * @param mtx Zu this zu addierende QuadMatrix
	 * @return Ergebnis der Matrixaddition (QuadMatrix)
	 */
	public QuadMatrix add(QuadMatrix mtx);
	/** Skalarmultiplikation f�r QuadMatrix
	 * @param n Skalar mit dem multipliziert werden soll
	 * @return Ergebnis der Skalarmultiplikation
	 */
	public QuadMatrix mul(double n);
	/** Matrixmultiplikation in der Form this*mtx
	 * @param mtx Mit this zu multiplizierende Matrix
	 * @return Ergebnis der Matrixmultiplikation
	 */
	public QuadMatrix mul(QuadMatrix mtx);
	/** Potenzierung von this zum Exponenten n (int)
	 * @param n Potenz (int)
	 * @return Ergebnis der Matrixmultiplikation
	 */
	public QuadMatrix pow(int n);
	/** Gibt den ben�tigten Platz dieser Implementation wieder
	 * @return Ben�tigter Platz (int)
	 */
	public int space();
	/** Gibt die ben�tigte Anzahl der Dereferenzierungen vom letzten Reset bis jetzt wieder
	 * @return Anzahl der Dereferenzierungen
	 */
	public int time();
	/** Resetet die Anzahl der Dereferenzierungen
	 */
	public void timeReset();
	public void setGen(int m, int n, double x);
}
