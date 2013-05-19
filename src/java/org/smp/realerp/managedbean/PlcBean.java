/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smp.realerp.managedbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.smp.realerp.entity.FbsPlc;
import org.smp.realerp.entity.FbsProject;
import org.smp.realerp.manager.ProjectManager;
import org.smp.realerp.session.FbsPlcFacade;

/**
 *
 * @author smp
 */
@ManagedBean(name = "plcBean")
@ViewScoped
public class PlcBean {

    @EJB
    ProjectManager projectManager;
    @EJB
    FbsPlcFacade fbsPlcFacade;
    FbsPlc fbsPlc = new FbsPlc();
    List<FbsPlc> fbsPlcList = new ArrayList<FbsPlc>();
    FbsPlc editFbsPlc = new FbsPlc();
    FbsPlc delFbsPlc = new FbsPlc();
    FbsProject fbsProject = new FbsProject();
    boolean renderDialog;

    @PostConstruct
    public void populate() {
        fbsPlcList.clear();
        fbsProject = new FbsProject();
        fbsProject = projectManager.populateFbsProject(LoginBean.fbsProject.getProjId());
        fbsPlcList = (List<FbsPlc>) fbsProject.getFbsPlcCollection();

    }

    public void addPlc() {
        boolean flag = projectManager.checkPlcName(fbsPlc.getPlcName());
        if (flag = true) {
            fbsPlc.setFbsProject(LoginBean.fbsProject);
            projectManager.addPlc(fbsPlc);
            fbsPlc = new FbsPlc();
            populate();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " PLC  Added", ""));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failure:", " PLC  name already exist"));
        }
    }

    public void edit(FbsPlc fbsPlc) {
        editFbsPlc = new FbsPlc();
        editFbsPlc = fbsPlc;
        renderDialog = true;
    }

    public void editPlc() {
        System.out.println("inside edit");
        projectManager.editPlc(editFbsPlc);
        renderDialog = false;
        populate();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, " PLC  Updated", ""));

    }

    public void confirmDeletePlc(FbsPlc fbsPlc) {
        delFbsPlc = new FbsPlc();
        delFbsPlc = fbsPlc;
    }

    public void deletePlc() {
        projectManager.deletePlc(delFbsPlc);
        delFbsPlc = new FbsPlc();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Plc Deleted:", ""));
        populate();

    }

    public FbsPlc getFbsPlc() {
        return fbsPlc;
    }

    public void setFbsPlc(FbsPlc fbsPlc) {
        this.fbsPlc = fbsPlc;
    }

    public List<FbsPlc> getFbsPlcList() {
        return fbsPlcList;
    }

    public void setFbsPlcList(List<FbsPlc> fbsPlcList) {
        this.fbsPlcList = fbsPlcList;
    }

    public FbsPlc getEditFbsPlc() {
        return editFbsPlc;
    }

    public void setEditFbsPlc(FbsPlc editFbsPlc) {
        this.editFbsPlc = editFbsPlc;
    }

    public boolean isRenderDialog() {
        return renderDialog;
    }

    public void setRenderDialog(boolean renderDialog) {
        this.renderDialog = renderDialog;
    }
}
