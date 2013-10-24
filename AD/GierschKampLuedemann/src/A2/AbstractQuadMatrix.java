package A2;

public abstract class AbstractQuadMatrix implements QuadMatrix {

	int size;
	int timeCounter=0;
	
	@Override
	public int getSize() {
		timeCounter++;
		return size;
	}
	
	@Override
	public boolean equals(Object mtx) {
		if(this == mtx) {return true;}
		if(!(mtx instanceof QuadMatrix)) {return false;}
		if(!(this.getSize() == ((QuadMatrix) mtx).getSize())) {return false;}
		for(int i = 1; i <= this.getSize(); i++) {
			for(int j= 1; j <= this.getSize(); j++) {
				if(Double.compare(this.get(i, j), ((QuadMatrix) mtx).get(i, j)) != 0) {return false;} 
			}
		}
		return true;
	}

	@Override
	public QuadMatrix add(QuadMatrix mtx) {
		if (this.getSize() != mtx.getSize()) { throw new IllegalArgumentException("Ungleiche Matrixen addiert"); }

		QuadMatrix akku = this.init(size);
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size; j++) {
				akku.set(i, j, this.get(i, j) + mtx.get(i, j));
			}
		}
		return akku;
	}

	@Override
	public QuadMatrix mul(double n) {
		QuadMatrix akku = this.init(getSize());
		for (int i = 1; i <= size; i++) {
			for (int j = 1; j <= size; j++) {
				akku.set(i, j, this.get(i, j) * n);
			}
		}
		return akku;
	}

	@Override
	public QuadMatrix mul(QuadMatrix mtx) {
		if (this.getSize() != mtx.getSize()) { throw new IllegalArgumentException("Ungleiche Matrixen addiert"); }

		QuadMatrix akku = this.init(size);
		for (int i = 1; i <= size; i++) {
			for (int k = 1; k <= size; k++) {
				akku.set(i, k, 0);
				for (int j = 1; j <= size; j++) {
					akku.set(i, k, akku.get(i, k) + this.get(i, j) * mtx.get(j, k));
				}
			}
		}
		return akku;
	}

	@Override
	public QuadMatrix pow(int n) {
		QuadMatrix akku = this.init(getSize());
		
		for(int i = 1; i <= getSize(); i++) {
			akku.set(i, i, 1);
		}
		
		for(int i = 0; i < n; i++) {
			akku = akku.mul(this);
		}
		return akku;
	}
	@Override
	public int time(){
		return timeCounter;
	}
	@Override
	public void timeReset(){
		timeCounter=0;
	}
}
