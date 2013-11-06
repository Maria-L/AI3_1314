package A5;

import A1.Liste;


public class List  {
	Object head = null;
	List tail = null;
	
	public List() {
	}
	
	private List(Object head, List tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Object head() {
		return head;
	}
	
	public List tail() {
		return tail;
	}
	
	public void head(Object n) {
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
			tail.insert(n,i-1);
			return true;
		}
	}
}
