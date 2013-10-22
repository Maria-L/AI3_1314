package A2;

import static org.junit.Assert.*;
import org.junit.Test;


public class AbstractQuadMatrixTest {
	
	

	@Test
	public void testEqualsObject() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		assertTrue(matrix1.equals(matrix2));
		assertTrue(matrix2.equals(matrix3));
		matrix1.set(3, 4, 1.0);
		assertFalse(matrix1.equals(matrix2));
		matrix3.set(3, 4, 1.0);
		assertFalse(matrix2.equals(matrix3));
	}

	@Test
	public void testAdd() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		matrix1.set(1, 1, 1);
		matrix2.set(1, 1, 2);
		matrix3.set(1, 1, 3);
		
		matrix1.set(1, 2, 2);
		matrix2.set(1, 2, 3);
		matrix3.set(1, 2, 5);
		
		assertTrue(matrix3.equals(matrix1.add(matrix2)));
		assertTrue(matrix3.equals(matrix2.add(matrix1)));
	}

	@Test
	public void testMulDouble() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		assertTrue(matrix1.equals(matrix2.mul(5)));
		matrix1.set(1, 1, 2); matrix1.set(1, 2, 3);
		matrix2.set(1, 1, 6); matrix2.set(1, 2, 9);
		assertTrue(matrix2.equals(matrix1.mul(3)));
	}

	@Test
	public void testMulQuadMatrix() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		matrix1.set(1, 1, 2); matrix1.set(1, 2, 3);
		matrix2.set(1, 1, 6); matrix2.set(1, 2, 9);
		matrix3.set(1, 1, 4); matrix3.set(1, 2, 8);
		assertEquals(matrix1.mul(matrix2.mul(matrix3)),matrix1.mul(matrix2).mul(matrix3));
		
		//Aufgabe 6
		int matrixSize = 20;
		QuadMatrix matrixA1 = QuadMatrixGenerator.random(matrix1, matrixSize, 0.1, 10);
		QuadMatrix matrixA2 = new QuadMatrixArrayListImpl(matrixSize); matrixA2 = matrixA2.add(matrixA1);
		QuadMatrix matrixA3 = new QuadMatrixListImpl(matrixSize); matrixA3 = matrixA3.add(matrixA1);
		QuadMatrix matrixB1 = QuadMatrixGenerator.random(matrix1, matrixSize, 0.1, 10);
		QuadMatrix matrixB2 = new QuadMatrixArrayListImpl(matrixSize); matrixB2 = matrixB2.add(matrixB1);
		QuadMatrix matrixB3 = new QuadMatrixListImpl(matrixSize); matrixB3 = matrixB3.add(matrixB1);
		
		assertEquals(matrixA1.mul(matrixB1),matrixA1.mul(matrixB1));
		assertEquals(matrixA1.mul(matrixB1),matrixA1.mul(matrixB2));
		assertEquals(matrixA1.mul(matrixB1),matrixA1.mul(matrixB3));
		assertEquals(matrixA1.mul(matrixB1),matrixA2.mul(matrixB1));
		assertEquals(matrixA1.mul(matrixB1),matrixA2.mul(matrixB2));
		assertEquals(matrixA1.mul(matrixB1),matrixA2.mul(matrixB3));
		assertEquals(matrixA1.mul(matrixB1),matrixA3.mul(matrixB1));
		assertEquals(matrixA1.mul(matrixB1),matrixA3.mul(matrixB2));
		assertEquals(matrixA1.mul(matrixB1),matrixA3.mul(matrixB3));
	}

	@Test
	public void testPow() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		for(int i = 1; i <= matrix1.getSize(); i++) {
			matrix1.set(i, i, 1);
		}
		matrix2.set(1, 1, 6); matrix2.set(1, 2, 9);
		matrix3.set(1, 1, 6); matrix3.set(1, 2, 9);
		
		assertEquals(matrix1, matrix2.pow(0));
		assertEquals(matrix2, matrix2.pow(1));
		assertEquals(matrix3, matrix3.pow(1));
	}
	
	@Test
	public void testSize() {
		QuadMatrix matrix1 = new QuadMatrixArrayImpl(10);
		QuadMatrix matrix2 = new QuadMatrixArrayListImpl(10);
		QuadMatrix matrix3 = new QuadMatrixListImpl(10);
		
		assertEquals(100,matrix1.space());
		assertEquals(0,matrix2.space());
		assertEquals(0,matrix3.space());
		
		matrix1.set(3, 4, 2); matrix1.set(5, 6, 3); matrix1.set(3, 4, 4); matrix1.set(5, 6, 0);
		matrix2.set(3, 4, 2); matrix2.set(5, 6, 3); matrix2.set(3, 4, 4); matrix2.set(5, 6, 0);
		matrix3.set(3, 4, 2); matrix3.set(5, 6, 3); matrix3.set(3, 4, 4); matrix3.set(5, 6, 0);
		
		assertEquals(100,matrix1.space());
		assertEquals(2,matrix2.space());
		assertEquals(3,matrix3.space());
	}

}
