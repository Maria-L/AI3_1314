package A2;

import java.util.*;
import A1.*;


public class main {

	/** @param args */
	public static void main(String[] args) {

		int t = 2000;
		int n = 900;
		double p = 0.01;
		QuadMatrix matrix = new QuadMatrixArrayImpl(5);

		//List<QuadMatrix> list = new ArrayList<QuadMatrix>();

		for (int j = 0; j < 5; j++) {
			Messung msg1 = new Messung();
			Messung msg2 = new Messung();
			for (int i = 0; i < t; i++) {
				 matrix = QuadMatrixGenerator.random(new QuadMatrixArrayListImpl(1), n, p, 1000);
				 msg1.add(matrix.space());
				 msg2.add(matrix.time());
			}
			System.out.println("Dereference: " + Math.rint(msg2.average()));
			System.out.println("Space: " + Math.rint(msg1.average()));
		}
		//System.out.println(QuadMatrixGenerator.random(new QuadMatrixListImpl(1), n, p, 1000).space());
	}
	
	/*
	 * Aufgabe 7c)
	 * ArrayImpl)
	 * 	Space(p,n) = n^2
	 * ArrayListImpl)
	 * 	Space(p,n) = n*p*2
	 * ListImpl)
	 * 	Space(p,n) = n*p*3
	 * 
	 * Aufgabe 7d)
	 * 	
	 */
	

	/*
	 * Experiment:
	 * Hypothese (Laufzeiterwartung usw), 
	 * Experiment,
	 * Analyse des Ergebnisses
	 */
	

}
