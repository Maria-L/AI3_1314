package graph;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;


public class GraphImplTest {

	public Graph graph(){
		Graph tgraph = new GraphImpl();
		int v1 = tgraph.addVertex("v1");
		int v2 = tgraph.addVertex("v2");
		int v3 = tgraph.addVertex("v3");
		
		int e1 = tgraph.addEdgeU(v1, v2);
		int e2 = tgraph.addEdgeD(v1, v3);
		int e3 = tgraph.addEdgeD(v3, v2);
		
		
		tgraph.setValE(e1,"Strecke",2);
		tgraph.setValE(e1,"Durchfluss", 4);
		tgraph.setValE(e2, "Strecke", 3);
		tgraph.setValE(e3, "Strecke", 4);
		
		tgraph.setValV(v1, "Größe", 5);
		
		tgraph.setStrE(e1, "Art", "ICE");
		tgraph.setStrE(e2, "Art", "ME");
		
		tgraph.setStrV(v1, "Sonder" , "Rangier");
		
		return tgraph; 
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {}

	@Test
	public void testAddVertex() {
		Graph tgraph = graph();

		assertTrue("v1" == (tgraph.getStrV(0, "name")));
		assertTrue("v2" == (tgraph.getStrV(1, "name")));
	}

	@Test
	public void testDeleteVertex() {
		Graph tgraph = graph();
		tgraph.deleteVertex(2);
		assertFalse(tgraph.getVertexes().contains(2));
		assertFalse(tgraph.getEdges().contains(3) && tgraph.getEdges().contains(5));
	}

	@Test
	public void testAddEdgeU() {
		Graph tgraph = graph();
		assertTrue("v1" == (tgraph.getStrV(tgraph.getSource(3),"name")));
	}

	@Test
	public void testAddEdgeD() {
		Graph tgraph = graph();
		assertTrue("v1" == (tgraph.getStrV(tgraph.getSource(3),"name")));
		assertTrue("v3" == (tgraph.getStrV(tgraph.getSource(5),"name")));
		
	}

	@Test
	public void testDeleteEdge() {
		Graph tgraph = graph();
		tgraph.deleteEdge(0, 1);
		assertFalse(tgraph.getEdges().contains(3));
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteEdgeEx(){
		Graph tgraph = graph();
		tgraph.deleteEdge(2, 0);
		assertTrue(tgraph.getEdges().contains(4));
	}
	

	@Test
	public void testIsEmpty() {
		Graph tgraph = new GraphImpl();
		assertTrue(tgraph.isEmpty());
	}

	@Test
	public void testGetSource() {
		Graph tgraph = graph();
		assertEquals(0,tgraph.getSource(3));
		assertEquals(2,tgraph.getSource(5));
	}

	@Test
	public void testGetTarget() {
		Graph tgraph = graph();
		assertEquals(1, tgraph.getTarget(3));
		assertEquals(2, tgraph.getTarget(4));
	}

	@Test
	public void testGetIncident() {
		Graph tgraph = graph();
		List<Integer> iList = new ArrayList<Integer>();
		iList.add(3);
		iList.add(4);
		List<Integer> iList2 = new ArrayList<Integer>();
		iList2.add(5);
		
		assertEquals(tgraph.getIncident(0),iList);
		assertEquals(tgraph.getIncident(2),iList2);
	}

	@Test
	public void testGetAdjacent() {
		Graph tgraph = graph();
		List<Integer> aList = new ArrayList<Integer>();
		aList.add(1);
		aList.add(2);
		List<Integer> aList2 = new ArrayList<Integer>();
		aList2.add(1);
		
		assertEquals(tgraph.getAdjacent(0),aList);
		assertEquals(tgraph.getAdjacent(2),aList2);
	}

	@Test
	public void testGetVertexes() {
		Graph tgraph = graph();
		List<Integer> vList = new ArrayList<Integer>();
		vList.add(0);
		vList.add(1);
		vList.add(2);
		
		assertEquals(tgraph.getVertexes(),vList);
	}

	@Test
	public void testGetEdges() {
		Graph tgraph = graph();
		List<Integer> eList = new ArrayList<Integer>();
		eList.add(3);
		eList.add(4);
		eList.add(5);
		
		assertEquals(tgraph.getEdges(),eList);
	}

	@Test
	public void testGetValE() {
		Graph tgraph = graph();
		assertEquals(2,tgraph.getValE(3, "Strecke"));
		assertEquals(3,tgraph.getValE(4, "Strecke"));
	}

	@Test
	public void testGetValV() {
		Graph tgraph = graph();
		assertEquals(5,tgraph.getValV(0,"Größe"));
	}
	
	@Test
	public void testGetStrE() {
		Graph tgraph = graph();
		assertTrue("ICE" == tgraph.getStrE(3, "Art"));
		assertTrue("ME" == tgraph.getStrE(4, "Art"));
	}

	@Test
	public void testGetStrV() {
		Graph tgraph = graph();
		assertTrue("Rangier" == tgraph.getStrV(0, "Sonder"));
	}

	@Test
	public void testGetAttrV() {
		Graph tgraph = graph();
		
		assertTrue(tgraph.getAttrV(0).contains("Größe") && tgraph.getAttrV(0).contains("Sonder"));
	}

	@Test
	public void testGetAttrE() {
		Graph tgraph = graph();
		
		assertTrue(tgraph.getAttrE(3).contains("Strecke") && tgraph.getAttrE(3).contains("Durchfluss") && tgraph.getAttrE(3).contains("Art"));
	}

	@Test
	public void testSetValE() {
		Graph tgraph = graph();
		
		assertEquals(tgraph.getValE(3, "Strecke"),2);
		tgraph.setValE(3, "Strecke", 3);
		assertEquals(tgraph.getValE(3,"Strecke"),3);
	}

	@Test
	public void testSetValV() {
		Graph tgraph = graph();
		
		assertEquals(tgraph.getValV(0, "Größe"),5);
		tgraph.setValV(0, "Größe", 6);
		assertEquals(tgraph.getValV(0, "Größe"),6);
	}

	@Test
	public void testSetStrE() {
		Graph tgraph = graph();
		
		assertTrue("ICE" == tgraph.getStrE(3, "Art"));
		tgraph.setStrE(3, "Art", "IC");
		assertTrue("IC" == tgraph.getStrE(3, "Art"));
		
	}

	@Test
	public void testSetStrV() {
		Graph tgraph = graph();
		assertTrue("Rangier" == tgraph.getStrV(0, "Sonder"));
		tgraph.setStrV(0, "Sonder", "Verlade");
		assertTrue("Verlade" == tgraph.getStrV(0, "Sonder"));
	}

}
