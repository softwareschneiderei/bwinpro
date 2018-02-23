/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package treegross.base;

import treegross.random.RandomNumber;

/**
 *
 * @author jhansen
 */
public class ScaleManager {

    public final static int SCALE_AUTO = 0;
    public final static int SCALE_FIXED = 1;
    public final static int SCALE_NOT = 2;

    private int nThreads;
    private int nt2;
    private int scaleMethod;
    private final Stand st;
    /*
     autoThreshold: Min. number of trees to force usage of parallel competition updating.
     Used in SCALE_AUTO mode only.
     */
    private int autoThreshold = 500;

    /**
     * Creates a new ScaleManager
     *
     * @param st the Stand
     * @param scaleMethod the method to use
     * @param scale the number of Threads to start parallel
     * @param autoTheshold Min. number of trees to force usage of parallel
     * competition updating in SCALE_AUTO
     */
    public ScaleManager(Stand st, int scaleMethod, int scale, int autoTheshold) {
        this.st = st;
        setAutoTheshold(autoTheshold);
        setScaleMethod(scaleMethod);
        setScale(scale);
    }

    public ScaleManager(Stand st) {
        this(st, SCALE_AUTO, Runtime.getRuntime().availableProcessors(), 500);
    }

    public final void setScale(int scale) {
        nThreads = scale;
        if (nThreads < 1 || nThreads > 32) {
            nThreads = 1;
        }
        nt2 = nThreads * 3;
    }

    public final void setAutoTheshold(int t) {
        if (t >= 0) {
            autoThreshold = t;
        } else {
            autoThreshold = 0;
        }
    }

    public final void setScaleMethod(int m) {
        this.scaleMethod = m;
        if (this.scaleMethod < 0 || this.scaleMethod > 2) {
            this.scaleMethod = SCALE_NOT;
        }        
    }
    
    public int getScaleMethod(){
        return scaleMethod;
    }

    public void updateCompetition() {
        int localScaleMethod = scaleMethod;
        //long msS = System.currentTimeMillis();
        if (nThreads == 1 || st.nTreesAlive < nt2) {
            localScaleMethod = SCALE_NOT;
        }
        //System.out.println("updating mechanism: "+scaleMethod+" "+nThreads);
        switch (localScaleMethod) {
            case SCALE_AUTO:
                if (st.nTreesAlive >= autoThreshold) {
                    updateCompetitionFixScale();
                } else {
                    updateCompetitionNoScale();
                }
                break;
            case SCALE_FIXED:
                updateCompetitionFixScale();
                break;
            case SCALE_NOT:
                updateCompetitionNoScale();
                break;
        }
        //System.out.println(st.ntrees+":"+(System.currentTimeMillis()-msS));
    }
    
    public void updateCompetitionMortality(int numberOfCandidates, int[] treeNo) {        
        //long msS = System.currentTimeMillis();
        int localScaleMethod = scaleMethod;
        if (nThreads == 1 || numberOfCandidates < nt2) {
            localScaleMethod = SCALE_NOT;
        }
        //System.out.println("updating mechanism: "+scaleMethod+" "+nThreads);
        //System.out.println("ucm "+numberOfCandidates+" "+localScaleMethod);
        switch (localScaleMethod) {
            case SCALE_AUTO:
                if (st.nTreesAlive >= autoThreshold) {
                    updateCompetitionMortalityFixScale(numberOfCandidates, treeNo);
                } else {
                    updateCompetitionMortalityNoScale(numberOfCandidates, treeNo);
                }
                break;
            case SCALE_FIXED:
                updateCompetitionMortalityFixScale(numberOfCandidates, treeNo);
                break;
            case SCALE_NOT:
                updateCompetitionMortalityNoScale(numberOfCandidates, treeNo);
                break;
        }
        //System.out.println(st.ntrees+":"+(System.currentTimeMillis()-msS));
    }

    public void updateCrown() {
        //long msS = System.currentTimeMillis();
        int localScaleMethod = scaleMethod;
        if (nThreads == 1 || st.nTreesAlive < nt2) {
            localScaleMethod = SCALE_NOT;
        }
        //System.out.println("updating mechanism: "+scaleMethod+" "+nThreads);
        switch (localScaleMethod) {
            case SCALE_AUTO:
                if (st.nTreesAlive >= autoThreshold) {
                    updateCrownFixScale();
                } else {
                    updateCrownNoScale();
                }
                break;
            case SCALE_FIXED:
                updateCrownFixScale();
                break;
            case SCALE_NOT:
                updateCrownNoScale();
                break;
        }
        //System.out.println(st.ntrees+":"+(System.currentTimeMillis()-msS));
    }

    void growTrees(int period, RandomNumber random) {
        //long msS = System.currentTimeMillis();
        int localScaleMethod = scaleMethod;
        if (nThreads == 1 || st.nTreesAlive < nt2) {
            localScaleMethod = SCALE_NOT;
        }
        //System.out.println("updating mechanism: "+scaleMethod+" "+nThreads);
        switch (localScaleMethod) {
            case SCALE_AUTO:
                if (st.nTreesAlive >= autoThreshold) {
                    growFixScale(period, random);
                } else {
                    growNoScale(period, random);
                }
                break;
            case SCALE_FIXED:
                growFixScale(period, random);
                break;
            case SCALE_NOT:
                growNoScale(period, random);
                break;
        }
        //System.out.println(st.ntrees+":"+(System.currentTimeMillis()-msS));
    }
    
    private void updateCompetitionMortalityNoScale(int numberOfCandidates, int[] treeNo) {
        for (int i = 0; i < numberOfCandidates; i++) {            
            st.tr[treeNo[i]].updateCompetition();
        }
    }
    
    private void updateCompetitionMortalityFixScale(int numberOfCandidates, int[] treeNo) {
        UpdateThreadCompetitionMortality[] ut = new UpdateThreadCompetitionMortality[nThreads];
        int avgSize = numberOfCandidates / nThreads;
        int rest = numberOfCandidates % nThreads;
        int s, e;
        e = rest + avgSize;
        s = 0;
        for (int i = 0; i < ut.length; i++) {
            //System.out.println("thread "+i+": "+st.ntrees+" se:"+s+" / "+e);
            ut[i] = new UpdateThreadCompetitionMortality(st, s, e, treeNo);
            ut[i].start();
            s = e;
            e += avgSize;
        }
        for (UpdateThreadCompetitionMortality ut1 : ut) {
            try {
                ut1.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private void updateCompetitionNoScale() {
        for (int i = 0; i < st.ntrees && !st.stop; i++) {
            if (st.tr[i].out < 0) {
                st.tr[i].updateCompetition();
            }
        }
    }

    private void updateCompetitionFixScale() {
        UpdateThreadCompetition[] ut = new UpdateThreadCompetition[nThreads];
        int avgSize = st.ntrees / nThreads;
        int rest = st.ntrees % nThreads;
        int s, e;
        e = rest + avgSize;
        s = 0;
        for (int i = 0; i < ut.length; i++) {
            //System.out.println("thread "+i+": "+st.ntrees+" se:"+s+" / "+e);
            ut[i] = new UpdateThreadCompetition(st, s, e);
            ut[i].start();
            s = e;
            e += avgSize;
        }
        for (UpdateThreadCompetition ut1 : ut) {
            try {
                ut1.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private void updateCrownNoScale() {
        for (int i = 0; i < st.ntrees && !st.stop; i++) {
            if (st.tr[i].out < 0) {
                st.tr[i].updateCrown();
            }
        }
    }

    private void updateCrownFixScale() {
        UpdateThreadCrown[] ut = new UpdateThreadCrown[nThreads];
        int avgSize = st.ntrees / nThreads;
        int rest = st.ntrees % nThreads;
        int s, e;
        e = rest + avgSize;
        s = 0;
        for (int i = 0; i < ut.length; i++) {
            //System.out.println("thread "+i+": "+st.ntrees+" se:"+s+" / "+e);
            ut[i] = new UpdateThreadCrown(st, s, e);
            ut[i].start();
            s = e;
            e += avgSize;
        }
        for (UpdateThreadCrown ut1 : ut) {
            try {
                ut1.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private void growNoScale(int period, RandomNumber random) {
        for (int i = 0; i < st.ntrees && !st.stop; i++) {
            if (st.tr[i].out < 0) {
                st.tr[i].grow(period, random);
            }
        }
    }

    private void growFixScale(int period, RandomNumber random) {
        GrowThread[] ut = new GrowThread[nThreads];
        int avgSize = st.ntrees / nThreads;
        int rest = st.ntrees % nThreads;
        int s, e;
        e = rest + avgSize;
        s = 0;
        for (int i = 0; i < ut.length; i++) {
            //System.out.println("thread "+i+": "+st.ntrees+" se:"+s+" / "+e);
            ut[i] = new GrowThread(st, s, e, period, random);
            ut[i].start();
            s = e;
            e += avgSize;
        }
        for (GrowThread ut1 : ut) {
            try {
                ut1.join();
            } catch (InterruptedException ex) {
            }
        }
    }

    private class UpdateThreadCompetition extends Thread {

        private final Stand st;
        private final int s, e;

        public UpdateThreadCompetition(Stand st, int startIndex, int endIndex) {
            this.st = st;
            s = startIndex;
            e = endIndex;
        }

        @Override
        public void run() {
            for (int i = s; i < e && !st.stop; i++) {
                if (st.tr[i].out < 0) {
                    st.tr[i].updateCompetition();
                }
            }
        }
    }
    
    private class UpdateThreadCompetitionMortality extends Thread {

        private final Stand st;
        private final int s, e;
        private final int[] treeNo;

        public UpdateThreadCompetitionMortality(Stand st, int startIndex, int endIndex, int[] treeNo) {
            this.st = st;
            s = startIndex;
            e = endIndex;
            this.treeNo = treeNo;
        }

        @Override
        public void run() {
            for (int i = s; i < e && !st.stop; i++) {
                st.tr[treeNo[i]].updateCompetition();            
            }
        }
    }

    private class UpdateThreadCrown extends Thread {

        private final Stand st;
        private final int s, e;

        public UpdateThreadCrown(Stand st, int startIndex, int endIndex) {
            this.st = st;
            s = startIndex;
            e = endIndex;
        }

        @Override
        public void run() {
            for (int i = s; i < e && !st.stop; i++) {
                if (st.tr[i].out < 0) {
                    st.tr[i].updateCrown();
                }
            }
        }
    }

    private class GrowThread extends Thread {

        private final Stand st;
        private final int s, e;
        private final int period;
        private final RandomNumber random;

        public GrowThread(Stand st, int startIndex, int endIndex, int period, RandomNumber random) {
            this.st = st;
            s = startIndex;
            e = endIndex;
            this.period = period;
            this.random = random;
        }

        @Override
        public void run() {
            for (int i = s; i < e && !st.stop; i++) {
                if (st.tr[i].out < 0) {
                    st.tr[i].grow(period, random);
                }
            }
        }
    }
}
