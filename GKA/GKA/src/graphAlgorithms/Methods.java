package graphAlgorithms;

import graph.Graph;


public class Methods {

	public static int bellmanFord(Graph graph, int v) {
		for (int id : graph.getVertexes()) {
			graph.setValV(id, "distanz", Integer.MAX_VALUE);
			graph.setValV(id, "vorgaenger", -1);
		}
		graph.setValV(v, "distanz", 0);

		if (graph.directed()) {
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																					//Wiederhole n-1 mal
				for (int id : graph.getEdges()) {																										//für jedes (u,v) aus E
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {	// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));		// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));															//		Vorgänger(v) := u
					}
				}
			}

			for (int id : graph.getEdges()) {																												// für jedes (u,v) aus E
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					System.out.println("negative Kreise");
					//throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
			}
		} else {
			for (int i = 0; i < graph.getVertexes().size() - 1; i++) {																						//Wiederhole n-1 mal
				for (int id : graph.getEdges()) {																											//für jedes (u,v) aus E
					if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {										// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getTarget(id), "distanz",graph.getValV(graph.getSource(id), "distanz")+ graph.getValE(id, "gewicht"));			// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getTarget(id), "vorgaenger",graph.getSource(id));																//		Vorgänger(v) := u
					}
					if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {										// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
						graph.setValV(graph.getSource(id), "distanz",graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht"));			// dann Distanz(v) := Distanz(u) + Gewicht(u,v)
						graph.setValV(graph.getSource(id), "vorgaenger",graph.getTarget(id));																//		Vorgänger(v) := u
					}
				}
			}

			for (int id : graph.getEdges()) {																												// für jedes (u,v) aus E
				if ((graph.getValV(graph.getSource(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getSource(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getTarget(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					System.out.println("negative Kreise");
					//throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
				if ((graph.getValV(graph.getTarget(id), "distanz") != Integer.MAX_VALUE && (graph.getValV(graph.getTarget(id), "distanz") + graph.getValE(id, "gewicht")) < graph.getValV(graph.getSource(id), "distanz"))) {		// wenn Distanz(u) + Gewicht(u,v) < Distanz(v)
					System.out.println("negative Kreise");
					//throw new IllegalArgumentException("Der Graph enthält negative Kreise");																// dann	Fehlerwert für Kreise mit negativen Gewichten
				}
			}
		}

		return graph.getValV(v, "distanz");
	}
}
