package A5;

import java.util.ArrayList;
import java.util.List;

public abstract class Methods {
	public static List<Integer> arrayNormal = new ArrayList<Integer>();
	public static List<Integer> arrayFancy = new ArrayList<Integer>();
	public static List<Integer> fancyArray = new ArrayList<Integer>();
	
	public static int fancyFunction(int n) {
		//arrayNormal.set(n, arrayNormal.get(n)+1);
		int accu = 0;
		if (n < 3) {
			accu = 1;
		} else {
			accu = fancyFunction(n-1) + 2*fancyFunction(n-2) + 3*fancyFunction(n-3);
		}
		return accu;
	}
	
	public static int fancyFunctionOpt(int n) {
		fancyArray = new ArrayList<Integer>();
		for(int i = 0; i < n+1; i++) {
			fancyArray.add(-1);
		}
		
		return fancyHelper(n);
	}
	
	private static int fancyHelper(int n) {
		//arrayFancy.set(n, arrayFancy.get(n)+1);
		if(fancyArray.get(n) != -1) {
			return fancyArray.get(n);
		} else if (n < 3) {
			fancyArray.set(n, 1);
			return 1;
		} else {
			fancyArray.set(n, 3*fancyHelper(n-3) + 2*fancyHelper(n-2) + fancyHelper(n-1));
			return fancyArray.get(n);
		}
	}

}
