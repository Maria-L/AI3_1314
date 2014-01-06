package graphAlgorithms;

import graph.*;
import java.util.*;


public class Methods {
	public static Counter counter = new Counter(0);
	public static Counter counterMatrix = new Counter(0);
	
	public static List<Integer> hamiltonDichtesteEcke(Graph graph) {
		counter.reset();
		counter.increment();												//Schritt 1:
		int vi = graph.getVertexes().get(0);								//Man w�hle eine beliebige Ecke vi aus graph
		List<Integer> akku = new ArrayList<Integer>(Arrays.asList(vi,vi));	//Und setze den bisher gefundenen Weg auf [vi, vi]
		
		System.out.println("Kantenfolge nach der Initialisierung: " + akku);
																			//Schritt 2:
		while(akku.size() < graph.getVertexes().size() + 1) {				//Solange nicht alle Ecken aufgenommen sind
			counter.increment();
			int dichtesteEcke = dichtesteEcke(graph, akku);					//	Man w�hle die dichteste Ecke zum bisher gefundenen Weg akku
			int dist = Integer.MAX_VALUE;
			List<Integer> tempKantenfolge = new ArrayList<Integer>();
			
			
			System.out.println("Neue dichteste Ecke:  " + dichtesteEcke);
			
			for(int i = 1; i < akku.size(); i++) {							//	Und setze diese in die ideale Stelle der Kantenfolge ein
				List<Integer> temp = new ArrayList<Integer>(akku);
				temp.add(i, dichtesteEcke);
				int tempDist = laengeVon(graph, temp);
				
				if(tempDist < dist) {
					tempKantenfolge = temp;
					dist = tempDist;
				}
			}
			
			akku = tempKantenfolge;
			System.out.println("Aktuelle Kantenfolge: " + akku + " der L�nge " + dist);
		}
		
		return akku;
	}
	
	/*
	 * Eine Methode die die L�nge des Weges zur�ck gibt die ihr gegeben wird
	 */
	public static int laengeVon(Graph graph, List<Integer> kantenfolge) {
		int akku = 0;
		
		for(int i = 1; i < kantenfolge.size(); i++) {
			if(graph.getAdjacent(kantenfolge.get(i-1)).contains(kantenfolge.get(i))) {	//Wenn die beiden Knoten miteinander verbunden sind
				for(int id : graph.getIncident(kantenfolge.get(i-1))) {
					if(graph.getSource(id) == kantenfolge.get(i)) {
						akku += graph.getValE(id, "distanz");
						break;
					} else if (graph.getTarget(id) == kantenfolge.get(i)){
						akku += graph.getValE(id, "distanz");
						break;
					}
				}
			} else {																	//Wenn nicht
				return Integer.MAX_VALUE;												//	Gebe unendlich zur�ck
			}
		}
		return akku;
	}
	
	public static int dichtesteEcke(Graph graph, List<Integer> kantenfolge) {
		int akku = -1;
		int dist = Integer.MAX_VALUE;
		
		for(int vertex : kantenfolge) {						//F�r jede Ecke aus der Kantenfolge
			counter.increment(2);
			for(int id : graph.getIncident(vertex)) {		//	F�r jede Incidente Kante dieser Ecken
				if(graph.getValE(id, "distanz") < dist)	{	//		Wenn die bisherige Distanz unterboten werden kann
					counter.increment();
					if(graph.getSource(id) == vertex) {		//			Target ist der zu erreichende Knoten
						counter.increment();
						if(!kantenfolge.contains(graph.getTarget(id))) {	//Wenn Target nicht in kantenfolge ist
							dist = graph.getValE(id, "distanz");			//	Speichere die neuen Werte
							akku = graph.getTarget(id);
							counter.increment(2);
						}
					} else {								//			Source ist der zu erreichende Knoten
						counter.increment();
						if(!kantenfolge.contains(graph.getSource(id))) {	//Wenn Source nicht in kantenfolge ist
							dist = graph.getValE(id, "distanz");			//	Speichere die neuen Werte
							akku = graph.getSource(id);
							counter.increment(2);
						}
					}
				}
			}
		}
		
		return akku;
	}
	
	/*  
	 * 0. Markiere jede Kante mit benutzt = 0 und erstelle eine initiale leere Kantenfolge
	 * 1a. W�hle einen beliebigen Knoten vi aus dem Graphen G mit einem Grad > 0, Gehe zu 2
	 * 1b. W�hle einen beliebigen Knoten aus der bisherigen Kantenfolge mit unbenutzt-Grad > 0. Wenn keiner gefunden wurde, kann keine Eulertour gefunden werden (nicht zusammenh�ngend)
	 * 2.  Finde einen Kreis und verwende daf�r den start und endpunkt vi. Wenn kein Kreis gefunden wurde, kann keine Eulertour gefunden werden.
	 * 3.  F�ge den Kreis in die bestehende Kantenfolge ein und markiere jede benutzte Kante mit benutzt = 1
	 * 4.  Wenn jede Kante mit benutzt = 1 markiert wurde, ist eine Eulertour gefunden, wenn nicht gehe zu 1b
	 */
	
	public static List<Integer> hierholzer(Graph graph){
		counter.reset();
		List<Integer> efolge= new ArrayList<Integer>();
		boolean unbenutzt;
		counter.increment();
		for(int id : graph.getEdges()){
			counter.increment();
			graph.setValE(id, "benutzt", 0);
		}
		counter.increment();
		int vi = graph.getVertexes().get(0);
		
		while(true) {
			if(vi == -1) {
				for(int id : efolge) {
					if(unbenutztGrad(graph, id) > 0) {
						vi = id;
						break;
					}
				}
			}
			
			List<Integer> unterliste = hierholzerKreis(graph,vi);
			
			System.out.println("Unterkreis �ber die Ecken: " + unterliste);
			
			if(unterliste.size() == 0) {
				throw new IllegalArgumentException("Kein Eulerkreis gefunden");
			}
			
			efolge = mergeList(efolge,unterliste);
			
			for(int i = 0; i < efolge.size() - 1; i++){
				counter.increment();
				for(int id : graph.getIncident(efolge.get(i))) {
					counter.increment(2);
					if(graph.getSource(id) == efolge.get(i + 1) || graph.getTarget(id) == efolge.get(i + 1)) {
						counter.increment();
						graph.setValE(id, "benutzt", 1);
						break;
					}
				}
			}
			
			unbenutzt = false;
			counter.increment();
			for(int id: graph.getEdges()){
				counter.increment();
				unbenutzt |= graph.getValE(id, "benutzt") == 0;
				if(unbenutzt) {break;}
			}
			if(!unbenutzt) {
				System.out.println("Gefundener Kreis �ber die Kantenfolge: " + efolge);
				return efolge;
			}
			vi = -1;
		}
	}
	
	public static int unbenutztGrad(Graph graph, int id){
		int unbenutztGrad = 0;
		counter.increment();
		for(int ed : graph.getIncident(id)){
			counter.increment();
			if(graph.getValE(ed, "benutzt") == 0){
				unbenutztGrad ++;
			}
		}
		return unbenutztGrad;
	}
	
	public static List<Integer> hierholzerKreis(Graph graph, int vi){
		List<Integer> kreis = new ArrayList<Integer>();
		int currentvi = vi;
		int nextvi = -1;
		kreis.add(vi);
		
		do {
			counter.increment();
			for(int ed : graph.getIncident(currentvi)){
				counter.increment();
				if(graph.getValE(ed, "benutzt") == 0){
					counter.increment();
					if(graph.getTarget(ed) == currentvi) {
						counter.increment();
						nextvi = graph.getSource(ed);
					} else {
						counter.increment();
						nextvi = graph.getTarget(ed);
					}
					counter.increment();
					graph.setValE(ed, "benutzt", 1);
					break;
				}
			}
			kreis.add(nextvi);
			currentvi = nextvi;
		} while (currentvi != vi);
		
		return kreis;
	}
	
	public static List<Integer> mergeList(List<Integer> oberliste, List<Integer> unterliste){
		List<Integer> mergeList = new ArrayList<Integer>();
		mergeList.addAll(oberliste);
		int index = 0;
		
		if(oberliste.size() == 0){return unterliste;}
		
		for(int i : oberliste){
			if(i == unterliste.get(0)){
				mergeList.remove(index);
				mergeList.addAll(index, unterliste);
				return mergeList;
			}
			index ++;
		}
		if(mergeList == oberliste){
			throw new IllegalArgumentException("Nicht zusammen passende Kreise. Eingabe illegal");
		}
		return mergeList;
	}

	/**Berechnet einen optimalen Fluss in einem schwach zusammenh�ngenden schlichten
	 * Digraphen. In diesem Digraphen m�ssen f�r jede Kante eine Kapazit�t "kapazitaet" 
	 * gegeben sein. Hierbei wird die Grundimplementation des Ford Fulkerson benutzt,
	 * die allerdings um eine FiFo-Warteschlange f�r markierte Knoten erweitert wurde.
	 * @param graph Zu bearbeitender Graph
	 * @param source Quelle des Graphen
	 * @param target Senke des Graphen
	 */
	public static int edmondsKarp(Graph graph, int source, int target) {
		//1 Initialisierung
		//Die Kapazit�t ist als Attribut "kapazitaet" gegeben
		//In der folgenden Schleife wird der Fluss auf 0 gesetzt
		counter.increment();
		for(int id : graph.getEdges()) {
			counter.increment();
			graph.setValE(id, "fluss", 0);
		}
		
		//Markiere jede Ecke als nicht inspiziert (0) und nicht markiert (0)
		counter.increment();
		for(int id : graph.getVertexes()) {
			counter.increment(2);
			graph.setValV(id, "inspiziert", 0);
			graph.setValV(id, "markiert", 0);
		}
		
		
		
		//F�ge die Quelle als erste zur Warteschlange hinzu
		//warteschlange.add(source);
		counter.increment(2);
		graph.setValV(source, "markiert", 1);
		graph.setValV(source, "maxFlow", Integer.MAX_VALUE);
		
		//2 Inspektion und Markierung
		
		while(true){
			boolean allInspected = false;
			
			List<Integer> warteschlange = new ArrayList<Integer>();

			while(true) {
				//Falls target markiert ist gehe zu 3.
				counter.increment();
				if(graph.getValV(target, "markiert") != 0) {
					break;
				}
			
				//b W�hle die n�chste Kante aus unserer Warteschleife. Diese ist markiert und
				//noch nicht inspiziert.
				int vi = -1;
				counter.increment();
				for(int id : graph.getVertexes()) {
					counter.increment(2);
					if(graph.getValV(id, "markiert") != 0 && graph.getValV(id, "inspiziert") == 0 && !warteschlange.contains(id)) {	
						warteschlange.add(id);
					}
				}
				
				//Wenn kein weiterer markierter, aber noch nicht inspizierter Knoten gefunden wurde, brich ab
				if(warteschlange.size() == 0) {allInspected = true; break;}
				
				//Hole das n�chste Element aus der Warteschlange und inspiziere es wie folgt
				System.out.print("Aktuelle Warteschlange: ");
				String print = "";
				for(int i = 0; i < warteschlange.size(); i++) {
					print = print + " [" + graph.getStrV(warteschlange.get(i),"name") + "] ";
				}
				System.out.println(print);
				
				vi = warteschlange.get(0);
				warteschlange.remove(0);
				counter.increment();
				graph.setValV(vi, "inspiziert", 1);
				
				
				System.out.println("Gew�hlte Ecke: " + graph.getStrV(vi, "name"));
				
				//Vorw�rtskante: F�r jede Kante e die inzident von vi ist mit unmarkierter Ecke vj
				//und fluss(e) < kapazit�t(e) markiere vj mit 
				//-> vorgaenger(vi), pos(1), neg(0) und maxFlow(min(kapazit�t(e)-fluss(e), maxFlow(vi)))
				counter.increment();
				for(int id : graph.getIncident(vi)) {
					//Wenn das Ziel der Kante nicht markiert ist und deren Kapazit�t gr��er ist als der Fluss
					counter.increment(5);
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					if(graph.getValV(t, "markiert") == 0 && graph.getValE(id, "kapazitaet") > graph.getValE(id, "fluss")) {
						counter.increment(8);
						graph.setValV(t, "vorgaenger", s);
						graph.setValV(t, "pos", 1);
						graph.setValV(t, "neg", 0);
						graph.setValV(t, "maxFlow", Math.min(graph.getValE(id, "kapazitaet") - graph.getValE(id, "fluss"), graph.getValV(s, "maxFlow")));
						graph.setValV(t, "markiert", 1);
						System.out.println("Neue Vorw�rtskante zu: " + graph.getStrV(t,"name"));
					}
				}
				
				//R�ckw�rtskante: F�r jede Kante e mit unmarkierter Ecke vj und Ziel vi
				//und fluss(e) > 0 markiere vj mit
				//-> vorgaenger(vi), pos(0), neg(1) und maxFlow(min(fluss(e), maxFlow(vi)))
				for(int id : graph.getEdges()) {
					counter.increment(5);
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					if(t == vi && graph.getValV(s, "markiert") == 0 && graph.getValE(id, "fluss") > 0) {
						counter.increment(8);
						graph.setValV(s, "vorgaenger", t);
						graph.setValV(s, "pos", 0);
						graph.setValV(s, "neg", 1);
						graph.setValV(s, "maxFlow", Math.min(graph.getValE(id, "fluss"), graph.getValV(t, "maxFlow")));
						graph.setValV(s, "markiert", 1);
						System.out.println("Neue R�ckw�rtskante zu: " + graph.getStrV(graph.getSource(id),"name"));
					}
				}
			}
			
			//2a Wenn alle markierten Ecken inspiziert wurden gehe nach 4
			if(allInspected) {break;}
			
			//3. Bei target beginnend l�sst sich anhand der Markierungen der gefundene vergr��ernde Weg bis 
			//zu source r�ckw�rts durchlaufen. F�r jede Vorw�rtskante wird fluss(e) um maxFlow(target) erh�ht und f�r jede 
			//R�ckw�rtskante wird fluss(e) um maxFlow(target) vermindert. 
			int id = target;
			counter.increment();
			int moreFlow = graph.getValV(target, "maxFlow");
			System.out.println("Folgender Fluss wird addiert: " + moreFlow);
			System.out.println("Der aktuelle Fluss in jeder Kante betr�gt: ");

			while(id != source) {
				counter.increment();
				if(graph.getValV(id, "pos") != 0){			//Wenn wir eine Vorw�rtskante haben
					counter.increment(2);
					for(int eid : graph.getIncident(graph.getValV(id, "vorgaenger"))) {
						counter.increment();
						if(graph.getTarget(eid) == id) {
							counter.increment(2);
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") + moreFlow);
						}
					}
				} else {
					counter.increment();
					for(int eid : graph.getIncident(id)) {
						counter.increment();
						if(graph.getTarget(eid) == id) {
							counter.increment(2);
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") - moreFlow);
						}
					}
				}
				//Setze die neue ID auf den Vorg�nger
				counter.increment();
				id = graph.getValV(id, "vorgaenger");
			}
			
			for(int ide : graph.getEdges()) {
				System.out.println(graph.getStrV(graph.getSource(ide), "name") + " -> " + graph.getStrV(graph.getTarget(ide), "name") + " = " + graph.getValE(ide,"fluss") + "/" + graph.getValE(ide,"kapazitaet"));
			}
			
			System.out.println("###############################################################");
			
			
			//Anschlie�end werden bei allen Ecke mit Ausnahme von q die Markierungen entfernt. Gehe zu 2.
			counter.increment();
			for(int vid : graph.getVertexes()) {
				if(vid != source) {
					counter.increment(6);
					graph.setValV(vid, "vorgaenger", 0);
					graph.setValV(vid, "pos", 0);
					graph.setValV(vid, "neg", 0);
					graph.setValV(vid, "maxFlow",0);
					graph.setValV(vid, "markiert", 0);
					graph.setValV(vid, "inspiziert", 0);
				}
			}
			counter.increment();
			graph.setValV(source, "inspiziert", 0);
		}
		//4 Es gibt keinen vergr��ernden Weg. Der jetzige Flusswert jeder Kante ist optimal
		int ergebnis = 0;
		counter.increment();
		for(int id : graph.getIncident(source)) {
			counter.increment();
			ergebnis += graph.getValE(id, "fluss");
		}
		return ergebnis;
	}
	
	
	
	/**Berechnet einen optimalen Fluss in einem schwach zusammenh�ngenden schlichten
	 * Digraphen. In diesem Digraphen m�ssen f�r jede Kante eine Kapazit�t "kapazitaet" 
	 * gegeben sein.
	 * @param graph Zu bearbeitender Graph
	 * @param source Quelle des Graphen
	 * @param target Senke des Graphen
	 */
	public static int fordFulkerson(Graph graph, int source, int target) {
		//1 Initialisierung
		//Die Kapazit�t ist als Attribut "kapazitaet" gegeben
		//In der folgenden Schleife wird der Fluss auf 0 gesetzt
		counter.increment();
		for(int id : graph.getEdges()) {
			counter.increment();
			graph.setValE(id, "fluss", 0);
		}
		
		Random generator = new Random();
		
		//Markiere jede Ecke als nicht inspiziert (0) und nicht markiert (0)
		counter.increment();
		for(int id : graph.getVertexes()) {
			counter.increment(2);
			graph.setValV(id, "inspiziert", 0);
			graph.setValV(id, "markiert", 0);
		}
		
		//Markiere die Quelle
		counter.increment(2);
		graph.setValV(source, "markiert", 1);
		graph.setValV(source, "maxFlow", Integer.MAX_VALUE);
		
		//2 Inspektion und Markierung
		
		while(true){
			boolean allInspected = false;

			while(true) {
				//Falls target markiert ist gehe zu 3.
				counter.increment();
				if(graph.getValV(target, "markiert") != 0) {
					break;
				}
			
				//b W�hle eine zuf�llige markierte, aber noch nicht inspizierte
				//Ecke vi und inspiziere sie wie folgt
				List<Integer> markierte = new ArrayList<Integer>();
				int vi = -1;
				counter.increment();
				for(int id : graph.getVertexes()) {
					counter.increment(2);
					if(graph.getValV(id, "markiert") != 0 && graph.getValV(id, "inspiziert") == 0) {	
						markierte.add(id);
					}
				}
				
				if(markierte.size() == 0) {allInspected = true; break;}
				vi = markierte.get(generator.nextInt(markierte.size()));
				graph.setValV(vi, "inspiziert", 1);
				counter.increment();
				
				System.out.println("Gew�hlte Ecke: " + graph.getStrV(vi, "name"));
				
				//Vorw�rtskante: F�r jede Kante e die inzident von vi ist mit unmarkierter Ecke vj
				//und fluss(e) < kapazit�t(e) markiere vj mit 
				//-> vorgaenger(vi), pos(1), neg(0) und maxFlow(min(kapazit�t(e)-fluss(e), maxFlow(vi)))
				counter.increment();
				for(int id : graph.getIncident(vi)) {
					//Wenn das Ziel der Kante nicht markiert ist und deren Kapazit�t gr��er ist als der Fluss
					counter.increment(2);
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					counter.increment(3);
					if(graph.getValV(t, "markiert") == 0 && graph.getValE(id, "kapazitaet") > graph.getValE(id, "fluss")) {
						counter.increment(8);
						graph.setValV(t, "vorgaenger", s);
						graph.setValV(t, "pos", 1);
						graph.setValV(t, "neg", 0);
						graph.setValV(t, "maxFlow", Math.min(graph.getValE(id, "kapazitaet") - graph.getValE(id, "fluss"), graph.getValV(s, "maxFlow")));
						graph.setValV(t, "markiert", 1);
						System.out.println("Neue Vorw�rtskante zu: " + graph.getStrV(t,"name"));
					}
				}
				
				//R�ckw�rtskante: F�r jede Kante e mit unmarkierter Ecke vj und Ziel vi
				//und fluss(e) > 0 markiere vj mit
				//-> vorgaenger(vi), pos(0), neg(1) und maxFlow(min(fluss(e), maxFlow(vi)))
				counter.increment();
				for(int id : graph.getEdges()) {
					counter.increment(2);
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					counter.increment(2);
					if(t == vi && graph.getValV(s, "markiert") == 0 && graph.getValE(id, "fluss") > 0) {
						counter.increment(8);
						graph.setValV(s, "vorgaenger", t);
						graph.setValV(s, "pos", 0);
						graph.setValV(s, "neg", 1);
						graph.setValV(s, "maxFlow", Math.min(graph.getValE(id, "fluss"), graph.getValV(t, "maxFlow")));
						graph.setValV(s, "markiert", 1);
						System.out.println("Neue R�ckw�rtskante zu: " + graph.getStrV(graph.getSource(id),"name"));
					}
				}
			}
			
			//2a Wenn alle markierten Ecken inspiziert wurden gehe nach 4
			if(allInspected) {break;}
			
			//3. Bei target beginnend l�sst sich anhand der Markierungen der gefundene vergr��ernde Weg bis 
			//zu source r�ckw�rts durchlaufen. F�r jede Vorw�rtskante wird fluss(e) um maxFlow(target) erh�ht und f�r jede 
			//R�ckw�rtskante wird fluss(e) um maxFlow(target) vermindert. 
			counter.increment();
			int id = target;
			int moreFlow = graph.getValV(target, "maxFlow");
			System.out.println("Folgender Fluss wird addiert: " + moreFlow);
			while(id != source) {
				counter.increment();
				if(graph.getValV(id, "pos") != 0){			//Wenn wir eine Vorw�rtskante haben
					counter.increment(2);
					for(int eid : graph.getIncident(graph.getValV(id, "vorgaenger"))) {
						counter.increment();
						if(graph.getTarget(eid) == id) {
							counter.increment(2);
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") + moreFlow);
						}
					}
				} else {
					counter.increment();
					for(int eid : graph.getIncident(id)) {
						counter.increment();
						if(graph.getTarget(eid) == id) {
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") - moreFlow);
						}
					}
				}
				//Setze die neue ID auf den Vorg�nger
				counter.increment();
				id = graph.getValV(id, "vorgaenger");
			}
			
			//Anschlie�end werden bei allen Ecke mit Ausnahme von q die Markierungen entfernt. Gehe zu 2.
			counter.increment();
			for(int vid : graph.getVertexes()) {
				if(vid != source) {
					counter.increment(6);
					graph.setValV(vid, "vorgaenger", 0);
					graph.setValV(vid, "pos", 0);
					graph.setValV(vid, "neg", 0);
					graph.setValV(vid, "maxFlow",0);
					graph.setValV(vid, "markiert", 0);
					graph.setValV(vid, "inspiziert", 0);
				}
			}
			counter.increment();
			graph.setValV(source, "inspiziert", 0);
		}
		//4 Es gibt keinen vergr��ernden Weg. Der jetzige Flusswert jeder Kante ist optimal
		int ergebnis = 0;
		counter.increment();
		for(int id : graph.getIncident(source)) {
			counter.increment();
			ergebnis += graph.getValE(id, "fluss");
		}
		return ergebnis;
	}
	
	
	/** Berechnet den k�rzesten Weg zu jedem Knoten im Graphen von v (Int) aus.
	 * Hierbei werden die ermittelten Werte im Graphen in jedem Knoten als "distanz" und "vorgaenger" gespeichert.
	 * Wenn "distanz" Integer.MAX_VALUE entspricht, dann gibt es keinen Weg zu diesem Knoten.
	 * Wenn "vorgaenger" Id.errorID entspricht, hat dieser Knoten keinen Vorg�nger
	 * @param graph Zu bearbeitender Graph
	 * @param v Knoten von dem aus die k�rzesten Wege gefunden werden sollen
	 */
	public static void bellmanFord(Graph graph, int v) {
		counter.increment();
		for (int id : graph.getVertexes()) {
			graph.setValV(id, "distanz", Integer.MAX_VALUE);
			graph.setValV(id, "vorgaenger", -1);
			counter.increment(2);
		}
		counter.increment();
		graph.setValV(v, "distanz", 0);

		counter.increment();
		if (graph.directed()) {		//Wenn der Graph gerichtet ist, dann k�nnen Kanten zur�ck ignoriert werden
			counter.increment();
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {	//Wiederhole n-1 mal
				counter.increment();
				for (int id : graph.getEdges()) {																																													//f�r jedes (u,v) aus E
					counter.increment(7);
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorg�nger(v) := u
						counter.increment(8);
					}
				}
			}

			counter.increment();
			for (int id : graph.getEdges()) {																												// f�r jedes (u,v) aus E
				counter.increment(7);
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enth�lt negative Kreise");																// dann	Fehlerwert f�r Kreise mit negativen Gewichten
				}
			}
		} else {					//Wenn der Graph ungerichtet ist, dann muss jede Abfrage auch in entgegengesetzte Richtung gef�hrt werden
			counter.increment();
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																																								//Wiederhole n-1 mal
				counter.increment();
				for (int id : graph.getEdges()) {																																													//f�r jedes (u,v) aus E
					counter.increment(7);
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorg�nger(v) := u
					}
					counter.increment(7);
					if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {	// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getSource(id), "distanz",graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht"));																					// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getSource(id), "vorgaenger",graph.getTarget(id));																              															//		Vorg�nger(v) := u
					}
				}
			}

			counter.increment();
			for (int id : graph.getEdges()) {																												// f�r jedes (u,v) aus E
				counter.increment(7);
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enth�lt negative Kreise");																// dann	Fehlerwert f�r Kreise mit negativen Gewichten
				}
				counter.increment(7);
				if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enth�lt negative Kreise");																// dann	Fehlerwert f�r Kreise mit negativen Gewichten
				}
			}
		}
	}
	
	/** Wendet den Floyd-Warschall-Algorithmus auf einen Graphen an.
	 * Zur�ckgegeben wird ein Hash, der zum einen die Transitmatrix und zum anderen die Distanzmatrix enth�lt.
	 * Diese k�nnen mit dem Key "trans" bzw "dist" geholt werden und sind vom Datentyp List<List<Integer>>
	 * @param graph
	 * @return
	 */
	public static HashMap<String,List<List<Integer>>> floydWarshall(Graph graph) {
		HashMap<String,List<List<Integer>>> akku = new HashMap<>();	//HashMap zum Ausgeben der Ergebnisse
		List<List<Integer>> trans = new ArrayList<List<Integer>>();	//Transitmatrix
		List<List<Integer>> dist = new ArrayList<List<Integer>>();	//Distanzmatrix
		List<Integer> vertexes = graph.getVertexes();				//Statische Liste der Vertexes
		int size = graph.getVertexes().size();
		counter.increment(2);
		
		for(int i = 0; i < size;i++) {
			counterMatrix.increment(2);
			trans.add(new ArrayList<Integer>());
			dist.add(new ArrayList<Integer>());
			
			for(int j = 0; j < size;j++) {
				counterMatrix.increment(2);
				trans.get(i).add(-1);								//transitmatrix mit Fehlerwert bef�llen
				if(i == j) {
					dist.get(i).add(0);								//Weg zu sich selber 0 setzen
				} else {
					dist.get(i).add(Integer.MAX_VALUE);				//Wege zu anderen auf Integer.MaxValue setzen
				}
			}
		}
		
		counter.increment();
		for(int i : graph.getEdges()) {								//Alle Kanten in der Distanzmatrix Eintragen
			int temp = graph.getValE(i, "gewicht");
			int source = graph.getSource(i);	//ID von Source suchen
			int target = graph.getTarget(i);	//ID von Target suchen
			counter.increment(3);
			
			source = vertexes.indexOf(source);	//Index in der Liste suchen
			target = vertexes.indexOf(target);
			
			counter.increment();
			if(graph.directed()) {
				counterMatrix.increment();
				dist.get(source).set(target, temp);
			} else {
				counterMatrix.increment(2);
				dist.get(source).set(target, temp);
				dist.get(target).set(source, temp);
			}
		}
		
		for(int j = 0; j < size;j++) {																		//f�r jeden Knoten j
			for(int i = 0; i < size;i++) {																	//    f�r jeden Knoten i != j
				if(i != j) {														
					for(int k = 0; k < size;k++) {															//        f�r jeden Knoten k != j
						if(k != j) {
							counterMatrix.increment(3);
							int dik = dist.get(i).get(k);
							int dij = dist.get(i).get(j);
							int djk = dist.get(j).get(k);
							
							if(dij != Integer.MAX_VALUE && djk != Integer.MAX_VALUE) {	
								if(dik > dij + djk) {														//            Wenn D(i,k) > D(i,j) + D(jk)
									counterMatrix.increment(2);
									dist.get(i).set(k, dij + djk);											//                D(i,k) = D(i,j) + D(jk)
									trans.get(i).set(k, j);													//                T(i,k) = j
								}
							}
							counterMatrix.increment();
							if(dist.get(i).get(i) < 0) {													//            Wenn D(i,i) < 0
								throw new IllegalArgumentException("Es wurden negative Kreise gefunden");	//                Werfe Fehler
							}
						}
					}
				}
			}
		}
		
		for(int i = 0; i < size;i++) {											//�ber die gesammte Transitmatrix iterieren
			for(int j = 0; j < size;j++) {
				if(trans.get(i).get(j) != -1) {									//Wenn der Matrixeintrag nicht id.ErrorID entspricht
					trans.get(i).set(j, vertexes.get(trans.get(i).get(j)));		//    Ersetze die ListenID durch die KnotenID
					counterMatrix.increment(2);
				}
			}
		}
		
		akku.put("trans", trans);
		akku.put("dist", dist);
		return akku;
	}
}
