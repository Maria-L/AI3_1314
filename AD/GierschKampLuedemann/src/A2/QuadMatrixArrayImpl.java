package A2;

import java.util.*;


public class QuadMatrixArrayImpl extends AbstractQuadMatrix {

	double[][] matrix;

	public QuadMatrixArrayImpl(int n) {
		size = n;
		matrix = new double[size][size];
		for (int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				timeCounter+=1;
				matrix[i][j] = 0.0;
			}
		}
		
	}

	public QuadMatrix init(int n) {
		return new QuadMatrixArrayImpl(n);
	}
	

	@Override
	public double get(int m, int n) {
		timeCounter+=1;
		return matrix[m-1][n-1];
	}

	@Override
	public void set(int m, int n, double x) {
		timeCounter+=1;
		matrix[m-1][n-1] = x;

	}
	
	public void setGen(int m, int n, double x) {
		timeCounter+=1;
		matrix[m-1][n-1] = x;
	}

	@Override
	public int space() {
		timeCounter++;
		return size * size;
	}
}
