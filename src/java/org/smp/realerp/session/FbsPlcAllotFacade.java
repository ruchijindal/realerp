/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsPlcAllot;

/**
 *
 * @author smp
 */
@Stateless
public class FbsPlcAllotFacade extends AbstractFacade<FbsPlcAllot> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsPlcAllotFacade() {
        super(FbsPlcAllot.class);
    }

}
