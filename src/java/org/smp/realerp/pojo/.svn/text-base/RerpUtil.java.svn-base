/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.pojo;

import java.text.DecimalFormat;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class RerpUtil {

    DecimalFormat formatter = new DecimalFormat("##,###");

    public String indianFormat(Double n) {
        boolean negFlag=n <0?true:false;

        n = Math.abs(n);


        String returnValue = "";

        if (n.intValue() > 9999) {
            formatter.applyPattern("#,##");

            returnValue = formatter.format((int) (n.intValue() / 1000)) + ",";
            formatter.applyPattern("#,###");

            returnValue += formatter.format(n.intValue() - (int) (n.intValue() / 1000) * 1000);
            if ((n.intValue() - (int) (n.intValue() / 1000) * 1000) == 0) {
                returnValue += "00";
            }

        } else if (n.intValue() >= 1000 && n.intValue() <= 9999) {
            formatter.applyPattern("#,###");

            returnValue = formatter.format(n.intValue());

        } else {


            returnValue += n.intValue();

        }

        if(negFlag == true)
        return "-"+returnValue;
        else
            return returnValue;

    }
}
