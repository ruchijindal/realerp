/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsDiscount;

/**
 *
 * @author smp
 */
@Stateless
public class FbsDiscountFacade extends AbstractFacade<FbsDiscount> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsDiscountFacade() {
        super(FbsDiscount.class);
    }

}
