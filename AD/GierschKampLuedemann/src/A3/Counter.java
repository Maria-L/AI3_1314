package A3;


public class Counter {
	private int count = 0;
	
	public Counter() {};
	
	void print() {
		System.out.println(count);
	}
	
	void reset() {
		count = 0;
	}
	
	void increment() {
		count++;
	}
	
	void increment(int n) {
		count = count + n;
	}
}
