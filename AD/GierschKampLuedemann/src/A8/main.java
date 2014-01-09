package A8;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/* AUFGABE 8.5 */
		
//		IList list1 = new MyList();
//		IList list2 = new MyList();
//		IList list3 = new MyList();
//		list1 = list1.sortIncreasingMonoton(100);
//		list2 = list2.sortIncreasingMonoton(100);
//		
//		
//		list3 = list1.merge(list2);
//		
//		System.out.println("Sind die beiden Listen nun sortiert gemerged worden? " + list3.isIncreasingMonoton());
		
		
		/* AUFGABE 8.7 */
		IList list1 = new MyList();
		list1 = list1.random(10);
		
		System.out.println("Unsortiert: " + list1);
		System.out.println("Sortiert: " + list1.mergeSort());
		System.out.println("Ist sortiert? " + list1.isIncreasingMonoton());

	}

}
