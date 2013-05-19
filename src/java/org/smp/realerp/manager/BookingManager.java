/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.manager;

import java.io.IOException;
import java.util.*;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import org.smp.realerp.entity.*;
import org.smp.realerp.session.FbsApplicantFacade;
import org.smp.realerp.session.FbsBlockFacade;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsFlatFacade;
import org.smp.realerp.session.FbsParkingFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@Stateless
@LocalBean
public class BookingManager {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsParkingFacade fbsParkingFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();

    public List<FbsProject> populateProjectList() {
        return fbsProjectFacade.findAll();
    }

    public FbsProject populateProject(int projId) {
        return fbsProjectFacade.find(projId);
    }

    public FbsBlock populateBlock(int blockId) {
        return fbsBlockFacade.find(blockId);
    }

    public void allotParking(List<FbsParkingType> fbsParkingTypeList, Integer parkingAllotArray[], FbsBooking fbsBooking) {
        FbsParkingType fbsParkingType;
        for (int i = 0; i < parkingAllotArray.length; i++) {
            fbsParkingType = new FbsParkingType();
            fbsParkingType = fbsParkingTypeList.get(i);
            // @NamedQuery(name = "FbsParking.countAvailableParking", query = "SELECT f FROM FbsParking f WHERE f.fbsParkingType = :fbsParkingType AND f.fbsBooking IS  NULL")
            List<FbsParking> parkings = em.createNamedQuery("FbsParking.countAvailableParking").setParameter("fbsParkingType", fbsParkingType).getResultList();
            if (parkings.size() >= parkingAllotArray[i]) {
                for (int j = 0; j < parkingAllotArray[i]; j++) {
                    parkings.get(j).setFbsBooking(fbsBooking);
                    System.out.println("+++++++++++p" + parkings.get(j).getFbsBooking().getStatus());
                    fbsParkingFacade.edit(parkings.get(j));
                }
            }


        }
    }

    public FbsBooking populateFbsBooking(FbsFlat fbsFlat) {
        // @NamedQuery(name = "FbsBooking.findByFbsFlat&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsFlat = :fbsFlat AND f.status = :status"),
        List<FbsBooking> bookingList = em.createNamedQuery("FbsBooking.findByFbsFlat&Status").setParameter("fbsFlat", fbsFlat).setParameter("status", "booked").getResultList();
        if (bookingList.isEmpty()) {
            return null;
        } else {
            return bookingList.get(0);
        }
    }

    public FbsBooking populateFbsCancelledBooking(FbsFlat fbsFlat) {
        // @NamedQuery(name = "FbsBooking.findByFbsFlat&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsFlat = :fbsFlat AND f.status = :status"),
        List<FbsBooking> bookingList = em.createNamedQuery("FbsBooking.findByFbsFlat&Status").setParameter("fbsFlat", fbsFlat).setParameter("status", "cancelled").getResultList();
        if (bookingList.isEmpty()) {
            return null;
        } else {
            return bookingList.get(0);
        }
    }

    public List<FbsApplicant> populateApplicantList(FbsFlat fbsFlat) {
        // @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status"),
        List<FbsApplicant> fbsApplicantList = em.createNamedQuery("FbsApplicant.findByFbsFlat&Status").setParameter("fbsFlat", fbsFlat).setParameter("status", "booked").getResultList();
        return fbsApplicantList;
    }

    public List<FbsApplicant> populateCancelledApplicantList(FbsFlat fbsFlat) {
        // @NamedQuery(name = "FbsApplicant.findByFbsFlat&Status", query = "SELECT f FROM FbsApplicant f where f.fbsFlat= :fbsFlat AND f.status = :status"),
        List<FbsApplicant> fbsApplicantList = em.createNamedQuery("FbsApplicant.findByFbsFlat&Status").setParameter("fbsFlat", fbsFlat).setParameter("status", "cancelled").getResultList();
        return fbsApplicantList;
    }

    public List<FbsBooking> populateBookingListByApplicant(String applicantName) {
        fbsBookingList.clear();
        //@NamedQuery(name = "FbsApplicant.findByApplicant&Status&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.applicantName LIKE :applicantName AND f.status = :status AND f.applicantFlag = :applicantFlag"),
        List<FbsApplicant> fbsApplicantList = em.createNamedQuery("FbsApplicant.findByApplicant&Status&ApplicantFlag").setParameter("applicantName", "%" + applicantName + "%").setParameter("status", "booked").setParameter("applicantFlag", 1).getResultList();
        System.out.println("fbsapplicat==>" + fbsApplicantList.size());
        for (int i = 0; i < fbsApplicantList.size(); i++) {
            // @NamedQuery(name = "FbsBooking.findByFbsFlat&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsFlat = :fbsFlat AND f.status = :status"),
            List<FbsBooking> bookings = em.createNamedQuery("FbsBooking.findByFbsFlat&Status").setParameter("fbsFlat", fbsApplicantList.get(i).getFbsFlat()).setParameter("status", "booked").getResultList();
            fbsBookingList.addAll(bookings);
        }
        return fbsBookingList;

    }

    public List<FbsBooking> populateBookingListByDatet(Date fromDate, Date toDate) {
        fbsBookingList.clear();
        //@NamedQuery(name = "FbsBooking.findByDate", query = "SELECT f FROM FbsBooking f WHERE f.bookingDt >= :fromDate AND f.bookingDt <= :toDate")
        fbsBookingList = em.createNamedQuery("FbsBooking.findByDate&Status").setParameter("fromDate", fromDate).setParameter("toDate", toDate).setParameter("status", "booked").getResultList();
        return fbsBookingList;

    }

    public void cancelBooking(FbsBooking fbsBooking) {
        fbsBooking.setStatus("cancelled");
        fbsBookingFacade.edit(fbsBooking);
        fbsBooking.getFbsFlat().setStatus("unbooked");
        fbsFlatFacade.edit(fbsBooking.getFbsFlat());
        List<FbsApplicant> fbsApplicantList = populateApplicantList(fbsBooking.getFbsFlat());
        for (int i = 0; i < fbsApplicantList.size(); i++) {
            fbsApplicantList.get(i).setStatus("cancelled");
            fbsApplicantFacade.edit(fbsApplicantList.get(i));
        }
        List<FbsParking> fbsParkingList = (List<FbsParking>) fbsBooking.getFbsParkingCollection();
        for (int i = 0; i < fbsParkingList.size(); i++) {
            fbsParkingList.get(i).setFbsBooking(null);
            fbsParkingFacade.edit(fbsParkingList.get(i));
        }
    }

    public void transferBooking(FbsFlat fbsFlat, FbsApplicant fbsApplicant, FbsApplicant fbsCoApplicant) {
        List<FbsApplicant> fbsApplicants = populateApplicantList(fbsFlat);
        for (int i = 0; i < fbsApplicants.size(); i++) {
            fbsApplicants.get(i).setStatus("transfered");
            fbsApplicants.get(i).setTransferDate(new Date());
            fbsApplicantFacade.edit(fbsApplicants.get(i));
        }
        fbsApplicant.setFbsFlat(fbsFlat);
        fbsCoApplicant.setFbsFlat(fbsFlat);
        fbsApplicant.setApplicantFlag(1);
        fbsApplicant.setStatus("booked");
        fbsCoApplicant.setApplicantFlag(2);
        fbsCoApplicant.setStatus("booked");
        fbsApplicantFacade.create(fbsApplicant);
        fbsApplicantFacade.create(fbsCoApplicant);



    }

    public List<FbsBooking> sortBookingList(List<FbsBooking> fbsBookingList) {
        Collections.sort(fbsBookingList, new Comparator() {

            public int compare(Object o1, Object o2) {
                FbsBooking f1 = (FbsBooking) o1;
                FbsBooking f2 = (FbsBooking) o2;
                return f1.getRegNumber().compareTo(f2.getRegNumber());
            }
        });
        return fbsBookingList;
    }

    public void authorizeBooking(FbsBooking fbsBooking) {
        fbsBookingFacade.edit(fbsBooking);
    }

    public List<FbsBooking> findTransferedUnits() {
        //    @NamedQuery(name = "FbsApplicant.findByStatus&ApplicantFlag", query = "SELECT f FROM FbsApplicant f where f.status = :status AND f.applicantFlag = :applicantFlag"),
// @NamedQuery(name = "FbsBooking.findByFbsFlat&Status", query = "SELECT f FROM FbsBooking f WHERE f.fbsFlat = :fbsFlat AND f.status = :status"),
        List<FbsApplicant> fbsApplicantList = em.createNamedQuery("FbsApplicant.findByStatus&ApplicantFlag").setParameter("status", "transfered").setParameter("applicantFlag", 1).getResultList();
        List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
        for (int i = 0; i < fbsApplicantList.size(); i++) {
            List<FbsBooking> fbsBookings = em.createNamedQuery("FbsBooking.findByFbsFlat&Status").setParameter("fbsFlat", fbsApplicantList.get(i).getFbsFlat()).setParameter("status", "booked").getResultList();
            fbsBookingList.addAll(fbsBookings);
        }
        return fbsBookingList;

    }
}
