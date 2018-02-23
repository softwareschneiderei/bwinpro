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
 * RandomNumber : this class can be used to calculated normal distributed
 * errors, etc. For normally distributed random numbers it is possible to limit
 * sigma. The values are calculated use the procedure described in Hartung, J.
 * (1989) Statistik (7.Auflage), Oldenbourg, page: 890
 *
 */
public class RandomNumber {

    /**
     * random type OFF: Random number generation is turned off.
     * <code>randomOn</code> is set to <code>false</code>. The next-methods
     * return 0.
     */
    public final static int OFF = 10;

    /**
     * random type PSEUDO_FIXED: Random number generator uses a fixed set of
     * random numbers. <code>randomOn</code> is set to <code>true</code>. The
     * next-methods return random numbers of static lists
     * (<code>UniformDistRandomList</code> and <code>NormalDistRandomList</code>
     * at index <code>loopPos</code> and increase <code>loopPos</code> by one.
     * If <code>loopPos</code> > highest index (999) <code>loopPos</code> is set
     * back to 0.
     */
    public final static int PSEUDO_FIXED = 11;

    /**
     * random type PSEUDO: A pseudo-random number generator
     * (<code>java.util.Random</code>) is used. <code>randomOn</code> is set to
     * <code>true</code>. The next-methods return random numbers generated at
     * runtime.
     */
    public final static int PSEUDO = 12;

    public final static int FIXED_SIZE = 5000;

    private int loopPos;
    private int randomType;
    public boolean randomOn;
    private final Random r;

    // parameters for normal dist. generation:
    private final double a1 = 0.31938513;
    private final double a2 = -0.356563782;
    private final double a3 = 1.781477937;
    private final double a4 = -1.821255978;
    private final double a5 = 1.330274429;

    public RandomNumber(int type) {
        setRandomType(type);
        loopPos = -1;
        r = new Random();
    }

    public int getRandomType() {
        return randomType;
    }

    public final void setRandomType(int type) {
        randomType = PSEUDO;
        if (type >= 10 && type <= 12) {
            randomType = type;
        }
        randomOn = (randomType != OFF);
    }

    public void setLoopIndex(int i) {
        loopPos = i;
    }

    public int getLoopIndex() {
        return loopPos;
    }

    private void loop() {
        loopPos++;
        if (loopPos > (FIXED_SIZE - 1)) {
            loopPos = 0;
        }
    }

    public double nextUniform() {
        switch (randomType) {
            case PSEUDO:
                return r.nextDouble();
            case PSEUDO_FIXED:
                loop();
                return UniformDistRandomList.getValue(loopPos);
        }
        return 0.5;
    }

    public double nextNormal() {
        switch (randomType) {
            case PSEUDO:
                return r.nextGaussian();
            case PSEUDO_FIXED:
                loop();
                return NormalDistRandomList.getValue(loopPos);
        }
        return 0;
    }

    public double nextNormal(double sigma) {
        if (!randomOn) {
            return 0;
        }

        double p = 0;
        double px;//=0.0;
        double z;//=0.0;
        double x;//=0.0;
        double t;//=0.0;
        double x2;//=0.0;

        do {
            p = nextUniform();
            if (p < 0.5) {
                px = 1 - p;
            } else {
                px = p;
            }

            x = sigma;
            x2 = sigma / 2.0;
            // 10 Iterations to find the value         
            for (int i = 0; i < 10; i++) {
                t = 1.0 / (1.0 + 0.2316419 * x);
                z = 1.0 - (1.0 / (Math.sqrt(2.0 * Math.PI))) * Math.exp(-x * x / 2.0)
                        * (a1 * t + a2 * t * t + a3 * t * t * t + a4 * t * t * t * t + a5 * t * t * t * t * t);
                if (z < px) {
                    x = x + x2;
                } else {
                    x = x - x2;
                }
                x2 = x2 / 2.0;
            }
        } while (x > sigma);
        if (p < 0.5) {
            x = -x;
        }

        return x;
    }

    @Override
    public RandomNumber clone() {
        RandomNumber c = new RandomNumber(randomType);
        c.setLoopIndex(loopPos);
        return c;
    }
}
