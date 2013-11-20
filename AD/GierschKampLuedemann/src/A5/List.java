package A5;
import A3.Counter;

import A1.Liste;


public class List  {
	Object head = null;
	List tail = null;
	static Counter count = new Counter();
	
	public List() {
	}
	
	private List(Object head, List tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Object head() {
		count.increment();
		return head;
	}
	
	public List tail() {
		count.increment();
		return tail;
	}
	
	public void head(Object n) {
		count.increment();
		this.tail = new List(this.head, this.tail);
		this.head = n;
	}
	
	public boolean insert(Object n, int i) {
		if(i == 0) {
			head(n);
			return true;
		} else if (tail == null) {
			return false;
		} else {
			count.increment();
			tail.insert(n,i-1);
			return true;
		}
	}
	public void printTime(){
		count.print();
	}
}
