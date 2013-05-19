/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsBooking;

/**
 *
 * @author smp
 */
@Stateless
public class FbsBookingFacade extends AbstractFacade<FbsBooking> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsBookingFacade() {
        super(FbsBooking.class);
    }

}
