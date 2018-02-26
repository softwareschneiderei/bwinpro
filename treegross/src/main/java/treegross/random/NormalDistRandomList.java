/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.random;

import java.util.Random;

/**
 *
 * @author jhansen
 *
 * this list stores 1000 pseudo random numbers the random numbers are normally
 * distributed with mean 0 and standard deviation 1 the list is static so the
 * list is initialized at class loading
 */
public class NormalDistRandomList {

    private static final double[] nod_0_1 = new double[RandomNumber.FIXED_SIZE];

    static {
        Random rnd = new Random();
        rnd.setSeed(1);
        for (int i = 0; i < RandomNumber.FIXED_SIZE; i++) {
            nod_0_1[i] = rnd.nextGaussian();
        }
    }

    /**
     *
     * @return the size of the pseudo random number list
     */
    public static int length() {
        return nod_0_1.length;
    }

    /**
     *
     * @param index
     * @return a normally distributed random number std 1 and mean 0
     */
    public static double getValue(int index) {
        return nod_0_1[index];
    }
}
