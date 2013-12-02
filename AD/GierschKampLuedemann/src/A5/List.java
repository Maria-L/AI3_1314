package A5;
import A3.Counter;



public class List  {
	Object head = null;
	List tail = null;
	static int length = 0;
	static Counter count = new Counter();
	
	public List() {
	}
	
	private List(Object head, List tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Object head() {
		count.increment();
		Object temp = head;
		head = tail.head;
		tail = tail.tail;
		return temp;
	}
	
	public List tail() {
		count.increment();
		return tail;
	}
	public int length(){
		return length;
	}
	
	public void head(Object n) {
//		count.increment();
		length++;
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
	public int getStepCounter(){
		return count.getCount();
	}
	public void resetStepCounter(){
		count.reset();
	}
	public String toString(){
		String accu = "[ ";
		List temp = this;
		for(int i=0; i < length; i++){
			accu = accu.concat(String.valueOf(temp.head()));
			accu = accu.concat(", ");
		}
		accu = accu.concat(" ]");
		return accu;
	}
}
