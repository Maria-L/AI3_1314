package A2;


public class QuadMatrixGenerator {
	/** Gibt eine Zufallsgenerierte QuadMatrix der Implementation von mtx wieder
	 * @param mtx Typ der Matrix, der wiedergegeben werden soll
	 * @param x Größe der Matrix
	 * @param n Wahrscheinlichkeit, dass ein Element nicht 0.0 ist
	 * @param m Reichweite von 0..m die ein Element haben kann
	 * @return Generierte Matrix
	 */
	public static QuadMatrix random(QuadMatrix mtx, int x, double n, double m) {
		QuadMatrix akku = mtx.init(x);
		
		for(int i = 1; i <= akku.getSize(); i++) {
			for(int j = 1; j <= akku.getSize(); j++) {
				if(Math.random() < n) {
					akku.setGen(i, j, Math.random() * m);
				}
			}
		}
		
		return akku;
	}
}
