/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.entity.FbsPlc;
import org.smp.realerp.session.FbsPlcFacade;
import org.smp.realerp.session.*;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class ProjectManager {

    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsPlannameFacade fbsPlannameFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsFlatTypeFacade fbsFlatTypeFacade;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    @EJB
    FbsPayplanFacade fbsPayplanFacade;
    @EJB
    FbsChargeFacade fbsChargeFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsParkingTypeFacade fbsParkingTypeFacade;
    @EJB
    FbsParkingFacade fbsParkingFacade;

    public FbsProject populateFbsProject(int projectId) {
        return fbsProjectFacade.find(projectId);
    }

    /**
     * ****For Plan Name Setting******
     */
    public void createPlanname(FbsPlanname fbsPlanname, FbsProject fbsProject) {
        try {
            fbsPlannameFacade.create(fbsPlanname);
            fbsProject.setProjId(fbsProject.getProjId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editPlannameInfo(FbsPlanname fbsPlanname) {
        try {
            fbsPlannameFacade.edit(fbsPlanname);
        } catch (Exception ex) {
            System.out.println("Exception in editing Planname information " + ex);
        }
    }

    /**
     * ********For Plan Setting*********
     */
    public void createPlanname(FbsPlanname fbsPlanname) {
        try {
            fbsPlannameFacade.create(fbsPlanname);
        } catch (Exception ex) {
            System.out.println("Exception in create Planname " + ex);
        }

    }

    public void editPlanname(FbsPlanname fbsPlanname) {
        try {
            fbsPlannameFacade.edit(fbsPlanname);
        } catch (Exception ex) {
            System.out.println("Exception in Planname editing " + ex);
        }
    }

    public void deletePlanname(FbsPlanname fbsPlanname) {
        try {
            fbsPlannameFacade.remove(fbsPlanname);
        } catch (Exception ex) {
            System.out.println("Exception in delete Planname " + ex);
        }
    }

    /**
     * **********FLAT TYPE SETTINGS*******************
     */
    public void addFlatType(FbsFlatType fbsFlatType) {
        try {
            fbsFlatTypeFacade.create(fbsFlatType);
        } catch (Exception ex) {
            System.out.println("Exception in add flat type: " + ex);

        }
    }

    public void editFlatType(FbsFlatType fbsFlatType) {
        try {
            fbsFlatTypeFacade.edit(fbsFlatType);
        } catch (Exception ex) {
            System.out.println("Exception in edit flat type: " + ex);

        }
    }

    public void deleteFlatType(FbsFlatType fbsFlatType) {
        try {
            fbsFlatTypeFacade.remove(fbsFlatType);
        } catch (Exception ex) {
            System.out.println("Exception in edit flat type: " + ex);

        }
    }

    public boolean checkFlatType(String flatType) {
        List<FbsFlatType> fbsFlatTypes = em.createNamedQuery("FbsFlatType.findByFlatType").setParameter("flatType", flatType).getResultList();
        if (fbsFlatTypes.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * *********** For Plc Setting ******
     */
    public void addPlc(FbsPlc fbsPlc) {
        try {
            fbsPlcFacade.create(fbsPlc);
        } catch (Exception ex) {
            System.out.println("Exception in add PLC : " + ex);

        }
    }

    public void editPlc(FbsPlc fbsPlc) {
        try {
            fbsPlcFacade.edit(fbsPlc);
        } catch (Exception ex) {
            System.out.println("Exception in edit PLC : " + ex);

        }
    }

    public void deletePlc(FbsPlc fbsPlc) {
        try {
            fbsPlcFacade.remove(fbsPlc);
        } catch (Exception ex) {
            System.out.println("Exception in delete PLc: " + ex);

        }
    }

    public boolean checkPlcName(String plcName) {
        List<FbsPlc> fbsPlcs = em.createNamedQuery("FbsPlc.findByPlcName").setParameter("plcName", plcName).getResultList();
        if (fbsPlcs.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ******for Payment Plan******
     */
    public List<FbsPayplan> populatePayPlanList() {
        return fbsPayplanFacade.findAll();
    }

    public List<FbsPlanname> populatePlanNameList() {
        return fbsPlannameFacade.findAll();
    }

    public void addPaymentPlan(FbsPayplan fbsPayplan) {
        try {
            fbsPayplanFacade.create(fbsPayplan);
        } catch (Exception ex) {
            System.out.println("Exception in add paymentPlan: " + ex);
        }
    }

    public void editPaymentPlane(FbsPayplan fbsPayplan) {
        try {
            fbsPayplanFacade.edit(fbsPayplan);
        } catch (Exception ex) {
            System.out.println("Exception in edit plan name: " + ex);

        }
    }

    public void deletePaymentPlan(FbsPayplan fbsPayplan) {
        try {
            fbsPayplanFacade.remove(fbsPayplan);
        } catch (Exception ex) {
            System.out.println("Exception in delete plan name: " + ex);

        }
    }

    /**
     * ***For Charges****
     */
    public void addCharges(FbsCharge fbsCharge) {
        try {
            fbsChargeFacade.create(fbsCharge);

        } catch (Exception ex) {
            System.out.println("exception in add charges" + ex);
        }
    }

    public void editCharges(FbsCharge fbsCharge) {
        try {
            fbsChargeFacade.edit(fbsCharge);

        } catch (Exception ex) {
            System.out.println("exception in edit charges" + ex);
        }
    }

    public void deleteCharges(FbsCharge fbsCharge) {

        try {
            fbsChargeFacade.remove(fbsCharge);

        } catch (Exception ex) {
            System.out.println("exception in delete charges" + ex);
        }
    }

    public boolean checkChargeName(String chargeName) {
        List<FbsCharge> fbsCharges = em.createNamedQuery("FbsCharge.findByChargeName").setParameter("chargeName", chargeName).getResultList();
        if (fbsCharges.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * *****For Parking Type*******
     */
    public void deleteParkingType(FbsParkingType fbsParkingType) {
        try {
            fbsParkingTypeFacade.remove(fbsParkingType);
        } catch (Exception ex) {
            System.out.println("exception in delete Parking Type" + ex);
        }
    }

    /**
     * **************Parking Type Setting****************
     */
    public List<FbsParkingType> populateParkingTypeList() {
        return fbsParkingTypeFacade.findAll();
    }

    public void addParking(FbsParkingType fbsParkingType) {
        fbsParkingTypeFacade.create(fbsParkingType);
        FbsParking fbsParking = new FbsParking();
        for (int i = 0; i < fbsParkingType.getNoOfParking(); i++) {
            fbsParking = new FbsParking();
            fbsParking.setFbsParkingType(fbsParkingType);
            fbsParking.setCharges(fbsParkingType.getCharges());
            fbsParking.setStatus("unbooked");
            fbsParking.setName((i + 1) + "");
            fbsParkingFacade.create(fbsParking);

        }
    }

    public FbsParkingType returnParkingType(int id) {
        return fbsParkingTypeFacade.find(id);
    }

    public void editParkingType(FbsParkingType editFbsParkingType, FbsParkingType previousFbsParkingType) {
        fbsParkingTypeFacade.edit(editFbsParkingType);
        FbsParking fbsParking = new FbsParking();
//            System.out.println("fbsParkingType1.getNoOfParking()" + fbsParkingType1.getNoOfParking());
//            System.out.println("fbsParkingType.getNoOfParking()" + fbsParkingType.getNoOfParking());
        for (int i = previousFbsParkingType.getNoOfParking(); i < editFbsParkingType.getNoOfParking(); i++) {
            System.out.println("inside for++++++++++");
            fbsParking = new FbsParking();
            fbsParking.setFbsParkingType(editFbsParkingType);
            fbsParking.setCharges(editFbsParkingType.getCharges());
            fbsParking.setStatus("unbooked");
            fbsParking.setName((i + 1) + "");
            fbsParkingFacade.create(fbsParking);
        }

    }

    public void deleteParking(FbsParking fbsParking) {
        FbsParkingType fbsParkingType = new FbsParkingType();
        fbsParkingType = fbsParkingTypeFacade.find(fbsParking.getFbsParkingType().getParkingTypeId());
        fbsParkingFacade.remove(fbsParking);
        fbsParkingType.setNoOfParking(fbsParkingType.getNoOfParking() - 1);
        fbsParkingTypeFacade.edit(fbsParkingType);
    }

    public List<FbsParking> sortParkingList(List<FbsParking> fbsParkingList) {
        Collections.sort(fbsParkingList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsParking f1 = (FbsParking) o1;
                FbsParking f2 = (FbsParking) o2;
                Integer name1 = Integer.valueOf(f1.getName());
                Integer name2 = Integer.valueOf(f2.getName());
                return name1.compareTo(name2);
            }
        });
        return fbsParkingList;
    }

    public FbsParkingType populateParkingList(FbsParking fbsParking) {
        FbsParkingType fbsParkingType = fbsParkingTypeFacade.find(fbsParking.getFbsParkingType().getParkingTypeId());
        return fbsParkingType;
    }
}
