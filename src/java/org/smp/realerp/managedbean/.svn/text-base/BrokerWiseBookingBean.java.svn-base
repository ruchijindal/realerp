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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import org.primefaces.event.DateSelectEvent;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BookingManager;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.session.*;
import org.smp.realerp.pojo.RerpUtil;

/**
 *
 * @author smp
 */
@ManagedBean(name = "brokerWiseBookingBean")
@ViewScoped
public class BrokerWiseBookingBean {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    BrokerManager brokerManager;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    BookingManager bookingManager;
    @EJB
    BlockManager blockManager;
    @EJB
    RerpUtil utility;
    FbsBroker fbsBroker = new FbsBroker();
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsBroker> fbsBrokerList = new ArrayList<FbsBroker>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    List<FbsBooking> bookingList = new ArrayList<FbsBooking>();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    Integer fbsRegistrationNo;
    Date bookingDateFrom;
    Date bookingDateTo;
    boolean renderProject;

    @PostConstruct
    public void populate() {

        fbsCompany = new FbsCompany();
        fbsProjectList.clear();
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsBroker = new FbsBroker();
        fbsProject = new FbsProject();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBrokerList.clear();
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            fbsProject = new FbsProject();
            renderProject = true;
            fbsProjectList.clear();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
        } else {
            fbsProject = new FbsProject();
            fbsProject = LoginBean.fbsProject;
            populateByBlock();
            renderProject = false;
            fbsProjectList.clear();
            fbsProjectList.add(fbsProject);
        }
        fbsBrokerList = (List<FbsBroker>) fbsCompany.getFbsBrokerCollection();
        fbsBookingList.clear();
        bookingList.clear();
        bookingList = em.createNamedQuery("FbsBooking.findByFbsBroker&Status").setParameter("status", "booked").getResultList();
        filterBookingList();


    }

    public void populateByBroker() {
        System.out.println("brokerid==>+" + fbsBroker.getBrokerId() + "   " + fbsBookingList.size());
        fbsProjectList.clear();
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsProject = new FbsProject();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();

        if (fbsBroker.getBrokerId() != 0) {
            fbsBroker = fbsBrokerFacade.find(fbsBroker.getBrokerId());
            for (int i = 0; i < fbsBookingList.size(); i++) {
                if (fbsBookingList.get(i).getFbsBroker().getBrokerId().intValue() == fbsBroker.getBrokerId()) {
                    if (!fbsProjectList.contains(fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject())) {
                        fbsProjectList.add(fbsBookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject());
                    }
                }
            }
        }
        filterBookingList();

    }


    public void populateByBlock() {
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

    public void populateByFloor() {
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();

        if (fbsBlock.getBlockId() != 0) {
            fbsBlock = fbsBlockFacade.find(fbsBlock.getBlockId());
            fbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();

        }
        fbsFloorList = paymentManager.sortFloorList(fbsFloorList);
        filterBookingList();
    }

    public void populateByFlat() {
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


    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    public void filterBookingList() {
        fbsBookingList.clear();
        int brokerId;
        int projectId;
        int blockId;
        int floorId;
        int flatId;
        int companyId;
        int registraionNo;
        Date bookingDate;
        for (int i = 0; i < bookingList.size(); i++) {
            brokerId = bookingList.get(i).getFbsBroker().getBrokerId();
            companyId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            projectId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            blockId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            floorId = bookingList.get(i).getFbsFlat().getFbsFloor().getFloorId();
            flatId = bookingList.get(i).getFbsFlat().getUnitCode();
            registraionNo = bookingList.get(i).getRegNumber();
            bookingDate = bookingList.get(i).getBookingDt();
            if (companyId == fbsCompany.getCompanyId()
                    && (fbsBroker.getBrokerId() == null || fbsBroker.getBrokerId() == 0 || brokerId == fbsBroker.getBrokerId())
                    && (fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)
                    && (fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0 || fbsBlock.getBlockId() == blockId)
                    && (fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0 || fbsFloor.getFloorId() == floorId)
                    && (fbsFlat.getUnitCode() == null || fbsFlat.getUnitCode() == 0 || fbsFlat.getUnitCode() == flatId)
                    && (fbsRegistrationNo == null || String.valueOf(registraionNo).contains(String.valueOf(fbsRegistrationNo)))
                    && ((bookingDateFrom == null || bookingDate.after(bookingDateFrom)) || bookingDate.equals(bookingDateFrom))
                    && ((bookingDateTo == null || bookingDate.before(bookingDateTo)) || bookingDate.equals(bookingDateTo))) {
                fbsBookingList.add(bookingList.get(i));

            }
        }
        fbsBookingList = bookingManager.sortBookingList(fbsBookingList);
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public double calculateBrokerCommission(FbsBooking fbsBooking) {
        FbsFlatType fbsFlatType = fbsBooking.getFbsFlat().getFbsFlatType();
        FbsPlanname fbsPlanname = fbsBooking.getFbsPlanname();
        FbsDiscount fbsDiscount = fbsBooking.getFbsDiscount();
        FbsBrokerCat fbsBrokerCat = fbsBooking.getFbsBroker().getFbsBrokerCat();
        double brokerCommission = brokerManager.calculateBrokerCommission(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat);
        return brokerCommission;
    }

    public double calculateBrokerPaidAmount(FbsBooking fbsBooking) {
        double totalBrokerPaidAmount = brokerManager.calculateBrokerPaidAmount(fbsBooking);
        return totalBrokerPaidAmount;
    }

    public double calculateBrokerPayableAmount(FbsBooking fbsBooking) {
        FbsFlatType fbsFlatType = fbsBooking.getFbsFlat().getFbsFlatType();
        FbsPlanname fbsPlanname = fbsBooking.getFbsPlanname();
        FbsDiscount fbsDiscount = fbsBooking.getFbsDiscount();
        FbsBrokerCat fbsBrokerCat = fbsBooking.getFbsBroker().getFbsBrokerCat();
        double brokerPayableAmount = brokerManager.calculateBrokerPayableAmount(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat, fbsBooking);
        return brokerPayableAmount;
    }

    public void generateReceipt(FbsBooking fbsBooking) throws IOException {
        int registrationNo = fbsBooking.getRegNumber();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/brokerBookingReceipt?registrationNo=" + registrationNo);
    }

    public void generateReceipt1() throws IOException {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        filterBookingList();
        System.out.println("size of list in bean is........." + fbsBookingList.size());
        session.setAttribute("fbsBookingList", fbsBookingList);
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/masterBrokerReceipt");
    }
    
    public void bookingDetail(FbsBooking fbsBooking) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBooking.getFbsFlat().getUnitCode() + "&redirectFlag=BrokerWise BookingList&name=Brokerwise Booking List&url=/faces/jsfpages/Broker/brokerWiseBooking.xhtml");
    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        bookingDateFrom = event.getDate();
        filterBookingList();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        bookingDateTo = event.getDate();
        filterBookingList();
    }

    public List<FbsBroker> getFbsBrokerList() {
        return fbsBrokerList;
    }

    public void setFbsBrokerList(List<FbsBroker> fbsBrokerList) {
        this.fbsBrokerList = fbsBrokerList;
    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
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

    public List<FbsBooking> getFbsBookingList() {
        return fbsBookingList;
    }

    public void setFbsBookingList(List<FbsBooking> fbsBookingList) {
        this.fbsBookingList = fbsBookingList;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

    public List<FbsFloor> getFbsFloorList() {
        return fbsFloorList;
    }

    public void setFbsFloorList(List<FbsFloor> fbsFloorList) {
        this.fbsFloorList = fbsFloorList;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public List<FbsFlat> getFbsFlatList() {
        return fbsFlatList;
    }

    public void setFbsFlatList(List<FbsFlat> fbsFlatList) {
        this.fbsFlatList = fbsFlatList;
    }

    public Integer getFbsRegistrationNo() {
        return fbsRegistrationNo;
    }

    public void setFbsRegistrationNo(Integer fbsRegistrationNo) {
        this.fbsRegistrationNo = fbsRegistrationNo;
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

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
    }
}
