package A6;

import java.util.*;


public class Methods {
	private static int bestVol;
	private static int bestPrice;
	public static List<Produkt> bestProducts;
	
	public static List<Produkt> perfektesMuesli(List<Produkt> products, int schuesselgroesse) {
		bestVol = Integer.MAX_VALUE;
		bestPrice = Integer.MAX_VALUE;
		bestProducts = new ArrayList<Produkt>();
		
		muesliHelper(products, new ArrayList<Produkt>(), schuesselgroesse, 0);
		
		
		return bestProducts;
	}
	
	private static void muesliHelper(List<Produkt> uebrigeZutaten, List<Produkt> gewaehlteZutaten, int volumen, int preis) {
		if(uebrigeZutaten.size() == 0) {
			if((bestVol == volumen && bestPrice > preis) || bestVol > volumen) {
				bestProducts = gewaehlteZutaten;
				bestVol = volumen;
				bestPrice = preis;
			}
			return;
		}
		
		List<Produkt> uebrig = new ArrayList<Produkt>();
		for(int i = 1; i < uebrigeZutaten.size(); i++) {
			uebrig.add(uebrigeZutaten.get(i));
		}
		
		if(uebrigeZutaten.get(0).getVol() <= volumen) {
			
			List<Produkt> gewaehlt = new ArrayList<Produkt>();
			
			gewaehlt.addAll(gewaehlteZutaten);
			gewaehlt.add(uebrigeZutaten.get(0));
			
			muesliHelper(uebrig, gewaehlt, volumen - uebrigeZutaten.get(0).getVol(), preis + uebrigeZutaten.get(0).getPrice());
		}
		
		muesliHelper(uebrig, gewaehlteZutaten, volumen, preis);
	}
	
	public static void main(String args[]) {
		List<Produkt> products = new ArrayList<Produkt>();
		
		products.add(new Produkt("Haferflocken", 80, 20));
		products.add(new Produkt("Haselnüsse", 10, 80));
		products.add(new Produkt("Foo", 111, 15));
		products.add(new Produkt("Kraut", 50, 40));
		products.add(new Produkt("Smarties", 25, 10));
		products.add(new Produkt("Hühneraugen", 30, 70));
		
		products = perfektesMuesli(products, 200);
		
		for(int i = 0; i < products.size(); i++) {
			System.out.println(products.get(i));
		}
		
		System.out.println("Übriges Volumen: " + bestVol);
		System.out.println("Gesamter Preis: " + bestPrice);
	}
}
