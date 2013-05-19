/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsPaymentFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "dashBoardBean")
@ViewScoped
public class DashBoardBean {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    RerpUtil utility;
    FbsCompany fbsCompany = new FbsCompany();
    FbsProject fbsProject = new FbsProject();
    FbsBooking fbsBooking = new FbsBooking();
    FbsComplaint fbsComplaint = new FbsComplaint();
    BookingListBean bookingListBean = new BookingListBean();
    List<FbsComplaint> fbsComplaintList = new ArrayList<FbsComplaint>();
    List<FbsComplaint> filterComplaintList = new ArrayList<FbsComplaint>();
    List<FbsComplaint> statusComplaintList = new ArrayList<FbsComplaint>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    List<FbsBooking> unAuthorizeBookingList = new ArrayList<FbsBooking>();
    List<FbsBooking> latestBookingList = new ArrayList<FbsBooking>();
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    List<FbsPayment> latestPaymentList = new ArrayList<FbsPayment>();
    List<FbsPayment> unAuthorizePaymentList = new ArrayList<FbsPayment>();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsBooking> latestProjectBookingList = new ArrayList<FbsBooking>();
    List<FbsPayment> latestProjectPaymentList = new ArrayList<FbsPayment>();
    List<String> flatList = new ArrayList<String>();
    Date beforeDate;
    boolean renderDashboard;

    @PostConstruct
    public void populate() {
        fbsComplaintList.clear();
        filterComplaintList.clear();
        statusComplaintList.clear();
        unAuthorizeBookingList.clear();
        latestBookingList.clear();
        latestPaymentList.clear();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            renderDashboard = true;
            fbsProject = new FbsProject();
            fbsProjectList.clear();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();

        } else {
            renderDashboard = false;
            fbsProject = new FbsProject();
            fbsProject = fbsProjectFacade.find(LoginBean.fbsProject.getProjId());

        }
        filterComplaintList = latestComplaintList();
        statusComplaintList = openComplaints();
        unAuthorizeBookingList = findUnauthorizeBooking();
        latestBookingList = findLatestBooking();
        latestProjectBookingList = latestProjectBooking();
        latestProjectPaymentList = latestProjectPayment();
        latestPaymentList = findLatestPayment();
        unAuthorizePaymentList = findUnauthorizePayments();

    }

    public Date findBeforeDate() {
        beforeDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE) - 7);
        beforeDate = cal.getTime();
        return beforeDate;

    }

    public List<FbsComplaint> latestComplaintList() {
        beforeDate = findBeforeDate();
        int projId;
        int companyId;
        //@NamedQuery(name = "FbsComplaint.findByCurrentDate", query = "SELECT f FROM FbsComplaint f  WHERE  f.complaintDt >= :beforeDate"),
        fbsComplaintList = em.createNamedQuery("FbsComplaint.findByCurrentDate").setParameter("beforeDate", beforeDate).getResultList();
        System.out.println("size of fbsComplaintList is........" + fbsComplaintList.size());
        for (int i = 0; i < fbsComplaintList.size(); i++) {
            companyId = fbsComplaintList.get(i).getFbsProject().getFbsCompany().getCompanyId();
            projId = fbsComplaintList.get(i).getFbsProject().getProjId();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)) {
                filterComplaintList.add(fbsComplaintList.get(i));
            }
        }
        return filterComplaintList;
    }

    public List<FbsComplaint> openComplaints() {
        fbsComplaintList.clear();
        statusComplaintList.clear();
        int projId;
        int companyId;
        //@NamedQuery(name = "FbsComplaint.findByStatus", query = "SELECT f FROM FbsComplaint f WHERE f.status = :status"),
        fbsComplaintList = em.createNamedQuery("FbsComplaint.findByStatus").setParameter("status", "Open").getResultList();
        System.out.println("size of fbsComplaintList is........" + fbsComplaintList.size());
        for (int i = 0; i < fbsComplaintList.size(); i++) {
            companyId = fbsComplaintList.get(i).getFbsProject().getFbsCompany().getCompanyId();
            projId = fbsComplaintList.get(i).getFbsProject().getProjId();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)) {
                statusComplaintList.add(fbsComplaintList.get(i));
            }
        }
        return statusComplaintList;
    }

    public List<FbsBooking> findUnauthorizeBooking() {
        fbsBookingList.clear();
        unAuthorizeBookingList.clear();
        //  @NamedQuery(name = "FbsBooking.findByAuthorizeStatus", query = "SELECT f FROM FbsBooking f WHERE f.authorizeStatus = :authorizeStatus"),
        fbsBookingList = em.createNamedQuery("FbsBooking.findByAuthorizeStatus").setParameter("authorizeStatus", "UA").getResultList();
        System.out.println("size of fbsBookingList is......." + fbsBookingList.size());
        int projId;
        int companyId;
        for (int i = 0; i < fbsBookingList.size(); i++) {
            projId = fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)) {
                unAuthorizeBookingList.add(fbsBookingList.get(i));
            }
        }
        return unAuthorizeBookingList;
    }

    public List<FbsBooking> latestProjectBooking() {
        latestProjectBookingList.clear();
        List<FbsBooking> bookingList = em.createNamedQuery("FbsBooking.findAllReverseOrderDate").getResultList();

        int projId;
        int companyId;
        for (FbsBooking fbsBooking : bookingList) {
            projId = fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId) && (fbsBooking.getStatus().equalsIgnoreCase("booked"))) {
                latestProjectBookingList.add(fbsBooking);

            }
        }
        return latestProjectBookingList;
    }

    public List<FbsPayment> latestProjectPayment() {
        latestProjectPaymentList.clear();
        int projId;
        int companyId;
        List<FbsPayment> fbsPayments = em.createNamedQuery("FbsPayment.findAllReverseOrderPaymentDate").getResultList();
        for (FbsPayment fbsPayment : fbsPayments) {
            projId = fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)) {
                latestProjectPaymentList.add(fbsPayment);

            }

        }


        return latestProjectPaymentList;

    }

    public List<FbsBooking> findLatestBooking() {
        latestBookingList.clear();
        fbsBookingList.clear();
        int projId;
        int companyId;
        String status = "";
        beforeDate = findBeforeDate();
        // @NamedQuery(name = "FbsBooking.findByCurrentDate", query = "SELECT f FROM FbsBooking f  WHERE  f.bookingDt >= :beforeDate"),
        fbsBookingList = em.createNamedQuery("FbsBooking.findByCurrentDate").setParameter("beforeDate", beforeDate).getResultList();
        System.out.println("size of fbsBookingList is......." + fbsBookingList.size());
        for (int i = 0; i < fbsBookingList.size(); i++) {
            projId = fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            status = fbsBookingList.get(i).getStatus();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId) && (status.equalsIgnoreCase("booked"))) {
                latestBookingList.add(fbsBookingList.get(i));
            }
        }
        System.out.println("size of latest booking " + latestBookingList.size());
        return latestBookingList;
    }

    public FbsApplicant showApplicant(FbsFlat fbsFlat) {
        // @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status AND f.applicantFlag = :applicantFlag"),
        List<FbsApplicant> fbsApplicants = em.createNamedQuery("FbsApplicant.findByFbsFlat&Status&ApplicantFlag").setParameter("fbsFlat", fbsFlat).setParameter("status", "booked").setParameter("applicantFlag", 1).getResultList();
        if (fbsApplicants.isEmpty()) {
            return null;
        } else {
            return fbsApplicants.get(0);
        }
    }

    public List<FbsPayment> findLatestPayment() {
        fbsPaymentList.clear();
        latestPaymentList.clear();
        beforeDate = findBeforeDate();
        int projId;
        int companyId;
        // @NamedQuery(name = "FbsPayment.findByCurrentDate", query = "SELECT f FROM Payment f  WHERE  f.paymentDate >= :beforeDate"),
        fbsPaymentList = em.createNamedQuery("FbsPayment.findByCurrentDate").setParameter("beforeDate", beforeDate).getResultList();
        System.out.println("size of fbsPaymentList is......." + fbsPaymentList.size());
        for (int i = 0; i < fbsPaymentList.size(); i++) {
            projId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)) {
                latestPaymentList.add(fbsPaymentList.get(i));
            }
        }
        return latestPaymentList;
    }

    public List<FbsPayment> findUnauthorizePayments() {
        fbsPaymentList.clear();
        unAuthorizePaymentList.clear();
        String status = "";
        int projId;
        int companyId;
        // @NamedQuery(name = "FbsPayment.findByStatus", query = "SELECT f FROM FbsPayment f  WHERE  f.status = :status"),
        fbsPaymentList = em.createNamedQuery("FbsPayment.findByStatus").setParameter("status", "Pending").getResultList();
        System.out.println("size of paymentList is......." + fbsPaymentList.size());
        for (int i = 0; i < fbsPaymentList.size(); i++) {

            status = fbsPaymentList.get(i).getFbsBooking().getStatus();
            projId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            companyId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();

            if ((fbsCompany == null || fbsCompany.getCompanyId() == null || fbsCompany.getCompanyId() == companyId)
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projId)
                    && (status.equalsIgnoreCase("booked"))) {
                unAuthorizePaymentList.add(fbsPaymentList.get(i));
            }

        }
        return unAuthorizePaymentList;
    }

    public void bookingDetail(FbsBooking fbsBooking) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBooking.getFbsFlat().getUnitCode() + "&redirectFlag=dashboard&name=Dashboard&url=/faces/jsfpages/Dashboard/sampleChart.xhtml");
    }

    public void bookingDetail1(FbsPayment fbsPayment) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsPayment.getFbsBooking().getFbsFlat().getUnitCode() + "&redirectFlag=dashboard&name=Dashboard&url=/faces/jsfpages/Dashboard/sampleChart.xhtml");
    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    public List<FbsComplaint> getFilterComplaintList() {
        return filterComplaintList;
    }

    public void setFilterComplaintList(List<FbsComplaint> filterComplaintList) {
        this.filterComplaintList = filterComplaintList;
    }

    public List<FbsComplaint> getStatusComplaintList() {
        return statusComplaintList;
    }

    public void setStatusComplaintList(List<FbsComplaint> statusComplaintList) {
        this.statusComplaintList = statusComplaintList;
    }

    public List<FbsBooking> getUnAuthorizeBookingList() {
        return unAuthorizeBookingList;
    }

    public void setUnAuthorizeBookingList(List<FbsBooking> unAuthorizeBookingList) {
        this.unAuthorizeBookingList = unAuthorizeBookingList;
    }

    public List<FbsBooking> getLatestBookingList() {
        return latestBookingList;
    }

    public void setLatestBookingList(List<FbsBooking> latestBookingList) {
        this.latestBookingList = latestBookingList;
    }

    public List<FbsPayment> getLatestPaymentList() {
        return latestPaymentList;
    }

    public void setLatestPaymentList(List<FbsPayment> latestPaymentList) {
        this.latestPaymentList = latestPaymentList;
    }

    public List<FbsPayment> getUnAuthorizePaymentList() {
        return unAuthorizePaymentList;
    }

    public void setUnAuthorizePaymentList(List<FbsPayment> unAuthorizePaymentList) {
        this.unAuthorizePaymentList = unAuthorizePaymentList;
    }

    public List<FbsBooking> getLatestProjectBookingList() {
        return latestProjectBookingList;
    }

    public void setLatestProjectBookingList(List<FbsBooking> latestProjectBookingList) {
        this.latestProjectBookingList = latestProjectBookingList;
    }

    public List<FbsPayment> getLatestProjectPaymentList() {
        return latestProjectPaymentList;
    }

    public void setLatestProjectPaymentList(List<FbsPayment> latestProjectPaymentList) {
        this.latestProjectPaymentList = latestProjectPaymentList;
    }

    public boolean isRenderDashboard() {
        return renderDashboard;
    }

    public void setRenderDashboard(boolean renderDashboard) {
        this.renderDashboard = renderDashboard;
    }
}
