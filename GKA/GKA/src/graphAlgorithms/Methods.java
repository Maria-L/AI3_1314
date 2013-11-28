package graphAlgorithms;

import graph.*;
import java.util.*;


public class Methods {
	public static Counter counter = new Counter(0);
	public static Counter counterMatrix = new Counter(0);

	/**Berechnet einen optimalen Fluss in einem schwach zusammenhängenden schlichten
	 * Digraphen. In diesem Digraphen müssen für jede Kante eine Kapazität "kapazitaet" 
	 * gegeben sein. Hierbei wird die Grundimplementation des Ford Fulkerson benutzt,
	 * die allerdings um eine FiFo-Warteschlange für markierte Knoten erweitert wurde.
	 * @param graph Zu bearbeitender Graph
	 * @param source Quelle des Graphen
	 * @param target Senke des Graphen
	 */
	public static int edmondsKarp(Graph graph, int source, int target) {
		//1 Initialisierung
		//Die Kapazität ist als Attribut "kapazitaet" gegeben
		//In der folgenden Schleife wird der Fluss auf 0 gesetzt
		for(int id : graph.getEdges()) {
			graph.setValE(id, "fluss", 0);
		}
		
		//Markiere jede Ecke als nicht inspiziert (0) und nicht markiert (0)
		for(int id : graph.getVertexes()) {
			graph.setValV(id, "inspiziert", 0);
			graph.setValV(id, "markiert", 0);
		}
		
		
		
		//Füge die Quelle als erste zur Warteschlange hinzu
		//warteschlange.add(source);
		graph.setValV(source, "markiert", 1);
		graph.setValV(source, "maxFlow", Integer.MAX_VALUE);
		
		//2 Inspektion und Markierung
		
		while(true){
			boolean allInspected = false;
			
			List<Integer> warteschlange = new ArrayList<Integer>();

			while(true) {
				//Falls target markiert ist gehe zu 3.
				if(graph.getValV(target, "markiert") != 0) {
					break;
				}
			
				//b Wähle die nächste Kante aus unserer Warteschleife. Diese ist markiert und
				//noch nicht inspiziert.
				int vi = -1;
				for(int id : graph.getVertexes()) {
					if(graph.getValV(id, "markiert") != 0 && graph.getValV(id, "inspiziert") == 0 && !warteschlange.contains(id)) {	
						warteschlange.add(id);
					}
				}
				
				//Wenn kein weiterer markierter, aber noch nicht inspizierter Knoten gefunden wurde, brich ab
				if(warteschlange.size() == 0) {allInspected = true; break;}
				
				//Hole das nächste Element aus der Warteschlange und inspiziere es wie folgt
				System.out.print("Aktuelle Warteschlange: ");
				String print = "";
				for(int i = 0; i < warteschlange.size(); i++) {
					print = print + " [" + graph.getStrV(warteschlange.get(i),"name") + "] ";
				}
				System.out.println(print);
				
				vi = warteschlange.get(0);
				warteschlange.remove(0);
				graph.setValV(vi, "inspiziert", 1);
				
				
				System.out.println("Gewählte Ecke: " + graph.getStrV(vi, "name"));
				
				//Vorwärtskante: Für jede Kante e die inzident von vi ist mit unmarkierter Ecke vj
				//und fluss(e) < kapazität(e) markiere vj mit 
				//-> vorgaenger(vi), pos(1), neg(0) und maxFlow(min(kapazität(e)-fluss(e), maxFlow(vi)))
				for(int id : graph.getIncident(vi)) {
					//Wenn das Ziel der Kante nicht markiert ist und deren Kapazität größer ist als der Fluss
					if(graph.getValV(graph.getTarget(id), "markiert") == 0 && graph.getValE(id, "kapazitaet") > graph.getValE(id, "fluss")) {
						graph.setValV(graph.getTarget(id), "vorgaenger", graph.getSource(id));
						graph.setValV(graph.getTarget(id), "pos", 1);
						graph.setValV(graph.getTarget(id), "neg", 0);
						graph.setValV(graph.getTarget(id), "maxFlow", Math.min(graph.getValE(id, "kapazitaet") - graph.getValE(id, "fluss"), graph.getValV(graph.getSource(id), "maxFlow")));
						graph.setValV(graph.getTarget(id), "markiert", 1);
						System.out.println("Neue Vorwärtskante zu: " + graph.getStrV(graph.getTarget(id),"name"));
					}
				}
				
				//Rückwärtskante: Für jede Kante e mit unmarkierter Ecke vj und Ziel vi
				//und fluss(e) > 0 markiere vj mit
				//-> vorgaenger(vi), pos(0), neg(1) und maxFlow(min(fluss(e), maxFlow(vi)))
				for(int id : graph.getEdges()) {
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					if(t == vi && graph.getValV(s, "markiert") == 0 && graph.getValE(id, "fluss") > 0) {
						graph.setValV(s, "vorgaenger", t);
						graph.setValV(s, "pos", 0);
						graph.setValV(s, "neg", 1);
						graph.setValV(s, "maxFlow", Math.min(graph.getValE(id, "fluss"), graph.getValV(t, "maxFlow")));
						graph.setValV(s, "markiert", 1);
						System.out.println("Neue Rückwärtskante zu: " + graph.getStrV(graph.getSource(id),"name"));
					}
				}
			}
			
			//2a Wenn alle markierten Ecken inspiziert wurden gehe nach 4
			if(allInspected) {break;}
			
			//3. Bei target beginnend lässt sich anhand der Markierungen der gefundene vergrößernde Weg bis 
			//zu source rückwärts durchlaufen. Für jede Vorwärtskante wird fluss(e) um maxFlow(target) erhöht und für jede 
			//Rückwärtskante wird fluss(e) um maxFlow(target) vermindert. 
			int id = target;
			int moreFlow = graph.getValV(target, "maxFlow");
			System.out.println("Folgender Fluss wird addiert: " + moreFlow);
			System.out.println("Der aktuelle Fluss in jeder Kante beträgt: ");

			while(id != source) {
				if(graph.getValV(id, "pos") != 0){			//Wenn wir eine Vorwärtskante haben
					for(int eid : graph.getIncident(graph.getValV(id, "vorgaenger"))) {
						if(graph.getTarget(eid) == id) {
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") + moreFlow);
						}
					}
				} else {
					for(int eid : graph.getIncident(id)) {
						if(graph.getTarget(eid) == id) {
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") - moreFlow);
						}
					}
				}
				//Setze die neue ID auf den Vorgänger
				id = graph.getValV(id, "vorgaenger");
			}
			
			
			for(int ide : graph.getEdges()) {
				System.out.println(graph.getStrV(graph.getSource(ide), "name") + " -> " + graph.getStrV(graph.getTarget(ide), "name") + " = " + graph.getValE(ide,"fluss") + "/" + graph.getValE(ide,"kapazitaet"));
			}
			
			System.out.println("###############################################################");
			
			
			//Anschließend werden bei allen Ecke mit Ausnahme von q die Markierungen entfernt. Gehe zu 2.
			for(int vid : graph.getVertexes()) {
				if(vid != source) {
					graph.setValV(vid, "vorgaenger", 0);
					graph.setValV(vid, "pos", 0);
					graph.setValV(vid, "neg", 0);
					graph.setValV(vid, "maxFlow",0);
					graph.setValV(vid, "markiert", 0);
					graph.setValV(vid, "inspiziert", 0);
				}
			}
			graph.setValV(source, "inspiziert", 0);
		}
		//4 Es gibt keinen vergrößernden Weg. Der jetzige Flusswert jeder Kante ist optimal
		int ergebnis = 0;
		for(int id : graph.getIncident(source)) {
			ergebnis += graph.getValE(id, "fluss");
		}
		return ergebnis;
	}
	
	
	
	/**Berechnet einen optimalen Fluss in einem schwach zusammenhängenden schlichten
	 * Digraphen. In diesem Digraphen müssen für jede Kante eine Kapazität "kapazitaet" 
	 * gegeben sein.
	 * @param graph Zu bearbeitender Graph
	 * @param source Quelle des Graphen
	 * @param target Senke des Graphen
	 */
	public static int fordFulkerson(Graph graph, int source, int target) {
		//1 Initialisierung
		//Die Kapazität ist als Attribut "kapazitaet" gegeben
		//In der folgenden Schleife wird der Fluss auf 0 gesetzt
		for(int id : graph.getEdges()) {
			graph.setValE(id, "fluss", 0);
		}
		
		Random generator = new Random();
		
		//Markiere jede Ecke als nicht inspiziert (0) und nicht markiert (0)
		for(int id : graph.getVertexes()) {
			graph.setValV(id, "inspiziert", 0);
			graph.setValV(id, "markiert", 0);
		}
		
		//Markiere die Quelle
		graph.setValV(source, "markiert", 1);
		graph.setValV(source, "maxFlow", Integer.MAX_VALUE);
		
		//2 Inspektion und Markierung
		
		while(true){
			boolean allInspected = false;

			while(true) {
				//Falls target markiert ist gehe zu 3.
				if(graph.getValV(target, "markiert") != 0) {
					break;
				}
			
				//b Wähle eine zufällige markierte, aber noch nicht inspizierte
				//Ecke vi und inspiziere sie wie folgt
				List<Integer> markierte = new ArrayList<Integer>();
				int vi = -1;
				for(int id : graph.getVertexes()) {
					if(graph.getValV(id, "markiert") != 0 && graph.getValV(id, "inspiziert") == 0) {	
						markierte.add(id);
					}
				}
				
				if(markierte.size() == 0) {allInspected = true; break;}
				vi = markierte.get(generator.nextInt(markierte.size()));
				graph.setValV(vi, "inspiziert", 1);
				
				System.out.println("Gewählte Ecke: " + graph.getStrV(vi, "name"));
				
				//Vorwärtskante: Für jede Kante e die inzident von vi ist mit unmarkierter Ecke vj
				//und fluss(e) < kapazität(e) markiere vj mit 
				//-> vorgaenger(vi), pos(1), neg(0) und maxFlow(min(kapazität(e)-fluss(e), maxFlow(vi)))
				for(int id : graph.getIncident(vi)) {
					//Wenn das Ziel der Kante nicht markiert ist und deren Kapazität größer ist als der Fluss
					if(graph.getValV(graph.getTarget(id), "markiert") == 0 && graph.getValE(id, "kapazitaet") > graph.getValE(id, "fluss")) {
						graph.setValV(graph.getTarget(id), "vorgaenger", graph.getSource(id));
						graph.setValV(graph.getTarget(id), "pos", 1);
						graph.setValV(graph.getTarget(id), "neg", 0);
						graph.setValV(graph.getTarget(id), "maxFlow", Math.min(graph.getValE(id, "kapazitaet") - graph.getValE(id, "fluss"), graph.getValV(graph.getSource(id), "maxFlow")));
						graph.setValV(graph.getTarget(id), "markiert", 1);
						System.out.println("Neue Vorwärtskante zu: " + graph.getStrV(graph.getTarget(id),"name"));
					}
				}
				
				//Rückwärtskante: Für jede Kante e mit unmarkierter Ecke vj und Ziel vi
				//und fluss(e) > 0 markiere vj mit
				//-> vorgaenger(vi), pos(0), neg(1) und maxFlow(min(fluss(e), maxFlow(vi)))
				for(int id : graph.getEdges()) {
					int s = graph.getSource(id);
					int t = graph.getTarget(id);
					if(t == vi && graph.getValV(s, "markiert") == 0 && graph.getValE(id, "fluss") > 0) {
						graph.setValV(s, "vorgaenger", t);
						graph.setValV(s, "pos", 0);
						graph.setValV(s, "neg", 1);
						graph.setValV(s, "maxFlow", Math.min(graph.getValE(id, "fluss"), graph.getValV(t, "maxFlow")));
						graph.setValV(s, "markiert", 1);
						System.out.println("Neue Rückwärtskante zu: " + graph.getStrV(graph.getSource(id),"name"));
					}
				}
			}
			
			//2a Wenn alle markierten Ecken inspiziert wurden gehe nach 4
			if(allInspected) {break;}
			
			//3. Bei target beginnend lässt sich anhand der Markierungen der gefundene vergrößernde Weg bis 
			//zu source rückwärts durchlaufen. Für jede Vorwärtskante wird fluss(e) um maxFlow(target) erhöht und für jede 
			//Rückwärtskante wird fluss(e) um maxFlow(target) vermindert. 
			int id = target;
			int moreFlow = graph.getValV(target, "maxFlow");
			System.out.println("Folgender Fluss wird addiert: " + moreFlow);
			while(id != source) {
				if(graph.getValV(id, "pos") != 0){			//Wenn wir eine Vorwärtskante haben
					for(int eid : graph.getIncident(graph.getValV(id, "vorgaenger"))) {
						if(graph.getTarget(eid) == id) {
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") + moreFlow);
						}
					}
				} else {
					for(int eid : graph.getIncident(id)) {
						if(graph.getTarget(eid) == id) {
							graph.setValE(eid, "fluss", graph.getValE(eid, "fluss") - moreFlow);
						}
					}
				}
				//Setze die neue ID auf den Vorgänger
				id = graph.getValV(id, "vorgaenger");
			}
			
			//Anschließend werden bei allen Ecke mit Ausnahme von q die Markierungen entfernt. Gehe zu 2.
			for(int vid : graph.getVertexes()) {
				if(vid != source) {
					graph.setValV(vid, "vorgaenger", 0);
					graph.setValV(vid, "pos", 0);
					graph.setValV(vid, "neg", 0);
					graph.setValV(vid, "maxFlow",0);
					graph.setValV(vid, "markiert", 0);
					graph.setValV(vid, "inspiziert", 0);
				}
			}
			graph.setValV(source, "inspiziert", 0);
		}
		//4 Es gibt keinen vergrößernden Weg. Der jetzige Flusswert jeder Kante ist optimal
		int ergebnis = 0;
		for(int id : graph.getIncident(source)) {
			ergebnis += graph.getValE(id, "fluss");
		}
		return ergebnis;
	}
	
	
	/** Berechnet den kürzesten Weg zu jedem Knoten im Graphen von v (Int) aus.
	 * Hierbei werden die ermittelten Werte im Graphen in jedem Knoten als "distanz" und "vorgaenger" gespeichert.
	 * Wenn "distanz" Integer.MAX_VALUE entspricht, dann gibt es keinen Weg zu diesem Knoten.
	 * Wenn "vorgaenger" Id.errorID entspricht, hat dieser Knoten keinen Vorgänger
	 * @param graph Zu bearbeitender Graph
	 * @param v Knoten von dem aus die kürzesten Wege gefunden werden sollen
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
		if (graph.directed()) {		//Wenn der Graph gerichtet ist, dann können Kanten zurück ignoriert werden
			counter.increment();
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {	//Wiederhole n-1 mal
				counter.increment();
				for (int id : graph.getEdges()) {																																													//für jedes (u,v) aus E
					counter.increment(7);
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorgänger(v) := u
						counter.increment(8);
					}
				}
			}

			counter.increment();
			for (int id : graph.getEdges()) {																												// für jedes (u,v) aus E
				counter.increment(7);
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
			}
		} else {					//Wenn der Graph ungerichtet ist, dann muss jede Abfrage auch in entgegengesetzte Richtung geführt werden
			counter.increment();
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																																								//Wiederhole n-1 mal
				counter.increment();
				for (int id : graph.getEdges()) {																																													//für jedes (u,v) aus E
					counter.increment(7);
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorgänger(v) := u
					}
					counter.increment(7);
					if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {	// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getSource(id), "distanz",graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht"));																					// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getSource(id), "vorgaenger",graph.getTarget(id));																              															//		Vorgänger(v) := u
					}
				}
			}

			counter.increment();
			for (int id : graph.getEdges()) {																												// für jedes (u,v) aus E
				counter.increment(7);
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
				counter.increment(7);
				if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
			}
		}
	}
	
	/** Wendet den Floyd-Warschall-Algorithmus auf einen Graphen an.
	 * Zurückgegeben wird ein Hash, der zum einen die Transitmatrix und zum anderen die Distanzmatrix enthält.
	 * Diese können mit dem Key "trans" bzw "dist" geholt werden und sind vom Datentyp List<List<Integer>>
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
				trans.get(i).add(-1);								//transitmatrix mit Fehlerwert befüllen
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
		
		for(int j = 0; j < size;j++) {																		//für jeden Knoten j
			for(int i = 0; i < size;i++) {																	//    für jeden Knoten i != j
				if(i != j) {														
					for(int k = 0; k < size;k++) {															//        für jeden Knoten k != j
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
		
		for(int i = 0; i < size;i++) {											//Über die gesammte Transitmatrix iterieren
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
