/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.smp.realerp.entity.FbsParking;
import org.smp.realerp.entity.FbsParkingType;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;
import org.smp.realerp.session.FbsParkingTypeFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "parkingBean")
@ViewScoped
public class ParkingBean {

    @EJB
    ProjectManager projectManager;
    @EJB
    FbsParkingTypeFacade fbsParkingTypeFacade;
    FbsParkingType fbsParkingType = new FbsParkingType();
    static FbsParkingType showFbsParkingType = new FbsParkingType();
    List<FbsParkingType> fbsParkingTypeList = new ArrayList<FbsParkingType>();
    FbsParkingType editFbsParkingType = new FbsParkingType();
    FbsParkingType previousFbsParkingType = new FbsParkingType();
    static FbsParkingType delFbsParkingType = new FbsParkingType();
    static List<FbsParking> fbsParkingList = new ArrayList<FbsParking>();
    FbsProject fbsProject = new FbsProject();
    boolean renderDelete = true;
    boolean renderDetail = true;
    boolean renderDialog;
    DecimalFormat format = new DecimalFormat("0.00");
    FbsParking deFbsParking = new FbsParking();

    @PostConstruct
    public void populate() {
        fbsParkingTypeList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        fbsParkingTypeList = (List<FbsParkingType>) fbsProject.getFbsParkingTypeCollection();
    }

    public void addParkingType() {
        fbsParkingType.setFbsProject(LoginBean.fbsProject);
        projectManager.addParking(fbsParkingType);
        fbsParkingType = new FbsParkingType();
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Added", ""));
    }

    public void parkingDetail(FbsParkingType fbsParkingType, String typeOfRequest) throws IOException {
        //System.out.println("inside parkingdetail........." + fbsParkingType.getParkingTypeId());
        showFbsParkingType = new FbsParkingType();
        showFbsParkingType = fbsParkingTypeFacade.find(fbsParkingType.getParkingTypeId());
        fbsParkingList.clear();
        fbsParkingList = (List<FbsParking>) showFbsParkingType.getFbsParkingCollection();
        fbsParkingList = projectManager.sortParkingList(fbsParkingList);
        if (typeOfRequest.equalsIgnoreCase("detail")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/ProjectSetting/parkingDetailList.xhtml");
        }
    }

    public void redIrectToProjectType() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/ProjectSetting/parkingTypeSetting.xhtml");
        populate();
    }

    public void edit(FbsParkingType fbsParkingType) {
        editFbsParkingType = new FbsParkingType();
        editFbsParkingType = fbsParkingType;
        renderDialog = true;
    }

    public boolean checkNoOfParking() {
        previousFbsParkingType = new FbsParkingType();
        previousFbsParkingType = fbsParkingTypeFacade.find(editFbsParkingType.getParkingTypeId());
        System.out.print("inside checkNoofparking");
        boolean checkParkingNum = false;
        System.out.print("editFbsParkingType.getNoOfParking()+++++++" + editFbsParkingType.getNoOfParking());
        System.out.print("previousFbsParkingType.getNoOfParking()+++++++" + previousFbsParkingType.getNoOfParking());
        if (editFbsParkingType.getNoOfParking() < previousFbsParkingType.getNoOfParking()) {
            System.out.print("inside checkNoofparking+++++++");
            checkParkingNum = true;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No of parking cannot be decreased", ""));
        }
        return checkParkingNum;

    }

    public void editParkingtype() {
        if (checkNoOfParking() == false) {
            projectManager.editParkingType(editFbsParkingType, previousFbsParkingType);
            renderDialog = false;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Type Updated", ""));
            populate();
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "No of parking cannot be decreased", ""));
        }

    }

    public void renderDeleteButton() {
        renderDelete = false;
        renderDetail = false;
    }

    public void confirmDeleteParkingType(FbsParkingType fbsParkingType) {
        delFbsParkingType = new FbsParkingType();
        delFbsParkingType = fbsParkingType;
    }

    public void deleteParkingType() {
        projectManager.deleteParkingType(delFbsParkingType);
        delFbsParkingType = new FbsParkingType();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Type Deleted", ""));
        populate();
    }

    public String roundOfUptoTwoDecimal(double value) {
        return format.format(value);
    }

    public void confirmDeleteParking(FbsParking fbsParking) {
        deFbsParking = new FbsParking();
        deFbsParking = fbsParking;
    }

    public void deleteParking() throws IOException {
        projectManager.deleteParking(deFbsParking);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Parking Deleted", ""));
        parkingDetail(deFbsParking.getFbsParkingType(), "delete");
        deFbsParking = new FbsParking();
    }

    public FbsParkingType getFbsParkingType() {
        return fbsParkingType;
    }

    public void setFbsParkingType(FbsParkingType fbsParkingType) {
        this.fbsParkingType = fbsParkingType;
    }

    public List<FbsParkingType> getFbsParkingTypeList() {
        return fbsParkingTypeList;
    }

    public void setFbsParkingTypeList(List<FbsParkingType> fbsParkingTypeList) {
        this.fbsParkingTypeList = fbsParkingTypeList;
    }

    public List<FbsParking> getFbsParkingList() {
        return fbsParkingList;
    }

    public void setFbsParkingList(List<FbsParking> fbsParkingList) {
        this.fbsParkingList = fbsParkingList;
    }

    public FbsParkingType getShowFbsParkingType() {
        return showFbsParkingType;
    }

    public void setShowFbsParkingType(FbsParkingType showFbsParkingType) {
        this.showFbsParkingType = showFbsParkingType;
    }

    public boolean isRenderDelete() {
        return renderDelete;
    }

    public void setRenderDelete(boolean renderDelete) {
        this.renderDelete = renderDelete;
    }

    public boolean isRenderDetail() {
        return renderDetail;
    }

    public void setRenderDetail(boolean renderDetail) {
        this.renderDetail = renderDetail;
    }

    public FbsParkingType getEditFbsParkingType() {
        return editFbsParkingType;
    }

    public void setEditFbsParkingType(FbsParkingType editFbsParkingType) {
        this.editFbsParkingType = editFbsParkingType;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
