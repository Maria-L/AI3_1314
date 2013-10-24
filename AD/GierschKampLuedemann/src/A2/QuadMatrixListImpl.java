package A2;

import java.util.*;


public class QuadMatrixListImpl extends AbstractQuadMatrix {

	List<Double[]> array = new LinkedList<Double[]>();

	public QuadMatrixListImpl(int n) {
		this.size = n;
	}

	@Override
	public double get(int m, int n) {
		for (Double[] doub : array) {
			timeCounter += 2;
			if (doub[0] == m && doub[1] == n) {
				timeCounter += 2;
				return doub[2];
			}
		}
		return 0.0;
	}

	@Override
	public void set(int m, int n, double x) {
		for (Double[] doub : array) {
			timeCounter += 2;
			if (doub[0] == m && doub[1] == n) {
				timeCounter += 2;
				if (x == 0.0) {
					array.remove(doub);
					return;
				} else {
					doub[2] = x;
					return;
				}
			}
		}
		if (x != 0) {
			Double[] akku = { (double) m, (double) n, x };
			array.add(akku);
		}
	}
	
	public void setGen(int m, int n, double x) {
		timeCounter += array.size() * 2;
		timeCounter += 1;
		if (x != 0) {
			timeCounter += 1;
			Double[] akku = { (double) m, (double) n, x };
			array.add(akku);
		}
	}

	@Override
	public QuadMatrix init(int n) {
		return new QuadMatrixListImpl(n);
	}

	@Override
	public int space() {
		return array.size() * 3;
	}
	

}
