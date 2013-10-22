package A1;

import processing.core.*;

public class ProcessingMain extends PApplet {
	
	public static final float CIRCLESIZE 	= 5;			//Gr��e der angezeigten Kreise
	public static final float FPS			= (float) 0.5;	//Bilder pro Sekunde
	public static final int	ANZAHLMESSUNGEN	= 100;			//Anzahl der durchgef�hrten Messungen pro Bild
	public static final int LISTENLAENGE	= 200;			//L�nge der erstellten Listene
	public static final int RIGHTSHIFT		= 66;			//Rechtsshift f�r das Koordinatensystem in Pixeln
	private Messung allAverages				= new Messung();//Akkumulator f�r die Durchschnittswerte aller Messungen

	public static void main(String args[]) {
		PApplet.main(new String[] { "--present", "A1.ProcessingMain" });
	}

	public void setup() {
		size(800*2, 500*2);
		background(0);
		frameRate(FPS);
	}

	public void draw() {
		background(0);								// Bild l�schen

		Messung mssg = new Messung();				//Neue Messung erstellen

		for (int j = 0; j < ANZAHLMESSUNGEN; j++) {	//Anzahl an Messungen

			Liste list = new ListeImpl();

			for (int i = 0; i < LISTENLAENGE; i++) {//L�nge der Listen
				list.insert(i, (int) (Math.rint(Math.random() * (i - 1))));
			}
			
			mssg.add(list.getStepCounter());		//Anzahl der Schritte dur aktuellen Messung hinzuf�gen
		}
		
		allAverages.add(mssg.average());			//Durchschnittswert dieser Messung speichern
		
		
		text("Average: " + mssg.average(), 10, 20);	//Durchschnitt, Varianz und Gesammtdurchschnitt ausgeben
		text("Variance: " + mssg.varianz(), 10, 40);
		text("Overall Average: " + allAverages.average(), 10, 65);
		text("Overall Variance: " + allAverages.varianz(), 10, 85);
		text(Math.rint(mssg.average()) + "->", 10, height / 2);	//Mittelwert der Statistik anzeigen
		text(Math.rint(mssg.average() * 1.1) + "->", 10, (float) ((height / 2) * 1.1));
		text(Math.rint(mssg.average() * 0.9) + "->", 10, (float) ((height / 2) * 0.9));
		
		for (int i = 0; i < ANZAHLMESSUNGEN; i++) {
			ellipse( (float) (((width-RIGHTSHIFT) / ANZAHLMESSUNGEN) * i + RIGHTSHIFT),		//x-position = (Weite / Menge) * Index
					((float) ((height / (mssg.average()*2)) * mssg.getMessungen().get(i))),	//y-position = (H�he / (Durchschnitt * 2)) * Wert
					CIRCLESIZE, CIRCLESIZE);
		}
		
		
	}
}
