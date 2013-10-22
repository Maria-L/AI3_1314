import A1.*;


public class main {

	/** @param args */
	public static void main(String[] args) {
		
		
		Messung m1 = new Messung();

		for (int t = 0; t < 100; t++) {
			Liste list = new ListeImpl();
			for (int i = 1; i <= 100; i++) {
				list.insert(i, (int) (Math.rint(Math.random() * (i-1))));
			}

			m1.add(list.getStepCounter());
			list.resetStepCounter();
			System.out.println(Math.round(m1.average()));

		}
		
//		Liste list = new ListeImpl();
//		for (int i = 0; i < 100; i++) {
//			list.insert(i, i);
//		}
//		
//		System.out.println(list.getStepCounter());
		
//	System.out.println("Der Durchschnitt beträgt: " + m1.average());
//	System.out.println("Die Varianz beträgt:      " + m1.varianz());
	}
}
