/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.text.DecimalFormat;
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
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.manager.CalculationManager;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@ManagedBean(name = "brokerPaymentBean")
@ViewScoped
public class BrokerPaymentBean {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsBrPaymentFacade fbsBrPaymentFacade;
    @EJB
    BlockManager blockManager;
    @EJB
    BrokerManager brokerManager;
    @EJB
    FbsBankFacade fbsBankFacade;
    @EJB
    FbsBrokerCatFacade fbsBrokerCatFacade;
    @EJB
    CalculationManager calculationManager;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    RerpUtil utility;
    FbsCompany fbsCompany = new FbsCompany();
    FbsBooking fbsBooking = new FbsBooking();
    FbsFlat fbsFlat = new FbsFlat();
    FbsBrokerCat fbsBrokerCat = new FbsBrokerCat();
    FbsBroker fbsBroker = new FbsBroker();
    FbsBank fbsBank = new FbsBank();
    FbsBrPayment statusBrPayment = new FbsBrPayment();
    FbsBrPayment delFbsBrPayment = new FbsBrPayment();
    FbsBrPayment selectedFbsBrPayment = new FbsBrPayment();
    FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat searchFbsFlat = new FbsFlat();
    FbsBrPayment fbsBrPayment = new FbsBrPayment();
    List<FbsBroker> fbsBrokerList = new ArrayList<FbsBroker>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    List<FbsBank> fbsBankList = new ArrayList<FbsBank>();
    List<FbsBrPayment> fbsBrPaymentList = new ArrayList<FbsBrPayment>();
    List<FbsBrokerCat> fbsBrokerCatList = new ArrayList<FbsBrokerCat>();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    double brokerPayableOutstanding = 0;
    DecimalFormat format = new DecimalFormat("0.00");
    boolean renderModeCheque;
    boolean renderModeNeft;
    boolean renderDialog;
    boolean renderBrCategoryDetail;
    boolean renderRegNumDetail;
    boolean renderPending;
    boolean renderAdvanceSearchForm;
    Integer searchType = 1;
    Integer fbsReceiptNo;
    Integer fbsRegistrationNo;
    String fbsBrokerPaymentMode;
    String chequeTransactionNo;
    String fbsAuthorizeBy;
    String fbsPaymentBy;
    Date paymentDateFrom;
    Date paymentDateTo;
    Date chequeDateFrom;
    Date chequeDateTo;
    Date clearingDateFrom;
    Date clearingDateTo;
    Date clearingDate;
    boolean validChequeDate;

    @PostConstruct
    public void populate() {
        fbsBrokerCat = new FbsBrokerCat();
        fbsBroker = new FbsBroker();
        fbsCompany = new FbsCompany();
        fbsProject = new FbsProject();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBrokerCatList.clear();
        fbsBrokerCatList = (List<FbsBrokerCat>) fbsCompany.getFbsBrokerCatCollection();
        fbsBrokerList = brokerManager.sortBrokerList(fbsBrokerList);
        fbsBrPayment.setPaymentDate(new Date());
        fbsBankList.clear();
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        fbsProjectList.clear();
        fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
        renderModeCheque = false;
        renderModeNeft = false;
        fbsBrPaymentList.clear();
        fbsBookingList.clear();
        fbsBrokerList.clear();
        filterBrokerPayment();
    }

    public void populateBlock() {
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        searchFbsFlat = new FbsFlat();
        if (fbsProject.getProjId() != 0) {
            fbsProject = fbsProjectFacade.find(fbsProject.getProjId());
            fbsBlockList = (List<FbsBlock>) fbsProject.getFbsBlockCollection();
        }
        filterBrokerPayment();
    }

    public void populateFloor() {
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsFloor = new FbsFloor();
        searchFbsFlat = new FbsFlat();

        if (fbsBlock.getBlockId() != 0) {
            fbsBlock = fbsBlockFacade.find(fbsBlock.getBlockId());
            fbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();

        }
        fbsFloorList = paymentManager.sortFloorList(fbsFloorList);
        filterBrokerPayment();
    }

    public void populateFlat() {
        fbsFlatList.clear();
        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());
            //@NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "booked").getResultList();

        }
        fbsFlatList = blockManager.sortFlatList(fbsFlatList);
        filterBrokerPayment();
    }

    public void populatePayment() {

        if (searchFbsFlat.getUnitCode() != 0) {
            searchFbsFlat = fbsFlatFacade.find(searchFbsFlat.getUnitCode());
        }
        filterBrokerPayment();
    }

    public void filterBrokerPayment() {
        fbsBrPaymentList.clear();
        List<FbsBrPayment> brokerPaymentList = fbsBrPaymentFacade.findAll();
        int companyId;
        int brokerCategoryId;
        int brokerId;
        String paymentMode;
        int receiptNo;
        int registraionNo;
        Date paymentDate;
        int projectId;
        int blockId;
        int floorId;
        int flatId;
        String status;
        String clearingBank;
        String chequeNo;
        String trancastionId;
        String authorizeBy;
        String userId;
        Date chequeDate;
        Date clearingDate;

        for (int i = 0; i < brokerPaymentList.size(); i++) {
            status = brokerPaymentList.get(i).getFbsBooking().getStatus();
            companyId = brokerPaymentList.get(i).getFbsBroker().getFbsCompany().getCompanyId();
            brokerCategoryId = brokerPaymentList.get(i).getFbsBroker().getFbsBrokerCat().getCategoryId();
            brokerId = brokerPaymentList.get(i).getFbsBroker().getBrokerId();
            paymentMode = brokerPaymentList.get(i).getPaymentMode();
            receiptNo = brokerPaymentList.get(i).getPaymentId();
            registraionNo = brokerPaymentList.get(i).getFbsBooking().getRegNumber();
            paymentDate = brokerPaymentList.get(i).getPaymentDate();
            projectId = brokerPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            blockId = brokerPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            floorId = brokerPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFloorId();
            flatId = brokerPaymentList.get(i).getFbsBooking().getFbsFlat().getUnitCode();
            clearingBank = brokerPaymentList.get(i).getClearingBank();
            chequeNo = brokerPaymentList.get(i).getChequeNo();
            trancastionId = brokerPaymentList.get(i).getTransactionId();
            authorizeBy = brokerPaymentList.get(i).getAuthorizedBy();
            userId = brokerPaymentList.get(i).getUserId();
            chequeDate = brokerPaymentList.get(i).getChequeDate();
            clearingDate = brokerPaymentList.get(i).getClearingDt();

            if ((status.equalsIgnoreCase("Booked")) && (companyId == fbsCompany.getCompanyId())
                    && (fbsBrokerCat == null || fbsBrokerCat.getCategoryId() == null || fbsBrokerCat.getCategoryId() == 0 || fbsBrokerCat.getCategoryId() == brokerCategoryId)
                    && (fbsBroker == null || fbsBroker.getBrokerId() == null || fbsBroker.getBrokerId() == 0 || fbsBroker.getBrokerId() == brokerId)
                    && (fbsBrokerPaymentMode == null || fbsBrokerPaymentMode.equalsIgnoreCase(paymentMode))
                    && (fbsReceiptNo == null || String.valueOf(receiptNo).contains(String.valueOf(fbsReceiptNo))) && (fbsRegistrationNo == null || String.valueOf(registraionNo).contains(String.valueOf(fbsRegistrationNo)))
                    && ((paymentDateFrom == null || paymentDate.after(paymentDateFrom)) || paymentDate.equals(paymentDateFrom))
                    && ((paymentDateTo == null || paymentDate.before(paymentDateTo)) || paymentDate.equals(paymentDateTo))
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)
                    && (fbsBlock == null || fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0 || fbsBlock.getBlockId() == blockId)
                    && (fbsFloor == null || fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0 || fbsFloor.getFloorId() == floorId)
                    && (searchFbsFlat == null || searchFbsFlat.getUnitCode() == null || searchFbsFlat.getUnitCode() == 0 || searchFbsFlat.getUnitCode() == flatId)
                    && (fbsBank == null || fbsBank.getBankId() == null || fbsBank.getBankId() == 0 || fbsBank.getBankName().equalsIgnoreCase(clearingBank))
                    && ((chequeTransactionNo == null || (chequeNo != null && chequeNo.contains(chequeTransactionNo)))
                    || (chequeTransactionNo == null || (trancastionId != null && trancastionId.contains(chequeTransactionNo))))
                    && ((fbsAuthorizeBy == null) || (authorizeBy != null && authorizeBy.toLowerCase().contains(fbsAuthorizeBy.toLowerCase())))
                    && (fbsPaymentBy == null || (userId != null && userId.toLowerCase().contains(fbsPaymentBy.toLowerCase())))
                    && ((chequeDateFrom == null) || ((chequeDate != null) && ((chequeDate.after(chequeDateFrom)) || chequeDate.equals(chequeDateFrom))))
                    && ((chequeDateTo == null) || ((chequeDate != null) && ((chequeDate.before(chequeDateTo)) || chequeDate.equals(chequeDateTo))))
                    && ((clearingDateFrom == null) || ((clearingDate != null) && ((clearingDate.after(clearingDateFrom)) || (clearingDate.equals(clearingDateFrom)))))
                    && ((clearingDateTo == null) || ((clearingDate != null) && ((clearingDate.before(clearingDateTo)) || (clearingDate.equals(clearingDateTo)))))) {
                fbsBrPaymentList.add(brokerPaymentList.get(i));

            }

        }
        fbsBrPaymentList = brokerManager.sortBrokerPaymentList(fbsBrPaymentList);
        Collections.reverse(fbsBrPaymentList);

    }

    public void showPaymentStatus(FbsBrPayment fbsBrPayment) {
        fbsBank = new FbsBank();
        statusBrPayment = new FbsBrPayment();
        statusBrPayment = fbsBrPayment;
        if (statusBrPayment.getStatus().equalsIgnoreCase("Pending")) {
            renderPending = true;
        } else {
            renderPending = false;
        }
        renderDialog = true;

    }

    public String convertFloorNo(int floorNo) {
        return paymentManager.convertFloorNo(floorNo);
    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        paymentDateFrom = event.getDate();
        filterBrokerPayment();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        paymentDateTo = event.getDate();
        filterBrokerPayment();
    }

    public void handleDateSelectForChequeFromDate(DateSelectEvent event) {
        chequeDateFrom = event.getDate();
        filterBrokerPayment();

    }

    public void handleDateSelectForChequeToDate(DateSelectEvent event) {
        chequeDateTo = event.getDate();
        filterBrokerPayment();
    }

    public void handleDateSelectForStatusToDate(DateSelectEvent event) {
        clearingDateTo = event.getDate();
        filterBrokerPayment();
    }

    public void handleDateSelectForStatusFromDate(DateSelectEvent event) {
        clearingDateFrom = event.getDate();
        filterBrokerPayment();

    }

    public void handleDateSelect(DateSelectEvent event) {
        clearingDate = event.getDate();
        validChequeClearingDate();

    }

    public void validChequeClearingDate() {
        Date receivedDate = null;
        //((bookingDateFrom == null) || (bookingDate.after(bookingDateFrom)) || (bookingDate.equals(bookingDateFrom)))
        if (statusBrPayment != null && statusBrPayment.getPaymentId() != null) {
            receivedDate = statusBrPayment.getPaymentDate();
            if ((clearingDate != null) && (clearingDate.after(receivedDate) || clearingDate.equals(receivedDate))) {
                validChequeDate = true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Cheque Date", ""));
                validChequeDate = false;
            }

        }


    }

    public void bookingDetail(FbsBrPayment fbsBrPayment) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsBrPayment.getFbsBooking().getFbsFlat().getUnitCode() + "&redirectFlag=brokerPaymentList&name=Broker Payment List&url=/faces/jsfpages/Broker/brokerPayment.xhtml");

    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    public void populateFbsBroker() {
        filterBrokerPayment();
        fbsBrokerList.clear();
        fbsBookingList.clear();
        fbsBroker = new FbsBroker();
        fbsBooking = new FbsBooking();
        if (fbsBrokerCat != null && fbsBrokerCat.getCategoryId() != 0) {
            fbsBrokerCat = fbsBrokerCatFacade.find(fbsBrokerCat.getCategoryId());
            fbsBrokerList = (List<FbsBroker>) fbsBrokerCat.getFbsBrokerCollection();
            renderBrCategoryDetail = true;
            renderRegNumDetail = false;
        } else {
            fbsBrokerCat = new FbsBrokerCat();
            renderBrCategoryDetail = false;
            renderRegNumDetail = false;
        }
    }

    public void populateRegNum() {
        filterBrokerPayment();
        fbsBookingList.clear();
        fbsBooking = new FbsBooking();
        if (fbsBroker != null && fbsBroker.getBrokerId() != 0) {
            fbsBroker = fbsBrokerFacade.find(fbsBroker.getBrokerId());
            List<FbsBooking> fbsBookings = (List<FbsBooking>) fbsBroker.getFbsBookingCollection();
            fbsBookingList.clear();
            for (int i = 0; i < fbsBookings.size(); i++) {
                if (fbsBookings.get(i).getStatus().equals("booked")) {
                    fbsBookingList.add(fbsBookings.get(i));
                }
            }
            fbsBookingList = brokerManager.sortBookingList(fbsBookingList);
        } else {
            fbsBroker = new FbsBroker();
        }

    }

    public void renderMode(String mode) {
        if (mode.equals("Cash")) {
            renderModeCheque = false;
            renderModeNeft = false;
        }
        if (mode.equals("Cheque")) {
            renderModeCheque = true;
            renderModeNeft = false;
        }
        if (mode.equals("RTGS/NEFT")) {
            renderModeCheque = false;
            renderModeNeft = true;
        }
    }

    public void showBookingDetail() {
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            fbsBooking = fbsBookingFacade.find(fbsBooking.getRegNumber());
            renderRegNumDetail = true;
        } else {
            fbsBooking = new FbsBooking();
            renderRegNumDetail = false;
        }
    }

    public String convertFloorNo(FbsBooking fbsBooking) {
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            return blockManager.convertFloorNo(fbsBooking.getFbsFlat().getFbsFloor().getFloorNo());
        } else {
            return "";
        }
    }

    public void showAmount(String showMessage) {
        brokerPayableOutstanding = 0.0;
        double enteredAmount = 0.00;
        if (fbsBrPayment.getAmount() != null) {
            enteredAmount = 0.00;
            enteredAmount = fbsBrPayment.getAmount();
        } else if (selectedFbsBrPayment.getAmount() != null) {
            enteredAmount = 0.00;
            enteredAmount = selectedFbsBrPayment.getAmount();
        }
        System.out.println("entered amount+ " + enteredAmount);
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            System.out.println("i im in payment bean show amount method.................");
            fbsFlat = fbsBooking.getFbsFlat();
            FbsFlatType fbsFlatType = new FbsFlatType();
            fbsFlatType = fbsFlat.getFbsFlatType();
            FbsDiscount fbsDiscount = fbsBooking.getFbsDiscount();
            FbsPlanname fbsPlanname = fbsBooking.getFbsPlanname();
            FbsBrokerCat fbsBrokerCat = fbsBroker.getFbsBrokerCat();
            double brokerCommission = brokerManager.calculateBrokerCommission(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat);
            System.out.println("brokerCommission " + brokerCommission);
            double totalBrokerPaidAmount = brokerManager.calculateBrokerPaidAmount(fbsBooking);
            System.out.println("totalBrokerPaidAmount " + totalBrokerPaidAmount);
            double brokerPayableAmount = brokerManager.calculateBrokerPayableAmount(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat, fbsBooking);
            System.out.println("brokerPayableAmount " + brokerPayableAmount);
            if (!showMessage.equals("nonEdit")) {
                FbsBrPayment chkFbsBrPayment = new FbsBrPayment();
                chkFbsBrPayment = fbsBrPaymentFacade.find(selectedFbsBrPayment.getPaymentId());
                totalBrokerPaidAmount -= chkFbsBrPayment.getAmount();
            }
            brokerPayableOutstanding = brokerPayableAmount - totalBrokerPaidAmount;

            if ((!showMessage.equals("edit")) && (Math.floor(enteredAmount) > Math.floor(brokerPayableOutstanding))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Payable amount should be " + Math.floor(brokerPayableOutstanding), ""));
            }
        }

    }

    public void addBrokerPayment() {
        System.out.println("fbsBrPayment.getAmount() " + fbsBrPayment.getAmount() + fbsBank.getBankId());
        System.out.println("brokerPayabaleAmount " + brokerPayableOutstanding);
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            if (Math.floor(fbsBrPayment.getAmount()) <= Math.floor(brokerPayableOutstanding)) {

                brokerManager.addBrokerPayment(fbsBrPayment, fbsBooking, fbsBroker);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment added", ""));
                fbsBrPayment = new FbsBrPayment();
                fbsBooking = new FbsBooking();
                fbsBroker = new FbsBroker();
                fbsBrokerCat = new FbsBrokerCat();
                populate();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fields marked with * are mendatory", ""));

        }
    }

    public void setValuesOnBlur() {
        fbsBrPayment = fbsBrPayment;
    }

    public void editPaymentStatus() {
        if (renderPending == true) {
            statusBrPayment.setStatus("Cleared");
            statusBrPayment.setAuthorizedBy(LoginBean.fbsLogin.getUserId());
            //statusBrPayment.setClearingDt(new Date());

            if (statusBrPayment.getPaymentMode().equals("Cheque")) {
                if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0 && validChequeDate == true) {
                    fbsBank = fbsBankFacade.find(fbsBank.getBankId());
                    statusBrPayment.setClearingBank(fbsBank.getBankName());
                    statusBrPayment.setClearingDt(clearingDate);
                    fbsBrPaymentFacade.edit(statusBrPayment);
                    clearingDate = null;
                    fbsBank = new FbsBank();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Broker Payment Status Updated", ""));
                    renderDialog = false;
                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Clearing Bank & Valid Clearing Date", ""));
                }
            } else if (!statusBrPayment.getPaymentMode().equals("Cheque")) {
                statusBrPayment.setClearingDt(new Date());
                fbsBrPaymentFacade.edit(statusBrPayment);
                clearingDate = null;
                statusBrPayment = new FbsBrPayment();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Broker Payment Status Updated", ""));
                renderDialog = false;

            }
        } else {
            statusBrPayment.setStatus("Pending");
            statusBrPayment.setAuthorizedBy(null);
            statusBrPayment.setClearingDt(null);
            statusBrPayment.setClearingBank(null);
            fbsBrPaymentFacade.edit(statusBrPayment);
            statusBrPayment = new FbsBrPayment();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Broker Payment Status Updated", ""));
            renderDialog = false;

        }
        populate();

    }

    public void showbank() {
        System.out.println("fbsbank--->" + fbsBank.getBankId());

    }

    public void generateReceipt(FbsBrPayment fbsBrPayment) throws IOException {
        int paymentId = fbsBrPayment.getPaymentId();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/brokerPaymentReceipt?paymentId=" + paymentId);
    }

    public void confirmDeleteBrokerPayment(FbsBrPayment fbsBrPayment) {
        delFbsBrPayment = new FbsBrPayment();
        delFbsBrPayment = fbsBrPayment;
    }

    public void deleteBrokerPayment() {
        fbsBrPaymentFacade.remove(delFbsBrPayment);
        delFbsBrPayment = new FbsBrPayment();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Broker Payment Deleted", ""));
        populate();
    }

    public void showPaymentDetail(FbsBrPayment editFbsBrPayment) {
        fbsBrokerCat = new FbsBrokerCat();
        fbsBroker = new FbsBroker();
        fbsBroker = editFbsBrPayment.getFbsBooking().getFbsBroker();
        System.out.println("fbsbroker " + fbsBroker.getBrokerId());
        fbsBrokerCat = fbsBroker.getFbsBrokerCat();
        fbsBrokerList.clear();
        fbsBrokerList = (List<FbsBroker>) fbsBrokerCat.getFbsBrokerCollection();
        populateRegNum();
        fbsBooking = new FbsBooking();
        fbsBooking = fbsBookingFacade.find(editFbsBrPayment.getFbsBooking().getRegNumber());
        renderMode(editFbsBrPayment.getPaymentMode());
        selectedFbsBrPayment = new FbsBrPayment();
        selectedFbsBrPayment = editFbsBrPayment;
        renderDialog = true;
    }

    public void editBrokerPayment() {
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            showAmount("edit");
            if (Math.floor(selectedFbsBrPayment.getAmount()) <= Math.floor(brokerPayableOutstanding)) {

                brokerManager.editBrokerPayment(selectedFbsBrPayment, fbsBooking, fbsBroker);
                renderDialog = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Updated", ""));
                selectedFbsBrPayment = new FbsBrPayment();
                fbsBooking = new FbsBooking();
                fbsBroker = new FbsBroker();
                fbsBrokerCat = new FbsBrokerCat();
                populate();
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fields marked with * are mendatory", ""));

        }
        populate();
    }

    public void reset() {
        System.out.print("inside reset");
        fbsBroker = new FbsBroker();
        fbsBrPayment = new FbsBrPayment();
        fbsBooking = new FbsBooking();
        fbsBrokerCat = new FbsBrokerCat();
        searchFbsFlat = new FbsFlat();
        fbsBank = new FbsBank();
        fbsProject = new FbsProject();
        fbsFloor = new FbsFloor();
        fbsBlock = new FbsBlock();
        fbsFlat = new FbsFlat();
        fbsBrokerList = new ArrayList<FbsBroker>();
        fbsBookingList = new ArrayList<FbsBooking>();
        fbsBrokerCatList = new ArrayList<FbsBrokerCat>();
        fbsBankList = new ArrayList<FbsBank>();
        fbsProjectList = new ArrayList<FbsProject>();
        fbsFloorList = new ArrayList<FbsFloor>();
        fbsBlockList = new ArrayList<FbsBlock>();
        fbsFlatList = new ArrayList<FbsFlat>();
        fbsReceiptNo = null;
        fbsRegistrationNo = 0;
        paymentDateFrom = null;
        paymentDateTo = null;
        chequeDateFrom = null;
        chequeDateTo = null;
        clearingDateFrom = null;
        clearingDateTo = null;
        fbsAuthorizeBy = null;
        fbsPaymentBy = null;

        populate();
    }

    public void changeTypeOfSearching() {
        if ((searchType != null) && (searchType == 1)) {
            renderAdvanceSearchForm = false;
        } else if ((searchType != null) && (searchType == 2)) {
            renderAdvanceSearchForm = true;

        }
    }

    public void populateFbsBank() {

        if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0) {
            fbsBank = fbsBankFacade.find(fbsBank.getBankId());
        }
        filterBrokerPayment();
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

    public List<FbsBooking> getFbsBookingList() {
        return fbsBookingList;
    }

    public void setFbsBookingList(List<FbsBooking> fbsBookingList) {
        this.fbsBookingList = fbsBookingList;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    public FbsBrPayment getFbsBrPayment() {
        return fbsBrPayment;
    }

    public void setFbsBrPayment(FbsBrPayment fbsBrPayment) {
        this.fbsBrPayment = fbsBrPayment;
    }

    public boolean isRenderModeCheque() {
        return renderModeCheque;
    }

    public void setRenderModeCheque(boolean renderModeCheque) {
        this.renderModeCheque = renderModeCheque;
    }

    public boolean isRenderModeNeft() {
        return renderModeNeft;
    }

    public void setRenderModeNeft(boolean renderModeNeft) {
        this.renderModeNeft = renderModeNeft;
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

    public List<FbsBrPayment> getFbsBrPaymentList() {
        return fbsBrPaymentList;
    }

    public void setFbsBrPaymentList(List<FbsBrPayment> fbsBrPaymentList) {
        this.fbsBrPaymentList = fbsBrPaymentList;
    }

    public FbsBrPayment getStatusBrPayment() {
        return statusBrPayment;
    }

    public void setStatusBrPayment(FbsBrPayment statusBrPayment) {
        this.statusBrPayment = statusBrPayment;
    }

    public FbsBrokerCat getFbsBrokerCat() {
        return fbsBrokerCat;
    }

    public void setFbsBrokerCat(FbsBrokerCat fbsBrokerCat) {
        this.fbsBrokerCat = fbsBrokerCat;
    }

    public List<FbsBrokerCat> getFbsBrokerCatList() {
        return fbsBrokerCatList;
    }

    public void setFbsBrokerCatList(List<FbsBrokerCat> fbsBrokerCatList) {
        this.fbsBrokerCatList = fbsBrokerCatList;
    }

    public FbsBrPayment getSelectedFbsBrPayment() {
        return selectedFbsBrPayment;
    }

    public void setSelectedFbsBrPayment(FbsBrPayment selectedFbsBrPayment) {
        this.selectedFbsBrPayment = selectedFbsBrPayment;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public boolean isRenderBrCategoryDetail() {
        return renderBrCategoryDetail;
    }

    public void setRenderBrCategoryDetail(boolean renderBrCategoryDetail) {
        this.renderBrCategoryDetail = renderBrCategoryDetail;
    }

    public boolean isRenderRegNumDetail() {
        return renderRegNumDetail;
    }

    public void setRenderRegNumDetail(boolean renderRegNumDetail) {
        this.renderRegNumDetail = renderRegNumDetail;
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

    public String getFbsBrokerPaymentMode() {
        return fbsBrokerPaymentMode;
    }

    public void setFbsBrokerPaymentMode(String fbsBrokerPaymentMode) {
        this.fbsBrokerPaymentMode = fbsBrokerPaymentMode;
    }

    public Integer getFbsReceiptNo() {
        return fbsReceiptNo;
    }

    public void setFbsReceiptNo(Integer fbsReceiptNo) {
        this.fbsReceiptNo = fbsReceiptNo;
    }

    public Integer getFbsRegistrationNo() {
        return fbsRegistrationNo;
    }

    public void setFbsRegistrationNo(Integer fbsRegistrationNo) {
        this.fbsRegistrationNo = fbsRegistrationNo;
    }

    public Date getPaymentDateFrom() {
        return paymentDateFrom;
    }

    public void setPaymentDateFrom(Date paymentDateFrom) {
        this.paymentDateFrom = paymentDateFrom;
    }

    public Date getPaymentDateTo() {
        return paymentDateTo;
    }

    public void setPaymentDateTo(Date paymentDateTo) {
        this.paymentDateTo = paymentDateTo;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
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

    public FbsFlat getSearchFbsFlat() {
        return searchFbsFlat;
    }

    public void setSearchFbsFlat(FbsFlat searchFbsFlat) {
        this.searchFbsFlat = searchFbsFlat;
    }

    public String getChequeTransactionNo() {
        return chequeTransactionNo;
    }

    public void setChequeTransactionNo(String chequeTransactionNo) {
        this.chequeTransactionNo = chequeTransactionNo;
    }

    public String getFbsAuthorizeBy() {
        return fbsAuthorizeBy;
    }

    public void setFbsAuthorizeBy(String fbsAuthorizeBy) {
        this.fbsAuthorizeBy = fbsAuthorizeBy;
    }

    public String getFbsPaymentBy() {
        return fbsPaymentBy;
    }

    public void setFbsPaymentBy(String fbsPaymentBy) {
        this.fbsPaymentBy = fbsPaymentBy;
    }

    public Date getChequeDateFrom() {
        return chequeDateFrom;
    }

    public void setChequeDateFrom(Date chequeDateFrom) {
        this.chequeDateFrom = chequeDateFrom;
    }

    public Date getChequeDateTo() {
        return chequeDateTo;
    }

    public void setChequeDateTo(Date chequeDateTo) {
        this.chequeDateTo = chequeDateTo;
    }

    public Date getClearingDateFrom() {
        return clearingDateFrom;
    }

    public void setClearingDateFrom(Date clearingDateFrom) {
        this.clearingDateFrom = clearingDateFrom;
    }

    public Date getClearingDateTo() {
        return clearingDateTo;
    }

    public void setClearingDateTo(Date clearingDateTo) {
        this.clearingDateTo = clearingDateTo;
    }

    public boolean isRenderPending() {
        return renderPending;
    }

    public void setRenderPending(boolean renderPending) {
        this.renderPending = renderPending;
    }

    public Date getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }
}
