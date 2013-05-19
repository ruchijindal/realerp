/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.*;
import java.util.ArrayList;
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
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.smp.realerp.entity.*;
import org.smp.realerp.manager.BlockManager;
import org.smp.realerp.manager.BookingManager;
import org.smp.realerp.session.FbsApplicantFacade;
import org.smp.realerp.session.FbsBlockFacade;
import org.smp.realerp.session.FbsBookingFacade;
import org.smp.realerp.session.FbsBrokerFacade;
import org.smp.realerp.session.FbsCompanyFacade;
import org.smp.realerp.session.FbsDiscountFacade;
import org.smp.realerp.session.FbsFlatFacade;
import org.smp.realerp.session.FbsFloorFacade;
import org.smp.realerp.session.FbsParkingFacade;
import org.smp.realerp.session.FbsParkingTypeFacade;
import org.smp.realerp.session.FbsPlannameFacade;
import org.smp.realerp.session.FbsPlcFacade;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "bookingBean")
@ViewScoped
public class BookingBean implements Serializable {

    @PersistenceContext(unitName = "RealErpPU")
    private EntityManager em;
    @EJB
    BookingManager bookingManager;
    @EJB
    FbsCompanyFacade fbsCompanyFacade;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    @EJB
    FbsBlockFacade fbsBlockFacade;
    @EJB
    FbsFloorFacade fbsFloorFacade;
    @EJB
    FbsFlatFacade fbsFlatFacade;
    @EJB
    FbsParkingTypeFacade fbsParkingTypeFacade;
    @EJB
    FbsParkingFacade fbsParkingFacade;
    @EJB
    FbsBrokerFacade fbsBrokerFacade;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    @EJB
    FbsPlannameFacade fbsPlannameFacade;
    @EJB
    FbsDiscountFacade fbsDiscountFacade;
    @EJB
    FbsBookingFacade fbsBookingFacade;
    @EJB
    FbsApplicantFacade fbsApplicantFacade;
    @EJB
    BlockManager blockManager;
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    List<FbsBlock> fbsBlockList = new ArrayList<FbsBlock>();
    List<FbsFloor> fbsFloorList = new ArrayList<FbsFloor>();
    List<FbsFlat> fbsFlatList = new ArrayList<FbsFlat>();
    List<FbsParkingType> fbsParkingTypeList = new ArrayList<FbsParkingType>();
    List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
    static List<FbsBroker> fbsBrokerList = new ArrayList<FbsBroker>();
    List<FbsPlanname> fbsPlannameList = new ArrayList<FbsPlanname>();
    List<FbsDiscount> fbsDiscountList = new ArrayList<FbsDiscount>();
    List<FbsPlc> fbsPlcList = new ArrayList<FbsPlc>();
    List<FbsCharge> fbsChargeList = new ArrayList<FbsCharge>();
    FbsCompany fbsCompany = new FbsCompany();
    FbsProject fbsProject = new FbsProject();
    FbsBlock fbsBlock = new FbsBlock();
    FbsFloor fbsFloor = new FbsFloor();
    FbsFlat fbsFlat = new FbsFlat();
    FbsFlat selectedfbsFlat = new FbsFlat();
    FbsFlatType fbsFlatType = new FbsFlatType();
    FbsParkingType fbsParkingType = new FbsParkingType();
    FbsBroker fbsBroker = new FbsBroker();
    FbsBroker selectedfbsBroker = new FbsBroker();
    FbsBooking fbsBooking = new FbsBooking();
    FbsPlanname fbsPlanname = new FbsPlanname();
    FbsPlanname selectedfbsPlanname = new FbsPlanname();
    FbsDiscount fbsDiscount = new FbsDiscount();
    FbsDiscount selectedfbsDiscount = new FbsDiscount();
    FbsApplicant fbsApplicant = new FbsApplicant();
    FbsApplicant fbsCoApplicant = new FbsApplicant();
    Integer parkingAllotArray[];
    boolean renderProject;
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    boolean renderFlat;
    String sessionId = "";

    public BookingBean() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession(false);
        sessionId = session.getId();
    }

    @PostConstruct
    public void populate() {
        fbsParkingTypeList.clear();
        fbsPlcList.clear();
        fbsChargeList.clear();
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsBrokerList.clear();
        fbsDiscountList.clear();
        fbsCompany = fbsCompanyFacade.find(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsBrokerList = (List<FbsBroker>) fbsCompany.getFbsBrokerCollection();
        fbsDiscountList = (List<FbsDiscount>) fbsCompany.getFbsDiscountCollection();
        fbsBooking.setBookingDt(new Date());
        if (request.getParameter("unitCode") == null) {
            renderFlat = false;
            if (LoginBean.fbsProject.getProjName().equals("Projects")) {
                renderProject = true;
                fbsProjectList.clear();
                fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
            } else {
                renderProject = false;
                fbsProject = new FbsProject();
                fbsProject = LoginBean.fbsProject;
                populateBlock();
            }
        } else {
            int unitCode = Integer.parseInt(request.getParameter("unitCode"));
            fbsFlat = new FbsFlat();
            fbsProject = new FbsProject();
            fbsBlock = new FbsBlock();
            fbsFloor = new FbsFloor();
            fbsFlat = fbsFlatFacade.find(unitCode);
            fbsProject = fbsFlat.getFbsFloor().getFbsBlock().getFbsProject();
            fbsBlock = fbsFlat.getFbsFloor().getFbsBlock();
            fbsFloor = fbsFlat.getFbsFloor();
            renderProject = false;

            populateBlock();
            populateFlatSpecification();
            renderFlat = true;

        }
        fbsProject.setProjCode("N/A");
        selectedfbsBroker.setBrokerId(0);
    }

    public void populateBlock() {
        fbsParkingTypeList.clear();
        fbsBlockList.clear();
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsPlannameList.clear();
        fbsPlcList.clear();
        fbsChargeList.clear();
        fbsFlatType = new FbsFlatType();
        if (fbsProject.getProjId() != 0) {
            fbsProject = fbsProjectFacade.find(fbsProject.getProjId());
            fbsBlockList = (List<FbsBlock>) fbsProject.getFbsBlockCollection();
            fbsParkingTypeList = (List<FbsParkingType>) fbsProject.getFbsParkingTypeCollection();
            parkingAllotArray = new Integer[fbsParkingTypeList.size()];
            for (int i = 0; i < fbsParkingTypeList.size(); i++) {
                fbsParkingTypeList.get(i).setNoOfParking(0);
                parkingAllotArray[i] = 0;
            }
            fbsPlannameList = (List<FbsPlanname>) fbsProject.getFbsPlannameCollection();
            fbsChargeList = (List<FbsCharge>) fbsProject.getFbsChargeCollection();
        }
    }

    public void populateFloor() {
        fbsFloorList.clear();
        fbsFlatList.clear();
        fbsPlcList.clear();
        fbsFlatType = new FbsFlatType();
        if (fbsBlock.getBlockId() != 0) {
            fbsBlock = fbsBlockFacade.find(fbsBlock.getBlockId());
            fbsFloorList = (List<FbsFloor>) fbsBlock.getFbsFloorCollection();
        }
        fbsFloorList = blockManager.sortFloorList(fbsFloorList);
    }

    public void populateFlat() {
        fbsFlatList.clear();
        fbsPlcList.clear();
        fbsFlatType = new FbsFlatType();
        if (fbsFloor.getFloorId() != 0) {
            fbsFloor = fbsFloorFacade.find(fbsFloor.getFloorId());
            //@NamedQuery(name = "FbsFlat.findByFloor&Locked&Unbooked", query = "SELECT f FROM FbsFlat f where f.fbsFloor = :fbsFloor and f.lockStatus = :lockStatus and f.status = :status"),
            fbsFlatList = em.createNamedQuery("FbsFlat.findByFloor&Locked&Unbooked").setParameter("fbsFloor", fbsFloor).setParameter("lockStatus", "locked").setParameter("status", "unbooked").getResultList();
        }
        fbsFlatList = blockManager.sortFlatList(fbsFlatList);
    }

    public void populateFlatSpecification() {
        System.out.println("populate flat specification+++++");
        fbsFlatType = new FbsFlatType();
        fbsPlcList.clear();
        if (fbsFlat.getUnitCode() != 0) {
            selectedfbsFlat = new FbsFlat();
            selectedfbsFlat = fbsFlatFacade.find(fbsFlat.getUnitCode());
            fbsFlatType = selectedfbsFlat.getFbsFlatType();
            System.out.println("++++++++" + fbsFlatType.getFlatSpecification());
            List<FbsPlcAllot> fbsPlcAllotList = new ArrayList<FbsPlcAllot>();
            fbsPlcAllotList = (List<FbsPlcAllot>) selectedfbsFlat.getFbsPlcAllotCollection();
            FbsPlc fbsPlc = new FbsPlc();
            for (int i = 0; i < fbsPlcAllotList.size(); i++) {
                fbsPlc = new FbsPlc();
                fbsPlc = fbsPlcFacade.find(fbsPlcAllotList.get(i).getFbsPlc().getPlcId());
                fbsPlcList.add(fbsPlc);
            }
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

    public void populatePlanname() {
        selectedfbsPlanname = new FbsPlanname();
        if (fbsPlanname.getPlanId() != null && fbsPlanname.getPlanId() != 0) {
            selectedfbsPlanname = fbsPlannameFacade.find(fbsPlanname.getPlanId());
        }
    }

    public void populateDiscount() {
        selectedfbsDiscount = new FbsDiscount();
        if (fbsDiscount.getDiscountId() != null && fbsDiscount.getDiscountId() != 0) {
            selectedfbsDiscount = fbsDiscountFacade.find(fbsDiscount.getDiscountId());
        }
    }

    public void populateBrokerDetails() {
        selectedfbsBroker = new FbsBroker();
        if (fbsBroker.getBrokerId() != null && fbsBroker.getBrokerId() != 0) {
            selectedfbsBroker = fbsBrokerFacade.find(fbsBroker.getBrokerId());
        }
    }

    public void applicantImageUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //path = path.replaceAll("build.*", "");
        path = path + "resources/documents/applicant_image/" + sessionId + ".png";
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        //delfbsProject.setImagePath("documents/project_image/" + delfbsProject.getProjId() + ".png");
        //fbsProjectFacade.edit(delfbsProject);
        //delfbsProject= new FbsProject();
    }

    public void coApplicantImageUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //path = path.replaceAll("build.*", "");
        path = path + "resources/documents/applicant_image/" + sessionId + ".co.png";
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        // delfbsProject.setImagePath("documents/project_image/" + delfbsProject.getProjId() + ".png");
        // fbsProjectFacade.edit(delfbsProject);
        //delfbsProject= new FbsProject();
    }

    public String flowListener(FlowEvent event) {
        boolean requiredFieldFlag = false;
        fbsApplicant.setApplicantName(fbsApplicant.getApplicantName());
        boolean flag = false;
        List<FbsParkingType> parkingTypeList = (List<FbsParkingType>) fbsProject.getFbsParkingTypeCollection();
        for (int i = 0; i < fbsParkingTypeList.size(); i++) {
            if (fbsParkingTypeList.get(i).getNoOfParking() > countAvailableParking(parkingTypeList.get(i))) {
                flag = true;
                break;
            }
        }
        if ((fbsProject.getProjId() == null || fbsProject.getProjId() == 0) || (fbsBlock.getBlockId() == null || fbsBlock.getBlockId() == 0) || (fbsFloor.getFloorId() == null || fbsFloor.getFloorId() == 0) || (fbsFlat.getUnitCode() == null || fbsFlat.getUnitCode() == 0) || (fbsDiscount.getDiscountId() == null || fbsDiscount.getDiscountId() == 0) || (fbsPlanname.getPlanId() == null || fbsPlanname.getPlanId() == 0) || (fbsBroker.getBrokerId() == null || fbsBroker.getBrokerId() == 0)) {
            requiredFieldFlag = true;

        }
        if (flag == false && requiredFieldFlag == false) {
            return event.getNewStep();
        } else {
            if (flag == true) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Parking Not Available", ""));
            } else if (requiredFieldFlag == true) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Fields marked with * are mendatory", ""));
            }

            return event.getOldStep();
        }
    }

    public void checkNoOfParking(FbsParkingType fbsParkingType) {
        if (fbsParkingType.getNoOfParking() > countAvailableParking(fbsParkingType)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error : Parking Not Available", ""));
        } else {
            parkingAllotArray[fbsParkingTypeList.indexOf(fbsParkingType)] = fbsParkingType.getNoOfParking();
        }

    }

    public void addBooking() throws IOException {
        try {
            System.out.println("inside addbooking");
            fbsBooking.setStatus("booked");
            System.out.println("fbsLogin.getUserId()++" + LoginBean.fbsLogin.getUserId());
            System.out.println("discount id+++ " + selectedfbsDiscount.getDiscountId());
            System.out.println("fbsBroker" + selectedfbsBroker.getBrokerId());
            System.out.println("fbsflat unitcode+" + selectedfbsFlat.getUnitCode());
            fbsBooking.setUserId(LoginBean.fbsLogin.getUserId());
            if (selectedfbsDiscount.getDiscountId() == null || selectedfbsDiscount.getDiscountId() == 0) {
                fbsBooking.setFbsDiscount(null);
            } else {
                fbsBooking.setFbsDiscount(selectedfbsDiscount);
            }
            if (selectedfbsPlanname.getPlanId() == null || selectedfbsPlanname.getPlanId() == 0) {
                fbsBooking.setFbsPlanname(null);
            } else {
                fbsBooking.setFbsPlanname(selectedfbsPlanname);
            }
            fbsBooking.setFbsBroker(selectedfbsBroker);
            fbsBooking.setFbsFlat(selectedfbsFlat);
            fbsBooking.setAuthorizeStatus("UA");
            fbsBookingFacade.create(fbsBooking);
            selectedfbsFlat.setStatus("booked");
            fbsFlatFacade.edit(selectedfbsFlat);
            fbsApplicant.setFbsFlat(selectedfbsFlat);
            fbsCoApplicant.setFbsFlat(selectedfbsFlat);
            fbsApplicant.setApplicantFlag(1);
            fbsApplicant.setStatus("booked");
            fbsCoApplicant.setApplicantFlag(2);
            fbsCoApplicant.setStatus("booked");
            fbsApplicantFacade.create(fbsApplicant);
            fbsApplicantFacade.create(fbsCoApplicant);
            bookingManager.allotParking(fbsParkingTypeList, parkingAllotArray, fbsBooking);
             FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + selectedfbsFlat.getUnitCode() + "&redirectFlag=bookingList&name=Booking List&url=/faces/jsfpages/Booking/bookingList.xhtml");

            //FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/faces/jsfpages/Booking/bookingDetail.xhtml?unitCode=" + selectedfbsFlat.getUnitCode() + "&redirectFlag=booking");
            fbsBooking = new FbsBooking();
            selectedfbsBroker = new FbsBroker();
            selectedfbsPlanname = new FbsPlanname();
            selectedfbsDiscount = new FbsDiscount();
            selectedfbsFlat = new FbsFlat();
            fbsApplicant = new FbsApplicant();
            fbsCoApplicant = new FbsApplicant();
            fbsFlatType = new FbsFlatType();
            fbsPlcList.clear();
            fbsChargeList.clear();
        } catch (Exception ex) {
            System.out.println("Exception in booking flat " + ex);
        }

    }

    public void setApplicant() {
        fbsApplicant = fbsApplicant;
        fbsCoApplicant = fbsCoApplicant;
    }

    public String convertFloorNo(int floorNo) {
        return blockManager.convertFloorNo(floorNo);
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public FbsBlock getFbsBlock() {
        return fbsBlock;
    }

    public void setFbsBlock(FbsBlock fbsBlock) {
        this.fbsBlock = fbsBlock;
    }

    public FbsFloor getFbsFloor() {
        return fbsFloor;
    }

    public void setFbsFloor(FbsFloor fbsFloor) {
        this.fbsFloor = fbsFloor;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
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

    public FbsFlat getFbsFlat() {
        return fbsFlat;
    }

    public void setFbsFlat(FbsFlat fbsFlat) {
        this.fbsFlat = fbsFlat;
    }

    public FbsFlatType getFbsFlatType() {
        return fbsFlatType;
    }

    public void setFbsFlatType(FbsFlatType fbsFlatType) {
        this.fbsFlatType = fbsFlatType;
    }

    public List<FbsParkingType> getFbsParkingTypeList() {
        return fbsParkingTypeList;
    }

    public void setFbsParkingTypeList(List<FbsParkingType> fbsParkingTypeList) {
        this.fbsParkingTypeList = fbsParkingTypeList;
    }

    public FbsParkingType getFbsParkingType() {
        return fbsParkingType;
    }

    public void setFbsParkingType(FbsParkingType fbsParkingType) {
        this.fbsParkingType = fbsParkingType;
    }

    public List<FbsBroker> getFbsBrokerList() {
        return fbsBrokerList;
    }

    public void setFbsBrokerList(List<FbsBroker> fbsBrokerList) {
        this.fbsBrokerList = fbsBrokerList;
    }

    public FbsBroker getFbsBroker() {
        return fbsBroker;
    }

    public void setFbsBroker(FbsBroker fbsBroker) {
        this.fbsBroker = fbsBroker;
    }

    public FbsBooking getFbsBooking() {
        return fbsBooking;
    }

    public void setFbsBooking(FbsBooking fbsBooking) {
        this.fbsBooking = fbsBooking;
    }

    public FbsPlanname getFbsPlanname() {
        return fbsPlanname;
    }

    public void setFbsPlanname(FbsPlanname fbsPlanname) {
        this.fbsPlanname = fbsPlanname;
    }

    public List<FbsPlanname> getFbsPlannameList() {
        return fbsPlannameList;
    }

    public void setFbsPlannameList(List<FbsPlanname> fbsPlannameList) {
        this.fbsPlannameList = fbsPlannameList;
    }

    public FbsDiscount getFbsDiscount() {
        return fbsDiscount;
    }

    public void setFbsDiscount(FbsDiscount fbsDiscount) {
        this.fbsDiscount = fbsDiscount;
    }

    public List<FbsDiscount> getFbsDiscountList() {
        return fbsDiscountList;
    }

    public void setFbsDiscountList(List<FbsDiscount> fbsDiscountList) {
        this.fbsDiscountList = fbsDiscountList;
    }

    public List<FbsCharge> getFbsChargeList() {
        return fbsChargeList;
    }

    public void setFbsChargeList(List<FbsCharge> fbsChargeList) {
        this.fbsChargeList = fbsChargeList;
    }

    public List<FbsPlc> getFbsPlcList() {
        return fbsPlcList;
    }

    public void setFbsPlcList(List<FbsPlc> fbsPlcList) {
        this.fbsPlcList = fbsPlcList;
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

    public FbsBroker getSelectedfbsBroker() {
        return selectedfbsBroker;
    }

    public void setSelectedfbsBroker(FbsBroker selectedfbsBroker) {
        this.selectedfbsBroker = selectedfbsBroker;
    }

    public FbsDiscount getSelectedfbsDiscount() {
        return selectedfbsDiscount;
    }

    public void setSelectedfbsDiscount(FbsDiscount selectedfbsDiscount) {
        this.selectedfbsDiscount = selectedfbsDiscount;
    }

    public FbsFlat getSelectedfbsFlat() {
        return selectedfbsFlat;
    }

    public void setSelectedfbsFlat(FbsFlat selectedfbsFlat) {
        this.selectedfbsFlat = selectedfbsFlat;
    }

    public FbsPlanname getSelectedfbsPlanname() {
        return selectedfbsPlanname;
    }

    public void setSelectedfbsPlanname(FbsPlanname selectedfbsPlanname) {
        this.selectedfbsPlanname = selectedfbsPlanname;
    }

    public boolean isRenderProject() {
        return renderProject;
    }

    public void setRenderProject(boolean renderProject) {
        this.renderProject = renderProject;
    }

    public boolean isRenderFlat() {
        return renderFlat;
    }

    public void setRenderFlat(boolean renderFlat) {
        this.renderFlat = renderFlat;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
