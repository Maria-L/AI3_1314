package graphAlgorithms;

import graph.*;
import java.util.*;


public class Methods {

	/** Berechnet den k�rzesten Weg zu jedem Knoten im Graphen von v (Int) aus.
	 * Hierbei werden die ermittelten Werte im Graphen in jedem Knoten als "distanz" und "vorgaenger" gespeichert.
	 * Wenn "distanz" Integer.MAX_VALUE entspricht, dann gibt es keinen Weg zu diesem Knoten.
	 * Wenn "vorgaenger" Id.errorID entspricht, hat dieser Knoten keinen Vorg�nger
	 * @param graph Zu bearbeitender Graph
	 * @param v Knoten von dem aus die k�rzesten Wege gefunden werden sollen
	 */
	public static void bellmanFord(Graph graph, int v) {
		for (int id : graph.getVertexes()) {
			graph.setValV(id, "distanz", Integer.MAX_VALUE);
			graph.setValV(id, "vorgaenger", -1);
		}
		graph.setValV(v, "distanz", 0);

		if (graph.directed()) {		//Wenn der Graph gerichtet ist, dann k�nnen Kanten zur�ck ignoriert werden
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																																								//Wiederhole n-1 mal
				for (int id : graph.getEdges()) {																																													//f�r jedes (u,v) aus E
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorg�nger(v) := u
					}
				}
			}

			for (int id : graph.getEdges()) {																												// f�r jedes (u,v) aus E
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enth�lt negative Kreise");																// dann	Fehlerwert f�r Kreise mit negativen Gewichten
				}
			}
		} else {					//Wenn der Graph ungerichtet ist, dann muss jede Abfrage auch in entgegengesetzte Richtung gef�hrt werden
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																																								//Wiederhole n-1 mal
				for (int id : graph.getEdges()) {																																													//f�r jedes (u,v) aus E
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	//   wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));																					//   dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																																		//		  Vorg�nger(v) := u
					}
					if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {	// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getSource(id), "distanz",graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht"));																					// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getSource(id), "vorgaenger",graph.getTarget(id));																              															//		Vorg�nger(v) := u
					}
				}
			}

			for (int id : graph.getEdges()) {																												// f�r jedes (u,v) aus E
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					throw new IllegalArgumentException("Der Graph enth�lt negative Kreise");																// dann	Fehlerwert f�r Kreise mit negativen Gewichten
				}
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
		HashMap<String,List<List<Integer>>> akku = new HashMap<>();
		List<List<Integer>> trans = new ArrayList<List<Integer>>();
		List<List<Integer>> dist = new ArrayList<List<Integer>>();
		List<Integer> vertexes = graph.getVertexes();
		int size = graph.getVertexes().size();
		
		for(int i = 0; i < size;i++) {
			trans.add(new ArrayList<Integer>());
			dist.add(new ArrayList<Integer>());
			
			for(int j = 0; j < size;j++) {
				trans.get(i).add(0);
				if(i == j) {
					dist.get(i).add(0);
				} else {
					dist.get(i).add(Integer.MAX_VALUE);
				}
			}
		}
		
		for(int i : graph.getEdges()) {
			int temp = graph.getValE(i, "distanz");
			int source = graph.getSource(i);
			int target = graph.getTarget(i);
			
			source = vertexes.indexOf(source);
			target = vertexes.indexOf(target);
			
			
		}
		
		
		akku.put("trans", trans);
		akku.put("dist", dist);
		return akku;
	}
}
