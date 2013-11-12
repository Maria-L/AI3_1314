package graphAlgorithms;


public class Counter {
	private int count;
	private int init;
	
	public Counter(int init) {
		this.init = init;
		count = init;
	}
	
	public int getCount() {
		return count;
	}
	
	public void reset() {
		count = init;
	}
	
	public void increment() {
		count++;
	}
	
	public void increment(int n) {
		count = count + n;
	}
}
