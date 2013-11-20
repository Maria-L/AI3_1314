package A3;


public class Counter {
	private int count = 0;
	
	public Counter() {};
	
	public void print() {
		System.out.println(count);
	}
	
	public void reset() {
		count = 0;
	}
	
	public void increment() {
		count++;
	}
	
	public void increment(int n) {
		count = count + n;
	}
}
