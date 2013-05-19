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
import org.primefaces.event.DateSelectEvent;
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsComplaint;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ComplaintManager;
import org.smp.realerp.pojo.replyMail;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsComplaintFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "complaintBean")
@ViewScoped
public class ComplaintBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    ComplaintManager complaintManager;
    @EJB
    FbsComplaintFacade fbsComplaintFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    FbsComplaint fbsComplaint = new FbsComplaint();
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsComplaint> fbsComplaintList = new ArrayList<FbsComplaint>();
    List<FbsComplaint> complaintList = new ArrayList<FbsComplaint>();
    static FbsComplaint delFbsComplaint = new FbsComplaint();
    FbsComplaint editComplaint = new FbsComplaint();
    String complaints = "";
    FbsProject fbsProject = new FbsProject();
    boolean renderDelete = true;
    String complainId = "Complaint Id";
    String applicantName = "Applicant Name";
    String status = "Select Status";
    Date fromDate;
    Date toDate;
    String subject = "";
    boolean renderDialog = false;
    boolean renderProject = true;
    String priority = "Select Priority";
    boolean renderDetails = false;
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    Integer searchType = 1;

    @PostConstruct
    public void populate() {
        System.out.println("inside populate.........");
        fbsComplaintList.clear();
        complaintList.clear();
        fbsProject = new FbsProject();
        status = "Select Status";
        applicantName = "";
        subject = "Subject";
        priority = "Select Priority";
        complainId = "";
        fromDate = null;
        toDate = null;
        fbsCompany = new FbsCompany();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            fbsProject = new FbsProject();
            fbsProjectList.clear();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();

        } else {
            fbsProject = new FbsProject();
            fbsProject = complaintManager.populateFbsProject(LoginBean.fbsProject.getProjId());
            renderProject = false;

        }
        complaintList.clear();
        complaintList = fbsComplaintFacade.findAll();
        fbsComplaintList.clear();
        fbsComplaintList.addAll(complaintList);
        filterComplaintList();
    }

    public void filterByProject() {
    }

    public void filterComplaintList() {
        System.out.println("inside filter........");
        if (fbsProject != null && fbsProject.getProjId() != null && fbsProject.getProjId() != 0) {
            fbsProject = complaintManager.populateFbsProject(fbsProject.getProjId());
        }
        int projectId;
        int companyId;
        String complaintstatus = "";
        String complaintPriority = "";
        Date complaintDate;
        fbsComplaintList.clear();
        for (int i = 0; i < complaintList.size(); i++) {
            projectId = complaintList.get(i).getFbsProject().getProjId();
            complaintstatus = complaintList.get(i).getStatus();
            complaintPriority = complaintList.get(i).getPriority();
            companyId = complaintList.get(i).getFbsProject().getFbsCompany().getCompanyId();
            complaintDate = complaintList.get(i).getComplaintDt();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)
                    && (status == null || status.equalsIgnoreCase("Select Status") || status.equalsIgnoreCase(complaintstatus))
                    && ((fromDate == null) || (complaintDate.after(fromDate)) || (complaintDate.equals(fromDate)))
                    && ((toDate == null) || (complaintDate.before(toDate)) || (complaintDate.equals(toDate)))
                    && (priority == null || priority.equalsIgnoreCase("Select Priority") || priority.equalsIgnoreCase(complaintPriority))) {

                fbsComplaintList.add(complaintList.get(i));
            }
        }
    }

    public List<FbsComplaint> populateComplaintListByComplaintId() {
        fbsComplaintList.clear();
        status = "Select Status";
        applicantName = "Applicant Name";
        subject = "Subject";
        priority = "Select Priority";
        fromDate = null;
        toDate = null;
        for (int i = 0; i < complaintList.size(); i++) {
            if ((complaintList.get(i).getComplaintId() != null && complaintList.get(i).getComplaintId() == (Integer.parseInt(complainId)))) {
                fbsComplaintList.add(complaintList.get(i));
            }
        }
        return fbsComplaintList;
    }

    public List<FbsComplaint> populateComplaintListByApplicantName() {
        fbsComplaintList.clear();
        status = "Select Status";
        complainId = "Complaint Id";
        subject = "Subject";
        priority = "Select Priority";
        fromDate = null;
        toDate = null;
        for (int i = 0; i < complaintList.size(); i++) {
            if ((complaintList.get(i).getName() != null && complaintList.get(i).getName().contains(applicantName))) {
                fbsComplaintList.add(complaintList.get(i));
            }
        }
        return fbsComplaintList;
    }

    public List<FbsComplaint> populateComplaintListBySubject() {
        fbsComplaintList.clear();
        status = "Select Status";
        complainId = "Complaint Id";
        applicantName = "Applicant Name";
        priority = "Select Priority";
        fromDate = null;
        toDate = null;
        for (int i = 0; i < complaintList.size(); i++) {
            if ((complaintList.get(i).getSubject() != null && complaintList.get(i).getSubject().contains(subject))) {
                fbsComplaintList.add(complaintList.get(i));
            }
        }
        return fbsComplaintList;
    }

    public void addComplaint() {
        fbsComplaint.setFbsProject(fbsProject);
        fbsComplaint.setRegisteredBy(LoginBean.fbsLogin.getUserId());
        fbsComplaint.setStatus("Open");
        complaintManager.addComplaint(fbsComplaint);
        ackComplaint();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Complaint Successfully Added", ""));
    }

    public void ackComplaint() {
        // replyMail reMail = new replyMail(fbsComplaint.getEmail(), fbsComplaint.getName(), fbsComplaint.getSubject(), fbsComplaint.getComplaint(),null, fbsComplaint.getComplaintId(), true, true);
        if (LoginBean.fbsLogin.getFbsCompany().getFbsMailSettingCollection().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Server Settings Not Found!", ""));
        } else {
            replyMail reMail = new replyMail(fbsComplaint, true, true);
            Thread thread = new Thread(reMail);
            thread.start();
        }
    }

    public void confirmDeleteComplaint(FbsComplaint fbsComplaint) {
        delFbsComplaint = new FbsComplaint();
        delFbsComplaint = fbsComplaint;
    }

    public void deleteComplaint() {
        System.out.println("inside bean delete method....");
        complaintManager.deleteComplaint(delFbsComplaint);
        delFbsComplaint = new FbsComplaint();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Complaint Deleted", ""));
        populate();
    }

    public void renderDeleteButton() {
        renderDelete = false;

    }

    public void showComplaintDetail(FbsComplaint fbsComplaint) {
        System.out.println("showComplaint...");
        renderDialog = true;
        editComplaint = new FbsComplaint();
        editComplaint = fbsComplaint;
    }

    public void editComplaintdialog() {
        System.out.println("inside edit complaint dialog");
        fbsComplaintFacade.edit(editComplaint);
        renderDialog = false;
        editComplaint = new FbsComplaint();
        populate();
    }

    public void redirectComplaintDetails(FbsComplaint fbsComplaint) throws IOException {
        System.out.println("fbscomplaint is............" + fbsComplaint.getComplaintId());
        if (fbsComplaint.getComplaintId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Complaint Details not available", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Complaint/complaintDetails.xhtml?complaintId=" + fbsComplaint.getComplaintId());
        }
    }

    public void detail(FbsComplaint complaint) throws IOException {
        if (complaint.getComplaintId() == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Complaint Details not available", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Complaint/complaintDetails.xhtml?complaintId=" + complaint.getComplaintId());
        }
    }

//    public void renderSearchingdetails() {
//
//        if (searchType == null || searchType.equals("1")) {
//            renderDetails = false;
//        } else {
//            renderDetails = true;
//        }
//    }
    public void renderSearchingdetails() {
        if ((searchType != null) && (searchType == 1)) {
            renderDetails = false;
        } else if ((searchType != null) && (searchType == 2)) {
            renderDetails = true;
        }
        //reset();
    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        fromDate = event.getDate();
        filterComplaintList();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        toDate = event.getDate();
        filterComplaintList();

    }

    public List<FbsComplaint> getFbsComplaintList() {
        return fbsComplaintList;
    }

    public void setFbsComplaintList(List<FbsComplaint> fbsComplaintList) {
        this.fbsComplaintList = fbsComplaintList;
    }

    public FbsComplaint getFbsComplaint() {
        return fbsComplaint;
    }

    public void setFbsComplaint(FbsComplaint fbsComplaint) {
        this.fbsComplaint = fbsComplaint;
    }

    public static FbsComplaint getDelFbsComplaint() {
        return delFbsComplaint;
    }

    public static void setDelFbsComplaint(FbsComplaint delFbsComplaint) {
        ComplaintBean.delFbsComplaint = delFbsComplaint;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public boolean isRenderDelete() {
        return renderDelete;
    }

    public void setRenderDelete(boolean renderDelete) {
        this.renderDelete = renderDelete;
    }

    public String getComplainId() {
        return complainId;
    }

    public void setComplainId(String complainId) {
        this.complainId = complainId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public List<FbsComplaint> getComplaintList() {
        return complaintList;
    }

    public void setComplaintList(List<FbsComplaint> complaintList) {
        this.complaintList = complaintList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public FbsComplaint getEditComplaint() {
        return editComplaint;
    }

    public void setEditComplaint(FbsComplaint editComplaint) {
        this.editComplaint = editComplaint;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean isRenderDetails() {
        return renderDetails;
    }

    public void setRenderDetails(boolean renderDetails) {
        this.renderDetails = renderDetails;
    }

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }
}
