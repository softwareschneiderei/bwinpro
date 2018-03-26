/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nwfva.silviculture.base;

import java.util.ResourceBundle;

/**
 *
 * @author nagel
 */
public class RBHolder {

    private static ResourceBundle rb = null;

    public static void setResourceBundle(ResourceBundle rb) {
        RBHolder.rb = rb;
    }

    public static ResourceBundle getResourceBundle() {
        return RBHolder.rb;
    }
}
