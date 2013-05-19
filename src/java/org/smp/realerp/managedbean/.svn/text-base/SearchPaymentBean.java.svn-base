/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

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
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@ManagedBean(name = "searchPaymentBean")
@ViewScoped
public class SearchPaymentBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsBankFacade fbsBankFacade;
    FbsCompany fbsCompany = new FbsCompany();
    FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    FbsBank fbsBank = new FbsBank();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    List<FbsPayment> paymentList = new ArrayList<FbsPayment>();
    List<FbsBank> fbsBankList = new ArrayList<FbsBank>();
    String status = "Select Status";
    String paymentMode = "Select Payment Mode";
    String chequeno;
    double paidAmount;
    String authorizeBy;
    String dateMode;
    Date fromDate;
    Date toDate;
    String clrBank = "Select Bank";

    @PostConstruct
    public void populate() {
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsPaymentList.clear();
        paymentList.clear();
        fbsProject = new FbsProject();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();

        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        paymentList = fbsPaymentFacade.findAll();
        filterPaymentList();
    }

    public void filterPaymentList() {
        int projectId;
        int blockId;
        int floorId;
        int flatId;
        int companyId;
        String bookingStatus = "";
        fbsPaymentList.clear();

        for (int i = 0; i < paymentList.size(); i++) {
            clrBank = paymentList.get(i).getClearingBank();
            bookingStatus = paymentList.get(i).getFbsBooking().getStatus();
            companyId = paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            projectId = paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            blockId = paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            floorId = paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFloorId();
            flatId = paymentList.get(i).getFbsBooking().getFbsFlat().getUnitCode();
            String paymentStatus = paymentList.get(i).getStatus();
            String paymentModeType = paymentList.get(i).getPaymentMode();
            if ((bookingStatus.equalsIgnoreCase("booked")) && (companyId == fbsCompany.getCompanyId()) && (fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId) && (fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0 || fbsBlock.getBlockId() == blockId)
                    && (fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0 || fbsFloor.getFloorId() == floorId) && (fbsFlat.getUnitCode() == null || fbsFlat.getUnitCode() == 0 || fbsFlat.getUnitCode() == flatId)
                    && (status.equalsIgnoreCase("select status") || status == null || status.equalsIgnoreCase(paymentStatus)) && (paymentMode.equalsIgnoreCase("select payment mode") || paymentMode == null || paymentMode.equalsIgnoreCase(paymentModeType))
                    && (fbsBank.getBankId() == null || fbsBank.getBankId() == 0 || (clrBank != null && fbsBank.getBankName().contains(clrBank)))) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }

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
        filterPaymentList();
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
        fbsFloorList = paymentManager.sortFloorList(fbsFloorList);
        filterPaymentList();
    }

    public void populateFlat() {
        fbsFlatList.clear();
        System.out.println("floorId is......" + fbsFloor.getFloorId());
        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());
            System.out.println("floorId is......" + fbsFloor.getFloorNo());
            //@NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "booked").getResultList();

            System.out.println("size of list is......." + fbsFlatList.size());
        }
        filterPaymentList();
    }

    public void populatePayment() {

        if (fbsFlat.getUnitCode() != 0) {
            fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        }
        filterPaymentList();
    }

    public void clrBank() {
        if (fbsBank.getBankId() != null || fbsBank.getBankId() != 0) {
            fbsBank = fbsBankFacade.find(fbsBank.getBankId());
            filterPaymentList();
        }

    }

    public List<FbsPayment> populatePaymentListByChequeNo() {
        fbsPaymentList.clear();
        for (int i = 0; i < paymentList.size(); i++) {
            if ((paymentList.get(i).getChequeNo() != null && paymentList.get(i).getChequeNo().contains(chequeno))
                    || (paymentList.get(i).getTransactionId() != null && paymentList.get(i).getTransactionId().contains(chequeno))) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }
        return fbsPaymentList;
    }

    public List<FbsPayment> populatePaymentListByAmount() {
        fbsPaymentList.clear();
        for (int i = 0; i < paymentList.size(); i++) {
            if ((paymentList.get(i).getPaidAmount() != null && paymentList.get(i).getPaidAmount() == paidAmount)) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }
        return fbsPaymentList;
    }

    public List<FbsPayment> populatePaymentListByAuthorizeBy() {
        fbsPaymentList.clear();
        for (int i = 0; i < paymentList.size(); i++) {
            if ((paymentList.get(i).getAuthorizedBy() != null) && (paymentList.get(i).getAuthorizedBy().contains(authorizeBy))) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }
        return fbsPaymentList;
    }

    public List<FbsPayment> populatePaymentListByDateMode() {
        fbsPaymentList.clear();
        if (fromDate == null || toDate == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Enter from & to date"));
            populate();
        } else if (fromDate != null && toDate != null) {
            if (dateMode.equals("paydate")) {
                fbsPaymentList.clear();
                System.out.println("fromdate==>" + fromDate);
                System.out.println("todate==>" + toDate);
                List<FbsPayment> dateList = em.createNamedQuery("FbsPayment.findByDate").setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
                for (int i = 0; i < dateList.size(); i++) {
                    fbsPaymentList.add(dateList.get(i));
                }
            } else if (dateMode.equals("chqdate")) {
                fbsPaymentList.clear();
                //@NamedQuery(name = "FbsPayment.findByChqDate", query = "SELECT f FROM FbsPayment f WHERE f.chequeDate >= :fromDate AND f.chequeDate <= :toDate"),
                List<FbsPayment> dateList = em.createNamedQuery("FbsPayment.findByChqDate").setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
                for (int i = 0; i < dateList.size(); i++) {
                    fbsPaymentList.add(dateList.get(i));
                }
            } else {
                fbsPaymentList.clear();
                //@NamedQuery(name = "FbsPayment.findByClrDate", query = "SELECT f FROM FbsPayment f WHERE f.clearingDt >= :fromDate AND f.clearingDt <= :toDate"),
                List<FbsPayment> dateList = em.createNamedQuery("FbsPayment.findByClrDate").setParameter("fromDate", fromDate).setParameter("toDate", toDate).getResultList();
                for (int i = 0; i < dateList.size(); i++) {
                    fbsPaymentList.add(dateList.get(i));
                }

            }
        }

        return fbsPaymentList;
    }

    public String convertFloorNo(int floorNo) {
        return paymentManager.convertFloorNo(floorNo);
    }

    public void reset() {
        System.out.println("helloooooooooooo");
        fbsProject = new FbsProject();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        fbsBank = new FbsBank();
        status = "Select Status";
        paymentMode = "Select Payment Mode";
        clrBank = "Select Bank";
        fromDate = null;
        toDate = null;
        chequeno = "";
        paidAmount = 0;
        authorizeBy = "";
        dateMode = "";
        populate();
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
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

    public List<FbsPayment> getFbsPaymentList() {
        return fbsPaymentList;
    }

    public void setFbsPaymentList(List<FbsPayment> fbsPaymentList) {
        this.fbsPaymentList = fbsPaymentList;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
    }

    public List<FbsFloor> getFbsFloorList() {
        return fbsFloorList;
    }

    public void setFbsFloorList(List<FbsFloor> fbsFloorList) {
        this.fbsFloorList = fbsFloorList;
    }

    public List<FbsFlat> getFbsFlatList() {
        return fbsFlatList;
    }

    public void setFbsFlatList(List<FbsFlat> fbsFlatList) {
        this.fbsFlatList = fbsFlatList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getChequeno() {
        return chequeno;
    }

    public void setChequeno(String chequeno) {
        this.chequeno = chequeno;
    }

    public String getAuthorizeBy() {
        return authorizeBy;
    }

    public void setAuthorizeBy(String authorizeBy) {
        this.authorizeBy = authorizeBy;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getDateMode() {
        return dateMode;
    }

    public void setDateMode(String dateMode) {
        this.dateMode = dateMode;
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

    public FbsBank getFbsBank() {
        return fbsBank;
    }

    public void setFbsBank(FbsBank fbsBank) {
        this.fbsBank = fbsBank;
    }

    public List<FbsBank> getFbsBankList() {
        return fbsBankList;
    }

    public void setFbsBankList(List<FbsBank> fbsBankList) {
        this.fbsBankList = fbsBankList;
    }
}
