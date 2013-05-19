/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BookingManager;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.manager.PaymentManager;
import org.smp.realerp.pojo.RerpUtil;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsFlatFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "cancelledBookingDetailBean")
@ViewScoped
public class CancelledBookingDetailBean {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    BookingManager bookingManager;
    @EJB
    BlockManager blockManager;
    @EJB
    PaymentManager paymentManager;
    @EJB
    BrokerManager brokerManager;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    RerpUtil utility;
    FbsBooking fbsBooking = new FbsBooking();
    FbsFlat fbsFlat = new FbsFlat();
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsPlanname fbsPlanname = new FbsPlanname();
    List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();
    List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
    List<FbsApplicant> fbsApplicantList = new ArrayList<FbsApplicant>();
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    List<FbsBrPayment> fbsBrokerPaymentList = new ArrayList<FbsBrPayment>();
    List<File> applicantFileList = new ArrayList<File>();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant fbsCoApplicant = new FbsApplicant();
    FbsBroker fbsBroker = new FbsBroker();
    String uploadImagePath;
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (request.getParameter("regNum") != null) {
            int regNum = Integer.parseInt(request.getParameter("regNum"));
            fbsBooking = new FbsBooking();
            fbsBooking = fbsBookingFacade.find(regNum);
            fbsFlat = new FbsFlat();
            fbsFlat = fbsBooking.getFbsFlat();
            fbsDiscount = new FbsDiscount();
            fbsDiscount = fbsBooking.getFbsDiscount();
            fbsPlanname = new FbsPlanname();
            fbsPlanname = fbsBooking.getFbsPlanname();
            fbsPlcAllotList.clear();
            fbsPlcAllotList = (List<FbsPlcAllot>) fbsFlat.getFbsPlcAllotCollection();
            fbsParkingList.clear();
            fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
            populateApplicantDetails();
            populateFbsPaymentList();
            populateBrokerPaymentDetails();
            getApplicantFileListDetail();
        }
    }

    public void populateApplicantDetails() {

        fbsApplicantList.clear();
        fbsApplicantList = bookingManager.populateCancelledApplicantList(fbsFlat);
        System.out.println("size of list..........." + fbsApplicantList.size());
        for (int i = 0; i < fbsApplicantList.size(); i++) {
            if (fbsApplicantList.get(i).getApplicantFlag() == 1) {
                fbsApplicant = fbsApplicantList.get(i);
            }
            if (fbsApplicantList.get(i).getApplicantFlag() == 2) {
                fbsCoApplicant = fbsApplicantList.get(i);
            }
        }
    }

    public void populateFbsPaymentList() {
        fbsPaymentList.clear();
        fbsPaymentList = (List<FbsPayment>) fbsBooking.getFbsPaymentCollection();
        fbsPaymentList = paymentManager.sortPaymentList(fbsPaymentList);
        Collections.reverse(fbsPaymentList);
    }

    public void populateBrokerPaymentDetails() {
        fbsBroker = new FbsBroker();
        fbsBroker = fbsBooking.getFbsBroker();
        fbsBrokerPaymentList.clear();
        fbsBrokerPaymentList = (List<FbsBrPayment>) fbsBooking.getFbsBrPaymentCollection();
        fbsBrokerPaymentList = brokerManager.sortBrokerPaymentList(fbsBrokerPaymentList);
        Collections.reverse(fbsBrokerPaymentList);
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

    public void setImagePath(int applicantId) {
        uploadImagePath = "";
        uploadImagePath = "resources/documents/applicant_image/";
        uploadImagePath += applicantId + ".png";
        System.out.println("uploadImagePath==> " + uploadImagePath);
        renderDialog = true;
    }

    public Date lastModifiedDate(File file) {
        return new Date(file.lastModified());
    }

    public long fileSizeCount(File file) {
        return file.length();
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public String roundOfUptoTwoDecimal(Double value) {
        return utility.indianFormat(value);
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
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

    public List<FbsParking> getFbsParkingList() {
        return fbsParkingList;
    }

    public void setFbsParkingList(List<FbsParking> fbsParkingList) {
        this.fbsParkingList = fbsParkingList;
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

    public List<FbsPayment> getFbsPaymentList() {
        return fbsPaymentList;
    }

    public void setFbsPaymentList(List<FbsPayment> fbsPaymentList) {
        this.fbsPaymentList = fbsPaymentList;
    }

    public List<FbsBrPayment> getFbsBrokerPaymentList() {
        return fbsBrokerPaymentList;
    }

    public void setFbsBrokerPaymentList(List<FbsBrPayment> fbsBrokerPaymentList) {
        this.fbsBrokerPaymentList = fbsBrokerPaymentList;
    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    public List<File> getApplicantFileList() {
        return applicantFileList;
    }

    public void setApplicantFileList(List<File> applicantFileList) {
        this.applicantFileList = applicantFileList;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public String getUploadImagePath() {
        return uploadImagePath;
    }

    public void setUploadImagePath(String uploadImagePath) {
        this.uploadImagePath = uploadImagePath;
    }
    
    
    
}
