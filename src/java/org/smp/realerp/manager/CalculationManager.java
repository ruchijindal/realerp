/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.smp.realerp.entity.*;
import org.smp.realerp.session.*;
import org.smp.realerp.session.FbsProjectFacade;
import org.smp.realerp.pojo.RerpUtil;
/**
 *
 * @author SMP Technologies
 */
@Stateless
@LocalBean

public class CalculationManager {



    /**
     * Creates a new instance of CalculationManager
     */
    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsPlcAllotFacade fbsPlcAllotFacade;
    @EJB
    FbsParkingTypeFacade fbsParkingTypeFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsPayplanFacade fbsPayplanFacade;
    @EJB
    FbsBankFacade fbsBankFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    RerpUtil utility;
    DecimalFormat format = new DecimalFormat("##,###");
    DecimalFormat formatter = new DecimalFormat("##,###");
    String[] unitdo = {"", " One", " Two", " Three", " Four", " Five",
        " Six", " Seven", " Eight", " Nine", " Ten", " Eleven", " Twelve",
        " Thirteen", " Fourteen", " Fifteen", " Sixteen", " Seventeen",
        " Eighteen", " Nineteen"};
    String[] tens = {"", "Ten", " Twenty", " Thirty", " Forty", " Fifty",
        " Sixty", " Seventy", " Eighty", " Ninety"};
    String[] digit = {"", " Hundred", " Thousand", " Lakh", " Crore"};
    private Double Date;

    public CalculationManager() {
    }

    public double calculateBookingDiscount(FbsDiscount fbsDiscount, FbsFlatType fbsFlatType) {
        double bookingDiscount = 0;

        if (fbsDiscount != null) {
            bookingDiscount = calculateFlatBSP(fbsFlatType) * fbsDiscount.getPercentage() / 100;
        } else {
            bookingDiscount = 0;
        }
        return bookingDiscount;
    }

    public double calculateFlatBSP(FbsFlatType fbsFlatType) {
        double flatBSP;


        if (fbsFlatType.getFlatBsp() != 0 && fbsFlatType.getFlatSba() != 0) {
            flatBSP = fbsFlatType.getFlatBsp() * fbsFlatType.getFlatSba();
        } else {
            flatBSP = 0;
        }

        return flatBSP;
    }

    public double calculateFlatBSPAfterDiscount(FbsFlatType fbsFlatType, FbsPlanname fbsPlanname, FbsDiscount fbsDiscount) {
        double basicPriceAfterDiscount = 0;
        basicPriceAfterDiscount = calculateFlatBSP(fbsFlatType) - calculatePlanDiscount(fbsPlanname, fbsFlatType) - calculateBookingDiscount(fbsDiscount, fbsFlatType);
        return basicPriceAfterDiscount;

    }

    public double calculateOtherCharge(List<FbsCharge> chargeList) {
        double otherCharge = 0;


        for (int i = 0; i < chargeList.size(); i++) {
            otherCharge += chargeList.get(i).getAmount();
        }

        return otherCharge;
    }

    public double calculateParkingCharge(List<FbsParking> fbsParkingList) {
        double parkingCharge = 0;
        for (int i = 0; i < fbsParkingList.size(); i++) {
            parkingCharge += fbsParkingList.get(i).getCharges();
        }
        return parkingCharge;
    }

    public double calculatePlanDiscount(FbsPlanname fbsPlanname, FbsFlatType fbsFlatType) {
        double planDiscount = 0;

        if (fbsPlanname != null) {
            planDiscount = fbsPlanname.getDiscount() * calculateFlatBSP(fbsFlatType) / 100;
        } else {
            planDiscount = 0;
        }

        return planDiscount;


    }

    public double calculatePLCCharge(List<FbsPlcAllot> fbsPlcAllots, FbsFlatType fbsFlatType) {
        double plcCharge = 0;

        plcCharge = calculatePLC(fbsPlcAllots) * fbsFlatType.getFlatSba();


        return plcCharge;
    }

    public double calculatePLC(List<FbsPlcAllot> fbsPlcAllots) {
        double plc = 0;

        for (int i = 0; i < fbsPlcAllots.size(); i++) {
            plc += fbsPlcAllots.get(i).getFbsPlc().getPlcCharge();
        }

        return plc;
    }

    public double calculateClearedPaidAmount(List<FbsPayment> fbsPaymentList) {
        double paidAmount = 0;


        //fbsPaymentList=em.createNamedQuery("FbsPayment.findByFbsBooking").setParameter("fbsBooking", fbsBooking).getResultList();

        for (FbsPayment fbsPayment : fbsPaymentList) {
            if (fbsPayment.getStatus().equalsIgnoreCase("cleared")) {
                paidAmount += fbsPayment.getPaidAmount();
            }
        }



        return paidAmount;
    }

    public double calculateTotalPaidAmount(List<FbsPayment> fbsPaymentList) {
        double paidAmount = 0;


        //fbsPaymentList=em.createNamedQuery("FbsPayment.findByFbsBooking").setParameter("fbsBooking", fbsBooking).getResultList();

        for (FbsPayment fbsPayment : fbsPaymentList) {

            paidAmount += fbsPayment.getPaidAmount();

        }



        return paidAmount;
    }

    public double calulateRemainingAmountExcludingServiceTax(String registrationNumber) {
        double remainingAmountExcludingServiceTax = 0;

        return remainingAmountExcludingServiceTax;
    }

    public double calculateServiceTaxAmountOnCrearedAmount(List<FbsPayment> fbsPaymentList) {
        double serviceTaxAmount = 0;
//        double temp1;
//        double recievedAmount;

        for (FbsPayment fbsPayment : fbsPaymentList) {
//            temp1 = (1 + (double) fbsPayment.getServiceTax() / 100);
//            recievedAmount = fbsPayment.getPaidAmount() / temp1;
            if (fbsPayment.getStatus().equalsIgnoreCase("Cleared")) {
                serviceTaxAmount += fbsPayment.getPaidAmount() * fbsPayment.getServiceTax() / 100;        //(fbsPayment.getPaidAmount() - recievedAmount);
            }
        }

        return serviceTaxAmount;
    }

    public double calculateTotalServiceTaxAmount(List<FbsPayment> fbsPaymentList) {
        double serviceTaxAmount = 0;


        for (FbsPayment fbsPayment : fbsPaymentList) {


            serviceTaxAmount += fbsPayment.getPaidAmount() * fbsPayment.getServiceTax() / 100;        //(fbsPayment.getPaidAmount() - recievedAmount);
        }

        return serviceTaxAmount;
    }

    public double calculateTotalCost(FbsFlatType fbsFlatType, FbsPlanname fbsPlanname, FbsDiscount fbsDiscount, List<FbsParking> fbsParkingList, List<FbsPlcAllot> fbsPlcAllots, List<FbsCharge> chargeList) {
        double totalCost = 0;
        totalCost = calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount) + calculateParkingCharge(fbsParkingList) + calculatePLCCharge(fbsPlcAllots, fbsFlatType) + calculateOtherCharge(chargeList);

        return totalCost;
    }

    public FbsApplicant getFbsApplicant(FbsFlat fbsFlat, int flag) {
        Query query = (Query) em.createNamedQuery("FbsApplicant.findByFbsFlat&Status&ApplicantFlag");
        query.setParameter("fbsFlat", fbsFlat);
        query.setParameter("status", "booked");
        query.setParameter("applicantFlag", flag);

        FbsApplicant fbsApplicant = (FbsApplicant) query.getSingleResult();
        System.out.println("first Applicant Name " + fbsApplicant.getApplicantName());
        return fbsApplicant;
    }

    public FbsBooking getBooking(int registrationNumber) {
        return fbsBookingFacade.find(registrationNumber);
    }

    public FbsFlat getFbsFlat(int registrationNumber) {
        return getBooking(registrationNumber).getFbsFlat();
    }

    public FbsProject getProject(int registrationnumber) {


        FbsProject fbsProject = getFbsBlock(registrationnumber).getFbsProject();
        return fbsProject;

    }

    public FbsFlatType getFbsFlatType(int registrationnumber) {


        FbsFlatType fbsFlatType = getBooking(registrationnumber).getFbsFlat().getFbsFlatType();
        return fbsFlatType;
    }

    public FbsFloor getFbsFloor(int registartionnumber) {

        FbsFloor fbsFloor = getFbsFlat(registartionnumber).getFbsFloor();
        return fbsFloor;

    }

    public FbsBlock getFbsBlock(int registartionNumber) {
        FbsBlock fbsBlock = getFbsFloor(registartionNumber).getFbsBlock();
        return fbsBlock;
    }

    public List<FbsPlcAllot> getPLCAllotList(FbsFlat fbsFlat) {
        List<FbsPlcAllot> fbsPlcAllots = em.createNamedQuery("FbsPlcAllot.findByFbsFlat").setParameter("fbsFlat", fbsFlat).getResultList();
        return fbsPlcAllots;

    }

    public List<FbsParking> getParkingList(FbsBooking fbsBooking) {
        List<FbsParking> fbsParkingList = em.createNamedQuery("FbsParking.findByRegistrationNumber").setParameter("fbsBooking", fbsBooking).getResultList();
        return fbsParkingList;
    }

    public List<FbsCharge> getChargeList(FbsProject fbsProject) {
        List<FbsCharge> fbsChargeList = em.createNamedQuery("FbsCharge.findByProject").setParameter("fbsProject", fbsProject).getResultList();
        return fbsChargeList;

    }

    public List<FbsPayment> getFbsPaymentList(FbsBooking fbsBooking) {
        List<FbsPayment> fbsPayments = em.createNamedQuery("FbsPayment.findByFbsBooking").setParameter("fbsBooking", fbsBooking).getResultList();
        return fbsPayments;
    }

    public List<FbsPayment> getClearedFbsPaymentList(FbsBooking fbsBooking) {
        List<FbsPayment> fbsPayments = em.createNamedQuery("FbsPayment.findByFbsBookingAndStatus").setParameter("fbsBooking", fbsBooking).setParameter("status", "Cleared").getResultList();
        return fbsPayments;
    }

    public List<FbsPayplan> getFbsPayplanList(FbsPlanname fbsPlanname) {
        List<FbsPayplan> fbsPayplans = em.createNamedQuery("FbsPayplan.findByFbsPlanName").setParameter("fbsPlanname", fbsPlanname).getResultList();
        return fbsPayplans;
    }

    public double calculateBSPInstallmentAmount(FbsPayplan fbsPayplan, FbsFlatType fbsFlatType, FbsPlanname fbsPlanname, FbsDiscount fbsDiscount) {
        double bSPinstallmentAmount = 0;
        bSPinstallmentAmount = calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount) * fbsPayplan.getPercentage() / 100;

        return bSPinstallmentAmount;

    }

    public double calculatePLCInstallmentAmount(FbsPayplan fbsPayplan, List<FbsPlcAllot> fbsPlcAllots, FbsFlatType fbsFlatType) {
        double plcInstallment = 0;
        plcInstallment = calculatePLCCharge(fbsPlcAllots, fbsFlatType) * fbsPayplan.getPercentage() / 100;
        return plcInstallment;
    }

    public double calculateRecieveAmountBetweenDueDate(Date startDate, Date endDate, List<FbsPayment> fbsPayments) {
        double amountRecieved = 0;
        System.out.println("Start Date ->" + startDate + " EndDate -> " + endDate);
        if (startDate == null) {
            for (FbsPayment fbsPayment : fbsPayments) {
                System.out.println("payment date ->" + fbsPayment.getPaymentDate());
                if ((fbsPayment.getPaymentDate().equals(endDate) || (fbsPayment.getPaymentDate().before(endDate))) && fbsPayment.getStatus().equalsIgnoreCase("cleared")) {
                    amountRecieved += fbsPayment.getPaidAmount() - calculateServiceTaxOnPaidAmount(fbsPayment);
                }
            }


        } else if (endDate == null) {
            System.out.println("Got END DATE NULLLLLL still working");

        } else {
            for (FbsPayment fbsPayment : fbsPayments) {
                if ((fbsPayment.getPaymentDate().equals(startDate) || (fbsPayment.getPaymentDate().after(startDate))) && (fbsPayment.getPaymentDate().equals(endDate) || fbsPayment.getPaymentDate().before(endDate)) && fbsPayment.getStatus().equalsIgnoreCase("cleared")) {
                    amountRecieved += fbsPayment.getPaidAmount() - calculateServiceTaxOnPaidAmount(fbsPayment);
                }
            }
        }


        return amountRecieved;
    }

    public double calculateServiceTaxOnPaidAmount(FbsPayment fbsPayment) {
        double serviceTax = 0;

        serviceTax = fbsPayment.getPaidAmount() * fbsPayment.getServiceTax() / 100;


        return serviceTax;

    }

    public double calculatePaidAmountAfterServiceTaxStatusCleared(List<FbsPayment> fbsPayments) {
        double paidAmountAfterServiceTax = 0;
        for (FbsPayment fbsPayment : fbsPayments) {
            if (fbsPayment.getStatus().equalsIgnoreCase("cleared")) {
                paidAmountAfterServiceTax += fbsPayment.getPaidAmount() - calculateServiceTaxOnPaidAmount(fbsPayment);
            }
        }

        return paidAmountAfterServiceTax;
    }

    public FbsBank getBankById(int bankId) {
        FbsBank fbsBank = fbsBankFacade.find(bankId);
        System.out.println("Bank Id is " + bankId);
        System.out.println("Bank Name is " + fbsBank.getBankName());
        return fbsBank;
    }

    public List<FbsPayment> getPaymentListForCompany(FbsCompany fbsCompany) {
        List<FbsPayment> paymentList = fbsPaymentFacade.findAll();
        List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
        for (int i = 0; i < paymentList.size(); i++) {
            if (paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId() == fbsCompany.getCompanyId()) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }
        return fbsPaymentList;
    }

    public List<FbsPayment> getPaymentListBetweenStartDateAndEndDateForCompany(Date startDate, Date endDate, FbsCompany fbsCompany) {
        List<FbsPayment> paymentList = fbsPaymentFacade.findAll();
        List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
        for (int i = 0; i < paymentList.size(); i++) {
//System.out.println(fbsCompany.getCompanyId()+"    "+paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId());
//System.out.println(paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId().intValue()== fbsCompany.getCompanyId());
            //          System.out.println(" "+paymentList.get(i).getPaymentDate().equals(startDate)+"  "+paymentList.get(i).getPaymentDate().after(startDate)+"  "+paymentList.get(i).getPaymentDate().equals(endDate)+"  "+paymentList.get(i).getPaymentDate().before(endDate));
            if ((paymentList.get(i).getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getFbsCompany().getCompanyId().intValue() == fbsCompany.getCompanyId()) && (paymentList.get(i).getPaymentDate().equals(startDate) || paymentList.get(i).getPaymentDate().after(startDate)) && (paymentList.get(i).getPaymentDate().equals(endDate) || paymentList.get(i).getPaymentDate().before(endDate))) {
                fbsPaymentList.add(paymentList.get(i));
            }
        }

        System.out.println("List size is      " + fbsPaymentList.size());
        return fbsPaymentList;

    }

    public List<FbsProject> getProjectListCompanyWise(FbsCompany fbsCompany) {
        List<FbsProject> fbsProjects = em.createNamedQuery("FbsProject.findByCompany").setParameter("fbsCompany", fbsCompany).getResultList();
        return fbsProjects;
    }

    public FbsBooking getFbsBookingFromFbsFlatByStatus(FbsFlat fbsFlat, String status) {
        FbsBooking fbsBooking = new FbsBooking();

        if (fbsFlat != null) {
            fbsBooking = (FbsBooking) em.createNamedQuery("FbsBooking.findByFbsFlat&Status").setParameter("fbsFlat", fbsFlat).setParameter("status", status).getSingleResult();
            System.out.println("Registarionnumbehte  mb" + fbsBooking.getRegNumber());
        }

        return fbsBooking;

    }

    public String convertIntegerIntoWords(int num) {

        String Str = "", ltr = "";
        int len = 0, q = 0, r = 0;



        while (num > 0) {

            len = numberCount(num);

            //Take the length of the number and do letter conversion

            switch (len) {
                case 8:
                    q = num / 10000000;
                    r = num % 10000000;
                    ltr = twonum(q);
                    Str = Str + ltr + digit[4];
                    num = r;
                    break;

                case 7:
                case 6:
                    q = num / 100000;
                    r = num % 100000;
                    ltr = twonum(q);
                    Str = Str + ltr + digit[3];
                    num = r;
                    break;

                case 5:
                case 4:

                    q = num / 1000;
                    r = num % 1000;
                    ltr = twonum(q);
                    Str = Str + ltr + digit[2];
                    num = r;
                    break;

                case 3:


                    if (len == 3) {
                        r = num;
                    }
                    ltr = threenum(r);
                    Str = Str + ltr;
                    num = 0;
                    break;

                case 2:

                    ltr = twonum(num);
                    Str = Str + ltr;
                    num = 0;
                    break;

                case 1:
                    Str = Str + unitdo[num];
                    num = 0;
                    break;
                default:

                    num = 0;



            }
            if (num == 0) {
                System.out.println(Str);
            }
        }
        return Str;

    }

    int numberCount(int num) {
        int cnt = 0, r = 0;

        while (num > 0) {
            r = num % 10;
            cnt++;
            num = num / 10;
        }

        return cnt;
    }

    //Function for Conversion of two digit
    String twonum(int numq) {
        int numr, nq;
        String ltr = "";

        nq = numq / 10;
        numr = numq % 10;

        if (numq > 19) {
            ltr = ltr + tens[nq] + unitdo[numr];
        } else {
            ltr = ltr + unitdo[numq];
        }

        return ltr;
    }

    //Function for Conversion of three digit
    String threenum(int numq) {
        int numr, nq;
        String ltr = "";

        nq = numq / 100;
        numr = numq % 100;

        if (numr == 0) {
            ltr = ltr + unitdo[nq] + digit[1];
        } else {
            ltr = ltr + unitdo[nq] + digit[1] + " and" + twonum(numr);
        }
        return ltr;
        /* sysDate = Calendar.getInstance();
        sysDateStr = new SimpleDateFormat("dd-mmm-yy").format(curDate).toString();
         */


    }

    public FbsPayplan getCurrentInstallmentNumber(List<FbsPayplan> fbsPayplans) {

        FbsPayplan fbsPayplan = new FbsPayplan();
        Date date = new Date();
        System.out.println("payplan size " + fbsPayplans.size());
        for (FbsPayplan payplan : fbsPayplans) {
            System.out.println("date1 " + payplan.getDueDate().toString() + "    " + (payplan.getDueDate().equals(date) || payplan.getDueDate().after(date)));
            if (payplan.getDueDate().equals(date) || payplan.getDueDate().after(date)) {
                fbsPayplan = payplan;
                break;
            }
        }

        
        return fbsPayplan;

    }

    public String roundOfUptoTwoDecimal(Double value)

    {

            return utility.indianFormat(value);
    }
}