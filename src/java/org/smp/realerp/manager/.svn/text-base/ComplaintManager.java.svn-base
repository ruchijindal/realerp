/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.FbsComplaint;
import org.smp.realerp.entity.FbsComplaintReply;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.session.FbsComplaintFacade;
import org.smp.realerp.session.FbsComplaintReplyFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class ComplaintManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsComplaintFacade fbsComplaintFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsComplaintReplyFacade fbsComplaintReplyFacade;
    public void addComplaint(FbsComplaint fbsComplaint) {
        try {
            fbsComplaintFacade.create(fbsComplaint);
            FbsComplaintReply fbsComplaintReply = new FbsComplaintReply();
            fbsComplaintReply.setMessageDate(new Date());
            fbsComplaintReply.setMessage(fbsComplaint.getComplaint());
            fbsComplaintReply.setFbsComplaint(fbsComplaint);
            fbsComplaintReply.setType("Complaint");
            fbsComplaintReplyFacade.create(fbsComplaintReply);
        } catch (Exception ex) {
            System.out.println("Exception in add Complaint" + ex);
        }

    }

    public void deleteComplaint(FbsComplaint fbsComplaint) {
        try {
            fbsComplaintFacade.remove(fbsComplaint);
        } catch (Exception ex) {
            System.out.println("Exception in delete Complaint" + ex);
        }
    }

    public void editComplaint(FbsComplaint fbsComplaint) {
        try {
            fbsComplaintFacade.edit(fbsComplaint);
        } catch (Exception ex) {
            System.out.println("Exception in edit Complaint" + ex);
        }
    }

//    public void editStatus(FbsStatus fbsStatus) {
//        try{
//            fbsStatusFacade.edit(fbsStatus);
//        } catch (Exception ex) {
//            System.out.println("Exception in edit Status" + ex);
//        }
//    }
    public FbsProject populateFbsProject(int projectId) {
        return fbsProjectFacade.find(projectId);
    }
}
