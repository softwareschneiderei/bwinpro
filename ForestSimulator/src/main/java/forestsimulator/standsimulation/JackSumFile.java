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
package forestsimulator.standsimulation;

import java.io.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import jonelo.jacksum.*;
import jonelo.jacksum.algorithm.*;

/**
 *
 * @author nagel
 */
public class JackSumFile {

    public String getSum(String filename) {
        try {
            // select an algorithm (md5 in this case)
            AbstractChecksum checksum = JacksumAPI.getChecksumInstance("sha1");
            // On some systems you get a better performance for particular
            // algorithms if you select an alternate algorithm (see also option -A)
            // checksum = JacksumAPI.getChecksumInstance("md5", true);
            // updates the checksum with the content of a file
            checksum.readFile(filename);
            System.out.println(checksum);
            return checksum.toString();
        } catch (NoSuchAlgorithmException nsae) {
            // algorithm doesn't exist
        } catch (IOException ex) {
            Logger.getLogger(JackSumFile.class.getName()).log(Level.SEVERE, "Problem reading checksum file", ex);
        }
        return "";
    }
}
