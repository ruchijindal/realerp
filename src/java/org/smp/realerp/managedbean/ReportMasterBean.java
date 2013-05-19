/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;
import org.smp.realerp.entity.FbsBlock;
import org.smp.realerp.entity.FbsBooking;
import org.smp.realerp.entity.FbsFlat;
import org.smp.realerp.entity.FbsFloor;
import org.smp.realerp.entity.FbsPayment;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.CalculationManager;
import org.smp.realerp.session.FbsBlockFacade;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsFlatFacade;
import org.smp.realerp.session.FbsFloorFacade;
import org.smp.realerp.session.FbsProjectFacade;
import org.smp.realerp.pojo.RerpUtil;

/**
 *
 * @author SMP Technologies
 */
@ManagedBean
@ViewScoped
public class ReportMasterBean {



    /** Creates a new instance of ReportMasterBean */
    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    CalculationManager calculationManager;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    BlockManager blockManager;
    @EJB
    RerpUtil utility;
    Date startDate, endDate;
    List<FbsPayment> fbsPaymentList = new ArrayList<FbsPayment>();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsBooking> fbsBookingList = new ArrayList<FbsBooking>();
    List<FbsBooking> bookingList = new ArrayList<FbsBooking>();
    FbsProject fbsProject = new FbsProject();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    DecimalFormat format = new DecimalFormat("0.00");
    DecimalFormat formatter = new DecimalFormat("##,###");
    FbsBooking fbsBooking;
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> consumerList = new ArrayList<Integer>();


    @PostConstruct
    void populate() {
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsProjectList.clear();
        fbsBlock = new FbsBlock();
        fbsFloor = new FbsFloor();
        fbsFlat = new FbsFlat();
        fbsProject = new FbsProject();
        fbsPaymentList.clear();
        fbsPaymentList = calculationManager.getPaymentListForCompany(LoginBean.fbsLogin.getFbsCompany());
        fbsProjectList = calculationManager.getProjectListCompanyWise(LoginBean.fbsLogin.getFbsCompany());
        bookingList = em.createNamedQuery("FbsBooking.findByStatus").setParameter("status", "booked").getResultList();
        System.out.println("Project List size is " + fbsProjectList.size());

    }

    public ReportMasterBean() {
    }

    public void populateBlock() {

        list.clear();

        int projectId;



        for (int i = 0; i < bookingList.size(); i++) {
            projectId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            if (fbsProject.getProjId() == projectId) {

                System.out.println("booking List" + bookingList.get(i).getRegNumber());

                list.add(bookingList.get(i).getRegNumber());
                System.out.println("list" + list);
            }

        }

        fbsBlockList = null;
        fbsFloorList.clear();
        fbsFlatList.clear();

        if (fbsProject.getProjId() != 0) {
            fbsProject = fbsProjectFacade.find(fbsProject.getProjId());
            fbsBlockList = (List<FbsBlock>) fbsProject.getFbsBlockCollection();
        }

    }

    public void populateFloor() {
        list.clear();

        int blockId;



        for (int i = 0; i < bookingList.size(); i++) {
            blockId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getBlockId();
            if (fbsBlock.getBlockId() == blockId) {

                System.out.println("booking List" + bookingList.get(i).getRegNumber());

                list.add(bookingList.get(i).getRegNumber());
                System.out.println("list" + list);
            }

        }

        fbsFloorList.clear();
        fbsFlatList.clear();


        if (fbsBlock.getBlockId() != 0) {
            fbsBlock = fbsBlockFacade.find(fbsBlock.getBlockId());
            fbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();
        }
        fbsFloorList = blockManager.sortFloorList(fbsFloorList);
    }

    public void populateFlat() {
        list.clear();

        int floorId;



        for (int i = 0; i < bookingList.size(); i++) {
            floorId = bookingList.get(i).getFbsFlat().getFbsFloor().getFloorId();
            if (fbsFloor.getFloorId() == floorId) {

                System.out.println("booking List" + bookingList.get(i).getRegNumber());

                list.add(bookingList.get(i).getRegNumber());
                System.out.println("list" + list);
            }

        }

        fbsFlatList.clear();

        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());

            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "booked").getResultList();
        }
        System.out.println("FbsFlat List " + fbsFlatList.size());

        fbsFlatList = blockManager.sortFlatList(fbsFlatList);
    }

    public void updateselectionList() {
        list.clear();
        try {
            fbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
            FbsBooking fbsBooking = calculationManager.getFbsBookingFromFbsFlatByStatus(fbsFlat, "booked");

            list.add(fbsBooking.getRegNumber());
        } catch (Exception e) {
            System.out.println("fbs flat not selected");
        }

        System.out.println("selected list" + list);
    }

    public void generateReportbetweenStartDateAndEndDate() {
        checkDate();
        fbsPaymentList.clear();

        fbsPaymentList = calculationManager.getPaymentListBetweenStartDateAndEndDateForCompany(startDate, endDate, LoginBean.fbsLogin.getFbsCompany());
        System.out.println("start Date " + startDate + " End Date " + endDate + "  list size " + fbsPaymentList.size() + "fbs company " + LoginBean.fbsLogin.getFbsCompany().getCompanyAbrv());


    }

    public void generateReportPDFBetweenStartDateAndEndDate() throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/collectionReport?startDate=" + dateFormat.format(startDate) + "&endDate=" + dateFormat.format(endDate));
        } catch (Exception e) {
            System.out.println("End date and/or strat date is null");
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/collectionReport?startDate=" + "&endDate=");
        }
    }

    public double totalPaymentCollectionForCompany() {
        return Math.round(calculationManager.calculateTotalPaidAmount(fbsPaymentList) * 100) / 100.0;
    }

    public double totalClearedPaymentCollectionForCompany() {
        return Math.round(calculationManager.calculateClearedPaidAmount(fbsPaymentList) * 100) / 100.0;
    }

    public double totalPendingPaymnetCollectionForCompany() {
        return Math.round((calculationManager.calculateTotalPaidAmount(fbsPaymentList) - calculationManager.calculateClearedPaidAmount(fbsPaymentList)) * 100) / 100.0;
    }

    public void genrateConsumerReportFromMasterReportPage() throws IOException {

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            //request.setAttribute("registrationList",list);
            HttpSession session = request.getSession(false);
            session.setAttribute("registrationList", list);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/ConsumerReport"+"&requestType=nonEmail");

        } else {
        }
        populate();

    }

    public void genrateDueLetterReportFromMasterReportPage() throws IOException {


        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
            }
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            //request.setAttribute("registrationList",list);
            HttpSession session = request.getSession(false);
            session.setAttribute("registrationList", list);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport"+"&requestType=nonEmail");

        } else {
        }
        populate();
    }

    public void genrateBulkDueLetterReportFromMasterReportPage() throws IOException {


        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        HttpSession httpSession = request.getSession(false);


        fbsBookingList.clear();
        int projectId;
        bookingList = em.createNamedQuery("FbsBooking.findByStatus").setParameter("status", "booked").getResultList();

        List<FbsBooking> bookingList = fbsBookingFacade.findAll();
        List<FbsBooking> fbsbookingList = new ArrayList<FbsBooking>();

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int i = 0; i < bookingList.size(); i++) {
            projectId = bookingList.get(i).getFbsFlat().getFbsFloor().getFbsBlock().getFbsProject().getProjId();
            if (fbsProject.getProjId() == projectId) {

                System.out.println("booking List" + bookingList.get(i).getRegNumber());

                list.add(bookingList.get(i).getRegNumber());
                System.out.println("list" + list);
            }
        }
        httpSession.setAttribute("list", list);
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/dueLetterReport"+"&requestType=nonEmail");
        populate();
    }
    public void sendToPreviousPage() throws IOException
    {
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession(false);
        System.out.println("Request Page In Bean "+session.getAttribute("reqPAGE"));
        FacesContext.getCurrentInstance().getExternalContext().redirect((String) session.getAttribute("reqPAGE"));
    }

    void clearObject() {
    }

    public String roundOfUptoTwoDecimal(Double value) {

        return utility.indianFormat(value);

    }

    public void checkDate() {
        Date tempDate = null;

        if ((startDate != null) && (endDate != null) && startDate.after(endDate)) {

            tempDate = startDate;
            startDate = endDate;
            endDate = tempDate;
        }
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<FbsPayment> getFbsPaymentList() {
        return fbsPaymentList;
    }

    public void setFbsPaymentList(List<FbsPayment> fbsPaymentList) {
        this.fbsPaymentList = fbsPaymentList;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public List<FbsBlock> getFbsBlockList() {
        return fbsBlockList;
    }

    public void setFbsBlockList(List<FbsBlock> fbsBlockList) {
        this.fbsBlockList = fbsBlockList;
    }

    public List<FbsFlat> getFbsFlatList() {
        return fbsFlatList;
    }

    public void setFbsFlatList(List<FbsFlat> fbsFlatList) {
        this.fbsFlatList = fbsFlatList;
    }

    public List<FbsFloor> getFbsFloorList() {
        return fbsFloorList;
    }

    public void setFbsFloorList(List<FbsFloor> fbsFloorList) {
        this.fbsFloorList = fbsFloorList;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock FbsBlock) {
        this.fbsBlock = FbsBlock;
    }

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat FbsFlat) {
        this.fbsFlat = FbsFlat;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

   

    }

