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
 * this list stores 1000 pseudo random numbers the random numbers are uniformly
 * distributed in the interval [0,1) the list is static so the list is
 * initialized at class loading
 */
public class UniformDistRandomList {

    private static final double[] unr0_1 = new double[RandomNumber.FIXED_SIZE];

    static {
        Random rnd = new Random();
        rnd.setSeed(1);
        for (int i = 0; i < RandomNumber.FIXED_SIZE; i++) {
            unr0_1[i] = rnd.nextDouble();
        }
    }

    /**
     *
     * @return the size of the pseudo random number list
     */
    public static int length() {
        return unr0_1.length;
    }

    /**
     *
     * @param index
     * @return a uniformly distributed random number between 0 and 1
     */
    public static double getValue(int index) {
        return unr0_1[index];
    }
}
