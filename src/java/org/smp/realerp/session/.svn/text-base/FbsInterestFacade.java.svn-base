/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsInterest;

/**
 *
 * @author smp
 */
@Stateless
public class FbsInterestFacade extends AbstractFacade<FbsInterest> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsInterestFacade() {
        super(FbsInterest.class);
    }

}
