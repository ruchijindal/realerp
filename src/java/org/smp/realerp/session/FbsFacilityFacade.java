/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsFacility;

/**
 *
 * @author smp
 */
@Stateless
public class FbsFacilityFacade extends AbstractFacade<FbsFacility> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsFacilityFacade() {
        super(FbsFacility.class);
    }

}
