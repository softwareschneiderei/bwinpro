/*
* @(#) EffectiveRootingDepth.java
*  (c) 2002-2009 Juergen Nagel, Northwest German Research Station,
*      Grätzelstr.2, 37079 Göttingen, Germany
*      E-Mail: Juergen.Nagel@nw-fva.de
*
*  This program is free software; you can redistribute it and/or
*  modify it under the terms of the GNU General Public License
*  as published by the Free Software Foundation.
*
*  This program is distributed in the hope that it will be useful,
*  but WITHOUT ANY WARRANTY; without even the implied warranty of
*  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*  GNU General Public License for more details.
 */
package forestsimulator.roots;

/**
 * TreeGrOSS : class EffectiveRootingDepth estimates the rooting depth of forest
 * stands according to paper of T. Czajkowski, B. Ahrends, A. Bolte /
 * Landbauforschung - vTI Agriculture and Forestry Research 2 2009 (59)87-94 In
 * this estimation linear functions provided by the authors are used.
 *
 *
 * http://www.nw-fva.de
 *
 * @version 11-NOV-2009
 * @author	Juergen Nagel
 */
public class EffectiveRootingDepth {

    public String preclevels[] = null;
    public String speciestypes[] = null;

    public EffectiveRootingDepth() {
        loadPreclevels();
    }

    public double getEffectiveRootingDepth(int stype, int plevel, int code, int age) {
        double depth = -99;
        int sptype = -9;
        if (code == 211) {
            sptype = 0;
        }
        if (code < 112) {
            sptype = 0;
        }
        if (code == 611) {
            sptype = 0;
        }
        if (code == 711) {
            sptype = 0;
        }
        if (code == 511) {
            sptype = 1;
        }
        if (sptype > -1) {
            switch (stype) {
                case 0: {
// Sand
                    if (plevel == 0 && sptype == 0) {
                        depth = 8.91 + 0.16 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 0) {
                        depth = 8.2 + 0.14 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 0) {
                        depth = 7.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 0) {
                        depth = 6.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 0 && sptype == 1) {
                        depth = 6.24 + 0.11 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 1) {
                        depth = 5.74 + 0.10 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 1) {
                        depth = 5.24 + 0.09 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 1) {
                        depth = 4.54 + 0.09 * age;
                        break;
                    }
                }
// Lehm
                case 1: {
                    if (plevel == 0 && sptype == 0) {
                        depth = 9.91 + 0.16 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 0) {
                        depth = 9.2 + 0.14 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 0) {
                        depth = 8.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 0) {
                        depth = 7.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 0 && sptype == 1) {
                        depth = 6.94 + 0.11 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 1) {
                        depth = 6.44 + 0.10 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 1) {
                        depth = 5.94 + 0.09 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 1) {
                        depth = 5.24 + 0.09 * age;
                        break;
                    }
                }
// Schluff
                case 2: {
                    if (plevel == 0 && sptype == 0) {
                        depth = 13.91 + 0.16 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 0) {
                        depth = 13.2 + 0.14 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 0) {
                        depth = 12.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 0) {
                        depth = 11.48 + 0.13 * age;
                        break;
                    }
                    if (plevel == 0 && sptype == 1) {
                        depth = 9.74 + 0.11 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 1) {
                        depth = 9.24 + 0.10 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 1) {
                        depth = 8.74 + 0.09 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 1) {
                        depth = 8.04 + 0.09 * age;
                        break;
                    }
                }
// Ton
                case 3: {
                    if (plevel == 0 && sptype == 0) {
                        depth = 8.81 + 0.09 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 0) {
                        depth = 8.45 + 0.08 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 0) {
                        depth = 8.10 + 0.07 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 0) {
                        depth = 7.74 + 0.06 * age;
                        break;
                    }
                    if (plevel == 0 && sptype == 1) {
                        depth = 6.23 + 0.06 * age;
                        break;
                    }
                    if (plevel == 1 && sptype == 1) {
                        depth = 5.92 + 0.06 * age;
                        break;
                    }
                    if (plevel == 2 && sptype == 1) {
                        depth = 5.73 + 0.05 * age;
                        break;
                    }
                    if (plevel == 3 && sptype == 1) {
                        depth = 5.42 + 0.04 * age;
                        break;
                    }
                }
// Gestein
                case 4: {
                    if (sptype == 0) {
                        depth = 6.68 + 0.08 * age;
                        break;
                    }
                    if (sptype == 1) {
                        depth = 5.74 + 0.06 * age;
                        break;
                    }
                }
            }

        }
        return depth;
    }

    /**
     * Precipitation level
     */
    private void loadPreclevels() {
        preclevels = new String[4];
        preclevels[0] = new String(" > 750 mm");
        preclevels[1] = new String("750 - 688 mm");
        preclevels[2] = new String("687 - 625 mm");
        preclevels[3] = new String(" < 625mm");
    }

    public double calculateRootingDepthDirect(int si, int pr) {
        double depth = 0.0;
        switch (si) {
            case 0: {
                depth = 10;
                break;
            }
            case 1:
                depth = 20;
                break;
            case 2:
                depth = 30;
                break;
        }
        return depth;
    }

    public String getPrecipitationLevelName(int n) {
        return preclevels[n];
    }
}
