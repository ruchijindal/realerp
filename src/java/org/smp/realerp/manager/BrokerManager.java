/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
public class BrokerManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsBrokerCatFacade fbsBrokerCatFacade;
    @EJB
    FbsUserFacade fbsUserFacade;
    @EJB
    FbsBrPaymentFacade fbsBrPaymentFacade;
    @EJB
    FbsBankFacade fbsBankFacade;
    FbsUser fbsUser;
    @EJB
    CalculationManager calculationManager;

    public void addBroker(FbsBroker fbsBroker) {
        fbsBrokerFacade.create(fbsBroker);
    }

    public void deleteBroker(FbsBroker fbsBroker) {
        try {
            fbsBrokerFacade.remove(fbsBroker);
        } catch (Exception ex) {
            System.out.println("Exception in delete broker " + ex);
        }
    }

    public void editBroker(FbsBroker fbsBroker) {
        try {
            System.out.println("inside edit*" + fbsBroker.getFbsBrokerCat().getCategoryId());
            fbsBrokerFacade.edit(fbsBroker);
        } catch (Exception ex) {
            System.out.println("Exception in broker editing " + ex);
        }
    }

    //for broker category
    public void createBrokerCategory(FbsBrokerCat fbsBrokerCat) {
        fbsBrokerCatFacade.create(fbsBrokerCat);

    }

    public void deleteBrokerCategory(FbsBrokerCat fbsBrokerCat) {
        try {
            fbsBrokerCatFacade.remove(fbsBrokerCat);
        } catch (Exception ex) {
            System.out.println("Exception in delete broker category" + ex);
        }
    }

    public void editBrokerCategory(FbsBrokerCat fbsBrokerCat) {
        try {
            fbsBrokerCatFacade.edit(fbsBrokerCat);
        } catch (Exception ex) {
            System.out.println("Exception in brokerCategory editing " + ex);
        }
    }

    /**
     * ***************Broker Payment*********************
     */
    public List<FbsBroker> sortBrokerList(List<FbsBroker> fbsBrokerList) {
        Collections.sort(fbsBrokerList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                FbsBroker f1 = (FbsBroker) o1;
                FbsBroker f2 = (FbsBroker) o2;
                
                return f1.getBrName().compareTo(f2.getBrName());
            }
        });
        return fbsBrokerList;
    }

    public List<FbsBooking> sortBookingList(List<FbsBooking> fbsBookingList) {
        Collections.sort(fbsBookingList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                FbsBooking f1 = (FbsBooking) o1;
                FbsBooking f2 = (FbsBooking) o2;
                return f1.getRegNumber().compareTo(f2.getRegNumber());
            }
        });
        return fbsBookingList;
    }

    public void addBrokerPayment(FbsBrPayment fbsBrPayment, FbsBooking fbsBooking, FbsBroker fbsBroker) {
        try {
            fbsBrPayment.setAmount(Math.floor(fbsBrPayment.getAmount()));
            fbsBrPayment.setFbsBooking(fbsBooking);
            fbsBrPayment.setFbsBroker(fbsBroker);
            fbsBrPayment.setUserId(LoginBean.fbsLogin.getUserId());
            fbsBrPayment.setStatus("Pending");

            fbsBrPaymentFacade.create(fbsBrPayment);
        } catch (Exception ex) {
            System.out.println("Exception in adding BrokerPayment: " + ex);
        }
    }
    
    public void editBrokerPayment(FbsBrPayment fbsBrPayment, FbsBooking fbsBooking, FbsBroker fbsBroker) {
        try {
            fbsBrPayment.setAmount(Math.floor(fbsBrPayment.getAmount()));
            if(fbsBrPayment.getPaymentMode().equals("Cash"))
            {
                fbsBrPayment.setChequeNo(null);
                fbsBrPayment.setDrawnOn(null);
                fbsBrPayment.setChequeDate(null);
                fbsBrPayment.setTransactionId(null);
            }
            else if(fbsBrPayment.getPaymentMode().equals("Cheque"))
            {
                fbsBrPayment.setTransactionId(null);                
            }
            else if(fbsBrPayment.getPaymentMode().equals("RTGS/NEFT"))
            {
               fbsBrPayment.setChequeNo(null); 
               fbsBrPayment.setChequeDate(null);
            }
            fbsBrPayment.setFbsBooking(fbsBooking);
            fbsBrPayment.setFbsBroker(fbsBroker);
            fbsBrPayment.setUserId(LoginBean.fbsLogin.getUserId());
            fbsBrPayment.setStatus("Pending");

            fbsBrPaymentFacade.edit(fbsBrPayment);
        } catch (Exception ex) {
            System.out.println("Exception in editing BrokerPayment: " + ex);
        }
    }


    public double calculateBrokerPaidAmount(FbsBooking fbsBooking) {
        double totalBrokerPaidAmount = 0.0;
        List<FbsBrPayment> fbsBrokerPaymentList = new ArrayList<FbsBrPayment>();
        fbsBrokerPaymentList = (List<FbsBrPayment>) fbsBooking.getFbsBrPaymentCollection();
        for (int i = 0; i < fbsBrokerPaymentList.size(); i++) {
            totalBrokerPaidAmount += fbsBrokerPaymentList.get(i).getAmount();
        }
        return totalBrokerPaidAmount;

    }

    public double calculateBrokerCommission(FbsFlatType fbsFlatType, FbsPlanname fbsPlanname, FbsDiscount fbsDiscount, FbsBrokerCat fbsBrokerCat) {
        double flatCost = calculationManager.calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount);
        double brokerCommission = flatCost * fbsBrokerCat.getCommission() / 100;
        return brokerCommission;
    }

    public double calculateBrokerPayableAmount(FbsFlatType fbsFlatType, FbsPlanname fbsPlanname, FbsDiscount fbsDiscount, FbsBrokerCat fbsBrokerCat, FbsBooking fbsBooking) {
        double brokerCommissionPayableAmount = 0;
        double flatCost = calculationManager.calculateFlatBSPAfterDiscount(fbsFlatType, fbsPlanname, fbsDiscount);
        double calPaidAmount = calculationManager.calculateTotalPaidAmount((List<FbsPayment>) fbsBooking.getFbsPaymentCollection());
        double serviceTax = calculationManager.calculateTotalServiceTaxAmount((List<FbsPayment>) fbsBooking.getFbsPaymentCollection());
        double flatPaidAmount = calPaidAmount - serviceTax;       
        double brokerCommission = calculateBrokerCommission(fbsFlatType, fbsPlanname, fbsDiscount, fbsBrokerCat);
        double percantageOfFlatAmountPaid = 0;
        if (flatPaidAmount >= flatCost) {
            brokerCommissionPayableAmount = brokerCommission;
        } else {
            percantageOfFlatAmountPaid = flatPaidAmount / flatCost * 100;         
            int bspInstallment = (int) (percantageOfFlatAmountPaid / fbsBrokerCat.getBspPercent());           
            brokerCommissionPayableAmount = brokerCommission * (bspInstallment * fbsBrokerCat.getBrokerPercent()) / 100;
        }
        return brokerCommissionPayableAmount;
    }
     public List<FbsBrPayment> sortBrokerPaymentList(List<FbsBrPayment> fbsPaymentList) {
        Collections.sort(fbsPaymentList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsBrPayment f1 = (FbsBrPayment) o1;
                FbsBrPayment f2 = (FbsBrPayment) o2;
                return f1.getPaymentId().compareTo(f2.getPaymentId());
            }
        });
        return fbsPaymentList;
    }
}
