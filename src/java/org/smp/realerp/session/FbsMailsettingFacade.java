/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsMailsetting;

/**
 *
 * @author smp
 */
@Stateless
public class FbsMailsettingFacade extends AbstractFacade<FbsMailsetting> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsMailsettingFacade() {
        super(FbsMailsetting.class);
    }
    
}
