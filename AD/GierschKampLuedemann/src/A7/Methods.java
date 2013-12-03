package A7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Methods {
	public static List<Integer> investment(List<Integer> investments){
		int accu = Integer.MIN_VALUE;
		int iopt = 0;
		int jopt = 0;
		int diff = 0;
		List<Integer> result = new ArrayList<Integer>();
		
		for(int i = 0 ; i < investments.size() ; i++){
			for(int j = i+1 ; j < investments.size() ; j++){
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
		
		if(investments.size() == 1) {
			throw new IllegalArgumentException("Das Array hat nur ein Element");
		} else if(investments.size() == 2){
			if(investments.get(0) <= investments.get(1)) {
				akku.addAll(Arrays.asList(0,1,investments.get(1) - investments.get(0)));
				return akku;
			} else {
				throw new IllegalArgumentException("Es gibt keinen gewinnbringenden Zeitraum");
			}
		}
		
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
		}

		akku = investHelper(lists2);
		
		if(akku.get(4) < 0) {
			throw new IllegalArgumentException("Es gibt keinen gewinnbringenden Zeitraum");
		}
		
		akku.set(2, investments.indexOf(akku.get(2)));
		for(int i = akku.get(2); i < investments.size();i++) {
			if(akku.get(3) == investments.get(i)) {
				akku.set(3, i);
				break;
			}
		}
		
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
			return investHelper(Arrays.asList(investHelper(list1),investHelper(list2))); 
		}
	}
	
	public static void main(String[] args) {
		List<Integer> invest = new ArrayList<Integer>();
		
		//invest.addAll(Arrays.asList(24,20,18,11));
		invest.addAll(Arrays.asList(24,3,20,4,5,2,22,6,24,5,2,4,6,1,12,15));
		//invest.addAll(Arrays.asList(1,2));
		
		System.out.println(investm(invest));
		System.out.println(investment(invest));
		
//		List unicorn = investmentOpt(invest);
//		System.out.println("i:" + unicorn.get(0) + " j:" + unicorn.get(1) + " Max:" + unicorn.get(2));
	}
}
