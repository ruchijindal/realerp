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
import org.smp.realerp.entity.FbsCompany;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.CompanyManager;
import org.smp.realerp.session.FbsProjectFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "projectBean")
@ViewScoped
public class ProjectBean {

    @EJB
    CompanyManager companyManager;
    @EJB
    FbsProjectFacade fbsProjectFacade;
    FbsProject fbsProject = new FbsProject();
    List<FbsProject> fbsProjectList = new ArrayList<FbsProject>();
    FbsProject editFbsProject = new FbsProject();
    FbsProject delfbsProject = new FbsProject();
    FbsCompany fbsCompany = new FbsCompany();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsProjectList.clear();
        fbsCompany = new FbsCompany();
        fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
        fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();

    }

    public void addProject() throws IOException {
        if (fbsCompany.getMaxProjects() > fbsCompany.getFbsProjectCollection().size()) {
            boolean flag = companyManager.checkProjectName(fbsProject.getProjName());
            if (flag == true) {
                System.out.println("companyid===>" + LoginBean.fbsLogin.getFbsCompany().getCompanyId());
                fbsProject.setFbsCompany(LoginBean.fbsLogin.getFbsCompany());
                companyManager.addProject(fbsProject);
                fbsProject = new FbsProject();
                populate();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Added", ""));
                fbsCompany = new FbsCompany();
                fbsCompany = companyManager.populateFbsCompany(LoginBean.fbsLogin.getFbsCompany().getCompanyId());
                LoginBean.fbsProjectList = (List<FbsProject>) fbsCompany.getFbsProjectCollection();
                // FacesContext.getCurrentInstance().getExternalContext().redirect("RealErp/faces/jsfpages/common/bottom.xhtml");
            } else {
                System.out.println("inside else..........");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Project Name already exist", ""));
            }
        } else {
            System.out.println("inside else..........");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Permission  Denied", ""));
        }

    }

    public void edit(FbsProject fbsProject) {
        editFbsProject = new FbsProject();
        editFbsProject = fbsProject;
        renderDialog = true;
    }

    public void editProject() {
        System.out.println("inside edit");
        companyManager.editProject(editFbsProject);
        renderDialog = false;
        populate();
        LoginBean.fbsProjectList = fbsProjectFacade.findAll();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Updated", ""));

    }

    public boolean checkProjectForDeletion(FbsProject fbsProject) {
        if (fbsProject != null) {
            return companyManager.checkProjectForDeletion(fbsProject);
        } else {
            return false;
        }
    }

    public void confirmDeleteProject(FbsProject fbsProject) {
        delfbsProject = new FbsProject();
        delfbsProject = fbsProject;
    }

    public void deleteProject() {
        companyManager.deleteProject(delfbsProject);
        delfbsProject = new FbsProject();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Deleted", ""));
        populate();
    }

    public void handleFileUpload(FileUploadEvent event) throws FileNotFoundException, IOException {
        DefaultUploadedFile inputFile = (DefaultUploadedFile) event.getFile();
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
        System.out.println(path);
        //path = path.replaceAll("build.*", "");
        path = path + "resources/documents/project_image/" + delfbsProject.getProjId() + ".png";
        File outputFile = new File(path);
        byte byteArray[] = inputFile.getContents();
        OutputStream fileOutputStream = new FileOutputStream(outputFile);
        fileOutputStream.write(byteArray);
        fileOutputStream.close();
        delfbsProject.setImagePath("resources/documents/project_image/" + delfbsProject.getProjId() + ".png");
        fbsProjectFacade.edit(delfbsProject);
        delfbsProject = new FbsProject();
        populate();
        FacesContext.getCurrentInstance().getExternalContext().redirect("/RealErp/faces/jsfpages/Company/setProject.xhtml");
    }

    public FbsProject getFbsProject() {
        return fbsProject;
    }

    public void setFbsProject(FbsProject fbsProject) {
        this.fbsProject = fbsProject;
    }

    public List<FbsProject> getFbsProjectList() {
        return fbsProjectList;
    }

    public void setFbsProjectList(List<FbsProject> fbsProjectList) {
        this.fbsProjectList = fbsProjectList;
    }

    public FbsProject getEditFbsProject() {
        return editFbsProject;
    }

    public void setEditFbsProject(FbsProject editFbsProject) {
        this.editFbsProject = editFbsProject;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }

    public FbsProject getDelfbsProject() {
        return delfbsProject;
    }

    public void setDelfbsProject(FbsProject delfbsProject) {
        this.delfbsProject = delfbsProject;
    }
}
