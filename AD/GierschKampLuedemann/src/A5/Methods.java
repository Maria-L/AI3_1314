package A5;

import java.util.ArrayList;
import java.util.List;

public abstract class Methods {
	public static List<Integer> array = new ArrayList<Integer>();
	
	public static int fancyFunction(int n) {
		array.set(n, array.get(n)+1);
		int accu = 0;
		if (n < 3) {
			accu = 1;
		} else {
			accu = fancyFunction(n-1) + 2*fancyFunction(n-2) + 3*fancyFunction(n-3);
		}
		return accu;
	}

}
