package A5;

import A1.Liste;
import A1.ListeImpl;
import A1.Messung;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Messung m1 = new Messung();

		for (int t = 0; t < 100; t++) {
			Liste list1 = new ListeImpl();
			List list2 = new List();
			
			for (int i = 1; i <= 100; i++) {
				int n = (int) (Math.rint(Math.random() * (i-1)));
				list1.insert(i, n);
				list2.insert(n, i);
			}

		}
		Liste list1 = new ListeImpl();
		List list2 = new List();
		
		for (int i = 0; i < 100; i++) {
			int n = (int) (Math.rint(Math.random() * (i-1)));
			list1.insert(n, i);
			list2.insert(n, i);
			System.out.println("I: "+i +" N: "+ n);
		}
		int list1length=list1.length();
		System.out.println(list1length);
		for( int i = 1; i <= list1length; i++) {
			System.out.print(list1.head() + " ");
		}
		System.out.println("");
		while(list2.head() != null) {
			System.out.print((int) list2.head() + " ");
			list2 = list2.tail();
		}

//		Liste list1 = new ListeImpl();
//		
//		for (int i = 0; i <= 3; i++) {
//			int n = (int) (1 * (i));
//			list1.insert(i, n);
//			System.out.println("I: "+ i +" N: "+ n + " N2: " + list1.top() + " ");
//		}
		
//		System.out.println(list1.length());
//		for( int i = 1; i < list1.length(); i++) {
//			System.out.print(list1.head() + " ");
//		}
	}

}
