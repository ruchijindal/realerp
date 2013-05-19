/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsParking;

/**
 *
 * @author smp
 */
@Stateless
public class FbsParkingFacade extends AbstractFacade<FbsParking> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsParkingFacade() {
        super(FbsParking.class);
    }

}
