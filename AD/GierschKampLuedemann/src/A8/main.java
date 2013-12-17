package A8;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IList list = new MyList();
		
		list.head(5);
		list.head(4);
		list.head(4);
		list.head(1);
		
		System.out.println(list.isIncreasingMonoton());
		
		list = list.sortIncreasingMonoton(100);
		
		System.out.println(list);
	}

}
