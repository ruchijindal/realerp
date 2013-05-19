/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsFlatType;

/**
 *
 * @author smp
 */
@Stateless
public class FbsFlatTypeFacade extends AbstractFacade<FbsFlatType> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsFlatTypeFacade() {
        super(FbsFlatType.class);
    }

}
