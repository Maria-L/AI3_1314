package graphReader;

import java.io.*;
import java.util.*;
import graph.*;
import graphAlgorithms.Methods;


public class main {

	/** @param args */
	public static void main(String[] args) {
		Graph graph = null;

		BufferedReader br = null;

		try {
			File file = new File("F:/GitDaten/AI3_1314/GKA/GKA/src/graphReader/graphs/graph_03.graph");
			br = new BufferedReader(new FileReader(file));

			graph = graphReaderGewicht(br);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		HashMap<String,List<List<Integer>>> hash = Methods.floydWarshall(graph);
		List<List<Integer>> list = hash.get("trans");
		//System.out.println(list);
		
		for(int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
//		for(int i : graph.getVertexes()) {
//			System.out.println("#######" + graph.getStrV(i, "name") + "#######");
//			Methods.bellmanFord(graph, i);
//			graph.print();
//			System.out.println("");
//			System.out.println("");
//			System.out.println("");
//		}
	}

	public static Graph graphReader1(BufferedReader br) throws IOException {
		String line = null;
		Graph graph = new GraphImpl();
		boolean directed;

		if (br.readLine() == "#gerichtet") {	//Prüfen ob der Graph gerichtet ist oder nicht
			directed = true;
		} else {
			directed = false;
		}

		while ((line = br.readLine()) != null) {								//Ausführen, bis es keine Zeilen mehr gibt
			String[] parts = line.split(",");									//Linie nach Komata splitten
			int v1 = -1;
			int v2 = -1;
			HashMap<String, Integer> names = new HashMap<String, Integer>();	//Map von Namen auf ID

			for (int v : graph.getVertexes()) {									//Namen aktualisieren
				names.put(graph.getStrV(v, "name"), v);
			}

			if (names.keySet().contains(parts[0])) {							//Prüfen ob der erste Knoten schon im Graphen ist
				v1 = names.get(parts[0]);										//Wenn ja: ID des Knoten raussuchen
			} else {
				v1 = graph.addVertex(parts[0]);									//Wenn nein: neuen Knoten in dem Graphen ablegen
			}

			if (names.keySet().contains(parts[1])) {							//Wie mit Knoten 1
				v2 = names.get(parts[1]);
			} else {
				v2 = graph.addVertex(parts[1]);
			}

			for (int i = 2; i < parts.length; i++) {							//Da die restlichen Stellen der Zeile Kantenattribute 
				Id id = new Id(0);												//sind dementsprechende Kanten zufügen
				int e1;
				String name = "Attribut" + id.newID();							//Namen für dieses Attribut zuweisen - "Attribut1", "Attribut2"...

				if (directed) {	
					e1 = graph.addEdgeD(v1, v2);								//Kante hinzufügen
				} else {
					e1 = graph.addEdgeU(v1, v2);
				}

				graph.setValE(e1, name, Integer.parseInt(parts[i]));			//Attribut an diese Kante setzen
			}
		}

		return graph;
	}
	
	public static Graph graphReaderGewicht(BufferedReader br) throws IOException {
		String line = null;
		Graph graph = new GraphImpl();

		if (br.readLine() == "#gerichtet") {	//Prüfen ob der Graph gerichtet ist oder nicht
			graph.setDirected(true);
		} else {
			graph.setDirected(false);
		}

		while ((line = br.readLine()) != null) {								//Ausführen, bis es keine Zeilen mehr gibt
			String[] parts = line.split(",");									//Linie nach Komata splitten
			int v1 = -1;
			int v2 = -1;
			HashMap<String, Integer> names = new HashMap<String, Integer>();	//Map von Namen auf ID

			for (int v : graph.getVertexes()) {									//Namen aktualisieren
				names.put(graph.getStrV(v, "name"), v);
			}

			if (names.keySet().contains(parts[0])) {							//Prüfen ob der erste Knoten schon im Graphen ist
				v1 = names.get(parts[0]);										//Wenn ja: ID des Knoten raussuchen
			} else {
				v1 = graph.addVertex(parts[0]);									//Wenn nein: neuen Knoten in dem Graphen ablegen
			}

			if (names.keySet().contains(parts[1])) {							//Wie mit Knoten 1
				v2 = names.get(parts[1]);
			} else {
				v2 = graph.addVertex(parts[1]);
			}

			for (int i = 2; i < parts.length; i++) {							//Da die restlichen Stellen der Zeile Kantenattribute 
				Id id = new Id(0);												//sind dementsprechende Kanten zufügen
				int e1;
				String name = "gewicht";										//Namen für dieses Attribut zuweisen - "Attribut1", "Attribut2"...

				if (graph.directed()) {	
					e1 = graph.addEdgeD(v1, v2);								//Kante hinzufügen
				} else {
					e1 = graph.addEdgeU(v1, v2);
				}

				graph.setValE(e1, name, Integer.parseInt(parts[i]));			//Attribut an diese Kante setzen
			}
		}

		return graph;
	}

}
