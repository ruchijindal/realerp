/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.smp.realerp.entity.FbsFlatType;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;

/**
 *
 * @author smp
 */
@ManagedBean(name = "flatTypeBean")
@ViewScoped
public class FlatTypeBean {

    @EJB
    ProjectManager projectManager;
    FbsFlatType fbsFlatType = new FbsFlatType();
    List<FbsFlatType> fbsFlatTypeList = new ArrayList<FbsFlatType>();
    FbsFlatType editFbsFlatType = new FbsFlatType();
    FbsFlatType delFbsFlatType = new FbsFlatType();
    FbsProject fbsProject = new FbsProject();
    FbsFlatType imageFbsFlatType = new FbsFlatType();
    String typeofImage;
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsFlatTypeList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        fbsFlatTypeList = (List<FbsFlatType>) fbsProject.getFbsFlatTypeCollection();
    }

    public void addFlatType() {
        boolean flag = projectManager.checkFlatType(fbsFlatType.getFlatType());
        if (flag = true) {
            fbsFlatType.setFbsProject(LoginBean.fbsProject);
            projectManager.addFlatType(fbsFlatType);
            fbsFlatType = new FbsFlatType();
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flat Type  Added", " "));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure:", "Flat Type already exit"));
        }
    }

    public void edit(FbsFlatType fbsFlatType) {
        editFbsFlatType = new FbsFlatType();
        editFbsFlatType = fbsFlatType;
        renderDialog = true;
    }

    public void editFlatType() {
        projectManager.editFlatType(editFbsFlatType);
        renderDialog = false;
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " Flat Type  Updated", ""));
    }

    public void confirmDeleteFlatType(FbsFlatType fbsFlatType) {
        delFbsFlatType = new FbsFlatType();
        delFbsFlatType = fbsFlatType;
    }

    public void deleteFlatType() {
        projectManager.deleteFlatType(delFbsFlatType);
        delFbsFlatType = new FbsFlatType();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Flat Type Deleted", ""));
        populate();
    }

    public void typeOfImage(FbsFlatType fbsFlatType, String type) {
        imageFbsFlatType = new FbsFlatType();
        imageFbsFlatType=fbsFlatType;
        typeofImage = type;

    }

    public void flatTypeImageUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        //path = path.replaceAll("build.*", "");
        path = path + "resources/documents/flat_image/" + imageFbsFlatType.getFlatTypeId() + "-"+typeofImage+".png";
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/ProjectSetting/flatTypeSettings.xhtml");
    }

    public FbsFlatType getFbsFlatType() {
        return fbsFlatType;
    }

    public void setFbsFlatType(FbsFlatType fbsFlatType) {
        this.fbsFlatType = fbsFlatType;
    }

    public List<FbsFlatType> getFbsFlatTypeList() {
        return fbsFlatTypeList;
    }

    public void setFbsFlatTypeList(List<FbsFlatType> fbsFlatTypeList) {
        this.fbsFlatTypeList = fbsFlatTypeList;
    }

    public FbsFlatType getEditFbsFlatType() {
        return editFbsFlatType;
    }

    public void setEditFbsFlatType(FbsFlatType editFbsFlatType) {
        this.editFbsFlatType = editFbsFlatType;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public FbsFlatType getImageFbsFlatType() {
        return imageFbsFlatType;
    }

    public void setImageFbsFlatType(FbsFlatType imageFbsFlatType) {
        this.imageFbsFlatType = imageFbsFlatType;
    }

    public String getTypeofImage() {
        return typeofImage;
    }

    public void setTypeofImage(String typeofImage) {
        this.typeofImage = typeofImage;
    }
    
    
}
