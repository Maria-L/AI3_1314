package A8;

import java.util.*;


public class MyList implements IList {

	Object head = null;
	IList tail = null;
	int length = 0;

	public MyList() {}

	private MyList(Object head, IList tail) {
		this.head = head;
		this.tail = tail;
	}

	@Override
	public Object head() {
		length--;
		Object temp = head;
		if(tail != null) {
		head = tail.top();
		tail = tail.tail();
		} else {
			head = null;
		}
		return temp;
	}

	@Override
	public Object top() {
		return head;
	}

	@Override
	public IList tail() {
		return tail;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public void head(Object n) {
		length++;
		if (this.head != null) {
			this.tail = new MyList(this.head, this.tail);
		}
		this.head = n;
	}

	@Override
	public boolean insert(Object n, int i) {
		if (i == 0) {
			head(n);
			return true;
		} else if (tail == null) {
			return false;
		} else {
			length++;
			tail.insert(n, i - 1);
			return true;
		}
	}
	
	public void append(Object n) {
		if (this.top() == null) {
			this.head = n;
		} else if (this.tail == null) {
			this.tail = new MyList(n, null);
		} else {
			tail.append(n);
		}
	}

	@Override
	public String toString() {
		String accu = "[ ";
		IList temp = this;
		while (temp != null) {
			accu = accu.concat(String.valueOf(temp.top()));
			accu = accu.concat(", ");
			temp = temp.tail();
		}
		accu = accu.concat(" ]");
		return accu;
	}

	@Override
	public boolean isIncreasingMonoton() {
		if (tail == null) {
			return true;
		} else {
			return ((int) this.top()) <= ((int) this.tail.top())
					&& tail.isIncreasingMonoton();
		}
	}

	public IList random(int n) {
		IList akku = new MyList();
		Random generator = new Random();

		for (int i = 0; i < n; i++) {
			akku.head(generator.nextInt(1000));
		}
		return akku;
	}

	@Override
	public IList sortIncreasingMonoton(int n) {
		IList akku = new MyList();
		IList randomList = this.random(n);

		while (randomList.top() != null) {
			int elem = (int) randomList.head();
			IList temp = akku;
			int i = 0;
			boolean inserted = false;

			while (temp.tail() != null) {
				if ((int) temp.top() >= elem) {
					akku.insert(elem, i);
					inserted = true;
					break;
				} else {
					i++;
					temp = temp.tail();
				}
			}
			if(!inserted) {
				akku.insert(elem, i+1);
			}
		}
		return akku;
	}

	@Override
	public IList merge(IList list) {
		IList akku = new MyList();
		IList list1 = this;
		IList list2 = list;

		while (list1.top() != null && list2.top() != null) {
			if ((int) list1.top() < (int) list2.top()) {
				akku.append(list1.head());
			} else {
				akku.append(list2.head());
			}
		}
		while (list1.top() != null) {
			akku.append(list1.head());
		}
		while (list2.top() != null) {
			akku.append(list2.head());
		}
		return akku;
	}

	@Override
	public IList mergeSort() {
		if (this.tail == null) {
			return this;
		} else {
			IList list1 = new MyList();
			IList list2 = new MyList();
			int initSize = this.length() / 2;

			while (this.top() != null) {
				if (this.length() > initSize) {
					list1.head(this.head());
				} else {
					list2.head(this.head());
				}
			}
			return list1.mergeSort().merge(list2.mergeSort());
		}
	}

}
