/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.smp.realerp.entity.FbsComplaint;
import org.smp.realerp.entity.FbsComplaintReply;
import org.smp.realerp.manager.ComplaintManager;
import org.smp.realerp.pojo.replyMail;
import org.smp.realerp.session.FbsComplaintFacade;
import org.smp.realerp.session.FbsComplaintReplyFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "complaintDetailBean")
@ViewScoped
public class ComplaintDetailBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    ComplaintManager complaintManager;
    @EJB
    FbsComplaintFacade fbsComplaintFacade;
    @EJB
    FbsComplaintReplyFacade fbsComplaintReplyFacade;
    FbsComplaint fbsComplaint = new FbsComplaint();
    FbsComplaint complaint = new FbsComplaint();
    String email = "";
    int complaintId;
    FbsComplaintReply fbsComplaintReply = new FbsComplaintReply();
    List<FbsComplaintReply> fbsComplaintReplyList = new ArrayList<FbsComplaintReply>();
    String renderType = "Reply";

    @PostConstruct
    public void populate() {
        System.out.println("inside detail bean...........");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        int complaintId = 0;
        if (request.getParameter("complaintId") != null) {
            complaintId = Integer.parseInt(request.getParameter("complaintId"));
        }
        fbsComplaint = fbsComplaintFacade.find(complaintId);
        fbsComplaintReplyList = (List<FbsComplaintReply>) fbsComplaint.getFbsComplaintReplyCollection();
        for (FbsComplaintReply f : fbsComplaintReplyList) {
            System.out.println(f.getComplaintReplyId() + " fbscomplaint is......." + f.getFbsComplaint().getComplaintId());
        }

    }

    public void renderModeDetails() {
    }

    public void replyComplaint() throws IOException {
        if (LoginBean.fbsLogin.getFbsCompany().getFbsMailSettingCollection().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Server Settings Not Found!", ""));
        } else {
            fbsComplaintReply.setFbsComplaint(fbsComplaint);
            fbsComplaintReply.setMessageDate(new Date());
            // replyMail reMail = new replyMail(fbsComplaint.getEmail(), fbsComplaint.getName(), fbsComplaint.getSubject(), fbsComplaint.getComplaint(), fbsComplaintReply.getMessage(), fbsComplaint.getComplaintId(), true, false);
            if (!fbsComplaintReply.getStatus().equalsIgnoreCase("0")) {
                System.out.println("condition true");
                fbsComplaint.setStatus(fbsComplaintReply.getStatus());
                fbsComplaintFacade.edit(fbsComplaint);
            } else {
                System.out.println("condition False");
                fbsComplaint.setStatus("Open");
                fbsComplaintReply.setStatus("Open");
                fbsComplaintFacade.edit(fbsComplaint);
            }
            if (renderType.equals("Reply")) {
                fbsComplaintReply.setType("Reply");
            } else {
                fbsComplaintReply.setType("Complaint");
            }

            fbsComplaintReplyFacade.create(fbsComplaintReply);
            replyMail reMail = new replyMail(fbsComplaint, fbsComplaintReply, true, false);
            Thread thread = new Thread(reMail);
            thread.start();
            fbsComplaintReply = new FbsComplaintReply();
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Complaint/complaintDetails.xhtml?complaintId=" + fbsComplaint.getComplaintId());
            // fbsComplaint=new FbsComplaint();
            populate();
        }
    }

    public FbsComplaint getFbsComplaint() {
        return fbsComplaint;
    }

    public void setFbsComplaint(FbsComplaint fbsComplaint) {
        this.fbsComplaint = fbsComplaint;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FbsComplaintReply getFbsComplaintReply() {
        return fbsComplaintReply;
    }

    public void setFbsComplaintReply(FbsComplaintReply fbsComplaintReply) {
        this.fbsComplaintReply = fbsComplaintReply;
    }

    public List<FbsComplaintReply> getFbsComplaintReplyList() {
        return fbsComplaintReplyList;
    }

    public void setFbsComplaintReplyList(List<FbsComplaintReply> fbsComplaintReplyList) {
        this.fbsComplaintReplyList = fbsComplaintReplyList;
    }

    public String getRenderType() {
        return renderType;
    }

    public void setRenderType(String renderType) {
        this.renderType = renderType;
    }
}
