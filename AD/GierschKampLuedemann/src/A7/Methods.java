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
		
		for(int i = 0 ; i < investments.size()-1 ; i++){
			for(int j = i ; j < investments.size()-1 ; j++){
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
	
	public static List<Integer> investmentOpt(List<Integer> investments) {
		List<List<Integer>> shit = new ArrayList<List<Integer>>();
		List<Integer> tempShit;
		for(int i = 0; i < investments.size(); i = i + 2) {
			tempShit = new ArrayList<Integer>();
			tempShit.add(investments.get(i));
			tempShit.add(investments.get(i+1));
			shit.add(tempShit);
		}
		
		List<Integer> result = new ArrayList<Integer>(Arrays.asList(-1,-1,-1));
		return invHelper(shit,result,0,investments.size()-1);
	}
	private static List<Integer> invHelper(List<List<Integer>> all,List<Integer> accu,int actIndex,int maxIndex){
		if(actIndex < maxIndex-1){
			if(all.get(actIndex).get(0) <= all.get(actIndex).get(1)){
				accu.set(0, all.get(actIndex).get(0));
				accu.set(1, all.get(actIndex).get(1));
			}
			accu.set(2, all.get(actIndex).get(0)-all.get(actIndex).get(1));
			return invHelper(all,accu,actIndex+1,maxIndex);
		}
		return accu;
	}
	
	public static List<Integer> investm(List<Integer> investments) {
		List<Integer> akku = new ArrayList<Integer>();
		List<List<Integer>> lists = new ArrayList<List<Integer>>();
		
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
			list = Arrays.asList(min,max,best.get(0),best.get(1),best.get(2));
		}
		System.out.println(lists);
		return investHelper(lists);
	}
	
	private static List<Integer> investHelper(List<List<Integer>> lists) {
		if(lists.size() == 1) {
			return lists.get(0);
		} else if (lists.size() == 2) {
			//Logik
			int num1 = lists.get(0).get(4);
			int num2 = lists.get(1).get(4);
			int num3 = lists.get(1).get(1) - lists.get(0).get(0);
			int max = lists.get(0).get(1) > lists.get(1).get(1) ? lists.get(0).get(1) : lists.get(1).get(1);
			int min = lists.get(0).get(1) < lists.get(1).get(0) ? lists.get(0).get(0) : lists.get(1).get(0);
			
			if(num1 > num2 && num1 > num3) {
				//Min und Max fehlt
				lists.get(0).set(0, min);
				lists.get(0).set(1, max);
				return lists.get(0);
			} else if (num2 > num1 && num2 > num3) {
				//Min und Max fehlt
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
		
		invest.addAll(Arrays.asList(24,2,20,2,5,1,22,24));
		
		System.out.println(investm(invest));
		
//		List unicorn = investmentOpt(invest);
//		System.out.println("i:" + unicorn.get(0) + " j:" + unicorn.get(1) + " Max:" + unicorn.get(2));
	}
}
