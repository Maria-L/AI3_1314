package A8;

import java.util.*;
import A3.Counter;
import A5.List;


public class MyList implements IList {
	Object head = null;
	IList tail = null;
	static int length = 0;
	
	public MyList() {
	}
	
	private MyList(Object head, IList tail) {
		this.head = head;
		this.tail = tail;
	}
	
	public Object head() {
		length--;
		Object temp = head;
		head = tail.top();
		tail = tail.tail();
		return temp;
	}
	
	public Object top() {
		return head;
	}
	
	public IList tail() {
		return tail;
	}
	public int length(){
		return length;
	}
	
	public void head(Object n) {
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
			tail.insert(n,i-1);
			return true;
		}
	}
	
	public String toString(){
		String accu = "[ ";
		IList temp = this;
		while(temp != null){
			accu = accu.concat(String.valueOf(temp.top()));
			accu = accu.concat(", ");
			temp = temp.tail();
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

	public IList random(int n) {
		IList akku = new MyList();
		Random generator = new Random();
		
		for(int i = 0; i < n ; i++) {
			akku.head(generator.nextInt(1000));
		}
		
		return akku;
	}

	@Override
	public IList sortIncreasingMonoton(int n) {
		IList akku = new MyList();
		ArrayList<Integer> randomList = new ArrayList<Integer>();
		Random generator = new Random();
		int biggest = 0;
		
		for(int i = 0; i < n ; i++) {
			randomList.add(generator.nextInt(1000));
		}
		
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < randomList.size(); j++) {
				if(randomList.get(j) > randomList.get(biggest)){
					biggest = j;
				}
			}
			akku.head(randomList.get(biggest));
			randomList.remove(biggest);
		}
		return akku;
	}
	
	
}
