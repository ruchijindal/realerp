/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.text.DecimalFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.managedbean.LoginBean;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class PaymentManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsServicetaxFacade fbsServicetaxFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsFlatTypeFacade fbsFlatTypeFacade;
    @EJB
    FbsDiscountFacade fbsDiscountFacade;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    @EJB
    FbsPlcAllotFacade fbsPlcAllotFacade;
    @EJB
    FbsParkingFacade fbsParkingFacade;
    FbsFlat fbsFlat = new FbsFlat();
    FbsFlatType fbsFlatType = new FbsFlatType();
    FbsPayment fbsPayment = new FbsPayment();
    FbsPayment obj = new FbsPayment();
    FbsLogin fbsLogin = new FbsLogin();
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsPlanname fbsPlanname = new FbsPlanname();
    FbsPlc fbsPlc = new FbsPlc();
    FbsPlcAllot fbsPlcAllot = new FbsPlcAllot();
    FbsServicetax fbsServiceTax = new FbsServicetax();
    List<FbsServicetax> servicetaxList = new ArrayList<FbsServicetax>();
    List<FbsPayment> paymentList = new ArrayList<FbsPayment>();
    List<FbsFlat> flatList = new ArrayList<FbsFlat>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();
    List<FbsCharge> fbsChargeList = new ArrayList<FbsCharge>();
    List<FbsParkingType> fbsParkingTypeList = new ArrayList<FbsParkingType>();
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    double totalPaidAmount = 0;
    int discountId = 0;
    double discountPercantage = 0;
    double totalpayoutstanding = 0.0;
    DecimalFormat format = new DecimalFormat("0.00");
    String status;
    Date clearingDate;
    int clearingBankId;

    public String convertFloorNo(int floorNo) {
        if (floorNo == 0) {
            return "Ground Floor";
        }
        if (floorNo == 1) {
            return "1st Floor";
        }
        if (floorNo == 2) {
            return "2nd Floor";
        }
        if (floorNo == 3) {
            return "3rd Floor";
        } else {
            return floorNo + "th Floor";
        }

    }

    public List<FbsFloor> sortFloorList(List<FbsFloor> fbsFloorList) {
        Collections.sort(fbsFloorList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsFloor f1 = (FbsFloor) o1;
                FbsFloor f2 = (FbsFloor) o2;
                return f1.getFloorNo().compareTo(f2.getFloorNo());
            }
        });
        return fbsFloorList;
    }

    public void addPayment(FbsPayment fbsPayment, FbsBooking fbsBooking) {
        try {
            
            fbsPayment.setPaidAmount(Math.floor(fbsPayment.getPaidAmount()));
            fbsPayment.setFbsBooking(fbsBooking);
            fbsPayment.setStatus("Pending");         
            fbsPayment.setUserId(LoginBean.fbsLogin.getUserId());
            
           
            List<FbsServicetax> servicetaxs = em.createNamedQuery("FbsServicetax.findByDate").setParameter("bookingDate", fbsPayment.getPaymentDate()).getResultList();
            if (servicetaxs.isEmpty()) {
                fbsPayment.setServiceTax(0.00);
            } else {
                fbsPayment.setServiceTax(servicetaxs.get(0).getServicetax());
            }
            fbsPaymentFacade.create(fbsPayment);
                    

        } catch (Exception ex) {
            System.out.println("Ecxeption in add payment " + ex);
        }
    }
    public void editPayment(FbsPayment fbsPayment, FbsBooking fbsBooking) {
        try {
             if(fbsPayment.getPaymentMode().equals("Cash"))
            {
                fbsPayment.setChequeNo(null);
                fbsPayment.setDrawnOn(null);
                fbsPayment.setChequeDate(null);
                fbsPayment.setTransactionId(null);
            }
            else if(fbsPayment.getPaymentMode().equals("Cheque"))
            {
                fbsPayment.setTransactionId(null);                
            }
            else if(fbsPayment.getPaymentMode().equals("RTGS/NEFT"))
            {
               fbsPayment.setChequeNo(null); 
               fbsPayment.setChequeDate(null);
            }
          
            fbsPayment.setPaidAmount(Math.floor(fbsPayment.getPaidAmount()));
            fbsPayment.setFbsBooking(fbsBooking);
            fbsPayment.setStatus("Pending");         
            fbsPayment.setUserId(LoginBean.fbsLogin.getUserId());
            
           
            List<FbsServicetax> servicetaxs = em.createNamedQuery("FbsServicetax.findByDate").setParameter("bookingDate", fbsPayment.getPaymentDate()).getResultList();
            if (servicetaxs.isEmpty()) {
                fbsPayment.setServiceTax(0.00);
            } else {
                fbsPayment.setServiceTax(servicetaxs.get(0).getServicetax());
            }
            fbsPaymentFacade.edit(fbsPayment);
                    

        } catch (Exception ex) {
            System.out.println("Ecxeption in add payment " + ex);
        }
    }    
   

    public void deletePayment(FbsPayment fbsPayment) {
        try {//fbsPayment=new FbsPayment();
            fbsPaymentFacade.remove(fbsPayment);
        } catch (Exception ex) {
            System.out.println("Exception in delete payment" + ex);
        }
    }

    public void editPayment(FbsPayment fbsPayment) {
        try {
            fbsPaymentFacade.edit(fbsPayment);
        } catch (Exception ex) {
            System.out.println("Exception in Payment editing " + ex);
        }
    }
    
    public List<FbsPayment> sortPaymentList(List<FbsPayment> fbsPaymentList) {
        Collections.sort(fbsPaymentList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsPayment f1 = (FbsPayment) o1;
                FbsPayment f2 = (FbsPayment) o2;
                return f1.getPaymentId().compareTo(f2.getPaymentId());
            }
        });
        return fbsPaymentList;
    }
}
