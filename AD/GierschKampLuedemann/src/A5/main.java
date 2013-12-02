package A5;

import A1.Liste;
import A1.ListeImpl;
import A1.Messung;


public class main {

	/** @param args */
	public static void main(String[] args) {

//		Messung m1 = new Messung();

		//		for (int t = 0; t < 100; t++) {
		//			Liste list1 = new ListeImpl();
		//			List list2 = new List();
		//			
		//			for (int i = 1; i <= 100; i++) {
		//				int n = (int) (Math.rint(Math.random() * (i-1)));
		//				list1.insert(i, n);
		//				list2.insert(n, i);
		//			}
		//
		//		}
		Liste list1 = new ListeImpl();
		List list2 = new List();

		for (int i = 0; i < 100 ; i++) {
			int n = (int) (Math.rint(Math.random() * (i - 1)));
			list1.insert(i, i);
			list2.insert(i, i);
			//			System.out.println("I: "+i +" N: "+ n);
		}
		//		int list1length=list1.length();
		//		System.out.println(list1length);
		//		for( int i = 1; i <= list1length; i++) {
		//			System.out.print(list1.head() + " ");
		//		}
		//		System.out.println("");
		//		while(list2.head() != null) {
		//			System.out.print((int) list2.head() + " ");
		//			list2 = list2.tail();
		//		}
		System.out.println();
		System.out.println("Alte Impl: " + list1.getStepCounter());
		System.out.println("Neue Impl: " + list2.getStepCounter());
		System.out.println();
		System.out.println("Länge: " + list1.length());
		System.out.println("Alte Liste: " + list1.toString());
		System.out.println("Länge: " + list2.length());
		System.out.println("Neue Liste: " + list2.toString());

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

		//		for(int i=0;i<=9;i++){
		//			System.out.println("Ergebnis f�r " + i + " ist " + Methods.fancyFunction(i));
		//		}
		//		for(int i=0;i<=Integer.MAX_VALUE;i++){
		//			int result =  Methods.fancyFunction(i);
		//			System.out.println("Ergebnis f�r " + i + " ist " + result);
		//			if(result < 0){
		//				break;
		//			}
		//		}
		
//		int n = 20;
//		for (int i = 0; i < n; i++) {
//			Methods.arrayNormal.add(i, 0);
//			Methods.arrayFancy.add(i, 0);
//		}
//
////		for (int i = 0; i < n; i++) {
////			System.out.println("Normal:    " + Methods.fancyFunction(i));
////			System.out.println("Optimiert: " + Methods.fancyFunctionOpt(i));
////		}
//		Methods.fancyFunction(n-1);
//		Methods.fancyFunctionOpt(n-1);
//
//		System.out.println(Methods.arrayNormal);
//		System.out.println(Methods.arrayFancy);
//		System.out.println(Methods.fancyArray);
	}

}
