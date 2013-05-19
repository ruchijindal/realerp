/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsBrokerCat;

/**
 *
 * @author smp
 */
@Stateless
public class FbsBrokerCatFacade extends AbstractFacade<FbsBrokerCat> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsBrokerCatFacade() {
        super(FbsBrokerCat.class);
    }

}
