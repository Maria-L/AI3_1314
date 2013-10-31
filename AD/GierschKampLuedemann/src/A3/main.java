package A3;

public class main {

	static Counter counter = new Counter();

	Counter getCounter() {
		return counter;
	}

	/** @param args */
	public static void main(String[] args) {

		for (int i = 0; i < 1000; i++) {
			counter.reset();
			System.out.println("##### " + i + " #####");
			System.out.println("Ergebnis: " + exp(2,i));
			counter.print();
		}
	}

	static int exp(int x, int k) {
		int r = 1;
		for (int i = 1; i <= k; i++) {
			counter.increment();
			r = r * x;
		}
		return r;
	}

	static int expOpt(int x, int k) {
		if (k == 0) { return 1; }

		if (k % 2 == 0) {
			int akku = expOpt(x, k / 2);
			counter.increment();
			return akku * akku;
		}
		int akku2 = expOpt(x, (k - 1) / 2);
		counter.increment(2);
		return x * akku2 * akku2;
	}

}
