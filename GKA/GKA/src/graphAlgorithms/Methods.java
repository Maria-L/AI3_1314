package graphAlgorithms;

import graph.*;

import java.util.*;


public class Methods {
	public static Counter counter = new Counter(0);
	public static Counter counterMatrix = new Counter(0);

	
	/**Berechnet einen optimalen Fluss in einem schwach zusammenhängenden schlichten
	 * Digraphen. In diesem Digraphen müssen für jede Kante eine Kapazität "kapazitaet" 
	 * gegeben sein.
	 * @param graph Zu bearbeitender Graph
	 * @param source Quelle des Graphen
	 * @param target Senke des Graphen
	 */
	public static void fordFulkerson(Graph graph, int source, int target) {
		//1 Initialisierung
		//Die Kapazität ist als Attribut "gewicht" gegeben
		
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
