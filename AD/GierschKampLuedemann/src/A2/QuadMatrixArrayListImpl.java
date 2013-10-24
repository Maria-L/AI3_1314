package A2;

import java.util.*;


public class QuadMatrixArrayListImpl extends AbstractQuadMatrix {

	List<List<Double[]>> array;

	public QuadMatrixArrayListImpl(int n) {
		array = new ArrayList<List<Double[]>>();

		for (int i = 0; i < n; i++) {
			timeCounter += 1;
			array.add(new LinkedList<Double[]>());
		}

		size = n;
	}

	@Override
	public double get(int m, int n) {
		for (Double[] list : array.get(m - 1)) {
			timeCounter += 2;
			if (list[0] == n) {
				timeCounter++;
				return list[1];
			}
		}
		return 0.0;
	}

	@Override
	public void set(int m, int n, double x) {
		for (Double[] list : array.get(m - 1)) {
			timeCounter += 2;
			if (list[0] == n) {
				timeCounter++;
				if (x != 0) {
					list[1] = x;
				} else {
					array.get(m - 1).remove(list);
				}
				return;
			}
		}
		if (x != 0) {
			Double[] akku = { (double) n, x };
			array.get(m - 1).add(akku);
		}
	}
	
	public void setGen(int m, int n, double x) {
		timeCounter += array.get(m-1).size() * 2;
		timeCounter += 1;
		if (x != 0) {
			timeCounter += 1;
			Double[] akku = { (double) n, x };
			array.get(m - 1).add(akku);
		}
	}

	@Override
	public QuadMatrix init(int n) {
		return new QuadMatrixArrayListImpl(n);
	}

	@Override
	public int space() {
		int akku = 0;
		for (List<Double[]> list : array) {
			timeCounter++;
			akku = akku + list.size();
		}
		return akku * 2;
	}

}
