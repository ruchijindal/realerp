/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BookingManager;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@ManagedBean(name = "bookingListBean")
@ViewScoped
public class BookingListBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    BookingManager bookingManager;
    @EJB
    BlockManager blockManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    List<FbsBooking> bookingList = new ArrayList<FbsBooking>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    boolean renderProject = true;
    FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    String fbsRegNum;
    Date bookingDateFrom;
    Date bookingDateTo;
    String applicantName;
    Integer searchType = 1;
    boolean renderAdvanceSearchForm;
    FbsBooking authorizeBooking = new FbsBooking();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant fbsCoApplicant = new FbsApplicant();
    FbsBooking cancelledFbsBooking = new FbsBooking();
    String fbsAuthorizeBy;
    String fbsBookingBy;
    String requestType = "booked";

    @PostConstruct
    public void populate() {
        fbsCompany = new FbsCompany();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        if (requestType.equalsIgnoreCase("transfered")) {
            bookingList.clear();
            bookingList = bookingManager.findTransferedUnits();
        } else {
            bookingList.clear();
            bookingList = em.createNamedQuery("FbsBooking.findByStatus").setParameter("status", requestType).getResultList();
        }
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            fbsProjectList.clear();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
            filterBookingList();
            renderProject = true;

        } else {
            renderProject = false;
            fbsProject = new FbsProject();
            fbsProject = LoginBean.fbsProject;
            fbsProjectList.clear();
            fbsProjectList.add(fbsProject);
            populateBlock();
        }
    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        bookingDateFrom = event.getDate();
        filterBookingList();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        bookingDateTo = event.getDate();
        filterBookingList();

    }

    public void populateBlock() {
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        if (fbsProject.getProjId() != 0) {
            fbsProject = fbsProjectFacade.find(fbsProject.getProjId());
            fbsBlockList = (List<FbsBlock>) fbsProject.getFbsBlockCollection();
        }
        filterBookingList();
    }

    public void populateFloor() {
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();

        if (fbsBlock.getBlockId() != 0) {
            fbsBlock = fbsBlockFacade.find(fbsBlock.getBlockId());
            fbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();

        }
        fbsFloorList = blockManager.sortFloorList(fbsFloorList);
        filterBookingList();
    }

    public void populateFlat() {
        fbsFlatList.clear();
        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());
            //@NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "booked").getResultList();

        }
        fbsFlatList = blockManager.sortFlatList(fbsFlatList);
        filterBookingList();
    }

    public void populateBooking() {

        if (fbsFlat.getUnitCode() != 0) {
            fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        }
        filterBookingList();
    }

    public void filterBookingList() {
        fbsBookingList.clear();
        int projectId;
        int blockId;
        int floorId;
        int flatId;
        int companyId;
        String regNum;
        Date bookingDate;
        FbsApplicant fbsApplicant;
        String authorizeBy;
        String bookingBy;
        for (int i = 0; i < bookingList.size(); i++) {
            companyId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            projectId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            blockId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            floorId = bookingList.get(i).getFbsFlat().getFbsFloor().getFloorId();
            flatId = bookingList.get(i).getFbsFlat().getUnitCode();
            regNum = bookingList.get(i).getRegNumber().toString();
            bookingDate = bookingList.get(i).getBookingDt();
            fbsApplicant = new FbsApplicant();
            fbsApplicant = showApplicant(bookingList.get(i).getFbsFlat());
            authorizeBy = bookingList.get(i).getAuthorizeBy();
            bookingBy = bookingList.get(i).getUserId();

            if (companyId == fbsCompany.getCompanyId()
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)
                    && (fbsBlock == null || fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0 || fbsBlock.getBlockId() == blockId)
                    && (fbsFloor == null || fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0 || fbsFloor.getFloorId() == floorId)
                    && (fbsFlat == null || fbsFlat.getUnitCode() == null || fbsFlat.getUnitCode() == 0 || fbsFlat.getUnitCode() == flatId)
                    && (fbsRegNum == null || regNum.contains(fbsRegNum))
                    && ((bookingDateFrom == null) || (bookingDate.after(bookingDateFrom)) || (bookingDate.equals(bookingDateFrom)))
                    && ((bookingDateTo == null) || (bookingDate.before(bookingDateTo)) || (bookingDate.equals(bookingDateTo)))
                    && (fbsAuthorizeBy == null || (authorizeBy != null && authorizeBy.contains(fbsAuthorizeBy)))
                    && (fbsBookingBy == null || (bookingBy != null && bookingBy.contains(fbsBookingBy)))
                    && ((applicantName == null) || (fbsApplicant != null && fbsApplicant.getApplicantName().toLowerCase().contains(applicantName.toLowerCase())))) {
                fbsBookingList.add(bookingList.get(i));

            }
        }
        fbsBookingList = bookingManager.sortBookingList(fbsBookingList);
        Collections.reverse(fbsBookingList);
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

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public void changeTypeOfSearching() {
        if ((searchType != null) && (searchType == 1)) {
            renderAdvanceSearchForm = false;
        } else if ((searchType != null) && (searchType == 2)) {
            renderAdvanceSearchForm = true;
        }
        reset();
    }

    public void reset() {
        fbsProject = new FbsProject();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        fbsRegNum = null;
        bookingDateFrom = null;
        bookingDateTo = null;
        applicantName = null;
        fbsAuthorizeBy = null;
        fbsBookingBy = null;
        filterBookingList();

    }

    public String statusValue(String status) {
        if (status.equalsIgnoreCase("AU")) {
            return "Authorize";
        } else {
            return "UnAuthorize";
        }
    }

    public void authorizeBooking(FbsBooking book) {
        authorizeBooking = new FbsBooking();
        authorizeBooking = book;
    }

    public void authorizeBook() {
        authorizeBooking.setAuthorizeBy(LoginBean.fbsLogin.getUserId());
        authorizeBooking.setAuthorizeDate(new Date());
        if (authorizeBooking.getAuthorizeStatus().equalsIgnoreCase("ua")) {
            authorizeBooking.setAuthorizeStatus("AU");
        } else {
            authorizeBooking.setAuthorizeStatus("UA");
        }

        bookingManager.authorizeBooking(authorizeBooking);
    }

    public void bookingDetail(FbsBooking fbsBooking) throws IOException {
        if (requestType.equalsIgnoreCase("booked") || requestType.equalsIgnoreCase("transfered")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBooking.getFbsFlat().getUnitCode() + "&redirectFlag=BookingList&name=Booking List&url=/faces/jsfpages/Booking/bookingList.xhtml");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/cancelledBookingDetail.xhtml?regNum=" + fbsBooking.getRegNumber());
        }
    }

    public void transferBooking(FbsBooking fbsBooking) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/transferBooking.xhtml?regNum=" + fbsBooking.getRegNumber() + "&rFlag=1" + "&renderFlag=1");

    }

    public void consumerReport(int registrationNumber) throws IOException {
        System.out.println("RegistrationNumber" + registrationNumber);
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/ConsumerReport?registrationNumber=" + registrationNumber+"&requestType=nonEmail");
    }

    public void dueLetterReport(int registrationNumber) throws IOException {
        System.out.println("RegistrationNumber" + registrationNumber);
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport?registrationNumber=" + registrationNumber+"&requestType=nonEmail");


    }

    public void conFirmBookingCancel(FbsBooking fbsBooking) {
        cancelledFbsBooking = new FbsBooking();
        cancelledFbsBooking = fbsBooking;
    }

    public void cancelBooking() {

        bookingManager.cancelBooking(cancelledFbsBooking);
        cancelledFbsBooking = new FbsBooking();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Booking Cancelled", ""));
        populate();
    }

    public void redirectBooking() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingList.xhtml");
        populate();

    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Date getBookingDateFrom() {
        return bookingDateFrom;
    }

    public void setBookingDateFrom(Date bookingDateFrom) {
        this.bookingDateFrom = bookingDateFrom;
    }

    public Date getBookingDateTo() {
        return bookingDateTo;
    }

    public void setBookingDateTo(Date bookingDateTo) {
        this.bookingDateTo = bookingDateTo;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
    }

    public List<FbsBooking> getFbsBookingList() {
        return fbsBookingList;
    }

    public void setFbsBookingList(List<FbsBooking> fbsBookingList) {
        this.fbsBookingList = fbsBookingList;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public String getFbsRegNum() {
        return fbsRegNum;
    }

    public void setFbsRegNum(String fbsRegNum) {
        this.fbsRegNum = fbsRegNum;
    }

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
    }

    public List<FbsFlat> getFbsFlatList() {
        return fbsFlatList;
    }

    public void setFbsFlatList(List<FbsFlat> fbsFlatList) {
        this.fbsFlatList = fbsFlatList;
    }

    public List<FbsFloor> getFbsFloorList() {
        return fbsFloorList;
    }

    public void setFbsFloorList(List<FbsFloor> fbsFloorList) {
        this.fbsFloorList = fbsFloorList;
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    public boolean isRenderAdvanceSearchForm() {
        return renderAdvanceSearchForm;
    }

    public void setRenderAdvanceSearchForm(boolean renderAdvanceSearchForm) {
        this.renderAdvanceSearchForm = renderAdvanceSearchForm;
    }

    public FbsBooking getAuthorizeBooking() {
        return authorizeBooking;
    }

    public void setAuthorizeBooking(FbsBooking authorizeBooking) {
        this.authorizeBooking = authorizeBooking;
    }

    public FbsApplicant getFbsApplicant() {
        return fbsApplicant;
    }

    public void setFbsApplicant(FbsApplicant fbsApplicant) {
        this.fbsApplicant = fbsApplicant;
    }

    public FbsApplicant getFbsCoApplicant() {
        return fbsCoApplicant;
    }

    public void setFbsCoApplicant(FbsApplicant fbsCoApplicant) {
        this.fbsCoApplicant = fbsCoApplicant;
    }

    public String getFbsAuthorizeBy() {
        return fbsAuthorizeBy;
    }

    public void setFbsAuthorizeBy(String fbsAuthorizeBy) {
        this.fbsAuthorizeBy = fbsAuthorizeBy;
    }

    public String getFbsBookingBy() {
        return fbsBookingBy;
    }

    public void setFbsBookingBy(String fbsBookingBy) {
        this.fbsBookingBy = fbsBookingBy;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }
}
