package A8;

import A3.Counter;
import A5.List;


public class MyList implements IList {
	Object head = null;
	IList tail = null;
	static int length = 0;
	static Counter count = new Counter();
	
	public MyList() {
	}
	
	private MyList(Object head, IList tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Object head() {
		length--;
		count.increment();
		Object temp = head;
		head = tail.top();
		tail = tail.tail();
		return temp;
	}
	
	public Object top() {
		return head;
	}
	
	public IList tail() {
		count.increment();
		return tail;
	}
	public int length(){
		return length;
	}
	
	public void head(Object n) {
//		count.increment();
		length++;
		this.tail = new MyList(this.head, this.tail);
		this.head = n;
	}
	
	public boolean insert(Object n, int i) {
		if(i == 0) {
			head(n);
			return true;
		} else if (tail == null) {
			return false;
		} else {
			length++;
			count.increment();
			tail.insert(n,i-1);
			return true;
		}
	}
	public int getStepCounter(){
		return count.getCount();
	}
	public void resetStepCounter(){
		count.reset();
	}
	public String toString(){
		String accu = "[ ";
		IList temp = this;
		for(int i=0; i < length; i++){
			accu = accu.concat(String.valueOf(temp.head()));
			accu = accu.concat(", ");
		}
		accu = accu.concat(" ]");
		return accu;
	}

	@Override
	public boolean isIncreasingMonoton() {
		if(tail.top() == null) {
			System.out.println("Abbruch");
			return true;
		} else {
			return ((int) this.top()) <= ((int) this.tail.top()) && tail.isIncreasingMonoton(); 
		}
	}
}
