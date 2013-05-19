/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;
import org.smp.realerp.entity.FbsBlock;
import org.smp.realerp.entity.FbsBooking;
import org.smp.realerp.entity.FbsBroker;
import org.smp.realerp.entity.FbsFlat;
import org.smp.realerp.entity.FbsFloor;
import org.smp.realerp.entity.FbsPayment;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.BrokerManager;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsBrokerFacade;
import org.smp.realerp.session.FbsFlatFacade;
import org.smp.realerp.session.FbsPaymentFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author SMP Technologies
 */
@ManagedBean
@RequestScoped
public class ChartBean {

    /** Creates a new instance of ChartBean */
    @PersistenceContext(unitName = "RealErpPU")
    EntityManager em;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsPaymentFacade fbsPaymentFacade;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    BrokerManager brokerManager;
    final String BOOKED = "booked";
    final String UNBOOKED = "unbooked";
    final String PENDING = "Pending";
    final String CLEARED = "Cleared";
    private CartesianChartModel bookingModel, projectWiseCollection, topBroker, bookingDetailForBlock, collectionForProject;
    private PieChartModel companyCollectionModal;
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsProject> fbsProjects = new ArrayList<FbsProject>();
    List<FbsPayment> fbsPayments = new ArrayList<FbsPayment>();
    List<FbsBroker> fbsBrokers = new ArrayList<FbsBroker>();
    List<FbsBooking> fbsBookings = new ArrayList<FbsBooking>();
     String bookingChartOption="0",paymentChartOption="0";
     
     LoginBean loginBean;

    public ChartBean() {
    }

    @PostConstruct
    public void createBookingModel() {
        fbsPayments.clear();
        fbsBookings.clear();
        fbsProjects.clear();
        fbsFlatList = fbsFlatFacade.findAll();
        System.out.println(LoginBean.fbsLogin.getFbsCompany().getCompanyId()+"Company id is ");
        fbsProjects =(List<FbsProject>) LoginBean.fbsLogin.getFbsCompany().getFbsProjectCollection();//em.createNamedQuery("FbsProject.findByCompany").setParameter("fbsCompany",LoginBean.fbsLogin.getFbsCompany()).getResultList();//fbsProjectFacade.findAll();
        
        popBookingCollection();//fbsBookingFacade.findAll();
       // fbsPayments = fbsPaymentFacade.findAll();
      //  bookingChartOption="0";paymentChartOption="0";

        bookingChart();
        companyCollection();
        projectwiseCollection();
        topFiveBroker();
        bookingDetailForAllBlock();
        projectCollectionChart();
System.out.println("count");
    }
private void popBookingCollection(){
    fbsBookings.clear();
    for(FbsProject fbsProject:fbsProjects){
        List<FbsBlock> fbsBlocks=(List<FbsBlock>)fbsProject.getFbsBlockCollection();
        for(FbsBlock fbsBlock:fbsBlocks){
            List<FbsFloor> fbsFloors=(List<FbsFloor>)fbsBlock.getFbsFloorCollection();
              for(FbsFloor fbsFloor:fbsFloors){
                  List<FbsFlat> fbsFlats=(List<FbsFlat>) fbsFloor.getFbsFlatCollection();
                   for(FbsFlat fbsFlat:fbsFlats){
                       fbsBookings.addAll( fbsFlat.getFbsBookingCollection());
                   }
              }

        }
    }
    for(FbsBooking fbsBooking:fbsBookings){
        fbsPayments.addAll(fbsBooking.getFbsPaymentCollection());
    }
}
    private void topFiveBroker() {
        topBroker = new CartesianChartModel();
        ChartSeries commision = new ChartSeries();
        ChartSeries bookingUnits = new ChartSeries();

        long bookunit = 0, commisionAmount = 0;

        commision.setLabel("Commision");
        bookingUnits.setLabel("Book Units");

        fbsBrokers =(List<FbsBroker>)LoginBean.fbsLogin.getFbsCompany().getFbsBrokerCollection(); //fbsBrokerFacade.findAll();
        for (FbsBroker fbsBroker : fbsBrokers) {
            bookunit = 0;
            commisionAmount = 0;
            for (FbsBooking fbsBooking : fbsBookings) {
                try {
                    if (fbsBooking.getFbsBroker().getBrokerId().equals(fbsBroker.getBrokerId())) {
                        bookunit++;
                        commisionAmount = (long) brokerManager.calculateBrokerCommission(fbsBooking.getFbsFlat().getFbsFlatType(), fbsBooking.getFbsPlanname(), fbsBooking.getFbsDiscount(), fbsBroker.getFbsBrokerCat());
                    }
                } catch (Exception e) {
                    System.out.println("Broker Not defined for " + fbsBooking.getRegNumber());
                }
            }
            commision.set(fbsBroker.getBrName(), commisionAmount);
            bookingUnits.set(fbsBroker.getBrName(), bookunit);
        }
        topBroker.addSeries(commision);
        topBroker.addSeries(bookingUnits);



    }

    private void projectwiseCollection() {
        projectWiseCollection = new CartesianChartModel();

        ChartSeries pendingAmount = new ChartSeries();
        ChartSeries clearedAmount = new ChartSeries();
        pendingAmount.setLabel("Pending");
        clearedAmount.setLabel("Clear");

        long countProjectPending = 0, countProjectCleared = 0;

        for (FbsProject fbsProject : fbsProjects) {
            countProjectCleared = 0;
            countProjectPending = 0;

            for (FbsPayment fbsPayment : fbsPayments) {

                if (fbsPayment.getStatus().equalsIgnoreCase(PENDING) && fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())) {
                    countProjectPending += fbsPayment.getPaidAmount();
                } else if (fbsPayment.getStatus().equalsIgnoreCase(CLEARED) && fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())) {
                    countProjectCleared += fbsPayment.getPaidAmount();
                }
            }

            pendingAmount.set(fbsProject.getProjAbvr(), countProjectPending);
            clearedAmount.set(fbsProject.getProjAbvr(), countProjectCleared);
        }
        projectWiseCollection.addSeries(clearedAmount);
        projectWiseCollection.addSeries(pendingAmount);

    }

    private void companyCollection() {
        companyCollectionModal = new PieChartModel();



        long countPending = 0, countCleared = 0;


        for (FbsPayment fbsPayment : fbsPayments) {
            if (fbsPayment.getStatus().equalsIgnoreCase(PENDING)) {
                countPending += fbsPayment.getPaidAmount();
            } else if (fbsPayment.getStatus().equalsIgnoreCase(CLEARED)) {
                countCleared += fbsPayment.getPaidAmount();
            }


        }
        companyCollectionModal.set(PENDING, countPending);
        companyCollectionModal.set(CLEARED, countCleared);


    }

    private void bookingChart() {
        bookingModel = new CartesianChartModel();


        ChartSeries booked = new ChartSeries();
        booked.setLabel("Booked");
        ChartSeries unBooked = new ChartSeries();
        unBooked.setLabel("UnBooked");

        int countBooked = 0, countUnBooked = 0;
        for (FbsProject fbsProject : fbsProjects) {
            countBooked = 0;
            countUnBooked = 0;
            for (FbsFlat fbsFlat : fbsFlatList) {
                // System.out.println(fbsProject.getProjId() + " flat " + fbsFlat.getFbsFloor().getFbsBlock().getFbsProject().getProjId() + "   " + (fbsFlat.getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())));
                if (fbsFlat.getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())) {

                    if (fbsFlat.getStatus().equalsIgnoreCase(BOOKED)) {
                        countBooked++;
                    } else if (fbsFlat.getStatus().equalsIgnoreCase(UNBOOKED)) {
                        countUnBooked++;
                    }
                }

            }
            //   System.out.println("booked " + countBooked + "   unbooked " + countUnBooked);
            booked.set(fbsProject.getProjAbvr(), countBooked);
            unBooked.set(fbsProject.getProjAbvr(), countUnBooked);
        }

        bookingModel.addSeries(booked);
        bookingModel.addSeries(unBooked);
    }

    public CartesianChartModel getBookingModel() {
        return bookingModel;
    }

    public void bookingDetailForAllBlock() {

        FbsProject project = LoginBean.fbsProject;
        bookingDetailForBlock = new CartesianChartModel();
 System.out.println("Booking Chart Option "+bookingChartOption);

        ChartSeries booked = new ChartSeries();
        booked.setLabel("Booked");
        ChartSeries canceled = new ChartSeries();
        canceled.setLabel("Canceled");
        ChartSeries transfer = new ChartSeries();
        transfer.setLabel("Transfer");



        Collection<FbsBlock> fbsBlockCollection = project.getFbsBlockCollection();
        int countBook = 0, countTransfer = 0, countCancel = 0;


        try {
            if (project != null) {

                for (FbsBlock fbsBlock : fbsBlockCollection) {
                    countBook = 0;
                    countCancel = 0;
                    countTransfer = 0;
                    for (FbsBooking fbsBooking : fbsBookings) {
                        if (fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(project.getProjId())) {
                            if (fbsBlock.getBlockId().equals(fbsBooking.getFbsFlat().getFbsFloor().getFbsBlock().getBlockId())) {
                                if (fbsBooking.getStatus().equalsIgnoreCase("booked")) {
                                    countBook++;
                                } else if (fbsBooking.getStatus().equalsIgnoreCase("cancelled")) {
                                    countCancel++;
                                } else if (fbsBooking.getStatus().equalsIgnoreCase("transfer")) {

                                    countTransfer++;
                                }
                            }
                        }
                    }

                    booked.set(fbsBlock.getBlockAbvr(), countBook);
                    transfer.set(fbsBlock.getBlockAbvr(), countTransfer);
                    canceled.set(fbsBlock.getBlockAbvr(), countCancel);

                }

            }
        } catch (Exception e) {
            System.out.println("Exception in Block Chart");
        }
        bookingDetailForBlock.addSeries(booked);
        bookingDetailForBlock.addSeries(transfer);
        bookingDetailForBlock.addSeries(canceled);
    }

    public void projectCollectionChart() {
        collectionForProject = new CartesianChartModel();
        FbsProject fbsProject = LoginBean.fbsProject;
        ChartSeries pendingAmount = new ChartSeries();
        ChartSeries clearedAmount = new ChartSeries();
        pendingAmount.setLabel("Pending");
        clearedAmount.setLabel("Clear");
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        long countProjectPending = 0, countProjectCleared = 0;
        System.out.println("collection Chart modul start up");
        try {
            if (fbsProject != null) {
               // System.out.println("Payment List Size " + fbsPayments.size());
                for (int i = 0; i < days.length; i++) {
                    countProjectPending = 0; countProjectCleared = 0;
                    for (FbsPayment fbsPayment : fbsPayments) {
                     //   System.out.println(fbsPayment.getPaymentDate() + " Date Difference " + differenceBetweenDateInDays(fbsPayment.getPaymentDate(), new Date()));
                        if (differenceBetweenDateInDays(fbsPayment.getPaymentDate(), new Date()) == i) {
                            
                            if (fbsPayment.getStatus().equalsIgnoreCase(PENDING) && fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())) {
                                countProjectPending += fbsPayment.getPaidAmount();
                            } else if (fbsPayment.getStatus().equalsIgnoreCase(CLEARED) && fbsPayment.getFbsBooking().getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId().equals(fbsProject.getProjId())) {
                                countProjectCleared += fbsPayment.getPaidAmount();
                            }
                        }
                        //System.out.println("count"+countProjectCleared+"   ");
                    }

                    System.out.println(calendar.getTime().toString()+" day "+(calendar.get(Calendar.DAY_OF_WEEK)-i-1)+"  cal  "+calendar.get(Calendar.DAY_OF_WEEK));
//
                    if((calendar.get(Calendar.DAY_OF_WEEK)-i-1)<0)
                    {

                    pendingAmount.set(days[calendar.get(Calendar.DAY_OF_WEEK)-i-1+7], countProjectPending);
                    clearedAmount.set(days[calendar.get(Calendar.DAY_OF_WEEK)-i-1+7], countProjectCleared);
                    }else{
                        pendingAmount.set(days[calendar.get(Calendar.DAY_OF_WEEK)-i-1], countProjectPending);
                    clearedAmount.set(days[calendar.get(Calendar.DAY_OF_WEEK)-i-1], countProjectCleared);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Login Bean Project is null");
        }
System.out.println("collection Chart Cllosed");
        collectionForProject.addSeries(clearedAmount);
        collectionForProject.addSeries(pendingAmount);




    }
    public void yahoo(){
        
        System.out.println("Callleddddddddddddddddddddddddddddddddddddddddddddddd "+bookingChartOption);
    }

    private long differenceBetweenDateInDays(Date firstDate, Date secondDate) {
        long days = 0;
        if(firstDate.before(secondDate)){
        long firstmillisecond = firstDate.getTime();
        long secmillisecond = secondDate.getTime();
        long diff = secmillisecond - firstmillisecond;
        days = diff / (24 * 60 * 60 * 1000);
        }else{
            days=999999999;
        }
        return days;
    }

    public void setBookingModel(CartesianChartModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    public PieChartModel getCompanyCollectionModal() {
        return companyCollectionModal;
    }

    public CartesianChartModel getProjectWiseCollection() {
        return projectWiseCollection;
    }

    public CartesianChartModel getTopBroker() {
        return topBroker;
    }

    public CartesianChartModel getBookingDetailForBlock() {
        return bookingDetailForBlock;
    }

    public CartesianChartModel getCollectionForProject() {
        return collectionForProject;
    }

    public String getBookingChartOption() {
        return bookingChartOption;
    }

    public void setBookingChartOption(String bookingChartOption) {
        System.out.println("Value   "+bookingChartOption);
        this.bookingChartOption = bookingChartOption;
    }

    public String getPaymentChartOption() {
        return paymentChartOption;
    }

    public void setPaymentChartOption(String paymentChartOption) {
        this.paymentChartOption = paymentChartOption;
    }

     

}
