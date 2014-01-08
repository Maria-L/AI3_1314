package A8;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IList list1 = new MyList();
		list1 = list1.random(10000);
		
		System.out.println("Unsortiert: " + list1);
		System.out.println("Sortiert: " + list1.mergeSort());
		System.out.println("Ist sortiert? " + list1.isIncreasingMonoton());
	}

}
