package A7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Methods {
	public static List<Integer> investment(List<Integer> investments){
		int accu = -1;
		int iopt = 0;
		int jopt = 0;
		int diff = 0;
		List<Integer> result = new ArrayList<Integer>();
		
		for(int i = 0 ; i < investments.size() ; i++){
			for(int j = i ; j < investments.size() ; j++){
				diff = investments.get(j) - investments.get(i);
				if( diff > accu){
					iopt = i;
					jopt = j;
					accu = diff;
				}
			}
		}
		result.addAll(Arrays.asList(iopt,jopt,accu));
		
		return result;
	}
	
	
	public static List<Integer> investm(List<Integer> investments) {
		List<Integer> akku = new ArrayList<Integer>();
		List<List<Integer>> lists = new ArrayList<List<Integer>>();
		List<List<Integer>> lists2 = new ArrayList<List<Integer>>();
		
		//Aufteilen der Liste in 4er Arrays
		for(int i = 0; i < investments.size(); i += 4) {
			lists.add(Arrays.asList(investments.get(i), investments.get(i+1), investments.get(i+2), investments.get(i+3)));
		}
		
		// Speichere in jedem 4er Tupel in der Form [min,max,bestmin,bestmax,best]
		for(List<Integer> list : lists) {
			int min = Integer.MAX_VALUE;
			int max = Integer.MIN_VALUE;
			for(int i = 0; i < 4; i++) {
				if(list.get(i) > max) {
					max = list.get(i);
				} else if(list.get(i) < min) {
					min = list.get(i);
				}
			}
			List<Integer> best = investment(list);
			lists2.add(Arrays.asList(min,max,list.get(best.get(0)),list.get(best.get(1)),best.get(2)));
			//list = Arrays.asList(min,max,best.get(0),best.get(1),best.get(2));
		}

		akku = investHelper(lists2);
		return Arrays.asList(akku.get(2), akku.get(3), akku.get(4));
	}
	
	private static List<Integer> investHelper(List<List<Integer>> lists) {
		if(lists.size() == 1) {
			return lists.get(0);
		} else if (lists.size() == 2) {
			//Logik
			int num1 = lists.get(0).get(4);	//Nehme den besten Intervall der linken Liste
			int num2 = lists.get(1).get(4);	//Nehme den besten Intervall der rechten Liste
			int num3 = lists.get(1).get(1) - lists.get(0).get(0);	//Berechne den größten möglichen Intervall aus der linken und der rechten Liste kombiniert
			int max = lists.get(0).get(1) > lists.get(1).get(1) ? lists.get(0).get(1) : lists.get(1).get(1);	//Speichere das Maximum der linken und rechten Liste kombiniert
			int min = lists.get(0).get(0) < lists.get(1).get(0) ? lists.get(0).get(0) : lists.get(1).get(0);	//Speichere das Minimum der linken und rechten Liste kombiniert 
			
			if(num1 > num2 && num1 > num3) {
				lists.get(0).set(0, min);
				lists.get(0).set(1, max);
				return lists.get(0);
			} else if (num2 > num1 && num2 > num3) {
				lists.get(1).set(0, min);
				lists.get(1).set(1, max);
				return lists.get(1);
			} else {
				return Arrays.asList(lists.get(0).get(0),lists.get(1).get(1),lists.get(0).get(0),lists.get(1).get(1),lists.get(1).get(1) - lists.get(0).get(0));
			}
			
		} else {
			List<List<Integer>> list1 = new ArrayList<List<Integer>>();
			List<List<Integer>> list2 = new ArrayList<List<Integer>>();
			for(int i = 0; i < lists.size(); i++) {
				if(i <= lists.size()/2) {
					list1.add(lists.get(i));
				} else {
					list2.add(lists.get(i));
				}
			}
			return investHelper(Arrays.asList(investHelper(list1),investHelper(list1))); 
		}
	}
	
	public static void main(String[] args) {
		List<Integer> invest = new ArrayList<Integer>();
		
		invest.addAll(Arrays.asList(24,3,20,4,5,2,22,24,6,5,2,4,6,1,12,15));
		
		System.out.println(investm(invest));
		System.out.println(investment(invest));
		
//		List unicorn = investmentOpt(invest);
//		System.out.println("i:" + unicorn.get(0) + " j:" + unicorn.get(1) + " Max:" + unicorn.get(2));
	}
}
