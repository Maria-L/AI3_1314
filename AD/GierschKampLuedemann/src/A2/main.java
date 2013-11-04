package A2;

import java.util.*;
import A1.*;


public class main {

	/** @param args */
	public static void main(String[] args) {

		int t = 100;
		int n = 10;
		int k = 40;
		double p = 0.1;
		QuadMatrix matrix = new QuadMatrixArrayImpl(n);

		//List<QuadMatrix> list = new ArrayList<QuadMatrix>();

		for (int j = 0; j < 100; j++) {
			//Messung msg1 = new Messung();
			Messung msg2 = new Messung();
			for (int i = 0; i < t; i++) {
				 matrix = QuadMatrixGenerator.random(new QuadMatrixListImpl(1), n, p, 1000);
				 //msg1.add(matrix.space());
				 matrix.timeReset();
				 matrix.pow(j);
				 msg2.add(matrix.time());
			}
			System.out.println((int) Math.rint(msg2.average()));
			//System.out.println("Space: " + Math.rint(msg1.average()));
		}
		//System.out.println(QuadMatrixGenerator.random(new QuadMatrixListImpl(1), n, p, 1000).space());
		
		//"Dereference for k = " + j + ": " +
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
