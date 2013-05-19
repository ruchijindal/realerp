/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smp.realerp.session;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsProject;

/**
 *
 * @author smp
 */
@Stateless
public class FbsProjectFacade extends AbstractFacade<FbsProject> {
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public FbsProjectFacade() {
        super(FbsProject.class);
    }

}
