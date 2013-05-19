/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import com.mysql.jdbc.jmx.LoadBalanceConnectionGroupManager;
import java.io.*;
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
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.*;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.pojo.SendAttachmentMail;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@ManagedBean(name = "bookingDetailBean")
@ViewScoped
public class BookingDetailBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    CalculationManager calculationManager;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    BlockManager blockManager;
    @EJB
    BookingManager bookingManager;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    PaymentManager paymentManager;
    @EJB
    FbsBankFacade fbsBankFacade;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    BrokerManager brokerManager;
    @EJB
    FbsBrPaymentFacade fbsBrPaymentFacade;
    @EJB
    FbsParkingFacade fbsParkingFacade;
    @EJB
    RerpUtil utility;
    DecimalFormat format = new DecimalFormat("0.00");
    FbsFlat fbsFlat = new FbsFlat();
    FbsBooking fbsBooking = new FbsBooking();
    String breadCrumName;
    String url;
    boolean renderBreadCrum;
    boolean renderBookingButton;
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsPlanname fbsPlanname = new FbsPlanname();
    List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();
    List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
    List<FbsApplicant> fbsApplicantList = new ArrayList<FbsApplicant>();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant fbsCoApplicant = new FbsApplicant();
    int showEditDialog;
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    boolean renderDialog;
    FbsBank fbsBank = new FbsBank();
    List<FbsBank> fbsBankList = new ArrayList<FbsBank>();
    boolean renderPending;
    FbsPayment fbsPayment = new FbsPayment();
    FbsPayment delFbsPayment = new FbsPayment();
    double totalPayableAmount = 0.0;
    boolean renderCheque;
    boolean renderNeft;
    List<FbsBrPayment> fbsBrokerPaymentList = new ArrayList<FbsBrPayment>();
    FbsBroker fbsBroker = new FbsBroker();
    FbsBrPayment fbsBrPayment = new FbsBrPayment();
    FbsBrPayment delFbsBrPayment = new FbsBrPayment();
    double brokerPayableOutstanding = 0.0;
    List<File> applicantFileList = new ArrayList<File>();
    File delFile;
    String uploadImagePath;
    Date clearingDate;
    boolean validChequeDate;
    List<FbsParkingType> fbsParkingTypeList = new ArrayList<FbsParkingType>();
    Integer parkingAllotArray[];
    boolean checkParking;
    FbsParking delFbsParking = new FbsParking();
    String attachmentType;
    List<FbsReport> filteredFbsReportList = new ArrayList<FbsReport>();

    @PostConstruct
    public void populate() {
        System.out.println("inside populate....");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        fbsFlat = new FbsFlat();
        int unitCode = 0;
        if (request.getParameter("redirectFlag").equals("graphicalEnquiry")) {
            renderBreadCrum = true;
        } else {
            renderBreadCrum = false;
            breadCrumName = request.getParameter("name");
            url = request.getParameter("url");
        }
        if (request.getParameter("unitCode") != null) {
            unitCode = Integer.parseInt(request.getParameter("unitCode"));
        }
        fbsFlat = fbsFlatFacade.find(unitCode);
        fbsBooking = new FbsBooking();
        fbsBooking = bookingManager.populateFbsBooking(fbsFlat);
        if (fbsBooking == null) {
            renderBookingButton = true;
        } else {
            fbsDiscount = new FbsDiscount();
            fbsDiscount = fbsBooking.getFbsDiscount();
            fbsPlanname = new FbsPlanname();
            fbsPlanname = fbsBooking.getFbsPlanname();
            fbsPlcAllotList.clear();
            fbsPlcAllotList = (List<FbsPlcAllot>) fbsFlat.getFbsPlcAllotCollection();
            fbsParkingList.clear();
            fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
            filteredFbsReportList = (List<FbsReport>) fbsBooking.getFbsReportCollection();
            populateFbsPaymentList();
            populateBrokerPaymentDetails();
            populateApplicantDetails();
            getApplicantFileListDetail();
        }

    }

    public void populateParkingTypeList() {
        renderDialog = !renderDialog;
        fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        fbsParkingTypeList.clear();
        fbsParkingTypeList = (List<FbsParkingType>) fbsFlat.getFbsFloor().getFbsBlock().getFbsProject().getFbsParkingTypeCollection();
        parkingAllotArray = new Integer[fbsParkingTypeList.size()];
        for (int i = 0; i < fbsParkingTypeList.size(); i++) {
            fbsParkingTypeList.get(i).setNoOfParking(0);
            parkingAllotArray[i] = 0;
        }
        fbsBooking = new FbsBooking();
        fbsBooking = bookingManager.populateFbsBooking(fbsFlat);
        fbsParkingList.clear();
        fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
    }

    public boolean renderProjectBreadCrum() {
        if (LoginBean.fbsProject.getProjName().equals("Projects")) {
            return true;
        } else {
            return false;
        }

    }

    public int countAvailableParking(FbsParkingType fbsParkingType) {
        //@NamedQuery(name = "FbsParking.countAvailableParking", query = "SELECT f FROM FbsParking f WHERE f.fbsParkingType = :fbsParkingType AND f.status = :status")
        List<FbsParking> parkings = em.createNamedQuery("FbsParking.countAvailableParking").setParameter("fbsParkingType", fbsParkingType).getResultList();
        if (parkings.isEmpty()) {
            return 0;
        } else {
            return parkings.size();
        }
    }

    public void checkNoOfParking(FbsParkingType fbsParkingType) {
        if (fbsParkingType.getNoOfParking() > countAvailableParking(fbsParkingType)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Parking Not Available", ""));
        } else {
            parkingAllotArray[fbsParkingTypeList.indexOf(fbsParkingType)] = fbsParkingType.getNoOfParking();
        }

    }

    public void addParking() {
        for (int i = 0; i < fbsParkingTypeList.size(); i++) {
            if (fbsParkingTypeList.get(i).getNoOfParking() > countAvailableParking(fbsParkingTypeList.get(i))) {
                checkParking = true;
                break;
            }

        }
        if (checkParking == true) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Parking Not Available", ""));
        } else {
            bookingManager.allotParking(fbsParkingTypeList, parkingAllotArray, fbsBooking);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Allotted", ""));
        }
        populateParkingTypeList();

    }

    public void confirmDeleteParking(FbsParking fbsParking) {
        delFbsParking = new FbsParking();
        delFbsParking = fbsParking;
    }

    public void deleteParking() {
        delFbsParking.setFbsBooking(null);
        fbsParkingFacade.edit(delFbsParking);
        populateParkingTypeList();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Deleted", ""));
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public void consumerReport() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/ConsumerReport?registrationNumber=" + fbsBooking.getRegNumber() + "&requestType=nonEmail");
    }

    public void dueLetterReport() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport?registrationNumber=" + fbsBooking.getRegNumber() + "&requestType=nonEmail");


    }

    public void redirectBookingForm() throws IOException {
        System.out.println("fbsflat unitcode++ " + fbsFlat);
        if (fbsFlat.getLockStatus().equalsIgnoreCase("unlocked")) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Flat must be locked", ""));
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/booking.xhtml?unitCode=" + fbsFlat.getUnitCode());
        }

    }

    public void transferBooking() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/transferBooking.xhtml?regNum=" + fbsBooking.getRegNumber() + "&rFlag=2" + "&renderFlag=1");
    }

    public void cancelBooking() throws IOException {
        System.out.println("cancel booking");

        bookingManager.cancelBooking(fbsBooking);
        fbsBooking = new FbsBooking();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Booking Cancelled", ""));
        bookingDetail();
    }

    public void populateApplicantDetails() {
        showEditDialog = 0;
        fbsApplicantList.clear();
        fbsApplicantList = bookingManager.populateApplicantList(fbsFlat);
        for (int i = 0; i < fbsApplicantList.size(); i++) {
            if (fbsApplicantList.get(i).getApplicantFlag() == 1) {
                fbsApplicant = fbsApplicantList.get(i);
            }
            if (fbsApplicantList.get(i).getApplicantFlag() == 2) {
                fbsCoApplicant = fbsApplicantList.get(i);
            }
        }
    }

    public void editApplicantDeatil(int flag) {
        showEditDialog = flag;

    }

    public void updateApplicantDetails(int flag) {
        if (flag == 1) {
            fbsApplicantFacade.edit(fbsApplicant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Applicant Info Updated", ""));
        } else if (flag == 2) {
            fbsApplicantFacade.edit(fbsCoApplicant);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Co-Applicant Info Updated", ""));
        }
        populateApplicantDetails();
    }

    public void setImagePath(int applicantId) {
        uploadImagePath = "";
        uploadImagePath = "resources/documents/applicant_image/";
        uploadImagePath += applicantId + ".png";
        System.out.println("uploadImagePath==> " + uploadImagePath);
        renderDialog = true;
    }

    public void uploadImage(FileUploadEvent event) throws FileNotFoundException, IOException {
        System.out.println("inside upoadimageImage");
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        System.out.println("path" + path);
        path = path + uploadImagePath;
        System.out.println("inside upoadimageImage==> " + path);
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        // bookingDetail();

    }

    public void removeImage(String imagepath) throws IOException {
        System.out.println("inside removeImage");
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path + imagepath;
        System.out.println("inside removeImage==>" + path);
        File outputFile = new File(path);
        outputFile.delete();
        //bookingDetail();
    }

    public boolean imageExist(String imagepath) {
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path + imagepath;
        File outputFile = new File(path);
        return outputFile.exists();
    }

    public void populateFbsPaymentList() {
        fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        fbsBooking = new FbsBooking();
        fbsBooking = bookingManager.populateFbsBooking(fbsFlat);
        fbsPaymentList.clear();
        fbsPaymentList = (List<FbsPayment>) fbsBooking.getFbsPaymentCollection();
        fbsPaymentList = paymentManager.sortPaymentList(fbsPaymentList);
        Collections.reverse(fbsPaymentList);
    }

    public void showPaymentStatus(FbsPayment statusFbsPayment) {
        FbsCompany fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBankList.clear();
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        fbsBank = new FbsBank();
        fbsPayment = new FbsPayment();
        fbsPayment = statusFbsPayment;
        if (fbsPayment.getStatus().equalsIgnoreCase("Pending")) {
            renderPending = true;
        } else {
            renderPending = false;
        }
        renderDialog = true;

    }

    public void editPaymentStatus() {

        if (renderPending == true) {
            fbsPayment.setStatus("Cleared");
            fbsPayment.setAuthorizedBy(LoginBean.fbsLogin.getUserId());

            if (fbsPayment.getPaymentMode().equals("Cheque")) {
                if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0 && validChequeDate == true) {
                    fbsBank = fbsBankFacade.find(fbsBank.getBankId());
                    fbsPayment.setClearingBank(fbsBank.getBankName());
                    fbsPayment.setClearingDt(clearingDate);
                    fbsPaymentFacade.edit(fbsPayment);
                    fbsPayment = new FbsPayment();
                    clearingDate = null;
                    fbsBank = new FbsBank();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                    renderDialog = false;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Clearing Bank & Valid Clearing Date", ""));
                }
            } else if (!fbsPayment.getPaymentMode().equals("Cheque")) {
                fbsPayment.setClearingDt(new Date());
                fbsPaymentFacade.edit(fbsPayment);
                clearingDate = null;
                fbsPayment = new FbsPayment();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                renderDialog = false;
            }

        } else {
            fbsPayment.setStatus("Pending");
            fbsPayment.setAuthorizedBy(null);
            fbsPayment.setClearingDt(null);
            fbsPayment.setClearingBank(null);
            fbsPaymentFacade.edit(fbsPayment);
            fbsPayment = new FbsPayment();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
            renderDialog = false;

        }

        populateFbsPaymentList();


    }

    public void resetPayment() {
        renderCheque = false;
        renderNeft = false;
        fbsPayment = new FbsPayment();
    }

    public void showPaymentDetail(FbsPayment editFbsPayment) {
        renderDialog = true;
        renderModeDetails(editFbsPayment.getPaymentMode());
        fbsPayment = new FbsPayment();
        fbsPayment = fbsPaymentFacade.find(editFbsPayment.getPaymentId());
    }

    public void editPayment() {
        System.out.println("edit payment booking id==> " + fbsBooking.getRegNumber());

        checkPaidAmount("edit");
        if (Math.floor(fbsPayment.getPaidAmount()) <= Math.floor(totalPayableAmount)) {
            paymentManager.editPayment(fbsPayment, fbsBooking);
            fbsPayment = new FbsPayment();
            renderCheque = false;
            renderNeft = false;
            renderDialog = false;
            populateFbsPaymentList();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Updated", ""));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount" + totalPayableAmount, ""));
        }

    }

    public void generateReceipt(FbsPayment fbsPayment) throws IOException {
        int paymentId = fbsPayment.getPaymentId();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/paymentReceipt?paymentId=" + paymentId);

    }

    public void delPayment(FbsPayment fbsPayment) {
        delFbsPayment = new FbsPayment();
        delFbsPayment = fbsPayment;
    }

    public void deletePayment() {
        paymentManager.deletePayment(delFbsPayment);
        delFbsPayment = new FbsPayment();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Deleted", ""));
        populateFbsPaymentList();
    }

    public void showbank() {
        System.out.println("fbsbank--->" + fbsBank.getBankId());

    }

    public void renderModeDetails(String paymentMode) {
        System.out.print("render  +++++" + paymentMode);
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
        System.out.println("renderCheque" + renderCheque);
        System.out.println("renderNeft" + renderNeft);
    }

    public void setOnBlur() {
        fbsPayment = fbsPayment;
        fbsBrPayment = fbsBrPayment;
    }

    public void showAddPaymentForm() {
        renderDialog = true;
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
            System.out.print("add payment.....");
            if (fbsPayment.getPaidAmount() == null || fbsBooking == null || fbsBooking.getRegNumber() == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fields marked with * are mendatory", ""));

            } else {

                if (Math.floor(fbsPayment.getPaidAmount()) <= Math.floor(totalPayableAmount)) {
                    paymentManager.addPayment(fbsPayment, fbsBooking);
                    fbsPayment = new FbsPayment();
                    fbsBooking = new FbsBooking();
                    renderCheque = false;
                    renderNeft = false;
                    renderDialog = false;
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Added", ""));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
                }
            }

        } catch (Exception ex) {
            System.out.println("Ecxeption in add payment " + ex);
        }
        populateFbsPaymentList();

    }

    public void bookingDetail() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + fbsFlat.getUnitCode() + "&redirectFlag=bookingList&name=Booking List&url=/faces/jsfpages/Booking/bookingList.xhtml");
    }

    public void populateBrokerPaymentDetails() {
        fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
        fbsBooking = new FbsBooking();
        fbsBooking = bookingManager.populateFbsBooking(fbsFlat);
        fbsBroker = new FbsBroker();
        fbsBroker = fbsBooking.getFbsBroker();
        fbsBrokerPaymentList.clear();
        fbsBrokerPaymentList = (List<FbsBrPayment>) fbsBooking.getFbsBrPaymentCollection();
        fbsBrokerPaymentList = brokerManager.sortBrokerPaymentList(fbsBrokerPaymentList);
        Collections.reverse(fbsBrokerPaymentList);
    }

    public void showBrokerPaymentStatus(FbsBrPayment statusFbsBrPayment) {
        FbsCompany fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBankList.clear();
        fbsBankList = (List<FbsBank>) fbsCompany.getFbsBankCollection();
        fbsBank = new FbsBank();
        fbsBrPayment = new FbsBrPayment();
        fbsBrPayment = statusFbsBrPayment;
        if (fbsBrPayment.getStatus().equalsIgnoreCase("Pending")) {
            renderPending = true;
        } else {
            renderPending = false;
        }
        renderDialog = true;

    }

    public void editBrokerPaymentStatus() {

        if (renderPending == true) {
            fbsBrPayment.setStatus("Cleared");
            fbsBrPayment.setAuthorizedBy(LoginBean.fbsLogin.getUserId());

            if (fbsBrPayment.getPaymentMode().equals("Cheque")) {
                if (fbsBank != null && fbsBank.getBankId() != null && fbsBank.getBankId() != 0 && validChequeDate == true) {
                    fbsBank = fbsBankFacade.find(fbsBank.getBankId());
                    fbsBrPayment.setClearingBank(fbsBank.getBankName());
                    fbsBrPayment.setClearingDt(clearingDate);
                    fbsBrPaymentFacade.edit(fbsBrPayment);
                    fbsBrPayment = new FbsBrPayment();
                    clearingDate = null;
                    fbsBank = new FbsBank();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                    renderDialog = false;
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Select Clearing Bank & Valid Clearing Date", ""));
                }
            } else if (!fbsBrPayment.getPaymentMode().equals("Cheque")) {
                fbsBrPayment.setClearingDt(new Date());
                fbsBrPaymentFacade.edit(fbsBrPayment);
                clearingDate = null;
                fbsBrPayment = new FbsBrPayment();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
                renderDialog = false;
            }

        } else {
            fbsBrPayment.setStatus("Pending");
            fbsBrPayment.setAuthorizedBy(null);
            fbsBrPayment.setClearingDt(null);
            fbsBrPayment.setClearingBank(null);
            fbsBrPaymentFacade.edit(fbsBrPayment);
            fbsBrPayment = new FbsBrPayment();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Status Updated", ""));
            renderDialog = false;

        }

        populateBrokerPaymentDetails();

    }

    public void generateBrokerPaymentReceipt(FbsBrPayment fbsBrPayment) throws IOException {
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
        populateBrokerPaymentDetails();
    }

    public void checkBrokerAmount(String showMessage) {
        brokerPayableOutstanding = 0.0;
        double enteredAmount = 0.00;
        if (fbsBrPayment.getAmount() != null) {
            enteredAmount = 0.00;
            enteredAmount = fbsBrPayment.getAmount();
        }

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
            chkFbsBrPayment = fbsBrPaymentFacade.find(fbsBrPayment.getPaymentId());
            totalBrokerPaidAmount -= chkFbsBrPayment.getAmount();
        }
        brokerPayableOutstanding = brokerPayableAmount - totalBrokerPaidAmount;

        if ((!showMessage.equals("edit")) && (Math.floor(enteredAmount) > Math.floor(brokerPayableOutstanding))) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Payable amount should be " + Math.floor(brokerPayableOutstanding), ""));
        }


    }

    public void addBrokerPayment() {

        if (Math.floor(fbsBrPayment.getAmount()) <= Math.floor(brokerPayableOutstanding)) {
            brokerManager.addBrokerPayment(fbsBrPayment, fbsBooking, fbsBroker);
            renderDialog = false;
            renderCheque = false;
            renderNeft = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment added", ""));
            fbsBrPayment = new FbsBrPayment();
            populateBrokerPaymentDetails();

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
        }

    }

    public void resetBrokerPayment() {
        fbsBrPayment = new FbsBrPayment();
        renderCheque = false;
        renderNeft = false;

    }

    public void showBrokerPaymentDetail(FbsBrPayment editFbsBrPayment) {
        renderDialog = true;
        renderModeDetails(editFbsBrPayment.getPaymentMode());
        fbsBrPayment = new FbsBrPayment();
        fbsBrPayment = fbsBrPaymentFacade.find(editFbsBrPayment.getPaymentId());
    }

    public void editBrokerPayment() {
        checkBrokerAmount("edit");
        if (Math.floor(fbsBrPayment.getAmount()) <= Math.floor(brokerPayableOutstanding)) {
            brokerManager.editBrokerPayment(fbsBrPayment, fbsBooking, fbsBroker);
            renderDialog = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Payment Updated", ""));
            fbsBrPayment = new FbsBrPayment();

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Amount", ""));
        }

        populateBrokerPaymentDetails();
    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    void getApplicantFileListDetail() {
        applicantFileList.clear();
        String applicantDirectoryPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/") + "resources" + File.separator + "documents" + File.separator + "applicant_document" + File.separator + fbsApplicant.getApplicantId();
        System.out.println(fbsCoApplicant.getApplicantId() + "applicant directory path " + applicantDirectoryPath);
        File applicantFolder = new File(applicantDirectoryPath);

        if (!applicantFolder.exists()) {
            try {
                applicantFolder.mkdir();
            } catch (Exception e) {
                System.out.println("exception in creation of folder in current directory");
            }
        } else {

            File[] files = applicantFolder.listFiles();
            for (int i = 0; i < files.length; i++) {
                applicantFileList.add(files[i]);
            }

        }



    }

    public Date lastModifiedDate(File file) {
        return new Date(file.lastModified());
    }

    public long fileSizeCount(File file) {
        return file.length();
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //  path = path.replaceAll("build.*", "");
        path = path + "resources" + File.separator + "documents" + File.separator + "applicant_document" + File.separator + fbsApplicant.getApplicantId() + File.separator + inputFile.getFileName();//.substring(inputFile.getFileName().indexOf("."));
        File outputFile = new File(path);
        System.out.println("Path is  " + path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        getApplicantFileListDetail();
    }

    public void confirmRemoveDocument(File file) {
        delFile = file;
    }

    public void removeDocument() throws IOException {
        System.out.println("inside removeImage");
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        path = path + "resources" + File.separator + "documents" + File.separator + "applicant_document" + File.separator + fbsApplicant.getApplicantId() + File.separator + delFile.getName();//.substring(inputFile.getFileName().indexOf("."));
        File outputFile = new File(path);
        outputFile.delete();
        getApplicantFileListDetail();

    }

    public void validChequeClearingDate() {
        Date receivedDate = null;
        //((bookingDateFrom == null) || (bookingDate.after(bookingDateFrom)) || (bookingDate.equals(bookingDateFrom)))
        if (fbsPayment != null && fbsPayment.getPaymentId() != null) {
            receivedDate = fbsPayment.getPaymentDate();

        }
        if (fbsBrPayment != null && fbsBrPayment.getPaymentId() != null) {
            receivedDate = fbsBrPayment.getPaymentDate();

        }
        if ((clearingDate != null) && (clearingDate.after(receivedDate) || clearingDate.equals(receivedDate))) {
            validChequeDate = true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Cheque Date", ""));
            validChequeDate = false;
        }
    }

    public void handleDateSelect(DateSelectEvent event) {
        clearingDate = event.getDate();
        validChequeClearingDate();

    }

    public void downloadDocument(String file) throws IOException {
        System.out.println("file name h " + file);

        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/DownloadFile?fileName=" + file);
    }

    public void previousApplicantDetails() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/transferBooking.xhtml?regNum=" + fbsBooking.getRegNumber() + "&rFlag=2" + "&renderFlag=2");

    }

    public void sendMailWithAttachment() throws IOException {
        String path = "";
        String relativeWebPath = "";
        if (LoginBean.fbsLogin.getFbsCompany().getFbsMailSettingCollection().isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Server Settings not found", ""));

        } else {
            System.out.println("attachmentType===> " + attachmentType);
            if (attachmentType.equals("Consumer Report")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/ConsumerReport?registrationNumber=" + fbsBooking.getRegNumber() + "&requestType=email");

                path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                relativeWebPath = "/resources/documents/" + "ConsumerReportTemp.pdf";
                path = path + relativeWebPath;
            }
            if (attachmentType.equals("Due Letter")) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport?registrationNumber=" + fbsBooking.getRegNumber() + "&requestType=email");

                path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
                relativeWebPath = "/resources/documents/" + "DueLetterTemp.pdf";
                path = path + relativeWebPath;
            }
            SendAttachmentMail sendMail = new SendAttachmentMail(fbsApplicant, path, attachmentType);
            Thread thread = new Thread(sendMail);
            thread.start();
        }
        bookingDetail();


    }

    public void dueLetterReportArchive(FbsReport fbsReport) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport?registrationNumber=" + fbsReport.getFbsBooking().getRegNumber() + "&requestType=nonEmail");


    }

    public String getBreadCrumName() {
        return breadCrumName;
    }

    public void setBreadCrumName(String breadCrumName) {
        this.breadCrumName = breadCrumName;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    public FbsDiscount getFbsDiscount() {
        return fbsDiscount;
    }

    public void setFbsDiscount(FbsDiscount fbsDiscount) {
        this.fbsDiscount = fbsDiscount;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public List<FbsParking> getFbsParkingList() {
        return fbsParkingList;
    }

    public void setFbsParkingList(List<FbsParking> fbsParkingList) {
        this.fbsParkingList = fbsParkingList;
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public void setFbsPlanname(FbsPlanname fbsPlanname) {
        this.fbsPlanname = fbsPlanname;
    }

    public List<FbsPlcAllot> getFbsPlcAllotList() {
        return fbsPlcAllotList;
    }

    public void setFbsPlcAllotList(List<FbsPlcAllot> fbsPlcAllotList) {
        this.fbsPlcAllotList = fbsPlcAllotList;
    }

    public boolean isRenderBookingButton() {
        return renderBookingButton;
    }

    public void setRenderBookingButton(boolean renderBookingButton) {
        this.renderBookingButton = renderBookingButton;
    }

    public boolean isRenderBreadCrum() {
        return renderBreadCrum;
    }

    public void setRenderBreadCrum(boolean renderBreadCrum) {
        this.renderBreadCrum = renderBreadCrum;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getShowEditDialog() {
        return showEditDialog;
    }

    public void setShowEditDialog(int showEditDialog) {
        this.showEditDialog = showEditDialog;
    }

    public List<FbsPayment> getFbsPaymentList() {
        return fbsPaymentList;
    }

    public void setFbsPaymentList(List<FbsPayment> fbsPaymentList) {
        this.fbsPaymentList = fbsPaymentList;
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

    public FbsPayment getFbsPayment() {
        return fbsPayment;
    }

    public void setFbsPayment(FbsPayment fbsPayment) {
        this.fbsPayment = fbsPayment;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public boolean isRenderPending() {
        return renderPending;
    }

    public void setRenderPending(boolean renderPending) {
        this.renderPending = renderPending;
    }

    public boolean isRenderCheque() {
        return renderCheque;
    }

    public void setRenderCheque(boolean renderCheque) {
        this.renderCheque = renderCheque;
    }

    public boolean isRenderNeft() {
        return renderNeft;
    }

    public void setRenderNeft(boolean renderNeft) {
        this.renderNeft = renderNeft;
    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    public List<FbsBrPayment> getFbsBrokerPaymentList() {
        return fbsBrokerPaymentList;
    }

    public void setFbsBrokerPaymentList(List<FbsBrPayment> fbsBrokerPaymentList) {
        this.fbsBrokerPaymentList = fbsBrokerPaymentList;
    }

    public FbsBrPayment getFbsBrPayment() {
        return fbsBrPayment;
    }

    public void setFbsBrPayment(FbsBrPayment fbsBrPayment) {
        this.fbsBrPayment = fbsBrPayment;
    }

    public List<File> getApplicantFileList() {
        return applicantFileList;
    }

    public void setApplicantFileList(List<File> applicantFileList) {
        this.applicantFileList = applicantFileList;
    }

    public String getUploadImagePath() {
        return uploadImagePath;
    }

    public void setUploadImagePath(String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }

    public Date getClearingDate() {
        return clearingDate;
    }

    public void setClearingDate(Date clearingDate) {
        this.clearingDate = clearingDate;
    }

    public List<FbsParkingType> getFbsParkingTypeList() {
        return fbsParkingTypeList;
    }

    public void setFbsParkingTypeList(List<FbsParkingType> fbsParkingTypeList) {
        this.fbsParkingTypeList = fbsParkingTypeList;
    }

    public String getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public List<FbsReport> getFilteredFbsReportList() {
        return filteredFbsReportList;
    }

    public void setFilteredFbsReportList(List<FbsReport> filteredFbsReportList) {
        this.filteredFbsReportList = filteredFbsReportList;
    }
}
