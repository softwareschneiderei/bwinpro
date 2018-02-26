/* http://www.nw-fva.de
 Version 07-11-2008

 (c) 2002 Juergen Nagel, Northwest German Forest Research Station, 
 Grätzelstr.2, 37079 Göttingen, Germany
 E-Mail: Juergen.Nagel@nw-fva.de
 
 This program is free software; you can redistribute it and/or
 modify it under the terms of the GNU General Public License
 as published by the Free Software Foundation.

 This program is distributed in the hope that it will be useful,
 but WITHOUT  WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.
 */
/**
 * TreeGrOSS : class heightcurve calculates a diameter - height regression.
 * There are 6 different curve types, see NAGEL, J. (1999): Konzeptionelle
 * Überlegungen zum schrittweisen Aufbau eines waldwachstumskundlichen
 * Simulationssystems für Nordwestdeutschland. Schriften aus der Forstlichen
 * Fakultät der Universität Göttingen und der Nieders. Forstl. Versuchsanstalt,
 * Band 128, J.D. Sauerländer's Verlag, Frankfurt a.M., S.122
 *
 * http://treegross.sourceforge.net
 *
 * @version 2.0 30-NOV-2004
 * @author	Juergen Nagel
 */
package treegross.base;

public class HeightCurve {

    int n;
    double sb[] = new double[6];
    double xbar[] = new double[6];
    double std[] = new double[6];
    double d[] = new double[6];
    double t[] = new double[6];
    double b[] = new double[6];
    double bc[] = new double[6];
    int lw[] = new int[6];
    int mw[] = new int[6];
    int isave[] = new int[6];
    double r[] = new double[26];
    double rx[] = new double[26];
    double ry[] = new double[26];
    int ind, m, df, dfr, ndep;
    double x[] = new double[5000];
    //  int   df,dfr,ndep,i,m,k;
    double dmat;
    double ans[] = new double[12];

    int numberOfFunctions = 0;

    public void heightcurve() {
        n = 0; // number of data points
    }

    /**
     * add single trees to a height curve type
     * @param typ
     * @param anz
     * @param dd
     * @param hh
     */
    public void adddh(int typ, int anz, double dd, double hh) {
        n = n + 1;
        // Parabel
        if (typ == 0) {
            x[n] = hh;
            x[n + anz] = dd;
            x[n + 2 * anz] = dd * dd;
            ind = 2;
        }
        // Prodan
        if (typ == 1) {
            x[n] = 1.0 / (hh - 1.3);
            x[n + anz] = 1.0 / dd;
            x[n + 2 * anz] = 1.0 / (dd * dd);
            ind = 2;
        }
        // Petterson 3.0
        if (typ == 2) {
            x[n] = 1.0 / Math.exp((Math.log(hh - 1.3)) / 3.0);
            x[n + anz] = 1.0 / dd;
            ind = 1;
        }
        // Korsun
        if (typ == 3) {
            x[n] = Math.log(hh);
            x[n + anz] = Math.log(dd);
            x[n + 2 * anz] = Math.log(dd) * Math.log(dd);
            ind = 2;
        }
        // logarithmisch
        if (typ == 4) {
            x[n] = hh;
            x[n + anz] = Math.log(dd);
            ind = 1;
        }
        if (typ == 5) {
            x[n] = Math.log(hh);
            x[n + anz] = Math.log(dd);
            x[n + 2 * anz] = dd;
            ind = 2;
        }
    }

    //

    public int getNumberOfFunctions() {
        getHeightCurveName(0);
        return numberOfFunctions;
    }

    public String getHeightCurveName(int number) {
        numberOfFunctions = 6;
        String name = "nicht gefunden";
        if (number == 0) {
            name = "0=Parabolic";
        }
        if (number == 1) {
            name = "1=Prodan";
        }
        if (number == 2) {
            name = "2=Petterson 3.0";
        }
        if (number == 3) {
            name = "3=Korsun";
        }
        if (number == 4) {
            name = "4=logarithmic";
        }
        if (number == 5) {
            name = "5=Freese";
        }
        return name;
    }

    public void start() {
        int i;
        for (i = 0; i <= 25; i++) {
            ry[i] = 0.0;
            rx[i] = 0.0;
            r[i] = 0.0;
        }
        for (i = 0; i <= 5; i++) {
            xbar[i] = 0.0;
            std[i] = 0.0;
            d[i] = 0.0;
            t[i] = 0.0;
            b[i] = 0.0;
            bc[i] = 0.0;
            isave[i] = 0;
        }
        ndep = 1;
        isave[1] = 2;
        isave[2] = 3;
        m = ind + 1;

        this.corre();
        this.order(ind);
        this.minV(ind);
        this.mulTre(ind);

    }
// returns the number of data points	

    int getn() {
        return n;
    }
// returns constant	

    public double getb0() {
        return ans[1];
    }
// returns 1st coefficient	

    public double getb1() {
        return bc[1];
    }
// returns 2nd coefficient	

    public double getb2() {
        return bc[2];
    }

// Return Height Kurve value, type= type of height kurve	
    public double hwert(int typ, double dk) {
        double hk = 0.0;
        if (dk > 0.0) {
            if (typ == 0) {
                hk = ans[1] + (bc[1] + bc[2] * dk) * dk;
            }  //Parabel
            if (typ == 1) {
                hk = 1.3 + dk * dk / (bc[2] + (bc[1] + ans[1] * dk) * dk);
            } //Prodan type
            if (typ == 2) {
                hk = 1.3 + Math.exp(Math.log(1.0 / (ans[1] + bc[1] * (1.0 / dk))) * 3.0);
            } //Petterson 3.0 
            if (typ == 3) {
                hk = Math.exp(ans[1] + bc[1] * Math.log(dk) + bc[2] * Math.log(dk) * Math.log(dk));
            } // Korsun type
            if (typ == 4) {
                hk = ans[1] + bc[1] * Math.log(dk);
            } // half logarithmic
            if (typ == 5) {
                hk = Math.exp(ans[1] + bc[1] * Math.log(dk) + bc[2] * dk);
            } //Freese type
        }
        if (hk < 1.30) {
            hk = 1.30;
        }
        //System.out.println("type: "+typ+" "+Arrays.toString(ans)+" "+Arrays.toString(bc)+" "+dk+ "-> hk: "+hk);
        return hk;
    }
// Return Height Kurve value, type= type of height kurve	

    public double getHeight(int typ, double dk, double b0, double b1, double b2) {
        double hk = 0.0;
        if (dk > 0.0) {
            if (typ == 0) {
                hk = b0 + (b1 + b2 * dk) * dk;
            }  //Parabel
            if (typ == 1) {
                hk = 1.3 + dk * dk / (b2 + (b1 + b0 * dk) * dk);
            } //Prodan type
            if (typ == 2) {
                hk = 1.3 + Math.exp(Math.log(1.0 / (b0 + b1 * (1.0 / dk))) * 3.0);
            } //Petterson 3.0 
            if (typ == 3) {
                hk = Math.exp(b0 + b1 * Math.log(dk) + b2 * Math.log(dk) * Math.log(dk));
            } // Korsun type
            if (typ == 4) {
                hk = b0 + b1 * Math.log(dk);
            } // half logarithmic
            if (typ == 5) {
                hk = Math.exp(b0 + b1 * Math.log(dk) + b2 * dk);
            } //Freese type
        }
        if (hk < 1.3) {
            hk = 1.3;
        }
        return hk;
    }

	// Klasse Corre
    void corre() {
        int k, j, i, l, jk;
        double fn;
        for (j = 1; j <= m; j++) {
            b[j] = 0.0;
            t[j] = 0.0;
        }
        k = (m * m + m) / 2;
        for (j = 1; j <= k; j++) {
            r[j] = 0.0;
        }
        fn = n;
        l = 0;
        for (j = 1; j <= m; j++) {
            for (i = 1; i <= n; i++) {
                l++;
                t[j] = t[j] + x[l];
            }
            xbar[j] = t[j];
            t[j] = t[j] / fn;
        }
        for (i = 1; i <= n; i++) {
            jk = 0;
            l = i - n;
            for (j = 1; j <= m; j++) {
                l = l + n;
                d[j] = x[l] - t[j];
                b[j] = b[j] + d[j];
            }
            for (j = 1; j <= m; j++) {
                for (k = 1; k <= j; k++) {
                    jk++;
                    r[jk] = r[jk] + d[j] * d[k];
                }
            }
        }
        jk = 0;
        for (j = 1; j <= m; j++) {
            xbar[j] = xbar[j] / fn;
            for (k = 1; k <= j; k++) {
                jk++;
                r[jk] = r[jk] - b[j] * b[k] / fn;
            }
        }
        jk = 0;
        for (j = 1; j <= m; j++) {
            jk = jk + j;
            std[j] = Math.sqrt(Math.abs(r[jk])); //stand fabs()
        }
        for (j = 1; j <= m; j++) {
            for (k = j; k <= m; k++) {
                jk = j + (k * k - k) / 2;
                l = m * (j - 1) + k;
                rx[l] = r[jk];
                l = m * (k - 1) + 1;
                rx[l] = r[jk];
                if (std[j] * std[k] == 0.0) {
                    r[jk] = 0.0;
                } else {
                    r[jk] = r[jk] / (std[j] * std[k]);
                }
            }
        }
        fn = Math.sqrt(fn - 1.0);
        for (j = 1; j <= m; j++) {
            std[j] = std[j] / fn;
        }
        l = -m;
        for (i = 1; i <= m; i++) {
            l = l + m + 1;
            b[i] = rx[l];
        }

    }

    //Ende Corre

    /**
     * Order
     */
    void order(int k) {
        //	System.out.println(" In Order"); 
        int mm, i, l2, l, j, l1;
        mm = 0;
        for (j = 1; j <= k; j++) {
            l2 = isave[j];
            if (ndep - l2 < 0) {
                l = ndep + (l2 * l2 - l2) / 2;
            } else {
                l = l2 + (ndep * ndep - ndep) / 2;
            }
            ry[j] = r[l];
            for (i = 1; i <= k; i++) {
                l1 = isave[i];
                if (l1 - l2 < 0) {
                    l = l1 + (l2 * l2 - l2) / 2;
                } else {
                    l = l2 + (l1 * l1 - l1) / 2;
                }
                mm++;
                rx[mm] = r[l];
            }
        }
        isave[k + 1] = ndep;

    }

    // Ende order

    /**
     * Minv
     */
    void minV(int n) {
        //	System.out.println(" In Minv"); 
        double biga, hold;
        int nk, k, kk, ki, jq, jr, jp, j, jk, ik, kj, i, ij, iz, ji;
        dmat = 1.0;
        nk = -n;
        for (k = 1; k <= n; k++) {
            nk = nk + n;
            lw[k] = k;
            mw[k] = k;
            kk = nk * k;
            biga = rx[kk];
            for (j = k; j <= n; j++) {
                iz = n * (j - 1);
                for (i = k; i <= n; i++) {
                    ij = iz + i;
                    if (Math.abs(biga) - Math.abs(rx[ij]) < 0.0) {
                        biga = rx[ij];
                        lw[k] = i;
                        mw[k] = j;
                    }
                }
            }
            j = lw[k];
            if (j - k > 0) {
                ki = k - n;
                for (i = 1; i <= n; i++) {
                    ki = ki + n;
                    hold = -rx[ki];
                    ji = ki - k + j;
                    rx[ki] = rx[ji];
                    rx[ji] = hold;
                }
            }
            i = mw[k];
            if (i - k > 0) {
                jp = n * (i - 1);
                for (j = 1; j <= n; j++) {
                    jk = nk + j;
                    ji = jp + j;
                    hold = -rx[jk];
                    rx[jk] = rx[ji];
                    rx[ji] = hold;
                }
            }
            if (biga == 0.0) {
                dmat = 0.0;
                break; //raus der Schleife und zum Ende
            }
            for (i = 1; i <= n; i++) {
                if (i - k != 0) {
                    ik = nk + i;
                    rx[ik] = rx[ik] / (-biga);
                }
            }
            for (i = 1; i <= n; i++) {
                ik = nk + i;
                hold = rx[ik];
                ij = i - n;
                for (j = 1; j <= n; j++) {
                    ij = ij + n;
                    if (i - k != 0) {
                        if (j - k != 0) {
                            kj = ij - i + k;
                            rx[ij] = hold * rx[kj] + rx[ij];
                        }
                    }
                }
            }
            kj = k - n;
            for (j = 1; j <= n; j++) {
                kj = kj + n;
                if (j - k != 0) {
                    rx[kj] = rx[kj] / biga;
                }
            }
            dmat = dmat * biga;
            rx[kk] = 1.0 / biga;
        }
        k = n;
        if (dmat == 0.0) {
            k = 0; //zum Ende
        }
        while (k > 1) {
            k = k - 1;
            //		if (k<=0) break ENDE;
            i = lw[k];
            if (i - k > 0) {
                jq = n * (k - 1);
                jr = n * (i - 1);
                for (j = 1; j <= n; j++) {
                    jk = jq + j;
                    hold = rx[jk];
                    ji = jr + j;
                    rx[jk] = -rx[ji];
                    rx[ji] = hold;
                }
            }
            j = mw[k];
            if (j - k > 0) {
                ki = k - n;
                for (i = 1; i <= n; i++) {
                    ki = ki + n;
                    hold = rx[ki];
                    ji = ki - k + j;
                    rx[ki] = -rx[ji];
                    rx[ji] = hold;
                }
            }
        }
		//		ji=ji; ENDE

    }

    // Ende Minv

    /**
     * Mulre
     */
    void mulTre(int k) {
        int i, j, mm, l1, l, fk;
        double sy, ssdr, ssar, rm, bo, fn;

        mm = k + 1;
        for (j = 1; j <= k; j++) {
            bc[j] = 0.0;
        }
        for (j = 1; j <= k; j++) {
            l1 = k * (j - 1);
            for (i = 1; i <= k; i++) {
                l = l1 + i;
                bc[j] = bc[j] + ry[i] * rx[l];
            }
        }
        rm = 0.0;
        bo = 0.0;
        l1 = isave[mm];
        for (i = 1; i <= k; i++) {
            rm = rm + bc[i] * ry[i];
            l = isave[i];
            if (std[l] != 0.0) {
                bc[i] = bc[i] * (std[l1] / std[l]);
            } else {
                bc[i] = 0.0;
            }
            bo = bo + bc[i] * xbar[l];
        }
        bo = xbar[l1] - bo;
        ssar = rm * b[l1];
        rm = Math.sqrt(Math.abs(rm));
        ssdr = b[l1] - ssar;
        fn = n - k - 1;
        sy = ssdr / fn;
        for (j = 1; j <= k; j++) {
            l1 = k * (j - 1) + j;
            l = isave[j];
            if (b[l] > 0.0) {
                sb[j] = Math.sqrt(Math.abs((rx[l1] / b[l]) * sy));
            } else {
                sb[j] = -999.9;
            }
            if (sb[j] != 0.0) {
                t[j] = bc[j] / sb[j];
            } else {
                sb[j] = -999.9;
            }
            t[j] = bc[j] / sb[j];
        }
        sy = Math.sqrt(Math.abs(sy));
        fk = k;
        ans[1] = bo;
        ans[2] = rm;
        ans[3] = sy;
        ans[4] = ssar;
        ans[5] = fk;
        ans[6] = ssar / fk;
        ans[7] = ssdr;
        ans[8] = fn;
        ans[9] = ssdr / fn;
        ans[10] = ans[6] / ans[9];
    }
}
