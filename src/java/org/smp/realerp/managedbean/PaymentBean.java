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
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.CalculationManager;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.session.FbsBankFacade;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsPaymentFacade;
import org.primefaces.event.DateSelectEvent;
import org.smp.realerp.session.*;
import org.smp.realerp.pojo.RerpUtil;

/**
 *
 * @author smp
 */
@ManagedBean(name = "paymentBean")
@ViewScoped
public class PaymentBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    BlockManager blockManager;
    @EJB
    CalculationManager calculationManager;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsBankFacade fbsBankFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    RerpUtil utility;
    FbsCompany fbsCompany = new FbsCompany();
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    List<FbsPayment> filteredPaymentList = new ArrayList<FbsPayment>();
    FbsProject fbsProject = new FbsProject();
    FbsBooking fbsBooking = new FbsBooking();
    FbsPayment fbsPayment = new FbsPayment();
    boolean renderDetail;
    boolean renderCheque;
    boolean renderNeft;
    double totalPayableAmount = 0.0;
    boolean renderDialog = false;
    List<FbsBank> fbsBankList = new ArrayList<FbsBank>();
    FbsPayment statusFbsPayment = new FbsPayment();
    FbsBank fbsBank = new FbsBank();
    FbsPayment delFbsPayment = new FbsPayment();
    Integer fbsReceiptNo;
    Integer fbsRegistrationNo;
    String fbsPaymentMode;
    Date paymentDateFrom;
    Date paymentDateTo;
    boolean renderAdvanceSearchForm;
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    boolean renderProject;
    String chequeTransactionNo;
    String fbsAuthorizeBy;
    String fbsPaymentBy;
    Date chequeDateFrom;
    Date chequeDateTo;
    Date clearingDateFrom;
    Date clearingDateTo;
    Integer searchType = 1;
    DecimalFormat format = new DecimalFormat("0.00");
    boolean renderPending;
    Date clearingDate;
    boolean validChequeDate;

    @PostConstruct
    public void populate() {

        fbsCompany = new FbsCompany();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsPaymentList.clear();
        fbsBankList.clear();
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            fbsProject = new FbsProject();
            renderProject = true;
            fbsProjectList.clear();
            fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
        } else {
            fbsProject = new FbsProject();
            fbsProject = LoginBean.fbsProject;
            populateBlock();
            renderProject = false;
            fbsProjectList.clear();
            fbsProjectList.add(fbsProject);
        }
        int projectId;
        int companyId;
        fbsPaymentList = fbsPaymentFacade.findAll();
        filterPayment();



    }

    public void handleDateSelectForFromDate(DateSelectEvent event) {
        paymentDateFrom = event.getDate();
        filterPayment();

    }

    public void handleDateSelectForToDate(DateSelectEvent event) {
        paymentDateTo = event.getDate();
        filterPayment();

    }

    public void handleDateSelectForChequeFromDate(DateSelectEvent event) {
        chequeDateFrom = event.getDate();
        filterPayment();

    }

    public void handleDateSelectForChequeToDate(DateSelectEvent event) {
        chequeDateTo = event.getDate();
        filterPayment();

    }

    public void handleDateSelectForStatusFromDate(DateSelectEvent event) {
        clearingDateFrom = event.getDate();
        filterPayment();

    }

    public void handleDateSelectForStatusToDate(DateSelectEvent event) {
        clearingDateTo = event.getDate();
        filterPayment();

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
        filterPayment();
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
        filterPayment();
    }

    public void populateFlat() {
        fbsFlatList.clear();
        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());
            //@NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "booked").getResultList();

        }
        fbsFlatList = blockManager.sortFlatList(fbsFlatList);
        filterPayment();
    }

    public void populatePayment() {

        if (fbsFlat.getUnitCode() != 0) {
            fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        }
        filterPayment();
    }

    public void populateFbsBank() {

        if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0) {
            fbsBank = fbsBankFacade.find(fbsBank.getBankId());
        }
        filterPayment();
    }

    public void filterPayment() {
        filteredPaymentList.clear();
        int receiptNo;
        int regNum;
        String paymentMode;
        Date paymentDate;
        int projectId;
        int blockId;
        int floorId;
        int flatId;
        int companyId;
        String clearingBank;
        String chequeNo;
        String trancastionId;
        String authorizeBy;
        String userId;
        Date chequeDate;
        Date clearingDate;
        for (int i = 0; i < fbsPaymentList.size(); i++) {
            companyId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId();
            receiptNo = fbsPaymentList.get(i).getPaymentId();
            regNum = fbsPaymentList.get(i).getFbsBooking().getRegNumber();
            paymentMode = fbsPaymentList.get(i).getPaymentMode();
            paymentDate = fbsPaymentList.get(i).getPaymentDate();
            projectId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            blockId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            floorId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFloorId();
            flatId = fbsPaymentList.get(i).getFbsBooking().getFbsFlat().getUnitCode();
            clearingBank = fbsPaymentList.get(i).getClearingBank();
            chequeNo = fbsPaymentList.get(i).getChequeNo();
            trancastionId = fbsPaymentList.get(i).getTransactionId();
            authorizeBy = fbsPaymentList.get(i).getAuthorizedBy();
            userId = fbsPaymentList.get(i).getUserId();
            chequeDate = fbsPaymentList.get(i).getChequeDate();
            clearingDate = fbsPaymentList.get(i).getClearingDt();
            if ((fbsPaymentList.get(i).getFbsBooking().getStatus().equalsIgnoreCase("booked")) && (companyId == fbsCompany.getCompanyId())
                    && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)
                    && (fbsReceiptNo == null || String.valueOf(receiptNo).contains(String.valueOf(fbsReceiptNo))) && (fbsRegistrationNo == null || String.valueOf(regNum).contains(String.valueOf(fbsRegistrationNo)))
                    && (fbsPaymentMode == null || fbsPaymentMode.equalsIgnoreCase(paymentMode))
                    && ((paymentDateFrom == null) || (paymentDate.after(paymentDateFrom)) || (paymentDate.equals(paymentDateFrom)))
                    && ((paymentDateTo == null) || (paymentDate.before(paymentDateTo)) || (paymentDate.equals(paymentDateTo)))
                    && (fbsBlock == null || fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0 || fbsBlock.getBlockId() == blockId)
                    && (fbsFloor == null || fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0 || fbsFloor.getFloorId() == floorId)
                    && (fbsFlat == null || fbsFlat.getUnitCode() == null || fbsFlat.getUnitCode() == 0 || fbsFlat.getUnitCode() == flatId)
                    && (fbsBank == null || fbsBank.getBankId() == null || fbsBank.getBankId() == 0 || fbsBank.getBankName().equalsIgnoreCase(clearingBank))
                    && ((chequeTransactionNo == null || (chequeNo != null && chequeNo.contains(chequeTransactionNo)))
                    || (chequeTransactionNo == null || (trancastionId != null && trancastionId.contains(chequeTransactionNo))))
                    && (fbsAuthorizeBy == null || (authorizeBy != null && authorizeBy.toLowerCase().contains(fbsAuthorizeBy.toLowerCase())))
                    && (fbsPaymentBy == null || (userId != null && userId.toLowerCase().contains(fbsPaymentBy.toLowerCase())))
                    && ((chequeDateFrom == null) || ((chequeDate != null) && ((chequeDate.after(chequeDateFrom)) || (chequeDate.equals(chequeDateFrom)))))
                    && ((chequeDateTo == null) || ((chequeDate != null) && ((chequeDate.before(chequeDateTo)) || (chequeDate.equals(chequeDateTo)))))
                    && ((clearingDateFrom == null) || ((clearingDate != null) && ((clearingDate.after(clearingDateFrom)) || (clearingDate.equals(clearingDateFrom)))))
                    && ((clearingDateTo == null) || ((clearingDate != null) && ((clearingDate.before(clearingDateTo)) || (clearingDate.equals(clearingDateTo)))))) {
                filteredPaymentList.add(fbsPaymentList.get(i));
            }

        }
        filteredPaymentList = paymentManager.sortPaymentList(filteredPaymentList);
        Collections.reverse(filteredPaymentList);


    }

    public String convertFloorNo(int floorNo) {
        return paymentManager.convertFloorNo(floorNo);
    }

    public void populateRegNum() {
        System.out.println("populate reg num" + fbsBooking.getRegNumber());
        if (fbsBooking != null && fbsBooking.getRegNumber() != null) {
            renderDetail = true;
            fbsBooking = fbsBookingFacade.find(fbsBooking.getRegNumber());
            if (fbsBooking != null) {
                int projectId = fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
                if ((fbsBooking.getStatus().equalsIgnoreCase("booked")) && (fbsProject == null || fbsProject.getProjId() == null || fbsProject.getProjId() == 0 || fbsProject.getProjId() == projectId)) {
                } else {
                    renderDetail = false;
                    fbsBooking = new FbsBooking();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Registration Number", ""));
                }

            } else {
                renderDetail = false;
                fbsBooking = new FbsBooking();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration Number not exist", ""));
            }
        } else {
            fbsBooking = new FbsBooking();
            renderDetail = false;
        }
    }

    public void renderModeDetails(String paymentMode) {
        if (paymentMode.equals("Cheque")) {
            renderCheque = true;
            renderNeft = false;
        } else if (paymentMode.equals("Cash")) {
            renderCheque = false;
            renderNeft = false;
        } else if (paymentMode.equals("RTGS/NEFT")) {
            renderCheque = false;
            renderNeft = true;
        }
    }

    public void setOnBlur() {
        fbsPayment = fbsPayment;
    }

    public String showFloorNo(FbsFlat fbsFlat) {
        if (fbsFlat != null && fbsFlat.getUnitCode() != null && fbsFlat.getUnitCode() != 0) {
            return blockManager.convertFloorNo(fbsFlat.getFbsFloor().getFloorNo());
        } else {
            return "";
        }
    }

    public void checkPaidAmount(String showMessage) {
        double enteredAmount = 0.00;
        double perServiceTax = 0.0;
        List<FbsServicetax> servicetaxs = new ArrayList<FbsServicetax>();
        if (fbsPayment.getPaidAmount() != null) {
            enteredAmount = 0.00;
            enteredAmount = fbsPayment.getPaidAmount();
            servicetaxs = em.createNamedQuery("FbsServicetax.findByDate").setParameter("bookingDate", fbsPayment.getPaymentDate()).getResultList();
            if (!servicetaxs.isEmpty()) {
                perServiceTax = servicetaxs.get(0).getServicetax();
            }
        }
        FbsPayment findAmount = new FbsPayment();
        if (fbsPayment != null && fbsPayment.getPaymentId() != null) {
            findAmount = fbsPaymentFacade.find(fbsPayment.getPaymentId());
        }
        if (fbsBooking != null && fbsBooking.getRegNumber() != null) {
            FbsFlatType fbsFlatType = new FbsFlatType();
            fbsFlatType = fbsBooking.getFbsFlat().getFbsFlatType();
            FbsDiscount fbsDiscount = fbsBooking.getFbsDiscount();
            FbsPlanname fbsPlanname = fbsBooking.getFbsPlanname();
            List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
            fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
            List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();
            fbsPlcAllotList = (List<FbsPlcAllot>) fbsBooking.getFbsFlat().getFbsPlcAllotCollection();
            List<FbsCharge> fbsChargesList = new ArrayList<FbsCharge>();
            fbsChargesList = (List<FbsCharge>) fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsChargeCollection();
            double flatCost = calculationManager.calculateTotalCost(fbsFlatType, fbsPlanname, fbsDiscount, fbsParkingList, fbsPlcAllotList, fbsChargesList);
            List<FbsPayment> paymentList = new ArrayList<FbsPayment>();
            paymentList = (List<FbsPayment>) fbsBooking.getFbsPaymentCollection();
            double calPaidAmount = calculationManager.calculateTotalPaidAmount(paymentList);
            if (!showMessage.equals("nonEdit")) {
                System.out.println("inside if==>");
                calPaidAmount = calPaidAmount - findAmount.getPaidAmount();
                paymentList.remove(findAmount);
            }
            double serviceTax = calculationManager.calculateTotalServiceTaxAmount(paymentList);


            totalPayableAmount = 0.0;
            if (calPaidAmount == 0) {
                totalPayableAmount = flatCost + (flatCost * perServiceTax / 100);
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Information", "Payable amount should be " + format.format(totalPayableAmount) + " .(including servicetax)"));
            } else {
                System.out.println("servicetaxs.get(0).getServicetax()" + perServiceTax);
                double totalRemainingAmount = flatCost - (calPaidAmount - serviceTax);
                totalPayableAmount = totalRemainingAmount + (totalRemainingAmount * perServiceTax / 100);
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Information", "Payable amount should be " + format.format(totalPayableAmount) + " .(including servicetax)"));
            }
            if ((!showMessage.equals("edit")) && (Math.floor(enteredAmount) > Math.floor(totalPayableAmount))) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Payable amount should be " + Math.floor(totalPayableAmount) + " .(including servicetax of '" + perServiceTax + "'%)", ""));
            }
        }

    }

    public void addPayment() {
        try {

            if (fbsPayment.getPaidAmount() == null || fbsBooking == null || fbsBooking.getRegNumber() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fields marked with * are mendatory", ""));

            } else {

                if (Math.floor(fbsPayment.getPaidAmount()) <= Math.floor(totalPayableAmount)) {
                    paymentManager.addPayment(fbsPayment, fbsBooking);
                    fbsPayment = new FbsPayment();
                    fbsBooking = new FbsBooking();
                    renderCheque = false;
                    renderNeft = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Added", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
                }
            }

        } catch (Exception ex) {
            System.out.println("Ecxeption in add payment " + ex);
        }
        populate();
    }

    public void showPaymentDetail(FbsPayment editFbsPayment) {
        renderDialog = true;
        renderModeDetails(editFbsPayment.getPaymentMode());
        fbsPayment = new FbsPayment();
        fbsPayment = fbsPaymentFacade.find(editFbsPayment.getPaymentId());
        fbsBooking = new FbsBooking();
        fbsBooking = fbsBookingFacade.find(fbsPayment.getFbsBooking().getRegNumber());
    }

    public void editPayment() {
        System.out.println("edit payment booking id==> " + fbsBooking.getRegNumber());
        if (fbsBooking != null && fbsBooking.getRegNumber() != null && fbsBooking.getRegNumber() != 0) {
            checkPaidAmount("edit");
            if (Math.floor(fbsPayment.getPaidAmount()) <= Math.floor(totalPayableAmount)) {
                paymentManager.editPayment(fbsPayment, fbsBooking);
                fbsPayment = new FbsPayment();
                fbsBooking = new FbsBooking();
                renderCheque = false;
                renderNeft = false;
                renderDialog = false;
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Updated", ""));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fields marked with * are mendatory", ""));

        }
        populate();
    }

    public void showPaymentStatus(FbsPayment fbsPayment) {
        fbsBank = new FbsBank();
        statusFbsPayment = new FbsPayment();
        statusFbsPayment = fbsPayment;
        if (statusFbsPayment.getStatus().equalsIgnoreCase("Pending")) {
            renderPending = true;
        } else {
            renderPending = false;
        }
        renderDialog = true;

    }

    public void showbank() {
        System.out.println("fbsbank--->" + fbsBank.getBankId());

    }

    public void validChequeClearingDate() {
        Date receivedDate = null;
        //((bookingDateFrom == null) || (bookingDate.after(bookingDateFrom)) || (bookingDate.equals(bookingDateFrom)))
        if (statusFbsPayment != null && statusFbsPayment.getPaymentId() != null) {
            receivedDate = statusFbsPayment.getPaymentDate();
            if ((clearingDate != null) && (clearingDate.after(receivedDate) || clearingDate.equals(receivedDate))) {
                validChequeDate = true;
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Cheque Date", ""));
                validChequeDate = false;
            }

        }


    }

    public void handleDateSelect(DateSelectEvent event) {
        clearingDate = event.getDate();
        validChequeClearingDate();

    }

    public void editPaymentStatus() {
        if (renderPending == true) {
            statusFbsPayment.setStatus("Cleared");
            statusFbsPayment.setAuthorizedBy(LoginBean.fbsLogin.getUserId());

            if (statusFbsPayment.getPaymentMode().equals("Cheque")) {
                if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0 && validChequeDate == true) {
                    fbsBank = fbsBankFacade.find(fbsBank.getBankId());
                    statusFbsPayment.setClearingBank(fbsBank.getBankName());
                    statusFbsPayment.setClearingDt(clearingDate);
                    fbsPaymentFacade.edit(statusFbsPayment);
                    statusFbsPayment = new FbsPayment();
                    clearingDate = null;
                    fbsBank = new FbsBank();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                    renderDialog = false;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Clearing Bank & Valid Clearing Date", ""));
                }
            } else if (!statusFbsPayment.getPaymentMode().equals("Cheque")) {
                statusFbsPayment.setClearingDt(new Date());
                fbsPaymentFacade.edit(statusFbsPayment);
                clearingDate = null;
                statusFbsPayment = new FbsPayment();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                renderDialog = false;
            }

        } else {
            statusFbsPayment.setStatus("Pending");
            statusFbsPayment.setAuthorizedBy(null);
            statusFbsPayment.setClearingDt(null);
            statusFbsPayment.setClearingBank(null);
            fbsPaymentFacade.edit(statusFbsPayment);
            statusFbsPayment = new FbsPayment();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
            renderDialog = false;

        }

        populate();




    }

    public void delPayment(FbsPayment fbsPayment) {
        delFbsPayment = new FbsPayment();
        delFbsPayment = fbsPayment;
    }

    public void deletePayment() {
        paymentManager.deletePayment(delFbsPayment);
        delFbsPayment = new FbsPayment();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Deleted", ""));
        populate();
    }

    public void generateReceipt(FbsPayment fbsPayment) throws IOException {
        int paymentId = fbsPayment.getPaymentId();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/paymentReceipt?paymentId=" + paymentId);

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
        System.out.print("inside reset");
        fbsPayment = new FbsPayment();
        fbsBooking = new FbsBooking();
        fbsReceiptNo = null;
        fbsRegistrationNo = null;
        fbsPaymentMode = null;
        paymentDateFrom = null;
        paymentDateTo = null;
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        fbsProjectList = new ArrayList<FbsProject>();
        fbsFloorList = new ArrayList<FbsFloor>();
        fbsFlatList = new ArrayList<FbsFlat>();
        fbsBlockList = new ArrayList<FbsBlock>();
        renderProject = false;
        chequeTransactionNo = null;
        fbsAuthorizeBy = null;
        fbsPaymentBy = null;
        chequeDateFrom = null;
        chequeDateTo = null;
        clearingDateFrom = null;
        clearingDateTo = null;
        fbsBank = new FbsBank();
        populate();
    }

    public void bookingDetail(FbsPayment fbsPayment) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsPayment.getFbsBooking().getFbsFlat().getUnitCode() + "&redirectFlag=paymentList&name=Payment List&url=/faces/jsfpages/Payment/quickPayment.xhtml");

    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
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

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    public FbsPayment getFbsPayment() {
        return fbsPayment;
    }

    public void setFbsPayment(FbsPayment fbsPayment) {
        this.fbsPayment = fbsPayment;
    }

    public List<FbsPayment> getFbsPaymentList() {
        return fbsPaymentList;
    }

    public void setFbsPaymentList(List<FbsPayment> fbsPaymentList) {
        this.fbsPaymentList = fbsPaymentList;
    }

    public boolean isRenderCheque() {
        return renderCheque;
    }

    public void setRenderCheque(boolean renderCheque) {
        this.renderCheque = renderCheque;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public boolean isRenderNeft() {
        return renderNeft;
    }

    public void setRenderNeft(boolean renderNeft) {
        this.renderNeft = renderNeft;
    }

    public FbsPayment getStatusFbsPayment() {
        return statusFbsPayment;
    }

    public void setStatusFbsPayment(FbsPayment statusFbsPayment) {
        this.statusFbsPayment = statusFbsPayment;
    }

    public Integer getFbsReceiptNo() {
        return fbsReceiptNo;
    }

    public void setFbsReceiptNo(Integer fbsReceiptNo) {
        this.fbsReceiptNo = fbsReceiptNo;
    }

    public List<FbsPayment> getFilteredPaymentList() {
        return filteredPaymentList;
    }

    public void setFilteredPaymentList(List<FbsPayment> filteredPaymentList) {
        this.filteredPaymentList = filteredPaymentList;
    }

    public Integer getFbsRegistrationNo() {
        return fbsRegistrationNo;
    }

    public void setFbsRegistrationNo(Integer fbsRegistrationNo) {
        this.fbsRegistrationNo = fbsRegistrationNo;
    }

    public String getFbsPaymentMode() {
        return fbsPaymentMode;
    }

    public void setFbsPaymentMode(String fbsPaymentMode) {
        this.fbsPaymentMode = fbsPaymentMode;
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

    public boolean isRenderAdvanceSearchForm() {
        return renderAdvanceSearchForm;
    }

    public void setRenderAdvanceSearchForm(boolean renderAdvanceSearchForm) {
        this.renderAdvanceSearchForm = renderAdvanceSearchForm;
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

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
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

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
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
