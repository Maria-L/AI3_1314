/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Uebungsaufgabe_3_2;
import static com.google.common.base.Preconditions.*;

/**
 *
 * @author abk640
 */
public class Potenzieren {

    public int counter = 0;

    public int exp(int x, int k){
        checkArgument(k >= 0);
        int r = 1;
        for(int i=0; i<k; i++){
            this.counter += 2;
            r = r * x;
        }
        return r;
    }

    public int exp01(int x, int k){
        checkArgument(k >= 0);
        if(k == 0) return 1;
        else if(k%2 == 0){
            this.counter += 3;
            return exp01(x,k/2)*exp01(x,k/2);
        }
        else {
            counter += 3;
            return x * (exp01(x,k/2) * exp01(x,k/2));
        }

    }

    public void reset(){
            this.counter = 0;
    }

    public static void main(String[] args){
//        System.out.println(exp(2,50));
    }

}
